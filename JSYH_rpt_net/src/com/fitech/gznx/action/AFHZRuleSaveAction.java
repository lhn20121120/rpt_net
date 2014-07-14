package com.fitech.gznx.action;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.AfTemplateCollRuleForm;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.gznx.form.OrgInfoForm;
import com.fitech.gznx.service.AFTemplateCollRuleDelegate;

public class AFHZRuleSaveAction extends Action {
	private static FitechException log = new FitechException(AFHZRuleSaveAction.class);

    /** 
     * 已使用Hibernate 卞以刚 2011-12-27
     * 影响对象：AfTemplate
     * Method execute
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException
	{
        
		Locale locale=getLocale(request);
		HttpSession session = request.getSession();
		Operator operator = null;
		if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
			operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
		/** 报表选中标志 **/
		String reportFlg = "0";
		if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
			reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
		}
        MessageResources resources = getResources(request);
        FitechMessages messages = new FitechMessages();
        
        //取得request范围内的请求参数，并存放在afRuleForm内
        boolean flag=false;
        AfTemplateCollRuleForm afRuleForm = (AfTemplateCollRuleForm) form;
        RequestUtils.populate(afRuleForm, request);
        String orgName=request.getParameter("orgName");
//        if(orgName!=null&&!"".equals(orgName)){
//        	orgName=new String(orgName.getBytes("ISO-8859-1"),"GBK");
//        }
        String orgId=request.getParameter("orgId");
        String opType=request.getParameter("optype");
        String templateId=request.getParameter("templateId");
        String versionId=request.getParameter("versionId");
        String reportName=request.getParameter("reportName");
//        if(reportName!=null&&!"".equals(reportName)){
//        	reportName=new String (reportName.getBytes("ISO-8859-1"));
//        }
        String collFormula=request.getParameter("collFormula");
        String hzStyle=request.getParameter("hzStyle");
        String collSchema=request.getParameter("collSchema");
        if ("0".equals(hzStyle)) {
        	afRuleForm.setHz_style(hzStyle);
        	 afRuleForm.setColl_schema(collSchema);
             afRuleForm.setOrg_id(orgId);
             afRuleForm.setOrg_name(orgName);
             afRuleForm.setTemplate_id(templateId);
             afRuleForm.setVersion_id(versionId);
             if(Config.HZGS_TJH.equals(collSchema)){
            	 afRuleForm.setColl_formula(afRuleForm.getPreOrgId()+"-sum("+Config.HZGS_TJH+")");
             }
             if(Config.HZGS_ZDY.equals(collSchema)){
            	 afRuleForm.setColl_formula(afRuleForm.getPreOrgId()+"-sum("+collFormula+")");
             }
             
             afRuleForm.setColl_schema("1");
             request.setAttribute("form", afRuleForm);
             if(AFTemplateCollRuleDelegate.isExistCollRule(afRuleForm)){
            	 flag=AFTemplateCollRuleDelegate.updateAfTemplateCollRule(afRuleForm);
             }else{
            	 flag=AFTemplateCollRuleDelegate.add(afRuleForm, operator.getOperatorId()+"");
              }
             if(flag){
             	messages.add("保存成功");
             }else{
             	messages.add("保存失败");
             	
             }

		}else if("1".equals(hzStyle)){
            afRuleForm.setTemplate_id(templateId);
            afRuleForm.setVersion_id(versionId);
            afRuleForm.setOrg_id(orgId);

			AFTemplateCollRuleDelegate.delete(afRuleForm,operator.getOperatorId().toString());
		}
               request.setAttribute("Message", messages);
               if(opType!=null&&!"".equals(opType)&&!"null".equals(opType)){
            	   return new ActionForward("/gznx/EditHZGS.do" + 
            			   "?templateId=" + templateId + 
            			   "&versionId=" + versionId + 
            			   "&reportName="+reportName+
            			   "&orgId="+orgId+
            	   "&opration=edit");
            	  
               }else{
            	   return new ActionForward("/gznx/hzformula.do" + 
              				"?childRepId=" + templateId + 
              				"&versionId=" + versionId + 				
              				"&reportName="+reportName+
              				"&templateId="+templateId+
              				"&orgId="+orgId+
              				"&orgName="+orgName+
              				"&opration=next");
               }
       
        	
        
     
         
         
         
		
	
	}
	  /**
     * 取得查询条件url  
     * @param logInForm
     * @return
     */
    public String getTerm(OrgInfoForm orgInfoForm)
    {
        String term="";
        
        String orgName = orgInfoForm.getOrgName();
        //String templateType = orgInfoForm.getTemplateType();
        if(orgName!=null)
        {
            term += (term.indexOf("?")>=0 ? "&" : "?");
            term += "orgName="+orgName.toString();   
        }
        
        if(term.indexOf("?")>=0)
            term = term.substring(term.indexOf("?")+1);
            
        // System.out.println("term"+term);
        return term;
        
    }

}
