
package com.cbrc.auth.form;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form for role.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 *
 * @struts.form name="roleForm"
 */
public class RoleForm extends ActionForm {

	private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
	private java.lang.Long _roleId = null;   
	private java.lang.String _roleName = null;   
	private java.lang.String setOrgId = null;   
	private java.lang.String setOrgName = null;

	/**
	 * Standard constructor.
	 */
	public RoleForm() {	
	}

	public java.lang.String getSetOrgId() {	
		return setOrgId;   
	}

	public void setSetOrgId(java.lang.String setOrgId) {
		this.setOrgId = setOrgId;
	}
	
	public java.lang.String getSetOrgName() {
		return setOrgName;
	}

	public void setSetOrgName(java.lang.String setOrgName) {
		this.setOrgName = setOrgName;
	}

   /**
    * Returns the roleId
    *
    * @return the roleId
    */
	public java.lang.Long getRoleId() {    
		return _roleId;   
	}
   
	/**
     * Sets the roleId
     *
     * @param roleId the new roleId value
     */
   
	public void setRoleId(java.lang.Long roleId) {    
		_roleId = roleId;   
	}
      
	/**
	 * Returns the roleName
	 *
	 * @return the roleName
	 */
	public java.lang.String getRoleName() {    
		return _roleName;   
	}
     
	/**
	 * Sets the roleName
	 *
	 * @param roleName the new roleName value
	 */
	public void setRoleName(java.lang.String roleName) {    
		_roleName = roleName;   
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
