package com.fitech.gznx.form;

import org.apache.struts.action.ActionForm;
// 人行报表
public class AFPBOCReportForm extends ActionForm {
	private String repInIds;
	private String orgId;
	private String date;
	private String fileName;
	private String batchName;
	private String batchTime;
	private String repFreqName;
	private String filePath;
	private String contents;
	private String batchId;
	private String repName;
	private String repFreqId;
	private String supplementFlag;
	public String getRepInIds() {
		return repInIds;
	}
	public void setRepInIds(String repInIds) {
		this.repInIds = repInIds;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getBatchName() {
		return batchName;
	}
	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}
	public String getBatchTime() {
		return batchTime;
	}
	public void setBatchTime(String batchTime) {
		this.batchTime = batchTime;
	}
	public String getRepFreqName() {
		return repFreqName;
	}
	public void setRepFreqName(String repFreqName) {
		this.repFreqName = repFreqName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}
	public String getBatchId() {
		return batchId;
	}
	public void setRepName(String repName) {
		this.repName = repName;
	}
	public String getRepName() {
		return repName;
	}
	public void setRepFreqId(String repFreqId) {
		this.repFreqId = repFreqId;
	}
	public String getRepFreqId() {
		return repFreqId;
	}
	public void setSupplementFlag(String supplementFlag) {
		this.supplementFlag = supplementFlag;
	}
	public String getSupplementFlag() {
		return supplementFlag;
	}
}
