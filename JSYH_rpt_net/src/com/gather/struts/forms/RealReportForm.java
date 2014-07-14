package com.gather.struts.forms;

import java.util.Set;

import org.apache.struts.action.ActionForm;

public class RealReportForm extends ActionForm{
	
	//�������ֶ�
	   private Integer repId = null;      //id
	   private java.lang.String  repCnName = null;  //����������
	   private java.lang.String  repEnName = null;  //����Ӣ����
	   private java.lang.Integer curUnit = null;   //���ҵ�λ
	   private java.lang.Integer repTypeId = null; //�������
	   
	   private java.lang.String repTypeName = null; //�����������
	   
	 //�ӱ����ֶ�
	   private java.lang.String childRepId = null;   //id
	   private java.lang.String versionId = null;    //�汾��
	   private java.lang.String reportName = null;   //��������
	   private java.util.Date startDate = null;      //��ʼʱ��
	   private java.util.Date endDate = null;       //����ʱ��
	   private java.lang.String formatTempId = null; 
	   private Integer child_repId = null;          //������id
	   private Integer child_curUnit = null;        //���ҵ�λ
	   
	   //ʵ���ӱ���
	   private java.lang.String realVersionId = null; //�汾��
	   private Integer delayTime = null;              //�ӳ�ʱ��
	   private Integer repFreqId = null;              //Ƶ��id
	   private Integer dataRangeId = null;            //���ݷ�Χid
	   private java.lang.String realChildRepId = null; //�ӱ���id
	   private Integer NormalTime = null;            //����ʱ��
	   
	   private String repFreqName = null;            //Ƶ������
	   private String dataRangeName = null;          //���ݷ�Χ����
	   
	   private String orgId="";                    //���ͻ�����ص����� id
	   private String orgName="";                 //���ͻ�����ص����� ����
	   
	   private int year;                          //����ѯ������ (��)
 	   private int month;                         //����ѯ������ (��)
	   
	    /** persistent field */
	    private Set MActuReps;

	    /** persistent field */
	    private Set MRepRanges;

	    /** persistent field */
	    private Set reports;

	    /** persistent field */
	    private Set repRangeDownLogs;
	   
	   
	   
	   
	   
	   

	/**
	 * @return Returns the child_curUnit.
	 */
	public Integer getChild_curUnit() {
		return child_curUnit;
	}







	/**
	 * @param child_curUnit The child_curUnit to set.
	 */
	public void setChild_curUnit(Integer child_curUnit) {
		this.child_curUnit = child_curUnit;
	}







	/**
	 * @return Returns the child_repId.
	 */
	public Integer getChild_repId() {
		return child_repId;
	}







	/**
	 * @param child_repId The child_repId to set.
	 */
	public void setChild_repId(Integer child_repId) {
		this.child_repId = child_repId;
	}







	/**
	 * @return Returns the childRepId.
	 */
	public java.lang.String getChildRepId() {
		return childRepId;
	}







	/**
	 * @param childRepId The childRepId to set.
	 */
	public void setChildRepId(java.lang.String childRepId) {
		this.childRepId = childRepId;
	}







	/**
	 * @return Returns the curUnit.
	 */
	public java.lang.Integer getCurUnit() {
		return curUnit;
	}







	/**
	 * @param curUnit The curUnit to set.
	 */
	public void setCurUnit(java.lang.Integer curUnit) {
		this.curUnit = curUnit;
	}







	/**
	 * @return Returns the dataRangeId.
	 */
	public Integer getDataRangeId() {
		return dataRangeId;
	}







	/**
	 * @param dataRangeId The dataRangeId to set.
	 */
	public void setDataRangeId(Integer dataRangeId) {
		this.dataRangeId = dataRangeId;
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
	 * @return Returns the delayTime.
	 */
	public Integer getDelayTime() {
		return delayTime;
	}







	/**
	 * @param delayTime The delayTime to set.
	 */
	public void setDelayTime(Integer delayTime) {
		this.delayTime = delayTime;
	}







	/**
	 * @return Returns the endDate.
	 */
	public java.util.Date getEndDate() {
		return endDate;
	}







	/**
	 * @param endDate The endDate to set.
	 */
	public void setEndDate(java.util.Date endDate) {
		this.endDate = endDate;
	}







	/**
	 * @return Returns the formatTempId.
	 */
	public java.lang.String getFormatTempId() {
		return formatTempId;
	}







	/**
	 * @param formatTempId The formatTempId to set.
	 */
	public void setFormatTempId(java.lang.String formatTempId) {
		this.formatTempId = formatTempId;
	}







	/**
	 * @return Returns the normalTime.
	 */
	public Integer getNormalTime() {
		return NormalTime;
	}







	/**
	 * @param normalTime The normalTime to set.
	 */
	public void setNormalTime(Integer normalTime) {
		NormalTime = normalTime;
	}







	/**
	 * @return Returns the realChildRepId.
	 */
	public java.lang.String getRealChildRepId() {
		return realChildRepId;
	}







	/**
	 * @param realChildRepId The realChildRepId to set.
	 */
	public void setRealChildRepId(java.lang.String realChildRepId) {
		this.realChildRepId = realChildRepId;
	}







	/**
	 * @return Returns the realVersionId.
	 */
	public java.lang.String getRealVersionId() {
		return realVersionId;
	}







	/**
	 * @param realVersionId The realVersionId to set.
	 */
	public void setRealVersionId(java.lang.String realVersionId) {
		this.realVersionId = realVersionId;
	}







	/**
	 * @return Returns the repCnName.
	 */
	public java.lang.String getRepCnName() {
		return repCnName;
	}







	/**
	 * @param repCnName The repCnName to set.
	 */
	public void setRepCnName(java.lang.String repCnName) {
		this.repCnName = repCnName;
	}







	/**
	 * @return Returns the repEnName.
	 */
	public java.lang.String getRepEnName() {
		return repEnName;
	}







	/**
	 * @param repEnName The repEnName to set.
	 */
	public void setRepEnName(java.lang.String repEnName) {
		this.repEnName = repEnName;
	}







	/**
	 * @return Returns the repFreqId.
	 */
	public Integer getRepFreqId() {
		return repFreqId;
	}







	/**
	 * @param repFreqId The repFreqId to set.
	 */
	public void setRepFreqId(Integer repFreqId) {
		this.repFreqId = repFreqId;
	}







	/**
	 * @return Returns the repFreqName.
	 */
	public String getRepFreqName() {
		return repFreqName;
	}







	/**
	 * @param repFreqName The repFreqName to set.
	 */
	public void setRepFreqName(String repFreqName) {
		this.repFreqName = repFreqName;
	}







	/**
	 * @return Returns the repId.
	 */
	public Integer getRepId() {
		return repId;
	}







	/**
	 * @param repId The repId to set.
	 */
	public void setRepId(Integer repId) {
		this.repId = repId;
	}







	/**
	 * @return Returns the reportName.
	 */
	public java.lang.String getReportName() {
		return reportName;
	}







	/**
	 * @param reportName The reportName to set.
	 */
	public void setReportName(java.lang.String reportName) {
		this.reportName = reportName;
	}







	/**
	 * @return Returns the repTypeId.
	 */
	public java.lang.Integer getRepTypeId() {
		return repTypeId;
	}







	/**
	 * @param repTypeId The repTypeId to set.
	 */
	public void setRepTypeId(java.lang.Integer repTypeId) {
		this.repTypeId = repTypeId;
	}







	/**
	 * @return Returns the repTypeName.
	 */
	public java.lang.String getRepTypeName() {
		return repTypeName;
	}







	/**
	 * @param repTypeName The repTypeName to set.
	 */
	public void setRepTypeName(java.lang.String repTypeName) {
		this.repTypeName = repTypeName;
	}







	/**
	 * @return Returns the startDate.
	 */
	public java.util.Date getStartDate() {
		return startDate;
	}







	/**
	 * @param startDate The startDate to set.
	 */
	public void setStartDate(java.util.Date startDate) {
		this.startDate = startDate;
	}







	/**
	 * @return Returns the versionId.
	 */
	public java.lang.String getVersionId() {
		return versionId;
	}







	/**
	 * @param versionId The versionId to set.
	 */
	public void setVersionId(java.lang.String versionId) {
		this.versionId = versionId;
	}







	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}







	/**
	 * @return Returns the mActuReps.
	 */
	public Set getMActuReps() {
		return MActuReps;
	}







	/**
	 * @param actuReps The mActuReps to set.
	 */
	public void setMActuReps(Set actuReps) {
		MActuReps = actuReps;
	}







	/**
	 * @return Returns the mRepRanges.
	 */
	public Set getMRepRanges() {
		return MRepRanges;
	}







	/**
	 * @param repRanges The mRepRanges to set.
	 */
	public void setMRepRanges(Set repRanges) {
		MRepRanges = repRanges;
	}







	/**
	 * @return Returns the reports.
	 */
	public Set getReports() {
		return reports;
	}







	/**
	 * @param reports The reports to set.
	 */
	public void setReports(Set reports) {
		this.reports = reports;
	}







	/**
	 * @return Returns the repRangeDownLogs.
	 */
	public Set getRepRangeDownLogs() {
		return repRangeDownLogs;
	}







	/**
	 * @param repRangeDownLogs The repRangeDownLogs to set.
	 */
	public void setRepRangeDownLogs(Set repRangeDownLogs) {
		this.repRangeDownLogs = repRangeDownLogs;
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
	 * @return Returns the month.
	 */
	public int getMonth() {
		return month;
	}







	/**
	 * @param month The month to set.
	 */
	public void setMonth(int month) {
		this.month = month;
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
