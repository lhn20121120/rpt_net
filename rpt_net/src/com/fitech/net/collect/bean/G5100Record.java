package com.fitech.net.collect.bean;

/**
 * This is the JavaBean for G5100 report
 * 
 * @author masclnj
 *
 */
public class G5100Record 
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
	 * 法人代表
	 */
	private String lawType;
	
	
	/**
	 * 法人注册地
	 */
	private String lawPlace;
	
	
	/**
	 * 行政区划代码
	 */
	private String code;
	
	
	/**
	 * 实收资本
	 */
	private String realCapital;
	
	
	/**
	 * 风险预警信号
	 */
	private String sign;
	
	
	/**
	 * 合计
	 */
	private G5100Record_Part all;
	
	
	/**
	 * 总行
	 */
	private G5100Record_Part nation;
	
	
	/**
	 * 省行
	 */
	private G5100Record_Part province;
	
	
	/**
	 * 地市级以下各级
	 */
	private G5100Record_Part city;


	/**
	 * @return Returns the all.
	 */
	public G5100Record_Part getAll() {
		return all;
	}


	/**
	 * @param all The all to set.
	 */
	public void setAll(G5100Record_Part all) {
		this.all = all;
	}


	/**
	 * @return Returns the city.
	 */
	public G5100Record_Part getCity() {
		return city;
	}


	/**
	 * @param city The city to set.
	 */
	public void setCity(G5100Record_Part city) {
		this.city = city;
	}


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
	 * @return Returns the lawPlace.
	 */
	public String getLawPlace() {
		return lawPlace;
	}


	/**
	 * @param lawPlace The lawPlace to set.
	 */
	public void setLawPlace(String lawPlace) {
		this.lawPlace = lawPlace;
	}


	/**
	 * @return Returns the lawType.
	 */
	public String getLawType() {
		return lawType;
	}


	/**
	 * @param lawType The lawType to set.
	 */
	public void setLawType(String lawType) {
		this.lawType = lawType;
	}


	/**
	 * @return Returns the nation.
	 */
	public G5100Record_Part getNation() {
		return nation;
	}


	/**
	 * @param nation The nation to set.
	 */
	public void setNation(G5100Record_Part nation) {
		this.nation = nation;
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
	 * @return Returns the province.
	 */
	public G5100Record_Part getProvince() {
		return province;
	}


	/**
	 * @param province The province to set.
	 */
	public void setProvince(G5100Record_Part province) {
		this.province = province;
	}


	/**
	 * @return Returns the realCapital.
	 */
	public String getRealCapital() {
		return realCapital;
	}


	/**
	 * @param realCapital The realCapital to set.
	 */
	public void setRealCapital(String realCapital) {
		this.realCapital = realCapital;
	}


	/**
	 * @return Returns the sign.
	 */
	public String getSign() {
		return sign;
	}


	/**
	 * @param sign The sign to set.
	 */
	public void setSign(String sign) {
		this.sign = sign;
	} 

}
