package com.fitech.net.orgcls.form;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class OrgclsNetForm extends ActionForm{

		   private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

		   private java.lang.String _orgClsId = null;
		   private java.lang.String _orgClsNm = null;
		   private java.lang.String _orgClsType = null;

		   private java.lang.String _reportName=null;

		   /**
		    * 复选框是否选中
		    */
		   private Integer selected=null;
		   
		   /**
		    * 是否选中
		    */
		   private Integer selAll=new Integer(0);

		   /**
		    * 该页得到的子报表id
		    */
		   private java.lang.String _childRepId=null;
		   
		   /**
		    * 该页得到的版本号id
		    */
		   private java.lang.String _versionId=null;
		   /**
		    *该页选择的机构分类id的集合
		    */
		   private String[] _selectOrgClsIds =null;

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
		   public OrgclsNetForm() {
		   }

		   
		 


			public java.lang.String getOrgClsId() {
				return _orgClsId;
			}
			
			
			
			
			
			public void setOrgClsId(java.lang.String orgClsId) {
				_orgClsId = orgClsId;
			}





			/**
		    * Returns the orgClsNm
		    *
		    * @return the orgClsNm
		    */
		   public java.lang.String getOrgClsNm() {
		      return _orgClsNm;
		   }

		   /**
		    * Sets the orgClsNm
		    *
		    * @param orgClsNm the new orgClsNm value
		    */
		   public void setOrgClsNm(java.lang.String orgClsNm) {
		      _orgClsNm = orgClsNm;
		   }
		   /**
		    * Returns the orgClsType
		    *
		    * @return the orgClsType
		    */
		   public java.lang.String getOrgClsType() {
		      return _orgClsType;
		   }

		   /**
		    * Sets the orgClsType
		    *
		    * @param orgClsType the new orgClsType value
		    */
		   public void setOrgClsType(java.lang.String orgClsType) {
		      _orgClsType = orgClsType;
		   }
		   
		   public void reset(ActionMapping mapping, HttpServletRequest request) {
			   _orgClsId = null;
			   _orgClsNm = null;
			   _orgClsType = null;
			   selAll=new Integer(0);
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

			public Integer getSelAll() {
				return selAll;
			}
			
			public void setSelAll(Integer selAll) {
				this.selAll = selAll;
			}
		   
		   public java.lang.String getChildRepId() {
				return _childRepId;
			}


			public void setChildRepId(java.lang.String childRepId) {
				_childRepId = childRepId;
			}


			public java.lang.String getVersionId() {
				return _versionId;
			}


			public void setVersionId(java.lang.String versionId) {
				_versionId = versionId;
			}

			public String[] getSelectOrgClsIds() {
				return _selectOrgClsIds;
			}

			public void setSelectOrgClsIds(String[] selectOrgClsIds) {
				_selectOrgClsIds = selectOrgClsIds;
			}

			public java.lang.String getReportName() {
				return _reportName;
			}

			public void setReportName(java.lang.String reportName) {
				_reportName = reportName;
			}





			public Integer getSelected() {
				return selected;
			}





			public void setSelected(Integer selected) {
				this.selected = selected;
			}

		}



