
package com.gather.struts.forms;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form for mappingRelation.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 *
 * @struts.form name="mappingRelationForm"
 */
public class MappingRelationForm extends ActionForm {

   private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

   private java.lang.String _orgid = null;
   private java.lang.String _replaceOrgId =null;
   private java.util.Date _startDate = null;
   private java.util.Date _endDate = null;
   private Integer _state = null;

   /**
    * Standard constructor.
    */
   public MappingRelationForm() {
   }

   /**
    * Returns the orgid
    *
    * @return the orgid
    */
   public java.lang.String getOrgid() {
      return _orgid;
   }

   /**
    * Sets the orgid
    *
    * @param orgid the new orgid value
    */
   public void setOrgid(java.lang.String orgid) {
      _orgid = orgid;
   }
   /**
    * Returns the replaceOrgId
    *
    * @return the replaceOrgId
    */
   public String getReplaceOrgId() {
      return _replaceOrgId;
   }

   /**
    * Sets the replaceOrgId
    *
    * @param replaceOrgId the new replaceOrgId value
    */
   public void setReplaceOrgId(String replaceOrgId) {
      _replaceOrgId = replaceOrgId;
   }
   /**
    * Returns the startDate
    *
    * @return the startDate
    */
   public java.util.Date getStartDate() {
      return _startDate;
   }

   /**
    * Returns the startDate as a String
    *
    * @return the startDate as a String
    */
   public String getStartDateAsString() {
      if( _startDate != null ) {
         return FORMAT.format(_startDate);
      }
      else {
         return "";
      }
   }

   /**
    * Sets the startDate
    *
    * @param startDate the new startDate value
    */
   public void setStartDate(java.util.Date startDate) {
      _startDate = startDate;
   }

   /**
    * Sets the startDate as a String.
    *
    * @param startDate the new startDate value as a String
    */
   public void setStartDateAsString(String startDate) {
      try {
         _startDate = new java.sql.Timestamp(FORMAT.parse(startDate).getTime());
      } catch (ParseException pe) {
         _startDate = new java.sql.Timestamp((new java.util.Date()).getTime());
      }
   }

   /**
    * Returns the endDate
    *
    * @return the endDate
    */
   public java.util.Date getEndDate() {
      return _endDate;
   }

   /**
    * Returns the endDate as a String
    * 
    * @return the endDate as a String
    */
   public String getEndDateAsString() {
      if( _endDate != null ) {
         return FORMAT.format(_endDate);
      }
      else {
         return "";
      }
   }

   /**
    * Sets the endDate
    *
    * @param endDate the new endDate value
    */
   public void setEndDate(java.util.Date endDate) {
      _endDate = endDate;
   }

   /**
    * Sets the endDate as a String.
    *
    * @param endDate the new endDate value as a String
    */
   public void setEndDateAsString(String endDate) {
      try {
         _endDate = new java.sql.Timestamp(FORMAT.parse(endDate).getTime());
      } catch (ParseException pe) {
         _endDate = new java.sql.Timestamp((new java.util.Date()).getTime());
      }
   }

   /**
    * Returns the state
    *
    * @return the state
    */
   public Integer getState() {
      return _state;
   }

   /**
    * Sets the state
    *
    * @param state the new state value
    */
   public void setState(Integer state) {
      _state = state;
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
      if(getOrgid() == null) {
         errors.add("orgid", new ActionError("error.orgid.required"));
      }
      if(getReplaceOrgId() == null) {
         errors.add("replaceOrgId", new ActionError("error.replaceOrgId.required"));
      }
      // TODO test format/data
      return errors;
   }
}
