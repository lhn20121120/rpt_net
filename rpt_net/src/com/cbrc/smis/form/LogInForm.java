
package com.cbrc.smis.form;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.cbrc.smis.util.FitechUtil;

/** 
 * @author 姚捷
 * 模块名:日志管理
 * 参数:  _logInId     日志编号
 * 		  _logTime     日志时间
 * 		  _logTypeId  日志类型编号
 * 		  _userName    操作员
 * 		  _startDate   起始时间
 * 		  _endDate     结束时间
 * 		  _operation    日志内容
 * 		  _memo 		备注
 * 		  _deleteLogInId 要删除的日志编号
 * 		
 * Creation date: 11-29-2005
 * @struts.form name="LogForm"
 */
public class LogInForm extends ActionForm {

   private final static SimpleDateFormat STARTFORMAT = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
   private final static SimpleDateFormat ENDFORMAT = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
   
   private java.lang.Integer _logInId = null;
   private java.lang.String _userName = null;
   /**
    * 日志时间
    */
   private java.lang.String _logTime = null;
   private java.lang.String _operation = null;
   private java.lang.String _memo = null;
   private java.lang.Integer _logTypeId = null;
   private String _logType = null;
   private String _startDate = null;
   private String _endDate = null;
   private java.lang.Integer[] _deleteLogInId = null;
   /**
    * Standard constructor.
    */
   public LogInForm() {
   }

   /**
    * Returns the logInId
    *
    * @return the logInId
    */
   public java.lang.Integer getLogInId() {
      return _logInId;
   }

   /**
    * Sets the logInId
    *
    * @param logInId the new logInId value
    */
   public void setLogInId(java.lang.Integer logInId) {
      _logInId = logInId;
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
      //--------------------
      //gongming 2008-07-25
      if(null != _userName)
          _userName = _userName.trim();
   }
   /**
    * Returns the logTime
    *
    * @return the logTime
    */
   public java.lang.String getLogTime() {
      return _logTime;
   }

   /**
    * Sets the logTime
    *
    * @param logTime the new logTime value
    */
   public void setLogTime(java.lang.String logTime) {
      _logTime = logTime;
   }
   
   /**
    * 设日志时间
    * 
    * @param _logTime Date 日志时间
    * @return void
    */
   public void setLogTime(Date logTime){
       
       
	   
	   java.text.SimpleDateFormat dateFormat=new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	  this._logTime = logTime==null ? "" : dateFormat.format(logTime);   
   }
   
   /**
    * 获得日志时间
    * 
    * @return Date
    */
   public Date getLogTimeAsDate(){
	  if(this._logTime!=null){
		  try{
			  return STARTFORMAT.parse(this._logTime);
		  }catch(ParseException pe){
			  return new Date();
		  }
	  }else{
		  return new Date();
	  }
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

   public String getEndDate() {
	   Date _formatEndDate = FitechUtil.formatToTimestamp(this._endDate);
	   return _formatEndDate == null ? null : ENDFORMAT.format(_formatEndDate);
	}

	public void setEndDate(String date) {
		_endDate = date;
	}

	public String getLogType() {
		return _logType;
	}

	public void setLogType(String type) {
		_logType = type;
	}

	public String getStartDate() {
		Date _formatStartDate = FitechUtil.formatToTimestamp(this._startDate);
		return _formatStartDate == null ? null : STARTFORMAT.format(_formatStartDate);
	}

	public void setStartDate(String date) {
		_startDate = date;
	}
	
	public java.lang.Integer[] getDeleteLogInId() {
		return _deleteLogInId;
	}

	public void setDeleteLogInId(java.lang.Integer[] logInId) {
		_deleteLogInId = logInId;
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
      return errors;
   }
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		_logInId = null;
		_userName = null;
		_logTime = null;
		_operation = null;
		_memo = null;
		_logTypeId = null;
		_logType = null;
		_startDate = null;
		_endDate = null;
		_deleteLogInId = null;
	}
}
