package com.cbrc.smis.form;

import org.apache.struts.action.ActionForm;

public class AfTemplateCollRuleForm extends ActionForm {
	private String template_id;//ģ����
	private String version_id;//ģ��汾
	private String org_id;//������
	private String coll_schema;//���ܷ�ʽ
	private String coll_formula;//���ܹ�ʽ
	private String coll_type;//���ܷ�ʽ  HZTJH--ͬ���У�CUSOTM_ORG---�Զ������
	private String org_name;
	private String reportName;
	private String orgName;
	private String hz_style;//0 ������� 1 ����
	private String preOrgId;//�ϼ�����id
	
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
