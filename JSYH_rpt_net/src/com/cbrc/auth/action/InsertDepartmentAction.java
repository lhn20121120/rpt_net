package com.cbrc.auth.action;

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
import org.apache.struts.util.RequestUtils;

import com.cbrc.auth.adapter.StrutsDepartmentDelegate;
import com.cbrc.auth.form.DepartmentForm;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;

/**
 * ��Ӳ�����Ϣ
 *
 * @author Ҧ��
 *
 * @struts.action
 *    path="/popedom_mgr/insertDepartment"
 *    name="departmentForm"
 *    scope="session"
 *    validate="false"
 *
 * @struts.action-forward
 *    name="dept_mgr"
 *    path="/popedom_mgr/dept_mgr.jsp"
 *    redirect="false"
 *

 */
public final class InsertDepartmentAction extends Action {
    private static FitechException log = new FitechException(InsertDepartmentAction.class); 
   /**
    * ��ʹ��hibernate ���Ը� 2011-12-27
    * Ӱ�����Department
    * Performs action.
    * @param mapping Action mapping.
    * @param form Action form.
    * @param request HTTP request.
    * @param response HTTP response.
    * @exception IOException if an input/output error occurs
    * @exception ServletException if a servlet exception occurs
    */
   
    public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,
    		HttpServletResponse response) throws IOException, ServletException {

    	MessageResources resources=getResources(request);
    	FitechMessages messages = new FitechMessages();
    	Locale locale = request.getLocale();
       
    	/**ȡ��request��Χ�ڵ�������������������*/
    	DepartmentForm departmentForm = new DepartmentForm(); 
    	RequestUtils.populate(departmentForm, request);

    	HttpSession session = request.getSession();       
    	com.cbrc.smis.security.Operator operator = null; 
       
    	if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
    		operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
       
    	boolean result = false;
    	try{
    		/**��ʹ��hibernate ���Ը� 2011-12-27
    		 * Ӱ�����Department*/
    		if(StrutsDepartmentDelegate.isExist(departmentForm,operator) == false){
    			/**�����¼*/
    			/**��ʹ��Hibernate ���Ը� 2011-12-28
    			 * Ӱ�����Department**/
        		result = StrutsDepartmentDelegate.create(departmentForm,operator);
        		if(result==true)
        			messages.add(FitechResource.getMessage(locale,resources,"save.success","dept.info"));
        		else{ /**�����򷵻�ԭҳ��*/
        			messages.add(FitechResource.getMessage(locale,resources,"save.failed","dept.info"));
        			request.setAttribute(Config.MESSAGES,messages);
        			return mapping.findForward("dept_add");
        		}
    		}else{ //�ò��������Ѿ�����
    			messages.add(resources.getMessage("save.failed.Exist"));	
    			request.setAttribute(Config.MESSAGES,messages);
    			return mapping.findForward("dept_add");
    		}
    		
    	}catch (Exception e){
    		/**�����򷵻�ԭҳ��*/
    		log.printStackTrace(e);
    		messages.add(FitechResource.getMessage(locale,resources,"save.failed","dept.info")); 
    		request.setAttribute(Config.MESSAGES,messages);
    		return mapping.findForward("dept_add");
		}
    	if(messages.getMessages() != null && messages.getMessages().size() > 0)
    		request.setAttribute(Config.MESSAGES,messages);
       
    	return new ActionForward("/popedom_mgr/viewDepartment.do");
    }
}
