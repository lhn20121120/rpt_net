package com.cbrc.smis.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
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

import com.cbrc.smis.adapter.StrutsReportInDelegate;
import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.net.adapter.StrutsOrgNetDelegate;
import com.fitech.net.form.OrgNetForm;

/**
 * �������ͳ��
 * 
 * @author jcm
 * @serialData 2008-02-26
 */
public class ReportStatisticsAction extends Action {
	private FitechException log=new FitechException(ReportStatisticsAction.class);
	
	/**
	 * Performs action.
	 * @param mapping Action mapping.
	 * @param form Action form.
	 * @param request HTTP request.
	 * @param response HTTP response.
	 * @exception IOException if an input/output error occurs
	 * @exception ServletException if a servlet exception occurs
	 */
	public ActionForward execute(ActionMapping mapping, 
			ActionForm form,
			HttpServletRequest request, 
			HttpServletResponse response)
			throws IOException, ServletException {
		try{
			MessageResources resources = getResources(request);
	        FitechMessages messages = new FitechMessages();
	        OrgNetForm orgNetForm = (OrgNetForm) form;
	        RequestUtils.populate(orgNetForm, request);
	        
	        
	        Calendar calendar = Calendar.getInstance();
	        if(orgNetForm.getYear() == null || orgNetForm.getYear().equals(""))			   
	        	orgNetForm.setYear(new Integer(calendar.get(Calendar.YEAR)));		   
			if(orgNetForm.getTerm() == null || orgNetForm.getTerm().equals(""))			   
				orgNetForm.setTerm(new Integer(calendar.get(Calendar.MONTH)));	    
		    
			int recordCount = 0; // ��¼����
			int offset = 0; // ƫ����
			int limit = 0; // ÿҳ��ʾ�ļ�¼����
			
			List resList = null;
			ApartPage aPage = new ApartPage();	   	
			String strCurPage = request.getParameter("curPage");
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
	        
	        try{
	        	orgNetForm.setPre_org_id(operator.getOrgId()); //��ѯ��ǰ�û����¼�����	        	
	        	//ȡ�ü�¼����
	        	recordCount=StrutsOrgNetDelegate.selectSubOrgCount(orgNetForm,operator);	  
		   		//��ʾ��ҳ��ļ�¼
		   		if(recordCount > 0){
		   			resList = new ArrayList();
		   			List list = StrutsOrgNetDelegate.selectSubOrgList(orgNetForm,operator, offset, limit);
		   			ReportInForm reportInForm = new ReportInForm();
		   			reportInForm.setYear(orgNetForm.getYear());
		   			reportInForm.setTerm(orgNetForm.getTerm());
		   			OrgNetForm orgForm = null;
		   			
		   			for(Iterator iter=list.iterator();iter.hasNext();){
		   				orgForm = (OrgNetForm)iter.next();
		   				this.setSubOrgInfo(orgForm,reportInForm);
		   				
	   					resList.add(orgForm);
		   			}
		   		}
	        }catch(Exception ex){
	        	log.printStackTrace(ex);
	        	messages.add(resources.getMessage("select.dataReport.failed"));  
	        }
	        
	        //��ApartPage��������request��Χ��
		 	aPage.setTerm(this.getTerm(orgNetForm));
		 	aPage.setCount(recordCount);
		 	request.setAttribute(Config.APART_PAGE_OBJECT,aPage);	 	
	         
	        if(messages.getMessages() != null && messages.getMessages().size() > 0)
	        	request.setAttribute(Config.MESSAGES,messages);
	        if(resList != null && resList.size() > 0)
	        	request.setAttribute("form",resList);
	                
	        return mapping.findForward("view");
		}catch(Exception e){
			log.printStackTrace(e);
		}		
		return mapping.findForward("view");
	}
	
	/**
	 * �ݹ��ȡ����������������б��������Ϣ
	 * 
	 * @param orgNetForm ��ǰ������Ϣ
	 * @param reportInForm ����Ӧ��������Ϣ
	 * @return void
	 */
	private void setSubOrgInfo(OrgNetForm orgNetForm,ReportInForm reportInForm){
		if(orgNetForm != null && orgNetForm.getOrg_id() != null 
				&& reportInForm != null){
			
			//����Ӧ������
			orgNetForm.setYbReportNum(new Integer(StrutsReportInDelegate
						.selectOrgReportStatisticsYB(orgNetForm.getOrg_id(),reportInForm)));
			//�����ѱ�����
			orgNetForm.setBsReportNum(new Integer(StrutsReportInDelegate
						.selectOrgReportStatisticsBS(orgNetForm.getOrg_id(),reportInForm)));
			
			orgNetForm.setSubOrgList(StrutsOrgNetDelegate.selectSubOrgList(orgNetForm.getOrg_id()));
			
			if(orgNetForm.getSubOrgList() != null && orgNetForm.getSubOrgList().size() > 0){
				OrgNetForm orgForm = null;
				for(int i=0;i<orgNetForm.getSubOrgList().size();i++){
					orgForm = (OrgNetForm)orgNetForm.getSubOrgList().get(i);
					
					this.setSubOrgInfo(orgForm,reportInForm);
				}
			}else return;
		}
	}
	
	/**
	 * ���ò�ѯ���ݲ���
	 * 
	 * @param orgNetForm
	 * @return String ��ѯ��URL
	 */
	public String getTerm(OrgNetForm orgNetForm){	
		String term="";	   

		/**����������Ʋ�ѯ����*/
		if(orgNetForm.getOrg_name() != null && !orgNetForm.getOrg_name().equals("")){		
			term += (term.indexOf("?")>=0 ? "&" : "?");			
			term += "orgName=" + orgNetForm.getOrg_name();   		   
		}
		/**���뱨���������*/
		if(orgNetForm.getYear() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "year=" + orgNetForm.getYear();
		}
		/**���뱨����������*/
		if(orgNetForm.getTerm() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "term=" + orgNetForm.getTerm();
		}
		if(term.indexOf("?")>=0)			
			term = term.substring(term.indexOf("?")+1);
		   
		return term;   
	}
}