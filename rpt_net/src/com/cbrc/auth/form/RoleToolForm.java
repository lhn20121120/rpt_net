
package com.cbrc.auth.form;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form for roleTool.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 *
 * @struts.form name="roleToolForm"
 */
public class RoleToolForm extends ActionForm {

   private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

   private java.lang.Long _roleToolId = null;
   private java.lang.Long _roleId = null;
   private java.lang.Long _menuId = null;
   
   /**
    * 角色名称
    */
   private String roleName =null;
   /**
    * 菜单名称
    */
   private String menuName = null;
   /**
    * 该角色设置的权限菜单id
    */
   private String selectedMenuIds = null;
   
   
   public String getRoleName() {
       return roleName;
    }
    
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    
    public String getSelectedMenuIds() {
        return selectedMenuIds;
    }
    
    public void setSelectedMenuIds(String selectedMenuIds) {
        this.selectedMenuIds = selectedMenuIds;
    }
    
    
    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

/**
    * Standard constructor.
    */
   public RoleToolForm() {
   }

   /**
    * Returns the roleToolId
    *
    * @return the roleToolId
    */
   public java.lang.Long getRoleToolId() {
      return _roleToolId;
   }

   /**
    * Sets the roleToolId
    *
    * @param roleToolId the new roleToolId value
    */
   public void setRoleToolId(java.lang.Long roleToolId) {
      _roleToolId = roleToolId;
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
    * Returns the menuId
    *
    * @return the menuId
    */
   public java.lang.Long getMenuId() {
      return _menuId;
   }

   /**
    * Sets the menuId
    *
    * @param menuId the new menuId value
    */
   public void setMenuId(java.lang.Long menuId) {
      _menuId = menuId;
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
