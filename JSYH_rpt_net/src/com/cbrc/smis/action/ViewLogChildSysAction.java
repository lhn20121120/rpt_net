package com.cbrc.smis.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.net.adapter.StrutsOrgNetDelegate;
import com.fitech.net.form.OrgNetForm;

/**
 * @����Action
 * @author ���
 *
 */
public final class ViewLogChildSysAction extends Action {
	private FitechException log=new FitechException(ViewLogChildSysAction.class);
	
	
   /**
    * @param result ��ѯ���ر�־,����ɹ�����true,���򷵻�false
    * @param OrgNetForm 
    * @param request 
    * @exception Exception ���쳣��׽���׳�
    */
	public ActionForward execute(
      ActionMapping mapping,
      ActionForm form,
      HttpServletRequest request,
      HttpServletResponse response
	)throws IOException, ServletException {

		FitechMessages messages = new FitechMessages();
		MessageResources resources = getResources(request);
	   
		// �Ƿ���Request
		OrgNetForm orgNetForm = (OrgNetForm)form ;	   
		RequestUtils.populate(orgNetForm, request);		    

		List resList=new ArrayList();		
        /***
         * ȡ�õ�ǰ�û���Ȩ����Ϣ
         * 
         */
        HttpSession session = request.getSession();
        String isFlag=(String)session.getAttribute("Flag");
        request.setAttribute("isFlag",isFlag);
        if(session.getAttribute("Flag")!=null)session.setAttribute("Flag",null);
        List list=(List)session.getAttribute("AllRole");
        if(list!=null){
        	request.setAttribute("AllRole",list);
        }
        Operator operator = null; 
        if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
            operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
        String orgId=operator.getOrgId();
       List allOrgList=null;
		try{
	   		//ȡ�ӻ����б�	
			getOrgList(orgId,resList);	
			
			 if (resList!=null && resList.size()!=0) 
	            {
				 allOrgList =  new ArrayList();
	                for (int i=0; i<resList.size(); i++) 
	                {
	                    OrgNetForm org = (OrgNetForm)resList.get(i);
	                    allOrgList.add(new LabelValueBean(org.getOrg_name(),org.getOrg_id().toString()));
	                }
	            }
	   	}catch (Exception e){
			log.printStackTrace(e);
			messages.add(resources.getMessage("select.orgNet.failed"));		
		}
		//�Ƴ�request��session��Χ�ڵ�����
		//FitechUtil.removeAttribute(mapping,request);
		//��ApartPage��������request��Χ��
	 
	 	 if(messages.getMessages() != null && messages.getMessages().size() > 0)
		   	  request.setAttribute(Config.MESSAGES,messages);
	 	 
	 	 if(resList!=null && resList.size()>0){
	 		 request.setAttribute(Config.RECORDS,allOrgList);	 		
	 	 }
	 	
	 	 return mapping.findForward("log_mgr");
	}
	
	private boolean flag = true;

	private void addChild(List list, OrgNetForm orgNet) {
		List orgList=list;
		if (orgNet != null) {
			String id = orgNet.getOrg_id();			
			if (flag) {
				orgList.add(orgNet);				
				}
			List childList = StrutsOrgNetDelegate.selectChildOrg(id);

			if (childList != null && !childList.equals("")) {
				for (int i = 0; i < childList.size(); i++) {
					OrgNetForm o = (OrgNetForm) childList.get(i);
					flag = false;
					orgList.add(o);		
					addChild(orgList,o);
				}
				flag = true;
			}
		}
	}

	private void getOrgList(String orgId,List list) {

		List lowerOrgList = StrutsOrgNetDelegate.selectChildOrg(orgId);
		if (lowerOrgList != null && !lowerOrgList.equals("")) {
			for (int i = 0; i < lowerOrgList.size(); i++) {
				OrgNetForm o = (OrgNetForm) lowerOrgList.get(i);
				try {
					addChild(list,o);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

}