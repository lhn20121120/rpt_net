package com.fitech.net.Excel2Xml;
/**
 * ���ϼƵ��嵥ʽ����
 * @author GEN
 *
 */
public class Bill_1Bean {
	/**
	 * ѭ���еõ�Ա�����Ҫ������
	 */
	private String[] detail;
	/**
	 * �ϼƵ����ݲ����ո������
	 */
	private String[] total;
	/**
	 * ����
	 */
	private String title;
	/**
	 * С����
	 */
	private String subtitle;
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
