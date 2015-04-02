package com.cbrc.smis.util;

import java.util.Locale;

import org.apache.struts.util.MessageResources;
/**
 * 读取资源文件信息类
 * 
 * @author rds
 * @date 2005-12-4
 */
public class FitechResource {
	/**
	 * 根据信息键，读取其值
	 * 
	 * @param locale Locale
	 * @param resources MessageResources
	 * @param key String 信息键
	 * @param subKey String 信息键
	 * @param param String 参数值
	 * @return String 信息
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
	 * 根据信息键，读取其值
	 * 
	 * @param locale Locale
	 * @param resources MessageResources
	 * @param key String 信息键
	 * @param subKey String 信息键
	 * @return String 信息
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
	 * 根据信息键，读取其值
	 * 
	 * @param locale Locale
	 * @param resources MessageResources
	 * @param key String 信息键
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
	 * 根据信息键，读取其值
	 * 
	 * @param locale Locale
	 * @param resources MessageResources
	 * @param key String 信息键
	 * @param param1 String 参数值
	 * @param param2 String 参数值
	 * @return String 信息
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
	 * 根据信息键，读取其值
	 * 
	 * @param locale Locale
	 * @param resources MessageResources
	 * @param key String 信息键
	 * @param param2 String 参数值
	 * @return String 信息
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
	 * 根据信息键，读取其值
	 * 
	 * @param locale Locale
	 * @param resources MessageResources
	 * @param key String 信息键
	 * @param param1 String 参数值
	 * @param param2 String 参数值
	 * @param param3 String 参数值
	 * @return String 信息
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