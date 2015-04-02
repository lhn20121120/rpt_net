
package com.gather.struts.forms;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form for MCurr.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 *
 * @struts.form name="MCurrForm"
 */
public class MCurrForm extends ActionForm {

   private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

   private java.lang.Integer _curId = null;
   private java.lang.String _curName = null;

   /**
    * Standard constructor.
    */
   public MCurrForm() {
   }

   /**
    * Returns the curId
    *
    * @return the curId
    */
   public java.lang.Integer getCurId() {
      return _curId;
   }

   /**
    * Sets the curId
    *
    * @param curId the new curId value
    */
   public void setCurId(java.lang.Integer curId) {
      _curId = curId;
   }
   /**
    * Returns the curName
    *
    * @return the curName
    */
   public java.lang.String getCurName() {
      return _curName;
   }

   /**
    * Sets the curName
    *
    * @param curName the new curName value
    */
   public void setCurName(java.lang.String curName) {
      _curName = curName;
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
      if(getCurId() == null) {
         errors.add("curId", new ActionError("error.curId.required"));
      }
      // TODO test format/data
      return errors;
   }
}
