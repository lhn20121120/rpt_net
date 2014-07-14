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
 * 已使用Hibernate 卞以刚 2011-12-27
 * 影响对象：AfReport AfReportAgain
 * 批量重报
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

		// 重报原因
		String cause = pform.getCause();

		// 批量重报报表的Id
		String param = request.getParameter("repInIds");
		// 含年份，期数，报表名称，当前页等参数的查询字符串
		String queryString = request.getParameter("queryString");

		if (StringUtils.isNotEmpty(param)) {
			//需重報報表
			String arry[] = param.split(Config.SPLIT_SYMBOL_COMMA);
			
			// 取得模板类型
			HttpSession session = request.getSession();
			Integer templateType = null;
			if (session.getAttribute(com.cbrc.smis.common.Config.REPORT_SESSION_FLG) != null)
				templateType = Integer.valueOf(session.getAttribute(
						com.cbrc.smis.common.Config.REPORT_SESSION_FLG).toString());

			try {
				/**已使用hibernate 卞以刚 2011-12-27
				 * 影响对象：AfReport AfReportAgain**/
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
