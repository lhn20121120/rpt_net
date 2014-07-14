package com.fitech.gznx.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.gznx.common.StringUtil;
import com.fitech.gznx.form.SqlQueryForm;
import com.fitech.gznx.service.AFExecuteSqlDelegate;

public class AddSqlExcuteAction extends Action {
	/**
	 * Performs action.
	 * 
	 * @param mapping
	 *            Action mapping.
	 * @param form
	 *            Action form.
	 * @param request
	 *            HTTP request.
	 * @param response
	 *            HTTP response.
	 * @exception IOException
	 *                if an input/output error occurs
	 * @exception ServletException
	 *                if a servlet exception occurs
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException
	{
		FitechMessages messages = new FitechMessages();
		SqlQueryForm sqlQueryForm = (SqlQueryForm) form;
		RequestUtils.populate(sqlQueryForm, request);
		boolean result = true;
		if(sqlQueryForm!=null && !StringUtil.isEmpty(sqlQueryForm.getContents())){
			result = AFExecuteSqlDelegate.setSql(sqlQueryForm.getContents());
			if(result){
				messages.add("sql执行成功");
			} else {
				messages.add("sql执行失败");
			}
		}
		request.setAttribute(Config.MESSAGES, messages);
		return mapping.findForward("index");
	}
}
