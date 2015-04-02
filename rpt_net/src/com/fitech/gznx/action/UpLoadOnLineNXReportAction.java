package com.fitech.gznx.action;

import java.io.IOException;
import java.io.PrintWriter;
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

import com.cbrc.smis.util.FitechException;
import com.fitech.gznx.form.AFReportForm;
import com.fitech.gznx.po.AfReport;
import com.fitech.gznx.po.AfReportForceRep;
import com.fitech.gznx.po.AfTemplate;
import com.fitech.gznx.service.AFReportDelegate;
import com.fitech.gznx.service.AFReportForceService;
import com.fitech.gznx.service.AFTemplateDelegate;
import com.fitech.net.template.action.UpLoadOnLineReportAction;

public class UpLoadOnLineNXReportAction extends Action {
	private static FitechException log = new FitechException(
			UpLoadOnLineReportAction.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		AFReportForceService forceService = new AFReportForceService();
		MessageResources resources = getResources(request);
		// FitechMessages messages = new FitechMessages();

		// ȡ��request��Χ�ڵ�����������������ReportInForm��
		AFReportForm reportInForm = (AFReportForm) form;
		RequestUtils.populate(reportInForm, request);
		boolean flag = true;
		String messagesStr = null;
		StringBuffer path = new StringBuffer("/viewOnLineSJBS.do");

		String repInId = request.getParameter("repInId");
		
		try {

			AfReport reportIn = AFReportDelegate.getReportIn(Long.valueOf(repInId));
			if (reportIn != null) {
				// �õ�����У��Ľ��ֵ
				Short BLValidateFlag = reportIn.getTblInnerValidateFlag()!=null ? reportIn.getTblInnerValidateFlag().shortValue() : -1;
				// �õ����У��Ľ��ֵ
				Short BJValidateFlag = reportIn.getTblOuterValidateFlag()!=null ? reportIn.getTblOuterValidateFlag().shortValue() : -1;
				
				/* add 2013-06-21 ������  ���ǿ��Ҫ�ϱ��ı���*/	 	
				List<AfReportForceRep> force = forceService.findAFReportForce(Long.valueOf(repInId),null);
				if (com.cbrc.smis.common.Config.SYS_BN_VALIDATE.equals(new Integer(1))) {
					if (BLValidateFlag == null
							|| !BLValidateFlag.equals(new Short("1"))) {
						// messages.add("����У�鲻ͨ���������ϱ��ñ���");
						messagesStr = "BN_VALIDATE_NOTPASS";
						// request.setAttribute(Config.MESSAGES, messages);
						flag = false;
						// return new ActionForward(path.toString());
						for(int i = 0; i < force.size(); i++){
							/*��������˱���У��*/
							if(force != null && force.get(i).getId().getForceTypeId() == 1){
								flag = true;
							}
						}
					}
				}
				
				if (flag && com.cbrc.smis.common.Config.SYS_BJ_VALIDATE.equals(new Integer(1))) {
					AfTemplate at  = AFTemplateDelegate.getTemplate(reportIn.getTemplateId(), reportIn.getVersionId());
					//�嵥������Ա��У��
					if(at.getReportStyle().intValue()==com.cbrc.smis.common.Config.REPORT_STYLE_QD){
						flag = true;
					}else{
						if (BJValidateFlag == null
								|| !BJValidateFlag.equals(new Short("1"))) {
							// messages.add("���У�鲻ͨ���������ϱ��ñ���");
							// request.setAttribute(Config.MESSAGES, messages);
							messagesStr = "BJ_VALIDATE_NOTPASS";
							flag = false;
							// return new ActionForward(path.toString());
							for(int i = 0; i < force.size(); i++){
								/*��������˱��У��*/
								if(force != null && force.get(i).getId().getForceTypeId() == 2){
									flag = true;
								}
							}
						}
					}
				}
				
				for(int i = 0; i < force.size(); i++){
					/*���������ȫ��У��*/
					if(force != null && force.get(i).getId().getForceTypeId() == -1){
						flag = true;
					}
				}
			}
			
			
			if (flag) {
				boolean bool = false;
				bool = AFReportDelegate.updateReport(reportIn);
				if (bool) {
					// new InputData().bnValidate(reportIn.getRepInId());
					// messages.add(resources.getMessage("select.uponlineReport.success"));
					messagesStr = "���ͳɹ���";
				} else {
					// messages.add(resources.getMessage("select.uponlineReport.failed"));
					messagesStr = "ϵͳæ�����Ժ�����...��";
				}
			}
		
		} catch (Exception ex) {
			log.printStackTrace(ex);
			// messages.add(resources.getMessage("select.uponlineReport.failed"));
			messagesStr = "ϵͳæ�����Ժ�����...��";
		}

		// if (messages.getMessages() != null && messages.getMessages().size() >
		// 0)
		// request.setAttribute(Config.MESSAGES, messages);

		// response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();

		response.setContentType("text/xml");
		response.setHeader("Cache-control", "no-cache");
		String result = "true";
		if (flag) {
			result = "true";
		} else {
			result = messagesStr;
		}
		out.println("<response><result>" + result + "</result></response>");
		out.close();

		return null;

		// return new ActionForward(path.toString());
	}
}
