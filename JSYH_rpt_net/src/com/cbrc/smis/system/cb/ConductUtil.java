package com.cbrc.smis.system.cb;

import java.util.List;

/**
 * �嵥ʽ�����������⹤����
 * 
 * @author rds
 * @date 2006-02-22
 */
public class ConductUtil {
	/**
	 * �жϵ�ǰ��XML��ǩ�Ƿ������ݱ����
	 * 
	 * @param colName String XML��ǩ����
	 * @param cols List ���ݱ���������б�
	 * @return �����,�򷵻�true;����,����false
	 * @throws Exception
	 */
	public static boolean isField(String colName,List cols){
		boolean result=false;
		
		if(colName==null || cols==null) return result;
		
		try{
			result=cols.contains(colName.toUpperCase());
		}catch(Exception e){
			result=false;
		}
		
		return result;
	}
}
