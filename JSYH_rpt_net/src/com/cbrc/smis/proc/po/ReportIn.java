package com.cbrc.smis.proc.po;

import java.io.Serializable;
/**
 * ʵ�����ݱ���ʵ����
 * 
 * @author rds
 * @serialData 2005-12-21 10:29
 */
public class ReportIn implements Serializable{
	/**
	 * ʵ�����ݱ���ID
	 */
	private Integer repInId;
	/**
	 * �ӱ���ID
	 */
	private String childRepId;
	/**
	 * �汾��
	 */
	private String versionId;
	/**
	 * ���ݷ�ΧID
	 */
	private Integer dataRangeId;
	/**
	 * ����ID
	 */
	private String orgId;
	/**
	 * ���ҵ�λID
	 */
	private Integer curId;
	/**
	 * ���
	 */
	private Integer year;
	/**
	 * ����
	 */
	private Integer term;
	/**
	 * �������
	 */
	private Integer reportStyle;
	/**
	 * ����Ƶ��
	 */
	private Integer repFreqId;
	
	
	/**
	 * ���캯��
	 */
	public ReportIn(){}
	
	public ReportIn(Integer repInId,String childRepId,String versionId,
			Integer dataRangeId,String orgId,Integer curId,Integer year,
			Integer term,Integer reportStyle,Integer repFreqId){
		this.repInId=repInId;
		this.childRepId=childRepId;
		this.versionId=versionId;
		this.dataRangeId=dataRangeId;
		this.orgId=orgId;
		this.curId=curId;
		this.year=year;
		this.term=term;
		this.reportStyle=reportStyle;
		this.repFreqId=repFreqId;

	}

	public String getChildRepId() {
		return childRepId;
	}

	public void setChildRepId(String childRepId) {
		this.childRepId = childRepId;
	}

	public Integer getCurId() {
		return curId;
	}
	public void setCurId(int curId){
		this.curId=new Integer(curId);
	}
	public void setCurId(Integer curId) {
		this.curId = curId;
	}

	public Integer getDataRangeId() {
		return dataRangeId;
	}
	public void setDataRangeId(int dataRangeId){
		this.dataRangeId=new Integer(dataRangeId);
	}
	public void setDataRangeId(Integer dataRangeId) {
		this.dataRangeId = dataRangeId;
	}

	public String getOrgId() {
		return orgId!=null?orgId.trim():orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public Integer getRepInId() {
		return repInId;
	}
	
	public void setRepInId(int repInId){
		this.repInId=new Integer(repInId);
	}
	public void setRepInId(Integer repInId) {
		this.repInId = repInId;
	}

	public Integer getTerm() {
		return term;
	}
	public void setTerm(int term){
		this.term=new Integer(term);
	}
	public void setTerm(Integer term) {
		this.term = term;
	}
	
	public String getVersionId() {
		return versionId;
	}

	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}

	public Integer getYear() {
		return year;
	}
	public void setYear(int year){
		this.year=new Integer(year);
	}
	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getReportStyle() {
		return reportStyle;
	}
	public void setReportStyle(int reportStyle){
		this.reportStyle=new Integer(reportStyle);
	}
	public void setReportStyle(Integer reportStyle) {
		this.reportStyle = reportStyle;
	}

	public Integer getRepFreqId() {
		return repFreqId;
	}

	public void setRepFreqId(Integer repFreqId) {
		this.repFreqId = repFreqId;
	}
}
