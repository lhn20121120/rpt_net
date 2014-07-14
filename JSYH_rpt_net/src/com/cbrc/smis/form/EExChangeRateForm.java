package com.cbrc.smis.form;

import org.apache.struts.action.ActionForm;

public class EExChangeRateForm extends ActionForm {
	 private String vcid;
     private Integer vdid;
     private String vdname;
     private String vccnname;
     private String vcenname;
     Double eeramt;
     public EExChangeRateForm(){
    	 
     }
	public Double getEeramt() {
		return eeramt;
	}
	public void setEeramt(Double eeramt) {
		this.eeramt = eeramt;
	}
	public String getVcid() {
		return vcid;
	}
	public void setVcid(String vcid) {
		this.vcid = vcid;
	}
	public Integer getVdid() {
		return vdid;
	}
	public void setVdid(Integer vdid) {
		this.vdid = vdid;
	}
	public String getVdname() {
		return vdname;
	}
	public void setVdname(String vdname) {
		this.vdname = vdname;
	}
	public String getVccnname() {
		return vccnname;
	}
	public void setVccnname(String vccnname) {
		this.vccnname = vccnname;
	}
	public String getVcenname() {
		return vcenname;
	}
	public void setVcenname(String vcenname) {
		this.vcenname = vcenname;
	}

}
