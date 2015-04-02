package com.fitech.institution.po;

public class BakTableInfoVo {
	private String bakTime;
	private String tableName;
	private String bakTableName;
	public String getBakTime() {
		return bakTime;
	}
	public void setBakTime(String bakTime) {
		this.bakTime = bakTime;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getBakTableName() {
		return bakTableName;
	}
	public void setBakTableName(String bakTableName) {
		this.bakTableName = bakTableName;
	}
	@Override
	public String toString() {
		return "BakTableInfoVo [bakTime=" + bakTime + ", tableName="
				+ tableName + ", bakTableName=" + bakTableName + "]";
	}
	
}
