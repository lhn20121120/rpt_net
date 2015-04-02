package com.fitech.model.worktask.model.pojo;

import java.math.BigDecimal;

/**
 * TFreqInfo entity. @author MyEclipse Persistence Tools
 */

public class TFreqInfo implements java.io.Serializable {

	// Fields

	private String freqId;
	private String freqName;
	private String freqDesc;
	private String systemId;
	private String bankFreqId;
	private BigDecimal orderId;

	// Constructors

	/** default constructor */
	public TFreqInfo() {
	}

	/** minimal constructor */
	public TFreqInfo(String freqId) {
		this.freqId = freqId;
	}

	/** full constructor */
	public TFreqInfo(String freqId, String freqName, String freqDesc,
			String systemId, String bankFreqId, BigDecimal orderId) {
		this.freqId = freqId;
		this.freqName = freqName;
		this.freqDesc = freqDesc;
		this.systemId = systemId;
		this.bankFreqId = bankFreqId;
		this.orderId = orderId;
	}

	// Property accessors

	public String getFreqId() {
		return this.freqId;
	}

	public void setFreqId(String freqId) {
		this.freqId = freqId;
	}

	public String getFreqName() {
		return this.freqName;
	}

	public void setFreqName(String freqName) {
		this.freqName = freqName;
	}

	public String getFreqDesc() {
		return this.freqDesc;
	}

	public void setFreqDesc(String freqDesc) {
		this.freqDesc = freqDesc;
	}

	public String getSystemId() {
		return this.systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public String getBankFreqId() {
		return this.bankFreqId;
	}

	public void setBankFreqId(String bankFreqId) {
		this.bankFreqId = bankFreqId;
	}

	public BigDecimal getOrderId() {
		return this.orderId;
	}

	public void setOrderId(BigDecimal orderId) {
		this.orderId = orderId;
	}

}