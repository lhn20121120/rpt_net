package com.fitech.net.hibernate;

import java.io.Serializable;

/**
 *������
 *���ڣ�2007-12-12
 *���ߣ��ܷ���
 */
public class ACompareLog implements Serializable{
	private ACompareLogKey key;
	private Integer acRepInId;
	private Integer acState;
	private String acLog;
	
	/**
	 * ���� acLog
	 */
	public String getAcLog() {
		return acLog;
	}
	/**
	 * ������acLog 
	 * ���� acLog
	 */
	public void setAcLog(String acLog) {
		this.acLog = acLog;
	}
	/**
	 * ���� acRepInId
	 */
	public Integer getAcRepInId() {
		return acRepInId;
	}
	/**
	 * ������acRepInId 
	 * ���� acRepInId
	 */
	public void setAcRepInId(Integer acRepInId) {
		this.acRepInId = acRepInId;
	}
	/**
	 * ���� acState
	 */
	public Integer getAcState() {
		return acState;
	}
	/**
	 * ������acState 
	 * ���� acState
	 */
	public void setAcState(Integer acState) {
		this.acState = acState;
	}
	/**
	 * ���� key
	 */
	public ACompareLogKey getKey() {
		return key;
	}
	/**
	 * ������key 
	 * ���� key
	 */
	public void setKey(ACompareLogKey key) {
		this.key = key;
	}

}

