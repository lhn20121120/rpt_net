package com.fitech.gznx.treexml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechUtil;


public class CreateOrgTreeByUserHZRule {
	
	private String orgId;
	private String templateId;
	
	public String getOrgId() {
		return orgId;
	}


	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}


	public String getTemplateId() {
		return templateId;
	}


	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}


	/**
	 * 
	 * title:该方法用于根据机构权限剪裁机构树后生成该用户的机构权限树
	 * 
	 * author:chenbing date:2008-2-20
	 * 
	 * @param operator
	 *            登陆用户对象
	 * @param purviewOrgList
	 *            权限机构 如果为NULL将视为所有机构
	 * @param xmlName
	 *            文件名称(返回的文件名有用户登录名+下划线和文件名称组成)
	 * @param isContailCollect
	 *            是否包含汇总机构
	 * @return
	 */
	public static String createByUser(String operatorId, List purviewOrgList,
			String xmlName, boolean isContailCollect) {

		String result = operatorId;

		result = result + "_" + xmlName + ".xml";

		String srcfilePath = Config.WEBROOTPATH + Config.FILESEPARATOR
				+ "tree_xml" + Config.FILESEPARATOR + "org_tree"
				+ Config.FILESEPARATOR + com.fitech.gznx.common.Config.BASE_ORG_TREEXML_NAME;

		String filePath = Config.WEBROOTPATH + Config.FILESEPARATOR
				+ "tree_xml" + Config.FILESEPARATOR + "org_tree"
				+ Config.FILESEPARATOR + result;

		try {

			File file = new File(filePath);

			if (file.exists())

				file.delete();

			File srcOrgFile = new File(srcfilePath);

			SAXReader saxReader = new SAXReader();

			InputStream input = new FileInputStream(srcOrgFile);

			Document srcDocument = saxReader.read(input, "GB2312"); // 源DOCUMENT

			Document dstDocument = DocumentHelper.createDocument(); // 目的DOCUMENT

			Element rootDst = dstDocument.addElement("tree"); // 建立目标文件跟接点

			rootDst.addAttribute("id", "0");

			List list = srcDocument.selectNodes("/tree/item");

			for (int i = 0; i < list.size(); i++) {

				Element srcElement = (Element) list.get(i);

				Element dstElement = rootDst; // 先等于目标根节点

				if ((purviewOrgList == null && operatorId
						.equals(com.fitech.gznx.common.Config.SUPERUSER))
						|| (purviewOrgList != null && purviewOrgList
								.contains(srcElement.attributeValue("id")))) {

					dstElement = rootDst.addElement("item");

					dstElement.addAttribute("text", srcElement
							.attributeValue("text"));

					dstElement.addAttribute("id", srcElement
							.attributeValue("id"));
					dstElement.addAttribute("checked", srcElement
							.attributeValue("checked"));

					dstElement.addAttribute("open", "1");

				}
				// if (i == 0)
				CreateOrgTreeByUserHZRule.createByUserIterator(operatorId,
						srcElement, dstElement, purviewOrgList);

				if (!isContailCollect) // 如果不包括虚拟机构

					break;

			}

			dstDocument.setXMLEncoding("GB2312");

			OutputFormat format = OutputFormat.createPrettyPrint();

			format.setEncoding("GB2312");

			XMLWriter output = new XMLWriter(new FileOutputStream(file), format);

			output.write(dstDocument);

			output.flush();

			output.close();
			input.close();

		} catch (Exception e) {
			e.printStackTrace();

			result = null;
		}

		return result;
	}
	
	
	/**
	 * 
	 * title:该方法用于 author:chenbing date:2008-2-20
	 * 
	 * @param srcElement
	 * @param dstElement
	 * @param checkedList
	 */
	public static void createByUserIterator(String operatorId,
			Element srcElement, Element dstElement, List purviewOrgList) {

		List srcList = srcElement.selectNodes("item");

		if (srcList.size() == 0)
			return;

		for (int i = 0; i < srcList.size(); i++) {

			Element srcE = (Element) srcList.get(i);

			Element dstE = dstElement;

			if ((purviewOrgList == null && operatorId.equals(com.fitech.gznx.common.Config.SUPERUSER))

					|| (purviewOrgList != null && purviewOrgList.contains(srcE
							.attributeValue("id")))) {

				dstE = dstElement.addElement("item");

				dstE.addAttribute("text", srcE.attributeValue("text"));

				dstE.addAttribute("id", srcE.attributeValue("id"));

			}
			CreateOrgTreeByUserHZRule.createByUserIterator(operatorId, srcE, dstE,
					purviewOrgList);
		}
	}

	/**
	 * 
	 * title:该方法用于生成所有机构机构树 author:chenbing date:2008-2-19 汇总关系修改专用
	 * 
	 * @param operator
	 *            登陆用户对象
	 * @param checkedList
	 *            选中机构列表 当选中列表为NULL是则不选中
	 * @param isContainCollect
	 *            是否包含虚拟机构
	 * @return
	 */
	public static String createAllOrg(Operator operator, List checkedList,boolean isContainCollect) {

		String result = //operator.getUserName()
			FitechUtil.getObjectName();

		if (checkedList == null) // 是否为NULL将决定返回的名称

			result = result + "_allOrg_.xml";

		else

			result = result + "_allOrg_checked_.xml";

		String srcfilePath = Config.WEBROOTPATH + Config.FILESEPARATOR
				+ "tree_xml" + Config.FILESEPARATOR + "org_tree"
				+ Config.FILESEPARATOR + com.fitech.gznx.common.Config.BASE_ORG_TREEXML_NAME;

		String filePath = Config.WEBROOTPATH + Config.FILESEPARATOR
				+ "tree_xml" + Config.FILESEPARATOR + "org_tree"
				+ Config.FILESEPARATOR + result;
		
		try {

			File file = new File(filePath);

			if (file.exists())

				file.delete();
			
			File srcOrgFile = new File(srcfilePath);

			SAXReader saxReader = new SAXReader();

			InputStream input = new FileInputStream(srcOrgFile);

			Document srcDocument = saxReader.read(input, "GB2312"); // 源DOCUMENT

			Document dstDocument = DocumentHelper.createDocument(); // 目的DOCUMENT

			Element rootDst = dstDocument.addElement("tree"); // 建立目标文件跟接点

			rootDst.addAttribute("id", "0");

			List list = srcDocument.selectNodes("/tree/item");

			for (int i = 0; i < list.size(); i++) {

				Element srcElement = (Element) list.get(i);

				Element dstElement = rootDst.addElement("item");

				dstElement.addAttribute("text", srcElement
						.attributeValue("text"));

				dstElement.addAttribute("id", srcElement.attributeValue("id"));

				dstElement.addAttribute("open", "1");
				String orgId = srcElement.attributeValue("id");

				if (checkedList != null
						&& checkedList
								.contains(orgId))
					dstElement.addAttribute("checked", "1");

				CreateOrgTreeByUserHZRule.createAllOrgIterator(srcElement,
						dstElement, checkedList);

				if (isContainCollect) // 如果不包括虚拟机构
					break;

			}

			dstDocument.setXMLEncoding("GB2312");

			OutputFormat format = OutputFormat.createPrettyPrint();

			format.setEncoding("GB2312");

			XMLWriter output = new XMLWriter(new FileOutputStream(file), format);

			output.write(dstDocument);

			output.flush();

			output.close();

		} catch (Exception e) {

			result = null;
		}

		return result;
	}
	
	
	/***
	 * 修改生成机构树 
	 * @param operator
	 * @param checkedList
	 * @param isContainCollect
	 * @return
	 */
	public static String createAllVorgRel(Operator operator, List checkedList,boolean isContainCollect) {

		String result = //operator.getUserName()
			FitechUtil.getObjectName();

		if (checkedList == null) // 是否为NULL将决定返回的名称

			result = result + "_allOrgRel_.xml";

		else

			result = result + "_allOrgRel_checked_.xml";

		String srcfilePath = Config.WEBROOTPATH + Config.FILESEPARATOR
				+ "tree_xml" + Config.FILESEPARATOR + "org_tree"
				+ Config.FILESEPARATOR + com.fitech.gznx.common.Config.V_ORG_REL_STR;

		String filePath = Config.WEBROOTPATH + Config.FILESEPARATOR
				+ "tree_xml" + Config.FILESEPARATOR + "org_tree"
				+ Config.FILESEPARATOR + result;
		
		try {

			File file = new File(filePath);

			if (file.exists())

				file.delete();
			
			File srcOrgFile = new File(srcfilePath);

			SAXReader saxReader = new SAXReader();

			InputStream input = new FileInputStream(srcOrgFile);

			Document srcDocument = saxReader.read(input, "GB2312"); // 源DOCUMENT

			Document dstDocument = DocumentHelper.createDocument(); // 目的DOCUMENT

			Element rootDst = dstDocument.addElement("tree"); // 建立目标文件跟接点

			rootDst.addAttribute("id", "0");

			List list = srcDocument.selectNodes("/tree/item");

			for (int i = 0; i < list.size(); i++) {

				Element srcElement = (Element) list.get(i);

				Element dstElement = rootDst.addElement("item");

				dstElement.addAttribute("text", srcElement
						.attributeValue("text"));

				dstElement.addAttribute("id", srcElement.attributeValue("id"));

				dstElement.addAttribute("open", "1");

				if (checkedList != null
						&& checkedList
								.contains(srcElement.attributeValue("id")))

					dstElement.addAttribute("checked", "1");

				CreateOrgTreeByUserHZRule.createAllOrgIterator(srcElement,
						dstElement, checkedList);

				if (isContainCollect) // 如果不包括虚拟机构
					break;

			}

			dstDocument.setXMLEncoding("GB2312");

			OutputFormat format = OutputFormat.createPrettyPrint();

			format.setEncoding("GB2312");

			XMLWriter output = new XMLWriter(new FileOutputStream(file), format);

			output.write(dstDocument);

			output.flush();

			output.close();

		} catch (Exception e) {
			e.printStackTrace();
			result = null;
		}

		return result;
	}
	/**
	 * 
	 * title:该方法用于生成所有机构(包含虚拟机构)的机构树方法中的递归 author:chenbing date:2008-2-20
	 * 
	 * @param srcElement
	 * @param dstElement
	 * @param checkedList
	 */
	public static void createAllOrgIterator(Element srcElement,
			Element dstElement, List checkedList) {

		List srcList = srcElement.selectNodes("item");

		if (srcList.size() == 0)
			return;

		for (int i = 0; i < srcList.size(); i++) {

			Element srcE = (Element) srcList.get(i);

			Element dstE = dstElement.addElement("item");

			dstE.addAttribute("text", srcE.attributeValue("text"));
			String orgid = srcE.attributeValue("id");
			dstE.addAttribute("id", orgid);

			if (checkedList != null
					&& checkedList.contains(orgid))
				dstE.addAttribute("checked", "1");
			CreateOrgTreeByUserHZRule.createAllOrgIterator(srcE, dstE, checkedList);
		}
	}

	
}
