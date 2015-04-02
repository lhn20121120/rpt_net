package com.fitech.gznx.form;

import java.util.Date;

import org.apache.struts.action.ActionForm;

import com.cbrc.smis.common.Config;

public class AFReportForm extends ActionForm {
	private String repId;
	private String versionId;
	private String preStandard;
	private String repFreqId;
	private String dataRangeId;
	private String curId;
	private String year;
	private String month;
	private String day;
	private String term;
	private String times;
	private String tblInnerValidateFlag;
	private String tblOuterValidateFlag;
	private String reportDataWarehouseFlag;
	private String repName;
	private String checkFlag;
	private String package_;
	private Date reportDate;
	private String abmormityChangeFlag;
	private String repRangeFlag;
	private String forseReportAgainFlag;
	private String laterReportDay;
	private String notReportFlag;
	private String writer;
	private String checker;
	private String principal;
	private String orgId;
	private String orgName;
	private Date checkDate;
	private Date verifyDate;
	private String bak1;
	private String bak2;
	private String recheckFlag;
	private String templateId;
	private String reReportTimes;
	
	private String date;
	private String templateType;
	private String curName;
	private String repFreqName;
	
	private Integer isReport;
	
	private String isLeader;
	//强制上报状态
	private String isFlag;
	//任务查询
	private String taskId;
	
	private String reviewStatus;//审阅状态
	
	public String getReviewStatus() {//默认为全部
		return reviewStatus==null?Config.DEFAULT_VALUE:reviewStatus;
	}
	public void setReviewStatus(String reviewStatus) {
		
		this.reviewStatus = reviewStatus;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getIsFlag() {
		return isFlag;
	}
	public void setIsFlag(String isFlag) {
		this.isFlag = isFlag;
	}
	/**页面各异常状态的单选框数组*/
	private String allFlags;
	/**查找后的列表显示的记录中的机构id的隐藏域数组*/
	private Integer[] repInIdArray;
	/**当前页的数*/
	private String curPage;
	/**重报原因*/
	private String cause;
	private String templateTypeName;
	private String supplementFlag;
	private String stateFlag;
	private String[] selectedOrgIds;
	
	private String styleFlg;
	
	private String searchOrgId;
	
	
	public String getStyleFlg() {
		return styleFlg;
	}
	public void setStyleFlg(String styleFlg) {
		this.styleFlg = styleFlg;
	}
	public String[] getSelectedOrgIds() {
		return selectedOrgIds;
	}
	public void setSelectedOrgIds(String[] selectedOrgIds) {
		this.selectedOrgIds = selectedOrgIds;
	}
	public String getCause() {
		return cause;
	}
	public void setCause(String cause) {
		this.cause = cause;
	}
	public String getAllFlags() {
		return allFlags;
	}
	public void setAllFlags(String allFlags) {
		this.allFlags = allFlags;
	}
	public Integer getIsReport() {
		return isReport;
	}
	public void setIsReport(Integer isReport) {
		this.isReport = isReport;
	}
	public String getCurName() {
		return curName;
	}
	public void setCurName(String curName) {
		this.curName = curName;
	}
	public String getRepFreqName() {
		return repFreqName;
	}
	public void setRepFreqName(String repFreqName) {
		this.repFreqName = repFreqName;
	}
	public String getTemplateType() {
		return templateType;
	}
	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getRepId() {
		return repId;
	}
	public void setRepId(String repId) {
		this.repId = repId;
	}
	public String getVersionId() {
		return versionId;
	}
	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}
	public String getPreStandard() {
		return preStandard;
	}
	public void setPreStandard(String preStandard) {
		this.preStandard = preStandard;
	}
	public String getDataRangeId() {
		return dataRangeId;
	}
	public void setDataRangeId(String dataRangeId) {
		this.dataRangeId = dataRangeId;
	}
	public String getCurId() {
		return curId;
	}
	public void setCurId(String curId) {
		this.curId = curId;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public String getTimes() {
		return times;
	}
	public void setTimes(String times) {
		this.times = times;
	}
	public String getTblInnerValidateFlag() {
		return tblInnerValidateFlag;
	}
	public void setTblInnerValidateFlag(String tblInnerValidateFlag) {
		this.tblInnerValidateFlag = tblInnerValidateFlag;
	}
	public String getTblOuterValidateFlag() {
		return tblOuterValidateFlag;
	}
	public void setTblOuterValidateFlag(String tblOuterValidateFlag) {
		this.tblOuterValidateFlag = tblOuterValidateFlag;
	}
	public String getReportDataWarehouseFlag() {
		return reportDataWarehouseFlag;
	}
	public void setReportDataWarehouseFlag(String reportDataWarehouseFlag) {
		this.reportDataWarehouseFlag = reportDataWarehouseFlag;
	}
	public String getRepName() {
		return repName;
	}
	public void setRepName(String repName) {
		this.repName = repName;
	}
	public String getCheckFlag() {
		return checkFlag;
	}
	public void setCheckFlag(String checkFlag) {
		this.checkFlag = checkFlag;
	}
	public String getPackage_() {
		return package_;
	}
	public void setPackage_(String package_) {
		this.package_ = package_;
	}
	public Date getReportDate() {
		return reportDate;
	}
	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}
	public String getAbmormityChangeFlag() {
		return abmormityChangeFlag;
	}
	public void setAbmormityChangeFlag(String abmormityChangeFlag) {
		this.abmormityChangeFlag = abmormityChangeFlag;
	}
	public String getRepRangeFlag() {
		return repRangeFlag;
	}
	public void setRepRangeFlag(String repRangeFlag) {
		this.repRangeFlag = repRangeFlag;
	}
	public String getForseReportAgainFlag() {
		return forseReportAgainFlag;
	}
	public void setForseReportAgainFlag(String forseReportAgainFlag) {
		this.forseReportAgainFlag = forseReportAgainFlag;
	}
	public String getLaterReportDay() {
		return laterReportDay;
	}
	public void setLaterReportDay(String laterReportDay) {
		this.laterReportDay = laterReportDay;
	}
	public String getNotReportFlag() {
		return notReportFlag;
	}
	public void setNotReportFlag(String notReportFlag) {
		this.notReportFlag = notReportFlag;
	}
	public String getWriter() {
		return writer;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
	public String getChecker() {
		return checker;
	}
	public void setChecker(String checker) {
		this.checker = checker;
	}
	public String getPrincipal() {
		return principal;
	}
	public void setPrincipal(String principal) {
		this.principal = principal;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public Date getCheckDate() {
		return checkDate;
	}
	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}
	public Date getVerifyDate() {
		return verifyDate;
	}
	public void setVerifyDate(Date verifyDate) {
		this.verifyDate = verifyDate;
	}
	public String getBak1() {
		return bak1;
	}
	public void setBak1(String bak1) {
		this.bak1 = bak1;
	}
	public String getBak2() {
		return bak2;
	}
	public void setBak2(String bak2) {
		this.bak2 = bak2;
	}
	public String getRecheckFlag() {
		return recheckFlag;
	}
	public void setRecheckFlag(String recheckFlag) {
		this.recheckFlag = recheckFlag;
	}
	public String getTemplateId() {
		return templateId;
	}
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	public String getReReportTimes() {
		return reReportTimes;
	}
	public void setReReportTimes(String reReportTimes) {
		this.reReportTimes = reReportTimes;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getRepFreqId() {
		return repFreqId;
	}
	public void setRepFreqId(String repFreqId) {
		this.repFreqId = repFreqId;
	}
	public Integer[] getRepInIdArray() {
		return repInIdArray;
	}
	public void setRepInIdArray(Integer[] repInIdArray) {
		this.repInIdArray = repInIdArray;
	}
	public String getCurPage() {
		return curPage;
	}
	public void setCurPage(String curPage) {
		this.curPage = curPage;
	}

	public String getIsLeader() {
		return isLeader;
	}
	public void setIsLeader(String isLeader) {
		this.isLeader = isLeader;
	}
	public String getTemplateTypeName() {
		return templateTypeName;
	}
	public void setTemplateTypeName(String templateTypeName) {
		this.templateTypeName = templateTypeName;
	}
	public String getSupplementFlag() {
		return supplementFlag;
	}
	public void setSupplementFlag(String supplementFlag) {
		this.supplementFlag = supplementFlag;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getStateFlag() {
		return stateFlag;
	}
	public void setStateFlag(String stateFlag) {
		this.stateFlag = stateFlag;
	}
	public String getSearchOrgId() {
		return searchOrgId;
	}
	public void setSearchOrgId(String searchOrgId) {
		this.searchOrgId = searchOrgId;
	}
	
	
}
