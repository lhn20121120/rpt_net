
package com.gather.struts.forms;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form for reportData.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 *
 * @struts.form name="reportDataForm"
 */
public class ReportDataForm extends ActionForm {

   private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

   private java.sql.Blob _pdf = null;
   private java.sql.Blob _note = null;
   private java.sql.Blob _xml = null;
   private java.lang.String _childRepId = null;
   private java.lang.String _versionId =null;

   /**
    * Standard constructor.
    */
   public ReportDataForm() {
   }

   /**
    * Returns the pdf
    *
    * @return the pdf
    */
   public java.sql.Blob getPdf() {
      return _pdf;
   }

   /**
    * Sets the pdf
    *
    * @param pdf the new pdf value
    */
   public void setPdf(java.sql.Blob pdf) {
      _pdf = pdf;
   }
   /**
    * Returns the note
    *
    * @return the note
    */
   public java.sql.Blob getNote() {
      return _note;
   }

   /**
    * Sets the note
    *
    * @param note the new note value
    */
   public void setNote(java.sql.Blob note) {
      _note = note;
   }
   /**
    * Returns the xml
    *
    * @return the xml
    */
   public java.sql.Blob getXml() {
      return _xml;
   }

   /**
    * Sets the xml
    *
    * @param xml the new xml value
    */
   public void setXml(java.sql.Blob xml) {
      _xml = xml;
   }
   /**
    * Returns the childRepId
    *
    * @return the childRepId
    */
   public java.lang.String getChildRepId() {
      return _childRepId;
   }

   /**
    * Sets the childRepId
    *
    * @param childRepId the new childRepId value
    */
   public void setChildRepId(java.lang.String childRepId) {
      _childRepId = childRepId;
   }
   /**
    * Returns the versionId
    *
    * @return the versionId
    */
   public java.lang.String getVersionId() {
      return _versionId;
   }

   /**
    * Sets the versionId
    *
    * @param versionId the new versionId value
    */
   public void setVersionId(java.lang.String versionId) {
      _versionId = versionId;
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
      if(getChildRepId() == null) {
         errors.add("childRepId", new ActionError("error.childRepId.required"));
      }
      if(getVersionId() == null) {
         errors.add("versionId", new ActionError("error.versionId.required"));
      }
      // TODO test format/data
      return errors;
   }
}
