
package com.gather.struts.forms;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form for MCellForm.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 *
 * @struts.form name="MCellFormForm"
 */
public class MCellFormForm extends ActionForm {

   private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

   private java.lang.Integer _cellFormId = null;
   private java.lang.String _cellForm =null;
   private java.lang.Integer _formType = null;

   /**
    * Standard constructor.
    */
   public MCellFormForm() {
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
    * Returns the cellForm
    *
    * @return the cellForm
    */
   public java.lang.String getCellForm() {
      return _cellForm;
   }

   /**
    * Sets the cellForm
    *
    * @param cellForm the new cellForm value
    */
   public void setCellForm(java.lang.String cellForm) {
      _cellForm = cellForm;
   }
   /**
    * Returns the formType
    *
    * @return the formType
    */
   public java.lang.Integer getFormType() {
      return _formType;
   }

   /**
    * Sets the formType
    *
    * @param formType the new formType value
    */
   public void setFormType(java.lang.Integer formType) {
      _formType = formType;
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
      if(getCellFormId() == null) {
         errors.add("cellFormId", new ActionError("error.cellFormId.required"));
      }
      // TODO test format/data
      return errors;
   }
}
