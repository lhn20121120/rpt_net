package com.fitech.gznx.action;

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
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.action.ViewTemplateDetailAction;
import com.cbrc.smis.adapter.StrutsMChildReportDelegate;
import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.MChildReportForm;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechUtil;
import com.fitech.gznx.form.AFTemplateForm;
import com.fitech.gznx.po.AfTemplate;
import com.fitech.gznx.service.AFTemplateDelegate;

public class ViewAFTemplateDetailAction extends Action {
    private static FitechException log = new FitechException(ViewTemplateDetailAction.class);

    /** 
     * jdbc技术 无特殊oracle语法 不需要修改 卞以刚 2011-12-27
     * 影响表：af_template_curr_relation af_template_freq_relation af_template_org_relation
     * 		 af_gatherformula af_validateformula af_cellinfo qd_cellinfo af_template
     * 影响对象：AfTemplate
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
        HttpServletResponse response)  
    throws IOException, ServletException
   {
        MessageResources resources = getResources(request);
        FitechMessages messages = new FitechMessages();
        List list = new ArrayList();
        AFTemplateForm templateForm = new AFTemplateForm();
        RequestUtils.populate(templateForm,request);
        if(templateForm.getBak2() != null && templateForm.getBak2().equals("3")){
        	/**jdbc技术 无特殊oracle语法 不需要修改 卞以刚 2011-12-27
        	 * 影响表：af_template_curr_relation af_template_freq_relation af_template_org_relation
        	 * 		 af_gatherformula af_validateformula af_cellinfo qd_cellinfo af_template*/
        	boolean result = AFTemplateDelegate.deleteTemplate(templateForm.getTemplateId(),templateForm.getVersionId());
        	if(!result){
        		messages.add("删除模板失败");  
        	}
        
        } else {
             try{
            	 /**已使用hibernate 卞以刚 2011-12-21
            	  * 影响对象：AfTemplate**/
            	 AfTemplate aftemplate = AFTemplateDelegate.getTemplate(templateForm.getTemplateId(),templateForm.getVersionId());
            	 AFTemplateForm aftemplateform = new AFTemplateForm();
            	 aftemplateform.setTemplateId(aftemplate.getId().getTemplateId());
            	 aftemplateform.setVersionId(aftemplate.getId().getVersionId());
            	 aftemplateform.setEndDate(aftemplate.getEndDate());
            	 aftemplateform.setIsCollect(String.valueOf(aftemplate.getIsCollect()));
            	 aftemplateform.setIsReport(String.valueOf(aftemplate.getIsReport()));
            	 aftemplateform.setUsingFlag(String.valueOf(aftemplate.getUsingFlag()));
            	 aftemplateform.setStartDate(aftemplate.getStartDate());
            	 aftemplateform.setTemplateName(aftemplate.getTemplateName());
            	 templateForm.setTemplateName(aftemplate.getTemplateName());
            	 if(aftemplate.getTemplateType().equals(com.fitech.gznx.common.Config.OTHER_REPORT) && 
            			 aftemplate.getReportStyle()!=null && aftemplate.getReportStyle().intValue()==Integer.valueOf(com.fitech.gznx.common.Config.REPORT_QD)){
            		 aftemplateform.setReportStyle(com.fitech.gznx.common.Config.REPORT_QD);
            	 }
    				
    			list.add(aftemplateform);
             }catch (Exception e) 
             {
                 log.printStackTrace(e);
                 messages.add(resources.getMessage("log.select.fail"));      
             }
        }
        
         //移除request或session范围内的属性
         FitechUtil.removeAttribute(mapping,request);
         //把ApartPage对象存放在request范围内
         
         
         if(messages.getMessages() != null && messages.getMessages().size() > 0)
           request.setAttribute(Config.MESSAGES,messages);
         if(list!=null && list.size()!=0)
           request.setAttribute(Config.RECORDS,list);
         //将查询条件存放在request范围内

         request.setAttribute("ReportName",templateForm.getTemplateName());
         if(templateForm.getBak2() != null ){
        	 if(templateForm.getBak2().equals("3")){
        		 return mapping.findForward("templateList");
        	 }else if(templateForm.getBak2().equals("2")){
        		 return mapping.findForward("templateDetailEdit");
        	 }
         }
        
         return mapping.findForward("templateDetailView");
    }

 }
