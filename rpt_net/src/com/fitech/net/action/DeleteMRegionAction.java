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
 * ��������ɾ��action��ʵ��
 *
 * @author jcm
 *
 */
public final class DeleteMRegionAction extends Action {
	private static FitechException log = new FitechException(DeleteMRegionAction.class);
   /**
    * @"view"  mapping Action
    * @return request request����
    * @return  response respponse����
    * @exception IOException  �Ƿ���IO�쳣��������׽���׳�
    * @exception ServletException �Ƿ���Servlet�쳣��������׽���׳�
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
      
      // ��request�����mRegionForm 
      RequestUtils.populate(mRegionForm, request);
      
      if(mRegionForm!=null && mRegionForm.getRegion_id()!=null){
    	  List list=null;
    	  list=StrutsMRegionDelegate.getLowerRegions(mRegionForm.getRegion_id());
    	  if(list!=null && list.size()>0){
    		  //// System.out.println("org can not delete!!!!!!!!!");
    		  messages.add("��ǰ����������һ���ӵ���������ɾ��!!!");
    		  request.setAttribute(Config.MESSAGES,messages);
    		  return new ActionForward(path);
    	  }
    	  List list1=null;
    	  list1=StrutsOrgNetDelegate.getOrgByRegionId(mRegionForm.getRegion_id());
    	  if(list1!=null && list1.size()>0){
    		  messages.add("��ǰ����������������������������ɾ��!!!");
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
