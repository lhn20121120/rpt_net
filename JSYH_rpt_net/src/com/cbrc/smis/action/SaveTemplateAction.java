package com.cbrc.smis.action;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.adapter.StrutsMChildReportDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.MCellFormuForm;
import com.cbrc.smis.form.MChildReportForm;
import com.cbrc.smis.util.FitechLog;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechPDF;
import com.cbrc.smis.util.FitechResource;
;
/**
 * ���汨��ģ����Ϣ��������ģ��������Ϣ���
 * 
 * @author rds
 * @date 2005-12-06
 */
public final class SaveTemplateAction extends Action {

   /**
    * Performs action.
    * @param mapping Action mapping.
    * @param form Action form.
    * @param request HTTP request.
    * @param response HTTP response.
    * @exception IOException if an input/output error occurs
    * @exception ServletException if a servlet exception occurs
    */
   public ActionForward execute(
      ActionMapping mapping,
      ActionForm form,
      HttpServletRequest request,
      HttpServletResponse response
   )
      throws IOException, ServletException {
	   Locale locale=getLocale(request);
	   MessageResources resources=getResources(request);
		
	   FitechMessages messages=new FitechMessages();
	   
	   MChildReportForm mChildReportForm=(MChildReportForm)form;
	   RequestUtils.populate(mChildReportForm,request);
	   
	   String tmplFile="";
	   tmplFile=mChildReportForm.getTmpFileName()==null?"":mChildReportForm.getTmpFileName();
	   
	   boolean flag=false;
	   
	   if(!tmplFile.equals("")){ //���ݱ���İ汾�ţ�����ı���Ѵ��ڣ�������ֹ
		   if(StrutsMChildReportDelegate.isChildReportExists(mChildReportForm.getChildRepId(),
				   mChildReportForm.getVersionId())==true){
			   messages.add(FitechResource.getMsg(locale,resources,"template.exists",
					   mChildReportForm.getChildRepId(),
					   mChildReportForm.getVersionId())
					   );
			   
			   request.setAttribute("ReportTitle",request.getParameter("reportTitle"));
			   request.setAttribute("ReportName",request.getParameter("reportTitle"));
			   request.setAttribute("ReportCurUnit",request.getParameter("reportCurUnit"));
			   request.setAttribute("ReportVesion",mChildReportForm.getVersionId());
			   request.setAttribute("ReportStyle",mChildReportForm.getReportStyle());
			   request.setAttribute("TmpFileName",tmplFile);
		   }else{
			   FitechPDF fitechPDF=new FitechPDF();
			   
			   String msgKey="";
			   if(fitechPDF.save(mChildReportForm)==true){  //����ģ����Ϣ�ɹ�
				   msgKey="save.success";
				   
				   MCellFormuForm mCellForumForm=new MCellFormuForm();
				   mCellForumForm.setReportName(mChildReportForm.getReportName());
				   mCellForumForm.setChildRepId(mChildReportForm.getChildRepId());
				   mCellForumForm.setVersionId(mChildReportForm.getVersionId());
				   mCellForumForm.setReportStyle(mChildReportForm.getReportStyle());
				   request.setAttribute("ReportName",request.getParameter("reportTitle"));
				   request.setAttribute("ObjForm",mCellForumForm);
				   
				   /*
				   request.setAttribute("ChildRepID",mChildReportForm.getChildRepId());
				   request.setAttribute("VersionID",mChildReportForm.getVersionId());*/
				   messages.add(FitechResource.getMessage(locale,resources,"save.success","template.msg"));
				   flag=true;
			   }else{	//����ģ����Ϣʧ��
				   request.setAttribute("ReportTitle",request.getParameter("reportTitle"));
				   request.setAttribute("ReportCurUnit",request.getParameter("reportCurUnit"));
				   request.setAttribute("ReportVesion",mChildReportForm.getVersionId());
				   request.setAttribute("ReportStyle",mChildReportForm.getReportStyle());
				   request.setAttribute("TmpFileName",tmplFile);
				   msgKey="save.fail";
				   messages.add(FitechResource.getMessage(locale,resources,msgKey,"template.msg"));
			   }
			   
			   FitechLog.writeLog(Config.LOG_OPERATION,
					   FitechResource.getMessage(locale,resources,msgKey,"template.msg"),
					   request);
		   }
	   }else{	//��ȡPDFģ���ļ��������ʱĿ¼�е��ļ���ʧ��
		   request.setAttribute("ReportTitle",request.getParameter("reportTitle"));
		   request.setAttribute("ReportCurUnit",request.getParameter("reportCurUnit"));
		   request.setAttribute("ReportVesion",mChildReportForm.getVersionId());
		   request.setAttribute("ReportStyle",mChildReportForm.getReportStyle());
		   request.setAttribute("TmpFileName",tmplFile);
		   messages.add(FitechResource.getMessage(locale,resources,"save.error","template.msg"));
	   }		   
	   
	   if(messages!=null && messages.getSize()>0) request.setAttribute(Config.MESSAGES,messages);
		
	   if(flag==true){	//���汨��ģ����Ϣ�ɹ���������ڱ���ϵ�趨ҳ��
		   return mapping.findForward("bjgxSet");
	   }else{	//���汨��ģ����Ϣʧ�ܣ�����ԭҳ��
		   if(mChildReportForm.getReportStyle().compareTo(Config.REPORT_STYLE_DD)==0){ //��Ե�ʽ
				return mapping.findForward("inputDD");
			}else if(mChildReportForm.getReportStyle().compareTo(Config.REPORT_STYLE_QD)==0){ //�嵥ʽ
				return mapping.findForward("inputQD");
		    }else{
		    	return mapping.findForward(Config.FORWARD_SYS_ERROR_PAGE);   //ϵͳ����ҳ
		    }
		   //return mapping.getInputForward();
	   }
   }
}
