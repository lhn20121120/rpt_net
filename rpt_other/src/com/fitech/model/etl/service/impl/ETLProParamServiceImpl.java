package com.fitech.model.etl.service.impl;

import java.sql.CallableStatement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fitech.framework.core.util.DateUtil;
import com.fitech.model.etl.service.IETLProParamService;

public class ETLProParamServiceImpl implements IETLProParamService{
	
	private String taskDate;
	
	private String formatStr;

	private String orgId;
	
	private String dataRangeId;
	
	public Map getProParams(){
		Map map = new HashMap();
		map.put("etldate",new java.sql.Date(DateUtil.getDateByString(taskDate).getTime()));
		map.put("orgid", this.orgId);
		map.put("datarangeid", this.dataRangeId);
		return map;
	}
	public Map getProParams(Date etlDate){
		Map map = new HashMap();
		if(this.formatStr==null || this.formatStr.equals("-1"))
			map.put("etldate",new java.sql.Date(etlDate.getTime()));
		else{
			SimpleDateFormat format = new SimpleDateFormat(this.formatStr);
			map.put("etldate",format.format(etlDate.getTime()));
		}
		map.put("orgid", this.orgId);
		map.put("datarangeid", this.dataRangeId);
		return map;
	}
	public Object getValue(String[] param){
		Object object = null;
		if(param[1].toLowerCase().equals("etldate")){
			if(param[0].toLowerCase().equals("date"))
				object = DateUtil.getTodayDate();
			else
				object = DateUtil.getTodayDateStr();
		}else
		if(param[1].toLowerCase().equals("orgid")){
			;
		}
		return object;
	}
	public void setCallStatement(CallableStatement cs){
		
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getDataRangeId() {
		return dataRangeId;
	}
	public void setDataRangeId(String dataRangeId) {
		this.dataRangeId = dataRangeId;
	}
	public String getTaskDate() {
		return taskDate;
	}
	public void setTaskDate(String taskDate) {
		this.taskDate = taskDate;
	}
	public String getFormatStr() {
		return formatStr;
	}
	public void setFormatStr(String formatStr) {
		this.formatStr = formatStr;
	}
}
