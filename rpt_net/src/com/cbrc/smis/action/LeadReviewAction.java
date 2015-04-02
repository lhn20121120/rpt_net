package com.cbrc.smis.action;

import java.util.ArrayList;
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
import com.cbrc.smis.entity.AfTemplateReview;
import com.cbrc.smis.other.Aditing;
import com.cbrc.smis.security.Operator;
import com.fitech.gznx.form.AFReportForm;
import com.fitech.gznx.service.AFReportProductDelegate;
import com.fitech.gznx.service.AFTemplateReviewDelegate;
import com.ibm.db2.jcc.a.a;

public class LeadReviewAction extends Action{
	
	/***
	 * �쵼���Ĺ���Action
	 */
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// TODO Auto-generated method stub
		//ȡ��request��Χ�ڵ�����������������reportForm��
        AFReportForm reportInForm = (AFReportForm)form ;
        RequestUtils.populate(reportInForm, request);
        
        
        int recordCount =0; //��¼����		
        int offset = 0; // ƫ����
		int limit = 0; // ÿҳ��ʾ�ļ�¼����
		int flagPass = 0;
		
			
		
		//List����ĳ�ʼ��
		List<Aditing> resList=null;

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
		
		flagPass = AFTemplateReviewDelegate.findCount();//��ѯ���ĸ���
		
		recordCount = AFReportProductDelegate.selectYJHExportReportCount(reportInForm,operator,0);
		List<Aditing> aditingList = AFReportProductDelegate.selectYJHExportReportList(reportInForm,operator,offset,limit,0);
		if(aditingList!=null && aditingList.size()>0){
			resList = new ArrayList<Aditing>();
			
			for(int i=0;i<aditingList.size();i++){
				Aditing a = (Aditing)aditingList.get(i);
				AfTemplateReview review = new AfTemplateReview();
				review.getId().setTemplateId(a.getChildRepId());//ģ��ID
				review.getId().setVersionId(a.getVersionId());//�汾��
				review.getId().setTerm(reportInForm.getTerm());
				List<AfTemplateReview> reviewList = AFTemplateReviewDelegate.findAFTemplateReview(review);//��ѯ�쵼��������
				
				if(reviewList!=null && reviewList.size()>0){//����������
					review = reviewList.get(0);
					a.setReviewStatus(review.getReviewStatus());
					if(reportInForm.getReviewStatus().equals(String.valueOf(Config.CHECK_FLAG_PASS))){//��ѯ״̬Ϊ������
						resList.add(a);
					}
				}else{//����������
					a.setReviewStatus(String.valueOf(Config.CHECK_FLAG_UNCHECK));//����Ϊδ����
					if(reportInForm.getReviewStatus().equals(String.valueOf(Config.CHECK_FLAG_UNCHECK))){//��ѯ״̬Ϊδ����
						resList.add(a);
					}
					
				}
				if(reportInForm.getReviewStatus().equals(String.valueOf(Config.DEFAULT_VALUE)))//��ѯ״̬Ϊȫ��
					resList.add(a);
			}
		}
		
		 //��ApartPage��������request��Χ��
		aPage.setTerm(this.getTerm(reportInForm));		 	
		aPage.setCount(recordCount);
		request.setAttribute(Config.APART_PAGE_OBJECT,aPage);
		request.setAttribute("flagPass", flagPass);//���ĸ���
		request.setAttribute("resList", resList);
		request.setAttribute("date", reportInForm.getDate());//����
		request.setAttribute("templateId", reportInForm.getTemplateId());//���
		request.setAttribute("repName", reportInForm.getRepName());//��������
		
		
		return mapping.findForward("view");
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
		
			

			/**���뱨����������*/
			if(reportInForm.getDate() != null){
				term += (term.indexOf("?")>=0 ? "&" : "?");
				term += "date=" + reportInForm.getDate();
			}	
			
			
			String reviewStatus = reportInForm.getReviewStatus();
			if(reviewStatus!=null&&!reviewStatus.equals("")){		
				term += (term.indexOf("?")>=0 ? "&" : "?");			
				term += "reviewStatus="+reviewStatus;    
			}

			if(term.indexOf("?")>=0)		
				term = term.substring(term.indexOf("?")+1);
			
			return term;   
		}
}
