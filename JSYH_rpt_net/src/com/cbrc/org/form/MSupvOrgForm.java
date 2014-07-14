
package com.cbrc.org.form;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form for MSupvOrg.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 *
 * @struts.form name="MSupvOrgForm"
 */
public class MSupvOrgForm extends ActionForm {

   private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

   private java.lang.String _orgId = null;
   private java.lang.String _supvOrgName = null;
   private java.lang.String _priorOrgId = null;
   private java.lang.String _orgCode = null;

   /**
    * Standard constructor.
    */
   public MSupvOrgForm() {
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
    * Returns the supvOrgName
    *
    * @return the supvOrgName
    */
   public java.lang.String getSupvOrgName() {
      return _supvOrgName;
   }

   /**
    * Sets the supvOrgName
    *
    * @param supvOrgName the new supvOrgName value
    */
   public void setSupvOrgName(java.lang.String supvOrgName) {
      _supvOrgName = supvOrgName;
   }
   /**
    * Returns the priorOrgId
    *
    * @return the priorOrgId
    */
   public java.lang.String getPriorOrgId() {
      return _priorOrgId;
   }

   /**
    * Sets the priorOrgId
    *
    * @param priorOrgId the new priorOrgId value
    */
   public void setPriorOrgId(java.lang.String priorOrgId) {
      _priorOrgId = priorOrgId;
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
