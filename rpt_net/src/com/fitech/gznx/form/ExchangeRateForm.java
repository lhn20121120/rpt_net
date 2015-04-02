//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_4.1.1/xslt/JavaClass.xsl

package com.fitech.gznx.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
/** 
 * ExchangeRateForm
 * @author	gongming
 * @date	2007-07-27
 * 
 */
public class ExchangeRateForm extends ActionForm {

	// --------------------------------------------------------- Instance Variables
	private static final long serialVersionUID = 1L;

	/**	erId property */
	private Long erId=null;
	
	/** extRate property */
	private String extRate=null;

	/** currencyCode1 property */
	private String sourceVcId;
	
	/** currencyCode2 property */
	private String targetVcId="CNY";

	/** rateDate property */
	private String rateDate=null;

	/**
	 * 查询开始时间
	 */
	private String startDate=null;
	/**
	 * 查询截止时间
	 */
	private String endDate=null;
	// --------------------------------------------------------- Methods
	
	/**
	 * 汇率起始用效期 
	 */
	private String starExpiDate = null;
	
	/**
	 * 汇率起始用效期
	 */
	private String overExpiDate = null;
	
	/** 
	 * Method validate
	 * @param mapping
	 * @param request
	 * @return ActionErrors
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {

		// TODO Auto-generated method stub
		return null;
	}

	/** 
	 * Method reset
	 * @param mapping
	 * @param request
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		this.erId = null;
		this.extRate = null;
		this.sourceVcId = null;
		this.targetVcId = "CNY";
		this.rateDate = null;
	}

	/** 
	 * Returns the extRate.
	 * @return Double
	 */
	public String getExtRate() {
		return extRate;
	}

	/** 
	 * Set the extRate.
	 * @param extRate The extRate to set
	 */
	public void setExtRate(String extRate) {
		this.extRate = extRate;
	}



	/** 
	 * Returns the rateDate.
	 * @return String
	 */
	public String getRateDate() {
		return rateDate;
	}

	/** 
	 * Set the rateDate.
	 * @param rateDate The rateDate to set
	 */
	public void setRateDate(String rateDate) {
		this.rateDate = rateDate;
	}


	public String getSourceVcId() {
		return sourceVcId;
	}

	public void setSourceVcId(String sourceVcId) {
		this.sourceVcId = sourceVcId;
	}

	public String getTargetVcId() {
		return targetVcId;
	}

	public void setTargetVcId(String targetVcId) {
		this.targetVcId = targetVcId;
	}

	public Long getErId() {
		return erId;
	}

	public void setErId(Long erId) {
		this.erId = erId;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getOverExpiDate() {
		return overExpiDate;
	}

	public void setOverExpiDate(String overExpiDate) {
		this.overExpiDate = overExpiDate;
	}

	public String getStarExpiDate() {
		return starExpiDate;
	}

	public void setStarExpiDate(String starExpiDate) {
		this.starExpiDate = starExpiDate;
	}
}