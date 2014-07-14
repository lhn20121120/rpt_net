package com.fitech.gznx.treexml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
import com.fitech.gznx.service.AFOrgDelegate;
import com.fitech.gznx.service.AFValiRelationDelegate;

/**
 * �����ܷ�У���ϵ��
 * @author Administrator
 *
 */
public class CreateOrgTreeByValidate {
	
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
		//���ںӱ����е�V_ORG������������V_ORG_REL��һ�������ֱ��ʹ�û��ܹ�ϵ�Ļ�����Σ�
		//�����Ҫ���£�������OrgInfoAction.java��beforeEdit�����е�AFOrgDelegate.makeOrgTree();�����CreateOrgTreeByValidate.createTreeForVorgRelXml();����
		//ͬʱ�޸�com.fitech.gznx.common.Config.BASE_ORG_TREEXML_NAMEΪcom.fitech.gznx.common.Config.V_ORG_REL_STR
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

				if (checkedList != null
						&& checkedList
								.contains(srcElement.attributeValue("id"))){
					dstElement.addAttribute("checked", "1");
				}

				createAllOrgIterator(srcElement,dstElement, checkedList);

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
			createAllOrgIterator(srcE, dstE, checkedList);
		}
	}
	
	
	/***
	 * �������ɹ�ϵ��---������չ�ֵĲ���
	 * @return
	 */
	public boolean createTreeForVorgRelXml() {

		boolean result = false;

		String tmpOrgTreeName = FitechUtil.getObjectName() + "_"
				+ com.fitech.gznx.common.Config.V_ORG_REL_XML;

		String filePath = Config.WEBROOTPATH + Config.FILESEPARATOR
				+ "tree_xml" + Config.FILESEPARATOR + "org_tree"
				+ Config.FILESEPARATOR + tmpOrgTreeName;

		File xmlFile = new File(filePath); // �������ɵ�xml�ļ�����

		org.dom4j.Document document = org.dom4j.DocumentHelper.createDocument();

		document.setXMLEncoding("GB2312");

		Element rootElement = document.addElement("tree");

		rootElement.addAttribute("id", "0");
		
		/**��ʹ��hibernate ���Ը� 2011-12-22**/
		List orgList = AFValiRelationDelegate.getAllFirstVorgRel();
		if(orgList!=null && orgList.size()!=0){
			for (int i = 0; i < orgList.size(); i++) {
	
				String strs = (String) orgList.get(i);
	
				Element element = rootElement.addElement("item");
	
				element.addAttribute("id", strs);
	
				element.addAttribute("text", strs);
	
				element.addAttribute("open", "1");
	
				//��ѯ��2��
				List orgRelList=AFValiRelationDelegate.getAllSecondVorgRel(strs);
				if(orgRelList==null || orgRelList.size()==0)
					continue;
				for(int j=0; j<orgRelList.size();j++){
					String[] ss = (String[])orgRelList.get(j);
					Element e = element.addElement("item");
	
					e.addAttribute("id", ss[0]);
	
					e.addAttribute("text", ss[1]);
					
					e.addAttribute("open", "1");
					
					this.iteratorCreateOrgRel(e,strs);
				}
			}
		}
		try {

			OutputFormat format = OutputFormat.createPrettyPrint();

			format.setEncoding("GB2312");

			XMLWriter output = new XMLWriter();

			output = new XMLWriter(new FileOutputStream(xmlFile), format);

			output.write(document);

			output.flush();

			output.close();

			result = true;

			this.initPathRel(tmpOrgTreeName);

		} catch (IOException e) {

			e.printStackTrace();

			result = false;
		}

		return result;
	}
	
	public void initPathRel(String orgTreeName) {
		String filePath = Config.WEBROOTPATH + Config.FILESEPARATOR
				+ "tree_xml" + Config.FILESEPARATOR + "org_tree"
				+ Config.FILESEPARATOR + com.fitech.gznx.common.Config.V_ORG_REL_STR;
		File xmlFile = new File(filePath);
		if (xmlFile.exists())
			xmlFile.delete();
		com.fitech.gznx.common.Config.V_ORG_REL_STR = orgTreeName;
	}
	
	/**
	 * ���ɻ�����
	 * @param element
	 */
	public void iteratorCreateOrgRel(Element element,String sysFlag) {

		List orgList = AFValiRelationDelegate.getChildListByRelOrgId(element.attributeValue("id"),sysFlag);

		if (orgList == null || orgList.size() == 0)
			return;

		for (int i = 0; i < orgList.size(); i++) {

			String[] strs = (String[]) orgList.get(i);

			Element e = element.addElement("item");

			e.addAttribute("id", strs[0]);

			e.addAttribute("text", strs[1]);
			
			e.addAttribute("open", "1");

			this.iteratorCreateOrgRel(e,sysFlag);
		}
	}
	
}
