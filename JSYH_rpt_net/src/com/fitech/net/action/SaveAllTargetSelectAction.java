package com.fitech.net.action;

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
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechUtil;
import com.fitech.net.adapter.StrutsTargetDefineDelegate;
import com.fitech.net.adapter.StrutsTargetRangeDelegate;
import com.fitech.net.form.TargetDefineForm;

/**
 * 
 * 
 * @author
 * 
 */
public final class SaveAllTargetSelectAction extends Action {
	private static FitechException log = new FitechException(
			SaveAllTargetSelectAction.class);

	/**
	 * Performs action.
	 * 
	 * @result ���³ɹ�����true�����򷵻�false
	 * @reportInForm FormBean��ʵ����
	 * @e Exception ����ʧ�ܲ�׽�쳣���׳�
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		Locale locale = getLocale(request);
		MessageResources resources = getResources(request);
		FitechMessages messages = new FitechMessages();

		TargetDefineForm targetDefineForm = (TargetDefineForm) form;
		RequestUtils.populate(targetDefineForm, request);

		if (targetDefineForm.getBusinessId() != null
				&& targetDefineForm.getBusinessId().equals("-1"))
			targetDefineForm.setBusinessId(null);
		if (targetDefineForm.getNormalId() != null
				&& targetDefineForm.getNormalId().equals("-1"))
			targetDefineForm.setNormalId(null);

		HttpSession session = request.getSession();
		Operator operator = (Operator) session
				.getAttribute(Config.OPERATOR_SESSION_NAME);

		String orgId = operator.getOrgId();

		String curPage = "";
		try {
			String flag=request.getParameter("flag");
			
			// �������ָ��
			List resList = StrutsTargetDefineDelegate.selectAll(
					targetDefineForm, orgId);
			if (resList != null && resList.size() > 0) {
				if (flag!=null && flag.equals("save") ) {
					for (int i = 0; i < resList.size(); i++) {
						TargetDefineForm tdf = (TargetDefineForm) resList.get(i);
						if (!StrutsTargetRangeDelegate.Exit(orgId,tdf.getTargetDefineId()))						
							StrutsTargetRangeDelegate.add(orgId, tdf.getTargetDefineId());						
					}
				} else if (flag!=null && flag.equals("cancel") ){
					for (int i = 0; i < resList.size(); i++) {
						TargetDefineForm tdf = (TargetDefineForm) resList.get(i);
						if (StrutsTargetRangeDelegate.Exit(orgId, tdf.getTargetDefineId()))						
							StrutsTargetRangeDelegate.delete(orgId, tdf.getTargetDefineId());						
					}
				}else{
					// System.out.println("��������!");
				}
			}
            if("save".equals(flag))
                messages.add("ָ���趨����ɹ���");
            if("cancel".equals(flag))
                messages.add("ָ��ȡ���ɹ���"); 
		} catch (Exception ex) {
			ex.printStackTrace();
			messages.add("ָ���趨����ʧ�ܣ�");
		}

		FitechUtil.removeAttribute(mapping, request);

		// �ж�������ʾ��Ϣ�����н���洢��Request�����У���������
		if (messages != null && messages.getSize() > 0)
			request.setAttribute(Config.MESSAGES, messages);
		String path = "";

		path = mapping.findForward("view").getPath() + "?curPage=" + curPage;

		return new ActionForward(path);
	}
}
