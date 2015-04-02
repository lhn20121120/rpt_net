package com.fitech.model.etl.model.vo;

/***
 * 频度信息表
 * @author admin
 *
 */
public class TFreqInfoVo {
	// Fields

	private String freqId;
	private String freqName;
	private String freqDesc;
	private Integer orderId;
	private String systemId;
	private String bankFreqId;
	private String executeTime;
	private Integer timeTypeId;
	
	/**此处字段不做数据库关联
	 * 只为页面日频度之类需要时分的类型为执行时间做字符串拼接*/
	private String hour;
	private String minutes;
	
	// Constructors

	/** default constructor */
	public TFreqInfoVo() {
	}

	/** full constructor */
	public TFreqInfoVo(String freqName, String freqDesc, Integer orderId, String systemId, String bankFreqId, String executeTime, Integer timeTypeId) {
		this.freqName = freqName;
		this.freqDesc = freqDesc;
		this.orderId = orderId;
		this.systemId = systemId;
		this.bankFreqId = bankFreqId;
		this.executeTime = executeTime;
		this.timeTypeId = timeTypeId;
	}

	// Property accessors
	/**主键Id*/
	public String getFreqId() {
		return this.freqId;
	}

	public void setFreqId(String freqId) {
		this.freqId = freqId;
	}
	
	/***频度展示名称*/
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
	
	/**排序字段*/
	public Integer getOrderId() {
		return this.orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	
	/**体系编号*/
	public String getSystemId() {
		return this.systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}
	
	/**行内频度编号*/
	public String getBankFreqId() {
		return this.bankFreqId;
	}

	public void setBankFreqId(String bankFreqId) {
		this.bankFreqId = bankFreqId;
	}
	
	/**执行时间*/
	public String getExecuteTime() {
		return this.executeTime;
	}

	public void setExecuteTime(String executeTime) {
		this.executeTime = executeTime;
	}
	
	/**外键 关联到T_TIME_TYPE 定时模式
	 * 用以区分分钟或者天数*/
	public Integer getTimeTypeId() {
		return this.timeTypeId;
	}

	public void setTimeTypeId(Integer timeTypeId) {
		this.timeTypeId = timeTypeId;
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
