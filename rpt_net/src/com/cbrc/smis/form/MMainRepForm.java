
package com.cbrc.smis.form;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form for MMainRep.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 *
 * @struts.form name="MMainRepForm"
 */
public class MMainRepForm extends ActionForm {

   private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

   private java.lang.Integer _repId = null;
   private java.lang.String _repCnName = null;
   private java.lang.String _repEnName = null;
   private java.lang.Integer _curUnit = null;
   private java.lang.Integer _repTypeId = null;

   /**
    * Standard constructor.
    */
   public MMainRepForm() {
   }

   /**
    * Returns the repId
    *
    * @return the repId
    */
   public java.lang.Integer getRepId() {
      return _repId;
   }

   /**
    * Sets the repId
    *
    * @param repId the new repId value
    */
   public void setRepId(java.lang.Integer repId) {
      _repId = repId;
   }
   /**
    * Returns the repCnName
    *
    * @return the repCnName
    */
   public java.lang.String getRepCnName() {
      return _repCnName;
   }

   /**
    * Sets the repCnName
    *
    * @param repCnName the new repCnName value
    */
   public void setRepCnName(java.lang.String repCnName) {
      _repCnName = repCnName;
   }
   /**
    * Returns the repEnName
    *
    * @return the repEnName
    */
   public java.lang.String getRepEnName() {
      return _repEnName;
   }

   /**
    * Sets the repEnName
    *
    * @param repEnName the new repEnName value
    */
   public void setRepEnName(java.lang.String repEnName) {
      _repEnName = repEnName;
   }
   /**
    * Returns the curUnit
    *
    * @return the curUnit
    */
   public java.lang.Integer getCurUnit() {
      return _curUnit;
   }

   /**
    * Sets the curUnit
    *
    * @param curUnit the new curUnit value
    */
   public void setCurUnit(java.lang.Integer curUnit) {
      _curUnit = curUnit;
   }
   /**
    * Returns the repTypeId
    *
    * @return the repTypeId
    */
   public java.lang.Integer getRepTypeId() {
      return _repTypeId;
   }

   /**
    * Sets the repTypeId
    *
    * @param repTypeId the new repTypeId value
    */
   public void setRepTypeId(java.lang.Integer repTypeId) {
      _repTypeId = repTypeId;
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
      if(getRepId() == null) {
         errors.add("repId", new ActionError("error.repId.required"));
      }
      // TODO test format/data
      return errors;
   }
}
