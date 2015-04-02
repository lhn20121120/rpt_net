package com.fitech.model.etl.model.pojo;

/**
 * TFreqInfo entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class TFreqInfo implements java.io.Serializable {

	// Fields

	private String freqId;
	private String freqName;
	private String freqDesc;
	private String systemId="bank";
	private String bankFreqId="1";
	private Integer orderId;

	// Constructors

	/** default constructor */
	public TFreqInfo() {
	}

	/** full constructor */
	public TFreqInfo(String freqName, String freqDesc, String systemId, String bankFreqId, Integer orderId) {
		this.freqName = freqName;
		this.freqDesc = freqDesc;
		this.systemId = systemId;
		this.bankFreqId = bankFreqId;
		this.orderId = orderId;
	}

	// Property accessors
	
	/***
	 * 频度Id
	 */
	public String getFreqId() {
		return this.freqId;
	}
	
	public void setFreqId(String freqId) {
		this.freqId = freqId;
	}
	
	/**频度名称*/
	public String getFreqName() {
		return this.freqName;
	}

	public void setFreqName(String freqName) {
		this.freqName = freqName;
	}
	
	/**频度描述*/
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
	
	/**银行内部行内id*/
	public String getBankFreqId() {
		return this.bankFreqId;
	}

	public void setBankFreqId(String bankFreqId) {
		this.bankFreqId = bankFreqId;
	}

	public Integer getOrderId() {
		return this.orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

}