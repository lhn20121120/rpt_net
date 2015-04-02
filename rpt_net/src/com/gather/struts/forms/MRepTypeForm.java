
package com.gather.struts.forms;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form for MRepType.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 *
 * @struts.form name="MRepTypeForm"
 */
public class MRepTypeForm extends ActionForm {

   private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

   private Integer _repTypeId =null;
   private java.lang.String _repTypeName =null;

   /**
    * Standard constructor.
    */
   public MRepTypeForm() {
   }

   /**
    * Returns the repTypeId
    *
    * @return the repTypeId
    */
   public Integer getRepTypeId() {
      return _repTypeId;
   }

   /**
    * Sets the repTypeId
    *
    * @param repTypeId the new repTypeId value
    */
   public void setRepTypeId(Integer repTypeId) {
      _repTypeId = repTypeId;
   }
   /**
    * Returns the repTypeName
    *
    * @return the repTypeName
    */
   public java.lang.String getRepTypeName() {
      return _repTypeName;
   }

   /**
    * Sets the repTypeName
    *
    * @param repTypeName the new repTypeName value
    */
   public void setRepTypeName(java.lang.String repTypeName) {
      _repTypeName = repTypeName;
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
      if(getRepTypeId() == null) {
         errors.add("repTypeId", new ActionError("error.repTypeId.required"));
      }
      // TODO test format/data
      return errors;
   }
}
