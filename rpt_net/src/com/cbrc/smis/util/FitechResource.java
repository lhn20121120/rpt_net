package com.cbrc.smis.util;

import java.util.Locale;

import org.apache.struts.util.MessageResources;
/**
 * ��ȡ��Դ�ļ���Ϣ��
 * 
 * @author rds
 * @date 2005-12-4
 */
public class FitechResource {
	/**
	 * ������Ϣ������ȡ��ֵ
	 * 
	 * @param locale Locale
	 * @param resources MessageResources
	 * @param key String ��Ϣ��
	 * @param subKey String ��Ϣ��
	 * @param param String ����ֵ
	 * @return String ��Ϣ
	 */
	public static String getMessage(Locale locale,
			MessageResources resources,
			String key,
			String subKey,
			String param){
		String msg="";
		if(resources==null) return msg;

		msg=resources.getMessage(locale,key,resources.getMessage(locale,subKey,param));
		//msg=resources.getMessage(locale,key,subKey,param);

		return msg==null?"":msg;
	}
	
	/**
	 * ������Ϣ������ȡ��ֵ
	 * 
	 * @param locale Locale
	 * @param resources MessageResources
	 * @param key String ��Ϣ��
	 * @param subKey String ��Ϣ��
	 * @return String ��Ϣ
	 */
	public static String getMessage(Locale locale,
			MessageResources resources,
			String key,
			String subKey){
		String msg="";
		if(resources==null) return msg;

		msg=resources.getMessage(locale,key,resources.getMessage(locale,subKey));

		return msg==null?"":msg;
	}
	
	/**
	 * ������Ϣ������ȡ��ֵ
	 * 
	 * @param locale Locale
	 * @param resources MessageResources
	 * @param key String ��Ϣ��
	 * @return String
	 */
	public static String getMessage(Locale locale,
			MessageResources resources,
			String key){
		String msg="";
		if(resources==null) return msg;

		msg=resources.getMessage(locale,key);

		return msg==null?"":msg;
	}
	
	/**
	 * ������Ϣ������ȡ��ֵ
	 * 
	 * @param locale Locale
	 * @param resources MessageResources
	 * @param key String ��Ϣ��
	 * @param param1 String ����ֵ
	 * @param param2 String ����ֵ
	 * @return String ��Ϣ
	 */
	public static String getMsg(Locale locale,
			MessageResources resources,
			String key,
			String param1,
			String param2){
		String msg="";
		if(resources==null) return msg;

		msg=resources.getMessage(locale,key,param1,param2);

		return msg==null?"":msg;
	}
	/**
	 * ������Ϣ������ȡ��ֵ
	 * 
	 * @param locale Locale
	 * @param resources MessageResources
	 * @param key String ��Ϣ��
	 * @param param2 String ����ֵ
	 * @return String ��Ϣ
	 */
	public static String getMsg(Locale locale,
			MessageResources resources,
			String key,
			String param1){
		String msg="";
		if(resources==null) return msg;

		msg=resources.getMessage(locale,key,param1);

		return msg==null?"":msg;
	}
	
	/**
	 * ������Ϣ������ȡ��ֵ
	 * 
	 * @param locale Locale
	 * @param resources MessageResources
	 * @param key String ��Ϣ��
	 * @param param1 String ����ֵ
	 * @param param2 String ����ֵ
	 * @param param3 String ����ֵ
	 * @return String ��Ϣ
	 */
	public static String getMsg(Locale locale,
			MessageResources resources,
			String key,
			String param1,
			String param2,
			String param3){
		String msg="";
		if(resources==null) return msg;

		msg=resources.getMessage(locale,key,param1,param2,param3);

		return msg==null?"":msg;
	}
}