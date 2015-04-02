package com.fitech.gznx.vo;

import java.util.HashMap;
import java.util.Map;

public class SearchVo {
	//灞曠ず绫诲瀷 1-4
	private Integer viewType;
	//1 杩炵画鏃堕棿 2鏃堕棿鐐�
	private Integer timeType;
	//startDate
	private String startTeam;
	private String  endTeam;
	
	public String getStartTeam() {
		return startTeam;
	}

	public void setStartTeam(String startTeam) {
		this.startTeam = startTeam;
	}

	public String getEndTeam() {
		return endTeam;
	}

	public void setEndTeam(String endTeam) {
		this.endTeam = endTeam;
	}

	private Integer mcurrId;
	public Integer getMcurrId() {
		return mcurrId;
	}

	public void setMcurrId(Integer mcurrId) {
		this.mcurrId = mcurrId;
	}

	private String startDate;
	private String endDate;
	//鏃堕棿鐐规棩鏈�
	private String theDate;
	//1=鏈湡銆�=涓婃湡銆�=骞村垵銆�=鍘诲勾鍚屾湡銆�=姣斾笂鏈熴�6=姣斿勾鍒濄�7=姣斿幓骞村悓鏈�
	private Integer[] dataType;
	//鎸囨爣鍜宑ellindex 閮藉湪閲岄潰   target_id/cellindex_id
	private String targetIds;
	
	private String orgIds;
	
	private String freqId;
	public String getFreqId() {
		return freqId;
	}

	public void setFreqId(String freqId) {
		this.freqId = freqId;
	}

	//椤圭洰
	private Map<String,String> itemMap;
	//鏃堕棿
	private Map dateMap;
	//鏈烘瀯
	private Map<String,String> orgMap;
	//鏁版嵁
	private Map dataMap;
	
	private String theYear;
	private String theMonth;
	private String singleDate;
	private String startYear;
	private String startMonth;
	private String endYear;
	private String endMonth;
	private String doubleStartDate;
	private String doubleEndDate;
	private String targets;
	
	
	
	
	public String getTargets() {
		return targets;
	}

	public void setTargets(String targets) {
		this.targets = targets;
	}

	public String getStartYear() {
		return startYear;
	}

	public void setStartYear(String startYear) {
		this.startYear = startYear;
	}

	public String getStartMonth() {
		return startMonth;
	}

	public void setStartMonth(String startMonth) {
		this.startMonth = startMonth;
	}

	public String getEndYear() {
		return endYear;
	}

	public void setEndYear(String endYear) {
		this.endYear = endYear;
	}

	public String getEndMonth() {
		return endMonth;
	}

	public void setEndMonth(String endMonth) {
		this.endMonth = endMonth;
	}

	public String getDoubleStartDate() {
		return doubleStartDate;
	}

	public void setDoubleStartDate(String doubleStartDate) {
		this.doubleStartDate = doubleStartDate;
	}

	public String getDoubleEndDate() {
		return doubleEndDate;
	}

	public void setDoubleEndDate(String doubleEndDate) {
		this.doubleEndDate = doubleEndDate;
	}

	public String getSingleDate() {
		return singleDate;
	}

	public void setSingleDate(String singleDate) {
		this.singleDate = singleDate;
	}

	public String getTheYear() {
		return theYear;
	}

	public void setTheYear(String theYear) {
		this.theYear = theYear;
	}

	public String getTheMonth() {
		return theMonth;
	}

	public void setTheMonth(String theMonth) {
		this.theMonth = theMonth;
	}

	public Integer getViewType() {
		return viewType;
	}

	public void setViewType(Integer viewType) {
		this.viewType = viewType;
	}

	public Integer getTimeType() {
		return timeType;
	}

	public void setTimeType(Integer timeType) {
		this.timeType = timeType;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getTheDate() {
		return theDate;
	}

	public void setTheDate(String theDate) {
		this.theDate = theDate;
	}

	public Integer[] getDataType() {
		return dataType;
	}

	public void setDataType(Integer[] dataType) {
		this.dataType = dataType;
	}

	

	public String getTargetIds() {
		return targetIds;
	}

	public void setTargetIds(String targetIds) {
		this.targetIds = targetIds;
	}

	public String getOrgIds() {
		return orgIds;
	}

	public void setOrgIds(String orgIds) {
		this.orgIds = orgIds;
	}

	public Map<String,String> getItemMap() {
		if(this.itemMap==null){
			this.itemMap= new HashMap();
		}
		return itemMap;
	}

	public void setItemMap(Map<String,String> itemMap) {
		this.itemMap = itemMap;
	}

	public Map getDateMap() {
		return dateMap;
	}

	public void setDateMap(Map dateMap) {
		this.dateMap = dateMap;
	}

	public Map<String,String> getOrgMap() {
		if(this.orgMap==null){
			this.orgMap= new HashMap<String,String>();
		}
		return orgMap;
	}

	public void setOrgMap(Map<String,String> orgMap) {
		this.orgMap = orgMap;
	}

	public Map getDataMap() {
		return dataMap;
	}

	public void setDataMap(Map dataMap) {
		this.dataMap = dataMap;
	}
	
}
