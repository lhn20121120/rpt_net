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
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.adapter.StrutsMRepRangeDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;

/**
 * 已使用hibernate 卞以刚 2011-12-22
 * 机构报送情况统计
 * 
 * @author jcm
 * @serialData 2008-02-28
 */
public final class DataStatAction extends Action {
	private FitechException log=new FitechException(DataStatAction.class);
	
   /**
    * 已使用hibernate 卞以刚 2011-12-27
    * 影响表：VIEW_M_REPORT REPORT_IN
    * @param result 查询返回标志,如果成功返回true,否则返回false
    * @param ReportInForm 
    * @param request 
    * @exception Exception 有异常捕捉并抛出
    */
	public ActionForward execute(ActionMapping mapping,ActionForm form
			,HttpServletRequest request,HttpServletResponse response)throws IOException, ServletException {

		MessageResources resources = null;
		FitechMessages messages = null;
		ReportInForm reportInForm = null;
		List resList = null;
		String searchType = request.getParameter("searchType");
		String returname = "view";
		try{
			resources = getResources(request);
	        messages = new FitechMessages();
	        reportInForm = (ReportInForm)form ;	   
			RequestUtils.populate(reportInForm, request);
	        /**已使用hibernate 卞以刚 2011-12-22
	         * 影响表：VIEW_M_REPORT REPORT_IN**/
			if(searchType==null){
				resList = StrutsMRepRangeDelegate.selectNew(reportInForm);
			}else if(searchType.equals("repRep")){
				returname="RepRepview";
				resList = StrutsMRepRangeDelegate.selectRepRepInfo(reportInForm);
			}else if(searchType.equals("laterRep")){
				returname="LaterRepview";
				resList = StrutsMRepRangeDelegate.selectLaterRepInfo(reportInForm);
			}
			
		}catch(Exception e){
			log.printStackTrace(e);
			messages.add(resources.getMessage("select.data.stat.failed"));
		}
		
		if(messages.getMessages() != null && messages.getMessages().size() > 0)
		   	  request.setAttribute(Config.MESSAGES,messages);
		
		if(resList!=null && resList.size()>0)	
	      	request.setAttribute("Records",resList);
		
		request.setAttribute("form",reportInForm);
		request.setAttribute("orgName",reportInForm.getOrgName());

		return mapping.findForward(returname);
	}
}