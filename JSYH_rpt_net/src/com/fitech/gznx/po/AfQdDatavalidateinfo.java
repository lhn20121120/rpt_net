package com.fitech.gznx.po;

/**
 * AfQdDatavalidateinfo entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class AfQdDatavalidateinfo implements java.io.Serializable {

	// Fields

	private AfQdDatavalidateinfoId id;
	private Long result;
	private String cause;

	private String sourceValue;
	private String targetValue;
	
	// Constructors

	public String getSourceValue() {
		return sourceValue;
	}

	public void setSourceValue(String sourceValue) {
		this.sourceValue = sourceValue;
	}

	public String getTargetValue() {
		return targetValue;
	}

	public void setTargetValue(String targetValue) {
		this.targetValue = targetValue;
	}

	/** default constructor */
	public AfQdDatavalidateinfo() {
	}

	/** minimal constructor */
	public AfQdDatavalidateinfo(AfQdDatavalidateinfoId id) {
		this.id = id;
	}

	/** full constructor */
	public AfQdDatavalidateinfo(AfQdDatavalidateinfoId id, Long result,
			String cause) {
		this.id = id;
		this.result = result;
		this.cause = cause;
	}

	// Property accessors

	public AfQdDatavalidateinfoId getId() {
		return this.id;
	}

	public void setId(AfQdDatavalidateinfoId id) {
		this.id = id;
	}

	public Long getResult() {
		return this.result;
	}

	public void setResult(Long result) {
		this.result = result;
	}

	public String getCause() {
		return this.cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}

}