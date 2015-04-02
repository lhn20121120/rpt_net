package com.fitech.model.etl.model.pojo;

/**
 * EtlTimeType entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class EtlTimeType implements java.io.Serializable {

	// Fields

	private Integer timeTypeId;
	private String timeTypeName;

	// Constructors

	/** default constructor */
	public EtlTimeType() {
	}

	/** full constructor */
	public EtlTimeType(String timeTypeName) {
		this.timeTypeName = timeTypeName;
	}

	// Property accessors
	
	/***
	 * 时间类型id
	 */
	public Integer getTimeTypeId() {
		return this.timeTypeId;
	}

	public void setTimeTypeId(Integer timeTypeId) {
		this.timeTypeId = timeTypeId;
	}
	
	/***
	 * 执行时间类型  用于区分执行时间是几时几分或者是期后多少天
	 * 1.时间点 如：21:02
	 * 2:期后多少天 如:期后8天
	 * @return
	 */
	public String getTimeTypeName() {
		return this.timeTypeName;
	}

	public void setTimeTypeName(String timeTypeName) {
		this.timeTypeName = timeTypeName;
	}

}