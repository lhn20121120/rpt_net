
package com.cbrc.auth.form;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form for operator.
 *
 * @author <a href="http://boss.bekk.no/boss/middlegen/"/>Middlegen</a>
 *
 * @struts.form name="operatorForm"
 */
public class OperatorForm extends ActionForm implements Serializable{

   
	private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

	private java.lang.Long _userId = null;  
	private java.lang.String _password = null;   
	private java.lang.String _userName = null;   
	private java.lang.String _firstName = null;   
	private java.lang.String _lastName = null;   
	private java.lang.String _mail = null;   
	private java.lang.String _identificationNumber = null;   
	private java.lang.String _employeeNumber = null;   
	private java.lang.String _title = null;   
	private java.lang.String _employeeType = null;   
	private java.lang.String _branch = null;   
	private java.lang.String _address = null;   
	private java.lang.String _postalAddress = null;   
	private java.lang.String _postalCode = null;   
	private java.lang.String _fax = null;   
	private java.lang.String _telephoneNumber = null;   
	private java.lang.String _manager = null;   
	private java.lang.String _sex = null;   
	private java.lang.String _age = null;   
	private java.lang.String _groupNumber = null;   
	private java.util.Date _updateTime = null;  
	private java.lang.Long _departmentId = null;      
	/**
	 * 部门名称
	 */
	private String deptName = null;
	/**
	 * 设定机构ID
	 */
	private String setOrgId = null;
    /**
     * 设定机构名称
     */   
	private String setOrgName = null;
    /**
     * 用户机构ID
     */   
	private String orgId = null;
    /**
     * 用户机构名称
     */  
	private String orgName = null;
    /**
     * 机构名称
     */   
	private String productUserName = null;  
	/**
	 * 超级用户
	 */
	private String superManager = null;
    /**
     * 重复密码
     */   
	private String password1 = null;
    /**
     * 旧密码
     */   
	private String passwordOld = null;   
	
	private String loginDate = null;
   
	public String getPasswordOld() {		   
		return passwordOld;	   
	}
	
	public void setPasswordOld(String passwordOld) {	
		this.passwordOld = passwordOld;	   
	}	   
	
	public String getPassword1() {	
		return password1;	   
	}
	    	
	public void setPassword1(String password1) {	
		this.password1 = password1;	   
	}
	
	/**
     * Standard constructor.
     */   
	public OperatorForm() {   
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
     * @param userId the new userId value
     */   
	public void setUserId(java.lang.Long userId) {    
		_userId = userId;   
	}
    /**
     * Returns the password
     *
     * @return the password
     */   
	public java.lang.String getPassword() {    
		return _password; 
	}
    /**
     * Sets the password
     *
     * @param password the new password value
     */   
	public void setPassword(java.lang.String password) {    
		_password = password;   
	}
   
	/**
     * Returns the userName
     *
     * @return the userName
     */   
	public java.lang.String getUserName() {    
		return _userName;   
	}

    /**
     * Sets the userName
     *
     * @param userName the new userName value
     */  
	public void setUserName(java.lang.String userName) {    
		_userName = userName;   
	}
    /**
     * Returns the firstName
     *
     * @return the firstName
     */   
	public java.lang.String getFirstName() {   
		return _firstName;
	}

    /**
     * Sets the firstName
     *
     * @param firstName the new firstName value
     */   
	public void setFirstName(java.lang.String firstName) {    
		_firstName = firstName;   
	}
    /**
     * Returns the lastName
     *
     * @return the lastName
     */   
	public java.lang.String getLastName() {    
		return _lastName;
	}

    /**
     * Sets the lastName
     *
     * @param lastName the new lastName value
     */   
	public void setLastName(java.lang.String lastName) {    
		_lastName = lastName;   
	}
    /**
     * Returns the mail
     *
     * @return the mail
     */   
	public java.lang.String getMail() {    
		return _mail;   
	}

    /**
     * Sets the mail
     *
     * @param mail the new mail value
     */   
	public void setMail(java.lang.String mail) {    
		_mail = mail;   
	}
    /**
     * Returns the identificationNumber
     *
     * @return the identificationNumber
     */   
	public java.lang.String getIdentificationNumber() {    
		return _identificationNumber;   
	}

    /**
     * Sets the identificationNumber
     *
     * @param identificationNumber the new identificationNumber value
     */   
	public void setIdentificationNumber(java.lang.String identificationNumber) {    
		_identificationNumber = identificationNumber;   
	}
    /**
     * Returns the employeeNumber
     *
     * @return the employeeNumber
     */   
	public java.lang.String getEmployeeNumber() {    
		return _employeeNumber;   
	}

   /**
    * Sets the employeeNumber
    *
    * @param employeeNumber the new employeeNumber value
    */
   public void setEmployeeNumber(java.lang.String employeeNumber) {
      _employeeNumber = employeeNumber;
   }
   /**
    * Returns the title
    *
    * @return the title
    */
   public java.lang.String getTitle() {
      return _title;
   }

   /**
    * Sets the title
    *
    * @param title the new title value
    */
   public void setTitle(java.lang.String title) {
      _title = title;
   }
   /**
    * Returns the employeeType
    *
    * @return the employeeType
    */
   public java.lang.String getEmployeeType() {
      return _employeeType;
   }

   /**
    * Sets the employeeType
    *
    * @param employeeType the new employeeType value
    */
   public void setEmployeeType(java.lang.String employeeType) {
      _employeeType = employeeType;
   }
   /**
    * Returns the branch
    *
    * @return the branch
    */
   public java.lang.String getBranch() {
      return _branch;
   }

   /**
    * Sets the branch
    *
    * @param branch the new branch value
    */
   public void setBranch(java.lang.String branch) {
      _branch = branch;
   }
   /**
    * Returns the address
    *
    * @return the address
    */
   public java.lang.String getAddress() {
      return _address;
   }

   /**
    * Sets the address
    *
    * @param address the new address value
    */
   public void setAddress(java.lang.String address) {
      _address = address;
   }
   /**
    * Returns the postalAddress
    *
    * @return the postalAddress
    */
   public java.lang.String getPostalAddress() {
      return _postalAddress;
   }

   /**
    * Sets the postalAddress
    *
    * @param postalAddress the new postalAddress value
    */
   public void setPostalAddress(java.lang.String postalAddress) {
      _postalAddress = postalAddress;
   }
   /**
    * Returns the postalCode
    *
    * @return the postalCode
    */
   public java.lang.String getPostalCode() {
      return _postalCode;
   }

   /**
    * Sets the postalCode
    *
    * @param postalCode the new postalCode value
    */
   public void setPostalCode(java.lang.String postalCode) {
      _postalCode = postalCode;
   }
   /**
    * Returns the fax
    *
    * @return the fax
    */
   public java.lang.String getFax() {
      return _fax;
   }

   /**
    * Sets the fax
    *
    * @param fax the new fax value
    */
   public void setFax(java.lang.String fax) {
      _fax = fax;
   }
   /**
    * Returns the telephoneNumber
    *
    * @return the telephoneNumber
    */
   public java.lang.String getTelephoneNumber() {
      return _telephoneNumber;
   }

   /**
    * Sets the telephoneNumber
    *
    * @param telephoneNumber the new telephoneNumber value
    */
   public void setTelephoneNumber(java.lang.String telephoneNumber) {
      _telephoneNumber = telephoneNumber;
   }
   /**
    * Returns the manager
    *
    * @return the manager
    */
   public java.lang.String getManager() {
      return _manager;
   }

   /**
    * Sets the manager
    *
    * @param manager the new manager value
    */
   public void setManager(java.lang.String manager) {
      _manager = manager;
   }
   /**
    * Returns the sex
    *
    * @return the sex
    */
   public java.lang.String getSex() {
      return _sex;
   }

   /**
    * Sets the sex
    *
    * @param sex the new sex value
    */
   public void setSex(java.lang.String sex) {
      _sex = sex;
   }
   /**
    * Returns the age
    *
    * @return the age
    */
   public java.lang.String getAge() {
      return _age;
   }

   /**
    * Sets the age
    *
    * @param age the new age value
    */
   public void setAge(java.lang.String age) {
      _age = age;
   }
   /**
    * Returns the groupNumber
    *
    * @return the groupNumber
    */
   public java.lang.String getGroupNumber() {
      return _groupNumber;
   }

   /**
    * Sets the groupNumber
    *
    * @param groupNumber the new groupNumber value
    */
   public void setGroupNumber(java.lang.String groupNumber) {
      _groupNumber = groupNumber;
   }
   /**
    * Returns the updateTime
    *
    * @return the updateTime
    */
   public Date getUpdateTime() {
      return _updateTime;
   }

   /**
    * Sets the updateTime
    *
    * @param updateTime the new updateTime value
    */
   public void setUpdateTime(Date updateTime) {
      _updateTime = updateTime;
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
	 * @return Returns the orgId.
	 */
	public String getOrgId() {
		return orgId;
	}
	/**
	 * @param orgId The orgId to set.
	 */
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	/**
	 * @return Returns the orgName.
	 */
	public String getOrgName() {
		return orgName;
	}
	/**
	 * @param orgName The orgName to set.
	 */
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	/**
	 * @return Returns the setOrgId.
	 */
	public String getSetOrgId() {
		return setOrgId;
	}
	/**
	 * @param setOrgId The setOrgId to set.
	 */
	public void setSetOrgId(String setOrgId) {
		this.setOrgId = setOrgId;
	}
	/**
	 * @return Returns the setOrgName.
	 */
	public String getSetOrgName() {
		return setOrgName;
	}
	/**
	 * @param setOrgName The setOrgName to set.
	 */
	public void setSetOrgName(String setOrgName) {
		this.setOrgName = setOrgName;
	}

    /**
	 * @return Returns the superManager.
	 */
	public String getSuperManager() {
		return this.superManager;
	}

	/**
	 * @param superManager The superManager to set.
	 */
	public void setSuperManager(String superManager) {
		this.superManager = superManager;
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

	public String getDeptName() {
	    return deptName;
	}
	
	public void setDeptName(String deptName) {
	    this.deptName = deptName;
	}
	
	public String getProductUserName() {
	    return productUserName;
	}
	
	public void setProductUserName(String productUserName) {
	    this.productUserName = productUserName;
	}

	public String getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(String loginDate) {
		this.loginDate = loginDate;
	}	
}
