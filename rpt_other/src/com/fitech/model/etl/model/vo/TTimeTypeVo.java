package com.fitech.model.etl.model.vo;

/***
 * 定时模式表
 * @author admin
 *
 */
public class TTimeTypeVo {
	// Fields

	private Integer timeTypeId;
	private String timeTypeName;

	// Constructors

	/** default constructor */
	public TTimeTypeVo() {
	}

	/** full constructor */
	public TTimeTypeVo(String timeTypeName) {
		this.timeTypeName = timeTypeName;
	}

	// Property accessors
	
	/**模式主键Id*/
	public Integer getTimeTypeId() {
		return this.timeTypeId;
	}

	public void setTimeTypeId(Integer timeTypeId) {
		this.timeTypeId = timeTypeId;
	}
	
	/**模式内容
	 *  1：时间点-- 例如 23:32
	 *	2：期后天数-- 例如期后8天
	 *	3：日期-- 01-12*/
	public String getTimeTypeName() {
		return this.timeTypeName;
	}

	public void setTimeTypeName(String timeTypeName) {
		this.timeTypeName = timeTypeName;
	}
}
