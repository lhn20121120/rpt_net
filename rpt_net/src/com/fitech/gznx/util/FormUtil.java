package com.fitech.gznx.util;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.struts.util.LabelValueBean;
import org.dom4j.Element;

import com.cbrc.smis.common.Config;
import com.fitech.gznx.common.StringUtil;
import com.fitech.gznx.common.TreeContentBuilder;
import com.fitech.gznx.common.TreeNode;
import com.fitech.gznx.po.AfOrg;
import com.fitech.gznx.service.AFOrgDelegate;
import com.fitech.gznx.service.AFTemplateTypeDelegate;
import com.fitech.gznx.service.StrutsCodeLibDelegate;
import com.fitech.gznx.service.XmlTreeUtil;
import com.fitech.net.adapter.StrutsOrgNetDelegate;
import com.fitech.net.hibernate.OrgNet;
public class FormUtil {
	
	/**
	 * 用户类型列表
	 */
	private String userTree = null;
	
	private String reportFlg = null;
	
	private String templateTypeTree = null;
	
	private List templateTypes=null;
	
	/**报表权限*/
	private String orgPodedom = null;
	
	/**报表权限*/
	private String orgReportPodedomTree = null;

	public String getUserTree() {
		if(userTree != null){
			return userTree;
		} else{
			String usertrees = XmlTreeUtil.createUserTree("TREE1_NODES",null,false);
			return usertrees;
		}
	}

	public void setUserTree(String userTree) {
		this.userTree = userTree;
	}
	
	/**
	 * 报表树
	 */
	private String templateTree = null;
	
	public String getTemplateTree() {
		if(templateTree != null){
			return templateTree;
		} else{
			String templateTrees = XmlTreeUtil.createTemplateTree("TREE1_NODES",null,false);
			return templateTrees;
		}
	}

	public void setTemplateTree(String templateTree) {
		this.templateTree = templateTree;
	}

	public String getReportFlg() {
		return reportFlg;
	}

	public void setReportFlg(String reportFlg) {
		this.reportFlg = reportFlg;
	}

	public String getTemplateTypeTree() {
		if(templateTypeTree != null){
			return templateTypeTree;
		} else{
			if(!StringUtil.isEmpty(reportFlg)){
				if(StringUtil.isEmpty(orgPodedom)){
					templateTypeTree = AFTemplateTypeDelegate.createTemplateTypeTree("TREE1_NODES",reportFlg);
				} else {
					if(Config.SYSTEM_SCHEMA_FLAG==0){ //防止在任务模式下加载机构树出错！
					templateTypeTree = AFTemplateTypeDelegate.createTemplateTypeTree("TREE1_NODES",reportFlg,orgPodedom.replaceAll("orgRepId","childRepId"));
					}
				}				
			}else{
				templateTypeTree = AFTemplateTypeDelegate.createTemplateTypeTree("TREE1_NODES");
			}
			return templateTypeTree;
		}
	}

	public void setTemplateTypeTree(String templateTypeTree) {
		this.templateTypeTree = templateTypeTree;
	}

	public List getTemplateTypes() {


		  if(this.templateTypes==null){	
			  List rtList=null;
			  try{
				  rtList=AFTemplateTypeDelegate.getTemplateTypes(reportFlg);
				  
				  
			  }catch(Exception e){
				  e.printStackTrace();
				  return rtList;
			  }
			  return rtList;
		  }else{
			return templateTypes;
		  }
	}

	public void setTemplateTypes(List templateTypes) {
		this.templateTypes = templateTypes;
	}

	public String getOrgPodedom() {
		return orgPodedom;
	}

	public void setOrgPodedom(String orgPodedom) {
		this.orgPodedom = orgPodedom;
	}

	public String getOrgReportPodedomTree() {

		
		if(orgReportPodedomTree == null){			
			if(orgPodedom != null && !orgPodedom.equals("")){
				List orgList = null;
				if(StringUtil.isEmpty(reportFlg)){
					orgList= AFOrgDelegate.selectOrgByIds(orgPodedom.replaceAll("orgRepId","orgId"));
				} else{
					orgList= AFOrgDelegate.selectOrgByIds(orgPodedom.replaceAll("orgRepId","orgId"),reportFlg);
				}
				
				if(orgList != null && orgList.size() > 0){
					List preOrgList = new ArrayList();
					for(int i=0;i<orgList.size();i++){
						AfOrg tempOrgInfo = (AfOrg) orgList.get(i);
						if(tempOrgInfo.getPreOrgId().equals("-99") || tempOrgInfo.getPreOrgId().equals("-98")){
							preOrgList.add(tempOrgInfo);
							/***
							 * ---------------------------------------BEGIN
							 * 为保证后续判断 此处集合中移除虚拟机构
							 *  20130131威海银行问题反馈修改
							 * 修改人：卞以刚
							 */
							orgList.remove(i);
							i--;
							/***
							 * ---------------------------------------END
							 */
						}
					}

					List<TreeNode> types = new ArrayList<TreeNode>();
					List root = new ArrayList();
					// 获得最终显示树List
					AfOrg orgInfo = null;
					if(orgList!=null && orgList.size()>0){
						for(int i=0;i<orgList.size();i++){
							if(((AfOrg)orgList.get(i)).getOrgId().equals(com.fitech.gznx.common.Config.HEAD_ORG_ID)){
								orgInfo = (AfOrg)orgList.get(i);
								break;
							}
						}
					}	
					if(orgInfo==null)
						orgInfo = (AfOrg)orgList.get(0);
					
					
					if(orgInfo!=null && orgInfo.getPreOrgId()!=null){
						root = this.productTree(orgList, orgInfo.getPreOrgId());
					}
					if(preOrgList!=null && preOrgList.size()>0){
						for(int i=0;i<preOrgList.size();i++){
							AfOrg tempOrgInfo = (AfOrg) preOrgList.get(i);
							boolean bl= true;
							for(int j=0;j<root.size();j++){
								TreeNode tr = (TreeNode)root.get(j);
								if(tr.getKey().equals(tempOrgInfo.getOrgId())){
									bl=false;//此处原先判断存在问题
									break;
								}
									
							}
							if(bl)
								root.add(new TreeNode(tempOrgInfo.getOrgId(), tempOrgInfo.getOrgName()));
						}
					}
					
					types.add(new TreeNode("-999", "全部机构",root));
					String treeContent = new TreeContentBuilder("TREE2_NODES", types, null,false).buildTreeContent();
					return treeContent;
				}
			}
			return orgReportPodedomTree;
		}else 
			return orgReportPodedomTree;	
	}
	

	/**
	 * 递归
	 * 
	 * @param items
	 *            源List
	 * @param orgId
	 *            orgId
	 * @return 2008-6-4
	 */
	private List<TreeNode> productTree(List items, String orgId) {
		List<TreeNode> result = new ArrayList<TreeNode>();
		// 获得下级机构list
		List temps = this.findOrgByParentOrgId(items, orgId);
		if (temps != null && temps.size() != 0) {
			for (int i = 0; i < temps.size(); i++) {
				AfOrg tempOrgInfo = (AfOrg) temps.get(i);
				result.add(new TreeNode(tempOrgInfo.getOrgId(), tempOrgInfo
						.getOrgName(), this.productTree(items, tempOrgInfo
						.getOrgId())));
			}
			return result;
		} else {			
			return result;
		}
	}
	
	/**
	 * 根据orgId取得下级所有机构
	 * 
	 * @param items
	 *            取得该下级机构,就从list里remove掉这个机构
	 * @param orgId
	 * @return List 2008-6-4
	 */
	private List findOrgByParentOrgId(List items, String orgId) {
		List<AfOrg> result = new ArrayList<AfOrg>();
		for (int i = 0; i < items.size(); i++) {
			AfOrg orgInfo = (AfOrg) items.get(i);
			if (orgInfo.getPreOrgId().trim().equals(orgId.trim())) {
				result.add(orgInfo);
				items.remove(i);
				i--;
			}
		}
		return result;
	}

	public void setOrgReportPodedomTree(String orgReportPodedomTree) {
		this.orgReportPodedomTree = orgReportPodedomTree;
	}

}
