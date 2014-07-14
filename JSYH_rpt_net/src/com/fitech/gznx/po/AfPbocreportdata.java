package com.fitech.gznx.po;

/**
 * AfPbocreportdata entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class AfPbocreportdata implements java.io.Serializable {

	// Fields

	private AfPbocreportdataId id;
	private String cellData;

	// Constructors

	/** default constructor */
	public AfPbocreportdata() {
	}

	/** minimal constructor */
	public AfPbocreportdata(AfPbocreportdataId id) {
		this.id = id;
	}

	/** full constructor */
	public AfPbocreportdata(AfPbocreportdataId id, String cellData) {
		this.id = id;
		this.cellData = cellData;
	}

	// Property accessors

	public AfPbocreportdataId getId() {
		return this.id;
	}

	public void setId(AfPbocreportdataId id) {
		this.id = id;
	}

	public String getCellData() {
		return this.cellData;
	}

	public void setCellData(String cellData) {
		this.cellData = cellData;
	}

}