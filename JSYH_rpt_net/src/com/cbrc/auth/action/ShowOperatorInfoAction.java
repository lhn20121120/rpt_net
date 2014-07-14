package com.cbrc.auth.action;


import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.cbrc.auth.adapter.StrutsOperatorDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
import com.cbrc.smis.util.FitechUtil;

/**
 * �鿴�����µ��û������Ϣ
 *  @author Ҧ��
 *
 * @struts.action
 *    path="/struts/viewOperator"
 *
 * @struts.action-forward
 *    name="view"
 *    path="/struts/viewOperator.jsp"
 *    redirect="false"
 *

 */
public final class ShowOperatorInfoAction extends Action {
    private static FitechException log = new FitechException(ShowOperatorInfoAction.class);  
    /**
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
        Locale locale = request.getLocale();

         List list = null;
         String orgId=null;
        	 
         try{
        	 orgId=request.getParameter("orgId");
             //��ʾ��¼
             	list = StrutsOperatorDelegate.select(orgId);
                  
         }catch (Exception e){
             log.printStackTrace(e);
             messages.add(FitechResource.getMessage(locale,resources,"select.fail","operator.info"));      
         }
         
         //�Ƴ�request��session��Χ�ڵ�����
         FitechUtil.removeAttribute(mapping,request);
         //��ApartPage��������request��Χ��
 
         if(messages.getMessages() != null && messages.getMessages().size() > 0)
           request.setAttribute(Config.MESSAGES,messages);
         if(list!=null && list.size()!=0)
               request.setAttribute(Config.RECORDS,list);
          
         return mapping.findForward("show_info");
    }
}
