/*
 * Created on 2005-5-23
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.cbrc.smis.util;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author rds
 *
 * ��ʾ��Ϣ������
 */
public class FitechMessages implements Serializable{
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
	public FitechMessages(){
		messages=new ArrayList();
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
			sb.append((String)messages.get(i) + "\\n");	
		}	
		return sb.toString();
	}
	
	/**
	 * 
	 * @return
	 */
	public String getMsg(){
		StringBuffer sb=new StringBuffer("");
		for(int i=0;i<messages.size();i++){
			sb.append((String)messages.get(i) + "<br/>");	
		}	
		return sb.toString();
	}
}