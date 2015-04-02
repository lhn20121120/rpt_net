
package com.cbrc.smis.form;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form for sysSet.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 *
 * @struts.form name="sysSetForm"
 */
public class SysSetForm extends ActionForm {

   private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

   private java.lang.Integer _sysSetId = null;
   private java.lang.String _sysSetKey = null;
   private java.lang.String _sysSetValue = null;
   private java.lang.String _memo = null;

   /**
    * Standard constructor.
    */
   public SysSetForm() {
   }

   /**
    * Returns the sysSetId
    *
    * @return the sysSetId
    */
   public java.lang.Integer getSysSetId() {
      return _sysSetId;
   }

   /**
    * Sets the sysSetId
    *
    * @param sysSetId the new sysSetId value
    */
   public void setSysSetId(java.lang.Integer sysSetId) {
      _sysSetId = sysSetId;
   }
   /**
    * Returns the sysSetKey
    *
    * @return the sysSetKey
    */
   public java.lang.String getSysSetKey() {
      return _sysSetKey;
   }

   /**
    * Sets the sysSetKey
    *
    * @param sysSetKey the new sysSetKey value
    */
   public void setSysSetKey(java.lang.String sysSetKey) {
      _sysSetKey = sysSetKey;
   }
   /**
    * Returns the sysSetValue
    *
    * @return the sysSetValue
    */
   public java.lang.String getSysSetValue() {
      return _sysSetValue;
   }

   /**
    * Sets the sysSetValue
    *
    * @param sysSetValue the new sysSetValue value
    */
   public void setSysSetValue(java.lang.String sysSetValue) {
      _sysSetValue = sysSetValue;
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
      if(getSysSetId() == null) {
         errors.add("sysSetId", new ActionError("error.sysSetId.required"));
      }
      // TODO test format/data
      return errors;
   }
}
