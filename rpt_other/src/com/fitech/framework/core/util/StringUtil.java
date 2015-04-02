package com.fitech.framework.core.util;

import java.text.DecimalFormat;
import java.util.List;

/**
 * @author 石昊东 2011-4-29
 * 
 */
public class StringUtil {

	/**
	 * 判断字符串是否为空
	 * 
	 * @param str
	 * @return str为空返回true,list不为空返回false
	 */
	public static boolean isEmpty(String str) {
		if (str == null || str.trim().length() == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断字符串数组是否为空
	 * 
	 * @param str
	 * @return str为空返回true,list不为空返回false
	 */
	public static boolean isEmpty(String[] strs) {
		if (strs == null
				|| strs.length == 0
				|| (strs.length == 1 && (strs[0] == null || strs[0].equals("")))) {
			return true;
		} else {
			return false;
		}
	}
	public static void main(String[] args){
		DecimalFormat format = new DecimalFormat("0000");
		System.out.println(format.format(0));
	}
	/**
	 * 将字符数组拼接成指定字符的字符串
	 * @param cks
	 * @param splitStr
	 * @return
	 */
	public static String converArrayToString(String[] cks,String splitStr){
		String str = "";
		if(cks!=null && splitStr!=null){
			for(String ck :cks){
				str+=ck+splitStr;
			}
			if(str!=null&& !str.equals(""))
				str=str.substring(0,str.length()-1);
		}
		return str;
	}
	/**
	 * 将指定的字符串列表拼接成字符串
	 * @param cks 字符串列表
	 * @param splitStr 分割字符
	 * @param besiStr 围堵字符
	 * @return
	 */
	public static String converArrayToString(List<String> cks,String splitStr,String besiStr){
		String str = "";
		if(cks!=null && splitStr!=null){
			for(String ck :cks){
				str+= besiStr + ck  + besiStr  + splitStr;
			}
			if(str!=null&& !str.equals(""))
				str=str.substring(0,str.length()-1);
		}
		return str;
	}
	/**
	 * 将字符串以指定字符分割成字符数组
	 * @param str
	 * @return
	 */
	public static String[] converStringToArray(String str,String splitStr){
		String[] cks=null;
		if(str!=null && !str.equals("") 
				&& splitStr!=null && !splitStr.equals("")){
			cks = str.split(splitStr);
		}
		return cks;
	}
	public static int freqConvert(String freqIdInput){
		int freqId = 1;
		if(freqIdInput.equals("day"))
			freqId = 6;
		if(freqIdInput.equals("month"))
			freqId = 1;
		if(freqIdInput.equals("season"))
			freqId = 2;
		if(freqIdInput.equals("halfyear"))
			freqId = 3;
		if(freqIdInput.equals("year"))
			freqId = 4;
		if(freqIdInput.equals("yearbegincarry"))
			freqId = 9;
		return freqId;
	}
}
