package com.fitech.model.etl.model.vo;

/***
 * 该任务类是集合类 集合了TFreqInfo,ETLFreqInfo,ETLTimeType
 * 3个类的字段
 * @author admin
 *
 */
public class TETLFreqInfoDataVo {
	private String freqId;
	private String freqName;
	private String freqDesc;
	private String systemId="bank";
	private String bankFreqId="1";
	private Integer orderId;
	private String executeTime;
	private Integer usingFlag;
	private Integer timeTypeId;
	private String timeTypeName;
	
	/**频度任务id*/
	public String getFreqId() {
		return freqId;
	}
	public void setFreqId(String freqId) {
		this.freqId = freqId;
	}
	
	/**频度任务名称*/
	public String getFreqName() {
		return freqName;
	}
	public void setFreqName(String freqName) {
		this.freqName = freqName;
	}
	
	/**频度任务描述*/
	public String getFreqDesc() {
		return freqDesc;
	}
	public void setFreqDesc(String freqDesc) {
		this.freqDesc = freqDesc;
	}
	
	/**系统Id*/
	public String getSystemId() {
		return systemId;
	}
	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}
	
	/**行内id*/
	public String getBankFreqId() {
		return bankFreqId;
	}
	public void setBankFreqId(String bankFreqId) {
		this.bankFreqId = bankFreqId;
	}
	
	/**排序*/
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	
	/**执行时间*/
	public String getExecuteTime() {
		return executeTime;
	}
	public void setExecuteTime(String executeTime) {
		this.executeTime = executeTime;
	}
	
	/**是否启用*/
	public Integer getUsingFlag() {
		return usingFlag;
	}
	public void setUsingFlag(Integer usingFlag) {
		this.usingFlag = usingFlag;
	}
	
	/**时间类型*/
	public Integer getTimeTypeId() {
		return timeTypeId;
	}
	public void setTimeTypeId(Integer timeTypeId) {
		this.timeTypeId = timeTypeId;
	}
	
	/**时间类型描述*/
	public String getTimeTypeName() {
		return timeTypeName;
	}
	public void setTimeTypeName(String timeTypeName) {
		this.timeTypeName = timeTypeName;
	}
	
	
}
