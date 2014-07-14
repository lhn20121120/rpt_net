
package com.cbrc.org.form;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form for MVirtuOrg.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 *
 * @struts.form name="MVirtuOrgForm"
 */
public class MVirtuOrgForm extends ActionForm {

   private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

   private java.lang.String _orgId = null;
   private java.lang.String _virtualOrgName = null;
   private java.lang.String _startDate = null;
   private java.lang.String _endDate = null;
   private java.lang.String _orgCode = null;
   private java.lang.String _virtuTypeId = null;

   /**
    * Standard constructor.
    */
   public MVirtuOrgForm() {
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
    * Returns the virtualOrgName
    *
    * @return the virtualOrgName
    */
   public java.lang.String getVirtualOrgName() {
      return _virtualOrgName;
   }

   /**
    * Sets the virtualOrgName
    *
    * @param virtualOrgName the new virtualOrgName value
    */
   public void setVirtualOrgName(java.lang.String virtualOrgName) {
      _virtualOrgName = virtualOrgName;
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
    * Returns the orgCode
    *
    * @return the orgCode
    */
   public java.lang.String getOrgCode() {
      return _orgCode;
   }

   /**
    * Sets the orgCode
    *
    * @param orgCode the new orgCode value
    */
   public void setOrgCode(java.lang.String orgCode) {
      _orgCode = orgCode;
   }
   /**
    * Returns the virtuTypeId
    *
    * @return the virtuTypeId
    */
   public java.lang.String getVirtuTypeId() {
      return _virtuTypeId;
   }

   /**
    * Sets the virtuTypeId
    *
    * @param virtuTypeId the new virtuTypeId value
    */
   public void setVirtuTypeId(java.lang.String virtuTypeId) {
      _virtuTypeId = virtuTypeId;
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
         errors.add("orgId", new ActionError("error.orgId.required"));
      }
      // TODO test format/data
      return errors;
   }
}
