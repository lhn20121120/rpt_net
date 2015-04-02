package com.gather.struts.forms;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

public class HandleReferFileForm extends ActionForm {
	
	private FormFile referFile=null;
	
	private String reportId="";     //for show hidden
	private String reportName="";   //for show
	private String version="";      //for show
	private Integer dataRange;      //
	private String dataRangeName="";  //for show 
	private Integer frequency;      // 
	private String freqName="";     //for show
	
	private Date setDate;           //��Ҫ�ϱ���ʱ��,����ѯ���·�����
	private int year;               //��ѯ��ʱ�� (��)
	private Integer terms;          //��ѯ��ʱ�� (����) for show
	private String orgId;           //����id
	private String orgName="";     //�������� for show
	
	private Integer normalDays;     //�����ӳ�����
	private Integer delayDays;      //��չ�ӳ�����
	
	private Integer realSubReportId;  //ʵ���ӱ���id  for show hidden
	private Date referedDate;    //�Ѿ��ύ����
	private Integer reportFlag;       //�ر���־
	private Integer repState;         //�������֤״̬ 
	
	private String state="";     //for show
	private String stateMsg="";
	private int ifCheckFlag;  // 0 û���ύ ��1 �Ѿ��ύ

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
	 * @return Returns the ifReferedFlag.
	 */
	public int getIfCheckFlag() {
		return ifCheckFlag;
	}

	/**
	 * @param ifReferedFlag The ifReferedFlag to set.
	 */
	public void setIfCheckFlag(int ifCheckFlag) {
		this.ifCheckFlag = ifCheckFlag;
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
	 * @return Returns the reportFlag.
	 */
	public Integer getReportFlag() {
		return reportFlag;
	}

	/**
	 * @param reportFlag The reportFlag to set.
	 */
	public void setReportFlag(Integer reportFlag) {
		this.reportFlag = reportFlag;
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

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		      ActionErrors errors = new ActionErrors();
		      return errors;
	   }

	/**
	 * @return Returns the stateMsg.
	 */
	public String getStateMsg() {
		return stateMsg;
	}

	/**
	 * @param stateMsg The stateMsg to set.
	 */
	public void setStateMsg(String stateMsg) {
		this.stateMsg = stateMsg;
	}
}
