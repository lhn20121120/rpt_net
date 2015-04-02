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

import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.MRepFreqForm;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechLog;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
import com.cbrc.smis.util.FitechUtil;
/**
 * �ϱ�Ƶ�ȸ��²�����Action����
 * @author ����
 *
 * @struts.action
 *    path="/struts/updateMRepFreq"
 *    name="MRepFreqForm"
 *    scope="request"
 *    validate="false"
 *
 * @struts.action-forward
 *    name="view"
 *    path="/struts/viewMRepFreq.do"
 *    redirect="false"
 */
public final class UpdateMRepFreqAction extends Action {
	private static FitechException log = new FitechException(UpdateMRepFreqAction.class);
 
	/**
	 * @param mapping Action mapping.
	 * @param form Action form.
	 * @param request HTTP request.
	 * @param response HTTP response.
	 * @exception IOException�Ƿ�������/������쳣
	 * @exception ServletException�Ƿ���servlet���쳣ռ��
	 */
	public ActionForward execute(
      ActionMapping mapping,
      ActionForm form,
      HttpServletRequest request,
      HttpServletResponse response
   
	)
      throws IOException, ServletException {
	
		Locale locale=getLocale(request);	   
		MessageResources resources=this.getResources(request);	   
		FitechMessages messages=new FitechMessages();	   
		MRepFreqForm mRepFreqForm = (MRepFreqForm) form;

		String curPage="";	   
		if(request.getParameter("curPage")!=null) curPage=(String)request.getParameter("curPage");					   
		boolean updateResult = false;
	   	  
		try {				
			HttpSession session = request.getSession(); 		   
			Operator operator = null;    				   
			if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)   						
				operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
		   		   
			if (mRepFreqForm != null){			
				updateResult = com.cbrc.smis.adapter.StrutsMRepFreqDelegate.update(mRepFreqForm);							   
				if (updateResult == true){					
					//���³ɹ�				   
					String msg = FitechResource.getMessage(locale,resources,
                           "update.success","repFreq.msg",mRepFreqForm.getRepFreqName());				   
					FitechLog.writeLog(Config.LOG_OPERATION,operator.getUserName(),msg);
					
					messages.add(FitechResource.getMessage(locale,resources,
                                "update.success","repFreq.info"));				
				}else				
					messages.add(FitechResource.getMessage(locale,resources,
							"update.failed","repFreq.info"));		
			}	   
		}catch (Exception e){		
			updateResult=false;           		  
			messages.add(FitechResource.getMessage(locale,resources,           
				   "update.failed","repFreq.msg",mRepFreqForm.getRepFreqName()));					   
			log.printStackTrace(e);			   
		}
	   	  
		FitechUtil.removeAttribute(mapping,request);						   
		//�ж�������ʾ��Ϣ�����н���洢��Request�����У���������			   
		if(messages!=null && messages.getSize()>0)				   		
			request.setAttribute(Config.MESSAGES,messages);
		        			   
		String path="";			   
		if(updateResult==true){	//�ɹ������ػ��ҵ�λ�б�ҳ
			form = null;
			path = mapping.findForward("update").getPath() + 
				"?curPage=" + curPage + 
				"&repFreqName=";		  
		}else{	//ʧ�ܣ������ύҳ		
			path = mapping.getInputForward().getPath();
		}
		path=path==null?mapping.findForward(Config.FORWARD_SYS_ERROR_PAGE).getPath():path;
		
		return new ActionForward(path);
	}  
}
