package com.fitech.model.etl.common;

public class JavaProceDureException  extends Exception{
	private String errorCode;

	public String getErrorCode() {
		return errorCode;
	}

	public JavaProceDureException() {
		// TODO Auto-generated constructor stub
	}

	public JavaProceDureException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public JavaProceDureException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public JavaProceDureException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 
	 * @param errorCode
	 * @param arg0
	 * @param arg1
	 */
	public JavaProceDureException(String errorCode,String arg0, Throwable arg1) {
		super(arg0, arg1);
		this.errorCode = errorCode;
	}

  public void setErrorCode(String errorCode) {
    this.errorCode = errorCode;
  }
 
}
