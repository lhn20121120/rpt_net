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
 * 币种插入一条记录的Action对象
 * 
 * @author masclnj
 *  
 */
public final class TargetBusinessAddAction extends Action {
    private static FitechException log = new FitechException(
            TargetBusinessAddAction.class);

    /**
     * 日志记录器
     */
     private static Logger logger = Logger.getLogger(TargetBusinessAddAction.class);
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
//                    messages.add("新增指标类型失败");
//
//                } else
//                    messages.add("新增指标类型成功");
//            }
//            // 设置一个request对象的属性
//
//            // request.setAttribute(Config.RECORDS, mCurrForm);
//
//        } catch (Exception e) {
//            log.printStackTrace(e);
//
//            messages.add("新增指标类型失败");
//        }
        
        //  ----------------------------------------------------------------------
        // 2008-06-19 gongming 判断在数据库内是否有相同名称的指标类型存在
        //    相同不允许新增
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
                            messages.add("新增指标类型失败");
                            if(logger.isInfoEnabled())
                                logger.info(" add targetBussiness fail.....");
                        } else
                            messages.add("新增指标类型成功");
                        if(logger.isInfoEnabled())
                            logger.info(" add targetBussiness success.....");
                    }
                }
                else
                {
                    messages.add("已有相同名称指标存在");
                    request.setAttribute(Config.MESSAGES, messages);
                    return mapping.getInputForward();
                }
            }
            else
            {
                messages.add("指标类型参数未获取");
                request.setAttribute(Config.MESSAGES, messages);
                mapping.getInputForward();
            }           
         } catch (Exception e) {
            log.printStackTrace(e);
            messages.add("新增指标类型失败");
        }
         //---------------------------------------------------------
        if (messages.getMessages() != null
                && messages.getMessages().size() != 0)
            request.setAttribute(Config.MESSAGES, messages);
        // 新增失败
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
