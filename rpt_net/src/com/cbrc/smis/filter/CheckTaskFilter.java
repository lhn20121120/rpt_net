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
 * ETL �������������Ƿ����������
 * 
 * @author rds
 * @date 2005-08-09
 */
public class CheckTaskFilter implements Filter{
	private boolean flag=false;
	FitechException log=new FitechException(CheckTaskFilter.class);
	
	/**
	 * ��Ҫ���˵�����
	 */
	private String needFilterPage="";
	
	/**
	 * doFilter����
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
	//				//�õ���������Ĭ��Ƶ������
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
	 * destroy����
	 * 
	 * @return void
	 */
	public void destroy(){
		
	}
	
	/**
	 * init����
	 * 
	 * @param config FilterConfig
	 * @return void
	 */
	public void init(FilterConfig config) throws ServletException{
		this.needFilterPage=config.getInitParameter("needFilterPage");
	}	
	
	   /**
	    * �жϵ�ǰ�����URI�Ƿ���Ҫ����
	    * 
	    * @param uri String �����URI
	    * @param loginPage String ϵͳ��¼ҳ
	    * @param notFilterPage String ����Ҫ���˵�ҳ
	    * @param session HttpSession
	    * @return boolean ��Ҫ���ˣ�����true;���򣬷���false
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
