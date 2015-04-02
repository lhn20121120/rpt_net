
package com.cbrc.smis.form;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form for MOrg.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 *
 * @struts.form name="MOrgForm"
 */
public class MOrgForm extends ActionForm {

   private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

   private java.lang.String _orgId = null;
   private java.lang.String _orgName = null;
   private java.lang.Integer _orgType = null;
   private java.lang.String _isCorp = null;
   private java.lang.String _isInUsing = null;

   

   /**
    * Standard constructor.
    */
   public MOrgForm() {
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
    * Returns the orgName
    *
    * @return the orgName
    */
   public java.lang.String getOrgName() {
      return _orgName;
   }

   /**
    * Sets the orgName
    *
    * @param orgName the new orgName value
    */
   public void setOrgName(java.lang.String orgName) {
      _orgName = orgName;
   }
   /**
    * Returns the orgType
    *
    * @return the orgType
    */
   public java.lang.Integer getOrgType() {
      return _orgType;
   }

   /**
    * Sets the orgType
    *
    * @param orgType the new orgType value
    */
   public void setOrgType(java.lang.Integer orgType) {
      _orgType = orgType;
   }
   /**
    * Returns the isCorp
    *
    * @return the isCorp
    */
   public java.lang.String getIsCorp() {
      return _isCorp;
   }

   /**
    * Sets the isCorp
    *
    * @param isCorp the new isCorp value
    */
   public void setIsCorp(java.lang.String isCorp) {
      _isCorp = isCorp;
   }
   /**
    * Returns the isInUsing
    *
    * @return the isInUsing
    */
   public java.lang.String getIsInUsing() {
      return _isInUsing;
   }

   /**
    * Sets the isInUsing
    *
    * @param isInUsing the new isInUsing value
    */
   public void setIsInUsing(java.lang.String isInUsing) {
      _isInUsing = isInUsing;
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
