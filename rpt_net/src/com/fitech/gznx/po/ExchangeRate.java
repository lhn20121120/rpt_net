package com.fitech.gznx.po;

/**
 * ExchangeRate entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ExchangeRate implements java.io.Serializable {

	// Fields

	private Long erId;
	private String rateDate;
	private String sourceVcId;
	private String targetVcId;
	private String extRate;
	private String bak1;
	private String bak2;

	private com.fitech.specval.po.VCurrency sourceVc;
	private com.fitech.specval.po.VCurrency targetVc;
	
	// Constructors

	/** default constructor */
	public ExchangeRate() {
	}

	/** minimal constructor */
	public ExchangeRate(Long erId) {
		this.erId = erId;
	}

	/** full constructor */
	public ExchangeRate(Long erId, String rateDate, String sourceVcId,
			String targetVcId, String extRate, String bak1, String bak2) {
		this.erId = erId;
		this.rateDate = rateDate;
		this.sourceVcId = sourceVcId;
		this.targetVcId = targetVcId;
		this.extRate = extRate;
		this.bak1 = bak1;
		this.bak2 = bak2;
	}

	// Property accessors

	public Long getErId() {
		return this.erId;
	}

	public void setErId(Long erId) {
		this.erId = erId;
	}

	public String getRateDate() {
		return this.rateDate;
	}

	public void setRateDate(String rateDate) {
		this.rateDate = rateDate;
	}

	public String getSourceVcId() {
		return this.sourceVcId;
	}

	public void setSourceVcId(String sourceVcId) {
		this.sourceVcId = sourceVcId;
	}

	public String getTargetVcId() {
		return this.targetVcId;
	}

	public void setTargetVcId(String targetVcId) {
		this.targetVcId = targetVcId;
	}

	public String getExtRate() {
		return this.extRate;
	}

	public void setExtRate(String extRate) {
		this.extRate = extRate;
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

	public com.fitech.specval.po.VCurrency getSourceVc() {
		return sourceVc;
	}

	public void setSourceVc(com.fitech.specval.po.VCurrency sourceVc) {
		this.sourceVc = sourceVc;
	}

	public com.fitech.specval.po.VCurrency getTargetVc() {
		return targetVc;
	}

	public void setTargetVc(com.fitech.specval.po.VCurrency targetVc) {
		this.targetVc = targetVc;
	}

}