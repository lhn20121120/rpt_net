package com.fitech.net.form;

import org.apache.struts.action.ActionForm;

public class VParameterForm extends ActionForm {
	/**
	 * 序号ID
	 */
	private Integer vpId = null;
	/**
	 * 对应字段ID
	 */
	private String vpColumnid = null;
	/**
	 * 对应表ID
	 */
	private String vpTableid = null;
	/**
	 * 对应表名称
	 */
	private String vpTabledesc = null;
	/**
	 * 对应字段名称
	 */
	private String vpColumndesc = null;
	/**
	 * 对应字段数据类型
	 */
	private Integer vpColtype = null;
	/**
	 * 名称
	 */
	private String vpNote = null;
	/**
	 * 表类别ID
	 */
	private String vttId = null;	
	/**
	 * 父序号
	 */
	private Integer pre_vpId = null;
	
	
	
	public Integer getPre_vpId() {
		return pre_vpId;
	}
	public void setPre_vpId(Integer pre_vpId) {
		this.pre_vpId = pre_vpId;
	}
	public String getVpColumnid() {
		return vpColumnid;
	}
	public void setVpColumnid(String vpColumnid) {
		this.vpColumnid = vpColumnid;
	}
	public String getVpColumndesc() {
		return vpColumndesc;
	}
	public void setVpColumndesc(String vpColumndesc) {
		this.vpColumndesc = vpColumndesc;
	}
	public Integer getVpColtype() {
		return vpColtype;
	}
	public void setVpColtype(Integer vpColtype) {
		this.vpColtype = vpColtype;
	}
	public Integer getVpId() {
		return vpId;
	}
	public void setVpId(Integer vpId) {
		this.vpId = vpId;
	}
	public String getVpNote() {
		return vpNote;
	}
	public void setVpNote(String vpNote) {
		this.vpNote = vpNote;
	}
	public String getVpTableid() {
		return vpTableid;
	}
	public void setVpTableid(String vpTableid) {
		this.vpTableid = vpTableid;
	}
	public String getVpTabledesc() {
		return vpTabledesc;
	}
	public void setVpTabledesc(String vpTabledesc) {
		this.vpTabledesc = vpTabledesc;
	}
	public String getVttId() {
		return vttId;
	}
	public void setVttId(String vttId) {
		this.vttId = vttId;
	}	
}
