
package com.cbrc.smis.form;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form for listingTable.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 *
 * @struts.form name="listingTableForm"
 */
public class ListingTableForm extends ActionForm {

   private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

   private java.lang.Integer _childRepId = null;
   private java.lang.String _versionId = null;
   private java.lang.String _tableName = null;
   private java.sql.Timestamp _createTime = null;

   /**
    * Standard constructor.
    */
   public ListingTableForm() {
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
    * Returns the tableName
    *
    * @return the tableName
    */
   public java.lang.String getTableName() {
      return _tableName;
   }

   /**
    * Sets the tableName
    *
    * @param tableName the new tableName value
    */
   public void setTableName(java.lang.String tableName) {
      _tableName = tableName;
   }
   /**
    * Returns the createTime
    *
    * @return the createTime
    */
   public java.sql.Timestamp getCreateTime() {
      return _createTime;
   }

   /**
    * Returns the createTime as a String
    *
    * @return the createTime as a String
    */
   public String getCreateTimeAsString() {
      if( _createTime != null ) {
         return FORMAT.format(_createTime);
      }
      else {
         return "";
      }
   }

   /**
    * Sets the createTime
    *
    * @param createTime the new createTime value
    */
   public void setCreateTime(java.sql.Timestamp createTime) {
      _createTime = createTime;
   }

   /**
    * Sets the createTime as a String.
    *
    * @param createTime the new createTime value as a String
    */
   public void setCreateTimeAsString(String createTime) {
      try {
         _createTime = new java.sql.Timestamp(FORMAT.parse(createTime).getTime());
      } catch (ParseException pe) {
         _createTime = new java.sql.Timestamp((new java.util.Date()).getTime());
      }
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
      if(getTableName() == null) {
         errors.add("tableName", new ActionError("error.tableName.required"));
      }
      // TODO test format/data
      return errors;
   }
}
