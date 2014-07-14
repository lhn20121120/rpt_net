package com.gather.common;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
      
      //在日上加日期
      public static Date addDay(Date sourceDate,int dayNumber){
    	  Calendar cal=Calendar.getInstance();
    	  cal.setTime(sourceDate);
    	  cal.add(Calendar.DAY_OF_YEAR,dayNumber);
    	  return cal.getTime();
      }
      /*
       * @author linfeng
       * @function 比大小 
       * @param Date firstDate
       * @param Date secondDate
       * @return int 如果第一个日期大，返回1，否则返回-1；如果相等，返回0
       */
      public static int compareTo(Date firstDate,Date secondDate){
    	  java.sql.Timestamp firstTime=new Timestamp(firstDate.getTime());
    	  Timestamp secondTime=new Timestamp(secondDate.getTime());
    	  return firstTime.compareTo(secondTime);
      }
      
      /*
       * @author linfeng
       * @function 得到天数差异时间，第一个日期比第二个日期大多少天
       * @param Date firstDate
       * @param Date secondDate
       * @return int 如果第一个日期大，返回天数，否则返回0；
       */
      
      public static int getOneGreatlessTwoDaysNumber(Date firstDate,Date secondDate){
    	  long firstD=firstDate.getTime();
    	  long secondD=secondDate.getTime();
    	  long resultD=firstD-secondD;
    	  long oneDay=24*60*60*1000;
    	  if(resultD<0){
    		  return 0;
    	  }else{
    		  return (int)(resultD/oneDay);
    	  }
      }
      
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String temp="2003-12-12";
		String dd2=DateUtil.toSimpleDateFormat(addDay(new Date(),10),NORMALDATE);
		// System.out.println("test date is; "+dd2);
		
		String dd1=DateUtil.toSimpleDateFormat(new Date(),NORMALDATE);
		// System.out.println("test date is; "+dd1);
		
		Date firstD=new Date();
		Date secondD=getDateByString("2004-10-10",NORMALDATE);
		// System.out.println("The difference time number is: "+getOneGreatlessTwoDaysNumber(firstD,secondD));
		
	}
}
