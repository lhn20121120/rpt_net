package com.cbrc.auth.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

import com.cbrc.auth.adapter.StrutsRoleToolDelegate;
import com.cbrc.auth.form.RoleForm;
import com.cbrc.auth.form.RoleToolForm;
import com.cbrc.auth.form.ToolSettingForm;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;

/**
 * 角色权限信息查看
 *
 * @author 姚捷
 *
 * @struts.action
 *    path="/struts/viewRoleTool"
 *
 * @struts.action-forward
 *    name="view"
 *    path="/struts/viewRoleTool.jsp"
 *    redirect="false"
 */
public final class ViewRoleToolAction extends Action {
    private static FitechException log = new FitechException(ViewRoleToolAction.class); 
   /**
    * 已使用hibernate 卞以刚 2011-12-28
    * 影响对象：ToolSetting RoleTool
    * Performs action.
    * @param mapping Action mapping.
    * @param form Action form.
    * @param request HTTP request.
    * @param response HTTP response.
    * @exception IOException if an input/output error occurs
    * @exception ServletException if a servlet exception occurs
    */
   public ActionForward execute(ActionMapping mapping,
		   ActionForm form,
		   HttpServletRequest request,
		   HttpServletResponse response)
   throws IOException, ServletException {
	                 
	   MessageResources resources=getResources(request);
       FitechMessages messages = new FitechMessages();
       Locale locale = request.getLocale();
       
       RoleForm roleForm = new RoleForm();
       RequestUtils.populate(roleForm,request);
       if(com.cbrc.smis.common.Config.WEB_SERVER_TYPE==1&&roleForm.getRoleName()!=null){
    	   roleForm.setRoleName(new String(roleForm.getRoleName().getBytes("iso-8859-1"), "gb2312"));
		 }
       /**全部的功能菜单*/
       List allMenu = null;
       /**该角色已经选择的功能菜单*/
       List roleMenu = null;
               
       try{
    	   HttpSession session = request.getSession();       
    	   Operator operator = null; 
           if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
               operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME); 
           
           /**可以为该角色设置的菜单权限*/
           /**已使用hibernate 卞以刚 2011-12-28
            * 影响对象：ToolSetting RoleTool*/
           List results = StrutsRoleToolDelegate.getRoleMenuPopedom(operator.getRoleIds(),operator.isSuperManager());
           if (results!=null && results.size()!=0){
               allMenu =  new ArrayList();
               for (int i=0; i<results.size(); i++){
            	   ToolSettingForm toolSettingForm = (ToolSettingForm)(results.get(i));
                   allMenu.add(new LabelValueBean(toolSettingForm.getMenuName(),toolSettingForm.getMenuId().toString()));
               }
           }
           /**查询该角色已经有的权限信息*/
           /**已使用hibernate 卞以刚 2011-12-28
            * 影响对象：RoleTool*/
           List list = StrutsRoleToolDelegate.getRoleMenus(roleForm.getRoleId());
           if (list!=null && list.size()!=0){
               roleMenu =  new ArrayList();
               for (int i=0; i<list.size(); i++){
                   RoleToolForm rt = (RoleToolForm)(list.get(i));
                   roleMenu.add(new LabelValueBean(rt.getMenuName(),rt.getMenuId().toString()));
               }
           }
       }catch (Exception e){           
    	   log.printStackTrace(e);
           messages.add(FitechResource.getMessage(locale,resources,"select.fail","role.popedom.info"));        
       }
       /**角色编号*/
       if(roleForm.getRoleId()!=null)
           request.setAttribute("RoleId",roleForm.getRoleId());
       /**角色名称*/
       if(roleForm.getRoleName()!=null)
    	   request.setAttribute("RoleName",roleForm.getRoleName());
                  
       /**所有菜单*/
       if(allMenu!=null && allMenu.size()!=0)
           request.setAttribute("AllMenu",allMenu);
       /**角色已经有的权限*/
       if(roleMenu!=null && roleMenu.size()!=0)
           request.setAttribute("RoleMenu",roleMenu);
       if(messages.getMessages() != null && messages.getMessages().size() > 0)
           request.setAttribute(Config.MESSAGES,messages);
       return mapping.findForward("role_data_access");
   }
}
