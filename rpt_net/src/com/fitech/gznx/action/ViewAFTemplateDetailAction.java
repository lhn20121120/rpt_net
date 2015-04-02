package com.fitech.gznx.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.action.ViewTemplateDetailAction;
import com.cbrc.smis.adapter.StrutsMChildReportDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechUtil;
import com.fitech.gznx.form.AFTemplateForm;
import com.fitech.gznx.po.AfTemplate;
import com.fitech.gznx.service.AFTemplateDelegate;

public class ViewAFTemplateDetailAction extends SystemSchemaBaseAction {
    private static FitechException log = new FitechException(ViewTemplateDetailAction.class);

    /** 
     * jdbc���� ������oracle�﷨ ����Ҫ�޸� ���Ը� 2011-12-27
     * Ӱ���af_template_curr_relation af_template_freq_relation af_template_org_relation
     * 		 af_gatherformula af_validateformula af_cellinfo qd_cellinfo af_template
     * Ӱ�����AfTemplate
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
        String reportFlg = "0";	
        HttpSession session = request.getSession();
    	if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
    		reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
    	}
        RequestUtils.populate(templateForm,request);
        if(templateForm.getBak2() != null && templateForm.getBak2().equals("3")){
        	/**jdbc���� ������oracle�﷨ ����Ҫ�޸� ���Ը� 2011-12-27
        	 * Ӱ���af_template_curr_relation af_template_freq_relation af_template_org_relation
        	 * 		 af_gatherformula af_validateformula af_cellinfo qd_cellinfo af_template*/
        	//boolean result = AFTemplateDelegate.deleteTemplate(templateForm.getTemplateId(),templateForm.getVersionId(),templateForm.getReportStyle());
        	boolean result = StrutsMChildReportDelegate.delTemplate(templateForm.getTemplateId(), templateForm.getVersionId(),
        			request.getParameter("isDelTemplateGroup"));
        	if(!result){
        		messages.add("ɾ��ģ��ʧ��");  
        	}
        
        } else {
             try{
            	 /**��ʹ��hibernate ���Ը� 2011-12-21
            	  * Ӱ�����AfTemplate**/
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
        
         //�Ƴ�request��session��Χ�ڵ�����
         FitechUtil.removeAttribute(mapping,request);
         //��ApartPage��������request��Χ��
         
         
         if(messages.getMessages() != null && messages.getMessages().size() > 0)
           request.setAttribute(Config.MESSAGES,messages);
         if(list!=null && list.size()!=0)
           request.setAttribute(Config.RECORDS,list);
         //����ѯ���������request��Χ��

         request.setAttribute("reportName",templateForm.getTemplateName());
         if(templateForm.getBak2() != null ){
        	 if(reportFlg.equals("2")){
	        	 if(templateForm.getBak2().equals("1")){
	        		 return mapping.findForward("templateDetailView");
	        	 }else if(templateForm.getBak2().equals("2")){
	        		 request.setAttribute("template_manage_flag", Config.TEMPLATE_MANAGE_FLAG);
	        		 return mapping.findForward("templateDetailEdit");
	        	 }
	         }
        	 if(reportFlg.equals("3")){
    	        
	        	 if(templateForm.getBak2().equals("1")){
	        		 return mapping.findForward("qt_templateDetailView");
	        	 }else if(templateForm.getBak2().equals("2")){
	        		 request.setAttribute("template_manage_flag", Config.TEMPLATE_MANAGE_FLAG);
	        		 return mapping.findForward("qt_templateDetailEdit");
	        	 }
             }
         }
        	 return mapping.findForward("templateList");
    }

 }
