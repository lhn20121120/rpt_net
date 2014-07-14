package com.cbrc.smis.form;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

import com.fitech.net.adapter.StrutsActuTargetResultDelegate;

public class UtilTargetForm {
 
	private List years = null;
	public String rangeId=null;
	public String repFreId=null;
	public String dataRangeId=null;
	public String orgId=null;

	public void setYears(List years) {
		this.years = years;
	}
	public String getRangeId() {
		return rangeId;
	}

	public void setRangeId(String rangeId) {
		this.rangeId = rangeId;
	}

	public  List getYears(){
		if(this.years == null){
			List targetList = new ArrayList();			
			try{
				List list = StrutsActuTargetResultDelegate.searchAllYear(rangeId,repFreId,dataRangeId,orgId);
				if(list!=null && list.size()>0){
					
					for(int i=0;i<list.size();i++){
						Integer in=(Integer)list.get(i);
						targetList.add(new LabelValueBean(in.toString(),in.toString()));
					}
				}
			}catch(Exception e){
				e.printStackTrace();
				targetList=new ArrayList();
			}
			return targetList;
		}else{
			return this.years;
		}
	}
	public String getDataRangeId() {
		return dataRangeId;
	}
	public void setDataRangeId(String dataRangeId) {
		this.dataRangeId = dataRangeId;
	}
	public String getRepFreId() {
		return repFreId;
	}
	public void setRepFreId(String repFreId) {
		this.repFreId = repFreId;
	}
	/**
	 * 返回 orgId
	 */
	public String getOrgId() {
		return orgId;
	}
	/**
	 * 参数：orgId 
	 * 设置 orgId
	 */
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}


}
