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
 * ��ʾ��������Ϣ
 * 
 * @author WHIBM
 * 
 */
public final class ViewOrgNetAction extends Action {

	private FitechException log = new FitechException(ViewOrgNetAction.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		HttpSession session = request.getSession();
		Operator operator = (Operator) session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
		

		// ��ѯ�����ӻ���
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

		// �õ��������Ϣ

		OrgNetForm onf = StrutsOrgNetDelegate.getMaxOrg();

		if (onf != null) {
			orgId = onf.getOrg_id();
			orgName = onf.getOrg_name();
		} else {
			orgId = operator.getOrgId();
			orgName = operator.getOrgName();
		}

		/* �Ƿ��Ƿ��ص�ҳ�� */
		String isBack = request.getParameter("back");
		if (isBack != null) {
			if (isBack.equals("_back"))
				return mapping.findForward("view");
		}

		// ��ѯ������Ϣ

		String orgInfos = this.getOrgInfo(orgId);

		String fileName = Config.WEBROOTPATH + "xml" + Config.FILESEPARATOR + "orgInfo.xml"; // ����һ��XML�ļ�
		org.dom4j.Document document = org.dom4j.DocumentHelper.createDocument();
		document.setXMLEncoding("GBK");

		Element rootElement = document.addElement("tree");
		rootElement.addAttribute("id", "0");

		// ����һ����Ŀ¼
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

		// �Ƿ�������ҳ����ת����
		String newAddView = (String) session.getAttribute("new_add_view");
		if (newAddView != null) {
			if (newAddView.equals("new_add_view"))
				return mapping.findForward("newAddView");
		}

		// �Ƿ����޸�ҳ����ת����
		String updateView = (String) session.getAttribute("update_view");
		if (updateView != null) {
			if (updateView.equals("update_view"))
				return mapping.findForward("updateView");
		}

		return mapping.findForward("view");
	}

	private boolean flag = true;

	/**
	 * ��������ӽ��
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
	 * �õ�XML�ļ��Ľ��
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
	 * ��ѯ������Ϣ
	 * 
	 * @param orgId
	 * @return ��ʽ����ʾ��������ϸ
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
				pre_org = "��";
			orgInfos = "����ID:       " + org_id + "\n��������:   " + org_type + "\n�ϼ�����:   " + pre_org + "\n��������:   " + org_region;
		}
		return orgInfos;
	}

	/**
	 * �ݹ�õ������ӻ���
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
	// int regIdFlag = 1; /* ����ID���������� */
	//
	// /**
	// * �ӵ���
	// *
	// * @param element
	// * @param mRegForm
	// * ������Ϣ
	// * @param session
	// * @param regionInOrg
	// * ������Ϣ
	// * @param flags
	// * ��־��:true ��ʾ���� false ��ʾ����
	// * @param showAll
	// * true:�л���,Ҳ����һ������,ƽ�е�
	// * @throws Exception
	// */
	// private void addRegion(Element element, MRegionForm mRegForm, HttpSession
	// session, List regionInOrg, boolean flags, boolean showAll) throws
	// Exception {
	//
	// /* �ѵ����ӵ�document�� */
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
	// /* flagsΪtrue:�õ����л��� */
	// if (flags) {
	// /* ���б���û���ϼ��Ļ���ȡ���� */
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
	// /* ����û����л���,Ҳ�е���,��Ҫ��ʾ */
	// if (showAll) {
	// isShowNextRegion(element2, mRegForm, session);
	// }
	// } else {
	// isShowNextRegion(element2, mRegForm, session);
	// }
	// }
	//
	// /**
	// * ��ʾ��һ������
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
	// * �õ��¸����
	// *
	// * @param orgId
	// * @param session
	// * @param rootElement
	// */
	// private void getNode(String orgId, HttpSession session, Element
	// rootElement) {
	//
	// /* ���ݻ���ID�õ��û�������һ������ */
	// List regionList = StrutsMRegionDelegate.getNextRegionList(orgId);
	// if (regionList != null && regionList.size() > 0) {
	// isShowRegion(regionList, session, rootElement);
	// }
	// }
	//
	// /**
	// * �Ƿ���ʾ����
	// *
	// * @param regionList
	// * @param session
	// * @param rootElement
	// */
	// private void isShowRegion(List regionList, HttpSession session, Element
	// rootElement) {
	//
	// /* ������������û�л����Ͳ�����(����ʾ) */
	// try {
	// for (int i = 0; i < regionList.size(); i++) {
	// MRegionForm mr = (MRegionForm) regionList.get(i);
	// /* ���Ҹõ����µ����л��� */
	// List regionInOrg =
	// StrutsOrgNetDelegate.getOrgByRegionId(mr.getRegion_id());
	//
	// if (regionInOrg == null || regionInOrg.size() <= 0) {
	// /* �õ�������һ������ */
	// List list =
	// StrutsMRegionDelegate.getLowerMRegionForms(mr.getRegion_id());
	// if (list == null || list.size() <= 0) {
	// continue; /* ����ʾ�õ��� */
	// } else {
	// boolean flag = false;
	// for (int j = 0; j < list.size(); j++) {
	// MRegionForm mrg = (MRegionForm) list.get(j);
	//
	// /* ���Ҹõ�����һ��������û�л��� */
	// List riOrg = StrutsOrgNetDelegate.getOrgByRegionId(mrg.getRegion_id());
	// if (riOrg != null || riOrg.size() > 0) {
	// flag = true; /* �л��� */
	// break;
	// }
	// }
	//
	// /* ����л���,����ʾ���� */
	// if (!flag)
	// continue;
	// else
	// addRegion(rootElement, mr, session, regionInOrg, false, false);
	// }
	// } else {
	// // ����û����л���,Ҳ�е���,��Ҫ��ʾ*/
	// boolean showAll = false; /* �Ƿ��е��� */
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
	// * ���б���û���ϼ��Ļ���ȡ����
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
	// * ������Ϣ
	// *
	// * @param orgId
	// * @return ��ʽ����ʾ��������ϸ
	// */
	// private String getRegionInfo(MRegionForm mr) {
	//
	// String regions = "";
	// if (mr != null) {
	// String regionName = mr.getRegion_name();
	// String pre_region = mr.getPre_region_name();
	// if (pre_region == null)
	// pre_region = "��";
	// regions = "��������: " + regionName + "\n��������: " + pre_region;
	// }
	//
	// return regions;
	// }
	//

}
