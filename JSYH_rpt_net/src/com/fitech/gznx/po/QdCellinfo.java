package com.fitech.gznx.po;

/**
 * QdCellinfo entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class QdCellinfo implements java.io.Serializable {

	// Fields

	private QdCellinfoId id=new QdCellinfoId();
	private Long colCount;
	private String startCol;
	private String endCol;
	private String flagCol;
	private String endFlag;
	private String tbr;
	private String fhr;
	private String shr;
	private Long startRow;
	private String bak1;
	private String bak2;
	private Long bak3;

	// Constructors

	/** default constructor */
	public QdCellinfo() {
	}

	/** minimal constructor */
	public QdCellinfo(QdCellinfoId id) {
		this.id = id;
	}

	/** full constructor */
	public QdCellinfo(QdCellinfoId id, Long colCount, String startCol,
			String endCol, String flagCol, String endFlag, String tbr,
			String fhr, String shr, Long startRow, String bak1, String bak2,
			Long bak3) {
		this.id = id;
		this.colCount = colCount;
		this.startCol = startCol;
		this.endCol = endCol;
		this.flagCol = flagCol;
		this.endFlag = endFlag;
		this.tbr = tbr;
		this.fhr = fhr;
		this.shr = shr;
		this.startRow = startRow;
		this.bak1 = bak1;
		this.bak2 = bak2;
		this.bak3 = bak3;
	}

	// Property accessors

	public QdCellinfoId getId() {
		return this.id;
	}

	public void setId(QdCellinfoId id) {
		this.id = id;
	}

	public Long getColCount() {
		return this.colCount;
	}

	public void setColCount(Long colCount) {
		this.colCount = colCount;
	}

	public String getStartCol() {
		return this.startCol;
	}

	public void setStartCol(String startCol) {
		this.startCol = startCol;
	}

	public String getEndCol() {
		return this.endCol;
	}

	public void setEndCol(String endCol) {
		this.endCol = endCol;
	}

	public String getFlagCol() {
		return this.flagCol;
	}

	public void setFlagCol(String flagCol) {
		this.flagCol = flagCol;
	}

	public String getEndFlag() {
		return this.endFlag;
	}

	public void setEndFlag(String endFlag) {
		this.endFlag = endFlag;
	}

	public String getTbr() {
		return this.tbr;
	}

	public void setTbr(String tbr) {
		this.tbr = tbr;
	}

	public String getFhr() {
		return this.fhr;
	}

	public void setFhr(String fhr) {
		this.fhr = fhr;
	}

	public String getShr() {
		return this.shr;
	}

	public void setShr(String shr) {
		this.shr = shr;
	}

	public Long getStartRow() {
		return this.startRow;
	}

	public void setStartRow(Long startRow) {
		this.startRow = startRow;
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

	public Long getBak3() {
		return this.bak3;
	}

	public void setBak3(Long bak3) {
		this.bak3 = bak3;
	}

}