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
import com.cbrc.smis.form.MCurUnitForm;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechLog;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;

/**
 * 货币单位插入一条记录的Action对象
 * 
 * @author 唐磊
 * 
 * @struts.action path="/struts/insertMCurUnit" name="MCurUnitForm"
 *                scope="Request" validate="false"
 * 
 * @struts.action-forward name="view" path="/struts/viewMCurUnit.do"
 *                        redirect="false"
 * 
 * 
 */
public final class InsertMCurUnitAction extends Action {
    private static FitechException log = new FitechException(
            InsertMCurUnitAction.class);

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


        MCurUnitForm mCurUnitForm = (MCurUnitForm) form;
        boolean insertResult = false;
        try {
        	HttpSession session = request.getSession();
    		Operator operator = null; 
    		if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
    			operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
    		
            if (mCurUnitForm != null) {
                insertResult = com.cbrc.smis.adapter.StrutsMCurUnitDelegate
                        .create(mCurUnitForm);

                /**添加成功*/
                if (insertResult == true){
                	/**写入日志*/
                	String msg = FitechResource.getMessage(locale,resources,
                			"save.success","curUnit.msg",
							mCurUnitForm.getCurUnitName());
                	FitechLog.writeLog(Config.LOG_OPERATION,operator.getUserName(),msg);
                	
                    messages.add(FitechResource.getMessage(locale, resources,
                            "save.success", "curUnit.info")); 
                }else{
                	/**写入日志*/
                	String msg = FitechResource.getMessage(locale,resources,
                			"save.failed","curUnit.msg",
							mCurUnitForm.getCurUnitName());
                	FitechLog.writeLog(Config.LOG_OPERATION,operator.getUserName(),msg);
                	
                    messages.add(FitechResource.getMessage(locale, resources,
                            "save.failed", "curUnit.info", mCurUnitForm.getCurUnitName()));
                }
            }
        } 
        catch (Exception e){
            log.printStackTrace(e);
            messages.add(FitechResource.getMessage(locale, resources,
                    "save.failed", "curUnit.info", mCurUnitForm.getCurUnitName()));
        }
        
        if(messages.getMessages() != null && messages.getMessages().size() != 0)
            request.setAttribute(Config.MESSAGES,messages);
       
        if (insertResult == false) {
            return mapping.findForward("insert");
        }
        String path = "/config/ViewCurUnit.do?curUnitName=";
        
        return new ActionForward(path);
    }
}
