package com.fitech.gznx.form;

import org.apache.struts.action.ActionForm;

public class CodeLibForm extends ActionForm {
	/**�ֵ�����ID*/
	private String codeTypeId;
	/**�ֵ�ID*/
    private String codeId;
    /**�ֵ�����ֵ*/
	private String codeTypeValue;
	
	private String typeName;
	/**�ֵ�ֵ*/
    private String codeValue;
    /**��Ч����*/
    private String effectiveDate;
    /**�Ƿ������޸�(0-������,1-����)*/
    private Integer isModi;
    /**��ע*/
    private String codeDesc;
    
    /**�ֵ�����ID �޸�*/
	private String codeTypeId_upd;
	/**�ֵ�ID �޸�*/
    private String codeId_upd;
    /**��Ч���� �޸�*/
    private String effectiveDate_upd;
    
	/**��ǰҳ��,��ѯ*/
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
