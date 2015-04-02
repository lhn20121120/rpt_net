package com.fitech.net.Excel2Xml;
/**
 * 代合计的清单式报表
 * @author GEN
 *
 */
public class Bill_1Bean {
	/**
	 * 循环行得单员格次序要从左到右
	 */
	private String[] detail;
	/**
	 * 合计的内容不带空格从左到右
	 */
	private String[] total;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 小标题
	 */
	private String subtitle;
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

	public String[] getDetail() {
		return detail;
	}

	public void setDetail(String[] detail) {
		this.detail = detail;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
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

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String[] getTotal() {
		return total;
	}

	public void setTotal(String[] total) {
		this.total = total;
	}

}
