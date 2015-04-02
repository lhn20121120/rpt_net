package com.cbrc.smis.action;

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

import com.cbrc.smis.adapter.StrutsMChildReportDelegate;
import com.cbrc.smis.adapter.StrutsMRepRangeDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.MChildReportForm;
import com.cbrc.smis.form.MRepRangeForm;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechUtil;
import com.fitech.gznx.po.AfOrg;
import com.fitech.gznx.service.AFOrgDelegate;
import com.fitech.net.adapter.StrutsOrgNetDelegate;
import com.fitech.net.hibernate.OrgNet;


public final class PreEditBSFWAction extends Action {
	/**
	 * 已使用Hibernate 卞以刚 2011-12-22
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
        //--------------------------------------------
        // gongming 2008-07-26
        String curPage = request.getParameter("curPage");
        if(StringUtils.isNotEmpty(curPage));
            request.setAttribute("curPage", curPage);
		if (orgId == null)
			orgId = curOrgId;
		
		/**已使用hibernate 卞以刚 2011-12-21**/
		MChildReportForm mChildReportForm=new StrutsMChildReportDelegate().getMChildReport(
				childRepId,versionId);
		if(mChildReportForm != null) request.setAttribute("ReportName",mChildReportForm.getReportName());
		
		/**从Session中消除机构列表信息**/
		if(session.getAttribute("SelectedOrgIds")!=null) session.setAttribute("SelectedOrgIds",null);
		/**
		 * 报送范围是否包含上级行标志
		 */
		boolean flag1 = false;
		if (session.getAttribute("SelectedOrgIds") == null) {
			HashMap hMap = new HashMap();
			
			/** 已使用Hibernate 卞以刚 2011-12-22**/
			List selectedOrgFromDB = StrutsMRepRangeDelegate.findAll(
					childRepId, versionId);
			if (selectedOrgFromDB != null && selectedOrgFromDB.size() > 0) {
				for (int i = 0; i < selectedOrgFromDB.size(); i++) {
					MRepRangeForm item = (MRepRangeForm) selectedOrgFromDB
							.get(i);
					//判断是否包含父节点
					if(item.getOrgId().trim().equals(orgId)){
						flag1 = true ;
					}
					hMap.put(item.getOrgId().trim(), "ok");
					
				}
			}
			session.setAttribute("SelectedOrgIds", hMap);
		}
		// 根据机构ID,取得机构名称;
		/** 已使用Hibernate 卞以刚 2011-12-22**/
		String orgName = StrutsOrgNetDelegate.getAfOrgName(orgId);
		
		String xmlorgname = FitechUtil.getObjectName()+ "bsfw.xml";  // 创建一个XML文件
		
		String fileName = Config.WEBROOTPATH + "xml" + Config.FILESEPARATOR + xmlorgname;
		request.setAttribute("childRepId", childRepId);
		request.setAttribute("versionId", versionId);
		request.setAttribute("orgId", orgId);
		request.setAttribute("curOrgId", curOrgId);
		/**已使用hibernate 卞以刚 2011-12-22**/
		List lowerOrgList = StrutsOrgNetDelegate.selectLowerAfOrgList(orgId,
				session);
		if (lowerOrgList == null || lowerOrgList.size() == 0) {
			request.setAttribute("isNull", "isNull");
		}
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
					hMap.remove(((AfOrg) lowerOrgList.get(i)).getOrgId());
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

		return mapping.findForward("view");
	}

	private boolean flag = true;

	private void addChild(Element e, AfOrg orgNet, HttpSession session) {
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
			List childList = AFOrgDelegate.selectLowerAfOrgList(id,
					session);

			if (childList != null && !childList.equals("")) {
				for (int i = 0; i < childList.size(); i++) {
					AfOrg o = (AfOrg) childList.get(i);
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
		List lowerOrgList = AFOrgDelegate.selectLowerAfOrgList(orgId,
				session);	
		if (lowerOrgList != null && !lowerOrgList.equals("")) {
			for (int i = 0; i < lowerOrgList.size(); i++) {
				AfOrg o = (AfOrg) lowerOrgList.get(i);
				try {
					addChild(rootElement, o, session);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

}
