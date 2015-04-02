package com.fitech.net.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.net.adapter.StrutsTargetDelegate;
import com.fitech.net.form.TargetNormalForm;
/**
 * 更新指标任务操作的Action对象
 *
 * @author masclnj

 */
public final class UpdateTargetNormalAction extends Action {
	private static FitechException log = new FitechException(UpdateTargetNormalAction.class);
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
   )throws IOException, ServletException {
	   
		FitechMessages messages=new FitechMessages();
		TargetNormalForm targetNormal = (TargetNormalForm)form;
		String curPage="";
		if(request.getParameter("curPage")!=null) curPage=(String)request.getParameter("curPage");
		boolean updateResult = false;
		
		try {
			if (targetNormal != null) {
				if(StrutsTargetDelegate.isMNormalExist(targetNormal.getNormalName()) == false){
					updateResult = com.fitech.net.adapter.StrutsTargetDelegate.update(targetNormal);
					if (updateResult == true){	//更新成功
						messages.add("更新成功");
					}else{						//更新失败
						messages.add("更新失败");
					}
				}else{
					messages.add("指标业务名称已经存在！");
					updateResult = true;
				}
			}
		} catch (Exception e) {
			log.printStackTrace(e);
		}
		//判断有无提示信息，如有将其存储在Request对象中，返回请求
		if(messages!=null && messages.getSize()>0)
			request.setAttribute(Config.MESSAGES,messages);
		
		String path="";
		if(updateResult==true){	//成功，返回币种列表页
			form = null;
			path = mapping.findForward("update").getPath() +
				"?curPage=" + curPage + 
				"&normalName=";
		}else{					//失败，返回提交页
			path = mapping.getInputForward().getPath();
		}
		path=path==null?mapping.findForward(Config.FORWARD_SYS_ERROR_PAGE).getPath():path;
		
		return new ActionForward(path);
	}
}