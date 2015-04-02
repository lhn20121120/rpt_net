package com.fitech.net.obtain.text;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.smis.dao.DBConn;

public class DeleteFormulaAction   extends Action{

	public ActionForward execute(ActionMapping mapping, 
			ActionForm form,
			HttpServletRequest request, 
			HttpServletResponse response)
			throws IOException, ServletException {
		
		String  id=String.valueOf(request.getParameter("id"));
		
		try
		{
				delete(id);
		}
		catch(Exception e)
		{
		 e.printStackTrace();	
		}
		
		
		return new ActionForward("/obtain/text/viewformula2.do");
	}
	public boolean delete(String id) throws HibernateException 
	{
		boolean result =false;
		DBConn conn = null;
		Session session = null;
		try {
			conn = new DBConn();
			session = conn.beginTransaction();
			// System.out.println(id);
			obtaintext infoFiles=(obtaintext)session.load(obtaintext.class,id);
			// System.out.println(infoFiles.getId()+infoFiles.getFormula());
			session.delete(infoFiles);
			session.flush();
			conn.endTransaction(true);
			result=true;
			// System.out.println("true");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(session != null) session.close();
		}
		return result;
		
	}
}
