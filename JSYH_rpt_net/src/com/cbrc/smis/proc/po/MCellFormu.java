package com.cbrc.smis.proc.po;

import java.io.Serializable;
/**
 * ���ڱ���ϵ���ʽʵ����
 * 
 * @author rds
 * @serialData 2005-12-20
 */
public class MCellFormu implements Serializable{
	/**
	 * ��ϵ���ʽ��ID
	 */
	private Integer cellFormuId;
	/**
	 * ��ϵ���ʽ������
	 */
	private String cellFormu;
	/**
	 * ��ϵ���ʽ�����
	 */
	private Integer cellType;
	/**
	 * С���㾫ȷ��λ��
	 */
	private Integer pointNumber;
	
	private String source;
	private String target;
	
	/**
	 * Ĭ�Ϲ��캯��
	 */
	public MCellFormu(){}
	/**
	 * @return Returns the pointNumber.
	 */
	public Integer getPointNumber() {
		return pointNumber;
	}
	/**
	 * @param pointNumber The pointNumber to set.
	 */
	public void setPointNumber(Integer pointNumber) {
		this.pointNumber = pointNumber;
	}
	/**
	 * ���캯��
	 */
	public MCellFormu(Integer cellFormuId,String cellFormu,Integer cellType,Integer pNumber){
		this.cellFormuId=cellFormuId;
		this.cellFormu=cellFormu;
		this.cellType=cellType;
		this.pointNumber=pNumber;
	}
	
	public String getCellFormu() {
		return cellFormu;
	}
	public void setCellFormu(String cellFormu) {
		this.cellFormu = cellFormu;
	}
	public Integer getCellFormuId() {
		return cellFormuId;
	}
	public void setCellFormuId(Integer cellFormuId) {
		this.cellFormuId = cellFormuId;
	}
	public Integer getCellType() {
		return cellType;
	}
	public void setCellType(Integer cellType) {
		this.cellType = cellType;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}

}
