package com.fitech.gznx.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.proc.impl.Report;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.gznx.dao.DaoModel;
import com.fitech.gznx.po.AfReport;
import com.fitech.gznx.po.OrgValiInfo;

/**
 * �ܷ�У�� ---2013-5-15
 * 
 * @author Administrator
 * 
 */
public class AfCollectDataValidateDelegate extends DaoModel {
	private static FitechException log = new FitechException(AFReportDelegate.class);
	private static FitechMessages messages = null;

	public static FitechMessages getMessages() {
		return messages;
	}

	public static void setMessages(FitechMessages messages) {
		AfCollectDataValidateDelegate.messages = messages;
	}

	/**
	 * @param conn
	 *            Connection ���ݿ�����
	 * @param repInId
	 *            int ʵ�����ݱ���ID
	 * @param ReportIn
	 * @return Exception
	 */
	public static AfReport getReportIn(Long repInId) throws Exception {

		AfReport reportIn = null;

		DBConn conn = null;

		try {
			conn = new DBConn();
			Session session = conn.openSession();

			String hql = "from AfReport afr where afr.repId=" + repInId;

			List list = session.find(hql.toString());

			if (list != null && list.size() > 0) {
				reportIn = (AfReport) list.get(0);
				reportIn.setTemplateStyle(Report.REPORT_STYLE_DD);
			}

		} catch (Exception e) {
			reportIn = null;
			throw new Exception(e.getMessage());
		} finally {
			if (conn != null)
				conn.closeSession();
		}

		return reportIn;
	}

	// ��ѯ�����ֵ

	public static List<Map<String, String>> parseCell(Connection orclCon, AfReport reportIn, Integer templateType) throws Exception {

		PreparedStatement pStmt = null;
		ResultSet plRS = null;
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		try {
			String orclSql = null;

			// �ж�ģ������
			orclSql = "select a.org_id,o.org_name,c.cell_pid,c.cell_name,c.row_name||'--'||c.col_name  name,b.cell_data ";
			orclSql += "from af_report a, af_pbocreportdata b, af_cellinfo c, af_org o ";
			orclSql += "where a.rep_id = b.rep_id and b.cell_id = c.cell_id(+)  and a.org_id = o.org_id ";
			orclSql += "and a.rep_id = ? order by to_number(c.row_num),c.col_num";

			pStmt = orclCon.prepareStatement(orclSql.toUpperCase());
			pStmt.setLong(1, reportIn.getRepId());
			plRS = pStmt.executeQuery();

			while (plRS.next()) {
				Map<String, String> cellMap = new HashMap<String, String>();
				cellMap.put("org_id", plRS.getString("org_id".toUpperCase()));
				cellMap.put("org_name", plRS.getString("org_name".toUpperCase()));
				cellMap.put("cell_pid", plRS.getString("cell_pid".toUpperCase()));
				cellMap.put("cell_name", plRS.getString("cell_name".toUpperCase()));
				cellMap.put("name", plRS.getString("name".toUpperCase()));
				cellMap.put("cell_data", plRS.getString("cell_data".toUpperCase()));
				list.add(cellMap);
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			if (plRS != null)
				plRS.close();
			if (pStmt != null)
				pStmt.close();
		}
		return list;
	}

	/**
	 * ��ѯ�ӻ��������req_id��ֵ ʹ��jdbc����Դ
	 * 
	 * 
	 */
	public static Map getSonReq(Connection orclCon, AfReport reportIn, Integer templateType) throws Exception {
		if (orclCon == null || reportIn == null || templateType == null)
			return null;
		PreparedStatement pStmt = null;
		ResultSet plRS = null;
		Map<String, Long> org_rep = new HashMap<String, Long>();

		try {
			String sql = "select  a.org_id , b.rep_id from (select org_id from af_vali_relation t ";
			sql += " where t.vali_id = (select t.org_id from af_report t where t.rep_id = ?)) a,";
			sql += " af_report b where b.times=1 and a.org_id = b.org_id" + " and b.year = ? and b.term = ? and b.day = ? and b.template_id=?";

			pStmt = orclCon.prepareStatement(sql.toUpperCase());
			pStmt.setLong(1, reportIn.getRepId());
			pStmt.setLong(2, reportIn.getYear());
			pStmt.setLong(3, reportIn.getTerm());
			pStmt.setLong(4, reportIn.getDay());
			pStmt.setString(5, reportIn.getTemplateId());
			plRS = pStmt.executeQuery();

			while (plRS.next()) {
				String org = plRS.getString(1);
				Long rep = plRS.getLong(2);
				// System.out.println(org);
				// System.out.println(rep);
				org_rep.put(org, rep);
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			if (plRS != null)
				plRS.close();
			if (pStmt != null)
				pStmt.close();
		}

		return org_rep;

	}

	/**
	 * �������У���� 2012-09-19
	 * 
	 * @author Nick
	 * @param CollectCustomForm
	 *            ����Ҫ����Ļ�����Ϣ
	 * @return boolean �Ƿ����ɹ�
	 * @throws SQLException
	 */
	public static boolean add(List<Map<String, String>> list, AfReport reportIn, String id) throws SQLException {

		boolean result = true;
		DBConn conn = null;
		Session session = null;

		try {
			conn = new DBConn();
			session = conn.beginTransaction();

			// ɾ����ʷ��¼
			if (list != null && list.size() > 0) {//�������У��ʧ����Ϣ������ɾ��ԭ�е�У�����󣬲�����Ϣ��У������Ϣ
				delete(session, id);
			}else{//���û��У��ʧ����Ϣ����ɾ��ԭ�е�У����������
				return delete(session, id);
			}

			//�������У��ʧ����Ϣ������ɾ��ԭ�е�У�����󣬲�����Ϣ��У������Ϣ
			OrgValiInfo cr = null;

			for (int i = 0; i < list.size(); i++) {
				cr = new OrgValiInfo();
				cr.setRep_id(Long.valueOf(id));
				cr.setTemplate_id(list.get(i).get("template_id"));
				cr.setCell_pid(list.get(i).get("cell_pid"));
				cr.setCell_name(list.get(i).get("cell_name"));
				cr.setOrg_id(list.get(i).get("org_id"));
				cr.setCell_data(list.get(i).get("cell_data"));
				cr.setOrg_name(list.get(i).get("org_name"));
				cr.setRol_name(list.get(i).get("name"));
				if (id.equals(list.get(i).get("rep_id"))) {
					cr.setFlag("1");
				} else {
					cr.setFlag("0");
				}
				// ����У���ϵ
				session.save(cr);
			}
			session.flush();
		} catch (Exception e) {
			log.printStackTrace(e);
			result = false;
		} finally {
			if (conn != null)
				conn.endTransaction(result);
		}
		return result;
	}

	/**
	 * ɾ��У����
	 * 
	 * @author Nick
	 * @param session
	 * @param orgId
	 *            String ɾ��У���ϵ��rep_id
	 * @return boolean �Ƿ�ɾ���ɹ�
	 * @throws SQLException
	 */
	public static boolean delete(Session session, String ids) throws SQLException {
		boolean result = true;
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			String sql = "delete from org_vali_info cr where cr.rep_id=?";

			conn = session.connection();
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(sql);

			String[] id = ids.split(",");
			for (int i = 0; i < id.length; i++) {
				pstmt.setLong(1, Long.valueOf(id[i]));
				pstmt.execute();
			}
			conn.commit();
		} catch (Exception e) {
			log.printStackTrace(e);
			result = false;
			conn.rollback();
		} finally {
			// �ر����ݿ���Դ
			if (pstmt != null)
				pstmt.close();
		}
		return result;
	}

	// ��ѯ�����ֵ

	public static List<Map<String, String>> getSonCell(Connection orclCon, AfReport reportIn, Integer templateType, String repids, String cell_name) throws Exception {

		PreparedStatement pStmt = null;
		ResultSet plRS = null;
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String[] ids = repids.split(",");
		for (int i = 0; i < ids.length; i++) {
			String id = ids[i];
			try {
				String orclSql = null;

				// �ж�ģ������
				orclSql = "select a.rep_id,a.org_id,o.org_name,c.cell_pid,c.cell_name,c.col_name ||'--'||c.row_name name,b.cell_data, a.template_id ";
				orclSql += "from af_report a, af_pbocreportdata b, af_cellinfo c, af_org o ";
				orclSql += "where a.rep_id = b.rep_id and b.cell_id = c.cell_id(+)  and a.org_id = o.org_id and a.rep_id = ?  and c.cell_name=? ";

				pStmt = orclCon.prepareStatement(orclSql.toUpperCase());
				pStmt.setString(1, id);
				pStmt.setString(2, cell_name);
				plRS = pStmt.executeQuery();
				while (plRS.next()) {
					Map<String, String> cellMap = new HashMap<String, String>();
					cellMap.put("org_id", plRS.getString("org_id".toUpperCase()));
					cellMap.put("org_name", plRS.getString("org_name".toUpperCase()));
					cellMap.put("cell_pid", plRS.getString("cell_pid".toUpperCase()));
					cellMap.put("cell_name", plRS.getString("cell_name".toUpperCase()));
					cellMap.put("name", plRS.getString("name".toUpperCase()));
					cellMap.put("cell_data", plRS.getString("cell_data".toUpperCase()));
					cellMap.put("template_id", plRS.getString("template_id".toUpperCase()));
					cellMap.put("rep_id", plRS.getString("rep_id".toUpperCase()));
					list.add(cellMap);
				}
			} catch (SQLException e) {
				throw new Exception(e.getMessage());
			} finally {
				if (plRS != null)
					plRS.close();
				if (pStmt != null)
					pStmt.close();
			}
		}
		return list;
	}

	/**
	 * �����ܷ�У���־λ
	 */
	public static boolean updateReport(Connection orclCon, String repId, boolean zfFlag) throws Exception {
		boolean result = true;
		PreparedStatement pStmt = null;

		try {
			orclCon.setAutoCommit(false);

			// ��ʼ���������
			String sql = "update af_report t set t.abmormity_change_flag=? where t.rep_id=?";
			pStmt = orclCon.prepareStatement(sql);
			// ��֯���²���
			pStmt.setLong(1, zfFlag == true ? new Long(1) : new Long(-1));// 1ΪУ��ͨ����-1ΪУ��δͨ��,NULLΪδУ��
			pStmt.setLong(2, Long.valueOf(repId));
			// ִ�и��²���
			result = pStmt.execute();
			orclCon.commit();
		} catch (Exception e) {
			result = false;
			orclCon.rollback();
		} finally {
			// �ر����ݿ���Դ
			if (pStmt != null)
				pStmt.close();
		}
		return result;
	}
}
