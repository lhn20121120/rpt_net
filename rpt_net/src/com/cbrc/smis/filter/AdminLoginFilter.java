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
 * ��̨����Ա��¼������
 * 
 * @author rds
 * @date 2005-08-09
 */
public class AdminLoginFilter implements Filter{
	private boolean flag=false;
	FitechException log=new FitechException(AdminLoginFilter.class);
	
	/**
	 * ϵͳ��¼ҳ
	 */
	private String loginPage="";
	/**
	 * ����Ҫ���˵�ҳ
	 */
	private String notFilterPage="";
	
	
	/**
	 * �ڹ������аѵ�ǰ�û������߳���
	 * �ں�����Ҫʹ�õĵط����ã������Ͳ��ô���request, session֮��Ĳ����ˡ�
	 */
	public static ThreadLocal<Operator> threadOpLocal = new ThreadLocal<Operator>();
	
	
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
				sb.append("<html><head><title>���µ�½</title></head><body>");
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
		this.loginPage=config.getInitParameter("loginPage");
		this.notFilterPage=config.getInitParameter("notFilterPage");
		
		if(this.loginPage==null)
			log.println("��������ʼ��ʱ����ȡϵͳ��¼ҳʧ��!");
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
