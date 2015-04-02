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
import com.cbrc.smis.util.FitechUtil;
import com.fitech.net.adapter.StrutsTargetDelegate;
import com.fitech.net.form.TargetNormalForm;

/**
 * ָ��ҵ�����Ͳ鿴
 * @author masclnj
 *
 */
public final class ViewTargetNormalAction extends Action {
	private static FitechException log = new FitechException(ViewTargetNormalAction.class); 

   /**
    *@list ���ݼ���List �������������
    * @exception IOException  IO�쳣
    * @exception ServletException  ServletException�쳣
    */
   public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
	  FitechMessages messages = new FitechMessages();
      MessageResources resources = getResources(request);
	   
      // �Ƿ���Request
      TargetNormalForm targetNormal = (TargetNormalForm)form;
      //// System.out.println("DataRgDesc==============="+mCurrForm.getDataRgDesc());
      RequestUtils.populate(targetNormal, request);
  	
		
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
	   		recordCount = StrutsTargetDelegate.getRecordCount(targetNormal);
	   		//��ʾ��ҳ��ļ�¼
	   		if(recordCount > 0)
		   	    resList = StrutsTargetDelegate.select(targetNormal,offset,limit);  
		}
		catch (Exception e) 
		{
			log.printStackTrace(e);
			messages.add(resources.getMessage("log.select.fail"));		
		}
		//�Ƴ�request��session��Χ�ڵ�����
		FitechUtil.removeAttribute(mapping,request);
		//��ApartPage��������request��Χ��
	 	aPage.setTerm(this.getTerm(targetNormal));
	 	aPage.setCount(recordCount);
	 	request.setAttribute(Config.APART_PAGE_OBJECT,aPage);
		
	 if(messages.getMessages() != null && messages.getMessages().size() > 0)
		   	  request.setAttribute(Config.MESSAGES,messages);
      //���StrutsMCurrDelegate���з��ص�reslist����Ϊ�ղ��Ҷ���Ĵ�С����0��
      //�򷵻�һ������reslist���ϵ�request����
      if(resList!=null && resList.size()>0) request.setAttribute(Config.RECORDS,resList);
    //���ص�ҳ��view     
      return mapping.findForward("view_normal");
   }
   
   public String getTerm(TargetNormalForm targetNormal)
   {
	   String term="";
	   
	   Integer normalId = targetNormal.getNormalId();
	   String normalName = targetNormal.getNormalName();
	   String normalNote = targetNormal.getNormalNote();
	   
	   if(normalId!=null)
	   {
		   term += (term.indexOf("")>=0 ? "" : "&");
		   term += "curId="+normalId.toString();   
	   }
	   if(normalName!=null && !normalName.equals(""))
	   {
		   term += (term.indexOf("")>=0 ? "" : "&");
		   term += "curName="+normalName;   
	   }
	   if(normalNote!=null && !normalNote.equals(""))
	   {
		   term += (term.indexOf("")>=0 ? "" : "&");
		   term += "curName="+normalNote;   
	   }
	   return term;
   }
   }