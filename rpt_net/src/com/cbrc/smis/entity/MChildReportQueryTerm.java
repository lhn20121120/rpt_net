package com.cbrc.smis.entity;

import java.io.Serializable;

/**
 * �����װ�����ڲ�ѯ�ӱ���(ģ��)���õ��Ĳ�ѯ����
 * 
 * @author chenbing
 *
 */
public class MChildReportQueryTerm implements Serializable{
	
	/**
	 * �ӱ��������
	 */
	private String reportName;

	/**
	 * �ӱ���İ汾��
	 */
	private String reportVersion;

	/**
	 * ����ʽ
	 */
	private String orderType;

	public MChildReportQueryTerm(String reportName, String reportVersion, String orderType) {
		super();
		// TODO �Զ����ɹ��캯�����
		this.reportName = reportName;
		this.reportVersion = reportVersion;
		this.orderType = orderType;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
          //------------------------
        // gongming 2008-07-25
        if( null != this.reportName)
            this.reportName = this.reportName.trim();
	}

	public String getReportVersion() {
		return reportVersion;
	}

	public void setReportVersion(String reportVersion) {
		this.reportVersion = reportVersion;
		//------------------------
        // gongming 2008-07-25
        if(null != this.reportVersion)
            this.reportVersion = this.reportVersion.trim();
	}


}
