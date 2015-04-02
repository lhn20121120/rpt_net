package com.cbrc.smis.form;

import org.apache.struts.action.ActionForm;

public class TargetDefineForm extends ActionForm{

	private String targetName;
	private String targetType;
	private String targetOpType;
	private String startDate;
	private String endDate;
	private String expreDefine;
	private String lawDefine;
	
	public TargetDefineForm(){}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getExpreDefine() {
		return expreDefine;
	}

	public void setExpreDefine(String expreDefine) {
		this.expreDefine = expreDefine;
	}

	public String getLawDefine() {
		return lawDefine;
	}

	public void setLawDefine(String lawDefine) {
		this.lawDefine = lawDefine;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getTargetName() {
		return targetName;
	}

	public void setTargetName(String targetName) {
		this.targetName = targetName;
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
}
