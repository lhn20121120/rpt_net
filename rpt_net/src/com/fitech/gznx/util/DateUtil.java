package com.fitech.gznx.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import com.fitech.gznx.common.Config;

/**
 * ���ڹ�����
 * 
 * @author jack
 * 
 */
public class DateUtil
{
	/**
	 * ���ڴ���·�����Ӧ��ֵ
	 */
	public static final int[] MONTH_ARRAY =
	{ 0, Calendar.JANUARY, Calendar.FEBRUARY, Calendar.MARCH, Calendar.APRIL, Calendar.MAY, Calendar.JUNE,
			Calendar.JULY, Calendar.AUGUST, Calendar.SEPTEMBER, Calendar.OCTOBER, Calendar.NOVEMBER,
			Calendar.DECEMBER };

	/**
	 * ���ڸ�ʽ������
	 */
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * ʱ��ָ��
	 */
	public static final String DATE_SPLIT = "-";

	/**
	 * һ��ĺ���ʱ��
	 */
	public static final long ONE_DAY_MILLIS = 24 * 60 * 60 * 1000;

	/**
	 * �жϸ������Ƿ���Ѯ������
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isTenDay(String date)
	{
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
	public static boolean isMonthDay(String date)
	{
		if (date == null || date.equals(""))
			return false;
		String[] dateSplit = date.split(DateUtil.DATE_SPLIT);
		int day = new Integer(dateSplit[2]).intValue();
		return day == DateUtil.getCalendar(date).getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * �жϸ������Ƿ��Ǽ�ĩ����
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isSeasonDay(String date)
	{
		if (date == null || date.equals(""))
			return false;
		return date.endsWith("03-31") || date.endsWith("06-30") || date.endsWith("09-30")
				|| date.endsWith("12-31");
	}

	/**
	 * �ж��Ƿ��ǰ��������
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isHalfYearDay(String date)
	{
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
	public static boolean isYearDay(String date)
	{
		if (date == null || date.equals(""))
			return false;
		return date.endsWith("12-31");
	}

	/**
	 * ��������·ݻ�ø��µ����һ�������
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static int getMonthLastDay(int year, int month)
	{
		int lastDay = 31;
		if (month == 4 || month == 6 || month == 9 || month == 11)
			lastDay = 30;
		if (month == 2)
		{
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
	public static String getMonthLastDayStr(int year, int month)
	{
		int lastDay = 31;
		if (month == 4 || month == 6 || month == 9 || month == 11)
			lastDay = 30;
		if (month == 2)
		{
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
	public static String getLastDay(String date)
	{
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
	public static String getLastTenDay(String date)
	{
		if (date == null || date.equals(""))
			return null;
		String[] dateSplit = date.split(DateUtil.DATE_SPLIT);
		int year = new Integer(dateSplit[0]).intValue();
		int month = new Integer(dateSplit[1]).intValue();
		int day = new Integer(dateSplit[2]).intValue();
		if (day >= 1 && day <= 10)
		{
			if (month == 1)
			{
				month = 12;
				year = year - 1;
			}
			else
			{
				month = month - 1;
			}
			return DateUtil.getMonthLastDayStr(year, month);
		}
		else if (day > 10 && day <= 20)
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
	public static String getLastMonth(String date)
	{
		if (date == null || date.equals(""))
			return null;
		String[] dateSplit = date.split(DateUtil.DATE_SPLIT);
		int year = new Integer(dateSplit[0]).intValue();
		int month = new Integer(dateSplit[1]).intValue();
		if (month == 1)
		{
			month = 12;
			year = year - 1;
		}
		else
		{
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
	public static String getLastSeason(String date)
	{
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
	public static String getLastHalfYear(String date)
	{
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
	public static String getLastYear(String date)
	{
		if (date == null || date.equals(""))
			return null;
		String[] dateSplit = date.split(DateUtil.DATE_SPLIT);
		int year = new Integer(dateSplit[0]).intValue();
		return DateUtil.getDateStr(year - 1, 12, 31);

	}

	/**
	 * ���ȥ��ͬ������
	 * 
	 * @param date
	 * @return
	 */
	public static String getLYSameDay(String date)
	{
		if (date == null || date.equals(""))
			return null;
		String[] dateSplit = date.split(DateUtil.DATE_SPLIT);
		int year = new Integer(dateSplit[0]).intValue();
		int month = new Integer(dateSplit[1]).intValue();
		int day = new Integer(dateSplit[2]).intValue();
		return DateUtil.getDateStr(year - 1, month, day);
	}

	/**
	 * ���Ѯ������
	 * 
	 * @param date
	 * @return
	 */
	public static String getStartTenday(String date)
	{
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
	public static String getStartMonth(String date)
	{
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
	public static String getStartSeason(String date)
	{
		if (date == null || date.equals(""))
			return null;
		String[] dateSplit = date.split(DateUtil.DATE_SPLIT);
		int year = new Integer(dateSplit[0]).intValue();
		int month = new Integer(dateSplit[1]).intValue();

		if (month <= 3)
			return DateUtil.getDateStr(year, 1, 1);
		else if (month <= 6)
			return DateUtil.getDateStr(year, 3, 1);
		else if (month <= 9)
			return DateUtil.getDateStr(year, 6, 1);
		else
			return DateUtil.getDateStr(year, 9, 1);
	}

	/**
	 * ��ð��������
	 * 
	 * @param date
	 * @return
	 */
	public static String getStartHalfYear(String date)
	{
		if (date == null || date.equals(""))
			return null;
		String[] dateSplit = date.split(DateUtil.DATE_SPLIT);
		int year = new Integer(dateSplit[0]).intValue();
		int month = new Integer(dateSplit[1]).intValue();

		if (month <= 6)
			return DateUtil.getDateStr(year, 1, 1);
		else
			return DateUtil.getDateStr(year, 6, 1);
	}

	/**
	 * ����������
	 * 
	 * @param date
	 * @return
	 */
	public static String getStartYear(String date)
	{
		if (date == null || date.equals(""))
			return null;
		String[] dateSplit = date.split(DateUtil.DATE_SPLIT);
		int year = new Integer(dateSplit[0]).intValue();
		return DateUtil.getDateStr(year, 1, 1);
	}

	/**
	 * ȡ�����ڵ�yyyy-MM-dd��ʽ
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public static String getDateStr(int year, int month, int day)
	{
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
	public static GregorianCalendar getCalendar(String date)
	{
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
	public static int getDays(String startDate, String endDate) throws Exception
	{
		if (startDate == null || startDate.equals("") || endDate == null || endDate.equals(""))
			throw new NullPointerException("��ʼʱ�����ֹʱ��Ϊ��!");

		/* ��ʼʱ��Calendar */
		Calendar startDateCalendar = DateUtil.getCalendar(startDate);
		/* ��ֹʱ��Calendar */
		Calendar endDateCalendar = DateUtil.getCalendar(endDate);

		long days = (endDateCalendar.getTimeInMillis() - startDateCalendar.getTimeInMillis())
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
	public static List getBetweenDate(String startDate, String endDate)
	{
		if (startDate.equals(endDate))
			return null;
		List dateList = new ArrayList();
		GregorianCalendar startCalendar = getCalendar(startDate);
		GregorianCalendar endCalendar = getCalendar(endDate);
		if (startCalendar.after(endCalendar))
			return null;

		endCalendar.add(GregorianCalendar.DATE, -1);

		String lastDayMonthLastDay = getLastMonth((DATE_FORMAT.format(endCalendar.getTime())));

		while (!lastDayMonthLastDay.equals(startDate))
		{
			// System.out.println("**"+lastDayMonthLastDay);
			if (!dateList.contains(lastDayMonthLastDay))
				dateList.add(lastDayMonthLastDay);
		    endCalendar.add(GregorianCalendar.DATE, -1);
			lastDayMonthLastDay = getLastMonth((DATE_FORMAT.format(endCalendar.getTime())));
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
	public static List getDateList(String startDate, String endDate)
	{
		if (startDate == null || startDate.equals("") || endDate == null || endDate.equals(""))
			return null;

		List dateList = new ArrayList();
		if (startDate.equals(endDate))
		{
			dateList.add(startDate);
			return dateList;
		}

		GregorianCalendar startCalendar = getCalendar(startDate);
		GregorianCalendar endCalendar = getCalendar(endDate);

		if (startCalendar.after(endCalendar))
			return null;
		String newDateStr = DATE_FORMAT.format(startCalendar.getTime());

		while (!newDateStr.equals(endDate))
		{
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
	public static String parseToChineseDate(String dateStr)
	{
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
	public static String getYear(String dateStr)
	{
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
	public static String getMonth(String dateStr)
	{
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
	public static String getDay(String dateStr)
	{
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
	public static List getdateList(String startDate, String endDate, int cirType, int interval)
			throws Exception
	{

		List dateList = new ArrayList();

		if (getDays(startDate, endDate) == 0 || cirType == 0)
		{

			dateList.add(startDate);

			return dateList;

		}

		dateList.add(startDate);

		GregorianCalendar startCalendar = getCalendar(startDate);

		GregorianCalendar endCalendar = getCalendar(endDate);

		if (cirType == 1)
		{

			/** ����ѭ�� */

			startCalendar.add(GregorianCalendar.DAY_OF_MONTH, interval+1);

			while (!startCalendar.after(endCalendar))
			{

				String tempdate = getdate(startCalendar);

				dateList.add(tempdate);

				startCalendar.add(GregorianCalendar.DAY_OF_MONTH, interval+1);

			}
			return dateList;
		}

		else if (cirType == 2)
		{

			/** ����ѭ�� */

			startCalendar = getWeekDay(startCalendar, interval);

			while (!startCalendar.after(endCalendar))
			{

				String tempdate = getdate(startCalendar);

				dateList.add(tempdate);

				startCalendar.add(GregorianCalendar.DAY_OF_WEEK, 7);

			}

			return dateList;

		}

		else if (cirType == 3)
		{

			/** ����ѭ�� */

			String day = String.valueOf(interval);

			if (day.length() == 1)

				day = "0" + day;

			List dayList = getDateList(startDate, endDate);

			dayList.remove(0);

			if (dayList != null && dayList.size() != 0)
			{

				for (int i = 0; i < dayList.size(); i++)
				{

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
	public static String getdate(GregorianCalendar gregorianCalendar)
	{

		String date = "";

		int year = gregorianCalendar.get(GregorianCalendar.YEAR);

		String y = String.valueOf(year);

		int month = gregorianCalendar.get(GregorianCalendar.MONTH) + 1;

		String m = String.valueOf(month);

		if (m.length() == 1)
		{

			m = "0" + m;

		}

		int day = gregorianCalendar.get(GregorianCalendar.DAY_OF_MONTH);

		String d = String.valueOf(day);

		if (d.length() == 1)
		{

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
	public static GregorianCalendar getWeekDay(GregorianCalendar gregorianCalendar, int weekday)
	{

		int curweekday = gregorianCalendar.get(GregorianCalendar.DAY_OF_WEEK);

		if (curweekday < weekday)
		{

			gregorianCalendar.add(GregorianCalendar.DAY_OF_WEEK, weekday - curweekday + 1);

		}

		else if (curweekday > weekday)
		{

			gregorianCalendar.add(GregorianCalendar.DAY_OF_WEEK, weekday + 7 - curweekday + 1);

		}

		return gregorianCalendar;

	}

	/**
	 * ���ؽ��������
	 */
	public static String getTodayDate()
	{

		Calendar calendar = Calendar.getInstance();

		return (DATE_FORMAT.format(calendar.getTime()));
	}

	/**
	 * ��ñ�������
	 * 
	 */
	public static int DaysOfCurMonth(int curYear, int curMonth) throws Exception
	{

		int resultDays = 30;
		/* ȡ�õ�����ݺ��·� */
		if (curMonth == 1 || curMonth == 3 || curMonth == 5 || curMonth == 7 || curMonth == 8

		|| curMonth == 10 || curMonth == 12)

			resultDays = 31;

		if (curMonth == 2)
		{

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
	public static int getfirtDayOfMonth(int year, int month) throws Exception
	{

		Calendar calendar = Calendar.getInstance();

		int curMonth = calendar.get(Calendar.MONTH);

		int curYear = calendar.get(Calendar.YEAR);

		calendar.add(Calendar.DAY_OF_MONTH, 1 - calendar.get(Calendar.DAY_OF_MONTH));

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
	public static List getDateList(String startDate, String endDate, Integer freq)
	{
		if (startDate == null || startDate.equals("") || endDate == null || endDate.equals(""))
			return null;

		List dateList = new ArrayList();
		if (startDate.equals(endDate))
		{
			dateList.add(startDate);
			return dateList;
		}

		GregorianCalendar startCalendar = getCalendar(startDate);
		GregorianCalendar endCalendar = getCalendar(endDate);

		if (startCalendar.after(endCalendar))
			return null;
		String newDateStr = DATE_FORMAT.format(startCalendar.getTime());

		while (startCalendar.before(endCalendar) || startCalendar.equals(endCalendar))
		{
			// ѮƵ��
			if (freq.equals(Config.FREQ_TENDAY) && DateUtil.isTenDay(newDateStr))
			{
				dateList.add(newDateStr);
				startCalendar.add(GregorianCalendar.DATE, 15);
				newDateStr = DATE_FORMAT.format(startCalendar.getTime());
				continue;
			}
			/* ��Ƶ�� */
			else if (freq.equals(Config.FREQ_MONTH) && DateUtil.isMonthDay(newDateStr))
			{
				dateList.add(newDateStr);
				startCalendar.add(GregorianCalendar.MONTH, 1);
				newDateStr = DATE_FORMAT.format(startCalendar.getTime());
				continue;
			}
			/* ��Ƶ�� */
			else if (freq.equals(Config.FREQ_SEASON) && DateUtil.isSeasonDay(newDateStr))
			{
				dateList.add(newDateStr);
				startCalendar.add(GregorianCalendar.MONTH, 3);
				newDateStr = DATE_FORMAT.format(startCalendar.getTime());
				continue;
			}
			/* ����Ƶ�� */
			else if (freq.equals(Config.FREQ_HALFYEAR) && DateUtil.isHalfYearDay(newDateStr))
			{
				dateList.add(newDateStr);
				startCalendar.add(GregorianCalendar.MONTH, 6);
				newDateStr = DATE_FORMAT.format(startCalendar.getTime());
				continue;
			}
			/* ��Ƶ�� */
			else if (freq.equals(Config.FREQ_YEAR) && DateUtil.isYearDay(newDateStr))
			{
				dateList.add(newDateStr);
				startCalendar.add(GregorianCalendar.YEAR, 1);
				newDateStr = DATE_FORMAT.format(startCalendar.getTime());
				continue;
			}
			else
			{
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
	public static String dateAdd(String date, int day)
	{
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
	public static String getLastFreqDate(String date, Integer freq)
	{
		if (freq.equals(Config.FREQ_DAY))
			return DateUtil.getLastDay(date);
		else if (freq.equals(Config.FREQ_TENDAY))
			return DateUtil.getLastTenDay(date);
		else if (freq.equals(Config.FREQ_MONTH))
			return DateUtil.getLastMonth(date);
		else if (freq.equals(Config.FREQ_SEASON))
			return DateUtil.getLastSeason(date);
		else if (freq.equals(Config.FREQ_HALFYEAR))
			return DateUtil.getLastHalfYear(date);
		else if (freq.equals(Config.FREQ_YEAR))
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
	public static String getStartFreqDate(String date, Integer freq)
	{
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
	
	public static void main(String args[])
	{
		/*
		 * GregorianCalendar startCalendar = getCalendar("2006-12-31");
		 * GregorianCalendar endCalendar = getCalendar("2006-12-31");
		 * 
		 * System.out.println(startCalendar.equals(endCalendar));
		 * 
		 * List list = DateUtil.getDateList("2006-01-05", "2009-02-28",
		 * Config.FREQ_MONTH); for (int i = 0; i < list.size(); i++) {
		 * System.out.println((String) list.get(i)); }
		 */
		
		System.out.println(DateUtil.getLastFreqPara(new Integer(2)));
	}
	
	/**
	 * ����Ƶ��ȡ�ø����ڵ����ڲ���,������ָ�겹¼
	 * 
	 * @param freq
	 *            Ƶ��
	 * @return
	 */
	public static String getLastFreqPara(Integer freq)
	{
		if(freq==null)
			return null;
		
		Calendar calendar = Calendar.getInstance();
		String date = DATE_FORMAT.format(calendar.getTime());
		String tempDate;
		String ParaDate = null;
		String year;
		String month;
		String day;
		//Ƶ�ȣ���
		if(freq.equals(Config.FREQ_TIME))
			ParaDate = date;
//		Ƶ�ȣ���
		else if (freq.equals(Config.FREQ_DAY))
			ParaDate = DateUtil.getLastDay(date);
//		Ƶ�ȣ�Ѯ
		else if (freq.equals(Config.FREQ_TENDAY)){
			tempDate = DateUtil.getLastTenDay(date);
			year = DateUtil.getYear(tempDate);
			month = getMonthStr(DateUtil.getMonth(tempDate));
			day = DateUtil.getDay(tempDate);
			if(day.equals("10"))
				ParaDate = year+","+month+"_1";
			else if(day.equals("20"))
				ParaDate = year+","+month+"_2";
			else
				ParaDate = year+","+month+"_3";
		}
//		Ƶ�ȣ���
		else if (freq.equals(Config.FREQ_MONTH)){
			tempDate = DateUtil.getLastMonth(date);
			year = DateUtil.getYear(tempDate);
			month = DateUtil.getMonth(tempDate);
			day = DateUtil.getDay(tempDate);
			ParaDate = year+","+month;
		}
//		Ƶ�ȣ���
		else if (freq.equals(Config.FREQ_SEASON)){
			tempDate = DateUtil.getLastSeason(date);
			year = DateUtil.getYear(tempDate);
			month = DateUtil.getMonth(tempDate);
			if(month.equals("03"))
				ParaDate = year+",1";
			if(month.equals("06"))
				ParaDate = year+",2";
			if(month.equals("09"))
				ParaDate = year+",3";
			if(month.equals("12"))
				ParaDate = year+",4";
		}
//		Ƶ�ȣ�����
		else if (freq.equals(Config.FREQ_HALFYEAR)){
			tempDate = DateUtil.getLastHalfYear(date);
			year = DateUtil.getYear(tempDate);
			month = DateUtil.getMonth(tempDate);
			if(month.equals("06"))
				ParaDate = year+",1";
			if(month.equals("12"))
				ParaDate = year+",2";
		}
//		Ƶ�ȣ���
		else if (freq.equals(Config.FREQ_YEAR)){
			tempDate = DateUtil.getLastYear(date);
			ParaDate = DateUtil.getYear(tempDate);
		}
		else
			ParaDate = null;
			return ParaDate;
	}
	/**
	 * ����λ���·�ת��һλ�����硰01��תΪ��1��
	 * 
	 */
	public static String getMonthStr(String date)
	{
		if(date==null||date.equals("")||date.length()!=2)
			return null;
		
		if(date.equals("10")||date.equals("11")||date.equals("12"))
			return date;
		
		else{
			return date.substring(1,2);
		}
	}
	
}
