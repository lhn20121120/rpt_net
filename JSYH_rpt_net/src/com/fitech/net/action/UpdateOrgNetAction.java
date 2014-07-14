package com.fitech.net.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
 * ����������²�����Action����
 *
 */
public final class UpdateOrgNetAction extends Action {
	private static FitechException log = new FitechException(UpdateOrgLayerAction.class);
   /**
    * @param mapping Action mapping.
    * @param form Action form.
    * @param request HTTP request.
    * @param response HTTP response.
    * @exception IOException�Ƿ�������/������쳣
    * @exception ServletException�Ƿ���servlet���쳣ռ��
    */
   public ActionForward execute(
      ActionMapping mapping,
      ActionForm form,
      HttpServletRequest request,
      HttpServletResponse response
   )
      throws IOException, ServletException {
	   
	   
	   
	   FitechMessages messages = new FitechMessages();
	   MessageResources resources = getResources(request);
	   OrgNetForm orgNetForm=(OrgNetForm)form;
	   //�ж��Ƿ��ǻ����б� ��Ϊ null��Ϊ������
	   String curPage=request.getParameter("curPage");
	   Operator operator = (Operator)request.getSession().getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
	   boolean result=false;
	   if(orgNetForm!=null){
			try {
				if(orgNetForm.getSetOrgId() == null) orgNetForm.setSetOrgId(operator.getOrgId());
				result=StrutsOrgNetDelegate.update(orgNetForm);
				if (result == true)
					messages.add(resources.getMessage("update.orgNet.success"));	
	            else
	            	messages.add(resources.getMessage("update.orgNet.failed"));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.printStackTrace(e);
				messages.add(resources.getMessage("update.orgNet.failed"));
			}
	   }
	   if(messages.getMessages() != null && messages.getMessages().size() > 0)
		   	  request.setAttribute(Config.MESSAGES,messages);
	   if(curPage!=null){
		   if(!result){
       		return new ActionForward("/netOrg/orgNet/orgNetEdit2.jsp?orgId="+orgNetForm.getOrg_id()+"&curPage="+curPage);
       	}
       	return new ActionForward("/org/selectOrgNet.do?curPage"+curPage);
	   }
	   
	   
	   
	   String path = "/viewOrgNet.do?org_name=";
	   
	   return new ActionForward(path);
	}
  }
