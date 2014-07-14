
package com.cbrc.smis.form;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form for reportAgainSet.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 *
 * @struts.form name="reportAgainSetForm"
 */
public class ReportAgainSetForm extends ActionForm {

   private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
   //强制报表日期格式
   private final static SimpleDateFormat FORMAT1 = new SimpleDateFormat("yyyy-MM-dd");

   private java.lang.Integer _rasId = null;
   private java.lang.String _cause = null;
   private java.sql.Timestamp _setDate = null;
   private java.lang.Integer _repInId = null;
   private java.lang.String repName=null;
   private java.lang.String orgName=null;
   private java.lang.String setDateStr=null; 
   private java.lang.String childRepId = null;
   private java.lang.String versionId = null;
   private java.lang.String dataRangeDes = null;
   private java.lang.String actuFreqName = null;
   private java.lang.String currName = null;
   private java.lang.String reportDate = null;

   /** 农信新增 */
   private java.util.Date setAgainDate = null; //设置重报日期
   private java.util.Date repDate = null; //报送时间（意义同reportDate，但此为Date型）
   
   public java.lang.String getSetDateStr() {
	   if( _setDate != null ) {
	        return FORMAT1.format(_setDate);
	     }
	     else {
	        return "";
	     }
   }

	public void setSetDateStr(java.lang.String setDateStr) {
		this.setDateStr=setDateStr;
	}

  /**
    * Standard constructor.
    */
   public ReportAgainSetForm() {
   }

   /**
    * Returns the rasId
    *
    * @return the rasId
    */
   public java.lang.Integer getRasId() {
      return _rasId;
   }

   /**
    * Sets the rasId
    *
    * @param rasId the new rasId value
    */
   public void setRasId(java.lang.Integer rasId) {
      _rasId = rasId;
   }
   /**
    * Returns the cause
    *
    * @return the cause
    */
   public java.lang.String getCause() {
      return _cause;
   }

   /**
    * Sets the cause
    *
    * @param cause the new cause value
    */
   public void setCause(java.lang.String cause) {
      _cause = cause;
   }
   /**
    * Returns the setDate
    *
    * @return the setDate
    */
   public java.sql.Timestamp getSetDate() {
      return _setDate;
   }

   /**
    * Returns the setDate as a String
    *
    * @return the setDate as a String
    */
   public String getSetDateAsString() {
      if( _setDate != null ) {
         return FORMAT.format(_setDate);
      }
      else {
         return "";
      }
   }

   /**
    * Sets the setDate
    *
    * @param setDate the new setDate value
    */
   public void setSetDate(java.sql.Timestamp setDate) {
      _setDate = setDate;
   }

   /**
    * Sets the setDate as a String.
    *
    * @param setDate the new setDate value as a String
    */
   public void setSetDateAsString(String setDate) {
      try {
         _setDate = new java.sql.Timestamp(FORMAT.parse(setDate).getTime());
      } catch (ParseException pe) {
         _setDate = new java.sql.Timestamp((new java.util.Date()).getTime());
      }
   }

   /**
    * Returns the repInId
    *
    * @return the repInId
    */
   public java.lang.Integer getRepInId() {
      return _repInId;
   }

   /**
    * Sets the repInId
    *
    * @param repInId the new repInId value
    */
   public void setRepInId(java.lang.Integer repInId) {
      _repInId = repInId;
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
      if(getRasId() == null) {
         errors.add("rasId", new ActionError("error.rasId.required"));
      }
      if(getRepInId() == null) {
         errors.add("repInId", new ActionError("error.repInId.required"));
      }
      // TODO test format/data
      return errors;
   }

	public java.lang.Integer get_repInId() {
		return _repInId;
	}
	
	public void set_repInId(java.lang.Integer inId) {
		_repInId = inId;
	}
	
	public java.lang.String getOrgName() {
		return orgName;
	}
	
	public void setOrgName(java.lang.String orgName) {
		this.orgName = orgName;
	}
	
	public java.lang.String getRepName() {
		return repName;
	}
	
	public void setRepName(java.lang.String repName) {
		this.repName = repName;
	}

	public java.lang.String getChildRepId() {
		return childRepId;
	}

	public void setChildRepId(java.lang.String childRepId) {
		this.childRepId = childRepId;
	}

	public java.lang.String getVersionId() {
		return versionId;
	}

	public void setVersionId(java.lang.String versionId) {
		this.versionId = versionId;
	}

	public java.lang.String getActuFreqName() {
		return actuFreqName;
	}

	public void setActuFreqName(java.lang.String actuFreqName) {
		this.actuFreqName = actuFreqName;
	}

	public java.lang.String getCurrName() {
		return currName;
	}

	public void setCurrName(java.lang.String currName) {
		this.currName = currName;
	}

	public java.lang.String getDataRangeDes() {
		return dataRangeDes;
	}

	public void setDataRangeDes(java.lang.String dataRangeDes) {
		this.dataRangeDes = dataRangeDes;
	}

	public java.lang.String getReportDate() {
		return reportDate;
	}
	public void setReportDate(java.lang.String reportDate) {
		this.reportDate = reportDate;
	}

	public java.util.Date getSetAgainDate() {
		return setAgainDate;
	}

	public void setSetAgainDate(java.util.Date setAgainDate) {
		this.setAgainDate = setAgainDate;
	}

	public java.util.Date getRepDate() {
		return repDate;
	}

	public void setRepDate(java.util.Date repDate) {
		this.repDate = repDate;
	}

}
