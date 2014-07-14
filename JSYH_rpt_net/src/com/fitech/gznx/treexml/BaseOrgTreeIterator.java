package com.fitech.gznx.treexml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechUtil;
import com.fitech.gznx.po.AfOrg;
import com.fitech.gznx.service.AFCollectRelationDelegate;
import com.fitech.gznx.service.AFOrgDelegate;



/**
 * 
 * title: 该类用于递归生成所有层次的机构树
 * 
 * @author chenbing 2008-2-19
 */
public class BaseOrgTreeIterator implements OrgTreeInterface {

	/**
	 * 
	 * title:该方法用于生成系统全局机构树 author:chenbing date:2008-2-19
	 * 
	 * @return
	 */
	public boolean createTreeForTagXml() {

		boolean result = false;

		String tmpOrgTreeName = FitechUtil.getObjectName() + "_"
				+ com.fitech.gznx.common.Config.BASE_ORG_TREEXML_STR;

		String filePath = Config.WEBROOTPATH + Config.FILESEPARATOR
				+ "tree_xml" + Config.FILESEPARATOR + "org_tree"
				+ Config.FILESEPARATOR + tmpOrgTreeName;

		File xmlFile = new File(filePath); // 定义生成的xml文件对象

		org.dom4j.Document document = org.dom4j.DocumentHelper.createDocument();

		document.setXMLEncoding("GB2312");

		Element rootElement = document.addElement("tree");

		rootElement.addAttribute("id", "0");
		
		/**已使用hibernate 卞以刚 2011-12-22**/
		//真实顶层机构
		List orgList = AFOrgDelegate.getAllFirstOrg();

		for (int i = 0; i < orgList.size(); i++) {

			String[] strs = (String[]) orgList.get(i);

			Element element = rootElement.addElement("item");

			element.addAttribute("id", strs[0]);

			element.addAttribute("text", strs[1]);

			element.addAttribute("open", "1");
			
			element.addAttribute("orgType", strs[3]);

			if (strs[2].equals(com.fitech.gznx.common.Config.NOT_IS_COLLECT))

				element.addAttribute(com.fitech.gznx.common.Config.XML_ATTRIBUTE_VIRTUAL, "0");

			else

				element.addAttribute(com.fitech.gznx.common.Config.XML_ATTRIBUTE_VIRTUAL, "1");
			
			this.iteratorCreate(element);
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

			this.initPath(tmpOrgTreeName);

		} catch (IOException e) {

			e.printStackTrace();

			result = false;
		}

		return result;
	}
	
	
/*	private void iteratorXNCreate(Element element) {

			List orgList = AFOrgDelegate.getChildListByOrgId(element
					.attributeValue("id"));

			if (orgList == null || orgList.size() == 0)
				return;

			for (int i = 0; i < orgList.size(); i++) {

				String[] strs = (String[]) orgList.get(i);

				Element e = element.addElement("item");

				e.addAttribute("id", strs[0]);

				e.addAttribute("text", strs[1]);

				if (strs[2].equals(com.fitech.gznx.common.Config.NOT_IS_COLLECT))

					e.addAttribute(com.fitech.gznx.common.Config.XML_ATTRIBUTE_VIRTUAL, "0");

				else

					e.addAttribute(com.fitech.gznx.common.Config.XML_ATTRIBUTE_VIRTUAL, "1");

				this.iteratorCreate(e);
			}
		}
		
	}*/


	/***
	 * 构建生成关系树
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
		List orgList = AFOrgDelegate.getAllFirstVorgRel();
		if(orgList!=null && orgList.size()!=0){
			for (int i = 0; i < orgList.size(); i++) {
	
				String strs = (String) orgList.get(i);
	
				Element element = rootElement.addElement("item");
	
				element.addAttribute("id", strs);
	
				element.addAttribute("text", strs);
	
				element.addAttribute("open", "1");
	
				//查询第2层
				List orgRelList=AFOrgDelegate.getAllSecondVorgRel(strs);
				if(orgRelList==null || orgRelList.size()==0)
					continue;
				for(int j=0; j<orgRelList.size();j++){
					String[] ss = (String[])orgRelList.get(j);
					Element e = element.addElement("item");
	
					e.addAttribute("id", ss[0]);
	
					e.addAttribute("text", ss[1]);
					
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
	
    /**
     * 
     * title:该方法用于初始化
     * author:chenbing
     * date:2008-3-15
     * @param orgTreeName
     */
	public void initPath(String orgTreeName) {
		String filePath = Config.WEBROOTPATH + Config.FILESEPARATOR
				+ "tree_xml" + Config.FILESEPARATOR + "org_tree"
				+ Config.FILESEPARATOR + com.fitech.gznx.common.Config.BASE_ORG_TREEXML_NAME;
		File xmlFile = new File(filePath);
		if (xmlFile.exists())
			xmlFile.delete();
		com.fitech.gznx.common.Config.BASE_ORG_TREEXML_NAME = orgTreeName;
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
	 * 
	 * title:该方法用于 author:chenbing date:2008-2-19
	 */
	public void iteratorCreate(Element element) {
		Element e = null;
		if(element.attributeValue("orgType").equals(com.fitech.gznx.common.Config.COLLECT_ORG_PARENT_ID)){
			List orgIdList = AFCollectRelationDelegate.getCRList(element.attributeValue("id"));
			for (Iterator iterator = orgIdList.iterator(); iterator.hasNext();) {
				String orgId = (String) iterator.next();
				AfOrg org = AFOrgDelegate.getOrgInfo(orgId);
			
				e =	element.addElement("item");
				
				e.addAttribute("id", org.getOrgId());
	
				e.addAttribute("text", org.getOrgName());
	
				if (org.getIsCollect().equals(com.fitech.gznx.common.Config.NOT_IS_COLLECT))
	
					e.addAttribute(com.fitech.gznx.common.Config.XML_ATTRIBUTE_VIRTUAL, "0");
	
				else
	
					e.addAttribute(com.fitech.gznx.common.Config.XML_ATTRIBUTE_VIRTUAL, "1");
				
//				e.addAttribute("orgType", com.fitech.gznx.common.Config.COLLECT_ORG_PARENT_ID);
				e.addAttribute("orgType", org.getPreOrgId());
				if(org.getOrgType().equals(com.fitech.gznx.common.Config.COLLECT_ORG_PARENT_ID)){
					this.iteratorCreate(e);
				}
			}
		}
		else{
			List orgList = AFOrgDelegate.getChildListByOrgId(element
				.attributeValue("id"));
		
			if (orgList == null || orgList.size() == 0)
				return;
	
			for (int i = 0; i < orgList.size(); i++) {
	
				String[] strs = (String[]) orgList.get(i);
	
				e = element.addElement("item");
	
				e.addAttribute("id", strs[0]);
	
				e.addAttribute("text", strs[1]);
	
				if (strs[2].equals(com.fitech.gznx.common.Config.NOT_IS_COLLECT))
	
					e.addAttribute(com.fitech.gznx.common.Config.XML_ATTRIBUTE_VIRTUAL, "0");
	
				else
	
					e.addAttribute(com.fitech.gznx.common.Config.XML_ATTRIBUTE_VIRTUAL, "1");
	
				e.addAttribute("orgType", strs[3]);
				
				this.iteratorCreate(e);
			}
		}
	}
	
	/**
	 * 生成机构树
	 * @param element
	 */
	public void iteratorCreateOrgRel(Element element,String sysFlag) {

		List orgList = AFOrgDelegate.getChildListByRelOrgId(element
				.attributeValue("id"),sysFlag);

		if (orgList == null || orgList.size() == 0)
			return;

		for (int i = 0; i < orgList.size(); i++) {

			String[] strs = (String[]) orgList.get(i);

			Element e = element.addElement("item");

			e.addAttribute("id", strs[0]);

			e.addAttribute("text", strs[1]);

			this.iteratorCreateOrgRel(e,sysFlag);
		}
	}
	
	/**
	 * 
	 * title:该方法用于在指定的父机构ID下增加指定子机构ID author:chenbing date:2008-2-19
	 * 
	 * @param parentOrgId
	 *            父机构ID
	 * @param orgId
	 *            子机构ID
	 * @return boolean
	 */
	public boolean addTreeNodeByParentOrg(String parentOrgId, String orgId) {
		boolean result = false;
		return result;
	}

	public boolean createTreeForTagXml(Operator operator, List checkList) {

		return false;
	}
}
