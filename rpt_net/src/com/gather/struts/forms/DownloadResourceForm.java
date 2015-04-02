
package com.gather.struts.forms;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form for downloadResource.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 *
 * @struts.form name="downloadResourceForm"
 */
public class DownloadResourceForm extends ActionForm {

   private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

   private Integer _resourceId = null;
   private Integer _resourceTypeId = null;
   private java.lang.String _resourceName = null;
   private java.util.Date _updateTime = null;
   private Integer _downTimes = null;
   private Integer _fileSize = null;

   /**
    * Standard constructor.
    */
   public DownloadResourceForm() {
   }

   /**
    * Returns the resourceId
    *
    * @return the resourceId
    */
   public Integer getResourceId() {
      return _resourceId;
   }

   /**
    * Sets the resourceId
    *
    * @param resourceId the new resourceId value
    */
   public void setResourceId(Integer resourceId) {
      _resourceId = resourceId;
   }
   /**
    * Returns the resourceTypeId
    *
    * @return the resourceTypeId
    */
   public Integer getResourceTypeId() {
      return _resourceTypeId;
   }

   /**
    * Sets the resourceTypeId
    *
    * @param resourceTypeId the new resourceTypeId value
    */
   public void setResourceTypeId(Integer resourceTypeId) {
      _resourceTypeId = resourceTypeId;
   }
   /**
    * Returns the resourceName
    *
    * @return the resourceName
    */
   public java.lang.String getResourceName() {
      return _resourceName;
   }

   /**
    * Sets the resourceName
    *
    * @param resourceName the new resourceName value
    */
   public void setResourceName(java.lang.String resourceName) {
      _resourceName = resourceName;
   }
   /**
    * Returns the updateTime
    *
    * @return the updateTime
    */
   public java.util.Date getUpdateTime() {
      return _updateTime;
   }

   /**
    * Returns the updateTime as a String
    *
    * @return the updateTime as a String
    */
   public String getUpdateTimeAsString() {
      if( _updateTime != null ) {
         return FORMAT.format(_updateTime);
      }
      else {
         return "";
      }
   }

   /**
    * Sets the updateTime
    *
    * @param updateTime the new updateTime value
    */
   public void setUpdateTime(java.util.Date updateTime) {
      _updateTime = updateTime;
   }

   /**
    * Sets the updateTime as a String.
    *
    * @param updateTime the new updateTime value as a String
    */
   public void setUpdateTimeAsString(String updateTime) {
      try {
         _updateTime = new java.sql.Timestamp(FORMAT.parse(updateTime).getTime());
      } catch (ParseException pe) {
         _updateTime = new java.sql.Timestamp((new java.util.Date()).getTime());
      }
   }

   /**
    * Returns the downTimes
    *
    * @return the downTimes
    */
   public Integer getDownTimes() {
      return _downTimes;
   }

   /**
    * Sets the downTimes
    *
    * @param downTimes the new downTimes value
    */
   public void setDownTimes(Integer downTimes) {
      _downTimes = downTimes;
   }
   /**
    * Returns the fileSize
    *
    * @return the fileSize
    */
   public Integer getFileSize() {
      return _fileSize;
   }

   /**
    * Sets the fileSize
    *
    * @param fileSize the new fileSize value
    */
   public void setFileSize(Integer fileSize) {
      _fileSize = fileSize;
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
      if(getResourceId() == null) {
         errors.add("resourceId", new ActionError("error.resourceId.required"));
      }
      // TODO test format/data
      return errors;
   }
}
