package com.fitech.gznx.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.MCellFormuForm;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechTemplate;
import com.fitech.gznx.form.ReportCheckForm;
import com.fitech.gznx.po.AfTemplateValiSche;
import com.fitech.gznx.service.AfTemplateValiScheDelegate;
import com.fitech.gznx.service.StrutsAFCellFormuDelegate;
import com.fitech.institution.adapter.AFValidateFormulaDelegate;
/**
 * 新增表内表间校验公式操作类
 * 
 * **/
public class AddValidateFormuAction extends Action {
	
	private FitechException log=new FitechException(this.getClass());
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		MessageResources resources = getResources(request);
		FitechMessages messages = new FitechMessages();
		Locale locale = getLocale(request);
		HttpSession session = request.getSession();
		List cells=new ArrayList();
		ReportCheckForm reportCheckForm = (ReportCheckForm) form;
		String reportName=request.getParameter("reportName");
		String reportFlg = "0";
		if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
			reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
		}
		try{
			MCellFormuForm mCellForumForm = new MCellFormuForm();
			mCellForumForm.setChildRepId(reportCheckForm.getTemplateId());
			mCellForumForm.setVersionId(reportCheckForm.getVersionId());
			mCellForumForm.setCellFormu(reportCheckForm.getFormulaValue());
			mCellForumForm.setCellFormuView(reportCheckForm.getFormulaName());
			mCellForumForm.setFormuType(Integer.valueOf(reportCheckForm.getValidateTypeId()));
			cells.add(mCellForumForm);
			FitechTemplate template = new FitechTemplate();
			
			String formula  = mCellForumForm.getCellFormu();
			List list  = AFValidateFormulaDelegate.findAllById(mCellForumForm.getChildRepId(), mCellForumForm.getVersionId());
			List Fromulas = AFValidateFormulaDelegate.getFormulasById(list);
			if(Fromulas.contains(formula)){
				messages.add("公式已经存在！");
			}else{
				if(reportFlg.equals("3") ){
					AfTemplateValiSche valiSchem = AfTemplateValiScheDelegate.findAfTemplateValiSche(reportCheckForm.getTemplateId(),reportCheckForm.getVersionId());
					reportFlg = valiSchem.getValidateSchemeId().toString();
				}
				if(template.validateAFU(mCellForumForm, locale, resources,reportFlg)){
					
						if(StrutsAFCellFormuDelegate.savePatch(cells)){
							messages.add("新增公式成功！");
						}else{
							messages.add("新增公式失败！");
						}
					
				}else{
					messages.add("公式不合法!");
				}
			}
		}catch(Exception e){
			log.printStackTrace(e);
		}
		request.setAttribute(Config.MESSAGES, messages);
		request.setAttribute("templateId", reportCheckForm.getTemplateId());
		request.setAttribute("versionId", reportCheckForm.getVersionId());
		request.setAttribute("reportCheckForm", reportCheckForm);
		return new ActionForward("/gznx/editBJGXInit.do?childRepId=" + 
				reportCheckForm.getTemplateId() + "&versionId=" + reportCheckForm.getVersionId()+"&reportName="+reportName );
	}
	
}
