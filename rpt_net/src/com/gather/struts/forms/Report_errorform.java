
/** 姬怀宝
	 * This is a delegate class to handle interaction with the backend persistence layer of hibernate. 
	 * It has a set of methods to handle persistence for MRepRange data (i.e. 
	 * com.gather.struts.forms.MRepRangeForm objects).
	 * 
	 * 新增的方法
	 */
package com.gather.struts.forms;
import org.apache.struts.action.ActionForm;
/**
 * 
 * 新增的FORM对象保存validateTypeName，cellForm
 * @author FitechServer
 *
 */
public class Report_errorform extends ActionForm{
	 private java.lang.String validateTypeName = null;
	 private java.lang.String cellForm = null;
	/**
	 * @return Returns the cellForm.
	 */
	public java.lang.String getCellForm() {
		return cellForm;
	}
	/**
	 * @param cellForm The cellForm to set.
	 */
	public void setCellForm(java.lang.String cellForm) {
		this.cellForm = cellForm;
	}
	/**
	 * @return Returns the validateTypeName.
	 */
	public java.lang.String getValidateTypeName() {
		return validateTypeName;
	}
	/**
	 * @param validateTypeName The validateTypeName to set.
	 */
	public void setValidateTypeName(java.lang.String validateTypeName) {
		this.validateTypeName = validateTypeName;
	}

}
