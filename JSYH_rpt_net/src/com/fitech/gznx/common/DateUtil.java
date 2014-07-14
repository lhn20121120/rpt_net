package com.fitech.gznx.common;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.fitech.gznx.common.Config;


/**
 * 日期工具类
 * 
 * @author db2admin
 * 
 */
public class DateUtil {
	/**
	 * 用于存放月份所对应的值
	 */
	public static final int[] MONTH_ARRAY = { 0, Calendar.JANUARY,
			Calendar.FEBRUARY, Calendar.MARCH, Calendar.APRIL, Calendar.MAY,
			Calendar.JUNE, Calendar.JULY, Calendar.AUGUST, Calendar.SEPTEMBER,
			Calendar.OCTOBER, Calendar.NOVEMBER, Calendar.DECEMBER };

	/**
	 * 日频度
	 */
	public static final int FREQ_DAY = 6;

	/**
	 * 旬频度
	 */
	public static final int FREQ_TENDAY = 5;

	/**
	 * 月频度
	 */
	public static final int FREQ_MONTH = 1;

	/**
	 * 季频度
	 */
	public static final int FREQ_SEASON = 2;

	/**
	 * 半年频度
	 */
	public static final int FREQ_HALFYEAR = 3;

	/**
	 * 年频度
	 */
	public static final int FREQ_YEAR = 4;



	/**
	 * 用于格式化日期
	 */
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd");

	/**
	 * 时间分割符
	 */
	public static final String DATE_SPLIT = "-";

	/**
	 * 一天的毫秒时间
	 */
	public static final long ONE_DAY_MILLIS = 24 * 60 * 60 * 1000;

	/**
	 * 异常处理日期
	 */
	public static String ERROR_DATE = "1900-01-01";

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
	 * 根据年份月份获得该月的最后一天的日期
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static int getMonthLastDay(int year, int month) {
		int lastDay = 31;
		if (month == 4 || month == 6 || month == 9 || month == 11)
			lastDay = 30;
		if (month == 2) {
			if (new GregorianCalendar().isLeapYear(year))
				lastDay = 29;
			else
				lastDay = 28;
		}
		return lastDay;
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
	 * 获得该日期的上日日期
	 * 
	 * @param date
	 * @return
	 */
	public static String getLastDay(String date) {
		if (date == null || date.equals(""))
			return null;
		Calendar calendar = DateUtil.getCalendar(date);
		long newMillis = calendar.getTimeInMillis() - DateUtil.ONE_DAY_MILLIS;
		calendar.setTimeInMillis(newMillis);
		return DATE_FORMAT.format(calendar.getTime());
	}

	/**
	 * 获得该日期的上旬日期
	 * 
	 * @param date
	 * @return
	 */
	public static String getLastTenDay(String date) {
		if (date == null || date.equals(""))
			return null;
		String[] dateSplit = date.split(DateUtil.DATE_SPLIT);
		int year = new Integer(dateSplit[0]).intValue();
		int month = new Integer(dateSplit[1]).intValue();
		int day = new Integer(dateSplit[2]).intValue();
		if (day >= 1 && day <= 10) {
			if (month == 1) {
				month = 12;
				year = year - 1;
			} else {
				month = month - 1;
			}
			return DateUtil.getMonthLastDayStr(year, month);
		} else if (day > 10 && day <= 20)
			return DateUtil.getDateStr(year, month, 10);
		else
			return DateUtil.getDateStr(year, month, 20);
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
	 * 
	 * title:该方法用于获得指定日期的上个结息季度日期(修改中)
	 * author:chenbing
	 * date:2008-4-16
	 * @param date
	 * @return
	 */
    public static String getLastCustom(String date){
    	if (date == null || date.equals(""))
			return null;
		String[] dateSplit = date.split(DateUtil.DATE_SPLIT);
		int year = new Integer(dateSplit[0]).intValue();
		return DateUtil.getDateStr(year - 1, 12, 31);
    	
    }
	/**
	 * 
	 * title:该方法用于返回指定日期的年末 author:chenbing date:2008-3-26
	 * 
	 * @param date
	 * @return
	 */
	public static String getEndYear(String date) {
		if (date == null || date.equals(""))
			return null;
		return date.split("-")[0] + "-12-31";
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
	 * title:该方法用于根据输入的日期和往年数返回往年同期日期 author:chenbing date:2008-3-26
	 * 
	 * @param date
	 * @param from
	 * @return
	 */
	public static String getFySameDay(String date, int from) {
		if (date == null || date.equals(""))
			return null;
		String[] dateSplit = date.split(DateUtil.DATE_SPLIT);
		int year = new Integer(dateSplit[0]).intValue();
		int month = new Integer(dateSplit[1]).intValue();
		int day = new Integer(dateSplit[2]).intValue();
		return DateUtil.getDateStr(year - from, month, day);
	}

	/**
	 * 获得旬初日期
	 * 
	 * @param date
	 * @return
	 */
	public static String getStartTenday(String date) {
		if (date == null || date.equals(""))
			return null;
		String[] dateSplit = date.split(DateUtil.DATE_SPLIT);
		int year = new Integer(dateSplit[0]).intValue();
		int month = new Integer(dateSplit[1]).intValue();
		int day = new Integer(dateSplit[2]).intValue();

		if (day >= 1 && day <= 10)
			return DateUtil.getDateStr(year, month, 1);
		else if (day > 10 && day <= 20)
			return DateUtil.getDateStr(year, month, 11);
		else
			return DateUtil.getDateStr(year, month, 21);
	}

	/**
	 * 获得月初日期
	 * 
	 * @param date
	 * @return
	 */
	public static String getStartMonth(String date) {
		if (date == null || date.equals(""))
			return null;
		String[] dateSplit = date.split(DateUtil.DATE_SPLIT);
		int year = new Integer(dateSplit[0]).intValue();
		int month = new Integer(dateSplit[1]).intValue();

		return DateUtil.getDateStr(year, month, 1);
	}

	/**
	 * 获得季初日期
	 * 
	 * @param date
	 * @return
	 */
	public static String getStartSeason(String date) {
		if (date == null || date.equals(""))
			return null;
		String[] dateSplit = date.split(DateUtil.DATE_SPLIT);
		int year = new Integer(dateSplit[0]).intValue();
		int month = new Integer(dateSplit[1]).intValue();

		if (month <= 3)
			return DateUtil.getDateStr(year, 1, 1);
		else if (month <= 6)
			return DateUtil.getDateStr(year, 4, 1);
		else if (month <= 9)
			return DateUtil.getDateStr(year, 7, 1);
		else
			return DateUtil.getDateStr(year, 10, 1);
	}

	/**
	 * 获得半年初日期
	 * 
	 * @param date
	 * @return
	 */
	public static String getStartHalfYear(String date) {
		if (date == null || date.equals(""))
			return null;
		String[] dateSplit = date.split(DateUtil.DATE_SPLIT);
		int year = new Integer(dateSplit[0]).intValue();
		int month = new Integer(dateSplit[1]).intValue();

		if (month <= 6)
			return DateUtil.getDateStr(year, 1, 1);
		else
			return DateUtil.getDateStr(year, 7, 1);
	}

	/**
	 * 获得年初日期
	 * 
	 * @param date
	 * @return
	 */
	public static String getStartYear(String date) {
		if (date == null || date.equals(""))
			return null;
		String[] dateSplit = date.split(DateUtil.DATE_SPLIT);
		int year = new Integer(dateSplit[0]).intValue();
		return DateUtil.getDateStr(year, 1, 1);
	}

	/**
	 * 
	 * title:该方法用于返回结息季初 author:chenbing date:2008-3-26
	 * 
	 * @param date
	 * @return
	 */
	public static String getStartCustom(String date) {
		if (date == null || date.equals(""))
			return null;
		String result = "";
		String[] dateSplit = date.split(DateUtil.DATE_SPLIT);
		int year = new Integer(dateSplit[0]).intValue();
		int month = new Integer(dateSplit[1]).intValue();
		int day = new Integer(dateSplit[2]).intValue();
		String startSeason = DateUtil.getStartSeason(date);
		if (day <= 20
				|| (month != 3 && month != 6 && month != 9 && month != 12)) {
			if (startSeason.endsWith("-01-01")) {
				int tmp = Integer.parseInt(dateSplit[0]) - 1;
				result = tmp + "-12-21";
			} else if (startSeason.endsWith("-04-01"))
				result = year + "-03-21";
			else if (startSeason.endsWith("-07-01"))
				result = year + "-06-21";
			else if (startSeason.endsWith("-10-01"))
				result = year + "-09-21";
		} else {
			if (startSeason.endsWith("-01-01") && month == 3)
				result = year + "-03-21";
			else if (startSeason.endsWith("-04-01") && month == 6)
				result = year + "-06-21";
			else if (startSeason.endsWith("-07-01") && month == 9)
				result = year + "-09-21";
			else if (startSeason.endsWith("-10-01") && month == 12)
				result = year + "-12-21";
		}
		return result;
	}

	/**
	 * 
	 * title:该方法用于根据频度获得期初到今天的天数
	 * 
	 * @param date
	 * @return
	 */
	public static String getDays(int freq) {
		String result = null;
		String today = DateUtil.getTodayDate();
		String startDate = today;
		try {
			if (freq == 1)
				startDate = today;
			else if (freq == 2)
				startDate = DateUtil.getStartTenday(today);
			else if (freq == 3)
				startDate = DateUtil.getStartMonth(today);
			else if (freq == 4)
				startDate = DateUtil.getStartSeason(today);
			else if (freq == 5)
				startDate = DateUtil.getStartHalfYear(today);
			else if (freq == 6)
				startDate = DateUtil.getStartYear(today);
			else if (freq == 7)
				startDate = DateUtil.getStartCustom(today);
			result = String.valueOf(DateUtil.getDays(startDate, today));
		} catch (Exception e) {
			result = "0";
		}
		return result;
	}

	/**
	 * title:该方法用于根据频度获得频度和指定日期获得期初到指定日期的天数 author:EREPORT date:2008-1-16
	 * 
	 * @param freq
	 * @param date
	 * @return
	 */
	public static String getDays(int freq, String date) {
		String result = null;
		String startDate = null;
		try {
			if (freq == 1)
				startDate = date;
			else if (freq == 2)
				startDate = DateUtil.getStartTenday(date);
			else if (freq == 3)
				startDate = DateUtil.getStartMonth(date);
			else if (freq == 4)
				startDate = DateUtil.getStartSeason(date);
			else if (freq == 5)
				startDate = DateUtil.getStartHalfYear(date);
			else if (freq == 6)
				startDate = DateUtil.getStartYear(date);
			else if (freq==7)
				startDate = DateUtil.getStartCustom(date);
			result = String.valueOf(DateUtil.getDays(startDate, date));
		} catch (Exception e) {
			result = "0";
		}
		return result;
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
	 * 根据时间字串,返回该时间的Canlendar对象
	 * 
	 * @param date
	 * @return
	 */
	public static GregorianCalendar getCalendar(String date) {
		if (date == null || date.equals(""))
			return null;
		String[] dateSplit = date.split(DateUtil.DATE_SPLIT);
		int year = new Integer(dateSplit[0]).intValue();
		int month = new Integer(dateSplit[1]).intValue();
		int day = new Integer(dateSplit[2]).intValue();
		return new GregorianCalendar(year, DateUtil.MONTH_ARRAY[month], day);
	}

	/**
	 * 根据起始时间和终止时间获得该时间段中相隔的天数
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int getDays(String startDate, String endDate)
			throws Exception {
		if (startDate == null || startDate.equals("") || endDate == null
				|| endDate.equals(""))
			throw new NullPointerException("起始时间或终止时间为空!");

		/* 开始时间Calendar */
		Calendar startDateCalendar = DateUtil.getCalendar(startDate);
		/* 终止时间Calendar */
		Calendar endDateCalendar = DateUtil.getCalendar(endDate);

		long days = (endDateCalendar.getTimeInMillis() - startDateCalendar
				.getTimeInMillis())
				/ DateUtil.ONE_DAY_MILLIS;
		if (days < 0)
			throw new IllegalArgumentException("起始时间大于终止时间");
		return new Integer(String.valueOf(days)).intValue() + 1;
	}

	/**
	 * 根据起始时间和结束时间,获得该间隔时间中的每月的最后一天的日期
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static List getBetweenDate(String startDate, String endDate) {
		if (startDate.equals(endDate))
			return null;
		List dateList = new ArrayList();
		GregorianCalendar startCalendar = getCalendar(startDate);
		GregorianCalendar endCalendar = getCalendar(endDate);
		if (startCalendar.after(endCalendar))
			return null;

		endCalendar.add(GregorianCalendar.DATE, -1);

		String lastDayMonthLastDay = getLastMonth((DATE_FORMAT
				.format(endCalendar.getTime())));

		while (!lastDayMonthLastDay.equals(startDate)) {
			// System.out.println("**"+lastDayMonthLastDay);
			if (!dateList.contains(lastDayMonthLastDay))
				dateList.add(lastDayMonthLastDay);
			endCalendar.add(GregorianCalendar.DATE, -1);
			lastDayMonthLastDay = getLastMonth((DATE_FORMAT.format(endCalendar
					.getTime())));
		}

		return dateList;
	}

	/**
	 * 根据起始时间和结束时间,获得该间隔时间中的每天的日期 如果起始日期和终止日期相同则返回该起始日期
	 * 
	 * @param startDate
	 *            起始日期
	 * @param endDate
	 *            终止日期
	 * @return
	 */
	public static List getDateList(String startDate, String endDate) {
		if (startDate == null || startDate.equals("") || endDate == null
				|| endDate.equals(""))
			return null;

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

		while (!newDateStr.equals(endDate)) {
			if (!dateList.contains(newDateStr))
				dateList.add(newDateStr);
			startCalendar.add(GregorianCalendar.DATE, 1);
			newDateStr = DATE_FORMAT.format(startCalendar.getTime());
		}
		dateList.add(newDateStr);
		return dateList;
	}

	/**
	 * 把yyyy-mm-dd转换为yyyy年mm月dd日的形式
	 * 
	 * @param dateStr
	 * @return
	 */
	public static String parseToChineseDate(String dateStr) {
		if (dateStr == null || dateStr.equals(""))
			return null;

		String[] dateSplit = dateStr.split(DateUtil.DATE_SPLIT);
		int year = new Integer(dateSplit[0]).intValue();
		int month = new Integer(dateSplit[1]).intValue();
		int day = new Integer(dateSplit[2]).intValue();

		return year + "年" + month + "月" + day + "日";
	}

	/**
	 * 从日期获得年
	 */
	public static String getYear(String dateStr) {
		String result = null;
		if (dateStr == null || dateStr.equals(""))
			return null;

		String[] dateSplit = dateStr.split(DateUtil.DATE_SPLIT);
		result = new String(dateSplit[0]);

		return result;
	}

	/**
	 * 从日期获得月
	 */
	public static String getMonth(String dateStr) {
		String result = null;
		if (dateStr == null || dateStr.equals(""))
			return null;

		String[] dateSplit = dateStr.split(DateUtil.DATE_SPLIT);
		result = new String(dateSplit[1]);

		return result;
	}

	/**
	 * 从日期获得日
	 */
	public static String getDay(String dateStr) {
		String result = null;
		if (dateStr == null || dateStr.equals(""))
			return null;

		String[] dateSplit = dateStr.split(DateUtil.DATE_SPLIT);
		result = new String(dateSplit[2]);

		return result;
	}

	/**
	 * 按照周期抽取时间
	 * 
	 * @param startDate
	 * @param endDate
	 * @param cirType
	 * @param interval
	 * @return
	 * @throws Exception
	 */
	public static List getdateList(String startDate, String endDate,
			int cirType, int interval) throws Exception {

		List dateList = new ArrayList();

		if (getDays(startDate, endDate) == 0 || cirType == 0) {

			dateList.add(startDate);

			return dateList;

		}

		dateList.add(startDate);

		GregorianCalendar startCalendar = getCalendar(startDate);

		GregorianCalendar endCalendar = getCalendar(endDate);

		if (cirType == 1) {

			/** 按天循环 */

			startCalendar.add(GregorianCalendar.DAY_OF_MONTH, interval + 1);

			while (!startCalendar.after(endCalendar)) {

				String tempdate = getdate(startCalendar);

				dateList.add(tempdate);

				startCalendar.add(GregorianCalendar.DAY_OF_MONTH, interval + 1);

			}
			return dateList;
		}

		else if (cirType == 2) {

			/** 按周循环 */

			startCalendar = getWeekDay(startCalendar, interval);

			while (!startCalendar.after(endCalendar)) {

				String tempdate = getdate(startCalendar);

				dateList.add(tempdate);

				startCalendar.add(GregorianCalendar.DAY_OF_WEEK, 7);

			}

			return dateList;

		}

		else if (cirType == 3) {

			/** 按月循环 */

			String day = String.valueOf(interval);

			if (day.length() == 1)

				day = "0" + day;

			List dayList = getDateList(startDate, endDate);

			dayList.remove(0);

			if (dayList != null && dayList.size() != 0) {

				for (int i = 0; i < dayList.size(); i++) {

					String tempdate = (String) dayList.get(i);

					String[] dateSplit = tempdate.split("-");

					String tempday = dateSplit[2];

					if (tempday.equals(day))

						dateList.add(tempdate);
				}
			}

			return dateList;

		}
		return dateList;
	}

	/**
	 * 将GregorianCalendar转换为yyyy-mm-dd
	 * 
	 * @param gregorianCalendar
	 * @return
	 */
	public static String getdate(GregorianCalendar gregorianCalendar) {

		String date = "";

		int year = gregorianCalendar.get(GregorianCalendar.YEAR);

		String y = String.valueOf(year);

		int month = gregorianCalendar.get(GregorianCalendar.MONTH) + 1;

		String m = String.valueOf(month);

		if (m.length() == 1) {

			m = "0" + m;

		}

		int day = gregorianCalendar.get(GregorianCalendar.DAY_OF_MONTH);

		String d = String.valueOf(day);

		if (d.length() == 1) {

			d = "0" + d;

		}

		return y + "-" + m + "-" + d;

	}

	/**
	 * 得到离给定日历最近的星期“？”
	 * 
	 * @param gregorianCalendar
	 * @param weekday
	 * @return
	 */
	public static GregorianCalendar getWeekDay(
			GregorianCalendar gregorianCalendar, int weekday) {

		int curweekday = gregorianCalendar.get(GregorianCalendar.DAY_OF_WEEK);

		if (curweekday < weekday) {

			gregorianCalendar.add(GregorianCalendar.DAY_OF_WEEK, weekday
					- curweekday + 1);

		}

		else if (curweekday > weekday) {

			gregorianCalendar.add(GregorianCalendar.DAY_OF_WEEK, weekday + 7
					- curweekday + 1);

		}

		return gregorianCalendar;

	}

	/**
	 * 返回今天的日期
	 */
	public static String getTodayDate() {

		Calendar calendar = Calendar.getInstance();

		return (DATE_FORMAT.format(calendar.getTime()));
	}

	/**
	 * 
	 * title:该方法用于返回没有分隔符的明细日期,该日期精确到时分秒 author:EREPORT date:2008-1-29
	 * 
	 * @return String
	 */
	public static String getTodayDetil() {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddkkmmss");
		return (format.format(calendar.getTime()));
	}

	/**
	 * 
	 * title:该方法用于返回有分隔符的明细日期,该日期精确到时分秒 author:Nick date:2008-2-29
	 * 
	 * @return String
	 */
	public static String getTodayDetail() {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
		return (format.format(calendar.getTime()));
	}

	/**
	 * 获得本月天数
	 * 
	 */
	public static int DaysOfCurMonth(int curYear, int curMonth)
			throws Exception {

		int resultDays = 30;
		/* 取得当年年份和月份 */
		if (curMonth == 1 || curMonth == 3 || curMonth == 5 || curMonth == 7
				|| curMonth == 8

				|| curMonth == 10 || curMonth == 12)

			resultDays = 31;

		if (curMonth == 2) {

			resultDays = 28;

			if ((curYear % 4 == 0 && curYear % 100 != 0) || curYear % 400 == 0)

				resultDays = 29;

		}

		return resultDays;

	}

	/**
	 * 得到当月的第一天是星期几
	 * 
	 * @param year
	 * @param month
	 * @return
	 * @throws Exception
	 */
	public static int getfirtDayOfMonth(int year, int month) throws Exception {

		Calendar calendar = Calendar.getInstance();

		int curMonth = calendar.get(Calendar.MONTH);

		int curYear = calendar.get(Calendar.YEAR);

		calendar.add(Calendar.DAY_OF_MONTH, 1 - calendar
				.get(Calendar.DAY_OF_MONTH));

		calendar.add(Calendar.MONTH, (month - 1) - curMonth);

		calendar.add(Calendar.YEAR, year - curYear);

		return calendar.get(Calendar.DAY_OF_WEEK);

	}

	/**
	 * 根据开始时间，结束时间和频度，获得该时间区间内所有该频度的日期列表
	 * 
	 * @param startDate
	 *            开始日期
	 * @param endDate
	 *            结束日期
	 * @param freq
	 *            频度
	 * @return
	 */
	public static List getDateList(String startDate, String endDate,
			Integer freq) {
		if (startDate == null || startDate.equals("") || endDate == null
				|| endDate.equals(""))
			return null;
        
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
			// 旬频度
			if (freq.equals(Config.FREQ_TENDAY)
					&& DateUtil.isTenDay(newDateStr)) {
				dateList.add(newDateStr);
				startCalendar.add(GregorianCalendar.DATE, 15);
				newDateStr = DATE_FORMAT.format(startCalendar.getTime());
				continue;
			}
			/* 月频度 */
			else if (freq.equals(Config.FREQ_MONTH)
					&& DateUtil.isMonthDay(newDateStr)) {
				dateList.add(newDateStr);
				startCalendar.add(GregorianCalendar.MONTH, 1);
				newDateStr = DATE_FORMAT.format(startCalendar.getTime());
				continue;
			}
			/* 季频度 */
			else if (freq.equals(Config.FREQ_SEASON)
					&& DateUtil.isSeasonDay(newDateStr)) {
				dateList.add(newDateStr);
				startCalendar.add(GregorianCalendar.MONTH, 3);
				newDateStr = DATE_FORMAT.format(startCalendar.getTime());
				continue;
			}
			/* 半年频度 */
			else if (freq.equals(Config.FREQ_HALFYEAR)
					&& DateUtil.isHalfYearDay(newDateStr)) {
				dateList.add(newDateStr);
				startCalendar.add(GregorianCalendar.MONTH, 6);
				newDateStr = DATE_FORMAT.format(startCalendar.getTime());
				continue;
			}
			/* 年频度 */
			else if (freq.equals(Config.FREQ_YEAR)
					&& DateUtil.isYearDay(newDateStr)) {
				dateList.add(newDateStr);
				startCalendar.add(GregorianCalendar.YEAR, 1);
				newDateStr = DATE_FORMAT.format(startCalendar.getTime());
				continue;
			} else {
				startCalendar.add(GregorianCalendar.DATE, 1);
				newDateStr = DATE_FORMAT.format(startCalendar.getTime());
				continue;
			}
		}
		return dateList;
	}

	/**
	 * 给一个指定日期加上一个天数，返回该新日期
	 * 
	 * @param date
	 *            原日期
	 * @param day
	 *            加的天数
	 * @return
	 */
	public static String dateAdd(String date, int day) {
		if (date == null || date.equals(""))
			return null;
		GregorianCalendar dateCalendar = getCalendar(date);
		dateCalendar.add(GregorianCalendar.DATE, day);
		return DATE_FORMAT.format(dateCalendar.getTime());
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
		if (freq.intValue() == DateUtil.FREQ_DAY)
			return DateUtil.getLastDay(date);
		else if (freq.intValue() == DateUtil.FREQ_TENDAY)
			return DateUtil.getLastTenDay(date);
		else if (freq.intValue() == DateUtil.FREQ_MONTH)
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
	 * 根据频度取得该日期的期初日期
	 * 
	 * @param date
	 *            日期
	 * @param freq
	 *            频度
	 * @return
	 */
	public static String getStartFreqDate(String date, Integer freq) {
		if (freq.equals(Config.FREQ_TENDAY))
			return DateUtil.getStartTenday(date);
		else if (freq.equals(Config.FREQ_MONTH))
			return DateUtil.getStartMonth(date);
		else if (freq.equals(Config.FREQ_SEASON))
			return DateUtil.getStartSeason(date);
		else if (freq.equals(Config.FREQ_HALFYEAR))
			return DateUtil.getStartHalfYear(date);
		else if (freq.equals(Config.FREQ_YEAR))
			return DateUtil.getStartYear(date);
		else
			return null;
	}

	/**
	 * 根据频度取得该日期的上期参数,现用于指标补录
	 * 
	 * @param freq
	 *            频度
	 * @return
	 */
	public static String getLastFreqPara(Integer freq) {
		if (freq == null)
			return null;

		Calendar calendar = Calendar.getInstance();
		String date = DATE_FORMAT.format(calendar.getTime());
		String tempDate;
		String ParaDate = null;
		String year;
		String month;
		String day;

		// 频度：日
		 if (freq.equals(Config.FREQ_DAY))
			ParaDate = DateUtil.getLastDay(date);
		// 频度：旬
		else if (freq.equals(Config.FREQ_TENDAY)) {
			tempDate = DateUtil.getLastTenDay(date);
			year = DateUtil.getYear(tempDate);
			month = getMonthStr(DateUtil.getMonth(tempDate));
			day = DateUtil.getDay(tempDate);
			if (day.equals("10"))
				ParaDate = year + "," + month + "_1";
			else if (day.equals("20"))
				ParaDate = year + "," + month + "_2";
			else
				ParaDate = year + "," + month + "_3";
		}
		// 频度：月
		else if (freq.equals(Config.FREQ_MONTH)) {
			tempDate = DateUtil.getLastMonth(date);
			year = DateUtil.getYear(tempDate);
			month = DateUtil.getMonth(tempDate);
			day = DateUtil.getDay(tempDate);
			ParaDate = year + "," + month;
		}
		// 频度：季
		else if (freq.equals(Config.FREQ_SEASON)) {
			tempDate = DateUtil.getLastSeason(date);
			year = DateUtil.getYear(tempDate);
			month = DateUtil.getMonth(tempDate);
			if (month.equals("03"))
				ParaDate = year + ",1";
			if (month.equals("06"))
				ParaDate = year + ",2";
			if (month.equals("09"))
				ParaDate = year + ",3";
			if (month.equals("12"))
				ParaDate = year + ",4";
		}
		// 频度：半年
		else if (freq.equals(Config.FREQ_HALFYEAR)) {
			tempDate = DateUtil.getLastHalfYear(date);
			year = DateUtil.getYear(tempDate);
			month = DateUtil.getMonth(tempDate);
			if (month.equals("06"))
				ParaDate = year + ",1";
			if (month.equals("12"))
				ParaDate = year + ",2";
		}
		// 频度：年
		else if (freq.equals(Config.FREQ_YEAR)) {
			tempDate = DateUtil.getLastYear(date);
			ParaDate = DateUtil.getYear(tempDate);
		} else
			ParaDate = null;
		return ParaDate;
	}

	/**
	 * 将两位数月份转成一位数，如“01”转为“1”
	 * 
	 */
	public static String getMonthStr(String date) {
		if (date == null || date.equals("") || date.length() != 2)
			return null;

		if (date.equals("10") || date.equals("11") || date.equals("12"))
			return date;

		else {
			return date.substring(1, 2);
		}
	}

	/**
	 * 
	 * title:该方法用于根据输入的时间判断该时间是否在指定的时间段列表 author:chenbing date:2008-3-1
	 * 
	 * @param time
	 * @param timeList
	 * @return
	 */
	public static boolean inBeweenTime(List timeList) {

		if (timeList == null || timeList.size() == 0)

			return false;

		boolean result = false;

		try {

			List list = timeList;

			int inTime = Integer.parseInt(Calendar.getInstance().getTime()
					.toString().split(" ")[3].replaceAll(":", ""));

			if (list != null) {

				for (int i = 0; i < list.size(); i++) {

					String[] strs = (String[]) list.get(i);

					int beginTime = Integer.parseInt(strs[0]
							.replaceAll(":", ""));

					int endTime = Integer.parseInt(strs[1].replaceAll(":", ""));

					if (beginTime < endTime) {

						if (inTime >= beginTime && inTime <= endTime) {

							result = true;

							break;
						}
					} else {

						if ((inTime >= beginTime && inTime <= 240000)
								|| (inTime >= 0 && inTime <= endTime)) {

							result = true;

							break;
						}
					}
				}
			}
		} catch (Exception e) {

			result = false;
		}
		return result;
	}

	/**
	 * 
	 * title:该方法用于根据频度和指定的日期获得上年同期累计天数 author:chenbing date:2008-3-6
	 * 
	 * @param freq
	 * @param date
	 * @return
	 */
	public static String getLSYC(String date) {

		String result = "0";

		String lastDate = DateUtil.getLYSameDay(date);

		result = DateUtil.getDays(DateUtil.FREQ_YEAR, lastDate);

		return result;
	}

	/**
	 * 
	 * title:该方法用于根据输入的日期返回上年累计天数 author:chenbing date:2008-3-6
	 * 
	 * @param date
	 * @return
	 */
	public static String getLYC(String date) {

		String result = "0";

		String[] strs = date.split("-");

		if (Integer.parseInt(strs[0]) % 4 == 0)

			result = "366";

		else

			result = "365";

		return result;

	}

	/**
	 * 
	 * title:该方法用于根据输入的日期返回上季累计天数 author:chenbing date:2008-3-6
	 * 
	 * @param date
	 * @return
	 */
	public static String getLSC(String date) {

		String result = "0";

		String lastSeason = DateUtil.getLastSeason(date);

		result = DateUtil.getDays(DateUtil.FREQ_SEASON, lastSeason);

		return result;
	}

	/**
	 * 
	 * title:该方法用于根据输入的日期返回累计季月数 author:chenbing date:2008-3-6
	 * 
	 * @param date
	 * @return
	 */
	public static String getSMC(String date) {

		String result = "0";

		int month = Integer.parseInt(date.split("-")[1]);

		if (month == 1 || month == 4 || month == 7 || month == 10)
			result = "1";
		else if (month == 2 || month == 5 || month == 8 || month == 11)
			result = "2";
		else if (month == 3 || month == 6 || month == 9 || month == 12)
			result = "3";
		return result;
	}

	/**
	 * 
	 * title:该方法用于根据输入的日期返回累计年月数 author:chenbing date:2008-3-6
	 * 
	 * @param date
	 * @return
	 */
	public static String getYMC(String date) {

		String result = "0";

		result = String.valueOf(Integer.parseInt(date.split("-")[1]));

		return result;
	}

	/**
	 * 
	 * title:该方法用于根据输入的日期返回季度数 author:chenbing date:2008-3-6
	 * 
	 * @param date
	 * @return
	 */
	public static String getSeasonNo(String date) {

		String result = "0";

		String tmp = DateUtil.getStartFreqDate(date, new Integer(
				DateUtil.FREQ_SEASON));

		if (tmp.endsWith("1-01"))

			result = "1";

		else if (tmp.endsWith("4-01"))

			result = "2";
		else if (tmp.endsWith("7-01"))

			result = "3";

		else if (tmp.endsWith("10-01"))

			result = "4";

		return result;
	}

	/**
	 * 根据频度确定输入的时期是否需要生成指定的操作
	 * 
	 * @return
	 */
	public static boolean isNeedBuild(String date, int buildFreq) {
		/* 日 */
		if (buildFreq == DateUtil.FREQ_DAY)
			return true;
		/* 旬 */
		else if (buildFreq == DateUtil.FREQ_TENDAY)
			return DateUtil.isTenDay(date);
		/* 月 */
		else if (buildFreq == DateUtil.FREQ_MONTH)
			return DateUtil.isMonthDay(date);
		/* 季 */
		else if (buildFreq == DateUtil.FREQ_SEASON)
			return DateUtil.isSeasonDay(date);
		/* 半年 */
		else if (buildFreq == DateUtil.FREQ_HALFYEAR)
			return DateUtil.isHalfYearDay(date);
		/* 年 */
		else if (buildFreq == DateUtil.FREQ_YEAR)
			return DateUtil.isYearDay(date);

		return false;
	}

	/**
	 * 
	 * title:该方法用于根据输入的日期返回频度列表 author:chenbing date:2008-4-1
	 * 
	 * @param date
	 * @return
	 */
	public static List getFreqListByDate(String date) {

		List tmp = new ArrayList();

		List result = null;

		if (DateUtil.isNeedBuild(date, DateUtil.FREQ_DAY))

			tmp.add(String.valueOf(DateUtil.FREQ_DAY));

		if (DateUtil.isNeedBuild(date, DateUtil.FREQ_TENDAY))

			tmp.add(String.valueOf(DateUtil.FREQ_TENDAY));

		if (DateUtil.isNeedBuild(date, DateUtil.FREQ_MONTH))

			tmp.add(String.valueOf(DateUtil.FREQ_MONTH));

		if (DateUtil.isNeedBuild(date, DateUtil.FREQ_SEASON))

			tmp.add(String.valueOf(DateUtil.FREQ_SEASON));

		if (DateUtil.isNeedBuild(date, DateUtil.FREQ_HALFYEAR))

			tmp.add(String.valueOf(DateUtil.FREQ_HALFYEAR));

		if (DateUtil.isNeedBuild(date, DateUtil.FREQ_YEAR))

			tmp.add(String.valueOf(DateUtil.FREQ_YEAR));

		if (tmp.size() != 0)

			result = tmp;

		return result;
	}

	/**
	 * 
	 * title:该方法用于根据输入的日期返回用逗号分割的频度字符串 author:chenbing date:2008-4-1
	 * 
	 * @param date
	 * @return
	 */
	public static String getFreqStrByDate(String date) {

		List tmp = DateUtil.getFreqListByDate(date);

		if (tmp == null)
			return null;

		String result = null;

		result = getStrsBySplitStr(tmp, ",", null);

		return result;
	}
	/**
	 * 
	 * title:该方法用于将指定的对象类型将其用指定的界限符和分隔符将对象中的字符串加工成指定的字符串 author:chenbing
	 * author:chenbing date:2008-2-26
	 * 
	 * @param obj
	 *            指定的对象
	 * @param splitStr
	 *            分隔符
	 * @param bracketStr
	 *            界限符 如果为空则没有界限符
	 * @return
	 */
	public static String getStrsBySplitStr(Object obj, String splitStr,
			String bracketStr) {

		if (obj == null)
			return null;

		bracketStr = bracketStr == null ? "" : bracketStr;

		String result = null;

		StringBuffer sb = new StringBuffer("");

		if (obj instanceof ArrayList) { // 字符串列表

			List list = (List) obj;

			for (int i = 0; i < list.size(); i++)

				sb.append(splitStr + bracketStr + ((String) list.get(i)).trim()
						+ bracketStr);

		} else if (obj instanceof String) {// 字符串

			String str = (String) obj;

			String[] strs = str.split(",");

			for (int i = 0; i < strs.length; i++)

				sb.append(splitStr + bracketStr + strs[i].trim() + bracketStr);

		} else if (obj instanceof HashSet) {// Set

			Set set = (HashSet) obj;

			List list = new ArrayList();

			list.addAll(set);

			for (int i = 0; i < list.size(); i++)

				sb.append(splitStr + bracketStr + ((String) list.get(i)).trim()
						+ bracketStr);
		}
		if (sb.length() > 1)

			result = sb.substring(1);

		return result;
	}
	/**
	 * 
	 * title:该方法用于根据输入的日期和频度返回最近一次的数据日期 author:chenbing date:2008-4-11
	 * 
	 * @param date
	 * @param freq
	 * @return
	 */
	public static String getNearDateByFreq(String date, int freq) {

		String result = null;

		List tmp = DateUtil.getFreqListByDate(date);

		result = (tmp.contains(String.valueOf(freq))) ? date : DateUtil.getLastFreqDate(date,
				new Integer(freq));

		return result;

	}
	public static boolean checkYearMonth(String yearStr, String monthStr) {

		boolean result = true;

		int year;

		int month;

		try {
			if (yearStr == null || monthStr == null)

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
     * title:该方法用于检查起始日期是否大于终止日期
     * author:chenbing
     * date:2008-6-27
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
	
	// 日期转数字
	public static long date2long(Date date) {

		return Long.parseLong(toSimpleDateFormat(date, "yyyyMMddHHmmss"));
	}
	
	/**
	 * 
	 * @param date
	 * @param simpleDateFormat
	 * @return
	 */
	public static String toSimpleDateFormat(Date date, String simpleDateFormat) {
		SimpleDateFormat format = new SimpleDateFormat(simpleDateFormat);
		if (date == null) {
			return null;
		} else {
			return format.format(date);
		}
	}
	
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
	
	/**
	 * 根据频度取得该日期的期底日期
	 * 
	 * @param date
	 *            日期
	 * @param freq
	 *            频度
	 * @return
	 */
	public static String getFreqDateLast(String date, Integer freq) {
		
		if (freq==null) return null;
		
		if (freq.equals(Config.FREQ_DAY)||freq.equals(Config.FREQ_WEEK))
			return date;
		else if (freq.equals(Config.FREQ_TENDAY))
			return DateUtil.getTendayLast(date);
		else if (freq.equals(Config.FREQ_YEARBEGAIN))
			return DateUtil.getStartYear(date);
		else
			return DateUtil.getMonthLast(date);//快报和月报一样
	}
	
	/**
	 * 获得旬底日期
	 * 
	 * @param date
	 * @return
	 */
	public static String getTendayLast(String date) {
		
		if (date == null || date.equals(""))
			return null;
		
		String[] dateSplit = date.split(DateUtil.DATE_SPLIT);
		
		int year = new Integer(dateSplit[0]).intValue();
		int month = new Integer(dateSplit[1]).intValue();
		int day = new Integer(dateSplit[2]).intValue();

		if (day >= 1 && day <= 10)
			return DateUtil.getDateStr(year, month, 10);
		else if (day > 10 && day <= 20)
			return DateUtil.getDateStr(year, month, 20);
		else
			//return DateUtil.getMonthLastDayStr(year, month);
			return DateUtil.getDateStr(year, month, 20);
	}

	/**
	 * 获得月底日期
	 * 
	 * @param date
	 * @return
	 */
	public static String getMonthLast(String date) {
		
		if (date == null || date.equals(""))
			return null;
		
		String[] dateSplit = date.split(DateUtil.DATE_SPLIT);
		int year = new Integer(dateSplit[0]).intValue();
		int month = new Integer(dateSplit[1]).intValue();

		return DateUtil.getMonthLastDayStr(year, month);
	}
	
	/**
	 *  获得昨天的日期 yyyy-MM-dd
	 */
	public static String getYestoday() {

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");   
		
		String date = formatter.format(calendar.getTime());
		
		return date;
	}
	
	/**
	 *  获得当天的日期 yyyy-MM-dd
	 */
	public static String getToday() {

		Calendar calendar = Calendar.getInstance();
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");   
		
		String date = formatter.format(calendar.getTime());
		
		return date;
	}
	
	/**
	 * 根据传入日期获得下月月初日期
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public static String getNextMonthDate(String date) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
		// 格式化结束时间
		Calendar cal = Calendar.getInstance();
		Date day = new Date();
		try{
			day = sdf.parse(date);
		}catch(Exception e){
			return "";
		}
		cal.setTime(day);
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1 , 1);
	
		String term = cal.get(Calendar.YEAR) + "-"+ (cal.get(Calendar.MONTH)+1) +"-1";
		
		return term;
	}
	
	/**
	 * 根据起止时间，返回中间经历的时间点（月）
	 * 
	 * @param startdate
	 *            起始时间
	 * @param enddate
	 *            结束时间
	 * 
	 * @return 时间点数组
	 * 
	 * @throws Exception
	 */
	public static String[] getTerms(String startdate, String enddate)
			throws Exception {
		List list = new ArrayList();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		// 格式化起始时间
		Calendar calStart = Calendar.getInstance();
		Date dateStart = sdf.parse(startdate);
		calStart.setTime(dateStart);
		
		// 格式化结束时间
		Calendar calEnd = Calendar.getInstance();
		Date dateEnd = sdf.parse(enddate);
		calEnd.setTime(dateEnd);
		
		// 给结束日期增加3个月的值，这样才会使最后一个时间点比输入的时间点大
		//calEnd.add(Calendar.MONTH, 3);

		int startMonth = calStart.get(Calendar.MONTH) + 1;// JAVA中月份从0开始，所以要加1
//		int season = 0;// 季度值
//		if (startMonth % 3 == 0) {// 如果没有余数，则正好为3，6，9或者12月份
//			season = startMonth / 3;
//		} else {// 如果有余数，则取最近的季度值
//			season = startMonth / 3 + 1;
//		}
		// 重新设置正确的起始时间
//		calStart.set(calStart.get(Calendar.YEAR), season * 3 - 1, 1);
		
		calStart.set(calStart.get(Calendar.YEAR), startMonth -1 , 1);
		calEnd.set(calEnd.get(Calendar.YEAR), calEnd.get(Calendar.MONTH) + 1 , 1);

		
		int year = 0;
		int month = 0;
		
		while (calStart.before(calEnd)) {
			year = calStart.get(Calendar.YEAR);
			month = calStart.get(Calendar.MONTH) + 1;// JAVA中月份从0开始，所以要加1
			if (String.valueOf(month).length() == 1) {
				list.add(year + "-0" + month);// 1-9月的时候，月份前面补零
			} else {
				list.add(year + "-" + month);
			}
//			calStart.add(Calendar.MONTH, 3);// 增加一个季度
			calStart.add(Calendar.MONTH, 1);

		}

		// 将列表转换为数组
		String[] date = (String[]) list.toArray(new String[list.size()]);

		return date;
	}
	/**
	 * 根据输入的指定日期返回该日期属于周几,周1就返回1，周五就返回5，以此类推，注意周日返回0
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
    public static int getDayOfWeek(int year ,int month,int day){
    	Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, DateUtil.MONTH_ARRAY[month]);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		return calendar.get(Calendar.DAY_OF_WEEK)-1;
    }
	public static void main(String args[]) {

//		System.out.println(DateUtil.getTerms("2009-12-10"));
		
	}
}
