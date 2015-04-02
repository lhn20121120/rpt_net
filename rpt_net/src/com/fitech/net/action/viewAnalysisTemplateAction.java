package com.fitech.net.action;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
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

import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.net.adapter.StrutsAnalysisTemplateDelegate;
import com.fitech.net.form.AAnalysisTPForm;

public class viewAnalysisTemplateAction extends Action{
	private static FitechException log = new FitechException(viewAnalysisTemplateAction.class);

	public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		  MessageResources resources = getResources(request);
	      FitechMessages messages = new FitechMessages();
	      //取得request范围内的请求参数，并存放在reportForm内
	      AAnalysisTPForm aAnalysisTPForm = (AAnalysisTPForm)form ;
	      RequestUtils.populate(aAnalysisTPForm, request);
		
		
		int recordCount =0; //记录总数		
		int offset=0; //偏移量
		int limit=0;  //每页显示的记录条数
		   
		//List对象的初始化
		List resList=null;
		HashMap map = null;
		ApartPage aPage=new ApartPage();
		String strCurPage=request.getParameter("curPage");
		if(strCurPage!=null){
			if(!strCurPage.equals(""))
				aPage.setCurPage(new Integer(strCurPage).intValue());
		}else
			aPage.setCurPage(1);
			

		/**
         * 取得当前用户的权限信息
         */   
		HttpSession session = request.getSession();
		Operator operator = null; 
		if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)  
			operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);     
		Calendar calendar = Calendar.getInstance();		
		if(aAnalysisTPForm.getYear() == null || "".equals(aAnalysisTPForm.getYear()))			   
			aAnalysisTPForm.setYear(new Integer(calendar.get(Calendar.YEAR)));		   
		if(aAnalysisTPForm.getTerm() == null || "".equals(aAnalysisTPForm.getTerm()))			   
			aAnalysisTPForm.setTerm(new Integer(calendar.get(Calendar.MONTH)+1));
		if(aAnalysisTPForm.getOrgId() == null) aAnalysisTPForm.setOrgId(operator.getOrgId());	 		
			//			计算偏移量 
			offset=(aPage.getCurPage()-1)*Config.PER_PAGE_ROWS; 
			limit = Config.PER_PAGE_ROWS;	
			
	        try{
	        	//取得记录总数
	           // recordCount = StrutsAnalysisTemplateDelegate.getAnalysisCount(aAnalysisTPForm);
	        	
	        	//显示分页后的记录
	        //    if(recordCount > 0)
	            //	resList = StrutsAnalysisTemplateDelegate.selectAnalysisReport(aAnalysisTPForm,offset,limit); 
	            	resList = StrutsAnalysisTemplateDelegate.selectAnalysisReport(aAnalysisTPForm); 
	            	
	            	// 查找一个类型下有多少条记录
	            	map = StrutsAnalysisTemplateDelegate.selectOneAnalysisCount(aAnalysisTPForm); 
	            	
	    	}catch(Exception ex){
				log.printStackTrace(ex);
				messages.add(resources.getMessage("select.dataReport.failed"));		
			}

         //把ApartPage对象存放在request范围内
//		aPage.setTerm(this.getTerm(aAnalysisTPForm));		 	
		aPage.setCount(recordCount);
		request.setAttribute(Config.APART_PAGE_OBJECT,aPage);
		request.setAttribute("orgId",aAnalysisTPForm.getOrgId());
		if(aAnalysisTPForm.getATName()!=null && !"".equals(aAnalysisTPForm.getATName())){
			request.setAttribute("ATName", aAnalysisTPForm.getATName());
		}
		

		if(messages.getMessages() != null && messages.getMessages().size() > 0)		
			request.setAttribute(Config.MESSAGES,messages);
		
		if(resList!=null && resList.size()>0){	
			request.setAttribute(Config.RECORDS,resList);   
		}
		if(map != null && map.size()>0){
			request.setAttribute("MAP",map);  
		}
		return mapping.findForward("view");	 
    }
    public String getTerm(AAnalysisTPForm aAnalysisTPForm){
    	
		String url="";  
		String orgId = aAnalysisTPForm.getOrgId();
		String year = String.valueOf(aAnalysisTPForm.getYear());
		String term = String.valueOf(aAnalysisTPForm.getTerm());	
		
		if(year!=null&&!year.equals("")){		
			url += (url.indexOf("?")>=0 ? "&" : "?");			
			url += "year="+year.toString();   		   
		}	
		
		if(term!=null&&!term.equals("")){		
			url += (url.indexOf("?")>=0 ? "&" : "?");			
			url += "term="+term.toString();    
		}
		
		if(orgId!=null&&!orgId.equals("")){		
			url += (url.indexOf("?")>=0 ? "&" : "?");			
			url += "orgId="+orgId;    
		}
		
		if(term.indexOf("?")>=0)		
			url = url.substring(url.indexOf("?")+1);
		
		return url;
	}	
}

/**
 * 新增统计分析模版
 * 
 * @author wh
 * 
 */

