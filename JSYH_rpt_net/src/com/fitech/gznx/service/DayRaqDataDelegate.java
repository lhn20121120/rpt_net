package com.fitech.gznx.service;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;

import com.cbrc.smis.adapter.StrutsMCurrDelegate;
import com.cbrc.smis.adapter.StrutsReportInDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.hibernate.MCurr;
import com.cbrc.smis.hibernate.ReportIn;
import com.cbrc.smis.other.Aditing;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.ReportExcelHandler;
import com.fitech.gznx.common.FileUtil;
import com.fitech.gznx.excel.NXReportExcelHandler;
import com.fitech.gznx.excel.QDReportExcelHandler;
import com.fitech.gznx.form.AFReportForm;
import com.fitech.gznx.form.DayTaskForm;
import com.fitech.gznx.po.AfReport;
import com.fitech.gznx.po.AfTemplate;
import com.fitech.net.action.UploadFileAction;
import com.runqian.report4.model.ReportDefine;
import com.runqian.report4.usermodel.Context;
import com.runqian.report4.usermodel.Engine;
import com.runqian.report4.usermodel.IReport;
import com.runqian.report4.util.ReportUtils;

public class DayRaqDataDelegate {

	/**
	 * 
	 * !!! 解析润乾报表，导出Excel并将Excel数据入库 <br>
	 * !!!参考SynsDataAction.java中的方法实现<br>
	 * 
	 * @return boolean
	 */
	public static boolean dataToDB(Aditing aditing, DayTaskForm form, String reportFlg) {
		FitechMessages messages = new FitechMessages();

		boolean result = true;
		String excelFile = null;
		try {
			/** 1.取数并导出Excel */
			String objDate = form.getTaskDate();
			String templateid = form.getTemplateId();
			String versionod = form.getVersionId();
			String dataRangeId = "1";// 默认境内口径
			String strorgId = aditing.getOrgId();
			String freqId = form.getRepFreqId();
			String curId = form.getCurId();

			String raqFileName = Config.RAQ_TEMPLATE_PATH + "templateFiles" + File.separator + "Raq" + File.separator;
			raqFileName += aditing.getChildRepId() + "_" + aditing.getVersionId() + ".raq";

			File raqFile = new File(raqFileName);
			FileInputStream inStream = new FileInputStream(raqFile);
			ReportDefine rd = (ReportDefine) ReportUtils.read(inStream);
			Context cxt = new Context(); // 构建报表引擎计算环境
			cxt.setParamValue("OrgID", strorgId);
			cxt.setParamValue("ReptDate", objDate.replace("-", ""));
			cxt.setParamValue("CCY", curId);
			cxt.setParamValue("RangeID", dataRangeId);
			cxt.setParamValue("Freq", Integer.valueOf(freqId).intValue());

			Engine engine = new Engine(rd, cxt); // 构造报表引擎
			IReport iReport = engine.calc(); // 运算报表

			excelFile = Config.WEBROOTPATH + "reportFiles" + File.separator + objDate + File.separator;
			File excelDir = new File(excelFile);
			if (!excelDir.exists()) {
				excelDir.mkdirs();
			}
			String excelFileName = excelFile + File.separator + templateid + "_" + versionod + "_" + strorgId + "_";
			excelFileName += curId + "_" + freqId + "_" + objDate.replace("-", "") + ".xls";
			// 润乾导出Excel
			ReportUtils.exportToExcel(excelFileName, iReport, false);

			/** 2.解析excel */
			String[] dataArr = objDate.split("-");
			String year = dataArr[0];
			String term = dataArr[1];
			String day = dataArr[2];
			// 监管报表
			ReportInForm reportInForm = new ReportInForm();

			if (curId == null || curId.equals(""))
				curId = "1";
			MCurr mCurr = StrutsMCurrDelegate.getISMCurr(curId);
			// 得到币种名称
			String CurrName = mCurr.getCurName();

			Integer repInId = null;
			// 预先插入新记录
			reportInForm.setChildRepId(templateid);
			reportInForm.setVersionId(versionod);
			reportInForm.setDataRangeId(new Integer(dataRangeId));
			reportInForm.setCurId(new Integer(curId));
			reportInForm.setYear(new Integer(year));
			reportInForm.setTerm(new Integer(term));
			reportInForm.setOrgId(strorgId);

			reportInForm.setOrgName(AFOrgDelegate.getOrgInfo(strorgId).getOrgName());
			// reportInForm.setVersionId(versionId);
			reportInForm.setTimes(new Integer(1));
			reportInForm.setReportDate(new Date());

			reportInForm.setCheckFlag(com.fitech.net.config.Config.CHECK_FLAG_UNREPORT);
			reportInForm.setCurName(CurrName);
			/** 已使用hibernate 卞以刚 2011-12-21 * */
			AfTemplate afTem = AFTemplateDelegate.getTemplate(reportInForm.getChildRepId(), reportInForm.getVersionId());
			String repName = "";
			if (afTem != null)
				repName = afTem.getTemplateName();
			reportInForm.setRepName(repName);
			// 插入数据
			StrutsReportInDelegate strutsReportInDelegate = null;
			AFReportDelegate AFstrutsReportInDelegate = null;
			if (reportFlg.equals(com.fitech.gznx.common.Config.CBRC_REPORT)) {// 银监会报表
				// 插入数据
				strutsReportInDelegate = new StrutsReportInDelegate();

				ReportIn reportIn = strutsReportInDelegate.insertNewReport(reportInForm);
				if (reportIn != null)
					repInId = reportIn.getRepInId();
			} else {
				// if(reportFlg.equals(com.fitech.gznx.common.Config.PBOC_REPORT)){//人行
				// 插入数据
				AFstrutsReportInDelegate = new AFReportDelegate();
				AFReportForm afReportForm = new AFReportForm();
				// 预先插入新记录
				afReportForm.setTemplateId(templateid);
				afReportForm.setVersionId(versionod);
				afReportForm.setDataRangeId(dataRangeId);
				afReportForm.setDay(day);
				afReportForm.setRepFreqId(freqId);
				afReportForm.setCurId(curId);
				afReportForm.setYear(year);
				afReportForm.setTerm(term);
				afReportForm.setOrgId(strorgId);
				afReportForm.setOrgName(AFOrgDelegate.getOrgInfo(strorgId).getOrgName());
				// reportInForm.setVersionId(versionId);
				afReportForm.setTimes("1");
				afReportForm.setReportDate(new Date());

				afReportForm.setCheckFlag(com.fitech.net.config.Config.CHECK_FLAG_UNREPORT.toString());
				reportInForm.setCurName(CurrName);
				AfReport reportIn = AFstrutsReportInDelegate.insertNewReport(afReportForm);
				if (reportIn != null)
					repInId = reportIn.getRepId().intValue();
			}

			UploadFileAction fileAction = new UploadFileAction();
			// 清单、点点分别处理
			if (afTem.getReportStyle().toString().equals(com.fitech.gznx.common.Config.REPORT_DD)) {

				// 解析 excel
				ReportExcelHandler excelHandler = null;
				NXReportExcelHandler NXexcelHandler = null;
				if (reportFlg.equals("1")) {
					excelHandler = new ReportExcelHandler(repInId, excelFileName);
				} else {
					// if(reportFlg.equals("2")){//人行
					NXexcelHandler = new NXReportExcelHandler(repInId, excelFileName, messages,afTem.getTemplateType());
				}

				/** 已使用Hibernate 卞以刚 2011-12-21 * */
				boolean flag = false;
				if (reportFlg.equals(com.fitech.gznx.common.Config.CBRC_REPORT)) {// 银监会
					flag = fileAction.getExcelReportISTrue(excelHandler.title, excelHandler.subTitle, versionod, templateid);
				} else {
					// if(reportFlg.equals(com.fitech.gznx.common.Config.PBOC_REPORT)){//人行
					flag = getExcelReportISTrue(NXexcelHandler.title, NXexcelHandler.subTitle, versionod, templateid);
				}
				if (!flag) {
					// 删除此文件
					File tempFile = new File(excelFile);
					if (tempFile.exists())
						tempFile.delete();
					/** * 失败处理 */
					if (!fileAction.messInfo.equals("")) {
						throw new Exception(fileAction.messInfo);
					} else {
						throw new Exception("报表名称匹配失败");
					}
				}

				if (reportFlg.equals(com.fitech.gznx.common.Config.CBRC_REPORT)) {// 银监会
					result = excelHandler.copyExcelToDB(false);
				} else {
					// if(reportFlg.equals(com.fitech.gznx.common.Config.PBOC_REPORT)){//人行
					result = NXexcelHandler.copyExcelToDB(reportFlg);
				}
				// result = excelHandler.copyExcelToDB();

			} else {
				// 清单
				// 验证清单式报表的标题和子标题
				QDReportExcelHandler qdExcelHandler = new QDReportExcelHandler(excelFileName, messages);
				if (qdExcelHandler.getMessages() != null && qdExcelHandler.getMessages().getSize() > 0) {
					messages = qdExcelHandler.getMessages();
					/** 失败处理（未实现） */
					throw new Exception("清单式报表的标题和子标题不符合");
				}
				/** 已使用Hibernate 卞以刚 2011-12-21 * */
				boolean flag = fileAction.getExcelReportISTrue(qdExcelHandler.title, qdExcelHandler.subTitle, versionod, templateid);
				if (!flag) {
					if (!fileAction.messInfo.equals("")) {
						throw new Exception(fileAction.messInfo);
					} else {
						throw new Exception("报表同步失败");
					}
				}

				// 将清单式报表的数据插入数据库

				result = new QDDataDelegate().qdIntoDB(templateid, versionod, repInId.toString(), excelFileName);

			}

			if (result) {// 直接审核通过，便于进行数据导出
				if (reportFlg.equals("1")) {// 总行
					result = strutsReportInDelegate.updateReportInCheckFlag_e(repInId, Config.CHECK_FLAG_PASS);
				} else {
					// if(reportFlg.equals("2")){
					result = AFstrutsReportInDelegate.updateReportInCheckFlag(repInId, Config.CHECK_FLAG_PASS);
				}
			}

			if (!result) {
				throw new Exception(reportInForm.getRepName() + "报表同步失败");
			}
		} catch (Exception e) {
			result = false;
			e.printStackTrace();
		} catch (Throwable e) {
			result = false;
			e.printStackTrace();
		} finally {// 删除临时文件
			FileUtil.deleteFile(excelFile);
		}

		return result;
	}

	private static boolean getExcelReportISTrue(String title, String subTitle, String versionId, String childRepId) {

		boolean flag = false;
		AfTemplate afTemplate = null;

		try {
			String titles = "";
			if (subTitle != null && !subTitle.equals(""))
				titles = title + "-" + subTitle;
			else
				titles = title;

			afTemplate = AFTemplateDelegate.getTemplate(childRepId, versionId);

			// System.out.println(afTemplate.getTemplateName());
			// System.out.println(titles.trim());
			boolean is = afTemplate.getTemplateName().equals(titles.trim());
			// System.out.println(is);
			if (afTemplate == null) {
				flag = false;
			} else if (afTemplate.getTemplateName().equals(titles.trim()) || afTemplate.getTemplateName().equals(title.trim())) {
				flag = true;
			} else {
				flag = false;
			}

		} catch (Exception ex) {
			flag = false;
			ex.printStackTrace();
		}

		return flag;

	}

}
