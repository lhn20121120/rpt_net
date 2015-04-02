/*
 * �������� 2005-7-22
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package com.gather.common;

import org.apache.log4j.Logger;
/**
 * @author rds
 * @date 2005-07-22
 *
 * �쳣������
 */
public class SMMISException extends Exception {
	/**
	 * �Ƿ������ʾ��Ϣ
	 */
	private boolean isPrint=true;
	/**
	 * ��ʾ��Ϣ��������
	 */
	private String className;
	/**
	 * Log
	 */
	private Logger log=null;
	
	/**
	 * ���캯��
	 */
	public SMMISException(){
		log=Logger.getRootLogger();
	}
	
	/**
	 * ���캯��
	 * 
	 * @param object Object
	 */
	public SMMISException(Class _class){
		this.log=Logger.getLogger(_class);
	}
	
	/**
	 * ���캯��
	 * 
	 * @param object Object
	 * @param isPrint boolean
	 */
	public SMMISException(Class _class,boolean isPrint){
		this.log=Logger.getLogger(_class);
		this.isPrint=isPrint;
	}
	
	/**
	 * ���캯��
	 * 
	 * @param isPrint boolean
	 */
	public SMMISException(boolean isPrint){
		log=Logger.getRootLogger();
		this.isPrint=isPrint;
	}
	
	/**
	 * ���캯��
	 * 
	 * @param e Exception
	 */
	public SMMISException (Exception e){
		log=Logger.getRootLogger();
		if(this.isPrint==true)
			e.printStackTrace();
	}
	
	/**
	 * ���캯��
	 */
	public SMMISException(String msg){
		log=Logger.getRootLogger();
		if(this.isPrint==true)
			log.info(msg);
	}
	
	/**
	 * ���캯��
	 * 
	 * @param msg String
	 */
	public SMMISException(String msg,boolean isPrint){
		log=Logger.getRootLogger();
		if(this.isPrint==true)
			log.info("Exception:" + msg);
	}
	
	/**
	 * ���ָ������Ϣ
	 * 
	 * @param msg String ��Ϣ
	 * @return void
	 */
	public void println(String msg){
		if(this.isPrint==true)
			log.info(msg);
	}
	
    /**
     * ����쳣����Ϣ
     * 
     * @param e Exception
     * @return void 
     */
	public void printStackTrace(Exception e){
		e.printStackTrace();
	}
}
