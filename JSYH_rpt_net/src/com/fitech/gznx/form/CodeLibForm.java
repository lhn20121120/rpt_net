package com.fitech.gznx.form;

import org.apache.struts.action.ActionForm;

public class CodeLibForm extends ActionForm {
	/**字典类型ID*/
	private String codeTypeId;
	/**字典ID*/
    private String codeId;
    /**字典类型值*/
	private String codeTypeValue;
	
	private String typeName;
	/**字典值*/
    private String codeValue;
    /**生效日期*/
    private String effectiveDate;
    /**是否允许修改(0-不可以,1-可以)*/
    private Integer isModi;
    /**备注*/
    private String codeDesc;
    
    /**字典类型ID 修改*/
	private String codeTypeId_upd;
	/**字典ID 修改*/
    private String codeId_upd;
    /**生效日期 修改*/
    private String effectiveDate_upd;
    
	/**当前页码,查询*/
	private int curPage;

	

	public int getCurPage() {
		return curPage;
	}

	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}

	public String getCodeId() {
		return codeId;
	}

	public void setCodeId(String codeId) {
		this.codeId = codeId;
	}

	public String getCodeTypeId() {
		return codeTypeId;
	}

	public void setCodeTypeId(String codeTypeId) {
		this.codeTypeId = codeTypeId;
	}

	public String getCodeTypeValue() {
		return codeTypeValue;
	}

	public void setCodeTypeValue(String codeTypeValue) {
		this.codeTypeValue = codeTypeValue;
	}

	public String getCodeValue() {
		return codeValue;
	}

	public void setCodeValue(String codeValue) {
		this.codeValue = codeValue;
	}

	public String getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public Integer getIsModi() {
		return isModi;
	}

	public void setIsModi(Integer isModi) {
		this.isModi = isModi;
	}

	public String getCodeDesc() {
		return codeDesc;
	}

	public void setCodeDesc(String codeDesc) {
		this.codeDesc = codeDesc;
	}

	public String getCodeId_upd() {
		return codeId_upd;
	}

	public void setCodeId_upd(String codeId_upd) {
		this.codeId_upd = codeId_upd;
	}

	public String getCodeTypeId_upd() {
		return codeTypeId_upd;
	}

	public void setCodeTypeId_upd(String codeTypeId_upd) {
		this.codeTypeId_upd = codeTypeId_upd;
	}

	public String getEffectiveDate_upd() {
		return effectiveDate_upd;
	}

	public void setEffectiveDate_upd(String effectiveDate_upd) {
		this.effectiveDate_upd = effectiveDate_upd;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
}
