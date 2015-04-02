package com.fitech.net.form;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

import com.fitech.net.adapter.StrutsMRegionDelegate;


/**
 * 
 * @author jcm
 * @serialData 2006-07-01
 */
public class UtilMRegionForm {
	/**
	 * 机构地区
	 */
	private List regions = null;

	public List getRegions() {
		if(regions == null){
			List regionList = new ArrayList();
			try{
				List list = StrutsMRegionDelegate.findAll();
				for(int i=0;i<list.size();i++){
					MRegionForm mRegionForm = null;
					mRegionForm = (MRegionForm)list.get(i);
					regionList.add(new LabelValueBean(mRegionForm.getRegion_name(),mRegionForm.getRegion_id().toString()));
				}
				
			}catch(Exception ex){
				ex.printStackTrace();
				regionList = new ArrayList();
			}
			return regionList;
		}else return regions;
	}

	public void setRegions(List regions) {
		this.regions = regions;
	}	
}
