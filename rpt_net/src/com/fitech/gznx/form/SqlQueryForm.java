package com.fitech.gznx.form;

import org.apache.struts.action.ActionForm;

public class SqlQueryForm extends ActionForm {
	private String contents;
	
	public String getContents() {
		return contents;
	}
	
	public void setContents(String contents) {
		this.contents = contents;
	}
}
