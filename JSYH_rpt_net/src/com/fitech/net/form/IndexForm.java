package com.fitech.net.form;

import org.apache.struts.action.ActionForm;

public class IndexForm extends ActionForm {

	/**
	 * ָ��ID
	 */
	private Integer id = null;
	/**
	 * ָ������
	 */
	private String name = null;
	/**
	 * ȱʡֵ
	 */
	private String defaultValue = null;
	/**
	 * ָ�꾫��
	 */
	private String precision = null;
	/**
	 * ָ��ֵ����
	 */
	private String type = null;
	/**
	 * �Ƿ������
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
