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
import com.cbrc.smis.form.MRepTypeForm;
import com.cbrc.smis.other.InnerToGather;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechLog;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
import com.cbrc.smis.util.FitechUtil;

/**
 * 上报类型更新操作的Action对象
 *
 * @author 唐磊
 *
 * @struts.action
 *    path="/struts/updateMCurUnit"
 *    name="MCurUnitForm"
 *    scope="request"
 *    validate="false"
 *
 * @struts.action-forward
 *    name="view"
 *    path="/struts/viewMCurUnit.do"
 *    redirect="false"
 *

 */
public final class UpdateMRepTypeAction extends Action {
	private static FitechException log = new FitechException(UpdateMRepTypeAction.class);
   /**
    * Performs action.
    * @param mapping Action mapping.
    * @param form Action form.
    * @param request HTTP请求
    * @param response HTTP请求
    * @exception IOException  是否有IO异常，如有则捕捉并抛出
    * @exception ServletException 是否有Servlet异常，如有则捕捉并抛出
    */
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
	   
	   HttpSession session = request.getSession();
	   
		MRepTypeForm mRepTypeForm = (MRepTypeForm) form;
		
		String curPage="";
		if(request.getParameter("curPage")!=null) curPage=(String)request.getParameter("curPage");
			
		boolean updateResult = false;
		
		try {
			if (mRepTypeForm != null) 
            {
				updateResult = com.cbrc.smis.adapter.StrutsMRepTypeDelegate.update(mRepTypeForm);
				if (updateResult == true)
                {	//更新成功
					//如果更新成功开始同步外网数据库
					boolean repTypeResult=InnerToGather.updateMRepType(mRepTypeForm);
					if(repTypeResult==true)
                    {
						//messages.add(FitechResource.getMessage(locale,resources,"update.success","repType.msg",mRepTypeForm.getRepTypeName()));
                        String msg = FitechResource.getMessage(locale,resources,
                                "update.success","repFreq.msg",mRepTypeForm.getRepTypeName());
                        FitechLog.writeLog(Config.LOG_SYSTEM_CREATESTORAGEXML,Config.SYSTEM_OPERATOR,msg);
                    }
                    else
                    {						//如果内网更新失败，提示出错
						//写入日志
						//messages.add(FitechResource.getMessage(locale,resources,"update.failed","repType.msg",mRepTypeForm.getRepTypeName()));
                        String msg = FitechResource.getMessage(locale,resources,
                                "update.failed","repFreq.msg",mRepTypeForm.getRepTypeName());
                        FitechLog.writeLog(Config.LOG_SYSTEM_CREATESTORAGEXML,Config.SYSTEM_OPERATOR,msg);
                    }
					messages.add(FitechResource.getMessage(locale,resources,"update.success","repFreq.msg"));
				}
                else
                {
					messages.add(FitechResource.getMessage(locale,resources,"update.failed","repFreq.msg"));
				}
			}
		} 
        catch (Exception e) 
        {
			log.printStackTrace(e);
            messages.add(FitechResource.getMessage(locale,resources,"update.failed","repFreq.msg"));
		}
		
		FitechUtil.removeAttribute(mapping,request);
			
		//判断有无提示信息，如有将其存储在Request对象中，返回请求
		if(messages!=null && messages.getSize()>0) request.setAttribute(Config.MESSAGES,messages);		
		        
		String path="";
		if(updateResult==true){	//成功，返回报表类别列表页
			form = null;
			path = mapping.findForward("view").getPath() + 
				"?curPage=" + curPage + 
				"&repTypeName=";
			/*// System.out.println("RepTypeAction PATH===================="+path.toString());*/
		}else{					//失败，返回提交页
			path = mapping.getInputForward().getPath();
		}
		
		path=path==null?mapping.findForward(Config.FORWARD_SYS_ERROR_PAGE).getPath():path;
		
		return new ActionForward(path);
	}
  }