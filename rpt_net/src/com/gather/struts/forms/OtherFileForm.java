
package com.gather.struts.forms;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

/**
 * Form for otherFile.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 *
 * @struts.form name="otherFileForm"
 */
public class OtherFileForm extends ActionForm {

   private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

   private Integer _otherFileId = null;
   private java.lang.String _orgId = null;
   private java.util.Date _upReportDate = null;
   private java.lang.String _fileName = null;
   private java.lang.String _operator = null;
   
   private FormFile referFile=null;
   private int ifCheckFlag=0;  

   /**
 * @return Returns the referFile.
 */
public FormFile getReferFile() {
	return referFile;
}

/**
 * @param referFile The referFile to set.
 */
public void setReferFile(FormFile referFile) {
	this.referFile = referFile;
}

/**
    * Standard constructor.
    */
   public OtherFileForm() {
   }

   /**
    * Returns the otherFileId
    *
    * @return the otherFileId
    */
   public Integer getOtherFileId() {
      return _otherFileId;
   }

   /**
    * Sets the otherFileId
    *
    * @param otherFileId the new otherFileId value
    */
   public void setOtherFileId(Integer otherFileId) {
      _otherFileId = otherFileId;
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
    * Returns the upReportDate
    *
    * @return the upReportDate
    */
   public java.util.Date getUpReportDate() {
      return _upReportDate;
   }

   /**
    * Returns the upReportDate as a String
    *
    * @return the upReportDate as a String
    */
   public String getUpReportDateAsString() {
      if( _upReportDate != null ) {
         return FORMAT.format(_upReportDate);
      }
      else {
         return "";
      }
   }

   /**
    * Sets the upReportDate
    *
    * @param upReportDate the new upReportDate value
    */
   public void setUpReportDate(java.util.Date upReportDate) {
      _upReportDate = upReportDate;
   }

   /**
    * Sets the upReportDate as a String.
    *
    * @param upReportDate the new upReportDate value as a String
    */
   public void setUpReportDateAsString(String upReportDate) {
      try {
         _upReportDate = new java.sql.Timestamp(FORMAT.parse(upReportDate).getTime());
      } catch (ParseException pe) {
         _upReportDate = new java.sql.Timestamp((new java.util.Date()).getTime());
      }
   }

   /**
    * Returns the fileName
    *
    * @return the fileName
    */
   public java.lang.String getFileName() {
      return _fileName;
   }

   /**
    * Sets the fileName
    *
    * @param fileName the new fileName value
    */
   public void setFileName(java.lang.String fileName) {
      _fileName = fileName;
   }
   /**
    * Returns the operator
    *
    * @return the operator
    */
   public java.lang.String getOperator() {
      return _operator;
   }

   /**
    * Sets the operator
    *
    * @param operator the new operator value
    */
   public void setOperator(java.lang.String operator) {
      _operator = operator;
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
      if(getOtherFileId() == null) {
         errors.add("otherFileId", new ActionError("error.otherFileId.required"));
      }
      // TODO test format/data
      return errors;
   }

/**
 * @return Returns the ifCheckFlag.
 */
public int getIfCheckFlag() {
	return ifCheckFlag;
}

/**
 * @param ifCheckFlag The ifCheckFlag to set.
 */
public void setIfCheckFlag(int ifCheckFlag) {
	this.ifCheckFlag = ifCheckFlag;
}
}
