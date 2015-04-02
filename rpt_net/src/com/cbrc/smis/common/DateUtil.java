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
	   * ��׼������ʽ
	   */
	  public static final String NORMALDATE="yyyy-MM-dd";
	  /**
	   * �й���׼��������ʽ 
	   */
	  public static final String CHINESEDATE="yyyy��MM��dd��";
	  /**
	   * ��׼����ʱ������
	   */
	  public static final String DATA_TIME = "yyyy-MM-dd HH:mm:ss";
	  /**
	   * ��׼����ʱ�����������
	   */
	  public static final String DATA_TIME_NUMBER = "yyyyMMddHHmmss";
	  /**
	   * �·�
	   */
	  public static final String MONTH="MM";
	  /**
	   * ��
	   */
	  public static final String YEAR="yyyy";
	  /**
	   * ��
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
     * ���ַ�������ת����������
     * 
     * @param date String �ַ�������
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
  	    	date=date.replaceAll("��","").replaceAll("��","").replaceAll("��","");
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
     * @function ���쳣getDateByString������������ͬ
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
    //���ڱȴ�С����һ���������ڵڶ����������򷵻�1 ,���򷵻�0
               //�����ͬ ������2
    public static int compareDate(Date date1,Date date2){
  	  long d1=date2long(date1);
  	  long d2=date2long(date2);
  	     if(d1>d2){
  	    	 return 1;   //��һ���������ڵڶ�������
  	     }else if(d1<d2){
  	    	 return 0;   //�ڶ����������ڵ�һ������
  	     }else{
  	    	 return 2;   //���
  	     }
    }
    //����ת�ַ���
    public static String date2String(Date dateTime){
      return toSimpleDateFormat(dateTime, "yyyy-MM-dd HH:mm:ss");
    }
    //����ת����
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
    //�����ϼ� ��int[0] ������ ��int[1] ������
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
    //�������ϼ���
    public static Date reduceMonth(Date sourceDate,int monthNumber){
  	  if(sourceDate==null) return null;
  	  Calendar cal=Calendar.getInstance();
  	  cal.setTime(sourceDate);
  	  cal.add(Calendar.MONTH, -monthNumber);
  	  return cal.getTime();
  	  
    }

}
