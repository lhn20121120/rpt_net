package com.cbrc.smis.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.cbrc.smis.adapter.StrutsMChildReportDelegate;
import com.cbrc.smis.adapter.StrutsMRepRangeDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.MChildReportForm;
import com.cbrc.smis.form.MRepRangeForm;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechUtil;
import com.fitech.gznx.action.SystemSchemaBaseAction;
import com.fitech.gznx.service.AFOrgDelegate;
import com.fitech.net.adapter.StrutsOrgNetDelegate;
import com.fitech.net.hibernate.OrgNet;

public final class ViewBSFWAction extends SystemSchemaBaseAction {
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
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		HttpSession session = request.getSession();
		Operator operator = (Operator) session
				.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
		String curOrgId = "";
		if (operator != null) {
			curOrgId = operator.getOrgId();
		}
		String childRepId = request.getParameter("childRepId");
		String versionId = request.getParameter("versionId");
		String orgId = request.getParameter("orgId");
		String orgIds = request.getParameter("orgIds");
		if (orgId == null)
			orgId = curOrgId;

		MChildReportForm mChildReportForm=new StrutsMChildReportDelegate().getMChildReport(
				childRepId,versionId);
		if(mChildReportForm != null) request.setAttribute("ReportName",mChildReportForm.getReportName());
		
		if (session.getAttribute("SelectedOrgIds") == null) {
			HashMap hMap = new HashMap();

			List selectedOrgFromDB = StrutsMRepRangeDelegate.findAll(
					childRepId, versionId);
			if (selectedOrgFromDB != null && selectedOrgFromDB.size() > 0) {
				for (int i = 0; i < selectedOrgFromDB.size(); i++) {
					MRepRangeForm item = (MRepRangeForm) selectedOrgFromDB
							.get(i);
					hMap.put(item.getOrgId().trim(), "ok");
				}
			}
			session.setAttribute("SelectedOrgIds", hMap);
		}
		// 根据机构ID,取得机构名称;
		String orgName = StrutsOrgNetDelegate.getOrgName(orgId);
		
		String xmlorgname = FitechUtil.getObjectName()+ "bsfw.xml";  // 创建一个XML文件

		String fileName = Config.WEBROOTPATH + "xml" + File.separator + xmlorgname;
		
		request.setAttribute("childRepId", childRepId);
		request.setAttribute("versionId", versionId);
		request.setAttribute("orgId", orgId);
		request.setAttribute("curOrgId", curOrgId);
		List lowerOrgList = StrutsOrgNetDelegate.selectLowerOrgList(orgId,
				session);
		/**
		 * 该处原先是：无子机构则不显示机构树
		 * 不合理，去除
		 */
//		if (lowerOrgList == null || lowerOrgList.size() == 0) {
//			request.setAttribute("isNull", "isNull");
//		}

		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding("GB2312");

		Element rootElement = document.addElement("tree");
		rootElement.addAttribute("id", "0");

		Element oneElement = rootElement.addElement("item");
		oneElement.addAttribute("text", orgName);
		oneElement.addAttribute("id", orgId);
		oneElement.addAttribute("open", "1");
		oneElement.addAttribute("checked", "1");
		getNode(orgId, session, oneElement);
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
		if(Config.TEMPLATE_MANAGE_FLAG == 1){
			return mapping.findForward("schemaFlag");
		}else{
			return mapping.findForward("view");
		}
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
				if (orgNet.getPreOrgId().equals("true")) {
					element.addAttribute("checked", "1");
					element.addAttribute("open", "1");
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
					if (o.getPreOrgId().equals("true")) {
						child.addAttribute("checked", "1");
						child.addAttribute("open", "1");
					}
					addChild(child, o, session);
				}
				flag = true;
			}
		}
	}

	private void getNode(String orgId, HttpSession session, Element rootElement) {

//		List lowerOrgList = StrutsOrgNetDelegate.selectLowerOrgList(orgId,
//				session);
		List lowerOrgList = AFOrgDelegate.selectLowerOrgList(orgId,
				session);	
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
