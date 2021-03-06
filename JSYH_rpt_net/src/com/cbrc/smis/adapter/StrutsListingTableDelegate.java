package com.cbrc.smis.adapter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.common.ConfigOncb;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.hibernate.ListingTable;
import com.cbrc.smis.util.FitechLog;

/**
 * This is a delegate class to handle interaction with the backend persistence
 * layer of hibernate. It has a set of methods to handle persistence for
 * ListingTable data (i.e. com.cbrc.smis.form.ListingTableForm objects).
 * 
 * @author <strong>Generated by Middlegen. </strong>
 */
public class StrutsListingTableDelegate {

	/**
	 * Create a new com.cbrc.smis.form.ListingTableForm object and persist (i.e.
	 * insert) it.
	 * 
	 * @param listingTableForm
	 *            The object containing the data for the new
	 *            com.cbrc.smis.form.ListingTableForm object
	 * @exception Exception
	 *                If the new com.cbrc.smis.form.ListingTableForm object
	 *                cannot be created or persisted.
	 */
	public static com.cbrc.smis.form.ListingTableForm create(
			com.cbrc.smis.form.ListingTableForm listingTableForm)
			throws Exception {
		com.cbrc.smis.hibernate.ListingTable listingTablePersistence = new com.cbrc.smis.hibernate.ListingTable();
		TranslatorUtil.copyVoToPersistence(listingTablePersistence,
				listingTableForm);
		javax.naming.InitialContext ctx = new javax.naming.InitialContext();
		// TODO: Make adapter get SessionFactory jndi name by ant task argument?
		net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory) ctx
				.lookup("java:AirlineHibernateFactory");
		net.sf.hibernate.Session session = factory.openSession();
		net.sf.hibernate.Transaction tx = session.beginTransaction();
		// TODO error?: listingTablePersistence =
		// (com.cbrc.smis.hibernate.ListingTable)session.save(listingTablePersistence);
		session.save(listingTablePersistence);
		tx.commit();
		session.close();
		TranslatorUtil.copyPersistenceToVo(listingTablePersistence,
				listingTableForm);
		return listingTableForm;
	}

	/**
	 * Update (i.e. persist) an existing com.cbrc.smis.form.ListingTableForm
	 * object.
	 * 
	 * @param listingTableForm
	 *            The com.cbrc.smis.form.ListingTableForm object containing the
	 *            data to be updated
	 * @exception Exception
	 *                If the com.cbrc.smis.form.ListingTableForm object cannot
	 *                be updated/persisted.
	 */
	public static com.cbrc.smis.form.ListingTableForm update(
			com.cbrc.smis.form.ListingTableForm listingTableForm)
			throws Exception {
		com.cbrc.smis.hibernate.ListingTable listingTablePersistence = new com.cbrc.smis.hibernate.ListingTable();
		TranslatorUtil.copyVoToPersistence(listingTablePersistence,
				listingTableForm);
		javax.naming.InitialContext ctx = new javax.naming.InitialContext();
		// TODO: Make adapter get SessionFactory jndi name by ant task argument?
		net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory) ctx
				.lookup("java:AirlineHibernateFactory");
		net.sf.hibernate.Session session = factory.openSession();
		net.sf.hibernate.Transaction tx = session.beginTransaction();
		session.update(listingTablePersistence);
		tx.commit();
		session.close();
		TranslatorUtil.copyPersistenceToVo(listingTablePersistence,
				listingTableForm);
		return listingTableForm;
	}

	/**
	 * Retrieve an existing com.cbrc.smis.form.ListingTableForm object for
	 * editing.
	 * 
	 * @param listingTableForm
	 *            The com.cbrc.smis.form.ListingTableForm object containing the
	 *            data used to retrieve the object (i.e. the primary key info).
	 * @exception Exception
	 *                If the com.cbrc.smis.form.ListingTableForm object cannot
	 *                be retrieved.
	 */
	public static com.cbrc.smis.form.ListingTableForm edit(
			com.cbrc.smis.form.ListingTableForm listingTableForm)
			throws Exception {
		com.cbrc.smis.hibernate.ListingTable listingTablePersistence = new com.cbrc.smis.hibernate.ListingTable();
		TranslatorUtil.copyVoToPersistence(listingTablePersistence,
				listingTableForm);
		javax.naming.InitialContext ctx = new javax.naming.InitialContext();
		// TODO: Make adapter get SessionFactory jndi name by ant task argument?
		net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory) ctx
				.lookup("java:AirlineHibernateFactory");
		net.sf.hibernate.Session session = factory.openSession();
		net.sf.hibernate.Transaction tx = session.beginTransaction();
		tx.commit();
		session.close();
		TranslatorUtil.copyPersistenceToVo(listingTablePersistence,
				listingTableForm);
		return listingTableForm;
	}

	/**
	 * Remove (delete) an existing com.cbrc.smis.form.ListingTableForm object.
	 * 
	 * @param listingTableForm
	 *            The com.cbrc.smis.form.ListingTableForm object containing the
	 *            data to be deleted.
	 * @exception Exception
	 *                If the com.cbrc.smis.form.ListingTableForm object cannot
	 *                be removed.
	 */
	public static void remove(
			com.cbrc.smis.form.ListingTableForm listingTableForm)
			throws Exception {
		com.cbrc.smis.hibernate.ListingTable listingTablePersistence = new com.cbrc.smis.hibernate.ListingTable();
		TranslatorUtil.copyVoToPersistence(listingTablePersistence,
				listingTableForm);
		javax.naming.InitialContext ctx = new javax.naming.InitialContext();
		// TODO: Make adapter get SessionFactory jndi name by ant task argument?
		net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory) ctx
				.lookup("java:AirlineHibernateFactory");
		net.sf.hibernate.Session session = factory.openSession();
		net.sf.hibernate.Transaction tx = session.beginTransaction();
		// TODO: is this really needed?
		session.delete(listingTablePersistence);
		tx.commit();
		session.close();
	}

	/**
	 * Retrieve all existing com.cbrc.smis.form.ListingTableForm objects.
	 * 
	 * @exception Exception
	 *                If the com.cbrc.smis.form.ListingTableForm objects cannot
	 *                be retrieved.
	 */
	public static List findAll() throws Exception {
		List retVals = new ArrayList();
		javax.naming.InitialContext ctx = new javax.naming.InitialContext();
		// TODO: Make adapter get SessionFactory jndi name by ant task argument?
		net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory) ctx
				.lookup("java:AirlineHibernateFactory");
		net.sf.hibernate.Session session = factory.openSession();
		net.sf.hibernate.Transaction tx = session.beginTransaction();
		retVals.addAll(session
				.find("from com.cbrc.smis.hibernate.ListingTable"));
		tx.commit();
		session.close();
		ArrayList listingTable_vos = new ArrayList();
		for (Iterator it = retVals.iterator(); it.hasNext();) {
			com.cbrc.smis.form.ListingTableForm listingTableFormTemp = new com.cbrc.smis.form.ListingTableForm();
			com.cbrc.smis.hibernate.ListingTable listingTablePersistence = (com.cbrc.smis.hibernate.ListingTable) it
					.next();
			TranslatorUtil.copyPersistenceToVo(listingTablePersistence,
					listingTableFormTemp);
			listingTable_vos.add(listingTableFormTemp);
		}
		retVals = listingTable_vos;
		return retVals;
	}

	/**
	 * Retrieve a set of existing com.cbrc.smis.form.ListingTableForm objects
	 * for editing.
	 * 
	 * @param listingTableForm
	 *            The com.cbrc.smis.form.ListingTableForm object containing the
	 *            data used to retrieve the objects (i.e. the criteria for the
	 *            retrieval).
	 * @exception Exception
	 *                If the com.cbrc.smis.form.ListingTableForm objects cannot
	 *                be retrieved.
	 */
	public static List select(
			com.cbrc.smis.form.ListingTableForm listingTableForm)
			throws Exception {
		List retVals = new ArrayList();
		javax.naming.InitialContext ctx = new javax.naming.InitialContext();
		// TODO: Make adapter get SessionFactory jndi name by ant task argument?
		net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory) ctx
				.lookup("java:AirlineHibernateFactory");
		net.sf.hibernate.Session session = factory.openSession();
		net.sf.hibernate.Transaction tx = session.beginTransaction();
		retVals
				.addAll(session
						.find("from com.cbrc.smis.hibernate.ListingTable as obj1 where obj1.createTime='"
								+ listingTableForm.getCreateTime() + "'"));
		tx.commit();
		session.close();
		ArrayList listingTable_vos = new ArrayList();
		for (Iterator it = retVals.iterator(); it.hasNext();) {
			com.cbrc.smis.form.ListingTableForm listingTableFormTemp = new com.cbrc.smis.form.ListingTableForm();
			com.cbrc.smis.hibernate.ListingTable listingTablePersistence = (com.cbrc.smis.hibernate.ListingTable) it
					.next();
			TranslatorUtil.copyPersistenceToVo(listingTablePersistence,
					listingTableFormTemp);
			listingTable_vos.add(listingTableFormTemp);
		}
		retVals = listingTable_vos;
		return retVals;
	}

	/**
	 * This method will return all objects referenced by MChildReport
	 */
	public static List getMChildReport(
			com.cbrc.smis.form.ListingTableForm listingTableForm)
			throws Exception {
		List retVals = new ArrayList();
		com.cbrc.smis.hibernate.ListingTable listingTablePersistence = null;
		javax.naming.InitialContext ctx = new javax.naming.InitialContext();
		// TODO: Make adapter get SessionFactory jndi name by ant task argument?
		net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory) ctx
				.lookup("java:AirlineHibernateFactory");
		net.sf.hibernate.Session session = factory.openSession();
		net.sf.hibernate.Transaction tx = session.beginTransaction();
		tx.commit();
		session.close();
		retVals.add(listingTablePersistence.getMChildReport());
		ArrayList CHILD_REP_ID_vos = new ArrayList();
		for (Iterator it = retVals.iterator(); it.hasNext();) {
			com.cbrc.smis.form.MChildReportForm CHILD_REP_ID_Temp = new com.cbrc.smis.form.MChildReportForm();
			com.cbrc.smis.hibernate.MChildReport CHILD_REP_ID_PO = (com.cbrc.smis.hibernate.MChildReport) it
					.next();
			TranslatorUtil.copyPersistenceToVo(CHILD_REP_ID_PO,
					CHILD_REP_ID_Temp);
			CHILD_REP_ID_vos.add(CHILD_REP_ID_Temp);
		}
		retVals = CHILD_REP_ID_vos;
		return retVals;
	}

	/**
	 * 该方法根据子报表名和版本号查出清单式报表的表名
	 * 
	 * @param childRepId
	 *            子报表ID
	 * @param versionId
	 *            子报表版本号
	 * @return 如果没有查到则返回空字符串 否则返回表名
	 * @throws Exception
	 */
	public static String getTalbeName(String childRepId, String versionId)
			throws Exception {

		String tableName = "";

		DBConn dBConn = null;

		Session session = null;

		Query query = null;

		List l = null;

		int size = 0;

		String hsql = "from ListingTable lt where lt.comp_id.childRepId=:childRepId and ";

		hsql = hsql + " lt.comp_id.versionId=:versionId ";

		try {

			dBConn = new DBConn();

			session = dBConn.openSession();

			query = session.createQuery(hsql);

			query.setString("childRepId", childRepId);

			query.setString("versionId", versionId);

			l = query.list();

			size = l.size();

			if (size != 0) {

				ListingTable lt = (ListingTable) l.get(0);

				tableName = lt.getComp_id().getTableName();

			}
		} catch (Exception e) {

		} finally {

			if (session != null)

				dBConn.closeSession();
		}

		return tableName;
	}

	/**
	 * @author cb
	 * 
	 * 根据输入的SQL字符串将清单式报表入库
	 * 
	 * @param sql
	 *            sql字串
	 * @param zipFileName
	 *            ZIP包文件名供日志记录使用
	 * @param xmlFileName
	 *            XML数据文件名日志记录使用
	 * @param repId
	 *            实际表单ID日志记录使用
	 * @throws Exception
	 */
	public static void insertRecord(String sql, String zipFileName,
			String xmlFileName, Integer repId) throws Exception {
		DBConn dBConn = null;

		Session session = null;

		Connection con = null;

		Statement stmt = null;

		try {

			dBConn = new DBConn();

			session = dBConn.beginTransaction();

			con = session.connection();

			stmt = con.createStatement();

			stmt.executeUpdate(sql);

			dBConn.endTransaction(true);

		} catch (Exception e) {

			dBConn.endTransaction(false);

			String errorMessage = "错误发生在存储" + zipFileName + "包文件中的";

			errorMessage = errorMessage + xmlFileName + "文件";

			errorMessage = errorMessage + ",其对应的实际的表单ID为" + repId.intValue();

			FitechLog.writeLog(Config.LOG_SYSTEM_SAVEDATA, ConfigOncb.HANDLER,errorMessage, sql);
			
			// System.out.println("错误的SQL字串是:" + sql);

		} finally {
			if (session != null) session.close();
		}

	}

	/**
	 * @author cb
	 * 
	 * 该方法用于将一组SQL语句在一个事务中执行用于清单式报表的入库
	 * 
	 * @param sqls
	 *            sql字串
	 * @param zipFileName
	 *            ZIP包文件名
	 * @param xmlFileName
	 *            XML数据文件名
	 * @param repId
	 *            子报表ID
	 * @throws Exception
	 */
	public static void insertRecords(List sqls, Integer repId,
			String zipFileName, String xmlFileName) throws Exception {

		if (sqls == null)

			return;

		DBConn dBConn = null;

		Session session = null;

		Connection con = null;

		PreparedStatement stmt = null;

		int size = sqls.size();

		try {

			dBConn = new DBConn();

			session = dBConn.beginTransaction();

			con = session.connection();

			con.setAutoCommit(false);

			for (int i = 0; i < size; i++) {

				String sql = (String) sqls.get(i);

				stmt = con.prepareStatement(sql); // 在这里将会执行多个SQL语句,这多个SQL语句将在同一个事务中

				stmt.executeUpdate();

			}
			con.commit();

		} catch (Exception e) {

			con.rollback();

			String errorMessage = "错误发生在存储" + zipFileName + "包文件中的";

			errorMessage = errorMessage + xmlFileName + "文件";

			errorMessage = errorMessage + ",其对应的实际的表单ID为" + repId.intValue();

			FitechLog.writeLog(Config.LOG_SYSTEM_SAVEDATA, ConfigOncb.HANDLER,
					errorMessage, "");

		} finally {

			if (stmt != null)
				stmt.close();

			if (con != null)
				con.close();

		}
	}
	
	/**将一组SQL语句在一个事务中执行用于清单式报表的入库
	 * @author wdw
	 * @param sqls
	 * @return
	 */
	public static void insertListRep(ArrayList sqls)
	{
		if (sqls == null) return;
		if(sqls.size()==0) return;
		DBConn dBConn = null;
		Session session = null;
		Connection con = null;
		PreparedStatement stmt = null;
		int size = sqls.size();
		// System.out.println("size is "+size);
		dBConn = new DBConn();
		session = dBConn.beginTransaction();
		try {
			con = session.connection();
			con.setAutoCommit(false);
			for (int i = 0; i < size; i++) {
				String sql = (String) sqls.get(i);
				// System.out.println(sql);
				stmt = con.prepareStatement(sql); // 在这里将会执行多个SQL语句,这多个SQL语句将在同一个事务中
//				stmt.executeUpdate();
				stmt.executeUpdate();
			}
			con.commit();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				if (stmt != null)
					stmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static boolean isCol1Exist(String sql){
		boolean bool = false;
		
		DBConn dBConn = null;
		Session session = null;
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {

			dBConn = new DBConn();
			session = dBConn.beginTransaction();
			con = session.connection();

			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);

			while(rs.next()){
				bool = true;
				break;
			}
			dBConn.endTransaction(true);

		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			bool = false;
			e.printStackTrace();
		} catch (SQLException e) {
			bool = false;
			e.printStackTrace();
		} finally {
			try {
				if(con!=null) con.close();
				if(stmt!=null) stmt.close();
				if(rs!=null) rs.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		return bool;
	}
}