package com.fitech.net.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.smis.util.FitechMessages;
import com.fitech.gznx.po.AfTemplate;
import com.fitech.gznx.po.AfTemplateShape;
import com.fitech.gznx.po.AfTemplateShapeId;
import com.fitech.net.adapter.StrutsExcelData;
import com.fitech.net.adapter.StrutsJiaoyanData;

public class SaveDataJiaoYanReportAction extends Action {
	/**
	 * 将提交的单元格格数据字符串分割，循环调用保存数据的方法
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String strs=request.getParameter("strs");
		FitechMessages messages = new FitechMessages();
		String templateId=request.getParameter("templateId");
		String versionId=request.getParameter("versionId");
		AfTemplate af = StrutsExcelData.getTemplateSimple(versionId, templateId);
		String reportName=af!=null?af.getTemplateName():request.getParameter("reportName");
		String savetype=request.getParameter("savetype");
		if(strs!=null && strs.length()>0){
			String[] allstrs=strs.split("-");
			if(templateId!=null && versionId!=null){
				try {
					for (int i = 0; i < allstrs.length; i++) {
						String[] datas=allstrs[i].split("@td@");
						AfTemplateShape as=new AfTemplateShape();
						if(as.getId()==null)
							as.setId(new AfTemplateShapeId());
						as.getId().setCellName(datas[0].trim());
						as.getId().setTemplateId(templateId.trim());
						as.getId().setVersionId(versionId.trim());
						as.setCellContext(datas[1].trim());
						boolean re=StrutsJiaoyanData.isexists(as);
						if(as!=null && re==false){
							StrutsJiaoyanData.save(as);
						}
					}
					messages.add("保存成功!");
				} catch (Exception e) {
					e.printStackTrace();
					messages.add("保存失败!");
				}
			}
		}
		if(messages.getSize()>0)
			request.setAttribute(com.cbrc.smis.common.Config.MESSAGES, messages);
		request.setAttribute("childRepId",templateId );
		request.setAttribute("versionId", versionId);
		if(savetype!=null && savetype.equals("1"))
			return new ActionForward("/gznx/preSetHZSD.do?opration=next&ReportName="+reportName+"&childRepId="+templateId+"&versionId="+versionId);
		return mapping.findForward("success");
	}
}
