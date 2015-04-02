package com.cbrc.smis.action;

import java.io.IOException;
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

import com.cbrc.smis.adapter.StrutsReportInDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechLog;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;

/**
 * 更新审核标志状态的ACTION
 *
 * @author 唐磊
 *
 */
public final class UpdateReportInAction extends Action {
	private static FitechException log=new FitechException(UpdateReportInAction.class);
	
   /**
    * Performs action.
    * @result 更新成功返回true，否则返回false
    * @reportInForm FormBean的实例化
    * @e Exception 更新失败捕捉异常并抛出
    */
   
	public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request
			,HttpServletResponse response)throws IOException, ServletException {
		
		Locale locale = getLocale(request);	   
		MessageResources resources = getResources(request);	   
		FitechMessages messages = new FitechMessages();
	   
		boolean result=false;

		ReportInForm reportInForm = (ReportInForm)form;	   
		RequestUtils.populate(reportInForm, request);
		String reportFlg = "0";
		HttpSession session = request.getSession();
		if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
			reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
		}
		String updateFlag = request.getParameter("updateFlag");
		String repInIds=request.getParameter("repInIds");
		try {
			
			String flag=request.getParameter("flag");		  
			String cause=request.getParameter("cause");
		 
			if(repInIds!=null && !repInIds.equals("")){			  
				String[] arr=repInIds.split(Config.SPLIT_SYMBOL_COMMA);			  
				Integer[] ids=null;			  
				if(arr!=null && arr.length>0) ids=new Integer[arr.length];
				
				for(int i=0;i<arr.length;i++){				
					ids[i]=Integer.valueOf(arr[i]);  			  
				}			  
				reportInForm.setRepInIdArray(ids);			  
				reportInForm.setCheckFlag(new Short(flag));

				if(flag.equals(String.valueOf(Config.CHECK_FLAG_NO))) {
					reportInForm.setForseReportAgainFlag(Config.FORSE_REPORT_AGAIN_FLAG_1);
					for(int i=0;i<arr.length;i++)
						FitechLog.writeRepLog(new Integer(20), "审核不通过", request, arr[i] ,reportFlg);
				}else{
					for(int i=0;i<arr.length;i++)
						FitechLog.writeRepLog(new Integer(20), "审核通过", request, arr[i] ,reportFlg);
				}
			}    	  
			if (reportInForm != null && reportInForm.getRepInIdArray()!=null) {			
				result = StrutsReportInDelegate.update(reportInForm);				
				if (result == true) {
					 if(com.cbrc.smis.adapter.StrutsReportInDelegate2.insert(reportInForm.getRepInIdArray(),cause) == true){        							  
						 //  messages.add(FitechResource.getMessage(locale, resources,"saveCheckRep.save.success"));    
							messages.add("重报设置成功!");
					 }
				} else {
					messages.add(FitechResource.getMessage(locale, resources,"saveCheckRep.save.failed"));
					return mapping.findForward("saveCheck");
				}
			}		
		} catch (Exception e) {
			log.printStackTrace(e);
			messages.add(FitechResource.getMessage(locale, resources,"errors.system"));
		}	 	
		if(messages.getMessages()!= null && messages.getMessages().size() > 0)		 
			request.setAttribute(Config.MESSAGES,messages);
	 	
	 	String term=mapping.findForward("saveCheck").getPath();
	 	/**加入报表编号条件*/
		if(reportInForm.getChildRepId() != null && !reportInForm.getChildRepId().equals("")){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "childRepId=" + reportInForm.getChildRepId();
		}
		/**加入报表名称条件*/
		if(reportInForm.getRepName() != null && !reportInForm.getRepName().equals("")){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			/**若是WebLogic则不需要进行转码，直接作为参数传递*/
			term += "repName=" + reportInForm.getRepName();
			/**若是WebSphere则需要先进行转码，再作为参数传递*/
			//term += "repName=" + new String(reportInForm.getRepName().getBytes("gb2312"), "iso-8859-1");
		}
		/**加入模板类型条件*/
		if(reportInForm.getFrOrFzType() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "frOrFzType=" + reportInForm.getFrOrFzType();
		}
		/**加入报送频度条件*/
		if(reportInForm.getRepFreqId() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "repFreqId=" + reportInForm.getRepFreqId();
		}
		/**加入报表年份条件*/
		if(reportInForm.getYear() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "year=" + reportInForm.getYear();
		}
		/**加入报表期数条件*/
		if(reportInForm.getTerm() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "term=" + reportInForm.getTerm();
		}
		/**加入报送机构条件*/
		if(reportInForm.getOrgId() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "orgId=" + reportInForm.getOrgId();
		}
	 	/**加入当前页条件*/
	 	if(reportInForm.getCurPage()!=null && !reportInForm.getCurPage().equals("")){		
	 		term += (term.indexOf("?")>=0 ? "&" : "?");			
	 		term += "curPage=" + reportInForm.getCurPage();   		   
	 	}
	 	if(updateFlag!=null && updateFlag.equals("1")){
	 		if(repInIds!=null && !repInIds.equals("")){	
		 		if(term.indexOf("?")>0)
			 		term+="&schemaRepIds="+repInIds;
			 	else
			 		term+="?schemaRepIds="+repInIds;
		 	}
	 	}
	 	return new ActionForward(term);
	}
}

