package com.cbrc.org.form;
/**
 * 该类用于记录存放用户访问页面的状态
 * 
 * @author cb
 */
public class PageStateForm {

	private int recordCount; // 记录的总数量
	
	private int recordCountByPage; //每页需显示的记录数

	private int pageCount; // 显示的记录的总页数

	private int pageVisiting; // 正在访问的页面号

	private boolean existsNextPage; // 是否有下一页

	private boolean existsListPage; // 是否有前一页
	
	private boolean firstPage;
	
	private boolean endPage;

	public PageStateForm(){}

	public boolean isEndPage() {
		return endPage;
	}

	public void setEndPage(boolean endPage) {
		this.endPage = endPage;
	}

	public boolean isExistsListPage() {
		return existsListPage;
	}

	public void setExistsListPage(boolean existsListPage) {
		this.existsListPage = existsListPage;
	}

	public boolean isExistsNextPage() {
		return existsNextPage;
	}

	public void setExistsNextPage(boolean existsNextPage) {
		this.existsNextPage = existsNextPage;
	}

	public boolean isFirstPage() {
		return firstPage;
	}

	public void setFirstPage(boolean firstPage) {
		this.firstPage = firstPage;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public int getPageVisiting() {
		return pageVisiting;
	}

	public void setPageVisiting(int pageVisiting) {
		this.pageVisiting = pageVisiting;
	}

	public int getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}

	public int getRecordCountByPage() {
		return recordCountByPage;
	}

	public void setRecordCountByPage(int recordCountByPage) {
		this.recordCountByPage = recordCountByPage;
	}

	
	
}

