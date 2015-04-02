package com.cbrc.smis.action;

import java.io.IOException;
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

import com.cbrc.smis.adapter.StrutsReportInDelegate;
import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.gznx.action.SystemSchemaBaseAction;
import com.fitech.gznx.common.StringUtil;

/**
 * @�����ѯ�Ĺ���Action
 * @author ����
 *
 */
public final class ViewReportInAction extends SystemSchemaBaseAction {
	private FitechException log=new FitechException(ViewReportInAction.class);
	     
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
		ReportInForm reportInForm = (ReportInForm)form ;
	    RequestUtils.populate(reportInForm, request);
	    request.setAttribute("schemaRepIds", request.getParameter("schemaRepIds"));
	    
        HttpSession session = request.getSession();		
	    String yestoday = (String) session.getAttribute(Config.USER_LOGIN_DATE);
		 if(reportInForm.getDate() ==null || reportInForm.getDate().equals("")){
		    	reportInForm.setDate(yestoday.substring(0, 7));
		    	reportInForm.setYear(new Integer(yestoday.substring(0,4)));
		    	reportInForm.setTerm(new Integer(yestoday.substring(5,7)));
		    }else{
		    	reportInForm.setYear(new Integer(reportInForm.getDate().split("-")[0]));
		    	reportInForm.setTerm(new Integer(reportInForm.getDate().split("-")[1]));
		    }
	    if(reportInForm.getYear()!=null && reportInForm.getYear().intValue()==0) reportInForm.setYear(null);
	    if(reportInForm.getTerm()!=null && reportInForm.getTerm().intValue()==0) reportInForm.setTerm(null);
	    
		int recordCount = 0; // ��¼����
		int offset = 0; // ƫ����
		int limit = 0; // ÿҳ��ʾ�ļ�¼����
		
		// List����ĳ�ʼ��	   
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

        Operator operator = null; 
        if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
            operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
        
        
        /******************��������ѯ(begin)********************/
		String workTaskTemp = request.getParameter("workTaskTemp");
		StringBuffer wtb = new StringBuffer("");
		String hsqlOld = "";
		if(Config.SYSTEM_SCHEMA_FLAG==1 && workTaskTemp!=null && !workTaskTemp.equals("")){
			String nodeFlag = request.getParameter("nodeFlag");
			if(nodeFlag!=null)
				request.setAttribute("nodeFlag",nodeFlag);
			session.setAttribute(Config.REPORT_SESSION_FLG, "1");
			String workTaskTerm = request.getParameter("workTaskTerm");
			String[] workTaskTerms = workTaskTerm.split("-");
			reportInForm.setYear(new Integer(workTaskTerms[0]));
			reportInForm.setTerm(new Integer(workTaskTerms[1]));
			reportInForm.setOrgId(request.getParameter("workTaskOrgId"));
			reportInForm.setDate(workTaskTerm);
			hsqlOld = operator.getChildRepCheckPodedom();
			String[] workTaskTemps = workTaskTemp.split(",");
			for(int i=0;i<workTaskTemps.length;i++)
				wtb.append(",'" + reportInForm.getOrgId() + workTaskTemps[i] + "'");
			operator.setChildRepCheckPodedom(wtb.substring(1));
			request.setAttribute("workTaskTerm", request.getParameter("workTaskTerm"));
			request.setAttribute("workTaskOrgId", request.getParameter("workTaskOrgId"));
			request.setAttribute("workTaskTemp", request.getParameter("workTaskTemp"));
		}
		/******************��������ѯ(end)********************/
        
        try {			
        	/*add ������  ���������ģʽ �Ͱ���˹��ĺ�û����˵�ȫ�����ҳ���*/
        	if(this.getSchemaFlag() == 1){
        		recordCount = StrutsReportInDelegate.getRecordCountFlag(reportInForm, operator, Config.CHECK_FLAG_PASS.toString()+","+Config.CHECK_FLAG_FAILED+","+Config.CHECK_FLAG_AFTERJY+","+Config.CHECK_FLAG_UNREPORT+","+Config.CHECK_FLAG_AFTERSAVE);
        	}else{
        		//091030 �ָ÷�����������踴�˵ļ�¼
    	   		//ȡ�ü�¼����
    	   		recordCount = StrutsReportInDelegate.getRecordCountOfmanual(reportInForm,operator);	
        	}  
	   		//��ʾ��ҳ��ļ�¼
	   		if(recordCount > 0)
	   			/*add ������ ���������ģʽ �Ͱ���˹��ĺ�û����˵�ȫ����ʾ����*/
	   			if(this.getSchemaFlag() == 1){
	   				resList = StrutsReportInDelegate.selectOfFlag(reportInForm, offset, limit, operator, Config.CHECK_FLAG_PASS.toString()+","+Config.CHECK_FLAG_FAILED+","+Config.CHECK_FLAG_AFTERJY+","+Config.CHECK_FLAG_UNREPORT+","+Config.CHECK_FLAG_AFTERSAVE);
	   			}else{
	   				resList = StrutsReportInDelegate.selectOfManual(reportInForm,offset,limit,operator);
	   			}	
        }catch (Exception e){			
        	log.printStackTrace(e);
			messages.add(resources.getMessage("manualCheck.get.failed"));		
		}
        
        /******************��������ѯ(begin)********************/
		if(Config.SYSTEM_SCHEMA_FLAG==1 && workTaskTemp!=null && !workTaskTemp.equals(""))
			operator.setChildRepCheckPodedom(hsqlOld);
		/******************��������ѯ(end)********************/
		
        		
		//��ApartPage��������request��Χ��
	 	aPage.setTerm(this.getTerm(reportInForm));
	 	aPage.setCount(recordCount);
	 	request.setAttribute(Config.APART_PAGE_OBJECT,aPage);
	 		 	 
	 	if(messages.getMessages() != null && messages.getMessages().size() > 0)		
	 		request.setAttribute(Config.MESSAGES,messages);
	 	 	 	
	 	request.setAttribute("SelectedFlag",reportInForm.getAllFlags());
	 	
	 	if(resList!=null && resList.size()>0){    	
	 		request.setAttribute("form",resList);     
	 	}  
		// add by ������ ������ģʽ��ӵ�request��
	 	request.setAttribute("system_schema_flag",this.getSchemaFlag());
	 	request.setAttribute("TEMPLATE_MANAGE_FLAG", Config.TEMPLATE_MANAGE_FLAG);
	 	if(this.getSchemaFlag() == 1){
	 		return mapping.findForward("viewCheckFlag");
	 	}else{
	 		return mapping.findForward("viewCheck");
	 	}  
	}
   
	/**
	 * ���ò�ѯ���ݲ���
	 * 
	 * @param reportInForm
	 * @return String ��ѯ��URL
	 */
	public String getTerm(ReportInForm reportInForm){	   
		String term="";
		
		/**���뱨��������*/
		if(reportInForm.getChildRepId() != null && !reportInForm.getChildRepId().equals("")){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "childRepId=" + reportInForm.getChildRepId();
		}
		/**���뱨����������*/
		if(reportInForm.getRepName() != null && !reportInForm.getRepName().equals("")){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "repName=" + reportInForm.getRepName();
		}
		/**����ģ����������*/
		if(reportInForm.getFrOrFzType() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "frOrFzType=" + reportInForm.getFrOrFzType();
		}
		/**����ģ�屨��Ƶ��*/
		if(reportInForm.getRepFreqId() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "repFreqId=" + reportInForm.getRepFreqId();
		}
		/**���뱨��ʱ������*/
		if(reportInForm.getDate() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "date=" + reportInForm.getDate();
		}
		/**���뱨���������*/
		if(reportInForm.getYear() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "year=" + reportInForm.getYear();
		}
		/**���뱨����������*/
		if(reportInForm.getTerm() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "term=" + reportInForm.getTerm();
		}
		/**���뱨�ͻ�������*/
		if(reportInForm.getOrgId() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "orgId=" + reportInForm.getOrgId();
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