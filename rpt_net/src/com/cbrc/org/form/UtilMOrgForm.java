package com.cbrc.org.form;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

import com.cbrc.org.adapter.StrutsMOrgDelegate;

/**
 * 
 * @author jcm
 * @serialData 2006-04-02
 */
public class UtilMOrgForm {
	/**
	 * 机构类型
	 */
	private List morgs = null;
	
	public void setMorgs(List morgs) {
		this.morgs = morgs;
	}


	public List getMorgs(){
		if(this.morgs == null){
			List mOrgList = new ArrayList();			
			try{
				List list = StrutsMOrgDelegate.findAll();
				if(list!=null && list.size()>0){
					MOrgForm mOrgForm = null;
					for(int i=0;i<list.size();i++){
						mOrgForm = (MOrgForm)list.get(i);
						mOrgList.add(new LabelValueBean(mOrgForm.getOrgName(),mOrgForm.getOrgId()));
					}
				}
			}catch(Exception e){
				e.printStackTrace();
				mOrgList=null;
			}
			return mOrgList;
		}else{
			return this.morgs;
		}
	}
}
