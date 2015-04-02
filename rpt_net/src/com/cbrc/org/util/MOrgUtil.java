package com.cbrc.org.util;

import java.util.List;

import com.cbrc.org.adapter.StrutsMOrgClDelegate;
import com.cbrc.org.adapter.StrutsMOrgDelegate;
import com.cbrc.org.form.MOrgForm;
import com.fitech.net.hibernate.OrgNet;
/**
 * 机构操作类
 * 
 * @author rds
 * @serialData 2005-12-07
 */
public class MOrgUtil {
	/**
	 * 根据机构的名称获取机构的ID值
	 * 
	 * @author rds
	 * @serialData 2005-12-07
	 * 
	 * @param orgName String 机构名称
	 * @return String
	 */
	public static String getOrgId(String orgName){
		String orgId="";
		
		if(orgName==null) return orgId;
		
		MOrgForm mOrgForm=StrutsMOrgDelegate.getOrgInfo(orgName);
		if(mOrgForm!=null) orgId=mOrgForm.getOrgId();
		
		return orgId;
	}
	
	/**
	 * 根据机构类型串，获取对应的机构信息列表
	 * 
	 * @param orgCls String 机构类型串
	 * @return List
	 */
	public static List getOrgs(String orgCls){
		return StrutsMOrgDelegate.getOrgs(orgCls);
	}
	
	/**
	 * 获取机构分类信息列表
	 * 
	 * @return List 无分类信息，返回null
	 */
	public static List getOrgCls(){
		try{
			return StrutsMOrgClDelegate.findAll();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 根据机构ID字符串，获取机构信息列表
	 * 
	 * @param orgs String 机构ID串值
	 * @return List 无机构信息，返回null
	 */
	public static List getOrgClsByOrgs(String orgs){
		if(orgs==null) return null;
		if(orgs.equals("")) return null;
		
		return StrutsMOrgDelegate.getOrgCls(orgs);
	}
	
	/**
	 * 根据机构类型字符串，获取机构类型信息列表
	 * 
	 * @param orgCls String
	 * @return List
	 */
	public static List getOrgCls(String orgCls){
		if(orgCls==null) return null;
		
		return com.cbrc.org.adapter.StrutsMOrgClDelegate.findOrgCls(orgCls);
	}
	
	/**
	 * 根据机构ID获得机构的名称
	 * 
	 * @author rds
	 * @date 2006-01-02 23:47
	 * 
	 * @param orgId String 机构ID
	 * @return String 机构名称
	 */
	public static String getOrgName(String orgId){
		OrgNet mOrgForm=StrutsMOrgDelegate.getOrgNet(orgId);
		
		if(mOrgForm!=null)
			return mOrgForm.getOrgName();
		else
			return "";
	}
}
