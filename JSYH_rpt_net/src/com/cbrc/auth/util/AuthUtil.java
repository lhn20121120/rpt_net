package com.cbrc.auth.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

import com.cbrc.auth.adapter.StrutsDepartmentDelegate;
import com.cbrc.auth.form.DepartmentForm;
import com.fitech.net.adapter.StrutsOrgNetDelegate;
import com.fitech.net.hibernate.OrgNet;

public class AuthUtil {
    
	/**��ǰϵͳ�û��Ĳ�����Ϣ*/
    private List deptList = null;
    /**��ǰϵͳ�û���������id*/
    private String OrgId= null;
    /**��ǰϵͳ�û������ӻ���*/
    private List subOrgList = null;
    /**��ǰϵͳ�û������ӻ���id�ִ�*/
    private String subOrgIds = null;
    
    /**��ǰϵͳ�û������ӻ���*/
    private List subOrgListWithIds = null;
    
    public String getOrgId() {
    	return OrgId;
    }

	public void setOrgId(String orgId) {
		OrgId = orgId;
	}

	public List getDeptList() {		
        if (this.deptList!=null) {
            return deptList;
        }
        else {
            ArrayList lists =  new ArrayList();
            
            List results = null;
            try {
                results = StrutsDepartmentDelegate.findAll(OrgId);
                if (results!=null) {
                    for (int i=0; i<results.size(); i++) {
                        DepartmentForm dept = (DepartmentForm)(results.get(i));
                        lists.add(new LabelValueBean(dept.getDeptName(),dept.getDepartmentId().toString()));
                    }
                }
            } catch (Exception e) {
                // TODO �Զ����� catch ��
                e.printStackTrace();
            }
            return lists;
        } 
    }

    public void setDeptList(List deptList) {
        this.deptList = deptList;
    }
    
	public String getSubOrgIds() {
		return subOrgIds;
	}

	public void setSubOrgIds(String subOrgIds) {
		this.subOrgIds = subOrgIds;
	}

	public List getSubOrgList() {
		if(subOrgList != null) 
			return subOrgList;
		
		subOrgList = new ArrayList();
		try{
			if(subOrgIds != null && !subOrgIds.equals(""))
				subOrgIds = subOrgIds + ",'" + OrgId + "'";
			else subOrgIds = "'" + OrgId + "'";
			
			List list = StrutsOrgNetDelegate.selectOrgByIds(subOrgIds);
			if(list != null && list.size() > 0){
				for(int i=0;i<list.size();i++){
					OrgNet orgNet = (OrgNet)list.get(i);
					subOrgList.add(new LabelValueBean(orgNet.getOrgName().trim(),orgNet.getOrgId().trim()));
				}
			}			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return this.subOrgList;
		
	}

	public void setSubOrgList(List subOrgList) {
		this.subOrgList = subOrgList;
	}
	
	public List getSubOrgListWithIds() {
		
		subOrgListWithIds = new ArrayList();
		try{
			if(subOrgIds != null && !subOrgIds.equals(""))
				subOrgIds = subOrgIds + ",'" + OrgId + "'";
			else subOrgIds = "'" + OrgId + "'";
			
			List list = StrutsOrgNetDelegate.selectOrgByIds(subOrgIds);
			String orgName=null;
			if(list != null && list.size() > 0){
				for(int i=0;i<list.size();i++){
					OrgNet orgNet = (OrgNet)list.get(i);
					orgName=orgNet.getOrgName().trim()+"["+orgNet.getOrgId().trim()+"]";
					subOrgListWithIds.add(new LabelValueBean(orgName,orgNet.getOrgId().trim()));
				}
			}			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return subOrgListWithIds;
	}

	public void setSubOrgListWithIds(List subOrgListWithIds) {
		this.subOrgListWithIds = subOrgListWithIds;
	}    
}
