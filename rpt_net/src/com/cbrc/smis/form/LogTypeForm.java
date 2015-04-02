
package com.cbrc.smis.form;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form for logType.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 *
 * @struts.form name="logTypeForm"
 */
public class LogTypeForm extends ActionForm {

   private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

   private java.lang.Integer _logTypeId = null;
   private java.lang.String _logType = null;

   /**
    * Standard constructor.
    */
   public LogTypeForm() {
   }

   /**
    * Returns the logTypeId
    *
    * @return the logTypeId
    */
   public java.lang.Integer getLogTypeId() {
      return _logTypeId;
   }

   /**
    * Sets the logTypeId
    *
    * @param logTypeId the new logTypeId value
    */
   public void setLogTypeId(java.lang.Integer logTypeId) {
      _logTypeId = logTypeId;
   }
   /**
    * Returns the logType
    *
    * @return the logType
    */
   public java.lang.String getLogType() {
      return _logType;
   }

   /**
    * Sets the logType
    *
    * @param logType the new logType value
    */
   public void setLogType(java.lang.String logType) {
      _logType = logType;
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
      if(getLogTypeId() == null) {
         errors.add("logTypeId", new ActionError("error.logTypeId.required"));
      }
      // TODO test format/data
      return errors;
   }
}
