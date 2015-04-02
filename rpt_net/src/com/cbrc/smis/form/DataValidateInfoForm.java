package com.cbrc.smis.form;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.jeval.Evaluator;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form for dataValidateInfo.
 * 
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 * 
 * @struts.form name="dataValidateInfoForm"
 */
public class DataValidateInfoForm extends ActionForm {

	private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

	private java.lang.Integer _repInId = null;

	private java.lang.Integer _validateTypeId = null;

	private java.lang.Integer _cellFormuId = null;

	private java.lang.Integer _result = null;
	
	private Integer seqNo=null;
	// 报表口径
   
	private java.lang.String dataRangeName=null;  
	
	
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
	 * 校验关系表达式描述
	 */
	private String cellFormuView=null;
	/**
	 * 校验类别名称
	 */
	private String validateTypeName=null;
	
	private String sourceValue=null;
	private String targetValue=null;
	
	private String difference = null;
	
	private String cause;
	
	
	Evaluator evaluator = new Evaluator();

	/**
	 * Standard constructor.
	 */
	public DataValidateInfoForm() {
	}

	/**
	 * Returns the repInId
	 * 
	 * @return the repInId
	 */
	public java.lang.Integer getRepInId() {
		return _repInId;
	}

	/**
	 * Sets the repInId
	 * 
	 * @param repInId
	 *            the new repInId value
	 */
	public void setRepInId(java.lang.Integer repInId) {
		_repInId = repInId;
	}

	/**
	 * Returns the validateTypeId
	 * 
	 * @return the validateTypeId
	 */
	public java.lang.Integer getValidateTypeId() {
		return _validateTypeId;
	}

	/**
	 * Sets the validateTypeId
	 * 
	 * @param validateTypeId
	 *            the new validateTypeId value
	 */
	public void setValidateTypeId(java.lang.Integer validateTypeId) {
		_validateTypeId = validateTypeId;
	}

	/**
	 * Returns the cellFormuId
	 * 
	 * @return the cellFormuId
	 */
	public java.lang.Integer getCellFormuId() {
		return _cellFormuId;
	}

	/**
	 * Sets the cellFormuId
	 * 
	 * @param cellFormuId
	 *            the new cellFormuId value
	 */
	public void setCellFormuId(java.lang.Integer cellFormuId) {
		_cellFormuId = cellFormuId;
	}

	/**
	 * Returns the result
	 * 
	 * @return the result
	 */
	public java.lang.Integer getResult() {
		return _result;
	}

	/**
	 * Sets the result
	 * 
	 * @param result
	 *            the new result value
	 */
	public void setResult(java.lang.Integer result) {
		_result = result;
	}

	/**
	 * Validate the properties that have been set from this HTTP request, and
	 * return an <code>ActionErrors</code> object that encapsulates any
	 * validation errors that have been found. If no errors are found, return
	 * <code>null</code> or an <code>ActionErrors</code> object with no
	 * recorded error messages.
	 * 
	 * @param mapping
	 *            The mapping used to select this instance
	 * @param request
	 *            The servlet request we are processing
	 */
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();
		// test for nullity

		// TODO test format/data
		return errors;
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

	public String getValidateTypeName() {
		return validateTypeName;
	}

	public void setValidateTypeName(String validateTypeName) {
		this.validateTypeName = validateTypeName;
	}

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	/**
	 * @return Returns the cellFormView.
	 */
	public String getCellFormuView() {
		return cellFormuView;
	}

	/**
	 * @param cellFormView The cellFormView to set.
	 */
	public void setCellFormuView(String cellFormuView) {
		this.cellFormuView = cellFormuView;
	}

	public java.lang.String getDataRangeName() {
		return dataRangeName;
	}

	public void setDataRangeName(java.lang.String dataRangeName) {
		this.dataRangeName = dataRangeName;
	}

	public String getSourceValue() {
		return sourceValue;
	}

	public void setSourceValue(String sourceValue) {
		this.sourceValue = sourceValue;
	}

	public String getTargetValue() {
		return targetValue;
	}

	public void setTargetValue(String targetValue) {
		this.targetValue = targetValue;
	}

	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}

	public String getDifference() {
		
		if(sourceValue!=null&&targetValue!=null)
			try{
				return evaluator.evaluate("("+sourceValue+")-("+targetValue+")");
			}catch(Exception e){
				return difference;
			}
		else
			return difference;
	}

	public void setDifference(String difference) {
		this.difference = difference;
	}

	
}
