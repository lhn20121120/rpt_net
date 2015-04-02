package com.fitech.net.action;

import java.io.IOException;
import java.util.List;

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

import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.net.adapter.StrutsMRegionDelegate;
import com.fitech.net.form.MRegionForm;

/**
 * @��������Action
 * @author jcm
 *
 */
public final class ViewMRegionAction extends Action {
	private FitechException log=new FitechException(ViewMRegionAction.class);
	
	
   /**
    * @param result ��ѯ���ر�־,����ɹ�����true,���򷵻�false
    * @param MRegionForm 
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
		MRegionForm mRegionForm = (MRegionForm)form ;	   
		RequestUtils.populate(mRegionForm, request);
			    
		int recordCount = 0; // ��¼����
		int offset = 0; // ƫ����
		int limit = 10; // ÿҳ��ʾ�ļ�¼����

		List resList=null;
	   
		ApartPage aPage=new ApartPage();
	   	String strCurPage=request.getParameter("curPage");
		if(strCurPage!=null){
		    if(!strCurPage.equals(""))
		      aPage.setCurPage(new Integer(strCurPage).intValue());
		}
		else
			aPage.setCurPage(1);
		//����ƫ����
		offset=(aPage.getCurPage()-1)*Config.PER_PAGE_ROWS; 
		limit = Config.PER_PAGE_ROWS;	
		
        /***
         * ȡ�õ�ǰ�û���Ȩ����Ϣ
         * 
         */
        HttpSession session = request.getSession();
        Operator operator = null; 
        if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
            operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
        
		try{
	   		//ȡ�ü�¼����
	   		recordCount = StrutsMRegionDelegate.getRecordCount(mRegionForm,operator);
	   			   		
	   		//��ʾ��ҳ��ļ�¼
	   		if(recordCount > 0)
		   	    resList = StrutsMRegionDelegate.select(mRegionForm,offset,limit,operator); 
	   		
	   	}catch (Exception e){
			log.printStackTrace(e);
			messages.add(resources.getMessage("select.mRegion.failed"));		
		}
		//�Ƴ�request��session��Χ�ڵ�����
		//FitechUtil.removeAttribute(mapping,request);
		//��ApartPage��������request��Χ��
	 	aPage.setTerm(this.getTerm(mRegionForm));
	 	aPage.setCount(recordCount);
	 	request.setAttribute(Config.APART_PAGE_OBJECT,aPage);
	 	
	 	 if(messages.getMessages() != null && messages.getMessages().size() > 0)
		   	  request.setAttribute(Config.MESSAGES,messages);
	 	 
	 	 if(resList!=null && resList.size()>0)
	 		 request.setAttribute(Config.RECORDS,resList); 
	 	 
	 	 return mapping.findForward("view");
	}

   
	public String getTerm(MRegionForm mRegionForm){
		String term="";
		Integer mRegionId = mRegionForm.getRegion_id();
		String mRegionName = mRegionForm.getRegion_name();
		
		if(mRegionId != null){
			term += (term.indexOf("") >= 0 ? "" : "&");
			term += "region_id=" + mRegionId.toString();
		}
		if(mRegionName != null && !mRegionName.equals("")){
			term += (term.indexOf("") >= 0 ? "" : "&");
			term += "region_name=" + mRegionName.toString();
		}
	   return term;
	}
}