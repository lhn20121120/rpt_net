package com.fitech.gznx.po;

/**
 * AfCodelib entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class AfCodelib implements java.io.Serializable {

	// Fields

	 private AfCodelibId id;
	private String typeName;
	private String codeName;
	private Long editFlg;
	private String codeBak;

	// Constructors

	/** default constructor */
	public AfCodelib() {
	}

	/** minimal constructor */
	public AfCodelib(AfCodelibId id) {
		this.id = id;
	}

	/** full constructor */
	public AfCodelib(AfCodelibId id, String typeName,
			String codeName, Long editFlg, String codeBak) {
		this.id = id;
		this.typeName = typeName;
		this.codeName = codeName;
		this.editFlg = editFlg;
		this.codeBak = codeBak;
	}

	// Property accessors



	public String getTypeName() {
		return this.typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getCodeName() {
		return this.codeName;
	}

	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}

	public Long getEditFlg() {
		return this.editFlg;
	}

	public void setEditFlg(Long editFlg) {
		this.editFlg = editFlg;
	}

	public String getCodeBak() {
		return this.codeBak;
	}

	public void setCodeBak(String codeBak) {
		this.codeBak = codeBak;
	}

	public AfCodelibId getId() {
		return id;
	}

	public void setId(AfCodelibId id) {
		this.id = id;
	}

}