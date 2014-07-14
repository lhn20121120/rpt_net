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
 * ���ڹ�����
 * 
 * @author db2admin
 * 
 */
public class DateUtil {
	/**
	 * ���ڴ���·�����Ӧ��ֵ
	 */
	public static final int[] MONTH_ARRAY = { 0, Calendar.JANUARY,
			Calendar.FEBRUARY, Calendar.MARCH, Calendar.APRIL, Calendar.MAY,
			Calendar.JUNE, Calendar.JULY, Calendar.AUGUST, Calendar.SEPTEMBER,
			Calendar.OCTOBER, Calendar.NOVEMBER, Calendar.DECEMBER };

	/**
	 * ��Ƶ��
	 */
	public static final int FREQ_DAY = 6;

	/**
	 * ѮƵ��
	 */
	public static final int FREQ_TENDAY = 5;

	/**
	 * ��Ƶ��
	 */
	public static final int FREQ_MONTH = 1;

	/**
	 * ��Ƶ��
	 */
	public static final int FREQ_SEASON = 2;

	/**
	 * ����Ƶ��
	 */
	public static final int FREQ_HALFYEAR = 3;

	/**
	 * ��Ƶ��
	 */
	public static final int FREQ_YEAR = 4;



	/**
	 * ���ڸ�ʽ������
	 */
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd");

	/**
	 * ʱ��ָ��
	 */
	public static final String DATE_SPLIT = "-";

	/**
	 * һ��ĺ���ʱ��
	 */
	public static final long ONE_DAY_MILLIS = 24 * 60 * 60 * 1000;

	/**
	 * �쳣��������
	 */
	public static String ERROR_DATE = "1900-01-01";

	/**
	 * �жϸ������Ƿ���Ѯ������
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
	 * �жϸ������Ƿ����µ�����
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
	 * �жϸ������Ƿ��Ǽ�ĩ����
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
	 * �ж��Ƿ��ǰ��������
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
	 * �ж��Ƿ����������
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
	 * title:�÷������ڸ������Ƿ��Ƿ��ǽ�Ϣ��ĩ author:db2admin date:2007-12-6
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
	 * ��������·ݻ�ø��µ����һ�������
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
	 * ��������·ݻ�ø��µ����һ�������
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
	 * ��ø����ڵ���������
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
	 * ��ø����ڵ���Ѯ����
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
	 * ��ø����ڵ�����ĩ����
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
	 * ��ø����ڵ��ϼ�����
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
	 * ��ø����ڵ��ϰ�������
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
	 * �������ĩ������
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
	 * title:�÷������ڻ��ָ�����ڵ��ϸ���Ϣ��������(�޸���)
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
	 * title:�÷������ڷ���ָ�����ڵ���ĩ author:chenbing date:2008-3-26
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
	 * ���ȥ��ͬ������
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
	 * title:�÷������ڸ�����������ں���������������ͬ������ author:chenbing date:2008-3-26
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
	 * ���Ѯ������
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
	 * ����³�����
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
	 * ��ü�������
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
	 * ��ð��������
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
	 * ����������
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
	 * title:�÷������ڷ��ؽ�Ϣ���� author:chenbing date:2008-3-26
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
	 * title:�÷������ڸ���Ƶ�Ȼ���ڳ������������
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
	 * title:�÷������ڸ���Ƶ�Ȼ��Ƶ�Ⱥ�ָ�����ڻ���ڳ���ָ�����ڵ����� author:EREPORT date:2008-1-16
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
	 * ȡ�����ڵ�yyyy-MM-dd��ʽ
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
	 * ����ʱ���ִ�,���ظ�ʱ���Canlendar����
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
	 * ������ʼʱ�����ֹʱ���ø�ʱ��������������
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int getDays(String startDate, String endDate)
			throws Exception {
		if (startDate == null || startDate.equals("") || endDate == null
				|| endDate.equals(""))
			throw new NullPointerException("��ʼʱ�����ֹʱ��Ϊ��!");

		/* ��ʼʱ��Calendar */
		Calendar startDateCalendar = DateUtil.getCalendar(startDate);
		/* ��ֹʱ��Calendar */
		Calendar endDateCalendar = DateUtil.getCalendar(endDate);

		long days = (endDateCalendar.getTimeInMillis() - startDateCalendar
				.getTimeInMillis())
				/ DateUtil.ONE_DAY_MILLIS;
		if (days < 0)
			throw new IllegalArgumentException("��ʼʱ�������ֹʱ��");
		return new Integer(String.valueOf(days)).intValue() + 1;
	}

	/**
	 * ������ʼʱ��ͽ���ʱ��,��øü��ʱ���е�ÿ�µ����һ�������
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
	 * ������ʼʱ��ͽ���ʱ��,��øü��ʱ���е�ÿ������� �����ʼ���ں���ֹ������ͬ�򷵻ظ���ʼ����
	 * 
	 * @param startDate
	 *            ��ʼ����
	 * @param endDate
	 *            ��ֹ����
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
	 * ��yyyy-mm-ddת��Ϊyyyy��mm��dd�յ���ʽ
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

		return year + "��" + month + "��" + day + "��";
	}

	/**
	 * �����ڻ����
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
	 * �����ڻ����
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
	 * �����ڻ����
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
	 * �������ڳ�ȡʱ��
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

			/** ����ѭ�� */

			startCalendar.add(GregorianCalendar.DAY_OF_MONTH, interval + 1);

			while (!startCalendar.after(endCalendar)) {

				String tempdate = getdate(startCalendar);

				dateList.add(tempdate);

				startCalendar.add(GregorianCalendar.DAY_OF_MONTH, interval + 1);

			}
			return dateList;
		}

		else if (cirType == 2) {

			/** ����ѭ�� */

			startCalendar = getWeekDay(startCalendar, interval);

			while (!startCalendar.after(endCalendar)) {

				String tempdate = getdate(startCalendar);

				dateList.add(tempdate);

				startCalendar.add(GregorianCalendar.DAY_OF_WEEK, 7);

			}

			return dateList;

		}

		else if (cirType == 3) {

			/** ����ѭ�� */

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
	 * ��GregorianCalendarת��Ϊyyyy-mm-dd
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
	 * �õ������������������ڡ�����
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
	 * ���ؽ��������
	 */
	public static String getTodayDate() {

		Calendar calendar = Calendar.getInstance();

		return (DATE_FORMAT.format(calendar.getTime()));
	}

	/**
	 * 
	 * title:�÷������ڷ���û�зָ�������ϸ����,�����ھ�ȷ��ʱ���� author:EREPORT date:2008-1-29
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
	 * title:�÷������ڷ����зָ�������ϸ����,�����ھ�ȷ��ʱ���� author:Nick date:2008-2-29
	 * 
	 * @return String
	 */
	public static String getTodayDetail() {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
		return (format.format(calendar.getTime()));
	}

	/**
	 * ��ñ�������
	 * 
	 */
	public static int DaysOfCurMonth(int curYear, int curMonth)
			throws Exception {

		int resultDays = 30;
		/* ȡ�õ�����ݺ��·� */
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
	 * �õ����µĵ�һ�������ڼ�
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
	 * ���ݿ�ʼʱ�䣬����ʱ���Ƶ�ȣ���ø�ʱ�����������и�Ƶ�ȵ������б�
	 * 
	 * @param startDate
	 *            ��ʼ����
	 * @param endDate
	 *            ��������
	 * @param freq
	 *            Ƶ��
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
			// ѮƵ��
			if (freq.equals(Config.FREQ_TENDAY)
					&& DateUtil.isTenDay(newDateStr)) {
				dateList.add(newDateStr);
				startCalendar.add(GregorianCalendar.DATE, 15);
				newDateStr = DATE_FORMAT.format(startCalendar.getTime());
				continue;
			}
			/* ��Ƶ�� */
			else if (freq.equals(Config.FREQ_MONTH)
					&& DateUtil.isMonthDay(newDateStr)) {
				dateList.add(newDateStr);
				startCalendar.add(GregorianCalendar.MONTH, 1);
				newDateStr = DATE_FORMAT.format(startCalendar.getTime());
				continue;
			}
			/* ��Ƶ�� */
			else if (freq.equals(Config.FREQ_SEASON)
					&& DateUtil.isSeasonDay(newDateStr)) {
				dateList.add(newDateStr);
				startCalendar.add(GregorianCalendar.MONTH, 3);
				newDateStr = DATE_FORMAT.format(startCalendar.getTime());
				continue;
			}
			/* ����Ƶ�� */
			else if (freq.equals(Config.FREQ_HALFYEAR)
					&& DateUtil.isHalfYearDay(newDateStr)) {
				dateList.add(newDateStr);
				startCalendar.add(GregorianCalendar.MONTH, 6);
				newDateStr = DATE_FORMAT.format(startCalendar.getTime());
				continue;
			}
			/* ��Ƶ�� */
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
	 * ��һ��ָ�����ڼ���һ�����������ظ�������
	 * 
	 * @param date
	 *            ԭ����
	 * @param day
	 *            �ӵ�����
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
	 * ����Ƶ��ȡ�ø����ڵ���������
	 * 
	 * @param date
	 *            ����
	 * @param freq
	 *            Ƶ��
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
	 * ����Ƶ��ȡ�ø����ڵ��ڳ�����
	 * 
	 * @param date
	 *            ����
	 * @param freq
	 *            Ƶ��
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
	 * ����Ƶ��ȡ�ø����ڵ����ڲ���,������ָ�겹¼
	 * 
	 * @param freq
	 *            Ƶ��
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

		// Ƶ�ȣ���
		 if (freq.equals(Config.FREQ_DAY))
			ParaDate = DateUtil.getLastDay(date);
		// Ƶ�ȣ�Ѯ
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
		// Ƶ�ȣ���
		else if (freq.equals(Config.FREQ_MONTH)) {
			tempDate = DateUtil.getLastMonth(date);
			year = DateUtil.getYear(tempDate);
			month = DateUtil.getMonth(tempDate);
			day = DateUtil.getDay(tempDate);
			ParaDate = year + "," + month;
		}
		// Ƶ�ȣ���
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
		// Ƶ�ȣ�����
		else if (freq.equals(Config.FREQ_HALFYEAR)) {
			tempDate = DateUtil.getLastHalfYear(date);
			year = DateUtil.getYear(tempDate);
			month = DateUtil.getMonth(tempDate);
			if (month.equals("06"))
				ParaDate = year + ",1";
			if (month.equals("12"))
				ParaDate = year + ",2";
		}
		// Ƶ�ȣ���
		else if (freq.equals(Config.FREQ_YEAR)) {
			tempDate = DateUtil.getLastYear(date);
			ParaDate = DateUtil.getYear(tempDate);
		} else
			ParaDate = null;
		return ParaDate;
	}

	/**
	 * ����λ���·�ת��һλ�����硰01��תΪ��1��
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
	 * title:�÷������ڸ��������ʱ���жϸ�ʱ���Ƿ���ָ����ʱ����б� author:chenbing date:2008-3-1
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
	 * title:�÷������ڸ���Ƶ�Ⱥ�ָ�������ڻ������ͬ���ۼ����� author:chenbing date:2008-3-6
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
	 * title:�÷������ڸ�����������ڷ��������ۼ����� author:chenbing date:2008-3-6
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
	 * title:�÷������ڸ�����������ڷ����ϼ��ۼ����� author:chenbing date:2008-3-6
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
	 * title:�÷������ڸ�����������ڷ����ۼƼ����� author:chenbing date:2008-3-6
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
	 * title:�÷������ڸ�����������ڷ����ۼ������� author:chenbing date:2008-3-6
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
	 * title:�÷������ڸ�����������ڷ��ؼ����� author:chenbing date:2008-3-6
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
	 * ����Ƶ��ȷ�������ʱ���Ƿ���Ҫ����ָ���Ĳ���
	 * 
	 * @return
	 */
	public static boolean isNeedBuild(String date, int buildFreq) {
		/* �� */
		if (buildFreq == DateUtil.FREQ_DAY)
			return true;
		/* Ѯ */
		else if (buildFreq == DateUtil.FREQ_TENDAY)
			return DateUtil.isTenDay(date);
		/* �� */
		else if (buildFreq == DateUtil.FREQ_MONTH)
			return DateUtil.isMonthDay(date);
		/* �� */
		else if (buildFreq == DateUtil.FREQ_SEASON)
			return DateUtil.isSeasonDay(date);
		/* ���� */
		else if (buildFreq == DateUtil.FREQ_HALFYEAR)
			return DateUtil.isHalfYearDay(date);
		/* �� */
		else if (buildFreq == DateUtil.FREQ_YEAR)
			return DateUtil.isYearDay(date);

		return false;
	}

	/**
	 * 
	 * title:�÷������ڸ�����������ڷ���Ƶ���б� author:chenbing date:2008-4-1
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
	 * title:�÷������ڸ�����������ڷ����ö��ŷָ��Ƶ���ַ��� author:chenbing date:2008-4-1
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
	 * title:�÷������ڽ�ָ���Ķ������ͽ�����ָ���Ľ��޷��ͷָ����������е��ַ����ӹ���ָ�����ַ��� author:chenbing
	 * author:chenbing date:2008-2-26
	 * 
	 * @param obj
	 *            ָ���Ķ���
	 * @param splitStr
	 *            �ָ���
	 * @param bracketStr
	 *            ���޷� ���Ϊ����û�н��޷�
	 * @return
	 */
	public static String getStrsBySplitStr(Object obj, String splitStr,
			String bracketStr) {

		if (obj == null)
			return null;

		bracketStr = bracketStr == null ? "" : bracketStr;

		String result = null;

		StringBuffer sb = new StringBuffer("");

		if (obj instanceof ArrayList) { // �ַ����б�

			List list = (List) obj;

			for (int i = 0; i < list.size(); i++)

				sb.append(splitStr + bracketStr + ((String) list.get(i)).trim()
						+ bracketStr);

		} else if (obj instanceof String) {// �ַ���

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
	 * title:�÷������ڸ�����������ں�Ƶ�ȷ������һ�ε��������� author:chenbing date:2008-4-11
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
     * title:�÷������ڼ����ʼ�����Ƿ������ֹ����
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
	
	// ���ڱȴ�С����һ���������ڵڶ����������򷵻�1 ,���򷵻�0
	// �����ͬ ������2
	public static int compareDate(Date date1, Date date2) {
		long d1 = date2long(date1);
		long d2 = date2long(date2);
		if (d1 > d2) {
			return 1; // ��һ���������ڵڶ�������
		} else if (d1 < d2) {
			return 0; // �ڶ����������ڵ�һ������
		} else {
			return 2; // ���
		}
	}
	
	// ����ת����
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
	 * @function ���쳣getDateByString������������ͬ
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
	 * ����Ƶ��ȡ�ø����ڵ��ڵ�����
	 * 
	 * @param date
	 *            ����
	 * @param freq
	 *            Ƶ��
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
			return DateUtil.getMonthLast(date);//�챨���±�һ��
	}
	
	/**
	 * ���Ѯ������
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
	 * ����µ�����
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
	 *  ������������ yyyy-MM-dd
	 */
	public static String getYestoday() {

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");   
		
		String date = formatter.format(calendar.getTime());
		
		return date;
	}
	
	/**
	 *  ��õ�������� yyyy-MM-dd
	 */
	public static String getToday() {

		Calendar calendar = Calendar.getInstance();
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");   
		
		String date = formatter.format(calendar.getTime());
		
		return date;
	}
	
	/**
	 * ���ݴ������ڻ�������³�����
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public static String getNextMonthDate(String date) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
		// ��ʽ������ʱ��
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
	 * ������ֹʱ�䣬�����м侭����ʱ��㣨�£�
	 * 
	 * @param startdate
	 *            ��ʼʱ��
	 * @param enddate
	 *            ����ʱ��
	 * 
	 * @return ʱ�������
	 * 
	 * @throws Exception
	 */
	public static String[] getTerms(String startdate, String enddate)
			throws Exception {
		List list = new ArrayList();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		// ��ʽ����ʼʱ��
		Calendar calStart = Calendar.getInstance();
		Date dateStart = sdf.parse(startdate);
		calStart.setTime(dateStart);
		
		// ��ʽ������ʱ��
		Calendar calEnd = Calendar.getInstance();
		Date dateEnd = sdf.parse(enddate);
		calEnd.setTime(dateEnd);
		
		// ��������������3���µ�ֵ�������Ż�ʹ���һ��ʱ���������ʱ����
		//calEnd.add(Calendar.MONTH, 3);

		int startMonth = calStart.get(Calendar.MONTH) + 1;// JAVA���·ݴ�0��ʼ������Ҫ��1
//		int season = 0;// ����ֵ
//		if (startMonth % 3 == 0) {// ���û��������������Ϊ3��6��9����12�·�
//			season = startMonth / 3;
//		} else {// �������������ȡ����ļ���ֵ
//			season = startMonth / 3 + 1;
//		}
		// ����������ȷ����ʼʱ��
//		calStart.set(calStart.get(Calendar.YEAR), season * 3 - 1, 1);
		
		calStart.set(calStart.get(Calendar.YEAR), startMonth -1 , 1);
		calEnd.set(calEnd.get(Calendar.YEAR), calEnd.get(Calendar.MONTH) + 1 , 1);

		
		int year = 0;
		int month = 0;
		
		while (calStart.before(calEnd)) {
			year = calStart.get(Calendar.YEAR);
			month = calStart.get(Calendar.MONTH) + 1;// JAVA���·ݴ�0��ʼ������Ҫ��1
			if (String.valueOf(month).length() == 1) {
				list.add(year + "-0" + month);// 1-9�µ�ʱ���·�ǰ�油��
			} else {
				list.add(year + "-" + month);
			}
//			calStart.add(Calendar.MONTH, 3);// ����һ������
			calStart.add(Calendar.MONTH, 1);

		}

		// ���б�ת��Ϊ����
		String[] date = (String[]) list.toArray(new String[list.size()]);

		return date;
	}
	/**
	 * ���������ָ�����ڷ��ظ����������ܼ�,��1�ͷ���1������ͷ���5���Դ����ƣ�ע�����շ���0
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
