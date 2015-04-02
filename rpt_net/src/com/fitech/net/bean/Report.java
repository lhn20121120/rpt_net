package com.fitech.net.bean;

public class Report {
	/**
	 * 机构id
	 */
	private String orgId;
	
	
	/**报表id*/
	private String childId;
	
	
	/**版本号*/
	private String versionId;
	
	
	/**年份*/
	private String year;
	
	
	/**期数*/
	private String month;
	
	
	/**范围*/
	private String range;
	
	
	/**货种*/
	private String style;
	
	
	
	/**审核标志*/
	private Short checkFlag;


	public Report(String orgId, String childId, String versionId, String year, String month, String range, String style) {
		super();
		// TODO Auto-generated constructor stub
		this.orgId = orgId;
		this.childId = childId;
		this.versionId = versionId;
		this.year = year;
		this.month = month;
		this.range = range;
		this.style = style;
	}
	
	public Report()
	{
		
	}


	/**
	 * @return Returns the childId.
	 */
	public String getChildId() {
		return childId;
	}


	/**
	 * @param childId The childId to set.
	 */
	public void setChildId(String childId) {
		this.childId = childId;
	}


	/**
	 * @return Returns the month.
	 */
	public String getMonth() {
		return month;
	}


	/**
	 * @param month The month to set.
	 */
	public void setMonth(String month) {
		this.month = month;
	}


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
	 * @return Returns the range.
	 */
	public String getRange() {
		return range;
	}


	/**
	 * @param range The range to set.
	 */
	public void setRange(String range) {
		this.range = range;
	}


	/**
	 * @return Returns the style.
	 */
	public String getStyle() {
		return style;
	}


	/**
	 * @param style The style to set.
	 */
	public void setStyle(String style) {
		this.style = style;
	}


	/**
	 * @return Returns the versionId.
	 */
	public String getVersionId() {
		return versionId;
	}


	/**
	 * @param versionId The versionId to set.
	 */
	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}


	/**
	 * @return Returns the year.
	 */
	public String getYear() {
		return year;
	}


	/**
	 * @param year The year to set.
	 */
	public void setYear(String year) {
		this.year = year;
	}

	/**
	 * @return Returns the checkFlag.
	 */
	public Short getCheckFlag() {
		return checkFlag;
	}

	/**
	 * @param checkFlag The checkFlag to set.
	 */
	public void setCheckFlag(Short checkFlag) {
		this.checkFlag = checkFlag;
	}
	
	
	
}
