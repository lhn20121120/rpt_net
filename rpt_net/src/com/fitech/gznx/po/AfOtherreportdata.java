package com.fitech.gznx.po;

/**
 * AfOtherreportdata entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class AfOtherreportdata implements java.io.Serializable {

	// Fields

	private AfOtherreportdataId id;
	private String cellData;

	// Constructors

	/** default constructor */
	public AfOtherreportdata() {
	}

	/** minimal constructor */
	public AfOtherreportdata(AfOtherreportdataId id) {
		this.id = id;
	}

	/** full constructor */
	public AfOtherreportdata(AfOtherreportdataId id, String cellData) {
		this.id = id;
		this.cellData = cellData;
	}

	// Property accessors

	public AfOtherreportdataId getId() {
		return this.id;
	}

	public void setId(AfOtherreportdataId id) {
		this.id = id;
	}

	public String getCellData() {
		return this.cellData;
	}

	public void setCellData(String cellData) {
		this.cellData = cellData;
	}

}