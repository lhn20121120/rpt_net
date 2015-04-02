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
 * 常用方法类
 * 
 * @author jack
 * @date 2009-12-16
 * 
 */
public class FitechUtil
{
	/**
	 * 标准日期样式
	 */
	public static final String NORMALDATE = "yyyy-MM-dd";

	/**
	 * 中国标准的日期样式
	 */
	public static final String CHINESEDATE = "yyyy年MM月dd日";

	/**
	 * 星期
	 */
	private static final String[][] WEEK =
	{
	{ "1", "星期一" },
	{ "2", "星期二" },
	{ "3", "星期三" },
	{ "4", "星期四" },
	{ "5", "星期五" },
	{ "6", "星期六" },
	{ "7", "星期日" } };

	/**
	 * getParameter方法<br>
	 * 对获取的提交值做GB2312编码转换<br>
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param paramName
	 *            String 变量名
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
	 * 把request和session 范围内的属性删除
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
	 * getAttribute方法<br>
	 * 对获取的提交值做GB2312编码转换<br>
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param attributeName
	 *            String 变量名
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
	 * 将日期字串格式化成时间串
	 * 
	 * @param date
	 *            String 时间字符串
	 * @return Date 转化成功返回Date ，否则返回null
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
	 * 从资源文件中，根据主键获取其值
	 * 
	 * @param resourcesFile
	 *            String 资源文件
	 * @param key
	 *            String 主键
	 * @return String 健的值
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
	 * 获取当天的日期
	 * 
	 * @param format
	 *            日期样式
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
	 * 获取当天是周几
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
	 * 根据频度获得数据库中最新的数据日期
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
	 * 方法说明:该方法用于根据输入的查询SQL用JDBC查询并根据输入的结果类型返回相应的结果 3:Integer 4:String
	 * @author jack
	 * @date 2009-12-15
	 * @param sql
	 *            查询SQL字符串 type:String
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
	 * 方法说明:该方法与上一个方法不同之处在于返回的是一个Map
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
	 * 根据用户选择的日期选项返回具体的日期
	 * @author jack
	 * @param datePara 参数
	 * @return date
	 */
	public static String getDate(String datePara,int freq){
		String date = null;
		
		if(datePara==null || datePara.equals("")){
			return null;
		}
		else{
			String [] para = datePara.split(",");
			
			//获得单次频度或日频度的日期
			if(freq==Config.FREQ_DAY.intValue()||freq==Config.FREQ_TIME.intValue()){
				date = para[0];
			}
			
			//获得旬频度的日期
			if(freq==Config.FREQ_TENDAY.intValue()){
				int year = new Integer (para[0]).intValue();
				String[] temp = para[1].split("_");
				String tendayFlag = temp[1];
				int month = new Integer (temp[0]).intValue();
				//上旬日期
				if(tendayFlag.equals("1")){
					date = DateUtil.getDateStr(year,month,10);
				}
				//中旬日期
				if(tendayFlag.equals("2")){
					date = DateUtil.getDateStr(year,month,20);
				}
				//下旬日期
				if(tendayFlag.equals("3")){
					date = DateUtil.getMonthLastDayStr(year,month);
				}
			}
			
			//获得月频度的日期
			if(freq==Config.FREQ_MONTH.intValue()){
				int year = new Integer (para[0]).intValue();
				date = DateUtil.getMonthLastDayStr(year,new Integer (para[1]).intValue());
			}
			
			//获得季频度的日期
			if(freq==Config.FREQ_SEASON.intValue()){
				int year = new Integer (para[0]).intValue();
				String season = para[1];
				//第一季度
				if(season.equals("1"))
					date = DateUtil.getMonthLastDayStr(year,3);
				//第二季度
				if(season.equals("2"))
					date = DateUtil.getMonthLastDayStr(year,6);
				//第三季度
				if(season.equals("3"))
					date = DateUtil.getMonthLastDayStr(year,9);
				//第四季度
				if(season.equals("4"))
					date = DateUtil.getMonthLastDayStr(year,12);
			}
			
			//获得半年频度的日期
			if(freq==Config.FREQ_HALFYEAR.intValue()){
				int year = new Integer (para[0]).intValue();
				//上半年
				if(new Integer(para[1]).intValue()==1)
					date = DateUtil.getMonthLastDayStr(year,6);
				//下半年
				if(new Integer(para[1]).intValue()==2)
					date = DateUtil.getMonthLastDayStr(year,12);
			}
			
			//获得年频度的日期
			if(freq==Config.FREQ_YEAR.intValue()){
				int year = new Integer (para[0]).intValue();
				date = DateUtil.getMonthLastDayStr(year,12);
			}
		}
		return date;
	}
	

}
