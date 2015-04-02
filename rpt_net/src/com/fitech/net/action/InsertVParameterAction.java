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
import com.fitech.net.adapter.StrutsVParameterDelegate;
import com.fitech.net.form.VParameterForm;

/**
 * ���������һ����¼��Action����
 * 
 * @author James
 * 
 */
public final class InsertVParameterAction extends Action {
    private static FitechException log = new FitechException(
    		InsertVParameterAction.class);

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

        boolean insertResult = false;
        MessageResources resources = getResources(request);

        FitechMessages messages = new FitechMessages();

        VParameterForm vParamForm = (VParameterForm) form;
        try {
            if (vParamForm != null){
            	boolean isExist = StrutsVParameterDelegate.isExist(vParamForm);
            	if(isExist != true){
            		insertResult = StrutsVParameterDelegate.create(vParamForm);
            		if(insertResult == false){ //�ü���Ļ�������Ѿ����ڣ�������ʧ��
                    	messages.add(resources.getMessage("insert.vParameter.failed"));
                    	request.setAttribute(Config.MESSAGES, messages);
                    	return mapping.findForward("insert");
                    }else{
                    	messages.add("����������ɹ���");
                    }
            	}else{
            		messages.add("ͬ���͵Ĳ������ֶ��Ѿ����ڣ�");
            		request.setAttribute(Config.MESSAGES, messages);
            		return mapping.getInputForward();
            	}
            }
        }catch (Exception e){
            messages.add(resources.getMessage("insert.vParameter.failed"));
            log.printStackTrace(e);
        }

        if(messages != null && messages.getSize() > 0)
            request.setAttribute(Config.MESSAGES, messages);
                
        String path = "/config/ViewVParam.do?vpTabledesc=&vpColumndesc=";

        return new ActionForward(path);
    }
}
