package com.fitech.net.form;

import org.apache.struts.action.ActionForm;

public class OrgTypeForm extends ActionForm {

	/**
	 * 机构类别ID
	 */
	private Integer org_type_id = null;
	/**
	 * 机构类别名称
	 */
	private String org_type_name = null;
	/**
	 * 机构级别ID
	 */
	private Integer org_layer_id = null;
	/**
	 * 机构级别名称
	 */
	private String org_layer_name = null;
	/**
	 * 上级机构类别
	 */
	private Integer pre_orgType_id = null;
	/**
	 * 上级机构名称
	 */
	private String pre_orgType_name = null;
		
	public OrgTypeForm(Integer org_layer_id, String org_layer_name, Integer org_type_id, String org_type_name, Integer pre_orgType_id, String pre_orgType_name) {
		super();
		// TODO Auto-generated constructor stub
		this.org_layer_id = org_layer_id;
		this.org_layer_name = org_layer_name;
		this.org_type_id = org_type_id;
		this.org_type_name = org_type_name;
		this.pre_orgType_id = pre_orgType_id;
		this.pre_orgType_name = pre_orgType_name;
	}
	public OrgTypeForm(){
		
	}

	public Integer getPre_orgType_id() {
		return pre_orgType_id;
	}
	public void setPre_orgType_id(Integer pre_orgType_id) {
		this.pre_orgType_id = pre_orgType_id;
	}
	public String getPre_orgType_name() {
		return pre_orgType_name;
	}
	public void setPre_orgType_name(String pre_orgType_name) {
		this.pre_orgType_name = pre_orgType_name;
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

	public Integer getOrg_type_id() {
		return org_type_id;
	}

	public void setOrg_type_id(Integer org_type_id) {
		this.org_type_id = org_type_id;
	}

	public String getOrg_type_name() {
		return org_type_name;
	}

	public void setOrg_type_name(String org_type_name) {
		this.org_type_name = org_type_name;
        //-------------------------------
        // gongming 2008-07-25
        if(null != this.org_type_name)
            this.org_type_name = this.org_type_name.trim();
	}
	
}
