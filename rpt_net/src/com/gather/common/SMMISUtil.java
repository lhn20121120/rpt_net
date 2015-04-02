/*
 * Created on 2005-11-27
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.gather.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import sun.misc.BASE64Encoder;

/**
 * @author rds
 *
 * 常用方法类 
 */
public class SMMISUtil {
 /**
  * 非法的附件后缀名
  */
 private static final String[] UNVALIDEXT={
 	"exe","bat","sh"	
 };
 
 /**
  * 星期
  */
  private static final String[][] WEEK={
  		{"1","星期一"},
		{"2","星期二"},
		{"3","星期三"},
		{"4","星期四"},
		{"5","星期五"},
		{"6","星期六"},
		{"7","星期日"}
  };
  
 /**
  * 加密算法名
  */
  private static final String ALGORITH="MD5";
  
 /**
  * 日志
  */
  private Log log=new Log(SMMISUtil.class);
  

  
 /**
  * 将字符串解析成日期类型 
  * 
  * @param _date String
  * @return Date 
  */
  public static Date parseDate(String _date){
  	if(_date==null || _date.equals("") || _date.length()<8){
  	  return null;
  	}else{
  	  try{
  	  	SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
  	  	return dateFormat.parse(_date);
  	  }catch(ParseException pe){
  	  	return null;
  	  }
  	}
  }
  
 /**
  * 获得使用MD5加密的字符串的Base64串值
  *
  * @param value String 要加密的字符串
  * @return String 加密后的Base64串
  */
  public static String hashMD5String(String value){
  	String result="";
  	
  	try{
  		MessageDigest md=MessageDigest.getInstance(ALGORITH);
  		byte[] md5Value=md.digest(value.getBytes());
  		BASE64Encoder encode=new BASE64Encoder();
  		result=encode.encode(md5Value);
  	}catch(NoSuchAlgorithmException nae){
  		new SMMISException(nae);
  	}
  	
  	return result;
  }
 
 
  /**
   * 标准日期样式
   */
  public static final String NORMALDATE="yyyy-MM-dd";
  /**
   * 中国标准的日期样式 
   */
  public static final String CHINESEDATE="yyyy年MM月dd日";
  /**
   * 月份
   */
  public static final String MONTH="MM";
  /**
   * 年
   */
  public static final String YEAR="";
  /**
   * 获取当天的日期
   * 
   * @param format 日期样式
   * @return String
   */
  public static String getToday(String format){
  	if(format==null) return "";
  	SimpleDateFormat dateFormat=new SimpleDateFormat(format);
  	return dateFormat.format(new Date());
  }
  
  /**
   * 获取当天是周几
   * 
   * @return String
   */
  public static String getDay(){
  	String _day="";
  	Date date=new Date();
  	int day=date.getDay();
  	for(int i=0;i<WEEK.length;i++){
  		if(WEEK[i][0].equals(String.valueOf(day))==true){
  			_day=WEEK[i][1];
  			break;
  		}
  	}
  	return _day;
  }
  

  

  
  /**
   * 判断用户上传的附件是否是正确
   * 
   * @author rds
   *
   * @param fileName String 上传附件的文件名
   * @return boolean 上传的附件不合法，返回false；否则，返回true
   */
  public static boolean isValidAffix(String fileName){
  	if(fileName==null) return false;
  	
  	String ext=fileName.substring(fileName.lastIndexOf(".")+1);
  	
  	for(int i=0;i<UNVALIDEXT.length;i++){
  		if(UNVALIDEXT[i].toLowerCase().equals(ext)){
  			return false;
  		}
  	}
  	
  	return true;
  }
  public static void main(String args[]){
	  
	  // System.out.println(SMMISUtil.getToday(SMMISUtil.MONTH));
  }
}
