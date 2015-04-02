package com.fitech.gznx.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.hibernate.ReportIn;
import com.cbrc.smis.proc.impl.Report;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechLog;
import com.fitech.gznx.form.AFReportForm;
import com.fitech.gznx.po.AfReport;
import com.fitech.gznx.po.AfTemplate;
import com.fitech.gznx.po.AfTemplateValiSche;
import com.fitech.gznx.procedure.ProcedureHandle;
import com.fitech.gznx.procedure.QDValidate;
import com.fitech.gznx.procedure.ValidateNxQDReport;
import com.fitech.gznx.procedure.ValidateP2PReport;
import com.fitech.gznx.service.AFReportDelegate;
import com.fitech.gznx.service.AFTemplateDelegate;
import com.fitech.gznx.service.AFValiRelationDelegate;
import com.fitech.gznx.service.AFValidateFormulaDelegate;
import com.fitech.gznx.service.AfTemplateValiScheDelegate;
import com.fitech.net.config.Config;

/**
 * 在线报表校验
 * 
 * @author Yao
 * 
 */
public class ValidateNXOnLineReportAction extends Action {
	
	private FitechException log = new FitechException(
			ValidateNXOnLineReportAction.class);
	
	/***
	 * 有oracle语法(nextval) 需要修改 卞以刚 2011-12-26
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		AFReportForm reportInForm = (AFReportForm) form;
		RequestUtils.populate(reportInForm, request);

		boolean resultBL = false;
		boolean resultBJ = false;
		boolean resultHZ = false;
		Integer repInId = null;
		String failedReportInIds = "";

		HttpSession session = request.getSession();

		Operator operator = null;
		if (session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME) != null)
			operator = (Operator) session
					.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);

		// 取得模板类型
		Integer templateType = null;
		if (session.getAttribute(com.cbrc.smis.common.Config.REPORT_SESSION_FLG) != null)
			templateType = Integer.valueOf(session.getAttribute(
					com.cbrc.smis.common.Config.REPORT_SESSION_FLG).toString());
		if (request.getParameter("repInId") != null) {
			try {
				repInId = new Integer(request.getParameter("repInId"));
				// System.out.println(repInId + " 校验开始==" + new Date());

				// 获取填报的数据
				/**已使用hibernate 卞以刚 2011-12-21**/
	            Integer reportStyle = AFReportDelegate.getReportStyle(repInId.longValue());
				
	         // 获取填报的数据
	        	/**已使用hibernate 卞以刚 2011-12-21
	        	 * 影响对象：AfReport**/
	            Connection orclCon = null;
	            orclCon = (new com.cbrc.smis.proc.jdbc.FitechConnection()).getConnect();
	            AfReport reportIn = AFReportDelegate.getReportIn(repInId.longValue());
	            if (reportStyle.toString().equals(com.fitech.gznx.common.Config.REPORT_DD)) {
					
	            	Map cellMap = ValidateP2PReport.parseCell(orclCon, reportIn, templateType);
	            	//如果填报的单元格集合没有数据
	            	if(cellMap == null || cellMap.isEmpty()) {
	            		if(orclCon!=null){
	            			orclCon.close();
	            			orclCon = null;
	            		}
	            		PrintWriter out = response.getWriter();
	            		
	            		response.setContentType("text/xml");
	            		response.setHeader("Cache-control", "no-cache");
	            		String result = "noData";
	            		out.println("<response><result>" + result + "</result></response>");
	            		out.close();
	            		return null;
	            	}
	            	AfTemplateValiSche valiSche = AfTemplateValiScheDelegate.findAfTemplateValiSche(
	            			reportIn.getTemplateId(),reportIn.getVersionId());
//                AfTemplate af = AFTemplateDelegate.getTemplate(reportIn.getTemplateId(),
//                					reportIn.getVersionId());
	            	if(valiSche!=null && valiSche.getValidateSchemeId()!=null
	            			&& valiSche.getValidateSchemeId().equals(1)&&false){
	            		List bjcellFormuList = AFValidateFormulaDelegate.getCellFormus(orclCon, reportIn, Report.VALIDATE_TYPE_BJ);
	            		List cellFormuList = AFValidateFormulaDelegate.getCellFormus(orclCon, reportIn, Report.VALIDATE_TYPE_BN);
	            		cellFormuList.addAll(bjcellFormuList);
	            		Integer repyjInId = AFReportDelegate.saveReportInfo(repInId.longValue());
	            		
	            		AFReportDelegate.saveMCellInfo(cellMap, reportIn.getTemplateId()
	            				, reportIn.getVersionId(), repyjInId);
	            		
	            		AFReportDelegate.saveMCellFormus(cellFormuList, reportIn.getTemplateId(),
	            				reportIn.getVersionId());
	            		if(orclCon!=null){
	            			orclCon.close();
	            			orclCon = null;
	            		}
	            		return new ActionForward("/report/validateOnLineReport.do?repInId="+repyjInId+"&validateState=1"+"&rhRepId="+repInId);
	            	}
				}
                
	            //点点、清单判断
	            if(reportStyle.toString().equals(com.fitech.gznx.common.Config.REPORT_DD)){
					if (com.cbrc.smis.common.Config.UP_VALIDATE_BN
							.equals(new Integer(1))) {
						/***
						 * 有oracle语法(nextval) 需要修改 
						 * 卞以刚 2011-12-26
						 */
						resultBL = ProcedureHandle.runBNJY(repInId, operator
								.getOperatorName(),templateType);
					} else {
						resultBL = true;
					}
					// 2007-10-17号增加表间校验 （吴昊）
					if (com.cbrc.smis.common.Config.UP_VALIDATE_BJ
							.equals(new Integer(1))) {
						/***
						 * 有oracle语法(nextval) 需要修改
						 * 卞以刚 2011-12-26
						 */
						resultBJ = ProcedureHandle.runBJJY(repInId, operator
								.getOperatorName(),templateType);
					} else {
						resultBJ = true;
					}
	            }else{
	            	//清单式检验
					if (com.cbrc.smis.common.Config.UP_VALIDATE_BN
							.equals(new Integer(1))) {
						// 大额授信报表校验
						if(com.fitech.gznx.common.Config.OTHER_REPORT.equals(String.valueOf(templateType.intValue()))){
							/***
							 * jdbc技术 无特殊oracle语法 不需要修改 
							 * 卞以刚 2011-12-23
							 */
							resultBL = QDValidate.bnValidate(repInId);
						} else {
							/***
							 * jdbc技术 无特殊oracle语法 不需要修改 
							 * 卞以刚 2011-12-23
							 */
							resultBL = new ValidateNxQDReport().bnValidate(repInId);
						}
						
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
				 
				// System.out.println(repInId + " 校验结束==" + new Date());
				AFReportDelegate afr = new AFReportDelegate();
				/**已使用Hibernate 卞以刚 2011-12-26**/
				afr.updateReportInCheckFlag(repInId, Config.CHECK_FLAG_AFTERJY);
            	if(orclCon!=null){
            		orclCon.close();
            		orclCon = null;
            	}

			} catch (Exception e) {
				e.printStackTrace();
				resultBL = false;
				resultBJ = false;
			}

			PrintWriter out = response.getWriter();

			response.setContentType("text/xml");
			response.setHeader("Cache-control", "no-cache");
			boolean result = true;
			if (resultBL && resultBJ){
				AfReport reportIn;
				try {
					reportIn = AFReportDelegate.getReportIn(repInId.longValue());
					FitechLog.writeLog(com.cbrc.smis.common.Config.LOG_OPERATION,operator.getUserName(),
							"报表[编号:" + reportIn.getTemplateId() 
	                        + ",版本号:" + reportIn.getVersionId() 
	                        + "]校验通过!");
				} catch (Exception e) {
					e.printStackTrace();
				}
				result = true;
			}
			else
				result = false;
		
			out.println("<response><result>" + result + "</result></response>");
			out.close();

		} else if (request.getParameter("repInIds") != null) {
			try {
				String[] repInIds = request.getParameter("repInIds").split(",");
				if (repInIds != null && repInIds.length > 0) {
					for (int i = 0; i < repInIds.length; i++) {
						repInId = new Integer(repInIds[i]);

						// 获取填报的数据
						/**已使用hibernate 卞以刚 2011-12-26**/
			            Integer reportStyle = AFReportDelegate.getReportStyle(repInId.longValue());
			            
			            //点点、清单判断
			            if(reportStyle.toString().equals(com.fitech.gznx.common.Config.REPORT_DD)){
							if (com.cbrc.smis.common.Config.UP_VALIDATE_BN
									.equals(new Integer(1))) {
								/**有oracle语法**/
								resultBL = ProcedureHandle.runBNJY(repInId, operator
										.getOperatorName(),templateType);
							} else
								resultBL = true;
							// 2007-10-17号增加表间校验 （吴昊）
	
							if (com.cbrc.smis.common.Config.UP_VALIDATE_BJ
									.equals(new Integer(1))) {
								/**有oracle语法**/
								resultBJ = ProcedureHandle.runBJJY(repInId, operator
										.getOperatorName(),templateType);
							} else
								resultBJ = true;
			            }else{
			            	//清单式检验
							if (com.cbrc.smis.common.Config.UP_VALIDATE_BN
									.equals(new Integer(1))) {
								// 大额授信报表校验
								if(com.fitech.gznx.common.Config.OTHER_REPORT.equals(String.valueOf(templateType.intValue()))){
									/**jdbc技术 无特殊语法 不需要修改**/
									resultBL = QDValidate.bnValidate(repInId);
								} else {
									/**jdbc技术 无特殊语法 不需要修改**/
									resultBL = new ValidateNxQDReport().bnValidate(repInId);
								}
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
						
						if (resultBL != true || resultBJ != true)
							failedReportInIds += (failedReportInIds == "" ? "" : ",") + repInId;
						AFReportDelegate afrd = new AFReportDelegate();
						/**hibernate 技术 卞以刚 2011-12-26**/
						afrd.updateReportInCheckFlag(repInId, Config.CHECK_FLAG_AFTERJY);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			PrintWriter out = response.getWriter();

			response.setContentType("text/xml");
			response.setHeader("Cache-control", "no-cache");
			String result = null;
			if (failedReportInIds != null && failedReportInIds.length() > 0)
				result = failedReportInIds;
			else
				result = "true";
			out.println("<response><result>" + result + "</result></response>");
			out.close();
		}

		return null;
	}
	
}
