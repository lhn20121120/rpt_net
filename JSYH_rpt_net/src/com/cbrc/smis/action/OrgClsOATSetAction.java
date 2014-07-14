package com.cbrc.smis.action;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.cbrc.smis.adapter.StrutsOrgDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.OrgForm;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.gather.adapter.StrutsJieKou;
/**
 * 按机构类型设置频度类别操作类
 * 
 * @author rds
 * @date 2006-02-07
 */
public class OrgClsOATSetAction extends Action {
	private static FitechException log=new FitechException(OrgOATSetAction.class);
	
	/**
	 * Performs action.
	 * 
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
		Locale locale = getLocale(request);
		MessageResources resources = getResources(request);
		
		OrgForm orgForm=(OrgForm)form;
		
		FitechMessages messages = new FitechMessages();
		
		String curPage=request.getParameter("curPage")!=null?request.getParameter("curPage"):"";
		
		boolean flag=false;
		
		if(orgForm!=null){
			List orgIds=StrutsOrgDelegate.getOrgIdsByOrgClsId(orgForm.getOrgClsId());
			
			if(orgIds!=null && orgIds.size()>0){
				flag=StrutsOrgDelegate.updatePatch(orgIds,orgForm.getOATId());
				/**同步外网操作**/
				
				if(flag==true){				
					if(StrutsJieKou.updatePatchOrgType(orgForm.getOATId(),orgIds)==false){
						log.println("更新外网机构信息失败!");
					}
				}
			}
		}
		
		if(messages!=null && messages.getSize()>0) request.setAttribute(Config.MESSAGES,messages);
		
		String path="";
		if(flag==true){
			messages.add(resources.getMessage(locale,"ORGOAT.set.success"));
			path=mapping.findForward("view").getPath();
		}else{
			request.setAttribute("OrgSrhName",orgForm.getOrgSrhName());
			messages.add(resources.getMessage(locale,"ORGOAT.set.failured"));
			path=mapping.getInputForward().getPath();
		}
		path+="?orgSrhName=" + orgForm.getOrgSrhName() + 
			"&orgClsId=" + orgForm.getOrgClsId() + 
			"&curPage=" + curPage;
		return new ActionForward(path);
	}
}
