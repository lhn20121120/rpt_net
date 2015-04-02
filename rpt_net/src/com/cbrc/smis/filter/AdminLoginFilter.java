package com.cbrc.smis.filter;

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
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
/**
 * 后台管理员登录过滤器
 * 
 * @author rds
 * @date 2005-08-09
 */
public class AdminLoginFilter implements Filter{
	private boolean flag=false;
	FitechException log=new FitechException(AdminLoginFilter.class);
	
	/**
	 * 系统登录页
	 */
	private String loginPage="";
	/**
	 * 不需要过滤的页
	 */
	private String notFilterPage="";
	
	
	/**
	 * 在过滤器中把当前用户放入线程内
	 * 在后面需要使用的地方调用，这样就不用传入request, session之类的参数了。
	 */
	public static ThreadLocal<Operator> threadOpLocal = new ThreadLocal<Operator>();
	
	
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
		if(Config.WEB_SERVER_PORT==null)
			Config.WEB_SERVER_PORT = request.getServerPort();
		HttpServletRequest req=(HttpServletRequest)request;
		HttpServletResponse res=(HttpServletResponse)response;
		HttpSession session=req.getSession();
		String uri=req.getRequestURI();
		String portal_url = (String) session.getAttribute("portal_url");
		if(isNeedFilter(uri,loginPage,notFilterPage,session)==true){						
			
			if (session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME) == null) {
				flag = true;
			} else {
				flag = false;
			}

			if (flag == true) {
	//			res.sendRedirect(req.getContextPath() + this.loginPage);
				PrintWriter pw = res.getWriter();
				StringBuffer sb = new StringBuffer("");
				sb.append("<html><head><title>重新登陆</title></head><body>");
				sb.append("<script language='javascript'>");
				if(Config.PORTAL){
					sb.append(" top.location='/portal/logout.jsp'");
				}else{
					sb.append(" top.location='" + req.getContextPath()
							+ this.loginPage + "'");
				}
				sb.append("</script>");
				sb.append("</body></html>");
				pw.write(sb.toString());
				pw.flush();
				pw.close();
				return;
			}
		}
		//threadOpLocal.set((Operator) req.getSession().getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME));
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
		this.loginPage=config.getInitParameter("loginPage");
		this.notFilterPage=config.getInitParameter("notFilterPage");
		
		if(this.loginPage==null)
			log.println("过滤器初始化时，获取系统登录页失败!");
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
	private boolean isNeedFilter(String uri,String loginPage,String notFilterPage,HttpSession session){
		boolean result=true;
		
		if(uri==null) return false;
		
		if(uri.indexOf(loginPage)>=0){
			result=false;
		}

		String[] arrNotFilterPage=notFilterPage.split(",");

		if(arrNotFilterPage!=null && arrNotFilterPage.length>0){
			for(int i=0;i<arrNotFilterPage.length;i++){
				if(uri.indexOf(arrNotFilterPage[i])>=0){
					result=false;
					break;
				}
			}
		}
		
		return result;
	}
}
