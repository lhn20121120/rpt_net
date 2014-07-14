
package com.cbrc.smis.form;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

/**
 * Form for mappingRelation.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 *
 * @struts.form name="mappingRelationForm"
 */
public class MappingRelationForm extends ActionForm {

   /**
	 * 
	 */
	private static final long serialVersionUID = 1467159805082759053L;

private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

   private java.lang.Integer _orgId = null;
   private java.lang.Integer _replaceOrgId = null;
   private java.sql.Date _startDate = null;
   private java.sql.Date _endDate = null;

   /**
    * Standard constructor.
    */
   public MappingRelationForm() {
   }

   /**
    * Returns the orgId
    *
    * @return the orgId
    */
   public java.lang.Integer getOrgId() {
      return _orgId;
   }

   /**
    * Sets the orgId
    *
    * @param orgId the new orgId value
    */
   public void setOrgId(java.lang.Integer orgId) {
      _orgId = orgId;
   }
   /**
    * Returns the replaceOrgId
    *
    * @return the replaceOrgId
    */
   public java.lang.Integer getReplaceOrgId() {
      return _replaceOrgId;
   }

   /**
    * Sets the replaceOrgId
    *
    * @param replaceOrgId the new replaceOrgId value
    */
   public void setReplaceOrgId(java.lang.Integer replaceOrgId) {
      _replaceOrgId = replaceOrgId;
   }
   /**
    * Returns the startDate
    *
    * @return the startDate
    */
   public java.sql.Date getStartDate() {
      return _startDate;
   }

   /**
    * Sets the startDate
    *
    * @param startDate the new startDate value
    */
   public void setStartDate(java.sql.Date startDate) {
      _startDate = startDate;
   }
   /**
    * Returns the endDate
    *
    * @return the endDate
    */
   public java.sql.Date getEndDate() {
      return _endDate;
   }

   /**
    * Sets the endDate
    *
    * @param endDate the new endDate value
    */
   public void setEndDate(java.sql.Date endDate) {
      _endDate = endDate;
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
      if(getOrgId() == null) {
         errors.add("orgId", new ActionMessage("error.orgId.required"));
      }
      if(getReplaceOrgId() == null) {
         errors.add("replaceOrgId", new ActionMessage("error.replaceOrgId.required"));
      }
      // TODO test format/data
      return errors;
   }
}
