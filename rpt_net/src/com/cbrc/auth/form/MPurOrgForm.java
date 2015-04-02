
package com.cbrc.auth.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form for MPurOrgForm.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 *
 * @struts.form name="MPurOrgForm"
 */
public class MPurOrgForm extends ActionForm {
	/**
	 * 机构id
	 */
	private java.lang.String orgId = null;
	/**
	 * 机构名称
	 */
	private java.lang.String orgName = null;
	/**
	 * 用户组ID
	 */   
	private java.lang.Long userGrpId = null;   
	/**
	 * 报表ID
	 */
	private java.lang.String childRepId = null;
	/**
	 * 权限类型
	 */
	private java.lang.Integer powType = null;
	
	/**
	 * Standard constructor.
	 */  
	public MPurOrgForm() {   
	}
	
	/**
	 * @return Returns the orgId.
	 */
	public java.lang.String getOrgId() {
		return orgId;
	}


	/**
	 * @param orgId The orgId to set.
	 */
	public void setOrgId(java.lang.String orgId) {
		this.orgId = orgId;
	}


	/**
	 * @return Returns the orgName.
	 */
	public java.lang.String getOrgName() {
		return orgName;
	}


	/**
	 * @param orgName The orgName to set.
	 */
	public void setOrgName(java.lang.String orgName) {
		this.orgName = orgName;
	}


	/**
	 * @return Returns the childRepId.
	 */
	public java.lang.String getChildRepId() {
		return childRepId;
	}

	/**
	 * @param childRepId The childRepId to set.
	 */
	public void setChildRepId(java.lang.String childRepId) {
		this.childRepId = childRepId;
	}

	/**
	 * @return Returns the userGrpId.
	 */
	public java.lang.Long getUserGrpId() {
		return userGrpId;
	}

	/**
	 * @param userGrpId The userGrpId to set.
	 */
	public void setUserGrpId(java.lang.Long userGrpId) {
		this.userGrpId = userGrpId;
	}

	/**
	 * @return Returns the powType.
	 */
	public java.lang.Integer getPowType() {
		return powType;
	}

	/**
	 * @param powType The powType to set.
	 */
	public void setPowType(java.lang.Integer powType) {
		this.powType = powType;
	}

	/**
	 * Validate the properties that have been set from this HTTP request,
	 * and return an <code>ActionErrors</code> object that encapsulates any
	 * validation errors that have been found.  If no errors are found, return
	 * <code>null</code> or an <code>ActionErrors</code> object with no
	 * recorded error messages.
	 *
	 * @param mapping The mapping used to select this instance
	 * @param request The servlet request we are processing
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {    
		ActionErrors errors = new ActionErrors();      
		return errors;   
	}
}
