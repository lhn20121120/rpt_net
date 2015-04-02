
package com.cbrc.smis.form;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form for abnormityChange.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 *
 * @struts.form name="abnormityChangeForm"
 */
public class AbnormityChangeForm extends ActionForm {

   private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

   private java.lang.String _childRepId = null;
   private java.lang.String _cellId = null;
   private Float _prevRiseStandard = null;
   private Float _prevFallStandard = null;
   private Float _sameRiseStandard = null;
   private Float _sameFallStandard = null;
   private java.lang.String _orgId = null;
   private java.lang.String _versionId = null;
   /**
    * 机构类别 
    */  
   private String orgClsId=null;
   /**
    * 报表类型
    */
   private Integer reportStyle=null;
   /**
    * 异常变化设定的串值
    */
   private String standard=null;
   /**
    * 机构类别串值
    */
   private String orgCls=null;
   /**
    * 报表名称
    */
   private String reportName=null;
   /**
    * 单元格名称
    */
   private String cellName = null;

   public String getCellName() {
    return cellName;
   }

   public void setCellName(String cellName) {
    this.cellName = cellName;
   }

    public String getReportName() {
    	return reportName;
    }
    
    public void setReportName(String reportName) {
    	this.reportName = reportName;
    }

    public String getOrgCls() {
        return orgCls;
	}
	
	public void setOrgCls(String orgCls) {
		this.orgCls = orgCls;
	}

/**
    * Standard constructor.
    */
   public AbnormityChangeForm() {
   }

   /**
    * Returns the childRepId
    *
    * @return the childRepId
    */
   public java.lang.String getChildRepId() {
      return _childRepId;
   }

   /**
    * Sets the childRepId
    *
    * @param childRepId the new childRepId value
    */
   public void setChildRepId(java.lang.String childRepId) {
      _childRepId = childRepId;
   }
   /**
    * Returns the cellId
    *
    * @return the cellId
    */
   public java.lang.String getCellId() {
      return _cellId;
   }
   public Integer getCellIdAsInteger(){
	   try{
		   return Integer.valueOf(this._cellId);
	   }catch(Exception e){
		   return null;
	   }
   }
   /**
    * Sets the cellId
    *
    * @param cellId the new cellId value
    */
   public void setCellId(java.lang.Integer cellId) {
      _cellId = cellId==null?null:String.valueOf(cellId);
   }
   public void setCellId(String cellId){
	   this._cellId=cellId;
   }
   /**
    * Returns the prevRiseStandard
    *
    * @return the prevRiseStandard
    */
   public Float getPrevRiseStandard() {
      return _prevRiseStandard;
   }

   /**
    * Sets the prevRiseStandard
    *
    * @param prevRiseStandard the new prevRiseStandard value
    */
   public void setPrevRiseStandard(Float prevRiseStandard) {
      _prevRiseStandard = prevRiseStandard;
   }
   /**
    * Returns the prevFallStandard
    *
    * @return the prevFallStandard
    */
   public Float getPrevFallStandard() {
      return _prevFallStandard;
   }

   /**
    * Sets the prevFallStandard
    *
    * @param prevFallStandard the new prevFallStandard value
    */
   public void setPrevFallStandard(Float prevFallStandard) {
      _prevFallStandard = prevFallStandard;
   }
   /**
    * Returns the sameRiseStandard
    *
    * @return the sameRiseStandard
    */
   public Float getSameRiseStandard() {
      return _sameRiseStandard;
   }

   /**
    * Sets the sameRiseStandard
    *
    * @param sameRiseStandard the new sameRiseStandard value
    */
   public void setSameRiseStandard(Float sameRiseStandard) {
      _sameRiseStandard = sameRiseStandard;
   }
   /**
    * Returns the sameFallStandard
    *
    * @return the sameFallStandard
    */
   public Float getSameFallStandard() {
      return _sameFallStandard;
   }

   /**
    * Sets the sameFallStandard
    *
    * @param sameFallStandard the new sameFallStandard value
    */
   public void setSameFallStandard(Float sameFallStandard) {
      _sameFallStandard = sameFallStandard;
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
    * Returns the versionId
    *
    * @return the versionId
    */
   public java.lang.String getVersionId() {
      return _versionId;
   }

   /**
    * 设置报表类型
    * 
    * @param Integer reportStyle
    * @return void
    */
   public void setReportStyle(Integer reportStyle){
	   this.reportStyle=reportStyle;
   }   
   /**
    * 获取报表类型
    * 
    * @return Integer
    */
   public Integer getReportStyle(){
	   return this.reportStyle;
   }
   
   
   /**
    * Sets the versionId
    *
    * @param versionId the new versionId value
    */
   public void setVersionId(java.lang.String versionId) {
      _versionId = versionId;
   }

   public void reset(ActionMapping mapping, HttpServletRequest request){
	   _childRepId = null;
	   _cellId = null;
	   _prevRiseStandard = null;
	   _prevFallStandard = null;
	   _sameRiseStandard = null;
	   _sameFallStandard = null;
	   _orgId = null;
	   _versionId = null;
	   reportStyle=null;
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

	public String getStandard() {
		return standard;
	}
	
	public void setStandard(String standard) {
		this.standard = standard;
	}

	public String getOrgClsId() {
		return orgClsId;
	}

	public void setOrgClsId(String orgClsId) {
		this.orgClsId = orgClsId;
	}
}
