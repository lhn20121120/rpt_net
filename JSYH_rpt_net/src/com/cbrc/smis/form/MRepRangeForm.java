
package com.cbrc.smis.form;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form for MRepRange.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 *
 * @struts.form name="MRepRangeForm"
 */
public class MRepRangeForm extends ActionForm {

   private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

   private java.lang.String _orgId = null;

   private java.lang.String _childRepId = null;
   private java.lang.String _versionId = null;
   private java.lang.String _strArray[]=new String[0];
   private String[] _childRepIds=null;
   private String[] _versionIds=null;
   private String[] _selectOrgIds=null;
   private String _reportName=null;
   
   private Integer reportStyle = null;
   
   private String selectRepIds = null;
   private String selectRepList = null;

   /**
    * 机构分类id
    */
   private String orgClsId = null;
   /**
    * 机构分类名称
    */
   private String orgClsName = null;
   
   /**
    * Standard constructor.
    */
   public MRepRangeForm() {
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

    public java.lang.String[] getStrArray() {
    	return _strArray;
    }
    
    public void setStrArray(java.lang.String[] strArray) {
    	_strArray = strArray;
    }
    
    public String getOrgClsId() {
        return orgClsId;
    }
    
    public void setOrgClsId(String orgClsId) {
        this.orgClsId = orgClsId;
    }

    public String getOrgClsName() {
        return orgClsName;
    }
    
    public void setOrgClsName(String orgClsName) {
        this.orgClsName = orgClsName;
    }

	public String[] getChildRepIds() {
		return _childRepIds;
	}

	public void setChildRepIds(String[] repIds) {
		_childRepIds = repIds;
	}

	public String[] getVersionIds() {
		return _versionIds;
	}

	public void setVersionIds(String[] ids) {
		_versionIds = ids;
	}

	public String[] getSelectOrgIds() {
		return _selectOrgIds;
	}

	public void setSelectOrgIds(String[] orgIds) {
		_selectOrgIds = orgIds;
	}

	public String getReportName() {
		return _reportName;
	}

	public void setReportName(String name) {
		_reportName = name;
	}

    public Integer getReportStyle() {
        return reportStyle;
    }

    public void setReportStyle(Integer reportStyle) {
        this.reportStyle = reportStyle;
    }

	public String getSelectRepIds() {
		return selectRepIds;
	}

	public void setSelectRepIds(String selectRepIds) {
		this.selectRepIds = selectRepIds;
	}
	
	public String getSelectRepList() {
		return selectRepList;
	}

	public void setSelectRepList(String selectRepList) {
		this.selectRepList = selectRepList;
	}	
}
