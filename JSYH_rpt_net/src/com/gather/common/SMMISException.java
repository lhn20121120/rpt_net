/*
 * 创建日期 2005-7-22
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package com.gather.common;

import org.apache.log4j.Logger;
/**
 * @author rds
 * @date 2005-07-22
 *
 * 异常处理类
 */
public class SMMISException extends Exception {
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
	public SMMISException(){
		log=Logger.getRootLogger();
	}
	
	/**
	 * 构造函数
	 * 
	 * @param object Object
	 */
	public SMMISException(Class _class){
		this.log=Logger.getLogger(_class);
	}
	
	/**
	 * 构造函数
	 * 
	 * @param object Object
	 * @param isPrint boolean
	 */
	public SMMISException(Class _class,boolean isPrint){
		this.log=Logger.getLogger(_class);
		this.isPrint=isPrint;
	}
	
	/**
	 * 构造函数
	 * 
	 * @param isPrint boolean
	 */
	public SMMISException(boolean isPrint){
		log=Logger.getRootLogger();
		this.isPrint=isPrint;
	}
	
	/**
	 * 构造函数
	 * 
	 * @param e Exception
	 */
	public SMMISException (Exception e){
		log=Logger.getRootLogger();
		if(this.isPrint==true)
			e.printStackTrace();
	}
	
	/**
	 * 构造函数
	 */
	public SMMISException(String msg){
		log=Logger.getRootLogger();
		if(this.isPrint==true)
			log.info(msg);
	}
	
	/**
	 * 构造函数
	 * 
	 * @param msg String
	 */
	public SMMISException(String msg,boolean isPrint){
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
	}
}
