package com.cbrc.smis.action;

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
/**
 *���ҵ�λɾ��action��ʵ��
 *
 * @author����
 *
 */
public final class DeleteMCurUnitAction extends Action {
	private static FitechException log = new FitechException(DeleteLogInAction.class);
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

      //mCurUnitForm MCurUnitForm�ĳ�ʼ��
      MCurUnitForm mCurUnitForm = new MCurUnitForm();
      // ��request�����mCurUnitForm 
      RequestUtils.populate(mCurUnitForm, request);
      
      try{
			HttpSession session = request.getSession();
    		Operator operator = null; 
    		if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
    			operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
    		
			boolean result = com.cbrc.smis.adapter.StrutsMCurUnitDelegate.remove(mCurUnitForm);
			/**ɾ���ɹ�*/
            if (result == true) {
            	/** д����־ */
            	String msg = FitechResource.getMessage(locale,resources,
            			"delete.success","curUnit.msg",
            			mCurUnitForm.getCurUnitName());
            	FitechLog.writeLog(Config.LOG_OPERATION,operator.getUserName(),msg);
            	
                messages.add(FitechResource.getMessage(locale, resources,
                        "delete.success", "curUnit.info"));
			}
            /**ɾ��ʧ��*/
            else{ 
            	/** д����־ */
            	String msg = FitechResource.getMessage(locale,resources,
            			"delete.failed","curUnit.msg",
            			mCurUnitForm.getCurUnitName());
            	FitechLog.writeLog(Config.LOG_OPERATION,operator.getUserName(),msg);
            	
                messages.add(FitechResource.getMessage(locale, resources,
                    "delete.failed", "curUnit.info"));
            }
		} 
        catch (Exception e){
			log.printStackTrace(e);
             messages.add(FitechResource.getMessage(locale, resources,
                        "delete.failed", "curUnit.info"));
        }
   
        if(messages.getMessages() != null && messages.getMessages().size() != 0)
               request.setAttribute(Config.MESSAGES,messages);
          
        String path = "/config/ViewCurUnit.do";
/*
        Integer curUnit = mCurUnitForm.getCurUnit();
        String curUnitName = mCurUnitForm.getCurUnitName();

        if (curUnit != null) {
            path += (path.indexOf("?") >= 0 ? "&" : "?");
            path += "curUnit=" + curUnit.toString();
        }
        if (curUnitName != null && !curUnitName.equals("")) {
            path += (path.indexOf("?") >= 0 ? "&" : "?");
            path += "curUnitName=" + curUnitName.toString();
        }*/
        return new ActionForward(path);  
   }
}
