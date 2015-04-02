package com.fitech.gznx.po;

import java.util.Date;

/**
 * AfReportAgain entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class AfReportAgain implements java.io.Serializable {

	// Fields

	private Long rasId;
	private String cause;
	private Date setDate;
	private Long templateType;
	private Long repId;
	private Long bak1;
	private String bak2;

	private AfReport afReport;
	
	
	// Constructors

	public AfReport getAfReport() {
		return afReport;
	}

	public void setAfReport(AfReport afReport) {
		this.afReport = afReport;
	}

	/** default constructor */
	public AfReportAgain() {
	}

	/** minimal constructor */
	public AfReportAgain(Long rasId) {
		this.rasId = rasId;
	}

	/** full constructor */
	public AfReportAgain(Long rasId, String cause, Date setDate,
			Long templateType, Long repId, Long bak1, String bak2) {
		this.rasId = rasId;
		this.cause = cause;
		this.setDate = setDate;
		this.templateType = templateType;
		this.repId = repId;
		this.bak1 = bak1;
		this.bak2 = bak2;
	}

	// Property accessors

	public Long getRasId() {
		return this.rasId;
	}

	public void setRasId(Long rasId) {
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

	public Long getTemplateType() {
		return this.templateType;
	}

	public void setTemplateType(Long templateType) {
		this.templateType = templateType;
	}

	public Long getRepId() {
		return this.repId;
	}

	public void setRepId(Long repId) {
		this.repId = repId;
	}

	public Long getBak1() {
		return this.bak1;
	}

	public void setBak1(Long bak1) {
		this.bak1 = bak1;
	}

	public String getBak2() {
		return this.bak2;
	}

	public void setBak2(String bak2) {
		this.bak2 = bak2;
	}

}