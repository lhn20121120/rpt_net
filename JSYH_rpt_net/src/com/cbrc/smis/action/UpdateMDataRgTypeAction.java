package com.cbrc.smis.action;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.MDataRgTypeForm;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechLog;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
import com.cbrc.smis.util.FitechUtil;
/**
 * 数据范围类型更新操作的Action对象.
 * @author 唐磊
 *
 * @struts.action
 *    path="/struts/updateMDataRgType"
 *    name="MDataRgTypeForm"
 *    scope="request"
 *    validate="false"
 *
 * @struts.action-forward
 *    name="view"
 *    path="/struts/viewMDataRgType.do"
 *    redirect="false"
 */
public final class UpdateMDataRgTypeAction extends Action {
	private static FitechException log = new FitechException(UpdateMDataRgTypeAction.class);
   
   
	public ActionForward execute(
      ActionMapping mapping,
      ActionForm form,
      HttpServletRequest request,
      HttpServletResponse response   
	)
      throws IOException, ServletException {
	   
		Locale locale=getLocale(request);	   
		MessageResources resources=this.getResources(request);	   
		FitechMessages messages=new FitechMessages();	   	   		
		MDataRgTypeForm mDataRgTypeForm = (MDataRgTypeForm) form;

		String curPage="";
		if(request.getParameter("curPage")!=null) curPage=(String)request.getParameter("curPage");
		boolean updateResult = false;
		
		try{	
			HttpSession session = request.getSession();
			Operator operator = null; 
			if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
				operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
			
			if(mDataRgTypeForm != null){
				updateResult = com.cbrc.smis.adapter.StrutsMDataRgTypeDelegate.update(mDataRgTypeForm);
							   
				if (updateResult == true){
					//更新成功
					messages.add(FitechResource.getMessage(locale,resources,           				   
							"update.success","dataRange.info"));  
					
					String msg = FitechResource.getMessage(locale,resources,
							"update.success","dataRange.msg",mDataRgTypeForm.getDataRgDesc());                    				   
					FitechLog.writeLog(Config.LOG_OPERATION,operator.getUserName(),msg);                                       												   
				}else				
					messages.add(FitechResource.getMessage(locale,resources, 					
							"update.failed","dataRange.info"));					  
			}			  
		}catch (Exception e){
			log.printStackTrace(e);
			messages.add(FitechResource.getMessage(locale,resources,           				   
					"update.failed","dataRange.info"));        	   
		}	   
		FitechUtil.removeAttribute(mapping,request);
		
		//判断有无提示信息，如有将其存储在Request对象中，返回请求			   
		if(messages!=null && messages.getSize()>0)		
			request.setAttribute(Config.MESSAGES,messages);
				
		String path="";		
		if(updateResult==true){	//成功，返回货币单位列表页			
			form = null;			
			path = mapping.findForward("update").getPath() + "?curPage=" + curPage + "&dataRgDesc=";		
		}else{					//失败，返回提交页		
			path = mapping.getInputForward().getPath();
		}				
		path=path==null?mapping.findForward(Config.FORWARD_SYS_ERROR_PAGE).getPath():path;
				
		return new ActionForward(path);	
	}
}
