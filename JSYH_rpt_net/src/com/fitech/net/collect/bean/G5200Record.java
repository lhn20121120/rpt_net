package com.fitech.net.collect.bean;

/**
 * This is JavaBean for report G5200
 * 
 * @author masclnj
 *
 */

public class G5200Record 
{
	/**
	 * 序号
	 */
	private String num;
	
	
	/**
	 * 客户代码
	 */
	private String cusNum;
	
	
	/**
	 * 客户名称
	 */
	private String cusName;
	
	
	/**
	 * 客户类型
	 */
	private String cusType;
	
	
	/**
	 * 客户所在地
	 */
	private String cusPlace;
	
	
	/**
	 * 行政区划代码
	 */
	private String code;
	
	
	/**
	 * 违约类型
	 */
	private String obeyType;


	/**
	 * @return Returns the code.
	 */
	public String getCode() {
		return code;
	}


	/**
	 * @param code The code to set.
	 */
	public void setCode(String code) {
		this.code = code;
	}


	/**
	 * @return Returns the cusName.
	 */
	public String getCusName() {
		return cusName;
	}


	/**
	 * @param cusName The cusName to set.
	 */
	public void setCusName(String cusName) {
		this.cusName = cusName;
	}


	/**
	 * @return Returns the cusNum.
	 */
	public String getCusNum() {
		return cusNum;
	}


	/**
	 * @param cusNum The cusNum to set.
	 */
	public void setCusNum(String cusNum) {
		this.cusNum = cusNum;
	}


	/**
	 * @return Returns the cusPlace.
	 */
	public String getCusPlace() {
		return cusPlace;
	}


	/**
	 * @param cusPlace The cusPlace to set.
	 */
	public void setCusPlace(String cusPlace) {
		this.cusPlace = cusPlace;
	}


	/**
	 * @return Returns the cusType.
	 */
	public String getCusType() {
		return cusType;
	}


	/**
	 * @param cusType The cusType to set.
	 */
	public void setCusType(String cusType) {
		this.cusType = cusType;
	}


	/**
	 * @return Returns the num.
	 */
	public String getNum() {
		return num;
	}


	/**
	 * @param num The num to set.
	 */
	public void setNum(String num) {
		this.num = num;
	}


	/**
	 * @return Returns the obeyType.
	 */
	public String getObeyType() {
		return obeyType;
	}


	/**
	 * @param obeyType The obeyType to set.
	 */
	public void setObeyType(String obeyType) {
		this.obeyType = obeyType;
	}

}
