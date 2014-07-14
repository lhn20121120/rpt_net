
package com.gather.struts.forms;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form for MOrg.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 *
 * @struts.form name="MOrgForm"
 */
public class MUserForm extends ActionForm {

   private java.lang.String userId = null;
   private java.lang.String name = null;
   private java.lang.String dept = null;
   private java.lang.String orgId = null;
   private java.lang.String password = null;

   /**
 * @return Returns the password.
 */
public java.lang.String getPassword() {
	return password;
}


/**
 * @param password The password to set.
 */
public void setPassword(java.lang.String password) {
	this.password = password;
}


/**
    * Standard constructor.
    */
   public MUserForm() {
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
      return null;
   }


/**
 * @return Returns the dept.
 */
public java.lang.String getDept() {
	return dept;
}


/**
 * @param dept The dept to set.
 */
public void setDept(java.lang.String dept) {
	this.dept = dept;
}


/**
 * @return Returns the name.
 */
public java.lang.String getName() {
	return name;
}


/**
 * @param name The name to set.
 */
public void setName(java.lang.String name) {
	this.name = name;
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
 * @return Returns the userId.
 */
public java.lang.String getUserId() {
	return userId;
}


/**
 * @param userId The userId to set.
 */
public void setUserId(java.lang.String userId) {
	this.userId = userId;
}
}
