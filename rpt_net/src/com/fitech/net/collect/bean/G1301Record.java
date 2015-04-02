package com.fitech.net.collect.bean;

public class G1301Record {

	//客户名称
	private String cusName;
	
	//客户代码
	private String cusId;
	
	//贷款数量
	private double loan;  //double
	
	//本金
	private double corpus;  //double
	
	//利息
	private double interest;  //double
	
	//逾期最长时间
	private String year; //int
	
	//专项准备金
	private double special;  //double
	
	//其他类贷款余额
	private double loan_other; //double
	
	//其他表内授信
	private double in_other;  //double
	
	//承诺或有负债
	private double promise;  //double
	
	//其他表外项目
	private double out_other;  //double
	
	//保证金
	private double bail;

	public double getBail() {
		return bail;
	}

	public void setBail(double bail) {
		this.bail = bail;
	}

	public double getCorpus() {
		return corpus;
	}

	public void setCorpus(double corpus) {
		this.corpus = corpus;
	}

	public String getCusId() {
		return cusId;
	}

	public void setCusId(String cusId) {
		this.cusId = cusId;
	}

	public String getCusName() {
		return cusName;
	}

	public void setCusName(String cusName) {
		this.cusName = cusName;
	}

	public double getIn_other() {
		return in_other;
	}

	public void setIn_other(double in_other) {
		this.in_other = in_other;
	}

	public double getInterest() {
		return interest;
	}

	public void setInterest(double interest) {
		this.interest = interest;
	}

	public double getLoan() {
		return loan;
	}

	public void setLoan(double loan) {
		this.loan = loan;
	}

	public double getLoan_other() {
		return loan_other;
	}

	public void setLoan_other(double loan_other) {
		this.loan_other = loan_other;
	}

	public double getOut_other() {
		return out_other;
	}

	public void setOut_other(double out_other) {
		this.out_other = out_other;
	}

	public double getPromise() {
		return promise;
	}

	public void setPromise(double promise) {
		this.promise = promise;
	}

	public double getSpecial() {
		return special;
	}

	public void setSpecial(double special) {
		this.special = special;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}
	
	
}
