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
	 * 领导审阅功能Action
	 */
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// TODO Auto-generated method stub
		//取得request范围内的请求参数，并存放在reportForm内
        AFReportForm reportInForm = (AFReportForm)form ;
        RequestUtils.populate(reportInForm, request);
        
        
        int recordCount =0; //记录总数		
        int offset = 0; // 偏移量
		int limit = 0; // 每页显示的记录条数
		int flagPass = 0;
		
			
		
		//List对象的初始化
		List<Aditing> resList=null;

		ApartPage aPage=new ApartPage();

		String strCurPage=request.getParameter("curPage");
		if(strCurPage!=null){
			if(!strCurPage.equals(""))
				aPage.setCurPage(new Integer(strCurPage).intValue());
		}else
			aPage.setCurPage(1);
		//计算偏移量
		offset=(aPage.getCurPage()-1)*Config.PER_PAGE_ROWS; 		
		limit = Config.PER_PAGE_ROWS;
		
		
		 /**
         * 取得当前用户的权限信息
         */   
		HttpSession session = request.getSession();
		Operator operator = null; 
		if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
			operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);    
		
		flagPass = AFTemplateReviewDelegate.findCount();//查询审阅个数
		
		recordCount = AFReportProductDelegate.selectYJHExportReportCount(reportInForm,operator,0);
		List<Aditing> aditingList = AFReportProductDelegate.selectYJHExportReportList(reportInForm,operator,offset,limit,0);
		if(aditingList!=null && aditingList.size()>0){
			resList = new ArrayList<Aditing>();
			
			for(int i=0;i<aditingList.size();i++){
				Aditing a = (Aditing)aditingList.get(i);
				AfTemplateReview review = new AfTemplateReview();
				review.getId().setTemplateId(a.getChildRepId());//模板ID
				review.getId().setVersionId(a.getVersionId());//版本号
				review.getId().setTerm(reportInForm.getTerm());
				List<AfTemplateReview> reviewList = AFTemplateReviewDelegate.findAFTemplateReview(review);//查询领导审阅数据
				
				if(reviewList!=null && reviewList.size()>0){//有审阅数据
					review = reviewList.get(0);
					a.setReviewStatus(review.getReviewStatus());
					if(reportInForm.getReviewStatus().equals(String.valueOf(Config.CHECK_FLAG_PASS))){//查询状态为已审阅
						resList.add(a);
					}
				}else{//无审阅数据
					a.setReviewStatus(String.valueOf(Config.CHECK_FLAG_UNCHECK));//设置为未审阅
					if(reportInForm.getReviewStatus().equals(String.valueOf(Config.CHECK_FLAG_UNCHECK))){//查询状态为未审阅
						resList.add(a);
					}
					
				}
				if(reportInForm.getReviewStatus().equals(String.valueOf(Config.DEFAULT_VALUE)))//查询状态为全部
					resList.add(a);
			}
		}
		
		 //把ApartPage对象存放在request范围内
		aPage.setTerm(this.getTerm(reportInForm));		 	
		aPage.setCount(recordCount);
		request.setAttribute(Config.APART_PAGE_OBJECT,aPage);
		request.setAttribute("flagPass", flagPass);//审阅个数
		request.setAttribute("resList", resList);
		request.setAttribute("date", reportInForm.getDate());//期数
		request.setAttribute("templateId", reportInForm.getTemplateId());//编号
		request.setAttribute("repName", reportInForm.getRepName());//报表名称
		
		
		return mapping.findForward("view");
	}
	
	 public String getTerm(AFReportForm reportInForm){	   
			String term="";
			
			/**加入报表编号条件*/
			if(reportInForm.getTemplateId() != null && !reportInForm.getTemplateId().equals("")){
				term += (term.indexOf("?")>=0 ? "&" : "?");
				term += "templateId=" + reportInForm.getTemplateId();
			}
			/**加入报表名称条件*/
			if(reportInForm.getRepName() != null && !reportInForm.getRepName().equals("")){
				term += (term.indexOf("?")>=0 ? "&" : "?");
				term += "repName=" + reportInForm.getRepName();
			}
		
			

			/**加入报表期数条件*/
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
