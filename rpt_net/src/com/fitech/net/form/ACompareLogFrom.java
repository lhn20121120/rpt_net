package com.fitech.net.form;

import org.apache.struts.action.ActionForm;

/**
 *������
 *���ڣ�2007-12-12
 *���ߣ��ܷ���
 */
public class ACompareLogFrom  extends ActionForm {
	private Integer repInId;
	private Integer acType;
	private Integer acRepInId;
	private Integer acState;
	private String acLog;
//	��������
	private String repName=null;
//	�ӱ���id
	private String childRepId=null;
	//�汾��
	private String versionId=null;
//	����ھ�
	private String dataRgTypeName=null;
//	�ϱ����
	private Integer year=null;
	//�ϱ��·�
	private Integer term=null;
	private String orgId;
//	���ҵ�����
	private String currName = null;
	/**
	 * ���� currName
	 */
	public String getCurrName() {
		return currName;
	}
	/**
	 * ������currName 
	 * ���� currName
	 */
	public void setCurrName(String currName) {
		this.currName = currName;
	}
	/**
	 * ���� orgId
	 */
	public String getOrgId() {
		return orgId;
	}
	/**
	 * ������orgId 
	 * ���� orgId
	 */
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	/**
	 * ���� childRepId
	 */
	public String getChildRepId() {
		return childRepId;
	}
	/**
	 * ������childRepId 
	 * ���� childRepId
	 */
	public void setChildRepId(String childRepId) {
		this.childRepId = childRepId;
	}
	
	/**
	 * ���� acLog
	 */
	public String getAcLog() {
		return acLog;
	}
	/**
	 * ������acLog 
	 * ���� acLog
	 */
	public void setAcLog(String acLog) {
		this.acLog = acLog;
	}
	/**
	 * ���� acRepInId
	 */
	public Integer getAcRepInId() {
		return acRepInId;
	}
	/**
	 * ������acRepInId 
	 * ���� acRepInId
	 */
	public void setAcRepInId(Integer acRepInId) {
		this.acRepInId = acRepInId;
	}
	/**
	 * ���� acState
	 */
	public Integer getAcState() {
		return acState;
	}
	/**
	 * ������acState 
	 * ���� acState
	 */
	public void setAcState(Integer acState) {
		this.acState = acState;
	}
	/**
	 * ���� acType
	 */
	public Integer getAcType() {
		return acType;
	}
	/**
	 * ������acType 
	 * ���� acType
	 */
	public void setAcType(Integer acType) {
		this.acType = acType;
	}
	/**
	 * ���� dataRgTypeName
	 */
	public String getDataRgTypeName() {
		return dataRgTypeName;
	}
	/**
	 * ������dataRgTypeName 
	 * ���� dataRgTypeName
	 */
	public void setDataRgTypeName(String dataRgTypeName) {
		this.dataRgTypeName = dataRgTypeName;
	}
	/**
	 * ���� repInId
	 */
	public Integer getRepInId() {
		return repInId;
	}
	/**
	 * ������repInId 
	 * ���� repInId
	 */
	public void setRepInId(Integer repInId) {
		this.repInId = repInId;
	}
	/**
	 * ���� repName
	 */
	public String getRepName() {
		return repName;
	}
	/**
	 * ������repName 
	 * ���� repName
	 */
	public void setRepName(String repName) {
		this.repName = repName;
	}
	/**
	 * ���� term
	 */
	public Integer getTerm() {
		return term;
	}
	/**
	 * ������term 
	 * ���� term
	 */
	public void setTerm(Integer term) {
		this.term = term;
	}
	/**
	 * ���� versionId
	 */
	public String getVersionId() {
		return versionId;
	}
	/**
	 * ������versionId 
	 * ���� versionId
	 */
	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}
	/**
	 * ���� year
	 */
	public Integer getYear() {
		return year;
	}
	/**
	 * ������year 
	 * ���� year
	 */
	public void setYear(Integer year) {
		this.year = year;
	}
	
}

