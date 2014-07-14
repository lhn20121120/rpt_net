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
import com.fitech.net.adapter.StrutsCollectTypeDelegate;
import com.fitech.net.form.CollectTypeForm;

/**
 * @ ���ܷ�ʽ��ѯAction
 * @ author jcm
 *
 */
public final class ViewCollectTypeAction extends Action {
	private FitechException log=new FitechException(ViewCollectTypeAction.class);
	
	
   /**
    * @param result ��ѯ���ر�־,����ɹ�����true,���򷵻�false
    * @param CollectTypeForm 
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
		CollectTypeForm collectTypeForm = (CollectTypeForm)form ;	   
		RequestUtils.populate(collectTypeForm, request);
			    
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
		
		try{
			if(collectTypeForm != null && collectTypeForm.getOrgId() != null){
				if(collectTypeForm.getOrgId().equals("0")) collectTypeForm.setOrgId(null);
			}
	   		//ȡ�ü�¼����
	   		recordCount = StrutsCollectTypeDelegate.select(collectTypeForm);
	   			   		
	   		//��ʾ��ҳ��ļ�¼
	   		if(recordCount > 0)
		   	    resList = StrutsCollectTypeDelegate.select(collectTypeForm,offset,limit); 
	   		
	   	}catch (Exception e){
			log.printStackTrace(e);
			messages.add(resources.getMessage("select.collectType.failed"));		
		}
		//�Ƴ�request��session��Χ�ڵ�����
		//FitechUtil.removeAttribute(mapping,request);
		//��ApartPage��������request��Χ��
	 	aPage.setTerm(this.getTerm(collectTypeForm));	 	
	 	aPage.setCount(recordCount);
	 	request.setAttribute(Config.APART_PAGE_OBJECT,aPage);
	 	
	 	 if(messages.getMessages() != null && messages.getMessages().size() > 0)
		   	  request.setAttribute(Config.MESSAGES,messages);
	 	 
	 	 if(resList!=null && resList.size()>0)
	 		 request.setAttribute(Config.RECORDS,resList); 
	 	 
	 	 return mapping.findForward("view");
	}

   
	public String getTerm(CollectTypeForm collectTypeForm){
		String term="";
		String orgId = collectTypeForm.getOrgId();
		String collectName = collectTypeForm.getCollectName();
		
		if(orgId != null){
			term += (term.indexOf("") >= 0 ? "" : "&");
			term += "orgId=" + orgId;
		}
		if(collectName != null && !collectName.equals("")){
			term += (term.indexOf("") >= 0 ? "" : "&");
			term += "collectName=" + collectName;
		}
	   return term;
	}
}