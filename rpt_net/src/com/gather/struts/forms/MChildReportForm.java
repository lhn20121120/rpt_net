
package com.gather.struts.forms;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form for MChildReport.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 *
 * @struts.form name="MChildReportForm"
 */
public class MChildReportForm extends ActionForm {
  private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

   private java.lang.String _childRepId =null;
   private java.lang.String _versionId =null;
   private java.lang.String _reportName =null;
   private java.util.Date _startDate =null;
   private java.util.Date _endDate =null;
   private java.lang.String _formatTempId = null;
   private Integer _repId = null;
   private Integer _curUnit =null;

   /**
    * Standard constructor.
    */
   public MChildReportForm() {
   }

   /**
    * Returns the childRepId
    *
    * @return the childRepId
    */
   public java.lang.String getChildRepId() {
      return _childRepId;
   }

   /**
    * Sets the childRepId
    *
    * @param childRepId the new childRepId value
    */
   public void setChildRepId(java.lang.String childRepId) {
      _childRepId = childRepId;
   }
   /**
    * Returns the versionId
    *
    * @return the versionId
    */
   public java.lang.String getVersionId() {
      return _versionId;
   }

   /**
    * Sets the versionId
    *
    * @param versionId the new versionId value
    */
   public void setVersionId(java.lang.String versionId) {
      _versionId = versionId;
   }
   /**7
    * Returns the reportName
    *
    * @return the reportName
    */
   public java.lang.String getReportName() {
      return _reportName;
   }

   /**
    * Sets the reportName
    *
    * @param reportName the new reportName value
    */
   public void setReportName(java.lang.String reportName) {
      _reportName = reportName;
   }
   /**
    * Returns the startDate
    *
    * @return the startDate
    */
   public java.util.Date getStartDate() {
      return _startDate;
   }

   /**
    * Sets the startDate
    *
    * @param startDate the new startDate value
    */
   public void setStartDate(java.util.Date startDate) {
      _startDate = startDate;
   }
   /**
    * Returns the endDate
    *
    * @return the endDate
    */
   public java.util.Date getEndDate() {
      return _endDate;
   }

   /**
    * Sets the endDate
    *
    * @param endDate the new endDate value
    */
   public void setEndDate(java.util.Date endDate) {
      _endDate = endDate;
   }
   /**
    * Returns the formatTempId
    *
    * @return the formatTempId
    */
   public java.lang.String getFormatTempId() {
      return _formatTempId;
   }

   /**
    * Sets the formatTempId
    *
    * @param formatTempId the new formatTempId value
    */
   public void setFormatTempId(java.lang.String formatTempId) {
      _formatTempId = formatTempId;
   }
   /**
    * Returns the repId
    *
    * @return the repId
    */
   public Integer getRepId() {
      return _repId;
   }

   /**
    * Sets the repId
    *
    * @param repId the new repId value
    */
   public void setRepId(Integer repId) {
      _repId = repId;
   }
   /**
    * Returns the curUnit
    *
    * @return the curUnit
    */
   public Integer getCurUnit() {
      return _curUnit;
   }

   /**
    * Sets the curUnit
    *
    * @param curUnit the new curUnit value
    */
   public void setCurUnit(Integer curUnit) {
      _curUnit = curUnit;
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
      // test for nullity
      if(getChildRepId() == null) {
         errors.add("childRepId", new ActionError("error.childRepId.required"));
      }
      if(getVersionId() == null) {
         errors.add("versionId", new ActionError("error.versionId.required"));
      }
      // TODO test format/data
      return errors;
   }
}
