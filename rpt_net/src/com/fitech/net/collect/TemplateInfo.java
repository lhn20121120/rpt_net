/*
 * Created on 2006-5-19
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.fitech.net.collect;

import java.util.HashSet;
import java.util.Iterator;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TemplateInfo {
	private String reportID;
	private String versionID;
	
	public TemplateInfo(){}
	
	public TemplateInfo(String reportID, String versionID){
		this.setReportID(reportID);
		this.setVersionID(versionID);
	}
	
	/**
	 * @return Returns the reportID.
	 */
	public String getReportID() {
		return reportID;
	}
	/**
	 * @return Returns the versionID.
	 */
	public String getVersionID() {
		return versionID;
	}
	/**
	 * @param reportID The reportID to set.
	 */
	public void setReportID(String reportID) {
		this.reportID = reportID;
	}
	/**
	 * @param versionID The versionID to set.
	 */
	public void setVersionID(String versionID) {
		this.versionID = versionID;
	}
	
	public boolean equals(Object o){		
		if (!(o instanceof TemplateInfo)) {
			return false;
		}
		
		if (this == o) {
			return true;
		}else{			
			 TemplateInfo template = (TemplateInfo)o;
			 boolean repEquals = false;
			 if (this.getReportID() == null && 
			 		template.getReportID() == null) {
			 	repEquals = true;
			 }else{
			 	 if (this.getReportID() != null 
			 	 		&& this.getReportID().equals(template.getReportID())) {
			 	 	repEquals = true;
			 	 }
			 }
			 //boolean resultFlag = 
			 boolean versionEquals = false;
			 if (this.getVersionID() == null && 
			 		template.getVersionID() == null) {
			 	versionEquals = true;
			 }else{
			 	 if (this.getVersionID() != null 
			 	 		&& this.getVersionID().equals(template.getVersionID())) {
			 	 	versionEquals = true;
			 	 }
			 }
			 if (repEquals == true && versionEquals == true) {
			 	 return true;
			 }else{
			 	 return false;
			 }
		}
		
	}
	
	public int hashCode() {
		if (this.getReportID() == null && this.getVersionID() == null) {
			return -1;
		}else if (this.getReportID() != null && this.getVersionID() != null){		
			return this.getReportID().hashCode() + this.getVersionID().hashCode();
		}else if (this.getReportID() == null && this.getVersionID() != null) {
			return this.getVersionID().hashCode();
		}else{
			return this.getReportID().hashCode();
		}
	} 
	
	public static void main(String args[]){
		TemplateInfo t = new TemplateInfo("G11", "0511");
		TemplateInfo t1 = new TemplateInfo("G11", "0512");
		HashSet set = new HashSet();
		set.add(t);
		set.add(t1);
		Iterator iter = set.iterator();
		while(iter.hasNext()){
			TemplateInfo tmp = (TemplateInfo)iter.next();
			// System.out.println(tmp.getReportID());
			// System.out.println(tmp.getVersionID());
		}
	}
}
