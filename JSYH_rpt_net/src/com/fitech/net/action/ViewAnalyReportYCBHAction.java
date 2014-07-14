package com.fitech.net.action;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

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

import com.cbrc.smis.adapter.Procedure;
import com.cbrc.smis.adapter.StrutsReportInInfoDelegate;
import com.cbrc.smis.adapter.TranslatorUtil;
import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.ReportInInfoForm;
import com.cbrc.smis.hibernate.ReportInInfo;
import com.cbrc.smis.other.Aditing;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
import com.cbrc.smis.util.FitechUtil;
/**
 * 查看异常变化的结果信息
 * @author WHIBM
 *
 */
public final class ViewAnalyReportYCBHAction extends Action {
	private FitechException log=new FitechException(ViewAnalyReportYCBHAction.class);
	
	/**
	 * @param result 查询返回标志,如果成功返回true,否则返回false
	 * @param ReportInInfoForm 
	 * @param request 
	 * @exception Exception 有异常捕捉并抛出
	 */
	public ActionForward execute(
	      ActionMapping mapping,
	      ActionForm form,
	      HttpServletRequest request,
	      HttpServletResponse response	
	)throws IOException, ServletException {
		 
		FitechMessages messages = new FitechMessages();		
		MessageResources resources = getResources(request);		  
		ReportInInfoForm reportInInfoForm = (ReportInInfoForm)form ;
		RequestUtils.populate(reportInInfoForm, request);
		Locale locale = getLocale(request);
		if(reportInInfoForm.getYear()!=null && reportInInfoForm.getYear().intValue()==0) 		
			reportInInfoForm.setYear(null);		   
		if(reportInInfoForm.getTerm()!=null && reportInInfoForm.getTerm().intValue()==0)		
			reportInInfoForm.setTerm(null);

		int recordCount =0; //记录总数		
		int offset=0; //偏移量		
		int limit=0;  //每页显示的记录条数
		//List对象的初始化		
		List resList=null;
		
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
		
		/***
         * 取得当前用户的权限信息
         * @author wh
         */
		HttpSession session = request.getSession();        
		Operator operator = null;         
		if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)        
			operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
          List resultList = new ArrayList();              
		try {		
			//取得记录总数		   	
	//		recordCount = StrutsReportInInfoDelegate.getAnalyRecordCountOfmanual(reportInInfoForm,operator);
			//显示分页后的记录		   	
	//		if(recordCount > 0)		
			// 得到所有已审核通过的报表信息
				resList = StrutsReportInInfoDelegate.selectAnalyOfManual(reportInInfoForm,operator); 
				if(resList!=null && resList.size()>0){
					try{
						for(Iterator iter=resList.iterator();iter.hasNext();)   {
							Aditing aditing =(Aditing)iter.next(); 
							if(aditing!=null){	
								Integer repInId=aditing.getRepInId();//实际数据报表ID
								
								if(repInId!=null){
									/**获取异常变化信息之前，执行异常变化计算存储程序**/
									boolean flag = StrutsReportInInfoDelegate.calculateYCBH(aditing);
									
										if(flag){
											 String childRepId=aditing.getChildRepId();    //子报表ID
											 String versionId=aditing.getVersionId();      //版本号
											 String orgId=aditing.getOrgId();              //机构ID
											 // 获得异常变化信息列表
											  List list=StrutsReportInInfoDelegate.find(repInId,childRepId,versionId,orgId);			

												if (list != null && list.size() > 0)
												{
													for (int i = 0; i < list.size(); i++)
													{
														ReportInInfoForm  repInInfoForm= (ReportInInfoForm) list.get(i);
														resultList.add(repInInfoForm);
													}
												}

										}
									}else{
										messages.add(FitechResource.getMessage(locale,resources,"report.read.info.error"));
									}
							}else{
								messages.add(FitechResource.getMessage(locale,resources,"report.read.info.error"));
							}
							
						}
						
					}catch(Exception e){
						log.printStackTrace(e);
					}
				}
		}catch (Exception e){				
			log.printStackTrace(e);			
			messages.add(resources.getMessage("manualCheck.get.failed"));		
		}
		List rList = null;
		if(resultList!=null && resultList.size()>0){    	
			//取得记录总数       		 
			recordCount = resultList.size();      	 
		}
		if(recordCount > 0){
			 rList = new ArrayList();
			int arraySize = limit + offset;
			if(resultList.size() < (limit + offset)) arraySize = resultList.size();
			for(int i=offset;i<arraySize;i++){	
				ReportInInfoForm  repInInfoForm= (ReportInInfoForm) resultList.get(i);
				rList.add(repInInfoForm);   
			}
		}
			
		//把ApartPage对象存放在request范围内		
		aPage.setTerm(this.getTerm(reportInInfoForm));		
		aPage.setCount(recordCount);		
		request.setAttribute(Config.APART_PAGE_OBJECT,aPage);
		
		if(messages.getMessages() != null && messages.getMessages().size() > 0)			
			request.setAttribute(Config.MESSAGES,messages);
		
		
		
		if(rList!=null && rList.size()>0){	    
			request.setAttribute(Config.RECORDS,rList);	     
		}
		request.setAttribute("SelectedFlag",reportInInfoForm.getAllFlags());
		
		//返回到页面view	    
		return mapping.findForward("viewSearch");	  
	}
	  
	// First see if there is a vo as request attribute.	
	public String getTerm(ReportInInfoForm reportInInfoForm){
		String term="";		
		String repName = reportInInfoForm.getRepName();		
		String allFlag = reportInInfoForm.getAllFlags();		
		String startDate = reportInInfoForm.getStartDate();		
		String endDate = reportInInfoForm.getEndDate();  
		Short checkFlag=reportInInfoForm.getCheckFlag();		
		String childRepId=reportInInfoForm.getChildRepId();		
		String versionId=reportInInfoForm.getVersionId();		
		String orgName=reportInInfoForm.getOrgName();		  
		//String reportDate=reportInInfoForm.getReportDate().toString();
		if(reportInInfoForm.getYear() != null&&!reportInInfoForm.getYear().equals("")){		
			term += (term.indexOf("?")>=0 ? "&" : "?");			
			term += "year="+reportInInfoForm.getYear().toString();   		  
		}  
		if(reportInInfoForm.getTerm() != null&&!reportInInfoForm.getTerm().equals("")){		
			term += (term.indexOf("?")>=0 ? "&" : "?");			
			term += "term="+reportInInfoForm.getTerm().toString();   		  
		}   
		if(childRepId!=null&&!childRepId.equals("")){		
			term += (term.indexOf("?")>=0 ? "&" : "?");			
			term += "childRepId="+childRepId.toString();   		  
		}		   		   
		if(versionId!=null&&!versionId.equals("")){			
			term += (term.indexOf("?")>=0 ? "&" : "?");			
			term += "versionId="+versionId.toString();   		
		}
		if(orgName!=null&&!orgName.equals("")){		
			term += (term.indexOf("?")>=0 ? "&" : "?");			
			term += "orgName="+orgName.toString();   		  
		}
		if(checkFlag!=null&&!checkFlag.equals("")){			
			term += (term.indexOf("?")>=0 ? "&" : "?");			
			term += "checkFlag="+checkFlag.toString();   		  
		}		   		   
		if(repName!=null&&!repName.equals("")){			
			term += (term.indexOf("?")>=0 ? "&" : "?");			
			term += "repName="+repName.toString();   		  
		}		   
		if(allFlag!=null && !allFlag.equals("")){			
			term += (term.indexOf("?")>=0 ? "&" : "?");			
			term += "allFlags="+allFlag.toString();   		   
		}		  
		if(startDate!=null && !startDate.equals("")){			
			term += (term.indexOf("?")>=0 ? "&" : "?");			
			term += "startDate="+startDate.toString();   		   
		}		
		if(endDate!=null && !endDate.equals("")){		
			term += (term.indexOf("?")>=0 ? "&" : "?");			
			term += "endDate="+endDate.toString();   		   
		}

		if(term.indexOf("?")>=0)		
			term = term.substring(term.indexOf("?")+1);
		    
		return term;  
	}	   
}