package com.cbrc.org.action;


import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * 代报机构
 * @author 姚捷
 * 
 * @param _orgId 代报机构Id
 * @param _actuOrgTypeId 真实机构类别Id
 * @param _orgName 机构名称
 * @param _startDate 起始时间
 * @param _endDate 终止时间
 * @param _licence 许可证号
 * @param _orgCode 机构分类Id
 * @param _orgType 机构类型
 * 
 * @struts.form name="MToRepOrgForm"
 */
public class MToRepOrgForm extends ActionForm {

   private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy 00:00:00");

   private java.lang.String _orgId = null;
   private java.lang.String _actuOrgTypeId = null;
   private java.lang.String _orgName = null;
   private java.lang.String _startDate = null;
   private java.lang.String _endDate = null;
   private java.lang.String _licence = null;
   private java.lang.String _orgCode = null;
   private String _orgType = null;
   

   /**
    * Standard constructor.
    */
   public MToRepOrgForm() {
   }

   /**
    * Returns the orgId
    *
    * @return the orgId
    */
   public java.lang.String getOrgId() {
      return _orgId;
   }

   /**
    * Sets the orgId
    *
    * @param orgId the new orgId value
    */
   public void setOrgId(java.lang.String orgId) {
      _orgId = orgId;
   }
   /**
    * Returns the actuOrgTypeId
    *
    * @return the actuOrgTypeId
    */
   public java.lang.String getActuOrgTypeId() {
      return _actuOrgTypeId;
   }

   /**
    * Sets the actuOrgTypeId
    *
    * @param actuOrgTypeId the new actuOrgTypeId value
    */
   public void setActuOrgTypeId(java.lang.String actuOrgTypeId) {
      _actuOrgTypeId = actuOrgTypeId;
   }
   /**
    * Returns the orgName
    *
    * @return the orgName
    */
   public java.lang.String getOrgName() {
      return _orgName;
   }

   /**
    * Sets the orgName
    *
    * @param orgName the new orgName value
    */
   public void setOrgName(java.lang.String orgName) {
      _orgName = orgName;
   }
   /**
    * Returns the startDate
    *
    * @return the startDate
    */
   public java.lang.String getStartDate() {
	   return this._startDate;
	   /*Date _formatStartDate = FitechUtil.formatToTimestamp(this._startDate);
	   return _formatStartDate == null ? null : FORMAT.format(_formatStartDate);*/
   }

   /**
    * Sets the startDate
    *
    * @param startDate the new startDate value
    */
   public void setStartDate(java.lang.String startDate) {
      _startDate = startDate;
   }
   /**
    * Returns the endDate
    *
    * @return the endDate
    */
   public java.lang.String getEndDate() {
	   return this._endDate;
	 /*  Date _formatStartDate = FitechUtil.formatToTimestamp(this._endDate);
	   return _formatStartDate == null ? null : FORMAT.format(_formatStartDate);*/
   }

   /**
    * Sets the endDate
    *
    * @param endDate the new endDate value
    */
   public void setEndDate(java.lang.String endDate) {
      _endDate = endDate;
   }
   /**
    * Returns the licence
    *
    * @return the licence
    */
   public java.lang.String getLicence() {
      return _licence;
   }

   /**
    * Sets the licence
    *
    * @param licence the new licence value
    */
   public void setLicence(java.lang.String licence) {
      _licence = licence;
   }
   /**
    * Returns the orgCode
    *
    * @return the orgCode
    */
   public java.lang.String getOrgCode() {
      return _orgCode;
   }

   /**
    * Sets the orgCode
    *
    * @param orgCode the new orgCode value
    */
   public void setOrgCode(java.lang.String orgCode) {
      _orgCode = orgCode;
   }
   
   	public String getOrgType() {
		return _orgType;
	}

	public void setOrgType(String type) {
		_orgType = type;
	}

   /**
    * Validate the properties that have been set from this HTTP request,
    * and return an <code>ActionErrors</code> object that encapsulates any
    * validation errors that have been found.  If no errors are found, return
    * <code>null</code> or an <code>ActionErrors</code> object with no
    * recorded error messages.
    *
    * @param mapping The mapping used to select this instance
    * @param request The servlet request we are processing
    */
   public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
      ActionErrors errors = new ActionErrors();
      return errors;
   }


}
