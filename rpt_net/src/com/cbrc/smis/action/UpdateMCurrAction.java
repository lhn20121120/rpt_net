package com.cbrc.smis.action;

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

import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.MCurrForm;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
import com.cbrc.smis.util.FitechUtil;
/**
 * 更新币种操作的Action对象
 *
 * @author 唐磊
 *
 * @struts.action
 *    path="/struts/updateMCurr"
 *    name="MCurrForm"
 *    scope=request"
 *    validate="false"
 *
 * @struts.action-forward
 *    name="view"
 *    path="/struts/viewMCurr.do"
 *    redirect="false"
 */
public final class UpdateMCurrAction extends Action {
	private static FitechException log = new FitechException(UpdateMCurrAction.class);   

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
	   
		Locale locale=getLocale(request);	   
		MessageResources resources=this.getResources(request);	   
		FitechMessages messages=new FitechMessages();	   
		MCurrForm mCurrForm = (MCurrForm) form;
		
		String curPage="";
		if(request.getParameter("curPage")!=null) curPage=(String)request.getParameter("curPage");
			
		boolean updateResult = false;
		
		try {
			if (mCurrForm != null) {
				updateResult = com.cbrc.smis.adapter.StrutsMCurrDelegate.update(mCurrForm);
				if (updateResult == true){	//更新成功
					messages.add(FitechResource.getMessage(locale,resources,
							"update.success","curr.info"));
				}else{						//更新失败
					messages.add(FitechResource.getMessage(locale,resources,
							"update.failed","curr.info"));
				}
			}
		} catch (Exception e) {
			log.printStackTrace(e);
		}

		FitechUtil.removeAttribute(mapping,request);
			
		//判断有无提示信息，如有将其存储在Request对象中，返回请求
		if(messages!=null && messages.getSize()>0)
			request.setAttribute(Config.MESSAGES,messages);
		
		String path="";
		if(updateResult==true){	//成功，返回币种列表页
			form = null;
			path = mapping.findForward("update").getPath() + 
				"?curPage=" + curPage + 
				"&curName=";
		}else{					//失败，返回提交页
			path = mapping.getInputForward().getPath();
		}
		
		path=path==null?mapping.findForward(Config.FORWARD_SYS_ERROR_PAGE).getPath():path;
		
		return new ActionForward(path);
	}  
}
