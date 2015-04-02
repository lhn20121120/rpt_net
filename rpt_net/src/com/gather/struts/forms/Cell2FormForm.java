package com.gather.struts.forms;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class Cell2FormForm  extends ActionForm {

    private Integer cell2FormId;
    private Integer cellFormId;
    private String childRepId;
    private String versionId;
    private Integer state;
	/**
	 * @return Returns the cell2FormId.
	 */
	public Integer getCell2FormId() {
		return cell2FormId;
	}
	/**
	 * @param cell2FormId The cell2FormId to set.
	 */
	public void setCell2FormId(Integer cell2FormId) {
		this.cell2FormId = cell2FormId;
	}
	/**
	 * @return Returns the cellFormId.
	 */
	public Integer getCellFormId() {
		return cellFormId;
	}
	/**
	 * @param cellFormId The cellFormId to set.
	 */
	public void setCellFormId(Integer cellFormId) {
		this.cellFormId = cellFormId;
	}
	/**
	 * @return Returns the childRepId.
	 */
	public String getChildRepId() {
		return childRepId;
	}
	/**
	 * @param childRepId The childRepId to set.
	 */
	public void setChildRepId(String childRepId) {
		this.childRepId = childRepId;
	}
	/**
	 * @return Returns the state.
	 */
	public Integer getState() {
		return state;
	}
	/**
	 * @param state The state to set.
	 */
	public void setState(Integer state) {
		this.state = state;
	}
	/**
	 * @return Returns the versionId.
	 */
	public String getVersionId() {
		return versionId;
	}
	/**
	 * @param versionId The versionId to set.
	 */
	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		return null;
    }

}
