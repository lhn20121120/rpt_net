package com.cbrc.auth.hibernate;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.fitech.net.hibernate.OrgNet;

/** @author Hibernate CodeGenerator */
public class Operator implements Serializable {

    /** identifier field */
    private Long userId;

    /** nullable persistent field */
    private String password;

    /** nullable persistent field */
    private String userName;

    /** nullable persistent field */
    private String firstName;

    /** nullable persistent field */
    private String lastName;

    /** nullable persistent field */
    private String mail;

    /** nullable persistent field */
    private String identificationNumber;

    /** nullable persistent field */
    private String employeeNumber;

    /** nullable persistent field */
    private String title;

    /** nullable persistent field */
    private String employeeType;

    /** nullable persistent field */
    private String branch;

    /** nullable persistent field */
    private String address;

    /** nullable persistent field */
    private String postalAddress;

    /** nullable persistent field */
    private String postalCode;

    /** nullable persistent field */
    private String fax;

    /** nullable persistent field */
    private String telephoneNumber;

    /** nullable persistent field */
    private String manager;

    /** nullable persistent field */
    private String sex;

    /** nullable persistent field */
    private String age;

    /** nullable persistent field */
    private String groupNumber;

    /** nullable persistent field */
    private Date updateTime;

    /** persistent field */
    private com.cbrc.auth.hibernate.Department department;

    /** persistent field */
    private Set userRoles;

    /** persistent field */
    private Set MUserToGrps;
    /** persistent field */
    
    private OrgNet setOrg;
    
    private OrgNet org;
    
    private String superManager;

    /** full constructor */
    public Operator(Long userId, String password, String userName, String firstName, String lastName, String mail, String identificationNumber, String employeeNumber, String title, String employeeType, String branch, String address, String postalAddress, String postalCode, String fax, String telephoneNumber, String manager, String sex, String age, String groupNumber, OrgNet org,OrgNet setOrg, Date updateTime, com.cbrc.auth.hibernate.Department department, Set userRoles, Set MUserToGrps,String superManager) {
        this.userId = userId;
        this.password = password;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mail = mail;
        this.identificationNumber = identificationNumber;
        this.employeeNumber = employeeNumber;
        this.title = title;
        this.employeeType = employeeType;
        this.branch = branch;
        this.address = address;
        this.postalAddress = postalAddress;
        this.postalCode = postalCode;
        this.fax = fax;
        this.telephoneNumber = telephoneNumber;
        this.manager = manager;
        this.sex = sex;
        this.age = age;
        this.groupNumber = groupNumber;
        this.updateTime = updateTime;
        this.department = department;
        this.userRoles = userRoles;
        this.MUserToGrps = MUserToGrps;
        this.setOrg = setOrg;
        this.org = org;
        this.superManager = superManager;
    }

    /** default constructor */
    public Operator() {
    }

    /** minimal constructor */
    public Operator(Long userId, com.cbrc.auth.hibernate.Department department, Set userRoles, Set MUserToGrps) {
        this.userId = userId;
        this.department = department;
        this.userRoles = userRoles;
        this.MUserToGrps = MUserToGrps;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMail() {
        return this.mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getIdentificationNumber() {
        return this.identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public String getEmployeeNumber() {
        return this.employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEmployeeType() {
        return this.employeeType;
    }

    public void setEmployeeType(String employeeType) {
        this.employeeType = employeeType;
    }

    public String getBranch() {
        return this.branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalAddress() {
        return this.postalAddress;
    }

    public void setPostalAddress(String postalAddress) {
        this.postalAddress = postalAddress;
    }

    public String getPostalCode() {
        return this.postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getFax() {
        return this.fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getTelephoneNumber() {
        return this.telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getManager() {
        return this.manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getSex() {
        return this.sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return this.age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGroupNumber() {
        return this.groupNumber;
    }

    public void setGroupNumber(String groupNumber) {
        this.groupNumber = groupNumber;
    }

    public Date getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public com.cbrc.auth.hibernate.Department getDepartment() {
        return this.department;
    }

    public void setDepartment(com.cbrc.auth.hibernate.Department department) {
        this.department = department;
    }

    public Set getUserRoles() {
        return this.userRoles;
    }

    public void setUserRoles(Set userRoles) {
        this.userRoles = userRoles;
    }

    public Set getMUserToGrps() {
        return this.MUserToGrps;
    }

    public void setMUserToGrps(Set MUserToGrps) {
        this.MUserToGrps = MUserToGrps;
    }
    
	public OrgNet getOrg() {
		return org;
	}

	public void setOrg(OrgNet org) {
		this.org = org;
	}

	public OrgNet getSetOrg() {
		return setOrg;
	}

	public void setSetOrg(OrgNet setOrg) {
		this.setOrg = setOrg;
	}
	
	public String getSuperManager() {
		return superManager;
	}

	public void setSuperManager(String superManager) {
		this.superManager = superManager;
	}

	public String toString() {
        return new ToStringBuilder(this)
            .append("userId", getUserId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof Operator) ) return false;
        Operator castOther = (Operator) other;
        return new EqualsBuilder()
            .append(this.getUserId(), castOther.getUserId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getUserId())
            .toHashCode();
    }

}
