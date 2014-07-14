package com.fitech.net.Excel2Xml;
/**
 * 拆分报表得一部分
 * @author GEN
 *
 */

public class Part {
	
	
	/**小标题*/
	private String subtitle;
	/**起始行*/
	private String startRow;
    /**报表id*/
	private String id;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStartRow() {
		return startRow;
	}
	public void setStartRow(String startRow) {
		this.startRow = startRow;
	}
	public String getSubtitle() {
		return subtitle;
	}
	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}


}
