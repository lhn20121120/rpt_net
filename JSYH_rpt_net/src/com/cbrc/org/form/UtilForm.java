package com.cbrc.org.form;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

import com.cbrc.org.adapter.StrutsMOrgClDelegate;

/**
 * 工具ActionForm
 * 
 * @author rds
 * @serialData 2005-12-07
 */
public class UtilForm {
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
}
