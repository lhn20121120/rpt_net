package com.fitech.net.action;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechUtil;
import com.fitech.net.adapter.StrutsTargetDelegate;
import com.fitech.net.form.TargetBusinessForm;
import com.fitech.net.hibernate.MBusiness;
/**
 * 更新指标任务操作的Action对象
 *
 * @author masclnj

 */
public final class UpdateTargetBusinessAction extends Action {
	private static FitechException log = new FitechException(UpdateTargetBusinessAction.class);
    
    /**
     * 日志记录器
     */
    private static Logger logger = Logger.getLogger(UpdateTargetBusinessAction.class);
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
	   Locale locale=getLocale(request);
	   MessageResources resources=this.getResources(request);
	   FitechMessages messages=new FitechMessages();
	   
	   HttpSession session = request.getSession();
	   
	   TargetBusinessForm targetBusiness = (TargetBusinessForm)form;
		
		String curPage="";
		if(request.getParameter("curPage")!=null) curPage=(String)request.getParameter("curPage");
			
		boolean updateResult = false;
		
		try {
            //----------------------------------------------------------------------
            // 2008-06-19 gongming 判断在数据库内是否有相同名称的指标类型存在
            // 相同不允许更新
            if(logger.isInfoEnabled())
                logger.info(" begin juge BusinessName isEquals with db.......");
            boolean equals = false;
            String tarName = targetBusiness.getBusinessName().trim();
            String tarId   = targetBusiness.getBusinessId().toString();
            if(logger.isInfoEnabled())
                logger.info(" get parametar tarName = [" + tarName +"] tarId = [" + tarId + "]");
            if(StringUtils.isNotEmpty(tarName) && StringUtils.isNotEmpty(tarId))
            {
                if(logger.isInfoEnabled())
                    logger.info("query all targetBusiness.........");
                List tarLst = StrutsTargetDelegate.findAll();
                if(tarLst != null && !tarLst.isEmpty())
                {
                    for (Iterator iter = tarLst.iterator(); iter.hasNext();)
                    {
                        MBusiness elmt = (MBusiness) iter.next();
                        if(tarName.equals(elmt.getBusinessName().trim())
                                && !tarId.equals(elmt.getBusinessId().toString()))
                        {
                            if(logger.isInfoEnabled())
                                logger.info("find equals bussinessName .......");
                            equals = true;
                            break;
                        }                    
                    }
                    if(!equals)
                    {                   
                        if (targetBusiness != null) {
                            if(logger.isInfoEnabled())
                                logger.info("begin update targetBusiness");
                            updateResult = StrutsTargetDelegate.update(targetBusiness);                        
                            if (updateResult == true){  //更新成功
                                if(logger.isInfoEnabled())
                                    logger.info("update targetBusiness success.......");
                                messages.add("更新成功");
                            }else{ 
                                if(logger.isInfoEnabled())
                                    logger.info("update targetBusiness faile........");
                                messages.add("更新失败");
                            }
                        }
                    }
                    else
                    {
                        messages.add("已有相同名称指标存在");
                    }
                    tarLst.clear();
                }
                else
                {
                    messages.add("程序发生异常，请与程序员联系或重试");
                }
            }
            else
            {
                messages.add("指标类型参数未获取");
            }
            
            //-------------------------------------------------------------------------------
//            if (targetBusiness != null) {
//                updateResult = com.fitech.net.adapter.StrutsTargetDelegate.update(targetBusiness);
//                if (updateResult == true){  //更新成功
//                    messages.add("更新成功");
//                }else{                      //更新失败
//                    messages.add("更新失败");
//                }
//            }
			
		} catch (Exception e) {
			log.printStackTrace(e);
		}

//		FitechUtil.removeAttribute(mapping,request);
       
		//判断有无提示信息，如有将其存储在Request对象中，返回请求
		if(messages!=null && messages.getSize()>0)
			request.setAttribute(Config.MESSAGES,messages);
		
		String path="";
		if(updateResult==true){	//成功，返回币种列表页
			form = null;
			path = mapping.findForward("update").getPath() +
				"?curPage=" + curPage + 
				"&businessName=";
		}else{					//失败，返回提交页
			//path = mapping.getInputForward().getPath();
            
            return mapping.getInputForward();
		}
		
		path=path==null?mapping.findForward(Config.FORWARD_SYS_ERROR_PAGE).getPath():path;
		
		return new ActionForward(path);
	}
  }
