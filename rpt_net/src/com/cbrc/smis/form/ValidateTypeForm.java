
package com.cbrc.smis.form;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form for validateType.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 *
 * @struts.form name="validateTypeForm"
 */
public class ValidateTypeForm extends ActionForm {

   private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

   private java.lang.Integer _validateTypeId = null;
   private java.lang.String _validateTypeName = null;

   /**
    * Standard constructor.
    */
   public ValidateTypeForm() {
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
    * @param validateTypeId the new validateTypeId value
    */
   public void setValidateTypeId(java.lang.Integer validateTypeId) {
      _validateTypeId = validateTypeId;
   }
   /**
    * Returns the validateTypeName
    *
    * @return the validateTypeName
    */
   public java.lang.String getValidateTypeName() {
      return _validateTypeName;
   }

   /**
    * Sets the validateTypeName
    *
    * @param validateTypeName the new validateTypeName value
    */
   public void setValidateTypeName(java.lang.String validateTypeName) {
      _validateTypeName = validateTypeName;
      //---------------------------------
      // gongming 2008-07-25
      if(null != this._validateTypeName)
          this._validateTypeName = this._validateTypeName.trim();
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
      if(getValidateTypeId() == null) {
         errors.add("validateTypeId", new ActionError("error.validateTypeId.required"));
      }
      // TODO test format/data
      return errors;
   }
}
