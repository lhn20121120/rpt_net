
package com.cbrc.org.form;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form for MRegionTyp.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 *
 * @struts.form name="MRegionTypForm"
 */
public class MRegionTypForm extends ActionForm {

   private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

   private java.lang.String _regionTypId = null;
   private java.lang.String _regionTypNm = null;

   /**
    * Standard constructor.
    */
   public MRegionTypForm() {
   }

   /**
    * Returns the regionTypId
    *
    * @return the regionTypId
    */
   public java.lang.String getRegionTypId() {
      return _regionTypId;
   }

   /**
    * Sets the regionTypId
    *
    * @param regionTypId the new regionTypId value
    */
   public void setRegionTypId(java.lang.String regionTypId) {
      _regionTypId = regionTypId;
   }
   /**
    * Returns the regionTypNm
    *
    * @return the regionTypNm
    */
   public java.lang.String getRegionTypNm() {
      return _regionTypNm;
   }

   /**
    * Sets the regionTypNm
    *
    * @param regionTypNm the new regionTypNm value
    */
   public void setRegionTypNm(java.lang.String regionTypNm) {
      _regionTypNm = regionTypNm;
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
      if(getRegionTypId() == null) {
         errors.add("regionTypId", new ActionError("error.regionTypId.required"));
      }
      // TODO test format/data
      return errors;
   }
}
