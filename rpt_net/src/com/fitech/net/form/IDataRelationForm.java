package com.fitech.net.form;

import org.apache.struts.action.ActionForm;

public class IDataRelationForm extends ActionForm
{

	/**
	 * 数据关联表ID（单元格ID）
	 */
	private Integer idrId = null;

	/**
	 * 关联方式
	 */
	private String idrRelative = null;

	/**
	 * 关联公式
	 */
	private String idrFormula = null;

	/**
	 * 默认值
	 */
	private String idrDefaultvalue = null;

	/**
	 * 初始化数值
	 */
	private String idrInitvalue = null;

	/**
	 * 显示业务系统生成的公式和SQL的文本域
	 */
	private String formulaText = null;

	/**
	 * SQL公式
	 */
	private String idrSql = null;

	/**
	 * 定义一个方法字段，用于ACTION的方法的跳转
	 */
	private String method = null;

	/**
	 * 报表id
	 */
	private String childRepId;

	/**
	 * 报表版本号
	 */
	private String versionId;

	/**
	 * session中的FORMBEAN是否修改或者是否是新增的标志
	 */
	private boolean isModify;

	/**
	 * 单元格名称
	 */
	private String cellName;

	/**
	 * 关联方式名称
	 */
	private String idrRelativeName;
	
	/**
	 * 币种ID
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
