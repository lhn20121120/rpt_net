
package com.fitech.net.hibernate;
import java.io.Serializable;


public class ActuTargetResult
    implements Serializable
{
	private Integer id=null;
	private Integer year=null;
	private Integer month=null;
	private String orgId=null;
	private TargetDefine targetDefine=null;
	
	private Integer regionId=null;
	private Integer curUnitId=null;
	private Integer repFreId=null;
	private Integer dataRangeId=null;
	private String targetResult=null;
	private String temp1=null;
	private String temp2=null;
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
	
	public TargetDefine getTargetDefine() {
		return targetDefine;
	}
	public void setTargetDefine(TargetDefine targetDefine) {
		this.targetDefine = targetDefine;
	}
	public String getTargetResult() {
		return targetResult;
	}
	public void setTargetResult(String targetResult) {
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
	
	
}