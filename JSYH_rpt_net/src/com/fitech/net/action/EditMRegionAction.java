package com.fitech.net.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.smis.util.FitechException;
import com.fitech.net.adapter.StrutsMRegionDelegate;
import com.fitech.net.form.MRegionForm;
/**
 * 机构级别更新操作的Action对象
 *
 */
public final class EditMRegionAction extends Action {
	private static FitechException log = new FitechException(EditMRegionAction.class);
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
	   
	   String region_id=request.getParameter("region_id");
	   String curPage=request.getParameter("curPage");
	   
	   MRegionForm mRegionForm=new MRegionForm();
	   if(region_id!=null && !region_id.equals("")){
		   
		   mRegionForm.setRegion_id(region_id);
		   mRegionForm=StrutsMRegionDelegate.selectOne(mRegionForm);
	   }
	   
	   if(request.getAttribute("mRegionForm")!=null){
		   request.removeAttribute("mRegionForm");
		   request.setAttribute("mRegionForm",mRegionForm);
	   }
	   
	   request.setAttribute("mRegionForm",mRegionForm);
	   
	   request.setAttribute("curPage",curPage);
	   
	   
	   String path="/netOrg/mRegion/mRegionEdit.jsp?curPage="+curPage;
	   return new ActionForward(path);
		//return mapping.findForward("editMRegion");
	}
  }
