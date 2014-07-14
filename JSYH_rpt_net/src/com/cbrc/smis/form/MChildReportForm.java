
package com.cbrc.smis.form;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
/**
 * Form for MChildReport.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 *
 * @struts.form name="MChildReportForm"
 */
public class MChildReportForm extends ActionForm {
   /**
    * 报表类型-点对点式
    */
   public final static Integer REPORT_STYLE_DD=new Integer(1);
   /**
    * 报表类型-清单式
    */
   public final static Integer REPORT_STYLE_QD=new Integer(2);
   
   private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

   private java.lang.String _childRepId = null;
   private java.lang.String _versionId = null;
   private java.lang.String _reportName = null;
   private java.lang.String _startDate = null;
   private java.lang.String _endDate = null;
   private java.lang.String _formatTempId = null;
   private java.lang.Integer _repId = null;
   private java.lang.Integer _curUnit = null;
   private java.lang.Integer _isPublic = null;
   private java.lang.Integer _repRangeFlag = null;
   private java.lang.Integer _actuRepFlag = null;
   private java.lang.String  templateType=null;
   private String priorityFlag;
   private String isReport;

   /**
    * 报表类别ID
    */
   private java.lang.Integer _repTypeId = null;
   /**
    * 代表的类型：点对点式或清单式
    */
   private Integer reportStyle=null;
   /**
    * 报表模板上传文件
    */
   private FormFile templateFile=null;
   
   private FormFile qdreportFile=null;
   /**
    * 临时文件的名称
    */
   private String tmpFileName=null;
   /**
    * 报表标题
    */
   private String reportTitle=null;
   /**
    * 货币单位名称
    */
   private String reportCurUnit="";
   
   /**
    * 报表类型名
    */
   private String _repTypeName = null;
   /**
    * 货币单位名称
    */
   private String _curUnitName = null;
   
   private String bak2 = null;
   /**拼接汇总时对应的主报表编号*/
   private String joinTemplateId = null;
   
   /**
    * Standard constructor.
    */   
   public MChildReportForm() {
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
      //--------------------
      // gongming 2008-07-26
      if(null != _versionId)
          _versionId = _versionId.trim();
      //--------------------
   }
   /**
    * Returns the reportName
    *
    * @return the reportName
    */
   public java.lang.String getReportName() {
      return _reportName;
   }

   /**
    * Sets the reportName
    *
    * @param reportName the new reportName value
    */
   public void setReportName(java.lang.String reportName) {
      _reportName = reportName;
      if(null != _reportName)
          _reportName = _reportName.trim();
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
    * Returns the formatTempId
    *
    * @return the formatTempId
    */
   public java.lang.String getFormatTempId() {
      return _formatTempId;
   }

   /**
    * Sets the formatTempId
    *
    * @param formatTempId the new formatTempId value
    */
   public void setFormatTempId(java.lang.String formatTempId) {
      _formatTempId = formatTempId;
   }
   /**
    * Returns the repId
    *
    * @return the repId
    */
   public java.lang.Integer getRepId() {
      return _repId;
   }

   /**
    * Sets the repId
    *
    * @param repId the new repId value
    */
   public void setRepId(java.lang.Integer repId) {
      _repId = repId;
   }
   /**
    * Returns the curUnit
    *
    * @return the curUnit
    */
   public java.lang.Integer getCurUnit() {
      return _curUnit;
   }

   /**
    * Sets the curUnit
    *
    * @param curUnit the new curUnit value
    */
   public void setCurUnit(java.lang.Integer curUnit) {
      _curUnit = curUnit;
   }
   /**
    * Returns the isPublic
    *
    * @return the isPublic
    */
   public java.lang.Integer getIsPublic() {
      return _isPublic;
   }

   /**
    * Sets the isPublic
    *
    * @param isPublic the new isPublic value
    */
   public void setIsPublic(java.lang.Integer isPublic) {
	   _isPublic = isPublic;
   }
   /**
    * Returns the repRangeFlag
    *
    * @return the repRangeFlag
    */
   public java.lang.Integer getRepRangeFlag() {
      return _repRangeFlag;
   }

   /**
    * Sets the repRangeFlag
    *
    * @param repRangeFlag the new repRangeFlag value
    */
   public void setRepRangeFlag(java.lang.Integer repRangeFlag) {
      _repRangeFlag = repRangeFlag;
   }
   /**
    * Returns the actuRepFlag
    *
    * @return the actuRepFlag
    */
   public java.lang.Integer getActuRepFlag() {
      return _actuRepFlag;
   }

   /**
    * Sets the actuRepFlag
    *
    * @param actuRepFlag the new actuRepFlag value
    */
   public void setActuRepFlag(java.lang.Integer actuRepFlag) {
      _actuRepFlag = actuRepFlag;
   }

   public Integer getReportStyle() {
		return reportStyle;
	}

	public void setReportStyle(Integer reportStyle) {
		this.reportStyle = reportStyle;
	}

   public void reset(ActionMapping mapping,HttpServletRequest request){
	   _childRepId = null;
	   _versionId = null;
	   _reportName = null;
	   _startDate = null;
	   _endDate = null;
	   _formatTempId = null;
	   _repId = null;
	    _curUnit = null;
	   _isPublic = null;
	   _repRangeFlag = null;
	   _actuRepFlag = null;	  
	   _repTypeId = null;
	   reportStyle=null;	   
	   templateFile=null;
	   tmpFileName=null;
	   reportTitle=null;
	   reportCurUnit=null;
	   templateType=null;	      
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
      
      // TODO test format/data
      return errors;
   }

	public java.lang.Integer getRepTypeId() {
		return _repTypeId;
	}
	
	public void setRepTypeId(java.lang.Integer typeId) {
		_repTypeId = typeId;
	}
	
	public FormFile getTemplateFile() {
		return templateFile;
	}
	
	public void setTemplateFile(FormFile templateFile) {
		this.templateFile = templateFile;
	}

	public String getTmpFileName() {
		return tmpFileName;
	}

	public void setTmpFileName(String tmpFileName) {
		this.tmpFileName = tmpFileName;
	}

	public String getReportCurUnit() {
		return reportCurUnit;
	}

	public void setReportCurUnit(String reportCurUnit) {
		this.reportCurUnit = reportCurUnit;
	}

	public String getReportTitle() {
		return reportTitle;
	}

	public void setReportTitle(String reportTitle) {
		this.reportTitle = reportTitle;
	}

	
    public String getCurUnitName() {
        return _curUnitName;
    }

    public void setCurUnitName(String unitName) {
        _curUnitName = unitName;
    }

    public String getRepTypeName() {
        return _repTypeName;
    }

    public void setRepTypeName(String typeName) {
        _repTypeName = typeName;
    }

	public java.lang.String getTemplateType() {
		return templateType;
	}

	public void setTemplateType(java.lang.String templateType) {
		this.templateType = templateType;
	}

	public String getPriorityFlag() {
		return priorityFlag;
	}

	public void setPriorityFlag(String priorityFlag) {
		this.priorityFlag = priorityFlag;
	}

	public String getIsReport() {
		return isReport;
	}

	public void setIsReport(String isReport) {
		this.isReport = isReport;
	}

	public String getBak2() {
		return bak2;
	}

	public void setBak2(String bak2) {
		this.bak2 = bak2;
	}

	public FormFile getQdreportFile() {
		return qdreportFile;
	}

	public void setQdreportFile(FormFile qdreportFile) {
		this.qdreportFile = qdreportFile;
	}

	public String getJoinTemplateId() {
		return joinTemplateId;
	}

	public void setJoinTemplateId(String joinTemplateId) {
		this.joinTemplateId = joinTemplateId;
	}
	
	
}
