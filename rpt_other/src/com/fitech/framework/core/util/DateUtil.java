package com.fitech.framework.core.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class DateUtil {

	public static final int[] MONTH_ARRAY = { 0, Calendar.JANUARY,
			Calendar.FEBRUARY, Calendar.MARCH, Calendar.APRIL, Calendar.MAY,
			Calendar.JUNE, Calendar.JULY, Calendar.AUGUST, Calendar.SEPTEMBER,
			Calendar.OCTOBER, Calendar.NOVEMBER, Calendar.DECEMBER };

	/**
	 * 时间分割符
	 */
	public static final String DATE_SPLIT = "-";

	/**
	 * 用于格式化日期
	 */
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd");

	/**
	 * 标准日期样式
	 */
	public static final String NORMALDATE = "yyyy-MM-dd";

	/**
	 * 中国标准的日期样式
	 */
	public static final String CHINESEDATE = "yyyy年MM月dd日";

	/**
	 * 标准日期时间类型
	 */
	public static final String DATA_TIME = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 标准日期时间的数字类型
	 */
	public static final String DATA_TIME_NUMBER = "yyyyMMddHHmmss";

	/**
	 * 月份
	 */
	public static final String MONTH = "MM";

	/**
	 * 年
	 */
	public static final String YEAR = "yyyy";

	/**
	 * 日
	 */
	public static final String DAY = "dd";

	/**
	 * 日频度
	 */
	public static final int FREQ_DAY = 5;

	/**
	 * 月频度
	 */
	public static final int FREQ_MONTH = 4;

	/**
	 * 季频度
	 */
	public static final int FREQ_SEASON = 3;

	/**
	 * 半年频度
	 */
	public static final int FREQ_HALFYEAR = 2;

	/**
	 * 年频度
	 */
	public static final int FREQ_YEAR = 1;

	public static String getToday(String format) {
		if (format == null)
			return "";
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(new Date());
	}

	public static Date getTodayDate() {
		Calendar calendar = Calendar.getInstance();
		return calendar.getTime();
	}
	
	public static java.sql.Timestamp getTodayTimestamp() {
		Calendar calendar = Calendar.getInstance();
		return new java.sql.Timestamp(calendar.getTime().getTime());
	}

	public static String getTodayDateStr() {

		Calendar calendar = Calendar.getInstance();

		return (DATE_FORMAT.format(calendar.getTime()));
	}

	/**
	 * 将字符型日期转换成日期型
	 * 
	 * @param date
	 *            String 字符型日期
	 * @return Date
	 */
	public static Date getDateByString(String date) {
		if (date == null)
			return null;
		if (date.equals(""))
			return null;

		SimpleDateFormat format = null;
		Date tempDate = null;

		try {
			date = date.trim();
			date = date.replaceAll(" ", "").replaceAll("\n", "");
			date = date.replaceAll("年", "").replaceAll("月", "").replaceAll("日",
					"");
			if (date.equals(""))
				return null;
			if (date.indexOf("-") > 0)
				format = new SimpleDateFormat("yyyy-MM-dd");
			else
				format = new SimpleDateFormat("yyyyMMdd");
			tempDate = format.parse(date);
		} catch (Exception e) {
			e.printStackTrace();
			tempDate = null;
		}

		return tempDate;
	}

	// public static Date getDateByString(String date, String formatStr) {
	// SimpleDateFormat format = new SimpleDateFormat(formatStr);
	// Date tempDate = null;
	// if (date == null || date.equals("")) {
	// return null;
	// } else {
	// try {
	// tempDate = format.parse(date);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// return tempDate;
	// }

	/**
	 * @function 抛异常getDateByString函数，功能相同
	 * @param date
	 * @param formatStr
	 * @return
	 * @throws Exception
	 */
	public static Date getFormatDate(String date, String formatStr)
			throws Exception {
		SimpleDateFormat format = new SimpleDateFormat(formatStr);
		Date tempDate = null;
		if (date == null || date.equals("")) {
			return null;
		} else {
			tempDate = format.parse(date);
		}
		return tempDate;
	}

	// 日期比大小，第一个参数大于第二个参数，则返回1 ,否则返回0
	// 如果相同 ，返回2
	public static int compareDate(Date date1, Date date2) {
		long d1 = date2long(date1);
		long d2 = date2long(date2);
		if (d1 > d2) {
			return 1; // 第一个参数大于第二个参数
		} else if (d1 < d2) {
			return 0; // 第二个参数大于第一个参数
		} else {
			return 2; // 相等
		}
	}
	
	 public static int comparedate(String DATE1, String DATE2) {
	        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        try {
	            Date dt1 = df.parse(DATE1);
	            Date dt2 = df.parse(DATE2);
	            if (dt1.getTime() > dt2.getTime()) {
	            	// 第一个参数大于第二个参数
	                return 1;
	            } else if (dt1.getTime() < dt2.getTime()) {
	            	// 第二个参数大于第一个参数
	                return -1;
	            } else {
	                return 0;// 相等
	            }
	        } catch (Exception exception) {
	            exception.printStackTrace();
	        }
	        return 0;
	    }


	// 日期转字符串
	public static String date2String(Date dateTime) {
		return toSimpleDateFormat(dateTime, "yyyy-MM-dd HH:mm:ss");
	}

	// 日期转字符串(中国标准的日期样式)
	public static String dateToString(Date dateTime) {
		return toSimpleDateFormat(dateTime, "yyyy-MM-dd");
	}

	// 日期转数字
	// public static long date2long(Date date) {
	//
	// return Long.parseLong(toSimpleDateFormat(date, "yyyyMMddHHmmss"));
	// }

	/**
	 * 
	 * @param date
	 * @param simpleDateFormat
	 * @return
	 */
	// public static String toSimpleDateFormat(Date date, String
	// simpleDateFormat) {
	// SimpleDateFormat format = new SimpleDateFormat(simpleDateFormat);
	// if (date == null) {
	// return null;
	// } else {
	// return format.format(date);
	// }
	// }

	// 在月上加 ，int[0] 返回年 ，int[1] 返回月
	public static int[] addMonth(int year, int month, int addNumber) {
		int[] myDate = new int[2];
		if ((month + addNumber) > 12) {
			month = (month + addNumber) - 12;
			year = year + 1;
		} else {
			month = month + addNumber;
		}
		myDate[0] = year;
		myDate[1] = month;
		return myDate;
	}

	// 在月上减 ，int[0] 返回年 ，int[1] 返回月
	public static int[] subtractMonth(int year, int month, int subtractNumber) {
		int[] myDate = new int[2];
		if ((month - subtractNumber) <= 0) {
			month = (month - subtractNumber) + 12;
			year = year - 1;
		} else {
			month = month - subtractNumber;
		}
		myDate[0] = year;
		myDate[1] = month;
		return myDate;
	}

	/**
	 * 给出一个日期，再给出一个天数，返回这个日期加上这个天数后的日期
	 * 
	 * @author LQ
	 */
	public static Date countDateForLongValue(Date date, int delayTime) {
		long today = date.getTime();
		// delayTime 延迟的天数 24：一天24小时 60：一小时60分
		// 60： 一分60秒 1000： 一秒1000毫秒
		long delay = delayTime * 24 * 60 * 60 * 1000;
		return new Date(today + delay);
	}

	/*
	 * 得到当前日期的星期
	 */
	public static String getDay() {
		String[] weeks = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		return weeks[new Date().getDay()];
	}

	/**
	 * 根据时间字串,返回该时间的Canlendar对象
	 * 
	 * @param date
	 * @return
	 */
	public static GregorianCalendar getCalendar(String date) {
		if (date == null || date.equals(""))
			return null;
		String[] dateSplit = date.split("-");
		int year = new Integer(dateSplit[0]).intValue();
		int month = new Integer(dateSplit[1]).intValue();
		int day = new Integer(dateSplit[2]).intValue();
		return new GregorianCalendar(year, DateUtil.MONTH_ARRAY[month], day);
	}

	public static List getDateList(String startDate, String endDate,
			Integer freq) {
		if (startDate == null || startDate.equals("") || endDate == null
				|| endDate.equals(""))
			return null;
		freq = new Integer(freq.intValue());
		List dateList = new ArrayList();
		if (startDate.equals(endDate)) {
			dateList.add(startDate);
			return dateList;
		}

		GregorianCalendar startCalendar = getCalendar(startDate);
		GregorianCalendar endCalendar = getCalendar(endDate);

		if (startCalendar.after(endCalendar))
			return null;
		String newDateStr = DATE_FORMAT.format(startCalendar.getTime());

		while (startCalendar.before(endCalendar)
				|| startCalendar.equals(endCalendar)) {
			if (freq.intValue() == DateUtil.FREQ_MONTH
					&& DateUtil.isMonthDay(newDateStr)) {
				dateList.add(newDateStr);
				startCalendar.add(GregorianCalendar.MONTH, 1);
				newDateStr = DATE_FORMAT.format(startCalendar.getTime());
				continue;
			}
			/* 季频度 */
			else if (freq.intValue() == DateUtil.FREQ_SEASON
					&& DateUtil.isSeasonDay(newDateStr)) {
				dateList.add(newDateStr);
				startCalendar.add(GregorianCalendar.MONTH, 3);
				newDateStr = DATE_FORMAT.format(startCalendar.getTime());
				continue;
			}
			/* 半年频度 */
			else if (freq.intValue() == DateUtil.FREQ_HALFYEAR
					&& DateUtil.isHalfYearDay(newDateStr)) {
				dateList.add(newDateStr);
				startCalendar.add(GregorianCalendar.MONTH, 6);
				newDateStr = DATE_FORMAT.format(startCalendar.getTime());
				continue;
			}
			/* 年频度 */
			else if (freq.intValue() == DateUtil.FREQ_YEAR
					&& DateUtil.isYearDay(newDateStr)) {
				dateList.add(newDateStr);
				startCalendar.add(GregorianCalendar.YEAR, 1);
				newDateStr = DATE_FORMAT.format(startCalendar.getTime());
				continue;
			} else {
				dateList.add(newDateStr);
				startCalendar.add(GregorianCalendar.DATE, 1);
				newDateStr = DATE_FORMAT.format(startCalendar.getTime());
				continue;
			}
		}
		return dateList;
	}
	public static List getDateList(String startDate, String endDate,
			String freqId) {
		List dateList = null;
		if(freqId.toLowerCase().equals("day"))
			dateList = getDateList(startDate,endDate,FREQ_DAY);
		return dateList;
	}
	/**
	 * 判断该日期是否是旬底日期
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isTenDay(String date) {
		if (date == null || date.equals(""))
			return false;
		if (date.endsWith("10") || date.endsWith("20"))
			return true;
		else
			return DateUtil.isMonthDay(date);
	}

	/**
	 * 判断该日期是否是月底日期
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isMonthDay(String date) {
		if (date == null || date.equals(""))
			return false;
		String[] dateSplit = date.split(DateUtil.DATE_SPLIT);
		int day = new Integer(dateSplit[2]).intValue();
		return day == DateUtil.getCalendar(date).getActualMaximum(
				Calendar.DAY_OF_MONTH);
	}

	/**
	 * 判断该日期是否是季末日期
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isSeasonDay(String date) {
		if (date == null || date.equals(""))
			return false;
		return date.endsWith("03-31") || date.endsWith("06-30")
				|| date.endsWith("09-30") || date.endsWith("12-31");
	}

	/**
	 * 判断是否是半年的日期
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isHalfYearDay(String date) {
		if (date == null || date.equals(""))
			return false;
		return date.endsWith("06-30") || date.substring(5).equals("12-31");
	}

	/**
	 * 判断是否是年底日期
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isYearDay(String date) {
		if (date == null || date.equals(""))
			return false;
		return date.endsWith("12-31");
	}
	
	/**
	 * 判断是否是年底日期
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isYearBegin(String date) {
		if (date == null || date.equals(""))
			return false;
		return date.endsWith("01-01");
	}

	/**
	 * 
	 * title:该方法用于该日期是否是否是结息季末 author:db2admin date:2007-12-6
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isCustomSeason(String date) {
		if (date == null || date.equals(""))
			return false;
		return date.endsWith("03-20") || date.endsWith("06-20")
				|| date.endsWith("09-20") || date.endsWith("12-20");
	}

	/**
	 * 
	 * title:该方法用于根据输入的起始年月和频度返回相应的时间列表 author:chenbing date:2008-6-24
	 * 
	 * @param _startDate
	 * @param _endDate
	 * @param freq
	 * @return
	 */
	public static List getYearMonthInterval(String _startDate, String _endDate,
			int freq) {
		List result = null;
		String startDate = _startDate.split("-")[0] + "-"
				+ _startDate.split("-")[1] + "-01";
		String endDate = null;
		int mon = Integer.parseInt(_endDate.split("-")[1]);
		try {

			if (mon == 1 || mon == 3 || mon == 5 || mon == 7 || mon == 8
					|| mon == 10 || mon == 11 || mon == 12)
				endDate = _endDate.split("-")[0] + "-" + _endDate.split("-")[1]
						+ "-31";
			else
				endDate = _endDate.split("-")[0] + "-" + _endDate.split("-")[1]
						+ "-30";

			result = DateUtil.getDateList(startDate, endDate, freq);
		} catch (Exception e) {

			result = null;
		}
		return result;
	}

	/**
	 * 
	 * title:该方法用于检查 author:chenbing date:2008-6-27
	 * 
	 * @param yearStr
	 * @param monthStr
	 * @return
	 */
	public static boolean checkYearMonth(String yearStr, String monthStr) {

		boolean result = true;

		int year;

		int month;

		try {
			if (yearStr == null || monthStr == null
					|| yearStr.trim().equals("") || monthStr.trim().equals(""))

				throw new Exception("error");

			year = Integer.parseInt(yearStr.trim());

			month = Integer.parseInt(monthStr.trim());

			if (year < 1990 || month < 0 || month > 12)

				throw new Exception("error");

		} catch (Exception e) {

			result = false;
		}

		return result;

	}

	/**
	 * 
	 * title:该方法用于检查起始日期是否大于终止日期 author:chenbing date:2008-6-27
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static boolean checkStartEndDate(String startDate, String endDate) {

		String[] strs = startDate.split("-");

		int startYear = Integer.parseInt(strs[0].trim());

		int startMonth = Integer.parseInt(strs[1].trim());

		strs = endDate.split("-");

		int endYear = Integer.parseInt(strs[0].trim());

		int endMonth = Integer.parseInt(strs[1].trim());

		if (startYear > endYear)

			return false;

		if (startYear == endYear && startMonth > endMonth)

			return false;

		return true;
	}

	/**
	 * 取得日期的yyyy-MM-dd形式
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public static String getDateStr(int year, int month, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, DateUtil.MONTH_ARRAY[month]);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		return DATE_FORMAT.format(calendar.getTime());
	}

	/**
	 * 根据年份月份获得该月的最后一天的日期
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static String getMonthLastDayStr(int year, int month) {
		int lastDay = 31;
		if (month == 4 || month == 6 || month == 9 || month == 11)
			lastDay = 30;
		if (month == 2) {
			if (new GregorianCalendar().isLeapYear(year))
				lastDay = 29;
			else
				lastDay = 28;
		}
		return DateUtil.getDateStr(year, month, lastDay);
	}
	
	
	
	

	/**
	 * 根据期数和频度，获取年初一期的报送期数
	 * 
	 * @param date
	 * @param freqId  1:年度  2 半年度   3  季度  4  月度
	 * @return
	 */
	public static String getBeginOfYearDayStr(String date, String freqId) {
		if(freqId.equals("4")){
			return date.substring(0, 4)+"-01-31";
		}else if(freqId.equals("3")){
			return date.substring(0, 4)+"-03-31";
		}else if(freqId.equals("2")){
			return date.substring(0, 4)+"-06-30";
		}else if(freqId.equals("1")){
			return date.substring(0, 4)+"-12-31";
		}else{
			return date.substring(0, 4)+"-01-31";
		}
	}
	
	

	/**
	 * 获得该日期的上季日期
	 * 
	 * @param date
	 * @return
	 */
	public static String getLastSeason(String date) {
		if (date == null || date.equals(""))
			return null;
		String[] dateSplit = date.split(DateUtil.DATE_SPLIT);
		int year = new Integer(dateSplit[0]).intValue();
		int month = new Integer(dateSplit[1]).intValue();

		if (month <= 3)
			return DateUtil.getDateStr(year - 1, 12, 31);
		else if (month <= 6)
			return DateUtil.getDateStr(year, 3, 31);
		else if (month <= 9)
			return DateUtil.getDateStr(year, 6, 30);
		else
			return DateUtil.getDateStr(year, 9, 30);
	}

	/**
	 * 获得该日期的上月末日期
	 * 
	 * @param date
	 * @return
	 */
	public static String getLastMonth(String date) {
		if (date == null || date.equals(""))
			return null;
		String[] dateSplit = date.split(DateUtil.DATE_SPLIT);
		int year = new Integer(dateSplit[0]).intValue();
		int month = new Integer(dateSplit[1]).intValue();
		if (month == 1) {
			month = 12;
			year = year - 1;
		} else {
			month = month - 1;
		}
		return DateUtil.getMonthLastDayStr(year, month);
	}

	/**
	 * 获得去年同期日期
	 * 
	 * @param date
	 * @return
	 */
	public static String getLYSameDay(String date) {
		if (date == null || date.equals(""))
			return null;
		String[] dateSplit = date.split(DateUtil.DATE_SPLIT);
		int year = new Integer(dateSplit[0]).intValue();
		int month = new Integer(dateSplit[1]).intValue();
		int day = new Integer(dateSplit[2]).intValue();
		return DateUtil.getDateStr(year - 1, month, day);
	}

	/**
	 * 
	 * title:该方法用于根据输入的年月和回溯次数返回回溯期间的月末 author:chenbing date:2008-8-8
	 * 
	 * @param dataYear
	 * @param dataMonth
	 * @param count
	 * @return
	 */
	public static List getTrendMonthDate(int dataYear, int dataMonth,
			int count, int freqId) {

		List result = null;

		String endDate = DateUtil.getDateStr(dataYear, dataMonth, 1);

		String startDate = endDate;

		try {

			for (int i = 1; i < count; i++)

				startDate = DateUtil.getLastFreqDate(startDate, freqId);

			result = DateUtil.getDateList(startDate, endDate, new Integer(
					freqId));

			result.add(DateUtil.getMonthLastDayStr(dataYear, dataMonth));

		} catch (Exception e) {

			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 
	 * title:该方法用于返回有分隔符的明细日期,该日期精确到时分秒 author:Nick date:2008-2-29
	 * 
	 * @return String
	 */
	public static String getTodayDetail() {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return (format.format(calendar.getTime()));
	}
	/**
	 * 该方法按14位数字编码返回时间戳字符串
	 * @return
	 */
	public static String getTodayDetailNumber(){
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat(DATA_TIME_NUMBER);
		return (format.format(calendar.getTime()));
	}
	public static Date getTodayTimeStamp(){
		Calendar calendar = Calendar.getInstance();
		return calendar.getTime();
	}
	public static String getLastDay(String date) {
		if (date == null || date.equals(""))
			return null;
		Calendar calendar = DateUtil.getCalendar(date);
		calendar.add(Calendar.DATE, -1);
		return DATE_FORMAT.format(calendar.getTime());
	}
	public static String getLastDay(String date,int n) {
		if (date == null || date.equals(""))
			return null;
		Calendar calendar = DateUtil.getCalendar(date);
		calendar.add(Calendar.DATE, n);
		return DATE_FORMAT.format(calendar.getTime());
	}
	/**
	 * 根据日期返回向后偏移的天数
	 * @param date
	 * @param n
	 * @return
	 */
	public static Date getNextDay(Date date,int n){
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, n);
		return calendar.getTime();
	}
	/**
	 * 根据频度取得该日期的上期日期
	 * 
	 * @param date
	 *            日期
	 * @param freq
	 *            频度
	 * @return
	 */
	public static String getLastFreqDate(String date, Integer freq) {
		if (freq.intValue() == DateUtil.FREQ_MONTH)
			return DateUtil.getLastMonth(date);
		else if (freq.intValue() == DateUtil.FREQ_SEASON)
			return DateUtil.getLastSeason(date);
		else if (freq.intValue() == DateUtil.FREQ_HALFYEAR)
			return DateUtil.getLastHalfYear(date);
		else if (freq.intValue() == DateUtil.FREQ_YEAR)
			return DateUtil.getLastYear(date);
		else
			return null;
	}

	/**
	 * 获得该日期的上半年日期
	 * 
	 * @param date
	 * @return
	 */
	public static String getLastHalfYear(String date) {
		if (date == null || date.equals(""))
			return null;
		String[] dateSplit = date.split(DateUtil.DATE_SPLIT);
		int year = new Integer(dateSplit[0]).intValue();
		int month = new Integer(dateSplit[1]).intValue();

		if (month <= 6)
			return DateUtil.getDateStr(year - 1, 12, 31);
		else
			return DateUtil.getDateStr(year, 6, 30);

	}

	/**
	 * 获得上年末的日期
	 * 
	 * @param date
	 * @return
	 */
	public static String getLastYear(String date) {
		if (date == null || date.equals(""))
			return null;
		String[] dateSplit = date.split(DateUtil.DATE_SPLIT);
		int year = new Integer(dateSplit[0]).intValue();
		return DateUtil.getDateStr(year - 1, 12, 31);

	}

	/**
	 * @param args
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws ParseException {
		Calendar cal = Calendar.getInstance();
		Calendar cal1 = Calendar.getInstance();
		Date date1 = cal.getTime();
		Date date2 = cal1.getTime();;
		System.out.println(date1.after(date2));
		System.out.println(DateUtil.getLastDay("2013-10-01"));
	}

	public static Date getLargeDate(Date date1, Date date2) {
		long d1 = date2long(date1);
		long d2 = date2long(date2);
		if (d1 > d2) {
			return date1;
		} else if (d1 < d2) {
			return date2;
		} else {
			return date1;
		}
	}

	public static long date2long(Date date) {

		return Long.parseLong(toSimpleDateFormat(date, "yyyyMMddHHmmss"));
	}

	public static String toSimpleDateFormat(Date date, String simpleDateFormat) {
		SimpleDateFormat format = new SimpleDateFormat(simpleDateFormat);
		if (date == null) {
			return null;
		} else {
			return format.format(date);
		}
	}

	public static Date getDateByString(String date, String formatStr) {
		SimpleDateFormat format = new SimpleDateFormat(formatStr);
		Date tempDate = null;
		if (date == null || date.equals("")) {
			return null;
		} else {
			try {
				tempDate = format.parse(date);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return tempDate;
	}
	
	public static java.sql.Timestamp stringToTime(String dateString)
	throws java.text.ParseException {

	DateFormat dateFormat;
	dateFormat = new SimpleDateFormat("yyyy-MM",
			Locale.ENGLISH);// 设定格式
	// dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss",
	// Locale.ENGLISH);
	dateFormat.setLenient(false);
	java.util.Date timeDate = dateFormat.parse(dateString);// util类型
	java.sql.Timestamp dateTime = new java.sql.Timestamp(timeDate.getTime());// Timestamp类型,timeDate.getTime()返回一个long型
	return dateTime;
	}

	public static java.sql.Timestamp string2Time(String dateString)
			throws java.text.ParseException {

		DateFormat dateFormat;
		dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss.SSS",
				Locale.ENGLISH);// 设定格式
		// dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss",
		// Locale.ENGLISH);
		dateFormat.setLenient(false);
		java.util.Date timeDate = dateFormat.parse(dateString);// util类型
		java.sql.Timestamp dateTime = new java.sql.Timestamp(timeDate.getTime());// Timestamp类型,timeDate.getTime()返回一个long型
		return dateTime;
	}
	/**
     * 获得两个日期之前相差的月份<br>
     * 
     * @param start
     * @param e
     * @return
     */
    public static int getMonth(Date start, Date end) {
        if (start.after(end)) {
            Date t = start;
            start = end;
            end = t;
        }
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(start);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(end);
        Calendar temp = Calendar.getInstance();
        temp.setTime(end);
        temp.add(Calendar.DATE, 1);

        int year = endCalendar.get(Calendar.YEAR)
                - startCalendar.get(Calendar.YEAR);
        int month = endCalendar.get(Calendar.MONTH)
                - startCalendar.get(Calendar.MONTH);

        if ((startCalendar.get(Calendar.DATE) == 1)
                && (temp.get(Calendar.DATE) == 1)) {
            return year * 12 + month + 1;
        } else if ((startCalendar.get(Calendar.DATE) != 1)
                && (temp.get(Calendar.DATE) == 1)) {
            return year * 12 + month;
        } else if ((startCalendar.get(Calendar.DATE) == 1)
                && (temp.get(Calendar.DATE) != 1)) {
            return year * 12 + month;
        } else {
            return (year * 12 + month - 1) < 0 ? 0 : (year * 12 + month);
        }
    }

}
