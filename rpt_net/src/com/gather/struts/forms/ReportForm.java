
package com.gather.struts.forms;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form for report.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 *
 * @struts.form name="reportForm"
 */
public class ReportForm extends ActionForm {

   private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

   private java.lang.String _versionId = null;
   private java.lang.Integer _repId =null;
   private java.lang.Integer _datarangeid =null;
   private java.lang.Integer _term =null;
   private java.lang.Integer _times = null;
   private java.lang.String _orgid =null;
   private java.lang.Integer _tblOuterValidateFlag =null;
   private java.lang.Integer _reportDataWarehouseFlag =null;
   private java.lang.Integer _repRangeFlag =null;
   private java.lang.Integer _abmormityChangeFlag =null;
   private java.lang.Integer _tblInnerValidateFlag =null;
   private java.lang.String _repName =null;
   private java.lang.Integer _checkFlag =null;
   private java.lang.String _childrepid =null;
   private java.lang.Integer _pNumber =null;
   private java.lang.String _reportFlag =null;
   private java.lang.Integer _fileFlag =null;
   private java.lang.String _laterReportDay =null;
   private java.lang.String _writer = null;
   private java.lang.Integer _curId =null;
   private java.util.Date _reportDate = null;
   private java.lang.Integer _notReportFlag =null;
   private java.lang.String _checker = null;
   private java.lang.String _principal =null;
   private java.lang.Integer _year = null;
   private java.lang.Integer _frequency = null;
   private java.lang.String image=null;
   private java.lang.String _endDate =null;
   private java.lang.String _startDate = null;
   /**
    * 重报原因
    */
   private String cause=null;
   
   /**
    * Standard constructor.
    */
   public ReportForm() {
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
    * Returns the repId
    *
    * @return the repId
    */
   public java.lang.Integer getRepId() {
      return _repId;
   }

   /**
    * Sets the repId
    *
    * @param repId the new repId value
    */
   public void setRepId(java.lang.Integer repId) {
      _repId = repId;
   }
   /**
    * Returns the datarangeid
    *
    * @return the datarangeid
    */
   public java.lang.Integer getDatarangeid() {
      return _datarangeid;
   }

   /**
    * Sets the datarangeid
    *
    * @param datarangeid the new datarangeid value
    */
   public void setDatarangeid(java.lang.Integer datarangeid) {
      _datarangeid = datarangeid;
   }
   /**
    * Returns the term
    *
    * @return the term
    */
   public java.lang.Integer getTerm() {
      return _term;
   }

   /**
    * Sets the term
    *
    * @param term the new term value
    */
   public void setTerm(java.lang.Integer term) {
      _term = term;
   }
   /**
    * Returns the times
    *
    * @return the times
    */
   public java.lang.Integer getTimes() {
      return _times;
   }

   /**
    * Sets the times
    *
    * @param times the new times value
    */
   public void setTimes(java.lang.Integer times) {
      _times = times;
   }
   /**
    * Returns the orgid
    *
    * @return the orgid
    */
   public java.lang.String getOrgid() {
      return _orgid;
   }

   /**
    * Sets the orgid
    *
    * @param orgid the new orgid value
    */
   public void setOrgid(java.lang.String orgid) {
      _orgid = orgid;
   }
   /**
    * Returns the tblOuterValidateFlag
    *
    * @return the tblOuterValidateFlag
    */
   public java.lang.Integer getTblOuterValidateFlag() {
      return _tblOuterValidateFlag;
   }

   /**
    * Sets the tblOuterValidateFlag
    *
    * @param tblOuterValidateFlag the new tblOuterValidateFlag value
    */
   public void setTblOuterValidateFlag(java.lang.Integer tblOuterValidateFlag) {
      _tblOuterValidateFlag = tblOuterValidateFlag;
   }
   /**
    * Returns the reportDataWarehouseFlag
    *
    * @return the reportDataWarehouseFlag
    */
   public java.lang.Integer getReportDataWarehouseFlag() {
      return _reportDataWarehouseFlag;
   }

   /**
    * Sets the reportDataWarehouseFlag
    *
    * @param reportDataWarehouseFlag the new reportDataWarehouseFlag value
    */
   public void setReportDataWarehouseFlag(java.lang.Integer reportDataWarehouseFlag) {
      _reportDataWarehouseFlag = reportDataWarehouseFlag;
   }
   /**
    * Returns the repRangeFlag
    *
    * @return the repRangeFlag
    */
   public java.lang.Integer getRepRangeFlag() {
      return _repRangeFlag;
   }

   /**
    * Sets the repRangeFlag
    *
    * @param repRangeFlag the new repRangeFlag value
    */
   public void setRepRangeFlag(java.lang.Integer repRangeFlag) {
      _repRangeFlag = repRangeFlag;
   }
   /**
    * Returns the abmormityChangeFlag
    *
    * @return the abmormityChangeFlag
    */
   public java.lang.Integer getAbmormityChangeFlag() {
      return _abmormityChangeFlag;
   }

   /**
    * Sets the abmormityChangeFlag
    *
    * @param abmormityChangeFlag the new abmormityChangeFlag value
    */
   public void setAbmormityChangeFlag(java.lang.Integer abmormityChangeFlag) {
      _abmormityChangeFlag = abmormityChangeFlag;
   }
   /**
    * Returns the tblInnerValidateFlag
    *
    * @return the tblInnerValidateFlag
    */
   public java.lang.Integer getTblInnerValidateFlag() {
      return _tblInnerValidateFlag;
   }

   /**
    * Sets the tblInnerValidateFlag
    *
    * @param tblInnerValidateFlag the new tblInnerValidateFlag value
    */
   public void setTblInnerValidateFlag(java.lang.Integer tblInnerValidateFlag) {
      _tblInnerValidateFlag = tblInnerValidateFlag;
   }
   /**
    * Returns the repName
    *
    * @return the repName
    */
   public java.lang.String getRepName() {
      return _repName;
   }

   /**
    * Sets the repName
    *
    * @param repName the new repName value
    */
   public void setRepName(java.lang.String repName) {
      _repName = repName;
   }
   /**
    * Returns the checkFlag
    *
    * @return the checkFlag
    */
   public java.lang.Integer getCheckFlag() {
      return _checkFlag;
   }

   /**
    * Sets the checkFlag
    *
    * @param checkFlag the new checkFlag value
    */
   public void setCheckFlag(java.lang.Integer checkFlag) {
      _checkFlag = checkFlag;
   }
   /**
    * Returns the childrepid
    *
    * @return the childrepid
    */
   public java.lang.String getChildrepid() {
      return _childrepid;
   }

   /**
    * Sets the childrepid
    *
    * @param childrepid the new childrepid value
    */
   public void setChildrepid(java.lang.String childrepid) {
      _childrepid = childrepid;
   }
   /**
    * Returns the package
    *
    * @return the package
    */
   public java.lang.Integer getPackage() {
      return _pNumber;
   }

   /**
    * Sets the package
    *
    * @param package the new package value
    */
   public void setPackage(java.lang.Integer pNumber) {
      _pNumber = pNumber;
   }
   /**
    * Returns the reportFlag
    *
    * @return the reportFlag
    */
   public java.lang.String getReportFlag() {
      return _reportFlag;
   }

   /**
    * Sets the reportFlag
    *
    * @param reportFlag the new reportFlag value
    */
   public void setReportFlag(java.lang.String reportFlag) {
      _reportFlag = reportFlag;
   }
   /**
    * Returns the fileFlag
    *
    * @return the fileFlag
    */
   public java.lang.Integer getFileFlag() {
      return _fileFlag;
   }

   /**
    * Sets the fileFlag
    *
    * @param fileFlag the new fileFlag value
    */
   public void setFileFlag(java.lang.Integer fileFlag) {
      _fileFlag = fileFlag;
   }
   /**
    * Returns the laterReportDay
    *
    * @return the laterReportDay
    */
   public java.lang.String getLaterReportDay() {
      return _laterReportDay;
   }

   /**
    * Sets the laterReportDay
    *
    * @param laterReportDay the new laterReportDay value
    */
   public void setLaterReportDay(java.lang.String laterReportDay) {
      _laterReportDay = laterReportDay;
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
    * Returns the curId
    *
    * @return the curId
    */
   public java.lang.Integer getCurId() {
      return _curId;
   }

   /**
    * Sets the curId
    *
    * @param curId the new curId value
    */
   public void setCurId(java.lang.Integer curId) {
      _curId = curId;
   }
   /**
    * Returns the reportDate
    *
    * @return the reportDate
    */
   public java.util.Date getReportDate() {
      return _reportDate;
   }

   /**
    * Returns the reportDate as a String
    *
    * @return the reportDate as a String
    */
   public String getReportDateAsString() {
      if( _reportDate != null ) {
         return FORMAT.format(_reportDate);
      }
      else {
         return "";
      }
   }

   /**
    * Sets the reportDate
    *
    * @param reportDate the new reportDate value
    */
   public void setReportDate(java.util.Date reportDate) {
      _reportDate = reportDate;
   }

   /**
    * Sets the reportDate as a String.
    *
    * @param reportDate the new reportDate value as a String
    */
   public void setReportDateAsString(String reportDate) {
      try {
         _reportDate = new java.sql.Timestamp(FORMAT.parse(reportDate).getTime());
      } catch (ParseException pe) {
         _reportDate = new java.sql.Timestamp((new java.util.Date()).getTime());
      }
   }

   /**
    * Returns the notReportFlag
    *
    * @return the notReportFlag
    */
   public java.lang.Integer getNotReportFlag() {
      return _notReportFlag;
   }

   /**
    * Sets the notReportFlag
    *
    * @param notReportFlag the new notReportFlag value
    */
   public void setNotReportFlag(java.lang.Integer notReportFlag) {
      _notReportFlag = notReportFlag;
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
    * Returns the year
    *
    * @return the year
    */
   public java.lang.Integer getYear() {
      return _year;
   }

   /**
    * Sets the year
    *
    * @param year the new year value
    */
   public void setYear(java.lang.Integer year) {
      _year = year;
   }

   /**
    * Returns the frequency
    *
    * @return the frequency
    */
   public java.lang.Integer getFrequency() {
      return _frequency;
   }

   /**
    * Sets the frequency
    *
    * @param frequencys the new frequency value
    */
   public void setFrequency(java.lang.Integer frequency) {
      _frequency = frequency;
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
      if(getRepId() == null) {
         errors.add("repId", new ActionError("error.repId.required"));
      }
      if(getTimes() == null) {
         errors.add("times", new ActionError("error.times.required"));
      }
      if(getRepName() == null) {
         errors.add("repName", new ActionError("error.repName.required"));
      }
      // TODO test format/data
      return errors;
   }

/**
 * @return Returns the image.
 */
public java.lang.String getImage() {
	return image;
}

/**
 * @param image The image to set.
 */
public void setImage(java.lang.String image) {
	this.image = image;
}

/**
 * @return Returns the _endDate.
 */
public java.lang.String getEndDate() {
	return _endDate;
}

/**
 * @param date The _endDate to set.
 */
public void setEndDate(java.lang.String date) {
	_endDate = date;
}

/**
 * @return Returns the _startDate.
 */
public java.lang.String getStartDate() {
	return _startDate;
}

/**
 * @param date The _startDate to set.
 */
public void setStartDate(java.lang.String date) {
	_startDate = date;
}
	
	/**
	 * 设置重报原因
	 * 
	 * @param cause String 重报原因
	 * @return void
	 */
	public void setCause(String cause){
		this.cause=cause;
	}
	
	/**
	 * 获得重报原因
	 * 
	 * @return String
	 */
	public String getCause(){
		return this.cause;
	}
}
