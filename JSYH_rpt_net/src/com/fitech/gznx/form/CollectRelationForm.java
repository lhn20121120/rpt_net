package com.fitech.gznx.form;

import org.apache.struts.action.ActionForm;

public class CollectRelationForm extends ActionForm {
	/**
	 * ���ܹ�ϵID[���ǻ�������һ��������ID,�����Ӧ���ܹ�ϵ���л���ID]
	 */
	private String collectId;

	/**
	 * ����ID[��Ӧ���ܹ�ϵ���еĻ���ID]
	 */
	private String orgId;
	/**
	 * ��������
	 */
	private String orgName;
	/**
	 * ��������
	 */
	private String style;

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getCollectId() {
		return collectId;
	}

	public void setCollectId(String collectId) {
		this.collectId = collectId;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
}
