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
import com.fitech.gznx.common.PageListInfo;
import com.fitech.gznx.form.AFReportForm;
import com.fitech.gznx.po.AfOrg;
import com.fitech.gznx.service.AFOrgDelegate;
import com.fitech.gznx.service.AFReportDelegate;

public class ViewNXDataReportAction extends Action {

	private static FitechException log=new FitechException(ViewNXDataReportAction.class);
	
	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-26
	 * Ӱ�����AfViewReport AfReport AfOrg
	 * @param result ��ѯ���ر�־,����ɹ�����true,���򷵻�false
	 * @param ReportInForm 
	 * @param request 
	 * @exception Exception ���쳣��׽���׳�
	 */
	public ActionForward execute(ActionMapping mapping,ActionForm form,
			   	HttpServletRequest request,HttpServletResponse response)throws IOException, ServletException {
		
		FitechMessages messages = new FitechMessages();
		MessageResources resources = getResources(request);	
		//List����ĳ�ʼ��
		List resList=null;
		
		// �Ƿ���Request
		AFReportForm afReportForm = (AFReportForm)form ;
		RequestUtils.populate(afReportForm, request);
		
		int recordCount =0; //��¼����

		   
		int curPage = 1;
		ApartPage aPage=new ApartPage();
		String strCurPage=request.getParameter("curPage");
		if(strCurPage!=null){
			if(!strCurPage.equals("")){
				curPage =  new Integer(strCurPage).intValue();
				aPage.setCurPage(new Integer(strCurPage).intValue());
			}
		}else
			aPage.setCurPage(1);			
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
		if (afReportForm == null) 
			afReportForm = new AFReportForm();
		
		if (afReportForm.getDate() == null || afReportForm.getDate().equals("")) {
			//�����������
			String yestoday = (String) session.getAttribute(Config.USER_LOGIN_DATE);
			afReportForm.setDate(yestoday);
		}
		if(afReportForm.getOrgId() == null)
			afReportForm.setOrgId(operator.getOrgId());
//		if(afReportForm.getSupplementFlag()==null||"".equals(afReportForm.getSupplementFlag()))
//			afReportForm.setSupplementFlag("-999");
		//ȡ��ģ������
		if(session.getAttribute(Config.REPORT_SESSION_FLG)!=null)
			afReportForm.setTemplateType(session.getAttribute(Config.REPORT_SESSION_FLG).toString());
		PageListInfo pageListInfo = null;
		try{
			/**��ʹ��hibernate ���Ը� 2011-12-26
			 * Ӱ�����AfViewReport AfReport**/
			pageListInfo = AFReportDelegate.selectNeedReportRecord(afReportForm, operator,curPage);
			recordCount = (int) pageListInfo.getRowCount();
			resList = pageListInfo.getList();
		}catch(Exception ex){
			log.printStackTrace(ex);
			messages.add(resources.getMessage("select.dataReport.failed"));		
		}
  
         //��ApartPage��������request��Χ��
		aPage.setTerm(this.getTerm(afReportForm));
		aPage.setCount(recordCount);
		request.setAttribute(Config.APART_PAGE_OBJECT,aPage);
		request.setAttribute("orgId",afReportForm.getOrgId());
		request.setAttribute("date",afReportForm.getDate());
		request.setAttribute("RequestParam", aPage.getTerm());
		
//		if(!StringUtil.isEmpty(afReportForm.getBak1()) && StringUtil.isEmpty(afReportForm.getTemplateTypeName())){
//	       	 String templateName = AFTemplateTypeDelegate.getTemplateTypeName(afReportForm.getBak1(), reportFlg);
//	       	afReportForm.setTemplateTypeName(templateName);
//	    }
		
		//20121217�޸ģ���ѡ�������ѯʱ��������������Ϣ
//		if(afReportForm.getOrgId().equals(Config.DEFAULT_VALUE)){//ȫ������
//			afReportForm.setOrgName("ȫ������");
//		}else{
//			AfOrg org = AFOrgDelegate.getOrgInfo(afReportForm.getOrgId());
//			if(org==null || org.getOrgName()==null || org.getOrgName().equals(""))
//				afReportForm.setOrgName("ȫ������");
//			else
//				afReportForm.setOrgName(org.getOrgName());
//		}
//		request.setAttribute("orgName",afReportForm.getOrgName());
		if(request.getAttribute("messagenotnull") != null)
			messages = (FitechMessages)request.getAttribute("messagenotnull");
		
		String ss = (String)request.getAttribute("checkTask");
		if(ss != null&&!ss.equals("")){
			messages.add(afReportForm.getDate()+resources.getMessage(ss));
			resList = null;
			recordCount = 0 ;
		}
		if(messages.getMessages() != null && messages.getMessages().size() > 0)
			request.setAttribute(Config.MESSAGES,messages);
		  
		if(resList!=null && resList.size()>0){
			request.setAttribute(Config.RECORDS,resList);
		}
		return mapping.findForward("view");	     
	}
	
	/**
	 * 
	 * @param afReportForm
	 * @return
	 */
	public String getTerm(AFReportForm afReportForm){
		String term="";  
		String orgId = afReportForm.getOrgId();
		String date = String.valueOf(afReportForm.getDate());	
		String repFreqId = afReportForm.getRepFreqId();
		String repName = afReportForm.getRepName();
		String stateFlag = afReportForm.getStateFlag();
		String templateId = afReportForm.getTemplateId();
//		String supplementFlag=afReportForm.getSupplementFlag();
		if(orgId!=null&&!orgId.equals("")){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "orgId="+orgId;
		}
		// add by ������
//		if(supplementFlag!=null&&!supplementFlag.equals("")){
//			term += (term.indexOf("?")>=0 ? "&" : "?");
//			term += "supplementFlag="+supplementFlag;
//		}

		if(repFreqId!=null&&!repFreqId.equals("")){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "repFreqId="+repFreqId;
		}
		if(repName!=null&&!repName.equals("")){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "repName="+repName;
		}
		if(date!=null&&!date.equals("")){		
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "date="+date.toString();
		}
		String orgName = afReportForm.getOrgName();
		if (orgName != null && !orgName.equals("")) {
			term += (term.indexOf("?") >= 0 ? "&" : "?");
			term += "orgName=" + orgName;
		}
		if(stateFlag!=null&&!stateFlag.equals("")){		
			term += (term.indexOf("?")>=0 ? "&" : "?");			
			term += "stateFlag="+stateFlag;    
		}
		if(templateId!=null&&!templateId.equals("")){		
			term += (term.indexOf("?")>=0 ? "&" : "?");			
			term += "templateId="+templateId;    
		}
		/**����ģ����������*/
		if(afReportForm.getBak1() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "bak1=" + afReportForm.getBak1();
		}
		if(term.indexOf("?")>=0)
			term = term.substring(term.indexOf("?")+1);
		return term;
	}	
}
