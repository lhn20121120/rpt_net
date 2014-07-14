/*
 * Created on 2006-5-15
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.fitech.net.obtain.excel.form;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FileForm extends ActionForm{
    public FileForm(){}
    
    private FormFile file;
    private String repID;
    private String versionID;
    private String reportName;
    
	/**
	 * @return Returns the file.
	 */
	public FormFile getFile() {
		return file;
	}
	/**
	 * @param file The file to set.
	 */
	public void setFile(FormFile file) {
		this.file = file;
	}
	
    
	/**
	 * @return Returns the repID.
	 */
	public String getRepID() {
		return repID;
	}
	/**
	 * @param repID The repID to set.
	 */
	public void setRepID(String repID) {
		this.repID = repID;
	}
	/**
	 * @return Returns the reportName.
	 */
	public String getReportName() {
		return reportName;
	}
	/**
	 * @param reportName The reportName to set.
	 */
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	/**
	 * @return Returns the versionID.
	 */
	public String getVersionID() {
		return versionID;
	}
	/**
	 * @param versionID The versionID to set.
	 */
	public void setVersionID(String versionID) {
		this.versionID = versionID;
	}
}
