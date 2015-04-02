
package com.cbrc.smis.form;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form for policyExecDetail.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 *
 * @struts.form name="policyExecDetailForm"
 */
public class PolicyExecDetailForm extends ActionForm {

   private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

   private java.lang.Integer _repInId = null;
   private java.sql.Timestamp _execTime = null;
   private java.lang.Integer _policyId = null;

   /**
    * Standard constructor.
    */
   public PolicyExecDetailForm() {
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
    * @param repInId the new repInId value
    */
   public void setRepInId(java.lang.Integer repInId) {
      _repInId = repInId;
   }
   /**
    * Returns the execTime
    *
    * @return the execTime
    */
   public java.sql.Timestamp getExecTime() {
      return _execTime;
   }

   /**
    * Returns the execTime as a String
    *
    * @return the execTime as a String
    */
   public String getExecTimeAsString() {
      if( _execTime != null ) {
         return FORMAT.format(_execTime);
      }
      else {
         return "";
      }
   }

   /**
    * Sets the execTime
    *
    * @param execTime the new execTime value
    */
   public void setExecTime(java.sql.Timestamp execTime) {
      _execTime = execTime;
   }

   /**
    * Sets the execTime as a String.
    *
    * @param execTime the new execTime value as a String
    */
   public void setExecTimeAsString(String execTime) {
      try {
         _execTime = new java.sql.Timestamp(FORMAT.parse(execTime).getTime());
      } catch (ParseException pe) {
         _execTime = new java.sql.Timestamp((new java.util.Date()).getTime());
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
      if(getRepInId() == null) {
         errors.add("repInId", new ActionError("error.repInId.required"));
      }
      if(getPolicyId() == null) {
         errors.add("policyId", new ActionError("error.policyId.required"));
      }
      // TODO test format/data
      return errors;
   }
}
