package com.fitech.gznx.po;

/**
 * AfQdReportdata entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class AfQdReportdata implements java.io.Serializable {

	// Fields

	private AfQdReportdataId id;
	private String cellName;
	private String cellData;
	private String bak1;
	private String bak2;

	// Constructors

	/** default constructor */
	public AfQdReportdata() {
	}

	/** minimal constructor */
	public AfQdReportdata(AfQdReportdataId id, String cellName) {
		this.id = id;
		this.cellName = cellName;
	}

	/** full constructor */
	public AfQdReportdata(AfQdReportdataId id, String cellName,
			String cellData, String bak1, String bak2) {
		this.id = id;
		this.cellName = cellName;
		this.cellData = cellData;
		this.bak1 = bak1;
		this.bak2 = bak2;
	}

	// Property accessors

	public AfQdReportdataId getId() {
		return this.id;
	}

	public void setId(AfQdReportdataId id) {
		this.id = id;
	}

	public String getCellName() {
		return this.cellName;
	}

	public void setCellName(String cellName) {
		this.cellName = cellName;
	}

	public String getCellData() {
		return this.cellData;
	}

	public void setCellData(String cellData) {
		this.cellData = cellData;
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