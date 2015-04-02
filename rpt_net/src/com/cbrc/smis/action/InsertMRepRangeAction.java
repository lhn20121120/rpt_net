package com.cbrc.smis.action;

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
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.adapter.StrutsMRepRangeDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.MRepRangeForm;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;

/**
 *  ���ݷ�Χ���
 *
 * @author ����
 *
 * @struts.action
 *    path="/struts/insertMRepRange"
 *    name="MRepRangeForm"
 *    scope="session"
 *    validate="false"
 *
 * @struts.action-forward
 *    name="view"
 *    path="/struts/viewMRepRange.do"
 *    redirect="false"
 *

 */
public final class InsertMRepRangeAction extends Action {
	private static FitechException log = new FitechException(InsertMRepRangeAction.class);
   /**
    * Performs action.
    * @param mapping Action mapping.
    * @param form Action form.
    * @param request HTTP request.
    * @param response HTTP response.
    * @exception IOException if an input/output error occurs
    * @exception ServletException if a servlet exception occurs
    */
	   
	   /**
	    *�������ݵ����ݿ�
	   * @exception IOException  �Ƿ���IO�쳣��������׽���׳�
	    * @exception ServletException �Ƿ���Servlet�쳣��������׽���׳�
	    */
	   public ActionForward execute(
	      ActionMapping mapping,
	      ActionForm form,
	      HttpServletRequest request,
	      HttpServletResponse response
	   )	
	      throws IOException, ServletException {
		   

            MessageResources resources = getResources(request);
            FitechMessages messages = new FitechMessages();
            Locale locale = request.getLocale();
           
           boolean insertResult=false;
		   List reclist=null;
		   MRepRangeForm mRepRangeForm = (MRepRangeForm)form;
		   RequestUtils.populate(mRepRangeForm, request);
	     
           try {
	    	  /*if (httpSession.getAttribute("SelectOrgIds")!=null){
	    		  List recList=new ArrayList();
	    		  recList=(List)httpSession.getAttribute("SelectOrgIds");
	    		  if (recList!=null && recList.size()!=0){
	    			  StrutsMRepRange
	    		  }
	    	  }*/
	         if (mRepRangeForm!=null){
	        	insertResult = StrutsMRepRangeDelegate.create(mRepRangeForm);
	        	
	        	if (insertResult!=true) return mapping.findForward("insert");
	         }
	         // ����һ��request���������
	        
	          request.setAttribute(Config.RECORDS, mRepRangeForm);
	    
	         } catch (Exception e) {
	    	   log.printStackTrace( e);
	         }
	         //
	         return mapping.findForward("view");
	      }
	   }

