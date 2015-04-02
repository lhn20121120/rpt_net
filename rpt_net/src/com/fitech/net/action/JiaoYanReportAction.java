package com.fitech.net.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.gznx.po.AfTemplate;
import com.fitech.gznx.po.AfTemplateShape;
import com.fitech.gznx.po.AfTemplateShapeId;
import com.fitech.net.adapter.StrutsExcelData;

public class JiaoYanReportAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		FitechMessages messages=new FitechMessages();
		String templateId=request.getParameter("childRepId");
		String next = request.getParameter("next");
		if(templateId==null || templateId.equals(""))
			templateId=(String)request.getAttribute("childRepId");
		String versionId=request.getParameter("versionId");
		AfTemplate af = StrutsExcelData.getTemplateSimple(versionId, templateId);
		String reportName=af!=null?af.getTemplateName():request.getParameter("reportName");
		String type=request.getParameter("type");
		String strs=request.getParameter("strs");
		if(strs!=null && !strs.equals("") && strs.length()>0 && strs.indexOf(",")!=-1){
			boolean res = deleteAF(strs.split("-"));
			messages.add(res?"É¾³ý³É¹¦!":"É¾³ýÊ§°Ü");
		}
		if(messages.getSize()>0)
			request.setAttribute(Config.MESSAGES,messages);
		request.setAttribute("aflist",StrutsExcelData.getTemplateDate(versionId, templateId));
		request.setAttribute("templateId",templateId );
		request.setAttribute("versionId",versionId );
		request.setAttribute("reportName", reportName);
		if(next!=null && !next.equals("")){
			request.setAttribute("next", next);
		}
		return mapping.findForward(type);
	}
	
	private boolean deleteAF(String[] strs){
		List<AfTemplateShape> list=new ArrayList<AfTemplateShape>();
		if(strs!=null && strs.length>0){
			for (int i = 0; i < strs.length; i++) {
				if(strs[i]!=null && !strs[i].equals("") && strs[i].length()>0){
					String[] substrs=strs[i].split(",");
					if(substrs!=null && substrs.length>=2){
						AfTemplateShape as=new AfTemplateShape();
						if(as.getId()==null)
							as.setId(new AfTemplateShapeId());
						as.getId().setTemplateId(substrs[0]);
						as.getId().setVersionId(substrs[1]);
						as.getId().setCellName(substrs[2]);
						list.add(as);
					}
				}
			}
			if(!list.isEmpty() && list.size()>0){
				return StrutsExcelData.deleteAFlist(list);
			}
		}
		return false;
	}

}
