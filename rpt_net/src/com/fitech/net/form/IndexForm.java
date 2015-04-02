package com.fitech.net.form;

import org.apache.struts.action.ActionForm;

public class IndexForm extends ActionForm {

	/**
	 * 指标ID
	 */
	private Integer id = null;
	/**
	 * 指标名称
	 */
	private String name = null;
	/**
	 * 缺省值
	 */
	private String defaultValue = null;
	/**
	 * 指标精度
	 */
	private String precision = null;
	/**
	 * 指标值类型
	 */
	private String type = null;
	/**
	 * 是否汇总项
	 */
	private String isCollect = null;
	
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getIsCollect() {
		return isCollect;
	}
	public void setIsCollect(String isCollect) {
		this.isCollect = isCollect;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPrecision() {
		return precision;
	}
	public void setPrecision(String precision) {
		this.precision = precision;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
