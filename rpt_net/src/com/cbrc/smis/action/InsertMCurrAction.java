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
import com.cbrc.smis.form.MCurrForm;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;

/**
 * ���ֲ���һ����¼��Action����
 * 
 * @author ����
 * 
 * @struts.action path="/struts/insertMCurr" name="MCurrForm" scope="request"
 *                validate="false"
 * 
 * @struts.action-forward name="view" path="/struts/viewMCurr.do"
 *                        redirect="false"
 * 
 * 
 */
public final class InsertMCurrAction extends Action {
    private static FitechException log = new FitechException(
            InsertMCurrAction.class);

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

        MessageResources resources = getResources(request);
        FitechMessages messages = new FitechMessages();
        Locale locale = request.getLocale();

        boolean insertResult = false;

        MCurrForm mCurrForm = (MCurrForm) form;
        try {
            if (mCurrForm != null) {
                insertResult = com.cbrc.smis.adapter.StrutsMCurrDelegate
                        .create(mCurrForm);
                if (insertResult != true) {
                    messages.add(FitechResource.getMessage(locale, resources,
                            "save.failed", "curr.info"));

                } else
                    messages.add(FitechResource.getMessage(locale, resources,
                            "save.success", "curr.info"));
            }
            // ����һ��request���������

            // request.setAttribute(Config.RECORDS, mCurrForm);

        } catch (Exception e) {
            log.printStackTrace(e);

            messages.add(FitechResource.getMessage(locale, resources,
                    "save.failed", "curr.info"));
        }
        if (messages.getMessages() != null
                && messages.getMessages().size() != 0)
            request.setAttribute(Config.MESSAGES, messages);
        // ����ʧ��
        if (insertResult == false) {
            return mapping.findForward("insert");
        }
        String path = "/config/ViewCurr.do?curName=";

        /*
         * Integer curId = mCurrForm.getCurId(); String curName =
         * mCurrForm.getCurName();
         * 
         * if(curId!=null) { path += (path.indexOf("?")>=0 ? "&" : "?"); path +=
         * "curId="+curId.toString(); } if(curName!=null && !curName.equals("")) {
         * path += (path.indexOf("?")>=0 ? "&" : "?"); path +=
         * "curName="+curName.toString(); }
         */
        return new ActionForward(path);
    }
}
