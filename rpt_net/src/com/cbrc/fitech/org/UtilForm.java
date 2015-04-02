package com.cbrc.fitech.org;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

import com.cbrc.org.adapter.StrutsMOrgClDelegate;
import com.cbrc.org.form.MOrgClForm;
import com.fitech.net.form.OrgNetForm;

/**
 * 工具ActionForm
 * 
 * @author jhb
 */
public class UtilForm {
	/**
	 * 机构类型(真实机构类型和虚拟机构类型)
	 */
	private List orgCls = null;
		
	
	private List orgList = null;
	/**
	 * 设置机构类型 
	 * 
	 * @param orgCls List
	 * @return void
	 */
	public void setOrgCls(List orgCls){
		this.orgCls=orgCls;
	}
	
	/**
	 * 获取机构类型
	 * 
	 * @return List
	 */
	public List getOrgCls(){
		if(this.orgCls==null){
			List rtList=new ArrayList();
			
			try{
				
				List list = StrutsMOrgClDelegate.findAll();
				if(list!=null && list.size()>0){
					MOrgClForm mOrgClForm=null;
					for(int i=0;i<list.size();i++){
						mOrgClForm=(MOrgClForm)list.get(i);
						rtList.add(new LabelValueBean(mOrgClForm.getOrgClsNm(),mOrgClForm.getOrgClsId()));
					}
				}
			}catch(Exception e){
				e.printStackTrace();
				rtList=null;
			}
			return rtList;
		}else{
			return this.orgCls;
		}
	}

	public List getOrgList()
	{
		if(this.orgList==null){
			List rtList=new ArrayList();
			
			try{
				
				List list = StrutsMOrgClDelegate.findAllOrgList();
				if(list!=null && list.size()>0){
					OrgNetForm orgNetForm = null;
					for(int i=0;i<list.size();i++){
						orgNetForm=(OrgNetForm)list.get(i);
						rtList.add(new LabelValueBean(orgNetForm.getOrg_name(),orgNetForm.getOrg_id()));
					}
				}
			}catch(Exception e){
				e.printStackTrace();
				rtList=null;
			}
			return rtList;
		}else{
			return this.orgList;
		}
	}

	public void setOrgList(List orgList)
	{
		this.orgList = orgList;
	}	
}
