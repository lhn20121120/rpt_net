package com.fitech.gznx.po;

/**
 * AfCellinfo entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class AfCellinfo implements java.io.Serializable {

	// Fields

	private Long cellId;
	private String rowName;
	private String colName;
	private String rowNum;
	private String colNum;
	private String cellName;
	private Long collectType;
	private String templateId;
	private Long dataType;
	private String versionId;
	private String cellPid;

	// Constructors

	/** default constructor */
	public AfCellinfo() {
	}

	/** minimal constructor */
	public AfCellinfo(Long cellId, String templateId, String versionId) {
		this.cellId = cellId;
		this.templateId = templateId;
		this.versionId = versionId;
	}

	/** full constructor */
	public AfCellinfo(Long cellId, String rowName, String colName,
			String rowNum, String colNum, String cellName, Long collectType,
			String templateId, Long dataType, String versionId) {
		this.cellId = cellId;
		this.rowName = rowName;
		this.colName = colName;
		this.rowNum = rowNum;
		this.colNum = colNum;
		this.cellName = cellName;
		this.collectType = collectType;
		this.templateId = templateId;
		this.dataType = dataType;
		this.versionId = versionId;
	}

	// Property accessors

	public Long getCellId() {
		return this.cellId;
	}

	public void setCellId(Long cellId) {
		this.cellId = cellId;
	}

	public String getRowName() {
		return this.rowName;
	}

	public void setRowName(String rowName) {
		this.rowName = rowName;
	}

	public String getColName() {
		return this.colName;
	}

	public void setColName(String colName) {
		this.colName = colName;
	}

	public String getRowNum() {
		return this.rowNum;
	}

	public void setRowNum(String rowNum) {
		this.rowNum = rowNum;
	}

	public String getColNum() {
		return this.colNum;
	}

	public void setColNum(String colNum) {
		this.colNum = colNum;
	}

	public String getCellName() {
		return this.cellName;
	}

	public void setCellName(String cellName) {
		this.cellName = cellName;
	}

	public Long getCollectType() {
		return this.collectType;
	}

	public void setCollectType(Long collectType) {
		this.collectType = collectType;
	}

	public String getTemplateId() {
		return this.templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public Long getDataType() {
		return this.dataType;
	}

	public void setDataType(Long dataType) {
		this.dataType = dataType;
	}

	public String getVersionId() {
		return this.versionId;
	}

	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}

	public String getCellPid() {
		return cellPid;
	}

	public void setCellPid(String cellPid) {
		this.cellPid = cellPid;
	}

}