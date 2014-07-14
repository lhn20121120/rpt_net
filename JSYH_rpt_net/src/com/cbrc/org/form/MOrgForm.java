package com.cbrc.org.form;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * MOrg Form
 * 
 * @author 姚捷
 * 
 * @struts.form name="MOrgForm"
 */
public class MOrgForm extends ActionForm {

    private final static SimpleDateFormat FORMAT = new SimpleDateFormat(
            "dd.MM.yyyy hh:mm:ss");

    /**
     * 机构id
     */
    private java.lang.String _orgId = null;

    /**
     * 机构名称
     */
    private java.lang.String _orgName = null;

    /**
     * 机构类型
     */
    private java.lang.String _orgType = null;

    /**
     * 是同法人
     */
    private java.lang.String _isCorp = null;

    /**
     * 机构分类id
     */
    private java.lang.String _orgClsId = null;

    /**
     * 机构分类名称
     */
    private String _orgClsName = null;

    /**
     * 机构编码
     */
    private java.lang.String _orgCode = null;

    /**
     * 选择的机构类型id列表
     */
    private String[] _selectOrgIds = null;

    /**
     * 该页所有的机构id
     */
    private String[] _orgIds = null;

    /**
     * 当前页
     */
    private String curPage = null;

    private String _versionId = null;

    private String _childRepId = null;

    private String _reportName = null;

    private String reportStyle = null;

    public String getReportStyle() {
        return reportStyle;
    }

    public void setReportStyle(String reportStyle) {
        this.reportStyle = reportStyle;
    }

    public String getReportName() {
        return _reportName;
    }

    public void setReportName(String name) {
        _reportName = name;
    }

    public String getChildRepId() {
        return _childRepId;
    }

    public void setChildRepId(String repId) {
        _childRepId = repId;
    }

    public String getVersionId() {
        return _versionId;
    }

    public void setVersionId(String id) {
        _versionId = id;
    }

    /**
     * Standard constructor.
     */
    public MOrgForm() {
    }
/**
    * Returns the orgId
    *
    * @return the orgId
    */
   public java.lang.String getOrgId() {
      return _orgId==null?null:_orgId.trim();
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
    * Returns the orgName
    *
    * @return the orgName
    */
   public java.lang.String getOrgName() {
      return _orgName;
   }

   /**
    * Sets the orgName
    *
    * @param orgName the new orgName value
    */
   public void setOrgName(java.lang.String orgName) {
      _orgName = orgName;
   }
   /**
    * Returns the orgType
    *
    * @return the orgType
    */
   public java.lang.String getOrgType() {
      return _orgType;
   }

   /**
    * Sets the orgType
    *
    * @param orgType the new orgType value
    */
   public void setOrgType(java.lang.String orgType) {
      _orgType = orgType;
   }
   /**
    * Returns the isCorp
    *
    * @return the isCorp
    */
   public java.lang.String getIsCorp() {
      return _isCorp;
   }

   /**
    * Sets the isCorp
    *
    * @param isCorp the new isCorp value
    */
   public void setIsCorp(java.lang.String isCorp) {
      _isCorp = isCorp;
   }
   /**
    * Returns the orgClsId
    *
    * @return the orgClsId
    */
   public java.lang.String getOrgClsId() {
      return _orgClsId;
   }

   /**
    * Sets the orgClsId
    *
    * @param orgClsId the new orgClsId value
    */
   public void setOrgClsId(java.lang.String orgClsId) {
      _orgClsId = orgClsId;
   }
   /**
    * Returns the orgCode
    *
    * @return the orgCode
    */
   public java.lang.String getOrgCode() {
      return _orgCode;
   }

   /**
    * Sets the orgCode
    *
    * @param orgCode the new orgCode value
    */
   public void setOrgCode(java.lang.String orgCode) {
      _orgCode = orgCode;
   }
   
   
   
   public String[] getOrgIds() {
       return _orgIds;
   }


   public void setOrgIds(String[] orgIds) {
       this._orgIds = orgIds;
   }


   public String[] getSelectOrgIds() {
       return _selectOrgIds;
   }


   public void setSelectOrgIds(String[] orgIds) {
       _selectOrgIds = orgIds;
   }
   
   public String getOrgClsName() {
       return _orgClsName;
   }


   public void setOrgClsName(String orgClsName) {
       _orgClsName = orgClsName;
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



   public void reset(ActionMapping arg0, HttpServletRequest arg1) {
	// TODO 自动生成方法存根
	//this.setStrArray(null);
	
   }

    public String getCurPage() {
        return curPage;
    }

    public void setCurPage(String curPage) {
        this.curPage = curPage;
    }

}
