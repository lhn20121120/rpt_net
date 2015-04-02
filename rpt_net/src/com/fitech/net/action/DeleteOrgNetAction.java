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
 * 机构删除action的实现
 *
 * @author jcm
 *
 */
public final class DeleteOrgNetAction extends Action {
	private static FitechException log = new FitechException(DeleteOrgNetAction.class);
	
	/**
	 * @"view"  mapping Action
	 * @return request request请求
	 * @return  response respponse请求
	 * @exception IOException  是否有IO异常，如有则捕捉并抛出
	 * @exception ServletException 是否有Servlet异常，如有则捕捉并抛出
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
				//查看该机构是否有下级机构
				int count = StrutsOrgNetDelegate.selectLowerOrgs(orgNetForm.getOrg_id());
				if(count > 0){
					messages.add("当前机构有子机构，不能删除!!!");
					request.setAttribute(Config.MESSAGES,messages);
					return new ActionForward(path);
				}
				//查看该机构是否有用户
				count = StrutsOperatorDelegate.findUsers(orgNetForm.getOrg_id());
				if(count > 0){
					messages.add("当前机构有用户，请选删除用户!!!");
					request.setAttribute(Config.MESSAGES,messages);
					return new ActionForward(path);
				}
				//查看该机构是否已经有报表上报        	  
				count = StrutsReportInDelegate.selectOrgRepList(orgNetForm.getOrg_id());        	  
				if(count > 0){        		
					messages.add("当前机构已有报表上报，不能删除!!!");        		  
					request.setAttribute(Config.MESSAGES,messages);        		  
					return new ActionForward(path);        	  
				}
				
				//删除机构相关信息        	  
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
