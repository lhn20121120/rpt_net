package com.fitech.dataCollect;

import java.util.List;

/**
 * 
 * �������ݱ����
 * @author Yao
 * 
 * */
public class Collect_Report {
	
	/**�ӱ���id*/
	private String childRepId;
	/**�ӱ���ģ������*/
	private String childRepName;
	/**�汾��*/
	private String versionId;
	/**���ݷ�Χid*/
	private Integer dataRangeId;
	/**���ݷ�Χ����*/
	private String dataRangeDesc;
	/**����id*/
	private String curId;
	/**���ҵ�λ*/
	private String curUnitName;
	/**���*/
	private int year;
	/**����*/
	private int term;
	/**�·�*/
	private int month;
	
	/** ��־ 
	 * 	1�ǵ�Ե㱨�� 
	 * 	2���嵥ʽ����
	 * 	��Ӧ��Ӧ������
	 * */
	private int flag;
	
	/**��Ե㱨������*/
	private List p2p_dataList;
	
	/**�嵥ʽ��������*/
	private List bill_dataList;
	/**�嵥ʽ��ʼ��*/
	private int startRow;

	public List getBill_dataList() {
		return bill_dataList;
	}

	public void setBill_dataList(List bill_dataList) {
		this.bill_dataList = bill_dataList;
	}

	public String getChildRepId() {
		return childRepId;
	}

	public void setChildRepId(String childRepId) {
		this.childRepId = childRepId;
	}

	public String getChildRepName() {
		return childRepName;
	}

	public void setChildRepName(String childRepName) {
		this.childRepName = childRepName;
	}

	public String getCurId() {
		return curId;
	}

	public void setCurId(String curId) {
		this.curId = curId;
	}

	public String getCurUnitName() {
		return curUnitName;
	}

	public void setCurUnitName(String curUnitName) {
		this.curUnitName = curUnitName;
	}

	public String getDataRangeDesc() {
		return dataRangeDesc;
	}

	public void setDataRangeDesc(String dataRangeDesc) {
		this.dataRangeDesc = dataRangeDesc;
	}



	public Integer getDataRangeId() {
		return dataRangeId;
	}

	public void setDataRangeId(Integer dataRangeId) {
		this.dataRangeId = dataRangeId;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public List getP2p_dataList() {
		return p2p_dataList;
	}

	public void setP2p_dataList(List list) {
		p2p_dataList = list;
	}

	public int getTerm() {
		return term;
	}

	public void setTerm(int term) {
		this.term = term;
	}

	public String getVersionId() {
		return versionId;
	}

	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getStartRow() {
		return startRow;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}
	
	
	
	
	
}
