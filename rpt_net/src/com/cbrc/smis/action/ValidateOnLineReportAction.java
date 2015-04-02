package com.cbrc.smis.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

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
import com.cbrc.smis.hibernate.ReportIn;
import com.cbrc.smis.proc.impl.Report;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.fitech.gznx.procedure.NXReportValid;
import com.fitech.gznx.procedure.ValidateQDReport;
import com.fitech.gznx.service.AFReportDelegate;
import com.fitech.net.config.Config;

/**
 * 在线报表校验
 * 
 * @author Yao
 * 
 */
public class ValidateOnLineReportAction extends Action
{
	private FitechException log = new FitechException(ValidateOnLineReportAction.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException
	{
		
		boolean resultBL = false;
		boolean resultBJ = false;
		Integer repInId = null;
		String failedReportInIds = "";
		Integer reportStyle = null;
		ReportIn reportIn = null;
		
		ReportInForm reportInForm = (ReportInForm) form;
		RequestUtils.populate(reportInForm, request);
		Operator operator = null;
		HttpSession session = request.getSession();
        if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
            operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
    	String updateCheckFlag = request.getParameter("updateCheckFlag");
		if(request.getParameter("repInId") != null){
			try{				
				repInId = new Integer(request.getParameter("repInId"));
				// System.out.println(repInId + " 校验开始==" + new Date());
				reportIn = StrutsReportInDelegate.getReportIn2(repInId);
				// 获取填报的数据
				reportStyle = reportIn.getMChildReport().getReportStyle();
				
	            //点点、清单判断	
	            if(!reportStyle.toString().equals(com.fitech.gznx.common.Config.REPORT_QD)){
	            	//DD
					if(com.cbrc.smis.common.Config.UP_VALIDATE_BN.equals(new Integer(1))){
						resultBL = Procedure.runBNJY(repInId,operator.getOperatorName());
					}else{
						resultBL=true;
					}
					// 2007-10-17号增加表间校验  （吴昊）
					if(com.cbrc.smis.common.Config.UP_VALIDATE_BJ.equals(new Integer(1))){
						resultBJ = Procedure.runBJJY(repInId,operator.getOperatorName());
					}else{
						resultBJ=true;
					}
	            }else{
	            	//清单式检验
					if (com.cbrc.smis.common.Config.UP_VALIDATE_BN
							.equals(new Integer(1))) {
						resultBL = new ValidateQDReport().bnValidate(repInId);
					} else {
						resultBL = true;
					}
					// 2007-10-17号增加表间校验 （吴昊）
					if (com.cbrc.smis.common.Config.UP_VALIDATE_BJ
							.equals(new Integer(1))) {
						resultBJ = true;
					} else {
						resultBJ = true;
					}
	            }
	            if(updateCheckFlag==null){
				// System.out.println(repInId + " 校验结束==" + new Date());
	            	StrutsReportInDelegate.updateReportInCheckFlag(repInId, Config.CHECK_FLAG_AFTERJY);
	            }
				if(request.getParameter("validateState")!=null){
					AFReportDelegate.deleteReportInfo(repInId, 
										reportIn.getMChildReport().getComp_id().getChildRepId(),
											reportIn.getMChildReport().getComp_id().getVersionId());
					String rhRepId = request.getParameter("rhRepId");
					
					if(rhRepId!=null && !"".equals(rhRepId)){
						Connection orclCon = (new com.cbrc.smis.proc.jdbc.FitechConnection()).getConnect();
						 if(orclCon.getAutoCommit())
				                orclCon.setAutoCommit(false);
						// 写表间或表内校验标识
				        Integer validFlag = Report.RESULT_OK;
				        String flagName = "";
				        if(com.cbrc.smis.common.Config.UP_VALIDATE_BN.equals(new Integer(1))){
				        	flagName = "TBL_INNER_VALIDATE_FLAG";
				        	if(!resultBL)validFlag = Report.RESULT_NO;
							AFReportDelegate.updateFlag(orclCon, Integer.valueOf(rhRepId), flagName , validFlag.intValue());
						}
				        
				        if(com.cbrc.smis.common.Config.UP_VALIDATE_BJ.equals(new Integer(1))){
				        	flagName = "TBL_OUTER_VALIDATE_FLAG";
				        	if(!resultBJ)validFlag = Report.RESULT_NO;
							 AFReportDelegate.updateFlag(orclCon, Integer.valueOf(rhRepId), flagName , validFlag.intValue());
						}
						 NXReportValid.close(orclCon);
						 AFReportDelegate afr = new AFReportDelegate();
							/**已使用Hibernate 卞以刚 2011-12-26**/
						 if(updateCheckFlag==null){
							 afr.updateReportInCheckFlag(Integer.valueOf(rhRepId), Config.CHECK_FLAG_AFTERJY);
						 }
					}
				}
			}catch (Exception e){
				//e.printStackTrace();
				log.printStackTrace(e);
				resultBL = false;
				resultBJ = false;
			}

			PrintWriter out = response.getWriter();

			response.setContentType("text/xml");
			response.setHeader("Cache-control", "no-cache");
			boolean result = true;
			if( resultBL && resultBJ ) result = true;
			else result = false;
			out.println("<response><result>" + result + "</result></response>");
			out.close();
			
		}else if(request.getParameter("repInIds") != null){
			try{				
				String[] repInIds = request.getParameter("repInIds").split(",");			
				if(repInIds != null && repInIds.length > 0){
					for(int i=0;i<repInIds.length;i++){
						repInId = new Integer(repInIds[i]);
						
						reportIn = StrutsReportInDelegate.getReportIn2(repInId);
						// 获取填报的数据
						/**已使用hibernate 卞以刚 2011-12-21**/
						reportStyle = reportIn.getMChildReport().getReportStyle();
						
			            //点点、清单判断
			            if(!reportStyle.toString().equals(com.fitech.gznx.common.Config.REPORT_QD)){
			            	//DD
							if(com.cbrc.smis.common.Config.UP_VALIDATE_BN.equals(new Integer(1))){
								/**使用jdbc 可能不需要修改  卞以刚 2011-12-21**/
								resultBL = Procedure.runBNJY(repInId,operator.getOperatorName());
							}else resultBL = true;
							// 2007-10-17号增加表间校验  （吴昊）
	
							if(com.cbrc.smis.common.Config.UP_VALIDATE_BJ.equals(new Integer(1))){
								/**使用jdbc 可能不需要修改  卞以刚 2011-12-21**/
								resultBJ = Procedure.runBJJY(repInId,operator.getOperatorName());
							}else resultBJ = true;
						
			            }else{
			            	//清单式检验
							if (com.cbrc.smis.common.Config.UP_VALIDATE_BN
									.equals(new Integer(1))) {
								/**使用jdbc 可能不需要修改  卞以刚 2011-12-21**/
								resultBL = new ValidateQDReport().bnValidate(repInId);
							} else {
								resultBL = true;
							}
							// 2007-10-17号增加表间校验 （吴昊）
							if (com.cbrc.smis.common.Config.UP_VALIDATE_BJ
									.equals(new Integer(1))) {
								resultBJ = true;
							} else {
								resultBJ = true;
							}
			            }
						
						if(resultBL != true || resultBJ != true) failedReportInIds+=(failedReportInIds==""?"":",") + repInId;
						/**已使用hibernate  卞以刚 2011-12-21**/
					
						if(updateCheckFlag==null){
							StrutsReportInDelegate.updateReportInCheckFlag(repInId, Config.CHECK_FLAG_AFTERJY);
						}
						
						if(request.getParameter("validateState")!=null){
							AFReportDelegate.deleteReportInfo(repInId, 
												reportIn.getMChildReport().getComp_id().getChildRepId(),
													reportIn.getMChildReport().getComp_id().getVersionId());
						}
					}
				}				
			}catch (Exception e){
				//e.printStackTrace();
				log.printStackTrace(e);
			}

			PrintWriter out = response.getWriter();

			response.setContentType("text/xml");
			response.setHeader("Cache-control", "no-cache");
			String result = null;
			if(failedReportInIds != null && failedReportInIds.length() > 0) result = failedReportInIds;
			else result = "true";
			out.println("<response><result>" + result + "</result></response>");
			out.close();
		}
		return null;
	}
}
