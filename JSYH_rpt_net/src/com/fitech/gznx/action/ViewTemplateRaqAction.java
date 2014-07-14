package com.fitech.gznx.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.adapter.StrutsMChildReportDelegate;
import com.cbrc.smis.form.MChildReportForm;
import com.fitech.gznx.po.AfTemplate;
import com.fitech.gznx.service.AFTemplateDelegate;

public class ViewTemplateRaqAction extends Action {
    public ActionForward execute(
	        ActionMapping mapping,
	        ActionForm form,
	        HttpServletRequest request,
	        HttpServletResponse response)  
	    throws IOException, ServletException {
		
        MChildReportForm mChildReportForm = (MChildReportForm) form;
        RequestUtils.populate(mChildReportForm,request);
        String curPage = request.getParameter("curPage");
        
        String childRepId = request.getParameter("childRepId");
        String versionId = request.getParameter("versionId");

    	StrutsMChildReportDelegate smr = new StrutsMChildReportDelegate();
    	mChildReportForm = smr.getMChildReport(childRepId,versionId);
    	
    	AfTemplate aft = AFTemplateDelegate.getTemplate(childRepId, versionId);
    	mChildReportForm.setPriorityFlag(aft.getPriorityFlag().toString());
    	mChildReportForm.setJoinTemplateId(aft.getJoinTemplateId());
    	
    	request.setAttribute("reportStyle", mChildReportForm.getReportStyle());
    	request.setAttribute("mChildReportForm", mChildReportForm);
    	
        return mapping.findForward("index");
	 }
}
