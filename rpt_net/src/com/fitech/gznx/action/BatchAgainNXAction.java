package com.fitech.gznx.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.hibernate.HibernateException;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.cbrc.smis.adapter.StrutsReportInDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.BatchAgainForm;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.gznx.form.AFReportForm;
import com.fitech.gznx.service.AFReportDelegate;

/**
 * ��ʹ��Hibernate ���Ը� 2011-12-27
 * Ӱ�����AfReport AfReportAgain
 * �����ر�
 * 
 * @author Dennis Yee
 * 
 */
public class BatchAgainNXAction extends Action {

	private FitechException log = new FitechException(BatchAgainNXAction.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		AFReportForm pform = (AFReportForm) form;
		FitechMessages messages = new FitechMessages();
		MessageResources resources = getResources(request);

		// �ر�ԭ��
		String cause = pform.getCause();

		// �����ر������Id
		String param = request.getParameter("repInIds");
		// ����ݣ��������������ƣ���ǰҳ�Ȳ����Ĳ�ѯ�ַ���
		String queryString = request.getParameter("queryString");

		if (StringUtils.isNotEmpty(param)) {
			//���؈���
			String arry[] = param.split(Config.SPLIT_SYMBOL_COMMA);
			
			// ȡ��ģ������
			HttpSession session = request.getSession();
			Integer templateType = null;
			if (session.getAttribute(com.cbrc.smis.common.Config.REPORT_SESSION_FLG) != null)
				templateType = Integer.valueOf(session.getAttribute(
						com.cbrc.smis.common.Config.REPORT_SESSION_FLG).toString());

			try {
				/**��ʹ��hibernate ���Ը� 2011-12-27
				 * Ӱ�����AfReport AfReportAgain**/
				boolean result = AFReportDelegate.batchAgain(arry, cause, templateType);

				if (result)
					messages.add(resources.getMessage("batchAgain.success"));
				else
					messages.add(resources.getMessage("batchAgain.failure"));

			} catch (HibernateException e) {

				messages.add(resources.getMessage("batchAgain.error"));
			}
		} else {
			messages.add(resources.getMessage("batchAgain.error"));
		}
		request.setAttribute(Config.MESSAGES, messages);
		return new ActionForward("/report/repNXSearch.do?" + queryString);
	}
}
