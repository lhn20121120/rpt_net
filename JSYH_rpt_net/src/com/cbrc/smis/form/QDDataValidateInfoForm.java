package com.cbrc.smis.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionMapping;

public class QDDataValidateInfoForm extends Action {
	/**
	 * 实际数据报表ID
	 */
	private Integer repInId;
	/**
	 * 校验公式类别ID
	 */
	private Integer validateTypeID;
	/**
	 * 关系表达式ID
	 */
	private Integer cellFormuId;
	/**
	 * 列名
	 */
	private String colName;
	/**
	 * 行号
	 */
	private Integer rowNo;
	/**
	 * 校验结果
	 */
	private Integer result;
	/**
	 * 子报表名称
	 */
	private String reportName=null;
	/**
	 * 版本号
	 */
	private String versionId=null;
	/**
	 * 校验关系表达式
	 */
	private String cellFormu=null;
	/**
	 * 校验类别名称
	 */
	private String validateTypeName=null;
	/**
	 * 校验未通过的原因
	 */
	private String cause=null;
	
	private String cellFormuView=null;
	
	public QDDataValidateInfoForm(){}	
	
	public void reset(ActionMapping mapping,HttpServletRequest request){
		this.repInId=null;
		this.validateTypeID=null;
		this.cellFormuId=null;
		this.colName=null;
		this.rowNo=null;
		this.result=null;
		this.reportName=null;
		this.versionId=null;
		this.cellFormu=null;
		this.cause=null;
		this.cellFormuView=null;
	}

	public String getCellFormu() {
		return cellFormu;
	}

	public void setCellFormu(String cellFormu) {
		this.cellFormu = cellFormu;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public String getVersionId() {
		return versionId;
	}

	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}

	public Integer getCellFormuId() {
		return cellFormuId;
	}



	public void setCellFormuId(Integer cellFormuId) {
		this.cellFormuId = cellFormuId;
	}



	public String getColName() {
		return colName;
	}



	public void setColName(String colName) {
		this.colName = colName;
	}



	public Integer getRepInId() {
		return repInId;
	}



	public void setRepInId(Integer repInId) {
		this.repInId = repInId;
	}



	public Integer getResult() {
		return result;
	}



	public void setResult(Integer result) {
		this.result = result;
	}



	public Integer getRowNo() {
		return rowNo;
	}

	public void setRowNo(Integer rowNo) {
		this.rowNo = rowNo;
	}

	public Integer getValidateTypeID() {
		return validateTypeID;
	}

	public void setValidateTypeID(Integer validateTypeID) {
		this.validateTypeID = validateTypeID;
	}

	public String getValidateTypeName() {
		return validateTypeName;
	}

	public void setValidateTypeName(String validateTypeName) {
		this.validateTypeName = validateTypeName;
	}

	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}

	public String getCellFormuView() {
		return cellFormuView;
	}

	public void setCellFormuView(String cellFormuView) {
		this.cellFormuView = cellFormuView;
	}
}
