package com.fitech.gznx.entity;
/**
 * ����ʵ���� �������
 * @author jack
 * @date 2009-12-11
 */
public class Ccy {
	
	private String  ccyName;
	
	private String  ccyId;

	public String getCcyId() {
		return ccyId;
	}

	public void setCcyId(String ccyId) {
		this.ccyId = ccyId;
	}

	public String getCcyName() {
		return ccyName;
	}

	public void setCcyName(String ccyName) {
		this.ccyName = ccyName;
	}

	public Ccy(String ccyName, String ccyId) {
		super();
		// TODO Auto-generated constructor stub
		this.ccyName = ccyName;
		this.ccyId = ccyId;
	}
	
	
}

