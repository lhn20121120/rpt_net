package com.cbrc.smis.action;


import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.adapter.StrutsAbnormityChangeDelegate;
import com.cbrc.smis.adapter.StrutsColAbnormityChangeDelegate;
import com.cbrc.smis.adapter.StrutsDataValidateInfoDelegate;
import com.cbrc.smis.adapter.StrutsMChildReportDelegate;
import com.cbrc.smis.adapter.StrutsQDDataValidateInfoDelegate;
import com.cbrc.smis.adapter.StrutsReportInDelegate;
import com.cbrc.smis.form.ReportInParticularForm;
import com.cbrc.smis.util.FitechException;



public final class ViewReportInParticularAction extends Action{
	private FitechException log = new FitechException(ViewReportInParticularAction.class);
	   /**
	    * ����
	    * @param result ��ѯ���ر�־,����ɹ�����true,���򷵻�false
	    * @param ReportInParticularForm 
	    * @param request 
	    * @exception Exception ���쳣��׽���׳�
	    */
	 public ActionForward execute(
		      ActionMapping mapping,
		      ActionForm form,
		      HttpServletRequest request,
		      HttpServletResponse response
		   )
		      throws IOException, ServletException {
		 
		   List list=null;
		   List abnormityList=null;
		   ReportInParticularForm reportInParticularForm= (ReportInParticularForm)form;
		   RequestUtils.populate(reportInParticularForm, request);
		   
		   if(reportInParticularForm!=null ){
			   request.setAttribute("repName", reportInParticularForm.getRepName());
			   try{
				   //���ص�laterReportDay�ĳٱ�����
				   int laterReportDay=StrutsReportInDelegate.selectLaterReportDay(reportInParticularForm.getRepInId());
				   reportInParticularForm.setLaterReportDay(String.valueOf(laterReportDay));
				   
				   //���ݷ��صĲ���result�����Ϊ1���ǵ�Ե�ʽ�����Ϊ2�����嵥ʽ
				   int result=StrutsMChildReportDelegate.getReportStyle(reportInParticularForm.getChildRepId(), reportInParticularForm.getVersionId());
				   
				   if(result==1)
				   {
					   //���ص�Ե�ʽ�ı����ڵ�����У���ϵ���Ҽ�¼
					   list=StrutsDataValidateInfoDelegate.select_AllRecords(reportInParticularForm.getRepInId().toString());
					   //���ص�Ե�ʽ���쳣�仯�Ĳ��Ҽ�¼
					   abnormityList=StrutsAbnormityChangeDelegate.select(reportInParticularForm.getChildRepId(), reportInParticularForm.getVersionId());
					   //�����¼����Ϊ�ջ����0���򷵻ذ�����¼��request����
					   if(list!=null && list.size()>0)
					   {
						   request.setAttribute("p2p",list);  
					   }
					   //����쳣��¼��Ϊ�ջ����0���򷵻ذ�����¼��request����
					   if(abnormityList!=null&&abnormityList.size()>0){
						   request.setAttribute("abnormity", abnormityList);
					   }
					   //����ٱ��������ڵ���0���򷵻ذ�����¼��request����
					   if(laterReportDay>=0){
						   request.setAttribute("day", reportInParticularForm.getLaterReportDay());
					   }
				   }
				   else if(result==2)
				   {
					   //�����嵥ʽ�ı����ڵ�����У���ϵ���Ҽ�¼
					   list=StrutsQDDataValidateInfoDelegate.select_AllRecords(reportInParticularForm.getRepInId().toString());
					   //�����嵥ʽ���쳣�仯�Ĳ��Ҽ�¼
					   abnormityList=StrutsColAbnormityChangeDelegate.select(reportInParticularForm.getChildRepId(), reportInParticularForm.getVersionId());
					   //�����¼����Ϊ�ջ����0���򷵻ذ�����¼��request����
					   if(list!=null && list.size()>0)
					   {
						  request.setAttribute("p2p", list);
					   }
					   //����쳣��¼��Ϊ�ջ����0���򷵻ذ�����¼��request����
					   if (abnormityList!=null&&abnormityList.size()>0){
						   request.setAttribute("abnormity", abnormityList);
					   }
					   //����ٱ��������ڵ���0���򷵻ذ�����¼��request����
					   if(laterReportDay>=0){
						  request.setAttribute("day", reportInParticularForm.getLaterReportDay());
					   }
				   }
				 
			   }catch(Exception e){
				   log.printStackTrace(e);
			   }
		   }
		   return mapping.findForward("viewResult");
	   }
	   
	   
}
