//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_4.0.1/xslt/JavaClass.xsl

package com.cbrc.org.action;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/** 
 * MyEclipse Struts
 * Creation date: 12-11-2005
 * 
 * XDoclet definition:
 * @struts.action validate="true"
 */
public class DeleteOrgFromSessionAction extends Action {
    /** 
     * Method execute
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     */
    public ActionForward execute(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response) throws IOException, ServletException {
        
        //取出要删除的机构id
        String deleteOrgId = request.getParameter("deleteOrgId");
        String orgClsId = request.getParameter("orgClsId");
        String orgClsName = request.getParameter("orgClsName");
        
        if(deleteOrgId!=null && !deleteOrgId.equals(""))
        {
            HttpSession session = request.getSession();
            HashMap sessionOrgIds = null; 
            //取出以前选择过的机构集合
            if(session.getAttribute("SelectedOrgIds")!=null)
            {   
                //取出以前选择过的机构集合
               sessionOrgIds = (HashMap) session.getAttribute("SelectedOrgIds");
               if(sessionOrgIds !=null && sessionOrgIds.size()!=0)
               {
                  sessionOrgIds.remove(deleteOrgId);
               }
            }         
        }
        String path = "/template/viewSelectedOrg.do";
        if (request.getParameter("childRepId") != null) {
            path += (path.indexOf("?")>=0 ? "&" : "?");
            path += "childRepId=" +request.getParameter("childRepId");
        }
        if (request.getParameter("versionId") != null) {
            path += (path.indexOf("?")>=0 ? "&" : "?");
            path += "versionId=" +request.getParameter("versionId");
        }
        if (request.getParameter("reportStyle") != null) {
            path += (path.indexOf("?")>=0 ? "&" : "?");
            path += "reportStyle=" +request.getParameter("reportStyle");
        }
        if (request.getParameter("reportName") != null) {
            path += (path.indexOf("?")>=0 ? "&" : "?");
            path += "reportName=" +request.getParameter("reportName");
        }
        if (request.getParameter("orgClsId") != null) {
            path += (path.indexOf("?")>=0 ? "&" : "?");
            path += "orgClsId=" +request.getParameter("orgClsId");
        }
        if (request.getParameter("orgClsName") != null) {
            path += (path.indexOf("?")>=0 ? "&" : "?");
            path += "orgClsName=" +request.getParameter("orgClsName");
        }
        if (request.getParameter("flag") != null) {
            path += (path.indexOf("?")>=0 ? "&" : "?");
            path += "flag=" +request.getParameter("flag");
        }
       /* 
        request.setAttribute("ChildRepId",request.getParameter("childRepId"));
        request.setAttribute("VersionId",request.getParameter("versionId"));
        request.setAttribute("ReportStyle",request.getParameter("reportStyle"));
        request.setAttribute("orgClsName",orgClsName);
        request.setAttribute("orgClsId",orgClsId);
        */
          
        return new ActionForward(path);
    }
}


