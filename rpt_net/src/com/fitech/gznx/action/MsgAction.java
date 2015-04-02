package com.fitech.gznx.action;

import java.io.IOException;
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

import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
import com.fitech.fitosa.adapter.ImpReportData;
import com.fitech.fitosa.bean.OrgInfoBean;
import com.fitech.gznx.common.Config;
import com.fitech.gznx.common.StringUtil;
import com.fitech.gznx.form.OrgInfoForm;
import com.fitech.gznx.po.AfOrg;
import com.fitech.gznx.service.AFOrgDelegate;
import com.fitech.gznx.service.XmlTreeUtil;
import com.fitech.net.adapter.StrutsMRegionDelegate;
import com.fitech.net.adapter.StrutsOrgNetDelegate;
import com.fitech.net.adapter.StrutsOrgTypeDelegate;
import com.fitech.net.form.MRegionForm;
import com.fitech.net.form.OrgNetForm;
import com.fitech.net.form.OrgTypeForm;

public class MsgAction extends Action {
	
	private static FitechException log = new FitechException(
			OrgInfoAction.class);

	FitechMessages messages = new FitechMessages();

	OrgNetForm orgNetForm = null;

	// 日志消息
	private String msg = "";

	/**
	 * Method execute
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// 根据传入的不同的参数，执行不同的动作
		msg = request.getParameter("msg");
		return mapping.findForward("msg");
		}
}
