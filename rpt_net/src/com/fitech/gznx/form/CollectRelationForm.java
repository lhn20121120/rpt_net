package com.fitech.gznx.form;

import org.apache.struts.action.ActionForm;

public class CollectRelationForm extends ActionForm {
	/**
	 * 汇总关系ID[就是机构表中一个机构的ID,将其对应汇总关系表中汇总ID]
	 */
	private String collectId;

	/**
	 * 机构ID[对应汇总关系表中的机构ID]
	 */
	private String orgId;
	/**
	 * 机构名称
	 */
	private String orgName;
	/**
	 * 汇总类型
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
