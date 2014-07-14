package com.fitech.gznx.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.fitech.gznx.common.Config;
import com.fitech.gznx.common.DateUtil;
import com.cbrc.smis.dao.DBConn;

/**
 * ���÷�����
 * 
 * @author jack
 * @date 2009-12-16
 * 
 */
public class FitechUtil
{
	/**
	 * ��׼������ʽ
	 */
	public static final String NORMALDATE = "yyyy-MM-dd";

	/**
	 * �й���׼��������ʽ
	 */
	public static final String CHINESEDATE = "yyyy��MM��dd��";

	/**
	 * ����
	 */
	private static final String[][] WEEK =
	{
	{ "1", "����һ" },
	{ "2", "���ڶ�" },
	{ "3", "������" },
	{ "4", "������" },
	{ "5", "������" },
	{ "6", "������" },
	{ "7", "������" } };

	/**
	 * getParameter����<br>
	 * �Ի�ȡ���ύֵ��GB2312����ת��<br>
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param paramName
	 *            String ������
	 * @return String
	 */
	public static String getParameter(HttpServletRequest request, String paramName)
	{
		if (paramName == null)
			return "";
		if (request.getParameter(paramName) == null)
			return "";
		try
		{
			// return request.getParameter(paramName);
			return new String(((String) request.getParameter(paramName)).getBytes("ISO8859-1"), "GB2312");
		}
		catch (Exception e)
		{
			return "";
		}
	}

	/**
	 * ��request��session ��Χ�ڵ�����ɾ��
	 * 
	 * @param mapping
	 * @param request
	 */

	public static void removeAttribute(ActionMapping mapping, HttpServletRequest request)
	{
		HttpSession session = request.getSession();
		if (mapping.getAttribute() != null)
		{
			if ("request".equals(mapping.getScope()))
				request.removeAttribute(mapping.getAttribute());
			else
				session.removeAttribute(mapping.getAttribute());
		}
	}

	/**
	 * getAttribute����<br>
	 * �Ի�ȡ���ύֵ��GB2312����ת��<br>
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param attributeName
	 *            String ������
	 * @return String
	 */
	public static String getAttribute(HttpServletRequest request, String attributeName)
	{
		if (attributeName == null)
			return "";
		if (request.getAttribute(attributeName) == null)
			return "";
		try
		{
			return new String(((String) request.getAttribute(attributeName)).getBytes("ISO8859-1"), "GB2312");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * �������ִ���ʽ����ʱ�䴮
	 * 
	 * @param date
	 *            String ʱ���ַ���
	 * @return Date ת���ɹ�����Date �����򷵻�null
	 */
	public static Date formatToTimestamp(String date)
	{
		if (date != null && !date.equals(""))
		{
			SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");
			try
			{
				return FORMAT.parse(date);
			}
			catch (ParseException e)
			{
				e.printStackTrace();
				return null;
			}
		}

		return null;
	}

	/**
	 * ����Դ�ļ��У�����������ȡ��ֵ
	 * 
	 * @param resourcesFile
	 *            String ��Դ�ļ�
	 * @param key
	 *            String ����
	 * @return String ����ֵ
	 */
	public static String getValueFromResources(String resourcesFile, String key)
	{
		if (resourcesFile == null || key == null)
			return null;

		MessageResources resources = MessageResources.getMessageResources(resourcesFile);

		String value = resources.getMessage(Locale.CHINA, key);

		return value == null ? null : value.trim();
	}

	/**
	 * ��ȡ���������
	 * 
	 * @param format
	 *            ������ʽ
	 * @return String
	 */
	public static String getToday(String format)
	{
		if (format == null)
			return "";
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(new Date());
	}

	/**
	 * ��ȡ�������ܼ�
	 * 
	 * @return String
	 */
	public static String getDay()
	{
		String _day = "";
		Date date = new Date();
		int day = date.getDay();
		for (int i = 0; i < WEEK.length; i++)
		{
			if (WEEK[i][0].equals(String.valueOf(day)) == true)
			{
				_day = WEEK[i][1];
				break;
			}
		}
		return _day;
	}


	/**
	 * ����Ƶ�Ȼ�����ݿ������µ���������
	 * 
	 * @param freq
	 * @return
	 */
	public static String getLatestDataDate(Integer freq) throws Exception
	{
		String date =  DateUtil.getYestoday();
		return date;
	}



	/**
	 * 
	 * ����˵��:�÷������ڸ�������Ĳ�ѯSQL��JDBC��ѯ����������Ľ�����ͷ�����Ӧ�Ľ�� 3:Integer 4:String
	 * @author jack
	 * @date 2009-12-15
	 * @param sql
	 *            ��ѯSQL�ַ��� type:String
	 * @return
	 */
	public static List jdbcQueryUtil(String sql)
	{
		System.out.println(sql);
		List result = new ArrayList();
		
		DBConn conn = null;
		ResultSet  rs = null;
		Connection connection=null;
		Session session = null;
		try{
			conn = new DBConn();
			session = conn.beginTransaction();
			connection = session.connection();

			rs = connection.createStatement().executeQuery(sql);
			
			while(rs.next()){
				result.add(new Double(rs.getDouble(1)));
				break;
			}
			return result;
		} catch(Exception ex){
			ex.printStackTrace();
			return null;
		} finally{
			
			if(conn != null) 
				conn.closeSession();
		}
	}
	/**
	 * @author yql 1.15
	 * @param sql
	 * @param i
	 * @return
	 */
	public static List jdbcQueryUtil(String sql,int i)
	{
		System.out.println(sql);
		List result = new ArrayList();
		
		DBConn conn = null;
		ResultSet  rs = null;
		Connection connection=null;
		Session session = null;
		try{
			conn = new DBConn();
			session = conn.beginTransaction();
			connection = session.connection();

			rs = connection.createStatement().executeQuery(sql);
			
			while(rs.next()){
				result.add(new Double(rs.getDouble(1)));
				//break;
			}
			return result;
		} catch(Exception ex){
			ex.printStackTrace();
			return null;
		} finally{
			
			if(conn != null) 
				conn.closeSession();
		}
	}

	/**
	 * 
	 * ����˵��:�÷�������һ��������֮ͬ�����ڷ��ص���һ��Map
	 * 
	 * @author jack
	 * @date 2009-12-15
	 * @param sql
	 * @return
	 */
	public static Map jdbcQueryUtilOfMap(String sql)
	{

		Map result = new TreeMap();
		DBConn conn = null;
		ResultSet  rs = null;
		Connection connection=null;
		Session session = null;
		try{
			conn = new DBConn();
			session = conn.beginTransaction();
			connection = session.connection();
			
			rs = connection.createStatement().executeQuery(sql);

			while (rs.next())
			{	
				if(rs.getString(1).contains("-")){
				 String sub = rs.getString(1).substring(0,10);
				 result.put(sub, rs.getString(2));
				}else{
					result.put(rs.getString(1), rs.getString(2));					
				}
				
			}

			return result;

		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		} finally{

			if(conn != null) 
				conn.closeSession();
		}

	}


	/**
	 * �����û�ѡ�������ѡ��ؾ��������
	 * @author jack
	 * @param datePara ����
	 * @return date
	 */
	public static String getDate(String datePara,int freq){
		String date = null;
		
		if(datePara==null || datePara.equals("")){
			return null;
		}
		else{
			String [] para = datePara.split(",");
			
			//��õ���Ƶ�Ȼ���Ƶ�ȵ�����
			if(freq==Config.FREQ_DAY.intValue()||freq==Config.FREQ_TIME.intValue()){
				date = para[0];
			}
			
			//���ѮƵ�ȵ�����
			if(freq==Config.FREQ_TENDAY.intValue()){
				int year = new Integer (para[0]).intValue();
				String[] temp = para[1].split("_");
				String tendayFlag = temp[1];
				int month = new Integer (temp[0]).intValue();
				//��Ѯ����
				if(tendayFlag.equals("1")){
					date = DateUtil.getDateStr(year,month,10);
				}
				//��Ѯ����
				if(tendayFlag.equals("2")){
					date = DateUtil.getDateStr(year,month,20);
				}
				//��Ѯ����
				if(tendayFlag.equals("3")){
					date = DateUtil.getMonthLastDayStr(year,month);
				}
			}
			
			//�����Ƶ�ȵ�����
			if(freq==Config.FREQ_MONTH.intValue()){
				int year = new Integer (para[0]).intValue();
				date = DateUtil.getMonthLastDayStr(year,new Integer (para[1]).intValue());
			}
			
			//��ü�Ƶ�ȵ�����
			if(freq==Config.FREQ_SEASON.intValue()){
				int year = new Integer (para[0]).intValue();
				String season = para[1];
				//��һ����
				if(season.equals("1"))
					date = DateUtil.getMonthLastDayStr(year,3);
				//�ڶ�����
				if(season.equals("2"))
					date = DateUtil.getMonthLastDayStr(year,6);
				//��������
				if(season.equals("3"))
					date = DateUtil.getMonthLastDayStr(year,9);
				//���ļ���
				if(season.equals("4"))
					date = DateUtil.getMonthLastDayStr(year,12);
			}
			
			//��ð���Ƶ�ȵ�����
			if(freq==Config.FREQ_HALFYEAR.intValue()){
				int year = new Integer (para[0]).intValue();
				//�ϰ���
				if(new Integer(para[1]).intValue()==1)
					date = DateUtil.getMonthLastDayStr(year,6);
				//�°���
				if(new Integer(para[1]).intValue()==2)
					date = DateUtil.getMonthLastDayStr(year,12);
			}
			
			//�����Ƶ�ȵ�����
			if(freq==Config.FREQ_YEAR.intValue()){
				int year = new Integer (para[0]).intValue();
				date = DateUtil.getMonthLastDayStr(year,12);
			}
		}
		return date;
	}
	

}
