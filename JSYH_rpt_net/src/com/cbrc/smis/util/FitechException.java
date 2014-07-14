package com.cbrc.smis.util;

import org.apache.log4j.Logger;

/**
 * @author rds
 * @date 2005-07-22
 *
 * 系统异常处理类
 */
public class FitechException {
	/**
	 * 是否输出提示信息
	 */
	private boolean isPrint=true;
	/**
	 * 提示信息关链的类
	 */
	private String className;
	/**
	 * Log
	 */
	private Logger log=null;
	
	/**
	 * 构造函数
	 */
	public FitechException(){
		log=Logger.getRootLogger();
		log.info("start  ");
	}
	
	/**
	 * 构造函数
	 * 
	 * @param object Object
	 */
	public FitechException(Class _class){
		this.log=Logger.getLogger(_class);
	}
	
	/**
	 * 构造函数
	 * 
	 * @param object Object
	 * @param isPrint boolean
	 */
	public FitechException(Class _class,boolean isPrint){
		this.log=Logger.getLogger(_class);
		this.isPrint=isPrint;
	}
	
	/**
	 * 构造函数
	 * 
	 * @param isPrint boolean
	 */
	public FitechException(boolean isPrint){
		log=Logger.getRootLogger();
		this.isPrint=isPrint;
	}
	
	/**
	 * 构造函数
	 * 
	 * @param e Exception
	 */
	public FitechException (Exception e){
		log=Logger.getRootLogger();
		if(this.isPrint==true)
			e.printStackTrace();
	}
	
	/**
	 * 构造函数
	 */
	public FitechException(String msg){
		log=Logger.getRootLogger();
		if(this.isPrint==true)
			log.info(msg);
	}
	
	/**
	 * 构造函数
	 * 
	 * @param msg String
	 */
	public FitechException(String msg,boolean isPrint){
		log=Logger.getRootLogger();
		if(this.isPrint==true)
			log.info("Exception:" + msg);
	}
	
	/**
	 * 输出指定的信息
	 * 
	 * @param msg String 信息
	 * @return void
	 */
	public void println(String msg){
		if(this.isPrint==true)
			log.info(msg);
	}
	
    /**
     * 输出异常的信息
     * 
     * @param e Exception
     * @return void 
     */
	public void printStackTrace(Exception e){
		e.printStackTrace();
		log.error(e.getMessage(), e);
	}
}
