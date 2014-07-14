
package com.gather.struts.forms;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form for resourceType.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 *
 * @struts.form name="resourceTypeForm"
 */
public class ResourceTypeForm extends ActionForm {

   private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

   private Integer _resourceTypeId = null;
   private java.lang.String _typeName = null;

   /**
    * Standard constructor.
    */
   public ResourceTypeForm() {
   }

   /**
    * Returns the resourceTypeId
    *
    * @return the resourceTypeId
    */
   public Integer getResourceTypeId() {
      return _resourceTypeId;
   }

   /**
    * Sets the resourceTypeId
    *
    * @param resourceTypeId the new resourceTypeId value
    */
   public void setResourceTypeId(Integer resourceTypeId) {
      _resourceTypeId = resourceTypeId;
   }
   /**
    * Returns the typeName
    *
    * @return the typeName
    */
   public java.lang.String getTypeName() {
      return _typeName;
   }

   /**
    * Sets the typeName
    *
    * @param typeName the new typeName value
    */
   public void setTypeName(java.lang.String typeName) {
      _typeName = typeName;
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
      if(getResourceTypeId() == null) {
         errors.add("resourceTypeId", new ActionError("error.resourceTypeId.required"));
      }
      // TODO test format/data
      return errors;
   }
}
