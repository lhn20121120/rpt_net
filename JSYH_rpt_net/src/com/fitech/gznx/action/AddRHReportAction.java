package com.fitech.gznx.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.MCellFormuForm;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.gznx.common.StringUtil;
import com.fitech.gznx.form.RhReportForm;
import com.fitech.gznx.po.AfTemplate;
import com.fitech.gznx.service.AfPboccellDelegate;
import com.fitech.net.adapter.StrutsExcelData;

public class AddRHReportAction extends Action {
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException
	{
		FitechMessages messages = new FitechMessages();
		RhReportForm reportForm = (RhReportForm) form;
		String templateId = (String)request.getAttribute("childRepID");
		String versionId = (String)request.getAttribute("versionID");
		if(templateId==null || templateId.equals(""))
			templateId=request.getParameter("templateId");
		if(versionId==null || versionId.equals(""))
			versionId=request.getParameter("versionId");
		AfTemplate af = StrutsExcelData.getTemplateSimple(versionId, templateId);
		String reportName=af!=null?af.getTemplateName():(String)request.getAttribute("reportName");
		if(!StringUtil.isEmpty(templateId)){
			reportForm.setTemplateId(templateId);
		}
		if(!StringUtil.isEmpty(reportName)){
			reportForm.setTemplateName(reportName);
		}
		if(!StringUtil.isEmpty(versionId)){
			reportForm.setVersionId(versionId);
		}
		String saveFlg = request.getParameter("saveFlg");
		boolean result = false;
		if(!StringUtil.isEmpty(saveFlg) && saveFlg.equals("save")){
			result = AfPboccellDelegate.save(reportForm);
			if(result){
				messages.add("±£´æ³É¹¦");
			}else{
				messages.add("±£´æÊ§°Ü");
			}
		} else if(!StringUtil.isEmpty(saveFlg) && saveFlg.equals("del")){
			String delcolId = request.getParameter("deletecolId");
			reportForm.setColId(delcolId);
			result = AfPboccellDelegate.deleteCell(reportForm);
			if(result){
				messages.add("É¾³ý³É¹¦");
			}else{
				messages.add("É¾³ýÊ§°Ü");
			}
		}
		
		String deleteIndex = request.getParameter("deleteIndex");
		List<RhReportForm> reportList =AfPboccellDelegate.getPbocCell(reportForm,deleteIndex);
		if(reportList !=null && reportList.size()>0){
			request.setAttribute(Config.RECORDS,reportList);
		}
		if(!StringUtil.isEmpty(deleteIndex) && Integer.parseInt(deleteIndex)>=0){
			RhReportForm rf = reportList.get(Integer.parseInt(deleteIndex));
			reportForm.setColId(rf.getColId());
			reportForm.setCurId(rf.getCurId());
			reportForm.setDanweiId(rf.getDanweiId());
			reportForm.setPsuziType(rf.getPsuziType());
			reportForm.setDataType(rf.getDataType());
		}
		if(!StringUtil.isEmpty(deleteIndex) && (Integer.parseInt(deleteIndex)>=0 || Integer.parseInt(deleteIndex)==-1)){
			request.setAttribute("addflg","1");
		}
		if(messages.getMessages() != null && messages.getMessages().size() > 0)		
			request.setAttribute(Config.MESSAGES,messages);
		request.setAttribute("ReportName",reportForm.getTemplateName());
		MCellFormuForm mCellForumForm = new MCellFormuForm();
		mCellForumForm.setReportName(reportForm.getTemplateName());
		mCellForumForm.setChildRepId(reportForm.getTemplateId());
		mCellForumForm.setVersionId(reportForm.getVersionId());
		
		request.setAttribute("ObjForm", mCellForumForm);
		return mapping.findForward("rhsb");
	}
}
