package com.cbrc.smis.action;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.MRepTypeForm;
import com.cbrc.smis.other.InnerToGather;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechLog;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;

/**
 * �ϱ�������һ����¼��Action����
 * 
 * @author����
 * 
 * @struts.action path="/struts/insertMRepType" name="MRepTypeForm"
 *                scope="request" validate="false"
 * 
 * @struts.action-forward name="view" path="/struts/viewMRepType.do"
 *                        redirect="false"
 * 
 * 
 */
public final class InsertMRepTypeAction extends Action {
    private static FitechException log = new FitechException(
            InsertMRepTypeAction.class);

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
       
        MessageResources resources=getResources(request);
        FitechMessages messages = new FitechMessages();
        Locale locale = request.getLocale();
        
        boolean insertResult = false;

        MRepTypeForm mRepTypeForm = (MRepTypeForm) form;
        try
        {
            if (mRepTypeForm != null) 
            {
                insertResult = com.cbrc.smis.adapter.StrutsMRepTypeDelegate
                        .create(mRepTypeForm);
                
                /**�������³ɹ�*/
                if (insertResult == true) 
                {
                    // ����������³ɹ���ʼ�����������ݿ�
                    boolean repTypeIntoGather = InnerToGather.insertMRepType(mRepTypeForm);
                    // ����������³ɹ������ذɣ�����
                    if (repTypeIntoGather == false) 
                    {  

                        String msg =FitechResource.getMessage(locale, resources,
                                "outweb.insert.failed", "repType.msg",
                                mRepTypeForm.getRepTypeName());
                        FitechLog.writeLog(Config.LOG_SYSTEM_CREATESTORAGEXML,Config.SYSTEM_OPERATOR,msg);
                    }
                    else
                    {

                        String msg =FitechResource.getMessage(locale, resources,
                                "outweb.insert.success", "repType.msg",
                                mRepTypeForm.getRepTypeName());
                        FitechLog.writeLog(Config.LOG_SYSTEM_CREATESTORAGEXML,Config.SYSTEM_OPERATOR,msg);
                    }  
                    messages.add(FitechResource.getMessage(locale,resources,"save.success","repType.info"));
                }
                /**�������²��ɹ�*/
                else
                {
                    messages.add(FitechResource.getMessage(locale,resources,"save.failed","repType.info"));
                    //return mapping.findForward("insert"); 
                }
                    
            }
            // ����һ��request���������
            //request.setAttribute(Config.RECORDS, mRepTypeForm);

        }
        catch (Exception e) 
        {
            log.printStackTrace(e);
            messages.add(FitechResource.getMessage(locale,resources,"save.failed","repType.info"));
        }
        
        if(messages.getMessages() != null && messages.getMessages().size() != 0)
            request.setAttribute(Config.MESSAGES,messages);
        // ����ʧ��
        if (insertResult == false) {
            return mapping.findForward("insert");
        }
        String path = "/config/ViewRepType.do";

       /* Integer repTypeId = mRepTypeForm.getRepTypeId();
        String repTypeName = mRepTypeForm.getRepTypeName();

        if (repTypeId != null) {
            path += (path.indexOf("?") >= 0 ? "&" : "?");
            path += "curUnit=" + repTypeId.toString();
        }
        if (repTypeName != null && !repTypeName.equals("")) {
            path += (path.indexOf("?") >= 0 ? "&" : "?");
            path += "curUnitName=" + repTypeName.toString();
        }*/
        return new ActionForward(path);
    }
}
