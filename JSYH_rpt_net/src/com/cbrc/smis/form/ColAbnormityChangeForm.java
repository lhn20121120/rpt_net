
package com.cbrc.smis.form;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form for colAbnormityChange.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 *
 * @struts.form name="colAbnormityChangeForm"
 */
public class ColAbnormityChangeForm extends ActionForm {

   private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

   private java.lang.String _childRepId = null;
   private java.lang.String _colName = null;
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
   
   public String getOrgClsId() {
	   return orgClsId;
   }

   public void setOrgClsId(String orgClsId) {
	   this.orgClsId = orgClsId;
   }

/**
    * Standard constructor.
    */
   public ColAbnormityChangeForm() {
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
    * Returns the colName
    *
    * @return the colName
    */
   public java.lang.String getColName() {
      return _colName;
   }

   /**
    * Sets the colName
    *
    * @param colName the new colName value
    */
   public void setColName(java.lang.String colName) {
      _colName = colName;
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
    * Sets the versionId
    *
    * @param versionId the new versionId value
    */
   public void setVersionId(java.lang.String versionId) {
      _versionId = versionId;
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
      if(getChildRepId() == null) {
         errors.add("childRepId", new ActionError("error.childRepId.required"));
      }
      if(getColName() == null) {
         errors.add("colName", new ActionError("error.colName.required"));
      }
      if(getOrgId() == null) {
         errors.add("orgId", new ActionError("error.orgId.required"));
      }
      if(getVersionId() == null) {
         errors.add("versionId", new ActionError("error.versionId.required"));
      }
      // TODO test format/data
      return errors;
   }
}
