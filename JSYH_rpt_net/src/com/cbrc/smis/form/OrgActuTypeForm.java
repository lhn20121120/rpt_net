package com.cbrc.smis.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class OrgActuTypeForm extends ActionForm {
	/**
	 * ID
	 */
	private Integer OATId=null;
	/**
	 * 频度类别名称
	 */
	private String OATName=null;
	/**
	 * 备注
	 */
	private String OATMemo=null;

	public OrgActuTypeForm(){}
		
	public Integer getOATId() {
		return OATId;
	}
	public void setOATId(Integer id) {
		OATId = id;
	}
	public String getOATMemo() {
		return OATMemo;
	}
	public void setOATMemo(String memo) {
		OATMemo = memo;
	}
	public String getOATName() {
		return OATName;
	}
	public void setOATName(String name) {
		OATName = name;
	}
	
	public void reset(ActionMapping mapping,HttpServletRequest request){
		this.OATId=null;
		this.OATName=null;
		this.OATMemo=null;
	}
}
