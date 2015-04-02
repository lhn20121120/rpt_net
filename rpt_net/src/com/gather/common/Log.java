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
 * 日志操作类
 */
public class Log {
	
  private Logger log=null;
   
 /**
  * 构造函数
  * 
  * @param className String 需输出日志的类名称
  */
  public Log(String className){
   	log=Logger.getLogger(className);
  }
  
 /**
  * 构造函数
  * 
  * @param _class class 需输出日志的类
  */
  public Log(Class _class){
  	log=Logger.getLogger(_class);
  }
  
 /**
  * info方法 
  * 
  * @param msg String 日志信息
  * @return void
  */
  public void info(String msg){
  	log.info(msg);
  }
  
  public static void write(int logType,String orgName,String orgId,String operation,String memo){
	  // System.out.println("orgId is: "+orgId);
	  if(!StrutsLogDelegate.insert(logType,orgName,orgId,operation,memo)){
		  Log log=new Log(Log.class);
		  log.info("机构："+orgId+"写入日志失败，时间："+DateUtil.getToday(DateUtil.DATA_TIME));
	  }
  }
}
