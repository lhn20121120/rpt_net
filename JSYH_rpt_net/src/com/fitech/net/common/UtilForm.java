package com.fitech.net.common;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

import com.fitech.net.org.StrutsOrgTogetherDelegate;
import com.fitech.net.org.form.OrgnetForm;
import com.fitech.net.orgcls.form.OrgclsNetForm;
import com.fitech.net.region.form.RegionForm;


public class UtilForm {
	
	private List deptName=null;
	
	public void setDeptName(List deptName) {
		this.deptName = deptName;
	}
	public List getDeptName() {
		if(this.deptName==null){
			List rtList=new ArrayList();			
			try{				
				List list = StrutsOrgTogetherDelegate.findAllfindAllDeptName();
				if(list!=null && list.size()>0){
					OrgnetForm regionTypNetForm=null;
					for(int i=0;i<list.size();i++){
						regionTypNetForm=(OrgnetForm)list.get(i);
						rtList.add(new LabelValueBean(regionTypNetForm.getDeptName(), regionTypNetForm.getDeptName()));				
						}
					}
			}catch(Exception e){
				e.printStackTrace();
				rtList=null;
				}
			return rtList;
		}else{
			return this.deptName;
		}
		
	}
	/**
	 * 地区的id应该是regionId
	 */ 
	private List region = null;		
	/**
	 * 获取地区
	 * 
	 * @return List
	 */
	public void setRegion(List region) {
		this.region = region;
	}
	public List getRegion() {		
		if(this.region==null){
			List rtList=new ArrayList();			
			try{				
				List list = StrutsOrgTogetherDelegate.findAllfindAllregion();
				if(list!=null && list.size()>0){
					RegionForm regionTypNetForm=null;
					for(int i=0;i<list.size();i++){
						regionTypNetForm=(RegionForm)list.get(i);
						rtList.add(new LabelValueBean(regionTypNetForm.getRegionName(), regionTypNetForm.getRegionId()));				
						}
					}
			}catch(Exception e){
				e.printStackTrace();
				rtList=null;}
			return rtList;
		}else{
			return this.region;
		}
		}
	/**
	 * 地区的id应该是regionId
	 */ 
	private List regionTypId = null;		
	/**
	 * 获取地区
	 * 
	 * @return List
	 */
	public void setRegionTypId(List regionTypId) {
		this.regionTypId = regionTypId;
	}
	public List getRegionTypId() {		
		if(this.regionTypId==null){
			List rtList=new ArrayList();			
			try{				
				List list = StrutsOrgTogetherDelegate.findAllfindAllregiontyp();
				if(list!=null && list.size()>0){
					RegionForm regionTypNetForm=null;
					for(int i=0;i<list.size();i++){
						regionTypNetForm=(RegionForm)list.get(i);
						rtList.add(new LabelValueBean(regionTypNetForm.getRegionTypId(), regionTypNetForm.getRegionTypId()));				
						}
					}
			}catch(Exception e){
				e.printStackTrace();
				rtList=null;}
			return rtList;
		}else{
			return this.regionTypId;
		}
		}
	
	/**
	 * 机构类型(真实机构类型和虚拟机构类型)
	 */
	private List orgCls = null;
		
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
				
				List list = StrutsOrgTogetherDelegate.findAllCopy();
				
				// System.out.println("list  " +list);
				if(list!=null && list.size()>0){
					OrgclsNetForm mOrgClForm=null;
					for(int i=0;i<list.size();i++){
						mOrgClForm=(OrgclsNetForm)list.get(i);
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
	/**
	 * 地区的id应该是OATId
	 *//* 
	private List oatIdList = null;
	*//**
	 * 获取OATId
	 * 
	 * @return List
	 *//*
	public List getOatIdList() {

		 if (this.oatIdList!=null) {
	            return oatIdList;
	        }
	        else {
	            ArrayList lists =  new ArrayList();
	            
	            List results = null;
	            try {
	                results = StrutsOrgTogetherDelegate.findOatId();
	                if (results!=null) {
	                    for (int i=0; i<results.size(); i++) {
	                    	OrgActuTypeForm  orgActuTypeForm=(OrgActuTypeForm)(results.get(i));
	                    	lists.add(new LabelValueBean(orgActuTypeForm.getOATId().toString(),orgActuTypeForm.getOATName()));				
							}
	                }
	            } catch (Exception e) {
	                // TODO 自动生成 catch 块
	                e.printStackTrace();
	            }
	            return lists;
	            
	        } 
		
	}
	public void setOatId(List oatIdList) {
		this.oatIdList = oatIdList;
	}*/

	
}