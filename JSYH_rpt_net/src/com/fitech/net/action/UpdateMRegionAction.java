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
import org.apache.struts.util.MessageResources;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.net.adapter.StrutsMRegionDelegate;
import com.fitech.net.adapter.StrutsOrgTypeDelegate;
import com.fitech.net.form.MRegionForm;
/**
 * 机构级别更新操作的Action对象
 *
 */
public final class UpdateMRegionAction extends Action {
	private static FitechException log = new FitechException(UpdateOrgLayerAction.class);
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
	   String curPage=(String)session.getAttribute("curPage");
	   session.removeAttribute("curPage");
	   // System.out.println("in updateMRegionAction curPage is "+curPage);
	   FitechMessages messages = new FitechMessages();
	   MessageResources resources = getResources(request);
	   MRegionForm mRegionForm=(MRegionForm)form;
	   Operator operator = (Operator)request.getSession().getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
	   if(mRegionForm!=null){
			try {
				if(mRegionForm.getSetOrgId() == null) mRegionForm.setSetOrgId(operator.getOrgId());
				mRegionForm.setOrg_type_id(StrutsOrgTypeDelegate.findMaxOrgTyp().getOrgTypeId());
				boolean result=StrutsMRegionDelegate.update(mRegionForm);
				if (result == true)
					messages.add(resources.getMessage("update.mRegion.success"));	
	            else
	            	messages.add(resources.getMessage("update.mRegion.failed"));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.printStackTrace(e);
				messages.add(resources.getMessage("update.mRegion.failed"));
			}
	   }
	   if(messages.getMessages() != null && messages.getMessages().size() > 0)
		   	  request.setAttribute(Config.MESSAGES,messages);
	   String path="";
	   if(curPage!=null)
		   path = "/viewMRegion.do?curPage="+curPage+"&region_name=";
	   else
		   path="/viewMRegion.do?region_name=";
	   return new ActionForward(path);
	}
  }
