package com.cbrc.smis.form;

import org.apache.struts.action.ActionForm;

public class AfTemplateCollRuleForm extends ActionForm {
	private String template_id;//模板编号
	private String version_id;//模板版本
	private String org_id;//机构号
	private String coll_schema;//汇总方式
	private String coll_formula;//汇总公式
	private String coll_type;//汇总方式  HZTJH--同级行，CUSOTM_ORG---自定义机构
	private String org_name;
	private String reportName;
	private String orgName;
	private String hz_style;//0 轧差汇总 1 加总
	private String preOrgId;//上级机构id
	
	public String getPreOrgId() {
		return preOrgId;
	}
	public void setPreOrgId(String preOrgId) {
		this.preOrgId = preOrgId;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getHz_style() {
		return hz_style;
	}
	public void setHz_style(String hzStyle) {
		hz_style = hzStyle;
	}
	public String getReportName() {
		return reportName;
	}
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	public String getOrg_name() {
		return org_name;
	}
	public void setOrg_name(String orgName) {
		org_name = orgName;
	}
	public String getColl_type() {
		return coll_type;
	}
	public void setColl_type(String collType) {
		coll_type = collType;
	}
	public String getTemplate_id() {
		return template_id;
	}
	public void setTemplate_id(String templateId) {
		template_id = templateId;
	}
	public String getVersion_id() {
		return version_id;
	}
	public void setVersion_id(String versionId) {
		version_id = versionId;
	}
	public String getOrg_id() {
		return org_id;
	}
	public void setOrg_id(String orgId) {
		org_id = orgId;
	}
	public String getColl_schema() {
		return coll_schema;
	}
	public void setColl_schema(String collSchema) {
		coll_schema = collSchema;
	}
	public String getColl_formula() {
		return coll_formula;
	}
	public void setColl_formula(String collFormula) {
		coll_formula = collFormula;
	}
	
	
}
