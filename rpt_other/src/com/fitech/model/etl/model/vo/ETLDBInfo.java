package com.fitech.model.etl.model.vo;

public class ETLDBInfo {
	
	private String productName;
	
	private String versionId;
	
	private String DriverName;
	
	private String DriverVersion;
	
	private String dburl;
	
	private String userName;

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getVersionId() {
		return versionId;
	}

	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}

	public String getDriverName() {
		return DriverName;
	}

	public void setDriverName(String driverName) {
		DriverName = driverName;
	}

	public String getDriverVersion() {
		return DriverVersion;
	}

	public void setDriverVersion(String driverVersion) {
		DriverVersion = driverVersion;
	}

	public String getDburl() {
		return dburl;
	}

	public void setDburl(String dburl) {
		this.dburl = dburl;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}


}
