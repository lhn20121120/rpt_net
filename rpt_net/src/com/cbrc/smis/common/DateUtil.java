/*
 * Created on 2005-12-12
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.cbrc.smis.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author cb
 * 
 * 
 * 
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DateUtil {    
      /**
	   * 标准日期样式
	   */
	  public static final String NORMALDATE="yyyy-MM-dd";
	  /**
	   * 中国标准的日期样式 
	   */
	  public static final String CHINESEDATE="yyyy年MM月dd日";
	  /**
	   * 标准日期时间类型
	   */
	  public static final String DATA_TIME = "yyyy-MM-dd HH:mm:ss";
	  /**
	   * 标准日期时间的数字类型
	   */
	  public static final String DATA_TIME_NUMBER = "yyyyMMddHHmmss";
	  /**
	   * 月份
	   */
	  public static final String MONTH="MM";
	  /**
	   * 年
	   */
	  public static final String YEAR="yyyy";
	  /**
	   * 日
	   */
	  public static final String DAY="dd";
	  
	  public static String getToday(String format){
		  	if(format==null) return "";
		  	SimpleDateFormat dateFormat=new SimpleDateFormat(format);
		  	return dateFormat.format(new Date());
		  }
	  
    public static Date getTodayDate(){
             return new Date();
    }
    
    /**
     * 将字符型日期转换成日期型
     * 
     * @param date String 字符型日期
     * @return Date
     */
    public static Date getDateByString(String date){
    	if(date==null) return null;
    	if(date.equals("")) return null;
    	
  	    SimpleDateFormat format = null;
  	    Date tempDate=null;
  	    
  	    try{
  	    	date=date.trim();
  	    	date=date.replaceAll(" ","").replaceAll("\n","");
  	    	date=date.replaceAll("年","").replaceAll("月","").replaceAll("日","");
  	    	if(date.equals("")) return null;
  	    	if(date.indexOf("-")>0) 
  	    		format=new SimpleDateFormat("yyyy-MM-dd");
  	    	else
  	    		format=new SimpleDateFormat("yyyyMMdd");
  	    	tempDate=format.parse(date);
  	    }catch(Exception e){
  	    	e.printStackTrace();
  	    	tempDate=null;
  	    }
  	    
  	    return tempDate;
    }
     
    public static Date getDateByString(String date,String formatStr){
  	    SimpleDateFormat format = new SimpleDateFormat(formatStr);
  	    Date tempDate=null;
  	    if (date == null || date.equals("")) {
  	      return null;
  	    }
  	    else {
  	    	try{
  	            tempDate=format.parse(date);
  	    	}catch(Exception e){e.printStackTrace();}
  	    }
  	    return tempDate;
    }
    /**
     * @function 抛异常getDateByString函数，功能相同
     * @param date
     * @param formatStr
     * @return
     * @throws Exception
     */
    public static Date getFormatDate(String date,String formatStr)throws Exception{
	    SimpleDateFormat format = new SimpleDateFormat(formatStr);
	    Date tempDate=null;
	    if (date == null || date.equals("")) {
	      return null;
	    }
	    else {
	            tempDate=format.parse(date);
	    }
	    return tempDate;
  }
    //日期比大小，第一个参数大于第二个参数，则返回1 ,否则返回0
               //如果相同 ，返回2
    public static int compareDate(Date date1,Date date2){
  	  long d1=date2long(date1);
  	  long d2=date2long(date2);
  	     if(d1>d2){
  	    	 return 1;   //第一个参数大于第二个参数
  	     }else if(d1<d2){
  	    	 return 0;   //第二个参数大于第一个参数
  	     }else{
  	    	 return 2;   //相等
  	     }
    }
    //日期转字符串
    public static String date2String(Date dateTime){
      return toSimpleDateFormat(dateTime, "yyyy-MM-dd HH:mm:ss");
    }
    //日期转数字
    public static long date2long(Date date){
 
  	  return Long.parseLong(toSimpleDateFormat(date,"yyyyMMddHHmmss"));
    }
       
    /**
     * 
     * @param date
     * @param simpleDateFormat
     * @return
     */
    public static String toSimpleDateFormat(Date date, String simpleDateFormat) {
      SimpleDateFormat format = new SimpleDateFormat(simpleDateFormat);
      if (date == null) {
        return null;
      }
      else {
        return format.format(date);
      }
    }
    //在月上加 ，int[0] 返回年 ，int[1] 返回月
    public static int[] addMonth(int year,int month,int addNumber){
  	 int[] myDate=new int[2];
  	   if((month+addNumber)>12) {
  		   month=(month+addNumber)-12;
  		   year=year+1;
  	   }else{
  		   month=month+addNumber;
  	   }
  	   myDate[0]=year;
  	   myDate[1]=month;
  	  return myDate;
    }
	/**
	 * @param args
	 */
	public static void main(String[] args) {
	    
	    String  s = " ";
	    
	    DateUtil   du = new DateUtil();
	    
	    Date date = DateUtil.getDateByString(s);
	    
	    if(date!=null){
	    	// System.out.println(date.toLocaleString());
	    }else{
	    	// System.out.println("date is null");
	    }
	}
    //在日期上减月
    public static Date reduceMonth(Date sourceDate,int monthNumber){
  	  if(sourceDate==null) return null;
  	  Calendar cal=Calendar.getInstance();
  	  cal.setTime(sourceDate);
  	  cal.add(Calendar.MONTH, -monthNumber);
  	  return cal.getTime();
  	  
    }

}
