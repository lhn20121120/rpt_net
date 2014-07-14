package com.fitech.gznx.action;

import java.io.IOException;
import java.util.List;

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

import com.cbrc.smis.action.ViewMChildReportAction;
import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechUtil;
import com.fitech.gznx.common.PageListInfo;
import com.fitech.gznx.form.AFTemplateForm;
import com.fitech.gznx.service.AFReportmerger;

public class AFReportFcwhAction extends Action  {
	private static FitechException log = new FitechException(ViewMChildReportAction.class);

    /** 
     * ��ʹ��hibernate  ���Ը� 2011-12-22
     * Ӱ�����AfTemplate
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
        
		
		HttpSession session = request.getSession();
		Operator operator = null;
		if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
			operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
		/** ����ѡ�б�־ **/
		String reportFlg = "0";
		if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
			reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
		}
        MessageResources resources = getResources(request);
        FitechMessages messages = new FitechMessages();
        //ȡ��request��Χ�ڵ�����������������logInForm��
        AFTemplateForm mChildReportForm = (AFTemplateForm) form;
         RequestUtils.populate(mChildReportForm, request);
     if(request.getAttribute("templateName")!=null)
    	 mChildReportForm.setTemplateName((String)request.getParameter("templateName"));
         int recordCount =0; //��¼����

         List list = null;
         ApartPage aPage=new ApartPage();
         int curPage = 1;
         String strCurPage=request.getParameter("curPage");
 		if(strCurPage!=null){
			if(!strCurPage.equals(""))
				curPage =  new Integer(strCurPage).intValue();
				aPage.setCurPage(new Integer(strCurPage).intValue());
		}else{
			aPage.setCurPage(1);
		}
         
         try{
             //��ʾ��ҳ��ļ�¼
        	 /**
        	  * ��ʹ��hibernate ���Ը� 2011-12-22
        	  * Ӱ�����AfTemplate**/
              PageListInfo pageList= AFReportmerger.getTemplates(mChildReportForm,reportFlg,curPage);  
              list = pageList.getList();
              recordCount = (int)pageList.getRowCount();
         }catch (Exception e){
             log.printStackTrace(e);
             messages.add(resources.getMessage("log.select.fail"));      
         }
         aPage.setCount(recordCount);
         //�Ƴ�request��session��Χ�ڵ�����
         FitechUtil.removeAttribute(mapping,request);
         //��ApartPage��������request��Χ��
         aPage.setTerm(this.getTerm(mChildReportForm));
         request.setAttribute(Config.APART_PAGE_OBJECT,aPage);
//         if(!StringUtil.isEmpty(mChildReportForm.getTemplateType()) && StringUtil.isEmpty(mChildReportForm.getTemplateTypeName())){
//        	 String templateName = AFTemplateTypeDelegate.getTemplateTypeName(mChildReportForm.getTemplateType(), reportFlg);
//        	 mChildReportForm.setTemplateTypeName(templateName);
//         }
         if(messages.getMessages() != null && messages.getMessages().size() > 0)
           request.setAttribute(Config.MESSAGES,messages);
         if(list!=null && list.size()!=0)
           request.setAttribute(Config.RECORDS,list);
         //����ѯ���������request��Χ��
         request.setAttribute("form",mChildReportForm);
         
    
		
		return mapping.findForward("index");
	}
	  /**
     * ȡ�ò�ѯ����url  
     * @param logInForm
     * @return
     */
    public String getTerm(AFTemplateForm mChildReportForm)
    {
        String term="";
        
        String reportName = mChildReportForm.getTemplateName();
        String templateType = mChildReportForm.getTemplateType();
        if(reportName!=null)
        {
            term += (term.indexOf("?")>=0 ? "&" : "?");
            term += "templateName="+reportName.toString();   
        }
        if(templateType!=null)
        {
            term += (term.indexOf("?")>=0 ? "&" : "?");
            term += "templateType="+templateType.toString();   
        }
        if(mChildReportForm.getTemplateName()!=null)
        {
            term += (term.indexOf("?")>=0 ? "&" : "?");
            term += "templateName="+mChildReportForm.getTemplateName().toString();   
        }
        if(term.indexOf("?")>=0)
            term = term.substring(term.indexOf("?")+1);
            
        // System.out.println("term"+term);
        return term;
        
    }
}
