
package com.cbrc.smis.form;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form for document.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 *
 * @struts.form name="documentForm"
 */
public class DocumentForm extends ActionForm {

   private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

   private java.lang.Integer _docId = null;
   private java.lang.String _docType = null;
   private java.lang.Integer _docSize = null;
   private java.lang.String _orgId = null;
   private java.sql.Date _docDate = null;
   private java.lang.String _docMemo = null;
   private java.lang.String _writer = null;
   private java.lang.String _checker = null;
   private java.lang.String _principal = null;

   /**
    * Standard constructor.
    */
   public DocumentForm() {
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
    * Returns the docType
    *
    * @return the docType
    */
   public java.lang.String getDocType() {
      return _docType;
   }

   /**
    * Sets the docType
    *
    * @param docType the new docType value
    */
   public void setDocType(java.lang.String docType) {
      _docType = docType;
   }
   /**
    * Returns the docSize
    *
    * @return the docSize
    */
   public java.lang.Integer getDocSize() {
      return _docSize;
   }

   /**
    * Sets the docSize
    *
    * @param docSize the new docSize value
    */
   public void setDocSize(java.lang.Integer docSize) {
      _docSize = docSize;
   }
   /**
    * Returns the orgId
    *
    * @return the orgId
    */
   public java.lang.String getOrgId() {
      return _orgId;
   }

   /**
    * Sets the orgId
    *
    * @param orgId the new orgId value
    */
   public void setOrgId(java.lang.String orgId) {
      _orgId = orgId;
   }
   /**
    * Returns the docDate
    *
    * @return the docDate
    */
   public java.sql.Date getDocDate() {
      return _docDate;
   }

   /**
    * Sets the docDate
    *
    * @param docDate the new docDate value
    */
   public void setDocDate(java.sql.Date docDate) {
      _docDate = docDate;
   }
   /**
    * Returns the docMemo
    *
    * @return the docMemo
    */
   public java.lang.String getDocMemo() {
      return _docMemo;
   }

   /**
    * Sets the docMemo
    *
    * @param docMemo the new docMemo value
    */
   public void setDocMemo(java.lang.String docMemo) {
      _docMemo = docMemo;
   }
   /**
    * Returns the writer
    *
    * @return the writer
    */
   public java.lang.String getWriter() {
      return _writer;
   }

   /**
    * Sets the writer
    *
    * @param writer the new writer value
    */
   public void setWriter(java.lang.String writer) {
      _writer = writer;
   }
   /**
    * Returns the checker
    *
    * @return the checker
    */
   public java.lang.String getChecker() {
      return _checker;
   }

   /**
    * Sets the checker
    *
    * @param checker the new checker value
    */
   public void setChecker(java.lang.String checker) {
      _checker = checker;
   }
   /**
    * Returns the principal
    *
    * @return the principal
    */
   public java.lang.String getPrincipal() {
      return _principal;
   }

   /**
    * Sets the principal
    *
    * @param principal the new principal value
    */
   public void setPrincipal(java.lang.String principal) {
      _principal = principal;
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
      if(getDocId() == null) {
         errors.add("docId", new ActionError("error.docId.required"));
      }
      // TODO test format/data
      return errors;
   }
}
