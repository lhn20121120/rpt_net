
package com.cbrc.smis.form;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form for policy.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 *
 * @struts.form name="policyForm"
 */
public class PolicyForm extends ActionForm {

   private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

   private java.lang.Integer _calId = null;
   private java.sql.Timestamp _defineTime = null;
   private java.lang.Integer _policyId = null;
   private java.lang.String _policyName = null;
   private java.lang.Integer _policyTypeId = null;

   /**
    * Standard constructor.
    */
   public PolicyForm() {
   }

   /**
    * Returns the calId
    *
    * @return the calId
    */
   public java.lang.Integer getCalId() {
      return _calId;
   }

   /**
    * Sets the calId
    *
    * @param calId the new calId value
    */
   public void setCalId(java.lang.Integer calId) {
      _calId = calId;
   }
   /**
    * Returns the defineTime
    *
    * @return the defineTime
    */
   public java.sql.Timestamp getDefineTime() {
      return _defineTime;
   }

   /**
    * Returns the defineTime as a String
    *
    * @return the defineTime as a String
    */
   public String getDefineTimeAsString() {
      if( _defineTime != null ) {
         return FORMAT.format(_defineTime);
      }
      else {
         return "";
      }
   }

   /**
    * Sets the defineTime
    *
    * @param defineTime the new defineTime value
    */
   public void setDefineTime(java.sql.Timestamp defineTime) {
      _defineTime = defineTime;
   }

   /**
    * Sets the defineTime as a String.
    *
    * @param defineTime the new defineTime value as a String
    */
   public void setDefineTimeAsString(String defineTime) {
      try {
         _defineTime = new java.sql.Timestamp(FORMAT.parse(defineTime).getTime());
      } catch (ParseException pe) {
         _defineTime = new java.sql.Timestamp((new java.util.Date()).getTime());
      }
   }

   /**
    * Returns the policyId
    *
    * @return the policyId
    */
   public java.lang.Integer getPolicyId() {
      return _policyId;
   }

   /**
    * Sets the policyId
    *
    * @param policyId the new policyId value
    */
   public void setPolicyId(java.lang.Integer policyId) {
      _policyId = policyId;
   }
   /**
    * Returns the policyName
    *
    * @return the policyName
    */
   public java.lang.String getPolicyName() {
      return _policyName;
   }

   /**
    * Sets the policyName
    *
    * @param policyName the new policyName value
    */
   public void setPolicyName(java.lang.String policyName) {
      _policyName = policyName;
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
      if(getCalId() == null) {
         errors.add("calId", new ActionError("error.calId.required"));
      }
      if(getPolicyId() == null) {
         errors.add("policyId", new ActionError("error.policyId.required"));
      }
      // TODO test format/data
      return errors;
   }
}
