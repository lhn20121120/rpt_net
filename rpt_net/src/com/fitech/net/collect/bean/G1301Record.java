package com.fitech.net.collect.bean;

public class G1301Record {

	//�ͻ�����
	private String cusName;
	
	//�ͻ�����
	private String cusId;
	
	//��������
	private double loan;  //double
	
	//����
	private double corpus;  //double
	
	//��Ϣ
	private double interest;  //double
	
	//�����ʱ��
	private String year; //int
	
	//ר��׼����
	private double special;  //double
	
	//������������
	private double loan_other; //double
	
	//������������
	private double in_other;  //double
	
	//��ŵ���и�ծ
	private double promise;  //double
	
	//����������Ŀ
	private double out_other;  //double
	
	//��֤��
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
