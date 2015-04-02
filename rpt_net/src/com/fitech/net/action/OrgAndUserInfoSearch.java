package com.fitech.net.action;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
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
import com.fitech.net.adapter.StrutsOrgNetDelegate;
import com.fitech.net.form.OrgNetForm;
import com.fitech.net.hibernate.OrgNet;

/**
 * 下级用户和机构的信息查询
 * @author wh
 *
 */
public final class OrgAndUserInfoSearch extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		HttpSession session = request.getSession();
		Operator operator = (Operator) session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
		      
		String orgId=operator.getOrgId();       
		String orgName = operator.getOrgName();
              
		//查询机构信息      
		String orgInfos=this.getOrgInfo(operator.getOrgId());
		String fileName = Config.WEBROOTPATH + "xml"+Config.FILESEPARATOR+"orgInfo.xml"; // 创建一个XML文件
		org.dom4j.Document document = org.dom4j.DocumentHelper.createDocument();
	//	document.setXMLEncoding("GBK");

		Element rootElement = document.addElement("tree");
		rootElement.addAttribute("id", "0");

		Element oneElement = rootElement.addElement("item");
		oneElement.addAttribute("text", orgName);		
		oneElement.addAttribute("id", orgId);
		oneElement.addAttribute("orgInfo",orgInfos);
		oneElement.addAttribute("open", "1");
		oneElement.addAttribute("checked", "1");
		getNode(orgId, session, oneElement);
		try {
//			OutputFormat format = OutputFormat.createPrettyPrint();
//			format.setEncoding("GBK");
//			XMLWriter output = new XMLWriter(
//					new FileWriter(new File(fileName)), format);
//			output.write(document);
//			output.close();
//			
//			
			String xmlStr = document.asXML();
			File file = new File(fileName);
			OutputStream out = new DataOutputStream(new FileOutputStream (file));
			StringBuffer sb = new  StringBuffer();
			sb.append(xmlStr);			
			out.write(sb.toString().getBytes("UTF-8"));
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mapping.findForward("view");
	}

	private boolean flag = true;

	private void addChild(Element e, OrgNet orgNet, HttpSession session) {
		if (orgNet != null) {
			String id = orgNet.getOrgId();						 
			String orgInfo = getOrgInfo(id);
			
			Element element = e;
			if (flag) {
				element = e.addElement("item");
				element.addAttribute("text", orgNet.getOrgName());
		
				element.addAttribute("id", orgNet.getOrgId());
				element.addAttribute("orgInfo",orgInfo);
			}
			List childList = StrutsOrgNetDelegate.selectLowerOrgList(id,session);
			String orgInfos=null;
			if (childList != null && !childList.equals("")) {
				for (int i = 0; i < childList.size(); i++) {
					OrgNet o = (OrgNet) childList.get(i);
					orgInfos=getOrgInfo(o.getOrgId());
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

	private void getNode(String orgId, HttpSession session, Element rootElement) {

		List lowerOrgList = StrutsOrgNetDelegate.selectLowerOrgList(orgId,session);
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
     * @param orgId
     * @return
     */
	private String getOrgInfo(String orgId){
	       String orgInfos = null;
	       OrgNetForm orgNetForm = StrutsOrgNetDelegate.selectOrgInfo(orgId);
	       if(orgNetForm!=null){
	    	   String org_id = orgNetForm.getOrg_id();
	    	   String org_type = orgNetForm.getOrg_type_name();
	    	   String pre_org = orgNetForm.getPre_org_name();
	    	   String org_region = orgNetForm.getRegion_name();
                   if(pre_org == null)pre_org = "无";
	    	   orgInfos="机构ID:       "+org_id+"\n机构类型:   "+org_type+"\n上级机构:   "+pre_org+"\n机构地区:   "+org_region;
	       }
	     return orgInfos;
	}
}
