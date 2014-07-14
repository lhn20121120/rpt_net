package com.fitech.gznx.action;

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
import com.fitech.gznx.po.AfTemplateOrgRelation;
import com.fitech.gznx.service.AFOrgDelegate;
import com.fitech.gznx.service.StrutsTemplateOrgRelationDelegate;
import com.fitech.net.adapter.StrutsOrgNetDelegate;
import com.fitech.net.hibernate.OrgNet;

public class ViewPreBSFWAction extends Action {
	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-27
	 * Ӱ�����AfOrg OrgNet AfTemplateOrgRelation
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
		String reportName = request.getParameter("reportName");
		if (orgId == null)
			orgId = curOrgId;

		/**��Session�����������б���Ϣ**/
		if(session.getAttribute("SelectedOrgIds")!=null) session.setAttribute("SelectedOrgIds",null);
		
		request.setAttribute("ReportName",reportName);
		
		if (session.getAttribute("SelectedOrgIds") == null) {
			HashMap hMap = new HashMap();
			
			/**��ʹ��hibernate ���Ը� 2011-12-27
			 * Ӱ�����AfTemplateOrgRelation*/
			List selectedOrgFromDB = StrutsTemplateOrgRelationDelegate.findAll(
					childRepId, versionId);
			if (selectedOrgFromDB != null && selectedOrgFromDB.size() > 0) {
				for (int i = 0; i < selectedOrgFromDB.size(); i++) {
					AfTemplateOrgRelation item = (AfTemplateOrgRelation) selectedOrgFromDB
							.get(i);
					hMap.put(item.getId().getOrgId().trim(), "ok");
				}
			}
			session.setAttribute("SelectedOrgIds", hMap);
		}
		// ���ݻ���ID,ȡ�û�������;
		/**��ʹ��hibernate ���Ը� 2011-12-22
		 * Ӱ�����OrgNet */
		String orgName = StrutsOrgNetDelegate.getOrgName(orgId);
		String xmlorgname = FitechUtil.getObjectName()+"bsfw.xml";
		String fileName = Config.WEBROOTPATH + "xml" + File.separator + xmlorgname; // ����һ��XML�ļ�
		request.setAttribute("childRepId", childRepId);
		request.setAttribute("versionId", versionId);
		request.setAttribute("orgId", orgId);
		request.setAttribute("curOrgId", curOrgId);
		/**��ʹ��hibernate ���Ը� 2011-12-22
		 * Ӱ����� OrgNet*/
		List lowerOrgList = StrutsOrgNetDelegate.selectLowerOrgList(orgId,
				session);
		if (lowerOrgList == null || lowerOrgList.size() == 0) {
			request.setAttribute("isNull", "isNull");
		}

		if (orgIds != null && !orgIds.equals("")) {
			String OrgIds[] = orgIds.split(",");
			HashMap hMap = null;
			if (session.getAttribute("SelectedOrgIds") == null) {
				hMap = new HashMap();

			} else {
				hMap = (HashMap) session.getAttribute("SelectedOrgIds");
				session.removeAttribute("SelectedOrgIds");
			}

			// ɾ���ӻ���
			if (orgId != null && !orgId.equals("")) {
				for (int i = 0; i < lowerOrgList.size(); i++) {
					hMap.remove(((OrgNet) lowerOrgList.get(i)).getOrgId());
				}
			}
			// ����ѡ�е���
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
		oneElement.addAttribute("checked", "1");
		/**��ʹ��hibernate  ���Ը� 2011-12-21
		 * Ӱ�����AfOrg OrgNet**/
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
			// System.out.println(e.getMessage());
		}
		request.setAttribute("xmlorgname", xmlorgname);
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
	
	/**
	 * ��ʹ��hibernate  ���Ը� 2011-12-21
	 * Ӱ�����AfOrg OrgNet
	 * */
	private void getNode(String orgId, HttpSession session, Element rootElement) {
		/** ����ѡ�б�־ **/
		String reportFlg = "0";
		
		if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
			reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
		}
		List lowerOrgList = null;
		if(reportFlg.equals(com.fitech.gznx.common.Config.PBOC_REPORT)){
			/**��ʹ��hibernate  ���Ը� 2011-12-21
			 * Ӱ�����AfOrg**/
			lowerOrgList = AFOrgDelegate.selectLowerOrgList(orgId,
					session);	
		}else{
			/**��ʹ��hibernate ���Ը� 2011-12-22
			 * Ӱ����� OrgNet**/
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
