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
 * �Զ�ͬ���û��������Ͳ�����Ϣ
 */
public class AutoSynchronized extends HttpServlet {
	/**
	 * ��Ŀ������·�� 
	 */
	public static String SMMIS_PATH="";

	/**
	 * init����
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
	 * ��¼�Ƿ���ͬ��
	 */
	private static boolean isSynchronized=false;
	
	/**
	 * �Զ�ͬ��ʱ��
	 */
	private int hour=0;
	
	/**
	 * �����Զ�ͬ��ʱ��
	 * 
	 * @param hour int
	 * @return void
	 */
	public void setHour(int hour){
		this.hour=hour;
	}
	
	/**
	 * ��ȡ�Զ�ͬ��ʱ��
	 * 
	 * @return int
	 */
	public int getHour(){
		return this.hour;
	}
	
	/**
	 * run���� 
	 * 
	 * @return void
	 */
	public void run(){		

	}
}	

