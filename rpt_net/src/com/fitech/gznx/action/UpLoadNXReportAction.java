package com.fitech.gznx.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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

import com.cbrc.smis.common.Config;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.gznx.form.AFReportForm;
import com.fitech.gznx.po.AfReport;
import com.fitech.gznx.po.AfReportForceRep;
import com.fitech.gznx.po.AfTemplate;
import com.fitech.gznx.service.AFReportDelegate;
import com.fitech.gznx.service.AFReportForceService;
import com.fitech.gznx.service.AFTemplateDelegate;
import com.fitech.net.template.action.UpLoadOnLineReportAction;

public class UpLoadNXReportAction extends Action {
	private static FitechException log = new FitechException(
			UpLoadOnLineReportAction.class);
	
	/***
	 * 已使用hibernate 卞以刚 2011-12-26
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		AFReportForceService forceService = new AFReportForceService();
		MessageResources resources = getResources(request);
		FitechMessages messages = new FitechMessages();

		// 取得request范围内的请求参数，并存放在ReportInForm内
		AFReportForm reportInForm = (AFReportForm) form;
		RequestUtils.populate(reportInForm, request);

		boolean flag = true;
		String messagesStr = null;

		Integer repInId = null;
		// 达到上报条件的报表
		List allowReportList = null;

		String type = request.getParameter("type");

		int notReport = 0;
		
		try {

			allowReportList = new ArrayList();
			
			if (type != null && type.equals("select")
					&& request.getParameter("repInIds") != null) {

				String[] repInIds = request.getParameter("repInIds").split(",");

				if (repInIds != null && repInIds.length > 0) {
					for (int i = 0; i < repInIds.length; i++) {
						
						flag = true;
						
						repInId = new Integer(repInIds[i]);

						AfReport reportIn = AFReportDelegate.getReportIn(Long.valueOf(repInId));

						// 得到表内校验的结果值
						Short BLValidateFlag = reportIn.getTblInnerValidateFlag() != null ? reportIn
								.getTblInnerValidateFlag().shortValue() : -1;
						// 得到表间校验的结果值
						Short BJValidateFlag = reportIn.getTblOuterValidateFlag() != null ? reportIn
								.getTblOuterValidateFlag().shortValue() : -1;
						/* add 2013-06-21 姜明青  获得强制要上报的报表*/	 	
						List<AfReportForceRep> force = forceService.findAFReportForce(Long.valueOf(repInId),null);
						
						if (Config.SYS_BN_VALIDATE.equals(new Integer(1))) {
							if (BLValidateFlag == null || !BLValidateFlag.equals(new Short("1"))) {
								messages.add(reportIn.getRepName() + "：表内校验未通过，无法报送！");
								flag = false;
							}
							for(int j = 0; j < force.size(); j++){
								/*报表忽略了表内校验*/
								if(force != null && force.get(j).getId().getForceTypeId() == 1){
									flag = true;
								}
							}
						}
						if (flag && Config.SYS_BJ_VALIDATE.equals(new Integer(1))) {
							AfTemplate at  = AFTemplateDelegate.getTemplate(reportIn.getTemplateId(), reportIn.getVersionId());
							//清单报表忽略表间校验
							if(at.getReportStyle().intValue()==com.cbrc.smis.common.Config.REPORT_STYLE_QD){
								flag = true;
							}else{
								if (BJValidateFlag == null || !BJValidateFlag.equals(new Short("1"))) {
									messages.add(reportIn.getRepName() + "：表间校验未通过，无法报送！");
									flag = false;
								}
								for(int j = 0; j < force.size(); j++){
									/*报表忽略了表间校验*/
									if(force != null && force.get(j).getId().getForceTypeId() == 2){
										flag = true;
									}
								}
							}
						}
						for(int j = 0; j < force.size(); j++){
							/*报表忽略了全部校验*/
							if(force != null && force.get(j).getId().getForceTypeId() == -1){
								flag = true;
							}
						}
						//如果需要校验的状态符合报送情况，即加入上报队列
						if(flag) {
							allowReportList.add(reportIn);
						}else{
							notReport++;
						}
					}
				}
			} else if (type != null && type.equals("all")) {
				
				//获得基础信息
				HttpSession session = request.getSession();
				Operator operator = (Operator) session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
				String templateType = session.getAttribute(Config.REPORT_SESSION_FLG).toString();
				
				reportInForm.setTemplateType(templateType);
				
				/***
				 * 已使用hibernate 卞以刚 2011-12-26
				 * 影响对象：AfTemplate AfReport
				 */
				List reps = AFReportDelegate.reportReports(reportInForm, operator, 
						new Integer(Config.CHECK_FLAG_AFTERJY));
				
				if (reps != null && reps.size() > 0) {
					
					for (int i = 0; i < reps.size(); i++) {
						
						flag = true;
						
						AfReport reportIn = (AfReport) reps.get(i);

						// 得到表内校验的结果值
						Short BLValidateFlag = reportIn.getTblInnerValidateFlag() != null ? reportIn
								.getTblInnerValidateFlag().shortValue() : -1;
						// 得到表间校验的结果值
						Short BJValidateFlag = reportIn.getTblOuterValidateFlag() != null ? reportIn
								.getTblOuterValidateFlag().shortValue() : -1;

						if (Config.SYS_BN_VALIDATE.equals(new Integer(1))) {
							if (BLValidateFlag == null || !BLValidateFlag.equals(new Short("1"))) {
								messages.add(reportIn.getRepName() + "：表内校验未通过，无法报送！");
								flag = false;
							}
						}
						if (flag && Config.SYS_BJ_VALIDATE.equals(new Integer(1))) {
							AfTemplate at  = AFTemplateDelegate.getTemplate(reportIn.getTemplateId(), reportIn.getVersionId());
							//清单报表忽略表间校验
							if(at.getReportStyle().intValue()==com.cbrc.smis.common.Config.REPORT_STYLE_QD){
								flag = true;
							}else{
								if (BJValidateFlag == null || !BJValidateFlag.equals(new Short("1"))) {
									messages.add(reportIn.getRepName() + "：表间校验未通过，无法报送！");
									flag = false;
								}
							}
						}
						//如果需要校验的状态符合报送情况，即加入上报队列
						if(flag) {
							allowReportList.add(reportIn);
						}else{
							notReport++;
						}
					}
				}
			}

			if (allowReportList.size() > 0) {
				
				boolean bool = false;
				
				/**已使用hibernate 卞以刚 2011-12-21*/
				bool = AFReportDelegate.updateReport(allowReportList);
				
				if (bool) {
					// new InputData().bnValidate(reportIn.getRepInId());
					messagesStr = "报送成功！";
				} else {
					messagesStr = "系统忙，请稍后再试...！";
				}
			}

		} catch (Exception ex) {
			log.printStackTrace(ex);
			// messages.add(resources.getMessage("select.uponlineReport.failed"));
			messagesStr = "系统忙，请稍后再试...！";
		}

		
		//输出信息
//		if (messages.getMessages() != null && messages.getMessages().size() > 0)
//			 request.setAttribute(Config.MESSAGES, messages);

				
		// response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();

		response.setContentType("text/xml");
		response.setHeader("Cache-control", "no-cache");
		String result = "true";
		if (notReport > 0) {
			result = String.valueOf(notReport);//messages.getAlertMsg();
		} else {
			result = "true";
			//result = messagesStr;
		}
		out.println("<response><result>" + result + "</result></response>");
		out.close();

		return null;

		// return new ActionForward(path.toString());
	}
}
