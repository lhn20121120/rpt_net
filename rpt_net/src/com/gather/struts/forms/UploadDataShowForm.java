package com.gather.struts.forms;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import com.gather.refer.data.DataStateMaker;

public class UploadDataShowForm extends ActionForm {
	
	private String reportId="";     //for show hidden
	private String reportName="";   //for show
	private String version="";      //for show
	private Integer dataRange;      //
	private String dataRangeName="";  //for show 
	private Integer frequency;      // 
	private String freqName="";     //for show
	
	private Date setDate;           //需要上报的时间,即查询的月份日期
	private int year;               //查询的时间 (年)
	private Integer terms;          //查询的时间 (期数) for show
	private String orgId;           //机构id
	private String orgName="";     //机构名称 for show
	
	private Integer normalDays;     //正常延迟天数
	private Integer delayDays;      //扩展延迟天数
	
	private Integer realSubReportId;  //实际子报表id  for show hidden
	private Date referedDate;    //已经提交日期
	private String reportFlag;       //重报标志
	private Integer repState;         //报表的验证状态 
	
	private String state="";     //for show
	
	private int ifReferedFlag;  // 0 没有提交 ；1 已经提交
	
	
	private FormFile referFile=null;   //for page to show upload file
    

	/**
	 * @return Returns the referFile.
	 */
	public FormFile getReferFile() {
		return referFile;
	}
	/**
	 * @param referFile The referFile to set.
	 */
	public void setReferFile(FormFile referFile) {
		this.referFile = referFile;
	}
	/**
	 * @return Returns the delayDays.
	 */
	public Integer getDelayDays() {
		return delayDays;
	}
	/**
	 * @param delayDays The delayDays to set.
	 */
	public void setDelayDays(Integer delayDays) {
		this.delayDays = delayDays;
	}
	/**
	 * @return Returns the normalDays.
	 */
	public Integer getNormalDays() {
		return normalDays;
	}
	/**
	 * @param normalDays The normalDays to set.
	 */
	public void setNormalDays(Integer normalDays) {
		this.normalDays = normalDays;
	}
	/**
	 * @return Returns the setDate.
	 */
	public Date getSetDate() {
		return setDate;
	}
	/**
	 * @param setDate The setDate to set.
	 */
	public void setSetDate(Date setDate) {
		this.setDate = setDate;
	}
	public UploadDataShowForm(){}
	public UploadDataShowForm(String reportId,String reportName,String version,Integer dataRange,String dataRangeName,Integer frequency,String freqName,Integer realSubReportId,String state,Date referedDate,int ifReferedFlag,String reportFlag){
		this.dataRange=dataRange;
		this.dataRangeName=dataRangeName;
		this.freqName=freqName;
		this.frequency=frequency;
		this.realSubReportId=realSubReportId;
		this.reportId=reportId;
		this.reportName=reportName;
		this.state=state;
		this.version=version;
		this.referedDate=referedDate;
		this.ifReferedFlag=ifReferedFlag;
		this.reportFlag=reportFlag;
		setMyState();
	}
	
	
	public void setMyState(){
		DataStateMaker.make(this);
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	/**
	 * @return Returns the dataRange.
	 */
	public Integer getDataRange() {
		return dataRange;
	}

	/**
	 * @param dataRange The dataRange to set.
	 */
	public void setDataRange(Integer dataRange) {
		this.dataRange = dataRange;
	}

	/**
	 * @return Returns the frequency.
	 */
	public Integer getFrequency() {
		return frequency;
	}

	/**
	 * @param frequency The frequency to set.
	 */
	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}

	/**
	 * @return Returns the realSubReportId.
	 */
	public Integer getRealSubReportId() {
		return realSubReportId;
	}

	/**
	 * @param realSubReportId The realSubReportId to set.
	 */
	public void setRealSubReportId(Integer realSubReportId) {
		this.realSubReportId = realSubReportId;
	}

	/**
	 * @return Returns the reportId.
	 */
	public String getReportId() {
		return reportId;
	}

	/**
	 * @param reportId The reportId to set.
	 */
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	/**
	 * @return Returns the reportName.
	 */
	public String getReportName() {
		return reportName;
	}

	/**
	 * @param reportName The reportName to set.
	 */
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	/**
	 * @return Returns the state.
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state The state to set.
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return Returns the version.
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version The version to set.
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	/**
	 * @return Returns the dataRangeName.
	 */
	public String getDataRangeName() {
		return dataRangeName;
	}
	/**
	 * @param dataRangeName The dataRangeName to set.
	 */
	public void setDataRangeName(String dataRangeName) {
		this.dataRangeName = dataRangeName;
	}
	/**
	 * @return Returns the freqName.
	 */
	public String getFreqName() {
		return freqName;
	}
	/**
	 * @param freqName The freqName to set.
	 */
	public void setFreqName(String freqName) {
		this.freqName = freqName;
	}
	/**
	 * @return Returns the ifReferedFlag.
	 */
	public int getIfReferedFlag() {
		return ifReferedFlag;
	}
	/**
	 * @param ifReferedFlag The ifReferedFlag to set.
	 */
	public void setIfReferedFlag(int ifReferedFlag) {
		this.ifReferedFlag = ifReferedFlag;
	}
	/**
	 * @return Returns the referedDate.
	 */
	public Date getReferedDate() {
		return referedDate;
	}
	/**
	 * @param referedDate The referedDate to set.
	 */
	public void setReferedDate(Date referedDate) {
		this.referedDate = referedDate;
	}
	/**
	 * @return Returns the reportFlag.
	 */
	public String getReportFlag() {
		return reportFlag;
	}
	/**
	 * @param reportFlag The reportFlag to set.
	 */
	public void setReportFlag(String reportFlag) {
		this.reportFlag = reportFlag;
	}
	/**
	 * @return Returns the repState.
	 */
	public Integer getRepState() {
		return repState;
	}
	/**
	 * @param repState The repState to set.
	 */
	public void setRepState(Integer repState) {
		this.repState = repState;
	}
	
	
	   public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		      ActionErrors errors = new ActionErrors();
		      return errors;
		   }
	/**
	 * @return Returns the terms.
	 */
	public Integer getTerms() {
		return terms;
	}
	/**
	 * @param terms The terms to set.
	 */
	public void setTerms(Integer terms) {
		this.terms = terms;
	}
	/**
	 * @return Returns the orgName.
	 */
	public String getOrgName() {
		return orgName;
	}
	/**
	 * @param orgName The orgName to set.
	 */
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	/**
	 * @return Returns the orgId.
	 */
	public String getOrgId() {
		return orgId;
	}
	/**
	 * @param orgId The orgId to set.
	 */
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	/**
	 * @return Returns the year.
	 */
	public int getYear() {
		return year;
	}
	/**
	 * @param year The year to set.
	 */
	public void setYear(int year) {
		this.year = year;
	}
}
