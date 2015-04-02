package com.fitech.net.orglayer.form;

import org.apache.struts.action.ActionForm;


public class  OrgLayerForm   extends ActionForm {

	   

	private java.lang.String  orgLayerId = null;
	private java.lang.String  orgLayer = null;
	public java.lang.String getOrgLayer() {
		return orgLayer;
	}
	public void setOrgLayer(java.lang.String orgLayer) {
		this.orgLayer = orgLayer;
	}
	public java.lang.String getOrgLayerId() {
		return orgLayerId;
	}
	public void setOrgLayerId(java.lang.String orgLayerId) {
		this.orgLayerId = orgLayerId;
	}

}