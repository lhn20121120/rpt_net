package com.fitech.gznx.action;

import java.io.IOException;
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

import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.MCellFormuForm;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
import com.fitech.gznx.form.QDValidateForm;
import com.fitech.gznx.service.AFQDValidateFormulaDelegate;
import com.fitech.gznx.service.StrutsAFCellFormuDelegate;

public class DeleteQDValidateAction extends Action {
	private FitechException log=new FitechException(DeleteBJGXAction.class);
	
	/**
	 * Performs action.
	 * @param mapping Action mapping.
	 * @param form Action form.
	 * @param request HTTP request.
	 * @param response HTTP response.
	 * @exception IOException if an input/output error occurs
	 * @exception ServletException if a servlet exception occurs
	 */
	public ActionForward execute(ActionMapping mapping, 
			ActionForm form,
			HttpServletRequest request, 
			HttpServletResponse response)
			throws IOException, ServletException {
		Locale locale = getLocale(request);
		MessageResources resources = getResources(request);
		
		FitechMessages messages=new FitechMessages();
		QDValidateForm qdValidateForm=(QDValidateForm) form;
		RequestUtils.populate(qdValidateForm,request);

		String curPage="";
		if(request.getParameter("curPage")!=null) curPage=(String)request.getParameter("curPage");
			
		try{
			String cellFormuIds=null;
			
			if(request.getParameter("formuIds")!=null) cellFormuIds=(String)request.getParameter("formuIds");
			
			if(AFQDValidateFormulaDelegate.deleteValidateFormula(cellFormuIds)==true){
				messages.add(FitechResource.getMessage(locale,resources,"delete.success","bjgx.info"));
			}else{
				messages.add(FitechResource.getMessage(locale,resources,"delete.failed","bjgx.info"));
			}
		}catch(Exception e){
			log.printStackTrace(e);
			messages.add(FitechResource.getMessage(locale,resources,"errors.system"));
			return mapping.findForward(Config.FORWARD_SYS_ERROR_PAGE);
		}
		
		if(messages!=null && messages.getSize()>0) request.setAttribute(Config.MESSAGES,messages);
		
		request.setAttribute("ReportName",request.getParameter("reportName"));
		ActionForward af = new ActionForward("/gznx/viewQDValidateList.do?templateId=" +qdValidateForm.getTemplateId()+"&versionId=" +
				qdValidateForm.getVersionId());
		return af;
	}
}
