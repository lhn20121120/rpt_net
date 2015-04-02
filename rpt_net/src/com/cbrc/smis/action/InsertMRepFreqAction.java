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
import com.cbrc.smis.form.MRepFreqForm;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechLog;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;

/**
 * �ϱ�Ƶ�Ȳ���һ����¼��Action����
 * 
 * @author ����
 * 
 * @struts.action path="/struts/insertMRepFreq" name="MRepFreqForm"
 *                scope="request" validate="false"
 * 
 * @struts.action-forward name="view" path="/struts/viewMRepFreq.do"
 *                        redirect="false"
 */
public final class InsertMRepFreqAction extends Action {
    private static FitechException log = new FitechException(
            InsertMRepFreqAction.class);

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
        Locale locale = getLocale(request);
        MessageResources resources = getResources(request);

        FitechMessages messages = new FitechMessages();

        MRepFreqForm mRepFreqForm = (MRepFreqForm) form;
        try {        	
        	HttpSession session = request.getSession();
    		Operator operator = null; 
    		if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
    			operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
    		
            if (mRepFreqForm != null) {
                insertResult = com.cbrc.smis.adapter.StrutsMRepFreqDelegate
                        .create(mRepFreqForm);

                if (insertResult == true){
                    // ����������³ɹ���ʼ��������
                    if (insertResult == true) {
                    	String msg =FitechResource.getMessage(locale, resources,
                                "save.success", "repFreq.msg",
                                mRepFreqForm.getRepFreqName());
                        FitechLog.writeLog(Config.LOG_OPERATION,operator.getUserName(),msg);
                        
                        messages.add(FitechResource.getMessage(locale, resources,
                                "save.success", "repFreq.info"));
                    }else {
                    	String msg =FitechResource.getMessage(locale, resources,
                                "save.failed", "repFreq.msg",
                                mRepFreqForm.getRepFreqName());
                        FitechLog.writeLog(Config.LOG_OPERATION,operator.getUserName(),msg);
                        
                        messages.add(FitechResource.getMessage(locale,
                                resources, "save.failed", "repFreq.info"));
                    }                    
                }
            }
        }catch (Exception e){
            insertResult = false;
            messages.add(FitechResource.getMessage(locale,
                    resources, "save.failed", "repFreq.info"));
            log.printStackTrace(e);
        }

        if (messages != null && messages.getSize() > 0)
            request.setAttribute(Config.MESSAGES, messages);

        // ����ʧ��
        if (insertResult == false) {
            return mapping.findForward("insert");
        }

        String path = "/config/ViewCurRepFreqence.do?repFreqName=";
        /*
        Integer repFreqId = mRepFreqForm.getRepFreqId();
        String repFreqName = mRepFreqForm.getRepFreqName();

        if (repFreqId != null) {
            path += (path.indexOf("?") >= 0 ? "&" : "?");
            path += "repFreqId=" + repFreqId.toString();
        }
        if (repFreqName != null && !repFreqName.equals("")) {
            path += (path.indexOf("?") >= 0 ? "&" : "?");
            path += "repFreqName=" + repFreqName.toString();
        }*/
        return new ActionForward(path);
    }
}
