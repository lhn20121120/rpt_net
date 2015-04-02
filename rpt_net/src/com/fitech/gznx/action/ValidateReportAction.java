package com.fitech.gznx.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
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

import com.cbrc.smis.common.Config;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
import com.fitech.gznx.form.AFReportForm;
import com.fitech.gznx.procedure.ProcedureHandle;
import com.fitech.gznx.service.AFReportDelegate;

public class ValidateReportAction extends Action {
	
	private static FitechException log = new FitechException(
			ValidateReportAction.class);

	/**
	 * Performs action.
	 * 
	 * @result 更新成功返回true，否则返回false
	 * @reportInForm FormBean的实例化
	 * @e Exception 更新失败捕捉异常并抛出
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		Locale locale = getLocale(request);
		MessageResources resources = getResources(request);
		FitechMessages messages = new FitechMessages();
		String failedReportInIds = "";
		AFReportForm reportInForm = (AFReportForm) form;
		RequestUtils.populate(reportInForm, request);
		String repInIds = request.getParameter("repInIds");
		try {
			Operator operator = null;
			Integer templateType = null;
			HttpSession session = request.getSession();
			
			if (session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME) != null)
				operator = (Operator) session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
			
			if (session.getAttribute(Config.REPORT_SESSION_FLG) != null)
				templateType = Integer.valueOf(session.getAttribute(Config.REPORT_SESSION_FLG).toString());
			
			//标志从哪个页面传进来需要什么查询条件
			String formFlg = request.getParameter("formFlg");
			String checkFlgs = "";
			if(formFlg!=null&&formFlg.equals("check") ){
				checkFlgs = com.fitech.net.config.Config.CHECK_FLAG_UNCHECK.toString();
			}else{
				checkFlgs = com.fitech.net.config.Config.CHECK_FLAG_UNCHECK.toString()+ "," +Config.CHECK_FLAG_PASS;
			}

			if (repInIds != null && !repInIds.equals("")) {
				String[] repInIdArr = repInIds.split(Config.SPLIT_SYMBOL_COMMA);
				if (repInIdArr != null && repInIdArr.length != 0) {
					for (int i = 0; i < repInIdArr.length; i++) {
						Integer repInId = new Integer(repInIdArr[i]);
						/**
						 * 有oracle语法(nextval) 需要修改
						 * 已修改 添加sqlserver数据库sql语句 卞以刚 待测试 2011-12-26
						 * 已增加数据库判断**/
						boolean res = ProcedureHandle.runBJJY(repInId, operator.getOperatorName(),templateType);
						if(!res)
							failedReportInIds += (failedReportInIds == "" ? "" : ",") + repInId;
					}
				}
			} else {

				reportInForm.setTemplateType(templateType.toString());
				/**hibernate 不需要修改 卞以刚 2011-12-27
				 * 影响对象： AfReport AfTemplate**/
				List list = AFReportDelegate.selectOfManualAll(reportInForm, operator,checkFlgs);
				if (list != null && list.size() != 0) {
					for (Iterator iter = list.iterator(); iter.hasNext();) {
						Integer repInId = (Integer) iter.next();
						/**
						 * 有oracle语法(nextval) 需要修改
						 * 已修改 添加sqlserver数据库sql语句 卞以刚 待测试 2011-12-26
						 * 已增加数据库判断**/
						boolean res = ProcedureHandle.runBJJY(repInId, operator.getOperatorName(),templateType);
						if(!res)
							failedReportInIds += (failedReportInIds == "" ? "" : ",") + repInId;
					}
				}
			}
			
		} catch (Exception e) {
			log.printStackTrace(e);
			messages.add(FitechResource.getMessage(locale, resources, "errors.system"));
		}
//		if (messages.getMessages() != null && messages.getMessages().size() > 0)
//			request.setAttribute(Config.MESSAGES, messages);

		PrintWriter out = response.getWriter();

		response.setContentType("text/xml");
		response.setHeader("Cache-control", "no-cache");
		String result = "";
		
		if (failedReportInIds != null && failedReportInIds.length() > 0)
			result = failedReportInIds;
		else
			result = "true";
		
		if (messages!=null && messages.getAlertMsg()!=null && !messages.getAlertMsg().trim().equals(""))
			result = "";
		
		out.println("<response><result>" + result + "</result></response>");
		out.close();
		
		return null;
	
	}
	
}
