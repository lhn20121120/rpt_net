package com.fitech.net.form;

import org.apache.struts.action.ActionForm;

public class OrgLayerForm extends ActionForm {

	/**
	 * 机构级别ID
	 */
	private Integer org_layer_id = null;
	
	/**
	 * 机构级别名称
	 */
	private String org_layer_name = null;

	public OrgLayerForm(){
		
	}
	
	public OrgLayerForm(Integer org_layer_id, String org_layer_name) {
		this.org_layer_id = org_layer_id;
		this.org_layer_name = org_layer_name;
	}

	public Integer getOrg_layer_id() {
		return org_layer_id;
	}

	public void setOrg_layer_id(Integer org_layer_id) {
		this.org_layer_id = org_layer_id;
	}

	public String getOrg_layer_name() {
		return org_layer_name;
	}

	public void setOrg_layer_name(String org_layer_name) {
		this.org_layer_name = org_layer_name;
	}
	
	
}
