
package com.cbrc.smis.form;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.cbrc.smis.util.FitechUtil;

/**
 * Form for reportIn.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 *
 * @struts.form name="reportInForm"
 */
public class NewReportInForm extends ActionForm {

	 private final static SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-mm-dd");

	   private java.lang.Integer _repInId = null;
	   private java.lang.String _childRepId = null;
	   private java.lang.Integer _dataRangeId = null;
	   private java.lang.Integer _term = null;
	   private java.lang.Integer _times = null;
	   private java.lang.String _orgId = null;
	   private java.lang.Integer _year = null;
	   private java.lang.Short _tblOuterValidateFlag = null;
	   private java.lang.Short _reportDataWarehouseFlag = null;
	   private java.lang.Short _tblInnerValidateFlag = null;
	   private java.lang.String _repName = null;
	   private java.lang.Short _checkFlag = null;
	   private java.lang.Integer _package = null;
	   private java.lang.Integer _curId = null;
	   private java.lang.String _versionId = null;
	   private Date _reportDate = null;
	   private java.lang.Short _abmormityChangeFlag = null;
	   private java.lang.Short _repRangeFlag = null;
	   private java.lang.Short _forseReportAgainFlag = null;
	   private java.lang.Short _laterReportDay = null;
	   private java.lang.Short _notReportFlag = null;
	   private java.lang.String _writer = null;
	   private java.lang.String _checker = null;
	   private java.lang.String _principal = null;
	   private java.lang.String _tblOuterInvalidateCause = null;

	   private java.lang.String _startDate= null;
	   private java.lang.String _endDate = null;
	   private java.lang.String _reportId=null;
	   private String _orgClsId=null;
	   private Integer rep_freq_id=null; 
	   //页面各异常状态的单选框数组
	   private String _allFlags=null;
	   //查找后的列表显示的记录中的机构id的隐藏域数组
	   private Integer[] _repInIdArray=null;
	   //存放用户的选择的下拉审核状态标志的数组
	   private Short[] _checkSignOption=null;
	   //报送机构的名称
	   private String _orgName=null;
	   //重报原因
	   private String cause=null;

	   private String setDate=null;
	   
	   //当前页的数
	   private String _curPage=null;

	   /**外网实际数据表ID**/
	   private Integer repOutId=null;
	   private Integer[] repOutIds=null;
	   
		public Integer[] getRepOutIds() {
			return repOutIds;
		}
		public void setRepOutIds(Integer[] repOutIds) {
			this.repOutIds = repOutIds;
		}
		public Integer getRepOutId() {
			return repOutId;
		}
		public void setRepOutId(Integer repOutId) {
			this.repOutId = repOutId;
		}
		public String getCurPage() {
			return _curPage;
		}
		public void setCurPage(String page) {
			_curPage = page;
		}
		public String getSetDate() {
		//	Date _formatStartDate = FitechUtil.formatToTimestamp(this.setDate);
		//	   return setDate == null ? null : FORMAT1.format(_formatStartDate);
			return this.setDate;//c曹发根
		}
		public Date getSetDateasDate(){
			return this.setDate!=null?FitechUtil.formatToTimestamp(this.setDate):new Date();
		}
		
		public void setSetDate(String setDate) {
			this.setDate = setDate;
		}
		
		
		public String getCause() {
			return cause;
		}
		
		public void setCause(String cause) {
			this.cause = cause;
		}
		
		public Short[] getCheckSignOption() {
			return _checkSignOption;
		}
		
		public void setCheckSignOption(Short[] checkSignOption) {
			_checkSignOption = checkSignOption;
		}
		
		public Integer[] getRepInIdArray() {
			return _repInIdArray;
		}
		
		public void setRepInIdArray(Integer[] repInIdArray) {
			_repInIdArray = repInIdArray;
		}
		
		public String getAllFlags() {
			return _allFlags;
		}
		
		public void setAllFlags(String allFlags) {
			_allFlags = allFlags;
		}
		
		public String getOrgClsId() {
			return _orgClsId;
		}
		
		public void setOrgClsId(String clsId) {
			_orgClsId = clsId;
		}
		
		public java.lang.String getReportId() {
			return _reportId;
		}
		
		public void setReportId(java.lang.String reportId) {
			_reportId = reportId;
		}
		
		/**
	    * Standard constructor.
	    */
	   public NewReportInForm() {
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
	    * Returns the term
	    *
	    * @return the term
	    */
	   public java.lang.Integer getTerm() {
	      return _term;
	   }

	   /**
	    * Sets the term
	    *
	    * @param term the new term value
	    */
	   public void setTerm(java.lang.Integer term) {
	      _term = term;
	   }
	   /**
	    * Returns the times
	    *
	    * @return the times
	    */
	   public java.lang.Integer getTimes() {
	      return _times;
	   }

	   /**
	    * Sets the times
	    *
	    * @param times the new times value
	    */
	   public void setTimes(java.lang.Integer times) {
	      _times = times;
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
	    * Returns the year
	    *
	    * @return the year
	    */
	   public java.lang.Integer getYear() {
	      return _year;
	   }

	   /**
	    * Sets the year
	    *
	    * @param year the new year value
	    */
	   public void setYear(java.lang.Integer year) {
	      _year = year;
	   }
	   /**
	    * Returns the tblOuterValidateFlag
	    *
	    * @return the tblOuterValidateFlag
	    */
	   public java.lang.Short getTblOuterValidateFlag() {
	      return _tblOuterValidateFlag;
	   }

	   /**
	    * Sets the tblOuterValidateFlag
	    *
	    * @param tblOuterValidateFlag the new tblOuterValidateFlag value
	    */
	   public void setTblOuterValidateFlag(java.lang.Short tblOuterValidateFlag) {
	      _tblOuterValidateFlag = tblOuterValidateFlag;
	   }
	   /**
	    * Returns the reportDataWarehouseFlag
	    *
	    * @return the reportDataWarehouseFlag
	    */
	   public java.lang.Short getReportDataWarehouseFlag() {
	      return _reportDataWarehouseFlag;
	   }

	   /**
	    * Sets the reportDataWarehouseFlag
	    *
	    * @param reportDataWarehouseFlag the new reportDataWarehouseFlag value
	    */
	   public void setReportDataWarehouseFlag(java.lang.Short reportDataWarehouseFlag) {
	      _reportDataWarehouseFlag = reportDataWarehouseFlag;
	   }
	   /**
	    * Returns the tblInnerValidateFlag
	    *
	    * @return the tblInnerValidateFlag
	    */
	   public java.lang.Short getTblInnerValidateFlag() {
	      return _tblInnerValidateFlag;
	   }

	   /**
	    * Sets the tblInnerValidateFlag
	    *
	    * @param tblInnerValidateFlag the new tblInnerValidateFlag value
	    */
	   public void setTblInnerValidateFlag(java.lang.Short tblInnerValidateFlag) {
	      _tblInnerValidateFlag = tblInnerValidateFlag;
	   }
	   /**
	    * Returns the repName
	    *
	    * @return the repName
	    */
	   public java.lang.String getRepName() {
	      return _repName!=null?_repName.trim():null;
	   }

	   /**
	    * Sets the repName
	    *
	    * @param repName the new repName value
	    */
	   public void setRepName(java.lang.String repName) {
	      _repName = repName;
	   }
	   /**
	    * Returns the checkFlag
	    *
	    * @return the checkFlag
	    */
	   public java.lang.Short getCheckFlag() {
	      return _checkFlag;
	   }

	   /**
	    * Sets the checkFlag
	    *
	    * @param checkFlag the new checkFlag value
	    */
	   public void setCheckFlag(java.lang.Short checkFlag) {
	      _checkFlag = checkFlag;
	   }
	   /**
	    * Returns the package
	    *
	    * @return the package
	    */
	   public java.lang.Integer getPackage() {
	      return _package;
	   }

	   /**
	    * Sets the package
	    *
	    * @param package the new package value
	    */
	   public void setPackage(java.lang.Integer paramPackage) {
	      _package = paramPackage;
	   }
	   /**
	    * Returns the curId
	    *
	    * @return the curId
	    */
	   public java.lang.Integer getCurId() {
	      return _curId;
	   }

	   /**
	    * Sets the curId
	    *
	    * @param curId the new curId value
	    */
	   public void setCurId(java.lang.Integer curId) {
	      _curId = curId;
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
	    * Returns the reportDate
	    *
	    * @return the reportDate
	    */
	   public Date  getReportDate() {
		   
		   return  _reportDate;
	   }

	   /**
	    * Sets the reportDate
	    *
	    * @param reportDate the new reportDate value
	    */
	   public void setReportDate(Date  reportDate) {
	      _reportDate = reportDate;
	   }
	   /**
	    * Returns the abmormityChangeFlag
	    *
	    * @return the abmormityChangeFlag
	    */
	   public java.lang.Short getAbmormityChangeFlag() {
	      return _abmormityChangeFlag;
	   }

	   /**
	    * Sets the abmormityChangeFlag
	    *
	    * @param abmormityChangeFlag the new abmormityChangeFlag value
	    */
	   public void setAbmormityChangeFlag(java.lang.Short abmormityChangeFlag) {
	      _abmormityChangeFlag = abmormityChangeFlag;
	   }
	   /**
	    * Returns the repRangeFlag
	    *
	    * @return the repRangeFlag
	    */
	   public java.lang.Short getRepRangeFlag() {
	      return _repRangeFlag;
	   }

	   /**
	    * Sets the repRangeFlag
	    *
	    * @param repRangeFlag the new repRangeFlag value
	    */
	   public void setRepRangeFlag(java.lang.Short repRangeFlag) {
	      _repRangeFlag = repRangeFlag;
	   }
	   /**
	    * Returns the forseReportAgainFlag
	    *
	    * @return the forseReportAgainFlag
	    */
	   public java.lang.Short getForseReportAgainFlag() {
	      return _forseReportAgainFlag;
	   }

	   /**
	    * Sets the forseReportAgainFlag
	    *
	    * @param forseReportAgainFlag the new forseReportAgainFlag value
	    */
	   public void setForseReportAgainFlag(java.lang.Short forseReportAgainFlag) {
	      _forseReportAgainFlag = forseReportAgainFlag;
	   }
	   /**
	    * Returns the laterReportDay
	    *
	    * @return the laterReportDay
	    */
	   public java.lang.Short getLaterReportDay() {
	      return _laterReportDay;
	   }

	   /**
	    * Sets the laterReportDay
	    *
	    * @param laterReportDay the new laterReportDay value
	    */
	   public void setLaterReportDay(java.lang.Short laterReportDay) {
	      _laterReportDay = laterReportDay;
	   }
	   /**
	    * Returns the notReportFlag
	    *
	    * @return the notReportFlag
	    */
	   public java.lang.Short getNotReportFlag() {
	      return _notReportFlag;
	   }

	   /**
	    * Sets the notReportFlag
	    *
	    * @param notReportFlag the new notReportFlag value
	    */
	   public void setNotReportFlag(java.lang.Short notReportFlag) {
	      _notReportFlag = notReportFlag;
	   }
	   /**
	    * Returns the writer
	    *
	    * @return the writer
	    */
	   public java.lang.String getWriter() {
	      return _writer;
	   }

	   /**
	    * Sets the writer
	    *
	    * @param writer the new writer value
	    */
	   public void setWriter(java.lang.String writer) {
	      _writer = writer;
	   }
	   /**
	    * Returns the checker
	    *
	    * @return the checker
	    */
	   public java.lang.String getChecker() {
	      return _checker;
	   }

	   /**
	    * Sets the checker
	    *
	    * @param checker the new checker value
	    */
	   public void setChecker(java.lang.String checker) {
	      _checker = checker;
	   }
	   /**
	    * Returns the principal
	    *
	    * @return the principal
	    */
	   public java.lang.String getPrincipal() {
	      return _principal;
	   }

	   /**
	    * Sets the principal
	    *
	    * @param principal the new principal value
	    */
	   public void setPrincipal(java.lang.String principal) {
	      _principal = principal;
	   }
	   /**
	    * Returns the tblOuterInvalidateCause
	    *
	    * @return the tblOuterInvalidateCause
	    */
	   public java.lang.String getTblOuterInvalidateCause() {
	      return _tblOuterInvalidateCause;
	   }

	   /**
	    * Sets the tblOuterInvalidateCause
	    *
	    * @param tblOuterInvalidateCause the new tblOuterInvalidateCause value
	    */
	   public void setTblOuterInvalidateCause(java.lang.String tblOuterInvalidateCause) {
	      _tblOuterInvalidateCause = tblOuterInvalidateCause;
	   }

	   public void reset(ActionMapping mapping,HttpServletRequest request){
		   this._year=null;
		   this._term=null;
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

		public java.lang.String getEndDate() {
			Date _formatEndDate = FitechUtil.formatToTimestamp(this._endDate);
			   return _formatEndDate == null ? null : FORMAT.format(_formatEndDate);
		}
		
		public void setEndDate(java.lang.String endDate) {
			_endDate = endDate;
		}
		
		public java.lang.String getStartDate() {
			Date _formatStartDate = FitechUtil.formatToTimestamp(this._startDate);
			   return _formatStartDate == null ? null : FORMAT.format(_formatStartDate);
		}
		
		public void setStartDate(java.lang.String startDate) {
			_startDate = startDate;
		}
		
		public String getOrgName() {
			return _orgName!=null?_orgName.trim():null;
		}
		
		public void setOrgName(String name) {
			_orgName = name;
		}
		public Integer getRep_freq_id() {
			return rep_freq_id;
		}
		public void setRep_freq_id(Integer rep_freq_id) {
			this.rep_freq_id = rep_freq_id;
		}
		
}