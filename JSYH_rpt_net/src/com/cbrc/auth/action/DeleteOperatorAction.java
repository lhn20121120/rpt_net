package com.cbrc.auth.action;

import java.io.IOException;
import java.util.Locale;

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
import com.cbrc.auth.form.OperatorForm;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
import com.fitech.fitosa.adapter.ImpReportData;
import com.fitech.fitosa.bean.UserInfoBean;

/**
     用户信息删除
 *
 * @author 姚捷
 * @struts.action
 *    path="/struts/deleteOperator"
 *
 * @struts.action-forward
 *    name="all"
 *    path="/struts/getAll.do"
 *    redirect="false"
 *

 */
public final class DeleteOperatorAction extends Action {
    private static FitechException log = new FitechException(DeleteOperatorAction.class);
    /**
     * 已使用Hibernate 卞以刚 2011-12-28
     * 影响对象：UserRole MUserToGrp Operator
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
    throws IOException, ServletException 
    {
        MessageResources resources=getResources(request);
        FitechMessages messages = new FitechMessages();
        Locale locale = request.getLocale();
        
        /**取得request范围内的请求参数，并存放在内*/
        OperatorForm operatorForm = new OperatorForm(); 
        RequestUtils.populate(operatorForm, request);
     
        try 
        {
        	/*
			 * 同步分析系统开始
			 
        	if(Config.ISADDFITOSA){
        		operatorForm = StrutsOperatorDelegate.getUserDetail(operatorForm
    					.getUserId());

    			UserInfoBean bean = new UserInfoBean();
    			bean.setUserId(operatorForm.getUserName());

    			ImpReportData ird = new ImpReportData();
    			ird.setWebroot(Config.WEBROOTPATH);
    			ird.deleteUser(bean);
    			ird.deleteRepUser(bean);
        	}
			*/

			/*
			 * 同步分析系统结束
			 */
            /**删除该用户*/
        	/**已使用Hibernate 卞以刚 2011-12-28
        	 * 影响对象：UserRole MUserToGrp Operator*/
            boolean result = StrutsOperatorDelegate.remove(operatorForm);
            if (result == false)
                /** 删除失败 */
                messages.add(FitechResource.getMessage(locale, resources,
                        "delete.failed", "operator.info"));
            else
                /** 删除成功 */
                messages.add(FitechResource.getMessage(locale, resources,
                        "delete.success", "operator.info"));  
        } 
        catch (Exception e) 
        {
             log.printStackTrace(e);
             messages.add(FitechResource.getMessage(locale,resources,"delete.failed","operator.info"));
        }       

        if(messages.getMessages() != null && messages.getMessages().size() != 0)
            request.setAttribute(Config.MESSAGES,messages);
        return mapping.findForward("view");
    }
 }
