package com.cbrc.org.action;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.adapter.StrutsMActuRepDelegate;
import com.cbrc.smis.adapter.StrutsMCellFormuDelegate;
import com.cbrc.smis.adapter.StrutsMCellToFormuDelegate;
import com.cbrc.smis.adapter.StrutsMChildReportDelegate;
import com.cbrc.smis.adapter.StrutsMRepRangeDelegate;
import com.cbrc.smis.adapter.StrutsReportDataDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.common.DateUtil;
import com.cbrc.smis.form.MActuRepForm;
import com.cbrc.smis.form.MCellFormuForm;
import com.cbrc.smis.form.MCellToFormuForm;
import com.cbrc.smis.form.MChildReportForm;
import com.cbrc.smis.form.MRepRangeForm;
import com.cbrc.smis.form.ReportDataForm;
import com.cbrc.smis.hibernate.MChildReport;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechLog;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
import com.cbrc.smis.util.FitechUtil;
import com.fitech.gznx.po.AfTemplate;
import com.fitech.gznx.service.AFTemplateDelegate;
import com.gather.adapter.StrutsJieKou;
import com.gather.struts.forms.Cell2FormForm;
 
/**
 * @author ����
 * 
 */
public final class InsertMChildReportAction extends Action{
	private FitechException log =new FitechException(InsertMChildReportAction.class);
	
	   /**
	    * ģ�淢��Action
	    * @param result �����Ƿ���ҳɹ�  
	    * @param mChildReportForm FormBean����
	    * @exception IOException �Ƿ���I/O�쳣
	    * request reqest����
	    */
	
	   public ActionForward execute(
	      ActionMapping mapping,
	      ActionForm form,
	      HttpServletRequest request,
	      HttpServletResponse response
	   )
	      throws IOException, ServletException {
		   Locale locale = getLocale(request);
		   MessageResources resources = getResources(request);

		   FitechMessages messages = new FitechMessages();
					  
		   MChildReportForm mChildReportForm=(MChildReportForm)form;
		   RequestUtils.populate(mChildReportForm,request);
		   
		   String curPage = request.getParameter("curPage") != null ? request.getParameter("curPage") : "";
		   
		   boolean result=false;
		   Operator operator = null;
		   HttpSession session = request.getSession();
	       if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
	            operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
		   try{
			   
			   /* ������־����,������־ */
			   if(mChildReportForm!=null){
				   String templateLog = mChildReportForm.getFormatTempId();
			       if(operator!=null) 
			    	   FitechLog.writeLog(Config.LOG_OPERATION,operator.getUserName(),templateLog);
	            	
			   }
			   
			   
			    //�Ƿ���Form������ڣ������򷵻�true������false
			   result=StrutsMChildReportDelegate.update(mChildReportForm);
				//�жϷ��صĲ����¼�Ƿ�ɹ����ɹ��򷵻�true�����򷵻�false������ʾģ�淢��ҳ��
			   result=AFTemplateDelegate.updateAFTemplateUsing( mChildReportForm.getChildRepId(),
					   mChildReportForm.getVersionId(),mChildReportForm.getStartDate(),mChildReportForm.getEndDate());
			  
			   
			   if(result==true){
				   //messages.add(FitechResource.getMessage(locale,resources,"bpfb.save.success"));
				   messages.add("�������ɹ�!");
				   MChildReport mChildReport=null;
				//   synchrocnizedGather(messages,locale,resources,mChildReport,mChildReportForm);
			   }else{
				   messages.add(FitechResource.getMessage(locale,resources,"bpfb.save.failed"));
			   }

		   }catch(Exception e){
			   log.printStackTrace(e);
			   messages.add(FitechResource.getMessage(locale,resources,"errors.system"));
		   }
		   try {
			   String startDate = mChildReportForm.getStartDate();
				Calendar cd = Calendar.getInstance();
				cd.setTime(new SimpleDateFormat(DateUtil.NORMALDATE).parse(startDate));
				cd.add(Calendar.DATE, -1);
				AfTemplate af = StrutsMChildReportDelegate.findMaxVersionReport(mChildReportForm.getChildRepId(),
						   mChildReportForm.getVersionId());
				if(af!=null){
					//af.setStartDate(new SimpleDateFormat(DateUtil.NORMALDATE).format(cd.getTime()));
					af.setEndDate(new SimpleDateFormat(DateUtil.NORMALDATE).format(cd.getTime()));
					StrutsMChildReportDelegate.update(af);
				}
				MChildReport mr = StrutsMChildReportDelegate.findMaxMchildReport(mChildReportForm.getChildRepId(),
						   mChildReportForm.getVersionId());
				if(mr!=null){
					//mr.setStartDate(new SimpleDateFormat(DateUtil.NORMALDATE).format(cd.getTime()));
					mr.setEndDate(new SimpleDateFormat(DateUtil.NORMALDATE).format(cd.getTime()));
					StrutsMChildReportDelegate.update(mr);
				}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   if(messages!=null && messages.getSize()>0) request.setAttribute(Config.MESSAGES,messages);
		   
		   String param=mapping.getParameter();  //���ô�
		   
		   if(result==true){ //�����ɹ�
			   if(Config.TEMPLATE_PUT.get(mChildReportForm.getChildRepId()+"_"+mChildReportForm.getVersionId())!=null){
				   Config.TEMPLATE_PUT.remove(mChildReportForm.getChildRepId()+"_"+mChildReportForm.getVersionId());				
			   }
			   if(mapping.findForward("view").getPath().equals("/template/viewMChildReport.do"))
				   return new ActionForward("/template/viewMChildReport.do?reportName=&versionId=&curPage="+curPage);
			   return mapping.findForward("view");//new ActionForward("/template/add/index.jsp");
		   }else{ //����ʧ��,����ԭҳ��
			   request.setAttribute("VersionId",mChildReportForm.getVersionId());
			   request.setAttribute("ChildRepId", mChildReportForm.getChildRepId());
			   request.setAttribute("ReportName", mChildReportForm.getReportName());
			   request.setAttribute("ReportStyle",mChildReportForm.getReportStyle());
			   return mapping.getInputForward();
		   }
	   }
	   
	   /**
	    * ģ�巢������ģ�����Ϣͬ��������
	    * 
	    * @return void
	    */
	   private void synchrocnizedGather(FitechMessages messages,
			   Locale locale,
			   MessageResources resources,
			   MChildReport mChildReport,
			   MChildReportForm mChildReportForm){
		   //try{
			   int i=0;
			   /**ģ�巢���󣬽���ǰ�汾�Ľ���ʱ����Ϊ��ǰģ����ʼ���ڵ�ǰһ��**/
			   /*if(mChildReport!=null && mChildReport.getComp_id()!=null && mChildReport.getComp_id().getChildRepId()!=null){
				   com.gather.struts.forms.MChildReportForm _mChildReportForm=new com.gather.struts.forms.MChildReportForm();
				   _mChildReportForm.setChildRepId(mChildReport.getComp_id().getChildRepId());
				   _mChildReportForm.setVersionId(mChildReport.getComp_id().getVersionId());
				   // System.out.println("_mChildReportForm.setChildRepId:" + _mChildReportForm.getChildRepId());
				   // System.out.println("_mChildReportForm.setVersionId:" + _mChildReportForm.getVersionId());
				   _mChildReportForm.setEndDate(StrutsMChildReportDelegate.getPreviousDate(mChildReportForm.getStartDate()));
				   if(StrutsJieKou.update(_mChildReportForm)==false){
					   messages.add(FitechResource.getMessage(locale,resources,"synchrocnized.template.failed"));
					   return;
				   }
			   }*/
			   
			   /**
			    * ��������ģ����Ϣͬ��������
			    */
			   // System.out.println("[InsertMChildReport]mChildReportForm.getChildRepId():" + mChildReportForm.getChildRepId());
			   // System.out.println("[InsertMChildReport]mChildReportForm.getVersionId():" + mChildReportForm.getVersionId());
			   MChildReport mcReport=StrutsMChildReportDelegate.getMChileReport(mChildReportForm.getChildRepId(),mChildReportForm.getVersionId());
			   /**ͬ������M_Main_Rep������**/
			   if(mcReport!=null){
				   com.gather.struts.forms.MMainRepForm mMainRepForm=new com.gather.struts.forms.MMainRepForm();
				   mMainRepForm.setRepId(mcReport.getMMainRep().getRepId());
				   mMainRepForm.setRepCnName(mcReport.getMMainRep().getRepCnName());
				   mMainRepForm.setRepEnName(mcReport.getMMainRep().getRepEnName());
				   mMainRepForm.setRepTypeId(mcReport.getMMainRep().getMRepType().getRepTypeId());
				   mMainRepForm.setCurUnit(mcReport.getMMainRep().getMCurUnit().getCurUnit());
				   // System.out.println("ͬ������M_Main_Rep������");
				   if(StrutsJieKou.create(mMainRepForm)==false){
					   messages.add(FitechResource.getMessage(locale,resources,"synchrocnized.template.failed"));
					   return;
				   }
			   }
			   
			   /**ͬ������M_Child_Report������**/
			   if(mcReport!=null){
				   com.gather.struts.forms.MChildReportForm _mChildReportForm=new com.gather.struts.forms.MChildReportForm();
				   _mChildReportForm.setChildRepId(mcReport.getComp_id().getChildRepId());
				   _mChildReportForm.setVersionId(mcReport.getComp_id().getVersionId());
				   _mChildReportForm.setCurUnit(mcReport.getMCurUnit().getCurUnit());
				   _mChildReportForm.setEndDate(FitechUtil.parseDate(mcReport.getEndDate()));
				   _mChildReportForm.setStartDate(FitechUtil.parseDate(mcReport.getStartDate()));
				   _mChildReportForm.setFormatTempId(mcReport.getFormatTempId());
				   _mChildReportForm.setRepId(mcReport.getMMainRep().getRepId());
				   _mChildReportForm.setReportName(mcReport.getReportName());
				   // System.out.println("ͬ������M_Child_Report������");
				   if(StrutsJieKou.create(_mChildReportForm)==false){
					   messages.add(FitechResource.getMessage(locale,resources,"synchrocnized.template.failed"));
					   return;
				   }
			   }
			   
			   /**ͬ������M_Rep_Range������**/
			   List mRepRangeList=StrutsMRepRangeDelegate.findAll(mChildReportForm.getChildRepId(),
					   mChildReportForm.getVersionId());
			   // System.out.println("ͬ������M_Rep_Range������");
			   if(mRepRangeList!=null && mRepRangeList.size()>0){
				   com.gather.struts.forms.MRepRangeForm _mRepRangeForm=null;
				   MRepRangeForm mRepRangeForm=null;
				   for(i=0;i<mRepRangeList.size();i++){
					   _mRepRangeForm=new com.gather.struts.forms.MRepRangeForm();
					   mRepRangeForm=(MRepRangeForm)mRepRangeList.get(i);
					   _mRepRangeForm.setChildRepId(mRepRangeForm.getChildRepId());
					   _mRepRangeForm.setVersionId(mRepRangeForm.getVersionId());
					   _mRepRangeForm.setOrgId(mRepRangeForm.getOrgId().trim());
					   if(StrutsJieKou.create(_mRepRangeForm)==false){
						   messages.add(FitechResource.getMessage(locale,resources,"synchrocnized.template.failed"));
						   return;
					   }
				   }
			   }
			   
			   //**ͬ��������M_Actu_Rep������**//*
			   List mActuRepList=StrutsMActuRepDelegate.getMActuRep(mChildReportForm.getChildRepId(),
					   mChildReportForm.getVersionId());
			   // System.out.println("ͬ��������M_Actu_Rep������");
			   if(mActuRepList!=null && mActuRepList.size()>0){
				   com.gather.struts.forms.MActuRepForm _mActuRepForm=null;
				   MActuRepForm mActuRepForm=null;
				   for(i=0;i<mActuRepList.size();i++){
					   _mActuRepForm=new com.gather.struts.forms.MActuRepForm();
					   mActuRepForm=(MActuRepForm)mActuRepList.get(i);
					   _mActuRepForm.setChildRepId(mActuRepForm.getChildRepId());
					   _mActuRepForm.setVersionId(mActuRepForm.getVersionId());
					   _mActuRepForm.setDataRangeId(mActuRepForm.getDataRangeId());
					   _mActuRepForm.setDelayTime(mActuRepForm.getDelayTime());
					   _mActuRepForm.setNormalTime(mActuRepForm.getNormalTime());
					   _mActuRepForm.setRepFreqId(mActuRepForm.getRepFreqId());
					   _mActuRepForm.setOrgType(mActuRepForm.getOATId());
					   // System.out.println("childRepId:" + _mActuRepForm.getChildRepId());
					   // System.out.println("versionId:" + _mActuRepForm.getVersionId());
					   // System.out.println("DataRangeId:" + _mActuRepForm.getDataRangeId());
					   // System.out.println("RepFreqId:" + _mActuRepForm.getRepFreqId());
					   // System.out.println("OrgTypeId:" + _mActuRepForm.getOrgType());
					   if(StrutsJieKou.create(_mActuRepForm)==false){
						   messages.add(FitechResource.getMessage(locale,resources,"synchrocnized.template.failed"));
						   return;
					   }
				   }
			   }
			   
			   //**ͬ��������Report_Data������**//*
			   ReportDataForm reportDataForm=StrutsReportDataDelegate.getReportData(
					   mChildReportForm.getChildRepId(),
					   mChildReportForm.getVersionId());
			   // System.out.println("ͬ��������Report_Data������");
			   if(reportDataForm!=null){
				   com.gather.struts.forms.ReportDataForm _reportDataForm=new com.gather.struts.forms.ReportDataForm();
				   _reportDataForm.setChildRepId(reportDataForm.getChildRepId());
				   _reportDataForm.setVersionId(reportDataForm.getVersionId());
				   _reportDataForm.setPdf(reportDataForm.getPdf());
				   _reportDataForm.setXml(reportDataForm.getXml());
				   _reportDataForm.setNote(reportDataForm.getNote());
				   if(StrutsJieKou.create(_reportDataForm)==false){
					   messages.add(FitechResource.getMessage(locale,resources,"synchrocnized.template.failed"));
					   return;
				   }
			   }
			   
			   // System.out.println("ͬ������M_Cell_Formu������");
			   List listFormu=StrutsMCellFormuDelegate.findAll(
					   mChildReportForm.getChildRepId(),
					   mChildReportForm.getVersionId());
			   
			   if(listFormu!=null && listFormu.size()>0){
				   MCellFormuForm _mCellFormuForm=null;
				   for(i=0;i<listFormu.size();i++){
					   _mCellFormuForm=(MCellFormuForm)listFormu.get(i);
					   com.gather.struts.forms.MCellFormForm _mfForm=new com.gather.struts.forms.MCellFormForm();
					   _mfForm.setCellFormId(_mCellFormuForm.getCellFormuId());
					   _mfForm.setCellForm(_mCellFormuForm.getCellFormu());
					   _mfForm.setFormType(_mCellFormuForm.getFormuType());
					   if(StrutsJieKou.create(_mfForm)==false){
						   messages.add(FitechResource.getMessage(locale,resources,"synchrocnized.template.failed"));
						   return;
					   }
				   }
			   }
			   
			   // System.out.println("ͬ������M_Cell_To_Formu������");
			   List listCellToFormu=StrutsMCellToFormuDelegate.findAll(
					   mChildReportForm.getChildRepId(),
					   mChildReportForm.getVersionId());
			   
			   if(listCellToFormu!=null && listCellToFormu.size()>0){
				   MCellToFormuForm _mCellToFormuForm=null;
				   for(i=0;i<listFormu.size();i++){
					   _mCellToFormuForm=(MCellToFormuForm)listCellToFormu.get(i);
					   Cell2FormForm _mfForm=new Cell2FormForm();
					   _mfForm.setCell2FormId(_mCellToFormuForm.getCellToFormuId());
					   _mfForm.setCellFormId(_mCellToFormuForm.getCellFormuId());
					   _mfForm.setChildRepId(_mCellToFormuForm.getChildRepId());
					   _mfForm.setVersionId(_mCellToFormuForm.getVersionId());
					   if(StrutsJieKou.create(_mfForm)==false){
						   messages.add(FitechResource.getMessage(locale,resources,"synchrocnized.template.failed"));
						   return;
					   }
				   }
			   }
		 
		   //}
//	      catch(Exception e){
//			   messages.add(FitechResource.getMessage(locale,resources,"synchrocnized.template.failed"));
//		   }
		 
	   }
	   
}
				   
			 
		   
		   
		   

	   

