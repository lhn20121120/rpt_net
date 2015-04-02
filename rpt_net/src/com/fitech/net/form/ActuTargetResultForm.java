package com.fitech.net.form;

import java.util.List;

import org.apache.struts.action.ActionForm;



public class ActuTargetResultForm extends ActionForm {
	private String yearAndMonth=null;
	private Integer id=null;
	private Integer year=null;
	private Integer month=null;
	private String orgId=null;
	private Integer targetDefineId=null;
	private String targetDefineName=null;
	private Integer regionId=null;
	private Integer curUnitId=null;
	private Integer repFreId=null;
	private Integer dataRangeId=null;
	private Float targetResult=null;
	private String temp1=null;
	private String temp2=null;
	private List cellList=null;
	private String preFormula=null;
	private String afFormula=null;
	private Integer reportInId=null;
	
	private String color=null;
	private String curUnitName=null;
	private String regionName=null;
	private String repFreName=null;
	private String dataRangeName=null;
	private String preStandardValue=null;
	private String preStandardColor=null;
	
	private String allWarnMessage=null;
	
	private String versionID=null;
	private String times=null;
    /** The composite primary key value. */
    private java.lang.Integer businessId;

    /** The value of the simple nmBusinesssortname property. */
    private java.lang.String businessName;
    
    
    private Integer change=new Integer(0);
    private String normalName=null;
    private Integer norChange=new Integer(0);
	public String getNormalName() {
		return normalName;
	}
	public void setNormalName(String normalName) {
		this.normalName = normalName;
	}
	public Integer getChange() {
		return change;
	}
	public void setChange(Integer change) {
		this.change = change;
	}
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	public Integer getReportInId() {
		return reportInId;
	}
	public void setReportInId(Integer reportInId) {
		this.reportInId = reportInId;
	}
	public String getAfFormula() {
		return afFormula;
	}
	public void setAfFormula(String afFormula) {
		this.afFormula = afFormula;
	}
	public List getCellList() {
		return cellList;
	}
	public void setCellList(List cellList) {
		this.cellList = cellList;
	}
	public String getPreFormula() {
		return preFormula;
	}
	public void setPreFormula(String preFormula) {
		this.preFormula = preFormula;
	}
	public Integer getCurUnitId() {
		return curUnitId;
	}
	public void setCurUnitId(Integer curUnitId) {
		this.curUnitId = curUnitId;
	}
	public Integer getDataRangeId() {
		return dataRangeId;
	}
	public void setDataRangeId(Integer dataRangeId) {
		this.dataRangeId = dataRangeId;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getMonth() {
		return month;
	}
	public void setMonth(Integer month) {
		this.month = month;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public Integer getRegionId() {
		return regionId;
	}
	public void setRegionId(Integer regionId) {
		this.regionId = regionId;
	}
	public Integer getRepFreId() {
		return repFreId;
	}
	public void setRepFreId(Integer repFreId) {
		this.repFreId = repFreId;
	}
	public Integer getTargetDefineId() {
		return targetDefineId;
	}
	public void setTargetDefineId(Integer targetDefineId) {
		this.targetDefineId = targetDefineId;
	}
	public Float getTargetResult() {
		return targetResult;
	}
	public void setTargetResult(Float targetResult) {
		this.targetResult = targetResult;
	}
	public String getTemp1() {
		return temp1;
	}
	public void setTemp1(String temp1) {
		this.temp1 = temp1;
	}
	public String getTemp2() {
		return temp2;
	}
	public void setTemp2(String temp2) {
		this.temp2 = temp2;
	}
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	public String getTargetDefineName() {
		return targetDefineName;
	}
	public void setTargetDefineName(String targetDefineName) {
		this.targetDefineName = targetDefineName;
	}
	public String getCurUnitName() {
		return curUnitName;
	}
	public void setCurUnitName(String curUnitName) {
		this.curUnitName = curUnitName;
	}
	public String getDataRangeName() {
		return dataRangeName;
	}
	public void setDataRangeName(String dataRangeName) {
		this.dataRangeName = dataRangeName;
	}
	public String getRepFreName() {
		return repFreName;
	}
	public void setRepFreName(String repFreName) {
		this.repFreName = repFreName;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getPreStandardColor() {
		return preStandardColor;
	}
	public void setPreStandardColor(String preStandardColor) {
		this.preStandardColor = preStandardColor;
	}
	public String getPreStandardValue() {
		return preStandardValue;
	}
	public void setPreStandardValue(String preStandardValue) {
		this.preStandardValue = preStandardValue;
	}
	public String getAllWarnMessage() {
		return allWarnMessage;
	}
	public void setAllWarnMessage(String allWarnMessage) {
		this.allWarnMessage = allWarnMessage;
	}
	public String getVersionID() {
		return versionID;
	}
	public void setVersionID(String versionID) {
		this.versionID = versionID;
	}	
	public String getTimes() {
		return times;
	}
	public void setTimes(String times) {
		this.times = times;
	}
	public java.lang.Integer getBusinessId()
	{
		return businessId;
	}
	public void setBusinessId(java.lang.Integer businessId)
	{
		this.businessId = businessId;
	}
	public java.lang.String getBusinessName()
	{
		return businessName;
	}
	public void setBusinessName(java.lang.String businessName)
	{
		this.businessName = businessName;
	}
	public Integer getNorChange() {
		return norChange;
	}
	public void setNorChange(Integer norChange) {
		this.norChange = norChange;
	}
	public String getYearAndMonth()
	{
		return yearAndMonth;
	}
	public void setYearAndMonth(String yearAndMonth)
	{
		this.yearAndMonth = yearAndMonth;
	}

}