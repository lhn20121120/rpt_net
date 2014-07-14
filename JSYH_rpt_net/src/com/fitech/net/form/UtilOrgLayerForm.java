package com.fitech.net.form;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

import com.fitech.net.adapter.StrutsOrgLayerDelegate;

/**
 * 
 * @author jcm
 * @serialData 2006-06-30
 */
public class UtilOrgLayerForm {
	/**
	 * 机构级别
	 */
	private List orgLayers = null;

	public List getOrgLayers() {
		if(orgLayers == null){
			List orgLayerList = new ArrayList();
			try{
				List list = StrutsOrgLayerDelegate.findAll();
				for(int i=0;i<list.size();i++){
					OrgLayerForm orgLayerForm = null;
					orgLayerForm = (OrgLayerForm)list.get(i);
					orgLayerList.add(new LabelValueBean(orgLayerForm.getOrg_layer_name(),orgLayerForm.getOrg_layer_id().toString()));
				}
			}catch(Exception e){
				e.printStackTrace();
				orgLayerList = new ArrayList();
			}
			return orgLayerList;
		}else
			return orgLayers;
	}

	public void setOrgLayers(List orgLayers) {
		this.orgLayers = orgLayers;
	}
}
