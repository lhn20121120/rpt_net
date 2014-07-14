package com.cbrc.smis.hibernate;

import java.io.Serializable;
public class VDate implements Serializable{
	 private Integer vdid;
	 private String vdname;
     private Integer vdyear;
     private Integer vdsea;
     private Integer vdmonth;
     private Integer vdday;
     public VDate(){
    	 
     }
     public VDate(Integer vdid){
    	 this.vdid=vdid;
     }
     public VDate(String vdname){
    	 this.vdname=vdname;
     }
	public Integer getVdday() {
		return vdday;
	}
	public void setVdday(Integer vdday) {
		this.vdday = vdday;
	}
	public Integer getVdid() {
		return vdid;
	}
	public void setVdid(Integer vdid) {
		this.vdid = vdid;
	}
	public Integer getVdmonth() {
		return vdmonth;
	}
	public void setVdmonth(Integer vdmonth) {
		this.vdmonth = vdmonth;
	}
	public String getVdname() {
		return vdname;
	}
	public void setVdname(String vdname) {
		this.vdname = vdname;
	}
	public Integer getVdsea() {
		return vdsea;
	}
	public void setVdsea(Integer vdsea) {
		this.vdsea = vdsea;
	}
	public Integer getVdyear() {
		return vdyear;
	}
	public void setVdyear(Integer vdyear) {
		this.vdyear = vdyear;
	}

}
