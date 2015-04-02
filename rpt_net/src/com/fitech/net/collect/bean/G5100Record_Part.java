package com.fitech.net.collect.bean;

/**
 *
 * @author masclnj
 *
 */
public class G5100Record_Part 
{
	/**
	 * 授信额度
	 */
	private String truth;
	
	
	/**
	 * 贷款余额
	 */
	private String loan;
	
	
	/**
	 * 正常贷款余额
	 */
	private String normalLoan;
	
	
	/**
	 * 关注贷款余额
	 */
	private String careLoan;
	
	
	/**
	 * 次关注贷款余额
	 */
	private String caresLoan;
	
	
	/**
	 * 可疑贷款余额
	 */
	private String doubtLoan;
	
	
	/**
	 * 损失贷款余额
	 */
	private String lossLoan;
	
	
	/**
	 * 关联企业授信额度
	 */
	private String relate_truth;
	
	
	/**
	 * 关联企业贷款余额
	 */
	private String relate_loan;


	/**
	 * @return Returns the careLoan.
	 */
	public String getCareLoan() {
		return careLoan;
	}


	/**
	 * @param careLoan The careLoan to set.
	 */
	public void setCareLoan(String careLoan) {
		this.careLoan = careLoan;
	}


	/**
	 * @return Returns the caresLoan.
	 */
	public String getCaresLoan() {
		return caresLoan;
	}


	/**
	 * @param caresLoan The caresLoan to set.
	 */
	public void setCaresLoan(String caresLoan) {
		this.caresLoan = caresLoan;
	}


	/**
	 * @return Returns the doubtLoan.
	 */
	public String getDoubtLoan() {
		return doubtLoan;
	}


	/**
	 * @param doubtLoan The doubtLoan to set.
	 */
	public void setDoubtLoan(String doubtLoan) {
		this.doubtLoan = doubtLoan;
	}


	/**
	 * @return Returns the loan.
	 */
	public String getLoan() {
		return loan;
	}


	/**
	 * @param loan The loan to set.
	 */
	public void setLoan(String loan) {
		this.loan = loan;
	}


	/**
	 * @return Returns the lossLoan.
	 */
	public String getLossLoan() {
		return lossLoan;
	}


	/**
	 * @param lossLoan The lossLoan to set.
	 */
	public void setLossLoan(String lossLoan) {
		this.lossLoan = lossLoan;
	}


	/**
	 * @return Returns the normalLoan.
	 */
	public String getNormalLoan() {
		return normalLoan;
	}


	/**
	 * @param normalLoan The normalLoan to set.
	 */
	public void setNormalLoan(String normalLoan) {
		this.normalLoan = normalLoan;
	}


	/**
	 * @return Returns the relate_loan.
	 */
	public String getRelate_loan() {
		return relate_loan;
	}


	/**
	 * @param relate_loan The relate_loan to set.
	 */
	public void setRelate_loan(String relate_loan) {
		this.relate_loan = relate_loan;
	}


	/**
	 * @return Returns the relate_truth.
	 */
	public String getRelate_truth() {
		return relate_truth;
	}


	/**
	 * @param relate_truth The relate_truth to set.
	 */
	public void setRelate_truth(String relate_truth) {
		this.relate_truth = relate_truth;
	}


	/**
	 * @return Returns the truth.
	 */
	public String getTruth() {
		return truth;
	}


	/**
	 * @param truth The truth to set.
	 */
	public void setTruth(String truth) {
		this.truth = truth;
	}
}
