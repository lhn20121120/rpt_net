//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_4.1.1/xslt/JavaClass.xsl

package com.fitech.specval.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
/** 
 * ExchangeRateForm
 * @author	cb
 * @date	2007-07-27
 * 
 */
public class SpecialValForm extends ActionForm {

	// --------------------------------------------------------- Instance Variables
	private static final long serialVersionUID = 1L;

	private Long speValId;
	private String valName;
	private String valFormula;
	private String valDes;
	private Long valFlag;
	private String bak1;
	private String bak2;
	
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
		this.speValId = null;
		this.valName = null;
		this.valFormula = null;
		this.valDes = null;
		this.valFlag = null;
		this.bak1 = null;
		this.bak2 = null;
	}

	public Long getSpeValId() {
		return speValId;
	}

	public void setSpeValId(Long speValId) {
		this.speValId = speValId;
	}

	public String getValName() {
		return valName;
	}

	public void setValName(String valName) {
		this.valName = valName;
	}

	public String getValFormula() {
		return valFormula;
	}

	public void setValFormula(String valFormula) {
		this.valFormula = valFormula;
	}

	public String getValDes() {
		return valDes;
	}

	public void setValDes(String valDes) {
		this.valDes = valDes;
	}

	public Long getValFlag() {
		return valFlag;
	}

	public void setValFlag(Long valFlag) {
		this.valFlag = valFlag;
	}

	public String getBak1() {
		return bak1;
	}

	public void setBak1(String bak1) {
		this.bak1 = bak1;
	}

	public String getBak2() {
		return bak2;
	}

	public void setBak2(String bak2) {
		this.bak2 = bak2;
	}


}