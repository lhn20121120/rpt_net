package com.cbrc.smis.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.cbrc.smis.adapter.StrutsMChildReportDelegate;
import com.cbrc.smis.adapter.StrutsMRepRangeDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.UpdateBSFWForm;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechMessages;
/**
 * 报送范围修改后的更新操作类
 * 
 * @author 王东伟
 *
 */
public class UpdateBSFWAction extends Action {
	/**
	 * 已使用hibernate 卞以刚 2011-12-22
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
		
		Locale locale=getLocale(request);
		MessageResources resources=this.getResources(request);
		FitechMessages messages=new FitechMessages();
		   
		UpdateBSFWForm bsfwForm=(UpdateBSFWForm)form;
		if(bsfwForm == null)					
			return mapping.findForward("updateOK");
				
		HttpSession session=request.getSession();	
		String orgIds=request.getParameter("orgIds");
		ArrayList list=new ArrayList();
		if(orgIds!=null){
			String[] arrOrgIds=orgIds.split(",");
			for(int i=0;i<arrOrgIds.length;i++){
				list.add(arrOrgIds[i]);				
			}
		}	
		String path = "";
		String url = "";
		//-----------------------------------------------------
        // gongming 2008-07-26
        String curPage = null;
        curPage = request.getParameter("curPage");
        if(StringUtils.isEmpty(curPage))
            curPage = "1";
        String childRepId="";
        String versionId="";
		try{
			childRepId = request.getParameter("childRepId");
			versionId = request.getParameter("versionId");
			/**已使用hibernate 卞以刚 2011-12-21**/
			
//			StrutsMRepRangeDelegate.removeLowerOrg(childRepId,versionId,((Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)).getOrgId());
			/**刘凯2013-1-28 报送范围因为考虑到汇总报表的报送 ，取消机构级联关系选择 ，修改前 删除该版本该编号的模版的所有下级行的报送范围包括**/
			StrutsMRepRangeDelegate.removeOne(versionId,childRepId);
			/**已使用hibernate 卞以刚 2011-12-22**/
			StrutsMRepRangeDelegate.updateBSFW(childRepId,versionId,list);
			messages.add("报送范围设置成功！");
			
			if(bsfwForm.getChildRepId()!=null && bsfwForm.getVersionId()!=null)
				/**已使用hibernate 卞以刚 2011-12-22**/
				bsfwForm.setReportName(StrutsMChildReportDelegate.getname(bsfwForm.getChildRepId(),versionId));

	        request.setAttribute("ObjForm",bsfwForm);	        
	                   
//	        path = mapping.findForward("setBSPL").getPath();        
//	        	       
//	        if (childRepId != null)
//	            url += (url.equals("") ? "" : "&") + "childRepId="
//	                    + childRepId;
//	        if (versionId != null)
//	            url += (url.equals("") ? "" : "&") + "versionId="
//	                    + versionId;
//	        if (bsfwForm.getReportName() != null)
//	            url += (url.equals("") ? "" : "&") + "reportName="
//	                    + bsfwForm.getReportName();
//	       if (bsfwForm.getReportStyle() != null)
//	            url += (url.equals("") ? "" : "&") + "reportStyle="
//	                    + bsfwForm.getReportStyle();
//	        
		}catch(Exception ex){
			ex.printStackTrace();
			messages.add("报送范围设置失败!");
			if(path.equals("")) return new ActionForward("/template/viewTemplate.do?childRepId=&versionId=&curPage="+curPage);
		}
		if(messages!=null && messages.getSize()>0)
			request.setAttribute(Config.MESSAGES,messages);
		
		//return new ActionForward(path + (url.equals("") ? "" : "?" + url));
		
		return new ActionForward("/template/viewTemplateDetail.do?childRepId="+childRepId+"&versionId="+versionId+"&bak2=2");
	}
}

 
