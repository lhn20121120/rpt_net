package com.fitech.net.collect.bean;

public class G2400Record {
	
	//机构名字
	private String org_name;
	
	//机构编码
	private String org_code;
	
	//同业拆入余额
	private String rest;
	
	//占资本净额比重
	private String percent;
	
	//资本净额
	private String net;
	
	
	
	public G2400Record()
	{
		
	}



	public String getNet() {
		return net;
	}



	public void setNet(String net) {
		this.net = net;
	}



	public String getOrg_code() {
		return org_code;
	}



	public void setOrg_code(String org_code) {
		this.org_code = org_code;
	}



	public String getOrg_name() {
		return org_name;
	}



	public void setOrg_name(String org_name) {
		this.org_name = org_name;
	}



	public String getPercent() {
		return percent;
	}



	public void setPercent(String percent) {
		this.percent = percent;
	}



	public String getRest() {
		return rest;
	}



	public void setRest(String rest) {
		this.rest = rest;
	}
	
}