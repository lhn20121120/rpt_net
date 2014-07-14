package com.fitech.net.collect.bean;

public class S4400Record {
	//客户名称
	private String cName;
	
	//客户代码
	private String cCode;
	
	//本期末
	private String this_Last;
	
	//比上期末
	private String last_last;
	
	//贷款余额占资本总额比例
	private String rate_all;
	
	//本期末表内表外应收利息
	private String this_interest;
	
	//本期利息实际回收额
	private String true_interest;
	
	//单户贷款欠息比例
	private String single_rate;
	
	//合计
	private String all;
	
	//逾期贷款
	private String over;
	
	//呆滞贷款
	private String later;
	
	//呆账贷款
	private String bad;
	
	//不良贷款比例
	private String bad_rate;

	/**
	 * @return Returns the all.
	 */
	public String getAll() {
		return all;
	}

	/**
	 * @param all The all to set.
	 */
	public void setAll(String all) {
		this.all = all;
	}

	/**
	 * @return Returns the bad.
	 */
	public String getBad() {
		return bad;
	}

	/**
	 * @param bad The bad to set.
	 */
	public void setBad(String bad) {
		this.bad = bad;
	}

	/**
	 * @return Returns the bad_rate.
	 */
	public String getBad_rate() {
		return bad_rate;
	}

	/**
	 * @param bad_rate The bad_rate to set.
	 */
	public void setBad_rate(String bad_rate) {
		this.bad_rate = bad_rate;
	}

	/**
	 * @return Returns the cCode.
	 */
	public String getCCode() {
		return cCode;
	}

	/**
	 * @param code The cCode to set.
	 */
	public void setCCode(String code) {
		cCode = code;
	}

	/**
	 * @return Returns the cName.
	 */
	public String getCName() {
		return cName;
	}

	/**
	 * @param name The cName to set.
	 */
	public void setCName(String name) {
		cName = name;
	}

	/**
	 * @return Returns the last_last.
	 */
	public String getLast_last() {
		return last_last;
	}

	/**
	 * @param last_last The last_last to set.
	 */
	public void setLast_last(String last_last) {
		this.last_last = last_last;
	}

	/**
	 * @return Returns the later.
	 */
	public String getLater() {
		return later;
	}

	/**
	 * @param later The later to set.
	 */
	public void setLater(String later) {
		this.later = later;
	}

	/**
	 * @return Returns the over.
	 */
	public String getOver() {
		return over;
	}

	/**
	 * @param over The over to set.
	 */
	public void setOver(String over) {
		this.over = over;
	}

	/**
	 * @return Returns the rate_all.
	 */
	public String getRate_all() {
		return rate_all;
	}

	/**
	 * @param rate_all The rate_all to set.
	 */
	public void setRate_all(String rate_all) {
		this.rate_all = rate_all;
	}

	/**
	 * @return Returns the single_rate.
	 */
	public String getSingle_rate() {
		return single_rate;
	}

	/**
	 * @param single_rate The single_rate to set.
	 */
	public void setSingle_rate(String single_rate) {
		this.single_rate = single_rate;
	}

	/**
	 * @return Returns the this_interest.
	 */
	public String getThis_interest() {
		return this_interest;
	}

	/**
	 * @param this_interest The this_interest to set.
	 */
	public void setThis_interest(String this_interest) {
		this.this_interest = this_interest;
	}

	/**
	 * @return Returns the this_Last.
	 */
	public String getThis_Last() {
		return this_Last;
	}

	/**
	 * @param this_Last The this_Last to set.
	 */
	public void setThis_Last(String this_Last) {
		this.this_Last = this_Last;
	}

	/**
	 * @return Returns the true_interest.
	 */
	public String getTrue_interest() {
		return true_interest;
	}

	/**
	 * @param true_interest The true_interest to set.
	 */
	public void setTrue_interest(String true_interest) {
		this.true_interest = true_interest;
	}

}
