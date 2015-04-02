
package com.cbrc.smis.form;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

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
public class MActuRepSetForm extends ActionForm {

   private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

   private java.lang.Integer _delayTime = null;
   private java.lang.Integer _dataRangeId = null;
   private java.lang.String _childRepId = null;
   private String _versionId = null;
   private java.lang.Integer _repFreqId = null;
   private java.lang.Integer _normalTime = null;
   
   private Integer OATId;
   
   /**
    * 数据范围描述
    */
   private String dataRgDesc = null;
   /**
    * 频度名称
    */
   private String repFreqName =null;
   /**
    * 正常时间数组 
    */
   private Integer[] _normalTimes = null;
   /**
    * 延迟时间数组
    */
   private Integer[] _delayTimes = null;
   /**
    * 数据范围id数组
    */
   private Integer[] _dataRangeIds =null;
   /**
    * 上报频度id数组
    */
   private Integer[] _repFreqIds = null;
   
   /**
    * 报表名称
    */
   private String reportName=null;
   /**
    * 报表类别
    */
   private Integer reportStyle=null;
   
   public Integer getReportStyle() {
	return reportStyle;
	}
	
	public void setReportStyle(Integer reportStyle) {
		this.reportStyle = reportStyle;
	}

/**
    * Standard constructor.
    */
   public MActuRepSetForm() {
   }

   /**
    * Returns the delayTime
    *
    * @return the delayTime
    */
   public java.lang.Integer getDelayTime() {
      return _delayTime;
   }

   /**
    * Sets the delayTime
    *
    * @param delayTime the new delayTime value
    */
   public void setDelayTime(java.lang.Integer delayTime) {
      _delayTime = delayTime;
   }
   /**
    * Returns the dataRangeId
    *
    * @return the dataRangeId
    */
   public java.lang.Integer getDataRangeId() {
      return _dataRangeId;
   }

   /**
    * Sets the dataRangeId
    *
    * @param dataRangeId the new dataRangeId value
    */
   public void setDataRangeId(java.lang.Integer dataRangeId) {
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
    * Returns the versionId
    *
    * @return the versionId
    */
   public String getVersionId() {
      return _versionId;
   }

   /**
    * Sets the versionId
    *
    * @param versionId the new versionId value
    */
   public void setVersionId(String versionId) {
      _versionId = versionId;
   }
   /**
    * Returns the repFreqId
    *
    * @return the repFreqId
    */
   public java.lang.Integer getRepFreqId() {
      return _repFreqId;
   }

   /**
    * Sets the repFreqId
    *
    * @param repFreqId the new repFreqId value
    */
   public void setRepFreqId(java.lang.Integer repFreqId) {
      _repFreqId = repFreqId;
   }
   /**
    * Returns the normalTime
    *
    * @return the normalTime
    */
   public java.lang.Integer getNormalTime() {
      return _normalTime;
   }

   /**
    * Sets the normalTime
    *
    * @param normalTime the new normalTime value
    */
   public void setNormalTime(java.lang.Integer normalTime) {
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
      return errors;
   }

	public Integer[] getDataRangeIds() {
		return _dataRangeIds;
	}
	
	public void setDataRangeIds(Integer[] rangeIds) {
		_dataRangeIds = rangeIds;
	}
	
	public Integer[] getDelayTimes() {
		return _delayTimes;
	}
	
	public void setDelayTimes(Integer[] times) {
		_delayTimes = times;
	}
	
	public Integer[] getNormalTimes() {
		return _normalTimes;
	}
	
	public void setNormalTimes(Integer[] times) {
		_normalTimes = times;
	}
	
	public Integer[] getRepFreqIds() {
		return _repFreqIds;
	}
	
	public void setRepFreqIds(Integer[] freqIds) {
		_repFreqIds = freqIds;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

    public String getDataRgDesc() {
        return dataRgDesc;
    }

    public void setDataRgDesc(String dataRgDesc) {
        this.dataRgDesc = dataRgDesc;
    }

    public String getRepFreqName() {
        return repFreqName;
    }

    public void setRepFreqName(String repFreqName) {
        this.repFreqName = repFreqName;
    }

	public Integer getOATId() {
		return OATId;
	}

	public void setOATId(Integer id) {
		OATId = id;
	}
}
