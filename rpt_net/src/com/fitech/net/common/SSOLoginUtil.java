package com.fitech.net.common;

import java.util.Locale;

import org.apache.struts.util.MessageResources;

import com.cbrc.smis.util.FitechException;

public class SSOLoginUtil {
	FitechException log=new FitechException(SSOLoginUtil.class);
	Locale LOCALE=Locale.CHINA;
	/**
	 * Excel模板对应PDF模板的起始行的偏移量
	 */
	private String REPORTFILE="com/fitech/net/common/SSOLoginResources";
	
	/**
	 * 从资源文件中，根据键获取其值
	 * @param key 资源文件的键
	 * @return String 健的值
	 */
	public String getValueByKey(String key){
		if(key==null) return null;
		
		MessageResources resources=MessageResources.getMessageResources(REPORTFILE);	
		
		String value=resources.getMessage(this.LOCALE,key);		
		
		return value==null?null:value.trim();
	}
}
