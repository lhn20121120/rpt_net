package com.fitech.gznx.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechUtil;
import com.fitech.gznx.po.AfTemplate;
import com.fitech.gznx.po.AfTemplateOuterRep;
import com.fitech.gznx.service.AFOrgDelegate;
import com.fitech.gznx.service.StrutsTemplateOrgOuterRelationDelegate;
import com.fitech.net.adapter.StrutsExcelData;
import com.fitech.net.adapter.StrutsOrgNetDelegate;
import com.fitech.net.hibernate.OrgNet;

public class PreEditBSSDAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
//		System.out.println("汇总设定 action ");
		HttpSession session = request.getSession();
		Operator operator = (Operator) session
				.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
		/** 报表选中标志 **/
		String reportFlg = "0";
		
		if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
			reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
		}
		String curOrgId = "";
		if (operator != null) {
			curOrgId = operator.getOrgId();
		}
		String childRepId = request.getParameter("childRepId");
		String versionId = request.getParameter("versionId");
		String orgId = request.getParameter("orgId");
		String orgIds = request.getParameter("orgIds");
		
		AfTemplate af = StrutsExcelData.getTemplateSimple(versionId, childRepId);
		String ReportName=af!=null?af.getTemplateName():request.getParameter("reportName");
        //--------------------------------------------
        // gongming 2008-07-26
        String curPage = request.getParameter("curPage");
        if(StringUtils.isNotEmpty(curPage));
            request.setAttribute("curPage", curPage);
		if (orgId == null)
			orgId = curOrgId;

		if(ReportName != null) {
			request.setAttribute("ReportName",ReportName);
		}
		/**从Session中消除机构列表信息**/
		if(session.getAttribute("SelectedOrgIds")!=null) session.setAttribute("SelectedOrgIds",null);
		/**
		 * 报送范围是否包含上级行标志
		 */
		boolean flag1 = false;
		if (session.getAttribute("SelectedOrgIds") == null) {
			HashMap hMap = new HashMap();

			List selectedOrgFromDB = StrutsTemplateOrgOuterRelationDelegate.findAll(
					childRepId, versionId);
			if (selectedOrgFromDB != null && selectedOrgFromDB.size() > 0) {
				for (int i = 0; i < selectedOrgFromDB.size(); i++) {
					AfTemplateOuterRep item = (AfTemplateOuterRep) selectedOrgFromDB
							.get(i);
					//判断是否包含父节点
					if(item.getId().getOrgId().trim().equals(orgId)){
						flag1 = true ;
					}
					hMap.put(item.getId().getOrgId().trim(), "ok");
				}
			}
			session.setAttribute("SelectedOrgIds", hMap);

		}
		// 根据机构ID,取得机构名称;
		String orgName = StrutsOrgNetDelegate.getOrgName(orgId);
		String xmlorgname = FitechUtil.getObjectName()+"bsfw.xml";
		String fileName = Config.WEBROOTPATH + "xml" + File.separator + xmlorgname; // 创建一个XML文件
		request.setAttribute("childRepId", childRepId);
		request.setAttribute("versionId", versionId);
		request.setAttribute("orgId", orgId);
		request.setAttribute("curOrgId", curOrgId);
		List lowerOrgList = StrutsOrgNetDelegate.selectLowerOrgList(orgId,
				session);
		/***
		 * 20121224人行修改，无子机构也能显示
		 */
//		if (lowerOrgList == null || lowerOrgList.size() == 0) {
//			request.setAttribute("isNull", "isNull");
//		}
		// if (lowerOrgList != null && lowerOrgList.size() != 0) {
		// request.setAttribute("lowerOrgList", lowerOrgList);
		// }
		if (orgIds != null && !orgIds.equals("")) {
			String OrgIds[] = orgIds.split(",");
			HashMap hMap = null;
			if (session.getAttribute("SelectedOrgIds") == null) {
				hMap = new HashMap();

			} else {
				hMap = (HashMap) session.getAttribute("SelectedOrgIds");
				session.removeAttribute("SelectedOrgIds");
			}

			// 删除子机构
			if (orgId != null && !orgId.equals("")) {
				for (int i = 0; i < lowerOrgList.size(); i++) {
					hMap.remove(((OrgNet) lowerOrgList.get(i)).getOrgId());
				}
			}
			// 加上选中的项
			if (orgIds != null)
				for (int i = 0; i < OrgIds.length; i++) {
					if (OrgIds[i].trim() != null
							&& !OrgIds[i].trim().equals(""))
						hMap.put(OrgIds[i].trim(), "ok");

				}
			session.setAttribute("SelectedOrgIds", hMap);
		}

		org.dom4j.Document document = org.dom4j.DocumentHelper.createDocument();
		document.setXMLEncoding("GB2312");

		Element rootElement = document.addElement("tree");
		rootElement.addAttribute("id", "0");

		Element oneElement = rootElement.addElement("item");
		oneElement.addAttribute("text", orgName);
		oneElement.addAttribute("id", orgId);
		oneElement.addAttribute("open", "1");
		if(flag1){
			oneElement.addAttribute("checked", "1");
		}
		
		getNode(orgId, session, oneElement,reportFlg);
		try {
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding("GB2312");
			XMLWriter output = new XMLWriter();
			output = new XMLWriter(new FileOutputStream(fileName), format);
			output.write(document);
			output.flush();
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		request.setAttribute("xmlorgname", xmlorgname);
		//判断是 查看还是修改的标志。
		String opration = request.getParameter("opration");
		if(null!=opration&&opration.equals("edit"))
			return mapping.findForward("edit");
		else if(null!=opration&&opration.equals("next"))
			return mapping.findForward("next");
		else
			return mapping.findForward("view");
	}

	private boolean flag = true;

	private void addChild(Element e, OrgNet orgNet, HttpSession session) {
		if (orgNet != null) {
			String id = orgNet.getOrgId();
			Element element = e;
			if (flag) {
				element = e.addElement("item");
				element.addAttribute("text", orgNet.getOrgName());
				element.addAttribute("id", orgNet.getOrgId());
				element.addAttribute("open", "1");
				if (orgNet.getPreOrgId().equals("true")) {
					element.addAttribute("checked", "1");
				}

			}
			List childList = StrutsOrgNetDelegate.selectLowerOrgList(id,
					session);

			if (childList != null && !childList.equals("")) {
				for (int i = 0; i < childList.size(); i++) {
					OrgNet o = (OrgNet) childList.get(i);
					flag = false;
					Element child = element.addElement("item");
					child.addAttribute("text", o.getOrgName());
					child.addAttribute("id", o.getOrgId());
					child.addAttribute("open", "1");
					if (o.getPreOrgId().equals("true")) {
						child.addAttribute("checked", "1");
					}
					addChild(child, o, session);
				}
				flag = true;
			}
		}
	}

	private void getNode(String orgId, HttpSession session, Element rootElement, String reportFlg) {
		List lowerOrgList = null;
		if(!reportFlg.equals(com.fitech.gznx.common.Config.CBRC_REPORT)){
			lowerOrgList = AFOrgDelegate.selectLowerOrgList(orgId,
					session);	
		}else{
			lowerOrgList = StrutsOrgNetDelegate.selectLowerOrgList(orgId,
					session);
		}
		
		if (lowerOrgList != null && !lowerOrgList.equals("")) {
			for (int i = 0; i < lowerOrgList.size(); i++) {
				OrgNet o = (OrgNet) lowerOrgList.get(i);
				try {
					addChild(rootElement, o, session);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

}
