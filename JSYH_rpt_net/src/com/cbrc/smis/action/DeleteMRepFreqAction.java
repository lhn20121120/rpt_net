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
import com.cbrc.smis.form.MRepFreqForm;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechLog;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
/**
 *�ϱ�Ƶ��ɾ��action��ʵ��
 *
 * @author����
 *
 */
public final class DeleteMRepFreqAction extends Action {
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

      //mRepFreqForm MRepFreqForm�ĳ�ʼ��
      MRepFreqForm mRepFreqForm = new MRepFreqForm();
      
      // ��request�����mRepFreqForm 
      RequestUtils.populate(mRepFreqForm, request);
      
		try {
			HttpSession session = request.getSession(); 		   
			Operator operator = null;    				   
			if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)   						
				operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
			
			boolean result = com.cbrc.smis.adapter.StrutsMRepFreqDelegate
					.remove(mRepFreqForm);
			if (result == true) {
				String msg = FitechResource.getMessage(locale, resources,
                        "delete.success", "repFreq.msg", mRepFreqForm.getRepFreqName());
                FitechLog.writeLog(Config.LOG_OPERATION,operator.getUserName(),msg);

                messages.add(FitechResource.getMessage(locale, resources,
                        "delete.success", "repFreq.info"));
			}
            else{
            	String msg = FitechResource.getMessage(locale, resources,
                        "delete.failed", "repFreq.msg",  mRepFreqForm.getRepFreqName());
            	FitechLog.writeLog(Config.LOG_OPERATION,operator.getUserName(),msg);
            
            	messages.add(FitechResource.getMessage(locale, resources,
                        "delete.failed", "repFreq.info"));
            }
		} 
        catch (Exception e){
			log.printStackTrace(e);
            messages.add(FitechResource.getMessage(locale, resources,
                    "delete.failed", "repFreq.info"));
		}

        if(messages.getMessages() != null && messages.getMessages().size() != 0)
               request.setAttribute(Config.MESSAGES,messages);
        
        String path="/config/ViewCurRepFreqence.do";
	   
	  /* Integer repFreqId = mRepFreqForm.getRepFreqId();
	   String repFreqName = mRepFreqForm.getRepFreqName();
	   
	   if(repFreqId!=null)
	   {
		   path += (path.indexOf("?")>=0 ? "&" : "?");
		   path += "repFreqId="+repFreqId.toString();   
	   }
	   if(repFreqName!=null && !repFreqName.equals(""))
	   {
		   path += (path.indexOf("?")>=0 ? "&" : "?");
		   path += "repFreqName="+repFreqName.toString();   
	   }
	  
	   // System.out.println("Path= "+path);*/
	   return new ActionForward(path);
   }
}
