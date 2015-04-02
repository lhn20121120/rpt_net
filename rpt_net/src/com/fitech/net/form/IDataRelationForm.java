package com.fitech.net.form;

import org.apache.struts.action.ActionForm;

public class IDataRelationForm extends ActionForm
{

	/**
	 * ���ݹ�����ID����Ԫ��ID��
	 */
	private Integer idrId = null;

	/**
	 * ������ʽ
	 */
	private String idrRelative = null;

	/**
	 * ������ʽ
	 */
	private String idrFormula = null;

	/**
	 * Ĭ��ֵ
	 */
	private String idrDefaultvalue = null;

	/**
	 * ��ʼ����ֵ
	 */
	private String idrInitvalue = null;

	/**
	 * ��ʾҵ��ϵͳ���ɵĹ�ʽ��SQL���ı���
	 */
	private String formulaText = null;

	/**
	 * SQL��ʽ
	 */
	private String idrSql = null;

	/**
	 * ����һ�������ֶΣ�����ACTION�ķ�������ת
	 */
	private String method = null;

	/**
	 * ����id
	 */
	private String childRepId;

	/**
	 * ����汾��
	 */
	private String versionId;

	/**
	 * session�е�FORMBEAN�Ƿ��޸Ļ����Ƿ��������ı�־
	 */
	private boolean isModify;

	/**
	 * ��Ԫ������
	 */
	private String cellName;

	/**
	 * ������ʽ����
	 */
	private String idrRelativeName;
	
	/**
	 * ����ID
	 */
	private String currId;
	
	public boolean isModify()
	{
		return isModify;
	}

	public void setModify(boolean isModify)
	{
		this.isModify = isModify;
	}

	public String getChildRepId()
	{
		return childRepId;
	}

	public void setChildRepId(String childRepId)
	{
		this.childRepId = childRepId;
	}

	public String getVersionId()
	{
		return versionId;
	}

	public void setVersionId(String versionId)
	{
		this.versionId = versionId;
	}

	public String getMethod()
	{
		return method;
	}

	public void setMethod(String method)
	{
		this.method = method;
	}

	public String getIdrSql()
	{
		return idrSql;
	}

	public void setIdrSql(String idrSql)
	{
		this.idrSql = idrSql;
	}

	public String getFormulaText()
	{
		return formulaText;
	}

	public void setFormulaText(String formulaText)
	{
		this.formulaText = formulaText;
	}

	public String getIdrDefaultvalue()
	{
		return idrDefaultvalue;
	}

	public void setIdrDefaultvalue(String idrDefaultvalue)
	{
		this.idrDefaultvalue = idrDefaultvalue;
	}

	public String getIdrFormula()
	{
		return idrFormula;
	}

	public void setIdrFormula(String idrFormula)
	{
		this.idrFormula = idrFormula;
	}

	public Integer getIdrId()
	{
		return idrId;
	}

	public void setIdrId(Integer idrId)
	{
		this.idrId = idrId;
	}

	public String getIdrInitvalue()
	{
		return idrInitvalue;
	}

	public void setIdrInitvalue(String idrInitvalue)
	{
		this.idrInitvalue = idrInitvalue;
	}

	public String getIdrRelative()
	{
		return idrRelative;
	}

	public void setIdrRelative(String idrRelative)
	{
		this.idrRelative = idrRelative;
	}

	public String getCellName()
	{
		return cellName;
	}

	public void setCellName(String cellName)
	{
		this.cellName = cellName;
	}

	public String getIdrRelativeName()
	{
		return idrRelativeName;
	}

	public void setIdrRelativeName(String idrRelativeName)
	{
		this.idrRelativeName = idrRelativeName;
	}

	public String getCurrId() {
		return currId;
	}

	public void setCurrId(String currId) {
		this.currId = currId;
	}	
}
