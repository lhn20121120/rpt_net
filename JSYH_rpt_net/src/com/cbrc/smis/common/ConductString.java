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
 * �������ڶ��ַ�������һЩ����
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class ConductString {

	/**
	 * ��������ȥ���ַ����Ŀո�,����ǰ��ո���м�Ŀո�
	 * 
	 * @param s
	 *            �ַ���
	 * @return �ַ���
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