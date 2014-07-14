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
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.MCurrForm;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;

/**
 * ����ɾ��action��ʵ��
 * 
 * @author����
 * 
 */
public final class DeleteMCurrAction extends Action {
    private static FitechException log = new FitechException(
            DeleteLogInAction.class);

    /**
     * @"view" mapping Action
     * @return request request����
     * @return response respponse����
     * @exception IOException
     *                �Ƿ���IO�쳣��������׽���׳�
     * @exception ServletException
     *                �Ƿ�Servlet�쳣��������׽���׳�
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        // mRepFreqForm MCurrForm�ĳ�ʼ��
        MCurrForm mCurrForm = new MCurrForm();

        // ��request�����mCurrForm
        RequestUtils.populate(mCurrForm, request);
        
            Locale locale=getLocale(request);
           MessageResources resources=this.getResources(request);
           FitechMessages messages=new FitechMessages();
        try
        {
            boolean result = com.cbrc.smis.adapter.StrutsMCurrDelegate
                    .remove(mCurrForm);
            if (result != true) 
            {
                messages.add(FitechResource.getMessage(locale, resources,
                        "delete.failed", "curr.info"));
            }
            else 
            {
                messages.add(FitechResource.getMessage(locale, resources,
                        "delete.success", "curr.info"));
            }
        }
        catch (Exception e) 
        {
            log.printStackTrace(e);

            messages.add(FitechResource.getMessage(locale, resources,
                "delete.failed", "curr.info"));
        }

        if(messages.getMessages() != null && messages.getMessages().size() != 0)
               request.setAttribute(Config.MESSAGES,messages);
        String path = "/config/ViewCurr.do";
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
