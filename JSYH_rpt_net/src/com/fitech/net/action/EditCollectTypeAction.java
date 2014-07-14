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
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.adapter.StrutsMChildReportDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.MChildReportForm;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.net.adapter.StrutsCollectTypeDelegate;
import com.fitech.net.adapter.StrutsOrgNetDelegate;
import com.fitech.net.form.CollectTypeForm;
import com.fitech.net.form.OrgNetForm;
import com.fitech.net.hibernate.OrgNet;

/**
 * @ 汇总方式修改展现Action
 * @ author jcm
 *
 */
public final class EditCollectTypeAction extends Action {
	private FitechException log=new FitechException(EditCollectTypeAction.class);
	
	
   /**
    * @param result 查询返回标志,如果成功返回true,否则返回false
    * @param CollectTypeForm 
    * @param request 
    * @exception Exception 有异常捕捉并抛出
    */
	public ActionForward execute(
      ActionMapping mapping,
      ActionForm form,
      HttpServletRequest request,
      HttpServletResponse response
	)throws IOException, ServletException {

		MessageResources resources=getResources(request);
        FitechMessages messages = new FitechMessages();
         
        CollectTypeForm collectTypeForm = (CollectTypeForm)form;
        RequestUtils.populate(collectTypeForm, request);
        
        String curPage = request.getParameter("curPage") != null ? request.getParameter("curPage") : null;
        String orgId = request.getParameter("orgId");
        String collectId = request.getParameter("collectId");
        
        if(orgId == null || orgId.equals("") || collectId  == null || collectId.equals("")){
        	messages.add(resources.getMessage("edit.collectType.failed"));
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
        	collectTypeForm = StrutsCollectTypeDelegate.selectCollectType(new Integer(collectId));
        	collectTypeForm.setOrgId(orgId);
        	 
            /**查询机构汇总方式已经设定的机构*/
        	List listSelectedOrg = StrutsCollectTypeDelegate.searchSelectedCollectOrg(orgId,new Integer(collectId));
        	if(listSelectedOrg != null && listSelectedOrg.size() > 0){
        		selectedSubOrgs = new ArrayList();
        		for(int i=0;i<listSelectedOrg.size();i++){
        			OrgNet orgNet = (OrgNet)listSelectedOrg.get(i);
        			selectedSubOrgs.add(new LabelValueBean(orgNet.getOrgName(),orgNet.getOrgId()));
        		}
        	}

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
            
            /**查询汇总方式已经选择的报表**/
            List listSelectedReport = StrutsCollectTypeDelegate.searchSelectedCollectReport(orgId,new Integer(collectId));
            if(listSelectedReport != null && listSelectedReport.size() > 0){
            	selectedReportList = new ArrayList();
                
            	for(int i=0;i<listSelectedReport.size();i++){
            		MChildReportForm mChildReportForm = (MChildReportForm)listSelectedReport.get(i);
                    selectedReportList.add(new LabelValueBean("[" + mChildReportForm.getChildRepId()+"]" + mChildReportForm.getReportName() + "[" 
            				+ mChildReportForm.getVersionId() + "]",mChildReportForm.getChildRepId()+"_" + mChildReportForm.getVersionId()));
            	}
            }
            
            /**查询机构汇总方式可汇总的报表（全部报表）**/
            //List list = StrutsMChildReportDelegate.getAllReports();
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
        	messages.add(resources.getMessage("edit.collectType.failed"));
        	if(messages.getMessages() != null && messages.getMessages().size() > 0)
                request.setAttribute(Config.MESSAGES,messages);
            return new ActionForward("/collectType/viewCollectType.do?curPage=" + curPage); 
        }
        
        OrgNet orgNet = StrutsOrgNetDelegate.selectOne(orgId);
        if(orgNet != null) request.setAttribute("orgName",orgNet.getOrgName());
        request.setAttribute("orgId",orgId);
        request.setAttribute("collectName",collectTypeForm.getCollectName());
        request.setAttribute("collectId",collectId);
            
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
        return mapping.findForward("edit");
    }
}