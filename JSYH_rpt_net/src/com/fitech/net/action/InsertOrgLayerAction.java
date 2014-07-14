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
 * 机构级别插入一条记录的Action对象
 * 
 * @author jcm 
 * 
 */
public final class InsertOrgLayerAction extends Action {
    private static FitechException log = new FitechException(
    		InsertOrgLayerAction.class);

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

        boolean insertResult = false;
        MessageResources resources = getResources(request);

        FitechMessages messages = new FitechMessages();

        OrgLayerForm orgLayerForm = (OrgLayerForm) form;
        try {
            if (orgLayerForm != null)
                insertResult = StrutsOrgLayerDelegate.create(orgLayerForm);
        } 
        catch (Exception e){
            insertResult = false;
            messages.add(resources.getMessage("insert.orgLayer.failed"));		
            log.printStackTrace(e);
        }

        if (messages != null && messages.getSize() > 0)
            request.setAttribute(Config.MESSAGES, messages);

        // 新增失败
        if (insertResult == false) {
            return mapping.findForward("insert");
        }
        
        FitechUtil.removeAttribute(mapping,request);
        form = null;
        
        String path = "/viewOrgLayer.do?org_layer_name=";

        return new ActionForward(path);
    }
}
