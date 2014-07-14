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
 * �����������һ����¼��Action����
 * 
 * @author jcm 
 * 
 */
public final class InsertOrgTypeAction extends Action {
    private static FitechException log = new FitechException(
    		InsertOrgTypeAction.class);

    /**
     * �������ݵ����ݿ�
     * 
     * @exception IOException
     *                �Ƿ���IO�쳣��������׽���׳�
     * @exception ServletException
     *                �Ƿ���Servlet�쳣��������׽���׳�
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
            if(insertResult == 2){ //�ü���Ļ�������Ѿ�����
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

        // ����ʧ��
        if (insertResult == 0 || insertResult == 2) { //����ʧ�ܻ�ü���Ļ��������ڵĻ���������ҳ��
            return mapping.findForward("insert");
        }
        
        FitechUtil.removeAttribute(mapping,request);
                
        String path = "/viewOrgType.do?org_type_name=";

        return new ActionForward(path);
    }
}
