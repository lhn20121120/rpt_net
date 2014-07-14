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
 * 提示信息操作类
 */
public class FitechMessages implements Serializable{
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
	public FitechMessages(){
		messages=new ArrayList();
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