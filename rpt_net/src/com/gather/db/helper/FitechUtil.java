
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
 * ���÷����� 
 * 
 * @author rds
 * @date 2005-11-13
 */
public class FitechUtil {
	
	/**
	 * ��־
	 */
	private static FitechException log = new FitechException(FitechUtil.class);
	
	/**
	 * �Ƿ��ĸ�����׺��
	 */
	private static final String[] UNVALIDEXT = { "exe", "bat", "sh" };

	/**
	 * ����
	 */
	private static final String[][] WEEK = { 
		{ "1", "����һ" }, 
		{ "2", "���ڶ�" },
		{ "3", "������" }, 
		{ "4", "������" }, 
		{ "5", "������" }, 
		{ "6", "������" },
		{ "7", "������" } 
	};

	/**
	 * �����㷨��
	 */
	private static final String ALGORITH = "MD5";

	/**
	 * ���ַ�������������
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
	 * ���ʹ��MD5���ܵ��ַ�����Base64��ֵ
	 * 
	 * @param value String Ҫ���ܵ��ַ���
	 * @return String ���ܺ��Base64��
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
	 * ��׼������ʽ
	 */
	public static final String NORMALDATE = "yyyy-MM-dd";

	/**
	 * �й���׼��������ʽ
	 */
	public static final String CHINESEDATE = "yyyy��MM��dd��";

	/**
	 * ��ȡ���������
	 * 
	 * @param format ������ʽ
	 * @return String
	 */
	public static String getToday(String format) {
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
	 * getParameter����<br>
	 * �Ի�ȡ���ύֵ��GB2312����ת��<br>
	 * 
	 * @param request HttpServletRequest
	 * @param paramName String ������
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
	 * �ж��û��ϴ��ĸ����Ƿ�����ȷ
	 * 
	 * @author rds
	 * 
	 * @param fileName String �ϴ��������ļ���
	 * @return boolean �ϴ��ĸ������Ϸ�������false�����򣬷���true
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
	 * �������ִ���ʽ����ʱ�䴮
	 * 
	 * @param date String ʱ���ַ���
	 * @return Date ת���ɹ�����Date �����򷵻�null
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
	 * ��request��session ��Χ�ڵ�����ɾ��
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
	 * д�ļ�����
	 * 
	 * @param in InputStream �ļ���
	 * @param ext String �ļ���׺��
	 * @param boolean true д�ļ��ɹ�������д����ļ��������򣬷���null
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
	 * ���ļ�����
	 * 
	 * @param fileName String �ļ���
	 * @reture InputStream ��ȡ�ļ�ʧ�ܣ�����null
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
	 * ɾ���ļ�����
	 * 
	 * @param fileName String Ҫɾ�����ļ�
	 * @return boolean ɾ���ɹ�������true;���򣬷���false
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

