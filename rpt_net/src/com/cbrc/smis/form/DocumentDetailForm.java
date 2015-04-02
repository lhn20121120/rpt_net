
package com.cbrc.smis.form;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form for documentDetail.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 *
 * @struts.form name="documentDetailForm"
 */
public class DocumentDetailForm extends ActionForm {

   private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

   private java.lang.Integer _docDetailId = null;
   private java.sql.Blob _docContent = null;
   private java.lang.Integer _docId = null;

   /**
    * Standard constructor.
    */
   public DocumentDetailForm() {
   }

   /**
    * Returns the docDetailId
    *
    * @return the docDetailId
    */
   public java.lang.Integer getDocDetailId() {
      return _docDetailId;
   }

   /**
    * Sets the docDetailId
    *
    * @param docDetailId the new docDetailId value
    */
   public void setDocDetailId(java.lang.Integer docDetailId) {
      _docDetailId = docDetailId;
   }
   /**
    * Returns the docContent
    *
    * @return the docContent
    */
   public java.sql.Blob getDocContent() {
      return _docContent;
   }

   /**
    * Sets the docContent
    *
    * @param docContent the new docContent value
    */
   public void setDocContent(java.sql.Blob docContent) {
      _docContent = docContent;
   }
   /**
    * Returns the docId
    *
    * @return the docId
    */
   public java.lang.Integer getDocId() {
      return _docId;
   }

   /**
    * Sets the docId
    *
    * @param docId the new docId value
    */
   public void setDocId(java.lang.Integer docId) {
      _docId = docId;
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
      if(getDocDetailId() == null) {
         errors.add("docDetailId", new ActionError("error.docDetailId.required"));
      }
      // TODO test format/data
      return errors;
   }
}
