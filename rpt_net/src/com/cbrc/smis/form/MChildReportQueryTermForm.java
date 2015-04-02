package com.cbrc.smis.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.cbrc.smis.common.ConductString;

/**
 * 该ActionForm封装从客户端输入的查询条件
 * 
 * @author chenbing
 * 
 */
public final class MChildReportQueryTermForm extends ActionForm {

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
	}

	public String getReportVersion() {

		return reportVersion;
	}

	public void setReportVersion(String reportVersion) {

		this.reportVersion = reportVersion;
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {

	}

	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {

		this.reportName = ConductString.getStringNotSpace(this.reportName);

		this.reportVersion = ConductString
				.getStringNotSpace(this.reportVersion);

		return null;
	}

}
