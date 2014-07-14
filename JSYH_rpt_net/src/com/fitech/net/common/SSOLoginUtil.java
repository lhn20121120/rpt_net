package com.fitech.net.common;

import java.util.Locale;

import org.apache.struts.util.MessageResources;

import com.cbrc.smis.util.FitechException;

public class SSOLoginUtil {
	FitechException log=new FitechException(SSOLoginUtil.class);
	Locale LOCALE=Locale.CHINA;
	/**
	 * Excelģ���ӦPDFģ�����ʼ�е�ƫ����
	 */
	private String REPORTFILE="com/fitech/net/common/SSOLoginResources";
	
	/**
	 * ����Դ�ļ��У����ݼ���ȡ��ֵ
	 * @param key ��Դ�ļ��ļ�
	 * @return String ����ֵ
	 */
	public String getValueByKey(String key){
		if(key==null) return null;
		
		MessageResources resources=MessageResources.getMessageResources(REPORTFILE);	
		
		String value=resources.getMessage(this.LOCALE,key);		
		
		return value==null?null:value.trim();
	}
}
