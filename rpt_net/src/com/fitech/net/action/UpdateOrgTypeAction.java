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
import com.fitech.net.adapter.StrutsOrgTypeDelegate;
import com.fitech.net.form.OrgTypeForm;
/**
 * 机构级别更新操作的Action对象
 *
 */
public final class UpdateOrgTypeAction extends Action {
	private static FitechException log = new FitechException(UpdateOrgTypeAction.class);
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
	   	   
		OrgTypeForm orgTypeForm = (OrgTypeForm) form;
		
		String curPage="";
		if(request.getParameter("curPage")!=null) curPage=(String)request.getParameter("curPage");
			
		boolean updateResult = false;
		
		try {
			if (orgTypeForm != null) {
				updateResult = StrutsOrgTypeDelegate.update(orgTypeForm);
				if (updateResult == true){	//更新成功
					messages.add(resources.getMessage("update.orgType.success"));	
				}
                else{
                	messages.add(resources.getMessage("update.orgType.failed"));	
				}
			}
		} 
        catch (Exception e) 
        {
			updateResult=false;
			messages.add(resources.getMessage("update.orgType.failed"));	
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
				"?curPage=" + curPage + "&org_type_name=";
		}else{
			path = mapping.getInputForward().getPath();
		}
		
		path= path==null? mapping.findForward(Config.FORWARD_SYS_ERROR_PAGE).getPath() : path;
		
		return new ActionForward(path);
	}
  }
