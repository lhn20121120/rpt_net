package com.cbrc.smis.entity;

import java.io.Serializable;

/**
 * 该类封装了用于查询子报表(模板)所用到的查询条件
 * 
 * @author chenbing
 *
 */
public class MChildReportQueryTerm implements Serializable{
	
	/**
	 * 子报表的名称
	 */
	private String reportName;

	/**
	 * 子报表的版本号
	 */
	private String reportVersion;

	/**
	 * 排序方式
	 */
	private String orderType;

	public MChildReportQueryTerm(String reportName, String reportVersion, String orderType) {
		super();
		// TODO 自动生成构造函数存根
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
