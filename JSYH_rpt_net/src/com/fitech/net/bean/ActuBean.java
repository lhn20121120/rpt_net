package com.fitech.net.bean;

import com.fitech.net.form.ActuTargetResultForm;

public class ActuBean extends ActuTargetResultForm{
	public ActuBean(ActuTargetResultForm form ){
		this.setAfFormula(form.getAfFormula());
		this.setAllWarnMessage(form.getAllWarnMessage());
		this.setBusinessId(form.getBusinessId());
		this.setBusinessName(form.getBusinessName());
		this.setCellList(form.getCellList());
		this.setColor(form.getColor());
		this.setCurUnitId(form.getCurUnitId());
		this.setCurUnitName(form.getCurUnitName());
		this.setDataRangeId(form.getDataRangeId());
		this.setDataRangeName(form.getDataRangeName());
		this.setId(form.getId());
		this.setMonth(form.getMonth());
		this.setMultipartRequestHandler(form.getMultipartRequestHandler());
		this.setOrgId(form.getOrgId());
		this.setPreFormula(form.getPreFormula());
		this.setPreStandardColor(form.getPreStandardColor());
		this.setPreStandardValue(form.getPreStandardValue());
		this.setRegionId(form.getRegionId());
		this.setRegionName(form.getRegionName());
		this.setRepFreId(form.getRepFreId());
		this.setRepFreName(form.getRepFreName());
		this.setReportInId(form.getRegionId());
	//	this.setServlet(form.getServletWrapper());
		this.setTargetDefineId(form.getTargetDefineId());
		this.setTargetDefineName(form.getTargetDefineName());
		this.setTargetResult(form.getTargetResult());
		this.setTemp1(form.getTemp1());
		this.setTemp2(form.getTemp2());
		this.setTimes(form.getTimes());
		this.setVersionID(form.getVersionID());
		this.setYear(form.getYear());
	}
	private Integer change=new Integer(0);
	public Integer getChange() {
		return change;
	}
	public void setChange(Integer change) {
		this.change = change;
	}
	

}
