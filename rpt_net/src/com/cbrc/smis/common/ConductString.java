/*
 * Created on 2005-12-10
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.cbrc.smis.common;

/**
 * @author cb
 * 
 * 该类用于对字符串进行一些操作
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class ConductString {

	/**
	 * 该类用于去除字符串的空格,包括前后空格和中间的空格
	 * 
	 * @param s
	 *            字符串
	 * @return 字符串
	 */
	public static String getStringNotSpace(String s) {

		if (s == null)

			return "";

		String oldString = s;

		String newString = "";

		int size = s.length();

		int j = 0;

		for (int i = 0; i < size; i++) {

			j++;

			String m = oldString.substring(i, j);

			if (!(m.equals(" ")))

				newString = newString + m;
		}

		return newString;
	}

}