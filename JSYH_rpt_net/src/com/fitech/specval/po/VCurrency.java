package com.fitech.specval.po;

/**
 * VCurrency entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class VCurrency implements java.io.Serializable {

	// Fields

	private String VCcyId;
	private String VCcyCnname;
	private String VCcyEnname;
	private String mappingCcyid;

	// Constructors

	/** default constructor */
	public VCurrency() {
	}

	/** minimal constructor */
	public VCurrency(String VCcyId) {
		this.VCcyId = VCcyId;
	}

	/** full constructor */
	public VCurrency(String VCcyId, String VCcyCnname, String VCcyEnname,
			String mappingCcyid) {
		this.VCcyId = VCcyId;
		this.VCcyCnname = VCcyCnname;
		this.VCcyEnname = VCcyEnname;
		this.mappingCcyid = mappingCcyid;
	}

	// Property accessors

	public String getVCcyId() {
		return this.VCcyId;
	}

	public void setVCcyId(String VCcyId) {
		this.VCcyId = VCcyId;
	}

	public String getVCcyCnname() {
		return this.VCcyCnname;
	}

	public void setVCcyCnname(String VCcyCnname) {
		this.VCcyCnname = VCcyCnname;
	}

	public String getVCcyEnname() {
		return this.VCcyEnname;
	}

	public void setVCcyEnname(String VCcyEnname) {
		this.VCcyEnname = VCcyEnname;
	}

	public String getMappingCcyid() {
		return this.mappingCcyid;
	}

	public void setMappingCcyid(String mappingCcyid) {
		this.mappingCcyid = mappingCcyid;
	}

}