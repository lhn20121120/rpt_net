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
 * 日报表处理辅助类
 * 
 * @author Nick
 * 
 */
public class DayReportDelegate {

	private static FitechException log = new FitechException(DayReportDelegate.class);
	// 日报表处理日志类型
	public static final Integer LOG_TYPE_50 = 50;
	// 日报表处理特殊用户ID
	public static final String LOG_USER_NAME = "Day_Report";

	/**
	 * 日报表数据处理
	 */
	public static void doDayReport(String date) {
		DBConn dbConn = null;
		Session session = null;
		Connection conn = null;
		try {
			// 初始化数据库资源
			dbConn = new DBConn();
			session = dbConn.openSession();
			conn = session.connection();
			/** 1.得到需要执行的任务列表 */
			List taskList = getTaskList(conn, date);
			if (taskList == null || taskList.size() == 0) {
				return;
			}

			/** 2.迭代执行进行报表数据的生成 */
			int size = taskList.size();
			DayTaskForm form = null;
			boolean result = false;
			for (int i = 0; i < size; i++) {
				form = (DayTaskForm) taskList.get(i);
				if (!hasTaskRunning(conn)) {// 如果没有正在执行中的任务，则进行任务的执行
					try {
						/** 2.1.修改开始时间，本期任务跑批标志位 */
						updateTaskFlag(conn, form, 1);// 1表示正在执行
					} catch (Exception e) {
						e.printStackTrace();
					}finally{
						close(conn, null, null);
						if (dbConn != null)
							dbConn.closeSession();
					}

					/**
					 * 因为日报表任务的执行时间较长，为了避免数据库连接的长时间占用，因此上一步关闭了数据库连接，下一步重新申请了数据库连接
					 * 单个日报表任务的执行
					 */
					result = runTask(form);

					try {
						dbConn = new DBConn();
						session = dbConn.openSession();
						conn = session.connection();
						/** 2.2.修改结束时间，本期任务跑批标志位 */
						updateTaskFlag(conn, form, (result ? 2 : -1));// 2为成功，-1为失败
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
	 * 单个日报表任务的执行
	 */
	public static boolean runTask(DayTaskForm form) throws Exception {
		StringBuffer oper = null;
		LogInForm log = null;
		// 执行前的日志处理
		oper = new StringBuffer();
		oper.append(form.getTaskDate()).append("：开始生成数据");
		log = getLogInForm(oper.toString());
		StrutsLogInDelegate.create(log);

		/** 1.初始化相关的参数信息 */
		// 1.1初始化报表类型参数
		AfTemplate template = AFTemplateDelegate.getTemplate(form.getTemplateId(), form.getVersionId());
		String templateType = template.getTemplateType();
		// 1.2初始化查询主对像
		AFReportForm reportInForm = new AFReportForm();
		reportInForm.setOrgId(Config.DEFAULT_VALUE);
		reportInForm.setDate(form.getTaskDate());
		reportInForm.setRepName(form.getTemplateName());
		// reportInForm.setTemplateType(templateType);
		reportInForm.setRepFreqId(form.getRepFreqId());
		// 1.3初始化查询辅助对象
		Operator operator = new Operator();
		operator.setOrgId(Config.DEFAULT_VALUE);
		operator.setSuperManager(true);// 因为超级管理员默认会查出所有此报表的权限，因此正好满足任务生成的条件，所以设定为true

		/** 2.查询出所有需要生成的报表列表信息 */
		String reportFlg = templateType;
		List reportList = null;
		if (reportFlg.equals(com.fitech.gznx.common.Config.CBRC_REPORT)) {// 银监会报表
			reportList = AFReportProductDelegate.selectYJHReportList(reportInForm, operator);
		} else {// 非银监会报表
			reportList = AFReportProductDelegate.selectNOTYJHReportList(reportInForm, operator, reportFlg);
		}

		/** 3.单张报表数据入库 */
		if (reportList == null || reportList.size() == 0) {
			// 执行后的日志处理
			oper = new StringBuffer();
			oper.append(form.getTaskDate()).append("：生成数据结束--");
			oper.append("[0个生成成功],").append("[0个生成失败]");
			log = getLogInForm(oper.toString());
			StrutsLogInDelegate.create(log);

			return true;
		}
		boolean result = true;
		int success = 0;
		int size = reportList.size();
		for (int i = 0; i < size; i++) {
			Aditing aditing = (Aditing) reportList.get(i);

			// 执行前的日志处理
			oper = new StringBuffer();
			oper.append(form.getTaskDate()).append("：编号-").append(aditing.getChildRepId()).append(",版本号-").append(aditing.getVersionId());
			oper.append(",频度-").append(aditing.getActuFreqName()).append(",币种-").append(aditing.getCurrName());
			oper.append(",机构-").append(aditing.getOrgName()).append(",开始时间-").append(DayDateUtil.formatDateToYYYYMMDD24(new Date()));
			log = getLogInForm(oper.toString());
			StrutsLogInDelegate.create(log);

			boolean tempResult = DayRaqDataDelegate.dataToDB(aditing, form, reportFlg);

			// 执行前的日志处理
			oper = new StringBuffer();
			oper.append(form.getTaskDate()).append("：编号-").append(aditing.getChildRepId()).append(",版本号-").append(aditing.getVersionId());
			oper.append(",频度-").append(aditing.getActuFreqName()).append(",币种-").append(aditing.getCurrName());
			oper.append(",机构-").append(aditing.getOrgName()).append(",结束时间-").append(DayDateUtil.formatDateToYYYYMMDD24(new Date()));
			oper.append(tempResult ? "[生成成功]" : "[生成失败]");
			log = getLogInForm(oper.toString());
			StrutsLogInDelegate.create(log);

			if (tempResult) {// 如果执行成功，则执行成功数加1，否则执行成功数值不变
				success++;
			}
		}

		// 执行后的日志处理
		oper = new StringBuffer();
		oper.append(form.getTaskDate()).append("：生成数据结束--");
		oper.append("[").append(success).append("个生成成功],");
		oper.append("[").append(size - success).append("个生成失败]");
		log = getLogInForm(oper.toString());
		StrutsLogInDelegate.create(log);

		return result;
	}

	/**
	 * 修改本期任务跑批标志位
	 * 
	 * @param conn
	 */
	public static void updateTaskFlag(Connection conn, DayTaskForm form, int flag) {
		// boolean result = false;

		PreparedStatement pstmt = null;

		try {
			conn.setAutoCommit(false);
			// 得到查询SQL
			StringBuffer sql = new StringBuffer();
			if (flag == 1) {
				sql.append("update DAY_TASK d set d.flag =?, d.start_date = sysdate, d.end_date = null");
			} else {
				sql.append("update DAY_TASK d set d.flag =?, d.end_date = sysdate ");
			}
			sql.append(" where d.task_date = to_date(?, 'yyyy-mm-dd')");

			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, flag);// 报表生成标志位，0为未执行，1为正在执行，2为执行成功，-1为执行失败
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
	 * 得到需要保存入库的日志对象
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
	 * 判断是否有任务正在执行
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
			// 得到查询SQL
			StringBuffer sql = new StringBuffer("select count(*) from DAY_TASK dk where flag=1 ");
			// 数据库查询操作
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
	 * 查询所有需要执行的任务列表
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
			// 得到查询SQL
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

			// 数据库查询操作
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
	 * 查询符合条件的日报表记录数
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
			// 初始化数据库资源
			dbConn = new DBConn();
			session = dbConn.openSession();
			conn = session.connection();
			// 得到查询SQL
			StringBuffer sql = new StringBuffer("select count(*) from DAY_TASK dk where 1=1 ");
			// 依据传过来的参数条件，增加相应的查询条件
			String whereSQL = getWhereSQL(dayTaskForm);
			sql.append(whereSQL);
			// 数据库查询操作
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

	// 依据传过来的参数条件，增加相应的查询条件
	private static String getWhereSQL(DayTaskForm dayTaskForm) {
		StringBuffer sql = null;

		if (dayTaskForm != null) {
			sql = new StringBuffer();
			/** 期数开始时间 */
			if (dayTaskForm.getQueryStartTaskDate() != null && !dayTaskForm.getQueryStartTaskDate().equals(""))
				sql.append(" and dk.task_date>=to_date('" + dayTaskForm.getQueryStartTaskDate() + "','yyyy-mm-dd')");
			/** 期数开始时间 */
			if (dayTaskForm.getQueryEndTaskDate() != null && !dayTaskForm.getQueryEndTaskDate().equals(""))
				sql.append(" and dk.task_date<=to_date('" + dayTaskForm.getQueryEndTaskDate() + "','yyyy-mm-dd')");
			/** 报表ID */
			if (dayTaskForm.getQueryTemplateId() != null && !dayTaskForm.getQueryTemplateId().trim().equals(""))
				sql.append(" and dk.template_id like upper('%" + dayTaskForm.getQueryTemplateId().trim() + "%')");
			/** 报表版本 */
			if (dayTaskForm.getQueryVersionId() != null && !dayTaskForm.getQueryVersionId().trim().equals(""))
				sql.append(" and dk.version_id like '%" + dayTaskForm.getQueryVersionId().trim() + "%'");
			/** 报表名称 */
			if (dayTaskForm.getQueryTemplateName() != null && !dayTaskForm.getQueryTemplateName().trim().equals(""))
				sql.append(" and dk.template_name like '%" + dayTaskForm.getQueryTemplateName().trim() + "%'");
			/** 报表生成标志位，0为未执行，1为正在执行，2为执行成功，-1为执行失败 */
			if (dayTaskForm.getQueryFlag() != null && dayTaskForm.getQueryFlag().intValue() != new Integer(Config.DEFAULT_VALUE).intValue())
				sql.append(" and dk.flag =" + dayTaskForm.getQueryFlag());
		}

		return (sql == null) ? null : sql.toString();
	}

	/**
	 * 查询符合条件的日报表记录对象列表
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

			// 初始化数据库资源
			dbConn = new DBConn();
			session = dbConn.openSession();
			conn = session.connection();
			// 得到查询SQL
			StringBuffer sql = new StringBuffer("select * from (select row_.*, rownum rownum_  from (");
			sql.append(" select dk.task_date,dk.template_id,dk.version_id,f.rep_freq_id,f.rep_freq_name,c.cur_id,c.cur_name,dk.flag,dk.start_date,dk.end_date,dk.template_name");
			sql.append(" from DAY_TASK dk left join m_rep_freq f on dk.rep_freq_id = f.rep_freq_id");
			sql.append(" left join m_curr c on dk.cur_id = c.cur_id where 1=1");
			// 依据传过来的参数条件，增加相应的查询条件
			String whereSQL = getWhereSQL(dayTaskForm);
			sql.append(whereSQL);
			sql.append(" order by task_date desc");
			sql.append(") row_  where rownum <= ?) where rownum_ > ?");

			// 数据库查询操作
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, (offset + limit));// 结束行
			pstmt.setInt(2, offset);// 起始行
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
	 * 获取数据库中的记录信息并实例化ActionForm
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
	 * 关闭数据库资源方法
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
	 * 机构名称对应的简称查询
	 */
	public static Map<String, String> getOrgShortNameMap() {
		DBConn dbConn = null;
		Session session = null;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		Map<String, String> orgMap = null;
		try {
			// 初始化数据库资源
			dbConn = new DBConn();
			session = dbConn.openSession();
			conn = session.connection();

			// 得到查询SQL
			String sql = new String("select org_id,org_short_name from org");

			// 数据库查询操作
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
