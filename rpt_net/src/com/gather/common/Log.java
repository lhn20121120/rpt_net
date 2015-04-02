/*
 * Created on 2005-5-23
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package com.gather.common;

import org.apache.log4j.Logger;

import com.gather.adapter.StrutsLogDelegate;
/**
 * @author rds
 *
 * ��־������
 */
public class Log {
	
  private Logger log=null;
   
 /**
  * ���캯��
  * 
  * @param className String �������־��������
  */
  public Log(String className){
   	log=Logger.getLogger(className);
  }
  
 /**
  * ���캯��
  * 
  * @param _class class �������־����
  */
  public Log(Class _class){
  	log=Logger.getLogger(_class);
  }
  
 /**
  * info���� 
  * 
  * @param msg String ��־��Ϣ
  * @return void
  */
  public void info(String msg){
  	log.info(msg);
  }
  
  public static void write(int logType,String orgName,String orgId,String operation,String memo){
	  // System.out.println("orgId is: "+orgId);
	  if(!StrutsLogDelegate.insert(logType,orgName,orgId,operation,memo)){
		  Log log=new Log(Log.class);
		  log.info("������"+orgId+"д����־ʧ�ܣ�ʱ�䣺"+DateUtil.getToday(DateUtil.DATA_TIME));
	  }
  }
}
