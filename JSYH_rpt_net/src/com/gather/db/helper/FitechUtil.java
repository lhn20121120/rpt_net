
package com.gather.db.helper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionMapping;

import sun.misc.BASE64Encoder;
/**
 * 常用方法类 
 * 
 * @author rds
 * @date 2005-11-13
 */
public class FitechUtil {
	
	/**
	 * 日志
	 */
	private static FitechException log = new FitechException(FitechUtil.class);
	
	/**
	 * 非法的附件后缀名
	 */
	private static final String[] UNVALIDEXT = { "exe", "bat", "sh" };

	/**
	 * 星期
	 */
	private static final String[][] WEEK = { 
		{ "1", "星期一" }, 
		{ "2", "星期二" },
		{ "3", "星期三" }, 
		{ "4", "星期四" }, 
		{ "5", "星期五" }, 
		{ "6", "星期六" },
		{ "7", "星期日" } 
	};

	/**
	 * 加密算法名
	 */
	private static final String ALGORITH = "MD5";

	/**
	 * 将字符串解析成日期
	 * 
	 * @param _date String
	 * @return Date
	 */
	public static Date parseDate(String _date) {
		if (_date == null || _date.equals("") || _date.length() < 8) {
			return null;
		} else {
			try {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				return dateFormat.parse(_date);
			} catch (ParseException pe) {
				return null;
			}
		}
	}

	/**
	 * 获得使用MD5加密的字符串的Base64串值
	 * 
	 * @param value String 要加密的字符串
	 * @return String 加密后的Base64串
	 */
	public static String hashMD5String(String value) {
		String result = "";

		try {
			MessageDigest md = MessageDigest.getInstance(ALGORITH);
			byte[] md5Value = md.digest(value.getBytes());
			BASE64Encoder encode = new BASE64Encoder();
			result = encode.encode(md5Value);
		} catch (NoSuchAlgorithmException nae) {
			new FitechException(nae);
		}

		return result;
	}
	
	/**
	 * 标准日期样式
	 */
	public static final String NORMALDATE = "yyyy-MM-dd";

	/**
	 * 中国标准的日期样式
	 */
	public static final String CHINESEDATE = "yyyy年MM月dd日";

	/**
	 * 获取当天的日期
	 * 
	 * @param format 日期样式
	 * @return String
	 */
	public static String getToday(String format) {
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
	public static String getDay() {
		String _day = "";
		Date date = new Date();
		int day = date.getDay();
		for (int i = 0; i < WEEK.length; i++) {
			if (WEEK[i][0].equals(String.valueOf(day)) == true) {
				_day = WEEK[i][1];
				break;
			}
		}
		return _day;
	}

	/**
	 * getParameter方法<br>
	 * 对获取的提交值做GB2312编码转换<br>
	 * 
	 * @param request HttpServletRequest
	 * @param paramName String 变量名
	 * @return String
	 */
	public static String getParameter(HttpServletRequest request,String paramName) {
		if (paramName == null)
			return "";
		if (request.getParameter(paramName) == null)
			return "";
		try {
			return new String(((String) request.getParameter(paramName)).getBytes("ISO8859-1"), "GB2312");
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 判断用户上传的附件是否是正确
	 * 
	 * @author rds
	 * 
	 * @param fileName String 上传附件的文件名
	 * @return boolean 上传的附件不合法，返回false；否则，返回true
	 */
	public static boolean isValidAffix(String fileName) {
		if (fileName == null)
			return false;

		String ext = fileName.substring(fileName.lastIndexOf(".") + 1);

		for (int i = 0; i < UNVALIDEXT.length; i++) {
			if (UNVALIDEXT[i].toLowerCase().equals(ext)) {
				return false;
			}
		}

		return true;
	}
	
	/**
	 * 将日期字串格式化成时间串
	 * 
	 * @param date String 时间字符串
	 * @return Date 转化成功返回Date ，否则返回null
	 */
	public static Date formatToTimestamp(String date) {
		if (date != null && !date.equals("")) {
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
	 * 把request和session 范围内的属性删除
	 * @param mapping 
	 * @param request
	 */
	
	public static void removeAttribute(ActionMapping mapping,HttpServletRequest request)
	{
		   HttpSession session=request.getSession();
		   if(mapping.getAttribute() != null) 
		   {
			   if("request".equals(mapping.getScope()))
			        request.removeAttribute(mapping.getAttribute());
			   else
			      session.removeAttribute(mapping.getAttribute());
		   }
	}
	
	/**
	 * 写文件操作
	 * 
	 * @param in InputStream 文件流
	 * @param ext String 文件后缀名
	 * @param boolean true 写文件成功，返回写入的文件名；否则，返回null
	 */
	public static String writeFile(InputStream in,String ext){
		String tmpFileName=String.valueOf(System.currentTimeMillis()) + "." + ext;
		
		BufferedOutputStream bout=null;
		
		try{
			File file=new File(Config.TEMP_DIR + tmpFileName);
			bout=new BufferedOutputStream(new FileOutputStream(file));
			
			byte[] line=new byte[1024];
			while(in.read(line)!=-1){
				bout.write(line);
			}
		}catch(IOException ioe){
			log.printStackTrace(ioe);
			tmpFileName=null;
		}catch(Exception e){
			log.printStackTrace(e);
			tmpFileName=null;
		}finally{
			if(bout!=null) 
				try{
					bout.close();
				}catch(IOException ioe){
					log.printStackTrace(ioe);
				}
		}
		
		return tmpFileName;
	}
	
	/**
	 * 读文件操作
	 * 
	 * @param fileName String 文件名
	 * @reture InputStream 读取文件失败，返回null
	 */
	public static InputStream readFile(String fileName){
		BufferedInputStream bin=null;
		
		try{
			bin=new BufferedInputStream(new FileInputStream(
					new File(Config.TEMP_DIR + fileName)));
		}catch(IOException ioe){
			log.printStackTrace(ioe);
		}catch(Exception e){
			log.printStackTrace(e);
		}
		
		return bin;
	}
	
	/**
	 * 删除文件操作
	 * 
	 * @param fileName String 要删除的文件
	 * @return boolean 删除成功，返回true;否则，返回false
	 */
	public static boolean deleteFile(String fileName){
		
		try{
			File file=new File(Config.TEMP_DIR + fileName);
			file.deleteOnExit();
			return true;
		}catch(Exception e){
			return false;
		}
	}
}

