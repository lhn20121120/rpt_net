package com.fitech.dataCollect;

import java.util.List;

/**
 * 
 * 汇总数据表对象
 * @author Yao
 * 
 * */
public class Collect_Report {
	
	/**子报表id*/
	private String childRepId;
	/**子报表模板名字*/
	private String childRepName;
	/**版本号*/
	private String versionId;
	/**数据范围id*/
	private Integer dataRangeId;
	/**数据范围描述*/
	private String dataRangeDesc;
	/**币种id*/
	private String curId;
	/**货币单位*/
	private String curUnitName;
	/**年份*/
	private int year;
	/**期数*/
	private int term;
	/**月份*/
	private int month;
	
	/** 标志 
	 * 	1是点对点报表 
	 * 	2是清单式报表
	 * 	对应相应的数据
	 * */
	private int flag;
	
	/**点对点报表数据*/
	private List p2p_dataList;
	
	/**清单式报表数据*/
	private List bill_dataList;
	/**清单式起始行*/
	private int startRow;

	public List getBill_dataList() {
		return bill_dataList;
	}

	public void setBill_dataList(List bill_dataList) {
		this.bill_dataList = bill_dataList;
	}

	public String getChildRepId() {
		return childRepId;
	}

	public void setChildRepId(String childRepId) {
		this.childRepId = childRepId;
	}

	public String getChildRepName() {
		return childRepName;
	}

	public void setChildRepName(String childRepName) {
		this.childRepName = childRepName;
	}

	public String getCurId() {
		return curId;
	}

	public void setCurId(String curId) {
		this.curId = curId;
	}

	public String getCurUnitName() {
		return curUnitName;
	}

	public void setCurUnitName(String curUnitName) {
		this.curUnitName = curUnitName;
	}

	public String getDataRangeDesc() {
		return dataRangeDesc;
	}

	public void setDataRangeDesc(String dataRangeDesc) {
		this.dataRangeDesc = dataRangeDesc;
	}



	public Integer getDataRangeId() {
		return dataRangeId;
	}

	public void setDataRangeId(Integer dataRangeId) {
		this.dataRangeId = dataRangeId;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public List getP2p_dataList() {
		return p2p_dataList;
	}

	public void setP2p_dataList(List list) {
		p2p_dataList = list;
	}

	public int getTerm() {
		return term;
	}

	public void setTerm(int term) {
		this.term = term;
	}

	public String getVersionId() {
		return versionId;
	}

	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getStartRow() {
		return startRow;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}
	
	
	
	
	
}
