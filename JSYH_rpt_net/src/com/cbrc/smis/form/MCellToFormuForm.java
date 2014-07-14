
package com.cbrc.smis.form;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form for MCellToFormu.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 *
 * @struts.form name="MCellToFormuForm"
 */
public class MCellToFormuForm extends ActionForm {

   private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

   private Integer cellToFormuId=null;
   private java.lang.String _startDate = null;
   private java.lang.String _endDate = null;
   private java.lang.Integer _cellFormuId = null;
   private java.lang.Integer _cellId = null;
   
   private String cellFormu = null;
   private Integer formuType = null;
   
   /**
    * 子报表ID
    */
   private String childRepId=null;
   /**
    * 版本号
    */
   private String versionId=null;
   
   /**
    * Standard constructor.
    */
   public MCellToFormuForm() {
   }

   /**
    * Returns the startDate
    *
    * @return the startDate
    */
   public java.lang.String getStartDate() {
      return _startDate;
   }

   /**
    * Sets the startDate
    *
    * @param startDate the new startDate value
    */
   public void setStartDate(java.lang.String startDate) {
      _startDate = startDate;
   }
   /**
    * Returns the endDate
    *
    * @return the endDate
    */
   public java.lang.String getEndDate() {
      return _endDate;
   }

   /**
    * Sets the endDate
    *
    * @param endDate the new endDate value
    */
   public void setEndDate(java.lang.String endDate) {
      _endDate = endDate;
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
    * Returns the cellId
    *
    * @return the cellId
    */
   public java.lang.Integer getCellId() {
      return _cellId;
   }

   /**
    * Sets the cellId
    *
    * @param cellId the new cellId value
    */
   public void setCellId(java.lang.Integer cellId) {
      _cellId = cellId;
   }

   
	public String getChildRepId() {
		return childRepId;
	}
	
	public void setChildRepId(String childRepId) {
		this.childRepId = childRepId;
	}
	
	public String getVersionId() {
		return versionId;
	}
	
	public void setVersionId(String versionId) {
		this.versionId = versionId;
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

    public String getCellFormu() {
        return cellFormu;
    }
    
    public void setCellFormu(String cellFormu) {
        this.cellFormu = cellFormu;
    }
    
    public Integer getFormuType() {
        return formuType;
    }
    
    public void setFormuType(Integer formuType) {
        this.formuType = formuType;
    }

	public Integer getCellToFormuId() {
		return cellToFormuId;
	}

	public void setCellToFormuId(Integer cellToFormuId) {
		this.cellToFormuId = cellToFormuId;
	}


}
