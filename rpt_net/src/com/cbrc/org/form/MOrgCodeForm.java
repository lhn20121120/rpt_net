
package com.cbrc.org.form;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form for MOrgCode.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 *
 * @struts.form name="MOrgCodeForm"
 */
public class MOrgCodeForm extends ActionForm {

   private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

   private java.lang.String _orgCode = null;
   private java.lang.String _regionId = null;
   private java.lang.String _orgId = null;
   private java.lang.String _orgName = null;

   /**
    * Standard constructor.
    */
   public MOrgCodeForm() {
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
   /**
    * Returns the regionId
    *
    * @return the regionId
    */
   public java.lang.String getRegionId() {
      return _regionId;
   }

   /**
    * Sets the regionId
    *
    * @param regionId the new regionId value
    */
   public void setRegionId(java.lang.String regionId) {
      _regionId = regionId;
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
      if(getOrgCode() == null) {
         errors.add("orgCode", new ActionError("error.orgCode.required"));
      }
      if(getRegionId() == null) {
         errors.add("regionId", new ActionError("error.regionId.required"));
      }
      if(getOrgId() == null) {
         errors.add("orgId", new ActionError("error.orgId.required"));
      }
      // TODO test format/data
      return errors;
   }
}
