package com.cbrc.smis.proc.po;

import java.io.Serializable;
/**
 * 表内表间关系表达式实体类
 * 
 * @author rds
 * @serialData 2005-12-20
 */
public class MCellFormu implements Serializable{
	/**
	 * 关系表达式的ID
	 */
	private Integer cellFormuId;
	/**
	 * 关系表达式的内容
	 */
	private String cellFormu;
	/**
	 * 关系表达式的类别
	 */
	private Integer cellType;
	/**
	 * 小数点精确的位数
	 */
	private Integer pointNumber;
	
	private String source;
	private String target;
	
	/**
	 * 默认构造函数
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
	 * 构造函数
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
