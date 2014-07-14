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
 * 生成总分校验关系树
 * @author Administrator
 *
 */
public class CreateOrgTreeByValidate {
	
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
		//由于河北银行的V_ORG表与其他银行V_ORG_REL不一样，因此直接使用汇总关系的机构层次，
		//如果需要更新，可以在OrgInfoAction.java中beforeEdit方法中的AFOrgDelegate.makeOrgTree();后添加CreateOrgTreeByValidate.createTreeForVorgRelXml();方法
		//同时修改com.fitech.gznx.common.Config.BASE_ORG_TREEXML_NAME为com.fitech.gznx.common.Config.V_ORG_REL_STR
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

				if (checkedList != null
						&& checkedList
								.contains(srcElement.attributeValue("id"))){
					dstElement.addAttribute("checked", "1");
				}

				createAllOrgIterator(srcElement,dstElement, checkedList);

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
			createAllOrgIterator(srcE, dstE, checkedList);
		}
	}
	
	
	/***
	 * 构建生成关系树---机构树展现的不对
	 * @return
	 */
	public boolean createTreeForVorgRelXml() {

		boolean result = false;

		String tmpOrgTreeName = FitechUtil.getObjectName() + "_"
				+ com.fitech.gznx.common.Config.V_ORG_REL_XML;

		String filePath = Config.WEBROOTPATH + Config.FILESEPARATOR
				+ "tree_xml" + Config.FILESEPARATOR + "org_tree"
				+ Config.FILESEPARATOR + tmpOrgTreeName;

		File xmlFile = new File(filePath); // 定义生成的xml文件对象

		org.dom4j.Document document = org.dom4j.DocumentHelper.createDocument();

		document.setXMLEncoding("GB2312");

		Element rootElement = document.addElement("tree");

		rootElement.addAttribute("id", "0");
		
		/**已使用hibernate 卞以刚 2011-12-22**/
		List orgList = AFValiRelationDelegate.getAllFirstVorgRel();
		if(orgList!=null && orgList.size()!=0){
			for (int i = 0; i < orgList.size(); i++) {
	
				String strs = (String) orgList.get(i);
	
				Element element = rootElement.addElement("item");
	
				element.addAttribute("id", strs);
	
				element.addAttribute("text", strs);
	
				element.addAttribute("open", "1");
	
				//查询第2层
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
	 * 生成机构树
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
