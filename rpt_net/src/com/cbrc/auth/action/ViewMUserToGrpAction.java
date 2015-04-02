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

import com.cbrc.auth.adapter.StrutsMUserGrpDelegate;
import com.cbrc.auth.adapter.StrutsMUserToGrpDelegate;
import com.cbrc.auth.form.MUserGrpForm;
import com.cbrc.auth.form.MUserToGrpForm;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;

/**
 * 用户用户组查看
 *
 *@author 姚捷
 *
 * @struts.action
 *    path="/struts/viewMUserToGrp"
 *
 * @struts.action-forward
 *    name="view"
 *    path="/struts/viewMUserToGrp.jsp"
 *    redirect="false"
 *

 */
public final class ViewMUserToGrpAction extends Action {
    private static FitechException log = new FitechException(ViewMUserToGrpAction.class); 
    /** 
     * 已使用Hibernate 卞以刚 2011-12-28
     * 影响对象：MUserToGrp MUserGrp
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
        
        MUserToGrpForm mUserToGrpForm = new MUserToGrpForm();
        RequestUtils.populate(mUserToGrpForm,request);      
//        mUserToGrpForm.setUserName(new String(request.getParameter("userName").getBytes("iso-8859-1"),"gb2312"));
        /**该用户已经设置的用户组*/
        List userSetUserGrp = null;          
        /**所有用户组信息*/
        List allUserGrp =null;
        

        String curPage=request.getParameter("curPage");
        
        HttpSession session = request.getSession();
        com.cbrc.smis.security.Operator operator = null; 
        if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
            operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);        
        
        try{            
            /**查询该用户设置的用户组*/
        	/**已使用hibernate 卞以刚 2011-12-21 
        	 * 影响对象：MUserToGrp*/
            List list = StrutsMUserToGrpDelegate.getUserSetUserGrp(mUserToGrpForm.getUserId());
            if (list!=null && list.size()!=0){
                userSetUserGrp =  new ArrayList();
                for (int i=0; i<list.size(); i++){
                    MUserToGrpForm item = (MUserToGrpForm)list.get(i);
                    userSetUserGrp.add(new LabelValueBean(item.getUserGrpNm(),item.getUserGrpId().toString()));
                }
            }
            
            /**查询所有用户组*/
            /**已使用Hibernate 卞以刚 2011-12-28
             * 影响对象：MUserGrp*/
            List userGrp = StrutsMUserGrpDelegate.findAll(operator.getOrgId(),operator.getUserGrpIds(),operator.isSuperManager());
            if (userGrp!=null && userGrp.size()!=0){
                allUserGrp =  new ArrayList();
                for (int i=0; i<userGrp.size(); i++){
                    MUserGrpForm mUserGrpForm = (MUserGrpForm)userGrp.get(i);
                    allUserGrp.add(new LabelValueBean(mUserGrpForm.getUserGrpNm(),mUserGrpForm.getUserGrpId().toString()));
                }
            }
        }catch (Exception e){
            log.printStackTrace(e);
            messages.add(FitechResource.getMessage(locale,resources,"select.fail","operator.userGrp.info"));        
        }
        /**用户编号*/
        if(mUserToGrpForm.getUserId()!=null)
            request.setAttribute("UserId",mUserToGrpForm.getUserId());
        /**用户名称*/
        if(mUserToGrpForm.getUserName()!=null)
            request.setAttribute("UserName",mUserToGrpForm.getUserName());
        /**该用户已经设置的用户组*/
         if(userSetUserGrp!=null && userSetUserGrp.size()!=0)
            request.setAttribute("UserSetUserGrp",userSetUserGrp);
        /**所有用户组信息*/
        if(allUserGrp!=null && allUserGrp.size()!=0)
            request.setAttribute("AllUserGrp",allUserGrp);
        if(messages.getMessages() != null && messages.getMessages().size() > 0)
            request.setAttribute(Config.MESSAGES,messages);
        request.setAttribute("curPage",curPage);
        return mapping.findForward("user_userGrp_setting");
    }
}

