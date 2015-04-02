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
 * 报送情况统计
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
		    
			int recordCount = 0; // 记录总数
			int offset = 0; // 偏移量
			int limit = 0; // 每页显示的记录条数
			
			List resList = null;
			ApartPage aPage = new ApartPage();	   	
			String strCurPage = request.getParameter("curPage");
			if(strCurPage!=null){		
				if(!strCurPage.equals(""))		    
					aPage.setCurPage(new Integer(strCurPage).intValue());
			}
			else
				aPage.setCurPage(1);
			//计算偏移量
			offset=(aPage.getCurPage()-1)*Config.PER_PAGE_ROWS; 		
			limit = Config.PER_PAGE_ROWS;
			
	        HttpSession session = request.getSession();
	        Operator operator = null; 
	        if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
	            operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
	        
	        try{
	        	orgNetForm.setPre_org_id(operator.getOrgId()); //查询当前用户的下级机构	        	
	        	//取得记录总数
	        	recordCount=StrutsOrgNetDelegate.selectSubOrgCount(orgNetForm,operator);	  
		   		//显示分页后的记录
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
	        
	        //把ApartPage对象存放在request范围内
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
	 * 递规获取机构报送情况及分行报送情况信息
	 * 
	 * @param orgNetForm 当前机构信息
	 * @param reportInForm 机构应报报表信息
	 * @return void
	 */
	private void setSubOrgInfo(OrgNetForm orgNetForm,ReportInForm reportInForm){
		if(orgNetForm != null && orgNetForm.getOrg_id() != null 
				&& reportInForm != null){
			
			//机构应报报表
			orgNetForm.setYbReportNum(new Integer(StrutsReportInDelegate
						.selectOrgReportStatisticsYB(orgNetForm.getOrg_id(),reportInForm)));
			//机构已报报表
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
	 * 设置查询传递参数
	 * 
	 * @param orgNetForm
	 * @return String 查询的URL
	 */
	public String getTerm(OrgNetForm orgNetForm){	
		String term="";	   

		/**加入机构名称查询条件*/
		if(orgNetForm.getOrg_name() != null && !orgNetForm.getOrg_name().equals("")){		
			term += (term.indexOf("?")>=0 ? "&" : "?");			
			term += "orgName=" + orgNetForm.getOrg_name();   		   
		}
		/**加入报表年份条件*/
		if(orgNetForm.getYear() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "year=" + orgNetForm.getYear();
		}
		/**加入报表期数条件*/
		if(orgNetForm.getTerm() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "term=" + orgNetForm.getTerm();
		}
		if(term.indexOf("?")>=0)			
			term = term.substring(term.indexOf("?")+1);
		   
		return term;   
	}
}