package com.fitech.net.Excel2Xml;
/**
 * �嵥ʽ�����ʵ����
 * @author GEN
 *
 */

public class BillBean {
	/**
	 * ѭ���еõ�Ա�����Ҫ������
	 */
	private String[] detail;
	/**
	 * ����
	 */
	private String title;
	/**
	 * excel�� ѭ����ʼ��
	 */
	private int startRow;
	/**
	 * ����id
	 */
	private String repId;
	
	/**
	 * �����xmlģ��
	 */
	private String model;
	/**
	 * ����ѭ����־�ַ���
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
