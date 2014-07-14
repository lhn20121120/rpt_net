package com.fitech.gznx.form;

import org.apache.struts.action.ActionForm;

public class QuickReportForm extends ActionForm {
	/**
	 * 报表类型
	 */
	private String reportType;
	
	/**
	 * 报表名称
	 */
	private String repName;
	
	/**
	 * 报送频度
	 */
	private String bsId;

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getRepName() {
		return repName;
	}

	public void setRepName(String repName) {
		this.repName = repName;
	}

	public String getBsId() {
		return bsId;
	}

	public void setBsId(String bsId) {
		this.bsId = bsId;
	}



	
}
