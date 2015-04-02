package com.cbrc.smis.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.adapter.StrutsMActuRepDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.MActuRepForm;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechUtil;

/**
 * 数据范围报送频度查询
 * @author 姚捷
 *
 * @struts.action
 *    path="/template/viewMActuRept"
 *
 * @struts.action-forward
 *    name="bspl"
 *    path="/template/view/bspl.jsp"
 *    redirect="false"
 *

 */
public final class ViewMActuRepAction extends Action {
    private static FitechException log = new FitechException(ViewMActuRepAction.class);
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
   throws IOException, ServletException 
   {

       MessageResources resources = getResources(request);
       FitechMessages messages = new FitechMessages();
       
       MActuRepForm mActuRepForm = new MActuRepForm();
       RequestUtils.populate(mActuRepForm,request);
       
       List list = null;
       
       try 
       {
           String childRepId = mActuRepForm.getChildRepId();
           String versionId = mActuRepForm.getVersionId();
           list = StrutsMActuRepDelegate.select(childRepId,versionId);  
       }
       catch (Exception e) 
       {
           log.printStackTrace(e);
           messages.add(resources.getMessage("log.select.fail"));      
       }
        //移除request或session范围内的属性
        FitechUtil.removeAttribute(mapping,request);
        
        if(messages.getMessages() != null && messages.getMessages().size() > 0)
          request.setAttribute(Config.MESSAGES,messages);
        	request.setAttribute("ReportName",mActuRepForm.getReportName());
     //   request.setAttribute("ReportName",new String(mActuRepForm.getReportName().getBytes("8859_1"),"GBK"));
        
        if(list!=null && list.size()!=0) request.setAttribute(Config.RECORDS,list);
        
        return mapping.findForward("bspl");  
  
   }

}
