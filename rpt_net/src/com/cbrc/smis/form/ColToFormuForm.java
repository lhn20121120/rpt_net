
package com.cbrc.smis.form;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form for colToFormu.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 *
 * @struts.form name="colToFormuForm"
 */
public class ColToFormuForm extends ActionForm {

   private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

   private java.lang.Integer _cellFormuId = null;
   private java.lang.Integer _childRepId = null;
   private java.lang.String _versionId = null;
   private java.lang.String _colName = null;

   /**
    * Standard constructor.
    */
   public ColToFormuForm() {
   }

   /**
    * Returns the cellFormuId
    *
    * @return the cellFormuId
    */
   public java.lang.Integer getCellFormuId() {
      return _cellFormuId;
   }

   /**
    * Sets the cellFormuId
    *
    * @param cellFormuId the new cellFormuId value
    */
   public void setCellFormuId(java.lang.Integer cellFormuId) {
      _cellFormuId = cellFormuId;
   }
   /**
    * Returns the childRepId
    *
    * @return the childRepId
    */
   public java.lang.Integer getChildRepId() {
      return _childRepId;
   }

   /**
    * Sets the childRepId
    *
    * @param childRepId the new childRepId value
    */
   public void setChildRepId(java.lang.Integer childRepId) {
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
    * Returns the colName
    *
    * @return the colName
    */
   public java.lang.String getColName() {
      return _colName;
   }

   /**
    * Sets the colName
    *
    * @param colName the new colName value
    */
   public void setColName(java.lang.String colName) {
      _colName = colName;
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
      if(getCellFormuId() == null) {
         errors.add("cellFormuId", new ActionError("error.cellFormuId.required"));
      }
      if(getChildRepId() == null) {
         errors.add("childRepId", new ActionError("error.childRepId.required"));
      }
      if(getVersionId() == null) {
         errors.add("versionId", new ActionError("error.versionId.required"));
      }
      if(getColName() == null) {
         errors.add("colName", new ActionError("error.colName.required"));
      }
      // TODO test format/data
      return errors;
   }
}
