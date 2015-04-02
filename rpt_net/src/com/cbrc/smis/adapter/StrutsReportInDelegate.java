package com.cbrc.smis.adapter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import com.cbrc.org.adapter.StrutsMOrgDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.common.ConfigOncb;
import com.cbrc.smis.common.DateUtil;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.form.MActuRepForm;
import com.cbrc.smis.form.ReportAgainSetForm;
import com.cbrc.smis.form.ReportInDataForm;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.form.ReportInInfoForm;
import com.cbrc.smis.hibernate.MActuRep;
import com.cbrc.smis.hibernate.MChildReport;
import com.cbrc.smis.hibernate.MChildReportPK;
import com.cbrc.smis.hibernate.MCurr;
import com.cbrc.smis.hibernate.MDataRgType;
import com.cbrc.smis.hibernate.MRepRange;
import com.cbrc.smis.hibernate.ReportAgainSet;
import com.cbrc.smis.hibernate.ReportIn;
import com.cbrc.smis.hibernate.ReportInData;
import com.cbrc.smis.hibernate.ReportInInfo;
import com.cbrc.smis.jdbc.FitechConnection;
import com.cbrc.smis.other.Aditing;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechLog;
import com.fitech.gznx.common.StringUtil;
import com.fitech.net.adapter.StrutsCollectTypeDelegate;
import com.fitech.net.adapter.StrutsOrgNetDelegate;
import com.fitech.net.form.ActuTargetResultForm;
import com.fitech.net.form.CollectTypeForm;
import com.fitech.net.hibernate.OrgNet;
import com.fitech.net.hibernate.ViewMReport;

/**
 * This is a delegate class to handle interaction with the backend persistence
 * layer of hibernate. It has a set of methods to handle persistence for
 * ReportIn data (i.e. com.cbrc.smis.form.ReportInForm objects).
 * 
 * @author唐磊
 */
public class StrutsReportInDelegate {
	private static FitechException log = new FitechException(
			StrutsReportInDelegate.class);

	public static boolean hidden(ReportInForm reportInForm) throws Exception {
		// 置标志result
		boolean result = false;
		// 连接和会话对象的初始化
		DBConn conn = null;
		Session session = null;

		if (reportInForm == null || reportInForm.getRepInId() == null)
			return result;

		try {
			conn = new DBConn();
			session = conn.beginTransaction();
			ReportIn reportIn = (ReportIn) session.load(ReportIn.class,
					reportInForm.getRepInId());
			if (reportIn != null) {
				Set reportInInfos = reportIn.getReportInInfos();
				for (Iterator iter = reportInInfos.iterator(); iter.hasNext();) {
					ReportInInfo info = (ReportInInfo) iter.next();
					session.delete(info);
				}

				ReportInDataForm dataForm = new ReportInDataForm();
				dataForm.setRepInId(reportInForm.getRepInId());
				ReportInData data = (ReportInData) session.load(
						ReportInData.class, dataForm.getRepInId());
				session.delete(data);
				// 会话对象删除持久层对象
				session.delete(reportIn);

			}
			session.flush();
			result = true;
		} catch (Exception he) {
			// 捕捉本类的异常,抛出
			result = false;
			log.printStackTrace(he);
		} finally {
			// 如果由连接则断开连接，结束会话，返回
			if (conn != null)
				conn.endTransaction(result);
		}
		return result;
	}
	
	/***
	 * 根据id获取reportin信息 卞以刚 2012-01-20
	 * @param repid
	 * @return
	 */
	public static ReportIn getReportInByReportInId(Integer repid)
	{
		// 连接和会话对象的初始化
		DBConn conn = null;
		Session session = null;
		try {
			conn = new DBConn();
			session=conn.openSession();
			ReportIn reportIn = (ReportIn) session.load(ReportIn.class,
					repid);
			return reportIn;
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			log.printStackTrace(e);
			return null;
		}
		finally {
			if (conn != null)
				conn.closeSession();
		}
	}
	
	
	/***
	 * 单纯更新ReportIn 卞以刚 2012-01-20
	 * @param reportIn
	 * @return
	 */
	public static boolean updateReportIn(ReportIn reportIn)
	{
		// 置标志result
		boolean result = false;
		// 连接和会话对象的初始化
		DBConn conn = null;
		Session session = null;
		if(reportIn!=null)
		{
			try {
				conn = new DBConn();
				session = conn.beginTransaction();
				session.update(reportIn);
				
				session.flush();
				result=true;
			} catch (HibernateException e) {
				// TODO Auto-generated catch block
				log.printStackTrace(e);
			}finally {
				// 如果由连接则断开连接，结束会话，返回
				if (conn != null)
					conn.endTransaction(result);
			}
		}
		return result;
	}

	/**
	 * 已使用hibernate 卞以刚 2011-12-22
	 * 影响对象：ReportIn
	 * 获取实际数据报表信息
	 * 
	 * @author rds
	 * @serialData 2005-12-18
	 * 
	 * @param repInId
	 *            Integer 实际数据报表ID
	 * @return ReportInForm
	 */
	public static com.cbrc.smis.form.ReportInForm getReportIn(Integer repInId) {
		ReportInForm reportInForm = null;

		if (repInId == null)
			return null;

		DBConn conn = null;

		try {
			conn = new DBConn();
			ReportIn reportIn = (ReportIn) conn.openSession().get(
					ReportIn.class, repInId);
			if (reportIn != null) {
				reportInForm = new ReportInForm();
				TranslatorUtil.copyPersistenceToVo(reportIn, reportInForm);
			}
		} catch (HibernateException he) {
			log.printStackTrace(he);
			reportInForm = null;
		} catch (Exception e) {
			log.printStackTrace(e);
			reportInForm = null;
		} finally {
			if (conn != null)
				conn.closeSession();
		}

		return reportInForm;
	}

	/**
	 * 已使用hibernate 卞以刚 2011-12-21
	 * 根据下述参数获得报表记录（这些参数可以唯一确定一张报表）
	 * 
	 * @author Yao
	 * 
	 * @param childRepId
	 *            报表ID
	 * @param versionId
	 *            版本号
	 * @param orgId
	 *            机构ID
	 * @param year
	 *            年份
	 * @param term
	 *            月份
	 * @param dataRangeId
	 *            数据范围ID
	 * @param curId
	 *            币种ID
	 * @return
	 * 
	 */
	public static ReportIn getReportIn(String childRepId, String versionId,
			String orgId, Integer year, Integer term, Integer dataRangeId,
			Integer curId, Integer times) {
		if (orgId == null || year == null || term == null
				|| dataRangeId == null || curId == null)
			return null;
		ReportIn reportIn = null;
		FitechConnection connFactory = null;
		//DBConn conn = null;
		Statement state = null;
		ResultSet rs = null;
		
		Connection connection = null;
		try {
			connFactory = new FitechConnection();
			connection = connFactory.getConn();
			state = connection.createStatement();

			
			//conn = new DBConn();
			//Session session = conn.openSession();
			// 查询是否插入过相关记录信息
//			StringBuffer selectHQL = new StringBuffer(
//					"from ReportIn ri where  ");
//			selectHQL
//					.append("ri.MChildReport.comp_id.versionId=:versionId and ");
//			selectHQL
//					.append("ri.MChildReport.comp_id.childRepId=:childRepId and ");
//			selectHQL.append("ri.orgId=:orgId and ");
//			selectHQL.append("ri.MCurr.curId=:mCurrId and ");
//			selectHQL.append("ri.MDataRgType.dataRangeId=:dataRangeId and ");
//			selectHQL.append("ri.year=:year and ");
//			selectHQL.append("ri.term=:term and ");
//			selectHQL.append("ri.times=:times");
//
//			Query query = session.createQuery(selectHQL.toString());
//			query.setString("childRepId", childRepId);
//			query.setString("versionId", versionId);
//			query.setString("orgId", orgId.trim());
//			query.setInteger("mCurrId", curId.intValue());
//			query.setInteger("dataRangeId", dataRangeId.intValue());
//			query.setInteger("year", year.intValue());
//			query.setInteger("term", term.intValue());
//			query.setInteger("times", times.intValue());
//			List queryList = query.list();
			//Query query = session.createSQLQuery(selectHQL.toString(), "", ReportIn.class);
			//Query query = session.createQuery(selectHQL.toString());
//			query.setString("childRepId", childRepId);
//			query.setString("versionId", versionId);
//			query.setString("orgId", orgId.trim());
//			query.setInteger("mCurrId", curId.intValue());
//			query.setInteger("dataRangeId", dataRangeId.intValue());
//			query.setInteger("year", year.intValue());
//			query.setInteger("term", term.intValue());
//			query.setInteger("times", times.intValue());
			
			StringBuffer selectHQL = new StringBuffer(
			"select * from report_in ri where  ");
			selectHQL
					.append("ri.version_Id='"+versionId+"' and ");
			selectHQL
					.append("ri.child_Rep_Id='"+childRepId+"' and ");
			selectHQL.append("ri.org_Id='"+orgId.trim()+"' and ");
			selectHQL.append("ri.cur_Id="+curId.intValue()+" and ");
			selectHQL.append("ri.data_Range_Id="+dataRangeId.intValue()+" and ");
			selectHQL.append("ri.year="+year.intValue()+" and ");
			selectHQL.append("ri.term="+term.intValue()+" and ");
			selectHQL.append("ri.times="+times.intValue()+"");
			
			rs = state.executeQuery(selectHQL.toString());
			
			if(rs.next()){
				Integer repInId = rs.getInt("REP_IN_ID");
//				String childrepid = rs.getString("CHILD_REP_ID");
//				Integer datarangeId = rs.getInt("DATA_RANGE_ID");
//				Integer Term = rs.getInt("TERM");
//				Integer Times = rs.getInt("TIMES");
//				String OrgId = rs.getString("ORG_ID");
//				Integer Year = rs.getInt("YEAR");
//				Integer tblOuterValidateFlag = rs.getInt("TBL_OUTER_VALIDATE_FLAG");
//				Integer reportDataWarehouseFlag = rs.getInt("REPORT_DATA_WAREHOUSE_FLAG");
//				Integer tblInnerValidateFlag = rs.getInt("TBL_INNER_VALIDATE_FLAG");
//				String repName = rs.getString("REP_NAME");
				Integer checkFlag = rs.getInt("CHECK_FLAG");
//				Integer packAge = rs.getInt("PACKAGE");
//				Integer CurId = rs.getInt("CUR_ID");
//				String VersionId = rs.getString("VERSION_ID");
//				Date reportDate = rs.getDate("REPORT_DATE");
//				Integer abmormityChangeFlag = rs.getInt("ABMORMITY_CHANGE_FLAG");
//				Integer repRangeFlag = rs.getInt("REP_RANGE_FLAG");
//				Integer forseReportAgainFlag = rs.getInt("FORSE_REPORT_AGAIN_FLAG");
//				Integer laterReportDay = rs.getInt("LATER_REPORT_DAY");
//				Integer notReportFlag = rs.getInt("NOT_REPORT_FLAG");
//				String writer = rs.getString("WRITER");
//				String checker = rs.getString("CHECKER");
//				String principal = rs.getString("PRINCIPAL");
//				String tblOuterInvalidateCause = rs.getString("TBL_OUTER_INVALIDATE_CAUSE");
//				Integer repOutId = rs.getInt("REP_OUT_ID");
//				Date checkDate = rs.getDate("CHECK_DATE");
//				Date verifyDate = rs.getDate("VERIFY_DATE");
//				Integer recheckFlag = rs.getInt("RECHECK_FLAG");
//				String repDesc = rs.getString("REP_DESC");
				
				
				reportIn  = new ReportIn();
				
				reportIn.setCheckFlag(Short.parseShort(checkFlag.toString()));
				reportIn.setRepInId(repInId);
				reportIn.setOrgId(orgId);
				reportIn.setYear(year);
				reportIn.setTerm(term);
				reportIn.getMCurr().setCurId(curId);
				reportIn.setTimes(times);
				reportIn.getMDataRgType().setDataRangeId(dataRangeId);
				reportIn.getMChildReport().getComp_id().setVersionId(versionId);
				reportIn.getMChildReport().getComp_id().setChildRepId(childRepId);
				
			}
			
			//List queryList = query.list();

			// 如果数据库中已经插入了改条记录，则直接返回改记录的信息，不做新的插入操作，避免数据冗余
//			if (queryList != null && queryList.size() > 0) {
//				reportIn = (ReportIn) queryList.get(0);
//			}
		} catch (Exception e) {
			e.printStackTrace();
			reportIn = null;
		} finally {
			try {
				if(rs!=null){
					rs.close();
					rs = null;
				}
				if(state!=null){
					state.close();
					state = null;
				}
				if(!connection.isClosed()){
					connection.close();
					connection = null;
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		/*	if(connFactory!=null){
				connFactory.close();
			}*/
//			if (conn != null)
//				conn.closeSession();
		}
		return reportIn;

	}

	/**
	 * 已使用hibernate 卞以刚 2011-12-21
	 * 保存报表基本信息(以前如果插入过则覆盖改记录)
	 * 
	 * @author Yao
	 * @param reportInForm
	 * @return
	 * @throws Exception
	 * 
	 */
	public ReportIn insertNewReport(ReportInForm reportInForm) throws Exception {
		if (reportInForm == null)
			return null;
		ReportIn reportIn = null;
		boolean result = false;

		DBConn conn = null;
		try {
			conn = new DBConn();
			Session session = conn.beginTransaction();

			String childRepId = reportInForm.getChildRepId();
			String versionId = reportInForm.getVersionId();
			String orgId = reportInForm.getOrgId();
			Integer year = reportInForm.getYear();
			Integer term = reportInForm.getTerm();
			Integer curId = reportInForm.getCurId();
			Integer dataRangeId = reportInForm.getDataRangeId();

			// 查询数据库中是否已经插入了相同记录
			/**已使用hibernate 卞以刚 2011-12-21**/
			reportIn = getReportIn(childRepId, versionId, orgId, year, term,
					dataRangeId, curId, new Integer(1));
			// 如果数据库中已经插入了改条记录，则直接返回改记录的信息，不做新的插入操作，避免数据冗余
			if (reportIn != null) {
				result = true;
				return reportIn;
			}
			// 否则则插入新的信息
			reportIn = new ReportIn();
			TranslatorUtil.copyVoToPersistence(reportIn, reportInForm);
			session.save(reportIn);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			if (conn != null)
				conn.endTransaction(result);
		}
		return reportIn;
	}
	
	/**
	 * 保存报表基本信息(以前如果插入过则覆盖改记录)
	 * 
	 * @author Yao
	 * @param reportInForm
	 * @return
	 * @throws Exception
	 * 
	 */
	public ReportIn insertNewyjhReport(ReportInForm reportInForm) throws Exception {
		if (reportInForm == null)
			return null;
		ReportIn reportIn = null;
		boolean result = false;

		DBConn conn = null;
		try {
			conn = new DBConn();
			Session session = conn.beginTransaction();

			String childRepId = reportInForm.getChildRepId();
			String versionId = reportInForm.getVersionId();
			String orgId = reportInForm.getOrgId();
			Integer year = reportInForm.getYear();
			Integer term = reportInForm.getTerm();
			Integer curId = reportInForm.getCurId();
			Integer dataRangeId = reportInForm.getDataRangeId();

			// 查询数据库中是否已经插入了相同记录
			reportIn = getReportIn(childRepId, versionId, orgId, year, term,
					dataRangeId, curId, new Integer(-2));
			// 如果数据库中已经插入了改条记录，则直接返回改记录的信息，不做新的插入操作，避免数据冗余
			if (reportIn != null) {
				result = true;
				return reportIn;
			}
			// 否则则插入新的信息
			reportIn = new ReportIn();
			TranslatorUtil.copyVoToPersistence(reportIn, reportInForm);
			session.save(reportIn);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			if (conn != null)
				conn.endTransaction(result);
		}
		return reportIn;
	}

	/**
	 * 根据前台页面的查询结果将用户选中更改的记录写审核标志为1
	 * 
	 * @author 唐磊
	 * @param orgArray
	 *            传过来的机构id的string型数组变量
	 * @param checkSign
	 *            传过来的用户选择的审核标志
	 * @param reportInPersistence
	 *            持久化对象ReportIn
	 * @param he
	 *            HibernateException 有异常则捕捉抛出
	 * @param e
	 *            Exception 有异常则捕捉抛出
	 * @param result
	 *            boolean型变量,更新成功返回true,否则返回false
	 */
	public static boolean update(com.cbrc.smis.form.ReportInForm reportInForm)
			throws Exception {
		// 更新标志
		boolean result = true;
		// 创建连接
		DBConn conn = null;
		Session session = null;
		// reportform中是否有参数
		if (reportInForm == null) {
			return false;
		}

		try {
			conn = new DBConn();
			session = conn.beginTransaction();

			// 循环给持久化对象传递form对象的机构id数组的参数
			if (reportInForm.getRepInIdArray() != null
					&& !reportInForm.getRepInIdArray().equals("")) {
				Integer repOutIds[] = new Integer[reportInForm
						.getRepInIdArray().length];
				for (int i = 0; i < reportInForm.getRepInIdArray().length; i++) {
					ReportIn reportInPersistence = (ReportIn) session.load(
							ReportIn.class, reportInForm.getRepInIdArray()[i]);
					// reportInPersistence.setCheckFlag(reportInForm.getCheckSignOption()[i]);
					reportInPersistence.setCheckFlag(reportInForm
							.getCheckFlag());
					// 新增复核时间
					reportInPersistence.setVerifyDate(new Date());
					
					if (reportInForm.getCheckFlag().equals(
									com.fitech.net.config.Config.CHECK_FLAG_FAILED)) {
						reportInPersistence.setTblInnerValidateFlag(null);
						reportInPersistence.setTblOuterValidateFlag(null);
					}
					// System.out.println("checkFlag:" +
					// reportInForm.getCheckFlag());
					if (reportInForm.getForseReportAgainFlag() != null)
						reportInPersistence.setForseReportAgainFlag(reportInForm.getForseReportAgainFlag());
					else
						reportInPersistence.setForseReportAgainFlag(null);

					session.update(reportInPersistence);
					session.flush();
					repOutIds[i] = reportInPersistence.getRepOutId();
				}
				reportInForm.setRepOutIds(repOutIds);
			} else {
				result = false;
			}

			// 异常的捕捉机制
		} catch (HibernateException he) {
			result = false;
			log.printStackTrace(he);
		} catch (Exception e) {
			result = false;
			log.printStackTrace(e);
		} finally {
			// 如果conn对象依然存在,则结束当前事务并断开连接
			if (conn != null)
				conn.endTransaction(result);
		}
		// 更新成功返回吧!!!
		return result;
	}

	/**
	 * Retrieve an existing com.cbrc.smis.form.ReportInForm object for editing.
	 * 
	 * @param reportInForm
	 *            The com.cbrc.smis.form.ReportInForm object containing the data
	 *            used to retrieve the object (i.e. the primary key info).
	 * @exception Exception
	 *                If the com.cbrc.smis.form.ReportInForm object cannot be
	 *                retrieved.
	 */
	public static com.cbrc.smis.form.ReportInForm edit(
			com.cbrc.smis.form.ReportInForm reportInForm) throws Exception {
		com.cbrc.smis.hibernate.ReportIn reportInPersistence = new com.cbrc.smis.hibernate.ReportIn();
		TranslatorUtil.copyVoToPersistence(reportInPersistence, reportInForm);
		// javax.naming.InitialContext ctx = new javax.naming.InitialContext();
		// TODO: Make adapter get SessionFactory jndi name by ant task argument?
		// net.sf.hibernate.SessionFactory factory =
		// (net.sf.hibernate.SessionFactory) ctx
		// .lookup("java:AirlineHibernateFactory");
		// net.sf.hibernate.Session session = factory.openSession();
		DBConn conn = new DBConn();
		Session session = conn.openSession();
		net.sf.hibernate.Transaction tx = session.beginTransaction();
		reportInPersistence = (com.cbrc.smis.hibernate.ReportIn) session.load(
				com.cbrc.smis.hibernate.ReportIn.class, reportInPersistence
						.getRepInId());
		tx.commit();
		session.close();
		TranslatorUtil.copyPersistenceToVo(reportInPersistence, reportInForm);
		return reportInForm;
	}

	/**
	 * 删除报表基本信息数据（无外键关联的情况下）
	 * 
	 * @param RepInId
	 * @throws Exception
	 * @author Yao
	 */
	public static boolean remove(Integer repInId) throws Exception {
		boolean result = false;
		DBConn conn = null;
		Session session = null;

		conn = new DBConn();

		try {
			session = conn.beginTransaction();
			ReportIn repIn = (ReportIn) session.load(ReportIn.class, repInId);
			if (repIn != null) {
				session.delete(repIn);
				session.flush();
				result = true;
			}
		} catch (Exception e) {
			result = false;
			log.printStackTrace(e);
		} finally {
			if (conn != null)
				conn.endTransaction(result);
		}
		return result;
	}

	/**
	 * Retrieve all existing com.cbrc.smis.form.ReportInForm objects.
	 * 
	 * @exception Exception
	 *                If the com.cbrc.smis.form.ReportInForm objects cannot be
	 *                retrieved.
	 */
	public static List findAll() throws Exception {
		List retVals = new ArrayList();
		javax.naming.InitialContext ctx = new javax.naming.InitialContext();
		// TODO: Make adapter get SessionFactory jndi name by ant task argument?
		net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory) ctx
				.lookup("java:AirlineHibernateFactory");
		net.sf.hibernate.Session session = factory.openSession();
		net.sf.hibernate.Transaction tx = session.beginTransaction();
		retVals.addAll(session.find("from com.cbrc.smis.hibernate.ReportIn"));
		tx.commit();
		session.close();
		ArrayList reportIn_vos = new ArrayList();
		for (Iterator it = retVals.iterator(); it.hasNext();) {
			com.cbrc.smis.form.ReportInForm reportInFormTemp = new com.cbrc.smis.form.ReportInForm();
			com.cbrc.smis.hibernate.ReportIn reportInPersistence = (com.cbrc.smis.hibernate.ReportIn) it
					.next();
			TranslatorUtil.copyPersistenceToVo(reportInPersistence,
					reportInFormTemp);
			reportIn_vos.add(reportInFormTemp);
		}
		retVals = reportIn_vos;
		return retVals;
	}

	/**
	 * This method will return all objects referenced by MDataRgType
	 */
	public static List getMDataRgType(
			com.cbrc.smis.form.ReportInForm reportInForm) throws Exception {
		List retVals = new ArrayList();
		com.cbrc.smis.hibernate.ReportIn reportInPersistence = null;
		javax.naming.InitialContext ctx = new javax.naming.InitialContext();
		// TODO: Make adapter get SessionFactory jndi name by ant task argument?
		net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory) ctx
				.lookup("java:AirlineHibernateFactory");
		net.sf.hibernate.Session session = factory.openSession();
		net.sf.hibernate.Transaction tx = session.beginTransaction();
		reportInPersistence = (com.cbrc.smis.hibernate.ReportIn) session.load(
				com.cbrc.smis.hibernate.ReportIn.class, reportInPersistence
						.getRepInId());
		tx.commit();
		session.close();
		retVals.add(reportInPersistence.getMDataRgType());
		ArrayList DATA_RANGE_ID_vos = new ArrayList();
		for (Iterator it = retVals.iterator(); it.hasNext();) {
			com.cbrc.smis.form.MDataRgTypeForm DATA_RANGE_ID_Temp = new com.cbrc.smis.form.MDataRgTypeForm();
			com.cbrc.smis.hibernate.MDataRgType DATA_RANGE_ID_PO = (com.cbrc.smis.hibernate.MDataRgType) it
					.next();
			TranslatorUtil.copyPersistenceToVo(DATA_RANGE_ID_PO,
					DATA_RANGE_ID_Temp);
			DATA_RANGE_ID_vos.add(DATA_RANGE_ID_Temp);
		}
		retVals = DATA_RANGE_ID_vos;
		return retVals;
	}

	/**
	 * This method will return all objects referenced by MCurr
	 */
	public static List getMCurr(com.cbrc.smis.form.ReportInForm reportInForm)
			throws Exception {
		List retVals = new ArrayList();
		com.cbrc.smis.hibernate.ReportIn reportInPersistence = null;
		javax.naming.InitialContext ctx = new javax.naming.InitialContext();
		// TODO: Make adapter get SessionFactory jndi name by ant task argument?
		net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory) ctx
				.lookup("java:AirlineHibernateFactory");
		net.sf.hibernate.Session session = factory.openSession();
		net.sf.hibernate.Transaction tx = session.beginTransaction();
		reportInPersistence = (com.cbrc.smis.hibernate.ReportIn) session.load(
				com.cbrc.smis.hibernate.ReportIn.class, reportInPersistence
						.getRepInId());
		tx.commit();
		session.close();
		retVals.add(reportInPersistence.getMCurr());
		ArrayList CUR_ID_vos = new ArrayList();
		for (Iterator it = retVals.iterator(); it.hasNext();) {
			com.cbrc.smis.form.MCurrForm CUR_ID_Temp = new com.cbrc.smis.form.MCurrForm();
			com.cbrc.smis.hibernate.MCurr CUR_ID_PO = (com.cbrc.smis.hibernate.MCurr) it
					.next();
			TranslatorUtil.copyPersistenceToVo(CUR_ID_PO, CUR_ID_Temp);
			CUR_ID_vos.add(CUR_ID_Temp);
		}
		retVals = CUR_ID_vos;
		return retVals;
	}

	/**
	 * This method will return all objects referenced by MRepRange
	 */
	public static List getMRepRange(com.cbrc.smis.form.ReportInForm reportInForm)
			throws Exception {
		List retVals = new ArrayList();
		com.cbrc.smis.hibernate.ReportIn reportInPersistence = null;
		javax.naming.InitialContext ctx = new javax.naming.InitialContext();
		// TODO: Make adapter get SessionFactory jndi name by ant task argument?
		net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory) ctx
				.lookup("java:AirlineHibernateFactory");
		net.sf.hibernate.Session session = factory.openSession();
		net.sf.hibernate.Transaction tx = session.beginTransaction();
		reportInPersistence = (com.cbrc.smis.hibernate.ReportIn) session.load(
				com.cbrc.smis.hibernate.ReportIn.class, reportInPersistence
						.getRepInId());
		tx.commit();
		session.close();
		retVals.add(reportInPersistence.getMRepRange());
		ArrayList ORG_ID_vos = new ArrayList();
		for (Iterator it = retVals.iterator(); it.hasNext();) {
			com.cbrc.smis.form.MRepRangeForm ORG_ID_Temp = new com.cbrc.smis.form.MRepRangeForm();
			com.cbrc.smis.hibernate.MRepRange ORG_ID_PO = (com.cbrc.smis.hibernate.MRepRange) it
					.next();
			TranslatorUtil.copyPersistenceToVo(ORG_ID_PO, ORG_ID_Temp);
			ORG_ID_vos.add(ORG_ID_Temp);
		}
		retVals = ORG_ID_vos;
		return retVals;
	}

	/**
	 * This method will return all objects referenced by MChildReport
	 */
	public static List getMChildReport(
			com.cbrc.smis.form.ReportInForm reportInForm) throws Exception {
		List retVals = new ArrayList();
		com.cbrc.smis.hibernate.ReportIn reportInPersistence = null;
		javax.naming.InitialContext ctx = new javax.naming.InitialContext();
		// TODO: Make adapter get SessionFactory jndi name by ant task argument?
		net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory) ctx
				.lookup("java:AirlineHibernateFactory");
		net.sf.hibernate.Session session = factory.openSession();
		net.sf.hibernate.Transaction tx = session.beginTransaction();
		reportInPersistence = (com.cbrc.smis.hibernate.ReportIn) session.load(
				com.cbrc.smis.hibernate.ReportIn.class, reportInPersistence
						.getRepInId());
		tx.commit();
		session.close();
		retVals.add(reportInPersistence.getMChildReport());
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
	 * 该方法用于将一个实际子报表对象持久化到数据库中去
	 * 
	 * @param mcr
	 * @param mCurr
	 * @param mdrt
	 * @param mrr
	 * @param year
	 * @param term
	 * @param writer
	 * @param checker
	 * @param principal
	 * @param times
	 * @param repName
	 * @param date
	 * @param zipFileName
	 * @param xmlFileName
	 * @param repOutId
	 * @return
	 */
	public static ReportIn insertReportIn(MChildReport mcr, MCurr mCurr,
			MDataRgType mdrt, MRepRange mrr, Integer year, Integer term,
			String writer, String checker, String principal, Integer times,
			String repName, Date date, String zipFileName, String xmlFileName,
			Integer repOutId) {

		ReportIn reportIn = new ReportIn();

		reportIn.setMChildReport(mcr); // 将子报表SET进实际报表对象中

		reportIn.setMCurr(mCurr); // 将货币SET进实际报表对象中

		reportIn.setMRepRange(mrr); // 将机构适用范围SET进实际报表对象中

		reportIn.setMDataRgType(mdrt); // 将数据范围SET进实际报表对象中

		reportIn.setOrgId(mrr.getComp_id().getOrgId());

		reportIn.setYear(year);

		reportIn.setTerm(term);

		reportIn.setWriter(writer);
		reportIn.setChecker(checker);
		reportIn.setPrincipal(principal);
		reportIn.setTimes(times);

		reportIn.setRepName(repName);

		reportIn.setReportDate(date);

		reportIn.setCheckFlag(new Short((short) 0));

		reportIn.setRepOutId(repOutId); // 外网数据记录ID

		reportIn.setReportDataWarehouseFlag(new Short("0")); // 初始化数据仓库的标志

		reportIn.setForseReportAgainFlag(new Short("0")); // 初始化数据重报填报标志

		/* reportIn.setReportDate(reportDate); */

		DBConn dBConn = null;

		Session session = null;

		try {

			dBConn = new DBConn();

			session = dBConn.beginTransaction();

			session.save(reportIn);

			dBConn.endTransaction(true);

		} catch (HibernateException e) {

			dBConn.endTransaction(false);

			String errorMessage = "错误发生在存储" + zipFileName + "包文件中的";

			errorMessage = errorMessage + xmlFileName + "文件";

			FitechLog.writeLog(Config.LOG_SYSTEM_SAVEDATA, ConfigOncb.HANDLER,
					errorMessage, "OVER");

			reportIn = null;

		} finally {

			if (session != null)

				dBConn.closeSession();
		}

		return reportIn;

	}

	/**
	 * @author cb 对实际报表表单修改
	 * 
	 * @param reportIn
	 * @param repName
	 * @param date
	 * @throws Exception
	 */
	public static void updateReportIn(ReportIn reportIn, String repName,
			String dateString) throws Exception {

		DBConn dBConn = null;

		Session session = null;

		Date date = null;

		if (dateString.equals(""))

			dateString = null;

		if (dateString != null) {

			try {

				date = DateUtil
						.getDateByString(dateString, DateUtil.NORMALDATE);

			} catch (Exception e) {

				date = null;
			}

		}

		try {

			dBConn = new DBConn();

			session = dBConn.beginTransaction();

			reportIn.setRepName(repName);

			reportIn.setReportDate(date);

			session.update(reportIn);

			dBConn.endTransaction(true);

		} catch (Exception e) {

			dBConn.endTransaction(false);
		} finally {

			if (session != null) {

				dBConn.closeSession();
			}
		}
	}

	/**
	 * 根据输入的参数来判断判断一个实际表单是否已经录入过了
	 * 
	 * @param childRepId
	 *            子报表ID
	 * @param versionId
	 *            版本号
	 * @param orgId
	 *            机构ID
	 * @param mCurr
	 *            币种名
	 * @param dataRangeId
	 *            数据范围
	 * @param year
	 *            年份
	 * @param term
	 *            期数
	 * @param times
	 *            次数
	 * @return boolean
	 * @throws Exception
	 */
	public static boolean isRepeat(String childRepId, String versionId,
			String orgId, String mCurrName, Integer dataRangeId, Integer year,
			Integer term, Integer times) throws Exception {

		DBConn dBConn = null;

		Session session = null;

		List l = null;

		Query query = null;

		boolean isre = false; // 默认不重复

		int size;

		String hsql = "from ReportIn ri where  ";

		hsql = hsql + "ri.MChildReport.comp_id.versionId=:versionId and ";

		hsql = hsql + " ri.MChildReport.comp_id.childRepId=:childRepId and ";

		hsql = hsql + "ri.orgId=:orgId and ";

		hsql = hsql + "ri.MCurr.curName=:mCurrName and ";

		hsql = hsql + "ri.MDataRgType.dataRangeId=:dataRangeId and ";

		hsql = hsql + "ri.year=:year and ";

		hsql = hsql + "ri.term=:term  and ";

		hsql = hsql + "ri.times=:times ";

		try {

			dBConn = new DBConn();

			session = dBConn.openSession();

			query = session.createQuery(hsql);

			query.setString("childRepId", childRepId);

			query.setString("versionId", versionId);

			query.setString("orgId", orgId);

			if (mCurrName == null || mCurrName.equals(""))

				query.setString("mCurrName", ConfigOncb.COMMONCURRNAME);

			else

				query.setString("mCurrName", mCurrName);

			query.setInteger("dataRangeId", dataRangeId.intValue());

			query.setInteger("year", year.intValue());

			query.setInteger("term", term.intValue());

			query.setInteger("times", times.intValue());

			l = query.list();

			size = l.size();

			if (size != 0)

				isre = true;

			else

				System.out.println("不存在");

		} catch (Exception e) {

			log.printStackTrace(e);

			isre = true;
		}

		finally {

			if (session != null)

				dBConn.closeSession();
		}
		return isre;
	}

	/*
	 * public ReportIn getReportInOncb(Integer id)throws Exception{
	 * 
	 * 
	 * DBConn dBConn = null;
	 * 
	 * Session session = null;
	 * 
	 * ReportIn = null;
	 * 
	 * try{
	 * 
	 * dBConn = new DBConn(); } }
	 */
	/**
	 * 取得需要转成xml的报表信息
	 * 
	 * @author 姚捷
	 * @return List 包含需要转成xml的报表信息
	 */
	public static List getNeedToXmlReps() {
		List result = new ArrayList();
		DBConn conn = null;
		Session session = null;

		String hql = "from ReportIn ri where ri.checkFlag=1 and ri.reportDataWarehouseFlag=0 and "
				+ "ri.times=(select max(r.times) from ReportIn r where "
				+ "ri.MChildReport.comp_id.childRepId=r.MChildReport.comp_id.childRepId and "
				+ "ri.MChildReport.comp_id.versionId=r.MChildReport.comp_id.versionId and "
				+ "ri.MDataRgType.dataRangeId=r.MDataRgType.dataRangeId and "
				+ "ri.orgId=r.orgId and ri.MCurr.curId=r.MCurr.curId and "
				+ "ri.year=r.year and ri.term=r.term and r.checkFlag=1 and r.reportDataWarehouseFlag=0)";

		try {
			conn = new DBConn();
			session = conn.openSession();
			result = session.createQuery(hql).list();
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return result;
	}

	/**
	 * 将报送数据仓库标志改变
	 * 
	 * @author 姚捷
	 * @param 实际子报表id
	 * @return 是否成功
	 */
	public static boolean setReportDataWarehouseFlag(Integer repInId) {
		boolean result = false;
		DBConn conn = null;
		Session session = null;

		conn = new DBConn();

		try {
			session = conn.beginTransaction();
			ReportIn repIn = (ReportIn) session.load(ReportIn.class, repInId);
			if (repIn != null) {
				repIn.setReportDataWarehouseFlag(Short.valueOf("1"));
				session.update(repIn);
				session.flush();
				result = true;
			}
		} catch (Exception e) {
			result = false;
			log.printStackTrace(e);
		} finally {
			if (conn != null)
				conn.endTransaction(result);
		}
		return result;
	}

	/**
	 * 重报管理查询（查询已设置重报的报表记录数）
	 * 
	 * @param reportInForm
	 *            报表FormBean
	 * @param operator
	 *            当前登录用户信息
	 * @return int 查询记录数
	 */
	public static int getCount(ReportInForm reportInForm, Operator operator) {
		int count = 0;
		DBConn conn = null;
		Session session = null;
		if (reportInForm == null || operator == null)
			return count;

		try {
			StringBuffer hql = new StringBuffer(
					"select count(*) from ReportAgainSet a where a.reportIn.forseReportAgainFlag="
							+ Config.FORSE_REPORT_AGAIN_FLAG_1);

			StringBuffer where = new StringBuffer("");
			/** 添加报表编号查询条件（忽略大小写模糊查询） */
			if (reportInForm.getChildRepId() != null
					&& !reportInForm.getChildRepId().equals("")) {
				where
						.append(" and upper(a.reportIn.MChildReport.comp_id.childRepId) like upper('%"
								+ reportInForm.getChildRepId().trim() + "%')");
			}
			/** 添加报表名称查询条件（模糊查询） */
			if (reportInForm.getRepName() != null
					&& !reportInForm.getRepName().equals("")) {
				where.append(" and a.reportIn.repName like '%"
						+ reportInForm.getRepName().trim() + "%'");
			}
			/** 添加模板类型（全部/法人/分支）查询条件 */
			if (reportInForm.getFrOrFzType() != null
					&& !reportInForm.getFrOrFzType().equals(
							Config.DEFAULT_VALUE)) {
				where.append(" and a.reportIn.MChildReport.frOrFzType='"
						+ reportInForm.getFrOrFzType() + "'");
			}
			/** 添加报送频度（月/季/半年/年）查询条件 */
			if (reportInForm.getRepFreqId() != null
					&& !String.valueOf(reportInForm.getRepFreqId()).equals(
							Config.DEFAULT_VALUE)) {
				where
						.append(" and a.reportIn.MDataRgType.dataRangeId in (select M.comp_id.dataRangeId from MActuRep M "
								+ "where M.comp_id.childRepId=a.reportIn.MChildReport.comp_id.childRepId and M.comp_id.versionId=a.reportIn.MChildReport.comp_id.versionId "
								+ "and M.comp_id.repFreqId="
								+ reportInForm.getRepFreqId() + ")");
			}
			// /**添加重报设定日期查询条件*/
			// if (reportInForm.getStartDate() != null &&
			// !reportInForm.getStartDate().equals("")){
			// if(reportInForm.getEndDate() != null &&
			// !reportInForm.getEndDate().equals("")){
			// where.append(" and date(a.setDate) between date('" +
			// reportInForm.getStartDate() + "') and date('" +
			// reportInForm.getEndDate() + "')");
			// }else{
			// where.append(" and date(a.setDate) >= date('" +
			// reportInForm.getStartDate() + "')");
			// }
			// }else{
			// if(reportInForm.getEndDate() != null &&
			// !reportInForm.getEndDate().equals("")){
			// where.append(" and date(a.setDate) <= date('" +
			// reportInForm.getEndDate() + "')");
			// }
			// }
			/** 添加日期（年份）查询条件 */
			if (reportInForm.getYear() != null) {
				where.append(" and a.reportIn.year=" + reportInForm.getYear());
			}
			/** 添加日期（月份）查询条件 */
			if (reportInForm.getTerm() != null) {
				where.append(" and a.reportIn.term=" + reportInForm.getTerm());
			}
			/** 添加机构查询条件 */
			if (!StringUtil.isEmpty(reportInForm.getOrgId())
					&& !reportInForm.getOrgId().equals(Config.DEFAULT_VALUE)) {
				where.append(" and a.reportIn.orgId='"
						+ reportInForm.getOrgId().trim() + "'");
			}

			/** 添加报表审核权限[重报权限=审核权限]（超级用户不用添加） 
			 * 已增加数据库判断*/
			if (operator.isSuperManager() == false) {
				if (operator.getChildRepCheckPodedom() == null
						|| operator.getChildRepCheckPodedom().equals(""))
					return count;
				if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
					where.append(" and ltrim(rtrim(a.reportIn.orgId)) || a.reportIn.MChildReport.comp_id.childRepId in("
									+ operator.getChildRepCheckPodedom() + ")");
				if(Config.DB_SERVER_TYPE.equals("sqlserver"))
					where.append(" and ltrim(rtrim(a.reportIn.orgId)) + a.reportIn.MChildReport.comp_id.childRepId in("
							+ operator.getChildRepCheckPodedom() + ")");
			}
			hql.append(where.toString());

			conn = new DBConn();
			session = conn.openSession();
			Query query = session.createQuery(hql.toString());
			List list = query.list();
			if (list != null && list.size() > 0)
				count = ((Integer) list.get(0)).intValue();
		} catch (Exception e) {
			count = 0;
			log.printStackTrace(e);
		} finally {
			// 关闭数据库连接
			if (conn != null)
				conn.closeSession();
		}
		return count;
	}

	/**
	 * 指标生成查找是否有该期报表数据
	 * 
	 * @param reportInForm
	 * @return
	 */
	public static int getCount(ActuTargetResultForm atrForm) {
		int result = 0;
		DBConn conn = null;
		Session session = null;
		if (atrForm == null)
			return result;

		StringBuffer hql = new StringBuffer(
				"select count(*) from ReportIn ri where 1=1");
		if (atrForm.getYear() != null)
			hql.append(" and ri.year=" + atrForm.getYear());
		if (atrForm.getMonth() != null)
			hql.append(" and ri.term=" + atrForm.getMonth());
		if (atrForm.getOrgId() != null && !atrForm.getOrgId().equals(""))
			hql.append(" and ri.orgId='" + atrForm.getOrgId() + "'");

		try {
			conn = new DBConn();
			session = conn.openSession();
			Query query = session.createQuery(hql.toString());
			List list = query.list();

			if (list != null)
				result = ((Integer) list.get(0)).intValue();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.closeSession();
		}

		return result;
	}

	/**
	 * 重报管理查询（查询已设置重报的报表记录）
	 * 
	 * @param reportInForm
	 *            报表FormBean
	 * @param operator
	 *            当前登录用户信息
	 * @param offset
	 *            偏移量
	 * @param limit
	 *            每页显示记录数
	 * @param operator
	 *            当前登录用户信息
	 * @return List 报表记录结果集
	 */
	public static List getRecord(ReportInForm reportInForm, int offset,
			int limit, Operator operator) {
		List resList = null;
		DBConn conn = null;
		Session session = null;

		if (reportInForm == null || operator == null)
			return resList;

		try {
			StringBuffer hql = new StringBuffer(
					"select a.rasId,a.reportIn.repInId,a.cause,a.setDate,a.reportIn.repName ,a.reportIn.orgId"
							+ ",a.reportIn.MChildReport.comp_id.childRepId,a.reportIn.MChildReport.comp_id.versionId,a.reportIn.MChildReport.reportName "
							+ ",a.reportIn.MCurr.curName,a.reportIn.MDataRgType.dataRgDesc,a.reportIn.MDataRgType.dataRangeId,a.reportIn.year,a.reportIn.term "
							+ "from ReportAgainSet a where a.reportIn.forseReportAgainFlag="
							+ Config.FORSE_REPORT_AGAIN_FLAG_1);

			StringBuffer where = new StringBuffer("");
			/** 添加报表编号查询条件（忽略大小写模糊查询） */
			if (reportInForm.getChildRepId() != null
					&& !reportInForm.getChildRepId().equals("")) {
				where
						.append(" and upper(a.reportIn.MChildReport.comp_id.childRepId) like upper('%"
								+ reportInForm.getChildRepId().trim() + "%')");
			}
			/** 添加报表名称查询条件（模糊查询） */
			if (reportInForm.getRepName() != null
					&& !reportInForm.getRepName().equals("")) {
				where.append(" and a.reportIn.repName like '%"
						+ reportInForm.getRepName().trim() + "%'");
			}
			/** 添加模板类型（全部/法人/分支）查询条件 */
			if (reportInForm.getFrOrFzType() != null
					&& !reportInForm.getFrOrFzType().equals(
							Config.DEFAULT_VALUE)) {
				where.append(" and a.reportIn.MChildReport.frOrFzType='"
						+ reportInForm.getFrOrFzType() + "'");
			}
			/** 添加报送频度（月/季/半年/年）查询条件 */
			if (reportInForm.getRepFreqId() != null
					&& !String.valueOf(reportInForm.getRepFreqId()).equals(
							Config.DEFAULT_VALUE)) {
				where
						.append(" and a.reportIn.MDataRgType.dataRangeId in (select M.comp_id.dataRangeId from MActuRep M "
								+ "where M.comp_id.childRepId=a.reportIn.MChildReport.comp_id.childRepId and M.comp_id.versionId=a.reportIn.MChildReport.comp_id.versionId "
								+ "and M.comp_id.repFreqId="
								+ reportInForm.getRepFreqId() + ")");
			}
			// /**添加重报设定日期查询条件*/
			// if (reportInForm.getStartDate() != null &&
			// !reportInForm.getStartDate().equals("")){
			// if(reportInForm.getEndDate() != null &&
			// !reportInForm.getEndDate().equals("")){
			// where.append(" and date(a.setDate) between date('" +
			// reportInForm.getStartDate() + "') and date('" +
			// reportInForm.getEndDate() + "')");
			// }else{
			// where.append(" and date(a.setDate) >= date('" +
			// reportInForm.getStartDate() + "')");
			// }
			// }else{
			// if(reportInForm.getEndDate() != null &&
			// !reportInForm.getEndDate().equals("")){
			// where.append(" and date(a.setDate) <= date('" +
			// reportInForm.getEndDate() + "')");
			// }
			// }
			/** 添加日期（年份）查询条件 */
			if (reportInForm.getYear() != null) {
				where.append(" and a.reportIn.year=" + reportInForm.getYear());
			}
			/** 添加日期（月份）查询条件 */
			if (reportInForm.getTerm() != null) {
				where.append(" and a.reportIn.term=" + reportInForm.getTerm());
			}
			/** 添加机构查询条件 */
			if (!StringUtil.isEmpty(reportInForm.getOrgId())
					&& !reportInForm.getOrgId().equals(Config.DEFAULT_VALUE)) {
				where.append(" and a.reportIn.orgId='"
						+ reportInForm.getOrgId().trim() + "'");
			}

			/** 添加报表审核权限[重报权限=审核权限]（超级用户不用添加）
			 * 已增加数据库判断 */
			if (operator.isSuperManager() == false) {
				if (operator.getChildRepCheckPodedom() == null
						|| operator.getChildRepCheckPodedom().equals(""))
					return resList;
				if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
					where.append(" and ltrim(rtrim(a.reportIn.orgId)) || a.reportIn.MChildReport.comp_id.childRepId in("
								+ operator.getChildRepCheckPodedom() + ")");
				if(Config.DB_SERVER_TYPE.equals("sqlserver"))
					where.append(" and ltrim(rtrim(a.reportIn.orgId)) + a.reportIn.MChildReport.comp_id.childRepId in("
							+ operator.getChildRepCheckPodedom() + ")");
			}
			hql.append(where.toString() + " order by a.setDate desc");

			conn = new DBConn();
			session = conn.openSession();
			Query query = session.createQuery(hql.toString());
			query.setFirstResult(offset).setMaxResults(limit);
			List list = query.list();
			if (list != null && list.size() > 0) {
				resList = new ArrayList();
				for (int i = 0; i < list.size(); i++) {
					Object[] items = (Object[]) list.get(i);
					ReportAgainSetForm setform = new ReportAgainSetForm();
					setform.setRasId((Integer) items[0]);
					setform.setRepInId(items[1] == null ? new Integer(0)
							: (Integer) items[1]);
					setform.setCause(items[2] == null ? "" : (String) items[2]);
					setform.setSetDate(items[3] != null ? (Timestamp) items[3]
							: null);
					if (items[4] != null && items[8] != null) {
						if (((String) items[4]).equals((String) items[8]))
							setform.setRepName((String) items[4]);
						else
							setform.setRepName((String) items[4] + "-"
									+ (String) items[8]);
					} else
						setform.setRepName("");
					setform.setOrgName(StrutsOrgNetDelegate.selectOne(
							items[5].toString()).getOrgName());
					setform.setChildRepId(items[6].toString());
					setform.setVersionId(items[7].toString());
					setform.setCurrName(items[9].toString());

					/** 设置报送频度 */
					MActuRep mActuRep = GetFreR(items[6].toString(), items[7]
							.toString(), items[11].toString());
					if (mActuRep != null) {
						setform.setActuFreqName(mActuRep.getMRepFreq()
								.getRepFreqName());
					}
					setform.setDataRangeDes(items[10].toString());
					setform.setReportDate(items[12].toString() + "-"
							+ items[13].toString());
					resList.add(setform);
				}
			}
		} catch (Exception e) {
			resList = null;
			log.printStackTrace(e);
		} finally {
			// 关闭数据库连接
			if (conn != null)
				conn.closeSession();
		}
		return resList;
	}

	/**
	 * 验证新建强制重报提交的表单
	 * 
	 * @author 曹发根
	 * @return true 信息填写正确；false 信息填写错误。
	 */
	public static boolean validate(ReportInForm reportInForm) {
		boolean result = false;
		DBConn conn = null;
		Session session = null;
		try {
			if (reportInForm == null)
				return result;
			String repName = reportInForm.getRepName();
			String versionId = reportInForm.getVersionId();
			String orgName = reportInForm.getOrgName();
			String dataRangeId = reportInForm.getDataRangeId().toString();
			String curId = reportInForm.getCurId().toString();
			String term = reportInForm.getTerm().toString();
			String year = reportInForm.getYear().toString();
			String cause = reportInForm.getCause();

			if (repName == null || versionId == null || orgName == null
					|| dataRangeId == null || curId == null || term == null
					|| year == null || cause == null)
				return result;
			if (repName.equals("") || versionId.equals("")
					|| orgName.equals("") || dataRangeId.equals("")
					|| curId.equals("") || term.equals("") || year.equals("")
					|| cause.equals(""))
				return result;

			String orgids = com.cbrc.org.adapter.StrutsMOrgDelegate
					.getOrgid(reportInForm.getOrgName());
			if (orgids.equals(""))
				return false;
			StringBuffer hql = new StringBuffer(
					"select count(*) from ReportIn a");
			StringBuffer where = new StringBuffer(" where a.repName='"
					+ repName + "' and a.MRepRange.comp_id.versionId='"
					+ versionId + "' and a.orgId in(" + orgids
					+ ") and a.MDataRgType.dataRangeId=" + dataRangeId
					+ " and a.MCurr.curId=" + curId + " and a.term=" + term
					+ " and a.year=" + year);
			hql.append(where);

			conn = new DBConn();
			session = conn.openSession();
			Query query = session.createQuery(hql.toString());
			List list = query.list();

			if (list != null) {
				int count = ((Integer) list.get(0)).intValue();
				if (count == 1)
					result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (conn != null)
				conn.closeSession();
		}

		return result;
	}

	/**
	 * 新建强制重报
	 * 
	 * @author 曹发根
	 * @return true 新建成功；false新建失败！
	 * @throws HibernateException
	 */
	public static boolean newForseReportIn(ReportInForm reportInForm)
			throws HibernateException {

		boolean result = false;
		DBConn conn = null;
		Session session = null;
		Transaction tx = null;
		try {
			conn = new DBConn();
			session = conn.openSession();
			tx = session.beginTransaction();
			String repName = reportInForm.getRepName();
			String versionId = reportInForm.getVersionId();
			String dataRangeId = reportInForm.getDataRangeId().toString();
			String curId = reportInForm.getCurId().toString();
			String term = reportInForm.getTerm().toString();
			String year = reportInForm.getYear().toString();
			String orgids = com.cbrc.org.adapter.StrutsMOrgDelegate
					.getOrgid(reportInForm.getOrgName());
			StringBuffer hql = new StringBuffer("from ReportIn a");
			StringBuffer where = new StringBuffer(" where a.repName='"
					+ repName + "' and a.MRepRange.comp_id.versionId='"
					+ versionId + "'and a.orgId in(" + orgids
					+ ")and a.MDataRgType.dataRangeId=" + dataRangeId
					+ " and a.MCurr.curId=" + curId + " and a.term=" + term
					+ " and a.year=" + year + " and a.checkFlag!="
					+ Config.CHECK_FLAG_NO + " and a.reportDataWarehouseFlag!="
					+ Config.Reported_Data_Warehouse + " order by a.times desc");
			hql.append(where);
			Query query = session.createQuery(hql.toString());
			List list = query.list();
			ReportIn reportIn = (ReportIn) list.get(0);
			reportIn.setForseReportAgainFlag(Config.FORSE_REPORT_AGAIN_FLAG_1);
			ReportAgainSet reportAgainSet = new ReportAgainSet();
			reportAgainSet.setCause(reportInForm.getCause());
			reportAgainSet.setSetDate(reportInForm.getSetDateasDate());
			reportAgainSet.setRepInId(reportIn.getRepInId());
			session.save(reportIn);
			session.save(reportAgainSet);
			tx.commit();
			TranslatorUtil.copyPersistenceToVo(reportIn, reportInForm);
			session.close();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			result = false;

		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return result;
	}

	/**
	 * 根据条件查询报表数量
	 * 
	 * @param reportInForm
	 * @return
	 */
	public static int getRecordCount(ReportInForm reportInForm,
			Operator operator) {
		int count = 0;

		// 连接对象和会话对象初始化
		DBConn conn = null;
		Session session = null;

		// 查询条件HQL的生成
		StringBuffer hql = new StringBuffer("select count(*) from ReportIn ri");
		StringBuffer where = new StringBuffer("");

		if (reportInForm != null) {
			// 查找条件的判断,查找名称不可为空
			if (reportInForm.getOrgId() != null
					&& !reportInForm.getOrgId().equals(""))
				where.append((where.toString().equals("") ? "" : " or ")
						+ "mc.orgId like '%" + reportInForm.getOrgId() + "%'");
		}

		try { // List集合的操作
			// 初始化
			hql.append((where.toString().equals("") ? "" : " where ")
					+ where.toString());
			/**
			 * 加上机构和报表权限信息
			 * 
			 * @author 姚捷
			 */
			if (operator.getOrgPopedomSQL() != null
					&& !operator.getOrgPopedomSQL().equals(""))
				where.append((where.toString().equals("") ? "" : " and ")
						+ "ri.orgId in(" + operator.getOrgPopedomSQL() + ")");
			if (operator.getChildRepPodedomSQL() != null
					&& !operator.getChildRepPodedomSQL().equals(""))
				where.append((where.toString().equals("") ? "" : " and ")
						+ "ri.MChildReport.comp_id.childRepId in("
						+ operator.getChildRepPodedomSQL() + ")");

			// conn对象的实例化
			conn = new DBConn();
			// 打开连接开始会话
			session = conn.openSession();
			List list = session.find(hql.toString());
			if (list != null && list.size() == 1) {
				count = ((Integer) list.get(0)).intValue();
			}

		} catch (HibernateException he) {
			log.printStackTrace(he);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			// 如果连接存在，则断开，结束会话，返回
			if (conn != null)
				conn.closeSession();
		}
		return count;
	}

	/**
	 * 疑是oracle语法(upper)  需要修改 卞以刚 2011-12-21
	 * 已使用hibernate 
	 * 报表审签的查找总记录数
	 * 
	 * @param reportInForm
	 * @param operator
	 * @return int 总记录数
	 */
	public static int getRecordCountOfmanual(ReportInForm reportInForm,
			Operator operator) {
		int count = 0;
		DBConn conn = null;
		Session session = null;

		if (reportInForm == null || operator == null)
			return count;

		try {
			// 查询条件HQL的生成
			StringBuffer hql = new StringBuffer(
					"select count(*) from ReportIn ri WHERE "
							+ "ri.times=(select max(r.times) from ReportIn r where r.MChildReport.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and "
							+ "r.MChildReport.comp_id.versionId=ri.MChildReport.comp_id.versionId and r.MDataRgType.dataRangeId=ri.MDataRgType.dataRangeId and r.orgId=ri.orgId and "
							+ "r.MCurr.curId=ri.MCurr.curId and r.year=ri.year and r.term=ri.term and r.checkFlag=ri.checkFlag)");

			/** 查询报表状态为未审签报表 */
			StringBuffer where = new StringBuffer(" and ri.checkFlag="
					+ com.fitech.net.config.Config.CHECK_FLAG_UNCHECK);

			/** 添加报表编号查询条件（忽略大小写模糊查询） */
			/**疑是oracle语法(upper)  需要修改 卞以刚 2011-12-21**/
			if (reportInForm.getChildRepId() != null
					&& !reportInForm.getChildRepId().equals("")) {
				where
						.append(" and upper(ri.MChildReport.comp_id.childRepId) like upper('%"
								+ reportInForm.getChildRepId().trim() + "%')");
			}
			/** 添加报表名称查询条件（模糊查询） */
			if (reportInForm.getRepName() != null
					&& !reportInForm.getRepName().equals("")) {
				where.append(" and ri.repName like '%"
						+ reportInForm.getRepName().trim() + "%'");
			}
			/** 添加模板类型（全部/法人/分支）查询条件 */
			if (reportInForm.getFrOrFzType() != null
					&& !reportInForm.getFrOrFzType().equals(
							Config.DEFAULT_VALUE)) {
				where.append(" and ri.MChildReport.frOrFzType='"
						+ reportInForm.getFrOrFzType() + "'");
			}
			/** 添加报送频度（月/季/半年/年）查询条件 */
			if (reportInForm.getRepFreqId() != null
					&& !String.valueOf(reportInForm.getRepFreqId()).equals(
							Config.DEFAULT_VALUE)) {
				where
						.append(" and ri.MDataRgType.dataRangeId in (select M.comp_id.dataRangeId from MActuRep M "
								+ "where M.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and M.comp_id.versionId=ri.MChildReport.comp_id.versionId "
								+ "and M.comp_id.repFreqId="
								+ reportInForm.getRepFreqId() + ")");
			}
			/** 添加日期（年份）查询条件 */
			if (reportInForm.getYear() != null) {
				where.append(" and ri.year=" + reportInForm.getYear());
			}
			/** 添加日期（月份）查询条件 */
			if (reportInForm.getTerm() != null) {
				where.append(" and ri.term=" + reportInForm.getTerm());
			}
			/** 添加机构查询条件 */
			if (!StringUtil.isEmpty(reportInForm.getOrgId())
					&& !reportInForm.getOrgId().equals(Config.DEFAULT_VALUE)) {
				where.append(" and ri.orgId='" + reportInForm.getOrgId().trim()
						+ "'");
			}

			/** 添加报表审核权限（超级用户不用添加）
			 * 已增加数据库判断 */
			if (operator.isSuperManager() == false) {
				if (operator.getChildRepCheckPodedom() == null
						|| operator.getChildRepCheckPodedom().equals(""))
					return count;
				if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
					where.append(" and ltrim(rtrim(ri.orgId)) || ri.MChildReport.comp_id.childRepId in("
								+ operator.getChildRepCheckPodedom() + ")");
				if(Config.DB_SERVER_TYPE.equals("sqlserver"))
					where.append(" and ltrim(rtrim(ri.orgId)) + ri.MChildReport.comp_id.childRepId in("
							+ operator.getChildRepCheckPodedom() + ")");
			}
			hql.append(where.toString());

			conn = new DBConn();
			session = conn.openSession();
			List list = session.find(hql.toString());
			if (list != null && list.size() > 0)
				count = ((Integer) list.get(0)).intValue();
		} catch (HibernateException he) {
			count = 0;
			log.printStackTrace(he);
		} catch (Exception e) {
			count = 0;
			log.printStackTrace(e);
		} finally {
			// 关闭数据库连接
			if (conn != null)
				conn.closeSession();
		}
		return count;
	}
	
	/**
	 * 疑是oracle语法(upper)  需要修改 卞以刚 2011-12-21
	 * 已使用hibernate 
	 * 报表审签的查找总记录数
	 * 
	 * @param reportInForm
	 * @param operator
	 * @return int 总记录数
	 */
	public static int getRecordCountFlag(ReportInForm reportInForm,
			Operator operator,String checkFlag) {
		
		System.out.println();
		int count = 0;
		DBConn conn = null;
		Session session = null;

		if (reportInForm == null || operator == null)
			return count;

		try {
			// 查询条件HQL的生成
			StringBuffer hql = new StringBuffer(
					"select count(*) from ReportIn ri WHERE "
							+ "ri.times=(select max(r.times) from ReportIn r where r.MChildReport.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and "
							+ "r.MChildReport.comp_id.versionId=ri.MChildReport.comp_id.versionId and r.MDataRgType.dataRangeId=ri.MDataRgType.dataRangeId and r.orgId=ri.orgId and "
							+ "r.MCurr.curId=ri.MCurr.curId and r.year=ri.year and r.term=ri.term and r.checkFlag=ri.checkFlag)");

			/** 查询报表状态为未审签报表 */
			StringBuffer where = new StringBuffer(" and ri.checkFlag in("+com.fitech.net.config.Config.CHECK_FLAG_UNCHECK+","+checkFlag+")");

			/** 添加报表编号查询条件（忽略大小写模糊查询） */
			/**疑是oracle语法(upper)  需要修改 卞以刚 2011-12-21**/
			if (reportInForm.getChildRepId() != null
					&& !reportInForm.getChildRepId().equals("")) {
				where
						.append(" and upper(ri.MChildReport.comp_id.childRepId) like upper('%"
								+ reportInForm.getChildRepId().trim() + "%')");
			}
			/** 添加报表名称查询条件（模糊查询） */
			if (reportInForm.getRepName() != null
					&& !reportInForm.getRepName().equals("")) {
				where.append(" and ri.repName like '%"
						+ reportInForm.getRepName().trim() + "%'");
			}
			/** 添加模板类型（全部/法人/分支）查询条件 */
			if (reportInForm.getFrOrFzType() != null
					&& !reportInForm.getFrOrFzType().equals(
							Config.DEFAULT_VALUE)) {
				where.append(" and ri.MChildReport.frOrFzType='"
						+ reportInForm.getFrOrFzType() + "'");
			}
			/** 添加报送频度（月/季/半年/年）查询条件 */
			if (reportInForm.getRepFreqId() != null
					&& !String.valueOf(reportInForm.getRepFreqId()).equals(
							Config.DEFAULT_VALUE)) {
				where
						.append(" and ri.MDataRgType.dataRangeId in (select M.comp_id.dataRangeId from MActuRep M "
								+ "where M.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and M.comp_id.versionId=ri.MChildReport.comp_id.versionId "
								+ "and M.comp_id.repFreqId="
								+ reportInForm.getRepFreqId() + ")");
			}
			/** 添加日期（年份）查询条件 */
			if (reportInForm.getYear() != null) {
				where.append(" and ri.year=" + reportInForm.getYear());
			}
			/** 添加日期（月份）查询条件 */
			if (reportInForm.getTerm() != null) {
				where.append(" and ri.term=" + reportInForm.getTerm());
			}
			/** 添加机构查询条件 */
			if (!StringUtil.isEmpty(reportInForm.getOrgId())
					&& !reportInForm.getOrgId().equals(Config.DEFAULT_VALUE)) {
				where.append(" and ri.orgId='" + reportInForm.getOrgId().trim()
						+ "'");
			}

			/** 添加报表审核权限（超级用户不用添加）
			 * 已增加数据库判断 */
			if (operator.isSuperManager() == false) {
				if (operator.getChildRepCheckPodedom() == null
						|| operator.getChildRepCheckPodedom().equals(""))
					return count;
				if(Config.DB_SERVER_TYPE.equals("oracle"))
					where.append(" and ltrim(rtrim(ri.orgId)) || ri.MChildReport.comp_id.childRepId in("
								+ operator.getChildRepCheckPodedom() + ")");
				if(Config.DB_SERVER_TYPE.equals("sqlserver"))
					where.append(" and ltrim(rtrim(ri.orgId)) + ri.MChildReport.comp_id.childRepId in("
							+ operator.getChildRepCheckPodedom() + ")");
			}
			hql.append(where.toString());
			conn = new DBConn();
			session = conn.openSession();
			List list = session.find(hql.toString());
			if (list != null && list.size() > 0)
				count = ((Integer) list.get(0)).intValue();
		} catch (HibernateException he) {
			count = 0;
			log.printStackTrace(he);
		} catch (Exception e) {
			count = 0;
			log.printStackTrace(e);
		} finally {
			// 关闭数据库连接
			if (conn != null)
				conn.closeSession();
		}
		return count;
	}

	/**
	 * 已使用hibernate  卞以刚 2011-12-21
	 * 报表审签的查找记录方法
	 * 
	 * @param reportInForm
	 *            页面FormBean
	 * @param offset
	 *            数据库查询起始位
	 * @param limit
	 *            查询记录数
	 * @param operator
	 *            当前登录用户
	 * @return List 查询报表结果集
	 */
	public static List selectOfManual(ReportInForm reportInForm, int offset,
			int limit, Operator operator) {
		List resList = null;
		DBConn conn = null;
		Session session = null;

		if (reportInForm == null || operator == null)
			return resList;

		try {
			// 查询条件HQL的生成
			StringBuffer hql = new StringBuffer(
					"from ReportIn ri WHERE "
							+ "ri.times=(select max(r.times) from ReportIn r where r.MChildReport.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and "
							+ "r.MChildReport.comp_id.versionId=ri.MChildReport.comp_id.versionId and r.MDataRgType.dataRangeId=ri.MDataRgType.dataRangeId and r.orgId=ri.orgId and "
							+ "r.MCurr.curId=ri.MCurr.curId and r.year=ri.year and r.term=ri.term and r.checkFlag=ri.checkFlag)");

			/** 查询报表状态为未审签报表 */
			StringBuffer where = new StringBuffer(" and ri.checkFlag="
					+ com.fitech.net.config.Config.CHECK_FLAG_UNCHECK);

			/** 添加报表编号查询条件（忽略大小写模糊查询） */
			if (reportInForm.getChildRepId() != null
					&& !reportInForm.getChildRepId().equals("")) {
				where
						.append(" and upper(ri.MChildReport.comp_id.childRepId) like upper('%"
								+ reportInForm.getChildRepId().trim() + "%')");
			}
			/** 添加报表名称查询条件（模糊查询） */
			if (reportInForm.getRepName() != null
					&& !reportInForm.getRepName().equals("")) {
				where.append(" and ri.repName like '%"
						+ reportInForm.getRepName().trim() + "%'");
			}
			/** 添加模板类型（全部/法人/分支）查询条件 */
			if (reportInForm.getFrOrFzType() != null
					&& !reportInForm.getFrOrFzType().equals(
							Config.DEFAULT_VALUE)) {
				where.append(" and ri.MChildReport.frOrFzType='"
						+ reportInForm.getFrOrFzType() + "'");
			}
			/** 添加报送频度（月/季/半年/年）查询条件 */
			if (reportInForm.getRepFreqId() != null
					&& !String.valueOf(reportInForm.getRepFreqId()).equals(
							Config.DEFAULT_VALUE)) {
				where
						.append(" and ri.MDataRgType.dataRangeId in (select M.comp_id.dataRangeId from MActuRep M "
								+ "where M.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and M.comp_id.versionId=ri.MChildReport.comp_id.versionId "
								+ "and M.comp_id.repFreqId="
								+ reportInForm.getRepFreqId() + ")");
			}
			/** 添加日期（年份）查询条件 */
			if (reportInForm.getYear() != null) {
				where.append(" and ri.year=" + reportInForm.getYear());
			}
			/** 添加日期（月份）查询条件 */
			if (reportInForm.getTerm() != null) {
				where.append(" and ri.term=" + reportInForm.getTerm());
			}
			/** 添加机构查询条件 */
			if (!StringUtil.isEmpty(reportInForm.getOrgId()) 
					&& !reportInForm.getOrgId().equals(Config.DEFAULT_VALUE)) {
				where.append(" and ri.orgId='" + reportInForm.getOrgId().trim()
						+ "'");
			}

			/** 添加报表审核权限（超级用户不用添加） */
			if (operator.isSuperManager() == false) {
				if (operator.getChildRepCheckPodedom() == null
						|| operator.getChildRepCheckPodedom().equals(""))
					return resList;
				if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
					where.append(" and ltrim(rtrim(ri.orgId)) || ri.MChildReport.comp_id.childRepId in("
								+ operator.getChildRepCheckPodedom() + ")");
				if(Config.DB_SERVER_TYPE.equals("sqlserver"))
					where.append(" and ltrim(rtrim(ri.orgId)) + ri.MChildReport.comp_id.childRepId in("
								+ operator.getChildRepCheckPodedom() + ")");
			}
			hql
					.append(where.toString()
							+ " order by ri.tblOuterValidateFlag,ri.year,ri.term,ri.orgId");

			conn = new DBConn();
			session = conn.openSession();
			Query query = session.createQuery(hql.toString());
			query.setFirstResult(offset).setMaxResults(limit);
			List list = query.list();

			if (list != null && list.size() > 0) {
				resList = new ArrayList();
				Aditing aditing = null;
				OrgNet orgNet = null;
				ReportIn reportInRecord = null;
				for (Iterator it = list.iterator(); it.hasNext();) {
					aditing = new Aditing();
					reportInRecord = (ReportIn) it.next();
					// 设置报表审核状态
					aditing.setCheckFlag(reportInRecord.getCheckFlag());
					// 设置表间校验结果
					if (reportInRecord.getTblOuterValidateFlag() != null)
						aditing.setTblOuterValidateFlag(reportInRecord
								.getTblOuterValidateFlag());
					// 设置报送机构名称
					/**已使用hibernate  卞以刚 2011-12-21**/
					orgNet = StrutsOrgNetDelegate.selectOne(reportInRecord
							.getOrgId());
					if (orgNet != null){
						aditing.setOrgName(orgNet.getOrgName());
						aditing.setOrgId(orgNet.getOrgId());
					}
					
					// 设置报表ID标识符
					aditing.setRepInId(reportInRecord.getRepInId());
				
					// 设置报表名称
					aditing.setRepName(reportInRecord.getRepName());
					// 设置报表报送日期
					aditing.setReportDate(reportInRecord.getReportDate());
					// 设置报表编号
					aditing.setChildRepId(reportInRecord.getMChildReport()
							.getComp_id().getChildRepId());
					// 设置报表版本号
					aditing.setVersionId(reportInRecord.getMChildReport()
							.getComp_id().getVersionId());
					// 设置异常变化标志
					aditing.setAbmormityChangeFlag(reportInRecord
							.getAbmormityChangeFlag());
					// 设置报表币种名称
					aditing.setCurrName(reportInRecord.getMCurr().getCurName());
					aditing.setCurId(reportInRecord.getMCurr().getCurId());
					
					/**已使用hibernate  卞以刚 2011-12-21**/
					MActuRep mActuRep = GetFreR(reportInRecord);
					if (mActuRep != null) {
						// 设置报送口径
						aditing.setDataRgTypeName(mActuRep.getMDataRgType()
								.getDataRgDesc());
						// 设置报送口径
						aditing.setDataRangeId(mActuRep.getMDataRgType()
								.getDataRangeId());
						// 设置报送频度
						aditing.setActuFreqName(mActuRep.getMRepFreq()
								.getRepFreqName());
						// 设置报送频度
						aditing.setActuFreqID(mActuRep.getMRepFreq()
								.getRepFreqId());
						if (aditing.getActuFreqID() != null) {
							// yyyy-mm-dd 根据日期确定该日期具体的期数日期
							String trueDate = com.fitech.gznx.common.DateUtil.getFreqDateLast(reportInForm.getDate(),
											aditing.getActuFreqID());
							aditing.setYear(Integer.valueOf(trueDate.substring(0, 4)));
							aditing.setTerm(Integer.valueOf(trueDate.substring(5, 7)));
							aditing.setDay(Integer.valueOf(trueDate.substring(8, 10)));
						}

					}
					resList.add(aditing);
				}
			}
		} catch (HibernateException he) {
			resList = null;
			log.printStackTrace(he);
		} catch (Exception e) {
			resList = null;
			log.printStackTrace(e);
		} finally {
			// 关闭数据库连接
			if (conn != null)
				conn.closeSession();
		}
		return resList;
	}
	
	/**
	 * 已使用hibernate  卞以刚 2011-12-21
	 * 报表审签的查找记录方法
	 * 
	 * @param reportInForm
	 *            页面FormBean
	 * @param offset
	 *            数据库查询起始位
	 * @param limit
	 *            查询记录数
	 * @param operator
	 *            当前登录用户
	 * @return List 查询报表结果集
	 */
	public static List selectOfFlag(ReportInForm reportInForm, int offset,
			int limit, Operator operator,String checkFlag) {
		List resList = null;
		DBConn conn = null;
		Session session = null;

		if (reportInForm == null || operator == null)
			return resList;

		try {
			// 查询条件HQL的生成
			StringBuffer hql = new StringBuffer(
					"from ReportIn ri WHERE "
							+ "ri.times=(select max(r.times) from ReportIn r where r.MChildReport.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and "
							+ "r.MChildReport.comp_id.versionId=ri.MChildReport.comp_id.versionId and r.MDataRgType.dataRangeId=ri.MDataRgType.dataRangeId and r.orgId=ri.orgId and "
							+ "r.MCurr.curId=ri.MCurr.curId and r.year=ri.year and r.term=ri.term and r.checkFlag=ri.checkFlag)");

			/** 查询报表状态为未审签报表 */
			StringBuffer where = new StringBuffer(" and ri.checkFlag in("+com.fitech.net.config.Config.CHECK_FLAG_UNCHECK+","+checkFlag+")");

			/** 添加报表编号查询条件（忽略大小写模糊查询） */
			if (reportInForm.getChildRepId() != null
					&& !reportInForm.getChildRepId().equals("")) {
				where
						.append(" and upper(ri.MChildReport.comp_id.childRepId) like upper('%"
								+ reportInForm.getChildRepId().trim() + "%')");
			}
			/** 添加报表名称查询条件（模糊查询） */
			if (reportInForm.getRepName() != null
					&& !reportInForm.getRepName().equals("")) {
				where.append(" and ri.repName like '%"
						+ reportInForm.getRepName().trim() + "%'");
			}
			/** 添加模板类型（全部/法人/分支）查询条件 */
			if (reportInForm.getFrOrFzType() != null
					&& !reportInForm.getFrOrFzType().equals(
							Config.DEFAULT_VALUE)) {
				where.append(" and ri.MChildReport.frOrFzType='"
						+ reportInForm.getFrOrFzType() + "'");
			}
			/** 添加报送频度（月/季/半年/年）查询条件 */
			if (reportInForm.getRepFreqId() != null
					&& !String.valueOf(reportInForm.getRepFreqId()).equals(
							Config.DEFAULT_VALUE)) {
				where
						.append(" and ri.MDataRgType.dataRangeId in (select M.comp_id.dataRangeId from MActuRep M "
								+ "where M.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and M.comp_id.versionId=ri.MChildReport.comp_id.versionId "
								+ "and M.comp_id.repFreqId="
								+ reportInForm.getRepFreqId() + ")");
			}
			/** 添加日期（年份）查询条件 */
			if (reportInForm.getYear() != null) {
				where.append(" and ri.year=" + reportInForm.getYear());
			}
			/** 添加日期（月份）查询条件 */
			if (reportInForm.getTerm() != null) {
				where.append(" and ri.term=" + reportInForm.getTerm());
			}
			/** 添加机构查询条件 */
			if (!StringUtil.isEmpty(reportInForm.getOrgId()) 
					&& !reportInForm.getOrgId().equals(Config.DEFAULT_VALUE)) {
				where.append(" and ri.orgId='" + reportInForm.getOrgId().trim()
						+ "'");
			}

			/** 添加报表审核权限（超级用户不用添加） */
			if (operator.isSuperManager() == false) {
				if (operator.getChildRepCheckPodedom() == null
						|| operator.getChildRepCheckPodedom().equals(""))
					return resList;
				if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
					where.append(" and ltrim(rtrim(ri.orgId)) || ri.MChildReport.comp_id.childRepId in("
								+ operator.getChildRepCheckPodedom() + ")");
				if(Config.DB_SERVER_TYPE.equals("sqlserver"))
					where.append(" and ltrim(rtrim(ri.orgId)) + ri.MChildReport.comp_id.childRepId in("
								+ operator.getChildRepCheckPodedom() + ")");
			}
			hql
					.append(where.toString()
							+ " order by ri.tblOuterValidateFlag,ri.year,ri.term,ri.orgId");

			conn = new DBConn();
			session = conn.openSession();
			Query query = session.createQuery(hql.toString());
			query.setFirstResult(offset).setMaxResults(limit);
			List list = query.list();

			if (list != null && list.size() > 0) {
				resList = new ArrayList();
				Aditing aditing = null;
				OrgNet orgNet = null;
				ReportIn reportInRecord = null;
				for (Iterator it = list.iterator(); it.hasNext();) {
					aditing = new Aditing();
					reportInRecord = (ReportIn) it.next();
					// 设置报表审核状态
					aditing.setCheckFlag(reportInRecord.getCheckFlag());
					// 设置表间校验结果
					if (reportInRecord.getTblOuterValidateFlag() != null)
						aditing.setTblOuterValidateFlag(reportInRecord
								.getTblOuterValidateFlag());
					// 设置报送机构名称
					/**已使用hibernate  卞以刚 2011-12-21**/
					orgNet = StrutsOrgNetDelegate.selectOne(reportInRecord
							.getOrgId());
					if (orgNet != null){
						aditing.setOrgName(orgNet.getOrgName());
						aditing.setOrgId(orgNet.getOrgId());
					}
					
					// 设置报表ID标识符
					aditing.setRepInId(reportInRecord.getRepInId());
				
					// 设置报表名称
					aditing.setRepName(reportInRecord.getRepName());
					// 设置报表报送日期
					aditing.setReportDate(reportInRecord.getReportDate());
					// 设置报表编号
					aditing.setChildRepId(reportInRecord.getMChildReport()
							.getComp_id().getChildRepId());
					// 设置报表版本号
					aditing.setVersionId(reportInRecord.getMChildReport()
							.getComp_id().getVersionId());
					// 设置异常变化标志
					aditing.setAbmormityChangeFlag(reportInRecord
							.getAbmormityChangeFlag());
					// 设置报表币种名称
					aditing.setCurrName(reportInRecord.getMCurr().getCurName());
					aditing.setCurId(reportInRecord.getMCurr().getCurId());
					
					/**已使用hibernate  卞以刚 2011-12-21**/
					MActuRep mActuRep = GetFreR(reportInRecord);
					if (mActuRep != null) {
						// 设置报送口径
						aditing.setDataRgTypeName(mActuRep.getMDataRgType()
								.getDataRgDesc());
						// 设置报送口径
						aditing.setDataRangeId(mActuRep.getMDataRgType()
								.getDataRangeId());
						// 设置报送频度
						aditing.setActuFreqName(mActuRep.getMRepFreq()
								.getRepFreqName());
						// 设置报送频度
						aditing.setActuFreqID(mActuRep.getMRepFreq()
								.getRepFreqId());
						if (aditing.getActuFreqID() != null) {
							// yyyy-mm-dd 根据日期确定该日期具体的期数日期
							String trueDate = com.fitech.gznx.common.DateUtil.getFreqDateLast(reportInForm.getDate(),
											aditing.getActuFreqID());
							aditing.setYear(Integer.valueOf(trueDate.substring(0, 4)));
							aditing.setTerm(Integer.valueOf(trueDate.substring(5, 7)));
							aditing.setDay(Integer.valueOf(trueDate.substring(8, 10)));
						}

					}
					resList.add(aditing);
				}
			}
		} catch (HibernateException he) {
			resList = null;
			log.printStackTrace(he);
		} catch (Exception e) {
			resList = null;
			log.printStackTrace(e);
		} finally {
			// 关闭数据库连接
			if (conn != null)
				conn.closeSession();
		}
		return resList;
	}

	/**
	 * 已使用hibernate 卞以刚 2011-12-21
	 * 需要进行全部校验的报表
	 * 
	 * @param reportInForm
	 *            报表FormBean
	 * @param operator
	 *            当前登录用户
	 * @return List 报表列表
	 */
	public static List selectOfManual(ReportInForm reportInForm,
			Operator operator) {
		List resList = null;
		DBConn conn = null;
		Session session = null;
		if (reportInForm == null || operator == null)
			return resList;

		try {
			StringBuffer hql = new StringBuffer(
					"from ReportIn ri where "
							+ "ri.times=(select max(r.times) from ReportIn r where r.MChildReport.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and "
							+ "r.MChildReport.comp_id.versionId=ri.MChildReport.comp_id.versionId and r.MDataRgType.dataRangeId=ri.MDataRgType.dataRangeId and r.orgId=ri.orgId and "
							+ "r.MCurr.curId=ri.MCurr.curId and r.year=ri.year and r.term=ri.term and r.checkFlag=ri.checkFlag)");

			/** 查询报表状态为未审核报表 */
			StringBuffer where = new StringBuffer(" and ri.checkFlag="
					+ com.fitech.net.config.Config.CHECK_FLAG_UNCHECK);

			/** 添加报表编号查询条件（忽略大小写模糊查询） */
			if (reportInForm.getChildRepId() != null
					&& !reportInForm.getChildRepId().equals("")) {
				where
						.append(" and upper(ri.MChildReport.comp_id.childRepId) like upper('%"
								+ reportInForm.getChildRepId().trim() + "%')");
			}
			/** 添加报表名称查询条件（模糊查询） */
			if (reportInForm.getRepName() != null
					&& !reportInForm.getRepName().equals("")) {
				where.append(" and ri.repName like '%"
						+ reportInForm.getRepName().trim() + "%'");
			}
			/** 添加模板类型（全部/法人/分支）查询条件 */
			if (reportInForm.getFrOrFzType() != null
					&& !reportInForm.getFrOrFzType().equals(
							Config.DEFAULT_VALUE)) {
				where.append(" and ri.MChildReport.frOrFzType='"
						+ reportInForm.getFrOrFzType() + "'");
			}
			/** 添加报送频度（月/季/半年/年）查询条件 */
			if (reportInForm.getRepFreqId() != null
					&& !String.valueOf(reportInForm.getRepFreqId()).equals(
							Config.DEFAULT_VALUE)) {
				where
						.append(" and ri.MDataRgType.dataRangeId in (select M.comp_id.dataRangeId from MActuRep M "
								+ "where M.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and M.comp_id.versionId=ri.MChildReport.comp_id.versionId "
								+ "and M.comp_id.repFreqId="
								+ reportInForm.getRepFreqId() + ")");
			}
			/** 添加日期（年份）查询条件 */
			if (reportInForm.getYear() != null) {
				where.append(" and ri.year=" + reportInForm.getYear());
			}
			/** 添加日期（月份）查询条件 */
			if (reportInForm.getTerm() != null) {
				where.append(" and ri.term=" + reportInForm.getTerm());
			}
			/** 添加机构查询条件 */
			if (reportInForm.getOrgId() != null
					&& !reportInForm.getOrgId().equals(Config.DEFAULT_VALUE)) {
				where.append(" and ri.orgId='" + reportInForm.getOrgId().trim()
						+ "'");
			}

			/** 添加报表审核权限（超级用户不用添加） 
			 * 已增加数据库判断*/
			if (operator.isSuperManager() == false) {
				if (operator.getChildRepCheckPodedom() == null
						|| operator.getChildRepCheckPodedom().equals(""))
					return resList;
				if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
					where.append(" and ltrim(rtrim(ri.orgId)) || ri.MChildReport.comp_id.childRepId in("
								+ operator.getChildRepCheckPodedom() + ")");
				if(Config.DB_SERVER_TYPE.equals("sqlserver"))
					where.append(" and ltrim(rtrim(ri.orgId)) + ri.MChildReport.comp_id.childRepId in("
								+ operator.getChildRepCheckPodedom() + ")");
			}
			hql.append(where.toString());

			conn = new DBConn();
			session = conn.openSession();
			Query query = session.createQuery(hql.toString());

			List list = query.list();
			if (list != null && list.size() > 0) {
				resList = new ArrayList();
				ReportIn reportInRecord = null;

				for (Iterator it = list.iterator(); it.hasNext();) {
					reportInRecord = (ReportIn) it.next();
					resList.add(reportInRecord.getRepInId());
				}
			}
		} catch (HibernateException he) {
			resList = null;
			log.printStackTrace(he);
		} catch (Exception e) {
			resList = null;
			log.printStackTrace(e);
		} finally {
			// 关闭数据库连接
			if (conn != null)
				conn.closeSession();
		}
		return resList;
	}

	/**
	 * 已使用hibernate 卞以刚 2011-12-21
	 * 得到频度
	 * 
	 * @param reportIn
	 *            报表对象
	 * @return MActuRep 报送频度
	 */
	public static MActuRep GetFreR(ReportIn reportIn) {
		DBConn conn = null;
		Session session = null;
		String data_range = "";

		if (reportIn.getMDataRgType() != null
				&& reportIn.getMDataRgType().getDataRangeId() != null)
			data_range = " and M.MDataRgType.dataRangeId="
					+ reportIn.getMDataRgType().getDataRangeId();

		StringBuffer sql = new StringBuffer("from MActuRep M where 1=1 ");
		String version = reportIn.getMChildReport().getComp_id().getVersionId();
		String RepId = reportIn.getMChildReport().getComp_id().getChildRepId();

		if (RepId != null && !RepId.equals(""))
			sql
					.append(" and M.MChildReport.comp_id.childRepId='" + RepId
							+ "'");
		if (version != null && !version.equals(""))
			sql.append(" and M.MChildReport.comp_id.versionId='" + version
					+ "'");
		sql.append(data_range);

		try {
			conn = new DBConn();
			session = conn.openSession();

			Query query = session.createQuery(sql.toString());
			List list = query.list();
			if (list != null) {
				for (Iterator it = list.iterator(); it.hasNext();) {
					MActuRep mActuRep = (MActuRep) it.next();
					return mActuRep;
				}
			}
		} catch (Exception ex) {
			log.printStackTrace(ex);
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return null;
	}

	/**
	 * 已使用hibernate 卞以刚 2011-12-21
	 * 得到频度
	 * 
	 * @param reportIn
	 *            报表对象
	 * @return MActuRep 报送频度
	 */
	public static MActuRep GetFreR(String childRepId, String versionId,
			String dataRangeId) {
		DBConn conn = null;
		Session session = null;

		try {
			StringBuffer sql = new StringBuffer("from MActuRep M where 1=1");

			if (childRepId != null && !childRepId.equals(""))
				sql.append(" and M.MChildReport.comp_id.childRepId='"
						+ childRepId + "'");
			if (versionId != null && !versionId.equals(""))
				sql.append(" and M.MChildReport.comp_id.versionId='"
						+ versionId + "'");
			if (dataRangeId != null && !dataRangeId.equals(""))
				sql.append(" and M.MDataRgType.dataRangeId=" + dataRangeId);

			conn = new DBConn();
			session = conn.openSession();

			Query query = session.createQuery(sql.toString());
			List list = query.list();
			if (list != null) {
				for (Iterator it = list.iterator(); it.hasNext();) {
					MActuRep mActuRep = (MActuRep) it.next();
					return mActuRep;
				}
			}
		} catch (Exception ex) {
			log.printStackTrace(ex);
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return null;
	}

	/**
	 * 已使用hibernate 卞以刚 2011-12-21
	 * 影响对象：MActuRep MChildReport
	 * 得到频度
	 * 
	 * @param in
	 * @return
	 */
	public static boolean isExistDataRange(String dataRangeId,
			String childRepId, String versionId) {
		DBConn conn = null;
		Session session = null;
		boolean bool = false;

		StringBuffer sql = new StringBuffer(
				"from MActuRep M where M.MChildReport.comp_id.childRepId='"
						+ childRepId
						+ "' and M.MChildReport.comp_id.versionId='"
						+ versionId + "' and M.MDataRgType.dataRangeId="
						+ dataRangeId + "");

		try {
			conn = new DBConn();
			session = conn.openSession();
			Query query = session.createQuery(sql.toString());
			List list = query.list();

			if (list != null && list.size() > 0)
				bool = true;
		} catch (Exception ex) {
			bool = false;
			ex.printStackTrace();
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return bool;
	}

	/***************************************************************************
	 * 已使用hibernate 卞以刚 2011-12-21
	 * 影响对象：MActuRep MChildReport MDataRgType MRepFreq
	 * 判断报表频度是否已经存在
	 * 
	 * @param dataRangeId
	 * @param childRepId
	 * @param versionId
	 * @param term
	 * @return
	 */
	public static boolean isExistDataRange(Integer dataRangeId,
			String childRepId, String versionId, Integer term) {
		DBConn conn = null;
		Session session = null;
		boolean bool = false;

		if (dataRangeId == null || childRepId == null || versionId == null
				|| term == null)
			return bool;
		try {
			String rep_freq = "";
			if (term.intValue() == 12)
				rep_freq = "'月','季','半年','年'";
			else if (term.intValue() == 6)
				rep_freq = "'月','季','半年'";
			else if (term.intValue() == 3 || term.intValue() == 9)
				rep_freq = "'月','季'";
			else
				rep_freq = "'月'";
			//添加日频度
			rep_freq += ",'日'";
			String hql = "from MActuRep M where M.MChildReport.comp_id.childRepId='"
					+ childRepId
					+ "' "
					+ "and M.MChildReport.comp_id.versionId='"
					+ versionId
					+ "' and M.MDataRgType.dataRangeId="
					+ dataRangeId
					+ " "
					+ "and M.MRepFreq.repFreqName in (" + rep_freq + ")";

			conn = new DBConn();
			session = conn.openSession();
			Query query = session.createQuery(hql);
			List list = query.list();

			if (list != null && list.size() > 0)
				bool = true;
		} catch (Exception ex) {
			bool = false;
			ex.printStackTrace();
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return bool;
	}
	/*
	 * add by wmm at 20130724
	 * 用来判断report_in_info 表里是否存在数据
	 * 
	 */
	public static boolean isExistDataReportInInfo(
			String childRepId,String templateId,String versionId,String year,String term,String day,String orgId,String curId,String repFreqId) {
		if(templateId==null||versionId==null||term==null){
			return false;
		}
		DBConn conn = null;
		Session session = null;
		boolean bool = false;
		StringBuffer sql=null;
		
		
		try {
			conn = new DBConn();
			session = conn.openSession();
			if(childRepId==null){
				sql=new StringBuffer("select ar.repInId from ReportIn ar,MActuRep mr  where mr.comp_id.childRepId=ar.MChildReport.comp_id.childRepId and mr.comp_id.versionId=ar.MChildReport.comp_id.versionId and mr.comp_id.repFreqId="+repFreqId+" and ar.MRepRange.comp_id.childRepId='"+templateId+"' and ar.MRepRange.comp_id.versionId='"+versionId+"'  and ar.year="+Long.valueOf(year)+" and ar.term="+Long.valueOf(term)+" and ar.orgId='"+orgId+"'  and ar.MCurr.curId="+curId) ;
				Query query = session.createQuery(sql.toString());
				List list = query.list();
				for (int i = 0; list!=null&&i <list.size(); i++) {
					childRepId=((Integer)list.get(i)).toString();
				}
			}
			if(childRepId!=null&&!"".equals(childRepId)){
				
				sql = new StringBuffer("from ReportInInfo ri where ri.comp_id.repInId='"+childRepId+"'");
				Query query = session.createQuery(sql.toString());
				List list = query.list();
				
				if (list != null && list.size() > 0)
					bool = true;
			}
		} catch (Exception ex) {
			bool = false;
			ex.printStackTrace();
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return bool;
	}
	/*
	 * add by wmm at 20130724
	 * 用来判断report_in_info 表里是否存在数据
	 * 
	 */
	public static String getReportInInfo(
			String childRepId,String templateId,String versionId,String year,String term,String day,String orgId,String curId,String repFreqId) {
		if(templateId==null||versionId==null||term==null){
			return null;
		}
		DBConn conn = null;
		Session session = null;
		boolean bool = false;
		StringBuffer sql=null;
		
		
		try {
			conn = new DBConn();
			session = conn.openSession();
			if(childRepId==null){
				sql=new StringBuffer("select ar.repInId from ReportIn ar, MActuRep mr  where mr.comp_id.childRepId=ar.MChildReport.comp_id.childRepId and mr.comp_id.versionId=ar.MChildReport.comp_id.versionId and mr.comp_id.repFreqId="+repFreqId+" and  ar.MRepRange.comp_id.childRepId='"+templateId+"' and ar.MRepRange.comp_id.versionId='"+versionId+"'  and ar.year="+Long.valueOf(year)+" and ar.term="+Long.valueOf(term)+" and ar.orgId='"+orgId+"' and ar.MCurr.curId="+curId );
				Query query = session.createQuery(sql.toString());
				List list = query.list();
				for (int i = 0; list!=null&&i <list.size(); i++) {
					childRepId=((Integer)list.get(i)).toString();
				}
			}
			
		} catch (Exception ex) {
			
			ex.printStackTrace();
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return childRepId;
	}
	/*
	 * add by wmm at 20130724
	 * 用来判断af_pbocreportdata 表里是否存在数据
	 * 
	 */
	public static boolean isExistDataAfPbocReport(
			String repId,String templateId,String versionId,String year,String term,String day,String orgId,String repFreqId) {
		if(templateId==null||versionId==null||term==null){
			return false;
		}
		DBConn conn = null;
		Session session = null;
		boolean bool = false;
		StringBuffer sql=null;
		
		
		try {
			conn = new DBConn();
			session = conn.openSession();
			if(repId==null){
				sql=new StringBuffer("select ar.repId from AfReport ar where ar.repFreqId="+repFreqId+"  and ar.templateId='"+templateId+"' and ar.versionId='"+versionId+"'  and ar.year="+Long.valueOf(year)+" and ar.term="+Long.valueOf(term)+" and ar.day="+Long.valueOf(day)+" and ar.orgId='"+orgId+"'");
				Query query = session.createQuery(sql.toString());
				List list = query.list();
				for (int i = 0; list!=null&&i <list.size(); i++) {
					repId=((Long)list.get(i)).toString();
				}
			}
			if(repId!=null&&!"".equals(repId)){
				 sql = new StringBuffer("from AfPbocreportdata ap where ap.id.repId='"+repId+"'");
				Query query = session.createQuery(sql.toString());
				List list = query.list();
				
				if (list != null && list.size() > 0)
					bool = true;
			}
		} catch (Exception ex) {
			bool = false;
			ex.printStackTrace();
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return bool;
	}
	/*
	 * add by wmm at 20130724
	 * 用来判断af_pbocreportdata 表里是否存在数据
	 * 
	 */
	public static boolean isExistDataAfOtherReport(
			String repId,String templateId,String versionId,String year,String term,String day,String orgId,String repFreqId) {
		if(templateId==null||versionId==null||term==null){
			return false;
		}
		DBConn conn = null;
		Session session = null;
		boolean bool = false;
		StringBuffer sql=null;
		
		
		try {
			conn = new DBConn();
			session = conn.openSession();
			if(repId==null){
				sql=new StringBuffer("select ar.repId from AfReport ar where ar.repFreqId="+repFreqId+"  and ar.templateId='"+templateId+"' and ar.versionId='"+versionId+"'  and ar.year="+Long.valueOf(year)+" and ar.term="+Long.valueOf(term)+" and ar.day="+Long.valueOf(day)+" and ar.orgId='"+orgId+"'");
				Query query = session.createQuery(sql.toString());
				List list = query.list();
				for (int i = 0; list!=null&&i <list.size(); i++) {
					repId=((Long)list.get(i)).toString();
				}
			}
			if(repId!=null&&!"".equals(repId)){
				sql = new StringBuffer("from AfOtherreportdata ap where ap.id.repId='"+repId+"'");
				Query query = session.createQuery(sql.toString());
				List list = query.list();
				
				if (list != null && list.size() > 0)
					bool = true;
			}
		} catch (Exception ex) {
			bool = false;
			ex.printStackTrace();
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return bool;
	}
	/*
	 * add by wmm at 20130724
	 * 用来判断af_pbocreportdata 表里是否存在数据
	 * 
	 */
	public static String getAfPbocReportRepId(
			String repId,String templateId,String versionId,String year,String term,String day,String orgId,String repFreqId) {
		if(templateId==null||versionId==null||term==null){
			return null;
		}
		DBConn conn = null;
		Session session = null;
		boolean bool = false;
		StringBuffer sql=null;
		
		
		try {
			conn = new DBConn();
			session = conn.openSession();
			if(repId==null){
				sql=new StringBuffer("select ar.repId from AfReport ar where ar.repFreqId="+repFreqId+"  and ar.templateId='"+templateId+"' and ar.versionId='"+versionId+"'  and ar.year="+Long.valueOf(year)+" and ar.term="+Long.valueOf(term)+" and ar.day="+Long.valueOf(day)+"  and ar.orgId='"+orgId+"'");
				Query query = session.createQuery(sql.toString());
				List list = query.list();
				for (int i = 0; list!=null&&i <list.size(); i++) {
					repId=((Long)list.get(i)).toString();
				}
			}
		
		} catch (Exception ex) {
			
			ex.printStackTrace();
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return repId;
	}


	/***************************************************************************
	 * 已使用hibernate 卞以刚 2011-12-21
	 * 查询报表信息
	 * 
	 * @param reportInForm
	 * @param offset
	 * @param limit
	 * @return
	 * @throws Exception
	 */

	public static List select(ReportInForm reportInForm, int offset, int limit,
			Operator operator) throws Exception {
		List retvals = null;
		DBConn conn = null;
		Session session = null;

		StringBuffer hql = new StringBuffer("from ReportIn ri");
		StringBuffer where = new StringBuffer("");

		if (reportInForm != null) {
			// 查找条件的判断,查找名称不可为空
			if (reportInForm.getOrgId() != null
					&& !reportInForm.getOrgId().equals(""))
				where.append((where.toString().equals("") ? "" : " and ")
						+ "ri.orgId like like '%" + reportInForm.getOrgId()
						+ "%'");
			/**
			 * 加上机构和报表权限信息
			 * 
			 * @author 姚捷
			 */
			if (operator.getOrgPopedomSQL() != null
					&& !operator.getOrgPopedomSQL().equals(""))
				where.append((where.toString().equals("") ? "" : " and ")
						+ "ri.orgId in(" + operator.getOrgPopedomSQL() + ")");
			if (operator.getChildRepPodedomSQL() != null
					&& !operator.getChildRepPodedomSQL().equals(""))
				where.append((where.toString().equals("") ? "" : " and ")
						+ "ri.MChildReport.comp_id.childRepId in("
						+ operator.getChildRepPodedomSQL() + ")");
		}

		try { // List集合的操作
			// 初始化
			hql.append((where.toString().equals("") ? "" : " where ")
					+ where.toString());

			// conn对象的实例化
			conn = new DBConn();
			// 打开连接开始会话
			session = conn.openSession();
			// 添加集合至Session
			// List list=session.find(hql.toString());
			Query query = session.createQuery(hql.toString());
			query.setFirstResult(offset).setMaxResults(limit);
			List list = query.list();

			if (list != null) {
				retvals = new ArrayList();
				// 循环读取数据库符合条件记录
				for (Iterator it = list.iterator(); it.hasNext();) {
					ReportInForm reportInFormTemp = new ReportInForm();
					ReportIn reportInPersistence = (ReportIn) it.next();
					TranslatorUtil.copyPersistenceToVo(reportInPersistence,
							reportInFormTemp);
					retvals.add(reportInFormTemp);
				}
			}
		} catch (HibernateException he) {
			retvals = null;
			log.printStackTrace(he);
		} catch (Exception e) {
			retvals = null;
			log.printStackTrace(e);
		} finally {
			// 如果连接存在，则断开，结束会话，返回
			if (conn != null)
				conn.closeSession();
		}
		return retvals;
	}

	/**
	 * 已使用hibernate 卞以刚 2011-12-21
	 * 通过报表id查找它的一些基本信息 ****未完成****************************************8
	 * 
	 * @param reportInId
	 * @return
	 */
	public String getReportInfo(Integer reportInId) {

		String result = "";
		DBConn conn = null;
		Session session = null;

		conn = new DBConn();

		try {
			session = conn.beginTransaction();
			ReportIn repIn = (ReportIn) session
					.load(ReportIn.class, reportInId);
			if (repIn != null) {
				result += repIn.getOrgId();
				session.update(repIn);
				session.flush();
				result = "";
			}
		} catch (Exception e) {
			result = "";
			log.printStackTrace(e);
		} finally {
			if (conn != null)
				conn.closeSession();
		}

		return result;
	}

	/**
	 * 已使用hibernate 卞以刚 2011-12-21
	 * 重报管理设定查询
	 * 
	 * @author曹发根
	 * @return 返回记录数量
	 */
	public static int getReportAgainSettingCount(ReportInForm form,
			Operator operator) {
		int result = 0;

		if (form == null)
			return result;

		StringBuffer hql = new StringBuffer("select count(*) from ReportIn a");
		StringBuffer where = new StringBuffer(" where a.checkFlag="
				+ Config.CHECK_FLAG_NO + " and a.forseReportAgainFlag!="
				+ Config.FORSE_REPORT_AGAIN_FLAG_1);

		String[] repname = null;
		if (!form.getRepName().equals("0"))
			repname = form.getRepName().split(",");
		if (form.getOrgName() != null && !form.getOrgName().equals("")) {
			String orgids = com.cbrc.org.adapter.StrutsMOrgDelegate
					.getOrgid(form.getOrgName());
			if (!orgids.equals(""))
				where.append((where.toString().equals("") ? "" : " and ")
						+ " a.orgId in(" + orgids + ")");
		}
		if (repname != null && !(repname.length < 2)) {
			where.append((where.toString().equals("") ? "" : " and ")
					+ " a.MChildReport.comp_id.childRepId ='" + repname[0]
					+ "'");
			where
					.append((where.toString().equals("") ? "" : " and ")
							+ " a.MChildReport.comp_id.versionId ='"
							+ repname[1] + "'");
		}
		if (null != form.getChildRepId() && !form.getChildRepId().equals("0")
				&& !form.getChildRepId().equals(""))
			where.append((where.toString().equals("") ? "" : " and ")
					+ " a.orgId in("
					+ com.cbrc.org.adapter.StrutsMOrgDelegate.getOrgids(form
							.getChildRepId()) + ")");
		if (null != form.getStartDate() && !form.getStartDate().equals(""))
			where
					.append((where.toString().equals("") ? "" : " and ")
							+ " day(a.reportDate) >=day('"
							+ form.getStartDate() + "')");
		if (null != form.getEndDate() && !form.getEndDate().equals(""))
			where.append((where.toString().equals("") ? "" : " and ")
					+ "day(a.reportDate) <=day('" + form.getEndDate() + "')");

		/**
		 * 加上机构和报表权限信息
		 * 
		 * @author 姚捷
		 */
		if (operator == null)
			return 0;

		/** 机构权限 */
		if (operator.getOrgPopedomSQL() != null
				&& !operator.getOrgPopedomSQL().equals("")) {
			where.append((where.toString().equals("") ? "" : " and ")
					+ " a.orgId in(" + operator.getOrgPopedomSQL() + ")");
		} else {
			return 0;
		}
		/** 报表权限 */
		if (operator.getChildRepPodedomSQL() != null
				&& !operator.getChildRepPodedomSQL().equals("")) {
			where.append((where.toString().equals("") ? "" : " and ")
					+ " a.MChildReport.comp_id.childRepId in("
					+ operator.getChildRepPodedomSQL() + ")");
		} else {
			return 0;
		}

		if (!where.toString().equals(""))
			hql.append(where.toString());

		DBConn conn = null;
		Session session = null;

		try {
			conn = new DBConn();
			session = conn.openSession();
			Query query = session.createQuery(hql.toString());
			List list = query.list();

			if (list != null) {
				result = ((Integer) list.get(0)).intValue();
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (conn != null)
				conn.closeSession();
		}

		return result;
	}

	/**
	 * 已使用hibernate 卞以刚 2011-12-21
	 * 报表重报设置查询
	 * 
	 * @author曹发根
	 * @return 返回记录
	 */
	public static List getReportAgainSettingRecord(ReportInForm form,
			int offset, int limit, Operator operator) {
		ArrayList result = new ArrayList();
		if (null == form)
			return result;
		StringBuffer hql = new StringBuffer("from ReportIn a ");
		StringBuffer where = new StringBuffer(" where a.checkFlag="
				+ Config.CHECK_FLAG_NO + " and a.forseReportAgainFlag!="
				+ Config.FORSE_REPORT_AGAIN_FLAG_1);
		String[] repname = null;
		if (!form.getRepName().equals("0"))
			repname = form.getRepName().split(",");
		if (form.getOrgName() != null && !form.getOrgName().equals("")) {
			String orgids = com.cbrc.org.adapter.StrutsMOrgDelegate
					.getOrgid(form.getOrgName());
			where.append((where.toString().equals("") ? "" : " and ")
					+ " a.orgId in(" + orgids + ")");
		}
		if (null != repname && !(repname.length < 2)) {
			where.append((where.toString().equals("") ? "" : " and ")
					+ " a.MChildReport.comp_id.childRepId ='" + repname[0]
					+ "'");
			where
					.append((where.toString().equals("") ? "" : " and ")
							+ " a.MChildReport.comp_id.versionId ='"
							+ repname[1] + "'");
		}
		if (null != form.getChildRepId() && !form.getChildRepId().equals("0")
				&& !form.getChildRepId().equals(""))
			where.append((where.toString().equals("") ? "" : " and ")
					+ " a.orgId in("
					+ com.cbrc.org.adapter.StrutsMOrgDelegate.getOrgids(form
							.getChildRepId()) + ")");
		if (null != form.getStartDate() && !form.getStartDate().equals(""))
			where
					.append((where.toString().equals("") ? "" : " and ")
							+ "day( a.reportDate) >=day('"
							+ form.getStartDate() + "')");
		if (null != form.getEndDate() && !form.getEndDate().equals(""))
			where.append((where.toString().equals("") ? "" : " and ")
					+ "day( a.reportDate )<=day('" + form.getEndDate() + "')");

		/**
		 * 加上机构和报表权限信息
		 * 
		 * @author 姚捷
		 */
		if (operator == null)
			return result;
		/** 机构权限 */
		if (operator.getOrgPopedomSQL() != null
				&& !operator.getOrgPopedomSQL().equals(""))
			where.append((where.toString().equals("") ? "" : " and ")
					+ " a.orgId in(" + operator.getOrgPopedomSQL() + ")");
		else
			return result;
		/** 报表权限 */
		if (operator.getChildRepPodedomSQL() != null
				&& !operator.getChildRepPodedomSQL().equals(""))
			where.append((where.toString().equals("") ? "" : " and ")
					+ " a.MChildReport.comp_id.childRepId in("
					+ operator.getChildRepPodedomSQL() + ")");
		else
			return result;

		where.append(" order by a.reportDate desc ");

		if (!where.toString().equals(""))
			hql.append(where.toString());

		DBConn conn = null;
		Session session = null;
		try {
			conn = new DBConn();
			session = conn.openSession();
			Query query = session.createQuery(hql.toString());
			query.setFirstResult(offset).setMaxResults(limit);
			List list = query.list();

			if (list != null)
				for (int i = 0; i < list.size(); i++) {
					ReportIn reportin = (ReportIn) list.get(i);
					ReportInForm reportInForm = new ReportInForm();
					TranslatorUtil.copyPersistenceToVo(reportin, reportInForm);
					/**已使用hibernate 卞以刚 2011-12-21**/
					if (StrutsMOrgDelegate.selectOne(reportInForm.getOrgId()) != null)
						/**已使用hibernate 卞以刚 2011-12-21**/
						reportInForm.setOrgName(StrutsMOrgDelegate.selectOne(
								reportInForm.getOrgId()).getOrg_name());
					result.add(reportInForm);
				}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return result;
	}

	/**
	 * 已使用hibernate 卞以刚 2011-12-21
	 * 新建重报报表设定
	 * 
	 * @author 曹发根
	 * @return true 新建成功；false新建失败！
	 * @throws HibernateException
	 */
	public static boolean newForseReportAgainSetting(ReportInForm reportInForm)
			throws HibernateException {
		boolean result = false;
		if (reportInForm == null)
			return result;
		DBConn conn = null;
		Session session = null;
		Transaction tx = null;
		try {
			conn = new DBConn();
			session = conn.openSession();
			tx = session.beginTransaction();
			ReportIn reportIn = (ReportIn) session.load(ReportIn.class,
					reportInForm.getRepInId());
			if (reportIn == null)
				return result;
			reportIn.setForseReportAgainFlag(Config.FORSE_REPORT_AGAIN_FLAG_1);
			reportIn.setCheckFlag(new Short(Config.CHECK_FLAG_NO.shortValue()));
			ReportAgainSet reportAgainSet = new ReportAgainSet();
			reportAgainSet.setCause(reportInForm.getCause());
			reportAgainSet.setSetDate(new Date());
			reportAgainSet.setRepInId(reportIn.getRepInId());
			session.save(reportIn);
			session.save(reportAgainSet);
			tx.commit();
			TranslatorUtil.copyPersistenceToVo(reportIn, reportInForm);
			session.close();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			result = false;

		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return result;
	}

	/**
	 * JDBC技术 需要修改 卞以刚 2011-12-21
	 * 重报 更新reportin的checkflag，和 薛书峰
	 */
	public static boolean updateForseReportAgain(ReportInForm reportInForm) {
		if (reportInForm == null || reportInForm.getRepInId() == null)
			return false;

		boolean result = false;
		DBConn conn = null;
		Connection _conn = null;
		Statement stmt = null;
		try {
			conn = new DBConn();
			_conn = conn.openSession().connection();
			stmt = _conn.createStatement();

			if (_conn.getAutoCommit() == true)
				_conn.setAutoCommit(false);

			stmt
					.addBatch("update report_in set CHECK_FLAG=-1,TBL_OUTER_VALIDATE_FLAG=null,TBL_INNER_VALIDATE_FLAG=null,FORSE_REPORT_AGAIN_FLAG="
							+ Config.FORSE_REPORT_AGAIN_FLAG_1
							+ " where rep_in_id=" + reportInForm.getRepInId());
			/**已增加数据库判断*/
			if(Config.DB_SERVER_TYPE.equals("oracle"))
				stmt.addBatch("insert into report_again_set(RAS_ID,CAUSE,SET_DATE,REP_IN_ID) values(seq_report_again_set.nextVal,'"
								+ reportInForm.getCause()
								+ "',sysdate,"
								+ reportInForm.getRepInId() + ")");
			if(Config.DB_SERVER_TYPE.equals("sqlserver"))
				stmt.addBatch("insert into report_again_set(CAUSE,SET_DATE,REP_IN_ID) values('"
						+ reportInForm.getCause()
						+ "',sysdate,"
						+ reportInForm.getRepInId() + ")");
			if(Config.DB_SERVER_TYPE.equals("db2")){
				stmt.addBatch("insert into report_again_set(RAS_ID,CAUSE,SET_DATE,REP_IN_ID) values(seq_report_again_set.nextVal,'"
						+ reportInForm.getCause()
						+ "',date(current timestamp),"
						+ reportInForm.getRepInId() + ")");
			}
			stmt.executeBatch();
			result = true;

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			try {
				if (result == true)
					_conn.commit();
				else
					_conn.rollback();
				_conn.setAutoCommit(true);
				if (stmt != null)
					stmt.close();
				if (_conn != null)
					_conn.close();
				if (conn != null)
					conn.closeSession();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;

	}

	/**
	 * 已使用hibernate 卞以刚 2011-12-21
	 * 根据repInId查找laterReportDay的记录 唐磊
	 */
	public static int selectLaterReportDay(Integer repInId) {
		int laterRepDay = 0;
		DBConn conn = null;
		Session session = null;
		List resList = null;

		if (repInId != null) {
			try {
				conn = new DBConn();
				session = conn.openSession();

				String hql = "from ReportIn ri where 1=1";
				hql += " and ri.laterReportDay=" + repInId;

				Query query = session.createQuery(hql.toString());
				resList = query.list();

				if (resList != null && resList.size() > 0) {
					laterRepDay = ((ReportIn) resList.get(0))
							.getLaterReportDay().intValue();
				}
			} catch (HibernateException he) {
				log.printStackTrace(he);
			} catch (Exception e) {
				log.printStackTrace(e);
			} finally {
				if (conn != null)
					conn.closeSession();
			}
		}
		return laterRepDay;
	}

	/**
	 * 已使用hibernate 卞以刚 2011-12-21
	 * insert report_in表
	 * 
	 * @param repIn
	 * @return
	 */
	public static ReportIn insertReportIn(ReportIn repIn) {
		DBConn conn = new DBConn();
		Session session = conn.beginTransaction();
		try {
			session.save(repIn);
			conn.endTransaction(true);
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			conn.endTransaction(false);
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.closeSession();
			}
		}
		return repIn;
	}

	/**
	 * 已使用hibernate 卞以刚 2011-12-21
	 * 获得当前机构已上报的报表
	 * 
	 * @author 王东伟
	 */
	public static List getHasCollectedRepsByCurOrg(MActuRepForm mar,
			String curOrgId, int year, int mon) {
		DBConn conn = new DBConn();
		Session session = conn.openSession();
		StringBuffer hql = new StringBuffer(
				"from ReportIn ri where "
						+ "ri.times=(select max(r.times) from ReportIn r where r.MChildReport.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and "
						+ "r.MChildReport.comp_id.versionId=ri.MChildReport.comp_id.versionId and r.MDataRgType.dataRangeId=ri.MDataRgType.dataRangeId and r.orgId=ri.orgId and "
						+ "r.MCurr.curId=ri.MCurr.curId and r.year=ri.year and r.term=ri.term and r.checkFlag not in ("
						+ com.fitech.net.config.Config.CHECK_FLAG_AFTERJY + ","
						+ com.fitech.net.config.Config.CHECK_FLAG_UNREPORT
						+ "))");

		hql.append(" and ri.MChildReport.comp_id.childRepId='"
				+ mar.getChildRepId() + "'");
		hql.append(" and ri.MChildReport.comp_id.versionId='"
				+ mar.getVersionId() + "'");
		hql.append(" and ri.MDataRgType.dataRangeId="
				+ mar.getDataRangeId().intValue() + "");
		hql.append(" and ri.orgId='" + curOrgId + "'");
		hql.append(" and ri.year=" + year).append(" and ri.term=" + mon);
		hql.append(" and ri.checkFlag in("
				+ com.fitech.net.config.Config.CHECK_FLAG_PASS + ","
				+ com.fitech.net.config.Config.CHECK_FLAG_UNCHECK + ")");
		List list = null;
		try {
			list = session.find(hql.toString());
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return list;
	}

	/**
	 * 已使用hibernate 卞以刚 2011-12-21
	 * 过滤已经报送过的汇总报表，加上币种的条件
	 * 
	 * @author jcm
	 */
	public static List getHasCollectedReps(MActuRepForm mar, String curOrgId,
			int year, int mon, Integer curId) {
		DBConn conn = new DBConn();
		Session session = conn.openSession();
		StringBuffer hql = new StringBuffer(
				"from ReportIn ri where "
						+ "ri.times=(select max(r.times) from ReportIn r where r.MChildReport.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and "
						+ "r.MChildReport.comp_id.versionId=ri.MChildReport.comp_id.versionId and r.MDataRgType.dataRangeId=ri.MDataRgType.dataRangeId and r.orgId=ri.orgId and "
						+ "r.MCurr.curId=ri.MCurr.curId and r.year=ri.year and r.term=ri.term and r.checkFlag not in ("
						+ com.fitech.net.config.Config.CHECK_FLAG_AFTERJY + ","
						+ com.fitech.net.config.Config.CHECK_FLAG_UNREPORT
						+ "))");

		hql.append(" and ri.MChildReport.comp_id.childRepId='"
				+ mar.getChildRepId() + "'");
		hql.append(" and ri.MChildReport.comp_id.versionId='"
				+ mar.getVersionId() + "'");
		hql.append(" and ri.MDataRgType.dataRangeId="
				+ mar.getDataRangeId().intValue() + "");
		hql.append(" and ri.orgId='" + curOrgId + "'");
		hql.append(" and ri.year=" + year).append(" and ri.term=" + mon);
		hql.append(" and ri.MCurr.curId=" + curId);
		hql.append(" and ri.checkFlag in("
				+ com.fitech.net.config.Config.CHECK_FLAG_PASS + ","
				+ com.fitech.net.config.Config.CHECK_FLAG_UNCHECK + ")");
		List list = null;
		try {
			list = session.find(hql.toString());
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return list;
	}

	/**
	 * 已使用hibernate 卞以刚 2011-12-21
	 * 根据条件查询汇总好的数据
	 * 
	 * @param reportInForm
	 * @return
	 */
	public static int getOnLineRecordCount(ReportInForm reportInForm) {
		int count = 0;

		// 连接对象和会话对象初始化
		DBConn conn = null;
		Session session = null;

		// 查询条件HQL的生成
		if (reportInForm == null || reportInForm.getOrgId() == null)
			return count;

		String hql = "select count(*) from ReportIn ri where ri.times=(select max(r.times) from ReportIn r where r.MChildReport.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and "
				+ "r.MChildReport.comp_id.versionId=ri.MChildReport.comp_id.versionId and r.MDataRgType.dataRangeId=ri.MDataRgType.dataRangeId and r.orgId=ri.orgId and "
				+ "r.MCurr.curId=ri.MCurr.curId and r.year=ri.year and r.term=ri.term and r._package is null and r.checkFlag="
				+ com.fitech.net.config.Config.CHECK_FLAG_UNREPORT
				+ ") and ri._package is null and ri.checkFlag="
				+ com.fitech.net.config.Config.CHECK_FLAG_UNREPORT;

		hql += " and ri.orgId='" + reportInForm.getOrgId() + "'";

		if (reportInForm.getRepName() != null
				&& !reportInForm.getRepName().equals("")) {
			hql += " and ri.repName like '%" + reportInForm.getRepName() + "%'";
		}
		if (reportInForm.getYear() != null
				&& !reportInForm.getYear().toString().equals("")) {
			hql += " and ri.year=" + reportInForm.getYear();
		}
		if (reportInForm.getTerm() != null
				&& !reportInForm.getTerm().toString().equals("")) {
			hql += " and ri.term=" + reportInForm.getTerm();
		}
		try { // List集合的操作
			conn = new DBConn();
			// 打开连接开始会话
			session = conn.openSession();
			List list = session.find(hql.toString());
			if (list != null && list.size() > 0) {
				count = ((Integer) list.get(0)).intValue();
			}

		} catch (HibernateException he) {
			log.printStackTrace(he);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			// 如果连接存在，则断开，结束会话，返回
			if (conn != null)
				conn.closeSession();
		}
		return count;
	}

	/**
	 * 已使用hibernate 卞以刚 2011-12-21
	 * 根据条件查询设定汇总方式的汇总数据
	 * 
	 * @param reportInForm
	 * @return
	 */
	public static int getOtherCollectRecordCount(ReportInForm reportInForm) {
		int count = 0;

		// 连接对象和会话对象初始化
		DBConn conn = null;
		Session session = null;

		// 查询条件HQL的生成
		if (reportInForm == null || reportInForm.getOrgId() == null)
			return count;

		String hql = "select count(*) from ReportIn ri where ri.times=(select max(r.times) from ReportIn r where r.MChildReport.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and "
				+ "r.MChildReport.comp_id.versionId=ri.MChildReport.comp_id.versionId and r.MDataRgType.dataRangeId=ri.MDataRgType.dataRangeId and r.orgId=ri.orgId and "
				+ "r.MCurr.curId=ri.MCurr.curId and r.year=ri.year and r.term=ri.term and r._package=ri._package and r._package is not null and r.checkFlag="
				+ com.fitech.net.config.Config.CHECK_FLAG_UNREPORT
				+ ") and ri._package is not null and ri.checkFlag="
				+ com.fitech.net.config.Config.CHECK_FLAG_UNREPORT;

		hql += " and ri.orgId='" + reportInForm.getOrgId() + "'";

		if (reportInForm.getRepName() != null
				&& !reportInForm.getRepName().equals("")) {
			hql += " and ri.repName like '%" + reportInForm.getRepName() + "%'";
		}
		if (reportInForm.getYear() != null
				&& !reportInForm.getYear().toString().equals("")) {
			hql += " and ri.year=" + reportInForm.getYear();
		}
		if (reportInForm.getTerm() != null
				&& !reportInForm.getTerm().toString().equals("")) {
			hql += " and ri.term=" + reportInForm.getTerm();
		}
		try { // List集合的操作
			conn = new DBConn();
			// 打开连接开始会话
			session = conn.openSession();
			List list = session.find(hql.toString());
			if (list != null && list.size() > 0) {
				count = ((Integer) list.get(0)).intValue();
			}

		} catch (HibernateException he) {
			log.printStackTrace(he);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			// 如果连接存在，则断开，结束会话，返回
			if (conn != null)
				conn.closeSession();
		}
		return count;
	}

	/**
	 * 已使用hibernate 卞以刚 2011-12-21
	 * 根据条件查询汇总好的数据
	 * 
	 * @param reportInForm
	 * @return
	 */
	public static List getOnLineRecords(ReportInForm reportInForm, int offset,
			int limit) {
		List resultList = null;

		// 连接对象和会话对象初始化
		DBConn conn = null;
		Session session = null;

		// 查询条件HQL的生成
		if (reportInForm == null || reportInForm.getOrgId() == null)
			return resultList;

		String hql = "from ReportIn ri where ri.times=(select max(r.times) from ReportIn r where r.MChildReport.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and "
				+ "r.MChildReport.comp_id.versionId=ri.MChildReport.comp_id.versionId and r.MDataRgType.dataRangeId=ri.MDataRgType.dataRangeId and r.orgId=ri.orgId and "
				+ "r.MCurr.curId=ri.MCurr.curId and r.year=ri.year and r.term=ri.term and r._package is null and r.checkFlag="
				+ com.fitech.net.config.Config.CHECK_FLAG_UNREPORT
				+ ") and ri._package is null and ri.checkFlag="
				+ com.fitech.net.config.Config.CHECK_FLAG_UNREPORT;

		hql += " and ri.orgId='" + reportInForm.getOrgId() + "'";

		if (reportInForm.getRepName() != null
				&& !reportInForm.getRepName().equals("")) {
			hql += " and ri.repName like '%" + reportInForm.getRepName() + "%'";
		}
		if (reportInForm.getYear() != null
				&& !reportInForm.getYear().toString().equals("")) {
			hql += " and ri.year=" + reportInForm.getYear();
		}
		if (reportInForm.getTerm() != null
				&& !reportInForm.getTerm().toString().equals("")) {
			hql += " and ri.term=" + reportInForm.getTerm();
		}
		hql += " order by ri.MChildReport.comp_id.childRepId";

		try { // List集合的操作
			conn = new DBConn();
			// 打开连接开始会话
			session = conn.openSession();
			Query query = session.createQuery(hql);
			query.setFirstResult(offset);
			query.setMaxResults(limit);
			List list = query.list();

			if (list != null && list.size() > 0) {
				resultList = new ArrayList();
				// 循环读取数据库符合条件记录

				for (Iterator it = list.iterator(); it.hasNext();) {
					// 实例化一个中间类Aditing接受所有页面所需的字段名
					Aditing aditing = new Aditing();
					// 生成持久化对象
					ReportIn reportInPersistence = (ReportIn) it.next();

					String sql = "from ReportIn ri where ri.times=(select max(r.times) from ReportIn r where r.MChildReport.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and "
							+ "r.MChildReport.comp_id.versionId=ri.MChildReport.comp_id.versionId and r.MDataRgType.dataRangeId=ri.MDataRgType.dataRangeId and r.orgId=ri.orgId and "
							+ "r.MCurr.curId=ri.MCurr.curId and r.year=ri.year and r.term=ri.term and r.checkFlag!="
							+ com.fitech.net.config.Config.CHECK_FLAG_UNREPORT
							+ ") and ri.orgId='"
							+ reportInPersistence.getOrgId()
							+ "' and ri.year="
							+ reportInPersistence.getYear()
							+ " and ri.term="
							+ reportInPersistence.getTerm()
							+ " and ri.MChildReport.comp_id.childRepId='"
							+ reportInPersistence.getMChildReport()
									.getComp_id().getChildRepId()
							+ "' and ri.MChildReport.comp_id.versionId='"
							+ reportInPersistence.getMChildReport()
									.getComp_id().getVersionId()
							+ "' and ri.MCurr.curId="
							+ reportInPersistence.getMCurr().getCurId()
							+ " and ri.MDataRgType.dataRangeId="
							+ reportInPersistence.getMDataRgType()
									.getDataRangeId()
							+ " and ri.checkFlag!="
							+ com.fitech.net.config.Config.CHECK_FLAG_UNREPORT;

					List subList = session.find(sql);
					if (subList != null && subList.size() > 0) {
						ReportIn reportIn = (ReportIn) subList.get(0);
						aditing.setCheckFlag(reportIn.getCheckFlag());
					} else
						aditing.setCheckFlag(new Short((short) (3)));
					// 将查找到的报表id传进aditing对象
					aditing.setRepInId(reportInPersistence.getRepInId());
					aditing.setYear(reportInPersistence.getYear());
					aditing.setTerm(reportInPersistence.getTerm());
					// 将查找的报表名称set进adtiting对象
					if (!reportInPersistence.getRepName().equals(
							reportInPersistence.getMChildReport()
									.getReportName()))
						aditing.setRepName(reportInPersistence.getRepName()
								+ "-"
								+ reportInPersistence.getMChildReport()
										.getReportName());
					else
						aditing.setRepName(reportInPersistence.getRepName());
					// 将查找的报表日期set进aditing对象
					aditing.setReportDate(reportInPersistence.getReportDate());
					// 将查找的子报表ID放进aditing对象
					aditing.setChildRepId(reportInPersistence.getMChildReport()
							.getComp_id().getChildRepId());
					// 将查找的版本号set进aditing对象
					aditing.setVersionId(reportInPersistence.getMChildReport()
							.getComp_id().getVersionId());
					// 将包含页面所需的所有记录的adtiing对象add进arraylist对象中返回action
					aditing.setAbmormityChangeFlag(reportInPersistence
							.getAbmormityChangeFlag());
					aditing.setDataRgTypeName(reportInPersistence
							.getMDataRgType().getDataRgDesc());
					aditing
							.setIsCollected(reportInPersistence.getRepOutId() != null ? reportInPersistence
									.getRepOutId()
									: new Integer(0));
					aditing.setCurrName(reportInPersistence.getMCurr()
							.getCurName());

					MActuRep mActuRep = GetFreR(reportInPersistence);
					if (mActuRep != null) {
						// 报送频度
						aditing.setActuFreqName(mActuRep.getMRepFreq()
								.getRepFreqName());
					}
					resultList.add(aditing);
				}
			}
		} catch (HibernateException he) {
			log.printStackTrace(he);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			// 如果连接存在，则断开，结束会话，返回
			if (conn != null)
				conn.closeSession();
		}
		return resultList;
	}

	/**
	 * 已使用hibernate 卞以刚 2011-12-21
	 * 根据条件查询以汇总方式生成的汇总好的数据
	 * 
	 * @param reportInForm
	 * @return
	 */
	public static List getOtherCollectRecords(ReportInForm reportInForm,
			int offset, int limit) {
		List resultList = null;

		// 连接对象和会话对象初始化
		DBConn conn = null;
		Session session = null;

		// 查询条件HQL的生成
		if (reportInForm == null || reportInForm.getOrgId() == null)
			return resultList;

		String hql = "from ReportIn ri where ri.times=(select max(r.times) from ReportIn r where r.MChildReport.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and "
				+ "r.MChildReport.comp_id.versionId=ri.MChildReport.comp_id.versionId and r.MDataRgType.dataRangeId=ri.MDataRgType.dataRangeId and r.orgId=ri.orgId and "
				+ "r.MCurr.curId=ri.MCurr.curId and r.year=ri.year and r.term=ri.term and r._package=ri._package and r._package is not null and r.checkFlag="
				+ com.fitech.net.config.Config.CHECK_FLAG_UNREPORT
				+ ") and ri._package is not null and ri.checkFlag="
				+ com.fitech.net.config.Config.CHECK_FLAG_UNREPORT;

		hql += " and ri.orgId='" + reportInForm.getOrgId() + "'";

		if (reportInForm.getRepName() != null
				&& !reportInForm.getRepName().equals("")) {
			hql += " and ri.repName like '%" + reportInForm.getRepName() + "%'";
		}
		if (reportInForm.getYear() != null
				&& !reportInForm.getYear().toString().equals("")) {
			hql += " and ri.year=" + reportInForm.getYear();
		}
		if (reportInForm.getTerm() != null
				&& !reportInForm.getTerm().toString().equals("")) {
			hql += " and ri.term=" + reportInForm.getTerm();
		}
		hql += " order by ri.MChildReport.comp_id.childRepId";

		try { // List集合的操作
			conn = new DBConn();
			// 打开连接开始会话
			session = conn.openSession();
			Query query = session.createQuery(hql);
			query.setFirstResult(offset);
			query.setMaxResults(limit);
			List list = query.list();

			if (list != null && list.size() > 0) {
				resultList = new ArrayList();
				// 循环读取数据库符合条件记录

				for (Iterator it = list.iterator(); it.hasNext();) {
					// 实例化一个中间类Aditing接受所有页面所需的字段名
					Aditing aditing = new Aditing();
					// 生成持久化对象
					ReportIn reportInPersistence = (ReportIn) it.next();

					// 将查找到的报表id传进aditing对象
					aditing.setRepInId(reportInPersistence.getRepInId());
					aditing.setYear(reportInPersistence.getYear());
					aditing.setTerm(reportInPersistence.getTerm());
					// 将查找的报表名称set进adtiting对象
					if (!reportInPersistence.getRepName().equals(
							reportInPersistence.getMChildReport()
									.getReportName()))
						aditing.setRepName(reportInPersistence.getRepName()
								+ "-"
								+ reportInPersistence.getMChildReport()
										.getReportName());
					else
						aditing.setRepName(reportInPersistence.getRepName());
					// 将查找的报表日期set进aditing对象
					aditing.setReportDate(reportInPersistence.getReportDate());
					// 将查找的子报表ID放进aditing对象
					aditing.setChildRepId(reportInPersistence.getMChildReport()
							.getComp_id().getChildRepId());
					// 将查找的版本号set进aditing对象
					aditing.setVersionId(reportInPersistence.getMChildReport()
							.getComp_id().getVersionId());
					// 将包含页面所需的所有记录的adtiing对象add进arraylist对象中返回action
					aditing.setAbmormityChangeFlag(reportInPersistence
							.getAbmormityChangeFlag());
					aditing.setDataRgTypeName(reportInPersistence
							.getMDataRgType().getDataRgDesc());
					aditing.setCurrName(reportInPersistence.getMCurr()
							.getCurName());
					/**已使用hibernate 卞以刚 2011-12-21**/
					CollectTypeForm collectTypeForm = StrutsCollectTypeDelegate
							.selectCollectType(reportInPersistence
									.get_package());
					if (collectTypeForm != null)
						aditing
								.setCollectType(collectTypeForm
										.getCollectName());
					/**已使用hibernate 卞以刚 2011-12-21**/
					MActuRep mActuRep = GetFreR(reportInPersistence);
					if (mActuRep != null) {
						// 报送频度
						aditing.setActuFreqName(mActuRep.getMRepFreq()
								.getRepFreqName());
					}
					resultList.add(aditing);
				}
			}
		} catch (HibernateException he) {
			log.printStackTrace(he);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			// 如果连接存在，则断开，结束会话，返回
			if (conn != null)
				conn.closeSession();
		}
		return resultList;
	}

	/**
	 * 已使用hibernate 卞以刚 2011-12-21
	 * 根据条件查询以汇总方式生成的汇总好的数据
	 * 
	 * @param reportInForm
	 * @return
	 */
	public static List getOtherCollectRecords(ReportInForm reportInForm) {
		List resultList = null;

		// 连接对象和会话对象初始化
		DBConn conn = null;
		Session session = null;

		// 查询条件HQL的生成
		if (reportInForm == null || reportInForm.getOrgId() == null)
			return resultList;

		String hql = "from ReportIn ri where ri.times=(select max(r.times) from ReportIn r where r.MChildReport.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and "
				+ "r.MChildReport.comp_id.versionId=ri.MChildReport.comp_id.versionId and r.MDataRgType.dataRangeId=ri.MDataRgType.dataRangeId and r.orgId=ri.orgId and "
				+ "r.MCurr.curId=ri.MCurr.curId and r.year=ri.year and r.term=ri.term and r._package=ri._package and r._package is not null and r.checkFlag="
				+ com.fitech.net.config.Config.CHECK_FLAG_UNREPORT
				+ ") and ri._package is not null and ri.checkFlag="
				+ com.fitech.net.config.Config.CHECK_FLAG_UNREPORT;

		hql += " and ri.orgId='" + reportInForm.getOrgId() + "'";

		if (reportInForm.getRepName() != null
				&& !reportInForm.getRepName().equals("")) {
			hql += " and ri.repName like '%" + reportInForm.getRepName() + "%'";
		}
		if (reportInForm.getYear() != null
				&& !reportInForm.getYear().toString().equals("")) {
			hql += " and ri.year=" + reportInForm.getYear();
		}
		if (reportInForm.getTerm() != null
				&& !reportInForm.getTerm().toString().equals("")) {
			hql += " and ri.term=" + reportInForm.getTerm();
		}
		hql += " order by ri.MChildReport.comp_id.childRepId";

		try { // List集合的操作
			conn = new DBConn();
			// 打开连接开始会话
			session = conn.openSession();
			Query query = session.createQuery(hql);
			List list = query.list();

			if (list != null && list.size() > 0) {
				resultList = new ArrayList();
				// 循环读取数据库符合条件记录

				for (Iterator it = list.iterator(); it.hasNext();) {
					ReportInForm reportInFormTemp = new ReportInForm();
					ReportIn reportInPersistence = (ReportIn) it.next();

					TranslatorUtil.copyPersistenceToVo(reportInPersistence,
							reportInFormTemp);
					resultList.add(reportInFormTemp);
				}
			}
		} catch (HibernateException he) {
			log.printStackTrace(he);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			// 如果连接存在，则断开，结束会话，返回
			if (conn != null)
				conn.closeSession();
		}
		return resultList;
	}

	/**
	 * 已使用hibernate 卞以刚 2011-12-21
	 * 根据条件查询汇总好可以上报的数据
	 * 
	 * @param reportInForm
	 * @param operator
	 * @return
	 */
	public static List getOnLineYBRecords(ReportInForm reportInForm,
			Operator operator) {
		List resultList = null;

		// 连接对象和会话对象初始化
		DBConn conn = null;
		Session session = null;

		// 查询条件HQL的生成
		if (reportInForm == null || reportInForm.getOrgId() == null)
			return resultList;

		String hql = "from ReportIn ri where ri.times=(select max(r.times) from ReportIn r where r.MChildReport.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and "
				+ "r.MChildReport.comp_id.versionId=ri.MChildReport.comp_id.versionId and r.MDataRgType.dataRangeId=ri.MDataRgType.dataRangeId and r.orgId=ri.orgId and "
				+ "r.MCurr.curId=ri.MCurr.curId and r.year=ri.year and r.term=ri.term and r.checkFlag="
				+ com.fitech.net.config.Config.CHECK_FLAG_UNREPORT
				+ ") and ri.checkFlag="
				+ com.fitech.net.config.Config.CHECK_FLAG_UNREPORT;

		hql += " and ri.orgId='" + reportInForm.getOrgId() + "'";

		if (reportInForm.getRepName() != null
				&& !reportInForm.getRepName().equals("")) {
			hql += " and ri.repName like '%" + reportInForm.getRepName() + "%'";
		}
		if (reportInForm.getYear() != null
				&& !reportInForm.getYear().toString().equals("")) {
			hql += " and ri.year=" + reportInForm.getYear();
		}
		if (reportInForm.getTerm() != null
				&& !reportInForm.getTerm().toString().equals("")) {
			hql += " and ri.term=" + reportInForm.getTerm();
		}
		/** 加上报表报送权限 */
		if (operator == null)
			return resultList;

		if (operator.isSuperManager() == false) {
			if (operator.getChildRepReportPopedom() == null
					|| operator.getChildRepReportPopedom().equals(""))
				return resultList;
			/**已增加数据库判断*/
			if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
				hql += " and '" + reportInForm.getOrgId()
					+ "'||ri.MChildReport.comp_id.childRepId in("
					+ operator.getChildRepReportPopedom() + ")";
			if(Config.DB_SERVER_TYPE.equals("sqlserver"))
				hql += " and '" + reportInForm.getOrgId()
					+ "'+ri.MChildReport.comp_id.childRepId in("
					+ operator.getChildRepReportPopedom() + ")";
		}
		hql += " order by ri.MChildReport.comp_id.childRepId";

		try { // List集合的操作
			conn = new DBConn();
			// 打开连接开始会话
			session = conn.openSession();
			Query query = session.createQuery(hql);
			List list = query.list();

			if (list != null && list.size() > 0) {
				resultList = new ArrayList();
				// 循环读取数据库符合条件记录

				for (Iterator it = list.iterator(); it.hasNext();) {
					// 实例化一个中间类Aditing接受所有页面所需的字段名
					Aditing aditing = new Aditing();
					// 生成持久化对象
					ReportIn reportInPersistence = (ReportIn) it.next();

					String sql = "from ReportIn ri where ri.times=(select max(r.times) from ReportIn r where r.MChildReport.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and "
							+ "r.MChildReport.comp_id.versionId=ri.MChildReport.comp_id.versionId and r.MDataRgType.dataRangeId=ri.MDataRgType.dataRangeId and r.orgId=ri.orgId and "
							+ "r.MCurr.curId=ri.MCurr.curId and r.year=ri.year and r.term=ri.term and r.checkFlag!="
							+ com.fitech.net.config.Config.CHECK_FLAG_UNREPORT
							+ ") and ri.orgId='"
							+ reportInPersistence.getOrgId()
							+ "' and ri.year="
							+ reportInPersistence.getYear()
							+ " and ri.term="
							+ reportInPersistence.getTerm()
							+ " and ri.MChildReport.comp_id.childRepId='"
							+ reportInPersistence.getMChildReport()
									.getComp_id().getChildRepId()
							+ "' and ri.MChildReport.comp_id.versionId='"
							+ reportInPersistence.getMChildReport()
									.getComp_id().getVersionId()
							+ "' and ri.MCurr.curId="
							+ reportInPersistence.getMCurr().getCurId()
							+ " and ri.MDataRgType.dataRangeId="
							+ reportInPersistence.getMDataRgType()
									.getDataRangeId()
							+ " and ri.checkFlag!="
							+ com.fitech.net.config.Config.CHECK_FLAG_UNREPORT;

					List subList = session.find(sql);
					if (subList != null && subList.size() > 0) {
						ReportIn reportIn = (ReportIn) subList.get(0);
						if (reportIn.getCheckFlag().shortValue() == com.fitech.net.config.Config.CHECK_FLAG_PASS
								.shortValue()
								|| reportIn.getCheckFlag().shortValue() == com.fitech.net.config.Config.CHECK_FLAG_UNCHECK
										.shortValue())
							continue;
						aditing.setCheckFlag(reportIn.getCheckFlag());
					} else
						aditing.setCheckFlag(new Short((short) (3)));
					// 将查找到的报表id传进aditing对象
					aditing.setRepInId(reportInPersistence.getRepInId());
					aditing.setYear(reportInPersistence.getYear());
					aditing.setTerm(reportInPersistence.getTerm());
					// 将查找的报表名称set进adtiting对象
					if (!reportInPersistence.getRepName().equals(
							reportInPersistence.getMChildReport()
									.getReportName()))
						aditing.setRepName(reportInPersistence.getRepName()
								+ "-"
								+ reportInPersistence.getMChildReport()
										.getReportName());
					else
						aditing.setRepName(reportInPersistence.getRepName());
					// 将查找的报表日期set进aditing对象
					aditing.setReportDate(reportInPersistence.getReportDate());
					// 将查找的子报表ID放进aditing对象
					aditing.setChildRepId(reportInPersistence.getMChildReport()
							.getComp_id().getChildRepId());
					// 将查找的版本号set进aditing对象
					aditing.setVersionId(reportInPersistence.getMChildReport()
							.getComp_id().getVersionId());
					// 将包含页面所需的所有记录的adtiing对象add进arraylist对象中返回action
					aditing.setAbmormityChangeFlag(reportInPersistence
							.getAbmormityChangeFlag());
					aditing.setDataRgTypeName(reportInPersistence
							.getMDataRgType().getDataRgDesc());
					aditing.setCurrName(reportInPersistence.getMCurr()
							.getCurName());
					aditing.setCurId(reportInPersistence.getMCurr().getCurId());
					aditing
							.setIsCollected(reportInPersistence.getRepOutId() != null ? reportInPersistence
									.getRepOutId()
									: new Integer(0));

					MActuRep mActuRep = GetFreR(reportInPersistence);
					if (mActuRep != null) {
						// 报送频度
						aditing.setActuFreqName(mActuRep.getMRepFreq()
								.getRepFreqName());
					}
					resultList.add(aditing);
				}
			}
		} catch (HibernateException he) {
			log.printStackTrace(he);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			// 如果连接存在，则断开，结束会话，返回
			if (conn != null)
				conn.closeSession();
		}
		return resultList;
	}

	/**
	 * 已使用hibernate 卞以刚 2011-12-21
	 * @author jcm 对实际报表表单修改
	 * 
	 * @param reportIn
	 * @param date
	 * @throws Exception
	 */
	public static boolean updateReportIn(ReportIn reportIn, String dateString)
			throws Exception {

		boolean bool = false;
		DBConn dBConn = null;
		Session session = null;
		Date date = null;

		if (dateString.equals(""))
			dateString = null;
		if (dateString != null) {
			try {
				date = DateUtil
						.getDateByString(dateString, DateUtil.NORMALDATE);
			} catch (Exception e) {
				date = null;
			}
		}
		try {
			dBConn = new DBConn();
			session = dBConn.beginTransaction();
			reportIn
					.setCheckFlag(com.fitech.net.config.Config.CHECK_FLAG_UNCHECK);
			reportIn.setReportDate(date);
			session.update(reportIn);
			dBConn.endTransaction(true);
			bool = true;
		} catch (Exception e) {
			bool = false;
			dBConn.endTransaction(false);
		} finally {
			if (session != null) {
				dBConn.closeSession();
			}
		}
		return bool;
	}

	/**
	 * 
	 * 已使用hibernate 卞以刚 2011-12-21
	 * 获取实际数据报表信息
	 * 
	 * @author jcm
	 * 
	 * @param repInId
	 *            Integer 实际数据报表ID
	 * @return ReportInForm
	 */
	public static com.cbrc.smis.hibernate.ReportIn getReportIn2(Integer repInId) {
		ReportIn reportIn = null;

		if (repInId == null)
			return null;

		DBConn conn = null;

		try {
			conn = new DBConn();
			reportIn = (ReportIn) conn.openSession().get(ReportIn.class,
					repInId);
		} catch (HibernateException he) {
			log.printStackTrace(he);
			reportIn = null;
		} catch (Exception e) {
			log.printStackTrace(e);
			reportIn = null;
		} finally {
			if (conn != null)
				conn.closeSession();
		}

		return reportIn;
	}

	/**
	 * 已使用hibernate 卞以刚 2011-12-21
	 * 查询可以生成数据文件的报表
	 * 
	 * @return 返回记录
	 */
	public static List getDataRecords(String orgId) {
		ArrayList result = new ArrayList();
		if (orgId == null)
			return result;

		String hql = "from ReportIn ri where ri.orgId ='" + orgId
				+ "' and ri.checkFlag ="
				+ com.fitech.net.config.Config.CHECK_FLAG_PASS;

		DBConn conn = null;
		Session session = null;
		try {
			conn = new DBConn();
			session = conn.openSession();
			Query query = session.createQuery(hql.toString());
			List list = query.list();

			if (list != null)
				for (int i = 0; i < list.size(); i++) {
					ReportIn reportin = (ReportIn) list.get(i);
					ReportInForm reportInForm = new ReportInForm();
					TranslatorUtil.copyPersistenceToVo(reportin, reportInForm);
					result.add(reportInForm);
				}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return result;
	}

	/**
	 * 已使用hibernate 卞以刚 2011-12-21
	 * 查询需要全部导出的报表
	 * 
	 * @param reportInForm
	 *            报表FromBean
	 * @param operator
	 *            当前登录用户ID
	 * @return List 全部导出报表结果集
	 */
	public static List getDataRecords(ReportInForm reportInForm,
			Operator operator) {
		List resList = null;
		DBConn conn = null;
		Session session = null;

		if (reportInForm == null || operator == null)
			return resList;

		try {
			StringBuffer hql = new StringBuffer(
					"from ReportIn ri where "
							+ "ri.times=(select max(r.times) from ReportIn r where r.MChildReport.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and "
							+ "r.MChildReport.comp_id.versionId=ri.MChildReport.comp_id.versionId and r.MDataRgType.dataRangeId=ri.MDataRgType.dataRangeId and r.orgId=ri.orgId and "
							+ "r.MCurr.curId=ri.MCurr.curId and r.year=ri.year and r.term=ri.term and r.checkFlag=ri.checkFlag) and ri.orgId='"
							+ operator.getOrgId() + "'");

			/** 查询报表状态为审核通过报表 */
			StringBuffer where = new StringBuffer(" and ri.checkFlag="
					+ com.fitech.net.config.Config.CHECK_FLAG_PASS);

			/** 添加报表编号查询条件（忽略大小写模糊查询） */
			if (reportInForm.getChildRepId() != null
					&& !reportInForm.getChildRepId().equals("")) {
				where
						.append(" and upper(ri.MChildReport.comp_id.childRepId) like upper('%"
								+ reportInForm.getChildRepId().trim() + "%')");
			}
			/** 添加报表名称查询条件（模糊查询） */
			if (reportInForm.getRepName() != null
					&& !reportInForm.getRepName().equals("")) {
				where.append(" and ri.repName like '%"
						+ reportInForm.getRepName().trim() + "%'");
			}
			/** 添加模板类型（全部/法人/分支）查询条件 */
			if (reportInForm.getFrOrFzType() != null
					&& !reportInForm.getFrOrFzType().equals(
							Config.DEFAULT_VALUE)) {
				where.append(" and ri.MChildReport.frOrFzType='"
						+ reportInForm.getFrOrFzType() + "'");
			}
			/** 添加报送频度（月/季/半年/年）查询条件 */
			if (reportInForm.getRepFreqId() != null
					&& !String.valueOf(reportInForm.getRepFreqId()).equals(
							Config.DEFAULT_VALUE)) {
				where
						.append(" and ri.MDataRgType.dataRangeId in (select M.comp_id.dataRangeId from MActuRep M "
								+ "where M.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and M.comp_id.versionId=ri.MChildReport.comp_id.versionId "
								+ "and M.comp_id.repFreqId="
								+ reportInForm.getRepFreqId() + ")");
			}
			/** 添加日期（年份）查询条件 */
			if (reportInForm.getYear() != null) {
				where.append(" and ri.year=" + reportInForm.getYear());
			}
			/** 添加日期（月份）查询条件 */
			if (reportInForm.getTerm() != null) {
				where.append(" and ri.term=" + reportInForm.getTerm());
			}

			/** 添加报表查看权限（超级用户不用添加） */
			if (operator.isSuperManager() == false) {
				if (operator.getChildRepSearchPopedom() == null
						|| operator.getChildRepSearchPopedom().equals(""))
					return resList;
				/**已增加数据库判断*/
				if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
					where.append(" and '" + operator.getOrgId()
						+ "'||ri.MChildReport.comp_id.childRepId in ("
						+ operator.getChildRepSearchPopedom() + ")");
				if(Config.DB_SERVER_TYPE.equals("sqlserver"))
					where.append(" and '" + operator.getOrgId()
						+ "'+ri.MChildReport.comp_id.childRepId in ("
						+ operator.getChildRepSearchPopedom() + ")");
			}
			hql.append(where.toString());

			conn = new DBConn();
			session = conn.openSession();
			Query query = session.createQuery(hql.toString());
			List list = query.list();

			if (list != null && list.size() > 0) {
				resList = new ArrayList();
				for (int i = 0; i < list.size(); i++) {
					ReportIn reportin = (ReportIn) list.get(i);
					ReportInForm reportInFormTemp = new ReportInForm();
					TranslatorUtil.copyPersistenceToVo(reportin,
							reportInFormTemp);
					resList.add(reportInFormTemp);
				}
			}
		} catch (Exception e) {
			resList = null;
			log.printStackTrace(e);
		} finally {
			// 关闭数据库连接
			if (conn != null)
				conn.closeSession();
		}
		return resList;
	}

	/**
	 * 使用jdbc 需要修改 卞以刚 2011-12-22
	 * 报送统计数量
	 * 
	 * @author 曹发根
	 * @param reportInForm
	 *            报表FormBean
	 * @param operator
	 *            当前登录用户
	 * @return 报送统计数量
	 */
	public static int getReportStatCount(ReportInInfoForm reportInForm,
			Operator operator) {
		int count = 0;
		DBConn conn = null;
		Session session = null;

		if (reportInForm == null || operator == null)
			return count;

		try {

			int term = reportInForm.getTerm().intValue();
			int year = reportInForm.getYear().intValue();
			String rep_freq = "";
			if (term == 12)
				rep_freq = "('月','季','半年','年')";
			else if (term == 6)
				rep_freq = "('月','季','半年')";
			else if (term == 3 || term == 9)
				rep_freq = "('月','季')";
			else
				rep_freq = "('月')";
			
			StringBuffer sql = new StringBuffer(
					"select count(*) from  VIEW_M_REPORT a  "
							+ "left join (select * from report_in   where year="
							+ reportInForm.getYear()
							+ " and term="
							+ reportInForm.getTerm()
							+ " and times=1) b "
							+ "on a. ORG_ID=b.org_id and a.CHILD_REP_ID=b.CHILD_REP_ID and a.VERSION_ID=b.VERSION_ID 	and a.CUR_ID=b.CUR_ID and a.DATA_RANGE_ID=b.DATA_RANGE_ID ");			
			StringBuffer where = null;
			if(term<10){
				where = new StringBuffer(" where a.START_DATE<='"
						+ year + "-0" + term + "-01' and a.END_DATE>='" + year + "-0"+ term + "-01' and a.REP_FREQ_NAME in " + rep_freq);
			}else{
				where = new StringBuffer(" where a.START_DATE<='"
						+ year + "-" + term + "-01' and a.END_DATE>='" + year + "-"+ term + "-01' and a.REP_FREQ_NAME in " + rep_freq);
			}
			/** 查询报表状态为审核通过报表 */

			/** 添加报表编号查询条件（忽略大小写模糊查询） */
			if (reportInForm.getChildRepId() != null
					&& !reportInForm.getChildRepId().equals("")) {
				where.append(" and upper(a.CHILD_REP_ID) like upper('%"
						+ reportInForm.getChildRepId().trim() + "%')");
			}
			/** 添加报表名称查询条件（模糊查询） */
			if (reportInForm.getRepName() != null
					&& !reportInForm.getRepName().equals("")) {
				where.append(" and a.REPORT_NAME like '%"
						+ reportInForm.getRepName().trim() + "%'");
			}
			/** 添加模板类型（全部/法人/分支）查询条件 */
			if (reportInForm.getFrOrFzType() != null
					&& !reportInForm.getFrOrFzType().equals(
							Config.DEFAULT_VALUE)) {
				where.append(" and a.frOrFzType='"
						+ reportInForm.getFrOrFzType() + "'");
			}
			/** 添加报送频度（月/季/半年/年）查询条件 */
			if (reportInForm.getRepFreqId() != null
					&& !String.valueOf(reportInForm.getRepFreqId()).equals(
							Config.DEFAULT_VALUE)) {
				where.append(" and a.REP_FREQ_ID="
						+ reportInForm.getRepFreqId());
			}
			if (!StringUtil.isEmpty(reportInForm.getOrgId())
					&& !reportInForm.getOrgId().equals(Config.DEFAULT_VALUE)) {
				where.append(" and a.ORG_ID='" + reportInForm.getOrgId().trim()
						+ "'");
			}
			/** 添加报表状态（未审核/审核通过/审核未通过）查询条件 */
			if (reportInForm.getCheckFlag() != null
					&& !String.valueOf(reportInForm.getCheckFlag()).equals(
							Config.DEFAULT_VALUE)) {
				// 暂时用-10代表未上报
				if (reportInForm.getCheckFlag().equals(Short.valueOf("-10"))) {
					where
							.append(" and (b.CHECK_FLAG is null or b.CHECK_FLAG not in(-1,0,1)) ");
				} else
					where.append(" and b.CHECK_FLAG="
							+ reportInForm.getCheckFlag());
			}
			/** 添加报表查看权限（超级用户不用添加） */
			if (operator.isSuperManager() == false) {
				if (operator.getChildRepSearchPopedom() == null
						|| operator.getChildRepSearchPopedom().equals(""))
					return count;
				/**已增加数据库判断*/
				if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
					where.append(" and a.ORG_ID||a.CHILD_REP_ID in (select ORG_REP_ID  from VIEW_ORG_REP where POW_TYPE=2 and USER_ID="
								+ operator.getOperatorId() + ")");
				if(Config.DB_SERVER_TYPE.equals("sqlserver"))
					where.append(" and a.ORG_ID+a.CHILD_REP_ID in (select ORG_REP_ID  from VIEW_ORG_REP where POW_TYPE=2 and USER_ID="
								+ operator.getOperatorId() + ")");
			}
			sql.append(where.toString());
			// System.out.println("sql:"+sql);
			conn = new DBConn();
			/**jdbc技术 需要修改 卞以刚 2011-12-21**/
			session = conn.openSession();
			Connection connection = session.connection();
			ResultSet rs = connection.createStatement().executeQuery(
					sql.toString());

			if (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (Exception e) {
			count = 0;
			log.printStackTrace(e);
		} finally {
			// 关闭数据库连接
			if (conn != null)
				conn.closeSession();
		}
		return count;
	}

	/**
	 * jdbc技术 oracle语法 需要修改 卞以刚 2011-12-21
	 * sql意思是查出前几条 可以改成sqlserver语法 top
	 * 已增加数据库判断
	 * 查询可以导出的数据文件记录数
	 * 
	 * @param reportInForm
	 *            报表FormBean
	 * @param offset
	 *            偏移量
	 * @param limit
	 *            每页显示记录数
	 * @param operator
	 *            当前登录用户
	 * @return List 可以导出的数据文件结果集
	 */
	public static List getReportStatRecord(ReportInInfoForm reportInForm,
			int offset, int limit, Operator operator) {
		List resList = new ArrayList();
		DBConn conn = null;
		Session session = null;

		if (reportInForm == null || operator == null)
			return resList;

		try {
			int term = reportInForm.getTerm().intValue();
			int year = reportInForm.getYear().intValue();
			String rep_freq = "";
			if (term == 12)
				rep_freq = "('月','季','半年','年','日')";
			else if (term == 6)
				rep_freq = "('月','季','半年','日')";
			else if (term == 3 || term == 9)
				rep_freq = "('月','季','日')";
			else
				rep_freq = "('月','日')";
			StringBuffer sql=null;
			/**oracle语法 需要修改 卞以刚 2011-12-21
			 * 已增加数据库判断**/
			if(Config.DB_SERVER_TYPE.equals("sqlserver"))
			{
				 sql= new StringBuffer(
						"select top "+(offset + limit)+"  a.org_id,d.org_name,a.CHILD_REP_ID,a.VERSION_ID,a.REPORT_NAME,"
								+ "a.CUR_ID,a.CUR_NAME,a.REP_FREQ_ID,a.REP_FREQ_NAME,a.DATA_RG_DESC,b.CHECK_FLAG,b.REP_IN_ID "
								+ " from  VIEW_M_REPORT a left join org d on a.org_id=d.org_id  "
								+ "left join (select * from report_in   where year="
								+ reportInForm.getYear()
								+ " and term="
								+ reportInForm.getTerm()
								+ " and times=1) b "
								+ "on a. ORG_ID=b.org_id and a.CHILD_REP_ID=b.CHILD_REP_ID and a.VERSION_ID=b.VERSION_ID 	and a.CUR_ID=b.CUR_ID and a.DATA_RANGE_ID=b.DATA_RANGE_ID ");
			}
			
			if(Config.DB_SERVER_TYPE.equals("oracle"))
			{
				sql= new StringBuffer(
						"select t.*,rownum from (select a.org_id,d.org_name,a.CHILD_REP_ID,a.VERSION_ID,a.REPORT_NAME,"
								+ "a.CUR_ID,a.CUR_NAME,a.REP_FREQ_ID,a.REP_FREQ_NAME,a.DATA_RG_DESC,b.CHECK_FLAG,b.REP_IN_ID "
								+ " from  VIEW_M_REPORT a left join org d on a.org_id=d.org_id  "
								+ "left join (select * from report_in   where year="
								+ reportInForm.getYear()
								+ " and term="
								+ reportInForm.getTerm()
								+ " and times=1) b "
								+ "on a. ORG_ID=b.org_id and a.CHILD_REP_ID=b.CHILD_REP_ID and a.VERSION_ID=b.VERSION_ID 	and a.CUR_ID=b.CUR_ID and a.DATA_RANGE_ID=b.DATA_RANGE_ID ");
			}
			if(Config.DB_SERVER_TYPE.equals("db2")){
				sql= new StringBuffer(
						"select * from (select t.*,row_number() over(order by t.ORG_ID,t.CHILD_REP_ID,t.VERSION_ID) as rownum from (select a.org_id,d.org_name,a.CHILD_REP_ID,a.VERSION_ID,a.REPORT_NAME,"
								+ "a.CUR_ID,a.CUR_NAME,a.REP_FREQ_ID,a.REP_FREQ_NAME,a.DATA_RG_DESC,b.CHECK_FLAG,b.REP_IN_ID "
								+ " from  VIEW_M_REPORT a left join org d on a.org_id=d.org_id  "
								+ "left join (select * from report_in   where year="
								+ reportInForm.getYear()
								+ " and term="
								+ reportInForm.getTerm()
								+ " and times=1) b "
								+ "on a. ORG_ID=b.org_id and a.CHILD_REP_ID=b.CHILD_REP_ID and a.VERSION_ID=b.VERSION_ID 	and a.CUR_ID=b.CUR_ID and a.DATA_RANGE_ID=b.DATA_RANGE_ID ");
			}
			StringBuffer where = null;
			if(term<10){
				where = new StringBuffer(" where a.START_DATE<='"
						+ year + "-0" + term + "-01' and a.END_DATE>='" + year + "-0"+ term + "-01' and a.REP_FREQ_NAME in " + rep_freq);
			}else{
				where = new StringBuffer(" where a.START_DATE<='"
						+ year + "-" + term + "-01' and a.END_DATE>='" + year + "-"+ term + "-01' and a.REP_FREQ_NAME in " + rep_freq);
			}
			
			/** 添加报表编号查询条件（忽略大小写模糊查询） */
			if (reportInForm.getChildRepId() != null
					&& !reportInForm.getChildRepId().equals("")) {
				where.append(" and upper(a.CHILD_REP_ID) like upper('%"
						+ reportInForm.getChildRepId().trim() + "%')");
			}
			/** 添加报表名称查询条件（模糊查询） */
			if (reportInForm.getRepName() != null
					&& !reportInForm.getRepName().equals("")) {
				where.append(" and a.REPORT_NAME like '%"
						+ reportInForm.getRepName().trim() + "%'");
			}
			/** 添加模板类型（全部/法人/分支）查询条件 */
			if (reportInForm.getFrOrFzType() != null
					&& !reportInForm.getFrOrFzType().equals(
							Config.DEFAULT_VALUE)) {
				where.append(" and a.frOrFzType='"
						+ reportInForm.getFrOrFzType() + "'");
			}
			/** 添加报送频度（月/季/半年/年）查询条件 */
			if (reportInForm.getRepFreqId() != null
					&& !String.valueOf(reportInForm.getRepFreqId()).equals(
							Config.DEFAULT_VALUE)) {
				where.append(" and a.REP_FREQ_ID="
						+ reportInForm.getRepFreqId());
			}
			if (!StringUtil.isEmpty(reportInForm.getOrgId())
					&& !reportInForm.getOrgId().equals(Config.DEFAULT_VALUE)) {
				where.append(" and a.ORG_ID='" + reportInForm.getOrgId().trim()
						+ "'");
			}
			/** 添加报表状态（未审核/审核通过/审核未通过）查询条件 */
			if (reportInForm.getCheckFlag() != null
					&& !String.valueOf(reportInForm.getCheckFlag()).equals(
							Config.DEFAULT_VALUE)) {
				// 暂时用-10代表未上报
				if (reportInForm.getCheckFlag().equals(Short.valueOf("-10"))) {
					where
							.append(" and (b.CHECK_FLAG is null or b.CHECK_FLAG not in(-1,0,1)) ");
				} else
					where.append(" and b.CHECK_FLAG="
							+ reportInForm.getCheckFlag());
			}
			/** 添加报表查看权限（超级用户不用添加） 
			 * 已增加数据库判断*/
			if (operator.isSuperManager() == false) {
				if (operator.getChildRepSearchPopedom() != null
						&& !operator.getChildRepSearchPopedom().equals(""))
				{
					if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
						where.append(" and a.ORG_ID||a.CHILD_REP_ID in (select ORG_REP_ID  from VIEW_ORG_REP where POW_TYPE=2 and USER_ID="
									+ operator.getOperatorId() + ")");
					if(Config.DB_SERVER_TYPE.equals("sqlserver"))
						where.append(" and a.ORG_ID+a.CHILD_REP_ID in (select ORG_REP_ID  from VIEW_ORG_REP where POW_TYPE=2 and USER_ID="
									+ operator.getOperatorId() + ")");
				}
			}
			if(Config.DB_SERVER_TYPE.equals("sqlserver"))
			{
				sql
						.append(where.toString()
								+ " order by a.ORG_ID,a.CHILD_REP_ID,a.VERSION_ID ");
			}
			if(Config.DB_SERVER_TYPE.equals("oracle"))
			{
				sql
				.append(where.toString()
						+ " order by a.ORG_ID,a.CHILD_REP_ID,a.VERSION_ID ) t where rownum<="+(offset+limit));
			}
			if(Config.DB_SERVER_TYPE.equals("db2")){
				sql
				.append(where.toString()
						+ " order by a.ORG_ID,a.CHILD_REP_ID,a.VERSION_ID ) t ) where rownum<="+(offset+limit));
			}
			
			// System.out.println("sql:"+sql);
			/**jdbc技术 需要修改 卞以刚 2011-12-21**/
			conn = new DBConn();
			session = conn.openSession();
			Connection connection = session.connection();
			ResultSet rs = connection.createStatement().executeQuery(
					sql.toString());
			int i = 1;
			while (rs.next()) {
				if (i <= offset) {
					i++;
					continue;
				}
				Aditing aditing = new Aditing();
				// 设置报表ID标识符
				// aditing.setRepInId(reportInRecord.getRepInId());
				// 设置报表年份
				aditing.setYear(reportInForm.getYear());
				// 设置报表期数
				aditing.setTerm(reportInForm.getTerm());
				// 设置报表名称
				aditing.setRepName(rs.getString("REPORT_NAME"));
				// 设置报表报送日期
				// aditing.setReportDate(reportInRecord.getReportDate());
				// 设置报表编号
				aditing.setChildRepId(rs.getString("CHILD_REP_ID"));
				// 设置报表版本号
				aditing.setVersionId(rs.getString("VERSION_ID"));
				// 设置报表币种名称
				aditing.setCurrName(rs.getString("CUR_NAME"));
				aditing.setCurId(rs.getInt("CUR_ID"));

				// 设置报送口径
				aditing.setDataRgTypeName(rs.getString("DATA_RG_DESC"));
				// 设置报送频度
				aditing.setActuFreqName(rs.getString("REP_FREQ_NAME"));
				aditing.setActuFreqID(rs.getInt("REP_FREQ_ID"));

				if (rs.getString("REP_IN_ID") != null) {
					aditing.setRepInId(new Integer(rs.getString("REP_IN_ID")));
				}
				if (rs.getString("CHECK_FLAG") == null
						|| rs.getInt("CHECK_FLAG") > 1) {
					if (aditing.getRepInId() != null)
						aditing.setRepInId(null);
				} else
					aditing.set_checkFlag(Short.valueOf(rs
							.getString("CHECK_FLAG")));

				aditing.setOrgName(rs.getString("ORG_NAME"));
				aditing.setOrgId(rs.getString("ORG_ID"));
				resList.add(aditing);

			}
		} catch (Exception e) {
			resList = null;
			log.printStackTrace(e);
		} finally {
			// 关闭数据库连接
			if (conn != null)
				conn.closeSession();
		}
		return resList;
	}

	/**
	 * 已使用hibernate 卞以刚 2011-12-21
	 * 更新reportIn表中报送人和审核人信息
	 * 
	 * @param reportInForm
	 * @return
	 * @throws Exception
	 */
	public static boolean updateChecker(
			com.cbrc.smis.form.ReportInForm reportInForm) throws Exception {
		// 更新标志
		boolean bool = false;
		// 创建连接
		DBConn conn = null;
		Session session = null;
		// reportform中是否有参数
		if (reportInForm == null) {
			return bool;
		}

		try {
			conn = new DBConn();
			session = conn.beginTransaction();

			if (reportInForm == null)
				return bool;

			ReportIn reportInPersistence = (ReportIn) session.load(
					ReportIn.class, reportInForm.getRepInId());

			reportInPersistence.setWriter(reportInForm.getWriter());
			reportInPersistence.setChecker(reportInForm.getChecker());
			reportInPersistence.setPrincipal(reportInForm.getPrincipal());

			session.update(reportInPersistence);
			session.flush();
			bool = true;

		} catch (HibernateException he) {
			bool = false;
			log.printStackTrace(he);
		} catch (Exception e) {
			bool = false;
			log.printStackTrace(e);
		} finally {
			// 如果conn对象依然存在,则结束当前事务并断开连接
			if (conn != null)
				conn.endTransaction(bool);
		}
		// 更新成功返回吧!!!
		return bool;
	}

	/**
	 * 已使用hibernate 卞以刚 2011-12-21
	 * 报表查看的查找总记录数
	 * 
	 * @author jcm
	 */
	public static int getRecordCountOfmanual2(
			ReportInInfoForm reportInInfoForm, Operator operator) {

		int count = 0;

		// 连接对象和会话对象初始化
		DBConn conn = null;
		Session session = null;

		// 查询条件HQL的生成
		StringBuffer hql = new StringBuffer(
				"select count(*) from ReportIn ri WHERE "
						+ "ri.times=(select max(r.times) from ReportIn r where r.MChildReport.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and "
						+ "r.MChildReport.comp_id.versionId=ri.MChildReport.comp_id.versionId and r.MDataRgType.dataRangeId=ri.MDataRgType.dataRangeId and r.orgId=ri.orgId and "
						+ "r.MCurr.curId=ri.MCurr.curId and r.year=ri.year and r.term=ri.term)");

		StringBuffer where = new StringBuffer("");

		if (reportInInfoForm == null) {
			return count;
		}

		try {

			// 查找条件的判断
			// 机构的条件判断
			if (operator != null && !operator.getOrgId().equals("")) {
				where.append(" and ri.orgId='" + operator.getOrgId().trim()
						+ "'");
			}
			if (reportInInfoForm.getYear() != null
					&& reportInInfoForm.getYear().intValue() > 0) {
				where.append(" and ri.year=" + reportInInfoForm.getYear());
			}
			if (reportInInfoForm.getTerm() != null
					&& reportInInfoForm.getTerm().intValue() > 0) {
				where.append(" and ri.term=" + reportInInfoForm.getTerm());
			}
			// 标志范围的条件判断

			// 报表名称的判断
			if (reportInInfoForm.getRepName() != null
					&& !reportInInfoForm.getRepName().equals("")) {
				where.append(" and ri.repName like '%"
						+ reportInInfoForm.getRepName() + "%'");

			}
			// List集合的操作
			// 初始化
			hql.append(where.toString());
			// conn对象的实例化
			conn = new DBConn();
			// 打开连接开始会话
			session = conn.openSession();

			List list = session.find(hql.toString());

			if (list != null && list.size() != 0) {
				count = ((Integer) list.get(0)).intValue();
			}

		} catch (HibernateException he) {
			log.printStackTrace(he);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			// 如果连接存在，则断开，结束会话，返回
			if (conn != null)
				conn.closeSession();
		}
		return count;
	}

	/**
	 * 已使用hibernate 卞以刚 2011-12-21
	 * 报表查看 取得ReportIn中符合条件的记录条数 zhangxinke
	 */
	public static int getRecordCount2(ReportInForm reportInForm, String orgId)
			throws Exception {
		int recordCount = 0;
		List retVals = null;
		DBConn conn = null;

		try {
			StringBuffer hql = new StringBuffer(
					"select count(*) from ReportIn mcr where 1=1 and "
							+ "mcr.orgId='" + orgId + "'");
			StringBuffer where = new StringBuffer("");
			Integer year = reportInForm.getYear();
			Integer term = reportInForm.getTerm();
			if (reportInForm != null) {

				if (year != null && !year.equals("")) {
					where.append(" and mcr.year=" + year + " ");
				}
				if (term != null && !term.equals("")) {
					where.append(" and mcr.term=" + term + "");
				}

				// 报表名称的判断
				if (reportInForm.getRepName() != null
						&& !reportInForm.getRepName().equals("")) {
					where.append(" and mcr.repName like '%"
							+ reportInForm.getRepName().trim() + "%'");

				}

			}
			hql.append(where.toString());

			conn = new DBConn();

			Session session = conn.openSession();
			Query query = session.createQuery(hql.toString());
			retVals = query.list();
			if (retVals != null) {
				recordCount = Integer.parseInt(retVals.get(0).toString());
			}
		} catch (HibernateException he) {
			log.printStackTrace(he);
		} finally {
			if (conn != null)
				conn.closeSession();
		}

		return recordCount;

	}

	/**
	 * 已使用hibernate 卞以刚 2011-12-21
	 * 报表查看 取得ReportIn中符合条件的记录 zhangxinke
	 */
	public static List select2(ReportInForm reportInForm, int offset,
			int limit, String orgId) throws Exception {

		List retVals = null;
		DBConn conn = null;

		try {
			StringBuffer hql = new StringBuffer(
					" from ReportIn mcr where 1=1 and " + "mcr.orgId='" + orgId
							+ "'");
			StringBuffer where = new StringBuffer("");

			if (reportInForm != null) {
				Integer year = reportInForm.getYear();
				Integer term = reportInForm.getTerm();
				String reportName = reportInForm.getRepName();
				if (year != null && !year.equals(""))
					where.append(" and mcr.year=" + year + "");
				if (term != null && !term.equals(""))
					where.append(" and mcr.term=" + term + "");
				// 报表名称的判断
				if (reportName != null && !reportName.equals(""))
					where.append(" and mcr.repName like '%" + reportName.trim()
							+ "%'");
			}
			hql.append(where.toString());

			conn = new DBConn();
			Session session = conn.openSession();
			Query query = session.createQuery(hql.toString());
			query.setFirstResult(offset);
			query.setMaxResults(limit);
			List list = query.list();

			if (list != null && list.size() > 0) {
				Iterator it = list.iterator();
				ReportIn reportIn = new ReportIn();
				retVals = new ArrayList();
				while (it.hasNext()) {
					reportIn = (ReportIn) it.next();
					Aditing aditing = new Aditing();
					TranslatorUtil.copyPersistenceToVo1(reportIn, aditing);
					retVals.add(aditing);
				}
			}
		} catch (HibernateException he) {
			log.printStackTrace(he);
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return retVals;
	}

	/**
	 * 已使用hibernate 卞以刚 2011-12-21
	 * 报送机构查看报送状态 jcm
	 */
	public static List selectOfManual2(ReportInInfoForm reportInInfoForm,
			int offset, int limit, Operator operator) throws Exception {
		List retvals = null;
		DBConn conn = null;
		Session session = null;

		StringBuffer hql = new StringBuffer(
				"from ReportIn ri where "
						+ "ri.times=(select max(r.times) from ReportIn r where r.MChildReport.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and "
						+ "r.MChildReport.comp_id.versionId=ri.MChildReport.comp_id.versionId and r.MDataRgType.dataRangeId=ri.MDataRgType.dataRangeId and r.orgId=ri.orgId and "
						+ "r.MCurr.curId=ri.MCurr.curId and r.year=ri.year and r.term=ri.term)");

		StringBuffer where = new StringBuffer("");

		if (reportInInfoForm == null) {
			return retvals;
		}

		try {

			// 机构的条件判断
			if (operator != null && !operator.getOrgId().equals("")) {
				where.append(" and ri.orgId='" + operator.getOrgId().trim()
						+ "'");
			}

			if (reportInInfoForm.getYear() != null
					&& reportInInfoForm.getYear().intValue() > 0) {
				where.append(" and ri.year=" + reportInInfoForm.getYear());
			}
			if (reportInInfoForm.getTerm() != null
					&& reportInInfoForm.getTerm().intValue() > 0) {
				where.append(" and ri.term=" + reportInInfoForm.getTerm());
			}
			// 报表名称的判断
			if (reportInInfoForm.getRepName() != null
					&& !reportInInfoForm.getRepName().equals("")) {
				where.append(" and ri.repName like '%"
						+ reportInInfoForm.getRepName() + "%'");
			}

			// conn对象的实例化
			conn = new DBConn();
			// 打开连接开始会话
			session = conn.openSession();
			// 添加集合至Session
			hql.append(where.toString());

			Query query = session.createQuery(hql.toString());

			query.setFirstResult(offset);
			query.setMaxResults(limit);
			List list = query.list();

			if (list != null) {
				retvals = new ArrayList();
				// 循环读取数据库符合条件记录
				for (Iterator it = list.iterator(); it.hasNext();) {
					// 实例化一个中间类Aditing接受所有页面所需的字段名
					Aditing aditing = new Aditing();
					// 生成持久化对象
					ReportIn reportInPersistence = (ReportIn) it.next();
					// 将查找到的审核标志set进aditing对象
					aditing.setCheckFlag(reportInPersistence.getCheckFlag());
					// 将查找到的报表id传进aditing对象
					aditing.setRepInId(reportInPersistence.getRepInId());
					aditing.setYear(reportInPersistence.getYear());
					aditing.setTerm(reportInPersistence.getTerm());
					// 将查找的报表名称set进adtiting对象
					if (!reportInPersistence.getRepName().equals(
							reportInPersistence.getMChildReport()
									.getReportName()))
						aditing.setRepName(reportInPersistence.getRepName()
								+ "-"
								+ reportInPersistence.getMChildReport()
										.getReportName());
					else
						aditing.setRepName(reportInPersistence.getRepName());
					// 将查找的报表日期set进aditing对象
					aditing.setReportDate(reportInPersistence.getReportDate());
					// 将查找的子报表ID放进aditing对象
					aditing.setChildRepId(reportInPersistence.getMChildReport()
							.getComp_id().getChildRepId());
					// 将查找的版本号set进aditing对象
					aditing.setVersionId(reportInPersistence.getMChildReport()
							.getComp_id().getVersionId());
					// 数据口径
					aditing.setDataRgTypeName(reportInPersistence
							.getMDataRgType().getDataRgDesc());
					// 报送频度
					// aditing.setActuFreqName(reportInPersistence.);
					/**已使用hibernate 卞以刚 2011-12-21**/
					MActuRep mActuRep = GetFreR(reportInPersistence);
					if (mActuRep != null) {
						aditing.setDataRgTypeName(mActuRep.getMDataRgType()
								.getDataRgDesc());
						aditing.setActuFreqName(mActuRep.getMRepFreq()
								.getRepFreqName());
					}
					// 审核不通过的原因
					/**已使用hibernate 卞以刚 2011-12-21**/
					ReportAgainSetForm reportAgainSetForm = StrutsReportAgainSetDelegate
							.getReportAgainSetInfo(reportInPersistence
									.getRepInId());
					if (reportAgainSetForm != null
							&& reportAgainSetForm.getCause() != null)
						aditing.setWhy(reportAgainSetForm.getCause());

					retvals.add(aditing);
				}
			}
		} catch (HibernateException he) {
			retvals = null;
			log.printStackTrace(he);
		} catch (Exception e) {
			retvals = null;
			log.printStackTrace(e);
		} finally {
			// 如果连接存在，则断开，结束会话，返回
			if (conn != null)
				conn.closeSession();
		}
		return retvals;
	}

	/**
	 * 已使用hibernate 卞以刚 2011-12-21
	 * 判断是否需要上报(需要解析文件)(没有记录要上新报,)***现在只需要判断是否
	 */
	public static boolean isReportInExist(Aditing aditing) {
		boolean flag = false;
		DBConn conn = new DBConn();
		Session session = conn.openSession();

		try {
			ReportIn reportIn = new ReportIn();
			MChildReport mChildReport = new MChildReport();
			MChildReportPK com_id = new MChildReportPK();
			com_id.setChildRepId(aditing.getChildRepId());
			com_id.setVersionId(aditing.getVersionId());
			mChildReport.setComp_id(com_id);
			reportIn.setMChildReport(mChildReport);
			
			/**已使用hibernate 卞以刚 2011-12-21**/
			MDataRgType md = StrutsMDataRgTypeDelegate.selectOneByName(aditing
					.getDataRgTypeName());
			reportIn.setMDataRgType(md);

			MActuRep mActuRep = GetFreR(reportIn);

			if (mActuRep != null) {
				aditing
						.setActuFreqName(mActuRep.getMRepFreq()
								.getRepFreqName());
			}

			String hql = "from ReportIn ri where "
					+ "ri.times=(select max(r.times) from ReportIn r where "
					+ "ri.MChildReport.comp_id.childRepId=r.MChildReport.comp_id.childRepId and "
					+ "ri.MChildReport.comp_id.versionId=r.MChildReport.comp_id.versionId and "
					+ "ri.MDataRgType.dataRgDesc=r.MDataRgType.dataRgDesc and "
					+ "ri.orgId=r.orgId and ri.MCurr.curName=r.MCurr.curName and "
					+ "ri.year=r.year and ri.term=r.term)"
					+ " and ri.MChildReport.comp_id.childRepId='"
					+ aditing.getChildRepId() + "'"
					+ " and ri.MChildReport.comp_id.versionId='"
					+ aditing.getVersionId() + "'" + " and ri.orgId='"
					+ aditing.getOrgName().trim() + "'"
					+ " and ri.MDataRgType.dataRgDesc='"
					+ aditing.getDataRgTypeName() + "'"
					+ " and ri.MCurr.curName='" + aditing.getCurrName() + "'"
					+ " and ri.year=" + aditing.getYear().intValue()
					+ " and ri.term=" + aditing.getTerm().intValue();

			List list = session.find(hql);

			if (list != null && list.size() != 0) {
				ReportIn reportInTemp = (ReportIn) list.get(0);
				if (reportInTemp.getCheckFlag().toString()
						.equals(
								com.fitech.net.config.Config.CHECK_FLAG_PASS
										.toString())
						|| reportInTemp.getCheckFlag().toString().equals(
								com.fitech.net.config.Config.CHECK_FLAG_UNCHECK
										.toString())) {
					aditing.setWhy("已报");
					flag = true;
				}
			}
		} catch (HibernateException he) {
			flag = false;
			he.printStackTrace();
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return flag;
	}

	/**
	 * 已使用hibernate 卞以刚 2011-12-21
	 * 判断是否需要上报(需要解析文件)(没有记录要上新报,)***现在只需要判断是否
	 */
	public static ReportIn isReportExist(Aditing aditing) {
		ReportIn reportIn = null;
		DBConn conn = new DBConn();
		Session session = conn.openSession();

		try {
			String hql = "from ReportIn ri where "
					+ "ri.times=(select max(r.times) from ReportIn r where "
					+ "ri.MChildReport.comp_id.childRepId=r.MChildReport.comp_id.childRepId and "
					+ "ri.MChildReport.comp_id.versionId=r.MChildReport.comp_id.versionId and "
					+ "ri.MDataRgType.dataRgDesc=r.MDataRgType.dataRgDesc and "
					+ "ri.orgId=r.orgId and ri.MCurr.curName=r.MCurr.curName and "
					+ "ri.year=r.year and ri.term=r.term)"
					+ " and ri.MChildReport.comp_id.childRepId='"
					+ aditing.getChildRepId() + "'"
					+ " and ri.MChildReport.comp_id.versionId='"
					+ aditing.getVersionId() + "'" + " and ri.orgId='"
					+ aditing.getOrgName().trim() + "'"
					+ " and ri.MDataRgType.dataRgDesc='"
					+ aditing.getDataRgTypeName() + "'"
					+ " and ri.MCurr.curName='" + aditing.getCurrName() + "'"
					+ " and ri.year=" + aditing.getYear().intValue()
					+ " and ri.term=" + aditing.getTerm().intValue();

			List list = session.find(hql);

			if (list != null && list.size() != 0) {
				reportIn = (ReportIn) list.get(0);
			}
		} catch (HibernateException he) {
			reportIn = null;
			he.printStackTrace();
		} catch (Exception e) {
			reportIn = null;
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return reportIn;
	}
	
	/***
	 * 已使用hibernate 卞以刚 2011-12-21
	 * @param repInId
	 * @return
	 */
	public static boolean updateReportIn(Integer repInId) {
		// 更新标志
		boolean result = false;
		// 创建连接
		DBConn conn = null;
		Session session = null;

		try {
			conn = new DBConn();
			session = conn.beginTransaction();

			ReportIn reportInPersistence = (ReportIn) session.load(
					ReportIn.class, repInId);

			if (reportInPersistence == null)
				return result;

			reportInPersistence
					.setCheckFlag(com.fitech.net.config.Config.CHECK_FLAG_FAILED);

			session.update(reportInPersistence);
			session.flush();

			result = true;
			// 异常的捕捉机制
		} catch (HibernateException he) {
			result = false;
			log.printStackTrace(he);
		} catch (Exception e) {
			result = false;
			log.printStackTrace(e);
		} finally {
			// 如果conn对象依然存在,则结束当前事务并断开连接
			if (conn != null)
				conn.endTransaction(result);
		}
		// 更新成功返回吧!!!
		return result;
	}
	
	/***
	 * 已使用hibernate 卞以刚 2011-12-21
	 * @param repInId
	 * @return
	 */
	public static boolean updateReportInCheckFlag(Integer repInId) {
		// 更新标志
		boolean result = false;
		// 创建连接
		DBConn conn = null;
		Session session = null;

		try {
			conn = new DBConn();
			session = conn.beginTransaction();

			ReportIn reportInPersistence = (ReportIn) session.load(
					ReportIn.class, repInId);

			if (reportInPersistence == null)
				return result;

			reportInPersistence
					.setCheckFlag(com.fitech.net.config.Config.CHECK_FLAG_UNREPORT);

			session.update(reportInPersistence);
			session.flush();

			result = true;
			// 异常的捕捉机制
		} catch (HibernateException he) {
			result = false;
			log.printStackTrace(he);
		} catch (Exception e) {
			result = false;
			log.printStackTrace(e);
		} finally {
			// 如果conn对象依然存在,则结束当前事务并断开连接
			if (conn != null)
				conn.endTransaction(result);
		}
		// 更新成功返回吧!!!
		return result;
	}
	
	/****
	 * 已使用hibernate 卞以刚 2011-12-21
	 * @param reportInForm
	 * @param flag
	 * @return
	 */
	public static int getYBRecordCount(ReportInForm reportInForm, Short flag) {
		int count = 0;

		DBConn conn = null;
		Session session = null;

		if (reportInForm == null || reportInForm.getOrgId() == null)
			return count;
		// 查询条件HQL的生成
		// StringBuffer hql = new StringBuffer(
		// "select count(*) from ReportIn ri WHERE "
		// + "ri.times=(select max(r.times) from ReportIn r where
		// r.MChildReport.comp_id.childRepId=ri.MChildReport.comp_id.childRepId
		// and "
		// + "r.MChildReport.comp_id.versionId=ri.MChildReport.comp_id.versionId
		// and r.MDataRgType.dataRangeId=ri.MDataRgType.dataRangeId and
		// r.orgId=ri.orgId and "
		// + "r.MCurr.curId=ri.MCurr.curId and r.year=ri.year and
		// r.term=ri.term) and ri.checkFlag="
		// + flag + " and ri.orgId='" + reportInForm.getOrgId() + "'");
		StringBuffer hql = new StringBuffer(
				"select count(*) from ReportIn ri WHERE "
						+ "ri.times=1 and ri.checkFlag=" + flag
						+ " and ri.orgId='" + reportInForm.getOrgId() + "'");
		if (reportInForm.getYear() != null
				&& reportInForm.getYear().intValue() > 0) {
			hql.append(" and ri.year=" + reportInForm.getYear());
		}
		if (reportInForm.getTerm() != null
				&& reportInForm.getTerm().intValue() > 0) {
			hql.append(" and ri.term=" + reportInForm.getTerm());
		}

		try {
			// conn对象的实例化
			conn = new DBConn();
			// 打开连接开始会话
			session = conn.openSession();

			List list = session.find(hql.toString());
			if (list != null && list.size() > 0)
				count = ((Integer) list.get(0)).intValue();

		} catch (HibernateException he) {
			count = 0;
			log.printStackTrace(he);
		} catch (Exception e) {
			count = 0;
			log.printStackTrace(e);
		} finally {
			// 如果连接存在，则断开，结束会话，返回
			if (conn != null)
				conn.closeSession();
		}
		return count;
	}

	/**
	 * 已使用hibernate 卞以刚 2011-12-21
	 * <p>
	 * 描述:查某个机构某个状态某个期数得报表个数
	 * </p>
	 * <p>
	 * 参数: year，term，orgId，flag，childReportIds（'G0100','G0200'）
	 * </p>
	 * <p>
	 * 日期：2007-11-18
	 * </p>
	 * <p>
	 * 作者：曹发根
	 * </p>
	 */
	public static int getYBRecordCount(Integer year, Integer term,
			String orgId, Short flag, String childReportIds) {
		int count = 0;
		DBConn conn = null;
		Session session = null;
		try {
			// 查询条件HQL的生成

			StringBuffer hql = new StringBuffer(
					"select count(*) from ReportIn ri WHERE "
							+ "ri.times=1 and ri.checkFlag=" + flag
							+ " and ri.orgId='" + orgId + "'");
			hql.append(" and ri.year=" + year);
			hql.append(" and ri.term=" + term);

			// 加上报表分配的权限
			if (childReportIds != null && !childReportIds.equals(""))
				hql.append(" and ri.MChildReport.comp_id.childRepId in ("
						+ childReportIds + ")");
			// conn对象的实例化
			conn = new DBConn();
			// 打开连接开始会话
			session = conn.openSession();

			List list = session.find(hql.toString());
			if (list != null && list.size() > 0)
				count = ((Integer) list.get(0)).intValue();

		} catch (HibernateException he) {
			count = 0;
			log.printStackTrace(he);
		} catch (Exception e) {
			count = 0;
			log.printStackTrace(e);
		} finally {
			// 如果连接存在，则断开，结束会话，返回
			if (conn != null)
				conn.closeSession();
		}
		return count;
	}

	/**
	 * 已使用hibernate 卞以刚 2011-12-21
	 * 查询未审核报表，根据不同用户
	 * 
	 * @param reportInForm
	 * @param operator
	 * @return
	 */
	public static int getWSRecordCountOfmanual(Operator operator) {

		int count = 0;
		// 连接对象和会话对象初始化
		DBConn conn = null;
		Session session = null;

		StringBuffer hql = new StringBuffer(
				"select count(*) from ReportIn ri WHERE "
						+ "ri.times=(select max(r.times) from ReportIn r where r.MChildReport.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and "
						+ "r.MChildReport.comp_id.versionId=ri.MChildReport.comp_id.versionId and r.MDataRgType.dataRangeId=ri.MDataRgType.dataRangeId and r.orgId=ri.orgId and "
						+ "r.MCurr.curId=ri.MCurr.curId and r.year=ri.year and r.term=ri.term)");

		StringBuffer where = new StringBuffer("  and ri.checkFlag="
				+ com.fitech.net.config.Config.CHECK_FLAG_UNCHECK);

		if (operator == null)
			return 0;

		/** 机构权限 */
		if (operator.getOrgPopedomSQL() != null
				&& !operator.getOrgPopedomSQL().equals("")) {
			String sql = "select distinct pv.comp_id.orgId from MPurView pv where pv.comp_id.userGrpId in("
					+ operator.getUserGrpIds() + ")";
			where.append(" and ri.orgId in(" + sql + ")");
		} else
			return 0;

		/** 报表权限 */
		if (operator.getChildRepPodedomSQL() != null
				&& !operator.getChildRepPodedomSQL().equals(""))
			where.append(" and ri.MChildReport.comp_id.childRepId in("
					+ operator.getChildRepPodedomSQL() + ")");
		else
			return 0;

		hql.append(where.toString());

		try {
			// conn对象的实例化
			conn = new DBConn();
			// 打开连接开始会话
			session = conn.openSession();

			List list = session.find(hql.toString());
			if (list != null && list.size() > 0)
				count = ((Integer) list.get(0)).intValue();

		} catch (HibernateException he) {
			count = 0;
			log.printStackTrace(he);
		} catch (Exception e) {
			count = 0;
			log.printStackTrace(e);
		} finally {
			// 如果连接存在，则断开，结束会话，返回
			if (conn != null)
				conn.closeSession();
		}
		return count;
	}

	
	/**
	 * 已使用hibernate  卞以刚  2011-12-21
	 * 修改报表的标志位
	 * 
	 * @param repInId
	 * @return
	 */
	public static boolean updateReportInCheckFlag(Integer repInId,
			Short checkFlag) {
		// 更新标志
		boolean result = false;
		// 创建连接
		DBConn conn = null;
		Session session = null;

		try {
			conn = new DBConn();
			session = conn.beginTransaction();

			ReportIn reportInPersistence = (ReportIn) session.load(
					ReportIn.class, repInId);

			if (reportInPersistence == null)
				return result;

			reportInPersistence.setCheckFlag(checkFlag);

			session.update(reportInPersistence);
			session.flush();

			result = true;
			// 异常的捕捉机制
		} catch (HibernateException he) {
			result = false;
			log.printStackTrace(he);
		} catch (Exception e) {
			result = false;
			log.printStackTrace(e);
		} finally {
			// 如果conn对象依然存在,则结束当前事务并断开连接
			if (conn != null)
				conn.endTransaction(result);
		}
		// 更新成功返回吧!!!
		return result;
	}

	/**
	 * 已使用hibernate 卞以刚 2011-12-21
	 * 修改报表的标志位(应急把上面方法改为非static)
	 * 
	 * @param repInId
	 * @return
	 */
	public boolean updateReportInCheckFlag_e(Integer repInId, Short checkFlag) {
		// 更新标志
		boolean result = false;
		// 创建连接
		DBConn conn = null;
		Session session = null;

		try {
			conn = new DBConn();
			session = conn.beginTransaction();

			ReportIn reportInPersistence = (ReportIn) session.load(
					ReportIn.class, repInId);

			if (reportInPersistence == null)
				return result;

			reportInPersistence.setCheckFlag(checkFlag);
			reportInPersistence.setTblInnerValidateFlag(null);
			reportInPersistence.setTblOuterValidateFlag(null);

			session.update(reportInPersistence);
			session.flush();

			result = true;
			// 异常的捕捉机制
		} catch (HibernateException he) {
			result = false;
			log.printStackTrace(he);
		} catch (Exception e) {
			result = false;
			log.printStackTrace(e);
		} finally {
			// 如果conn对象依然存在,则结束当前事务并断开连接
			if (conn != null)
				conn.endTransaction(result);
		}
		// 更新成功返回吧!!!
		return result;
	}

	/**
	 * 已使用hibernate 卞以刚 2011-12-21
	 * 更新标志(是否手工调平过)
	 * 
	 * @param reportInForm
	 * @return
	 */
	public static boolean updateReportInHandworkFlag(Integer repInId,
			Integer handFlag) {
		if (repInId == null)
			return false;
		// 更新标志
		boolean result = false;
		// 创建连接
		DBConn conn = null;
		Session session = null;

		try {
			conn = new DBConn();
			session = conn.beginTransaction();

			ReportIn reportInPersistence = (ReportIn) session.load(
					ReportIn.class, repInId);

			if (reportInPersistence == null)
				return result;
			reportInPersistence.setRepOutId(handFlag);

			session.update(reportInPersistence);
			session.flush();

			result = true;
			// 异常的捕捉机制
		} catch (HibernateException he) {
			result = false;
			log.printStackTrace(he);
		} catch (Exception e) {
			result = false;
			log.printStackTrace(e);
		} finally {
			// 如果conn对象依然存在,则结束当前事务并断开连接
			if (conn != null)
				conn.endTransaction(result);
		}
		// 更新成功返回吧!!!
		return result;
	}

	/**
	 * 已使用hibernate 卞以刚 2011-12-21
	 * 查看该机构是否已经有报表上报
	 * 
	 * @param orgId
	 * @return
	 */
	public static int selectOrgRepList(String orgId) {

		int count = 0;
		DBConn conn = null;
		Session session = null;

		try {
			if (orgId == null)
				return count;

			conn = new DBConn();
			session = conn.openSession();
			String hql = "select count(*) from ReportIn rep where rep.orgId = '"
					+ orgId + "'";

			List list = session.find(hql.toString());
			if (list != null && list.size() > 0)
				count = ((Integer) list.get(0)).intValue();

		} catch (HibernateException he) {
			log.printStackTrace(he);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			// 如果连接存在，则断开，结束会话，返回
			if (conn != null)
				conn.closeSession();
		}
		return count;
	}

	/**
	 * 已使用hibernate 卞以刚 2011-12-21
	 * 审核通过全部报表
	 * 
	 * @author jcm
	 * @param reportInForm
	 *            报表FormBean
	 * @param operator
	 *            当前登录用户
	 * @return List 查询结果集
	 */
	public static List selectAllRepId(ReportInForm reportInForm,
			Operator operator) throws Exception {
		List resList = null;
		DBConn conn = null;
		Session session = null;

		if (reportInForm == null || operator == null)
			return resList;

		try {
			StringBuffer hql = new StringBuffer(
					"from ReportIn ri WHERE "
							+ "ri.times=(select max(r.times) from ReportIn r where r.MChildReport.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and "
							+ "r.MChildReport.comp_id.versionId=ri.MChildReport.comp_id.versionId and r.MDataRgType.dataRangeId=ri.MDataRgType.dataRangeId and r.orgId=ri.orgId and "
							+ "r.MCurr.curId=ri.MCurr.curId and r.year=ri.year and r.term=ri.term and r.checkFlag=ri.checkFlag)");

			/** 查询报表状态为未审核报表 */
			StringBuffer where = new StringBuffer(" and ri.checkFlag="
					+ com.fitech.net.config.Config.CHECK_FLAG_UNCHECK);

			/** 添加报表编号查询条件（忽略大小写模糊查询） */
			if (reportInForm.getChildRepId() != null
					&& !reportInForm.getChildRepId().equals("")) {
				where
						.append(" and upper(ri.MChildReport.comp_id.childRepId) like upper('%"
								+ reportInForm.getChildRepId().trim() + "%')");
			}
			/** 添加报表名称查询条件（模糊查询） */
			if (reportInForm.getRepName() != null
					&& !reportInForm.getRepName().equals("")) {
				where.append(" and ri.repName like '%"
						+ reportInForm.getRepName().trim() + "%'");
			}
			/** 添加模板类型（全部/法人/分支）查询条件 */
			if (reportInForm.getFrOrFzType() != null
					&& !reportInForm.getFrOrFzType().equals(
							Config.DEFAULT_VALUE)) {
				where.append(" and ri.MChildReport.frOrFzType='"
						+ reportInForm.getFrOrFzType() + "'");
			}
			/** 添加报送频度（月/季/半年/年）查询条件 */
			if (reportInForm.getRepFreqId() != null
					&& !String.valueOf(reportInForm.getRepFreqId()).equals(
							Config.DEFAULT_VALUE)) {
				where
						.append(" and ri.MDataRgType.dataRangeId in (select M.comp_id.dataRangeId from MActuRep M "
								+ "where M.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and M.comp_id.versionId=ri.MChildReport.comp_id.versionId "
								+ "and M.comp_id.repFreqId="
								+ reportInForm.getRepFreqId() + ")");
			}
			/** 添加日期（年份）查询条件 */
			if (reportInForm.getYear() != null) {
				where.append(" and ri.year=" + reportInForm.getYear());
			}
			/** 添加日期（月份）查询条件 */
			if (reportInForm.getTerm() != null) {
				where.append(" and ri.term=" + reportInForm.getTerm());
			}
			/** 添加机构查询条件 */
			if (reportInForm.getOrgId() != null
					&& !reportInForm.getOrgId().equals(Config.DEFAULT_VALUE)) {
				where.append(" and ri.orgId='" + reportInForm.getOrgId().trim()
						+ "'");
			}

			/** 添加报表审核权限（超级用户不用添加）
			 * 已增加数据库判断 */
			if (operator.isSuperManager() == false) {
				if (operator.getChildRepCheckPodedom() == null
						|| operator.getChildRepCheckPodedom().equals(""))
					return resList;
				if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
					where.append(" and ltrim(rtrim(ri.orgId)) || ri.MChildReport.comp_id.childRepId in("
								+ operator.getChildRepCheckPodedom() + ")");
				if(Config.DB_SERVER_TYPE.equals("sqlserver"))
					where.append(" and ltrim(rtrim(ri.orgId)) + ri.MChildReport.comp_id.childRepId in("
								+ operator.getChildRepCheckPodedom() + ")");
			}
			hql.append(where.toString());

			conn = new DBConn();
			session = conn.openSession();
			Query query = session.createQuery(hql.toString());

			List list = query.list();
			if (list != null && list.size() > 0) {
				resList = new ArrayList();
				ReportIn reportIn = null;
				for (Iterator it = list.iterator(); it.hasNext();) {
					reportIn = (ReportIn) it.next();
					resList.add(reportIn.getRepInId());
				}
			}
		} catch (HibernateException he) {
			resList = null;
			log.printStackTrace(he);
		} catch (Exception e) {
			resList = null;
			log.printStackTrace(e);
		} finally {
			// 关闭数据库连接
			if (conn != null)
				conn.closeSession();
		}
		return resList;
	}

	/**
	 * 已使用hibernate 卞以刚 2011-12-21
	 * 需要进行全部校验的报表(数据上报功能所用)
	 * 
	 * @param reportInForm
	 * @param operator
	 *            当前登录用户
	 * @return List 校验的报表ID列表
	 * @throws Exception
	 */
	public static List selectAllReports(ReportInForm reportInForm,
			Operator operator) throws Exception {
		List retvals = null;
		DBConn conn = null;
		Session session = null;

		if (reportInForm.getYear() == null || reportInForm.getTerm() == null
				|| reportInForm.getOrgId() == null)
			return retvals;

		try {
			StringBuffer hql = new StringBuffer(
					"select ri.repInId from ReportIn ri where " + "ri.times=1");

			/** 只校验'未校验'和'已校验'的报表 */
			hql.append(" and ri.checkFlag in ("
					+ com.fitech.net.config.Config.CHECK_FLAG_AFTERJY + ","
					+ com.fitech.net.config.Config.CHECK_FLAG_AFTERSAVE + ")");

			/** 加上报表时间和报送机构 */
			hql.append(" and ri.orgId='" + reportInForm.getOrgId()
					+ "' and ri.year=" + reportInForm.getYear()
					+ " and ri.term=" + reportInForm.getTerm());

			/** 加上报送权限 */
			if (operator == null)
				return retvals;
			if (operator.isSuperManager() == false) {
				if (operator.getChildRepReportPopedom() == null
						|| operator.getChildRepReportPopedom().equals(""))
					return retvals;
				if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
					hql.append(" and '" + reportInForm.getOrgId()
							+ "'||ri.MChildReport.comp_id.childRepId in ("
							+ operator.getChildRepReportPopedom() + ")");
				if(Config.DB_SERVER_TYPE.equals("sqlserver"))
					hql.append(" and '" + reportInForm.getOrgId()
							+ "'+ri.MChildReport.comp_id.childRepId in ("
							+ operator.getChildRepReportPopedom() + ")");
			}

			conn = new DBConn();
			session = conn.openSession();
			Query query = session.createQuery(hql.toString());

			List list = query.list();
			if (list != null && list.size() > 0) {
				retvals = new ArrayList();
				for (Iterator it = list.iterator(); it.hasNext();) {
					retvals.add((Integer) it.next());
				}
			}
		} catch (HibernateException he) {
			retvals = null;
			log.printStackTrace(he);
		} catch (Exception e) {
			retvals = null;
			log.printStackTrace(e);
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return retvals;
	}

	/***************************************************************************
	 * oracle语法 需要修改 卞以刚 2011-12-21
	 * 机构报表报送情况统计(应报统计)
	 * 
	 * @param orgId
	 *            机构ID
	 * @param reportInForm
	 *            报表formBean
	 * @author jcm
	 * @return OrgNetForm 机构formBean
	 */
	public static int selectOrgReportStatisticsYB(String orgId,
			ReportInForm reportInForm) {
		int count = 0;
		DBConn conn = null;
		Session session = null;

		if (orgId == null || orgId.equals("") || reportInForm == null
				|| reportInForm.getYear() == null
				|| reportInForm.getTerm() == null)
			return count;

		try {
			/** 根据时间判断应报报送频度 */
			int term = reportInForm.getTerm().intValue();
			int year = reportInForm.getYear().intValue();

			String rep_freq = "";
			if (term == 12)
				rep_freq = "('月','季','半年','年')";
			else if (term == 6)
				rep_freq = "('月','季','半年')";
			else if (term == 3 || term == 9)
				rep_freq = "('月','季')";
			else
				rep_freq = "('月')";

			/** 机构报送情况统计SQL(应报统计) */
			/**oracle语法 需要修改 卞以刚 2011-12-21
			 * 已增加数据库判断**/
			String hql="";
			if(Config.DB_SERVER_TYPE.equals("oracle"))
				hql= "select count(*) from ViewMReport viewMR WHERE "
					+ "viewMR.comp_id.orgId='"
					+ orgId
					+ "' and viewMR.repFreqName in "
					+ rep_freq
					+ " and to_date('"
					+ year
					+ "-"
					+ term
					+ "-01','yyyy-mm-dd') "
					+ "between to_date(viewMR.startDate,'yyyy-mm-dd') and to_date(viewMR.endDate,'yyyy-mm-dd')";
			if(Config.DB_SERVER_TYPE.equals("sqlserver"))
				hql= "select count(*) from ViewMReport viewMR WHERE "
					+ "viewMR.comp_id.orgId='"
					+ orgId
					+ "' and viewMR.repFreqName in "
					+ rep_freq
					+ " and convert(datetime,'"
					+ year
					+ "-"
					+ term
					+ "-01',120) "
					+ "between convert(datetime,viewMR.startDate,120) and convert(datetime,viewMR.endDate,120)";
			if(Config.DB_SERVER_TYPE.equals("db2"))
				hql= "select count(*) from ViewMReport viewMR WHERE "
					+ "viewMR.comp_id.orgId='"
					+ orgId
					+ "' and viewMR.repFreqName in "
					+ rep_freq
					+ " and ('"
					+ year
					+ "-"
					+ term
					+ "-01') "
					+ "between date(viewMR.startDate) and date(viewMR.endDate)";
			
			conn = new DBConn();
			session = conn.openSession();

			List list = session.find(hql.toString());
			if (list != null && list.size() > 0) {
				count = ((Integer) list.get(0)).intValue();
			}
		} catch (HibernateException he) {
			count = 0;
			log.printStackTrace(he);
		} catch (Exception e) {
			count = 0;
			log.printStackTrace(e);
		} finally {
			// 关闭数据库连接
			if (conn != null)
				conn.closeSession();
		}
		return count;
	}

	/***************************************************************************
	 * 机构报表报送情况统计(已复核统计)
	 * 
	 * @param orgId
	 *            机构ID
	 * @param reportInForm
	 *            报表formBean
	 * @author jcm
	 * @return OrgNetForm 机构formBean
	 */
/*	public static int selectOrgReportStatisticsFH(String orgId,
			ReportInForm reportInForm) {
		int count = 0;
		DBConn conn = null;
		Session session = null;

		if (orgId == null || orgId.equals("") || reportInForm == null
				|| reportInForm.getYear() == null
				|| reportInForm.getTerm() == null)
			return count;

		try {
			*//** 机构报送情况统计SQL(报送报表复核通过的) *//*
			String hql = "select count(*) from ReportIn ri WHERE "
					+ "ri.times=(select max(r.times) from ReportIn r WHERE "
					+ "r.MChildReport.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and "
					+ "r.MChildReport.comp_id.versionId=ri.MChildReport.comp_id.versionId and "
					+ "r.MDataRgType.dataRangeId=ri.MDataRgType.dataRangeId and r.orgId=ri.orgId and "
					+ "r.MCurr.curId=ri.MCurr.curId and r.year=ri.year and r.term=ri.term and r.checkFlag=ri.checkFlag) and "
					+ "ri.year=" + reportInForm.getYear() + " and ri.term="
					+ reportInForm.getTerm() + " and " + "ri.orgId='" + orgId
					+ "' and ri.checkFlag in ("
					// 加上复核
					+ com.fitech.net.config.Config.CHECK_FLAG_AFTERRECHECK + Config.SPLIT_SYMBOL_COMMA
					+ Config.CHECK_FLAG_PASS + ")";

			conn = new DBConn();
			session = conn.openSession();

			List list = session.find(hql.toString());
			if (list != null && list.size() > 0) {
				count = ((Integer) list.get(0)).intValue();
			}
		} catch (HibernateException he) {
			count = 0;
			log.printStackTrace(he);
		} catch (Exception e) {
			count = 0;
			log.printStackTrace(e);
		} finally {
			// 关闭数据库连接
			if (conn != null)
				conn.closeSession();
		}
		return count;
	}*/

	/***************************************************************************
	 * 已使用hibernate 卞以刚 2011-12-21
	 * 机构报表报送情况统计(已报审签统计)
	 * 
	 * @param orgId
	 *            机构ID
	 * @param reportInForm
	 *            报表formBean
	 * @author jcm
	 * @return OrgNetForm 机构formBean
	 */
	public static int selectOrgReportStatisticsSQ(String orgId,
			ReportInForm reportInForm) {
		int count = 0;
		DBConn conn = null;
		Session session = null;

		if (orgId == null || orgId.equals("") || reportInForm == null
				|| reportInForm.getYear() == null
				|| reportInForm.getTerm() == null)
			return count;

		try {
			/** 机构报送情况统计SQL(报送报表审核通过的) */
			String hql = "select count(*) from ReportIn ri WHERE "
					+ "ri.times=(select max(r.times) from ReportIn r WHERE "
					+ "r.MChildReport.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and "
					+ "r.MChildReport.comp_id.versionId=ri.MChildReport.comp_id.versionId and "
					+ "r.MDataRgType.dataRangeId=ri.MDataRgType.dataRangeId and r.orgId=ri.orgId and "
					+ "r.MCurr.curId=ri.MCurr.curId and r.year=ri.year and r.term=ri.term and r.checkFlag=ri.checkFlag) and "
					+ "ri.year=" + reportInForm.getYear() + " and ri.term="
					+ reportInForm.getTerm() + " and " + "ri.orgId='" + orgId
					+ "' and ri.checkFlag in ("
					+ com.fitech.net.config.Config.CHECK_FLAG_PASS + ")";

			conn = new DBConn();
			session = conn.openSession();

			List list = session.find(hql.toString());
			if (list != null && list.size() > 0) {
				count = ((Integer) list.get(0)).intValue();
			}
		} catch (HibernateException he) {
			count = 0;
			log.printStackTrace(he);
		} catch (Exception e) {
			count = 0;
			log.printStackTrace(e);
		} finally {
			// 关闭数据库连接
			if (conn != null)
				conn.closeSession();
		}
		return count;
	}

	/***************************************************************************
	 * 已使用hibernate 卞以刚 2011-12-21
	 * 机构报表报送情况统计(已报统计)
	 * 
	 * @param orgId
	 *            机构ID
	 * @param reportInForm
	 *            报表formBean
	 * @author jcm
	 * @return 
	 */
	public static int selectOrgReportStatisticsBS(String orgId, ReportInForm reportInForm) {
		int count = 0;
		DBConn conn = null;
		Session session = null;

		if (orgId == null || orgId.equals("") || reportInForm == null
				|| reportInForm.getYear() == null
				|| reportInForm.getTerm() == null)
			return count;

		try {
			/** 机构报送情况统计SQL(报送报表包括未审和审核通过的) */
			String hql = "select count(*) from ReportIn ri WHERE "
					+ "ri.times=(select max(r.times) from ReportIn r WHERE "
					+ "r.MChildReport.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and "
					+ "r.MChildReport.comp_id.versionId=ri.MChildReport.comp_id.versionId and "
					+ "r.MDataRgType.dataRangeId=ri.MDataRgType.dataRangeId and r.orgId=ri.orgId and "
					+ "r.MCurr.curId=ri.MCurr.curId and r.year=ri.year and r.term=ri.term and r.checkFlag=ri.checkFlag) and "
					+ "ri.year=" + reportInForm.getYear() + " and ri.term="
					+ reportInForm.getTerm() + " and " + "ri.orgId='" + orgId
					+ "' and ri.checkFlag in ("
					//0,1,5
					+ com.fitech.net.config.Config.CHECK_FLAG_UNCHECK + Config.SPLIT_SYMBOL_COMMA
					+ Config.CHECK_FLAG_PASS 
					//+ Config.SPLIT_SYMBOL_COMMA + Config.CHECK_FLAG_AFTERRECHECK 
					+")";

			conn = new DBConn();
			session = conn.openSession();

			List list = session.find(hql.toString());
			if (list != null && list.size() > 0) {
				count = ((Integer) list.get(0)).intValue();
			}
		} catch (HibernateException he) {
			count = 0;
			log.printStackTrace(he);
		} catch (Exception e) {
			count = 0;
			log.printStackTrace(e);
		} finally {
			// 关闭数据库连接
			if (conn != null)
				conn.closeSession();
		}
		return count;
	}

	/***************************************************************************
	 * 已使用hibernate 卞以刚 2011-12-21
	 * 机构报表报送情况统计(重报统计)
	 * 
	 * @param orgId
	 *            机构ID
	 * @param reportInForm
	 *            报表formBean
	 * @author jcm
	 * @return OrgNetForm 机构formBean
	 */
	public static int selectOrgReportStatisticsCB(String orgId,ReportInForm reportInForm) {
		
		int count = 0;
		DBConn conn = null;
		Session session = null;

		if (orgId == null || orgId.equals("") || reportInForm == null
				|| reportInForm.getYear() == null
				|| reportInForm.getTerm() == null)
			return count;

		try {
			/** 机构报送情况统计SQL(重报) */
			String hql = "select count(*) from ReportIn ri WHERE "
					+ "ri.times=(select max(r.times) from ReportIn r WHERE "
					+ "r.MChildReport.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and "
					+ "r.MChildReport.comp_id.versionId=ri.MChildReport.comp_id.versionId and "
					+ "r.MDataRgType.dataRangeId=ri.MDataRgType.dataRangeId and r.orgId=ri.orgId and "
					+ "r.MCurr.curId=ri.MCurr.curId and r.year=ri.year and r.term=ri.term and r.checkFlag=ri.checkFlag) and "
					+ "ri.year=" + reportInForm.getYear() + " and ri.term="
					+ reportInForm.getTerm() + " and " + "ri.orgId='" + orgId
					+ "' and ri.forseReportAgainFlag in ("
					+ Config.FORSE_REPORT_AGAIN_FLAG_1 + ")";

			conn = new DBConn();
			session = conn.openSession();

			List list = session.find(hql.toString());
			if (list != null && list.size() > 0) {
				count = ((Integer) list.get(0)).intValue();
			}
		} catch (HibernateException he) {
			count = 0;
			log.printStackTrace(he);
		} catch (Exception e) {
			count = 0;
			log.printStackTrace(e);
		} finally {
			// 关闭数据库连接
			if (conn != null)
				conn.closeSession();
		}
		return count;
	}

	/**
	 * 已使用hibernate 卞以刚 2011-12-21
	 * 批量重报
	 * 
	 * @author gongming
	 * @date 2008-01-18
	 * @param pId
	 *            String 字符串数组
	 * @param cause
	 *            String 批量重报原因
	 * @return 成功返回true 失败返回false
	 */
	public static boolean batchAgain(String pId[], String cause)
			throws HibernateException {
		boolean batch = false;
		DBConn conn = null;
		Session session = null;
		Transaction tx = null;
		try {
			if (pId != null) {
				conn = new DBConn();
				session = conn.openSession();
				tx = session.beginTransaction();
				int length = pId.length;
				ReportIn reportIn = null;
				for (int i = 0; i < length; i++) {
					reportIn = (ReportIn) session.load(ReportIn.class,
							new Integer(pId[i]));
					if (reportIn != null) {
						reportIn
								.setForseReportAgainFlag(Config.FORSE_REPORT_AGAIN_FLAG_1);
						reportIn.setCheckFlag(Short.valueOf("-1"));

						ReportAgainSet reportAgainSet = new ReportAgainSet();
						reportAgainSet.setCause(cause);
						reportAgainSet.setSetDate(new Date());
						reportAgainSet.setRepInId(reportIn.getRepInId());

						session.update(reportIn);
						session.save(reportAgainSet);
					}
				}
				tx.commit();
				session.flush();
				session.close();
				batch = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return batch;
	}

	/**
	 * 疑是oracle语法(upper) 需要修改 卞以刚 2011-12-21
	 * 使用hibernate 
	 * 查询可以导出的数据文件记录数
	 * 
	 * @author jcm
	 * @param reportInForm
	 *            报表FormBean
	 * @param operator
	 *            当前登录用户
	 * @return 可以导出的数据文件记录数
	 */
	public static int getDataRecordsPageCount(ReportInForm reportInForm,
			Operator operator) {
		int count = 0;
		DBConn conn = null;
		Session session = null;

		if (reportInForm == null || operator == null)
			return count;

		try {
			StringBuffer hql = new StringBuffer(
					"select count(*) from ReportIn ri where "
							+ "ri.times=(select max(r.times) from ReportIn r where r.MChildReport.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and "
							+ "r.MChildReport.comp_id.versionId=ri.MChildReport.comp_id.versionId and r.MDataRgType.dataRangeId=ri.MDataRgType.dataRangeId and r.orgId=ri.orgId and "
							+ "r.MCurr.curId=ri.MCurr.curId and r.year=ri.year and r.term=ri.term and r.checkFlag=ri.checkFlag) and ri.orgId='"
							+ operator.getOrgId() + "'");

			/** 查询报表状态为审核通过报表 */
			StringBuffer where = new StringBuffer(" and ri.checkFlag="
					+ com.fitech.net.config.Config.CHECK_FLAG_PASS);

			/** 添加报表编号查询条件（忽略大小写模糊查询） */
			/**疑是oracle语法 需要修改 卞以刚 2011-12-21**/
			if (reportInForm.getChildRepId() != null
					&& !reportInForm.getChildRepId().equals("")) {
				where
						.append(" and upper(ri.MChildReport.comp_id.childRepId) like upper('%"
								+ reportInForm.getChildRepId().trim() + "%')");
			}
			/** 添加报表名称查询条件（模糊查询） */
			if (reportInForm.getRepName() != null
					&& !reportInForm.getRepName().equals("")) {
				where.append(" and ri.repName like '%"
						+ reportInForm.getRepName().trim() + "%'");
			}
			/** 添加模板类型（全部/法人/分支）查询条件 */
			if (reportInForm.getFrOrFzType() != null
					&& !reportInForm.getFrOrFzType().equals(
							Config.DEFAULT_VALUE)) {
				where.append(" and ri.MChildReport.frOrFzType='"
						+ reportInForm.getFrOrFzType() + "'");
			}
			/** 添加报送频度（月/季/半年/年）查询条件 */
			if (reportInForm.getRepFreqId() != null
					&& !String.valueOf(reportInForm.getRepFreqId()).equals(
							Config.DEFAULT_VALUE)) {
				where
						.append(" and ri.MDataRgType.dataRangeId in (select M.comp_id.dataRangeId from MActuRep M "
								+ "where M.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and M.comp_id.versionId=ri.MChildReport.comp_id.versionId "
								+ "and M.comp_id.repFreqId="
								+ reportInForm.getRepFreqId() + ")");
			}
			/** 添加日期（年份）查询条件 */
			if (reportInForm.getYear() != null) {
				where.append(" and ri.year=" + reportInForm.getYear());
			}
			/** 添加日期（月份）查询条件 */
			if (reportInForm.getTerm() != null) {
				where.append(" and ri.term=" + reportInForm.getTerm());
			}

			/** 添加报表查看权限（超级用户不用添加）
			 * 已增加数据库判断 */
			if (operator.isSuperManager() == false) {
				if (operator.getChildRepSearchPopedom() == null
						|| operator.getChildRepSearchPopedom().equals(""))
					return count;
				if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
					where.append(" and '" + operator.getOrgId()
							+ "'||ri.MChildReport.comp_id.childRepId in ("
							+ operator.getChildRepSearchPopedom() + ")");
				if(Config.DB_SERVER_TYPE.equals("sqlserver"))
					where.append(" and '" + operator.getOrgId()
							+ "'+ri.MChildReport.comp_id.childRepId in ("
							+ operator.getChildRepSearchPopedom() + ")");
			}
			hql.append(where.toString());

			conn = new DBConn();
			session = conn.openSession();
			List list = session.find(hql.toString());
			if (list != null && list.size() > 0)
				count = ((Integer) list.get(0)).intValue();
		} catch (Exception e) {
			count = 0;
			log.printStackTrace(e);
		} finally {
			// 关闭数据库连接
			if (conn != null)
				conn.closeSession();
		}
		return count;
	}

	/**
	 * 疑是oracle语法(upper) 需要修改 卞以刚 2011-12-21
	 * 使用hibernate
	 * 查询可以导出的数据文件记录数
	 * 
	 * @param reportInForm
	 *            报表FormBean
	 * @param offset
	 *            偏移量
	 * @param limit
	 *            每页显示记录数
	 * @param operator
	 *            当前登录用户
	 * @return List 可以导出的数据文件结果集
	 */
	public static List getDataRecordsPageUse(ReportInForm reportInForm,
			int offset, int limit, Operator operator) {
		List resList = null;
		DBConn conn = null;
		Session session = null;

		if (reportInForm == null || operator == null)
			return resList;

		try {
			StringBuffer hql = new StringBuffer(
					"from ReportIn ri where "
							+ "ri.times=(select max(r.times) from ReportIn r where r.MChildReport.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and "
							+ "r.MChildReport.comp_id.versionId=ri.MChildReport.comp_id.versionId and r.MDataRgType.dataRangeId=ri.MDataRgType.dataRangeId and r.orgId=ri.orgId and "
							+ "r.MCurr.curId=ri.MCurr.curId and r.year=ri.year and r.term=ri.term and r.checkFlag=ri.checkFlag) and ri.orgId='"
							+ operator.getOrgId() + "'");

			/** 查询报表状态为审核通过报表 */
			StringBuffer where = new StringBuffer(" and ri.checkFlag="
					+ com.fitech.net.config.Config.CHECK_FLAG_PASS);

			/** 添加报表编号查询条件（忽略大小写模糊查询） */
			/**疑是oracle语法 需要修改 卞以刚 2011-12-21**/
			if (reportInForm.getChildRepId() != null
					&& !reportInForm.getChildRepId().equals("")) {
				where
						.append(" and upper(ri.MChildReport.comp_id.childRepId) like upper('%"
								+ reportInForm.getChildRepId().trim() + "%')");
			}
			/** 添加报表名称查询条件（模糊查询） */
			if (reportInForm.getRepName() != null
					&& !reportInForm.getRepName().equals("")) {
				where.append(" and ri.repName like '%"
						+ reportInForm.getRepName().trim() + "%'");
			}
			/** 添加模板类型（全部/法人/分支）查询条件 */
			if (reportInForm.getFrOrFzType() != null
					&& !reportInForm.getFrOrFzType().equals(
							Config.DEFAULT_VALUE)) {
				where.append(" and ri.MChildReport.frOrFzType='"
						+ reportInForm.getFrOrFzType() + "'");
			}
			/** 添加报送频度（月/季/半年/年）查询条件 */
			if (reportInForm.getRepFreqId() != null
					&& !String.valueOf(reportInForm.getRepFreqId()).equals(
							Config.DEFAULT_VALUE)) {
				where
						.append(" and ri.MDataRgType.dataRangeId in (select M.comp_id.dataRangeId from MActuRep M "
								+ "where M.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and M.comp_id.versionId=ri.MChildReport.comp_id.versionId "
								+ "and M.comp_id.repFreqId="
								+ reportInForm.getRepFreqId() + ")");
			}
			/** 添加日期（年份）查询条件 */
			if (reportInForm.getYear() != null) {
				where.append(" and ri.year=" + reportInForm.getYear());
			}
			/** 添加日期（月份）查询条件 */
			if (reportInForm.getTerm() != null) {
				where.append(" and ri.term=" + reportInForm.getTerm());
			}

			/** 添加报表查看权限（超级用户不用添加） 
			 * 已增加数据库判断*/
			if (operator.isSuperManager() == false) {
				if (operator.getChildRepSearchPopedom() == null
						|| operator.getChildRepSearchPopedom().equals(""))
					return resList;
				if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
					where.append(" and '" + operator.getOrgId()
							+ "'||ri.MChildReport.comp_id.childRepId in ("
							+ operator.getChildRepSearchPopedom() + ")");
				if(Config.DB_SERVER_TYPE.equals("sqlserver"))
					where.append(" and '" + operator.getOrgId()
							+ "'+ri.MChildReport.comp_id.childRepId in ("
							+ operator.getChildRepSearchPopedom() + ")");
			}
			hql
					.append(where.toString()
							+ " order by ri.year,ri.term,ri.MChildReport.comp_id.childRepId");

			conn = new DBConn();
			session = conn.openSession();
			Query query = session.createQuery(hql.toString());
			query.setFirstResult(offset).setMaxResults(limit);
			List list = query.list();
			if (list != null && list.size() > 0) {
				resList = new ArrayList();
				ReportIn reportInRecord = null;
				Aditing aditing = null;

				for (Iterator it = list.iterator(); it.hasNext();) {
					aditing = new Aditing();
					reportInRecord = (ReportIn) it.next();
					// 设置报表ID标识符
					aditing.setRepInId(reportInRecord.getRepInId());
					// 设置报表年份
					aditing.setYear(reportInRecord.getYear());
					// 设置报表期数
					aditing.setTerm(reportInRecord.getTerm());
					// 设置报表名称
					aditing.setRepName(reportInRecord.getRepName());
					// 设置报表报送日期
					aditing.setReportDate(reportInRecord.getReportDate());
					// 设置报表编号
					aditing.setChildRepId(reportInRecord.getMChildReport()
							.getComp_id().getChildRepId());
					// 设置报表版本号
					aditing.setVersionId(reportInRecord.getMChildReport()
							.getComp_id().getVersionId());
					// 设置报表币种名称
					aditing.setCurrName(reportInRecord.getMCurr().getCurName());
					MActuRep mActuRep = GetFreR(reportInRecord);
					if (mActuRep != null) {
						// 设置报送口径
						aditing.setDataRgTypeName(mActuRep.getMDataRgType()
								.getDataRgDesc());
						// 设置报送频度
						aditing.setActuFreqName(mActuRep.getMRepFreq()
								.getRepFreqName());
					}
					resList.add(aditing);
				}
			}
		} catch (Exception e) {
			resList = null;
			log.printStackTrace(e);
		} finally {
			// 关闭数据库连接
			if (conn != null)
				conn.closeSession();
		}
		return resList;
	}

	/**
	 * 疑是oracle用法(upper) 需要修改 卞以刚 2011-12-21
	 * 报表复核的查找总记录数
	 * 
	 * @param reportInForm
	 * @param operator
	 * @return int 总记录数
	 */
	public static int getRecordCountOfmanualRecheck(ReportInForm reportInForm,
			Operator operator) {
		int count = 0;
		DBConn conn = null;
		Session session = null;

		if (reportInForm == null || operator == null)
			return count;

		try {
			// 查询条件HQL的生成
			StringBuffer hql = new StringBuffer(
					"select count(*) from ReportIn ri WHERE "
							+ "ri.times=(select max(r.times) from ReportIn r where r.MChildReport.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and "
							+ "r.MChildReport.comp_id.versionId=ri.MChildReport.comp_id.versionId and r.MDataRgType.dataRangeId=ri.MDataRgType.dataRangeId and r.orgId=ri.orgId and "
							+ "r.MCurr.curId=ri.MCurr.curId and r.year=ri.year and r.term=ri.term and r.checkFlag=ri.checkFlag)");

			/** 查询报表状态为未复核报表 */
			StringBuffer where = new StringBuffer(" and ri.checkFlag="
					+ com.fitech.net.config.Config.CHECK_FLAG_UNCHECK);

			/** 添加报表编号查询条件（忽略大小写模糊查询） */
			/**疑是oracle用法 需要修改 卞以刚 2011-12-21**/
			if (reportInForm.getChildRepId() != null
					&& !reportInForm.getChildRepId().equals("")) {
				where
						.append(" and upper(ri.MChildReport.comp_id.childRepId) like upper('%"
								+ reportInForm.getChildRepId().trim() + "%')");
			}
			/** 添加报表名称查询条件（模糊查询） */
			if (reportInForm.getRepName() != null
					&& !reportInForm.getRepName().equals("")) {
				where.append(" and ri.repName like '%"
						+ reportInForm.getRepName().trim() + "%'");
			}
			/** 添加模板类型（全部/法人/分支）查询条件 */
			if (reportInForm.getFrOrFzType() != null
					&& !reportInForm.getFrOrFzType().equals(
							Config.DEFAULT_VALUE)) {
				where.append(" and ri.MChildReport.frOrFzType='"
						+ reportInForm.getFrOrFzType() + "'");
			}
			/** 添加报送频度（月/季/半年/年）查询条件 */
			if (reportInForm.getRepFreqId() != null
					&& !String.valueOf(reportInForm.getRepFreqId()).equals(
							Config.DEFAULT_VALUE)) {
				where
						.append(" and ri.MDataRgType.dataRangeId in (select M.comp_id.dataRangeId from MActuRep M "
								+ "where M.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and M.comp_id.versionId=ri.MChildReport.comp_id.versionId "
								+ "and M.comp_id.repFreqId="
								+ reportInForm.getRepFreqId() + ")");
			}
			/** 添加日期（年份）查询条件 */
			if (reportInForm.getYear() != null) {
				where.append(" and ri.year=" + reportInForm.getYear());
			}
			/** 添加日期（月份）查询条件 */
			if (reportInForm.getTerm() != null) {
				where.append(" and ri.term=" + reportInForm.getTerm());
			}
			/** 添加机构查询条件 */
			if (!StringUtil.isEmpty(reportInForm.getOrgId()) 
					&& !reportInForm.getOrgId().equals(Config.DEFAULT_VALUE)) {
				where.append(" and ri.orgId='" + reportInForm.getOrgId().trim()
						+ "'");
			}

			/** 添加报表fu核权限（超级用户不用添加）
			 * 已增加数据库判断 */
			if (operator.isSuperManager() == false) {
				if (operator.getChildRepVerifyPopedom() == null
						|| operator.getChildRepVerifyPopedom().equals(""))
					return count;
				if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
					where.append(" and ltrim(rtrim(ri.orgId)) || ri.MChildReport.comp_id.childRepId in("
								+ operator.getChildRepVerifyPopedom() + ")");
				if(Config.DB_SERVER_TYPE.equals("sqlserver"))
					where.append(" and ltrim(rtrim(ri.orgId)) + ri.MChildReport.comp_id.childRepId in("
								+ operator.getChildRepVerifyPopedom() + ")");
			}
			hql.append(where.toString());

			conn = new DBConn();
			session = conn.openSession();
			List list = session.find(hql.toString());
			if (list != null && list.size() > 0)
				count = ((Integer) list.get(0)).intValue();
		} catch (HibernateException he) {
			count = 0;
			log.printStackTrace(he);
		} catch (Exception e) {
			count = 0;
			log.printStackTrace(e);
		} finally {
			// 关闭数据库连接
			if (conn != null)
				conn.closeSession();
		}
		return count;
	}

	/**
	 * 疑是oracle语法(upper) 需要修改 卞以刚 2011-12-21
	 * 使用hibernate
	 * 报表复核的查找记录方法
	 * 
	 * @param reportInForm
	 *            页面FormBean
	 * @param offset
	 *            数据库查询起始位
	 * @param limit
	 *            查询记录数
	 * @param operator
	 *            当前登录用户
	 * @return List 查询报表结果集
	 */
	public static List selectOfManualRecheck(ReportInForm reportInForm,
			int offset, int limit, Operator operator) {
		List resList = null;
		DBConn conn = null;
		Session session = null;

		if (reportInForm == null || operator == null)
			return resList;

		try {
			// 查询条件HQL的生成
			StringBuffer hql = new StringBuffer(
					"from ReportIn ri WHERE "
							+ "ri.times=(select max(r.times) from ReportIn r where r.MChildReport.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and "
							+ "r.MChildReport.comp_id.versionId=ri.MChildReport.comp_id.versionId and r.MDataRgType.dataRangeId=ri.MDataRgType.dataRangeId and r.orgId=ri.orgId and "
							+ "r.MCurr.curId=ri.MCurr.curId and r.year=ri.year and r.term=ri.term and r.checkFlag=ri.checkFlag)");

			/** 查询报表状态为未复核报表 */
			StringBuffer where = new StringBuffer(" and ri.checkFlag="
					+ com.fitech.net.config.Config.CHECK_FLAG_UNCHECK);

			/** 添加报表编号查询条件（忽略大小写模糊查询） */
			/**疑是oracle语法 需要修改 卞以刚 2011-12-21**/
			if (reportInForm.getChildRepId() != null
					&& !reportInForm.getChildRepId().equals("")) {
				where
						.append(" and upper(ri.MChildReport.comp_id.childRepId) like upper('%"
								+ reportInForm.getChildRepId().trim() + "%')");
			}
			/** 添加报表名称查询条件（模糊查询） */
			if (reportInForm.getRepName() != null
					&& !reportInForm.getRepName().equals("")) {
				where.append(" and ri.repName like '%"
						+ reportInForm.getRepName().trim() + "%'");
			}
			/** 添加模板类型（全部/法人/分支）查询条件 */
			if (reportInForm.getFrOrFzType() != null
					&& !reportInForm.getFrOrFzType().equals(
							Config.DEFAULT_VALUE)) {
				where.append(" and ri.MChildReport.frOrFzType='"
						+ reportInForm.getFrOrFzType() + "'");
			}
			/** 添加报送频度（月/季/半年/年）查询条件 */
			if (reportInForm.getRepFreqId() != null
					&& !String.valueOf(reportInForm.getRepFreqId()).equals(
							Config.DEFAULT_VALUE)) {
				where
						.append(" and ri.MDataRgType.dataRangeId in (select M.comp_id.dataRangeId from MActuRep M "
								+ "where M.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and M.comp_id.versionId=ri.MChildReport.comp_id.versionId "
								+ "and M.comp_id.repFreqId="
								+ reportInForm.getRepFreqId() + ")");
			}
			/** 添加日期（年份）查询条件 */
			if (reportInForm.getYear() != null) {
				where.append(" and ri.year=" + reportInForm.getYear());
			}
			/** 添加日期（月份）查询条件 */
			if (reportInForm.getTerm() != null) {
				where.append(" and ri.term=" + reportInForm.getTerm());
			}
			/** 添加机构查询条件 */
			if (!StringUtil.isEmpty(reportInForm.getOrgId())
					&& !reportInForm.getOrgId().equals(Config.DEFAULT_VALUE)) {
				where.append(" and ri.orgId='" + reportInForm.getOrgId().trim()
						+ "'");
			}

			/** 添加报表fu核权限（超级用户不用添加） 
			 * 已增加数据库判断*/
			if (operator.isSuperManager() == false) {
				if (operator.getChildRepVerifyPopedom() == null
						|| operator.getChildRepVerifyPopedom().equals(""))
					return resList;
				if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
					where.append(" and ltrim(rtrim(ri.orgId)) || ri.MChildReport.comp_id.childRepId in("
								+ operator.getChildRepVerifyPopedom() + ")");
				if(Config.DB_SERVER_TYPE.equals("sqlserver"))
					where.append(" and ltrim(rtrim(ri.orgId)) + ri.MChildReport.comp_id.childRepId in("
								+ operator.getChildRepVerifyPopedom() + ")");
			}
			hql
					.append(where.toString()
							+ " order by ri.tblOuterValidateFlag,ri.year,ri.term,ri.orgId");

			conn = new DBConn();
			session = conn.openSession();
			Query query = session.createQuery(hql.toString());
			query.setFirstResult(offset).setMaxResults(limit);
			List list = query.list();

			if (list != null && list.size() > 0) {
				resList = new ArrayList();
				Aditing aditing = null;
				OrgNet orgNet = null;
				ReportIn reportInRecord = null;
				for (Iterator it = list.iterator(); it.hasNext();) {
					aditing = new Aditing();
					reportInRecord = (ReportIn) it.next();
					// 设置报表审核状态
					aditing.setCheckFlag(reportInRecord.getCheckFlag());
					// 设置表间校验结果
					if (reportInRecord.getTblOuterValidateFlag() != null)
						aditing.setTblOuterValidateFlag(reportInRecord
								.getTblOuterValidateFlag());
					// 设置报送机构名称
					/**已用hibernate 卞以刚 2011-12-21**/
					orgNet = StrutsOrgNetDelegate.selectOne(reportInRecord
							.getOrgId());
					if (orgNet != null){
						aditing.setOrgName(orgNet.getOrgName());
						aditing.setOrgId(orgNet.getOrgId());
					}
					// 设置报表ID标识符
					aditing.setRepInId(reportInRecord.getRepInId());
					// 设置报表年份
					aditing.setYear(reportInRecord.getYear());
					// 设置报表期数
					aditing.setTerm(reportInRecord.getTerm());
					// 设置报表名称
					aditing.setRepName(reportInRecord.getRepName());
					// 设置报表报送日期
					aditing.setReportDate(reportInRecord.getReportDate());
					// 设置报表编号
					aditing.setChildRepId(reportInRecord.getMChildReport()
							.getComp_id().getChildRepId());
					// 设置报表版本号
					aditing.setVersionId(reportInRecord.getMChildReport()
							.getComp_id().getVersionId());
					// 设置异常变化标志
					aditing.setAbmormityChangeFlag(reportInRecord
							.getAbmormityChangeFlag());
					// 设置报表币种名称
					aditing.setCurrName(reportInRecord.getMCurr().getCurName());
					aditing.setCurId(reportInRecord.getMCurr().getCurId());
					
					MActuRep mActuRep = GetFreR(reportInRecord);
					if (mActuRep != null) {
						// 设置报送口径
						aditing.setDataRgTypeName(mActuRep.getMDataRgType()
								.getDataRgDesc());
						// 设置报送口径
						aditing.setDataRangeId(mActuRep.getMDataRgType()
								.getDataRangeId());
						// 设置报送频度
						aditing.setActuFreqName(mActuRep.getMRepFreq()
								.getRepFreqName());
						// 设置报送频度
						aditing.setActuFreqID(mActuRep.getMRepFreq()
								.getRepFreqId());
					}
					resList.add(aditing);
				}
			}
		} catch (HibernateException he) {
			resList = null;
			log.printStackTrace(he);
		} catch (Exception e) {
			resList = null;
			log.printStackTrace(e);
		} finally {
			// 关闭数据库连接
			if (conn != null)
				conn.closeSession();
		}
		return resList;
	}

	/**
	 * 疑是oracle语法(upper) 需要修改 卞以刚 2011-12-21
	 * 使用hibernate
	 * 复核通过全部报表
	 * 
	 * @author jcm
	 * @param reportInForm
	 *            报表FormBean
	 * @param operator
	 *            当前登录用户
	 * @return List 查询结果集
	 */
	public static List selectAllRepIdRecheck(ReportInForm reportInForm,
			Operator operator) throws Exception {
		List resList = null;
		DBConn conn = null;
		Session session = null;

		if (reportInForm == null || operator == null)
			return resList;

		try {
			StringBuffer hql = new StringBuffer(
					"from ReportIn ri WHERE "
							+ "ri.times=(select max(r.times) from ReportIn r where r.MChildReport.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and "
							+ "r.MChildReport.comp_id.versionId=ri.MChildReport.comp_id.versionId and r.MDataRgType.dataRangeId=ri.MDataRgType.dataRangeId and r.orgId=ri.orgId and "
							+ "r.MCurr.curId=ri.MCurr.curId and r.year=ri.year and r.term=ri.term and r.checkFlag=ri.checkFlag)");

			/** 查询报表状态为未审核报表 */
			StringBuffer where = new StringBuffer(" and ri.checkFlag="
					+ com.fitech.net.config.Config.CHECK_FLAG_UNCHECK);

			/** 添加报表编号查询条件（忽略大小写模糊查询） */
			/**疑是oracle语法(upper) 需要修改 卞以刚 2011-12-21**/
			if (reportInForm.getChildRepId() != null
					&& !reportInForm.getChildRepId().equals("")) {
				where
						.append(" and upper(ri.MChildReport.comp_id.childRepId) like upper('%"
								+ reportInForm.getChildRepId().trim() + "%')");
			}
			/** 添加报表名称查询条件（模糊查询） */
			if (reportInForm.getRepName() != null
					&& !reportInForm.getRepName().equals("")) {
				where.append(" and ri.repName like '%"
						+ reportInForm.getRepName().trim() + "%'");
			}
			/** 添加模板类型（全部/法人/分支）查询条件 */
			if (reportInForm.getFrOrFzType() != null
					&& !reportInForm.getFrOrFzType().equals(
							Config.DEFAULT_VALUE)) {
				where.append(" and ri.MChildReport.frOrFzType='"
						+ reportInForm.getFrOrFzType() + "'");
			}
			/** 添加报送频度（月/季/半年/年）查询条件 */
			if (reportInForm.getRepFreqId() != null
					&& !String.valueOf(reportInForm.getRepFreqId()).equals(
							Config.DEFAULT_VALUE)) {
				where
						.append(" and ri.MDataRgType.dataRangeId in (select M.comp_id.dataRangeId from MActuRep M "
								+ "where M.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and M.comp_id.versionId=ri.MChildReport.comp_id.versionId "
								+ "and M.comp_id.repFreqId="
								+ reportInForm.getRepFreqId() + ")");
			}
			/** 添加日期（年份）查询条件 */
			if (reportInForm.getYear() != null) {
				where.append(" and ri.year=" + reportInForm.getYear());
			}
			/** 添加日期（月份）查询条件 */
			if (reportInForm.getTerm() != null) {
				where.append(" and ri.term=" + reportInForm.getTerm());
			}
			/** 添加机构查询条件 */
			if (reportInForm.getOrgId() != null
					&& !reportInForm.getOrgId().equals(Config.DEFAULT_VALUE)) {
				where.append(" and ri.orgId='" + reportInForm.getOrgId().trim()
						+ "'");
			}

			/** 添加报表审核权限（超级用户不用添加） 
			 * 已增加数据库判断*/
			if (operator.isSuperManager() == false) {
				if (operator.getChildRepCheckPodedom() == null
						|| operator.getChildRepCheckPodedom().equals(""))
					return resList;
				if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
					where.append(" and ltrim(rtrim(ri.orgId)) || ri.MChildReport.comp_id.childRepId in("
								+ operator.getChildRepCheckPodedom() + ")");
				if(Config.DB_SERVER_TYPE.equals("sqlserver"))
					where.append(" and ltrim(rtrim(ri.orgId)) + ri.MChildReport.comp_id.childRepId in("
								+ operator.getChildRepCheckPodedom() + ")");
			}
			hql.append(where.toString());

			conn = new DBConn();
			session = conn.openSession();
			Query query = session.createQuery(hql.toString());

			List list = query.list();
			if (list != null && list.size() > 0) {
				resList = new ArrayList();
				ReportIn reportIn = null;
				for (Iterator it = list.iterator(); it.hasNext();) {
					reportIn = (ReportIn) it.next();
					resList.add(reportIn.getRepInId());
				}
			}
		} catch (HibernateException he) {
			resList = null;
			log.printStackTrace(he);
		} catch (Exception e) {
			resList = null;
			log.printStackTrace(e);
		} finally {
			// 关闭数据库连接
			if (conn != null)
				conn.closeSession();
		}
		return resList;
	}

    /**
     * 已使用hibernate 卞以刚 2011-12-21
     * 根据参数获得报表
     * @param reportInForm
     * @param operator
     * @param checkFlag
     * @return
     */
    public static List reportReports(ReportInForm reportInForm, Operator operator,Integer checkFlag)throws Exception {
    	
		List result = null;
		
		DBConn conn = null;
		Session session = null;
		
		try {

			conn = new DBConn();
			session = conn.openSession();
			
			int term = reportInForm.getTerm().intValue();
			int year = reportInForm.getYear().intValue();

//			String rep_freq = "";
//			if (term == 12)
//				rep_freq = "('月','季','半年','年')";
//			else if (term == 6)
//				rep_freq = "('月','季','半年')";
//			else if (term == 3 || term == 9)
//				rep_freq = "('月','季')";
//			else
//				rep_freq = "('月')";
//			String strTerm =String.valueOf(term);
//			if(term<10)  
//			 strTerm ="0"+term;
			
			String hql="from ReportIn a where a.year=" + year 
				+ " and a.term=" + term 
//				+ " and a.repFreqName in"+rep_freq
				+ " and a.checkFlag=" + checkFlag
				+ " and times=1"
				+ " and a.orgId='"+operator.getOrgId()+"'";
			
			/**	加报表名 */
			if(reportInForm.getRepName() != null){
				hql += " and a.repName like '%" + reportInForm.getRepName() + "%'";
			}
			/**加上报送权限
			 * 已增加数据库判断*/
			if(operator.isSuperManager() == false){
				if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
					hql += " and '" + operator.getOrgId() + "'||a.MChildReport.comp_id.childRepId in ("+ operator.getChildRepReportPopedom() +")";
				if(Config.DB_SERVER_TYPE.equals("sqlserver"))
					hql += " and '" + operator.getOrgId() + "'+a.MChildReport.comp_id.childRepId in ("+ operator.getChildRepReportPopedom() +")";
			}
			
			Query query = session.createQuery(hql);
			
			List list = query.list();
    		
			
			if(list!=null && list.size()>0){
				result = new ArrayList();
				result = list;
			}

			
		} catch (Exception he) {
			log.printStackTrace(he);
			throw he;

		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return result;
	}
	
	/**
	 * 已使用hibernate 卞以刚 2011-12-21
	 * 根据前台页面的查询结果将用户选中更改的记录写复核标志为5
	 * 
	 * @author 唐磊
	 * @param orgArray
	 *            传过来的机构id的string型数组变量
	 * @param checkSign
	 *            传过来的用户选择的审核标志
	 * @param reportInPersistence
	 *            持久化对象ReportIn
	 * @param he
	 *            HibernateException 有异常则捕捉抛出
	 * @param e
	 *            Exception 有异常则捕捉抛出
	 * @param result
	 *            boolean型变量,更新成功返回true,否则返回false
	 */
	public static boolean updateRecheck(
			com.cbrc.smis.form.ReportInForm reportInForm) throws Exception {
		// 更新标志
		boolean result = true;
		// 创建连接
		DBConn conn = null;
		Session session = null;
		// reportform中是否有参数
		if (reportInForm == null) {
			return false;
		}

		try {
			conn = new DBConn();
			session = conn.beginTransaction();

			// 循环给持久化对象传递form对象的机构id数组的参数
			if (reportInForm.getRepInIdArray() != null
					&& !reportInForm.getRepInIdArray().equals("")) {
				Integer repOutIds[] = new Integer[reportInForm
						.getRepInIdArray().length];
				for (int i = 0; i < reportInForm.getRepInIdArray().length; i++) {
					ReportIn reportInPersistence = (ReportIn) session.load(
							ReportIn.class, reportInForm.getRepInIdArray()[i]);
					reportInPersistence.setCheckFlag(reportInForm
							.getCheckFlag());

					if (reportInForm
							.getCheckFlag()
							.equals("-5"
									//com.fitech.net.config.Config.CHECK_FLAG_RECHECKFAILED
									)) {
						reportInPersistence.setTblInnerValidateFlag(null);
						reportInPersistence.setTblOuterValidateFlag(null);
					}
					if (reportInForm.getForseReportAgainFlag() != null)
						reportInPersistence
								.setForseReportAgainFlag(reportInForm
										.getForseReportAgainFlag());

					session.update(reportInPersistence);
					session.flush();
					repOutIds[i] = reportInPersistence.getRepOutId();
				}
				reportInForm.setRepOutIds(repOutIds);
			} else {
				result = false;
			}

			// 异常的捕捉机制
		} catch (HibernateException he) {
			result = false;
			log.printStackTrace(he);
		} catch (Exception e) {
			result = false;
			log.printStackTrace(e);
		} finally {
			// 如果conn对象依然存在,则结束当前事务并断开连接
			if (conn != null)
				conn.endTransaction(result);
		}
		// 更新成功返回吧!!!
		return result;
	}
	
	/***
	 * 已使用hibernate 卞以刚 2011-12-21
	 * @param allowReportList
	 * @param dateString
	 * @return
	 */
	public static boolean updateMoreReportIn(List allowReportList, String dateString) {


		boolean bool = false;
		DBConn dBConn = null;
		Session session = null;
		Date date = null;

		if (dateString.equals(""))
			dateString = null;
		if (dateString != null) {
			try {
				date = DateUtil
						.getDateByString(dateString, DateUtil.NORMALDATE);
			} catch (Exception e) {
				date = null;
			}
		}
		try {
			dBConn = new DBConn();
			session = dBConn.beginTransaction();
			for(int i=0;i<allowReportList.size();i++){
				ReportIn reportIn = (ReportIn) allowReportList.get(i);
				reportIn
				.setCheckFlag(com.fitech.net.config.Config.CHECK_FLAG_UNCHECK);
				reportIn.setReportDate(date);
				session.update(reportIn);
			}
			
			dBConn.endTransaction(true);
			bool = true;
		} catch (Exception e) {
			bool = false;
			dBConn.endTransaction(false);
		} finally {
			if (session != null) {
				dBConn.closeSession();
			}
		}
		return bool;
	
	}

}
