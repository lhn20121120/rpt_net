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
		int recordCount =0; //记录总数
		int offset=0; //偏移量
		int limit=0;  //每页显示的记录条数
		List list = null;
		
		ApartPage aPage=new ApartPage();
	   	String strCurPage=request.getParameter("curPage");
	   	
	   	if(strCurPage!=null && !strCurPage.equals(""))
        {
            aPage.setCurPage(new Integer(strCurPage).intValue());
        }
		else
			aPage.setCurPage(1);
		//计算偏移量
		offset=(aPage.getCurPage()-1)*Config.PER_PAGE_ROWS; 
		limit = Config.PER_PAGE_ROWS;
		 try {
	            // 取得记录总数
	            recordCount = StrutsMOrgClDelegate.getRecordCount();
	            // System.out.println("List size is" + recordCount);
	            // 从数据库查询该页的记录
	            
	            if (recordCount > 0)
	                list= StrutsMOrgClDelegate.select(offset, limit);
	               
	               // mOrgClForm.setPlanList(listFromDB);
	                // System.out.println("List size is" + list);
	        } catch (Exception e) {
	            log.printStackTrace(e);
	            messages.add(resources.getMessage("orgcls.select.fail"));
	        }
//	      移除request或session范围内的属性
	        FitechUtil.removeAttribute(mapping,request);
	        //把ApartPage对象存放在request范围内
	        aPage.setCount(recordCount);
	        request.setAttribute(Config.APART_PAGE_OBJECT,aPage);
	        
	        if(messages.getMessages() != null && messages.getMessages().size() > 0)
	          request.setAttribute(Config.MESSAGES,messages);
	        if(list!=null && list.size()!=0)
	              request.setAttribute(Config.RECORDS,list);
	        
	        return mapping.findForward("success");
	       
	}

}

