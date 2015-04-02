package com.fitech.model.worktask.vo;

public class TemplateVo {
	private String templateId;
	private String versionId;
	private String templateName;
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
	@Override
	public String toString() {
		return "TemplateVo [templateId=" + templateId + ", versionId="
				+ versionId + ", templateName=" + templateName + "]";
	}
	
}
