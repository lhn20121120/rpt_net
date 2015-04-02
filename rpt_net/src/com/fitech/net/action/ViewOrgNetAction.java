package com.fitech.net.action;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
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

import com.cbrc.smis.common.Config;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.fitech.net.adapter.StrutsOrgNetDelegate;
import com.fitech.net.form.OrgNetForm;
import com.fitech.net.hibernate.OrgNet;

/**
 * 显示机构的信息
 * 
 * @author WHIBM
 * 
 */
public final class ViewOrgNetAction extends Action {

	private FitechException log = new FitechException(ViewOrgNetAction.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		HttpSession session = request.getSession();
		Operator operator = (Operator) session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
		

		// 查询所有子机构
		List resList = new ArrayList();
		this.getAllSubOrgs(resList, operator.getOrgId());

		String orgIds = "";
		if (resList != null && resList.size() > 0) {
			for (Iterator iter = resList.iterator(); iter.hasNext();) {
				OrgNetForm orgNetForm = (OrgNetForm) iter.next();
				orgIds = orgIds.equals("") ? orgNetForm.getOrg_id() : orgIds + "," + orgNetForm.getOrg_id();
			}
			if(operator.isSuperManager() ){
				orgIds = orgIds.equals("") ? operator.getOrgId(): orgIds + "," + operator.getOrgId();
			}
		}
		request.setAttribute("orgIds", orgIds);

		String orgId = null;
		String orgName = null;

		// 得到最机构信息

		OrgNetForm onf = StrutsOrgNetDelegate.getMaxOrg();

		if (onf != null) {
			orgId = onf.getOrg_id();
			orgName = onf.getOrg_name();
		} else {
			orgId = operator.getOrgId();
			orgName = operator.getOrgName();
		}

		/* 是否是返回的页面 */
		String isBack = request.getParameter("back");
		if (isBack != null) {
			if (isBack.equals("_back"))
				return mapping.findForward("view");
		}

		// 查询机构信息

		String orgInfos = this.getOrgInfo(orgId);

		String fileName = Config.WEBROOTPATH + "xml" + Config.FILESEPARATOR + "orgInfo.xml"; // 创建一个XML文件
		org.dom4j.Document document = org.dom4j.DocumentHelper.createDocument();
		document.setXMLEncoding("GBK");

		Element rootElement = document.addElement("tree");
		rootElement.addAttribute("id", "0");

		// 创建一个根目录
		Element oneElement = rootElement.addElement("item");
		oneElement.addAttribute("text", orgName);
		oneElement.addAttribute("id", orgId);
		oneElement.addAttribute("orgInfo", orgInfos);
		oneElement.addAttribute("open", "1");
		oneElement.addAttribute("checked", "1");
		getNode(orgId, session, oneElement);

		try {
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding("GBK");
			XMLWriter output = new XMLWriter(new FileWriter(new File(fileName)), format);
			output.write(document);
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 是否由新增页面跳转来的
		String newAddView = (String) session.getAttribute("new_add_view");
		if (newAddView != null) {
			if (newAddView.equals("new_add_view"))
				return mapping.findForward("newAddView");
		}

		// 是否由修改页面跳转来的
		String updateView = (String) session.getAttribute("update_view");
		if (updateView != null) {
			if (updateView.equals("update_view"))
				return mapping.findForward("updateView");
		}

		return mapping.findForward("view");
	}

	private boolean flag = true;

	/**
	 * 添加所有子结点
	 * 
	 * @param e
	 * @param orgNet
	 * @param session
	 */
	private void addChild(Element e, OrgNet orgNet, HttpSession session) {
		if (orgNet != null) {
			String id = orgNet.getOrgId();
			String orgInfo = getOrgInfo(id);
			Element element = e;
			if (flag) {
				element = e.addElement("item");
				element.addAttribute("text", orgNet.getOrgName());
				element.addAttribute("id", orgNet.getOrgId());
				element.addAttribute("orgInfo", orgInfo);

			}
			List childList = StrutsOrgNetDelegate.selectLowerOrgList(id, session);
			String orgInfos = null;
			if (childList != null && !childList.equals("")) {
				for (int i = 0; i < childList.size(); i++) {
					OrgNet o = (OrgNet) childList.get(i);
					orgInfos = getOrgInfo(o.getOrgId());
					flag = false;
					Element child = element.addElement("item");
					child.addAttribute("text", o.getOrgName());
					child.addAttribute("id", o.getOrgId());
					child.addAttribute("orgInfo", orgInfos);
					addChild(child, o, session);
				}
				flag = true;
			}
		}
	}

	/**
	 * 得到XML文件的结点
	 * 
	 * @param orgId
	 * @param session
	 * @param rootElement
	 */
	private void getNode(String orgId, HttpSession session, Element rootElement) {

		List lowerOrgList = StrutsOrgNetDelegate.selectLowerOrgList(orgId, session);
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

	/**
	 * 查询机构信息
	 * 
	 * @param orgId
	 * @return 格式化显示机构的详细
	 */
	private String getOrgInfo(String orgId) {

		String orgInfos = null;
		OrgNetForm orgNetForm = StrutsOrgNetDelegate.selectOrgInfo(orgId);

		if (orgNetForm != null) {
			String org_id = orgNetForm.getOrg_id();
			String org_type = orgNetForm.getOrg_type_name();
			String pre_org = orgNetForm.getPre_org_name();
			String org_region = orgNetForm.getRegion_name();

			if (pre_org == null)
				pre_org = "无";
			orgInfos = "机构ID:       " + org_id + "\n机构类型:   " + org_type + "\n上级机构:   " + pre_org + "\n机构地区:   " + org_region;
		}
		return orgInfos;
	}

	/**
	 * 递归得到所有子机构
	 * 
	 * @param orgList
	 */
	private void getAllSubOrgs(List resList, String orgId) {
		List list = StrutsOrgNetDelegate.selectAllOrg(orgId);
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				OrgNetForm orgNetForm = (OrgNetForm) list.get(i);
				resList.add(orgNetForm);
				this.getAllSubOrgs(resList, orgNetForm.getOrg_id());
			}
		}
	}

	// private boolean flag = true;
	//
	// private void addChild(Element e, OrgNet orgNet, HttpSession session) {
	//
	// if (orgNet != null) {
	// String id = orgNet.getOrgId();
	// String orgInfo = getOrgInfo(id);
	// Element element = e;
	// if (flag) {
	// element = e.addElement("item");
	// element.addAttribute("text", orgNet.getOrgName());
	// element.addAttribute("id", orgNet.getOrgId());
	// element.addAttribute("orgInfo", orgInfo);
	// }
	//
	// List childList = StrutsOrgNetDelegate.selectLowerOrgList(id, session);
	// String orgInfos = null;
	// if (childList != null && !childList.equals("")) {
	// for (int i = 0; i < childList.size(); i++) {
	// OrgNet o = (OrgNet) childList.get(i);
	// orgInfos = getOrgInfo(o.getOrgId());
	// flag = false;
	// Element child = element.addElement("item");
	// child.addAttribute("text", o.getOrgName());
	// child.addAttribute("id", o.getOrgId());
	// child.addAttribute("orgInfo", orgInfos);
	// addChild(child, o, session);
	// }
	// flag = true;
	// }
	// }
	// }
	//
	// int regIdFlag = 1; /* 地区ID的自增变量 */
	//
	// /**
	// * 加地区
	// *
	// * @param element
	// * @param mRegForm
	// * 地区信息
	// * @param session
	// * @param regionInOrg
	// * 机构信息
	// * @param flags
	// * 标志量:true 显示机构 false 显示地区
	// * @param showAll
	// * true:有机构,也有下一级地区,平行的
	// * @throws Exception
	// */
	// private void addRegion(Element element, MRegionForm mRegForm, HttpSession
	// session, List regionInOrg, boolean flags, boolean showAll) throws
	// Exception {
	//
	// /* 把地区加到document中 */
	// Element element2 = element;
	// String regionName = mRegForm.getRegion_name();
	// String ids = "*!*" + regIdFlag++;
	//
	// if (mRegForm != null) {
	// element2 = element.addElement("item");
	// element2.addAttribute("id", ids);
	// element2.addAttribute("text", regionName);
	// element2.addAttribute("orgInfo", getRegionInfo(mRegForm));
	// }
	//
	// /* flags为true:该地区有机构 */
	// if (flags) {
	// /* 把列表中没有上级的机构取出来 */
	// if (regionInOrg != null && regionInOrg.size() > 0) {
	// List notPreOrg = removeNotPreOrg(regionInOrg);
	// if (notPreOrg != null && notPreOrg.size() > 0) {
	// for (int i = 0; i < notPreOrg.size(); i++) {
	// OrgNet onet = (OrgNet) notPreOrg.get(i);
	// addChild(element2, onet, session);
	// }
	// }
	// }
	//
	// /* 如果该机构有机构,也有地区,都要显示 */
	// if (showAll) {
	// isShowNextRegion(element2, mRegForm, session);
	// }
	// } else {
	// isShowNextRegion(element2, mRegForm, session);
	// }
	// }
	//
	// /**
	// * 显示下一级地区
	// *
	// * @param element2
	// * @param mRegForm
	// * @param session
	// */
	// public void isShowNextRegion(Element element2, MRegionForm mRegForm,
	// HttpSession session) {
	//
	// Element ee = element2;
	// List list =
	// StrutsMRegionDelegate.getLowerMRegionForms(mRegForm.getRegion_id());
	// if (list != null && list.size() > 0) {
	// isShowRegion(list, session, ee);
	// }
	// }
	//
	// /**
	// * 得到下个结点
	// *
	// * @param orgId
	// * @param session
	// * @param rootElement
	// */
	// private void getNode(String orgId, HttpSession session, Element
	// rootElement) {
	//
	// /* 根据机构ID得到该机构的下一级地区 */
	// List regionList = StrutsMRegionDelegate.getNextRegionList(orgId);
	// if (regionList != null && regionList.size() > 0) {
	// isShowRegion(regionList, session, rootElement);
	// }
	// }
	//
	// /**
	// * 是否显示地区
	// *
	// * @param regionList
	// * @param session
	// * @param rootElement
	// */
	// private void isShowRegion(List regionList, HttpSession session, Element
	// rootElement) {
	//
	// /* 如果这个地区下没有机构就不处理(不显示) */
	// try {
	// for (int i = 0; i < regionList.size(); i++) {
	// MRegionForm mr = (MRegionForm) regionList.get(i);
	// /* 查找该地区下的所有机构 */
	// List regionInOrg =
	// StrutsOrgNetDelegate.getOrgByRegionId(mr.getRegion_id());
	//
	// if (regionInOrg == null || regionInOrg.size() <= 0) {
	// /* 该地区的下一级地区 */
	// List list =
	// StrutsMRegionDelegate.getLowerMRegionForms(mr.getRegion_id());
	// if (list == null || list.size() <= 0) {
	// continue; /* 不显示该地区 */
	// } else {
	// boolean flag = false;
	// for (int j = 0; j < list.size(); j++) {
	// MRegionForm mrg = (MRegionForm) list.get(j);
	//
	// /* 查找该地区下一级地区有没有机构 */
	// List riOrg = StrutsOrgNetDelegate.getOrgByRegionId(mrg.getRegion_id());
	// if (riOrg != null || riOrg.size() > 0) {
	// flag = true; /* 有机构 */
	// break;
	// }
	// }
	//
	// /* 如果有机构,就显示地区 */
	// if (!flag)
	// continue;
	// else
	// addRegion(rootElement, mr, session, regionInOrg, false, false);
	// }
	// } else {
	// // 如果该机构有机构,也有地区,都要显示*/
	// boolean showAll = false; /* 是否还有地区 */
	// List li = StrutsMRegionDelegate.getLowerMRegionForms(mr.getRegion_id());
	//
	// if (li != null && li.size() > 0) {
	// showAll = true;
	// }
	// addRegion(rootElement, mr, session, regionInOrg, true, showAll);
	// }
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	//
	// /**
	// * 把列表中没有上级的机构取出来
	// *
	// * @param regionInOrg
	// * @return
	// */
	// private List removeNotPreOrg(List regionInOrg) {
	//
	// List result = new ArrayList();
	// String orgIds = "";
	//
	// for (int i = 0; i < regionInOrg.size(); i++) {
	// OrgNet onet = (OrgNet) regionInOrg.get(i);
	// orgIds += onet.getOrgId() + ",";
	// }
	// for (int i = 0; i < regionInOrg.size(); i++) {
	// OrgNet ont = (OrgNet) regionInOrg.get(i);
	// int index = orgIds.indexOf(ont.getPreOrgId().trim());
	// if (index < 0) {
	// result.add(ont);
	// }
	// }
	// return result;
	// }
	//
	//	
	//
	// /**
	// * 地区信息
	// *
	// * @param orgId
	// * @return 格式化显示地区的详细
	// */
	// private String getRegionInfo(MRegionForm mr) {
	//
	// String regions = "";
	// if (mr != null) {
	// String regionName = mr.getRegion_name();
	// String pre_region = mr.getPre_region_name();
	// if (pre_region == null)
	// pre_region = "无";
	// regions = "地区名称: " + regionName + "\n所属地区: " + pre_region;
	// }
	//
	// return regions;
	// }
	//

}
