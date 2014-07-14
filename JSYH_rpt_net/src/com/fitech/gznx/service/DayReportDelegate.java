package com.fitech.gznx.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.hibernate.Session;

import com.cbrc.smis.adapter.StrutsLogInDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.form.LogInForm;
import com.cbrc.smis.other.Aditing;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.fitech.gznx.form.AFReportForm;
import com.fitech.gznx.form.DayTaskForm;
import com.fitech.gznx.po.AfTemplate;
import com.fitech.gznx.util.DayDateUtil;

/**
 * �ձ���������
 * 
 * @author Nick
 * 
 */
public class DayReportDelegate {

	private static FitechException log = new FitechException(DayReportDelegate.class);
	// �ձ�������־����
	public static final Integer LOG_TYPE_50 = 50;
	// �ձ����������û�ID
	public static final String LOG_USER_NAME = "Day_Report";

	/**
	 * �ձ������ݴ���
	 */
	public static void doDayReport(String date) {
		DBConn dbConn = null;
		Session session = null;
		Connection conn = null;
		try {
			// ��ʼ�����ݿ���Դ
			dbConn = new DBConn();
			session = dbConn.openSession();
			conn = session.connection();
			/** 1.�õ���Ҫִ�е������б� */
			List taskList = getTaskList(conn, date);
			if (taskList == null || taskList.size() == 0) {
				return;
			}

			/** 2.����ִ�н��б������ݵ����� */
			int size = taskList.size();
			DayTaskForm form = null;
			boolean result = false;
			for (int i = 0; i < size; i++) {
				form = (DayTaskForm) taskList.get(i);
				if (!hasTaskRunning(conn)) {// ���û������ִ���е���������������ִ��
					try {
						/** 2.1.�޸Ŀ�ʼʱ�䣬��������������־λ */
						updateTaskFlag(conn, form, 1);// 1��ʾ����ִ��
					} catch (Exception e) {
						e.printStackTrace();
					}finally{
						close(conn, null, null);
						if (dbConn != null)
							dbConn.closeSession();
					}

					/**
					 * ��Ϊ�ձ��������ִ��ʱ��ϳ���Ϊ�˱������ݿ����ӵĳ�ʱ��ռ�ã������һ���ر������ݿ����ӣ���һ���������������ݿ�����
					 * �����ձ��������ִ��
					 */
					result = runTask(form);

					try {
						dbConn = new DBConn();
						session = dbConn.openSession();
						conn = session.connection();
						/** 2.2.�޸Ľ���ʱ�䣬��������������־λ */
						updateTaskFlag(conn, form, (result ? 2 : -1));// 2Ϊ�ɹ���-1Ϊʧ��
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(conn, null, null);
			if (dbConn != null)
				dbConn.closeSession();
		}
	}

	/**
	 * �����ձ��������ִ��
	 */
	public static boolean runTask(DayTaskForm form) throws Exception {
		StringBuffer oper = null;
		LogInForm log = null;
		// ִ��ǰ����־����
		oper = new StringBuffer();
		oper.append(form.getTaskDate()).append("����ʼ��������");
		log = getLogInForm(oper.toString());
		StrutsLogInDelegate.create(log);

		/** 1.��ʼ����صĲ�����Ϣ */
		// 1.1��ʼ���������Ͳ���
		AfTemplate template = AFTemplateDelegate.getTemplate(form.getTemplateId(), form.getVersionId());
		String templateType = template.getTemplateType();
		// 1.2��ʼ����ѯ������
		AFReportForm reportInForm = new AFReportForm();
		reportInForm.setOrgId(Config.DEFAULT_VALUE);
		reportInForm.setDate(form.getTaskDate());
		reportInForm.setRepName(form.getTemplateName());
		// reportInForm.setTemplateType(templateType);
		reportInForm.setRepFreqId(form.getRepFreqId());
		// 1.3��ʼ����ѯ��������
		Operator operator = new Operator();
		operator.setOrgId(Config.DEFAULT_VALUE);
		operator.setSuperManager(true);// ��Ϊ��������ԱĬ�ϻ������д˱����Ȩ�ޣ�������������������ɵ������������趨Ϊtrue

		/** 2.��ѯ��������Ҫ���ɵı����б���Ϣ */
		String reportFlg = templateType;
		List reportList = null;
		if (reportFlg.equals(com.fitech.gznx.common.Config.CBRC_REPORT)) {// ����ᱨ��
			reportList = AFReportProductDelegate.selectYJHReportList(reportInForm, operator);
		} else {// ������ᱨ��
			reportList = AFReportProductDelegate.selectNOTYJHReportList(reportInForm, operator, reportFlg);
		}

		/** 3.���ű���������� */
		if (reportList == null || reportList.size() == 0) {
			// ִ�к����־����
			oper = new StringBuffer();
			oper.append(form.getTaskDate()).append("���������ݽ���--");
			oper.append("[0�����ɳɹ�],").append("[0������ʧ��]");
			log = getLogInForm(oper.toString());
			StrutsLogInDelegate.create(log);

			return true;
		}
		boolean result = true;
		int success = 0;
		int size = reportList.size();
		for (int i = 0; i < size; i++) {
			Aditing aditing = (Aditing) reportList.get(i);

			// ִ��ǰ����־����
			oper = new StringBuffer();
			oper.append(form.getTaskDate()).append("�����-").append(aditing.getChildRepId()).append(",�汾��-").append(aditing.getVersionId());
			oper.append(",Ƶ��-").append(aditing.getActuFreqName()).append(",����-").append(aditing.getCurrName());
			oper.append(",����-").append(aditing.getOrgName()).append(",��ʼʱ��-").append(DayDateUtil.formatDateToYYYYMMDD24(new Date()));
			log = getLogInForm(oper.toString());
			StrutsLogInDelegate.create(log);

			boolean tempResult = DayRaqDataDelegate.dataToDB(aditing, form, reportFlg);

			// ִ��ǰ����־����
			oper = new StringBuffer();
			oper.append(form.getTaskDate()).append("�����-").append(aditing.getChildRepId()).append(",�汾��-").append(aditing.getVersionId());
			oper.append(",Ƶ��-").append(aditing.getActuFreqName()).append(",����-").append(aditing.getCurrName());
			oper.append(",����-").append(aditing.getOrgName()).append(",����ʱ��-").append(DayDateUtil.formatDateToYYYYMMDD24(new Date()));
			oper.append(tempResult ? "[���ɳɹ�]" : "[����ʧ��]");
			log = getLogInForm(oper.toString());
			StrutsLogInDelegate.create(log);

			if (tempResult) {// ���ִ�гɹ�����ִ�гɹ�����1������ִ�гɹ���ֵ����
				success++;
			}
		}

		// ִ�к����־����
		oper = new StringBuffer();
		oper.append(form.getTaskDate()).append("���������ݽ���--");
		oper.append("[").append(success).append("�����ɳɹ�],");
		oper.append("[").append(size - success).append("������ʧ��]");
		log = getLogInForm(oper.toString());
		StrutsLogInDelegate.create(log);

		return result;
	}

	/**
	 * �޸ı�������������־λ
	 * 
	 * @param conn
	 */
	public static void updateTaskFlag(Connection conn, DayTaskForm form, int flag) {
		// boolean result = false;

		PreparedStatement pstmt = null;

		try {
			conn.setAutoCommit(false);
			// �õ���ѯSQL
			StringBuffer sql = new StringBuffer();
			if (flag == 1) {
				sql.append("update DAY_TASK d set d.flag =?, d.start_date = sysdate, d.end_date = null");
			} else {
				sql.append("update DAY_TASK d set d.flag =?, d.end_date = sysdate ");
			}
			sql.append(" where d.task_date = to_date(?, 'yyyy-mm-dd')");

			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, flag);// �������ɱ�־λ��0Ϊδִ�У�1Ϊ����ִ�У�2Ϊִ�гɹ���-1Ϊִ��ʧ��
			pstmt.setString(2, form.getTaskDate());
			pstmt.execute();

			conn.commit();
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
			log.printStackTrace(e);
		} finally {
			close(null, pstmt, null);
		}
		// return result;
	}

	/**
	 * �õ���Ҫ����������־����
	 */
	public static LogInForm getLogInForm(String operation) {
		if (operation == null || operation.trim().length() == 0) {
			return null;
		}

		LogInForm logInForm = new LogInForm();
		logInForm.setUserName(LOG_USER_NAME);
		logInForm.setLogTime(new Date());
		logInForm.setLogTypeId(LOG_TYPE_50);
		logInForm.setOperation(operation);

		return logInForm;
	}

	/**
	 * �ж��Ƿ�����������ִ��
	 * 
	 * @param conn
	 * 
	 * @return boolean
	 */
	public static boolean hasTaskRunning(Connection conn) {
		boolean result = false;

		Statement stmt = null;
		ResultSet rs = null;

		try {
			// �õ���ѯSQL
			StringBuffer sql = new StringBuffer("select count(*) from DAY_TASK dk where flag=1 ");
			// ���ݿ��ѯ����
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql.toString());

			if (rs.next() && rs.getInt(1) > 0) {
				result = true;
			}

		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			close(null, stmt, rs);
		}
		return result;
	}

	/**
	 * ��ѯ������Ҫִ�е������б�
	 * 
	 * @param conn
	 * @param date
	 * 
	 * @return List
	 */
	public static List getTaskList(Connection conn, String date) {
		List result = null;

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			// �õ���ѯSQL
			StringBuffer sql = new StringBuffer();
			sql.append(" select dk.task_date,dk.template_id,dk.version_id,f.rep_freq_id,f.rep_freq_name,c.cur_id,c.cur_name,dk.flag,dk.start_date,dk.end_date,dk.template_name");
			sql.append(" from DAY_TASK dk left join m_rep_freq f on dk.rep_freq_id = f.rep_freq_id");
			sql.append(" left join m_curr c on dk.cur_id = c.cur_id ");
			if (date != null && date.length() == 10) {
				sql.append(" where task_date=to_date('").append(date).append("','yyyy-mm-dd')");
			} else {
				sql.append(" where flag=0");
			}
			sql.append(" order by task_date");

			// ���ݿ��ѯ����
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();

			result = new ArrayList();
			DayTaskForm form = null;
			while (rs.next()) {
				form = DBinfoToForm(rs);
				result.add(form);
			}
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			close(null, pstmt, rs);
		}
		return result;
	}

	/**
	 * ��ѯ�����������ձ����¼��
	 * 
	 * @param dayTaskForm
	 * @return int
	 */
	public static int getRecordCount(DayTaskForm dayTaskForm) {
		int recordCount = 0;

		DBConn dbConn = null;
		Session session = null;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			// ��ʼ�����ݿ���Դ
			dbConn = new DBConn();
			session = dbConn.openSession();
			conn = session.connection();
			// �õ���ѯSQL
			StringBuffer sql = new StringBuffer("select count(*) from DAY_TASK dk where 1=1 ");
			// ���ݴ������Ĳ���������������Ӧ�Ĳ�ѯ����
			String whereSQL = getWhereSQL(dayTaskForm);
			sql.append(whereSQL);
			// ���ݿ��ѯ����
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql.toString());

			if (rs.next()) {
				recordCount = rs.getInt(1);
			}

		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			close(conn, stmt, rs);
			if (dbConn != null)
				dbConn.closeSession();
		}
		return recordCount;
	}

	// ���ݴ������Ĳ���������������Ӧ�Ĳ�ѯ����
	private static String getWhereSQL(DayTaskForm dayTaskForm) {
		StringBuffer sql = null;

		if (dayTaskForm != null) {
			sql = new StringBuffer();
			/** ������ʼʱ�� */
			if (dayTaskForm.getQueryStartTaskDate() != null && !dayTaskForm.getQueryStartTaskDate().equals(""))
				sql.append(" and dk.task_date>=to_date('" + dayTaskForm.getQueryStartTaskDate() + "','yyyy-mm-dd')");
			/** ������ʼʱ�� */
			if (dayTaskForm.getQueryEndTaskDate() != null && !dayTaskForm.getQueryEndTaskDate().equals(""))
				sql.append(" and dk.task_date<=to_date('" + dayTaskForm.getQueryEndTaskDate() + "','yyyy-mm-dd')");
			/** ����ID */
			if (dayTaskForm.getQueryTemplateId() != null && !dayTaskForm.getQueryTemplateId().trim().equals(""))
				sql.append(" and dk.template_id like upper('%" + dayTaskForm.getQueryTemplateId().trim() + "%')");
			/** ����汾 */
			if (dayTaskForm.getQueryVersionId() != null && !dayTaskForm.getQueryVersionId().trim().equals(""))
				sql.append(" and dk.version_id like '%" + dayTaskForm.getQueryVersionId().trim() + "%'");
			/** �������� */
			if (dayTaskForm.getQueryTemplateName() != null && !dayTaskForm.getQueryTemplateName().trim().equals(""))
				sql.append(" and dk.template_name like '%" + dayTaskForm.getQueryTemplateName().trim() + "%'");
			/** �������ɱ�־λ��0Ϊδִ�У�1Ϊ����ִ�У�2Ϊִ�гɹ���-1Ϊִ��ʧ�� */
			if (dayTaskForm.getQueryFlag() != null && dayTaskForm.getQueryFlag().intValue() != new Integer(Config.DEFAULT_VALUE).intValue())
				sql.append(" and dk.flag =" + dayTaskForm.getQueryFlag());
		}

		return (sql == null) ? null : sql.toString();
	}

	/**
	 * ��ѯ�����������ձ����¼�����б�
	 * 
	 * @param dayTaskForm
	 * @return List
	 */
	public static List select(DayTaskForm dayTaskForm, int offset, int limit) {
		List retVals = null;

		DBConn dbConn = null;
		Session session = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			// ��ʼ�����ݿ���Դ
			dbConn = new DBConn();
			session = dbConn.openSession();
			conn = session.connection();
			// �õ���ѯSQL
			StringBuffer sql = new StringBuffer("select * from (select row_.*, rownum rownum_  from (");
			sql.append(" select dk.task_date,dk.template_id,dk.version_id,f.rep_freq_id,f.rep_freq_name,c.cur_id,c.cur_name,dk.flag,dk.start_date,dk.end_date,dk.template_name");
			sql.append(" from DAY_TASK dk left join m_rep_freq f on dk.rep_freq_id = f.rep_freq_id");
			sql.append(" left join m_curr c on dk.cur_id = c.cur_id where 1=1");
			// ���ݴ������Ĳ���������������Ӧ�Ĳ�ѯ����
			String whereSQL = getWhereSQL(dayTaskForm);
			sql.append(whereSQL);
			sql.append(" order by task_date desc");
			sql.append(") row_  where rownum <= ?) where rownum_ > ?");

			// ���ݿ��ѯ����
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, (offset + limit));// ������
			pstmt.setInt(2, offset);// ��ʼ��
			rs = pstmt.executeQuery();

			retVals = new ArrayList();
			DayTaskForm form = null;
			while (rs.next()) {
				form = DBinfoToForm(rs);
				retVals.add(form);
			}
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			close(conn, pstmt, rs);
			if (dbConn != null)
				dbConn.closeSession();
		}
		return retVals;
	}

	/**
	 * ��ȡ���ݿ��еļ�¼��Ϣ��ʵ����ActionForm
	 */
	private static DayTaskForm DBinfoToForm(ResultSet rs) throws SQLException, Exception {
		if (rs == null) {
			return null;
		}

		DayTaskForm form = new DayTaskForm();
		if (rs.getDate("task_date") != null) {
			form.setTaskDate(DayDateUtil.formatDateToYYYYMMDD(rs.getDate("task_date")));
		}
		form.setTemplateId(rs.getString("template_id"));
		form.setVersionId(rs.getString("version_id"));
		form.setRepFreqId(rs.getString("rep_freq_id"));
		form.setRepFreqName(rs.getString("rep_freq_name"));
		form.setCurId(rs.getString("cur_id"));
		form.setCurName(rs.getString("cur_name"));
		form.setFlag(rs.getInt("flag"));
		if (rs.getDate("start_date") != null) {
			form.setStartDate(DayDateUtil.formatDateToYYYYMMDD24(rs.getTimestamp("start_date")));
		}
		if (rs.getDate("end_date") != null) {
			form.setEndDate(DayDateUtil.formatDateToYYYYMMDD24(rs.getTimestamp("end_date")));
		}
		form.setTemplateName(rs.getString("template_name"));

		return form;
	}

	/**
	 * �ر����ݿ���Դ����
	 */
	public static void close(Connection conn, Statement stmt, ResultSet rs) {

		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			if (stmt != null) {
				stmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * �������ƶ�Ӧ�ļ�Ʋ�ѯ
	 */
	public static Map<String, String> getOrgShortNameMap() {
		DBConn dbConn = null;
		Session session = null;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		Map<String, String> orgMap = null;
		try {
			// ��ʼ�����ݿ���Դ
			dbConn = new DBConn();
			session = dbConn.openSession();
			conn = session.connection();

			// �õ���ѯSQL
			String sql = new String("select org_id,org_short_name from org");

			// ���ݿ��ѯ����
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);

			orgMap = new HashMap<String, String>();
			while (rs.next()) {
				orgMap.put(rs.getString("org_id"), rs.getString("org_short_name"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(conn, stmt, rs);
			if (dbConn != null)
				dbConn.closeSession();
		}

		return orgMap;
	}
}
