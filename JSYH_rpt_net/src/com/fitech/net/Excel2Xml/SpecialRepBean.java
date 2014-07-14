package com.fitech.net.Excel2Xml;

import java.util.List;
/**
 * 拆分得报表实体类
 * @author GEN
 *
 */

public class SpecialRepBean {
	/**大表的标题*/
	private String title;
	
	private List partList;
	/**小表的小标题位置*/
	private String row;
	private String col;

	public String getCol() {
		return col;
	}

	public void setCol(String col) {
		this.col = col;
	}

	public String getRow() {
		return row;
	}

	public void setRow(String row) {
		this.row = row;
	}

	public List getPartList() {
		return partList;
	}

	public void setPartList(List partList) {
		this.partList = partList;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
