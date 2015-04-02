package com.fitech.gznx.po;

/**
 * VCurrency entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class VCurrency implements java.io.Serializable {

	// Fields

	private String vcId;
	private String vcCnnm;
	private String vcEnnm;

	// Constructors

	/** default constructor */
	public VCurrency() {
	}

	/** minimal constructor */
	public VCurrency(String vcId) {
		this.vcId = vcId;
	}

	/** full constructor */
	public VCurrency(String vcId, String vcCnnm, String vcEnnm) {
		this.vcId = vcId;
		this.vcCnnm = vcCnnm;
		this.vcEnnm = vcEnnm;
	}

	// Property accessors

	public String getVcId() {
		return this.vcId;
	}

	public void setVcId(String vcId) {
		this.vcId = vcId;
	}

	public String getVcCnnm() {
		return this.vcCnnm;
	}

	public void setVcCnnm(String vcCnnm) {
		this.vcCnnm = vcCnnm;
	}

	public String getVcEnnm() {
		return this.vcEnnm;
	}

	public void setVcEnnm(String vcEnnm) {
		this.vcEnnm = vcEnnm;
	}

}