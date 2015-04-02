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
 * 保存报表模板信息，将报表模板的相关信息入库
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
	   
	   if(!tmplFile.equals("")){ //根据报表的版本号，输入的编号已存在，操作终止
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
			   if(fitechPDF.save(mChildReportForm)==true){  //保存模板信息成功
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
			   }else{	//保存模板信息失败
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
	   }else{	//获取PDF模板文件存放在临时目录中的文件名失败
		   request.setAttribute("ReportTitle",request.getParameter("reportTitle"));
		   request.setAttribute("ReportCurUnit",request.getParameter("reportCurUnit"));
		   request.setAttribute("ReportVesion",mChildReportForm.getVersionId());
		   request.setAttribute("ReportStyle",mChildReportForm.getReportStyle());
		   request.setAttribute("TmpFileName",tmplFile);
		   messages.add(FitechResource.getMessage(locale,resources,"save.error","template.msg"));
	   }		   
	   
	   if(messages!=null && messages.getSize()>0) request.setAttribute(Config.MESSAGES,messages);
		
	   if(flag==true){	//保存报表模板信息成功，进入表内表间关系设定页面
		   return mapping.findForward("bjgxSet");
	   }else{	//保存报表模板信息失败，返回原页面
		   if(mChildReportForm.getReportStyle().compareTo(Config.REPORT_STYLE_DD)==0){ //点对点式
				return mapping.findForward("inputDD");
			}else if(mChildReportForm.getReportStyle().compareTo(Config.REPORT_STYLE_QD)==0){ //清单式
				return mapping.findForward("inputQD");
		    }else{
		    	return mapping.findForward(Config.FORWARD_SYS_ERROR_PAGE);   //系统错误页
		    }
		   //return mapping.getInputForward();
	   }
   }
}
