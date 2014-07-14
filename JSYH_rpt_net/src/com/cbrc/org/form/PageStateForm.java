package com.cbrc.org.form;
/**
 * �������ڼ�¼����û�����ҳ���״̬
 * 
 * @author cb
 */
public class PageStateForm {

	private int recordCount; // ��¼��������
	
	private int recordCountByPage; //ÿҳ����ʾ�ļ�¼��

	private int pageCount; // ��ʾ�ļ�¼����ҳ��

	private int pageVisiting; // ���ڷ��ʵ�ҳ���

	private boolean existsNextPage; // �Ƿ�����һҳ

	private boolean existsListPage; // �Ƿ���ǰһҳ
	
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

