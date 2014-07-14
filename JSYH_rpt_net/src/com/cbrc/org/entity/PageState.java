/**
 * 该包定义一些实体类型
 */
package com.cbrc.org.entity;


;

/**
 * 该类用于记录存放用户访问页面的状态
 * 
 * @author cb
 * 
 */
public class PageState {

	private String recordCount; // 记录的总数量
	
	private String recordCountByPage; //每页需显示的记录数

	private String pageCount; // 显示的记录的总页数

	private String pageVisiting; // 正在访问的页面号

	private String existsNextPage; // 是否有下一页

	private String existsListPage; // 是否有前一页

	public PageState(){}

	public PageState(String recordCount, String recordCountByPage, String pageCount, String pageVisiting, String existsNextPage, String existsListPage) {
		super();
		// TODO 自动生成构造函数存根
		this.recordCount = recordCount;
		this.recordCountByPage = recordCountByPage;
		this.pageCount = pageCount;
		this.pageVisiting = pageVisiting;
		this.existsNextPage = existsNextPage;
		this.existsListPage = existsListPage;
	}

	public String getExistsListPage() {
		return existsListPage;
	}

	public void setExistsListPage(String existsListPage) {
		this.existsListPage = existsListPage;
	}

	public String getExistsNextPage() {
		return existsNextPage;
	}

	public void setExistsNextPage(String existsNextPage) {
		this.existsNextPage = existsNextPage;
	}

	public String getPageCount() {
		return pageCount;
	}

	public void setPageCount(String pageCount) {
		this.pageCount = pageCount;
	}

	public String getPageVisiting() {
		return pageVisiting;
	}

	public void setPageVisiting(String pageVisiting) {
		this.pageVisiting = pageVisiting;
	}

	public String getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(String recordCount) {
		this.recordCount = recordCount;
	}

	public String getRecordCountByPage() {
		return recordCountByPage;
	}

	public void setRecordCountByPage(String recordCountByPage) {
		this.recordCountByPage = recordCountByPage;
	}

	
}
