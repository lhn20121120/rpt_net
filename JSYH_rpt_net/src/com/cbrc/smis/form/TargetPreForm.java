package com.cbrc.smis.form;

import org.apache.struts.action.ActionForm;

public class TargetPreForm extends ActionForm{
	
	private String year;
	private String month;
	private String targetType;
	private String targetOpType;
	
	public TargetPreForm(){}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getTargetOpType() {
		return targetOpType;
	}

	public void setTargetOpType(String targetOpType) {
		this.targetOpType = targetOpType;
	}

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}
	

}
