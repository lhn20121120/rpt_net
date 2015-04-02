
package com.gather.struts.forms;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form for MDataRgType.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 *
 * @struts.form name="MDataRgTypeForm"
 */
public class MDataRgTypeForm extends ActionForm {

   private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

   private Integer _dataRangeId = null;
   private java.lang.String _dataRgDesc = null;

   /**
    * Standard constructor.
    */
   public MDataRgTypeForm() {
   }

   /**
    * Returns the dataRangeId
    *
    * @return the dataRangeId
    */
   public Integer getDataRangeId() {
      return _dataRangeId;
   }

   /**
    * Sets the dataRangeId
    *
    * @param dataRangeId the new dataRangeId value
    */
   public void setDataRangeId(Integer dataRangeId) {
      _dataRangeId = dataRangeId;
   }
   /**
    * Returns the dataRgDesc
    *
    * @return the dataRgDesc
    */
   public java.lang.String getDataRgDesc() {
      return _dataRgDesc;
   }

   /**
    * Sets the dataRgDesc
    *
    * @param dataRgDesc the new dataRgDesc value
    */
   public void setDataRgDesc(java.lang.String dataRgDesc) {
      _dataRgDesc = dataRgDesc;
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
      if(getDataRangeId() == null) {
         errors.add("dataRangeId", new ActionError("error.dataRangeId.required"));
      }
      // TODO test format/data
      return errors;
   }
}
