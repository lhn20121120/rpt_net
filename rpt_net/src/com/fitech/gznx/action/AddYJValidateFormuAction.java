package com.fitech.gznx.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cbrc.smis.common.Config;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.cbrc.smis.form.MCellFormuForm;
import com.cbrc.smis.hibernate.MCellFormu;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechTemplate;
import com.fitech.gznx.form.ReportCheckForm;
import com.fitech.gznx.service.StrutsAFCellFormuDelegate;
/**
 * �������ڱ���У�鹫ʽ������
 * 
 * **/
public class AddYJValidateFormuAction extends Action {
	
	private FitechException log=new FitechException(this.getClass());
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		MessageResources resources = getResources(request);
		FitechMessages messages = new FitechMessages();
		Locale locale = getLocale(request);
		HttpSession session = request.getSession();
		ReportCheckForm reportCheckForm = (ReportCheckForm) form;
		String reportName=request.getParameter("reportName");
		String reportFlg = "0";
		if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
			reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
		}
		try{
			if(StrutsAFCellFormuDelegate.saveYJPatch(reportCheckForm)){
				messages.add("������ʽ�ɹ���");
			}else{
				messages.add("������ʽʧ�ܣ�");
			}
//			MCellFormuForm mCellForumForm = new MCellFormuForm();
//			mCellForumForm.setChildRepId(reportCheckForm.getTemplateId());
//			mCellForumForm.setVersionId(reportCheckForm.getVersionId());
//			mCellForumForm.setCellFormu(reportCheckForm.getFormulaValue());
//			mCellForumForm.setCellFormuView(reportCheckForm.getFormulaName());
//			mCellForumForm.setFormuType(Integer.valueOf(reportCheckForm.getValidateTypeId()));
//			cells.add(mCellForumForm);
//			FitechTemplate template = new FitechTemplate();
//			if(template.validateAFU(mCellForumForm, locale, resources,reportFlg)){	
//				if(StrutsAFCellFormuDelegate.savePatch(cells)){
//					messages.add("������ʽ�ɹ���");
//				}else{
//					messages.add("������ʽʧ�ܣ�");
//				}
//			}else{
//				messages.add("��ʽ���Ϸ�!");
//			}
		}catch(Exception e){
			log.printStackTrace(e);
			messages.add("������ʽʧ�ܣ�");
		}
		request.setAttribute(Config.MESSAGES, messages);
		request.setAttribute("templateId", reportCheckForm.getTemplateId());
		request.setAttribute("versionId", reportCheckForm.getVersionId());
		request.setAttribute("reportCheckForm", reportCheckForm);
		
		return new ActionForward("/template/mod/editBJGXInit.do?childRepId=" + 
				reportCheckForm.getTemplateId() + "&versionId=" + reportCheckForm.getVersionId()+"&reportName="+reportName );
	}
	
}