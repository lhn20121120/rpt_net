/** 姬怀宝
	 * This is a delegate class to handle interaction with the backend persistence layer of hibernate. 
	 * It has a set of methods to handle persistence for MRepRange data (i.e. 
	 * com.gather.struts.forms.MRepRangeForm objects).
	 * 
	 * 新增的方法
	 */

package com.gather.struts.forms;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
public class CopyLogform extends ActionForm {
	 private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

	   private Integer _logId = null;
	   private java.lang.String _userName = null;
	   private java.util.Date _logTime = null;
	   private java.lang.String _operation = null;
	   private java.lang.String _memo = null;
	   private java.lang.Integer _logTypeId = null;
	   private java.lang.String _logType = null;
	   private java.lang.String _endDate = null;
	   private java.lang.String _startDate = null;
	   private String orgId = null;
	   /**
	    * Standard constructor.
	    */
	   public CopyLogform() {
	   }

	   /**
	    * Returns the logId
	    *
	    * @return the logId
	    */
	   public Integer getLogId() {
	      return _logId;
	   }

	   /**
	    * Sets the logId
	    *
	    * @param logId the new logId value
	    */
	   public void setLogId(Integer logId) {
	      _logId = logId;
	   }
	   /**
	    * Returns the userName
	    *
	    * @return the userName
	    */
	   public java.lang.String getUserName() {
	      return _userName;
	   }

	   /**
	    * Sets the userName
	    *
	    * @param userName the new userName value
	    */
	   public void setUserName(java.lang.String userName) {
	      _userName = userName;
	   }
	   /**
	    * Returns the logTime
	    *
	    * @return the logTime
	    */
	   public java.util.Date getLogTime() {
	      return _logTime;
	   }

	   /**
	    * Sets the logTime
	    *
	    * @param logTime the new logTime value
	    */
	   public void setLogTime(java.util.Date logTime) {
	      _logTime = logTime;
	   }
	   /**
	    * Returns the operation
	    *
	    * @return the operation
	    */
	   public java.lang.String getOperation() {
	      return _operation;
	   }

	   /**
	    * Sets the operation
	    *
	    * @param operation the new operation value
	    */
	   public void setOperation(java.lang.String operation) {
	      _operation = operation;
	   }
	   /**
	    * Returns the memo
	    *
	    * @return the memo
	    */
	   public java.lang.String getMemo() {
	      return _memo;
	   }

	   /**
	    * Sets the memo
	    *
	    * @param memo the new memo value
	    */
	   public void setMemo(java.lang.String memo) {
	      _memo = memo;
	   }
	   /**
	    * Returns the logTypeId
	    *
	    * @return the logTypeId
	    */
	   public java.lang.Integer getLogTypeId() {
	      return _logTypeId;
	   }

	   /**
	    * Sets the logTypeId
	    *
	    * @param logTypeId the new logTypeId value
	    */
	   public void setLogTypeId(java.lang.Integer logTypeId) {
	      _logTypeId = logTypeId;
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
	      if(getLogId() == null) {
	         errors.add("logId", new ActionError("error.logId.required"));
	      }
	      // TODO test format/data
	      return errors;
	   }

	/**
	 * @return Returns the _logType.
	 */
	public String getLogType() {
		return _logType;
	}

	/**
	 * @param type The _logType to set.
	 */
	public void setLogType(String type) {
		_logType = type;
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
	 * @return Returns the orgId.
	 */
	public String getOrgId() {
		return orgId;
	}
	/**
	 * @param orgId The orgId to set.
	 */
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
}
