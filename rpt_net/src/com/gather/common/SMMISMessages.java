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
 * ��ʾ��Ϣ������
 */
public class SMMISMessages implements Serializable{
 /**
  * ��Ϣ��ʶ
  * ��flag=-1ʱ����ʾ����ʾ��Ϣ;
  * ��flag=1ʱ����ʾ�ɹ���ʾ��Ϣ��
  * ��flag=0ʱ����ʾ������ʾ��Ϣ
  */
  private int flag=-1;
  
 /**
  * ��ʾ��Ϣ
  */
  private ArrayList messages;
 
 /**
  * ��ʾ��Ϣ
  */
  private String alertMsg;
  
 /**
  * messages��size
  */
  private int size;
  
 /**
  * ���캯��
  */
  public SMMISMessages(){
  	messages=new ArrayList();
  }
  
 /**
  * setFlag����
  * 
  * @param flag int 
  * @return void
  */
  public void setFlag(int flag){
  	this.flag=flag;
  }
  
 /**
  * getFlag����
  * 
  * @return int
  */
  public int getFlag(){
  	return this.flag;
  }
  
 /**
  * add����
  * 
  * @param msg String ��ʾ��Ϣ
  * @return void
  */
  public void add(String msg){
  	messages.add(msg);
  }
  
 /**
  * add����
  * 
  * @param msg String ��ʾ��Ϣ
  * @param messages
  */
  public void add(int flag,String msg){
  	this.flag=flag;
  	messages.add(msg);
  }
  
 /**
  * set����
  * 
  * @param messages ArrayList
  * @return void
  */
  public void setMessages(ArrayList messages){
  	this.messages=messages;
  }
  
 /**
  * get����
  * 
  * @return ArrayList
  */
  public ArrayList getMessages(){
  	return this.messages;
  }
  
 /**
  * ���Message��size
  * 
  * @return int
  */
  public int getSize(){
  	return this.messages.size();
  }
  
 /**
  * getAlertMsg����
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
