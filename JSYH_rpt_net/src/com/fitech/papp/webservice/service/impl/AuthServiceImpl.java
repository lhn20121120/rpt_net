package com.fitech.papp.webservice.service.impl;

import org.apache.log4j.Logger;

import com.fitech.papp.webservice.service.AuthService;

/**
 * http://localhost:8080/portal/services/AuthWebService
 * 
 * 
 * 
 */
public class AuthServiceImpl implements AuthService {
	public static Logger log = Logger.getLogger(AuthServiceImpl.class);

	@Override
	public String isExist(String sessionId) {
		

		return null;
	}

	@Override
	public String setSession(String sessionId, String username) {
		
		return null;
	}
}
