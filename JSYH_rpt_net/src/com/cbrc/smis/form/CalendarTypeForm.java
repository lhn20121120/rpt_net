
package com.cbrc.smis.form;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form for calendarType.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 *
 * @struts.form name="calendarTypeForm"
 */
public class CalendarTypeForm extends ActionForm {

   private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

   private java.lang.Integer _calTypeId = null;
   private java.lang.String _calTypeName = null;

   /**
    * Standard constructor.
    */
   public CalendarTypeForm() {
   }

   /**
    * Returns the calTypeId
    *
    * @return the calTypeId
    */
   public java.lang.Integer getCalTypeId() {
      return _calTypeId;
   }

   /**
    * Sets the calTypeId
    *
    * @param calTypeId the new calTypeId value
    */
   public void setCalTypeId(java.lang.Integer calTypeId) {
      _calTypeId = calTypeId;
   }
   /**
    * Returns the calTypeName
    *
    * @return the calTypeName
    */
   public java.lang.String getCalTypeName() {
      return _calTypeName;
   }

   /**
    * Sets the calTypeName
    *
    * @param calTypeName the new calTypeName value
    */
   public void setCalTypeName(java.lang.String calTypeName) {
      _calTypeName = calTypeName;
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
      return errors;
   }
}
