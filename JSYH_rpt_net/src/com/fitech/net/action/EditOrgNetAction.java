package com.fitech.net.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.smis.util.FitechException;
import com.fitech.net.adapter.StrutsOrgNetDelegate;
import com.fitech.net.form.OrgNetForm;
import com.fitech.net.hibernate.OrgNet;
/**
 * 机构级别更新操作的Action对象
 *
 */
public final class EditOrgNetAction extends Action {
	private static FitechException log = new FitechException(EditOrgNetAction.class);
   /**
    * @param mapping Action mapping.
    * @param form Action form.
    * @param request HTTP request.
    * @param response HTTP response.
    * @exception IOException是否有输入/输出的异常
    * @exception ServletException是否有servlet的异常占用
    */
   public ActionForward execute(
      ActionMapping mapping,
      ActionForm form,
      HttpServletRequest request,
      HttpServletResponse response
   )
      throws IOException, ServletException {
	   HttpSession session=request.getSession();
	   String orgId=request.getParameter("org_id");
	   OrgNet orgNet=null;
	   if(orgId!=null && !orgId.equals("")){
		   orgNet=StrutsOrgNetDelegate.selectOne(orgId);
	   }
	   if(orgNet!=null){
		   OrgNetForm orgNetForm=new OrgNetForm();
		   orgNetForm.setOrg_id(orgNet.getOrgId());
		   orgNetForm=StrutsOrgNetDelegate.selectOne(orgNetForm);
		   request.setAttribute("update","updateOrg");
		   if(request.getAttribute("orgNetForm")!=null){
			   request.removeAttribute("orgNetForm");
			   request.setAttribute("orgNetForm",orgNetForm);
		   }else{
			   request.setAttribute("orgNetForm",orgNetForm);
		   }
	   }
		return mapping.findForward("editOrgNet");
	}
  }
