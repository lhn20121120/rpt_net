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
import com.cbrc.org.form.MOrgClForm;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;



 /**zhangxinke
 *
 */
public final class InsertOrgClsZxkAction extends Action {
    private static FitechException log = new FitechException(InsertOrgClsZxkAction.class); 
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

       MessageResources resources=getResources(request);
       FitechMessages messages = new FitechMessages();
       Locale locale = request.getLocale();
       
       /**取得request范围内的请求参数，并存放在内*/
       MOrgClForm mOrgClForm =(MOrgClForm)form; 
       RequestUtils.populate(mOrgClForm, request);
      
       boolean result = false;
       try 
       {  
    	      	  
    	  int keyEqual = StrutsMOrgClDelegate.getorgClsId(mOrgClForm.getOrgClsId());
    	  if(keyEqual>0)/**如果有增加的机构分类编号已经存在,则不允许增加*/ 
    	  { 
    	     messages.add("该分类编号已经存在");
    		  
    	  }else{
           /**插入记录*/
           result = StrutsMOrgClDelegate.create(mOrgClForm);
       
           if(result==true)
               messages.add("子行分类保存成功！");
           else /**错误则返回原页面*/
           {
               messages.add("子行分类保存失败！");
               request.setAttribute(Config.MESSAGES,messages);
               return mapping.findForward("orgcls_add");
           }
               
       }
    	  }
       catch (Exception e) 
       {
           /**错误则返回原页面*/
          log.printStackTrace(e);
          messages.add(FitechResource.getMessage(locale,resources,"save.failed","dept.info")); 
          request.setAttribute(Config.MESSAGES,messages);
          return mapping.findForward("orgcls_add");
       }
       
       if(messages.getMessages() != null && messages.getMessages().size() > 0)
           request.setAttribute(Config.MESSAGES,messages);
       
       return new ActionForward("/orgcls/orgCls.do");
   }
}
