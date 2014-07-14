package com.cbrc.smis.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

import com.cbrc.auth.adapter.StrutsOperatorDelegate;
import com.cbrc.auth.adapter.StrutsRoleDelegate;
import com.cbrc.auth.form.OperatorForm;
import com.cbrc.auth.form.RoleForm;
import com.cbrc.auth.form.UserRoleForm;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;

/**
 * 查看用户角色信息 
 * @author 吴昊
 * 
 */
public final class ViewUserAllRoleAction extends Action {
    private static FitechException log = new FitechException(ViewUserAllRoleAction.class); 
    /** 
     * Method execute
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     */
    public ActionForward execute(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)  throws IOException, ServletException
    {

        MessageResources resources=getResources(request);
        FitechMessages messages = new FitechMessages();
        Locale locale = request.getLocale();
        
        UserRoleForm userRoleForm = new UserRoleForm();
        RequestUtils.populate(userRoleForm,request);
        
        /**取得指定用户角色信息*/
        List allRole =null;
        
        String orgId=request.getParameter("orgId");
        if(orgId!=null){
        	
        }
        HttpSession session = request.getSession();
        try 
        {
            /**取得指定用户角色信息*/
        	List orgUserList=StrutsOperatorDelegate.findUserId(orgId);
        	String userIds="";
        	if(orgUserList!=null){
        		for(int i=0;i<orgUserList.size();i++){
        			OperatorForm operatorForm=(OperatorForm)orgUserList.get(i);
        			userIds+=operatorForm.getUserId().toString().trim()+",";
        		}
        	}
        	if(userIds!=null){
        		userIds=userIds.substring(0,userIds.length()-1);
        	}
        	
            List roleList = StrutsRoleDelegate.findAllRole(userIds);

            if (roleList!=null && roleList.size()!=0) 
            {
                allRole =  new ArrayList();
                for (int i=0; i<roleList.size(); i++) 
                {
                    RoleForm role = (RoleForm)roleList.get(i);
                    allRole.add(new LabelValueBean(role.getRoleName(),role.getRoleId().toString()));
                }
            }
        }
        catch (Exception e) 
        {
            log.printStackTrace(e);
            messages.add(FitechResource.getMessage(locale,resources,"select.fail","operator.userGrp.info"));        
        }
       String flag="isSearch";
        if(allRole!=null && allRole.size()!=0)
        {
        	session.setAttribute("AllRole",allRole);
        	session.setAttribute("Flag",flag);
        }
        if(messages.getMessages() != null && messages.getMessages().size() > 0)
            request.setAttribute(Config.MESSAGES,messages);
        return mapping.findForward("show_user_role");

    }

}

