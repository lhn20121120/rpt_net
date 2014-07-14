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
	 * �������ʱ���ʽ��Ϊ"2010-08-11"���Ƶ��ַ���
	 * 
	 * @param inputDate
	 *            Date ���������ʱ��
	 * 
	 * @return String ��ʽ���Ժ���ַ���
	 */
	public static String formatDateToYYYYMMDD(Date inputDate) throws Exception {
		if (inputDate == null) {
			return null;
		}
		return formatYYYYMMDD.format(inputDate);
	}

	/**
	 * �������ʱ���ַ���(��"2010-08-11")ת��Ϊʱ�����
	 * 
	 * @param inputStr
	 *            String �������ʱ���ַ���(��"2010-08-11")
	 * 
	 * @return Date ʱ�����
	 */
	public static Date formatYYYYMMDDToDate(String inputStr) throws Exception {
		if (inputStr == null) {
			return null;
		}
		return formatYYYYMMDD.parse(inputStr);
	}

	/**
	 * �������ʱ���ʽ��Ϊ"2010-08-11 04:50:32"���Ƶ��ַ���
	 * 
	 * @param inputDate
	 *            Date ���������ʱ��
	 * 
	 * @return String ��ʽ���Ժ���ַ���
	 */
	public static String formatDateToYYYYMMDD24(Date inputDate) throws Exception {
		if (inputDate == null) {
			return null;
		}
		return formatYYYYMMDDHHMMSS.format(inputDate);
	}

	/**
	 * �������ʱ���ַ���(��"2010-08-11")ת��Ϊ������ʼ��ʱ�����(2010-08-11 00:00:00)
	 * 
	 * @param inputStr
	 *            String �����ʱ���ַ���
	 * 
	 * @return String ��ʽ���Ժ���ַ���
	 */
	public static Date formatDateToMin24(String inputStr) throws Exception {
		if (inputStr == null) {
			return null;
		}

		inputStr += " 00:00:00";
		return formatYYYYMMDDHHMMSS.parse(inputStr);
	}

	/**
	 * �������ʱ���ַ���(��"2010-08-11")ת��Ϊ���������ʱ�����(2010-08-11 23:59:59)
	 * 
	 * @param inputStr
	 *            String �����ʱ���ַ���
	 * 
	 * @return String ��ʽ���Ժ���ַ���
	 */
	public static Date formatDateToMax24(String inputStr) throws Exception {
		if (inputStr == null) {
			return null;
		}

		inputStr += " 23:59:59";
		return formatYYYYMMDDHHMMSS.parse(inputStr);
	}

	/**
	 * ȡ�ô������ڵ��µ�1��ʱ���
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
