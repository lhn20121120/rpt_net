package com.fitech.gznx.service.webservice.impl;

import java.io.Serializable;

public class User  implements Serializable{
	private java.lang.Long _userId = null;  
	private java.lang.String _password = null;   
	private java.lang.String _userName = null;   
	private java.lang.String _firstName = null;   
	private java.lang.String _lastName = null;
	public java.lang.Long get_userId() {
		return _userId;
	}
	public void set_userId(java.lang.Long userId) {
		_userId = userId;
	}
	public java.lang.String get_password() {
		return _password;
	}
	public void set_password(java.lang.String password) {
		_password = password;
	}
	public java.lang.String get_userName() {
		return _userName;
	}
	public void set_userName(java.lang.String userName) {
		_userName = userName;
	}
	public java.lang.String get_firstName() {
		return _firstName;
	}
	public void set_firstName(java.lang.String firstName) {
		_firstName = firstName;
	}
	public java.lang.String get_lastName() {
		return _lastName;
	}
	public void set_lastName(java.lang.String lastName) {
		_lastName = lastName;
	} 
}
