package com.cbrc.smis.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechUtil;
import com.fitech.dataCollect.Util;

/**
 * ���ػ�������Action
 * @author gen
 *
 */
public final class ViewCollectDownAction extends Action {
	private FitechException log=new FitechException(ViewCollectDownAction.class);
	
	
   /**
    * @param result ��ѯ���ر�־,����ɹ�����true,���򷵻�false
    * @param ReportInForm 
    * @param request 
    * @exception Exception ���쳣��׽���׳�
    */
	public ActionForward execute(
      ActionMapping mapping,
      ActionForm form,
      HttpServletRequest request,
      HttpServletResponse response
	)throws IOException, ServletException {

		FitechMessages messages = new FitechMessages();
		MessageResources resources = getResources(request);
	   
		// �Ƿ���Request
		ReportInForm reportInForm = (ReportInForm)form ;	   
		RequestUtils.populate(reportInForm, request);
			    


		List excelList=null;
		

		int mon=Integer.parseInt(reportInForm.getSetDate());
		
      
		try{
			excelList = Util.getExcels(reportInForm.getYear().toString(),reportInForm.getSetDate());
	   	}catch (Exception e){
			log.printStackTrace(e);
			messages.add("�����������ʧ�ܣ�");		
		}
		//�Ƴ�request��session��Χ�ڵ�����
		FitechUtil.removeAttribute(mapping,request);

	 	
	 	 if(messages.getMessages() != null && messages.getMessages().size() > 0)
		   	  request.setAttribute(Config.MESSAGES,messages);
	 	 
	 	 if(excelList!=null && excelList.size()>0)
	 		 request.setAttribute(Config.RECORDS,excelList);
	 	 else
	 		 request.setAttribute(Config.RECORDS,null);     
	 	 request.setAttribute("year",reportInForm.getYear());
	 	 request.setAttribute("mon",reportInForm.getSetDate());
	 	 
	 	 return mapping.findForward("view");
	}

   


	
}