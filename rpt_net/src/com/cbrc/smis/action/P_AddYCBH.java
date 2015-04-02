package com.cbrc.smis.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.security.Operator;
import com.fitech.net.adapter.StrutsOrgNetDelegate;
import com.fitech.net.hibernate.OrgNet;


public final class P_AddYCBH extends Action {

   /**
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
	   
	   HttpSession session=request.getSession();
	   Operator operator=(Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
	   String curOrgId=request.getParameter("curOrgId");
	   
	   
	   
	   String childRepId=request.getParameter("childRepId");
	   String versionId=request.getParameter("versionId");
	   String reportName=request.getParameter("reportName");
	   String reportStyle=request.getParameter("reportStyle");
	   String orgId=request.getParameter("orgId");
	   String orgIds=request.getParameter("orgIds");
	   if(curOrgId.equals("-1"))
	   {
		  
		   session.removeAttribute("curOrgId");
		   session.setAttribute("curOrgId",orgId);
	   }
	   else
	   {
		   curOrgId=(String)session.getAttribute("curOrgId");
	   }
	   if(orgId==null)orgId=curOrgId;
	   // System.out.println("orgId is "+orgId);
	   // System.out.println(childRepId);
	   // System.out.println(versionId);
	   List lowerOrgList=StrutsOrgNetDelegate.selectLowerOrgList(orgId,session);
	   request.setAttribute("childRepId",childRepId);
	   request.setAttribute("versionId",versionId);
	   
	   request.setAttribute("orgId",orgId);
	   request.setAttribute("curOrgId",curOrgId);
	   request.setAttribute("reportStyle",reportStyle);
	   if(reportName!=null)
	   request.setAttribute("reportName",new String (reportName.getBytes("iso-8859-1"),"gb2312"));
	   if(lowerOrgList!=null && lowerOrgList.size()!=0){
		   request.setAttribute("lowerOrgList",lowerOrgList);
	   }
	   
	   
	   if(orgIds!=null && !orgIds.equals(""))
	   {
		   String OrgIds[]=orgIds.split(",");
		   HashMap hMap=null;
			if(session.getAttribute("SelectedOrgIds")==null)
	        {
	             hMap = new HashMap();
	           
	        }	
			else
			{
				hMap=(HashMap)session.getAttribute("SelectedOrgIds");
				session.removeAttribute("SelectedOrgIds");
			}
			
			//删除子机构
			
			 if(curOrgId.equals("-1"))
			 {
				 lowerOrgList=StrutsOrgNetDelegate.selectLowerOrgList(((Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)).getOrgId(),session);
			 }
			if(orgId!=null && !orgId.equals(""))
			{
			
			for(int i=0;i<lowerOrgList.size();i++)
			{
				
				hMap.remove(((OrgNet)lowerOrgList.get(i)).getOrgId());
			}
			}
			//加上选中的项
			if(orgIds!=null)
			 for(int i=0;i<OrgIds.length;i++)
	         {
				 if(OrgIds[i].trim()!=null && !OrgIds[i].trim().equals(""))
	             hMap.put(OrgIds[i].trim(),"ok");
	                          
	         }
			// System.out.println(hMap.size());
	     session.setAttribute("SelectedOrgIds",hMap);
	   }
	   
      return mapping.findForward("view");
   }
}
