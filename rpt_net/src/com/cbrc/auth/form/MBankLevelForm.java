
package com.cbrc.auth.form;

import org.apache.struts.action.ActionForm;

public class MBankLevelForm extends ActionForm {

	/**�м�����ID*/
	private Integer bankLevelId = null;
	/**�м���������*/
	private String bankLevelName = null;
	
	public MBankLevelForm(){
		
	}
	public MBankLevelForm(Integer bankLevelId,String bankLevelName){
		this.setBankLevelId(bankLevelId);
		this.setBankLevelName(bankLevelName);
	}
	public Integer getBankLevelId() {
		return bankLevelId;
	}
	public void setBankLevelId(Integer bankLevelId) {
		this.bankLevelId = bankLevelId;
	}
	public String getBankLevelName() {
		return bankLevelName;
	}
	public void setBankLevelName(String bankLevelName) {
		this.bankLevelName = bankLevelName;
	}	
}
