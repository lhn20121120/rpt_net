package com.cbrc.smis.util;

import org.apache.log4j.Logger;

/**
 * @author rds
 * @date 2005-07-22
 *
 * ϵͳ�쳣������
 */
public class FitechException {
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
	public FitechException(){
		log=Logger.getRootLogger();
		log.info("start  ");
	}
	
	/**
	 * ���캯��
	 * 
	 * @param object Object
	 */
	public FitechException(Class _class){
		this.log=Logger.getLogger(_class);
	}
	
	/**
	 * ���캯��
	 * 
	 * @param object Object
	 * @param isPrint boolean
	 */
	public FitechException(Class _class,boolean isPrint){
		this.log=Logger.getLogger(_class);
		this.isPrint=isPrint;
	}
	
	/**
	 * ���캯��
	 * 
	 * @param isPrint boolean
	 */
	public FitechException(boolean isPrint){
		log=Logger.getRootLogger();
		this.isPrint=isPrint;
	}
	
	/**
	 * ���캯��
	 * 
	 * @param e Exception
	 */
	public FitechException (Exception e){
		log=Logger.getRootLogger();
		if(this.isPrint==true)
			e.printStackTrace();
	}
	
	/**
	 * ���캯��
	 */
	public FitechException(String msg){
		log=Logger.getRootLogger();
		if(this.isPrint==true)
			log.info(msg);
	}
	
	/**
	 * ���캯��
	 * 
	 * @param msg String
	 */
	public FitechException(String msg,boolean isPrint){
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
		log.error(e.getMessage(), e);
	}
}
