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
	 * title:�÷������ڸ��ݻ���Ȩ�޼��û����������ɸ��û��Ļ���Ȩ����
	 * 
	 * author:chenbing date:2008-2-20
	 * 
	 * @param operator
	 *            ��½�û�����
	 * @param purviewOrgList
	 *            Ȩ�޻��� ���ΪNULL����Ϊ���л���
	 * @param xmlName
	 *            �ļ�����(���ص��ļ������û���¼��+�»��ߺ��ļ��������)
	 * @param isContailCollect
	 *            �Ƿ�������ܻ���
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

			Document srcDocument = saxReader.read(input, "GB2312"); // ԴDOCUMENT

			Document dstDocument = DocumentHelper.createDocument(); // Ŀ��DOCUMENT

			Element rootDst = dstDocument.addElement("tree"); // ����Ŀ���ļ����ӵ�

			rootDst.addAttribute("id", "0");

			List list = srcDocument.selectNodes("/tree/item");

			for (int i = 0; i < list.size(); i++) {

				Element srcElement = (Element) list.get(i);

				Element dstElement = rootDst; // �ȵ���Ŀ����ڵ�

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

				if (!isContailCollect) // ����������������

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
	 * title:�÷������� author:chenbing date:2008-2-20
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
	 * title:�÷��������������л��������� author:chenbing date:2008-2-19 ���ܹ�ϵ�޸�ר��
	 * 
	 * @param operator
	 *            ��½�û�����
	 * @param checkedList
	 *            ѡ�л����б� ��ѡ���б�ΪNULL����ѡ��
	 * @param isContainCollect
	 *            �Ƿ�����������
	 * @return
	 */
	public static String createAllOrg(Operator operator, List checkedList,boolean isContainCollect) {

		String result = //operator.getUserName()
			FitechUtil.getObjectName();

		if (checkedList == null) // �Ƿ�ΪNULL���������ص�����

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

			Document srcDocument = saxReader.read(input, "GB2312"); // ԴDOCUMENT

			Document dstDocument = DocumentHelper.createDocument(); // Ŀ��DOCUMENT

			Element rootDst = dstDocument.addElement("tree"); // ����Ŀ���ļ����ӵ�

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

				if (isContainCollect) // ����������������
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
	 * �޸����ɻ����� 
	 * @param operator
	 * @param checkedList
	 * @param isContainCollect
	 * @return
	 */
	public static String createAllVorgRel(Operator operator, List checkedList,boolean isContainCollect) {

		String result = //operator.getUserName()
			FitechUtil.getObjectName();

		if (checkedList == null) // �Ƿ�ΪNULL���������ص�����

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

			Document srcDocument = saxReader.read(input, "GB2312"); // ԴDOCUMENT

			Document dstDocument = DocumentHelper.createDocument(); // Ŀ��DOCUMENT

			Element rootDst = dstDocument.addElement("tree"); // ����Ŀ���ļ����ӵ�

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

				if (isContainCollect) // ����������������
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
	 * title:�÷��������������л���(�����������)�Ļ����������еĵݹ� author:chenbing date:2008-2-20
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
