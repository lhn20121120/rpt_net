package com.cbrc.auth.action;

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

import com.cbrc.auth.adapter.StrutsRoleDelegate;
import com.cbrc.auth.adapter.StrutsUserRoleDelegate;
import com.cbrc.auth.form.RoleForm;
import com.cbrc.auth.form.UserRoleForm;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;

/**
 * �鿴�û���ɫ��Ϣ
 *
 * @author Ҧ��
 *
 * @struts.action
 *    path="/struts/viewUserRole"
 *
 * @struts.action-forward
 *    name="view"
 *    path="/struts/viewUserRole.jsp"
 *    redirect="false"
 *

 */
public final class ViewUserRoleAction extends Action {
    private static FitechException log = new FitechException(ViewUserRoleAction.class); 
    /** 
     * ��ʹ��hibernate ���Ը� 2011-12-28
     * Ӱ�����UserRole Role
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
      
        /**���û��Ѿ����õĽ�ɫ*/
        List userSetRole = null;          
        /**���н�ɫ��Ϣ*/
        List allRole =null;
        String curPage=request.getParameter("curPage");        
        HttpSession session = request.getSession();
        com.cbrc.smis.security.Operator operator = null; 
        if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
            operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
                       
        try{
            /**���û��Ѿ����õĽ�ɫ*/
        	/**��ʹ��hibernate ���Ը� 2011-12-21 
        	 * Ӱ�����UserRole*/
            List list = StrutsUserRoleDelegate.getUserSetRole(userRoleForm.getUserId());
            if (list!=null && list.size()!=0){
                userSetRole =  new ArrayList();
                for (int i=0; i<list.size(); i++){
                    UserRoleForm item = (UserRoleForm)list.get(i);
                    userSetRole.add(new LabelValueBean(item.getRoleName(),item.getRoleId().toString()));
                }
            }
            
            /**���н�ɫ��Ϣ*/
            /**��ʹ��Hibernate ���Ը� 2011-12-28
             * Ӱ�����Role**/
            List roleList = StrutsRoleDelegate.findAll(operator.getOrgId(),operator.getRoleIds(),operator.isSuperManager());
            if (roleList!=null && roleList.size()!=0){
                allRole =  new ArrayList();
                for (int i=0; i<roleList.size(); i++){
                    RoleForm roleForm = (RoleForm)roleList.get(i);
                    allRole.add(new LabelValueBean(roleForm.getRoleName(),roleForm.getRoleId().toString()));
                }
            }
        }catch (Exception e){
            log.printStackTrace(e);
            messages.add(FitechResource.getMessage(locale,resources,"select.fail","operator.userGrp.info"));        
        }
        /**�û����*/
        if(userRoleForm.getUserId()!=null)
            request.setAttribute("UserId",userRoleForm.getUserId());
        /**�û�����*/
        if(userRoleForm.getUserName()!=null)
            request.setAttribute("UserName",userRoleForm.getUserName());
        /**���û��Ѿ����õĽ�ɫ*/
        if(userSetRole!=null && userSetRole.size()!=0)
            request.setAttribute("UserSetRole",userSetRole);
        /**�����û�����Ϣ*/
        if(allRole!=null && allRole.size()!=0)
            request.setAttribute("AllRole",allRole);
        if(messages.getMessages() != null && messages.getMessages().size() > 0)
            request.setAttribute(Config.MESSAGES,messages);
        request.setAttribute("curPage",curPage);
        return mapping.findForward("user_role_setting");
    }
}

