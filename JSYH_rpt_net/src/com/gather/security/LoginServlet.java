package com.gather.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginServlet extends HttpServlet{
	
	  /**
	   * doGet方法
	   * 
	   * @return void
	   */
	  public void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException,ServletException{
          String orgId=request.getParameter("orgId");
          if(orgId==null) orgId="0001";
          setOrgSession(request,orgId);
          response.sendRedirect("index.jsp");
	  }
	  
	  /**
	   * doPost方法
	   * 
	   * @return void
	   */
	  public void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException,ServletException{
	  	doGet(request,response);
	  }
	
	  private void setOrgSession(HttpServletRequest request,String orgId){
		  HttpSession session=request.getSession(true);
		  Org org=new Org();
		  org.setOrgId(orgId);
		  session.setAttribute("orgId",org.getOrgId());
		  session.setAttribute("orgObject",org);
	  }

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
