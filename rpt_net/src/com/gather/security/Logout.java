/*
 * �������� 2005-7-12
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package com.gather.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gather.adapter.StrutsMUserDelegate;
import com.gather.common.Config;
import com.gather.common.Log;
import com.gather.struts.forms.MUserForm;

/**
 * @author rds
 * @date 2005-07-12
 * 
 * �û���¼������
 */

public class Logout extends Action{

  
	public ActionForward execute(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response
		)
			throws IOException, ServletException {
		  HttpSession session=request.getSession();
		  if(session!=null){
			  if(session.getAttribute("user")!=null){
				  Log.write(Config.LOG_LOGOUT,((MUserForm)session.getAttribute("user")).getName(),((MUserForm)session.getAttribute("user")).getOrgId(),"�û�"+(String)session.getAttribute("orgId")+"�˳�ϵͳ","");  
			  }
			  session.removeAttribute("orgId");
			  session.removeAttribute("user");
		  }
           return mapping.findForward("logout");
	  }
	  

  
  private MUserForm checkUser(String userId,String pw){
	  return StrutsMUserDelegate.checkUser(userId,pw);
  }
  
    
}
