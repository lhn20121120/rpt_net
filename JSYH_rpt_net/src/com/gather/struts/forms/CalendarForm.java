
package com.gather.struts.forms;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form for calendar.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 *
 * @struts.form name="calendarForm"
 */
public class CalendarForm extends ActionForm {

   private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

   private Integer _calId = null;
   private java.lang.String _calName = null;
   private java.lang.String _calMethod = null;

   /**
    * Standard constructor.
    */
   public CalendarForm() {
   }

   /**
    * Returns the calId
    *
    * @return the calId
    */
   public Integer getCalId() {
      return _calId;
   }

   /**
    * Sets the calId
    *
    * @param calId the new calId value
    */
   public void setCalId(Integer calId) {
      _calId = calId;
   }
   /**
    * Returns the calName
    *
    * @return the calName
    */
   public java.lang.String getCalName() {
      return _calName;
   }

   /**
    * Sets the calName
    *
    * @param calName the new calName value
    */
   public void setCalName(java.lang.String calName) {
      _calName = calName;
   }
   /**
    * Returns the calMethod
    *
    * @return the calMethod
    */
   public java.lang.String getCalMethod() {
      return _calMethod;
   }

   /**
    * Sets the calMethod
    *
    * @param calMethod the new calMethod value
    */
   public void setCalMethod(java.lang.String calMethod) {
      _calMethod = calMethod;
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
      // TODO test format/data
      return errors;
   }
}
