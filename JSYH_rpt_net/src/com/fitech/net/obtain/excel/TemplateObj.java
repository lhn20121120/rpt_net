/*
 * Created on 2006-5-18
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.fitech.net.obtain.excel;

import java.util.HashMap;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TemplateObj {
	private String repID;
	private String versionID;
	private String mode;
	private String uri;
	private String excelName;
	private String guid;
	private HashMap dataSourceMap;
	
	/**
	 * @return Returns the mode.
	 */
	public String getMode() {
		return mode;
	}
	/**
	 * @return Returns the repID.
	 */
	public String getRepID() {
		return repID;
	}
	/**
	 * @return Returns the uri.
	 */
	public String getUri() {
		return uri;
	}
	/**
	 * @return Returns the versionID.
	 */
	public String getVersionID() {
		return versionID;
	}
	/**
	 * @param mode The mode to set.
	 */
	public void setMode(String mode) {
		this.mode = mode;
	}
	/**
	 * @param repID The repID to set.
	 */
	public void setRepID(String repID) {
		this.repID = repID;
	}
	/**
	 * @param uri The uri to set.
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}
	/**
	 * @param versionID The versionID to set.
	 */
	public void setVersionID(String versionID) {
		this.versionID = versionID;
	}
	
	
	/**
	 * @return Returns the dataSrouceMap.
	 */
	public HashMap getDataSourceMap() {
		return dataSourceMap;
	}
	/**
	 * @param dataSrouceMap The dataSrouceMap to set.
	 */
	public void setDataSourceMap(HashMap dataSourceMap) {
		this.dataSourceMap = dataSourceMap;
	}
	/**
	 * @return Returns the excelName.
	 */
	public String getExcelName() {
		return excelName;
	}
	/**
	 * @param excelName The excelName to set.
	 */
	public void setExcelName(String excelName) {
		this.excelName = excelName;
	}
	/**
	 * @return Returns the guid.
	 */
	public String getGuid() {
		return guid;
	}
	/**
	 * @param guid The guid to set.
	 */
	public void setGuid(String guid) {
		this.guid = guid;
	}
}
