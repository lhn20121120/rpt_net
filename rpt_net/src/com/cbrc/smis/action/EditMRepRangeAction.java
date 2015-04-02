package com.cbrc.smis.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.form.MRepRangeForm;

/**
 * 报表范围编辑Action的实现
 *
 * @author 唐磊
 *
 */
public final class EditMRepRangeAction extends Action {

   /**
    * @param mapping Action mapping.
    * @param form Action form.
    * @param request HTTP request.
    * @param response HTTP response.
    * @exception IOException 是否有输入/输出异常，如有捕捉并抛出
    * @exception ServletException 是否有Servlet异常，如有捕捉并抛出
    */
   public ActionForward execute(
      ActionMapping mapping,
      ActionForm form,
      HttpServletRequest request,
      HttpServletResponse response
   )
      throws IOException, ServletException {

      HttpSession session = request.getSession();
      request.setAttribute("action", "update");

      MRepRangeForm MRepRangeForm = new MRepRangeForm();

      // Now set the form members with request values 
      RequestUtils.populate(MRepRangeForm, request);
      try {
         //MRepRangeForm = com.cbrc.smis.adapter.StrutsMRepRangeDelegate.edit(MRepRangeForm);
         session.setAttribute("MRepRangeForm", MRepRangeForm);
      } catch (Exception e) {
         getServlet().log("Create error", e);
         request.setAttribute("name", "MRepRange");
         return mapping.findForward("ejb-finder-exception");
      }

      return mapping.findForward("form");
   }
   
   /**
    * Get the named value as a string from the request parameter, or from the
    * request obj if not found.
    * 
    * @param req The request object.
    * @param name The name of the parameter.
    *
    * @return The value of the parameter as a String.
    */
   private String getParameter(HttpServletRequest req, String name) {
      String value = req.getParameter(name);
      if (value == null) {
         value = req.getAttribute(name).toString();
      }
      return value;
   }
}
