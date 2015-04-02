
package com.cbrc.smis.form;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form for MTorepOrg.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 *
 * @struts.form name="MTorepOrgForm"
 */
public class MTorepOrgForm extends ActionForm {

   private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

   private java.lang.String _orgName = null;
   private java.lang.String _startDate = null;
   private java.lang.String _endDate = null;
   private java.lang.String _licence = null;
   private java.lang.String _orgId = null;
   private java.lang.String _actuOrgTypeId = null;

   /**
    * Standard constructor.
    */
   public MTorepOrgForm() {
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
      return _startDate;
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
      return _endDate;
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
      if(getOrgId() == null) {
         errors.add("orgId", new ActionError("error.orgId.required"));
      }
      // TODO test format/data
      return errors;
   }
}
