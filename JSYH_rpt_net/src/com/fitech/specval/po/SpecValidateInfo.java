package com.fitech.specval.po;

/**
 * SpecValidateInfo entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SpecValidateInfo implements java.io.Serializable {

	// Fields

	private Long speValId;
	private String valName;
	private String valFormula;
	private String valDes;
	private Long valFlag;
	private String bak1;
	private String bak2;

	// Constructors

	/** default constructor */
	public SpecValidateInfo() {
	}

	/** minimal constructor */
	public SpecValidateInfo(Long speValId) {
		this.speValId = speValId;
	}

	/** full constructor */
	public SpecValidateInfo(Long speValId, String valName, String valFormula,
			String valDes, Long valFlag, String bak1, String bak2) {
		this.speValId = speValId;
		this.valName = valName;
		this.valFormula = valFormula;
		this.valDes = valDes;
		this.valFlag = valFlag;
		this.bak1 = bak1;
		this.bak2 = bak2;
	}

	// Property accessors

	public Long getSpeValId() {
		return this.speValId;
	}

	public void setSpeValId(Long speValId) {
		this.speValId = speValId;
	}

	public String getValName() {
		return this.valName;
	}

	public void setValName(String valName) {
		this.valName = valName;
	}

	public String getValFormula() {
		return this.valFormula;
	}

	public void setValFormula(String valFormula) {
		this.valFormula = valFormula;
	}

	public String getValDes() {
		return this.valDes;
	}

	public void setValDes(String valDes) {
		this.valDes = valDes;
	}

	public Long getValFlag() {
		return this.valFlag;
	}

	public void setValFlag(Long valFlag) {
		this.valFlag = valFlag;
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