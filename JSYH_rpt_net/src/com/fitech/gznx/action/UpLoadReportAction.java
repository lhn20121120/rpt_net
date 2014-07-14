package com.fitech.gznx.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

import com.cbrc.smis.adapter.StrutsReportInDelegate;
import com.cbrc.smis.adapter.StrutsReportInInfoDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.entity.AFDataTrace;
import com.cbrc.smis.entity.AFForceRep;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.form.ReportInInfoForm;
import com.cbrc.smis.hibernate.ReportIn;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.service.IAFForceRepService;
import com.cbrc.smis.service.impl.AFForceRepServiceImpl;
import com.cbrc.smis.util.FitechEXCELReport;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.fitosa.adapter.ImpReportData;
import com.fitech.fitosa.bean.CellBean;
import com.fitech.fitosa.bean.ReportBean;
import com.fitech.gznx.service.AFReportDelegate;
import com.fitech.net.template.action.UpLoadOnLineReportAction;

/***
 * 已使用hibernate 卞以刚 2011-12-21
 * @author Administrator
 *
 */
public class UpLoadReportAction extends Action {
	private static FitechException log = new FitechException(
			UpLoadOnLineReportAction.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		IAFForceRepService repService = new AFForceRepServiceImpl();
		MessageResources resources = getResources(request);
		FitechMessages messages = new FitechMessages();

		// 取得request范围内的请求参数，并存放在ReportInForm内
		ReportInForm reportInForm = (ReportInForm) form;
		RequestUtils.populate(reportInForm, request);

		boolean flag = true;
		String messagesStr = null;

		Integer repInId = null;
		// 达到上报条件的报表
		List allowReportList = null;

		String type = request.getParameter("type");

		int notReport = 0;
		
		try {

			allowReportList = new ArrayList();
			
			if (type != null && type.equals("select")
					&& request.getParameter("repInIds") != null) {

				String[] repInIds = request.getParameter("repInIds").split(",");

				if (repInIds != null && repInIds.length > 0) {
					for (int i = 0; i < repInIds.length; i++) {
						
						flag = true;
						
						repInId = new Integer(repInIds[i]);
						/**已使用hibernate 卞以刚 2011-12-21**/
						ReportIn reportIn = StrutsReportInDelegate.getReportIn2(repInId);

						// 得到表内校验的结果值
						Short BLValidateFlag = reportIn.getTblInnerValidateFlag() != null ? reportIn
								.getTblInnerValidateFlag().shortValue() : -1;
						// 得到表间校验的结果值
						Short BJValidateFlag = reportIn.getTblOuterValidateFlag() != null ? reportIn
								.getTblOuterValidateFlag().shortValue() : -1;

						if (Config.SYS_BN_VALIDATE.equals(new Integer(1))) {
							if (BLValidateFlag == null || !BLValidateFlag.equals(new Short("1"))) {
								messages.add(reportIn.getRepName() + "：表内校验未通过，无法报送！");
								flag = false;
							}
						}
						if (flag && Config.SYS_BJ_VALIDATE.equals(new Integer(1))) {
							if (BJValidateFlag == null || !BJValidateFlag.equals(new Short("1"))) {
								AFForceRep rep = null;
								if(Config.ISFORCEREP){
									try {
										rep = repService.findAFForceRepByRepInId(reportIn.getRepInId());
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
								if(rep!=null){
									flag = true;
								}else{
									messages.add(reportIn.getRepName() + "：表间校验未通过，无法报送！");
									flag = false;
								}
							
							}
						}
						//如果需要校验的状态符合报送情况，即加入上报队列
						if(flag) {
							allowReportList.add(reportIn);
						}else{
							notReport++;
						}
					}
				}
			} else if (type != null && type.equals("all")) {
				
				//获得基础信息
				HttpSession session = request.getSession();
				Operator operator = (Operator) session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
				String templateType = session.getAttribute(Config.REPORT_SESSION_FLG).toString();
				
				if(reportInForm.getDate()!=null){
					reportInForm.setYear(Integer.valueOf(reportInForm.getDate().substring(0, 4)));
					reportInForm.setTerm(Integer.valueOf(reportInForm.getDate().substring(5, 7)));
				}
				/**已使用hibernate 卞以刚 2011-12-21**/				
				List repIns = StrutsReportInDelegate.reportReports(reportInForm, operator, 
						new Integer(Config.CHECK_FLAG_AFTERJY));
				
				if (repIns != null && repIns.size() > 0) {
					
					for (int i = 0; i < repIns.size(); i++) {
						
						flag = true;
						
						ReportIn reportIn = (ReportIn) repIns.get(i);

						// 得到表内校验的结果值
						Short BLValidateFlag = reportIn.getTblInnerValidateFlag() != null ? reportIn
								.getTblInnerValidateFlag().shortValue() : -1;
						// 得到表间校验的结果值
						Short BJValidateFlag = reportIn.getTblOuterValidateFlag() != null ? reportIn
								.getTblOuterValidateFlag().shortValue() : -1;

						if (Config.SYS_BN_VALIDATE.equals(new Integer(1))) {
							if (BLValidateFlag == null || !BLValidateFlag.equals(new Short("1"))) {
								messages.add(reportIn.getRepName() + "：表内校验未通过，无法报送！");
								flag = false;
							}
						}
						if (flag && Config.SYS_BJ_VALIDATE.equals(new Integer(1))) {
							if (BJValidateFlag == null || !BJValidateFlag.equals(new Short("1"))) {
								AFForceRep rep = null;
								if(Config.ISFORCEREP){
									try {
										rep = repService.findAFForceRepByRepInId(reportIn.getRepInId());
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
								if(rep!=null){
									flag = true;
								}else{
									messages.add(reportIn.getRepName() + "：表间校验未通过，无法报送！");
									flag = false;
								}
							}
						}
						//如果需要校验的状态符合报送情况，即加入上报队列
						if(flag) {
							allowReportList.add(reportIn);
						}else{
							notReport++;
						}
					}
				}
			}

			if (allowReportList.size() > 0) {
				
				boolean bool = false;
				Calendar calendar = Calendar.getInstance();
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				String date = format.format(calendar.getTime());	
				/**已使用hibernate 卞以刚 2011-12-21**/
				bool = StrutsReportInDelegate.updateMoreReportIn(allowReportList,date);
				for(int i=0;i<allowReportList.size();i++){
					ReportIn reportIn = (ReportIn) allowReportList.get(i);
					/*
					 * 同步分析系统开始
					*/ 
					if(Config.ISADDFITOSA&&reportIn.getOrgId().equals(com.fitech.gznx.common.Config.HEAD_ORG_ID)){//如果不是法人机构，则不进行同步数据操作
						ReportBean bean = new ReportBean();

						bean.setTemplateId(reportIn.getMChildReport().getComp_id()
								.getChildRepId());
						bean.setVersion(reportIn.getMChildReport().getComp_id()
								.getVersionId());
						bean.setDateRangeId(reportIn.getMDataRgType().getDataRangeId()
								.toString());
						bean.setCurId(reportIn.getMCurr().getCurId().toString());
						bean.setOrgId(reportIn.getOrgId());
						bean.setReptMonth(reportIn.getTerm().toString());
						bean.setReptYear(reportIn.getYear().toString());

						int offset = FitechEXCELReport.getOffsetRows(reportIn
								.getMChildReport().getComp_id().getChildRepId(),
								reportIn.getMChildReport().getComp_id().getVersionId());

						List cells = StrutsReportInInfoDelegate
								.getAllReportInInfo(reportIn.getRepInId());

						List cellInfos = new ArrayList();

						for (int cellcount = 0; cellcount < cells.size(); cellcount++) {
							ReportInInfoForm reportInInfo = (ReportInInfoForm) cells
									.get(cellcount);
							CellBean cellBean = new CellBean();

							int row = reportInInfo.getRowId().intValue() + offset;
							cellBean.setCellName(reportInInfo.getColId()
									+ String.valueOf(row));
							cellBean.setColId(reportInInfo.getColId());
							cellBean.setCurrTermValue(reportInInfo.getReportValue());
							cellBean.setRowId(String.valueOf(row));

							cellInfos.add(cellBean);
						}

						bean.setCellList(cellInfos);

						List reportList = new ArrayList();
						reportList.add(bean);

						ImpReportData ird = new ImpReportData();
						ird.setWebroot(Config.WEBROOTPATH);
						ird.InsertReportDate(reportList);

					}
					
					/*
					 * 同步分析系统结束
					 */
					
					
				}
				if (bool) {
					// new InputData().bnValidate(reportIn.getRepInId());
					messagesStr = "报送成功！";
				} else {
					messagesStr = "系统忙，请稍后再试...！(UE)";
				}
			}

		} catch (Exception ex) {
			log.printStackTrace(ex);
			// messages.add(resources.getMessage("select.uponlineReport.failed"));
			messagesStr = "系统忙，请稍后再试...！(UA)";
		}

		
		//输出信息
		if (messages.getMessages() != null && messages.getMessages().size() > 0)
			 request.setAttribute(Config.MESSAGES, messages);

				
		//response.setCharacterEncoding("GB2312");
		PrintWriter out = response.getWriter();

		response.setContentType("text/xml");
		response.setHeader("Cache-control", "no-cache");
		String result = "true";
		//if (messages.getAlertMsg()==null || messages.getAlertMsg().trim().equals("")) {
		if ( notReport>0 ) {
			result = String.valueOf(notReport);//messages.getAlertMsg();
		} else {
			result = "true";
			//result = messagesStr;
		}
		out.println("<response><result>" + result + "</result></response>");
		out.close();

		return null;

		// return new ActionForward(path.toString());
	}
}
