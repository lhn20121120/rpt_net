package com.fitech.model.etl.model.pojo;

/**
 * EtlFreqInfo entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class EtlFreqInfo implements java.io.Serializable {

	// Fields

	private String freqId;
	private String executeTime;
	private Integer usingFlag;
	
	private EtlTimeType etlTimeType=new EtlTimeType();

	// Constructors

	/** default constructor */
	public EtlFreqInfo() {
	}

	/** minimal constructor */
	public EtlFreqInfo(String freqId) {
		this.freqId = freqId;
	}

	/** full constructor */
	public EtlFreqInfo(String freqId, EtlTimeType etlTimeType, String executeTime, Integer usingFlag) {
		this.freqId = freqId;
		this.etlTimeType = etlTimeType;
		this.executeTime = executeTime;
		this.usingFlag = usingFlag;
	}

	// Property accessors
	
	/***
	 * 频度任务id 外键关联TFreqInfo
	 */
	public String getFreqId() {
		return this.freqId;
	}

	public void setFreqId(String freqId) {
		this.freqId = freqId;
	}
	
	
	
	/***
	 * 执行时间
	 * @return
	 */
	public String getExecuteTime() {
		return this.executeTime;
	}

	public void setExecuteTime(String executeTime) {
		this.executeTime = executeTime;
	}
	
	/***
	 * 是否启用
	 * 0为不启用
	 * 1为启用
	 * @return
	 */
	public Integer getUsingFlag() {
		return this.usingFlag;
	}

	public void setUsingFlag(Integer usingFlag) {
		this.usingFlag = usingFlag==null || usingFlag<0?0:usingFlag;
	}
	
	/***
	 * 时间类型id　外键关联ETLTimeType
	 * @return
	 */
	public EtlTimeType getEtlTimeType() {
		return etlTimeType;
	}

	public void setEtlTimeType(EtlTimeType etlTimeType) {
		this.etlTimeType = etlTimeType;
	}
	
	

}