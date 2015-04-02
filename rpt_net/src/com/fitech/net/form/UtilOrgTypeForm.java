package com.fitech.net.form;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

import com.cbrc.smis.adapter.StrutsMChildReportDelegate;
import com.fitech.net.adapter.StrutsOrgTypeDelegate;

/**
 * 
 * @author jcm
 * @serialData 2006-07-01
 */
public class UtilOrgTypeForm {
	/**
	 * 机构类型
	 */
	private List orgTypes = null;
	
	/**
	 * 机构部门
	 */
	private List orgDept = null;
	
	/**
	 * 机构ID
	 */
	private String orgId = null;
	
	public List getOrgTypes() {
		if(orgId == null) return new ArrayList();
		
		if(orgTypes == null){
			List orgTypeList = new ArrayList();
			try{
				List list = StrutsOrgTypeDelegate.findSubOrgTypes(orgId);
				if(list != null && list.size() > 0){
					for(int i=0;i<list.size();i++){
						OrgTypeForm orgTypeForm = null;
						orgTypeForm = (OrgTypeForm)list.get(i);
						orgTypeList.add(new LabelValueBean(orgTypeForm.getOrg_type_name(),orgTypeForm.getOrg_type_id().toString()));
					}
				}
			}catch(Exception e){
				e.printStackTrace();
				orgTypeList = new ArrayList();
			}
			return orgTypeList;
		}else return orgTypes;
	}

	public void setOrgTypes(List orgTypes) {
		this.orgTypes = orgTypes;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public List getOrgDept()
	{
	
	if(orgDept == null){
		List orgTypeList = new ArrayList();
		try{
			List list = StrutsMChildReportDelegate.findOrgDept();
			if(list != null && list.size() > 0){
				for(int i=0;i<list.size();i++){
					ETLReportForm form=null;
					form = (ETLReportForm)list.get(i);
					orgTypeList.add(new LabelValueBean(form.getOrgName(),form.getDeptId().toString()));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			orgTypeList = new ArrayList();
		}
		return orgTypeList;
	}else return orgDept;
		
	}

	public void setOrgDept(List orgDept)
	{
		this.orgDept = orgDept;
	}	
}
