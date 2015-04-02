package com.fitech.net.template.action;

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
import com.cbrc.smis.common.Config;
import com.cbrc.smis.adapter.StrutsOrgDelegate;
import com.cbrc.smis.adapter.StrutsReportInDelegate;
import com.cbrc.smis.adapter.StrutsReportInInfoDelegate;
import com.cbrc.smis.entity.AFForceRep;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.form.ReportInInfoForm;
import com.cbrc.smis.hibernate.ReportIn;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.service.IAFForceRepService;
import com.cbrc.smis.service.impl.AFForceRepServiceImpl;
import com.cbrc.smis.util.FitechEXCELReport;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechLog;
import com.fitech.fitosa.adapter.ImpReportData;
import com.fitech.fitosa.bean.CellBean;
import com.fitech.fitosa.bean.ReportBean;

/**
 * ���ݱ���
 * 
 * 
 */
public class UpLoadOnLineReportAction extends Action
{
	private static FitechException log = new FitechException(UpLoadOnLineReportAction.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException
	{
 
		IAFForceRepService repService = new AFForceRepServiceImpl();
		MessageResources resources = getResources(request);
		String reportFlg = "0";
		HttpSession session = request.getSession();
		if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
			reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
		}
	//	FitechMessages messages = new FitechMessages();
		
		//ȡ��request��Χ�ڵ�����������������ReportInForm��
		ReportInForm reportInForm = (ReportInForm) form;
		RequestUtils.populate(reportInForm, request);
		boolean flag = true;
		String messagesStr = null;
		StringBuffer path = new StringBuffer("/viewOnLineSJBS.do");
		// ���ע 07-10-18
	/*	if (reportInForm.getOrgId() != null && !reportInForm.getOrgId().equals(""))
		{
			path = new StringBuffer("/viewDataReport.do");
			path = path.append("?term=" + reportInForm.getTerm()).append("&orgId=" + reportInForm.getOrgId())
					.append("&curPage=" + reportInForm.getCurPage());

			if (reportInForm.getYear() != null)
				path = path.append("&year=" + reportInForm.getYear().toString());
		}

		if (reportInForm.getRepInId() == null)
		{
//			messages.add(resources.getMessage("select.uponlineReport.failed"));
//
//			if (messages.getMessages() != null && messages.getMessages().size() > 0)
//				request.setAttribute(Config.MESSAGES, messages);
			messagesStr="ϵͳæ�����Ժ�����...��";
			flag = false;
		//	return new ActionForward(path.toString());
		}*/
	
			try
			{
				
	/*                ���ע����
				if (reportInForm.getReportId() != null && !reportInForm.getReportId().equals(""))
				{
					String[] repInIds = reportInForm.getReportId().split(",");
					if (repInIds != null && repInIds.length != 0)
					{
						for (int i = 0; i < repInIds.length; i++)
						{
						//	reportIn = StrutsReportInDelegate.getReportIn2(new Integer(repInIds[i]));
							bool = StrutsReportInDelegate.updateReportIn(reportIn, date);
						}
					}
				}
				else
				{
					reportIn = StrutsReportInDelegate.getReportIn2(reportInForm.getRepInId());
					bool = StrutsReportInDelegate.updateReportIn(reportIn, date);
				}*/
				ReportIn reportIn =StrutsReportInDelegate.getReportIn2(reportInForm.getRepInId());
				if (reportIn != null ){
					//�õ�����У��Ľ��ֵ
					Short BLValidateFlag = reportIn.getTblInnerValidateFlag();
					//�õ����У��Ľ��ֵ
					Short BJValidateFlag = reportIn.getTblOuterValidateFlag();
					if( com.cbrc.smis.common.Config.SYS_BN_VALIDATE.equals(new Integer(1))){
						if(BLValidateFlag == null || !BLValidateFlag.equals(new Short("1"))){
							//messages.add("����У�鲻ͨ���������ϱ��ñ���");
							messagesStr="BN_VALIDATE_NOTPASS";
							//request.setAttribute(Config.MESSAGES, messages);
							flag = false;  
						//	return new ActionForward(path.toString());
						}
					}
					if(flag &&  com.cbrc.smis.common.Config.SYS_BJ_VALIDATE.equals(new Integer(1))){
						if(BJValidateFlag == null || !BJValidateFlag.equals(new Short("1"))){
						//	messages.add("���У�鲻ͨ���������ϱ��ñ���");
						//	request.setAttribute(Config.MESSAGES, messages);
							//�ж��Ƿ���ǿ���ϱ�
							AFForceRep rep = null;
							if(Config.ISFORCEREP){
								
								try {
									rep = repService.findAFForceRepByRepInId(reportIn.getRepInId());
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							if(rep!=null){
								flag = true;
							}else{
								messagesStr="BJ_VALIDATE_NOTPASS";
								flag = false;
							}
							
						//	return new ActionForward(path.toString());
						}
					}
					
				}
				if(flag){
					boolean bool = false;
					Calendar calendar = Calendar.getInstance();
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					String date = format.format(calendar.getTime());		
					bool = StrutsReportInDelegate.updateReportIn(reportIn, date);
					
					
					/*
					 * ͬ������ϵͳ��ʼ
					 */
					if(Config.ISADDFITOSA){
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
							//cellBean.setColId(reportInInfo.getColId().toString());
							cellBean.setCurrTermValue(reportInInfo.getReportValue());
							//cellBean.setRowId(String.valueOf(row));

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
					 * ͬ������ϵͳ����
					 */
					
					if (bool)
					{
						//new InputData().bnValidate(reportIn.getRepInId());
						//messages.add(resources.getMessage("select.uponlineReport.success"));
						messagesStr="���ͳɹ�";
					}
					else
					{
						//messages.add(resources.getMessage("select.uponlineReport.failed"));
						messagesStr="ϵͳæ�����Ժ�����...��";
					}
				}
	
			}
			catch (Exception ex)
			{
				log.printStackTrace(ex);
				//messages.add(resources.getMessage("select.uponlineReport.failed"));
				messagesStr="ϵͳæ�����Ժ�����...��";
			}
		

	//	if (messages.getMessages() != null && messages.getMessages().size() > 0)
	//		request.setAttribute(Config.MESSAGES, messages);
		

	//	response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();

		response.setContentType("text/xml");
		response.setHeader("Cache-control", "no-cache");
		String result = "true";
		if( flag)
		{
			result ="true";
			String repInId = request.getParameter("repInId");
		    FitechLog.writeRepLog(new Integer(11), "���ͳɹ�", request, repInId ,reportFlg);
		}else{
			result = messagesStr;
		}
		out.println("<response><result>" + result + "</result></response>");
		out.close();
		return null;

//		return new ActionForward(path.toString());
	}
	private String getDataRange(int dataRnageId){
		String result = "";
		switch(dataRnageId){
		case 1:{result = "����";break;}
		case 2:{result = "����";break;}
		case 3:{result = "����";break;}
		}
		return result;
	}
}
