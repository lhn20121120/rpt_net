/**
 * �ð�����һЩʵ������
 */
package com.cbrc.org.entity;


;

/**
 * �������ڼ�¼����û�����ҳ���״̬
 * 
 * @author cb
 * 
 */
public class PageState {

	private String recordCount; // ��¼��������
	
	private String recordCountByPage; //ÿҳ����ʾ�ļ�¼��

	private String pageCount; // ��ʾ�ļ�¼����ҳ��

	private String pageVisiting; // ���ڷ��ʵ�ҳ���

	private String existsNextPage; // �Ƿ�����һҳ

	private String existsListPage; // �Ƿ���ǰһҳ

	public PageState(){}

	public PageState(String recordCount, String recordCountByPage, String pageCount, String pageVisiting, String existsNextPage, String existsListPage) {
		super();
		// TODO �Զ����ɹ��캯�����
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
