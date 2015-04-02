package com.cbrc.smis.proc.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.proc.po.AbnormityChange;
import com.cbrc.smis.proc.po.DataValidateInfo;
import com.cbrc.smis.proc.po.MCellFormu;
import com.cbrc.smis.proc.po.ReportIn;

/**
 * ����ķ���ʵ����
 * 
 * @author rds
 * @serialData 2005-12-20 21:14
 */
public class ReportImpl {
	/**
	 * ��õ�Ե�ʽ����ĵ�Ԫ���ֵ
	 * 
	 * @param conn
	 *            Connection ���ݿ�����
	 * @param repInId
	 *            int ʵ�ʱ���ID
	 * @param childRepId
	 *            String �ӱ���Id
	 * @param versionId
	 *            String �汾��
	 * @param cellName
	 *            String ��Ԫ������
	 * @return String ��Ԫ�����ֵ
	 */
	public static String getCellValue(Connection conn, int repInId,
			String childRepId, String versionId, String cellName)
			throws Exception {
		String value = "0";

		if (conn == null || repInId <= 0 || childRepId == null
				|| versionId == null || cellName == null)
			return value;

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			String sql = "select rii.report_value from report_in_info rii where rii.rep_in_id=?".toUpperCase()
					+ " and rii.cell_id=(select mc.cell_id from m_cell mc where mc.child_rep_id=? and ".toUpperCase()
					+ " mc.version_id=? and mc.cell_name=?)".toUpperCase();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, repInId);
			pstmt.setString(2, childRepId);
			pstmt.setString(3, versionId);
			pstmt.setString(4, cellName);
			rs = pstmt.executeQuery();
			if (rs != null && rs.next()) {
				value = rs.getString("report_value".toUpperCase());
			}
		} catch (SQLException sqle) {
			throw new Exception(sqle.getMessage());
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		}

		if (null == value || value.trim().equals(""))
			value = "0";

		return value;
	}

	/**
	 * ��õ�Ե�ʽ����ĵ�Ԫ���ֵ
	 * 
	 * @param conn
	 *            Connection ���ݿ�����
	 * @param repInId
	 *            int ʵ�ʱ���ID
	 * @param childRepId
	 *            String �ӱ���Id
	 * @param versionId
	 *            String �汾��
	 * @param dataRangeId
	 *            int ���ݷ�ΧID
	 * @param orgId
	 *            String ����ID
	 * @param curId
	 *            int ���ҵ�λID
	 * @param year
	 *            int ���
	 * @param term
	 *            int ����
	 * @param cellName
	 *            String ��Ԫ������
	 * @param flag
	 *            int ��־ֵ
	 * @return String ��Ԫ�����ֵ
	 */
	public static String getCellValue(Connection conn, int repInId,
			String childRepId, String versionId, int dataRangeId, String orgId,
			int curId, int year, int term, String cellName, int flag)
			throws Exception {
		String value = "0";

		if (conn == null || repInId <= 0 || childRepId == null
				|| versionId == null || dataRangeId <= 0 || orgId == null
				|| curId <= 0 || year <= 0 || term <= 0 || cellName == null)
			return value;

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			String sql = "";
			if (flag == 2) {
				sql = "select rii.report_value from report_in_info rii where rii.rep_in_id=(".toUpperCase()
						+ "select ri.rep_in_id from report_in ri where ri.child_rep_id='".toUpperCase()
						+ childRepId
						+ "'"
						+ " and ri.version_id='".toUpperCase()
						+ versionId
						+ "' "
						+ " and ri.data_range_id=".toUpperCase()
						+ dataRangeId
						+ " and ri.org_id='".toUpperCase()
						+ orgId
						+ "' and ri.cur_id=".toUpperCase()
						+ curId
						+ " and ri.year=".toUpperCase()
						+ year
						+ " and ri.term=".toUpperCase()
						+ term
						+ " and "
						+ "ri.times in (select max(r.times) from report_in r where r.child_rep_id='".toUpperCase()
						+ childRepId
						+ "' and r.version_id='".toUpperCase()
						+ versionId
						+ "'"
						+ "and r.data_range_id=".toUpperCase()
						+ dataRangeId
						+ " and r.org_id='".toUpperCase()
						+ orgId
						+ "' and r.cur_id=".toUpperCase()
						+ curId
						+ " and r.year=".toUpperCase()
						+ year
						+ " and r.term=".toUpperCase()
						+ term
						+ ")) and "
						+ "rii.cell_id=(select cell_id from m_cell mc where mc.child_rep_id='".toUpperCase()
						+ childRepId
						+ "' and mc.version_id='".toUpperCase()
						+ versionId
						+ "' and mc.cell_name='".toUpperCase() + cellName + "')";
			} else {
				sql = "select rii.report_value from report_in_info rii where rii.rep_in_id=".toUpperCase()
						+ repInId
						+ " and "
						+ "rii.cell_id=(select cell_id from m_cell mc where mc.child_rep_id='".toUpperCase()
						+ childRepId
						+ "' and mc.version_id='".toUpperCase()
						+ versionId
						+ "' and mc.cell_name='".toUpperCase() + cellName + "')";
			}

			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs != null && rs.next()) {
				value = rs.getString("report_value".toUpperCase());
			}
		} catch (SQLException sqle) {
			throw new Exception(sqle.getMessage());
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		}

		if (value.trim().equals(""))
			value = "0";

		return value;
	}

	/**
	 * ���������ʽ��ֵ
	 * 
	 * @param conn
	 *            Connection ��������
	 * @param calculatorSql
	 *            String ����SQL
	 * @param arrLength
	 *            in ���ؽ��������ĳ���
	 * @return float[] �޽��������null
	 * @exception Exception
	 */
	public static String getCalculatorFormu(Connection conn, String valueFormu)
			throws Exception {
		String value = "0";

		if (conn == null || valueFormu == null)
			return value;

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			String sql = "select " + valueFormu + " from (values 1) as tbl";

			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs != null && rs.next()) {
				value = rs.getString(1);
			}
		} catch (SQLException sqle) {
			value = "0";
			// throw new Exception(sqle.getMessage());
		} catch (Exception e) {
			value = "0";
			// throw new Exception(e.getMessage());
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		}

		return value;
	}

	/**
	 * �������ֵ
	 * 
	 * @param conn
	 *            Connection ��������
	 * @param calculatorSql
	 *            String ����SQL
	 * @param arrLength
	 *            in ���ؽ��������ĳ���
	 * @return String[] �޽��������null
	 * @exception Exception
	 */
	public static String[] getCalculatorResult(Connection conn,
			String calculatorSql, int arrLength) {
		String[] arr = null;

		if (conn == null || calculatorSql == null || arrLength <= 0)
			return null;
		/** ���oracle���ݿ�,��doubleת��ΪTO_NUMBER */
		calculatorSql = calculatorSql.replaceAll("double", "TO_NUMBER");
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
	//		String sql = "select " + calculatorSql + " from (values 1) as tbl";
			String sql = "select " + calculatorSql + " from dual";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs != null && rs.next()) {
				arr = new String[arrLength];
				for (int i = 0; i < arrLength; i++) {
					arr[i] = rs.getString(i + 1);
				}
			}
		} catch (SQLException sqle) {
			arr = null;
		} catch (Exception e) {
			arr = null;
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
			}
		}

		return arr;
	}

	/**
	 * �������ֵ
	 * 
	 * @param conn
	 *            Connection ��������
	 * @param calculatorSql
	 *            String ����SQL
	 * @return String �޽��������null
	 * @exception Exception
	 */
	public static String getExpressionResult(Connection conn,
			String calculatorSql) throws Exception {
		String res = "0";

		if (conn == null || calculatorSql == null)
			return res;

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			String sql = "select " + calculatorSql.replaceAll("/", "*double(1.0)/")
					+ " from (values 1) as tbl";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs != null && rs.next()) {
				res = rs.getString(1);
			}
		} catch (SQLException sqle) {
			res = "0";
		} catch (Exception e) {
			res = "0";
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		}

		if (res.trim().equals(""))
			res = "0";

		return res;
	}

	/**
	 * ����嵥ʽ������ʽ������ֵ��
	 * 
	 * @param conn
	 *            Connection ��������
	 * @param calculatorSql
	 *            String ����SQL
	 * @param arrLength
	 *            in ���ؽ��������ĳ���
	 * @return List �޽��������null
	 * @exception Exception
	 */
	public static List getCalculatorResults(Connection conn,
			String calculatorSql, int arrLength) throws Exception {
		List resList = null;

		if (conn == null || calculatorSql == null || arrLength <= 0)
			return null;
		if (calculatorSql.equals(""))
			return null;

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			resList = new ArrayList();
			pstmt = conn.prepareStatement(calculatorSql);
			rs = pstmt.executeQuery();
			if (rs != null && rs.next()) {
//				resList = new ArrayList();
				do {
					double[] arr = new double[arrLength];
					for (int i = 0; i < arrLength; i++) {
						arr[i] = rs.getFloat(i + 1);
					}
					resList.add(arr);
				} while (rs.next());
			}
		} catch (SQLException sqle) {
			throw new Exception(sqle.getMessage());
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		}

		return resList;
	}

	/**
	 * �����ӱ���ID�Ͱ汾�Ż���嵥ʽ����Ķ�Ӧ��ʵ�����ݴ洢��Table
	 * 
	 * @param conn
	 *            Connection ���ݿ�����
	 * @param childRepId
	 *            String �ӱ���ID
	 * @param versionId
	 *            String �汾��
	 * @return String ���ݱ����ƣ���δ���ҵ�������null
	 */
	public static String findTableName(Connection conn, String childRepId,
			String versionId) {
		String tbl = null;

		if (conn == null || childRepId == null || versionId == null)
			return null;

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			String sql = "select table_name from listing_tables where child_rep_id=? and version_id=?".toUpperCase();

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, childRepId);
			pstmt.setString(2, versionId);
			rs = pstmt.executeQuery();

			if (rs != null && rs.next()) {
				tbl = rs.getString("table_name".toUpperCase());
			}
		} catch (SQLException sqle) {
			tbl = null;
			// sqle.printStackTrace();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException sqle) {
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException sqle) {
				}
		}

		return tbl;
	}

	/**
	 * �����У��������д�����ݿ�
	 * 
	 * @param conn
	 *            Connection
	 * @param dataValidateInfoList
	 *            List У������Ϣ
	 * @param reportStyle
	 *            int �������
	 * @return boolean д��ɹ�������true�����򣬷���false
	 * @exception Exception
	 */
	public static boolean writeBJDataValidateInfo(Connection conn,
			List dataValidateInfoList, int reportStyle) throws Exception {
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

			DataValidateInfo dvi = null;
			String sql = "";
			String usql = "";
	

			for (int i = 0; i < dataValidateInfoList.size(); i++) {
				dvi = (DataValidateInfo) dataValidateInfoList.get(i);
				if (reportStyle == Report.REPORT_STYLE_DD) {
					usql = "select * from DATA_VALIDATE_INFO where REP_IN_ID="
							+ dvi.getRepInId() + " and CELL_FORMU_ID="
							+ dvi.getCellFormuId();
				} else {
					usql = "select * from QD_DATA_VALIDATE_INFO where REP_IN_ID="
						+ dvi.getRepInId() + " and CELL_FORMU_ID="
						+ dvi.getCellFormuId();
				}
				if (ustmt.execute(usql)) {
					urs = ustmt.getResultSet();
				}
				if (urs != null && urs.next()) {
					if (reportStyle == Report.REPORT_STYLE_DD) {
						// sql="update data_validate_info set
						// result="+dvi.getResult()+" where
						// rep_in_id="+dvi.getRepInId()+" and
						// cell_formu_id="+dvi.getCellFormuId();
						sql = "delete from DATA_VALIDATE_INFO where REP_IN_ID="
								+ dvi.getRepInId() + " and CELL_FORMU_ID="
								+ dvi.getCellFormuId();
					} else {
						// sql="update qd_data_validate_info set
						// result="+dvi.getResult()+" where
						// rep_in_id="+dvi.getRepInId()+" and
						// cell_formu_id="+dvi.getCellFormuId();
						sql = "delete from qd_data_validate_info where rep_in_id="
								+ dvi.getRepInId()
								+ " and CELL_FORMU_ID="
								+ dvi.getCellFormuId();
					}
					stmt.execute(sql);
				}
				if(Config.DB_SERVER_TYPE.equals("sybase")){
					
					if (reportStyle == Report.REPORT_STYLE_DD) {
						String sql1="insert into data_validate_info(rep_in_id,cell_formu_id,validate_type_id,result,source_value,target_value)";
						sql = sql1.toUpperCase()
								+ " values("
								+ dvi.getRepInId()
								+ ","
								+ dvi.getCellFormuId()
								+ ","
								+ dvi.getValidateTypeId()
								+ ","
								+ dvi.getResult()
								+ ",'"
								+ dvi.getSourceValue()
								+ "','"
								+ dvi.getTargetValue()
								+ "')";
					} else {
//						String sql2="insert into qd_data_validate_info(rep_in_id,validate_type_id,cell_formu_id,result)";
//						sql = sql2.toUpperCase()
//								+ " values("
//								+ dvi.getRepInId()
//								+ ","
//								+ dvi.getValidateTypeId()
//								+ ","
//								+ dvi.getCellFormuId()
//								+ ","
//								+ dvi.getResult()
//								+ "')";
					}
					
					
					
					
				}else{
					if (reportStyle == Report.REPORT_STYLE_DD) {
						sql = "insert into data_validate_info(rep_in_id,seq_no,cell_formu_id,validate_type_id,result,source_value,target_value)"
								+ " values("
								+ dvi.getRepInId()
								+ ","
								+ dvi.getSeqNo()
								+ ","
								+ dvi.getCellFormuId()
								+ ","
								+ dvi.getValidateTypeId()
								+ ","
								+ dvi.getResult()
								+ ",'"
								+ dvi.getSourceValue()
								+ "','"
								+ dvi.getTargetValue()
								+ "')";
					} else {
//						sql = "insert into qd_data_validate_info(rep_in_id,validate_type_id,cell_formu_id,result)"
//								+ " values("
//								+ dvi.getRepInId()
//								+ ","
//								+ dvi.getValidateTypeId()
//								+ ","
//								+ dvi.getCellFormuId()
//								+ ","
//								+ dvi.getResult()
//								+ "')";
					}
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
	 * ��У��������д�����ݿ�
	 * 
	 * @param conn
	 *            Connection
	 * @param dataValidateInfoList
	 *            List У������Ϣ
	 * @param reportStyle
	 *            int �������
	 * @return boolean д��ɹ�������true�����򣬷���false
	 * @exception Exception
	 */
	public static boolean writeDataValidateInfo(Connection conn,int repInId,
			List dataValidateInfoList, int reportStyle) throws Exception {
		boolean result = false;

		if (conn == null || dataValidateInfoList == null)
			return false;

		Statement stmt = null;

		try {
		//	if (conn.getAutoCommit() == true)
		//		conn.setAutoCommit(false);

			stmt = conn.createStatement();

			DataValidateInfo dvi = null;
			String sql = "";
            //ɾ���ظ��ļ��鲻ͨ������Ϣ2007-09-30 �����޸�
			
		//	String delSql = "delete from DATA_VALIDATE_INFO where REP_IN_ID=" + repInId;
        //    stmt.execute(delSql);
            
			for (int i = 0; i < dataValidateInfoList.size(); i++) {
				dvi = (DataValidateInfo) dataValidateInfoList.get(i);
				if(Config.DB_SERVER_TYPE.equals("sybase")){
					
					if (reportStyle == Report.REPORT_STYLE_DD) {
						String sql1="insert into data_validate_info(rep_in_id,cell_formu_id,validate_type_id,result,source_value,target_value)";
						sql = sql1.toUpperCase()
								+ " values("
								+ dvi.getRepInId()
								+ ","
								+ dvi.getCellFormuId()
								+ ","
								+ dvi.getValidateTypeId()
								+ ","
								+ dvi.getResult()
								+ ",'"
								+ dvi.getSourceValue()
								+ "','"
								+ dvi.getTargetValue()
								+ "')";
					} 
				}else{
					if (reportStyle == Report.REPORT_STYLE_DD) {
						sql = "insert into data_validate_info(rep_in_id,seq_no,cell_formu_id,validate_type_id,result,source_value,target_value)"
							+ " values("
								+ dvi.getRepInId()
								+ ","
								+ dvi.getSeqNo()
								+ ","
								+ dvi.getCellFormuId()
								+ ","
								+ dvi.getValidateTypeId()
								+ ","
								+ dvi.getResult()
								+ ",'"
								+ dvi.getSourceValue()
								+ "','"
								+ dvi.getTargetValue()
								+ "')";
					}
				}
				stmt.execute(sql);
				if (stmt.getUpdateCount() != 1) {
					result = false;
					break;
				}
				result = true;
			}
		} catch (SQLException sqle) {
			result = false;
			throw new Exception(sqle);
		} catch (Exception e) {
			result = false;
			throw new Exception(e);
		} 
		finally {
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
		}

		return result;
	}

	/**
	 * ����嵥ʽ����ĺϼ����е�ĳ�е�ֵ
	 * 
	 * @param conn
	 *            Connection ���ݿ�����
	 * @param table
	 *            String ʵ�����ݱ���
	 * @param repInId
	 *            int ʵ�����ݱ����ID
	 * @param colName
	 *            String �ϼ���ֵ
	 * @return String
	 * @exception Exception
	 */
	public static String getTotalCellValue(Connection conn, String table,
			int repInId, String colName) throws Exception {
		String res = "0";

		if (conn == null || table == null || colName == null)
			return res;

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			String sql = "select " + colName.toUpperCase() + " from " + table.toUpperCase()
					+ " where rep_in_id=".toUpperCase() + repInId
					+ " and replace(col1,' ','')='�ϼ�'".toUpperCase();

			pstmt = conn.prepareStatement(sql);
			/*
			 * pstmt.setInt(1,repInId); pstmt.setString(2,"�ϼ�");
			 */
			rs = pstmt.executeQuery();
			if (rs != null && rs.next()) {
				res = rs.getString(colName);
			}
		} catch (SQLException sqle) {
			throw new Exception(sqle.getMessage());
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		}

		if (res.trim().equals(""))
			res = "0";

		return res;
	}

	/**
	 * ʹ��jdbc ���ܲ���Ҫ�޸� ���Ը� 2011-12-21
	 * ���ʵ�����ݱ������
	 * 
	 * @param conn
	 *            Connection ���ݿ�����
	 * @param repInId
	 *            int ʵ�����ݱ���ID
	 * @param ReportIn
	 * @return Exception
	 */
	public static ReportIn getReportIn(Connection conn, int repInId)
			throws Exception {
		if (conn == null) return null;

		ReportIn reportIn = null;
      
        //����2007-09-24�޸�
        PreparedStatement  pStmt = null;
        ResultSet plRS = null;
        try {
            String plSql = "select a.child_rep_id,a.version_id,a.data_range_id,a.org_id, ";
                    plSql += "a.cur_id,a.year,a.term,b.report_style,b.rep_freq_id ";
                    plSql += "from report_in a, view_m_report b where ";
                    plSql += "a.rep_in_id = ? and ";
                    plSql += "a.child_rep_id = b.child_rep_id and a.version_id = b.version_id";

            pStmt = conn.prepareStatement(plSql.toUpperCase());
            pStmt.setInt(1, repInId);
            plRS = pStmt.executeQuery();         
            
            if (plRS != null && plRS.next()){
                reportIn = new ReportIn();               
                reportIn.setRepInId(repInId);// �ӱ���ID
                reportIn.setVersionId(plRS.getString("version_id".toUpperCase()));// �汾��
                reportIn.setChildRepId(plRS.getString("child_rep_id".toUpperCase()));
                reportIn.setDataRangeId(plRS.getInt("data_range_id".toUpperCase())); // ���ݷ�ΧID
                reportIn.setOrgId(plRS.getString("org_id".toUpperCase()));// ����ID
                reportIn.setCurId(plRS.getInt("cur_id".toUpperCase()));// ���ҵ�λID
                reportIn.setYear(plRS.getInt("year".toUpperCase()));// �������
                reportIn.setTerm(plRS.getInt("term".toUpperCase()));// ��������
                reportIn.setReportStyle(plRS.getInt("report_style".toUpperCase()));
                reportIn.setRepFreqId(plRS.getInt("rep_freq_id".toUpperCase()));

            }
        } 
//////////////////////////////////////
        catch (SQLException sqle) {
			reportIn = null;
			throw new Exception(sqle.getMessage());
		} catch (Exception e) {
			reportIn = null;
			throw new Exception(e.getMessage());
		} finally {
			if (plRS != null)
				plRS.close();
			if (pStmt != null)
				pStmt.close();
		}

		return reportIn;
	}

	/**
	 * ��ñ�������<br>
	 * �����ֵΪ1:��ʱ��Ե�ʽ����<br>
	 * �����ֵΪ2:��ʱ�嵥ʽ����<br>
	 * 
	 * @param conn
	 *            Connection ��������
	 * @param childRepId
	 *            String �ӱ���ID
	 * @param versionId
	 *            String �汾��
	 * @return int
	 * @exception Exception
	 */
	public static int getReportStyle(Connection conn, String childRepId,
			String versionId) throws Exception {
		int reportStyle = 0;

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			String sql = "select report_style from m_child_report where child_rep_id=? and version_id=?".toUpperCase();

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, childRepId);
			pstmt.setString(2, versionId);
			rs = pstmt.executeQuery();
			if (rs != null && rs.next()) {
				reportStyle = rs.getInt("report_style".toUpperCase());
			}
		} catch (SQLException sqle) {
			throw new Exception(sqle.getMessage());
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		}

		return reportStyle;
	}

   
	/**
	 * ��ȡ�����[���ڱ��У��/���ڱ��ļ���]��ϵ���ʽ�б�
	 * 
	 * @param conn
	 *            Connection ��������
	 * @param childRepId
	 *            String �ӱ���ID
	 * @param versionId
	 *            String �汾��
	 * @return List ���ʽ�б�
	 * @exception Exception
	 */
	public static List getCellFormus(Connection conn, String childRepId,
			String versionId, int formuType) throws Exception {
		ArrayList resList = null;

		if (conn == null || childRepId == null || versionId == null)
			return resList;

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			String sql = "select mcf.cell_formu_id,mcf.cell_formu from m_cell_formu mcf "
					+ "where mcf.cell_formu_id in ("
					+ "select mctf.cell_formu_id from m_cell_to_formu mctf where "
					+ "mctf.child_rep_id=? and mctf.version_id=?) and formu_type=?";
			
			//�����ƶȣ����˵���Ӧ�ھ������⹫ʽ
			sql += " and cell_formu_id not in ("
			       +" select cell_formu_id from formu_excel where DATARANGE = "
			       + "(select data_range_id from view_m_report m "
			       + " where m.CHILD_REP_ID = ? and m.VERSION_ID = ? group by m.DATA_RANGE_ID)"
			       +" and to_char(sysdate,'yyyymm') >= begintime and to_char(sysdate,'yyyymm') <= endtime)";
			
			System.out.println("ReportImpl[910]-ChildRep:" + childRepId + "_" + versionId);
			System.out.println("ReportImpl[910]-formuType:" + formuType);
			System.out.println("ReportImpl[910]-getCellFormus:" + sql);
			
			pstmt = conn.prepareStatement(sql.toUpperCase());
			pstmt.setString(1, childRepId);
			pstmt.setString(2, versionId);
			pstmt.setInt(3, formuType);
			
			//�����ƶȣ����˵���Ӧ�ھ������⹫ʽ
			pstmt.setString(4, childRepId);
			pstmt.setString(5, versionId);

			rs = pstmt.executeQuery();
			if (rs != null) {
				resList = new ArrayList();
				java.util.Map map = ReportImpl.getCellFormuDefine(conn);
				String mKey = "";
				while (rs.next()) {
					MCellFormu mCellFormu = new MCellFormu();
					mCellFormu.setCellFormuId(new Integer(rs
							.getInt("cell_formu_id".toUpperCase())));
					mCellFormu.setCellFormu(rs.getString("cell_formu".toUpperCase()));
					mKey = "" + mCellFormu.getCellFormuId() + childRepId
							+ versionId;
					if (map.containsKey(mKey)) {
						mCellFormu.setPointNumber((Integer) map.get(mKey));
					} else {
						mCellFormu.setPointNumber(new Integer(0));
					}
					resList.add(mCellFormu);
				}
			}
		}       
        catch (SQLException sqle) {
			throw new Exception(sqle.getMessage());
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		}
		return resList;
	}
    /**
     * ʹ��jdbc ���ܲ���Ҫ�޸�  ���Ը� 2011-12-21
     * ��ȡ�����[���ڱ��У��/���ڱ��ļ���]��ϵ���ʽ�б�
     * @author gongming
     * @date   2007-09-27
     * @param conn              Connection          ��������
     * @param reportIn          ReportIn            ��������
     * @return  List ���ʽ����
     * @throws Exception
     */
    public static List getCellFormus(Connection orclCon, ReportIn reportIn,int formuType) throws Exception{
        ArrayList resList = null;
        PreparedStatement pStmt = null;
        ResultSet plRS = null;
        try {
            if (reportIn.getChildRepId() != null
                    && reportIn.getVersionId() != null) {
                //��ѯĳһ�ű�������е�Ԫ����ʽ
                String plSql = "select a.cell_formu_id,a.cell_formu,c.cell_len from m_cell_formu a ";
                       plSql += " left join m_cell_to_formu b on a.cell_formu_id = b.cell_formu_id ";
                       plSql += " left join cell_formu_define c on a.cell_formu_id = c.cell_formu_id ";
                       plSql += " where b.child_rep_id = ? and b.version_id = ? and a.formu_type= ";
                       plSql += formuType;
// // System.out.println(plSql +"="+reportIn.getChildRepId()+"-"+reportIn.getVersionId());
                pStmt = orclCon.prepareStatement(plSql.toUpperCase());
//                System.out.println(reportIn.getChildRepId()+"-"+reportIn.getVersionId());
                pStmt.setString(1, reportIn.getChildRepId());
                pStmt.setString(2, reportIn.getVersionId());

                plRS = pStmt.executeQuery();

                resList = new ArrayList();
                while (plRS.next()) {
                    MCellFormu mCellFormu = new MCellFormu();
                    mCellFormu.setCellFormuId(new Integer(plRS.getInt("cell_formu_id".toUpperCase())));
                    mCellFormu.setCellFormu(plRS.getString("cell_formu".toUpperCase()));
                    mCellFormu.setPointNumber(new Integer(plRS.getInt("cell_len".toUpperCase())));
                    mCellFormu.setCellType(new Integer(formuType));
                    resList.add(mCellFormu);
                }
            } 
        } 
        catch (SQLException sqle) {
            throw new Exception(sqle.getMessage());
        } finally {
            if (plRS != null)   plRS.close();
            if (pStmt != null)  pStmt.close();
        }
        return resList;
    }
	/**
	 * ��õ�Ԫ��ID
	 * 
	 * @param conn
	 *            Connection ��������
	 * @param childRepId
	 *            String �ӱ���ID
	 * @param versionId
	 *            String �汾��
	 * @param cellName
	 *            String ��Ԫ����
	 * @return int
	 */
	public static int getCellId(Connection conn, String childRepId,
			String versionId, String cellName) throws Exception {
		int cellId = 0;

		if (conn == null || childRepId == null || versionId == null
				|| cellName == null)
			return cellId;

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			String sql = "select cell_id from m_cell where child_rep_id=? and version_id=? and cell_name=?";

			pstmt = conn.prepareStatement(sql.toUpperCase());
			pstmt.setString(1, childRepId);
			pstmt.setString(2, versionId);
			pstmt.setString(3, cellName);

			rs = pstmt.executeQuery();
			if (rs != null && rs.next()) {
				cellId = rs.getInt("cell_id".toUpperCase());
			}
		} catch (SQLException sqle) {
			throw new Exception(sqle.getMessage());
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		}

		return cellId;
	}

	/**
	 * ���µ�Ԫ���ֵ
	 * 
	 * @param conn
	 *            Connection ��������
	 * @param repInId
	 *            int ʵ�ʱ���ID
	 * @param cellId
	 *            int ��Ԫ��ID
	 * @param valueFormu
	 *            String ��Ԫ��ֵֵ
	 * @return boolean ���³ɹ�������true;���򣬷���false
	 */
	public static boolean updateCellValue(Connection conn, int repInId,
			int cellId, String valueFormu) throws Exception {
		boolean result = false;

		if (conn == null || repInId <= 0 || cellId <= 0)
			return result;

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			String sql = "select count(*) from report_in_info where rep_in_id=".toUpperCase()
					+ repInId + " and cell_id=".toUpperCase() + cellId;
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs != null && (rs.next() && rs.getInt(1) > 0)) {
				sql = "update report_in_info set report_value='".toUpperCase() + valueFormu
						+ "'" + " where rep_in_id=".toUpperCase() + repInId + " and cell_id=".toUpperCase()
						+ cellId;
			} else {
				sql = "insert into report_in_info(cell_id,rep_in_id,report_value) values(".toUpperCase()
						+ cellId + "," + repInId + ",'" + valueFormu + "')";
			}
			// // System.out.println(sql);
			pstmt = conn.prepareStatement(sql);
			if (pstmt.executeUpdate() > 0)
				result = true;
		} catch (SQLException sqle) {
			throw new Exception(sqle.getMessage());
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		}

		return result;
	}

	/**
	 * �����쳣�仯����Ϣ�б�
	 * 
	 * @param conn
	 *            Connection ���ݿ�����
	 * @param childRepId
	 *            String �ӱ���ID
	 * @param versionId
	 *            String �汾��
	 * @param orgId
	 *            String ����Id
	 * @param flag
	 *            int ��flag=1ʱ����ȡ��Ե�ʽ���쳣�仯����flag=2ʱ����ȡ�嵥ʽ���쳣�仯
	 * @return List �޼�¼������null
	 */
	public static List getAbnormityChangeList(Connection conn,
			String childRepId, String versionId, String orgId, int flag)
			throws Exception {
		List acList = null;

		if (childRepId == null || versionId == null || orgId == null)
			return null;

		Statement pstmt = null;
		ResultSet rs = null;

		try {
			String sql = "";
			if (flag == Report.REPORT_STYLE_DD) { // ��Ե�ʽ�쳣�仯
				sql = "select CELL_ID,PREV_RISE_STANDARD,PREV_FALL_STANDARD,SAME_RISE_STANDARD,SAME_FALL_STANDARD "
						+ "from ABNORMITY_CHANGE where CHILD_REP_ID='"+childRepId+"' and VERSION_ID='"+versionId+"'   and ORG_ID='"+orgId+"'";
			} else { // �嵥ʽ�쳣�仯
				sql = "select COL_NAME,PREV_RISE_STANDARD,PREV_FALL_STANDARD,SAME_RISE_STANDARD,SAME_FALL_STANDARD "
						+ "from ABNORMITY_CHANGE where CHILD_REP_ID=? and VERSION_ID=? and ORG_ID=?";
			}

			/*
			 * // System.out.println(sql); // System.out.println(childRepId + "\t" +
			 * versionId + "\t" + orgId);
			 */

			pstmt = conn.createStatement();
			rs=pstmt.executeQuery(sql);
			if (rs != null) {
				acList = new ArrayList();
				while (rs.next()) {
					AbnormityChange ac = new AbnormityChange();
					if (flag == Report.REPORT_STYLE_DD) { // ��Ե�ʽ�쳣�仯
						ac.setCellId(rs.getInt("cell_id"));
					} else {
						ac.setColName(rs.getString("col_name"));
					}
					ac.setPrevFallStandard(rs.getFloat("PREV_RISE_STANDARD"));
					ac.setPrevRiseStandard(rs.getFloat("PREV_FALL_STANDARD"));
					ac.setSameFallStandard(rs.getFloat("SAME_RISE_STANDARD"));
					ac.setSameRiseStandard(rs.getFloat("SAME_FALL_STANDARD"));
					acList.add(ac);
				}
			}
		} catch (SQLException sqle) {
			throw new Exception(sqle.getMessage());
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		}

		return acList;
	}

	/**
	 * �����������
	 * 
	 * @param date
	 *            String YYYY-MM
	 * @param repFreqNm
	 *            String ����Ƶ��
	 * @return String YYYY-MM
	 */
	public static String getPrevTerm(String date, String repFreqNm) {
		if (date == null || repFreqNm == null)
			return null;

		String prevTerm = null;

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M");

		try {
			Date _date = dateFormat.parse(date);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(_date);
			int amout = -1;
			repFreqNm = repFreqNm.trim();
			if (repFreqNm.equals("��")) {
				amout = -3;
			} else if (repFreqNm.equals("����")) {
				amout = -6;
			} else if (repFreqNm.equals("��")) {
				amout = -12;
			}
			calendar.add(Calendar.MONTH, amout);
			_date = calendar.getTime();
			prevTerm = dateFormat.format(_date);
		} catch (ParseException pe) {
			prevTerm = null;
		}

		return prevTerm;
	}
	/**
	 * ����϶�������
	 * 
	 * @param date
	 *            String YYYY-MM
	 * @param repFreqNm
	 *            String ����Ƶ��
	 * @return String YYYY-MM
	 */
	public static String getPrevTwoTerm(String date, String repFreqNm) {
		if (date == null || repFreqNm == null)
			return null;

		String prevTerm = null;

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M");

		try {
			Date _date = dateFormat.parse(date);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(_date);
			int amout = -1;
			repFreqNm = repFreqNm.trim();
			if (repFreqNm.equals("��")) {
				amout = -6;
			} else if (repFreqNm.equals("����")) {
				amout = -12;
			} else if (repFreqNm.equals("��")) {
				amout = -24;
			}
			calendar.add(Calendar.MONTH, amout);
			_date = calendar.getTime();
			prevTerm = dateFormat.format(_date);
		} catch (ParseException pe) {
			prevTerm = null;
		}

		return prevTerm;
	}

	
	/**ʹ��jdbc ���ܲ���Ҫ�޸�  ���Ը� 2011-12-21**/
	/**
	 * ����ʵ�����ݱ��״̬��Ϣ
	 * 
	 * @param conn
	 *            Connection
	 * @param repInId
	 *            int
	 * @param flagName
	 *            String ״̬��ʶ��
	 * @param flagValue
	 *            int ״ֵ̬
	 * @return boolean ���²����ɹ�������true;���򣬷���false
	 * @exception Exception
	 */
	public static boolean updateFlag(Connection conn, int repInId,
			String flagName, int flagValue) throws Exception {
		boolean result = false;

		if (conn == null)
			return false;
        Statement pstmt = null;  
        //2007-09-29 ����
		//PreparedStatement pstmt = null;

		try {
           
			String sql = "update REPORT_IN set " + flagName.toUpperCase()+ "=" + flagValue
					+ " where REP_IN_ID=" + repInId;
			pstmt = conn.createStatement();
			//if (pstmt.executeUpdate() > 0)
            if(pstmt.executeUpdate(sql) > 0)
				result = true;
           // conn.commit();
		} catch (SQLException sqle) {
			result = false;
			throw new Exception(sqle.getMessage());
		} finally {
			if (pstmt != null)
				pstmt.close();
        }
		return result;
	}

	/**
	 * �õ����й�ʽ�ľ���(����������)
	 */
	public static java.util.Map getCellFormuDefine(Connection conn) {
		ResultSet rs = null;
		java.util.Map map = null;

		try {
			String sql = "select cell_formu_id,child_rep_id,version_id,cell_len from cell_formu_define";
			Statement st = conn.createStatement();
			rs = st.executeQuery(sql.toUpperCase());
			if (rs != null) {
				map = new java.util.Hashtable();
				String childRepID, versionId, cellFormuID;
				Integer cellLen;
				String mKey;
				while (rs.next()) {
					cellFormuID = rs.getString("cell_formu_id").trim();
					childRepID = rs.getString("child_rep_id").trim();
					versionId = rs.getString("version_id").trim();
					cellLen = new Integer(rs.getInt("cell_len"));
					mKey = cellFormuID + childRepID + versionId;
					map.put(mKey, cellLen);
				}
			}
		} catch (Exception e) {
			return null;
		}
		return map;
	}

	/**
	 * ����У�鲻ͨ���������ر���¼
	 * 
	 * @param repInId
	 *            Integer ʵ�ʱ���ID
	 * @return void
	 */
	public static void insertReportAgainSet(Connection conn, Integer repInId)
			throws Exception {
		if (conn == null || repInId == null)
			return;

		PreparedStatement pstmt = null;
		String sql = "";
		try {
			if(Config.DB_SERVER_TYPE.equals("sybase")){
				 sql = "insert into report_again_set(cause,set_date,rep_in_id) values("
					+ " '����У��δͨ��!',Sysdate,";
				 sql=sql.toUpperCase()+repInId + ")";
					
    		}
			if(Config.DB_SERVER_TYPE.equals("oracle")){
				sql = "insert into report_again_set(ras_id,cause,set_date,rep_in_id) values("
					+ "  seq_report_again_set.nextval,'����У��δͨ��!',Sysdate,"
					+ repInId + ")";
				
    		}
			if(Config.DB_SERVER_TYPE.equals("sqlserver")){
				sql = "insert into report_again_set(cause,set_date,rep_in_id) values("
					+ "'����У��δͨ��!',Sysdate,"
					+ repInId + ")";
				
    		}
    		if(Config.DB_SERVER_TYPE.equals("db2")){
    			sql = "insert into report_again_set(ras_id,cause,set_date,rep_in_id) values("
					+ "  nextval for seq_report_again_set,'����У��δͨ��!',Sysdate,"
					+ repInId + ")";
    		
    		}  
			// // System.out.println(sql);
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
		} catch (SQLException sqle) {
			throw new Exception(sqle.getMessage());
		} finally {
			if (pstmt != null)
				pstmt.close();
		}
	}

	/**
	 * ���ݻ���ID�����Ƶ�����ID
	 * 
	 * @param conn
	 *            Connection ���ݿ�����
	 * @param orgId
	 *            String ����ID
	 * @return int
	 */
	public static Integer getOATId(Connection conn, String orgId)
			throws Exception {
		Integer oatId = null;

		if (conn == null || orgId == null)
			return oatId;

		PreparedStatement pstmt = null;

		try {
			String sql = "select OAT_ID from ORG where ORG_ID='" + orgId + "'";

			pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			if (rs != null && rs.next()) {
				oatId = new Integer(rs.getString("OAT_ID"));
			}
		} catch (SQLException sqle) {
			oatId = null;
			throw new Exception(sqle.getMessage());
		} finally {
			if (pstmt != null)
				pstmt.close();
		}

		return oatId;
	}

	/**
	 * ��ñ����Ƶ��ID
	 * 
	 * @param conn
	 *            Connection ���ݿ�����
	 * @param reportIn
	 *            ReportIn
	 * @param childRepId
	 *            String �ӱ���Id
	 * @param oatId
	 *            Integer
	 * @return int
	 */
	public static int getRepFreqId(Connection conn, ReportIn reportIn,
			String childRepId, Integer oatId) throws Exception {
		int repFreqId = 0;

		if (conn == null || reportIn == null || childRepId == null)
			return repFreqId;

		PreparedStatement pstmt = null;

		try {
			String sql = "select rep_freq_id from m_actu_rep where ".toUpperCase()
					+ " child_rep_id='".toUpperCase() + childRepId + "' and "
					+ " version_id='".toUpperCase() + reportIn.getVersionId() + "' and "
					+ " data_range_id=".toUpperCase() + reportIn.getDataRangeId() + " and "
					+ " oat_id=".toUpperCase() + oatId;

			pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			if (rs != null && rs.next()) {
				repFreqId = rs.getInt("rep_freq_id".toUpperCase());
			}
		} catch (SQLException sqle) {
			repFreqId = 0;
			throw new Exception(sqle.getMessage());
		} finally {
			if (pstmt != null)
				pstmt.close();
		}

		return repFreqId;
	}

	/**
	 * ��ȡS44�����capAmount��Ԫ���ֵ
	 * 
	 * @param conn
	 *            Connection ���ݿ�����
	 * @param repInId
	 *            Integer ����ID
	 * @return String ��Ԫ���ֵ
	 */
	public static String getCapAmountValue(Connection conn, Integer repInId)
			throws Exception {
		String val = "";

		if (conn == null || repInId == null)
			return val;

		PreparedStatement pstmt = null;

		try {
			String sql = "select Col2 from S44 where Col1='capAmount' and rep_in_id=".toUpperCase()
					+ repInId;

			pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			if (rs != null && rs.next()) {
				val = rs.getString("Col2");
			}
		} catch (SQLException sqle) {
			val = "";
			throw new Exception(sqle.getMessage());
		} finally {
			if (pstmt != null)
				pstmt.close();
		}

		return val;
	}

	/**
	 * ���ݱ���ID��ȡ��ǰ�����Ƶ��
	 * 
	 * @param repInId
	 *            int ����ID
	 * @return String
	 */
	public static String getRepActuName(Connection conn, int repInId)
			throws Exception {
		String repActuNm = "";

		if (conn == null)
			return repActuNm;

		PreparedStatement pstmt = null;

		try {
			String sql = "select rep_freq_name from v_cur_report_in where rep_in_id=".toUpperCase()
					+ repInId;

			pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			if (rs != null && rs.next()) {
				repActuNm = rs.getString("rep_freq_name".toUpperCase());
			}
		} catch (SQLException sqle) {
			repActuNm = "";
			throw new Exception(sqle.getMessage());
		} finally {
			if (pstmt != null)
				pstmt.close();
		}

		return repActuNm;
	}

	/**
	 * �жϵ�Ԫ���Ƿ��ǰٷ���
	 * 
	 * @param conn
	 *            Connection ��������
	 * @param childRepId
	 *            String ������
	 * @param versionId
	 *            String �汾��
	 * @param cellName
	 *            String ��Ԫ������
	 * @return boolean �ǰٷ�����Ԫ�񣬷���true;���򣬷���false
	 */
	public static boolean isPercentCell(Connection conn, String childRepId,
			String versionId, String cellName) throws Exception {
		boolean res = false;
		if (conn == null || cellName == null || childRepId == null
				|| versionId == null)
			return res;

		PreparedStatement pstmt = null;

		try {
			String sql = "select count(child_rep_id) as count1 from cell_percent where ".toUpperCase()
					+ "child_rep_id='".toUpperCase()
					+ childRepId
					+ "' and "
					+ "version_id='".toUpperCase()
					+ versionId + "' and " + "cell_name='".toUpperCase() + cellName + "'";

			pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			if (rs != null && rs.next()) {
				if (rs.getInt("count1".toUpperCase()) > 0)
					res = true;
			}
		} catch (SQLException sqle) {
			res = false;
			throw new Exception(sqle.getMessage());
		} finally {
			if (pstmt != null)
				pstmt.close();
		}

		return res;
	}

	/**
	 * �������
	 * @return
	 */
	private String filterAllowDistinction(){
		
		String source = "";
		return source;
	}
	
	/**
	 * ���Է���
	 */
	public static void main(String[] args) {
		// // System.out.println(ReportImpl.getPrevTerm("2005-1"));
	}
}