package com.fitech.net.template.action;

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

import com.cbrc.smis.action.ViewMChildReportAction;
import com.cbrc.smis.adapter.StrutsMChildReportDelegate;
import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.MChildReportForm;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechUtil;
import com.fitech.net.common.CommMethod;

public class ViewTemplateNetAction extends Action {
	    private static FitechException log = new FitechException(ViewMChildReportAction.class);

	    /** 
	     * Method execute
	     * @param mapping
	     * @param form
	     * @param request
	     * @param response
	     * @return ActionForward
	     */
	    public ActionForward execute(
	        ActionMapping mapping,
	        ActionForm form,
	        HttpServletRequest request,
	        HttpServletResponse response)  throws IOException, ServletException{
	        

	        MessageResources resources = getResources(request);
	        FitechMessages messages = new FitechMessages();
	        //ȡ��request��Χ�ڵ�����������������logInForm��
	        MChildReportForm mChildReportForm = (MChildReportForm) form;
	        RequestUtils.populate(mChildReportForm, request);
	         
	        int recordCount = 0; // ��¼����
	 		int offset = 0; // ƫ����
	 		int limit = 10; // ÿҳ��ʾ�ļ�¼����
	 	    List list = null;
	 	   
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
	        	 //        	ȡ�ü�¼����
	             recordCount = StrutsMChildReportDelegate.getRecordCount2(mChildReportForm);
	             
	             if(recordCount > 0)
	            	 list = StrutsMChildReportDelegate.select2(mChildReportForm,offset,limit);  
	         }catch(Exception ex){
	        	 log.printStackTrace(ex);
	             messages.add(resources.getMessage("select.template.failed"));  
	         }
	         
	         //�Ƴ�request��session��Χ�ڵ�����
	         FitechUtil.removeAttribute(mapping,request);
	         //��ApartPage��������request��Χ��
	         aPage.setTerm(this.getTerm(mChildReportForm));
	 	 	 aPage.setCount(recordCount);
	         request.setAttribute(Config.APART_PAGE_OBJECT,aPage);
	         
	         if(messages.getMessages() != null && messages.getMessages().size() > 0)
	           request.setAttribute(Config.MESSAGES,messages);
	         if(list!=null && list.size()!=0)
	           request.setAttribute(Config.RECORDS,list);
	         //����ѯ���������request��Χ��
	         request.setAttribute("form",mChildReportForm);
	         String obtianUrl =  CommMethod.getAbsolutePath(request,  "templateConfigurePre.do");
	         request.setAttribute("obtianUrl", obtianUrl);
	         return mapping.findForward("init");
	    }
	    public String getTerm(MChildReportForm mChildReportForm){
			String term="";
			String childRepId = mChildReportForm.getChildRepId();
			String versionId = mChildReportForm.getVersionId();
			
			if(childRepId != null && !childRepId.equals("")){
				term += (term.indexOf("") >= 0 ? "" : "&");
				term += "org_id=" + childRepId;
			}
			if(versionId != null && !versionId.equals("")){
				term += (term.indexOf("") >= 0 ? "" : "&");
				term += "versionId=" + versionId;
			}
		   return term;
		}
	 }
