/*
 * Created on 2005-8-1
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.gather.servlets;

import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

/**
 * @author rds
 * @date 2005-08-01
 * 
 * 自动同步用户、机构和部门信息
 */
public class AutoSynchronized extends HttpServlet {
	/**
	 * 项目的物理路径 
	 */
	public static String SMMIS_PATH="";

	/**
	 * init方法
	 * 
	 * @return void
	 */
	public void init(){
		ServletContext context=getServletContext();
		AutoSynchronized.SMMIS_PATH=context.getRealPath("/");
		//// System.out.println("SMMIS_PATH is -- : "+AutoSynchronized.SMMIS_PATH);
		//new Log(AutoSynchronized.class).info(":::SMMIS_PATH is -- : "+AutoSynchronized.SMMIS_PATH);
		  
		  
		Timer timer=new Timer();
		SMMISTimerTask timerTask=new SMMISTimerTask();
		
		if(getInitParameter("hour")!=null){
			String _hour=getInitParameter("hour");
			try{
				int hour=Integer.parseInt(_hour);
				timerTask.setHour(hour);
			}catch(NumberFormatException nfe){
				//new SMMISException(nfe);
			}
		}
		
		timer.schedule(timerTask,0,2*60*1000);
	}
}

class SMMISTimerTask extends TimerTask{
	/**
	 * 记录是否已同步
	 */
	private static boolean isSynchronized=false;
	
	/**
	 * 自动同步时间
	 */
	private int hour=0;
	
	/**
	 * 设置自动同步时间
	 * 
	 * @param hour int
	 * @return void
	 */
	public void setHour(int hour){
		this.hour=hour;
	}
	
	/**
	 * 获取自动同步时间
	 * 
	 * @return int
	 */
	public int getHour(){
		return this.hour;
	}
	
	/**
	 * run方法 
	 * 
	 * @return void
	 */
	public void run(){		

	}
}	

