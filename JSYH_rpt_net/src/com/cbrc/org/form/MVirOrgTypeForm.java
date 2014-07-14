
package com.cbrc.org.form;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form for MVirOrgType.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 *
 * @struts.form name="MVirOrgTypeForm"
 */
public class MVirOrgTypeForm extends ActionForm {

   private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

   private java.lang.String _virtuTypeId = null;
   private java.lang.String _virtuTypeNm = null;

   /**
    * Standard constructor.
    */
   public MVirOrgTypeForm() {
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
    * Returns the virtuTypeNm
    *
    * @return the virtuTypeNm
    */
   public java.lang.String getVirtuTypeNm() {
      return _virtuTypeNm;
   }

   /**
    * Sets the virtuTypeNm
    *
    * @param virtuTypeNm the new virtuTypeNm value
    */
   public void setVirtuTypeNm(java.lang.String virtuTypeNm) {
      _virtuTypeNm = virtuTypeNm;
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
      if(getVirtuTypeId() == null) {
         errors.add("virtuTypeId", new ActionError("error.virtuTypeId.required"));
      }
      // TODO test format/data
      return errors;
   }
}
