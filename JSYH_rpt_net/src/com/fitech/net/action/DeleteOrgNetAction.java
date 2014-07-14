package com.fitech.net.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

import com.cbrc.auth.adapter.StrutsOperatorDelegate;
import com.cbrc.smis.adapter.StrutsReportInDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.net.adapter.StrutsOrgNetDelegate;
import com.fitech.net.form.OrgNetForm;
/**
 * ����ɾ��action��ʵ��
 *
 * @author jcm
 *
 */
public final class DeleteOrgNetAction extends Action {
	private static FitechException log = new FitechException(DeleteOrgNetAction.class);
	
	/**
	 * @"view"  mapping Action
	 * @return request request����
	 * @return  response respponse����
	 * @exception IOException  �Ƿ���IO�쳣��������׽���׳�
	 * @exception ServletException �Ƿ���Servlet�쳣��������׽���׳�
	 */
	public ActionForward execute(
      ActionMapping mapping,
      ActionForm form,
      HttpServletRequest request,
      HttpServletResponse response   
	)throws IOException, ServletException{
		
		String path = "/viewOrgNet.do";	   
		String curPage=request.getParameter("curPage");
		if(curPage!=null)path="/org/selectOrgNet.do?curPage="+curPage;
		MessageResources resources = this.getResources(request);	   
		FitechMessages messages = new FitechMessages();

		OrgNetForm orgNetForm = new OrgNetForm();                 
		RequestUtils.populate(orgNetForm, request);
      
		if(orgNetForm!=null && orgNetForm.getOrg_id()!=null){    	
			try {   
				//�鿴�û����Ƿ����¼�����
				int count = StrutsOrgNetDelegate.selectLowerOrgs(orgNetForm.getOrg_id());
				if(count > 0){
					messages.add("��ǰ�������ӻ���������ɾ��!!!");
					request.setAttribute(Config.MESSAGES,messages);
					return new ActionForward(path);
				}
				//�鿴�û����Ƿ����û�
				count = StrutsOperatorDelegate.findUsers(orgNetForm.getOrg_id());
				if(count > 0){
					messages.add("��ǰ�������û�����ѡɾ���û�!!!");
					request.setAttribute(Config.MESSAGES,messages);
					return new ActionForward(path);
				}
				//�鿴�û����Ƿ��Ѿ��б����ϱ�        	  
				count = StrutsReportInDelegate.selectOrgRepList(orgNetForm.getOrg_id());        	  
				if(count > 0){        		
					messages.add("��ǰ�������б����ϱ�������ɾ��!!!");        		  
					request.setAttribute(Config.MESSAGES,messages);        		  
					return new ActionForward(path);        	  
				}
				
				//ɾ�����������Ϣ        	  
				boolean result=StrutsOrgNetDelegate.removeOrgXGInfo(orgNetForm);        	          	
				if (result == true)        		
					messages.add(resources.getMessage("delete.orgNet.success"));        	  
				else        		
					messages.add(resources.getMessage("delete.orgNet.failed"));	    		
			}catch (Exception e){    		
				log.printStackTrace(e);
    			messages.add(resources.getMessage("delete.orgNet.failed"));	
    		}      
		}else{    	
			messages.add(resources.getMessage("delete.orgNet.failed"));	       
		}            
		if(messages.getMessages() != null && messages.getMessages().size() != 0)          	
			request.setAttribute(Config.MESSAGES,messages);
      
		return new ActionForward(path);   
	}
}
