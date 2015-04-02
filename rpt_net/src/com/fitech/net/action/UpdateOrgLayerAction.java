package com.fitech.net.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechUtil;
import com.fitech.net.adapter.StrutsOrgLayerDelegate;
import com.fitech.net.form.OrgLayerForm;
/**
 * 机构级别更新操作的Action对象
 *
 */
public final class UpdateOrgLayerAction extends Action {
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
	   
	   MessageResources resources=this.getResources(request);
	   FitechMessages messages=new FitechMessages();
	   	   
		OrgLayerForm orgLayerForm = (OrgLayerForm) form;
		
		String curPage="";
		if(request.getParameter("curPage")!=null) curPage=(String)request.getParameter("curPage");
			
		boolean updateResult = false;
		
		try {
			if (orgLayerForm != null) {
				updateResult = StrutsOrgLayerDelegate.update(orgLayerForm);
				if (updateResult == true){	//更新成功
					messages.add(resources.getMessage("update.orgLayer.success"));	
				}
                else{
                	messages.add(resources.getMessage("update.orgLayer.failed"));	
				}
			}
		} 
        catch (Exception e) 
        {
			updateResult=false;
			messages.add(resources.getMessage("update.orgLayer.failed"));	
			log.printStackTrace(e);
		}

		FitechUtil.removeAttribute(mapping,request);
			
		//判断有无提示信息，如有将其存储在Request对象中，返回请求
		if(messages!=null && messages.getSize()>0)
			request.setAttribute(Config.MESSAGES,messages);
		        
		String path="";
		if(updateResult==true){
			form = null;
			path = mapping.findForward("update").getPath() + 
				"?curPage=" + curPage + "&org_layer_name=";
		}else{
			path = mapping.getInputForward().getPath();
		}
		
		path= path==null? mapping.findForward(Config.FORWARD_SYS_ERROR_PAGE).getPath() : path;
		
		return new ActionForward(path);
	}
  }
