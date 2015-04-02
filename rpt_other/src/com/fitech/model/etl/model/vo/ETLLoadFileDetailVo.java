package com.fitech.model.etl.model.vo;

import com.fitech.model.etl.model.pojo.EtlLoadFileDetailId;

public class ETLLoadFileDetailVo {
	// Fields

	private EtlLoadFileDetailId id=new EtlLoadFileDetailId();
	private String tableCode;
	private String columnCode;

	// Constructors

	/** default constructor */
	public ETLLoadFileDetailVo() {
	}

	/** minimal constructor */
	public ETLLoadFileDetailVo(EtlLoadFileDetailId id, String columnCode) {
		this.id = id;
		this.columnCode = columnCode;
	}

	/** full constructor */
	public ETLLoadFileDetailVo(EtlLoadFileDetailId id, String tableCode, String columnCode) {
		this.id = id;
		this.tableCode = tableCode;
		this.columnCode = columnCode;
	}

	// Property accessors

	public EtlLoadFileDetailId getId() {
		return this.id;
	}

	public void setId(EtlLoadFileDetailId id) {
		this.id = id;
	}

	public String getTableCode() {
		return this.tableCode;
	}

	public void setTableCode(String tableCode) {
		this.tableCode = tableCode;
	}

	public String getColumnCode() {
		return this.columnCode;
	}

	public void setColumnCode(String columnCode) {
		this.columnCode = columnCode;
	}
}
