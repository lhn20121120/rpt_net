package com.cbrc.smis.form;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

import com.cbrc.smis.adapter.StrutsOrgDelegate;
import com.fitech.net.form.OrgTypeForm;

/**
 * 
 * @author jcm
 * @serialData 2006-04-02
 */
public class UtilOrgForm {
	/**
	 * 机构类型
	 */
	private List morgs = null;
	private List orgLevels=null;
	public void setMorgs(List morgs) {
		this.morgs = morgs;
	}

	public void setOrgLevels(List orgLevels) {
		this.orgLevels = orgLevels;
	}
	public List getMorgs(){
		if(this.morgs == null){
			List orgList = new ArrayList();			
			try{
				List list = StrutsOrgDelegate.findAll();
				if(list!=null && list.size()>0){
					OrgForm orgForm = null;
					for(int i=0;i<list.size();i++){
						orgForm = (OrgForm)list.get(i);
						orgList.add(new LabelValueBean(orgForm.getOrgName(),orgForm.getOrgId()+","+orgForm.getOATId()));
					}
				}
			}catch(Exception e){
				e.printStackTrace();
				orgList=new ArrayList();
			}
			return orgList;
		}else{
			return this.morgs;
		}
	}
	/**
	 * 获取机构等级列表
	 * @return
	 */
	public List getOrgLevel(){
		if(this.orgLevels == null){
			List orgLevelList = new ArrayList();			
			try{
				List list = StrutsOrgDelegate.findOrgLevel();
				if(list!=null && list.size()>0){
					OrgTypeForm orgTypeForm = null;
					for(int i=0;i<list.size();i++){
						orgTypeForm = (OrgTypeForm)list.get(i);
						orgLevelList.add(new LabelValueBean(orgTypeForm.getOrg_type_name(),orgTypeForm.getOrg_type_id()+""));
					}
				}
			}catch(Exception e){
				e.printStackTrace();
				orgLevelList=new ArrayList();
			}
			return orgLevelList;
		}else{
			return this.orgLevels;
		}
	}

	
}
