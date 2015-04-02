package com.fitech.gznx.action;

import java.io.IOException;
import java.util.List;

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

import com.cbrc.smis.action.ViewMChildReportAction;
import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.MCellFormuForm;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechUtil;
import com.fitech.gznx.form.AFTemplateForm;
import com.fitech.gznx.form.ReportCheckForm;
import com.fitech.gznx.po.AfValidateformula;
import com.fitech.gznx.service.ReportCheckView;

public class AFReportCheckViewAction extends Action {
	private static FitechException log = new FitechException(AFReportCheckViewAction.class);

    /** 
     * 已使用Hibernate 卞以刚 2011-12-28
     * 影响对象：MCellFormu MCellToFormu ValidateType AfValidateformula
     * Method execute
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException
	{
        
		
		HttpSession session = request.getSession();
        MessageResources resources = getResources(request);
        FitechMessages messages = new FitechMessages();
        //取得request范围内的请求参数，并存放在logInForm内
        List list = null;
        ReportCheckForm reportcheck = (ReportCheckForm) form;
        RequestUtils.populate(reportcheck, request);
        String templateId = null;
		String versionId = request.getParameter("versionId");
		 if(request.getParameter("templateId")!=null){
        	 templateId = request.getParameter("templateId");
         }else{
        	 templateId=request.getParameter("childRepId");
         }
		
         try{
        	 /** 报表选中标志 **/
     		String reportFlg = "0";
     		
     		if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
     			reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
     		}
     		if(reportFlg.equals("1")){
     			/**已使用hibernate 卞以刚 2011-12-22
     			 * 影响对象：MCellFormu MCellToFormu ValidateType*/
     			list = ReportCheckView.getBNJList(templateId,versionId); 
     		} else {
     			/**已使用hibernate 卞以刚 2011-12-22
     			 * 影响对象：AfValidateformula ValidateType**/
     			list = ReportCheckView.getList(templateId,versionId); 
     		}
             
         }catch (Exception e){
             log.printStackTrace(e);
             messages.add(resources.getMessage("log.select.fail"));      
         }
         //移除request或session范围内的属性
         FitechUtil.removeAttribute(mapping,request);
         
         if(messages.getMessages() != null && messages.getMessages().size() > 0)
           request.setAttribute(Config.MESSAGES,messages);

           request.setAttribute(Config.RECORDS,list); 
           request.setAttribute("reportStyle",Config.REPORT_STYLE_DD);

           MCellFormuForm mCellForumForm = new MCellFormuForm();
			mCellForumForm.setReportName(request.getParameter("templateName"));
			mCellForumForm.setChildRepId(templateId);
			mCellForumForm.setVersionId(versionId);
			mCellForumForm.setReportStyle(Config.REPORT_STYLE_DD);
			request.setAttribute("ObjForm", mCellForumForm);
			AFTemplateForm mChildReportForm =new AFTemplateForm();
			mChildReportForm.setTemplateId(templateId);
			mChildReportForm.setTemplateName(request.getParameter("templateName"));
			mChildReportForm.setVersionId(versionId);
			request.setAttribute("form", mChildReportForm);
   
        
		return mapping.findForward("check");
	}
	  /**
     * 取得查询条件url  
     * @param logInForm
     * @return
     */
	  public String getTerm(ReportCheckForm reportcheck)
	    {
	        String term="";
	        
	        String reportName = reportcheck.getFormulaName();
	              
	        if(reportName!=null)
	        {
	            term += (term.indexOf("?")>=0 ? "&" : "?");
	            term += "reportName="+reportName.toString();   
	        }
	        if(term.indexOf("?")>=0)
	            term = term.substring(term.indexOf("?")+1);
	            
	        // System.out.println("term"+term);
	        return term;
	        
	    }


}