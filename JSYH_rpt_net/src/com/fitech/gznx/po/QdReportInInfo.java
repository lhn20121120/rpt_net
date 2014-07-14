package com.fitech.gznx.po;

/**
 * QdReportInInfo entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class QdReportInInfo implements java.io.Serializable {

	// Fields

	private QdReportInInfoId id;
	private String cellName;
	private String reportValue;
	private String thanPrevRise;
	private String thanSameRise;
	private String thanSameFall;
	private String thanPrevFall;
	private String bak1;
	private String bak2;

	// Constructors

	/** default constructor */
	public QdReportInInfo() {
	}

	/** minimal constructor */
	public QdReportInInfo(QdReportInInfoId id, String cellName) {
		this.id = id;
		this.cellName = cellName;
	}

	/** full constructor */
	public QdReportInInfo(QdReportInInfoId id, String cellName,
			String reportValue, String thanPrevRise, String thanSameRise,
			String thanSameFall, String thanPrevFall, String bak1, String bak2) {
		this.id = id;
		this.cellName = cellName;
		this.reportValue = reportValue;
		this.thanPrevRise = thanPrevRise;
		this.thanSameRise = thanSameRise;
		this.thanSameFall = thanSameFall;
		this.thanPrevFall = thanPrevFall;
		this.bak1 = bak1;
		this.bak2 = bak2;
	}

	// Property accessors

	public QdReportInInfoId getId() {
		return this.id;
	}

	public void setId(QdReportInInfoId id) {
		this.id = id;
	}

	public String getCellName() {
		return this.cellName;
	}

	public void setCellName(String cellName) {
		this.cellName = cellName;
	}

	public String getReportValue() {
		return this.reportValue;
	}

	public void setReportValue(String reportValue) {
		this.reportValue = reportValue;
	}

	public String getThanPrevRise() {
		return this.thanPrevRise;
	}

	public void setThanPrevRise(String thanPrevRise) {
		this.thanPrevRise = thanPrevRise;
	}

	public String getThanSameRise() {
		return this.thanSameRise;
	}

	public void setThanSameRise(String thanSameRise) {
		this.thanSameRise = thanSameRise;
	}

	public String getThanSameFall() {
		return this.thanSameFall;
	}

	public void setThanSameFall(String thanSameFall) {
		this.thanSameFall = thanSameFall;
	}

	public String getThanPrevFall() {
		return this.thanPrevFall;
	}

	public void setThanPrevFall(String thanPrevFall) {
		this.thanPrevFall = thanPrevFall;
	}

	public String getBak1() {
		return this.bak1;
	}

	public void setBak1(String bak1) {
		this.bak1 = bak1;
	}

	public String getBak2() {
		return this.bak2;
	}

	public void setBak2(String bak2) {
		this.bak2 = bak2;
	}

}