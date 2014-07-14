package com.fitech.gznx.po;

/**
 * AfPboccell entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class AfPboccell implements java.io.Serializable {

	// Fields

	private AfPboccellId id;
	private String curId;
	private String dataType;
	private String psuziType;
	private String danweiId;

	// Constructors

	/** default constructor */
	public AfPboccell() {
	}

	/** minimal constructor */
	public AfPboccell(AfPboccellId id) {
		this.id = id;
	}

	/** full constructor */
	public AfPboccell(AfPboccellId id, String curId, String dataType,
			String psuziType, String danweiId) {
		this.id = id;
		this.curId = curId;
		this.dataType = dataType;
		this.psuziType = psuziType;
		this.danweiId = danweiId;
	}

	// Property accessors

	public AfPboccellId getId() {
		return this.id;
	}

	public void setId(AfPboccellId id) {
		this.id = id;
	}

	public String getCurId() {
		return this.curId;
	}

	public void setCurId(String curId) {
		this.curId = curId;
	}

	public String getDataType() {
		return this.dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getPsuziType() {
		return this.psuziType;
	}

	public void setPsuziType(String psuziType) {
		this.psuziType = psuziType;
	}

	public String getDanweiId() {
		return this.danweiId;
	}

	public void setDanweiId(String danweiId) {
		this.danweiId = danweiId;
	}

}