/*
 * Created on 2006-5-17
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.fitech.net.common;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
import javax.servlet.http.HttpServletRequest;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CommMethod {
	
	/**
	 * 获得根目录路径.
	 * 
	 * @param request HTTP 请求
	 * @return String 返回根目录路径
	 */
	public static String getRootPath(HttpServletRequest request) {
		if (request == null) {
			throw new RuntimeException("request is null.");
		}
		StringBuffer url = new StringBuffer();
		url.append(request.getScheme() + "://");
		url.append(request.getServerName());
		
		if (request.getServerPort() != 80)
			url.append(":" + request.getServerPort());
		url.append(request.getContextPath());
		
		return url.toString();
	}
	
	/**
	 * 获得文件的绝对路径
	 * @param request HTTP 请求
	 * @param relativePath 相对路径
	 * @return 文件的绝对路径 http://server:port/project/...
	 */
	public static String getAbsolutePath(
			HttpServletRequest request,
			String relativePath) {
		
			if (relativePath.startsWith("/")){				
				return getRootPath(request) + relativePath;
			}else{				
				return getRootPath(request) + "/" + relativePath;
			}
	}
	
	
	/**
	 * 建立通用的页面标签元素
	 * @param request http请求
	 */
	public static void buidPageInfo(HttpServletRequest request) {
		//css
		request.setAttribute("css", CommMethod.getAbsolutePath(request, "css/common.css"));
		//ocx
		request.setAttribute("ocx", CommMethod.getAbsolutePath(request, "ocx/dsoframer.ocx"));
		//js
		request.setAttribute("obtain_js", CommMethod.getAbsolutePath(request, "js/obtain.js"));
		request.setAttribute("webEditor_js", CommMethod.getAbsolutePath(request, "js/webEditor.js"));
		request.setAttribute("date_js", CommMethod.getAbsolutePath(request, "js/date.js"));
	}
	
	/**
     *  
     * TODO 本方法的是格式化数字为长度
     * 
     * @author wunaigang(2005-4-19)	
     * @param n
     * @param len 字符串标准长度
     * @return
     */
	public static String formatInteger(int n, int len) {

		StringBuffer buffer = new StringBuffer(len);

		try {
			String data = String.valueOf(n);

			if (data.length() < len) {
				for (int i = 0; i < len - data.length(); i++) {
					buffer.append('0');
				}
				buffer.append(data);
			}else {
				buffer.append(data.substring(data.length() - len));
			}
		}catch (Exception e) {
			// System.out.println(e.getMessage());
		}

		return buffer.toString();
	}
	/**
	 * 
	 */
	public static boolean IsNumber(String stemp)
	{
		for(int i=0;i<stemp.length();i++)
		{
			char c=stemp.charAt(i);
			if(c<'0'||c>'9')
				return false;
		}
		return true;
	}
	/**
	 * 是否符号
	 * @param temp
	 * @return
	 */
	public static boolean IsMark(char temp)
	{
		
		switch(temp){
			case '+':return true;
			case '-':return true;
			case '*':return true;
			case '/':return true;
			case '(':return true;
			case ')':return true;
			case '>':return true;
			case '<':return true;
			case '=':return true;	
			case ',':return true;
			
			default:return false;
		}
	}
	
}
