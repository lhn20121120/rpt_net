package com.fitech.gznx.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.apache.commons.beanutils.BeanUtils;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.hibernate.MCell;
import com.cbrc.smis.hibernate.ReportIn;
import com.cbrc.smis.util.FitechLog;
import com.fitech.gznx.common.Config;
import com.fitech.gznx.form.AFReportForm;
import com.fitech.gznx.po.AfCellinfo;
import com.fitech.gznx.po.AfPbocreportdata;
import com.fitech.gznx.po.AfPbocreportdataId;
import com.fitech.gznx.po.AfReport;

public class AFPbocReportDelegate {

	public static boolean buLing(List repInIds, String reportFlg , HttpServletRequest request) throws HibernateException {
		DBConn conn = new DBConn();
		Session session = conn.openSession();
		boolean reslut = false;
		Transaction tran = session.beginTransaction();
		String repInId="";
		try {
			if (repInIds != null && repInIds.size() > 0) {
				for (int i = 0; i <repInIds.size(); i++) {
					repInId = (String) repInIds.get(i);
					List AFPbocReports = getAllById(repInId);
					// af_pbocreportdata表存在该 repinid记录的将其所有值射为0
					AFReportForm reportInForm = null;
					reportInForm = AFReportDelegate.getReportIn(repInId + "");

					if (AFPbocReports != null && AFPbocReports.size() > 0) {
						for (int j = 0; j < AFPbocReports.size(); j++) {
							AfPbocreportdata afPbocreportdata = (AfPbocreportdata) AFPbocReports.get(j);
							afPbocreportdata.setCellData("0");
							session.update(afPbocreportdata);
						}
					} else {// af_pbocreportdata表不存在该 repinid记录的将就通过 repinid
						// 查到该报表的 模版编号和版本号 。
						// 在从af_cellinfo表中查询出该模版对应的所有 cell_id；在通过
						// cell_id，templateId，versionId 去 insert
						// af_pbocreportdata 对象保存
						List cellList = new ArrayList();
						if (reportInForm != null) {
							String templateId = reportInForm.getTemplateId();
							String versionId = reportInForm.getVersionId();
							cellList = StrutsAFCellDelegate.getAllCell(templateId, versionId, reportFlg);
							String cellId = "";
							AfPbocreportdata afPbocreportdata = null;

							for (Iterator iterator = cellList.iterator(); iterator.hasNext();) {
								if (reportFlg.equals(Config.CBRC_REPORT)) {
									MCell cell = (MCell) iterator.next();
									cellId = cell.getCellId() + "";
								} else {
									AfCellinfo cell = (AfCellinfo) iterator.next();
									cellId = cell.getCellId() + "";
								}
								afPbocreportdata = new AfPbocreportdata();

								afPbocreportdata.setCellData("0");
								afPbocreportdata.setId(new AfPbocreportdataId(new Long(repInId), new Long(cellId)));

								session.save(afPbocreportdata);
							}
						}
					}
					System.out.println(repInId + "报表已经补零");
					// 修改af_report checkFlg 的 状态为 审核通过。
//					new AFReportDelegate()
//							.updateReportInCheckFlag(repInId, com.cbrc.smis.common.Config.CHECK_FLAG_PASS);
					AfReport afReprot = new AfReport();
					BeanUtils.copyProperties(afReprot, reportInForm);
					afReprot.setCheckFlag(new Long(com.cbrc.smis.common.Config.CHECK_FLAG_PASS));
					afReprot.setCheckDate(new Date());
					afReprot.setReportDate(new Date());
					afReprot.setChecker("系统补零");
					session.update(afReprot);
					//添加补零日志
					FitechLog.writeRepLog(new Integer(12), "一键补零成功", request, repInId+"" ,reportFlg);
				}
			}
			tran.commit();

			reslut = true;
		} catch (Exception e) {
			tran.rollback();
			reslut = false;
			e.printStackTrace();
		}

		return reslut;
	}

	public static List getAllById(String repInId) throws HibernateException {
		List datas = new ArrayList();
		DBConn conn = new DBConn();
		Session session = conn.openSession();
		String hql = "from  AfPbocreportdata a where a.id.repId =" + repInId;
		try {
			datas = session.find(hql);
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		session.close();
		return datas;
	}

	// public static List findAll () throws Exception {
	// List retVals = new ArrayList();
	// javax.naming.InitialContext ctx = new javax.naming.InitialContext();
	// // TODO: Make adapter get SessionFactory jndi name by ant task argument?
	// net.sf.hibernate.SessionFactory factory =
	// (net.sf.hibernate.SessionFactory)ctx.lookup("java:AirlineHibernateFactory");
	// net.sf.hibernate.Session session = factory.openSession();
	// net.sf.hibernate.Transaction tx = session.beginTransaction();
	// retVals.addAll(session.find("from com.cbrc.smis.hibernate.ReportData"));
	// tx.commit();
	// session.close();
	// ArrayList reportData_vos = new ArrayList();
	// for (Iterator it = retVals.iterator(); it.hasNext(); ) {
	// com.cbrc.smis.form.ReportDataForm reportDataFormTemp = new
	// com.cbrc.smis.form.ReportDataForm();
	// com.cbrc.smis.hibernate.ReportData reportDataPersistence =
	// (com.cbrc.smis.hibernate.ReportData)it.next();
	// TranslatorUtil.copyPersistenceToVo(reportDataPersistence,
	// reportDataFormTemp);
	// reportData_vos.add(reportDataFormTemp);
	// }
	// retVals = reportData_vos;
	// return retVals;
	// }
	public static ReportIn getSimReportIn(Integer repInId) {		
		// TODO Auto-generated method stub
		DBConn conn = null;
		Session session = null;
		try {
			conn = new DBConn();
			session = conn.beginTransaction();

			String sql = "from ReportIn ri where ri.repInId = "+repInId;
			return (ReportIn)session.createQuery(sql).uniqueResult();
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return null;
	}
}
