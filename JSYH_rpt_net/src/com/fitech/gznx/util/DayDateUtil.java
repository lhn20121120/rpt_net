package com.fitech.gznx.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DayDateUtil {

	public static SimpleDateFormat formatYYYYMMDDHHMMSSS = new SimpleDateFormat("yyyymmddHHmmssSSS");

	public static SimpleDateFormat formatYYYYMM = new SimpleDateFormat("yyyyMM");

	public static SimpleDateFormat formatYYYYMMDD = new SimpleDateFormat("yyyy-MM-dd");

	public static SimpleDateFormat formatYYYYMMDDHHMMSS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static String getDetailedTime() {
		return formatYYYYMMDDHHMMSSS.format(new Date());
	}

	public static String getYYYYMMDate() {
		return formatYYYYMMDDHHMMSSS.format(new Date());
	}

	/**
	 * 将传入的时间格式化为"2010-08-11"类似的字符串
	 * 
	 * @param inputDate
	 *            Date 传入的完整时间
	 * 
	 * @return String 格式化以后的字符串
	 */
	public static String formatDateToYYYYMMDD(Date inputDate) throws Exception {
		if (inputDate == null) {
			return null;
		}
		return formatYYYYMMDD.format(inputDate);
	}

	/**
	 * 将传入的时间字符串(如"2010-08-11")转换为时间对象
	 * 
	 * @param inputStr
	 *            String 将传入的时间字符串(如"2010-08-11")
	 * 
	 * @return Date 时间对象
	 */
	public static Date formatYYYYMMDDToDate(String inputStr) throws Exception {
		if (inputStr == null) {
			return null;
		}
		return formatYYYYMMDD.parse(inputStr);
	}

	/**
	 * 将传入的时间格式化为"2010-08-11 04:50:32"类似的字符串
	 * 
	 * @param inputDate
	 *            Date 传入的完整时间
	 * 
	 * @return String 格式化以后的字符串
	 */
	public static String formatDateToYYYYMMDD24(Date inputDate) throws Exception {
		if (inputDate == null) {
			return null;
		}
		return formatYYYYMMDDHHMMSS.format(inputDate);
	}

	/**
	 * 将传入的时间字符串(如"2010-08-11")转换为当天起始的时间对象(2010-08-11 00:00:00)
	 * 
	 * @param inputStr
	 *            String 传入的时间字符串
	 * 
	 * @return String 格式化以后的字符串
	 */
	public static Date formatDateToMin24(String inputStr) throws Exception {
		if (inputStr == null) {
			return null;
		}

		inputStr += " 00:00:00";
		return formatYYYYMMDDHHMMSS.parse(inputStr);
	}

	/**
	 * 将传入的时间字符串(如"2010-08-11")转换为当天结束的时间对象(2010-08-11 23:59:59)
	 * 
	 * @param inputStr
	 *            String 传入的时间字符串
	 * 
	 * @return String 格式化以后的字符串
	 */
	public static Date formatDateToMax24(String inputStr) throws Exception {
		if (inputStr == null) {
			return null;
		}

		inputStr += " 23:59:59";
		return formatYYYYMMDDHHMMSS.parse(inputStr);
	}

	/**
	 * 取得传入日期当月的1号时间点
	 * 
	 * @param date
	 * @return
	 */
	public static String firstDayOfMonth(Date date) throws Exception {
		String result = formatDateToYYYYMMDD(date);

		if (result == null || result.equals("")) {
			return null;
		}

		return result.substring(0, 7) + "-01";
	}
}
