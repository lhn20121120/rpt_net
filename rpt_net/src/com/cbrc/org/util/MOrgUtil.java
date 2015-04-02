package com.cbrc.org.util;

import java.util.List;

import com.cbrc.org.adapter.StrutsMOrgClDelegate;
import com.cbrc.org.adapter.StrutsMOrgDelegate;
import com.cbrc.org.form.MOrgForm;
import com.fitech.net.hibernate.OrgNet;
/**
 * ����������
 * 
 * @author rds
 * @serialData 2005-12-07
 */
public class MOrgUtil {
	/**
	 * ���ݻ��������ƻ�ȡ������IDֵ
	 * 
	 * @author rds
	 * @serialData 2005-12-07
	 * 
	 * @param orgName String ��������
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
	 * ���ݻ������ʹ�����ȡ��Ӧ�Ļ�����Ϣ�б�
	 * 
	 * @param orgCls String �������ʹ�
	 * @return List
	 */
	public static List getOrgs(String orgCls){
		return StrutsMOrgDelegate.getOrgs(orgCls);
	}
	
	/**
	 * ��ȡ����������Ϣ�б�
	 * 
	 * @return List �޷�����Ϣ������null
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
	 * ���ݻ���ID�ַ�������ȡ������Ϣ�б�
	 * 
	 * @param orgs String ����ID��ֵ
	 * @return List �޻�����Ϣ������null
	 */
	public static List getOrgClsByOrgs(String orgs){
		if(orgs==null) return null;
		if(orgs.equals("")) return null;
		
		return StrutsMOrgDelegate.getOrgCls(orgs);
	}
	
	/**
	 * ���ݻ��������ַ�������ȡ����������Ϣ�б�
	 * 
	 * @param orgCls String
	 * @return List
	 */
	public static List getOrgCls(String orgCls){
		if(orgCls==null) return null;
		
		return com.cbrc.org.adapter.StrutsMOrgClDelegate.findOrgCls(orgCls);
	}
	
	/**
	 * ���ݻ���ID��û���������
	 * 
	 * @author rds
	 * @date 2006-01-02 23:47
	 * 
	 * @param orgId String ����ID
	 * @return String ��������
	 */
	public static String getOrgName(String orgId){
		OrgNet mOrgForm=StrutsMOrgDelegate.getOrgNet(orgId);
		
		if(mOrgForm!=null)
			return mOrgForm.getOrgName();
		else
			return "";
	}
}
