
package com.gather.struts.forms;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form for MCurUnit.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 *
 * @struts.form name="MCurUnitForm"
 */
public class MCurUnitForm extends ActionForm {

   private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

   private Integer _curUnit = null;
   private java.lang.String _curUnitName = null;

   /**
    * Standard constructor.
    */
   public MCurUnitForm() {
   }

   /**
    * Returns the curUnit
    *
    * @return the curUnit
    */
   public Integer getCurUnit() {
      return _curUnit;
   }

   /**
    * Sets the curUnit
    *
    * @param curUnit the new curUnit value
    */
   public void setCurUnit(Integer curUnit) {
      _curUnit = curUnit;
   }
   /**
    * Returns the curUnitName
    *
    * @return the curUnitName
    */
   public java.lang.String getCurUnitName() {
      return _curUnitName;
   }

   /**
    * Sets the curUnitName
    *
    * @param curUnitName the new curUnitName value
    */
   public void setCurUnitName(java.lang.String curUnitName) {
      _curUnitName = curUnitName;
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
      if(getCurUnit() == null) {
         errors.add("curUnit", new ActionError("error.curUnit.required"));
      }
      // TODO test format/data
      return errors;
   }
}
