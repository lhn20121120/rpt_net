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
 * 指标业务类型插入一条记录的Action对象
 * 
 * @author masclnj
 *  
 */
public final class TargetNormalAddAction extends Action {
    private static FitechException log = new FitechException(
            TargetNormalAddAction.class);

    /**
     * 插入数据到数据库
     * 
     * @exception IOException
     *                是否有IO异常，如有则捕捉并抛出
     * @exception ServletException
     *                是否有Servlet异常，如有则捕捉并抛出
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws ServletException {

        FitechMessages messages = new FitechMessages();
        boolean insertResult = false;

        TargetNormalForm targetNormalForm = (TargetNormalForm) form;
        try {
            if (targetNormalForm != null) {
            	if(StrutsTargetDelegate.isMNormalExist(targetNormalForm.getNormalName()) == false){
            		insertResult = StrutsTargetDelegate.create(targetNormalForm);
            
            		if (insertResult != true) {
            			messages.add("新增指标业务失败");
            		} else
            			messages.add("新增指标业务成功");
            	}else
            		messages.add("指标业务名称已经存在！");
            }
        } catch (Exception e) {
            log.printStackTrace(e);

            messages.add("新增指标业务失败");
        }
        if (messages.getMessages() != null
                && messages.getMessages().size() != 0)
            request.setAttribute(Config.MESSAGES, messages);
        // 新增失败
        if (insertResult == false) {
            return mapping.findForward("bad_add");
        }
        String path = "/target/viewTargetNormal.do";
        return new ActionForward(path);
    }
}
