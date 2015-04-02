package com.cbrc.smis.action;

import java.io.IOException;
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
import com.cbrc.smis.adapter.StrutsReportInDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;

/**
 * 审核操作前批量表间校验
 *
 * @author jcm
 * @serialData 2008-02-21
 */
public final class SHJYReportInAction extends Action {
	private static FitechException log=new FitechException(SHJYReportInAction.class);
	
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
	   
		ReportInForm reportInForm = (ReportInForm)form;	   
		RequestUtils.populate(reportInForm, request);
		
		try {
			Operator operator = null;
			HttpSession session = request.getSession();
	        if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
	            operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
			String repInIds=request.getParameter("repInIds");			
		 
			if(repInIds!=null && !repInIds.equals("")){
				String[] repInIdArr = repInIds.split(Config.SPLIT_SYMBOL_COMMA);
				if(repInIdArr != null && repInIdArr.length != 0){
					for(int i=0;i<repInIdArr.length;i++){
						try{
							Integer repInId = new Integer(repInIdArr[i]);
							Procedure.runBJJY(repInId,operator.getOperatorName());
						}catch(Exception ex){
							ex.printStackTrace();
						}
					}
				}				
			}else{
				 
		        
				List list = StrutsReportInDelegate.selectOfManual(reportInForm,operator);
				if(list != null && list.size() != 0){
					for(Iterator iter=list.iterator();iter.hasNext();){
						Integer repInId = (Integer)iter.next();
						Procedure.runBJJY(repInId,operator.getOperatorName());
					}
				}
			}	
			messages.add("批量校验完成！");
		} catch (Exception e) {
			log.printStackTrace(e);
			messages.add(FitechResource.getMessage(locale, resources,"errors.system"));
		}	 	
		if(messages.getMessages()!= null && messages.getMessages().size() > 0)		 
			request.setAttribute(Config.MESSAGES,messages);
	 	
	 	String term = mapping.findForward("shjyCheckRep").getPath();	 	
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

	 	return new ActionForward(term);
	}
}

