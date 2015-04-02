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
 * 参数表插入一条记录的Action对象
 * 
 * @author James
 * 
 */
public final class AddEtlIndexFormulaAction extends Action {
    private static FitechException log = new FitechException(
    		AddEtlIndexFormulaAction.class);

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
            	//查找该指标名是否已经存在
            	boolean isExist = StrutsFormulaDelegate.isExist(etlIndexForm);
            	if(isExist == true){
            		messages.add("指标名"+etlIndexForm.getIndexName()+"已经存在！");
            		request.setAttribute(Config.MESSAGES, messages);
            		return new ActionForward("/template/add/addEtlIndex.jsp");
            	}else{
            		insertResult = StrutsFormulaDelegate.createEtlIndex(etlIndexForm);
            		if(insertResult == false){ //新增失败
                    	messages.add("新增公式指标失败!");
                    	request.setAttribute(Config.MESSAGES, messages);
                    	return new ActionForward("/template/add/addEtlIndex.jsp");
                    }else{
                    	messages.add("新增公式指标成功！");
                    }
            	}
            }
        }catch (Exception e){
        	messages.add("新增公式指标失败!");
            log.printStackTrace(e);
        }

        if(messages != null && messages.getSize() > 0)
            request.setAttribute(Config.MESSAGES, messages);
        
        return mapping.findForward("add");
    }
}
