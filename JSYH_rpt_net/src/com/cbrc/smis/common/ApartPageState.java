package com.cbrc.smis.common;

import java.io.Serializable;

/**
 * 
 * @author chenbing
 * 
 * �������ڷ�ҳ���� �����е��������ڼ�¼�û����ʵ�ҳ��״̬ ͬʱ�����ṩ"��ҳ","ǰһҳ","��һҳ","βҳ"��"��xҳ"��һЩҳ������ķ���
 */
public class ApartPageState implements Serializable {

	/**
	 * Ϊ�˱���Struts��ǩ���Ʋ����ϳ�������,���������佫�����������γ���
	 */
	private final int TRUE = 1;

	private final int FLASE = 0;

	/**
	 * ���������ҳ���ַ�������
	 */
	private final String FIRSTPAGE = "t";

	/**
	 * �������ǰһҳ���ַ�������
	 */
	private final String FRONTPAGE = "f";

	/**
	 * ���������һҳ���ַ�������
	 */
	private final String NEXTPAGE = "n";

	/**
	 * �������βҳ���ַ�������
	 */
	private final String ENDPAGE = "e";

	/**
	 * ������ʾ���ҳ���ַ�������
	 */
	private final String ABSOLUTEPAGE = "a";

	/**
	 * ��ʾ��¼�����
	 */
	private int begin;

	/**
	 * ÿҳ��ʾ�ļ�¼��,��������"begin"���������ʹ��
	 */
	private int limit;

	/**
	 * ��¼��������
	 */
	private int recordCount;

	/**
	 * ��ʾ��¼����ҳ��
	 */
	private int pageCount;

	/**
	 * ���ڷ��ʵ�ҳ���
	 */
	private int pageVisiting;

	/**
	 * �Ƿ�����ҳ
	 */
	private int isFirstPage;

	/**
	 * �Ƿ���βҳ
	 */
	private int isEndPage;

	/**
	 * �ù��캯�������е����Խ��г�ʼ������
	 * 
	 * @param recordCount
	 *            ��¼����
	 * @param limit
	 *            ÿҳ��ʾ�ļ�¼��
	 */
	public ApartPageState(int recordCount, int limit) {

		super();

		this.begin = 0; // ��ʼ����ʾ���

		this.limit = limit; // ��ʼ����ʾ�ĳ���

		this.recordCount = recordCount; // ��ʼ���ܼ�¼��

		int m = (this.recordCount) / (this.limit);

		int leavings = (this.recordCount) % (this.limit); // �õ�����

		if (leavings != 0)

			m++;

		this.pageCount = m; // ��ʼ����ҳ��

		this.pageVisiting = 1; // ��ʼ�����ڷ��ʵ�ҳ��

		this.conductPageState();

	}

	/**
	 * �÷������ڴ���isFirstPage,isEndPage��������
	 * 
	 */
	public void conductPageState() {

		if (this.recordCount != 0) {

			if ((this.pageVisiting) == 1)

				this.isFirstPage = this.TRUE;

			else

				this.isFirstPage = this.FLASE;

			if ((this.pageVisiting) == (this.pageCount))

				this.isEndPage = this.TRUE;

			else

				this.isEndPage = this.FLASE;

		} else {     //�����ѯ��¼Ϊ����ִ����������

			this.isFirstPage = this.TRUE;

			this.isEndPage = this.TRUE;
		}

	}

	public int getBegin() {
		return begin;
	}

	public void setBegin(int begin) {
		this.begin = begin;
	}

	public int getIsEndPage() {
		return isEndPage;
	}

	public void setIsEndPage(int isEndPage) {
		this.isEndPage = isEndPage;
	}

	public int getIsFirstPage() {
		return isFirstPage;
	}

	public void setIsFirstPage(int isFirstPage) {
		this.isFirstPage = isFirstPage;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
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

	/**
	 * �÷������ڴ��������ҳʱ�Ĳ���
	 * 
	 */
	public void visitFirstPage() {

		this.pageVisiting = 1;

		this.begin = 0;

		this.conductPageState();

	}

	/**
	 * �÷������ڴ������βҳʱ�Ĳ���
	 * 
	 */
	public void visitEndPage() {

		this.pageVisiting = this.pageCount;

		this.begin = ((this.pageVisiting - 1) * (this.limit));

		this.conductPageState();

	}

	/**
	 * �÷������ڴ��������һҳʱ�Ĳ���
	 * 
	 */
	public void visitNextPage() {

		this.pageVisiting = this.pageVisiting + 1;

		this.begin = ((this.pageVisiting - 1) * (this.limit));

		this.conductPageState();
	}

	/**
	 * �÷������ڴ������ǰһҳʱ�Ĳ���
	 * 
	 */
	public void visitFrontPage() {

		this.pageVisiting = this.pageVisiting - 1;

		this.begin = ((this.pageVisiting - 1) * (this.limit));

		this.conductPageState();
	}

	/**
	 * �÷������ڴ�����ʾ���ҳ��ʱ�Ĳ���,����ҳ���Ǵӵ�һҳ��ʼ��ʾ��
	 * 
	 * @param pageNumber
	 */
	public void visitAbsolutePage(int pageNumber) {

		if ((pageNumber < 1) || (pageNumber > (this.pageCount))){
			if(pageNumber < 1)
				this.pageVisiting = 1;        // ��������ҳ��С��1���ҳ��ֵ�ķ�Χ�����������ҳ
			else
				this.pageVisiting = this.pageCount; // ��������ҳ��������ҳ��ֵ�ķ�Χ�����������ҳ��
		}
		else

			this.pageVisiting = pageNumber;

		this.begin = ((this.pageVisiting - 1) * (this.limit));

		this.conductPageState();

	}

	/**
	 * ��������ķ�ҳ������ͳһ�����ҳ���� ע��÷��������ж���ҳ,ǰһҳ,��һҳ,βҳ,����ҳ���ַ����ֱ���t,f,n,e,a������
	 * 
	 * @param apartType
	 *            type:String ����t,f,n,e,a�����ֵ���ҳ,ǰһҳ,��һҳ,βҳ,����ҳ���ַ���
	 * @throws Exception
	 */
	public void conduct(String apartType) throws Exception {

		if (apartType.equals(this.FRONTPAGE)) {

			this.visitFrontPage();

			return;

		}

		if (apartType.equals(this.NEXTPAGE)) {

			this.visitNextPage();

			return;

		}

		if (apartType.equals(this.FIRSTPAGE)) {

			this.visitFirstPage();

			return;

		}

		if (apartType.equals(this.ENDPAGE)) {

			this.visitEndPage();

			return;

		}

		int pageNumber;

		try {

			pageNumber = Integer.parseInt(apartType);

		} catch (Exception e) {

			pageNumber = 1;
		}

		this.visitAbsolutePage(pageNumber);
	}
}
