package com.cbrc.smis.action;

import java.io.IOException;
import java.io.PrintWriter;
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
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.adapter.Procedure;
import com.cbrc.smis.adapter.StrutsReportInDelegate;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.fitech.net.config.Config;

/**
 * 校验全部（数据报送）
 * @author jcm
 * @2008-01-07
 */
public class ValidateAllReportAction extends Action{
	private FitechException log = new FitechException(ValidateAllReportAction.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException{
		
		boolean resultBL = false;
		boolean resultBJ = false;
		boolean exception = false;
		Integer repInId = null;
		String failedReportInIds = "";
		ReportInForm reportInForm = (ReportInForm) form;
		RequestUtils.populate(reportInForm, request);
		
		if(reportInForm.getDate() != null && !reportInForm.getDate().equals("")){
			reportInForm.setYear(Integer.valueOf(reportInForm.getDate().substring(0, 4)));
			reportInForm.setTerm(Integer.valueOf(reportInForm.getDate().substring(5, 7)));
		}
		
		/**
		 * 若未获得正确的报表时间（年、月）
		 * 则判断校验全部出错，按异常处理
		 */		
		if(reportInForm.getYear() != null && reportInForm.getTerm() != null){
			try{	 			
				Operator operator = null;
				HttpSession session = request.getSession();
		        if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
		            operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
		        
		        List repIdList = StrutsReportInDelegate.selectAllReports(reportInForm,operator);
		        if(repIdList != null && repIdList.size() > 0){
		        	for(Iterator iter=repIdList.iterator();iter.hasNext();){
		        		repInId = (Integer)iter.next();
		        		
		        		if(com.cbrc.smis.common.Config.UP_VALIDATE_BN.equals(new Integer(1))){
							resultBL = Procedure.runBNJY(repInId,operator.getOperatorName());
						}else resultBL = true;
						if(com.cbrc.smis.common.Config.UP_VALIDATE_BJ.equals(new Integer(1))){
							resultBJ = Procedure.runBJJY(repInId,operator.getOperatorName());
						}else resultBJ = true;
						
						if(resultBL != true || resultBJ != true) failedReportInIds+=(failedReportInIds==""?"":",") + repInId;
						StrutsReportInDelegate.updateReportInCheckFlag(repInId, Config.CHECK_FLAG_AFTERJY);
		        	}
		        }
			}catch(Exception ex){
				exception = true;
				log.printStackTrace(ex);
			}
		}else exception = true;
		
		PrintWriter out = response.getWriter();

		response.setContentType("text/xml");
		response.setHeader("Cache-control", "no-cache");
		String result = null;
		if(exception == true) result = "exception";
		else if(failedReportInIds != null && failedReportInIds.length() > 0) result = failedReportInIds;
		else result = "true";
		out.println("<response><result>" + result + "</result></response>");
		out.close();
		
		return null;
	}
}
