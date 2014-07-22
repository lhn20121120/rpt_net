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
import com.cbrc.smis.form.MDataRgTypeForm;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechLog;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
/**
 *���ݷ�Χɾ��action��ʵ��
 *
 * @author����
 *
 */
public final class DeleteMDataRgTypeAction extends Action {
	private static FitechException log = new FitechException(DeleteLogInAction.class);
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
   )
   throws IOException, ServletException{

	   Locale locale=getLocale(request);
	   MessageResources resources=this.getResources(request);
	   FitechMessages messages=new FitechMessages();
	   
      //mDataRgTypeForm MDataRgTypeForm�ĳ�ʼ��
      MDataRgTypeForm mDataRgTypeForm = new MDataRgTypeForm();
      
      // ��request�����mDataRgTypeForm 
      RequestUtils.populate(mDataRgTypeForm, request);
      

		try {
			HttpSession session = request.getSession();
    		Operator operator = null; 
    		if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
    			operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
    		
			boolean result = com.cbrc.smis.adapter.StrutsMDataRgTypeDelegate
					.remove(mDataRgTypeForm);
			if (result == true) {
				/**д����־*/
            	String msg = FitechResource.getMessage(locale,resources,
            			"delete.success","dataRange.msg",
						mDataRgTypeForm.getDataRgDesc());
            	FitechLog.writeLog(Config.LOG_OPERATION,operator.getUserName(),msg);
            	
            	messages.add(FitechResource.getMessage(locale, resources,
                            "delete.success", "dataRange.info"));
			}
            else{
            	/**д����־*/
            	String msg = FitechResource.getMessage(locale,resources,
            			"delete.failed","dataRange.msg",
						mDataRgTypeForm.getDataRgDesc());
            	FitechLog.writeLog(Config.LOG_OPERATION,operator.getUserName(),msg);
            	
                messages.add(FitechResource.getMessage(locale, resources,
                        "delete.failed","dataRange.info"));
            }
		}
        catch (Exception e) {
			log.printStackTrace(e);
             messages.add(FitechResource.getMessage(locale, resources,
                     "delete.failed", "repFreq.msg", mDataRgTypeForm.getDataRgDesc()));
        }
        if(messages.getMessages() != null && messages.getMessages().size() != 0)
            request.setAttribute(Config.MESSAGES,messages);
       
		String path="/config/ViewDataRangeType.do";
	   
		return new ActionForward(path);
   }
}