package com.fitech.gznx.form;

import org.apache.struts.action.ActionForm;

public class DayTaskForm extends ActionForm {
	/** ���� */
	private String taskDate;
	/** ����ID */
	private String templateId;
	/** ����汾 */
	private String versionId;
	/** �������� */
	private String templateName;
	/** ָ���Ƶ��ID */
	private String repFreqId;
	/** ָ���Ƶ������ */
	private String repFreqName;
	/** ����ID */
	private String curId;
	/** ָ���������� */
	private String curName;
	/** �������ɱ�־λ��0Ϊδִ�У�1Ϊ����ִ�У�2Ϊִ�гɹ���-1Ϊִ��ʧ�� */
	private Integer flag;
	/** ��ʼʱ�� */
	private String startDate;
	/** ����ʱ�� */
	private String endDate;

	// ��ѯ������ʼ
	/** ������ʼʱ�� */
	private String queryStartTaskDate;
	/** ������ʼʱ�� */
	private String queryEndTaskDate;
	/** ����ID */
	private String queryTemplateId;
	/** ����汾 */
	private String queryVersionId;
	/** �������� */
	private String queryTemplateName;
	/** �������ɱ�־λ��0Ϊδִ�У�1Ϊ����ִ�У�2Ϊִ�гɹ���-1Ϊִ��ʧ�� */
	private Integer queryFlag;

	// ��ѯ��������
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
