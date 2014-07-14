package com.fitech.net.action;

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

import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.net.adapter.StrutsVParameterDelegate;
import com.fitech.net.form.VParameterForm;

/**
 * @������鿴Action
 * @author James
 *
 */
public final class ViewVParameterAction extends Action {
	private FitechException log=new FitechException(ViewVParameterAction.class);
	
	
   /**
    * @param result ��ѯ���ر�־,����ɹ�����true,���򷵻�false
    * @param OrgTypeForm 
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
		VParameterForm vParamForm = (VParameterForm)form ;	   
		RequestUtils.populate(vParamForm, request);
		
		int recordCount = 0; // ��¼����
		int offset = 0; // ƫ����
		int limit = 10; // ÿҳ��ʾ�ļ�¼����

		List resList = null;
	   
		ApartPage aPage = new ApartPage();
	   	String strCurPage = request.getParameter("curPage");
		if(strCurPage != null){
		    if(!strCurPage.equals(""))
		      aPage.setCurPage(new Integer(strCurPage).intValue());
		}
		else
			aPage.setCurPage(1);
		//����ƫ����
		offset=(aPage.getCurPage()-1)*Config.PER_PAGE_ROWS; 
		limit = Config.PER_PAGE_ROWS;	
//		
//        /***
//         * ȡ�õ�ǰ�û���Ȩ����Ϣ
//         * 
//         */
//        HttpSession session = request.getSession();
//        Operator operator = null; 
//        if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
//            operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
        
		try{
	   		//ȡ�ü�¼����
			recordCount = StrutsVParameterDelegate.getRecordCount(vParamForm);
	   			   		
	   		//��ʾ��ҳ��ļ�¼
	   		if(recordCount > 0)
		   	    resList = StrutsVParameterDelegate.select(vParamForm,offset,limit); 
	   		
	   	}catch (Exception e){
			log.printStackTrace(e);
			messages.add(resources.getMessage("select.vParamForm.failed"));		
		}
		//�Ƴ�request��session��Χ�ڵ�����
		//FitechUtil.removeAttribute(mapping,request);
		//��ApartPage��������request��Χ��
	   	
	 	aPage.setTerm(this.getTerm(vParamForm));
	 	aPage.setCount(recordCount);
	 	request.setAttribute(Config.APART_PAGE_OBJECT, aPage);
	 	
	 	 if(messages.getMessages() != null && messages.getMessages().size() > 0)
		   	  request.setAttribute(Config.MESSAGES,messages);
	 	 
	 	 if(resList!=null && resList.size()>0)
	 		 request.setAttribute(Config.RECORDS,resList);
	 	 
	 	 return mapping.findForward("view");
	}

   
	public String getTerm(VParameterForm vParamForm){
		String term="";
		
		if (vParamForm != null && vParamForm.getVpId() != null) {
			Integer vpId = vParamForm.getVpId();
			String vpNote = vParamForm.getVpNote();			
			if(vpId != null){
				term += (term.indexOf("") >= 0 ? "" : "&");
				term += "vpId=" + vpId.toString();
			}
//			if(vpNote != null && !vpNote.equals("")){
//				term += (term.indexOf("") >= 0 ? "" : "&");
//				term += "vpNote=" + vpNote;
//			}
		}
	   return term;
	}
}