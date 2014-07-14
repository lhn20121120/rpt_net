package com.fitech.net.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.net.adapter.StrutsACompareLogDelegate;
import com.fitech.net.form.ACompareLogFrom;

/**
 *描述：
 *日期：2007-12-12
 *作者：曹发根
 */
public class InsertACompareLogAction   extends Action {
	private FitechException log=new FitechException(SelectACompareLogAction.class);
	
	
	   /**
	    * @param result 
	    * @param CollectTypeForm 
	    * @param request 
	    * @exception Exception 有异常捕捉并抛出
	    */
		public ActionForward execute(
	      ActionMapping mapping,
	      ActionForm form,
	      HttpServletRequest request,
	      HttpServletResponse response
		)throws IOException, ServletException {

			FitechMessages messages = new FitechMessages();
		   String repInIds=request.getParameter("repInIds");
			String curPage=request.getParameter("curPage");
		   request.setAttribute("curPage", curPage);
			// 是否有Request
			ACompareLogFrom aCompareLogFrom = (ACompareLogFrom)form ;	   
			RequestUtils.populate(aCompareLogFrom, request);
			if(repInIds!=null)StrutsACompareLogDelegate.compare(repInIds,new Integer(1));
			
		 	 
		 	 return mapping.findForward("view");
		}

}