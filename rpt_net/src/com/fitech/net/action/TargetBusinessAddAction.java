package com.fitech.net.action;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.net.adapter.StrutsTargetDelegate;
import com.fitech.net.form.TargetBusinessForm;
import com.fitech.net.hibernate.MBusiness;

/**
 * ���ֲ���һ����¼��Action����
 * 
 * @author masclnj
 *  
 */
public final class TargetBusinessAddAction extends Action {
    private static FitechException log = new FitechException(
            TargetBusinessAddAction.class);

    /**
     * ��־��¼��
     */
     private static Logger logger = Logger.getLogger(TargetBusinessAddAction.class);
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

        TargetBusinessForm targetBusinessForm = (TargetBusinessForm) form;
//        try {
//            if (targetBusinessForm != null) {
//                insertResult = com.fitech.net.adapter.StrutsTargetDelegate
//                        .create(targetBusinessForm);
//                if (insertResult != true) {
//                    messages.add("����ָ������ʧ��");
//
//                } else
//                    messages.add("����ָ�����ͳɹ�");
//            }
//            // ����һ��request���������
//
//            // request.setAttribute(Config.RECORDS, mCurrForm);
//
//        } catch (Exception e) {
//            log.printStackTrace(e);
//
//            messages.add("����ָ������ʧ��");
//        }
        
        //  ----------------------------------------------------------------------
        // 2008-06-19 gongming �ж������ݿ����Ƿ�����ͬ���Ƶ�ָ�����ʹ���
        //    ��ͬ����������
        try {             
            String tarName = targetBusinessForm.getBusinessName().trim();
            if(logger.isInfoEnabled())
                logger.info("get paramertr businessName [" + tarName + "]");
            if(StringUtils.isNotEmpty(tarName))
            {
                boolean eqauls = false;
                if(logger.isInfoEnabled())
                    logger.info(" query all targetBusiness.....");
                List tarLst = StrutsTargetDelegate.findAll();                
                if(tarLst != null && !tarLst.isEmpty())
                {                    
                    for (Iterator iter = tarLst.iterator(); iter.hasNext();)
                    {
                        MBusiness elmt = (MBusiness) iter.next();
                        if(tarName.equals(elmt.getBusinessName().trim()))
                        {
                            if(logger.isInfoEnabled())
                                logger.info(" find same bussinessName.....");
                            eqauls = true;
                            break;
                        }
                    }
                    tarLst.clear();
                }
                if(!eqauls)
                {
                    if (targetBusinessForm != null) {
                        insertResult = StrutsTargetDelegate.create(targetBusinessForm);
                        if (insertResult != true) {
                            messages.add("����ָ������ʧ��");
                            if(logger.isInfoEnabled())
                                logger.info(" add targetBussiness fail.....");
                        } else
                            messages.add("����ָ�����ͳɹ�");
                        if(logger.isInfoEnabled())
                            logger.info(" add targetBussiness success.....");
                    }
                }
                else
                {
                    messages.add("������ͬ����ָ�����");
                    request.setAttribute(Config.MESSAGES, messages);
                    return mapping.getInputForward();
                }
            }
            else
            {
                messages.add("ָ�����Ͳ���δ��ȡ");
                request.setAttribute(Config.MESSAGES, messages);
                mapping.getInputForward();
            }           
         } catch (Exception e) {
            log.printStackTrace(e);
            messages.add("����ָ������ʧ��");
        }
         //---------------------------------------------------------
        if (messages.getMessages() != null
                && messages.getMessages().size() != 0)
            request.setAttribute(Config.MESSAGES, messages);
        // ����ʧ��
        if (insertResult == false) {
            return mapping.findForward("bad_add");
        }
        String path = "/target/viewTargetBusiness.do";

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
