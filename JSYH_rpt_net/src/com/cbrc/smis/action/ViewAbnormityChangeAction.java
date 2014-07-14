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

import com.cbrc.smis.adapter.StrutsAbnormityChangeDelegate;
import com.cbrc.smis.adapter.StrutsColAbnormityChangeDelegate;
import com.cbrc.smis.adapter.StrutsMChildReportDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.AbnormityChangeForm;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechUtil;
import com.fitech.net.adapter.StrutsOrgNetDelegate;

/**
 * 异常变化查看
 * @author 姚捷
 * @struts.action
 *    path="/template/viewAbnormityChange"
 *
 * @struts.action-forward
 *    name="ycbh"
 *    path="/template/view/ycbh.jsp"
 *    redirect="false"
 *

 */
public final class ViewAbnormityChangeAction extends Action {
    private static FitechException log = new FitechException(ViewAbnormityChangeAction.class);
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
       MessageResources resources=getResources(request);
       FitechMessages messages = new FitechMessages();
      
      AbnormityChangeForm abnormityChangeForm = new AbnormityChangeForm();
      RequestUtils.populate(abnormityChangeForm,request);
      String curOrgId=request.getParameter("curOrgId");
      String orgId=request.getParameter("orgId");
      List orgCls= null;
      List AbnormityChanges = null;
      try
      {
         // orgCls = StrutsMOrgClDelegate.findAll(); 
          
          String childRepId = abnormityChangeForm.getChildRepId();
          String versionId = abnormityChangeForm.getVersionId();
          
          
          int reportStyle = StrutsMChildReportDelegate.getReportStyle(childRepId,versionId);
          switch(reportStyle)
          {
              /** 处理点对点报表 */
              case 1:  
              {
            	  orgCls=StrutsOrgNetDelegate.selectLowerOrgListInAbnormityChange(orgId,childRepId,versionId);
                  AbnormityChanges = StrutsAbnormityChangeDelegate.select(childRepId,versionId);
                  break;
              }
              /**处理清单式报表*/
              case 2: 
              {
            	  
            	  orgCls=StrutsOrgNetDelegate.selectLowerOrgListInColAbnormityChange(orgId,childRepId,versionId);
            	   AbnormityChanges=  StrutsColAbnormityChangeDelegate.select(childRepId,versionId);
                  break;
              }
              default:
                  messages.add(resources.getMessage("AbnormityChange.select.failed"));
          }
          
          
      }
      catch(Exception e)
      {
          log.printStackTrace(e);
      }
      //移除request或session范围内的属性
      FitechUtil.removeAttribute(mapping,request);
      request.setAttribute("abnormityChangeForm",abnormityChangeForm);
      request.setAttribute("ReportName", abnormityChangeForm.getReportName());
      request.setAttribute("curOrgId",curOrgId);
      if(orgCls!=null && orgCls.size()!=0)
          request.setAttribute("lowerOrgList",orgCls);
      if(AbnormityChanges!=null && AbnormityChanges.size()!=0)
          request.setAttribute(Config.RECORDS,AbnormityChanges);
      return mapping.findForward("ycbh");
   }
}
