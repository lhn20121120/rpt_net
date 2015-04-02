 package com.fitech.net.form;

import org.apache.struts.action.ActionForm;


/**
 * Form for MCell.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 *
 * @struts.form name="MCellForm"
 */
public class TargetRangeForm extends ActionForm {



private java.lang.Integer id;
    private java.lang.String orgId;
    private Integer targetDefineId;
	public java.lang.Integer getId() {
		return id;
	}
	public void setId(java.lang.Integer id) {
		this.id = id;
	}
	public java.lang.String getOrgId() {
		return orgId;
	}
	public void setOrgId(java.lang.String orgId) {
		this.orgId = orgId;
	}
	public Integer getTargetDefineId() {
		return targetDefineId;
	}
	public void setTargetDefineId(Integer targetDefineId) {
		this.targetDefineId = targetDefineId;
	}
}