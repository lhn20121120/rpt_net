package com.cbrc.auth.action;

import java.io.IOException;
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
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

import com.cbrc.auth.adapter.StrutsOperatorDelegate;
import com.cbrc.auth.form.OperatorForm;
import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
import com.cbrc.smis.util.FitechUtil;

/**
 * �鿴�û������Ϣ
 *  @author wh
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
public final class SearchOperatorAction extends Action {
    private static FitechException log = new FitechException(SearchOperatorAction.class);  
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
        OperatorForm operatorForm=(OperatorForm)form;
		RequestUtils.populate(operatorForm,request);
		
        
         int recordCount =0; //��¼����
         int offset=0; //ƫ����
         int limit=0;  //ÿҳ��ʾ�ļ�¼����
         List list = null;
         
         ApartPage aPage=new ApartPage();
         String strCurPage=request.getParameter("curPage");
         
         if(strCurPage!=null && !strCurPage.equals(""))
         {
             aPage.setCurPage(new Integer(strCurPage).intValue());
         }
         else
             aPage.setCurPage(1);
         //����ƫ����
         offset=(aPage.getCurPage()-1) * Config.PER_PAGE_ROWS; 
         limit = Config.PER_PAGE_ROWS;   

         HttpSession session = request.getSession();
         com.cbrc.smis.security.Operator operator = null; 
         if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
             operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);         
         
         try{
        	 String subOrgIds = operator.getSubOrgIds();
        	 if(subOrgIds != null && !subOrgIds.equals("")) subOrgIds = subOrgIds + ",'" + operator.getOrgId() + "'";
        	 else subOrgIds = "'" + operator.getOrgId() + "'";
        	 
        	 String userName=operatorForm.getUserName();
        	 
             //ȡ�ü�¼����
             recordCount = StrutsOperatorDelegate.getRecordCountOP(subOrgIds,operator.isSuperManager(),userName,null);
             
             //��ʾ��ҳ��ļ�¼
             if(recordCount > 0){
             	list = StrutsOperatorDelegate.selectOP(subOrgIds,offset,limit,operator.isSuperManager(),userName,null);
             }
                  
         }catch (Exception e){
             log.printStackTrace(e);
             messages.add(FitechResource.getMessage(locale,resources,"select.fail","operator.info"));      
         }
         
         //�Ƴ�request��session��Χ�ڵ�����
         FitechUtil.removeAttribute(mapping,request);
         //��ApartPage��������request��Χ��
         aPage.setCount(recordCount);
         request.setAttribute(Config.APART_PAGE_OBJECT,aPage);
         
         if(messages.getMessages() != null && messages.getMessages().size() > 0)
           request.setAttribute(Config.MESSAGES,messages);
         if(list!=null && list.size()!=0)
               request.setAttribute(Config.RECORDS,list);
          
         return mapping.findForward("user_mgr");
    }
}
