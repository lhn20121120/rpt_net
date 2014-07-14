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
 * ���÷����� 
 */
public class SMMISUtil {
 /**
  * �Ƿ��ĸ�����׺��
  */
 private static final String[] UNVALIDEXT={
 	"exe","bat","sh"	
 };
 
 /**
  * ����
  */
  private static final String[][] WEEK={
  		{"1","����һ"},
		{"2","���ڶ�"},
		{"3","������"},
		{"4","������"},
		{"5","������"},
		{"6","������"},
		{"7","������"}
  };
  
 /**
  * �����㷨��
  */
  private static final String ALGORITH="MD5";
  
 /**
  * ��־
  */
  private Log log=new Log(SMMISUtil.class);
  

  
 /**
  * ���ַ����������������� 
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
  * ���ʹ��MD5���ܵ��ַ�����Base64��ֵ
  *
  * @param value String Ҫ���ܵ��ַ���
  * @return String ���ܺ��Base64��
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
   * ��׼������ʽ
   */
  public static final String NORMALDATE="yyyy-MM-dd";
  /**
   * �й���׼��������ʽ 
   */
  public static final String CHINESEDATE="yyyy��MM��dd��";
  /**
   * �·�
   */
  public static final String MONTH="MM";
  /**
   * ��
   */
  public static final String YEAR="";
  /**
   * ��ȡ���������
   * 
   * @param format ������ʽ
   * @return String
   */
  public static String getToday(String format){
  	if(format==null) return "";
  	SimpleDateFormat dateFormat=new SimpleDateFormat(format);
  	return dateFormat.format(new Date());
  }
  
  /**
   * ��ȡ�������ܼ�
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
   * �ж��û��ϴ��ĸ����Ƿ�����ȷ
   * 
   * @author rds
   *
   * @param fileName String �ϴ��������ļ���
   * @return boolean �ϴ��ĸ������Ϸ�������false�����򣬷���true
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
