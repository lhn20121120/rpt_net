package com.fitech.model.etl.model.vo;

import com.fitech.model.etl.model.pojo.EtlTimeType;

public class ETLFreqInfoVo {
	// Fields

	private String freqId;
	private String executeTime;
	private Integer usingFlag;
	
	private EtlTimeType etlTimeType=new EtlTimeType();
	
	/**此处字段不做数据库关联
	 * 只为页面日频度之类需要时分的类型为执行时间做字符串拼接*/
	private String hour;
	private String minutes;

	// Constructors

	/** default constructor */
	public ETLFreqInfoVo() {
	}

	/** minimal constructor */
	public ETLFreqInfoVo(String freqId) {
		this.freqId = freqId;
	}

	/** full constructor */
	public ETLFreqInfoVo(String freqId, EtlTimeType etlTimeType, String executeTime, Integer usingFlag) {
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

	public String getHour() {
		return hour;
	}

	public void setHour(String hour) {
		this.hour = hour;
	}

	public String getMinutes() {
		return minutes;
	}

	public void setMinutes(String minutes) {
		this.minutes = minutes;
	}
	
	
}
