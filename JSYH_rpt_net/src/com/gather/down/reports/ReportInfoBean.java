package com.gather.down.reports;

public class ReportInfoBean {

	
    private String formModelID="";   //子报表id
    private String parentName="";   //主报表中文名称
    private String sonName="";   //子报表名称
    private String version="";   //版本
    private String address="";   //空
    private String startTime="";   //开始时间
    private String endTime="";   //结束时间
    private String file="";        //文件名称
    
    private String orgId="";      //供比较下载记录时，使用
	/**
	 * @return Returns the orgId.
	 */
	public String getOrgId() {
		return orgId;
	}
	/**
	 * @param orgId The orgId to set.
	 */
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	/**
	 * @return Returns the file.
	 */
	public String getFile() {
		return file;
	}
	/**
	 * @param file The file to set.
	 */
	public void setFile(String file) {
		this.file = file;
	}
	/**
	 * @return Returns the address.
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @param address The address to set.
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * @return Returns the endTime.
	 */
	public String getEndTime() {
		return endTime;
	}
	/**
	 * @param endTime The endTime to set.
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	/**
	 * @return Returns the formModelID.
	 */
	public String getFormModelID() {
		return formModelID;
	}
	/**
	 * @param formModelID The formModelID to set.
	 */
	public void setFormModelID(String formModelID) {
		this.formModelID = formModelID;
	}
	/**
	 * @return Returns the parentName.
	 */
	public String getParentName() {
		return parentName;
	}
	/**
	 * @param parentName The parentName to set.
	 */
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	/**
	 * @return Returns the sonName.
	 */
	public String getSonName() {
		return sonName;
	}
	/**
	 * @param sonName The sonName to set.
	 */
	public void setSonName(String sonName) {
		this.sonName = sonName;
	}
	/**
	 * @return Returns the startTime.
	 */
	public String getStartTime() {
		return startTime;
	}
	/**
	 * @param startTime The startTime to set.
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	/**
	 * @return Returns the version.
	 */
	public String getVersion() {
		return version;
	}
	/**
	 * @param version The version to set.
	 */
	public void setVersion(String version) {
		this.version = version;
	}
    
    

}
