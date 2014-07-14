package com.fitech.net.action;

import java.io.IOException;
import java.util.List;
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

import com.cbrc.smis.action.DeleteLogInAction;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.net.form.TargetNormalForm;

/**
 * 指标业务删除action的实现
 * 
 * @author masclnj
 * 
 */
public final class DeleteTargetNormalAction extends Action {
    private static FitechException log = new FitechException(
            DeleteLogInAction.class);

    /**
     * @"view" mapping Action
     * @return request request请求
     * @return response respponse请求
     * @exception IOException
     *                是否有IO异常，如有则捕捉并抛出
     * @exception ServletException
     *                是否Servlet异常，如有则捕捉并抛出
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        // mRepFreqForm MCurrForm的初始化
      //  MCurrForm mCurrForm = new MCurrForm();
        TargetNormalForm targetNormal = (TargetNormalForm)form;
        // 将request保存进mCurrForm
        RequestUtils.populate(targetNormal, request);
        
            Locale locale=getLocale(request);
           MessageResources resources=this.getResources(request);
           FitechMessages messages=new FitechMessages();
        try
        {
        	Integer id=targetNormal.getNormalId();
        	List list=com.fitech.net.adapter.StrutsTargetDelegate.selectNormalInfo(id.toString());
        	 boolean result=false;
        	if(list.size()==0)
        		result=true;
        	else{
        		result=false;
        		messages.add("该指标业务已使用");
        	}
        	if(result)
        		result = com.fitech.net.adapter.StrutsTargetDelegate
                    .remove(targetNormal);
           if(result)
            {
                messages.add("删除成功");
            }
           else if(messages.getMessages() == null || messages.getMessages().size() == 0)
               messages.add("删除失败");
        }
        catch (Exception e) 
        {
            log.printStackTrace(e);

            messages.add("删除失败");
        }

        if(messages.getMessages() != null && messages.getMessages().size() != 0)
               request.setAttribute(Config.MESSAGES,messages);
        String path = "/target/viewTargetNormal.do";
/*
        Integer curId = mCurrForm.getCurId();
        String curName = mCurrForm.getCurName();

        if (curId != null) {
            path += (path.indexOf("?") >= 0 ? "&" : "?");
            path += "curId=" + curId.toString();
        }
        if (curName != null && !curName.equals("")) {
            path += (path.indexOf("?") >= 0 ? "&" : "?");
            path += "curName=" + curName.toString();
        }

        // System.out.println("Path= " + path);*/
        return new ActionForward(path);
    }
}
