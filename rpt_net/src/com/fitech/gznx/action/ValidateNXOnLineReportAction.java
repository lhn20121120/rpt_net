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
 * ���߱���У��
 * 
 * @author Yao
 * 
 */
public class ValidateNXOnLineReportAction extends Action {
	
	private FitechException log = new FitechException(
			ValidateNXOnLineReportAction.class);
	
	/***
	 * ��oracle�﷨(nextval) ��Ҫ�޸� ���Ը� 2011-12-26
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

		// ȡ��ģ������
		Integer templateType = null;
		if (session.getAttribute(com.cbrc.smis.common.Config.REPORT_SESSION_FLG) != null)
			templateType = Integer.valueOf(session.getAttribute(
					com.cbrc.smis.common.Config.REPORT_SESSION_FLG).toString());
		if (request.getParameter("repInId") != null) {
			try {
				repInId = new Integer(request.getParameter("repInId"));
				// System.out.println(repInId + " У�鿪ʼ==" + new Date());

				// ��ȡ�������
				/**��ʹ��hibernate ���Ը� 2011-12-21**/
	            Integer reportStyle = AFReportDelegate.getReportStyle(repInId.longValue());
				
	         // ��ȡ�������
	        	/**��ʹ��hibernate ���Ը� 2011-12-21
	        	 * Ӱ�����AfReport**/
	            Connection orclCon = null;
	            orclCon = (new com.cbrc.smis.proc.jdbc.FitechConnection()).getConnect();
	            AfReport reportIn = AFReportDelegate.getReportIn(repInId.longValue());
	            if (reportStyle.toString().equals(com.fitech.gznx.common.Config.REPORT_DD)) {
					
	            	Map cellMap = ValidateP2PReport.parseCell(orclCon, reportIn, templateType);
	            	//�����ĵ�Ԫ�񼯺�û������
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
                
	            //��㡢�嵥�ж�
	            if(reportStyle.toString().equals(com.fitech.gznx.common.Config.REPORT_DD)){
					if (com.cbrc.smis.common.Config.UP_VALIDATE_BN
							.equals(new Integer(1))) {
						/***
						 * ��oracle�﷨(nextval) ��Ҫ�޸� 
						 * ���Ը� 2011-12-26
						 */
						resultBL = ProcedureHandle.runBNJY(repInId, operator
								.getOperatorName(),templateType);
					} else {
						resultBL = true;
					}
					// 2007-10-17�����ӱ��У�� ����껣�
					if (com.cbrc.smis.common.Config.UP_VALIDATE_BJ
							.equals(new Integer(1))) {
						/***
						 * ��oracle�﷨(nextval) ��Ҫ�޸�
						 * ���Ը� 2011-12-26
						 */
						resultBJ = ProcedureHandle.runBJJY(repInId, operator
								.getOperatorName(),templateType);
					} else {
						resultBJ = true;
					}
	            }else{
	            	//�嵥ʽ����
					if (com.cbrc.smis.common.Config.UP_VALIDATE_BN
							.equals(new Integer(1))) {
						// ������ű���У��
						if(com.fitech.gznx.common.Config.OTHER_REPORT.equals(String.valueOf(templateType.intValue()))){
							/***
							 * jdbc���� ������oracle�﷨ ����Ҫ�޸� 
							 * ���Ը� 2011-12-23
							 */
							resultBL = QDValidate.bnValidate(repInId);
						} else {
							/***
							 * jdbc���� ������oracle�﷨ ����Ҫ�޸� 
							 * ���Ը� 2011-12-23
							 */
							resultBL = new ValidateNxQDReport().bnValidate(repInId);
						}
						
					} else {
						resultBL = true;
					}
					// 2007-10-17�����ӱ��У�� ����껣�
					if (com.cbrc.smis.common.Config.UP_VALIDATE_BJ
							.equals(new Integer(1))) {
						resultBJ = true;
					} else {
						resultBJ = true;
					}
	            } 
				 
				// System.out.println(repInId + " У�����==" + new Date());
				AFReportDelegate afr = new AFReportDelegate();
				/**��ʹ��Hibernate ���Ը� 2011-12-26**/
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
							"����[���:" + reportIn.getTemplateId() 
	                        + ",�汾��:" + reportIn.getVersionId() 
	                        + "]У��ͨ��!");
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

						// ��ȡ�������
						/**��ʹ��hibernate ���Ը� 2011-12-26**/
			            Integer reportStyle = AFReportDelegate.getReportStyle(repInId.longValue());
			            
			            //��㡢�嵥�ж�
			            if(reportStyle.toString().equals(com.fitech.gznx.common.Config.REPORT_DD)){
							if (com.cbrc.smis.common.Config.UP_VALIDATE_BN
									.equals(new Integer(1))) {
								/**��oracle�﷨**/
								resultBL = ProcedureHandle.runBNJY(repInId, operator
										.getOperatorName(),templateType);
							} else
								resultBL = true;
							// 2007-10-17�����ӱ��У�� ����껣�
	
							if (com.cbrc.smis.common.Config.UP_VALIDATE_BJ
									.equals(new Integer(1))) {
								/**��oracle�﷨**/
								resultBJ = ProcedureHandle.runBJJY(repInId, operator
										.getOperatorName(),templateType);
							} else
								resultBJ = true;
			            }else{
			            	//�嵥ʽ����
							if (com.cbrc.smis.common.Config.UP_VALIDATE_BN
									.equals(new Integer(1))) {
								// ������ű���У��
								if(com.fitech.gznx.common.Config.OTHER_REPORT.equals(String.valueOf(templateType.intValue()))){
									/**jdbc���� �������﷨ ����Ҫ�޸�**/
									resultBL = QDValidate.bnValidate(repInId);
								} else {
									/**jdbc���� �������﷨ ����Ҫ�޸�**/
									resultBL = new ValidateNxQDReport().bnValidate(repInId);
								}
							} else {
								resultBL = true;
							}
							// 2007-10-17�����ӱ��У�� ����껣�
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
						/**hibernate ���� ���Ը� 2011-12-26**/
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
