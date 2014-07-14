package com.cbrc.org.action;

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
import org.apache.struts.util.RequestUtils;

import com.cbrc.org.form.MOrgClForm;
import com.cbrc.smis.util.FitechException;
/**
 * Inserts a MOrgCl.
 *
 * @author ����
 *
 * @struts.action
 *    path="/struts/insertMOrgCl"
 *    name="MOrgClForm"
 *    scope="session"
 *    validate="false"
 *
 * @struts.action-forward
 *    name="view"
 *    path="/struts/viewMOrgCl.do"
 *    redirect="false"
 *

 */
public final class InsertMOrgClAction extends Action {
	private static FitechException log = new FitechException(InsertMOrgClAction.class); 
   /**
    * Performs action.
    * @param mapping Action mapping.
    * @param form Action form.
    * @param request HTTP request.
    * @param response HTTP response.
    * @exception IOException if an input/output error occurs
    * @exception ServletException if a servlet exception occurs
    */
   public ActionForward execute(
      ActionMapping mapping,
      ActionForm form,
      HttpServletRequest request,
      HttpServletResponse response
   )
      throws IOException, ServletException {
	   	//����LISt�Ķ���
	    List list = null;
	    
	    //�Ự����Seesion�õ�request�����session����
	   	HttpSession httpSession=request.getSession();
	   	
	   	//FormBean����mOrgClForm�Ķ��壬��form����ǿ��ת��ΪMOrgClForm
	   	MOrgClForm mOrgClForm = (MOrgClForm)form;
	   	
	   	RequestUtils.populate(mOrgClForm,request);
      try {
    	  if(mOrgClForm!=null){
         list = null; //StrutsMOrgClDelegate.create(mOrgClForm);
         // Set the newly created vo as a request attribute to be picked up
         request.setAttribute("MOrgCl", mOrgClForm);
         return mapping.findForward("view");
      } 
      }catch (Exception e) {
         log.printStackTrace(e);
         request.setAttribute("name", "MOrgCl");
         return mapping.findForward("ejb-create-exception");
      }
      
      return null;
      
   }
}
