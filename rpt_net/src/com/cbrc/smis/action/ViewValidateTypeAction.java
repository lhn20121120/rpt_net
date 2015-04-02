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

import com.cbrc.smis.adapter.StrutsValidateTypeDelegate;
import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.ValidateTypeForm;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechUtil;

/**
 * @author ����
 *
 * @ViewValidateType Action  У������������ݵ�Action����
 *
 */
public final class ViewValidateTypeAction extends Action {
	private static FitechException log = new FitechException(ViewValidateTypeAction.class); 

   /**
    *@list ���ݼ���List �������������
    * @exception IOException  IO�쳣
    * @exception ServletException  ServletException�쳣
    */
   public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
	  FitechMessages messages = new FitechMessages();
      MessageResources resources = getResources(request);
	   
      // �Ƿ���Request
      ValidateTypeForm validateTypeForm = (ValidateTypeForm)form;
      //// System.out.println("DataRgDesc==============="+validateTypeForm.getDataRgDesc());
      RequestUtils.populate(validateTypeForm, request);
  	
		
		int recordCount =0; //��¼����
		int offset=0; //ƫ����
		int limit=0;  //ÿҳ��ʾ�ļ�¼����

      //List����ĳ�ʼ��
      List resList=null;
      //
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
		
		try 
		{
	   		//ȡ�ü�¼����
	   		recordCount = StrutsValidateTypeDelegate.getRecordCount(validateTypeForm);
	   		//��ʾ��ҳ��ļ�¼
	   		if(recordCount > 0)
		   	    resList = StrutsValidateTypeDelegate.select(validateTypeForm,offset,limit);  
		}
		catch (Exception e) 
		{
			log.printStackTrace(e);
			messages.add(resources.getMessage("log.select.fail"));		
		}
		//�Ƴ�request��session��Χ�ڵ�����
		//FitechUtil.removeAttribute(mapping,request);
		//��ApartPage��������request��Χ��
	 	aPage.setTerm(this.getTerm(validateTypeForm));
	 	aPage.setCount(recordCount);
	 	request.setAttribute(Config.APART_PAGE_OBJECT,aPage);
		
	 if(messages.getMessages() != null && messages.getMessages().size() > 0)
		   	  request.setAttribute(Config.MESSAGES,messages);
      //���StrutsValidateTypeDelegate���з��ص�reslist����Ϊ�ղ��Ҷ���Ĵ�С����0��
      //�򷵻�һ������reslist���ϵ�request����
      if(resList!=null && resList.size()>0) request.setAttribute(Config.RECORDS,resList);
    //���ص�ҳ��view     
      return mapping.findForward("view");
   }
   
   public String getTerm(ValidateTypeForm validateTypeForm)
   {
	   String term="";
	   
	   Integer validateTypeId = validateTypeForm.getValidateTypeId();
	   String validateTypeName = validateTypeForm.getValidateTypeName();
	   
	   if(validateTypeId!=null)
	   {
		   term += (term.indexOf("")>=0 ? "" : "&");
		   term += "validateTypeId="+validateTypeId.toString();   
	   }
	   if(validateTypeName!=null && !validateTypeName.equals(""))
	   {
		   term += (term.indexOf("")>=0 ? "" : "&");
		   term += "validateTypeName="+validateTypeName.toString();   
	   }
	   return term;
   }
   }