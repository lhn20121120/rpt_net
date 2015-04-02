package com.fitech.framework.core.util;

import java.util.List;

/**
 * @author 石昊东 2011-4-29
 * 
 */
public class ListUtil {

	/**
	 * 判断list是否为空
	 * 
	 * @param list
	 * @return list为空返回true,list不为空返回false
	 */
	public static boolean isEmpty(List list) {
		if (list == null || list.size() == 0) {
			return true;
		}
		return false;
	}
}
