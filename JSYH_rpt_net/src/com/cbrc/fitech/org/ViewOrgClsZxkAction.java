package com.cbrc.fitech.org;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.cbrc.org.action.ViewMOrgAction;
import com.cbrc.org.adapter.StrutsMOrgClDelegate;
import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechUtil;

public class ViewOrgClsZxkAction extends Action{
	
	private static FitechException log = new FitechException(ViewMOrgAction.class); 
	
	public ActionForward execute(
		      ActionMapping mapping,
		      ActionForm form,
		      HttpServletRequest request,
		      HttpServletResponse response
		   )throws IOException, ServletException {
		
		MessageResources resources=getResources(request);
	    FitechMessages messages = new FitechMessages();
	    Locale locale = request.getLocale();
		
		//MOrgClForm mOrgClForm=new MOrgClForm();
		int recordCount =0; //��¼����
		int offset=0; //ƫ����
		int limit=0;  //ÿҳ��ʾ�ļ�¼����
		List list = null;
		
		ApartPage aPage=new ApartPage();
	   	String strCurPage=request.getParameter("curPage");
	   	
	   	if(strCurPage!=null && !strCurPage.equals(""))
        {
            aPage.setCurPage(new Integer(strCurPage).intValue());
        }
		else
			aPage.setCurPage(1);
		//����ƫ����
		offset=(aPage.getCurPage()-1)*Config.PER_PAGE_ROWS; 
		limit = Config.PER_PAGE_ROWS;
		 try {
	            // ȡ�ü�¼����
	            recordCount = StrutsMOrgClDelegate.getRecordCount();
	            // System.out.println("List size is" + recordCount);
	            // �����ݿ��ѯ��ҳ�ļ�¼
	            
	            if (recordCount > 0)
	                list= StrutsMOrgClDelegate.select(offset, limit);
	               
	               // mOrgClForm.setPlanList(listFromDB);
	                // System.out.println("List size is" + list);
	        } catch (Exception e) {
	            log.printStackTrace(e);
	            messages.add(resources.getMessage("orgcls.select.fail"));
	        }
//	      �Ƴ�request��session��Χ�ڵ�����
	        FitechUtil.removeAttribute(mapping,request);
	        //��ApartPage��������request��Χ��
	        aPage.setCount(recordCount);
	        request.setAttribute(Config.APART_PAGE_OBJECT,aPage);
	        
	        if(messages.getMessages() != null && messages.getMessages().size() > 0)
	          request.setAttribute(Config.MESSAGES,messages);
	        if(list!=null && list.size()!=0)
	              request.setAttribute(Config.RECORDS,list);
	        
	        return mapping.findForward("success");
	       
	}

}

