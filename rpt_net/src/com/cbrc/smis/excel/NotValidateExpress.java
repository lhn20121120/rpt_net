package com.cbrc.smis.excel;

import java.util.PropertyResourceBundle;

/***
 * 针对个别机构过滤部分校验
 * @author jcm
 * @serialData 2008-03-14
 */
public class NotValidateExpress {
	
	/**个别机构不需要校验的公式配置文件*/
	private static final String EXPRESSFILE="com/cbrc/smis/excel/ExpressResources";
	/**单例模式*/
	private static NotValidateExpress notValidate = null;
	private static PropertyResourceBundle resourceBundle = null;
	
	/**
	 * 单例类私有构造函数
	 * @author jcm
	 */
	private NotValidateExpress(){}
	
	/**
	 * 获得单例类对象
	 * @author jcm
	 * 
	 * @return　NotValidateExpress 单例类对象
	 */
	public static NotValidateExpress newInstance(){
		if(notValidate == null){
			notValidate = new NotValidateExpress();
			resourceBundle = (PropertyResourceBundle) PropertyResourceBundle
		    			.getBundle(EXPRESSFILE);
		}
		
		return notValidate;
	}
	
	/**
	 * 根据键值获得该机构需过滤的校验公式
	 * 
	 * @param key 键值（机构ID+报表ID）
	 * @return value 需过滤的校验公式字串
	 */
	public String getNotValidateExpresses(String key){
		String value = null;
		if(key == null) return value;
		
		value = resourceBundle.getString(key);
		return value;
	}
}
