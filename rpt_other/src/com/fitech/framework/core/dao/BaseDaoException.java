package com.fitech.framework.core.dao;

import com.fitech.framework.core.lang.BaseException;


/**
 * @author ahxu
 */
public class BaseDaoException extends BaseException {

	public BaseDaoException() {
		super();
	}

	/**
	 * @param message
	 */
	public BaseDaoException(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public BaseDaoException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause
	 */
	public BaseDaoException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * 
	 * @param errorCode
	 * @param message
	 * @param cause
	 */
	public BaseDaoException(String errorCode,String message, Throwable cause) {
		super(errorCode,message, cause);
	}

}
