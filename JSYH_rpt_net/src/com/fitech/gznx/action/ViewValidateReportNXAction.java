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

import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.gznx.common.StringUtil;
import com.fitech.gznx.form.AFReportForm;
import com.fitech.gznx.service.AFReportDelegate;
import com.fitech.gznx.service.AFTemplateTypeDelegate;

/**
 * ��ѯ�踴��У��ı���
 * @author YeE
 *
 */
public class ViewValidateReportNXAction extends Action {
	
	private FitechException log=new FitechException(ViewValidateReportNXAction.class);
    
	/**
	 * @param result ��ѯ���ر�־,����ɹ�����true,���򷵻�false
	 * @param ReportInForm 
	 * @param request 
	 * @exception Exception ���쳣��׽���׳�
	 */
	public ActionForward execute(
      ActionMapping mapping,
      ActionForm form,
      HttpServletRequest request,
      HttpServletResponse response   
	)throws IOException, ServletException {
		
		FitechMessages messages = new FitechMessages();	   
		MessageResources resources = getResources(request);	   
		AFReportForm reportInForm = (AFReportForm) form;
	    RequestUtils.populate(reportInForm, request);
	    
//	    if(reportInForm.getYear()!=null && reportInForm.getYear().intValue()==0) reportInForm.setYear(null);
//	    if(reportInForm.getTerm()!=null && reportInForm.getTerm().intValue()==0) reportInForm.setTerm(null);	    
	    if(reportInForm.getSupplementFlag()==null||"".equals(reportInForm.getSupplementFlag()))
			reportInForm.setSupplementFlag("-999");
		int recordCount = 0; // ��¼����
		int offset = 0; // ƫ����
		int limit = 0; // ÿҳ��ʾ�ļ�¼����
		
		List resList=null;
		ApartPage aPage=new ApartPage();
		String strCurPage=request.getParameter("curPage");
		if(strCurPage!=null){		
			if(!strCurPage.equals(""))		    
				aPage.setCurPage(new Integer(strCurPage).intValue());
		}
		else
			aPage.setCurPage(1);
		//����ƫ����
		offset=(aPage.getCurPage()-1)*Config.PER_PAGE_ROWS; 		
		limit = Config.PER_PAGE_ROWS;
		
        HttpSession session = request.getSession();
        Operator operator = null; 
        if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
            operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
        
        /** ����ѡ�б�־ **/
		String reportFlg = "";
		if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
			reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
		}
		if(StringUtil.isEmpty(reportInForm.getDate())){
			String yestoday = (String) session.getAttribute(Config.USER_LOGIN_DATE);
			reportInForm.setDate(yestoday);	
		}
        //ȡ��ģ������
		reportInForm.setTemplateType(reportFlg);
        
        try {
	   		//ȡ�ü�¼����
        	/**��ʹ��hibernate ���Ը� 2011-12-27
        	 * Ӱ�����AfReport AfTemplate**/
	   		recordCount = AFReportDelegate.getRecordCountOfmanual(reportInForm,
	   							operator,Config.CHECK_FLAG_UNCHECK + Config.SPLIT_SYMBOL_COMMA + Config.CHECK_FLAG_PASS);	  
	   		//��ʾ��ҳ��ļ�¼
	   		if(recordCount > 0)
	   			/**��ʹ��hibernate ���Ը� 2011-12-27
	   			 * Ӱ�����AfReport AfTemplate**/
		   	    resList = AFReportDelegate.selectOfManual(reportInForm,offset,
		   	    				limit, operator, Config.CHECK_FLAG_UNCHECK + Config.SPLIT_SYMBOL_COMMA + Config.CHECK_FLAG_PASS);
        }catch (Exception e){			
        	log.printStackTrace(e);
			messages.add(resources.getMessage("manualCheck.get.failed"));		
		} 
                
		//��ApartPage��������request��Χ��
	 	aPage.setTerm(this.getTerm(reportInForm));
	 	aPage.setCount(recordCount);
	 	request.setAttribute(Config.APART_PAGE_OBJECT,aPage);
//	 	if(!StringUtil.isEmpty(reportInForm.getBak1()) && StringUtil.isEmpty(reportInForm.getTemplateTypeName())){
//	       	 String templateName = AFTemplateTypeDelegate.getTemplateTypeName(reportInForm.getBak1(), reportFlg);
//	       	reportInForm.setTemplateTypeName(templateName);
//	        }
	 	if(messages.getMessages() != null && messages.getMessages().size() > 0)		
	 		request.setAttribute(Config.MESSAGES,messages);
	 	 	 	
	 	request.setAttribute("SelectedFlag",reportInForm.getAllFlags());
	 	
	 	if(resList!=null && resList.size()>0){    	
	 		request.setAttribute("form",resList);     
	 	}     	 	
	 	return mapping.findForward("viewCheck");  
	}
   
	/**
	 * ���ò�ѯ���ݲ���
	 * 
	 * @param reportInForm
	 * @return String ��ѯ��URL
	 */
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
//		if(reportInForm.getFrOrFzType() != null){
//			term += (term.indexOf("?")>=0 ? "&" : "?");
//			term += "frOrFzType=" + reportInForm.getFrOrFzType();
//		}
		/**���뱨��Ƶ������*/
		if(reportInForm.getRepFreqId() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "repFreqId=" + reportInForm.getRepFreqId();
		}
		/**���뱨���������� add by ������*/
		if(reportInForm.getSupplementFlag() != null&&!"".equals(reportInForm.getSupplementFlag())){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "supplementFlag=" + reportInForm.getSupplementFlag();
		}
		/**���뱨��date����*/
		if(reportInForm.getDate() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "date=" + reportInForm.getDate();
		}
		/**���뱨���������*/
//		if(reportInForm.getYear() != null){
//			term += (term.indexOf("?")>=0 ? "&" : "?");
//			term += "year=" + reportInForm.getYear();
//		}
//		/**���뱨����������*/
//		if(reportInForm.getTerm() != null){
//			term += (term.indexOf("?")>=0 ? "&" : "?");
//			term += "term=" + reportInForm.getTerm();
//		}
		/**���뱨�ͻ�������*/
		if(reportInForm.getOrgId() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "orgId=" + reportInForm.getOrgId();
		}
		/**����ģ����������*/
		if(reportInForm.getBak1() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "bak1=" + reportInForm.getBak1();
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
