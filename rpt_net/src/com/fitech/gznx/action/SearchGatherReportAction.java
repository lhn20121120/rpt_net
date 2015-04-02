package com.fitech.gznx.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.security.Operator;
import com.fitech.gznx.form.AFReportForm;
import com.fitech.gznx.po.AfTemplate;
import com.fitech.gznx.service.AFTemplateDelegate;

public class SearchGatherReportAction extends Action {

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// �Ƿ���Request
		AFReportForm afReportForm = (AFReportForm)form ;
		RequestUtils.populate(afReportForm, request);
		
		int recordCount =0; //��¼����
	   
		//List����ĳ�ʼ��
		List resList=null;
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
		
		if (afReportForm == null) 
			afReportForm = new AFReportForm();
		
		if (afReportForm.getDate() == null || afReportForm.getDate().equals("")) {
			//�����������
			String yestoday = (String) session.getAttribute(Config.USER_LOGIN_DATE);
			afReportForm.setDate(yestoday);
		}
		
		AfTemplate af = new AfTemplate();
		//��¼ģ����� templateType=3  isReport=1
		af.setTemplateType("3");
		af.setIsReport(new Long(1));
		List<AfTemplate> templateList = AFTemplateDelegate.findGatherReport(af);
		request.setAttribute("templateList", templateList);
		
		  //��ApartPage��������request��Χ��
		aPage.setTerm(this.getTerm(afReportForm));
		aPage.setCount(recordCount);
		request.setAttribute(Config.APART_PAGE_OBJECT,aPage);
		request.setAttribute("orgId",afReportForm.getOrgId());
		request.setAttribute("date",afReportForm.getDate());
		request.setAttribute("RequestParam", aPage.getTerm());
		
		//��ѯ��¼ģ��ͳ����Ϣ����ʵ��
		//...
		//��ѯ��¼ģ��ͳ����Ϣ  ����
		return mapping.findForward("INDEX");
	}

	public String getTerm(AFReportForm afReportForm){
		String term="";  
		String orgId = afReportForm.getOrgId();
		String date = String.valueOf(afReportForm.getDate());	
		String repFreqId = afReportForm.getRepFreqId();
		String repName = afReportForm.getRepName();
		
		if(orgId!=null&&!orgId.equals("")){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "orgId="+orgId;
		}
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
		if(orgName!=null&&!orgName.equals("")){		
			term += (term.indexOf("?")>=0 ? "&" : "?");			
			term += "orgName="+orgName;    
		}
		
		if(term.indexOf("?")>=0)
			term = term.substring(term.indexOf("?")+1);
		
		return term;
	}	
}
