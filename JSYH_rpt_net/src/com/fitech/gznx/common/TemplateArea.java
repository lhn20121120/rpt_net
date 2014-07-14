package com.fitech.gznx.common;

import java.util.ArrayList;
import java.util.List;

public class TemplateArea {
	private int startRow;
	private int endRow;
	private int startCol;
	private int endCol;
	private List itemList = new ArrayList();
	private List colList = new ArrayList();
	private List pidList = new ArrayList();
	public int getStartRow() {
		return startRow;
	}
	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}
	public int getEndRow() {
		return endRow;
	}
	public void setEndRow(int endRow) {
		this.endRow = endRow;
	}
	public int getStartCol() {
		return startCol;
	}
	public void setStartCol(int startCol) {
		this.startCol = startCol;
	}
	public int getEndCol() {
		return endCol;
	}
	public void setEndCol(int endCol) {
		this.endCol = endCol;
	}
	public List getItemList() {
		return itemList;
	}
	public void setItemList(List itemList) {
		this.itemList = itemList;
	}
	public List getColList() {
		return colList;
	}
	public void setColList(List colList) {
		this.colList = colList;
	}
	public List getPidList() {
		return pidList;
	}
	public void setPidList(List pidList) {
		this.pidList = pidList;
	}
	public void addPidList(Object obj) {
		this.pidList.add(obj);
	}
	public void addColList(Object obj) {
		this.colList.add(obj);
	}
	public void addItemList(Object obj) {
		this.itemList.add(obj);
	}

}
