package com.fitech.net.action;

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

import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.net.adapter.StrutsMRegionDelegate;
import com.fitech.net.adapter.StrutsOrgNetDelegate;
import com.fitech.net.form.MRegionForm;
/**
 * 机构地区删除action的实现
 *
 * @author jcm
 *
 */
public final class DeleteMRegionAction extends Action {
	private static FitechException log = new FitechException(DeleteMRegionAction.class);
   /**
    * @"view"  mapping Action
    * @return request request请求
    * @return  response respponse请求
    * @exception IOException  是否有IO异常，如有则捕捉并抛出
    * @exception ServletException 是否有Servlet异常，如有则捕捉并抛出
    */
   public ActionForward execute(
      ActionMapping mapping,
      ActionForm form,
      HttpServletRequest request,
      HttpServletResponse response
   )
   throws IOException, ServletException{
	   
	   
	   String curPage=request.getParameter("curPage");
	   String path="/viewMRegion.do?curPage="+curPage;
	   MessageResources resources=this.getResources(request);
	   FitechMessages messages=new FitechMessages();

      MRegionForm mRegionForm = new MRegionForm();
      
      // 将request保存进mRegionForm 
      RequestUtils.populate(mRegionForm, request);
      
      if(mRegionForm!=null && mRegionForm.getRegion_id()!=null){
    	  List list=null;
    	  list=StrutsMRegionDelegate.getLowerRegions(mRegionForm.getRegion_id());
    	  if(list!=null && list.size()>0){
    		  //// System.out.println("org can not delete!!!!!!!!!");
    		  messages.add("当前地区存在下一级子地区，不能删除!!!");
    		  request.setAttribute(Config.MESSAGES,messages);
    		  return new ActionForward(path);
    	  }
    	  List list1=null;
    	  list1=StrutsOrgNetDelegate.getOrgByRegionId(mRegionForm.getRegion_id());
    	  if(list1!=null && list1.size()>0){
    		  messages.add("当前地区有其他机构和它关联，不能删除!!!");
    		  request.setAttribute(Config.MESSAGES,messages);
    		  return new ActionForward(path);
    	  }
    	  
      }
      
		try 
        {
			boolean result = StrutsMRegionDelegate.remove(mRegionForm);
			if (result == true)
				messages.add(resources.getMessage("delete.mRegion.success"));	
            else
            	messages.add(resources.getMessage("delete.mRegion.failed"));	
		} 
        catch (Exception e){
			log.printStackTrace(e);
			messages.add(resources.getMessage("delete.mRegion.failed"));	
		}

        if(messages.getMessages() != null && messages.getMessages().size() != 0)
               request.setAttribute(Config.MESSAGES,messages);
        
        request.setAttribute("curPage",curPage);

	   return new ActionForward(path);
 }

}
