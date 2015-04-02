package com.cbrc.auth.form;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form for userRole.
 * 
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 * 
 * @struts.form name="userRoleForm"
 */
public class UserRoleForm extends ActionForm {

    private final static SimpleDateFormat FORMAT = new SimpleDateFormat(
            "dd.MM.yyyy hh:mm:ss");

    private java.lang.Long _userRoleId = null;

    private java.lang.Long _roleId = null;

    private java.lang.Long _userId = null;

    /**
     * 角色名称
     */
    private String roleName = null;

    /**
     * 用户名称
     */
    private String userName = null;

    /**
     * 选中的角色id字串（之间用“，”号隔开）
     */
    private String selectedRoleIds = null;
    /**
     * 所有角色
     */
    private String allRole = null;
    /**
     * Standard constructor.
     */
    public UserRoleForm() {
    }

    /**
     * Returns the userRoleId
     * 
     * @return the userRoleId
     */
    public java.lang.Long getUserRoleId() {
        return _userRoleId;
    }

    /**
     * Sets the userRoleId
     * 
     * @param userRoleId
     *            the new userRoleId value
     */
    public void setUserRoleId(java.lang.Long userRoleId) {
        _userRoleId = userRoleId;
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
     * @param roleId
     *            the new roleId value
     */
    public void setRoleId(java.lang.Long roleId) {
        _roleId = roleId;
    }

    /**
     * Returns the userId
     * 
     * @return the userId
     */
    public java.lang.Long getUserId() {
        return _userId;
    }

    /**
     * Sets the userId
     * 
     * @param userId
     *            the new userId value
     */
    public void setUserId(java.lang.Long userId) {
        _userId = userId;
    }

    /**
     * Validate the properties that have been set from this HTTP request, and
     * return an <code>ActionErrors</code> object that encapsulates any
     * validation errors that have been found. If no errors are found, return
     * <code>null</code> or an <code>ActionErrors</code> object with no
     * recorded error messages.
     * 
     * @param mapping
     *            The mapping used to select this instance
     * @param request
     *            The servlet request we are processing
     */
    public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        return errors;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSelectedRoleIds() {
        return selectedRoleIds;
    }

    public void setSelectedRoleIds(String selectedRoleIds) {
        this.selectedRoleIds = selectedRoleIds;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getAllRole() {
        return allRole;
    }

    public void setAllRole(String allRole) {
        this.allRole = allRole;
    }

}
