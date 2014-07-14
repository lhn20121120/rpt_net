package com.fitech.gznx.action;

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

import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.fitech.gznx.form.AFReportForm;
import com.fitech.gznx.procedure.ProcedureHandle;
import com.fitech.gznx.service.AFReportDelegate;
import com.fitech.net.config.Config;

/**
 * 有oracle(nextval)语法  需要修改 卞以刚 2011-12-26
 * 校验全部（数据报送）
 * @author jcm
 * @2008-01-07
 */
public class ValidateNXAllReportAction extends Action{
	private FitechException log = new FitechException(ValidateNXAllReportAction.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException{
		
		AFReportForm reportInForm = (AFReportForm) form;
		RequestUtils.populate(reportInForm, request);
		
		boolean resultBL = false;
		boolean resultBJ = false;
		boolean exception = false;
		Long repInId = null;
		
		String failedReportInIds = "";

		/**
		 * 若未获得正确的报表时间
		 * 则判断校验全部出错，按异常处理
		 */		
		if(reportInForm.getDate() != null){
			try{	 			
				Operator operator = null;
				HttpSession session = request.getSession();
		        if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
		            operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
		        
				// 取得模板类型
				if (session.getAttribute(com.cbrc.smis.common.Config.REPORT_SESSION_FLG) != null)
					reportInForm.setTemplateType(session.getAttribute(
							com.cbrc.smis.common.Config.REPORT_SESSION_FLG).toString());
		        
				/***
				 * 已使用hibernate 卞以刚 2011-12-26
				 * 影响对象：AfTemplate AfReport
				 */
		        List repIdList = AFReportDelegate.selectAllReports(reportInForm,operator);
		        
		        if(repIdList != null && repIdList.size() > 0){
		        	
		        	for(Iterator iter=repIdList.iterator();iter.hasNext();){
		        		
		        		repInId = (Long)iter.next();
		        		
		        		if(com.cbrc.smis.common.Config.UP_VALIDATE_BN.equals(new Integer(1))){
		        			/***
		        			 * 有oracle(nextval)语法  需要修改 卞以刚 2011-12-26
		        			 */
							resultBL = ProcedureHandle.runBNJY(repInId.intValue(),operator.getOperatorName(),
									Integer.valueOf(reportInForm.getTemplateType()));
						}else 
							resultBL = true;
		        		
						if(com.cbrc.smis.common.Config.UP_VALIDATE_BJ.equals(new Integer(1))){
							/***
		        			 * 有oracle(nextval)语法  需要修改 卞以刚 2011-12-26
		        			 */
							resultBJ = ProcedureHandle.runBJJY(repInId.intValue(),operator.getOperatorName(),
									Integer.valueOf(reportInForm.getTemplateType()));
						}else 
							resultBJ = true;
						
						if(resultBL != true || resultBJ != true) 
							failedReportInIds += (failedReportInIds==""?"":",") + repInId;
						
						AFReportDelegate afrd = new AFReportDelegate();
						/**已使用hibernate 卞以刚 2011-12-26*/
						afrd.updateReportInCheckFlag(repInId.intValue(), Config.CHECK_FLAG_AFTERJY);
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
		
		if(exception == true) 
			result = "exception";
		else if(failedReportInIds != null && failedReportInIds.length() > 0) 
			result = failedReportInIds;
		else 
			result = "true";
		
		out.println("<response><result>" + result + "</result></response>");
		out.close();
		
		return null;
	}
}
