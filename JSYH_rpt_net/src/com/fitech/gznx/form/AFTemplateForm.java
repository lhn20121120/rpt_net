package com.fitech.gznx.form;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

import com.fitech.gznx.service.XmlTreeUtil;

public class AFTemplateForm extends ActionForm {
	
	private String templateId;
	private String versionId;
	private String templateName;
	private String startDate;
	private String endDate;
	private String isLeader;
	private String supplementFlag;
	private String filePath;
	private String usingFlag;
	private String templateType;
	private String templateTypeName;
	private String priorityFlag;
	private String isReport;
	private String isCollect;
	private String bak1;
	private String bak2;
	private String[] currList;
	
	private String curUnit;
	private String repTypeId;
	
	private Map keyMap;
	private String reportStyle;
	
	// 币种列表信息
	private String treeCurrContent;
	
	/** ZIP文件路径 */
	private FormFile reportFile=null;
	
	/** ZIP文件路径 */
	private FormFile qdreportFile=null;
	
	/**拼接汇总时对应的主报表编号*/
	private String joinTemplateId = null;
	
	public String getTemplateId() {
		return templateId;
	}
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	public String getVersionId() {
		return versionId;
	}
	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getIsLeader() {
		return isLeader;
	}
	public void setIsLeader(String isLeader) {
		this.isLeader = isLeader;
	}
	public String getSupplementFlag() {
		return supplementFlag==null || supplementFlag.trim().equals("")?"null":supplementFlag;
	}
	public void setSupplementFlag(String supplementFlag) {
		this.supplementFlag = supplementFlag;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getUsingFlag() {
		return usingFlag;
	}
	public void setUsingFlag(String usingFlag) {
		this.usingFlag = usingFlag;
	}
	public String getTemplateType() {
		return templateType;
	}
	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}
	public String getPriorityFlag() {
		return priorityFlag;
	}
	public void setPriorityFlag(String priorityFlag) {
		this.priorityFlag = priorityFlag;
	}
	public String getIsReport() {
		return isReport;
	}
	public void setIsReport(String isReport) {
		this.isReport = isReport;
	}
	public String getIsCollect() {
		return isCollect;
	}
	public void setIsCollect(String isCollect) {
		this.isCollect = isCollect;
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
	public FormFile getReportFile() {
		return reportFile;
	}
	public void setReportFile(FormFile reportFile) {
		this.reportFile = reportFile;
	}
	public String[] getCurrList() {
		return currList;
	}
	public void setCurrList(String[] currList) {
		this.currList = currList;
	}
	public String getTreeCurrContent() {
		if (this.treeCurrContent!=null) {
	   		return treeCurrContent;
	   	}
	   	else {
	   		String currTree = XmlTreeUtil.createCurrTree("TREE1_NODES",keyMap,true);
	   		return currTree;
	   	}
	}
	public void setTreeCurrContent(String treeCurrContent) {
		this.treeCurrContent = treeCurrContent;
	}
	public Map getKeyMap() {
		return keyMap;
	}
	public void setKeyMap(Map keyMap) {
		this.keyMap = keyMap;
	}
	public String getTemplateTypeName() {
		return templateTypeName;
	}
	public void setTemplateTypeName(String templateTypeName) {
		this.templateTypeName = templateTypeName;
	}
	public String getReportStyle() {
		return reportStyle;
	}
	public void setReportStyle(String reportStyle) {
		this.reportStyle = reportStyle;
	}
	public FormFile getQdreportFile() {
		return qdreportFile;
	}
	public void setQdreportFile(FormFile qdreportFile) {
		this.qdreportFile = qdreportFile;
	}

	public String getCurUnit() {
		return curUnit;
	}
	public void setCurUnit(String curUnit) {
		this.curUnit = curUnit;
	}
	public String getRepTypeId() {
		return repTypeId;
	}
	public void setRepTypeId(String repTypeId) {
		this.repTypeId = repTypeId;
	}
	public String getJoinTemplateId() {
		return joinTemplateId;
	}
	public void setJoinTemplateId(String joinTemplateId) {
		this.joinTemplateId = joinTemplateId;
	}
		
}
