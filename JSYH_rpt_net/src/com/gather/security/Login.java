package com.gather.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gather.adapter.StrutsMUserDelegate;
import com.gather.common.BusinessCommonUtil;
import com.gather.common.Config;
import com.gather.common.Log;
import com.gather.common.SMMISMessages;
import com.gather.refer.data.ShowInfoUtil;
import com.gather.struts.forms.MUserForm;

/**
 * @author rds
 * @date 2005-07-12
 * 
 * 用户登录操作类
 */

public class Login extends Action{

  
	public ActionForward execute(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response
		)
			throws IOException, ServletException {
		   MUserForm userForm=(MUserForm)form;
		   String userName=userForm.getUserId();
		   String pw=userForm.getPassword();
		   if(userName==null){
			   userName=request.getParameter("userId");
			   pw=request.getParameter("password");
		   } 
		   MUserForm user=checkUser(userName,pw);
		   SMMISMessages msg=new SMMISMessages();
		   if(user==null){
			   msg.add(0,"错误的用户或密码");
			   request.setAttribute(Config.MESSAGE_ATTRIBUTE_NAME,msg);
			   return mapping.findForward("login");  
		   } 
           setOrgSession(request,user);
           // System.out.println("Login test"+user.getUserId()+"--"+user.getOrgId());
           Log.write(Config.LOG_LOGIN,user.getName(),user.getOrgId(),"机构"+user.getOrgId().trim()+"的用户成功登录","");
           return mapping.findForward("index");
	  }
	  
  
	  private void setOrgSession(HttpServletRequest request,MUserForm user){
		  HttpSession session=request.getSession(true);
		  Org org=new Org();
		  org.setOrgId(user.getOrgId());
		  
		  session.setAttribute("orgId",org.getOrgId());
		  session.setAttribute("orgObject",org);
		  session.setAttribute("user",user);
		  
		  //装入待报机构的机构信息
		  List orgList=new ArrayList();
		  String[] orgIds=BusinessCommonUtil.getOrgId(user.getOrgId());
		  for(int i=0;i<orgIds.length;i++){
			  orgList.add(ShowInfoUtil.getOrgFormInfo(orgIds[i]));
		  }
		  session.setAttribute("orgList",orgList);
	  }

  
  private MUserForm checkUser(String userId,String pw){
	  return StrutsMUserDelegate.checkUser(userId,pw);
  }
  
    
}
