package com.fitech.net.Excel2Xml;
/**
 * 清单式报表得实体类
 * @author GEN
 *
 */

public class BillBean {
	/**
	 * 循环行得单员格次序要从左到右
	 */
	private String[] detail;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * excel中 循环开始行
	 */
	private int startRow;
	/**
	 * 报表id
	 */
	private String repId;
	
	/**
	 * 报表的xml模板
	 */
	private String model;
	/**
	 * 结束循环标志字符串
	 */
	private String endStr;
	public String[] getDetail() {
		return detail;
	}
	public void setDetail(String[] detail) {
		this.detail = detail;
	}
	public String getEndStr() {
		return endStr;
	}
	public void setEndStr(String endStr) {
		this.endStr = endStr;
	}
	public String getRepId() {
		return repId;
	}
	public void setRepId(String repId) {
		this.repId = repId;
	}
	public int getStartRow() {
		return startRow;
	}
	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}

}
