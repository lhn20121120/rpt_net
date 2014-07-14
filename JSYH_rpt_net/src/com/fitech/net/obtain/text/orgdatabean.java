package com.fitech.net.obtain.text;
 
import java.util.List;
public class orgdatabean {
private List datalist=null;
private String orgId=null;
//求平均数的时候用来计算总数
private int count=0;
private double result=0;

public int getCount() {
	return count;
}
public void setCount(int count) {
	this.count = count;
}


public double getResult() {
	return result;
}
public void setResult(double result) {
	this.result = result;
}
public List getDatalist() {
	return datalist;
}
public void setDatalist(List datalist) {
	this.datalist = datalist;
}
public String getOrgId() {
	return orgId;
}
public void setOrgId(String orgId) {
	this.orgId = orgId;
}

	
}
