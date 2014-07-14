
package com.cbrc.smis.form;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form for reportInInfo.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 *
 * @struts.form name="reportInInfoForm"
 */
public class ColAbnormityChangeInfoForm extends ActionForm {

   private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

	 /**
	  * 填报值
	  */
   private String _reportValue = null;
   /**
    * 行号
    */
   private java.lang.Integer _rowNo = null;
   /**
    * 列名
    */
   private String _colName = null;
   
   private java.lang.Integer _repInId = null;
   private Float _thanPrevRise = null;
   private Float _thanSameRise = null;
   private Float _thanSameFall = null;
   private Float _thanPrevFall = null;

   /**
    * Standard constructor.
    */
   public ColAbnormityChangeInfoForm() {
   }

   /**
    * Returns the reportValue
    *
    * @return the reportValue
    */
   public String getReportValue() {
      return _reportValue;
   }

   /**
    * Sets the reportValue
    *
    * @param reportValue the new reportValue value
    */
   public void setReportValue(String reportValue) {
      _reportValue = reportValue;
   }
   /**
    * Returns the cellId
    *
    * @return the cellId
    */
   public java.lang.Integer getRowNo() {
      return _rowNo;
   }

   /**
    * Sets the cellId
    *
    * @param cellId the new cellId value
    */
   public void setRowNo(java.lang.Integer rowNo) {
      _rowNo = rowNo;
   }
   
   /**
    * 设置列名
    *
    * @param colName String 列名
    * @return void
    */
    public void setColName(String colName){
    	this._colName=colName;
    }
    
   /**
    * 获取列名
    *
    * @return String
    */
    public String getColName(){
    	return this._colName;	
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
    * Returns the thanPrevRise
    *
    * @return the thanPrevRise
    */
   public Float getThanPrevRise() {
      return _thanPrevRise;
   }

   /**
    * Sets the thanPrevRise
    *
    * @param thanPrevRise the new thanPrevRise value
    */
   public void setThanPrevRise(Float thanPrevRise) {
      _thanPrevRise = thanPrevRise;
   }
   /**
    * Returns the thanSameRise
    *
    * @return the thanSameRise
    */
   public Float getThanSameRise() {
      return _thanSameRise;
   }

   /**
    * Sets the thanSameRise
    *
    * @param thanSameRise the new thanSameRise value
    */
   public void setThanSameRise(Float thanSameRise) {
      _thanSameRise = thanSameRise;
   }
   /**
    * Returns the thanSameFall
    *
    * @return the thanSameFall
    */
   public Float getThanSameFall() {
      return _thanSameFall;
   }

   /**
    * Sets the thanSameFall
    *
    * @param thanSameFall the new thanSameFall value
    */
   public void setThanSameFall(Float thanSameFall) {
      _thanSameFall = thanSameFall;
   }
   /**
    * Returns the thanPrevFall
    *
    * @return the thanPrevFall
    */
   public Float getThanPrevFall() {
      return _thanPrevFall;
   }

   /**
    * Sets the thanPrevFall
    *
    * @param thanPrevFall the new thanPrevFall value
    */
   public void setThanPrevFall(Float thanPrevFall) {
      _thanPrevFall = thanPrevFall;
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
}
