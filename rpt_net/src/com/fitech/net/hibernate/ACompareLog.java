package com.fitech.net.hibernate;

import java.io.Serializable;

/**
 *描述：
 *日期：2007-12-12
 *作者：曹发根
 */
public class ACompareLog implements Serializable{
	private ACompareLogKey key;
	private Integer acRepInId;
	private Integer acState;
	private String acLog;
	
	/**
	 * 返回 acLog
	 */
	public String getAcLog() {
		return acLog;
	}
	/**
	 * 参数：acLog 
	 * 设置 acLog
	 */
	public void setAcLog(String acLog) {
		this.acLog = acLog;
	}
	/**
	 * 返回 acRepInId
	 */
	public Integer getAcRepInId() {
		return acRepInId;
	}
	/**
	 * 参数：acRepInId 
	 * 设置 acRepInId
	 */
	public void setAcRepInId(Integer acRepInId) {
		this.acRepInId = acRepInId;
	}
	/**
	 * 返回 acState
	 */
	public Integer getAcState() {
		return acState;
	}
	/**
	 * 参数：acState 
	 * 设置 acState
	 */
	public void setAcState(Integer acState) {
		this.acState = acState;
	}
	/**
	 * 返回 key
	 */
	public ACompareLogKey getKey() {
		return key;
	}
	/**
	 * 参数：key 
	 * 设置 key
	 */
	public void setKey(ACompareLogKey key) {
		this.key = key;
	}

}

