
package com.cbrc.org.form;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form for MRegion.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 *
 * @struts.form name="MRegionForm"
 */
public class MRegionForm extends ActionForm {

   private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

   private java.lang.String _regionId = null;
   private java.lang.String _regionTypId = null;
   private java.lang.String _regionName = null;

   /**
    * Standard constructor.
    */
   public MRegionForm() {
   }

   /**
    * Returns the regionId
    *
    * @return the regionId
    */
   public java.lang.String getRegionId() {
      return _regionId;
   }

   /**
    * Sets the regionId
    *
    * @param regionId the new regionId value
    */
   public void setRegionId(java.lang.String regionId) {
      _regionId = regionId;
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
    * Returns the regionName
    *
    * @return the regionName
    */
   public java.lang.String getRegionName() {
      return _regionName;
   }

   /**
    * Sets the regionName
    *
    * @param regionName the new regionName value
    */
   public void setRegionName(java.lang.String regionName) {
      _regionName = regionName;
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
      if(getRegionId() == null) {
         errors.add("regionId", new ActionError("error.regionId.required"));
      }
      // TODO test format/data
      return errors;
   }
}
