package com.cbrc.smis.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.cbrc.smis.adapter.StrutsReportInDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;

/**
 * @author 曹发根 新建强制重报
 */
public final class NewForseReportAgainAction extends Action {
	private static FitechException log = new FitechException(NewForseReportAgainAction.class);  

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		ReportInForm reportInForm = (ReportInForm) form;
		boolean re=false;//判断返回页面；

		//RequestUtils.populate(reportInForm, request);
		MessageResources resources=getResources(request);
		 FitechMessages messages = new FitechMessages();
		try {
			//验证表单数据是否合格
			if (reportInForm != null) {
				if(!StrutsReportInDelegate.validate(reportInForm)){
					 messages.add(resources.getMessage("forse_report_again.input.err"));                  
			    }//验证合格后新建强制重报
				else if(StrutsReportInDelegate.newForseReportIn(reportInForm)){
					
					//将内网的强制重报标志插入外网的Report实际报表中的Report_Flag标志
				//	boolean insertReportInIntoGather=InnerToGather.insertReportFlag(reportInForm);
					messages.add(resources.getMessage("forse_report_again.input.success"));
					re=true;
				}else messages.add(resources.getMessage("forse_report_again.new.err"));
		    }else{
			    messages.add(resources.getMessage("forse_report_again.input.err"));				
		    }

		} catch (Exception e) {
			log.printStackTrace(e);
			messages.add(resources.getMessage("forse_report_again.new.err"));
		    return mapping.getInputForward();
		}
		if(messages.getMessages() != null && messages.getMessages().size() > 0)
		   	  request.setAttribute(Config.MESSAGES,messages);
		if(re){
		    String path = "/selForseReportAgain.do";
		    return new ActionForward(path);
		}else return mapping.getInputForward();

	}
}
