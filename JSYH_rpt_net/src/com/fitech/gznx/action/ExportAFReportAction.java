package com.fitech.gznx.action;

import java.io.IOException;
import java.util.Calendar;
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
import com.fitech.gznx.common.DateUtil;
import com.fitech.gznx.common.PageListInfo;
import com.fitech.gznx.common.StringUtil;
import com.fitech.gznx.form.AFReportForm;
import com.fitech.gznx.service.AFReportProductDelegate;
import com.fitech.gznx.service.AFTemplateTypeDelegate;

public class ExportAFReportAction extends Action {
    private static FitechException log = new FitechException(ExportAFReportAction.class);

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
        
    	
        MessageResources resources = getResources(request);
        FitechMessages messages = new FitechMessages();
        //ȡ��request��Χ�ڵ�����������������reportForm��
        AFReportForm reportInForm = (AFReportForm)form ;
        RequestUtils.populate(reportInForm, request);
                 
        int recordCount =0; //��¼����		
        int offset = 0; // ƫ����
		int limit = 0; // ÿҳ��ʾ�ļ�¼����
		   
		//List����ĳ�ʼ��
		List resList=null;

		ApartPage aPage=new ApartPage();

		String strCurPage=request.getParameter("curPage");
		if(strCurPage!=null){
			if(!strCurPage.equals(""))
				aPage.setCurPage(new Integer(strCurPage).intValue());
		}else
			aPage.setCurPage(1);
		//����ƫ����
		offset=(aPage.getCurPage()-1)*Config.PER_PAGE_ROWS; 		
		limit = Config.PER_PAGE_ROWS;
			
        /**
         * ȡ�õ�ǰ�û���Ȩ����Ϣ
         */   
		HttpSession session = request.getSession();
		Operator operator = null; 
		if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
			operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);          
		/** ����ѡ�б�־ **/
		String reportFlg = "0";
		if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
			reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
		}

		if(StringUtil.isEmpty(reportInForm.getDate())){
			String yestoday = (String) session.getAttribute(Config.USER_LOGIN_DATE);
			reportInForm.setDate(yestoday);	
		}

		try{
			if(reportFlg.equals("1")){
				/**jdbc���� ����oracle�﷨(upper) ���Ը� 2011-12-22**/
				recordCount = AFReportProductDelegate.selectYJHExportReportCount(reportInForm,operator);
				/**jdbc���� ����oracle�﷨(upper) ���Ը� 2011-12-22**/
				resList = AFReportProductDelegate.selectYJHExportReportList(reportInForm,operator,offset,limit);
			} else{
				//���ñ�������Ϊ���ǲ�¼�ı���
				reportInForm.setIsReport(Integer.valueOf(com.fitech.gznx.common.Config.TEMPLATE_REPORT));
				/**jdbc���� ����oracle�﷨ ���޸� ���Ը� 2011-12-22**/
				recordCount = AFReportProductDelegate.selectNOTYJHExportReportCount(reportInForm,operator,reportFlg);
				/**jdbc���� oracle�﷨(rownum) ���޸� ���Ը� 2011-12-22**/
				resList = AFReportProductDelegate.selectNOTYJHExportReportList(reportInForm,operator,offset,limit,reportFlg);

			}
		}catch(Exception ex){
			log.printStackTrace(ex);
			messages.add(resources.getMessage("select.dataReport.failed"));		
		}
  
         //��ApartPage��������request��Χ��
		aPage.setTerm(this.getTerm(reportInForm));		 	
		aPage.setCount(recordCount);
		request.setAttribute(Config.APART_PAGE_OBJECT,aPage);
		request.setAttribute("orgId",reportInForm.getOrgId());
		request.setAttribute("date",reportInForm.getDate());

		request.setAttribute("reportName", reportInForm.getRepName());
//		if(!StringUtil.isEmpty(reportInForm.getBak1()) && StringUtil.isEmpty(reportInForm.getTemplateTypeName())){
//	       	 String templateName = AFTemplateTypeDelegate.getTemplateTypeName(reportInForm.getBak1(), reportFlg);
//	       	reportInForm.setTemplateTypeName(templateName);
//	    }
		if(messages.getMessages() != null && messages.getMessages().size() > 0)		
			request.setAttribute(Config.MESSAGES,messages);
		  
		if(resList!=null && resList.size()>0){
			request.setAttribute(Config.RECORDS,resList);   
		}
		return mapping.findForward("index");	 
    }
    public String getTerm(AFReportForm reportInForm){	   
		String term="";
		
		/**���뱨��������*/
		if(reportInForm.getTemplateId() != null && !reportInForm.getTemplateId().equals("")){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "templateId=" + reportInForm.getTemplateId();
		}
		/**���뱨����������*/
		if(reportInForm.getRepName() != null && !reportInForm.getRepName().equals("")){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "repName=" + reportInForm.getRepName();
		}
		/**����ģ����������*/
		if(reportInForm.getBak1() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "bak1=" + reportInForm.getBak1();
		}
		/**���뱨��Ƶ������*/
		if(reportInForm.getRepFreqId() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "repFreqId=" + reportInForm.getRepFreqId();
		}

		/**���뱨����������*/
		if(reportInForm.getDate() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "date=" + reportInForm.getDate();
		}	
		if(reportInForm.getOrgId() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "orgId=" + reportInForm.getOrgId();
		}
		if(reportInForm.getSupplementFlag() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "supplementFlag=" + reportInForm.getSupplementFlag();
		}
		String orgName = reportInForm.getOrgName();
		if(orgName!=null&&!orgName.equals("")){		
			term += (term.indexOf("?")>=0 ? "&" : "?");			
			term += "orgName="+orgName;    
		}

		if(term.indexOf("?")>=0)		
			term = term.substring(term.indexOf("?")+1);
		
		return term;   
	}
}
