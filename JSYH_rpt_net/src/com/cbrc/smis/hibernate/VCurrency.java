package com.cbrc.smis.hibernate;

import java.io.Serializable;

public class VCurrency implements Serializable{
	String vcid ;
	String vccnname;
	String vcenname;
	String vcinname;
	public VCurrency(){
		
	}
    public VCurrency(String vcid ){
		this.vcid=vcid;
		
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
	public String getVcid() {
		return vcid;
	}
	public void setVcid(String vcid) {
		this.vcid = vcid;
	}
	public String getVcinname() {
		return vcinname;
	}
	public void setVcinname(String vcinname) {
		this.vcinname = vcinname;
	}

}
