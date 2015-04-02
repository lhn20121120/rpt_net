package com.fitech.net.action;
 
import java.io.IOException;
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

import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.MCurUnitForm;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechLog;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
import com.fitech.net.form.AAnalysisTPForm;
/**
 * ɾ��action��ʵ��
 *
 * @author wh
 *
 */
public final class DeleteAnalysisTemplateAction extends Action {
	private static FitechException log = new FitechException(DeleteAnalysisTemplateAction.class);
   /**
    * @"view"  mapping Action
    * @return request request����
    * @return  response respponse����
    * @exception IOException  �Ƿ���IO�쳣�����в�׽���׳�
    * @exception ServletException �Ƿ���Servlet�쳣��������׽���׳�
    */
   public ActionForward execute(
      ActionMapping mapping,
      ActionForm form,
      HttpServletRequest request,
      HttpServletResponse response
   )
   throws IOException, ServletException{
	   Locale locale=getLocale(request);
	   MessageResources resources=this.getResources(request);
	   FitechMessages messages=new FitechMessages();

	   AAnalysisTPForm aAnalysisTPForm = (AAnalysisTPForm)form ;
	   RequestUtils.populate(aAnalysisTPForm, request);
      
      try{
			HttpSession session = request.getSession();
    		Operator operator = null; 
    		if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
    			operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
    		
			boolean result = com.cbrc.smis.adapter.StrutsMCurUnitDelegate.remove(aAnalysisTPForm);
			/**ɾ���ɹ�*/
            if (result == true) {
            	/** д����־ */
            	 
            	
                messages.add("ɾ���ɹ�");
			}
            /**ɾ��ʧ��*/
            else{ 
            	/** д����־ */
            	 
            	
                messages.add("ɾ��ʧ��");
            }
		} 
        catch (Exception e){
			log.printStackTrace(e);
			messages.add("ɾ��ʧ��");
        }
   
        if(messages.getMessages() != null && messages.getMessages().size() != 0)
               request.setAttribute(Config.MESSAGES,messages);
    
        return mapping.findForward("view");	 
   }
}
