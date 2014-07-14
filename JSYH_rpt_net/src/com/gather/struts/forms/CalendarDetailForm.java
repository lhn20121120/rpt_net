
package com.gather.struts.forms;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form for calendarDetail.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 *
 * @struts.form name="calendarDetailForm"
 */
public class CalendarDetailForm extends ActionForm {

   private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

   private java.lang.String _calYear =null;
   private java.lang.String _calMonth = null;
   private java.lang.String _calDay = null;
   private Integer _calId =null;
   private Integer _calTypeId =null;
   private java.util.Date calDate;

   /**
    * Standard constructor.
    */
   public CalendarDetailForm() {
   }

   /**
    * Returns the calYear
    *
    * @return the calYear
    */
   public java.lang.String getCalYear() {
      return _calYear;
   }

   /**
    * Sets the calYear
    *
    * @param calYear the new calYear value
    */
   public void setCalYear(java.lang.String calYear) {
      _calYear = calYear;
   }
   /**
    * Returns the calMonth
    *
    * @return the calMonth
    */
   public java.lang.String getCalMonth() {
      return _calMonth;
   }

   /**
    * Sets the calMonth
    *
    * @param calMonth the new calMonth value
    */
   public void setCalMonth(java.lang.String calMonth) {
      _calMonth = calMonth;
   }
   /**
    * Returns the calDay
    *
    * @return the calDay
    */
   public java.lang.String getCalDay() {
      return _calDay;
   }

   /**
    * Sets the calDay
    *
    * @param calDay the new calDay value
    */
   public void setCalDay(java.lang.String calDay) {
      _calDay = calDay;
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
    * Returns the calTypeId
    *
    * @return the calTypeId
    */
   public Integer getCalTypeId() {
      return _calTypeId;
   }

   /**
    * Sets the calTypeId
    *
    * @param calTypeId the new calTypeId value
    */
   public void setCalTypeId(Integer calTypeId) {
      _calTypeId = calTypeId;
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
      if(getCalYear() == null) {
         errors.add("calYear", new ActionError("error.calYear.required"));
      }
      if(getCalMonth() == null) {
         errors.add("calMonth", new ActionError("error.calMonth.required"));
      }
      if(getCalDay() == null) {
         errors.add("calDay", new ActionError("error.calDay.required"));
      }
      if(getCalId() == null) {
         errors.add("calId", new ActionError("error.calId.required"));
      }
      if(getCalTypeId() == null) {
         errors.add("calTypeId", new ActionError("error.calTypeId.required"));
      }
      // TODO test format/data
      return errors;
   }

/**
 * @return Returns the calDate.
 */
public java.util.Date getCalDate() {
	return calDate;
}

/**
 * @param calDate The calDate to set.
 */
public void setCalDate(java.util.Date calDate) {
	this.calDate = calDate;
}
}
