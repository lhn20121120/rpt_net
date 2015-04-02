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
 * ָ��ҵ�����Ͳ���һ����¼��Action����
 * 
 * @author masclnj
 *  
 */
public final class TargetNormalAddAction extends Action {
    private static FitechException log = new FitechException(
            TargetNormalAddAction.class);

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
            throws ServletException {

        FitechMessages messages = new FitechMessages();
        boolean insertResult = false;

        TargetNormalForm targetNormalForm = (TargetNormalForm) form;
        try {
            if (targetNormalForm != null) {
            	if(StrutsTargetDelegate.isMNormalExist(targetNormalForm.getNormalName()) == false){
            		insertResult = StrutsTargetDelegate.create(targetNormalForm);
            
            		if (insertResult != true) {
            			messages.add("����ָ��ҵ��ʧ��");
            		} else
            			messages.add("����ָ��ҵ��ɹ�");
            	}else
            		messages.add("ָ��ҵ�������Ѿ����ڣ�");
            }
        } catch (Exception e) {
            log.printStackTrace(e);

            messages.add("����ָ��ҵ��ʧ��");
        }
        if (messages.getMessages() != null
                && messages.getMessages().size() != 0)
            request.setAttribute(Config.MESSAGES, messages);
        // ����ʧ��
        if (insertResult == false) {
            return mapping.findForward("bad_add");
        }
        String path = "/target/viewTargetNormal.do";
        return new ActionForward(path);
    }
}
