package com.fitech.net.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechUtil;
import com.fitech.net.adapter.StrutsTargetDefineDelegate;
import com.fitech.net.form.TargetDefineForm;
public final class UpdateTargetDefineAction extends Action {
	private static FitechException log = new FitechException(UpdateTargetDefineAction.class);
   /**
    * @param mapping Action mapping.
    * @param form Action form.
    * @param request HTTP request.
    * @param response HTTP response.
    * @exception IOException�Ƿ�������/������쳣
    * @exception ServletException�Ƿ���servlet���쳣ռ��
    */
 
	public ActionForward execute(
	      ActionMapping mapping,
	      ActionForm form,
	      HttpServletRequest request,
	      HttpServletResponse response
	   )throws IOException, ServletException {
		   		   
		MessageResources resources=this.getResources(request);		
		FitechMessages messages=new FitechMessages();		
		TargetDefineForm targetDefineForm = (TargetDefineForm) form;
								
		String curPage="";					
		if(request.getParameter("curPage")!=null) curPage=(String)request.getParameter("curPage");

		boolean updateResult = false;
					
		try {	
			/***
	         * ȡ�õ�ǰ�û���Ȩ����Ϣ
	         */   
			HttpSession session = request.getSession();
			Operator operator = null; 
			if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)  
				operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);  
			
			if (targetDefineForm != null) {
				targetDefineForm.setSetOrgId(operator.getOrgId());
				updateResult = StrutsTargetDefineDelegate.update(targetDefineForm);
				System.out.println("updateResult="+updateResult);
				if (updateResult == true){	//���³ɹ�													
					messages.add("ָ�궨���޸ĳɹ���");										   
				}else{	                				
					messages.add("ָ�궨���޸�ʧ�ܣ�");	
				}			  
			}				
		}catch (Exception e){					
			updateResult=false;							
			messages.add("ָ�궨���޸�ʧ�ܣ�");								
			log.printStackTrace(e);  
		}	       
		
		if(updateResult==true)	       	
			FitechUtil.removeAttribute(mapping,request);
		
System.out.println(messages.getAlertMsg());		
		//�ж�������ʾ��Ϣ�����н���洢��Request�����У���������			
		if(messages!=null && messages.getSize()>0)			
			request.setAttribute(Config.MESSAGES,messages);

		String path="";
		
		if(updateResult==true){
			form = null;
			path = mapping.findForward("view").getPath() + "?curPage=" + curPage + "&defineName=&businessId=&normalId=" ;
		}else{			
			path = mapping.findForward("edit").getPath() + "?defineName=&businessId=&normalId=";
		}
		return new ActionForward(path);
	}
}