
package com.cbrc.smis.form;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * 工作日历明细
 * @struts.form name="calendarDetailForm"
 */
public class CalendarDetailForm extends ActionForm {

   private final static SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");
   /**
    * 年
    */
   private java.lang.Integer _calYear = null;
   /**
    * 月
    */
   private java.lang.Integer _calMonth = null;
   /**
    * 日
    */
   private java.lang.Integer _calDay = null;
   /**
    * 工作日历id
    */
   private java.lang.Integer _calId = null;
   /**
    * 日历类别（工作日，非工作日）
    */
   private java.lang.Integer _calTypeId = null;
   /**
    *  日期 
    */
   private String calDate=null;
   /**
    * 工作日集合（工作日历设定—保存中使用）
    */
   private Integer[] _workDay = null;
 
   

/**
    * Standard constructor.
    */
   public CalendarDetailForm() {
   }

   /**
    * Returns the calYear
    *
    * @return the calYear
    */
   public java.lang.Integer getCalYear() {
      return _calYear;
   }

   /**
    * Sets the calYear
    *
    * @param calYear the new calYear value
    */
   public void setCalYear(java.lang.Integer calYear) {
      _calYear = calYear;
   }
   /**
    * Returns the calMonth
    *
    * @return the calMonth
    */
   public java.lang.Integer getCalMonth() {
      return _calMonth;
   }

   /**
    * Sets the calMonth
    *
    * @param calMonth the new calMonth value
    */
   public void setCalMonth(java.lang.Integer calMonth) {
      _calMonth = calMonth;
   }
   /**
    * Returns the calId
    *
    * @return the calId
    */
   public java.lang.Integer getCalId() {
      return _calId;
   }

   /**
    * Sets the calId
    *
    * @param calId the new calId value
    */
   public void setCalId(java.lang.Integer calId) {
      _calId = calId;
   }
   /**
    * Returns the calTypeId
    *
    * @return the calTypeId
    */
   public java.lang.Integer getCalTypeId() {
      return _calTypeId;
   }

   /**
    * Sets the calTypeId
    *
    * @param calTypeId the new calTypeId value
    */
   public void setCalTypeId(java.lang.Integer calTypeId) {
      _calTypeId = calTypeId;
   }

   /**
    * 获取日历日期
    * 
    * @return String
    */
   public String getCalDate() {
		return calDate;
	}
   /**
    * 获取日期
    * 
    * @return Date
    */
   public Date getCalDateAsDate(){
	   if(this.calDate!=null){
		   try{
			   return FORMAT.parse(this.calDate);
		   }catch(ParseException pe){
			   return null;
		   }
	   }else{
		   return null;
	   }
   }
    /**
     * 设置日历日期
     * 
     * @param calDate String 
     * @return void
     */
	public void setCalDate(String calDate) {
		this.calDate = calDate;
	}
	
	/**
     * 设置日历日期
     * 
     * @param calDate Date 
     * @return void
     */
	public void setCalDate(Date calDate){
		this.calDate=calDate==null?"":FORMAT.format(calDate);
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
      
      // TODO test format/data
      return errors;
   }

	public java.lang.Integer getCalDay() {
		return _calDay;
	}
	
	public void setCalDay(java.lang.Integer day) {
		_calDay = day;
	}
	
	public Integer[] getWorkDay() {
		return _workDay;
	}
	
	public void setWorkDay(Integer[] day) {
		_workDay = day;
	}


}
