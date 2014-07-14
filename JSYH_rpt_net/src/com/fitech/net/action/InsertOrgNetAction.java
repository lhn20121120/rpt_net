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
import com.fitech.net.adapter.StrutsOrgNetDelegate;
import com.fitech.net.form.OrgNetForm;

/**
 * ��������һ����¼��Action����
 * 
 * @author jcm 
 * 
 */
public final class InsertOrgNetAction extends Action {
    private static FitechException log = new FitechException(
    		InsertOrgNetAction.class);

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

    	int insertResult = 0;
        MessageResources resources = getResources(request);
        
        //�ж��Ƿ����µ����ҳ��
        String curPage=request.getParameter("curPage");
        
        
        HttpSession session = request.getSession();
		Operator operator = null; 
		if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)  
			operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);   
		
		String orgId = operator.getOrgId();

        FitechMessages messages = new FitechMessages();

        OrgNetForm orgNetForm = (OrgNetForm) form;
        orgNetForm.setSetOrgId(orgId);
        try {        	
            if (orgNetForm != null)
                insertResult = StrutsOrgNetDelegate.create(orgNetForm,orgId);
            if(insertResult == 2){ //���������Ѿ�����
            	messages.add(resources.getMessage("insert.orgNet.exist"));
            }else if(insertResult == 1){
            	messages.add(resources.getMessage("insert.orgNet.success"));
            }else if(insertResult == 0){
            	messages.add(resources.getMessage("insert.orgNet.failed"));
            }
        } 
        catch (Exception e){
            insertResult = 0;
            messages.add(resources.getMessage("insert.orgNet.failed"));		
            log.printStackTrace(e);
        }
        
        if (messages != null && messages.getSize() > 0)
            request.setAttribute(Config.MESSAGES, messages);
        
        if(curPage!=null){
        	if(insertResult==2){
        		return new ActionForward("/netOrg/newAddOrg/AddOrg2.jsp?orgType="+orgNetForm.getOrg_type_id()+"&curPage="+curPage+"&preOrgId="+orgNetForm.getPre_org_id());
        	}
        	return new ActionForward("/org/selectOrgNet.do?curPage"+curPage);
        }
        
        //����ʧ��
        if(session.getAttribute("new_add_view")!=null)        		
        	session.removeAttribute("new_add_view");
         
        session.setAttribute("new_add_view","new_add_view");        
        return new ActionForward("/viewOrgNet.do?org_id=&org_name=&org_type_id=&pre_org_id=&region_id=");
        //return mapping.findForward("insert");
    }
}
