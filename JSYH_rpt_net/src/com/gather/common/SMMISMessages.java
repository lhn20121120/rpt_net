/*
 * Created on 2005-5-23
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.gather.common;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author rds
 *
 * 提示信息操作类
 */
public class SMMISMessages implements Serializable{
 /**
  * 信息标识
  * 当flag=-1时，表示无提示信息;
  * 当flag=1时，表示成功提示信息；
  * 当flag=0时，表示错误提示信息
  */
  private int flag=-1;
  
 /**
  * 提示信息
  */
  private ArrayList messages;
 
 /**
  * 提示信息
  */
  private String alertMsg;
  
 /**
  * messages的size
  */
  private int size;
  
 /**
  * 构造函数
  */
  public SMMISMessages(){
  	messages=new ArrayList();
  }
  
 /**
  * setFlag方法
  * 
  * @param flag int 
  * @return void
  */
  public void setFlag(int flag){
  	this.flag=flag;
  }
  
 /**
  * getFlag方法
  * 
  * @return int
  */
  public int getFlag(){
  	return this.flag;
  }
  
 /**
  * add方法
  * 
  * @param msg String 提示信息
  * @return void
  */
  public void add(String msg){
  	messages.add(msg);
  }
  
 /**
  * add方法
  * 
  * @param msg String 提示信息
  * @param messages
  */
  public void add(int flag,String msg){
  	this.flag=flag;
  	messages.add(msg);
  }
  
 /**
  * set方法
  * 
  * @param messages ArrayList
  * @return void
  */
  public void setMessages(ArrayList messages){
  	this.messages=messages;
  }
  
 /**
  * get方法
  * 
  * @return ArrayList
  */
  public ArrayList getMessages(){
  	return this.messages;
  }
  
 /**
  * 获得Message的size
  * 
  * @return int
  */
  public int getSize(){
  	return this.messages.size();
  }
  
 /**
  * getAlertMsg方法
  * 
  * @return String
  */
  public String getAlertMsg(){
  	StringBuffer sb=new StringBuffer("");
  	for(int i=0;i<messages.size();i++){
  	  sb.append((String)messages.get(i) + "");	
  	}
  	
  	return sb.toString();
  }
}
