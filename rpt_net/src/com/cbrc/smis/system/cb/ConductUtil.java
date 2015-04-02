package com.cbrc.smis.system.cb;

import java.util.List;

/**
 * 清单式报表的数据入库工具类
 * 
 * @author rds
 * @date 2006-02-22
 */
public class ConductUtil {
	/**
	 * 判断当前的XML标签是否是数据表的列
	 * 
	 * @param colName String XML标签名称
	 * @param cols List 数据表的列名称列表
	 * @return 如果是,则返回true;否则,返回false
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
