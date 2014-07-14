
package com.cbrc.smis.form;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form for policyType.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 *
 * @struts.form name="policyTypeForm"
 */
public class PolicyTypeForm extends ActionForm {

   private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

   private java.lang.Integer _policyTypeId = null;
   private java.lang.String _policyTypeName = null;
   private java.lang.String _memo = null;

   /**
    * Standard constructor.
    */
   public PolicyTypeForm() {
   }

   /**
    * Returns the policyTypeId
    *
    * @return the policyTypeId
    */
   public java.lang.Integer getPolicyTypeId() {
      return _policyTypeId;
   }

   /**
    * Sets the policyTypeId
    *
    * @param policyTypeId the new policyTypeId value
    */
   public void setPolicyTypeId(java.lang.Integer policyTypeId) {
      _policyTypeId = policyTypeId;
   }
   /**
    * Returns the policyTypeName
    *
    * @return the policyTypeName
    */
   public java.lang.String getPolicyTypeName() {
      return _policyTypeName;
   }

   /**
    * Sets the policyTypeName
    *
    * @param policyTypeName the new policyTypeName value
    */
   public void setPolicyTypeName(java.lang.String policyTypeName) {
      _policyTypeName = policyTypeName;
   }
   /**
    * Returns the memo
    *
    * @return the memo
    */
   public java.lang.String getMemo() {
      return _memo;
   }

   /**
    * Sets the memo
    *
    * @param memo the new memo value
    */
   public void setMemo(java.lang.String memo) {
      _memo = memo;
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
      if(getPolicyTypeId() == null) {
         errors.add("policyTypeId", new ActionError("error.policyTypeId.required"));
      }
      // TODO test format/data
      return errors;
   }
}
