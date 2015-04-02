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
	 * ��ʹ��hibernate ���Ը� 2011-12-26
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		AFReportForceService forceService = new AFReportForceService();
		MessageResources resources = getResources(request);
		FitechMessages messages = new FitechMessages();

		// ȡ��request��Χ�ڵ�����������������ReportInForm��
		AFReportForm reportInForm = (AFReportForm) form;
		RequestUtils.populate(reportInForm, request);

		boolean flag = true;
		String messagesStr = null;

		Integer repInId = null;
		// �ﵽ�ϱ������ı���
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

						// �õ�����У��Ľ��ֵ
						Short BLValidateFlag = reportIn.getTblInnerValidateFlag() != null ? reportIn
								.getTblInnerValidateFlag().shortValue() : -1;
						// �õ����У��Ľ��ֵ
						Short BJValidateFlag = reportIn.getTblOuterValidateFlag() != null ? reportIn
								.getTblOuterValidateFlag().shortValue() : -1;
						/* add 2013-06-21 ������  ���ǿ��Ҫ�ϱ��ı���*/	 	
						List<AfReportForceRep> force = forceService.findAFReportForce(Long.valueOf(repInId),null);
						
						if (Config.SYS_BN_VALIDATE.equals(new Integer(1))) {
							if (BLValidateFlag == null || !BLValidateFlag.equals(new Short("1"))) {
								messages.add(reportIn.getRepName() + "������У��δͨ�����޷����ͣ�");
								flag = false;
							}
							for(int j = 0; j < force.size(); j++){
								/*��������˱���У��*/
								if(force != null && force.get(j).getId().getForceTypeId() == 1){
									flag = true;
								}
							}
						}
						if (flag && Config.SYS_BJ_VALIDATE.equals(new Integer(1))) {
							AfTemplate at  = AFTemplateDelegate.getTemplate(reportIn.getTemplateId(), reportIn.getVersionId());
							//�嵥������Ա��У��
							if(at.getReportStyle().intValue()==com.cbrc.smis.common.Config.REPORT_STYLE_QD){
								flag = true;
							}else{
								if (BJValidateFlag == null || !BJValidateFlag.equals(new Short("1"))) {
									messages.add(reportIn.getRepName() + "�����У��δͨ�����޷����ͣ�");
									flag = false;
								}
								for(int j = 0; j < force.size(); j++){
									/*��������˱��У��*/
									if(force != null && force.get(j).getId().getForceTypeId() == 2){
										flag = true;
									}
								}
							}
						}
						for(int j = 0; j < force.size(); j++){
							/*���������ȫ��У��*/
							if(force != null && force.get(j).getId().getForceTypeId() == -1){
								flag = true;
							}
						}
						//�����ҪУ���״̬���ϱ���������������ϱ�����
						if(flag) {
							allowReportList.add(reportIn);
						}else{
							notReport++;
						}
					}
				}
			} else if (type != null && type.equals("all")) {
				
				//��û�����Ϣ
				HttpSession session = request.getSession();
				Operator operator = (Operator) session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
				String templateType = session.getAttribute(Config.REPORT_SESSION_FLG).toString();
				
				reportInForm.setTemplateType(templateType);
				
				/***
				 * ��ʹ��hibernate ���Ը� 2011-12-26
				 * Ӱ�����AfTemplate AfReport
				 */
				List reps = AFReportDelegate.reportReports(reportInForm, operator, 
						new Integer(Config.CHECK_FLAG_AFTERJY));
				
				if (reps != null && reps.size() > 0) {
					
					for (int i = 0; i < reps.size(); i++) {
						
						flag = true;
						
						AfReport reportIn = (AfReport) reps.get(i);

						// �õ�����У��Ľ��ֵ
						Short BLValidateFlag = reportIn.getTblInnerValidateFlag() != null ? reportIn
								.getTblInnerValidateFlag().shortValue() : -1;
						// �õ����У��Ľ��ֵ
						Short BJValidateFlag = reportIn.getTblOuterValidateFlag() != null ? reportIn
								.getTblOuterValidateFlag().shortValue() : -1;

						if (Config.SYS_BN_VALIDATE.equals(new Integer(1))) {
							if (BLValidateFlag == null || !BLValidateFlag.equals(new Short("1"))) {
								messages.add(reportIn.getRepName() + "������У��δͨ�����޷����ͣ�");
								flag = false;
							}
						}
						if (flag && Config.SYS_BJ_VALIDATE.equals(new Integer(1))) {
							AfTemplate at  = AFTemplateDelegate.getTemplate(reportIn.getTemplateId(), reportIn.getVersionId());
							//�嵥������Ա��У��
							if(at.getReportStyle().intValue()==com.cbrc.smis.common.Config.REPORT_STYLE_QD){
								flag = true;
							}else{
								if (BJValidateFlag == null || !BJValidateFlag.equals(new Short("1"))) {
									messages.add(reportIn.getRepName() + "�����У��δͨ�����޷����ͣ�");
									flag = false;
								}
							}
						}
						//�����ҪУ���״̬���ϱ���������������ϱ�����
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
				
				/**��ʹ��hibernate ���Ը� 2011-12-21*/
				bool = AFReportDelegate.updateReport(allowReportList);
				
				if (bool) {
					// new InputData().bnValidate(reportIn.getRepInId());
					messagesStr = "���ͳɹ���";
				} else {
					messagesStr = "ϵͳæ�����Ժ�����...��";
				}
			}

		} catch (Exception ex) {
			log.printStackTrace(ex);
			// messages.add(resources.getMessage("select.uponlineReport.failed"));
			messagesStr = "ϵͳæ�����Ժ�����...��";
		}

		
		//�����Ϣ
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
