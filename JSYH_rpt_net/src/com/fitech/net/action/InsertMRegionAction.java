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
import org.apache.struts.util.MessageResources;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechUtil;
import com.fitech.net.adapter.StrutsMRegionDelegate;
import com.fitech.net.adapter.StrutsOrgTypeDelegate;
import com.fitech.net.form.MRegionForm;

/**
 * 机构地区插入一条记录的Action对象
 * 
 * @author jcm 
 * 
 */
public final class InsertMRegionAction extends Action {
    private static FitechException log = new FitechException(
    		InsertMRegionAction.class);

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
        MessageResources resources = getResources(request);

        HttpSession session = request.getSession();
		Operator operator = null; 
		if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)  
			operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);   
		
		String orgId = operator.getOrgId();
		
        FitechMessages messages = new FitechMessages();

        MRegionForm mRegionForm = (MRegionForm) form;
        mRegionForm.setSetOrgId(orgId);
        mRegionForm.setOrg_type_id(StrutsOrgTypeDelegate.findMaxOrgTyp().getOrgTypeId());
        
        try {
            if (mRegionForm != null)
                insertResult = StrutsMRegionDelegate.create(mRegionForm);
            if(insertResult == 2){ //机构地区已经存在
            	messages.add(resources.getMessage("insert.mRegion.exist"));
            }else if (insertResult == 1)
            	messages.add(resources.getMessage("insert.mRegion.success"));
        } 
        catch (Exception e){
            insertResult = 0;
            messages.add(resources.getMessage("insert.mRegion.failed"));		
            log.printStackTrace(e);
        }

        if (messages != null && messages.getSize() > 0)
            request.setAttribute(Config.MESSAGES, messages);

        // 新增失败
        if (insertResult == 0 || insertResult == 2) { //操作失败或该机构类型的地区存在的话返回新增页面
            return mapping.findForward("insert");
        }
        
        FitechUtil.removeAttribute(mapping,request);
                
        String path = "/viewMRegion.do?region_name=";

        return new ActionForward(path);
    }
}
