package com.cbrc.org.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.hibernate.Session;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.smis.dao.DBConn;

public final class TestAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DBConn conn = null;
		Session session = null;

		try{
			conn = new DBConn();
			session = conn.openSession();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			conn.closeSession();
		}
		
		
		 System.out.println();

		return null;
	}
	public static void main(String[] args)throws Exception{
//		ReportDefine rd = (ReportDefine)ReportUtils.read("C:\\Users\\CHENBING\\Desktop\\445.raq");
//		INormalCell cell =  rd.getCell(10, (short)2);
//		cell.setModified(false);
//	    OutputStream out = new FileOutputStream("C:\\Users\\CHENBING\\Desktop\\445_bak.raq");
		String path = "C:\\\\apache-tomcat-6.0.18\\\\webapps\\\\FITETL\\\\etlScript\\\\tmp\\\\";
		path = "C:\\apache-tomcat-6.0.18\\webapps\\FITETL\\etlScript\\tmp\\";
     	path = path.replaceAll("\\\\", "\\\\\\\\");
		String str = "call %SCRIPTPATH%md.bat";
		String ss  = str.replaceFirst("%SCRIPTPATH%", path);
//		String str = "callaaamd.bat";
//		String ss  = str.replaceFirst("aaa", "C:\\\\apache-tomcat-6.0.18\\\\webapps\\\\FITETL\\\\etlScript\\\\tmp\\\\");
		System.out.println(ss);
		
	}
}
