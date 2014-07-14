
package com.cbrc.org.form;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form for MActuOrg.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 *
 * @struts.form name="MActuOrgForm"
 */
public class MActuOrgForm extends ActionForm {

   private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

   private java.lang.String _actuOrgTypeId = null;
   private java.lang.String _actuOrgTypeName = null;

   /**
    * Standard constructor.
    */
   public MActuOrgForm() {
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
    * Returns the actuOrgTypeName
    *
    * @return the actuOrgTypeName
    */
   public java.lang.String getActuOrgTypeName() {
      return _actuOrgTypeName;
   }

   /**
    * Sets the actuOrgTypeName
    *
    * @param actuOrgTypeName the new actuOrgTypeName value
    */
   public void setActuOrgTypeName(java.lang.String actuOrgTypeName) {
      _actuOrgTypeName = actuOrgTypeName;
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
      if(getActuOrgTypeId() == null) {
         errors.add("actuOrgTypeId", new ActionError("error.actuOrgTypeId.required"));
      }
      // TODO test format/data
      return errors;
   }
}
