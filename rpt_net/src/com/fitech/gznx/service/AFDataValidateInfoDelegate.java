package com.fitech.gznx.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import net.sf.hibernate.HibernateException;

import com.cbrc.smis.adapter.StrutsReportInDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.form.DataValidateInfoForm;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.proc.impl.Report;
import com.cbrc.smis.proc.po.DataValidateInfo;
import com.cbrc.smis.util.FitechException;
import com.fitech.gznx.po.AfDatavalidateinfo;
import com.fitech.gznx.util.TranslatorUtil;

public class AFDataValidateInfoDelegate {

	private static FitechException log = new FitechException(
			AFDataValidateInfoDelegate.class);

	/**
	 * jdbc技术 无特殊oracle语法
	 * 不需要修改 卞以刚 2011-12-26
	 * 将校验结果批量写入数据库
	 * 
	 * @param conn
	 *            Connection
	 * @param dataValidateInfoList
	 *            List 校验结果信息
	 * @param reportStyle
	 *            int 报表类别
	 * @param validateDesc
	 * 			  校验类别描述（1表内，２表间）
	 * @return boolean 写入成功，返回true；否则，返回false
	 * @exception Exception
	 */
	public static boolean writeDataValidateInfo(Connection conn, Integer repInId,
			List dataValidateInfoList,Integer validateDesc, Integer reportStyle) throws Exception {
		boolean result = false;

		if (conn == null || dataValidateInfoList == null)
			return false;

		Statement stmt = null;
		Statement ustmt = null;
		ResultSet urs = null;
		
		try {
			// if (conn.getAutoCommit() == true)
			// conn.setAutoCommit(false);

			stmt = conn.createStatement();
			ustmt = conn.createStatement();
			
			
			AfDatavalidateinfo dvi = null;
			String sql = "";

			// 删除重复的检验不通过的信息2007-09-30 龚明修改

			// String delSql = "delete from DATA_VALIDATE_INFO where REP_IN_ID="
			// + repInId;
			// stmt.execute(delSql);
			String usql = "select * from AF_DATAVALIDATEINFO where REP_ID="
				+ repInId + " and VALIDATE_DESC="+ validateDesc;
			
			if (ustmt.execute(usql)) {
				urs = ustmt.getResultSet();
			}
			
			if (urs != null && urs.next()) {
				
				if (reportStyle == Report.REPORT_STYLE_DD) {

					sql = "delete from AF_DATAVALIDATEINFO where REP_ID="
							+ repInId + " and VALIDATE_DESC=" + validateDesc;
				} else {

//					sql = "delete from qd_data_validate_info where rep_in_id="
//							+ dvi.getRepInId()
//							+ " and CELL_FORMU_ID="
//							+ dvi.getCellFormuId();
				}
				stmt.execute(sql);
			}
			
			for (int i = 0; i < dataValidateInfoList.size(); i++) {
				
				dvi = (AfDatavalidateinfo) dataValidateInfoList.get(i);
				
				if (reportStyle == Report.REPORT_STYLE_DD) {
					String sql1 = "INSERT INTO AF_DATAVALIDATEINFO(REP_ID,FORMULA_ID,VALIDATE_FLG,VALIDATE_DESC" +
							",SOURCE_VALUE,TARGET_VALUE)";
					sql = sql1.toUpperCase() + " VALUES("
							+ dvi.getId().getRepId() + ","
							+ dvi.getId().getFormulaId() + ","
							+ dvi.getValidateFlg() + ","
							+ dvi.getValidateDesc() + ",'"
							+ dvi.getSourceValue() + "','"
							+ dvi.getTargetValue() + "')";
				} else {
					// String sql2= "insert into
					// qd_data_validate_info(rep_in_id,validate_type_id,cell_formu_id,result)";
					// sql = sql2.toUpperCase()
					// + " values("
					// + dvi.getId().getRepId()
					// + ","
					// + dvi.getValidateTypeId()
					// + ","
					// + dvi.getCellFormuId()
					// + ","
					// + dvi.getResult()
					// + ")";
				}

				// System.out.println("::::sql is: "+sql);
				stmt.execute(sql);
				
				if (stmt.getUpdateCount() != 1) {
					result = false;
					break;
				}
				result = true;
			}
		} catch (SQLException sqle) {
			result = false;
			log.printStackTrace(sqle);
		} catch (Exception e) {
			result = false;
			log.printStackTrace(e);
		} finally {
			/*
			 * if (conn != null) { if (result == true) { try { conn.commit();
			 * conn.setAutoCommit(true); } catch (SQLException sqle1) { } } else {
			 * try { conn.rollback(); conn.setAutoCommit(true); } catch
			 * (SQLException sqle2) { } } }
			 */
			if (stmt != null)
				stmt.close();
		}

		return result;
	}

	/**
	 * 将表间校验结果批量写入数据库
	 * 
	 * @param conn
	 *            Connection
	 * @param dataValidateInfoList
	 *            List 校验结果信息
	 * @param reportStyle
	 *            int 报表类别
	 * @param validateDesc
	 * 			  校验类别描述（1表内，２表间）
	 * @return boolean 写入成功，返回true；否则，返回false
	 * @exception Exception
	 */
	public static boolean writeBJDataValidateInfo(Connection conn,
			List dataValidateInfoList,int validateDesc, int reportStyle) throws Exception {
		boolean result = false;

		if (conn == null || dataValidateInfoList == null)
			return false;

		Statement stmt = null;
		Statement ustmt = null;
		ResultSet urs = null;

		try {
		//	if (conn.getAutoCommit() == true)
		//		conn.setAutoCommit(false);

			stmt = conn.createStatement();
			ustmt = conn.createStatement();

			AfDatavalidateinfo dvi = null;
			String sql = "";
			String usql = "";
	

			for (int i = 0; i < dataValidateInfoList.size(); i++) {
				
				dvi = (AfDatavalidateinfo) dataValidateInfoList.get(i);
				
				if (reportStyle == Report.REPORT_STYLE_DD) {
					usql = "select * from AF_DATAVALIDATEINFO where REP_ID="
							+ dvi.getId().getRepId() + " and FORMULA_ID="
							+ dvi.getId().getFormulaId();
				} else {
//					usql = "select * from AF_QD_DATAVALIDATEINFO where REP_ID="
//						+ dvi.getId().getRepId() + " and FORMULA_ID="
//						+ dvi.getId().getFormulaId();
				}
				
				if (ustmt.execute(usql)) {
					urs = ustmt.getResultSet();
				}
				
				if (urs != null && urs.next()) {
					
					if (reportStyle == Report.REPORT_STYLE_DD) {

						sql = "delete from AF_DATAVALIDATEINFO where REP_ID="
								+ dvi.getId().getRepId() + " and FORMULA_ID="
								+ dvi.getId().getFormulaId();
					} else {

//						sql = "delete from qd_data_validate_info where rep_in_id="
//								+ dvi.getRepInId()
//								+ " and CELL_FORMU_ID="
//								+ dvi.getCellFormuId();
					}
					stmt.execute(sql);
				}
					
				if (reportStyle == Report.REPORT_STYLE_DD) {
					String sql1="INSERT INTO AF_DATAVALIDATEINFO(REP_ID,FORMULA_ID,VALIDATE_FLG,VALIDATE_DESC" +
							",SOURCE_VALUE,TARGET_VALUE)";
					sql = sql1.toUpperCase()
							+ " VALUES("
							+ dvi.getId().getRepId() + ","
							+ dvi.getId().getFormulaId() + ","
							+ dvi.getValidateFlg() + ","
							+ dvi.getValidateDesc() + ",'"
							+ dvi.getSourceValue() + "','"
							+ dvi.getTargetValue() + "')";
				} else {
//					String sql2="insert into qd_data_validate_info(rep_in_id,validate_type_id,cell_formu_id,result)";
//					sql = sql2.toUpperCase()
//							+ " values("
//							+ dvi.getRepInId()
//							+ ","
//							+ dvi.getValidateTypeId()
//							+ ","
//							+ dvi.getCellFormuId()
//							+ ","
//							+ dvi.getResult()
//							+ "')";
				}
					
				// // System.out.println("::::sql is: "+sql);
				// System.out.println("=="+sql);
				stmt.execute(sql);
				if (stmt.getUpdateCount() != 1) {
					result = false;
					break;
				}
			}

			result = true;
		} catch (SQLException sqle) {
			result = false;
			throw new Exception(sqle);
		} catch (Exception e) {
			result = false;
			throw new Exception(e);
		} finally {
			/*if (conn != null) {
				if (result == true) {
					try {
						conn.commit();
						conn.setAutoCommit(true);
					} catch (SQLException sqle1) {
					}
				} else {
					try {
						conn.rollback();
						conn.setAutoCommit(true);
					} catch (SQLException sqle2) {
					}
				}
			}*/
			 if(stmt != null)
                stmt.close();
            if(ustmt != null)
                ustmt.close();
		}

		return result;
	}

	/**
	 * 已使用hibernate 卞以刚 2011-12-27
	 * 影响对象：AfDatavalidateinfo
	 * 根据实际数据报表Id和校验类别获得校验信息列表
	 * 
	 * @author rds 
	 * @date 2006-01-02 23:14
	 * 
	 * @param repInId Integer 实际数据表ID
	 * @param validateTypeId 校验类别ID
	 * @return List
	 */
	public static List find(Integer repInId){
		
		List resList=null;
		StringBuffer buffer=new StringBuffer();
		if(repInId==null) return resList;
		
		DBConn conn=null;
		
		try{
			conn=new DBConn();
			
			String hql="from AfDatavalidateinfo dvi where dvi.id.repId=" +	repInId;
			
			List list=conn.openSession().find(hql);
			
			if(list!=null && list.size()>0){
				resList=new ArrayList();
				AfDatavalidateinfo dataValidateInfo=null;
				for(int i=0;i<list.size();i++){
					dataValidateInfo=(AfDatavalidateinfo)list.get(i);
					DataValidateInfoForm form=new DataValidateInfoForm();
					TranslatorUtil.copyPersistenceToVo(dataValidateInfo,form);
					
					String cellFormu = form.getCellFormu();
					List cellNames = com.cbrc.smis.proc.impl.Expression.getCellNames(form.getCellFormu());
					if(false && cellNames!=null && !cellNames.isEmpty()){
						ReportInForm reportInForm = AFReportDelegate.getReportIn(repInId);
						for (int j = 0; j < cellNames.size(); j++) {
							String cellName = (String)cellNames.get(j);
							String sql = "";
							if(cellName.indexOf("_")!=-1){
								String[] report = cellName.split("_");
								if(report!=null &&report.length>=3 && report[2].indexOf(".")!=-1){
									sql = "select a.rowName,a.colName,a.colNum,a.cellPid from AfCellinfo a where a.templateId='"+
											report[0]+"' and a.versionId='"+report[1]+"'"+
											" and a.colNum='"+report[2].substring(0, report[2].indexOf("."))+"' and a.cellPid='"+report[2].substring(report[2].indexOf(".")+1, report[2].length())+"' ";
								}else{
									sql = "select a.rowName,a.colName,a.colNum,a.cellPid from AfCellinfo a where a.templateId='"+
											report[0]+"' and a.versionId='"+report[1]+"'"+
									" and a.cellName='"+report[2]+"'";
								}
							}else{
								if(cellName.indexOf(".")!=-1){
									sql = "select a.rowName,a.colName,a.colNum,a.cellPid from AfCellinfo a where a.templateId='"+
											reportInForm.getChildRepId()+"' and a.versionId='"+reportInForm.getVersionId()+"'"+
									" and a.colNum='"+cellName.substring(0, cellName.indexOf("."))+"' and a.cellPid='"+cellName.substring(cellName.indexOf(".")+1, cellName.length())+"' ";
								}else{
									sql = "select a.rowName,a.colName,a.colNum,a.cellPid from AfCellinfo a where a.templateId='"+
											reportInForm.getChildRepId()+"' and a.versionId='"+reportInForm.getVersionId()+"'"+
									" and a.cellName='"+cellName+"'";
								}
							}
							Object[] objs = (Object[])conn.openSession().createQuery(sql).uniqueResult();
							String crName=objs[0]+"|"+objs[1];
							if(dataValidateInfo.getValidateType().getValidateTypeId().equals(Report.VALIDATE_TYPE_BJ)){
								if(cellName.indexOf("_")!=-1){
									String[] report = cellName.split("_");
									crName=report[0]+"_"+report[1]+"_"+crName;
								}
							}
							cellFormu = cellFormu.replace(cellName, crName);
						}
					}
					form.setCellFormuView(cellFormu.replaceAll("[A-Z]\\.", ""));
					
					resList.add(form);
				}
			}
		}catch(HibernateException he){
			resList=null;
			log.printStackTrace(he);
		}catch(Exception e){
			resList=null;
			log.printStackTrace(e);
		}finally{
			if(conn!=null) conn.closeSession();
		}
		
		return resList;
	}
}
