package com.fitech.net.action;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.net.adapter.StrutsIDataRelationDelegate;
import com.fitech.net.config.Config;
import com.fitech.net.form.IDataRelationForm;

/**
 * 保存数据维护的关系信息
 *
 * @author jcm
 */
public final class SaveProtectTemplateAction extends Action {
    private static FitechException log = new FitechException(ViewProtectTemplateAction.class);
   /**
    * Performs action.
    * @param mapping Action mapping.
    * @param form Action form.
    * @param request HTTP request.
    * @param response HTTP response.
    * @exception IOException if an input/output error occurs
    * @exception ServletException if a servlet exception occurs
    */
   public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		   HttpServletResponse response) throws IOException, ServletException { 
       MessageResources resources = getResources(request);
       FitechMessages messages = new FitechMessages();
       
       String curPage = request.getParameter("curPage") != null ? request.getParameter("curPage") : "";
       IDataRelationForm iDataRelationForm = (IDataRelationForm) form;        
       RequestUtils.populate(iDataRelationForm, request);
       boolean flag=false;
       if(request.getSession().getAttribute(Config.DATA_RELATION_IS_SET)!=null){
    	   HashMap hm=(HashMap)request.getSession().getAttribute(Config.DATA_RELATION_IS_SET);
    	   flag=StrutsIDataRelationDelegate.save(hm);
    	   if(flag){
    		   messages.add("关系设定保存成功！");
    	   }
    	   else{
    		   messages.add("关系设定保存失败！");
    	   }
       }
        
        request.setAttribute(com.cbrc.smis.common.Config.MESSAGES,messages);    
        return new ActionForward("/template/viewProTmpt.do?curPage="+curPage);
   }

}
