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

import com.cbrc.smis.adapter.StrutsMCellFormuDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.MCellFormuForm;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
import com.fitech.gznx.service.StrutsAFCellFormuDelegate;

public class DeleteBJGXAction extends Action {
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
		MCellFormuForm mCellForumForm = (MCellFormuForm) form;
		RequestUtils.populate(mCellForumForm, request);
		  /** 报表选中标志 **/
		String reportFlg = "0";
		HttpSession session = request.getSession();
		if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
			reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
		}
		String curPage="";
		if(request.getParameter("curPage")!=null) curPage=(String)request.getParameter("curPage");
			
		try{
			String cellFormuIds=null;
			
			if(request.getParameter("cellFormuIds")!=null) cellFormuIds=(String)request.getParameter("cellFormuIds");
			
			if(StrutsAFCellFormuDelegate.delete(cellFormuIds,mCellForumForm.getChildRepId(),mCellForumForm.getVersionId(),reportFlg)==true){
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
		
		return new ActionForward(mapping.getInputForward().getPath() + 
				(curPage.equals("")?"":("?" + "curPage=" + curPage)));
	}
}
