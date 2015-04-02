package com.fitech.model.worktask.model.pojo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * AfReportAgain entity. @author MyEclipse Persistence Tools
 */

public class AfReportAgain implements java.io.Serializable {

	// Fields

	private Integer rasId;
	private String cause;
	private Date setDate;
	private Integer templateType;
	private Integer repId;
	private Integer bak1;
	private String bak2;

	// Constructors

	/** default constructor */
	public AfReportAgain() {
	}

	/** full constructor */
	public AfReportAgain(String cause, Date setDate, Integer templateType,
			Integer repId, Integer bak1, String bak2) {
		this.cause = cause;
		this.setDate = setDate;
		this.templateType = templateType;
		this.repId = repId;
		this.bak1 = bak1;
		this.bak2 = bak2;
	}

	// Property accessors

	public Integer getRasId() {
		return this.rasId;
	}

	public void setRasId(Integer rasId) {
		this.rasId = rasId;
	}

	public String getCause() {
		return this.cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}

	public Date getSetDate() {
		return this.setDate;
	}

	public void setSetDate(Date setDate) {
		this.setDate = setDate;
	}

	public Integer getTemplateType() {
		return this.templateType;
	}

	public void setTemplateType(Integer templateType) {
		this.templateType = templateType;
	}

	public Integer getRepId() {
		return this.repId;
	}

	public void setRepId(Integer repId) {
		this.repId = repId;
	}

	public Integer getBak1() {
		return this.bak1;
	}

	public void setBak1(Integer bak1) {
		this.bak1 = bak1;
	}

	public String getBak2() {
		return this.bak2;
	}

	public void setBak2(String bak2) {
		this.bak2 = bak2;
	}

}