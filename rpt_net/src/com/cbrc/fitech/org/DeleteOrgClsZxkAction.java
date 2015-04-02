package com.cbrc.fitech.org;

import java.io.IOException;
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

import com.cbrc.org.adapter.StrutsMOrgClDelegate;
import com.cbrc.org.adapter.StrutsMOrgDelegate;
import com.cbrc.org.form.MOrgClForm;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;


/**
 * 子行分类信息删除
 *
 * @author zhangxinke
 *
 *
 */
public final class DeleteOrgClsZxkAction extends Action{
    private static FitechException log = new FitechException(DeleteOrgClsZxkAction.class);
    
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
   )throws IOException, ServletException 
   {
       MessageResources resources=getResources(request);
       FitechMessages messages = new FitechMessages();
       Locale locale = request.getLocale();
   
       /**取得request范围内的请求参数，并存放在内*/
       MOrgClForm mOrgClForm = new MOrgClForm(); 
       RequestUtils.populate(mOrgClForm, request); 
       
       // System.out.println(mOrgClForm.getOrgClsId());
       
      // int orgclsId = Integer.parseInt(mOrgClForm.getOrgClsId());
      
     int orghaveorgClId = StrutsMOrgDelegate.getMOrgFromorgClsId(mOrgClForm.getOrgClsId());
       
       /**查看当前是否有用户属于该子行*/
       
      if(orghaveorgClId>0)/**如果要删除的orgclsId和MORG表中的orgclsId相等,则不允许删除*/ 
       messages.add("不能删除该分类类型！");
      else/**反之*/
       {
           try 
           {
           
               boolean result = StrutsMOrgClDelegate.remove2(mOrgClForm);
               if(result==false)/**删除失败*/
                    messages.add("删除子行分类失败！");
               else/**删除成功*/
                   messages.add("删除子行分类成功！");  
           } 
           catch (Exception e) 
           {
               log.printStackTrace(e);
               messages.add("删除子行分类失败！");
           }
       } 
      
       if(messages.getMessages() != null && messages.getMessages().size() != 0)
           request.setAttribute(Config.MESSAGES,messages);
       return new ActionForward("/orgcls/orgCls.do");
   }
    
   
  
   
   
}



