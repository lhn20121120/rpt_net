package com.cbrc.smis.filter;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechException;
import com.fitech.gznx.common.DateUtil;
import com.fitech.gznx.service.ETLTaskDelegate;
/**
 * ETL 任务跑批任务是否结束过滤器
 * 
 * @author rds
 * @date 2005-08-09
 */
public class CheckTaskFilter implements Filter{
	private boolean flag=false;
	FitechException log=new FitechException(CheckTaskFilter.class);
	
	/**
	 * 需要过滤的请求
	 */
	private String needFilterPage="";
	
	/**
	 * doFilter方法
	 * 
	 * @param request ServletRequest
	 * @param response ServletResponse
	 * @param chain FilterChain
	 * @return void
	 */
	public void doFilter(ServletRequest request,ServletResponse response,FilterChain chain) 
		throws IOException,ServletException{
		HttpServletRequest req=(HttpServletRequest)request;
		HttpServletResponse res=(HttpServletResponse)response;
		HttpSession session=req.getSession();
		String uri=req.getRequestURI();
		
		boolean flag  = isNeedFilter(uri,needFilterPage,session);
		if(Config.ETL_TASK_CHECK==1&&flag){						
			String date = req.getParameter("date");
			if(date!=null){
				System.out.println("date="+date);
			}
			else{
				date = (String) session.getAttribute(Config.USER_LOGIN_DATE);
				System.out.println("date is login_date="+date);
			}
			String reportFlag = (String)session.getAttribute(Config.REPORT_SESSION_FLG);
			if(reportFlag!=null && reportFlag.equals("1")){
				boolean taskOver = ETLTaskDelegate.isOver(date);
				if(!taskOver){
					req.setAttribute("checkTask", "task.is.running");
	//				PrintWriter pw = res.getWriter();
	//				StringBuffer sb = new StringBuffer("");
	//				
	////				sb.append("<html><head><title></title></head><body>");
	//				sb.append("<script language='javascript'>");
	////				String ss  = " ETL TASK IS RUNNING,TRY LATER!";
	////				String content = new String (ss.getBytes(),"GB2312");
	////				sb.append("alert('"+date+content+"');");
	//				
	//				//得到上期日期默认频度是月
	//				date = DateUtil.getLastFreqDate(date, 1);
	//				String newUrl = "location='" 
	//						+ uri + "?date="+date+"'";
	//				sb.append(newUrl);
	//				
	//				sb.append("</script>");
	////				sb.append("</body></html>");
	//				pw.write(sb.toString());
	//				pw.flush();
	//				pw.close();
	//				return ;
				}
			}
		}
		chain.doFilter(request,response);		
	}
	
	/**
	 * destroy方法
	 * 
	 * @return void
	 */
	public void destroy(){
		
	}
	
	/**
	 * init方法
	 * 
	 * @param config FilterConfig
	 * @return void
	 */
	public void init(FilterConfig config) throws ServletException{
		this.needFilterPage=config.getInitParameter("needFilterPage");
	}	
	
	   /**
	    * 判断当前请求的URI是否需要过滤
	    * 
	    * @param uri String 请求的URI
	    * @param loginPage String 系统登录页
	    * @param notFilterPage String 不需要过滤的页
	    * @param session HttpSession
	    * @return boolean 需要过滤，返回true;否则，返回false
	    */
	private boolean isNeedFilter(String uri,String needFilterPage,HttpSession session){
		boolean result=false;
		
		if(uri==null) return false;

		String[] arrNeedFilterPage=needFilterPage.split(",");
		if(arrNeedFilterPage!=null && arrNeedFilterPage.length>0){
			for(int i=0;i<arrNeedFilterPage.length;i++){			
				if(uri.indexOf(arrNeedFilterPage[i])>=0){
					result=true;
					break;
				}
			}
		}
		
		return result;
	}
}
