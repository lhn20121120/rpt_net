
package com.gather.struts.forms;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form for MRepFreq.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 *
 * @struts.form name="MRepFreqForm"
 */
public class MRepFreqForm extends ActionForm {

   private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

   private Integer _repFreqId =null;
   private java.lang.String _repFreqName =null;

   /**
    * Standard constructor.
    */
   public MRepFreqForm() {
   }

   /**
    * Returns the repFreqId
    *
    * @return the repFreqId
    */
   public Integer getRepFreqId() {
      return _repFreqId;
   }

   /**
    * Sets the repFreqId
    *
    * @param repFreqId the new repFreqId value
    */
   public void setRepFreqId(Integer repFreqId) {
      _repFreqId = repFreqId;
   }
   /**
    * Returns the repFreqName
    *
    * @return the repFreqName
    */
   public java.lang.String getRepFreqName() {
      return _repFreqName;
   }

   /**
    * Sets the repFreqName
    *
    * @param repFreqName the new repFreqName value
    */
   public void setRepFreqName(java.lang.String repFreqName) {
      _repFreqName = repFreqName;
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
      if(getRepFreqId() == null) {
         errors.add("repFreqId", new ActionError("error.repFreqId.required"));
      }
      // TODO test format/data
      return errors;
   }
}
