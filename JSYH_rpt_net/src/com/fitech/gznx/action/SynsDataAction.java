package com.fitech.gznx.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.smis.adapter.StrutsMChildReportDelegate;
import com.cbrc.smis.adapter.StrutsMCurrDelegate;
import com.cbrc.smis.adapter.StrutsMDataRgTypeDelegate;
import com.cbrc.smis.adapter.StrutsReportInDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.MChildReportForm;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.hibernate.MCurr;
import com.cbrc.smis.hibernate.MDataRgType;
import com.cbrc.smis.hibernate.MRepFreq;
import com.cbrc.smis.hibernate.ReportIn;
import com.cbrc.smis.other.Aditing;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.ReportExcelHandler;
import com.fitech.gznx.common.DateUtil;
import com.fitech.gznx.common.StringUtil;
import com.fitech.gznx.excel.NXReportExcelHandler;
import com.fitech.gznx.excel.QDReportExcelHandler;
import com.fitech.gznx.form.AFReportForm;
import com.fitech.gznx.po.AfReport;
import com.fitech.gznx.po.AfTemplate;
import com.fitech.gznx.service.AFOrgDelegate;
import com.fitech.gznx.service.AFReportDelegate;
import com.fitech.gznx.service.AFReportProductDelegate;
import com.fitech.gznx.service.AFTemplateDelegate;
import com.fitech.gznx.service.QDDataDelegate;
import com.fitech.net.action.UploadFileAction;
import com.runqian.report4.model.ReportDefine;
import com.runqian.report4.usermodel.Context;
import com.runqian.report4.usermodel.Engine;
import com.runqian.report4.usermodel.IReport;
import com.runqian.report4.util.ReportUtils;

/***
 * ͬ���ϱ�����
 * 
 * @author Administrator
 * 
 */
public class SynsDataAction extends Action {

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
        boolean isSynTrace = false;
		FitechMessages messages = new FitechMessages();
		HttpSession session = request.getSession();
		Operator operator = null;
		if (session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME) != null)
			operator = (Operator) session
					.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
		/** ����ѡ�б�־ **/
		String reportFlg = "0";
		String excelFile = null;
		File pathFile = null;

		if (session.getAttribute(Config.REPORT_SESSION_FLG) != null) {
			reportFlg = (String) session
					.getAttribute(Config.REPORT_SESSION_FLG);
		}
		
		if(request.getParameter("isaddtrace")!=null){
			isSynTrace = Boolean.valueOf(request.getParameter("isaddtrace"));
		}
		List fileList = null;

		List<String> pathList = new ArrayList<String>();

		try {
			String repNames = request.getParameter("repNames") != null ? request
					.getParameter("repNames")
					: null;
			String type = request.getParameter("type") != null ? request
					.getParameter("type") : "";

			String date = request.getParameter("date") != null ? request
					.getParameter("date") : "";

			String year = date.substring(0, 4);
			String term = date.substring(5, 7);

			String orgId = request.getParameter("orgId") != null ? request
					.getParameter("orgId") : "";
			String repFreqId = request.getParameter("repFreqId") != null ? request
					.getParameter("repFreqId")
					: "";

			// ���type������downAll �Ͳ�������ȫ��
			if (!type.equals("downAll")) {
				if (repNames != null && !repNames.equals("")) {
					String[] repNameArr = repNames.split(",");
					if (repNameArr != null && repNameArr.length > 0) {

						/** ��ѡ���� */
						fileList = new ArrayList();
						for (int i = 0; i < repNameArr.length; i++) {
							String[] repInfo = repNameArr[i].split("_");
							Map report = new HashMap();
							try {
								report.put("templateid", repInfo[0]);
								report.put("versionod", repInfo[1]);
								if (StringUtil.isEmpty(repInfo[2])) {
									report.put("dataRgId", "1");
								} else {
									report.put("dataRgId", repInfo[2]);
								}

								report.put("freqID", repInfo[3]);
								report.put("curId", repInfo[4]);
								report.put("orgId", repInfo[5]);

							} catch (Exception e) {
								e.printStackTrace();
							}
							String raqFileName = Config.RAQ_TEMPLATE_PATH
									+ "templateFiles" + File.separator + "Raq"
									+ File.separator + repInfo[0] + "_"
									+ repInfo[1] + ".raq";
							File file = new File(raqFileName);
							if (!file.exists()) {
								messages.add(repInfo[0] + repInfo[1]
										+ "û���ҵ�ģ���ļ�");
								continue;
							}
							report.put("file", file);
							fileList.add(report);
						}
					}
				}
			} else {
				String repName = request.getParameter("repName");
				if (repName != null) {
					repName = new String(repName.getBytes("ISO-8859-1"),"GB2312");
				}
				String templateType = request.getParameter("templateType") != null ? request
						.getParameter("templateType")
						: "";
				AFReportForm reportInForm = new AFReportForm();
				reportInForm.setOrgId(orgId);
				reportInForm.setDate(date);
				reportInForm.setRepName(repName);
				reportInForm.setTemplateType(templateType);
				reportInForm.setRepFreqId(repFreqId);
				List list = null;
				// ����ᱨ��
				if (reportFlg.equals(com.fitech.gznx.common.Config.CBRC_REPORT)) {
					list = AFReportProductDelegate.selectYJHReportList(
							reportInForm, operator);
				} else {
					list = AFReportProductDelegate.selectNOTYJHReportList(
							reportInForm, operator, reportFlg);
				}
				// ȡ��ģ��������������
				if (list != null && list.size() > 0) {
					fileList = new ArrayList();
					for (int i = 0; i < list.size(); i++) {
						Aditing aditing = (Aditing) list.get(i);
						Map report = new HashMap();
						try {
							report.put("templateid", aditing.getChildRepId());
							report.put("versionod", aditing.getVersionId());
							report.put("dataRgId", aditing.getDataRangeId());
							report.put("freqID", aditing.getActuFreqID());
							report.put("curId", aditing.getCurId());
							report.put("orgId", aditing.getOrgId());

						} catch (Exception e) {
							e.printStackTrace();
						}
						String raqFileName = Config.RAQ_TEMPLATE_PATH
								+ "templateFiles" + File.separator + "Raq"
								+ File.separator + aditing.getChildRepId()
								+ "_" + aditing.getVersionId() + ".raq";
						File file = new File(raqFileName);
						if (!file.exists()) {
							messages.add(aditing.getRepName() + "û���ҵ�ģ���ļ�");
							continue;
						}

						report.put("file", file);
						fileList.add(report);
					}
				}

			}
			if (fileList == null || fileList.size() <= 0) {
				if (messages.getMessages() != null
						&& messages.getMessages().size() > 0)
					request.setAttribute(Config.MESSAGES, messages);

			} else {
				//�����û�������Ŀ¼
				excelFile = Config.WEBROOTPATH + "reportFiles"
							+ File.separator + operator.getUserName() + File.separator;
				//����
				pathFile = new File(excelFile);
				if (!pathFile.canWrite())
					pathFile.mkdirs();
				
				// ����excel�ļ�
				for (int i = 0; i < fileList.size(); i++) {
					Map reportMap = (Map) fileList.get(i);
					InputStream inStream = null;
					try {
						String objDate = DateUtil.getFreqDateLast(date, Integer
								.valueOf(String
										.valueOf(reportMap.get("freqID"))));

						String templateid = (String) reportMap
								.get("templateid");
						String versionod = (String) reportMap.get("versionod");
						String dataRangeId = String.valueOf(reportMap
								.get("dataRgId"));
						String strorgId = (String) reportMap.get("orgId");
						String actuFreqID = String.valueOf(reportMap
								.get("freqID"));

						AfTemplate aftemplate = AFTemplateDelegate.getTemplate(
								templateid, versionod);
						if (aftemplate.getReportStyle() != null
								&& com.fitech.gznx.common.Config.REPORT_QD
										.equals(String.valueOf(aftemplate
												.getReportStyle()))) {
							reportMap.put("QDFlg",com.fitech.gznx.common.Config.PROFLG_SENCEN_QD);
						}
						String excelFileName = null;
						
						File file = (File) reportMap.get("file");
						inStream = new FileInputStream(file);
						ReportDefine rd = (ReportDefine) ReportUtils
								.read(inStream);
						Context cxt = new Context(); // ��������������㻷��
						cxt.setParamValue("OrgID", strorgId);
						cxt.setParamValue("ReptDate", objDate.replace("-", ""));
						String curId = String.valueOf(reportMap.get("curId"));
						String freqId = String.valueOf(reportMap.get("freqID"));

						cxt.setParamValue("CCY", curId);

						cxt.setParamValue("RangeID", reportMap.get("dataRgId"));
						cxt.setParamValue("Freq", Integer.valueOf(freqId).intValue());

						Engine engine = new Engine(rd, cxt); // ���챨������

						IReport iReport = engine.calc(); // ���㱨��
						
						
						excelFileName = excelFile + File.separator + templateid + "_" + versionod
								+ "_" + strorgId + "_" + curId + "_" + freqId
								+ "_" + objDate.replace("-", "") + ".xls";
											
						ReportUtils.exportToExcel(excelFileName, iReport, false);
						
						
						/** �ϴ���� */
						
						/** ����excel *************************************************/
						//��ܱ���
						ReportInForm reportInForm = new ReportInForm();
						boolean result = false;
						try {
							if (curId == null || curId.equals(""))
								curId = "1";
							/** ��ʹ��Hibernate ���Ը� 2011-12-21 **/
							MCurr mCurr = StrutsMCurrDelegate.getISMCurr(curId);
							// �õ���������
							String CurrName = mCurr.getCurName();

							Integer repInId = null;
							// Ԥ�Ȳ����¼�¼
							reportInForm.setChildRepId(templateid);
							reportInForm.setVersionId(versionod);
							reportInForm
									.setDataRangeId(new Integer(dataRangeId));
							reportInForm.setCurId(new Integer(curId));
							reportInForm.setYear(new Integer(year));
							reportInForm.setTerm(new Integer(term));
							reportInForm.setOrgId(strorgId);
							/** ��ʹ��hibernate ���Ը� 2011-12-21 **/
							reportInForm.setOrgName(AFOrgDelegate.getOrgInfo(
									strorgId).getOrgName());
							// reportInForm.setVersionId(versionId);
							reportInForm.setTimes(new Integer(1));
							reportInForm.setReportDate(new Date());

							reportInForm
									.setCheckFlag(com.fitech.net.config.Config.CHECK_FLAG_UNREPORT);
							reportInForm.setCurName(CurrName);
							/** ��ʹ��hibernate ���Ը� 2011-12-21 **/
							AfTemplate afTem = AFTemplateDelegate.getTemplate(reportInForm.getChildRepId(), reportInForm.getVersionId());
							String repName = "";
							if(afTem!=null)
								repName = afTem.getTemplateName();
							reportInForm.setRepName(repName);
							// ��������
							StrutsReportInDelegate strutsReportInDelegate = null;
							AFReportDelegate AFstrutsReportInDelegate = null;
							if(reportFlg.equals(com.fitech.gznx.common.Config.CBRC_REPORT)){//����ᱨ��
								// ��������
								strutsReportInDelegate = new StrutsReportInDelegate();
//								/** ��ʹ��hibernate ���Ը� 2011-12-21 **/
								ReportIn reportIn = strutsReportInDelegate.insertNewReport(reportInForm);
								if (reportIn != null)
									repInId = reportIn.getRepInId();
							}else{
						
							//if(reportFlg.equals(com.fitech.gznx.common.Config.PBOC_REPORT)){//����
								// ��������
								AFstrutsReportInDelegate = new AFReportDelegate();
								AFReportForm afReportForm = new AFReportForm();
								// Ԥ�Ȳ����¼�¼
								afReportForm.setTemplateId(templateid);
								afReportForm.setVersionId(versionod);
								afReportForm.setDataRangeId(dataRangeId);
								afReportForm.setDay(date.split("-")[2]);
								afReportForm.setRepFreqId(freqId);
								afReportForm.setCurId(curId);
								afReportForm.setYear(year);
								afReportForm.setTerm(term);
								afReportForm.setOrgId(strorgId);
								/** ��ʹ��hibernate ���Ը� 2011-12-21 **/
								afReportForm.setOrgName(reportInForm.getOrgName());
								// reportInForm.setVersionId(versionId);
								afReportForm.setTimes("1");
								afReportForm.setReportDate(new Date());

								afReportForm
										.setCheckFlag(com.fitech.net.config.Config.CHECK_FLAG_UNREPORT.toString());
								afReportForm.setCurName(CurrName);
								afReportForm.setRepName(afTem.getTemplateName());
								AfReport reportIn = AFstrutsReportInDelegate.insertNewReport(afReportForm);
								if (reportIn != null)
									repInId = reportIn.getRepId().intValue();
							}
							
							UploadFileAction fileAction = new UploadFileAction();
							// �嵥�����ֱ���
							if (afTem.getReportStyle().toString().equals(
									com.fitech.gznx.common.Config.REPORT_DD)) {

								// ���� excel
								ReportExcelHandler excelHandler = null;
								NXReportExcelHandler NXexcelHandler = null;
								if(reportFlg.equals("1")){
									excelHandler = new ReportExcelHandler(repInId, excelFileName);
								}else{
								//if(reportFlg.equals("2")){//����
									NXexcelHandler = new NXReportExcelHandler(repInId, excelFileName,messages,afTem.getTemplateType());
								}
								
								/** ��ʹ��Hibernate ���Ը� 2011-12-21 **/
								boolean flag = false;
								if(reportFlg.equals(com.fitech.gznx.common.Config.CBRC_REPORT)){//�����
									flag = fileAction.getExcelReportISTrue(
											excelHandler.title,
											excelHandler.subTitle, versionod,
											templateid);
								}else{
								//if(reportFlg.equals(com.fitech.gznx.common.Config.PBOC_REPORT)){//����
									flag = getExcelReportISTrue(NXexcelHandler.title, NXexcelHandler.subTitle, versionod, templateid);
								}
								if (!flag) {
									/*** ʧ�ܴ��� */
									if (!fileAction.messInfo.equals("")) {
										messages.add(fileAction.messInfo);
									} else {
										messages.add("����ͬ��ʧ��");
									}
									request.setAttribute(com.cbrc.smis.common.Config.MESSAGES,fileAction.messInfo);
									
									//ɾ�����ļ�
									File tempFile = new File(excelFile);
									if (tempFile.exists())
										tempFile.delete();
									continue;
								}

								/** 1008��� */
								if(reportFlg.equals(com.fitech.gznx.common.Config.CBRC_REPORT)){//�����
									result = excelHandler.copyExcelToDB(isSynTrace);
								}else{
								//if(reportFlg.equals(com.fitech.gznx.common.Config.PBOC_REPORT)){//����
									result = NXexcelHandler.copyExcelToDB(reportFlg);
								}
								//result = excelHandler.copyExcelToDB();

							} else {
								// �嵥
								// ��֤�嵥ʽ����ı�����ӱ���
								QDReportExcelHandler qdExcelHandler = new QDReportExcelHandler(
										excelFileName, messages);
								if (qdExcelHandler.getMessages() != null
										&& qdExcelHandler.getMessages().getSize() > 0) {
									messages = qdExcelHandler.getMessages();
									request.setAttribute(com.cbrc.smis.common.Config.MESSAGES,messages);
									/** ʧ�ܴ���δʵ�֣� */
									throw new Exception("�嵥ʽ����ı�����ӱ��ⲻ����");
								}
								/** ��ʹ��Hibernate ���Ը� 2011-12-21 **/
								boolean flag = fileAction.getExcelReportISTrue(
										qdExcelHandler.title,
										qdExcelHandler.subTitle, versionod,
										templateid);
								if (!flag) {
									if (!fileAction.messInfo.equals("")) {
										messages.add(fileAction.messInfo);
									} else {
										messages.add("����ͬ��ʧ��");
									}
									request.setAttribute(com.cbrc.smis.common.Config.MESSAGES,messages);
									/** ʧ�ܴ���δʵ�֣� */
									throw new Exception("����ͬ��ʧ��");
								}

								// ���嵥ʽ��������ݲ������ݿ�
								/** ʹ��jdbc ��Ҫ�޸� ���Ը� 2011-12-21 **/
								result = new QDDataDelegate().qdIntoDB(templateid, versionod, repInId.toString(), excelFileName);

							}

							if (result){
								/** ��ʹ��hibernate ���Ը� 2011-12-21 **/
								if(reportFlg.equals("1")){//����
									result = strutsReportInDelegate.updateReportInCheckFlag_e(repInId,Config.CHECK_FLAG_AFTERSAVE);
								}else{
								//if(reportFlg.equals("2")){
									result = AFstrutsReportInDelegate.updateReportInCheckFlag(repInId,Config.CHECK_FLAG_AFTERSAVE);
								}
							}
								

							if (!result) {
								messages.add(reportInForm.getRepName()+"����ͬ��ʧ��");
								request.setAttribute(com.cbrc.smis.common.Config.MESSAGES,messages);
								throw new Exception(reportInForm.getRepName()+"����ͬ��ʧ��");						
							}
							

						} catch (Exception e) {
							messages.add(reportInForm.getRepName()+"����ͬ��ʧ��");
							e.printStackTrace();
						
							continue;
						}

						//ɾ���ļ�
						File tempFile = new File(excelFileName);
						if(tempFile.exists())
							tempFile.delete();
						messages.add(reportInForm.getRepName()+"����ͬ���ɹ�");
						request.setAttribute(com.cbrc.smis.common.Config.MESSAGES, messages);
					
					
						
					} catch (Exception e) {
						e.printStackTrace();
						messages.add("ϵͳæ���Ժ����ԣ�");
					} catch (Throwable e1) {
						e1.printStackTrace();
						messages.add("ϵͳæ���Ժ����ԣ�");
					} finally {
						if (inStream != null) {
							inStream.close();
						}
					}
				}

			}

			if (messages.getMessages() == null
					|| messages.getMessages().size() == 0)
				messages.add("true");
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			//ɾ���ļ���
			if(pathFile!=null){
				pathFile.delete();
				pathFile = null;
			}
		}
		if (messages.getMessages() != null && messages.getMessages().size() > 0)
			request.setAttribute("message", messages.getMessages());
		
		//response.setCharacterEncoding("gb2312");
		//PrintWriter out = response.getWriter();

		//response.setContentType("text/xml");
		//response.setHeader("Cache-control", "no-cache");

		//out.println("<response><result>" + messages.getMessages() + "</result></response>");
		//out.close();
		return mapping.findForward("index");

	}
	
	private boolean getExcelReportISTrue(String title, String subTitle, String versionId, String childRepId) {
		
		boolean flag = false;

		AfTemplate afTemplate = null;

		try {
			String titles = "";
			if(subTitle!=null && !subTitle.equals("") )
				titles = title + "-" + subTitle;
			else
				titles = title;
			
			afTemplate = AFTemplateDelegate.getTemplate(childRepId, versionId);
			
//			System.out.println(afTemplate.getTemplateName());
//			System.out.println(titles.trim());
			boolean is = afTemplate.getTemplateName().equals(titles.trim());
//			System.out.println(is);
			if (afTemplate == null) {
				
				flag = false;

			} else if ( afTemplate.getTemplateName().equals(titles.trim())
					|| afTemplate.getTemplateName().equals(title.trim())) {
				
				flag = true;
				
			} else {
				
				flag = false;
				
			}

		}
		catch (Exception ex) {
			
			flag = false;
			ex.printStackTrace();

		}

		return flag;
		
	}
}
