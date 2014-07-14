
package com.gather.struts.forms;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form for dataValidateInfo.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 *
 * @struts.form name="dataValidateInfoForm"
 */
public class DataValidateInfoForm extends ActionForm {

   private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

   private java.lang.Integer _repOutId =  null;
   private java.lang.Integer _validateTypeId =  null;
   private java.lang.Integer _cellFormId = null;
   private Integer sequenceId=null;

   /**
 * @return Returns the sequenceId.
 */
public Integer getSequenceId() {
	return sequenceId;
}

/**
 * @param sequenceId The sequenceId to set.
 */
public void setSequenceId(Integer sequenceId) {
	this.sequenceId = sequenceId;
}

/**
    * Standard constructor.
    */
   public DataValidateInfoForm() {
   }

   /**
    * Returns the repOutId
    *
    * @return the repOutId
    */
   public java.lang.Integer getRepOutId() {
      return _repOutId;
   }

   /**
    * Sets the repOutId
    *
    * @param repOutId the new repOutId value
    */
   public void setRepOutId(java.lang.Integer repOutId) {
      _repOutId = repOutId;
   }
   /**
    * Returns the validateTypeId
    *
    * @return the validateTypeId
    */
   public java.lang.Integer getValidateTypeId() {
      return _validateTypeId;
   }

   /**
    * Sets the validateTypeId
    *
    * @param validateTypeId the new validateTypeId value
    */
   public void setValidateTypeId(java.lang.Integer validateTypeId) {
      _validateTypeId = validateTypeId;
   }
   /**
    * Returns the cellFormId
    *
    * @return the cellFormId
    */
   public java.lang.Integer getCellFormId() {
      return _cellFormId;
   }

   /**
    * Sets the cellFormId
    *
    * @param cellFormId the new cellFormId value
    */
   public void setCellFormId(java.lang.Integer cellFormId) {
      _cellFormId = cellFormId;
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
      if(getRepOutId() == null) {
         errors.add("repOutId", new ActionError("error.repOutId.required"));
      }
      if(getValidateTypeId() == null) {
         errors.add("validateTypeId", new ActionError("error.validateTypeId.required"));
      }
      if(getCellFormId() == null) {
         errors.add("cellFormId", new ActionError("error.cellFormId.required"));
      }
      // TODO test format/data
      return errors;
   }
}
