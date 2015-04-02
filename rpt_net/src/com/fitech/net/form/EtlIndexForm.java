package com.fitech.net.form;

import org.apache.struts.action.ActionForm;

public class EtlIndexForm  extends ActionForm {
	 private String indexName;
     private String formual;
     private String desc;
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getFormual() {
		return formual;
	}
	public void setFormual(String formual) {
		this.formual = formual;
	}
	public String getIndexName() {
		return indexName;
	}
	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}
}
