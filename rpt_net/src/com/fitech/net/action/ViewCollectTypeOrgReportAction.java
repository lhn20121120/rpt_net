package com.fitech.net.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.MessageResources;

import com.cbrc.smis.adapter.StrutsMChildReportDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.MChildReportForm;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.net.adapter.StrutsOrgNetDelegate;
import com.fitech.net.form.OrgNetForm;
import com.fitech.net.hibernate.OrgNet;

/** 
 * 汇总方式的机构及报表展现
 * @author jcm
 * 
 * @struts.action path="/viewUserGrpOrgPopedom" name="mUserGrpForm" scope="request" validate="true"
 * @struts.action-forward name="user_group_org_popedom" path="/popedom_mgr/user_group_mgr/user_group_org_popedom.jsp"
 */
public class ViewCollectTypeOrgReportAction extends Action {

    private static FitechException log = new FitechException(ViewCollectTypeOrgReportAction.class); 
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
        HttpServletResponse response)  throws IOException, ServletException{
    	
        MessageResources resources=getResources(request);
        FitechMessages messages = new FitechMessages();
            
        String orgId = request.getParameter("orgId");
        String curPage = request.getParameter("curPage") != null ? request.getParameter("curPage") : null;
        
        if(orgId == null || orgId.equals("")){
        	messages.add(resources.getMessage("select.collectTypeOrgReport.failed"));
        	if(messages.getMessages() != null && messages.getMessages().size() > 0)
                request.setAttribute(Config.MESSAGES,messages);
            return new ActionForward("/collectType/viewCollectType.do?curPage=" + curPage);
        }
        /**该汇总方式可以汇总的机构*/
        List subOrgList = null;        
        /**该汇总方式已经设定的机构**/
        List selectedSubOrgs = null;
        /**该汇总方式可以汇总的报表**/
        List reportList = null;
        /**该汇总方式已经设定的报表**/
        List selectedReportList = null;       
        try {            
            /**查询机构汇总方式可以汇总的机构（全部子行）**/
            List childOrgList = StrutsOrgNetDelegate.selectChildOrg(orgId);
            if(childOrgList != null && childOrgList.size() > 0){
            	subOrgList = new ArrayList();
            	for(int i=0;i<childOrgList.size();i++){
            		OrgNetForm orgNetForm = (OrgNetForm)childOrgList.get(i);
            		if(orgNetForm != null)
            			subOrgList.add(new LabelValueBean(orgNetForm.getOrg_name(),orgNetForm.getOrg_id()));
            	}
            }  
            /**查询机构汇总方式可汇总的报表（全部报表）**/
           // List list = StrutsMChildReportDelegate.getAllReports();
            //2009-09-12  gongming修改，原方法不能提供版本号字段
            List list = StrutsMChildReportDelegate.getAllReport();
            if(list != null && list.size() > 0){
            	reportList = new ArrayList();
            	for(int i=0;i<list.size();i++){
            		MChildReportForm mChildReportForm = (MChildReportForm)list.get(i);            		
            		reportList.add(new LabelValueBean("[" + mChildReportForm.getChildRepId()+"]" + mChildReportForm.getReportName() + "[" 
            				+ mChildReportForm.getVersionId() + "]",mChildReportForm.getChildRepId()+"_" + mChildReportForm.getVersionId()));
            	}
            }
        }catch (Exception e){
        	e.printStackTrace();
            log.printStackTrace(e);
            messages.add(resources.getMessage("select.collectTypeOrgReport.failed"));  
            if(messages.getMessages() != null && messages.getMessages().size() > 0)
                request.setAttribute(Config.MESSAGES,messages);
            return new ActionForward("/collectType/viewCollectType.do?curPage=" + curPage);
        }
        
        OrgNet orgNet = StrutsOrgNetDelegate.selectOne(orgId);
        if(orgNet != null) request.setAttribute("orgName",orgNet.getOrgName());
        request.setAttribute("orgId",orgId);
            
        /**汇总方式机构权限及报表权限*/
        if(subOrgList!=null && subOrgList.size()!=0)
            request.setAttribute("subOrgList",subOrgList);
        if(selectedSubOrgs != null && selectedSubOrgs.size() != 0)
        	request.setAttribute("selectedSubOrgs",selectedSubOrgs);
        if(reportList != null && reportList.size() != 0)
        	request.setAttribute("reportList",reportList);
        if(selectedReportList != null && selectedReportList.size() != 0)
        	request.setAttribute("selectedReportList",selectedReportList);
        
        if(messages.getMessages() != null && messages.getMessages().size() > 0)
            request.setAttribute(Config.MESSAGES,messages);
        return mapping.findForward("add");
    }
}

