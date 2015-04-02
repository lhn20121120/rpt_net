
package com.gather.struts.forms;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form for MActuRep.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 *
 * @struts.form name="MActuRepForm"
 */
public class MActuRepForm extends ActionForm {

   private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

   private java.lang.String _versionId = null;
   private Integer _delayTime =null;
   private Integer _repFreqId =null;
   private Integer _dataRangeId =null;
   private java.lang.String _childRepId = null;
   private Integer _normalTime =null;
   private Integer orgType=null;
   
   public Integer getOrgType() {
	return orgType;
}

public void setOrgType(Integer orgType) {
	this.orgType = orgType;
}

/**
    * Standard constructor.
    */
   public MActuRepForm() {
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
    * Returns the delayTime
    *
    * @return the delayTime
    */
   public Integer getDelayTime() {
      return _delayTime;
   }

   /**
    * Sets the delayTime
    *
    * @param delayTime the new delayTime value
    */
   public void setDelayTime(Integer delayTime) {
      _delayTime = delayTime;
   }
   /**
    * Returns the repFreqId
    *
    * @return the repFreqId
    */
   public Integer getRepFreqId() {
      return _repFreqId;
   }

   /**
    * Sets the repFreqId
    *
    * @param repFreqId the new repFreqId value
    */
   public void setRepFreqId(Integer repFreqId) {
      _repFreqId = repFreqId;
   }
   /**
    * Returns the dataRangeId
    *
    * @return the dataRangeId
    */
   public Integer getDataRangeId() {
      return _dataRangeId;
   }

   /**
    * Sets the dataRangeId
    *
    * @param dataRangeId the new dataRangeId value
    */
   public void setDataRangeId(Integer dataRangeId) {
      _dataRangeId = dataRangeId;
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
    * Returns the normalTime
    *
    * @return the normalTime
    */
   public Integer getNormalTime() {
      return _normalTime;
   }

   /**
    * Sets the normalTime
    *
    * @param normalTime the new normalTime value
    */
   public void setNormalTime(Integer normalTime) {
      _normalTime = normalTime;
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
      if(getVersionId() == null) {
         errors.add("versionId", new ActionError("error.versionId.required"));
      }
      if(getRepFreqId() == null) {
         errors.add("repFreqId", new ActionError("error.repFreqId.required"));
      }
      if(getDataRangeId() == null) {
         errors.add("dataRangeId", new ActionError("error.dataRangeId.required"));
      }
      if(getChildRepId() == null) {
         errors.add("childRepId", new ActionError("error.childRepId.required"));
      }
      // TODO test format/data
      return errors;
   }
}
