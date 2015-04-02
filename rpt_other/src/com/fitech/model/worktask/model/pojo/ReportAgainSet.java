package com.fitech.model.worktask.model.pojo;

import java.util.Date;

/**
 * ReportAgainSet entity. @author MyEclipse Persistence Tools
 */

public class ReportAgainSet implements java.io.Serializable {

	// Fields

	private Long rasId;
	private String cause;
	private Date setDate;
	private Long repInId;

	// Constructors

	/** default constructor */
	public ReportAgainSet() {
	}

	/** full constructor */
	public ReportAgainSet(String cause, Date setDate, Long repInId) {
		this.cause = cause;
		this.setDate = setDate;
		this.repInId = repInId;
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

	public Long getRepInId() {
		return this.repInId;
	}

	public void setRepInId(Long repInId) {
		this.repInId = repInId;
	}

}