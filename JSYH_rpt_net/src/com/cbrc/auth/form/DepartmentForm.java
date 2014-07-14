
package com.cbrc.auth.form;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form for department.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 *
 * @struts.form name="departmentForm"
 */
public class DepartmentForm extends ActionForm {

   private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
   /**
    * 部门id
    */
   private java.lang.Long _departmentId = null;
   /**
    * 部门名称
    */
   private java.lang.String _deptName = null;
   /**
    * 用户编号
    */
   private java.lang.Long _productUserId = null;

   private java.lang.String orgId = null;
   /**
    * Standard constructor.
    */
   public DepartmentForm() {
   }

   
   public java.lang.String getOrgId() {
	return orgId;
}


public void setOrgId(java.lang.String orgId) {
	this.orgId = orgId;
}


/**
    * Returns the departmentId
    *
    * @return the departmentId
    */
   public java.lang.Long getDepartmentId() {
      return _departmentId;
   }

   /**
    * Sets the departmentId
    *
    * @param departmentId the new departmentId value
    */
   public void setDepartmentId(java.lang.Long departmentId) {
      _departmentId = departmentId;
   }
   /**
    * Returns the deptName
    *
    * @return the deptName
    */
   public java.lang.String getDeptName() {
      return _deptName;
   }

   /**
    * Sets the deptName
    *
    * @param deptName the new deptName value
    */
   public void setDeptName(java.lang.String deptName) {
      _deptName = deptName;
   }
   /**
    * Returns the productUserId
    *
    * @return the productUserId
    */
   public java.lang.Long getProductUserId() {
      return _productUserId;
   }

   /**
    * Sets the productUserId
    *
    * @param productUserId the new productUserId value
    */
   public void setProductUserId(java.lang.Long productUserId) {
      _productUserId = productUserId;
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
