package com.fitech.net.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechUtil;
import com.fitech.net.adapter.StrutsTargetDefineDelegate;
import com.fitech.net.form.TargetDefineForm;
import com.fitech.net.form.TargetDefineWarnForm;

/**
 * 机构级别插入一条记录的Action对象
 * 
 * @author jcm 
 * 
 */
public final class insertTargetDefineAction extends Action {
    private static FitechException log = new FitechException(
    		insertTargetDefineAction.class);

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

        int insertResult = 0;
        //MessageResources resources = getResources(request);
        FitechMessages messages = new FitechMessages();

        /***
         * 取得当前用户的权限信息
         */   
		HttpSession session = request.getSession();
		Operator operator = null; 
		if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)  
			operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);  
		
        TargetDefineForm targetDefineForm = (TargetDefineForm) form;
        try {
            if (targetDefineForm != null){
            	targetDefineForm.setSetOrgId(operator.getOrgId());
            	boolean result=StrutsTargetDefineDelegate.Exit(targetDefineForm);
				if(result==true){
					messages.add("指标名字已经存在！");
					insertResult=0;
				}
				else{
                  insertResult = StrutsTargetDefineDelegate.create(targetDefineForm);
                  if(insertResult==1)messages.add("增加指标定义成功 ");
                  else messages.add("指标定义格式错误");
				}
            }
        } 
        catch (Exception e){
System.out.println("定义失败");
            insertResult = 0;
            messages.add("增加指标定义失败");		
   //         log.printStackTrace(e);
        }
        if(insertResult!=0)FitechUtil.removeAttribute(mapping,request);
        
        if (messages != null && messages.getSize() > 0)
            request.setAttribute(Config.MESSAGES, messages);
        
        if(targetDefineForm.getDefineName()!=null && targetDefineForm.getDefineName().equals(""))
                request.setAttribute("defineName",targetDefineForm.getDefineName());
        if(insertResult==0){
        	return mapping.findForward("add");
        }
        else{
	        TargetDefineWarnForm targetDefineWarnForm = new TargetDefineWarnForm();
	        targetDefineWarnForm.setTargetDefineId(targetDefineForm.getTargetDefineId());
	        targetDefineWarnForm.setTargetDefineName(targetDefineForm.getDefineName());
	        
	        request.setAttribute("ObjForm",targetDefineWarnForm);
	        return mapping.findForward("next");
        }
    }
}
