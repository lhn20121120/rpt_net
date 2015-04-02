package com.fitech.net.form;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

import com.fitech.net.adapter.StrutsOrgNetDelegate;

/**
 * 
 * @author jcm
 * @serialData 2006-07-02
 */
public class UtilOrgNetForm {
	/**
	 * 机构对象
	 */
	private List orgNets = null;
			
	public List getOrgNets() {
		if(orgNets == null){
			List orgNetList = new ArrayList();
			try{
				List list = StrutsOrgNetDelegate.findAll();
				OrgNetForm orgNetForm = null;
				if(list != null && list.size() > 0){
					for(int i=0;i<list.size();i++){
						orgNetForm = (OrgNetForm)list.get(i);
						orgNetList.add(new LabelValueBean(orgNetForm.getOrg_name(),orgNetForm.getOrg_id()));
					}
				}
			}catch(Exception e){
				e.printStackTrace();
				orgNetList = new ArrayList();
			}
			return orgNetList;
		}else return orgNets;
	}
	
	public void setOrgNets(List orgNets) {
		this.orgNets = orgNets;
	}
}
