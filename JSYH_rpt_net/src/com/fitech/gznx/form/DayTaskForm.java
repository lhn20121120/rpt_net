package com.fitech.gznx.form;

import org.apache.struts.action.ActionForm;

public class DayTaskForm extends ActionForm {
	/** 期数 */
	private String taskDate;
	/** 报表ID */
	private String templateId;
	/** 报表版本 */
	private String versionId;
	/** 报表名称 */
	private String templateName;
	/** 指标表频度ID */
	private String repFreqId;
	/** 指标表频度名称 */
	private String repFreqName;
	/** 币种ID */
	private String curId;
	/** 指标表币种名称 */
	private String curName;
	/** 报表生成标志位，0为未执行，1为正在执行，2为执行成功，-1为执行失败 */
	private Integer flag;
	/** 起始时间 */
	private String startDate;
	/** 结束时间 */
	private String endDate;

	// 查询条件开始
	/** 期数开始时间 */
	private String queryStartTaskDate;
	/** 期数开始时间 */
	private String queryEndTaskDate;
	/** 报表ID */
	private String queryTemplateId;
	/** 报表版本 */
	private String queryVersionId;
	/** 报表名称 */
	private String queryTemplateName;
	/** 报表生成标志位，0为未执行，1为正在执行，2为执行成功，-1为执行失败 */
	private Integer queryFlag;

	// 查询条件结束
	public String getTaskDate() {
		return taskDate;
	}

	public void setTaskDate(String taskDate) {
		this.taskDate = taskDate;
	}

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

	public String getRepFreqId() {
		return repFreqId;
	}

	public void setRepFreqId(String repFreqId) {
		this.repFreqId = repFreqId;
	}

	public String getRepFreqName() {
		return repFreqName;
	}

	public void setRepFreqName(String repFreqName) {
		this.repFreqName = repFreqName;
	}

	public String getCurId() {
		return curId;
	}

	public void setCurId(String curId) {
		this.curId = curId;
	}

	public String getCurName() {
		return curName;
	}

	public void setCurName(String curName) {
		this.curName = curName;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
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

	public String getQueryStartTaskDate() {
		return queryStartTaskDate;
	}

	public void setQueryStartTaskDate(String queryStartTaskDate) {
		this.queryStartTaskDate = queryStartTaskDate;
	}

	public String getQueryEndTaskDate() {
		return queryEndTaskDate;
	}

	public void setQueryEndTaskDate(String queryEndTaskDate) {
		this.queryEndTaskDate = queryEndTaskDate;
	}

	public String getQueryTemplateId() {
		return queryTemplateId;
	}

	public void setQueryTemplateId(String queryTemplateId) {
		this.queryTemplateId = queryTemplateId;
	}

	public String getQueryVersionId() {
		return queryVersionId;
	}

	public void setQueryVersionId(String queryVersionId) {
		this.queryVersionId = queryVersionId;
	}

	public String getQueryTemplateName() {
		return queryTemplateName;
	}

	public void setQueryTemplateName(String queryTemplateName) {
		this.queryTemplateName = queryTemplateName;
	}

	public Integer getQueryFlag() {
		return queryFlag;
	}

	public void setQueryFlag(Integer queryFlag) {
		this.queryFlag = queryFlag;
	}

}
