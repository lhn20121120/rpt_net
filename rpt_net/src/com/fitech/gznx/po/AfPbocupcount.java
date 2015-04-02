package com.fitech.gznx.po;

/**
 * AfPbocupcount entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class AfPbocupcount implements java.io.Serializable {

	// Fields

	private String fileName;
	private Long times;

	// Constructors

	/** default constructor */
	public AfPbocupcount() {
	}

	/** minimal constructor */
	public AfPbocupcount(String fileName) {
		this.fileName = fileName;
	}

	/** full constructor */
	public AfPbocupcount(String fileName, Long times) {
		this.fileName = fileName;
		this.times = times;
	}

	// Property accessors

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Long getTimes() {
		return this.times;
	}

	public void setTimes(Long times) {
		this.times = times;
	}

}