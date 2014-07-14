package com.fitech.net.template.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.net.adapter.StrutsFormulaDelegate;
import com.fitech.net.form.EtlIndexForm;

/**
 * ���������һ����¼��Action����
 * 
 * @author James
 * 
 */
public final class AddEtlIndexFormulaAction extends Action {
    private static FitechException log = new FitechException(
    		AddEtlIndexFormulaAction.class);

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
 
        
        EtlIndexForm etlIndexForm = (EtlIndexForm) form;
        RequestUtils.populate(etlIndexForm, request);
        String indexName=request.getParameter("indexName");
 	   String formual=request.getParameter("formual");
 	   String desc=request.getParameter("desc");
 		if(indexName!=null && !indexName.equals(""))
 			etlIndexForm.setIndexName(indexName);
 		
 		if(formual!=null && !formual.equals(""))
 			etlIndexForm.setFormual(formual);
 		
 		if(desc!=null && !desc.equals(""))
 			etlIndexForm.setDesc(desc);
 		
        try {
            if (etlIndexForm != null){
            	//���Ҹ�ָ�����Ƿ��Ѿ�����
            	boolean isExist = StrutsFormulaDelegate.isExist(etlIndexForm);
            	if(isExist == true){
            		messages.add("ָ����"+etlIndexForm.getIndexName()+"�Ѿ����ڣ�");
            		request.setAttribute(Config.MESSAGES, messages);
            		return new ActionForward("/template/add/addEtlIndex.jsp");
            	}else{
            		insertResult = StrutsFormulaDelegate.createEtlIndex(etlIndexForm);
            		if(insertResult == false){ //����ʧ��
                    	messages.add("������ʽָ��ʧ��!");
                    	request.setAttribute(Config.MESSAGES, messages);
                    	return new ActionForward("/template/add/addEtlIndex.jsp");
                    }else{
                    	messages.add("������ʽָ��ɹ���");
                    }
            	}
            }
        }catch (Exception e){
        	messages.add("������ʽָ��ʧ��!");
            log.printStackTrace(e);
        }

        if(messages != null && messages.getSize() > 0)
            request.setAttribute(Config.MESSAGES, messages);
        
        return mapping.findForward("add");
    }
}
