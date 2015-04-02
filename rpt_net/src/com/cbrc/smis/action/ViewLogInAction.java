package com.cbrc.smis.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.adapter.StrutsLogInDelegate;
import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.LogInForm;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechUtil;


/** 
 * @author Ҧ��
 * ģ����:��־����  
 * ����:��־��ѯ����ҳ��ʾ
 * Creation date: 11-29-2005
 * @struts.action path="/viewLogIn" name="ViewLogForm" input="/system_mgr/log_mgr.jsp" scope="request" validate="true"
 * @struts.action-forward name="log_mgr" path="/system_mgr/log_mgr.jsp"
 */
public final class ViewLogInAction extends Action {
	private static FitechException log = new FitechException(ViewLogInAction.class);  
	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-28
	 * Ӱ�����LogIn
	 * com.cbrc.smis.adapter.StrutsLogInDelegate��
	 * 206,210,277��281��oracle�﷨to_date����ת��Ϊsqlserver convert���� ������
	 * ���������ݿ��ж�
    * Performs action.
    * @param mapping Action mapping.
    * @param form Action form.
    * @param request HTTP request.
    * @param response HTTP response.
    * @exception IOException if an input/output error occurs
    * @exception ServletException if a servlet exception occurs
    */
   public ActionForward execute(
      ActionMapping mapping,
      ActionForm form,
      HttpServletRequest request,
      HttpServletResponse response
   )
      throws IOException, ServletException {
	
	   MessageResources resources=getResources(request);
	   FitechMessages messages = new FitechMessages();
	   //ȡ��request��Χ�ڵ�����������������logInForm��
		LogInForm logInForm = (LogInForm) form;
		RequestUtils.populate(logInForm, request);
	
		int recordCount =0; //��¼����
		int offset=0; //ƫ����
		int limit=0;  //ÿҳ��ʾ�ļ�¼����
		List list = null;
		
		ApartPage aPage=new ApartPage();
	   	String strCurPage=request.getParameter("curPage");
		if(strCurPage!=null){
		    if(!strCurPage.equals(""))
		      aPage.setCurPage(new Integer(strCurPage).intValue());
		}
		else
			aPage.setCurPage(1);
		//����ƫ����
		offset=(aPage.getCurPage()-1)*Config.PER_PAGE_ROWS; 
		limit = Config.PER_PAGE_ROWS;	
		
		try 

		{
	   		//ȡ�ü�¼����
			/**��ʹ��hibernate ���Ը� 2011-12-28
			 * Ӱ�����LogIn
			 *  com.cbrc.smis.adapter.StrutsLogInDelegate��277��281��oracle�﷨to_date����ת��Ϊsqlserver convert���� ������
			 *  ���������ݿ��ж�*/
	   		recordCount = StrutsLogInDelegate.getRecordCount(logInForm);
	   		//��ʾ��ҳ��ļ�¼
	   		if(recordCount > 0)
	   			/**��ʹ��hibernate ���Ը� 2011-12-28
	   			 * Ӱ�����LogIn
	   			 * 206��210�н�oracle�﷨to_date����ת��Ϊsqlserver convert���� ������
	   			 * ���������ݿ��ж�*/
		   	    list = StrutsLogInDelegate.select(logInForm,offset,limit);  
		}
		catch (Exception e) 
		{
			log.printStackTrace(e);
			messages.add(resources.getMessage("log.select.fail"));		
		}
		//�Ƴ�request��session��Χ�ڵ�����
		FitechUtil.removeAttribute(mapping,request);
		//��ApartPage��������request��Χ��
	 	aPage.setTerm(this.getTerm(logInForm));
	 	aPage.setCount(recordCount);
	 	request.setAttribute(Config.APART_PAGE_OBJECT,aPage);
		
	 	if(messages.getMessages() != null && messages.getMessages().size() > 0)
	   	  request.setAttribute(Config.MESSAGES,messages);
	 	if(list!=null && list.size()!=0)
		  request.setAttribute(Config.RECORDS,list);
	 	//����ѯ���������request��Χ��
	 	request.setAttribute("form",logInForm);
	 	
	 	return mapping.findForward("log_mgr");
   }
   /**
    * ȡ�ò�ѯ����url  
    * @param logInForm
    * @return
    */
   public String getTerm(LogInForm logInForm)
   {
	   String term="";
	   
	   Integer logTypeId = logInForm.getLogTypeId();
	   String operation = logInForm.getOperation();
	   String userName = logInForm.getUserName();
	   String startDate = logInForm.getStartDate();
	   String endDate = logInForm.getEndDate();
	   
	   if(logTypeId!=null)
	   {
		   term += (term.indexOf("?")>=0 ? "&" : "?");
		   term += "logTypeId="+logTypeId.toString();   
	   }
	   if(operation!=null && !operation.equals(""))
	   {
		   term += (term.indexOf("?")>=0 ? "&" : "?");
		   term += "operation="+operation.toString();   
	   }
	   if(userName!=null && !userName.equals(""))
	   {
		   term += (term.indexOf("?")>=0 ? "&" : "?");
		   term += "userName="+userName.toString();   
	   }
	   if(startDate!=null && !startDate.equals(""))
	   {
		   term += (term.indexOf("?")>=0 ? "&" : "?");
		   term += "startDate="+startDate.toString();   
	   }
	   if(endDate!=null && !endDate.equals(""))
	   {
		   term += (term.indexOf("?")>=0 ? "&" : "?");
		   term += "endDate="+endDate.toString();   
	   }
	   if(term.indexOf("?")>=0)
		   term = term.substring(term.indexOf("?")+1);		   	  
	   return term;	   
   }
}
