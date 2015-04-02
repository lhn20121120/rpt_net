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

import com.cbrc.smis.adapter.StrutsMCellFormuDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
/**
 * 删除表内表间关系表达式操作类
 * 
 * @author rds
 * @serialData 2005-12-19 17:45
 */
public class DeleteBJGXAction extends Action {
	private FitechException log=new FitechException(DeleteBJGXAction.class);
	
	/**
	 * 已使用hibernate 卞以刚 2011-12-22
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
		
		String curPage="";
		if(request.getParameter("curPage")!=null) curPage=(String)request.getParameter("curPage");
			
		try{
			String cellFormuIds=null;
			
			if(request.getParameter("cellFormuIds")!=null) cellFormuIds=(String)request.getParameter("cellFormuIds");
			/**已使用hibernate 卞以刚 2011-12-22**/
			if(StrutsMCellFormuDelegate.delete(cellFormuIds)==true){
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
