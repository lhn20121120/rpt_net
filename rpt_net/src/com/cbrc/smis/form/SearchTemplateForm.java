
package com.cbrc.smis.form;

import org.apache.struts.action.ActionForm;


public class SearchTemplateForm extends ActionForm {
	
	/**
	 * 报表名称
	 */
	private String repName;
	
	
	
	/**
	 * 报送时间,年份
	 */
	private String year;
	
	
	
	/**
	 * 报送时间,月份
	 */
	private String month;



	/**
	 * @return Returns the month.
	 */
	public String getMonth() {
		return month;
	}



	/**
	 * @param month The month to set.
	 */
	public void setMonth(String month) {
		this.month = month;
	}



	/**
	 * @return Returns the repName.
	 */
	public String getRepName() {
		return repName;
	}



	/**
	 * @param repName The repName to set.
	 */
	public void setRepName(String repName) {
		this.repName = repName;
	}



	/**
	 * @return Returns the year.
	 */
	public String getYear() {
		return year;
	}



	/**
	 * @param year The year to set.
	 */
	public void setYear(String year) {
		this.year = year;
	}



	public SearchTemplateForm(String repName, String year, String month) {
		super();
		// TODO Auto-generated constructor stub
		this.repName = repName;
		this.year = year;
		this.month = month;
	}
	
	
	

}
