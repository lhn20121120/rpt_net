package com.cbrc.smis.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.util.FitechException;

/**
 * 判断要读取的文件是excel还是pdf
 * 
 * @author wng.wl
 * @serialData 2006-5-24
 */
public class ReadReportControl extends HttpServlet {
	/** 
	 *  
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return void
	 * @exception IOException,ServletException
	 */
	public void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException,ServletException{	
		
		String childRepId=null;   //子报表ID
		String versionId=null;    //版本号				
		
		if(request.getParameter("childRepId")!=null) childRepId=(String)request.getParameter("childRepId");
		if(request.getParameter("versionId")!=null) versionId=(String)request.getParameter("versionId");	
		
		if(!"".equals(this.isExsist(childRepId,versionId)) && this.isExsist(childRepId,versionId).equals("pdf"))
		{
			// System.out.println(request.getContextPath());
			String part="ReadPDFServlet?childRepId="+childRepId+"&versionId="+versionId;
			
			request.getRequestDispatcher(part).forward(request,response);
		}			
		else if(!"".equals(this.isExsist(childRepId,versionId)) && this.isExsist(childRepId,versionId).equals("excel"))
		{
			String part="ReadExcelServlet?childRepId="+childRepId+"&versionId="+versionId;
			
			request.getRequestDispatcher(part).forward(request,response);
		}
	}
	
	public void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException,ServletException{
		doGet(request,response);
	}
	
	//判断是否存在此要读取的模范板文件(存在返回文件的格式,不存在返回null)
	public String isExsist(String childRepId,String versionId)
	{
		String returnStr=null;		
		FitechException log = new FitechException(ReadReportControl.class);		
		String str="select mcr.templateType from MChildReport mcr where 1=1 and mcr.comp_id.childRepId='"+childRepId+"' and mcr.comp_id.versionId='"+versionId+"'";		
		DBConn conn = null;
		
		try{
			conn = new DBConn();
			Session session=conn.openSession();
			
			Query query=session.createQuery(str);
			List queryList=query.list();
			
			if(queryList.size()!=0){
				String format=String.valueOf(queryList.get(0));				
				if(!"".equals(format) && format.equals("pdf"))
					returnStr="pdf";
				else
					if(!"".equals(format) && format.equals("excel"))
						returnStr="excel";
			}
		}catch(HibernateException he){
			he.printStackTrace();
			log.printStackTrace(he);
		}finally{
			if(conn != null) conn.closeSession();
		}
		
		return returnStr;
	}
}


