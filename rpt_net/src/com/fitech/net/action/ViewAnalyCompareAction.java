package com.fitech.net.action;


import java.io.IOException;
import java.util.ArrayList;
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

import com.cbrc.smis.adapter.StrutsMRepRangeDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.net.adapter.StrutsAnalysisTemplateDelegate;
import com.fitech.net.form.FromCompareForm;

/**
 * 查看实际数据报表的异常变化详细
 * 
 * @author rds
 * @date 2006-01-2
 */
public class ViewAnalyCompareAction extends Action {
	private FitechException log=new FitechException(ViewAnalyCompareAction.class);
	
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
		Locale locale = getLocale(request);
		MessageResources resources = getResources(request);
		
		HttpSession session=request.getSession();
		Operator operator = null;         
		if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)        
			operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
		String org = operator.getOrgId();
		FitechMessages messages = new FitechMessages();
		FromCompareForm fcompForm = null;
		int reportStyle=0;
		List resList = new ArrayList();
		try{
			// 页面传过来的参数
			String parStr=request.getParameter("standard");      

			if(parStr!=null){
				
				String allParStr[] = parStr.split("#");
				//	  比对类型 1,2,3,
				String compareValue=allParStr[0];
			//	compareValue=compareValue.substring(0,compareValue.length()-1);
				// 参数值 
				String  condition[] = allParStr[1].split("&");
				if(condition!=null){
					for(int i=0;i<condition.length;i++){
						String str[] = condition[i].split(",");
						FromCompareForm fromCompareForm = new FromCompareForm();
						fromCompareForm.setYear(new Integer(str[0]));						
						fromCompareForm.setTerm(new Integer(str[1]));
						fromCompareForm.setReportName( str[2] );
						fromCompareForm.setCellId(new Integer(str[3]));
						fromCompareForm.setVersionId(StrutsMRepRangeDelegate.getFRVersionId(fromCompareForm.getYear().toString(),fromCompareForm.getTerm().toString(),org));
						
						fcompForm=StrutsAnalysisTemplateDelegate.getCompareResult(compareValue,fromCompareForm,org);
						
						resList.add(fcompForm);
					}
				}
			}
			if(resList!=null && resList.size()>0){	    
				request.setAttribute(Config.RECORDS,resList);	     
			}
			
		}catch(Exception e){
			reportStyle=0;
			log.printStackTrace(e);
		}		
		
		if(messages!=null && messages.getSize()>0) request.setAttribute(Config.MESSAGES,messages);
			return mapping.findForward("view");

	}
}
