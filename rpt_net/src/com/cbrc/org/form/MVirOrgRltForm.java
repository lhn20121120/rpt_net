
package com.cbrc.org.form;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form for MVirOrgRlt.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 *
 * @struts.form name="MVirOrgRltForm"
 */
public class MVirOrgRltForm extends ActionForm {

   private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

   private java.lang.String _virOrgId = null;
   private java.lang.String _orgId = null;
   private java.lang.String _sumProp = null;
   private java.lang.String _startDate = null;
   private java.lang.String _endDate = null;

   /**
    * Standard constructor.
    */
   public MVirOrgRltForm() {
   }

   /**
    * Returns the virOrgId
    *
    * @return the virOrgId
    */
   public java.lang.String getVirOrgId() {
      return _virOrgId;
   }

   /**
    * Sets the virOrgId
    *
    * @param virOrgId the new virOrgId value
    */
   public void setVirOrgId(java.lang.String virOrgId) {
      _virOrgId = virOrgId;
   }
   /**
    * Returns the orgId
    *
    * @return the orgId
    */
   public java.lang.String getOrgId() {
      return _orgId;
   }

   /**
    * Sets the orgId
    *
    * @param orgId the new orgId value
    */
   public void setOrgId(java.lang.String orgId) {
      _orgId = orgId;
   }
   /**
    * Returns the sumProp
    *
    * @return the sumProp
    */
   public java.lang.String getSumProp() {
      return _sumProp;
   }

   /**
    * Sets the sumProp
    *
    * @param sumProp the new sumProp value
    */
   public void setSumProp(java.lang.String sumProp) {
      _sumProp = sumProp;
   }
   /**
    * Returns the startDate
    *
    * @return the startDate
    */
   public java.lang.String getStartDate() {
      return _startDate;
   }

   /**
    * Sets the startDate
    *
    * @param startDate the new startDate value
    */
   public void setStartDate(java.lang.String startDate) {
      _startDate = startDate;
   }
   /**
    * Returns the endDate
    *
    * @return the endDate
    */
   public java.lang.String getEndDate() {
      return _endDate;
   }

   /**
    * Sets the endDate
    *
    * @param endDate the new endDate value
    */
   public void setEndDate(java.lang.String endDate) {
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
      if(getVirOrgId() == null) {
         errors.add("virOrgId", new ActionError("error.virOrgId.required"));
      }
      if(getOrgId() == null) {
         errors.add("orgId", new ActionError("error.orgId.required"));
      }
      // TODO test format/data
      return errors;
   }
}
