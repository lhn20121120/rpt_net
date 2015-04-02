package com.fitech.model.worktask.model.pojo;

import java.util.Date;

/**
 * ViewWorkTaskOperatorId entity. @author MyEclipse Persistence Tools
 */

public class ViewWorkTaskOperatorId implements java.io.Serializable {

	// Fields

	private Long userId;
	private String password;
	private String userName;
	private String firstName;
	private String lastName;
	private String mail;
	private String identificationNumber;
	private String employeeNumber;
	private String title;
	private String employeeType;
	private String branch;
	private String address;
	private String postalAddress;
	private String postalCode;
	private String fax;
	private String telephoneNumber;
	private String manager;
	private String sex;
	private String age;
	private String groupNumber;
	private Date updateTime;
	private Long departmentId;
	private String orgId;
	private String setOrgId;
	private String superManager;

	// Constructors

	/** default constructor */
	public ViewWorkTaskOperatorId() {
	}

	/** minimal constructor */
	public ViewWorkTaskOperatorId(Long userId) {
		this.userId = userId;
	}

	/** full constructor */
	public ViewWorkTaskOperatorId(Long userId, String password,
			String userName, String firstName, String lastName, String mail,
			String identificationNumber, String employeeNumber, String title,
			String employeeType, String branch, String address,
			String postalAddress, String postalCode, String fax,
			String telephoneNumber, String manager, String sex, String age,
			String groupNumber, Date updateTime, Long departmentId,
			String orgId, String setOrgId, String superManager) {
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
		this.departmentId = departmentId;
		this.orgId = orgId;
		this.setOrgId = setOrgId;
		this.superManager = superManager;
	}

	// Property accessors

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

	public Long getDepartmentId() {
		return this.departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public String getOrgId() {
		return this.orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getSetOrgId() {
		return this.setOrgId;
	}

	public void setSetOrgId(String setOrgId) {
		this.setOrgId = setOrgId;
	}

	public String getSuperManager() {
		return this.superManager;
	}

	public void setSuperManager(String superManager) {
		this.superManager = superManager;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ViewWorkTaskOperatorId))
			return false;
		ViewWorkTaskOperatorId castOther = (ViewWorkTaskOperatorId) other;

		return ((this.getUserId() == castOther.getUserId()) || (this
				.getUserId() != null
				&& castOther.getUserId() != null && this.getUserId().equals(
				castOther.getUserId())))
				&& ((this.getPassword() == castOther.getPassword()) || (this
						.getPassword() != null
						&& castOther.getPassword() != null && this
						.getPassword().equals(castOther.getPassword())))
				&& ((this.getUserName() == castOther.getUserName()) || (this
						.getUserName() != null
						&& castOther.getUserName() != null && this
						.getUserName().equals(castOther.getUserName())))
				&& ((this.getFirstName() == castOther.getFirstName()) || (this
						.getFirstName() != null
						&& castOther.getFirstName() != null && this
						.getFirstName().equals(castOther.getFirstName())))
				&& ((this.getLastName() == castOther.getLastName()) || (this
						.getLastName() != null
						&& castOther.getLastName() != null && this
						.getLastName().equals(castOther.getLastName())))
				&& ((this.getMail() == castOther.getMail()) || (this.getMail() != null
						&& castOther.getMail() != null && this.getMail()
						.equals(castOther.getMail())))
				&& ((this.getIdentificationNumber() == castOther
						.getIdentificationNumber()) || (this
						.getIdentificationNumber() != null
						&& castOther.getIdentificationNumber() != null && this
						.getIdentificationNumber().equals(
								castOther.getIdentificationNumber())))
				&& ((this.getEmployeeNumber() == castOther.getEmployeeNumber()) || (this
						.getEmployeeNumber() != null
						&& castOther.getEmployeeNumber() != null && this
						.getEmployeeNumber().equals(
								castOther.getEmployeeNumber())))
				&& ((this.getTitle() == castOther.getTitle()) || (this
						.getTitle() != null
						&& castOther.getTitle() != null && this.getTitle()
						.equals(castOther.getTitle())))
				&& ((this.getEmployeeType() == castOther.getEmployeeType()) || (this
						.getEmployeeType() != null
						&& castOther.getEmployeeType() != null && this
						.getEmployeeType().equals(castOther.getEmployeeType())))
				&& ((this.getBranch() == castOther.getBranch()) || (this
						.getBranch() != null
						&& castOther.getBranch() != null && this.getBranch()
						.equals(castOther.getBranch())))
				&& ((this.getAddress() == castOther.getAddress()) || (this
						.getAddress() != null
						&& castOther.getAddress() != null && this.getAddress()
						.equals(castOther.getAddress())))
				&& ((this.getPostalAddress() == castOther.getPostalAddress()) || (this
						.getPostalAddress() != null
						&& castOther.getPostalAddress() != null && this
						.getPostalAddress()
						.equals(castOther.getPostalAddress())))
				&& ((this.getPostalCode() == castOther.getPostalCode()) || (this
						.getPostalCode() != null
						&& castOther.getPostalCode() != null && this
						.getPostalCode().equals(castOther.getPostalCode())))
				&& ((this.getFax() == castOther.getFax()) || (this.getFax() != null
						&& castOther.getFax() != null && this.getFax().equals(
						castOther.getFax())))
				&& ((this.getTelephoneNumber() == castOther
						.getTelephoneNumber()) || (this.getTelephoneNumber() != null
						&& castOther.getTelephoneNumber() != null && this
						.getTelephoneNumber().equals(
								castOther.getTelephoneNumber())))
				&& ((this.getManager() == castOther.getManager()) || (this
						.getManager() != null
						&& castOther.getManager() != null && this.getManager()
						.equals(castOther.getManager())))
				&& ((this.getSex() == castOther.getSex()) || (this.getSex() != null
						&& castOther.getSex() != null && this.getSex().equals(
						castOther.getSex())))
				&& ((this.getAge() == castOther.getAge()) || (this.getAge() != null
						&& castOther.getAge() != null && this.getAge().equals(
						castOther.getAge())))
				&& ((this.getGroupNumber() == castOther.getGroupNumber()) || (this
						.getGroupNumber() != null
						&& castOther.getGroupNumber() != null && this
						.getGroupNumber().equals(castOther.getGroupNumber())))
				&& ((this.getUpdateTime() == castOther.getUpdateTime()) || (this
						.getUpdateTime() != null
						&& castOther.getUpdateTime() != null && this
						.getUpdateTime().equals(castOther.getUpdateTime())))
				&& ((this.getDepartmentId() == castOther.getDepartmentId()) || (this
						.getDepartmentId() != null
						&& castOther.getDepartmentId() != null && this
						.getDepartmentId().equals(castOther.getDepartmentId())))
				&& ((this.getOrgId() == castOther.getOrgId()) || (this
						.getOrgId() != null
						&& castOther.getOrgId() != null && this.getOrgId()
						.equals(castOther.getOrgId())))
				&& ((this.getSetOrgId() == castOther.getSetOrgId()) || (this
						.getSetOrgId() != null
						&& castOther.getSetOrgId() != null && this
						.getSetOrgId().equals(castOther.getSetOrgId())))
				&& ((this.getSuperManager() == castOther.getSuperManager()) || (this
						.getSuperManager() != null
						&& castOther.getSuperManager() != null && this
						.getSuperManager().equals(castOther.getSuperManager())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getUserId() == null ? 0 : this.getUserId().hashCode());
		result = 37 * result
				+ (getPassword() == null ? 0 : this.getPassword().hashCode());
		result = 37 * result
				+ (getUserName() == null ? 0 : this.getUserName().hashCode());
		result = 37 * result
				+ (getFirstName() == null ? 0 : this.getFirstName().hashCode());
		result = 37 * result
				+ (getLastName() == null ? 0 : this.getLastName().hashCode());
		result = 37 * result
				+ (getMail() == null ? 0 : this.getMail().hashCode());
		result = 37
				* result
				+ (getIdentificationNumber() == null ? 0 : this
						.getIdentificationNumber().hashCode());
		result = 37
				* result
				+ (getEmployeeNumber() == null ? 0 : this.getEmployeeNumber()
						.hashCode());
		result = 37 * result
				+ (getTitle() == null ? 0 : this.getTitle().hashCode());
		result = 37
				* result
				+ (getEmployeeType() == null ? 0 : this.getEmployeeType()
						.hashCode());
		result = 37 * result
				+ (getBranch() == null ? 0 : this.getBranch().hashCode());
		result = 37 * result
				+ (getAddress() == null ? 0 : this.getAddress().hashCode());
		result = 37
				* result
				+ (getPostalAddress() == null ? 0 : this.getPostalAddress()
						.hashCode());
		result = 37
				* result
				+ (getPostalCode() == null ? 0 : this.getPostalCode()
						.hashCode());
		result = 37 * result
				+ (getFax() == null ? 0 : this.getFax().hashCode());
		result = 37
				* result
				+ (getTelephoneNumber() == null ? 0 : this.getTelephoneNumber()
						.hashCode());
		result = 37 * result
				+ (getManager() == null ? 0 : this.getManager().hashCode());
		result = 37 * result
				+ (getSex() == null ? 0 : this.getSex().hashCode());
		result = 37 * result
				+ (getAge() == null ? 0 : this.getAge().hashCode());
		result = 37
				* result
				+ (getGroupNumber() == null ? 0 : this.getGroupNumber()
						.hashCode());
		result = 37
				* result
				+ (getUpdateTime() == null ? 0 : this.getUpdateTime()
						.hashCode());
		result = 37
				* result
				+ (getDepartmentId() == null ? 0 : this.getDepartmentId()
						.hashCode());
		result = 37 * result
				+ (getOrgId() == null ? 0 : this.getOrgId().hashCode());
		result = 37 * result
				+ (getSetOrgId() == null ? 0 : this.getSetOrgId().hashCode());
		result = 37
				* result
				+ (getSuperManager() == null ? 0 : this.getSuperManager()
						.hashCode());
		return result;
	}

}