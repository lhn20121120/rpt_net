package com.cbrc.smis.excel;

import java.util.PropertyResourceBundle;

/***
 * ��Ը���������˲���У��
 * @author jcm
 * @serialData 2008-03-14
 */
public class NotValidateExpress {
	
	/**�����������ҪУ��Ĺ�ʽ�����ļ�*/
	private static final String EXPRESSFILE="com/cbrc/smis/excel/ExpressResources";
	/**����ģʽ*/
	private static NotValidateExpress notValidate = null;
	private static PropertyResourceBundle resourceBundle = null;
	
	/**
	 * ������˽�й��캯��
	 * @author jcm
	 */
	private NotValidateExpress(){}
	
	/**
	 * ��õ��������
	 * @author jcm
	 * 
	 * @return��NotValidateExpress ���������
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
	 * ���ݼ�ֵ��øû�������˵�У�鹫ʽ
	 * 
	 * @param key ��ֵ������ID+����ID��
	 * @return value ����˵�У�鹫ʽ�ִ�
	 */
	public String getNotValidateExpresses(String key){
		String value = null;
		if(key == null) return value;
		
		value = resourceBundle.getString(key);
		return value;
	}
}
