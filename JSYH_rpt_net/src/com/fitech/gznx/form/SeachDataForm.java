package com.fitech.gznx.form;

import java.util.List;

import org.apache.struts.action.ActionForm;

import com.cbrc.smis.form.MCurrForm;

public class SeachDataForm extends ActionForm{
	/***
	 * ±“÷÷ºØ∫œ
	 */
	private List<MCurrForm> currFormList;

	public List<MCurrForm> getCurrFormList() {
		return currFormList;
	}

	public void setCurrFormList(List<MCurrForm> currFormList) {
		this.currFormList = currFormList;
	}
	
}
