package com.fitech.net.form;

import org.apache.struts.action.ActionForm;

/**
 *描述：
 *日期：2007-12-12
 *作者：曹发根
 */
public class ACompareLogFrom  extends ActionForm {
	private Integer repInId;
	private Integer acType;
	private Integer acRepInId;
	private Integer acState;
	private String acLog;
//	报表名称
	private String repName=null;
//	子报表id
	private String childRepId=null;
	//版本号
	private String versionId=null;
//	报表口径
	private String dataRgTypeName=null;
//	上报年份
	private Integer year=null;
	//上报月份
	private Integer term=null;
	private String orgId;
//	货币的名称
	private String currName = null;
	/**
	 * 返回 currName
	 */
	public String getCurrName() {
		return currName;
	}
	/**
	 * 参数：currName 
	 * 设置 currName
	 */
	public void setCurrName(String currName) {
		this.currName = currName;
	}
	/**
	 * 返回 orgId
	 */
	public String getOrgId() {
		return orgId;
	}
	/**
	 * 参数：orgId 
	 * 设置 orgId
	 */
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	/**
	 * 返回 childRepId
	 */
	public String getChildRepId() {
		return childRepId;
	}
	/**
	 * 参数：childRepId 
	 * 设置 childRepId
	 */
	public void setChildRepId(String childRepId) {
		this.childRepId = childRepId;
	}
	
	/**
	 * 返回 acLog
	 */
	public String getAcLog() {
		return acLog;
	}
	/**
	 * 参数：acLog 
	 * 设置 acLog
	 */
	public void setAcLog(String acLog) {
		this.acLog = acLog;
	}
	/**
	 * 返回 acRepInId
	 */
	public Integer getAcRepInId() {
		return acRepInId;
	}
	/**
	 * 参数：acRepInId 
	 * 设置 acRepInId
	 */
	public void setAcRepInId(Integer acRepInId) {
		this.acRepInId = acRepInId;
	}
	/**
	 * 返回 acState
	 */
	public Integer getAcState() {
		return acState;
	}
	/**
	 * 参数：acState 
	 * 设置 acState
	 */
	public void setAcState(Integer acState) {
		this.acState = acState;
	}
	/**
	 * 返回 acType
	 */
	public Integer getAcType() {
		return acType;
	}
	/**
	 * 参数：acType 
	 * 设置 acType
	 */
	public void setAcType(Integer acType) {
		this.acType = acType;
	}
	/**
	 * 返回 dataRgTypeName
	 */
	public String getDataRgTypeName() {
		return dataRgTypeName;
	}
	/**
	 * 参数：dataRgTypeName 
	 * 设置 dataRgTypeName
	 */
	public void setDataRgTypeName(String dataRgTypeName) {
		this.dataRgTypeName = dataRgTypeName;
	}
	/**
	 * 返回 repInId
	 */
	public Integer getRepInId() {
		return repInId;
	}
	/**
	 * 参数：repInId 
	 * 设置 repInId
	 */
	public void setRepInId(Integer repInId) {
		this.repInId = repInId;
	}
	/**
	 * 返回 repName
	 */
	public String getRepName() {
		return repName;
	}
	/**
	 * 参数：repName 
	 * 设置 repName
	 */
	public void setRepName(String repName) {
		this.repName = repName;
	}
	/**
	 * 返回 term
	 */
	public Integer getTerm() {
		return term;
	}
	/**
	 * 参数：term 
	 * 设置 term
	 */
	public void setTerm(Integer term) {
		this.term = term;
	}
	/**
	 * 返回 versionId
	 */
	public String getVersionId() {
		return versionId;
	}
	/**
	 * 参数：versionId 
	 * 设置 versionId
	 */
	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}
	/**
	 * 返回 year
	 */
	public Integer getYear() {
		return year;
	}
	/**
	 * 参数：year 
	 * 设置 year
	 */
	public void setYear(Integer year) {
		this.year = year;
	}
	
}

