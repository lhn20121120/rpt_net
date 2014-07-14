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
 * 机构级别插入一条记录的Action对象
 * 
 * @author jcm 
 * 
 */
public final class InsertOrgTypeAction extends Action {
    private static FitechException log = new FitechException(
    		InsertOrgTypeAction.class);

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
            throws IOException, ServletException {

        int insertResult = 0;
        MessageResources resources = getResources(request);

        FitechMessages messages = new FitechMessages();

        OrgTypeForm orgTypeForm = (OrgTypeForm) form;
        try {
            if (orgTypeForm != null)
                insertResult = StrutsOrgTypeDelegate.create(orgTypeForm);
            if(insertResult == 2){ //该级别的机构类别已经存在
            	messages.add(resources.getMessage("update.orgType.exist"));
            }
        } 
        catch (Exception e){
            insertResult = 0;
            messages.add(resources.getMessage("insert.orgType.failed"));		
            log.printStackTrace(e);
        }

        if (messages != null && messages.getSize() > 0)
            request.setAttribute(Config.MESSAGES, messages);

        // 新增失败
        if (insertResult == 0 || insertResult == 2) { //操作失败或该级别的机构类别存在的话返回新增页面
            return mapping.findForward("insert");
        }
        
        FitechUtil.removeAttribute(mapping,request);
                
        String path = "/viewOrgType.do?org_type_name=";

        return new ActionForward(path);
    }
}
