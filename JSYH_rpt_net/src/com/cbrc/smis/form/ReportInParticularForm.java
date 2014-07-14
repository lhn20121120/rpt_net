package com.cbrc.smis.form;

import java.util.Date;

import org.apache.struts.action.ActionForm;

/**
 * 
 * @author 唐磊
 *
 */
public class ReportInParticularForm extends ActionForm {
	private java.lang.Integer _repInId = null;

	private java.lang.String _childRepId = null;

	private java.lang.Integer _term = null;

	private java.lang.String _repName = null;

	private java.lang.String _versionId = null;

	private Date _reportDate = null;

	//报送机构的名称
	private String _orgName = null;
	
	//来自于Date_Validate_Info中的Cell_Formu
	private String _cellFormu=null;
	
//	来自于Date_Validate_Info中的Result
	private String _result=null;

	//来自ValidateType中的validate_Type_Name
	private String _validateTypeName=null;
	
	//QDDatavalidateInfo中的cause
	private String _cause=null;
	
	
	//ReportIn中的laterReportDay
	private String _laterReportDay=null;
	
	
	
	
	public String getLaterReportDay() {
		return _laterReportDay;
	}

	public void setLaterReportDay(String reportDay) {
		_laterReportDay = reportDay;
	}

	public String getCause() {
		return _cause;
	}

	public void setCause(String cause) {
		_cause = cause;
	}

	public String getValidateTypeName() {
		return _validateTypeName;
	}

	public void setValidateTypeName(String validateTypeName) {
		_validateTypeName = validateTypeName;
	}

	public String getResult() {
		return _result;
	}

	public void setResult(String _result) {
		this._result = _result;
	}

	public String getCellFormu() {
		return _cellFormu;
	}

	public void setCellFormu(String cellFormu) {
		_cellFormu = cellFormu;
	}

	public java.lang.String getChildRepId() {
		return _childRepId;
	}

	public void setChildRepId(java.lang.String repId) {
		_childRepId = repId;
	}

	public String getOrgName() {
		return _orgName;
	}

	public void setOrgName(String name) {
		_orgName = name;
	}

	public java.lang.Integer getRepInId() {
		return _repInId;
	}

	public void setRepInId(java.lang.Integer inId) {
		_repInId = inId;
	}

	public java.lang.String getRepName() {
		return _repName;
	}

	public void setRepName(java.lang.String name) {
		_repName = name;
	}

	public Date getReportDate() {
		return _reportDate;
	}

	public void setReportDate(Date date) {
		_reportDate = date;
	}

	public java.lang.Integer getTerm() {
		return _term;
	}

	public void setTerm(java.lang.Integer _term) {
		this._term = _term;
	}

	public java.lang.String getVersionId() {
		return _versionId;
	}

	public void setVersionId(java.lang.String id) {
		_versionId = id;
	}
	  

}
