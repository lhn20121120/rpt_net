package com.fitech.net.form;

import org.apache.struts.action.ActionForm;

public class MRegionForm extends ActionForm {

	/**
	 * 地区ID
	 */
	private Integer region_id = null;
	/**
	 * 地区名称
	 */
	private String region_name = null;
	/**
	 * 所属地区ID
	 */
	private Integer pre_region_id = null;
	/**
	 * 所属地区名称
	 */
	private String pre_region_name = null;
	/**
	 * 地区所属机构类型ID
	 */
	private Integer org_type_id = null;
	/**
	 * 地区所属机构类型名称
	 */
	private String org_type_name = null;
	/**
	 * 被建机构ID
	 */
	private java.lang.String setOrgId;

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
	}

	public Integer getPre_region_id() {
		return pre_region_id;
	}

	public void setPre_region_id(Integer pre_region_id) {
		this.pre_region_id = pre_region_id;
	}

	public String getPre_region_name() {
		return pre_region_name;
	}

	public void setPre_region_name(String pre_region_name) {
		this.pre_region_name = pre_region_name;
	}

	public Integer getRegion_id() {
		return region_id;
	}

	public void setRegion_id(Integer region_id) {
		this.region_id = region_id;
	}

	public String getRegion_name() {
		return region_name;
	}

	public void setRegion_name(String region_name) {
		this.region_name = region_name;
	}

	public java.lang.String getSetOrgId() {
		return setOrgId;
	}

	public void setSetOrgId(java.lang.String setOrgId) {
		this.setOrgId = setOrgId;
	}
	
	
}
