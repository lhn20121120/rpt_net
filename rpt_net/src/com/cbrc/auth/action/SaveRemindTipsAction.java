package com.cbrc.auth.action;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.RequestUtils;

import com.cbrc.auth.form.RemindTipsForm;
import com.cbrc.auth.hibernate.RemindTips;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.security.Operator;

/**
 * 保存贴士提醒设置
 * @author jcm
 * @2008-02-20
 */
public class SaveRemindTipsAction extends Action{

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response
	)
		throws IOException, ServletException {

		
		HttpSession session = request.getSession();
        Operator operator = null; 
        if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
            operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
        
        RemindTipsForm rtForm = (RemindTipsForm)form;
        RequestUtils.populate(rtForm, request);
        
        /**若已经有小贴士,则先删除再插入新的贴士*/
        if(Config.REMINDTIPS != null){
        	File file = new File(Config.WEBROOTPATH+"remindTips.txt");
        	if(file.exists()) file.delete();
        	
        	Config.REMINDTIPS = null;
        }
        
        if(request.getParameter("type") == null){
        	BufferedWriter writer = new BufferedWriter(new FileWriter(new File(Config.WEBROOTPATH+"remindTips.txt")));
            writer.write(rtForm.getRtDate() + Config.SPLIT_SYMBOL_COMMA + rtForm.getRtTitle().trim() + Config.SPLIT_SYMBOL_COMMA 
            		+ operator.getOperatorId() + Config.SPLIT_SYMBOL_COMMA + operator.getOperatorName());
            writer.close();
            
            Config.REMINDTIPS = new RemindTips(rtForm.getRtDate(),rtForm.getRtTitle().trim()
            		,String.valueOf(operator.getOperatorId()),operator.getOperatorName());
        }
        
		return mapping.findForward("view_remind");
	}
}
