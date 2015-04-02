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
 * @author����
 */
public class StrutsReportInDelegate {
	private static FitechException log = new FitechException(
			StrutsReportInDelegate.class);

	public static boolean hidden(ReportInForm reportInForm) throws Exception {
		// �ñ�־result
		boolean result = false;
		// ���ӺͻỰ����ĳ�ʼ��
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
				// �Ự����ɾ���־ò����
				session.delete(reportIn);

			}
			session.flush();
			result = true;
		} catch (Exception he) {
			// ��׽������쳣,�׳�
			result = false;
			log.printStackTrace(he);
		} finally {
			// �����������Ͽ����ӣ������Ự������
			if (conn != null)
				conn.endTransaction(result);
		}
		return result;
	}
	
	/***
	 * ����id��ȡreportin��Ϣ ���Ը� 2012-01-20
	 * @param repid
	 * @return
	 */
	public static ReportIn getReportInByReportInId(Integer repid)
	{
		// ���ӺͻỰ����ĳ�ʼ��
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
	 * ��������ReportIn ���Ը� 2012-01-20
	 * @param reportIn
	 * @return
	 */
	public static boolean updateReportIn(ReportIn reportIn)
	{
		// �ñ�־result
		boolean result = false;
		// ���ӺͻỰ����ĳ�ʼ��
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
				// �����������Ͽ����ӣ������Ự������
				if (conn != null)
					conn.endTransaction(result);
			}
		}
		return result;
	}

	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-22
	 * Ӱ�����ReportIn
	 * ��ȡʵ�����ݱ�����Ϣ
	 * 
	 * @author rds
	 * @serialData 2005-12-18
	 * 
	 * @param repInId
	 *            Integer ʵ�����ݱ���ID
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
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * ��������������ñ����¼����Щ��������Ψһȷ��һ�ű���
	 * 
	 * @author Yao
	 * 
	 * @param childRepId
	 *            ����ID
	 * @param versionId
	 *            �汾��
	 * @param orgId
	 *            ����ID
	 * @param year
	 *            ���
	 * @param term
	 *            �·�
	 * @param dataRangeId
	 *            ���ݷ�ΧID
	 * @param curId
	 *            ����ID
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
			// ��ѯ�Ƿ�������ؼ�¼��Ϣ
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

			// ������ݿ����Ѿ������˸�����¼����ֱ�ӷ��ظļ�¼����Ϣ�������µĲ��������������������
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
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * ���汨�������Ϣ(��ǰ���������򸲸Ǹļ�¼)
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

			// ��ѯ���ݿ����Ƿ��Ѿ���������ͬ��¼
			/**��ʹ��hibernate ���Ը� 2011-12-21**/
			reportIn = getReportIn(childRepId, versionId, orgId, year, term,
					dataRangeId, curId, new Integer(1));
			// ������ݿ����Ѿ������˸�����¼����ֱ�ӷ��ظļ�¼����Ϣ�������µĲ��������������������
			if (reportIn != null) {
				result = true;
				return reportIn;
			}
			// ����������µ���Ϣ
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
	 * ���汨�������Ϣ(��ǰ���������򸲸Ǹļ�¼)
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

			// ��ѯ���ݿ����Ƿ��Ѿ���������ͬ��¼
			reportIn = getReportIn(childRepId, versionId, orgId, year, term,
					dataRangeId, curId, new Integer(-2));
			// ������ݿ����Ѿ������˸�����¼����ֱ�ӷ��ظļ�¼����Ϣ�������µĲ��������������������
			if (reportIn != null) {
				result = true;
				return reportIn;
			}
			// ����������µ���Ϣ
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
	 * ����ǰ̨ҳ��Ĳ�ѯ������û�ѡ�и��ĵļ�¼д��˱�־Ϊ1
	 * 
	 * @author ����
	 * @param orgArray
	 *            �������Ļ���id��string���������
	 * @param checkSign
	 *            ���������û�ѡ�����˱�־
	 * @param reportInPersistence
	 *            �־û�����ReportIn
	 * @param he
	 *            HibernateException ���쳣��׽�׳�
	 * @param e
	 *            Exception ���쳣��׽�׳�
	 * @param result
	 *            boolean�ͱ���,���³ɹ�����true,���򷵻�false
	 */
	public static boolean update(com.cbrc.smis.form.ReportInForm reportInForm)
			throws Exception {
		// ���±�־
		boolean result = true;
		// ��������
		DBConn conn = null;
		Session session = null;
		// reportform���Ƿ��в���
		if (reportInForm == null) {
			return false;
		}

		try {
			conn = new DBConn();
			session = conn.beginTransaction();

			// ѭ�����־û����󴫵�form����Ļ���id����Ĳ���
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
					// ��������ʱ��
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

			// �쳣�Ĳ�׽����
		} catch (HibernateException he) {
			result = false;
			log.printStackTrace(he);
		} catch (Exception e) {
			result = false;
			log.printStackTrace(e);
		} finally {
			// ���conn������Ȼ����,�������ǰ���񲢶Ͽ�����
			if (conn != null)
				conn.endTransaction(result);
		}
		// ���³ɹ����ذ�!!!
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
	 * ɾ�����������Ϣ���ݣ����������������£�
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
	 * �÷������ڽ�һ��ʵ���ӱ������־û������ݿ���ȥ
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

		reportIn.setMChildReport(mcr); // ���ӱ���SET��ʵ�ʱ��������

		reportIn.setMCurr(mCurr); // ������SET��ʵ�ʱ��������

		reportIn.setMRepRange(mrr); // ���������÷�ΧSET��ʵ�ʱ��������

		reportIn.setMDataRgType(mdrt); // �����ݷ�ΧSET��ʵ�ʱ��������

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

		reportIn.setRepOutId(repOutId); // �������ݼ�¼ID

		reportIn.setReportDataWarehouseFlag(new Short("0")); // ��ʼ�����ݲֿ�ı�־

		reportIn.setForseReportAgainFlag(new Short("0")); // ��ʼ�������ر����־

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

			String errorMessage = "�������ڴ洢" + zipFileName + "���ļ��е�";

			errorMessage = errorMessage + xmlFileName + "�ļ�";

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
	 * @author cb ��ʵ�ʱ�����޸�
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
	 * ��������Ĳ������ж��ж�һ��ʵ�ʱ��Ƿ��Ѿ�¼�����
	 * 
	 * @param childRepId
	 *            �ӱ���ID
	 * @param versionId
	 *            �汾��
	 * @param orgId
	 *            ����ID
	 * @param mCurr
	 *            ������
	 * @param dataRangeId
	 *            ���ݷ�Χ
	 * @param year
	 *            ���
	 * @param term
	 *            ����
	 * @param times
	 *            ����
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

		boolean isre = false; // Ĭ�ϲ��ظ�

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

				System.out.println("������");

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
	 * ȡ����Ҫת��xml�ı�����Ϣ
	 * 
	 * @author Ҧ��
	 * @return List ������Ҫת��xml�ı�����Ϣ
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
	 * ���������ݲֿ��־�ı�
	 * 
	 * @author Ҧ��
	 * @param ʵ���ӱ���id
	 * @return �Ƿ�ɹ�
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
	 * �ر������ѯ����ѯ�������ر��ı����¼����
	 * 
	 * @param reportInForm
	 *            ����FormBean
	 * @param operator
	 *            ��ǰ��¼�û���Ϣ
	 * @return int ��ѯ��¼��
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
			/** ��ӱ����Ų�ѯ���������Դ�Сдģ����ѯ�� */
			if (reportInForm.getChildRepId() != null
					&& !reportInForm.getChildRepId().equals("")) {
				where
						.append(" and upper(a.reportIn.MChildReport.comp_id.childRepId) like upper('%"
								+ reportInForm.getChildRepId().trim() + "%')");
			}
			/** ��ӱ������Ʋ�ѯ������ģ����ѯ�� */
			if (reportInForm.getRepName() != null
					&& !reportInForm.getRepName().equals("")) {
				where.append(" and a.reportIn.repName like '%"
						+ reportInForm.getRepName().trim() + "%'");
			}
			/** ���ģ�����ͣ�ȫ��/����/��֧����ѯ���� */
			if (reportInForm.getFrOrFzType() != null
					&& !reportInForm.getFrOrFzType().equals(
							Config.DEFAULT_VALUE)) {
				where.append(" and a.reportIn.MChildReport.frOrFzType='"
						+ reportInForm.getFrOrFzType() + "'");
			}
			/** ��ӱ���Ƶ�ȣ���/��/����/�꣩��ѯ���� */
			if (reportInForm.getRepFreqId() != null
					&& !String.valueOf(reportInForm.getRepFreqId()).equals(
							Config.DEFAULT_VALUE)) {
				where
						.append(" and a.reportIn.MDataRgType.dataRangeId in (select M.comp_id.dataRangeId from MActuRep M "
								+ "where M.comp_id.childRepId=a.reportIn.MChildReport.comp_id.childRepId and M.comp_id.versionId=a.reportIn.MChildReport.comp_id.versionId "
								+ "and M.comp_id.repFreqId="
								+ reportInForm.getRepFreqId() + ")");
			}
			// /**����ر��趨���ڲ�ѯ����*/
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
			/** ������ڣ���ݣ���ѯ���� */
			if (reportInForm.getYear() != null) {
				where.append(" and a.reportIn.year=" + reportInForm.getYear());
			}
			/** ������ڣ��·ݣ���ѯ���� */
			if (reportInForm.getTerm() != null) {
				where.append(" and a.reportIn.term=" + reportInForm.getTerm());
			}
			/** ��ӻ�����ѯ���� */
			if (!StringUtil.isEmpty(reportInForm.getOrgId())
					&& !reportInForm.getOrgId().equals(Config.DEFAULT_VALUE)) {
				where.append(" and a.reportIn.orgId='"
						+ reportInForm.getOrgId().trim() + "'");
			}

			/** ��ӱ������Ȩ��[�ر�Ȩ��=���Ȩ��]�������û�������ӣ� 
			 * ���������ݿ��ж�*/
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
			// �ر����ݿ�����
			if (conn != null)
				conn.closeSession();
		}
		return count;
	}

	/**
	 * ָ�����ɲ����Ƿ��и��ڱ�������
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
	 * �ر������ѯ����ѯ�������ر��ı����¼��
	 * 
	 * @param reportInForm
	 *            ����FormBean
	 * @param operator
	 *            ��ǰ��¼�û���Ϣ
	 * @param offset
	 *            ƫ����
	 * @param limit
	 *            ÿҳ��ʾ��¼��
	 * @param operator
	 *            ��ǰ��¼�û���Ϣ
	 * @return List �����¼�����
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
			/** ��ӱ����Ų�ѯ���������Դ�Сдģ����ѯ�� */
			if (reportInForm.getChildRepId() != null
					&& !reportInForm.getChildRepId().equals("")) {
				where
						.append(" and upper(a.reportIn.MChildReport.comp_id.childRepId) like upper('%"
								+ reportInForm.getChildRepId().trim() + "%')");
			}
			/** ��ӱ������Ʋ�ѯ������ģ����ѯ�� */
			if (reportInForm.getRepName() != null
					&& !reportInForm.getRepName().equals("")) {
				where.append(" and a.reportIn.repName like '%"
						+ reportInForm.getRepName().trim() + "%'");
			}
			/** ���ģ�����ͣ�ȫ��/����/��֧����ѯ���� */
			if (reportInForm.getFrOrFzType() != null
					&& !reportInForm.getFrOrFzType().equals(
							Config.DEFAULT_VALUE)) {
				where.append(" and a.reportIn.MChildReport.frOrFzType='"
						+ reportInForm.getFrOrFzType() + "'");
			}
			/** ��ӱ���Ƶ�ȣ���/��/����/�꣩��ѯ���� */
			if (reportInForm.getRepFreqId() != null
					&& !String.valueOf(reportInForm.getRepFreqId()).equals(
							Config.DEFAULT_VALUE)) {
				where
						.append(" and a.reportIn.MDataRgType.dataRangeId in (select M.comp_id.dataRangeId from MActuRep M "
								+ "where M.comp_id.childRepId=a.reportIn.MChildReport.comp_id.childRepId and M.comp_id.versionId=a.reportIn.MChildReport.comp_id.versionId "
								+ "and M.comp_id.repFreqId="
								+ reportInForm.getRepFreqId() + ")");
			}
			// /**����ر��趨���ڲ�ѯ����*/
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
			/** ������ڣ���ݣ���ѯ���� */
			if (reportInForm.getYear() != null) {
				where.append(" and a.reportIn.year=" + reportInForm.getYear());
			}
			/** ������ڣ��·ݣ���ѯ���� */
			if (reportInForm.getTerm() != null) {
				where.append(" and a.reportIn.term=" + reportInForm.getTerm());
			}
			/** ��ӻ�����ѯ���� */
			if (!StringUtil.isEmpty(reportInForm.getOrgId())
					&& !reportInForm.getOrgId().equals(Config.DEFAULT_VALUE)) {
				where.append(" and a.reportIn.orgId='"
						+ reportInForm.getOrgId().trim() + "'");
			}

			/** ��ӱ������Ȩ��[�ر�Ȩ��=���Ȩ��]�������û�������ӣ�
			 * ���������ݿ��ж� */
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

					/** ���ñ���Ƶ�� */
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
			// �ر����ݿ�����
			if (conn != null)
				conn.closeSession();
		}
		return resList;
	}

	/**
	 * ��֤�½�ǿ���ر��ύ�ı�
	 * 
	 * @author �ܷ���
	 * @return true ��Ϣ��д��ȷ��false ��Ϣ��д����
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
	 * �½�ǿ���ر�
	 * 
	 * @author �ܷ���
	 * @return true �½��ɹ���false�½�ʧ�ܣ�
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
	 * ����������ѯ��������
	 * 
	 * @param reportInForm
	 * @return
	 */
	public static int getRecordCount(ReportInForm reportInForm,
			Operator operator) {
		int count = 0;

		// ���Ӷ���ͻỰ�����ʼ��
		DBConn conn = null;
		Session session = null;

		// ��ѯ����HQL������
		StringBuffer hql = new StringBuffer("select count(*) from ReportIn ri");
		StringBuffer where = new StringBuffer("");

		if (reportInForm != null) {
			// �����������ж�,�������Ʋ���Ϊ��
			if (reportInForm.getOrgId() != null
					&& !reportInForm.getOrgId().equals(""))
				where.append((where.toString().equals("") ? "" : " or ")
						+ "mc.orgId like '%" + reportInForm.getOrgId() + "%'");
		}

		try { // List���ϵĲ���
			// ��ʼ��
			hql.append((where.toString().equals("") ? "" : " where ")
					+ where.toString());
			/**
			 * ���ϻ����ͱ���Ȩ����Ϣ
			 * 
			 * @author Ҧ��
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

			// conn�����ʵ����
			conn = new DBConn();
			// �����ӿ�ʼ�Ự
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
			// ������Ӵ��ڣ���Ͽ��������Ự������
			if (conn != null)
				conn.closeSession();
		}
		return count;
	}

	/**
	 * ����oracle�﷨(upper)  ��Ҫ�޸� ���Ը� 2011-12-21
	 * ��ʹ��hibernate 
	 * ������ǩ�Ĳ����ܼ�¼��
	 * 
	 * @param reportInForm
	 * @param operator
	 * @return int �ܼ�¼��
	 */
	public static int getRecordCountOfmanual(ReportInForm reportInForm,
			Operator operator) {
		int count = 0;
		DBConn conn = null;
		Session session = null;

		if (reportInForm == null || operator == null)
			return count;

		try {
			// ��ѯ����HQL������
			StringBuffer hql = new StringBuffer(
					"select count(*) from ReportIn ri WHERE "
							+ "ri.times=(select max(r.times) from ReportIn r where r.MChildReport.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and "
							+ "r.MChildReport.comp_id.versionId=ri.MChildReport.comp_id.versionId and r.MDataRgType.dataRangeId=ri.MDataRgType.dataRangeId and r.orgId=ri.orgId and "
							+ "r.MCurr.curId=ri.MCurr.curId and r.year=ri.year and r.term=ri.term and r.checkFlag=ri.checkFlag)");

			/** ��ѯ����״̬Ϊδ��ǩ���� */
			StringBuffer where = new StringBuffer(" and ri.checkFlag="
					+ com.fitech.net.config.Config.CHECK_FLAG_UNCHECK);

			/** ��ӱ����Ų�ѯ���������Դ�Сдģ����ѯ�� */
			/**����oracle�﷨(upper)  ��Ҫ�޸� ���Ը� 2011-12-21**/
			if (reportInForm.getChildRepId() != null
					&& !reportInForm.getChildRepId().equals("")) {
				where
						.append(" and upper(ri.MChildReport.comp_id.childRepId) like upper('%"
								+ reportInForm.getChildRepId().trim() + "%')");
			}
			/** ��ӱ������Ʋ�ѯ������ģ����ѯ�� */
			if (reportInForm.getRepName() != null
					&& !reportInForm.getRepName().equals("")) {
				where.append(" and ri.repName like '%"
						+ reportInForm.getRepName().trim() + "%'");
			}
			/** ���ģ�����ͣ�ȫ��/����/��֧����ѯ���� */
			if (reportInForm.getFrOrFzType() != null
					&& !reportInForm.getFrOrFzType().equals(
							Config.DEFAULT_VALUE)) {
				where.append(" and ri.MChildReport.frOrFzType='"
						+ reportInForm.getFrOrFzType() + "'");
			}
			/** ��ӱ���Ƶ�ȣ���/��/����/�꣩��ѯ���� */
			if (reportInForm.getRepFreqId() != null
					&& !String.valueOf(reportInForm.getRepFreqId()).equals(
							Config.DEFAULT_VALUE)) {
				where
						.append(" and ri.MDataRgType.dataRangeId in (select M.comp_id.dataRangeId from MActuRep M "
								+ "where M.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and M.comp_id.versionId=ri.MChildReport.comp_id.versionId "
								+ "and M.comp_id.repFreqId="
								+ reportInForm.getRepFreqId() + ")");
			}
			/** ������ڣ���ݣ���ѯ���� */
			if (reportInForm.getYear() != null) {
				where.append(" and ri.year=" + reportInForm.getYear());
			}
			/** ������ڣ��·ݣ���ѯ���� */
			if (reportInForm.getTerm() != null) {
				where.append(" and ri.term=" + reportInForm.getTerm());
			}
			/** ��ӻ�����ѯ���� */
			if (!StringUtil.isEmpty(reportInForm.getOrgId())
					&& !reportInForm.getOrgId().equals(Config.DEFAULT_VALUE)) {
				where.append(" and ri.orgId='" + reportInForm.getOrgId().trim()
						+ "'");
			}

			/** ��ӱ������Ȩ�ޣ������û�������ӣ�
			 * ���������ݿ��ж� */
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
			// �ر����ݿ�����
			if (conn != null)
				conn.closeSession();
		}
		return count;
	}
	
	/**
	 * ����oracle�﷨(upper)  ��Ҫ�޸� ���Ը� 2011-12-21
	 * ��ʹ��hibernate 
	 * ������ǩ�Ĳ����ܼ�¼��
	 * 
	 * @param reportInForm
	 * @param operator
	 * @return int �ܼ�¼��
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
			// ��ѯ����HQL������
			StringBuffer hql = new StringBuffer(
					"select count(*) from ReportIn ri WHERE "
							+ "ri.times=(select max(r.times) from ReportIn r where r.MChildReport.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and "
							+ "r.MChildReport.comp_id.versionId=ri.MChildReport.comp_id.versionId and r.MDataRgType.dataRangeId=ri.MDataRgType.dataRangeId and r.orgId=ri.orgId and "
							+ "r.MCurr.curId=ri.MCurr.curId and r.year=ri.year and r.term=ri.term and r.checkFlag=ri.checkFlag)");

			/** ��ѯ����״̬Ϊδ��ǩ���� */
			StringBuffer where = new StringBuffer(" and ri.checkFlag in("+com.fitech.net.config.Config.CHECK_FLAG_UNCHECK+","+checkFlag+")");

			/** ��ӱ����Ų�ѯ���������Դ�Сдģ����ѯ�� */
			/**����oracle�﷨(upper)  ��Ҫ�޸� ���Ը� 2011-12-21**/
			if (reportInForm.getChildRepId() != null
					&& !reportInForm.getChildRepId().equals("")) {
				where
						.append(" and upper(ri.MChildReport.comp_id.childRepId) like upper('%"
								+ reportInForm.getChildRepId().trim() + "%')");
			}
			/** ��ӱ������Ʋ�ѯ������ģ����ѯ�� */
			if (reportInForm.getRepName() != null
					&& !reportInForm.getRepName().equals("")) {
				where.append(" and ri.repName like '%"
						+ reportInForm.getRepName().trim() + "%'");
			}
			/** ���ģ�����ͣ�ȫ��/����/��֧����ѯ���� */
			if (reportInForm.getFrOrFzType() != null
					&& !reportInForm.getFrOrFzType().equals(
							Config.DEFAULT_VALUE)) {
				where.append(" and ri.MChildReport.frOrFzType='"
						+ reportInForm.getFrOrFzType() + "'");
			}
			/** ��ӱ���Ƶ�ȣ���/��/����/�꣩��ѯ���� */
			if (reportInForm.getRepFreqId() != null
					&& !String.valueOf(reportInForm.getRepFreqId()).equals(
							Config.DEFAULT_VALUE)) {
				where
						.append(" and ri.MDataRgType.dataRangeId in (select M.comp_id.dataRangeId from MActuRep M "
								+ "where M.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and M.comp_id.versionId=ri.MChildReport.comp_id.versionId "
								+ "and M.comp_id.repFreqId="
								+ reportInForm.getRepFreqId() + ")");
			}
			/** ������ڣ���ݣ���ѯ���� */
			if (reportInForm.getYear() != null) {
				where.append(" and ri.year=" + reportInForm.getYear());
			}
			/** ������ڣ��·ݣ���ѯ���� */
			if (reportInForm.getTerm() != null) {
				where.append(" and ri.term=" + reportInForm.getTerm());
			}
			/** ��ӻ�����ѯ���� */
			if (!StringUtil.isEmpty(reportInForm.getOrgId())
					&& !reportInForm.getOrgId().equals(Config.DEFAULT_VALUE)) {
				where.append(" and ri.orgId='" + reportInForm.getOrgId().trim()
						+ "'");
			}

			/** ��ӱ������Ȩ�ޣ������û�������ӣ�
			 * ���������ݿ��ж� */
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
			// �ر����ݿ�����
			if (conn != null)
				conn.closeSession();
		}
		return count;
	}

	/**
	 * ��ʹ��hibernate  ���Ը� 2011-12-21
	 * ������ǩ�Ĳ��Ҽ�¼����
	 * 
	 * @param reportInForm
	 *            ҳ��FormBean
	 * @param offset
	 *            ���ݿ��ѯ��ʼλ
	 * @param limit
	 *            ��ѯ��¼��
	 * @param operator
	 *            ��ǰ��¼�û�
	 * @return List ��ѯ��������
	 */
	public static List selectOfManual(ReportInForm reportInForm, int offset,
			int limit, Operator operator) {
		List resList = null;
		DBConn conn = null;
		Session session = null;

		if (reportInForm == null || operator == null)
			return resList;

		try {
			// ��ѯ����HQL������
			StringBuffer hql = new StringBuffer(
					"from ReportIn ri WHERE "
							+ "ri.times=(select max(r.times) from ReportIn r where r.MChildReport.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and "
							+ "r.MChildReport.comp_id.versionId=ri.MChildReport.comp_id.versionId and r.MDataRgType.dataRangeId=ri.MDataRgType.dataRangeId and r.orgId=ri.orgId and "
							+ "r.MCurr.curId=ri.MCurr.curId and r.year=ri.year and r.term=ri.term and r.checkFlag=ri.checkFlag)");

			/** ��ѯ����״̬Ϊδ��ǩ���� */
			StringBuffer where = new StringBuffer(" and ri.checkFlag="
					+ com.fitech.net.config.Config.CHECK_FLAG_UNCHECK);

			/** ��ӱ����Ų�ѯ���������Դ�Сдģ����ѯ�� */
			if (reportInForm.getChildRepId() != null
					&& !reportInForm.getChildRepId().equals("")) {
				where
						.append(" and upper(ri.MChildReport.comp_id.childRepId) like upper('%"
								+ reportInForm.getChildRepId().trim() + "%')");
			}
			/** ��ӱ������Ʋ�ѯ������ģ����ѯ�� */
			if (reportInForm.getRepName() != null
					&& !reportInForm.getRepName().equals("")) {
				where.append(" and ri.repName like '%"
						+ reportInForm.getRepName().trim() + "%'");
			}
			/** ���ģ�����ͣ�ȫ��/����/��֧����ѯ���� */
			if (reportInForm.getFrOrFzType() != null
					&& !reportInForm.getFrOrFzType().equals(
							Config.DEFAULT_VALUE)) {
				where.append(" and ri.MChildReport.frOrFzType='"
						+ reportInForm.getFrOrFzType() + "'");
			}
			/** ��ӱ���Ƶ�ȣ���/��/����/�꣩��ѯ���� */
			if (reportInForm.getRepFreqId() != null
					&& !String.valueOf(reportInForm.getRepFreqId()).equals(
							Config.DEFAULT_VALUE)) {
				where
						.append(" and ri.MDataRgType.dataRangeId in (select M.comp_id.dataRangeId from MActuRep M "
								+ "where M.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and M.comp_id.versionId=ri.MChildReport.comp_id.versionId "
								+ "and M.comp_id.repFreqId="
								+ reportInForm.getRepFreqId() + ")");
			}
			/** ������ڣ���ݣ���ѯ���� */
			if (reportInForm.getYear() != null) {
				where.append(" and ri.year=" + reportInForm.getYear());
			}
			/** ������ڣ��·ݣ���ѯ���� */
			if (reportInForm.getTerm() != null) {
				where.append(" and ri.term=" + reportInForm.getTerm());
			}
			/** ��ӻ�����ѯ���� */
			if (!StringUtil.isEmpty(reportInForm.getOrgId()) 
					&& !reportInForm.getOrgId().equals(Config.DEFAULT_VALUE)) {
				where.append(" and ri.orgId='" + reportInForm.getOrgId().trim()
						+ "'");
			}

			/** ��ӱ������Ȩ�ޣ������û�������ӣ� */
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
					// ���ñ������״̬
					aditing.setCheckFlag(reportInRecord.getCheckFlag());
					// ���ñ��У����
					if (reportInRecord.getTblOuterValidateFlag() != null)
						aditing.setTblOuterValidateFlag(reportInRecord
								.getTblOuterValidateFlag());
					// ���ñ��ͻ�������
					/**��ʹ��hibernate  ���Ը� 2011-12-21**/
					orgNet = StrutsOrgNetDelegate.selectOne(reportInRecord
							.getOrgId());
					if (orgNet != null){
						aditing.setOrgName(orgNet.getOrgName());
						aditing.setOrgId(orgNet.getOrgId());
					}
					
					// ���ñ���ID��ʶ��
					aditing.setRepInId(reportInRecord.getRepInId());
				
					// ���ñ�������
					aditing.setRepName(reportInRecord.getRepName());
					// ���ñ���������
					aditing.setReportDate(reportInRecord.getReportDate());
					// ���ñ�����
					aditing.setChildRepId(reportInRecord.getMChildReport()
							.getComp_id().getChildRepId());
					// ���ñ���汾��
					aditing.setVersionId(reportInRecord.getMChildReport()
							.getComp_id().getVersionId());
					// �����쳣�仯��־
					aditing.setAbmormityChangeFlag(reportInRecord
							.getAbmormityChangeFlag());
					// ���ñ����������
					aditing.setCurrName(reportInRecord.getMCurr().getCurName());
					aditing.setCurId(reportInRecord.getMCurr().getCurId());
					
					/**��ʹ��hibernate  ���Ը� 2011-12-21**/
					MActuRep mActuRep = GetFreR(reportInRecord);
					if (mActuRep != null) {
						// ���ñ��Ϳھ�
						aditing.setDataRgTypeName(mActuRep.getMDataRgType()
								.getDataRgDesc());
						// ���ñ��Ϳھ�
						aditing.setDataRangeId(mActuRep.getMDataRgType()
								.getDataRangeId());
						// ���ñ���Ƶ��
						aditing.setActuFreqName(mActuRep.getMRepFreq()
								.getRepFreqName());
						// ���ñ���Ƶ��
						aditing.setActuFreqID(mActuRep.getMRepFreq()
								.getRepFreqId());
						if (aditing.getActuFreqID() != null) {
							// yyyy-mm-dd ��������ȷ�������ھ������������
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
			// �ر����ݿ�����
			if (conn != null)
				conn.closeSession();
		}
		return resList;
	}
	
	/**
	 * ��ʹ��hibernate  ���Ը� 2011-12-21
	 * ������ǩ�Ĳ��Ҽ�¼����
	 * 
	 * @param reportInForm
	 *            ҳ��FormBean
	 * @param offset
	 *            ���ݿ��ѯ��ʼλ
	 * @param limit
	 *            ��ѯ��¼��
	 * @param operator
	 *            ��ǰ��¼�û�
	 * @return List ��ѯ��������
	 */
	public static List selectOfFlag(ReportInForm reportInForm, int offset,
			int limit, Operator operator,String checkFlag) {
		List resList = null;
		DBConn conn = null;
		Session session = null;

		if (reportInForm == null || operator == null)
			return resList;

		try {
			// ��ѯ����HQL������
			StringBuffer hql = new StringBuffer(
					"from ReportIn ri WHERE "
							+ "ri.times=(select max(r.times) from ReportIn r where r.MChildReport.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and "
							+ "r.MChildReport.comp_id.versionId=ri.MChildReport.comp_id.versionId and r.MDataRgType.dataRangeId=ri.MDataRgType.dataRangeId and r.orgId=ri.orgId and "
							+ "r.MCurr.curId=ri.MCurr.curId and r.year=ri.year and r.term=ri.term and r.checkFlag=ri.checkFlag)");

			/** ��ѯ����״̬Ϊδ��ǩ���� */
			StringBuffer where = new StringBuffer(" and ri.checkFlag in("+com.fitech.net.config.Config.CHECK_FLAG_UNCHECK+","+checkFlag+")");

			/** ��ӱ����Ų�ѯ���������Դ�Сдģ����ѯ�� */
			if (reportInForm.getChildRepId() != null
					&& !reportInForm.getChildRepId().equals("")) {
				where
						.append(" and upper(ri.MChildReport.comp_id.childRepId) like upper('%"
								+ reportInForm.getChildRepId().trim() + "%')");
			}
			/** ��ӱ������Ʋ�ѯ������ģ����ѯ�� */
			if (reportInForm.getRepName() != null
					&& !reportInForm.getRepName().equals("")) {
				where.append(" and ri.repName like '%"
						+ reportInForm.getRepName().trim() + "%'");
			}
			/** ���ģ�����ͣ�ȫ��/����/��֧����ѯ���� */
			if (reportInForm.getFrOrFzType() != null
					&& !reportInForm.getFrOrFzType().equals(
							Config.DEFAULT_VALUE)) {
				where.append(" and ri.MChildReport.frOrFzType='"
						+ reportInForm.getFrOrFzType() + "'");
			}
			/** ��ӱ���Ƶ�ȣ���/��/����/�꣩��ѯ���� */
			if (reportInForm.getRepFreqId() != null
					&& !String.valueOf(reportInForm.getRepFreqId()).equals(
							Config.DEFAULT_VALUE)) {
				where
						.append(" and ri.MDataRgType.dataRangeId in (select M.comp_id.dataRangeId from MActuRep M "
								+ "where M.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and M.comp_id.versionId=ri.MChildReport.comp_id.versionId "
								+ "and M.comp_id.repFreqId="
								+ reportInForm.getRepFreqId() + ")");
			}
			/** ������ڣ���ݣ���ѯ���� */
			if (reportInForm.getYear() != null) {
				where.append(" and ri.year=" + reportInForm.getYear());
			}
			/** ������ڣ��·ݣ���ѯ���� */
			if (reportInForm.getTerm() != null) {
				where.append(" and ri.term=" + reportInForm.getTerm());
			}
			/** ��ӻ�����ѯ���� */
			if (!StringUtil.isEmpty(reportInForm.getOrgId()) 
					&& !reportInForm.getOrgId().equals(Config.DEFAULT_VALUE)) {
				where.append(" and ri.orgId='" + reportInForm.getOrgId().trim()
						+ "'");
			}

			/** ��ӱ������Ȩ�ޣ������û�������ӣ� */
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
					// ���ñ������״̬
					aditing.setCheckFlag(reportInRecord.getCheckFlag());
					// ���ñ��У����
					if (reportInRecord.getTblOuterValidateFlag() != null)
						aditing.setTblOuterValidateFlag(reportInRecord
								.getTblOuterValidateFlag());
					// ���ñ��ͻ�������
					/**��ʹ��hibernate  ���Ը� 2011-12-21**/
					orgNet = StrutsOrgNetDelegate.selectOne(reportInRecord
							.getOrgId());
					if (orgNet != null){
						aditing.setOrgName(orgNet.getOrgName());
						aditing.setOrgId(orgNet.getOrgId());
					}
					
					// ���ñ���ID��ʶ��
					aditing.setRepInId(reportInRecord.getRepInId());
				
					// ���ñ�������
					aditing.setRepName(reportInRecord.getRepName());
					// ���ñ���������
					aditing.setReportDate(reportInRecord.getReportDate());
					// ���ñ�����
					aditing.setChildRepId(reportInRecord.getMChildReport()
							.getComp_id().getChildRepId());
					// ���ñ���汾��
					aditing.setVersionId(reportInRecord.getMChildReport()
							.getComp_id().getVersionId());
					// �����쳣�仯��־
					aditing.setAbmormityChangeFlag(reportInRecord
							.getAbmormityChangeFlag());
					// ���ñ����������
					aditing.setCurrName(reportInRecord.getMCurr().getCurName());
					aditing.setCurId(reportInRecord.getMCurr().getCurId());
					
					/**��ʹ��hibernate  ���Ը� 2011-12-21**/
					MActuRep mActuRep = GetFreR(reportInRecord);
					if (mActuRep != null) {
						// ���ñ��Ϳھ�
						aditing.setDataRgTypeName(mActuRep.getMDataRgType()
								.getDataRgDesc());
						// ���ñ��Ϳھ�
						aditing.setDataRangeId(mActuRep.getMDataRgType()
								.getDataRangeId());
						// ���ñ���Ƶ��
						aditing.setActuFreqName(mActuRep.getMRepFreq()
								.getRepFreqName());
						// ���ñ���Ƶ��
						aditing.setActuFreqID(mActuRep.getMRepFreq()
								.getRepFreqId());
						if (aditing.getActuFreqID() != null) {
							// yyyy-mm-dd ��������ȷ�������ھ������������
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
			// �ر����ݿ�����
			if (conn != null)
				conn.closeSession();
		}
		return resList;
	}

	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * ��Ҫ����ȫ��У��ı���
	 * 
	 * @param reportInForm
	 *            ����FormBean
	 * @param operator
	 *            ��ǰ��¼�û�
	 * @return List �����б�
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

			/** ��ѯ����״̬Ϊδ��˱��� */
			StringBuffer where = new StringBuffer(" and ri.checkFlag="
					+ com.fitech.net.config.Config.CHECK_FLAG_UNCHECK);

			/** ��ӱ����Ų�ѯ���������Դ�Сдģ����ѯ�� */
			if (reportInForm.getChildRepId() != null
					&& !reportInForm.getChildRepId().equals("")) {
				where
						.append(" and upper(ri.MChildReport.comp_id.childRepId) like upper('%"
								+ reportInForm.getChildRepId().trim() + "%')");
			}
			/** ��ӱ������Ʋ�ѯ������ģ����ѯ�� */
			if (reportInForm.getRepName() != null
					&& !reportInForm.getRepName().equals("")) {
				where.append(" and ri.repName like '%"
						+ reportInForm.getRepName().trim() + "%'");
			}
			/** ���ģ�����ͣ�ȫ��/����/��֧����ѯ���� */
			if (reportInForm.getFrOrFzType() != null
					&& !reportInForm.getFrOrFzType().equals(
							Config.DEFAULT_VALUE)) {
				where.append(" and ri.MChildReport.frOrFzType='"
						+ reportInForm.getFrOrFzType() + "'");
			}
			/** ��ӱ���Ƶ�ȣ���/��/����/�꣩��ѯ���� */
			if (reportInForm.getRepFreqId() != null
					&& !String.valueOf(reportInForm.getRepFreqId()).equals(
							Config.DEFAULT_VALUE)) {
				where
						.append(" and ri.MDataRgType.dataRangeId in (select M.comp_id.dataRangeId from MActuRep M "
								+ "where M.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and M.comp_id.versionId=ri.MChildReport.comp_id.versionId "
								+ "and M.comp_id.repFreqId="
								+ reportInForm.getRepFreqId() + ")");
			}
			/** ������ڣ���ݣ���ѯ���� */
			if (reportInForm.getYear() != null) {
				where.append(" and ri.year=" + reportInForm.getYear());
			}
			/** ������ڣ��·ݣ���ѯ���� */
			if (reportInForm.getTerm() != null) {
				where.append(" and ri.term=" + reportInForm.getTerm());
			}
			/** ��ӻ�����ѯ���� */
			if (reportInForm.getOrgId() != null
					&& !reportInForm.getOrgId().equals(Config.DEFAULT_VALUE)) {
				where.append(" and ri.orgId='" + reportInForm.getOrgId().trim()
						+ "'");
			}

			/** ��ӱ������Ȩ�ޣ������û�������ӣ� 
			 * ���������ݿ��ж�*/
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
			// �ر����ݿ�����
			if (conn != null)
				conn.closeSession();
		}
		return resList;
	}

	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * �õ�Ƶ��
	 * 
	 * @param reportIn
	 *            �������
	 * @return MActuRep ����Ƶ��
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
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * �õ�Ƶ��
	 * 
	 * @param reportIn
	 *            �������
	 * @return MActuRep ����Ƶ��
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
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * Ӱ�����MActuRep MChildReport
	 * �õ�Ƶ��
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
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * Ӱ�����MActuRep MChildReport MDataRgType MRepFreq
	 * �жϱ���Ƶ���Ƿ��Ѿ�����
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
				rep_freq = "'��','��','����','��'";
			else if (term.intValue() == 6)
				rep_freq = "'��','��','����'";
			else if (term.intValue() == 3 || term.intValue() == 9)
				rep_freq = "'��','��'";
			else
				rep_freq = "'��'";
			//�����Ƶ��
			rep_freq += ",'��'";
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
	 * �����ж�report_in_info �����Ƿ��������
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
	 * �����ж�report_in_info �����Ƿ��������
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
	 * �����ж�af_pbocreportdata �����Ƿ��������
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
	 * �����ж�af_pbocreportdata �����Ƿ��������
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
	 * �����ж�af_pbocreportdata �����Ƿ��������
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
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * ��ѯ������Ϣ
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
			// �����������ж�,�������Ʋ���Ϊ��
			if (reportInForm.getOrgId() != null
					&& !reportInForm.getOrgId().equals(""))
				where.append((where.toString().equals("") ? "" : " and ")
						+ "ri.orgId like like '%" + reportInForm.getOrgId()
						+ "%'");
			/**
			 * ���ϻ����ͱ���Ȩ����Ϣ
			 * 
			 * @author Ҧ��
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

		try { // List���ϵĲ���
			// ��ʼ��
			hql.append((where.toString().equals("") ? "" : " where ")
					+ where.toString());

			// conn�����ʵ����
			conn = new DBConn();
			// �����ӿ�ʼ�Ự
			session = conn.openSession();
			// ��Ӽ�����Session
			// List list=session.find(hql.toString());
			Query query = session.createQuery(hql.toString());
			query.setFirstResult(offset).setMaxResults(limit);
			List list = query.list();

			if (list != null) {
				retvals = new ArrayList();
				// ѭ����ȡ���ݿ����������¼
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
			// ������Ӵ��ڣ���Ͽ��������Ự������
			if (conn != null)
				conn.closeSession();
		}
		return retvals;
	}

	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * ͨ������id��������һЩ������Ϣ ****δ���****************************************8
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
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * �ر������趨��ѯ
	 * 
	 * @author�ܷ���
	 * @return ���ؼ�¼����
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
		 * ���ϻ����ͱ���Ȩ����Ϣ
		 * 
		 * @author Ҧ��
		 */
		if (operator == null)
			return 0;

		/** ����Ȩ�� */
		if (operator.getOrgPopedomSQL() != null
				&& !operator.getOrgPopedomSQL().equals("")) {
			where.append((where.toString().equals("") ? "" : " and ")
					+ " a.orgId in(" + operator.getOrgPopedomSQL() + ")");
		} else {
			return 0;
		}
		/** ����Ȩ�� */
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
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * �����ر����ò�ѯ
	 * 
	 * @author�ܷ���
	 * @return ���ؼ�¼
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
		 * ���ϻ����ͱ���Ȩ����Ϣ
		 * 
		 * @author Ҧ��
		 */
		if (operator == null)
			return result;
		/** ����Ȩ�� */
		if (operator.getOrgPopedomSQL() != null
				&& !operator.getOrgPopedomSQL().equals(""))
			where.append((where.toString().equals("") ? "" : " and ")
					+ " a.orgId in(" + operator.getOrgPopedomSQL() + ")");
		else
			return result;
		/** ����Ȩ�� */
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
					/**��ʹ��hibernate ���Ը� 2011-12-21**/
					if (StrutsMOrgDelegate.selectOne(reportInForm.getOrgId()) != null)
						/**��ʹ��hibernate ���Ը� 2011-12-21**/
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
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * �½��ر������趨
	 * 
	 * @author �ܷ���
	 * @return true �½��ɹ���false�½�ʧ�ܣ�
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
	 * JDBC���� ��Ҫ�޸� ���Ը� 2011-12-21
	 * �ر� ����reportin��checkflag���� Ѧ���
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
			/**���������ݿ��ж�*/
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
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * ����repInId����laterReportDay�ļ�¼ ����
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
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * insert report_in��
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
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * ��õ�ǰ�������ϱ��ı���
	 * 
	 * @author ����ΰ
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
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * �����Ѿ����͹��Ļ��ܱ������ϱ��ֵ�����
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
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * ����������ѯ���ܺõ�����
	 * 
	 * @param reportInForm
	 * @return
	 */
	public static int getOnLineRecordCount(ReportInForm reportInForm) {
		int count = 0;

		// ���Ӷ���ͻỰ�����ʼ��
		DBConn conn = null;
		Session session = null;

		// ��ѯ����HQL������
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
		try { // List���ϵĲ���
			conn = new DBConn();
			// �����ӿ�ʼ�Ự
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
			// ������Ӵ��ڣ���Ͽ��������Ự������
			if (conn != null)
				conn.closeSession();
		}
		return count;
	}

	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * ����������ѯ�趨���ܷ�ʽ�Ļ�������
	 * 
	 * @param reportInForm
	 * @return
	 */
	public static int getOtherCollectRecordCount(ReportInForm reportInForm) {
		int count = 0;

		// ���Ӷ���ͻỰ�����ʼ��
		DBConn conn = null;
		Session session = null;

		// ��ѯ����HQL������
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
		try { // List���ϵĲ���
			conn = new DBConn();
			// �����ӿ�ʼ�Ự
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
			// ������Ӵ��ڣ���Ͽ��������Ự������
			if (conn != null)
				conn.closeSession();
		}
		return count;
	}

	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * ����������ѯ���ܺõ�����
	 * 
	 * @param reportInForm
	 * @return
	 */
	public static List getOnLineRecords(ReportInForm reportInForm, int offset,
			int limit) {
		List resultList = null;

		// ���Ӷ���ͻỰ�����ʼ��
		DBConn conn = null;
		Session session = null;

		// ��ѯ����HQL������
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

		try { // List���ϵĲ���
			conn = new DBConn();
			// �����ӿ�ʼ�Ự
			session = conn.openSession();
			Query query = session.createQuery(hql);
			query.setFirstResult(offset);
			query.setMaxResults(limit);
			List list = query.list();

			if (list != null && list.size() > 0) {
				resultList = new ArrayList();
				// ѭ����ȡ���ݿ����������¼

				for (Iterator it = list.iterator(); it.hasNext();) {
					// ʵ����һ���м���Aditing��������ҳ��������ֶ���
					Aditing aditing = new Aditing();
					// ���ɳ־û�����
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
					// �����ҵ��ı���id����aditing����
					aditing.setRepInId(reportInPersistence.getRepInId());
					aditing.setYear(reportInPersistence.getYear());
					aditing.setTerm(reportInPersistence.getTerm());
					// �����ҵı�������set��adtiting����
					if (!reportInPersistence.getRepName().equals(
							reportInPersistence.getMChildReport()
									.getReportName()))
						aditing.setRepName(reportInPersistence.getRepName()
								+ "-"
								+ reportInPersistence.getMChildReport()
										.getReportName());
					else
						aditing.setRepName(reportInPersistence.getRepName());
					// �����ҵı�������set��aditing����
					aditing.setReportDate(reportInPersistence.getReportDate());
					// �����ҵ��ӱ���ID�Ž�aditing����
					aditing.setChildRepId(reportInPersistence.getMChildReport()
							.getComp_id().getChildRepId());
					// �����ҵİ汾��set��aditing����
					aditing.setVersionId(reportInPersistence.getMChildReport()
							.getComp_id().getVersionId());
					// ������ҳ����������м�¼��adtiing����add��arraylist�����з���action
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
						// ����Ƶ��
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
			// ������Ӵ��ڣ���Ͽ��������Ự������
			if (conn != null)
				conn.closeSession();
		}
		return resultList;
	}

	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * ����������ѯ�Ի��ܷ�ʽ���ɵĻ��ܺõ�����
	 * 
	 * @param reportInForm
	 * @return
	 */
	public static List getOtherCollectRecords(ReportInForm reportInForm,
			int offset, int limit) {
		List resultList = null;

		// ���Ӷ���ͻỰ�����ʼ��
		DBConn conn = null;
		Session session = null;

		// ��ѯ����HQL������
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

		try { // List���ϵĲ���
			conn = new DBConn();
			// �����ӿ�ʼ�Ự
			session = conn.openSession();
			Query query = session.createQuery(hql);
			query.setFirstResult(offset);
			query.setMaxResults(limit);
			List list = query.list();

			if (list != null && list.size() > 0) {
				resultList = new ArrayList();
				// ѭ����ȡ���ݿ����������¼

				for (Iterator it = list.iterator(); it.hasNext();) {
					// ʵ����һ���м���Aditing��������ҳ��������ֶ���
					Aditing aditing = new Aditing();
					// ���ɳ־û�����
					ReportIn reportInPersistence = (ReportIn) it.next();

					// �����ҵ��ı���id����aditing����
					aditing.setRepInId(reportInPersistence.getRepInId());
					aditing.setYear(reportInPersistence.getYear());
					aditing.setTerm(reportInPersistence.getTerm());
					// �����ҵı�������set��adtiting����
					if (!reportInPersistence.getRepName().equals(
							reportInPersistence.getMChildReport()
									.getReportName()))
						aditing.setRepName(reportInPersistence.getRepName()
								+ "-"
								+ reportInPersistence.getMChildReport()
										.getReportName());
					else
						aditing.setRepName(reportInPersistence.getRepName());
					// �����ҵı�������set��aditing����
					aditing.setReportDate(reportInPersistence.getReportDate());
					// �����ҵ��ӱ���ID�Ž�aditing����
					aditing.setChildRepId(reportInPersistence.getMChildReport()
							.getComp_id().getChildRepId());
					// �����ҵİ汾��set��aditing����
					aditing.setVersionId(reportInPersistence.getMChildReport()
							.getComp_id().getVersionId());
					// ������ҳ����������м�¼��adtiing����add��arraylist�����з���action
					aditing.setAbmormityChangeFlag(reportInPersistence
							.getAbmormityChangeFlag());
					aditing.setDataRgTypeName(reportInPersistence
							.getMDataRgType().getDataRgDesc());
					aditing.setCurrName(reportInPersistence.getMCurr()
							.getCurName());
					/**��ʹ��hibernate ���Ը� 2011-12-21**/
					CollectTypeForm collectTypeForm = StrutsCollectTypeDelegate
							.selectCollectType(reportInPersistence
									.get_package());
					if (collectTypeForm != null)
						aditing
								.setCollectType(collectTypeForm
										.getCollectName());
					/**��ʹ��hibernate ���Ը� 2011-12-21**/
					MActuRep mActuRep = GetFreR(reportInPersistence);
					if (mActuRep != null) {
						// ����Ƶ��
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
			// ������Ӵ��ڣ���Ͽ��������Ự������
			if (conn != null)
				conn.closeSession();
		}
		return resultList;
	}

	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * ����������ѯ�Ի��ܷ�ʽ���ɵĻ��ܺõ�����
	 * 
	 * @param reportInForm
	 * @return
	 */
	public static List getOtherCollectRecords(ReportInForm reportInForm) {
		List resultList = null;

		// ���Ӷ���ͻỰ�����ʼ��
		DBConn conn = null;
		Session session = null;

		// ��ѯ����HQL������
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

		try { // List���ϵĲ���
			conn = new DBConn();
			// �����ӿ�ʼ�Ự
			session = conn.openSession();
			Query query = session.createQuery(hql);
			List list = query.list();

			if (list != null && list.size() > 0) {
				resultList = new ArrayList();
				// ѭ����ȡ���ݿ����������¼

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
			// ������Ӵ��ڣ���Ͽ��������Ự������
			if (conn != null)
				conn.closeSession();
		}
		return resultList;
	}

	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * ����������ѯ���ܺÿ����ϱ�������
	 * 
	 * @param reportInForm
	 * @param operator
	 * @return
	 */
	public static List getOnLineYBRecords(ReportInForm reportInForm,
			Operator operator) {
		List resultList = null;

		// ���Ӷ���ͻỰ�����ʼ��
		DBConn conn = null;
		Session session = null;

		// ��ѯ����HQL������
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
		/** ���ϱ�����Ȩ�� */
		if (operator == null)
			return resultList;

		if (operator.isSuperManager() == false) {
			if (operator.getChildRepReportPopedom() == null
					|| operator.getChildRepReportPopedom().equals(""))
				return resultList;
			/**���������ݿ��ж�*/
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

		try { // List���ϵĲ���
			conn = new DBConn();
			// �����ӿ�ʼ�Ự
			session = conn.openSession();
			Query query = session.createQuery(hql);
			List list = query.list();

			if (list != null && list.size() > 0) {
				resultList = new ArrayList();
				// ѭ����ȡ���ݿ����������¼

				for (Iterator it = list.iterator(); it.hasNext();) {
					// ʵ����һ���м���Aditing��������ҳ��������ֶ���
					Aditing aditing = new Aditing();
					// ���ɳ־û�����
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
					// �����ҵ��ı���id����aditing����
					aditing.setRepInId(reportInPersistence.getRepInId());
					aditing.setYear(reportInPersistence.getYear());
					aditing.setTerm(reportInPersistence.getTerm());
					// �����ҵı�������set��adtiting����
					if (!reportInPersistence.getRepName().equals(
							reportInPersistence.getMChildReport()
									.getReportName()))
						aditing.setRepName(reportInPersistence.getRepName()
								+ "-"
								+ reportInPersistence.getMChildReport()
										.getReportName());
					else
						aditing.setRepName(reportInPersistence.getRepName());
					// �����ҵı�������set��aditing����
					aditing.setReportDate(reportInPersistence.getReportDate());
					// �����ҵ��ӱ���ID�Ž�aditing����
					aditing.setChildRepId(reportInPersistence.getMChildReport()
							.getComp_id().getChildRepId());
					// �����ҵİ汾��set��aditing����
					aditing.setVersionId(reportInPersistence.getMChildReport()
							.getComp_id().getVersionId());
					// ������ҳ����������м�¼��adtiing����add��arraylist�����з���action
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
						// ����Ƶ��
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
			// ������Ӵ��ڣ���Ͽ��������Ự������
			if (conn != null)
				conn.closeSession();
		}
		return resultList;
	}

	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * @author jcm ��ʵ�ʱ�����޸�
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
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * ��ȡʵ�����ݱ�����Ϣ
	 * 
	 * @author jcm
	 * 
	 * @param repInId
	 *            Integer ʵ�����ݱ���ID
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
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * ��ѯ�������������ļ��ı���
	 * 
	 * @return ���ؼ�¼
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
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * ��ѯ��Ҫȫ�������ı���
	 * 
	 * @param reportInForm
	 *            ����FromBean
	 * @param operator
	 *            ��ǰ��¼�û�ID
	 * @return List ȫ��������������
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

			/** ��ѯ����״̬Ϊ���ͨ������ */
			StringBuffer where = new StringBuffer(" and ri.checkFlag="
					+ com.fitech.net.config.Config.CHECK_FLAG_PASS);

			/** ��ӱ����Ų�ѯ���������Դ�Сдģ����ѯ�� */
			if (reportInForm.getChildRepId() != null
					&& !reportInForm.getChildRepId().equals("")) {
				where
						.append(" and upper(ri.MChildReport.comp_id.childRepId) like upper('%"
								+ reportInForm.getChildRepId().trim() + "%')");
			}
			/** ��ӱ������Ʋ�ѯ������ģ����ѯ�� */
			if (reportInForm.getRepName() != null
					&& !reportInForm.getRepName().equals("")) {
				where.append(" and ri.repName like '%"
						+ reportInForm.getRepName().trim() + "%'");
			}
			/** ���ģ�����ͣ�ȫ��/����/��֧����ѯ���� */
			if (reportInForm.getFrOrFzType() != null
					&& !reportInForm.getFrOrFzType().equals(
							Config.DEFAULT_VALUE)) {
				where.append(" and ri.MChildReport.frOrFzType='"
						+ reportInForm.getFrOrFzType() + "'");
			}
			/** ��ӱ���Ƶ�ȣ���/��/����/�꣩��ѯ���� */
			if (reportInForm.getRepFreqId() != null
					&& !String.valueOf(reportInForm.getRepFreqId()).equals(
							Config.DEFAULT_VALUE)) {
				where
						.append(" and ri.MDataRgType.dataRangeId in (select M.comp_id.dataRangeId from MActuRep M "
								+ "where M.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and M.comp_id.versionId=ri.MChildReport.comp_id.versionId "
								+ "and M.comp_id.repFreqId="
								+ reportInForm.getRepFreqId() + ")");
			}
			/** ������ڣ���ݣ���ѯ���� */
			if (reportInForm.getYear() != null) {
				where.append(" and ri.year=" + reportInForm.getYear());
			}
			/** ������ڣ��·ݣ���ѯ���� */
			if (reportInForm.getTerm() != null) {
				where.append(" and ri.term=" + reportInForm.getTerm());
			}

			/** ��ӱ���鿴Ȩ�ޣ������û�������ӣ� */
			if (operator.isSuperManager() == false) {
				if (operator.getChildRepSearchPopedom() == null
						|| operator.getChildRepSearchPopedom().equals(""))
					return resList;
				/**���������ݿ��ж�*/
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
			// �ر����ݿ�����
			if (conn != null)
				conn.closeSession();
		}
		return resList;
	}

	/**
	 * ʹ��jdbc ��Ҫ�޸� ���Ը� 2011-12-22
	 * ����ͳ������
	 * 
	 * @author �ܷ���
	 * @param reportInForm
	 *            ����FormBean
	 * @param operator
	 *            ��ǰ��¼�û�
	 * @return ����ͳ������
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
				rep_freq = "('��','��','����','��')";
			else if (term == 6)
				rep_freq = "('��','��','����')";
			else if (term == 3 || term == 9)
				rep_freq = "('��','��')";
			else
				rep_freq = "('��')";
			
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
			/** ��ѯ����״̬Ϊ���ͨ������ */

			/** ��ӱ����Ų�ѯ���������Դ�Сдģ����ѯ�� */
			if (reportInForm.getChildRepId() != null
					&& !reportInForm.getChildRepId().equals("")) {
				where.append(" and upper(a.CHILD_REP_ID) like upper('%"
						+ reportInForm.getChildRepId().trim() + "%')");
			}
			/** ��ӱ������Ʋ�ѯ������ģ����ѯ�� */
			if (reportInForm.getRepName() != null
					&& !reportInForm.getRepName().equals("")) {
				where.append(" and a.REPORT_NAME like '%"
						+ reportInForm.getRepName().trim() + "%'");
			}
			/** ���ģ�����ͣ�ȫ��/����/��֧����ѯ���� */
			if (reportInForm.getFrOrFzType() != null
					&& !reportInForm.getFrOrFzType().equals(
							Config.DEFAULT_VALUE)) {
				where.append(" and a.frOrFzType='"
						+ reportInForm.getFrOrFzType() + "'");
			}
			/** ��ӱ���Ƶ�ȣ���/��/����/�꣩��ѯ���� */
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
			/** ��ӱ���״̬��δ���/���ͨ��/���δͨ������ѯ���� */
			if (reportInForm.getCheckFlag() != null
					&& !String.valueOf(reportInForm.getCheckFlag()).equals(
							Config.DEFAULT_VALUE)) {
				// ��ʱ��-10����δ�ϱ�
				if (reportInForm.getCheckFlag().equals(Short.valueOf("-10"))) {
					where
							.append(" and (b.CHECK_FLAG is null or b.CHECK_FLAG not in(-1,0,1)) ");
				} else
					where.append(" and b.CHECK_FLAG="
							+ reportInForm.getCheckFlag());
			}
			/** ��ӱ���鿴Ȩ�ޣ������û�������ӣ� */
			if (operator.isSuperManager() == false) {
				if (operator.getChildRepSearchPopedom() == null
						|| operator.getChildRepSearchPopedom().equals(""))
					return count;
				/**���������ݿ��ж�*/
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
			/**jdbc���� ��Ҫ�޸� ���Ը� 2011-12-21**/
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
			// �ر����ݿ�����
			if (conn != null)
				conn.closeSession();
		}
		return count;
	}

	/**
	 * jdbc���� oracle�﷨ ��Ҫ�޸� ���Ը� 2011-12-21
	 * sql��˼�ǲ��ǰ���� ���Ըĳ�sqlserver�﷨ top
	 * ���������ݿ��ж�
	 * ��ѯ���Ե����������ļ���¼��
	 * 
	 * @param reportInForm
	 *            ����FormBean
	 * @param offset
	 *            ƫ����
	 * @param limit
	 *            ÿҳ��ʾ��¼��
	 * @param operator
	 *            ��ǰ��¼�û�
	 * @return List ���Ե����������ļ������
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
				rep_freq = "('��','��','����','��','��')";
			else if (term == 6)
				rep_freq = "('��','��','����','��')";
			else if (term == 3 || term == 9)
				rep_freq = "('��','��','��')";
			else
				rep_freq = "('��','��')";
			StringBuffer sql=null;
			/**oracle�﷨ ��Ҫ�޸� ���Ը� 2011-12-21
			 * ���������ݿ��ж�**/
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
			
			/** ��ӱ����Ų�ѯ���������Դ�Сдģ����ѯ�� */
			if (reportInForm.getChildRepId() != null
					&& !reportInForm.getChildRepId().equals("")) {
				where.append(" and upper(a.CHILD_REP_ID) like upper('%"
						+ reportInForm.getChildRepId().trim() + "%')");
			}
			/** ��ӱ������Ʋ�ѯ������ģ����ѯ�� */
			if (reportInForm.getRepName() != null
					&& !reportInForm.getRepName().equals("")) {
				where.append(" and a.REPORT_NAME like '%"
						+ reportInForm.getRepName().trim() + "%'");
			}
			/** ���ģ�����ͣ�ȫ��/����/��֧����ѯ���� */
			if (reportInForm.getFrOrFzType() != null
					&& !reportInForm.getFrOrFzType().equals(
							Config.DEFAULT_VALUE)) {
				where.append(" and a.frOrFzType='"
						+ reportInForm.getFrOrFzType() + "'");
			}
			/** ��ӱ���Ƶ�ȣ���/��/����/�꣩��ѯ���� */
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
			/** ��ӱ���״̬��δ���/���ͨ��/���δͨ������ѯ���� */
			if (reportInForm.getCheckFlag() != null
					&& !String.valueOf(reportInForm.getCheckFlag()).equals(
							Config.DEFAULT_VALUE)) {
				// ��ʱ��-10����δ�ϱ�
				if (reportInForm.getCheckFlag().equals(Short.valueOf("-10"))) {
					where
							.append(" and (b.CHECK_FLAG is null or b.CHECK_FLAG not in(-1,0,1)) ");
				} else
					where.append(" and b.CHECK_FLAG="
							+ reportInForm.getCheckFlag());
			}
			/** ��ӱ���鿴Ȩ�ޣ������û�������ӣ� 
			 * ���������ݿ��ж�*/
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
			/**jdbc���� ��Ҫ�޸� ���Ը� 2011-12-21**/
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
				// ���ñ���ID��ʶ��
				// aditing.setRepInId(reportInRecord.getRepInId());
				// ���ñ������
				aditing.setYear(reportInForm.getYear());
				// ���ñ�������
				aditing.setTerm(reportInForm.getTerm());
				// ���ñ�������
				aditing.setRepName(rs.getString("REPORT_NAME"));
				// ���ñ���������
				// aditing.setReportDate(reportInRecord.getReportDate());
				// ���ñ�����
				aditing.setChildRepId(rs.getString("CHILD_REP_ID"));
				// ���ñ���汾��
				aditing.setVersionId(rs.getString("VERSION_ID"));
				// ���ñ����������
				aditing.setCurrName(rs.getString("CUR_NAME"));
				aditing.setCurId(rs.getInt("CUR_ID"));

				// ���ñ��Ϳھ�
				aditing.setDataRgTypeName(rs.getString("DATA_RG_DESC"));
				// ���ñ���Ƶ��
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
			// �ر����ݿ�����
			if (conn != null)
				conn.closeSession();
		}
		return resList;
	}

	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * ����reportIn���б����˺��������Ϣ
	 * 
	 * @param reportInForm
	 * @return
	 * @throws Exception
	 */
	public static boolean updateChecker(
			com.cbrc.smis.form.ReportInForm reportInForm) throws Exception {
		// ���±�־
		boolean bool = false;
		// ��������
		DBConn conn = null;
		Session session = null;
		// reportform���Ƿ��в���
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
			// ���conn������Ȼ����,�������ǰ���񲢶Ͽ�����
			if (conn != null)
				conn.endTransaction(bool);
		}
		// ���³ɹ����ذ�!!!
		return bool;
	}

	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * ����鿴�Ĳ����ܼ�¼��
	 * 
	 * @author jcm
	 */
	public static int getRecordCountOfmanual2(
			ReportInInfoForm reportInInfoForm, Operator operator) {

		int count = 0;

		// ���Ӷ���ͻỰ�����ʼ��
		DBConn conn = null;
		Session session = null;

		// ��ѯ����HQL������
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

			// �����������ж�
			// �����������ж�
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
			// ��־��Χ�������ж�

			// �������Ƶ��ж�
			if (reportInInfoForm.getRepName() != null
					&& !reportInInfoForm.getRepName().equals("")) {
				where.append(" and ri.repName like '%"
						+ reportInInfoForm.getRepName() + "%'");

			}
			// List���ϵĲ���
			// ��ʼ��
			hql.append(where.toString());
			// conn�����ʵ����
			conn = new DBConn();
			// �����ӿ�ʼ�Ự
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
			// ������Ӵ��ڣ���Ͽ��������Ự������
			if (conn != null)
				conn.closeSession();
		}
		return count;
	}

	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * ����鿴 ȡ��ReportIn�з��������ļ�¼���� zhangxinke
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

				// �������Ƶ��ж�
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
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * ����鿴 ȡ��ReportIn�з��������ļ�¼ zhangxinke
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
				// �������Ƶ��ж�
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
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * ���ͻ����鿴����״̬ jcm
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

			// �����������ж�
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
			// �������Ƶ��ж�
			if (reportInInfoForm.getRepName() != null
					&& !reportInInfoForm.getRepName().equals("")) {
				where.append(" and ri.repName like '%"
						+ reportInInfoForm.getRepName() + "%'");
			}

			// conn�����ʵ����
			conn = new DBConn();
			// �����ӿ�ʼ�Ự
			session = conn.openSession();
			// ��Ӽ�����Session
			hql.append(where.toString());

			Query query = session.createQuery(hql.toString());

			query.setFirstResult(offset);
			query.setMaxResults(limit);
			List list = query.list();

			if (list != null) {
				retvals = new ArrayList();
				// ѭ����ȡ���ݿ����������¼
				for (Iterator it = list.iterator(); it.hasNext();) {
					// ʵ����һ���м���Aditing��������ҳ��������ֶ���
					Aditing aditing = new Aditing();
					// ���ɳ־û�����
					ReportIn reportInPersistence = (ReportIn) it.next();
					// �����ҵ�����˱�־set��aditing����
					aditing.setCheckFlag(reportInPersistence.getCheckFlag());
					// �����ҵ��ı���id����aditing����
					aditing.setRepInId(reportInPersistence.getRepInId());
					aditing.setYear(reportInPersistence.getYear());
					aditing.setTerm(reportInPersistence.getTerm());
					// �����ҵı�������set��adtiting����
					if (!reportInPersistence.getRepName().equals(
							reportInPersistence.getMChildReport()
									.getReportName()))
						aditing.setRepName(reportInPersistence.getRepName()
								+ "-"
								+ reportInPersistence.getMChildReport()
										.getReportName());
					else
						aditing.setRepName(reportInPersistence.getRepName());
					// �����ҵı�������set��aditing����
					aditing.setReportDate(reportInPersistence.getReportDate());
					// �����ҵ��ӱ���ID�Ž�aditing����
					aditing.setChildRepId(reportInPersistence.getMChildReport()
							.getComp_id().getChildRepId());
					// �����ҵİ汾��set��aditing����
					aditing.setVersionId(reportInPersistence.getMChildReport()
							.getComp_id().getVersionId());
					// ���ݿھ�
					aditing.setDataRgTypeName(reportInPersistence
							.getMDataRgType().getDataRgDesc());
					// ����Ƶ��
					// aditing.setActuFreqName(reportInPersistence.);
					/**��ʹ��hibernate ���Ը� 2011-12-21**/
					MActuRep mActuRep = GetFreR(reportInPersistence);
					if (mActuRep != null) {
						aditing.setDataRgTypeName(mActuRep.getMDataRgType()
								.getDataRgDesc());
						aditing.setActuFreqName(mActuRep.getMRepFreq()
								.getRepFreqName());
					}
					// ��˲�ͨ����ԭ��
					/**��ʹ��hibernate ���Ը� 2011-12-21**/
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
			// ������Ӵ��ڣ���Ͽ��������Ự������
			if (conn != null)
				conn.closeSession();
		}
		return retvals;
	}

	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * �ж��Ƿ���Ҫ�ϱ�(��Ҫ�����ļ�)(û�м�¼Ҫ���±�,)***����ֻ��Ҫ�ж��Ƿ�
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
			
			/**��ʹ��hibernate ���Ը� 2011-12-21**/
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
					aditing.setWhy("�ѱ�");
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
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * �ж��Ƿ���Ҫ�ϱ�(��Ҫ�����ļ�)(û�м�¼Ҫ���±�,)***����ֻ��Ҫ�ж��Ƿ�
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
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * @param repInId
	 * @return
	 */
	public static boolean updateReportIn(Integer repInId) {
		// ���±�־
		boolean result = false;
		// ��������
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
			// �쳣�Ĳ�׽����
		} catch (HibernateException he) {
			result = false;
			log.printStackTrace(he);
		} catch (Exception e) {
			result = false;
			log.printStackTrace(e);
		} finally {
			// ���conn������Ȼ����,�������ǰ���񲢶Ͽ�����
			if (conn != null)
				conn.endTransaction(result);
		}
		// ���³ɹ����ذ�!!!
		return result;
	}
	
	/***
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * @param repInId
	 * @return
	 */
	public static boolean updateReportInCheckFlag(Integer repInId) {
		// ���±�־
		boolean result = false;
		// ��������
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
			// �쳣�Ĳ�׽����
		} catch (HibernateException he) {
			result = false;
			log.printStackTrace(he);
		} catch (Exception e) {
			result = false;
			log.printStackTrace(e);
		} finally {
			// ���conn������Ȼ����,�������ǰ���񲢶Ͽ�����
			if (conn != null)
				conn.endTransaction(result);
		}
		// ���³ɹ����ذ�!!!
		return result;
	}
	
	/****
	 * ��ʹ��hibernate ���Ը� 2011-12-21
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
		// ��ѯ����HQL������
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
			// conn�����ʵ����
			conn = new DBConn();
			// �����ӿ�ʼ�Ự
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
			// ������Ӵ��ڣ���Ͽ��������Ự������
			if (conn != null)
				conn.closeSession();
		}
		return count;
	}

	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * <p>
	 * ����:��ĳ������ĳ��״̬ĳ�������ñ������
	 * </p>
	 * <p>
	 * ����: year��term��orgId��flag��childReportIds��'G0100','G0200'��
	 * </p>
	 * <p>
	 * ���ڣ�2007-11-18
	 * </p>
	 * <p>
	 * ���ߣ��ܷ���
	 * </p>
	 */
	public static int getYBRecordCount(Integer year, Integer term,
			String orgId, Short flag, String childReportIds) {
		int count = 0;
		DBConn conn = null;
		Session session = null;
		try {
			// ��ѯ����HQL������

			StringBuffer hql = new StringBuffer(
					"select count(*) from ReportIn ri WHERE "
							+ "ri.times=1 and ri.checkFlag=" + flag
							+ " and ri.orgId='" + orgId + "'");
			hql.append(" and ri.year=" + year);
			hql.append(" and ri.term=" + term);

			// ���ϱ�������Ȩ��
			if (childReportIds != null && !childReportIds.equals(""))
				hql.append(" and ri.MChildReport.comp_id.childRepId in ("
						+ childReportIds + ")");
			// conn�����ʵ����
			conn = new DBConn();
			// �����ӿ�ʼ�Ự
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
			// ������Ӵ��ڣ���Ͽ��������Ự������
			if (conn != null)
				conn.closeSession();
		}
		return count;
	}

	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * ��ѯδ��˱������ݲ�ͬ�û�
	 * 
	 * @param reportInForm
	 * @param operator
	 * @return
	 */
	public static int getWSRecordCountOfmanual(Operator operator) {

		int count = 0;
		// ���Ӷ���ͻỰ�����ʼ��
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

		/** ����Ȩ�� */
		if (operator.getOrgPopedomSQL() != null
				&& !operator.getOrgPopedomSQL().equals("")) {
			String sql = "select distinct pv.comp_id.orgId from MPurView pv where pv.comp_id.userGrpId in("
					+ operator.getUserGrpIds() + ")";
			where.append(" and ri.orgId in(" + sql + ")");
		} else
			return 0;

		/** ����Ȩ�� */
		if (operator.getChildRepPodedomSQL() != null
				&& !operator.getChildRepPodedomSQL().equals(""))
			where.append(" and ri.MChildReport.comp_id.childRepId in("
					+ operator.getChildRepPodedomSQL() + ")");
		else
			return 0;

		hql.append(where.toString());

		try {
			// conn�����ʵ����
			conn = new DBConn();
			// �����ӿ�ʼ�Ự
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
			// ������Ӵ��ڣ���Ͽ��������Ự������
			if (conn != null)
				conn.closeSession();
		}
		return count;
	}

	
	/**
	 * ��ʹ��hibernate  ���Ը�  2011-12-21
	 * �޸ı���ı�־λ
	 * 
	 * @param repInId
	 * @return
	 */
	public static boolean updateReportInCheckFlag(Integer repInId,
			Short checkFlag) {
		// ���±�־
		boolean result = false;
		// ��������
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
			// �쳣�Ĳ�׽����
		} catch (HibernateException he) {
			result = false;
			log.printStackTrace(he);
		} catch (Exception e) {
			result = false;
			log.printStackTrace(e);
		} finally {
			// ���conn������Ȼ����,�������ǰ���񲢶Ͽ�����
			if (conn != null)
				conn.endTransaction(result);
		}
		// ���³ɹ����ذ�!!!
		return result;
	}

	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * �޸ı���ı�־λ(Ӧ�������淽����Ϊ��static)
	 * 
	 * @param repInId
	 * @return
	 */
	public boolean updateReportInCheckFlag_e(Integer repInId, Short checkFlag) {
		// ���±�־
		boolean result = false;
		// ��������
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
			// �쳣�Ĳ�׽����
		} catch (HibernateException he) {
			result = false;
			log.printStackTrace(he);
		} catch (Exception e) {
			result = false;
			log.printStackTrace(e);
		} finally {
			// ���conn������Ȼ����,�������ǰ���񲢶Ͽ�����
			if (conn != null)
				conn.endTransaction(result);
		}
		// ���³ɹ����ذ�!!!
		return result;
	}

	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * ���±�־(�Ƿ��ֹ���ƽ��)
	 * 
	 * @param reportInForm
	 * @return
	 */
	public static boolean updateReportInHandworkFlag(Integer repInId,
			Integer handFlag) {
		if (repInId == null)
			return false;
		// ���±�־
		boolean result = false;
		// ��������
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
			// �쳣�Ĳ�׽����
		} catch (HibernateException he) {
			result = false;
			log.printStackTrace(he);
		} catch (Exception e) {
			result = false;
			log.printStackTrace(e);
		} finally {
			// ���conn������Ȼ����,�������ǰ���񲢶Ͽ�����
			if (conn != null)
				conn.endTransaction(result);
		}
		// ���³ɹ����ذ�!!!
		return result;
	}

	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * �鿴�û����Ƿ��Ѿ��б����ϱ�
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
			// ������Ӵ��ڣ���Ͽ��������Ự������
			if (conn != null)
				conn.closeSession();
		}
		return count;
	}

	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * ���ͨ��ȫ������
	 * 
	 * @author jcm
	 * @param reportInForm
	 *            ����FormBean
	 * @param operator
	 *            ��ǰ��¼�û�
	 * @return List ��ѯ�����
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

			/** ��ѯ����״̬Ϊδ��˱��� */
			StringBuffer where = new StringBuffer(" and ri.checkFlag="
					+ com.fitech.net.config.Config.CHECK_FLAG_UNCHECK);

			/** ��ӱ����Ų�ѯ���������Դ�Сдģ����ѯ�� */
			if (reportInForm.getChildRepId() != null
					&& !reportInForm.getChildRepId().equals("")) {
				where
						.append(" and upper(ri.MChildReport.comp_id.childRepId) like upper('%"
								+ reportInForm.getChildRepId().trim() + "%')");
			}
			/** ��ӱ������Ʋ�ѯ������ģ����ѯ�� */
			if (reportInForm.getRepName() != null
					&& !reportInForm.getRepName().equals("")) {
				where.append(" and ri.repName like '%"
						+ reportInForm.getRepName().trim() + "%'");
			}
			/** ���ģ�����ͣ�ȫ��/����/��֧����ѯ���� */
			if (reportInForm.getFrOrFzType() != null
					&& !reportInForm.getFrOrFzType().equals(
							Config.DEFAULT_VALUE)) {
				where.append(" and ri.MChildReport.frOrFzType='"
						+ reportInForm.getFrOrFzType() + "'");
			}
			/** ��ӱ���Ƶ�ȣ���/��/����/�꣩��ѯ���� */
			if (reportInForm.getRepFreqId() != null
					&& !String.valueOf(reportInForm.getRepFreqId()).equals(
							Config.DEFAULT_VALUE)) {
				where
						.append(" and ri.MDataRgType.dataRangeId in (select M.comp_id.dataRangeId from MActuRep M "
								+ "where M.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and M.comp_id.versionId=ri.MChildReport.comp_id.versionId "
								+ "and M.comp_id.repFreqId="
								+ reportInForm.getRepFreqId() + ")");
			}
			/** ������ڣ���ݣ���ѯ���� */
			if (reportInForm.getYear() != null) {
				where.append(" and ri.year=" + reportInForm.getYear());
			}
			/** ������ڣ��·ݣ���ѯ���� */
			if (reportInForm.getTerm() != null) {
				where.append(" and ri.term=" + reportInForm.getTerm());
			}
			/** ��ӻ�����ѯ���� */
			if (reportInForm.getOrgId() != null
					&& !reportInForm.getOrgId().equals(Config.DEFAULT_VALUE)) {
				where.append(" and ri.orgId='" + reportInForm.getOrgId().trim()
						+ "'");
			}

			/** ��ӱ������Ȩ�ޣ������û�������ӣ�
			 * ���������ݿ��ж� */
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
			// �ر����ݿ�����
			if (conn != null)
				conn.closeSession();
		}
		return resList;
	}

	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * ��Ҫ����ȫ��У��ı���(�����ϱ���������)
	 * 
	 * @param reportInForm
	 * @param operator
	 *            ��ǰ��¼�û�
	 * @return List У��ı���ID�б�
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

			/** ֻУ��'δУ��'��'��У��'�ı��� */
			hql.append(" and ri.checkFlag in ("
					+ com.fitech.net.config.Config.CHECK_FLAG_AFTERJY + ","
					+ com.fitech.net.config.Config.CHECK_FLAG_AFTERSAVE + ")");

			/** ���ϱ���ʱ��ͱ��ͻ��� */
			hql.append(" and ri.orgId='" + reportInForm.getOrgId()
					+ "' and ri.year=" + reportInForm.getYear()
					+ " and ri.term=" + reportInForm.getTerm());

			/** ���ϱ���Ȩ�� */
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
	 * oracle�﷨ ��Ҫ�޸� ���Ը� 2011-12-21
	 * �������������ͳ��(Ӧ��ͳ��)
	 * 
	 * @param orgId
	 *            ����ID
	 * @param reportInForm
	 *            ����formBean
	 * @author jcm
	 * @return OrgNetForm ����formBean
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
			/** ����ʱ���ж�Ӧ������Ƶ�� */
			int term = reportInForm.getTerm().intValue();
			int year = reportInForm.getYear().intValue();

			String rep_freq = "";
			if (term == 12)
				rep_freq = "('��','��','����','��')";
			else if (term == 6)
				rep_freq = "('��','��','����')";
			else if (term == 3 || term == 9)
				rep_freq = "('��','��')";
			else
				rep_freq = "('��')";

			/** �����������ͳ��SQL(Ӧ��ͳ��) */
			/**oracle�﷨ ��Ҫ�޸� ���Ը� 2011-12-21
			 * ���������ݿ��ж�**/
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
			// �ر����ݿ�����
			if (conn != null)
				conn.closeSession();
		}
		return count;
	}

	/***************************************************************************
	 * �������������ͳ��(�Ѹ���ͳ��)
	 * 
	 * @param orgId
	 *            ����ID
	 * @param reportInForm
	 *            ����formBean
	 * @author jcm
	 * @return OrgNetForm ����formBean
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
			*//** �����������ͳ��SQL(���ͱ�����ͨ����) *//*
			String hql = "select count(*) from ReportIn ri WHERE "
					+ "ri.times=(select max(r.times) from ReportIn r WHERE "
					+ "r.MChildReport.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and "
					+ "r.MChildReport.comp_id.versionId=ri.MChildReport.comp_id.versionId and "
					+ "r.MDataRgType.dataRangeId=ri.MDataRgType.dataRangeId and r.orgId=ri.orgId and "
					+ "r.MCurr.curId=ri.MCurr.curId and r.year=ri.year and r.term=ri.term and r.checkFlag=ri.checkFlag) and "
					+ "ri.year=" + reportInForm.getYear() + " and ri.term="
					+ reportInForm.getTerm() + " and " + "ri.orgId='" + orgId
					+ "' and ri.checkFlag in ("
					// ���ϸ���
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
			// �ر����ݿ�����
			if (conn != null)
				conn.closeSession();
		}
		return count;
	}*/

	/***************************************************************************
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * �������������ͳ��(�ѱ���ǩͳ��)
	 * 
	 * @param orgId
	 *            ����ID
	 * @param reportInForm
	 *            ����formBean
	 * @author jcm
	 * @return OrgNetForm ����formBean
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
			/** �����������ͳ��SQL(���ͱ������ͨ����) */
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
			// �ر����ݿ�����
			if (conn != null)
				conn.closeSession();
		}
		return count;
	}

	/***************************************************************************
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * �������������ͳ��(�ѱ�ͳ��)
	 * 
	 * @param orgId
	 *            ����ID
	 * @param reportInForm
	 *            ����formBean
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
			/** �����������ͳ��SQL(���ͱ������δ������ͨ����) */
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
			// �ر����ݿ�����
			if (conn != null)
				conn.closeSession();
		}
		return count;
	}

	/***************************************************************************
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * �������������ͳ��(�ر�ͳ��)
	 * 
	 * @param orgId
	 *            ����ID
	 * @param reportInForm
	 *            ����formBean
	 * @author jcm
	 * @return OrgNetForm ����formBean
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
			/** �����������ͳ��SQL(�ر�) */
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
			// �ر����ݿ�����
			if (conn != null)
				conn.closeSession();
		}
		return count;
	}

	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * �����ر�
	 * 
	 * @author gongming
	 * @date 2008-01-18
	 * @param pId
	 *            String �ַ�������
	 * @param cause
	 *            String �����ر�ԭ��
	 * @return �ɹ�����true ʧ�ܷ���false
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
	 * ����oracle�﷨(upper) ��Ҫ�޸� ���Ը� 2011-12-21
	 * ʹ��hibernate 
	 * ��ѯ���Ե����������ļ���¼��
	 * 
	 * @author jcm
	 * @param reportInForm
	 *            ����FormBean
	 * @param operator
	 *            ��ǰ��¼�û�
	 * @return ���Ե����������ļ���¼��
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

			/** ��ѯ����״̬Ϊ���ͨ������ */
			StringBuffer where = new StringBuffer(" and ri.checkFlag="
					+ com.fitech.net.config.Config.CHECK_FLAG_PASS);

			/** ��ӱ����Ų�ѯ���������Դ�Сдģ����ѯ�� */
			/**����oracle�﷨ ��Ҫ�޸� ���Ը� 2011-12-21**/
			if (reportInForm.getChildRepId() != null
					&& !reportInForm.getChildRepId().equals("")) {
				where
						.append(" and upper(ri.MChildReport.comp_id.childRepId) like upper('%"
								+ reportInForm.getChildRepId().trim() + "%')");
			}
			/** ��ӱ������Ʋ�ѯ������ģ����ѯ�� */
			if (reportInForm.getRepName() != null
					&& !reportInForm.getRepName().equals("")) {
				where.append(" and ri.repName like '%"
						+ reportInForm.getRepName().trim() + "%'");
			}
			/** ���ģ�����ͣ�ȫ��/����/��֧����ѯ���� */
			if (reportInForm.getFrOrFzType() != null
					&& !reportInForm.getFrOrFzType().equals(
							Config.DEFAULT_VALUE)) {
				where.append(" and ri.MChildReport.frOrFzType='"
						+ reportInForm.getFrOrFzType() + "'");
			}
			/** ��ӱ���Ƶ�ȣ���/��/����/�꣩��ѯ���� */
			if (reportInForm.getRepFreqId() != null
					&& !String.valueOf(reportInForm.getRepFreqId()).equals(
							Config.DEFAULT_VALUE)) {
				where
						.append(" and ri.MDataRgType.dataRangeId in (select M.comp_id.dataRangeId from MActuRep M "
								+ "where M.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and M.comp_id.versionId=ri.MChildReport.comp_id.versionId "
								+ "and M.comp_id.repFreqId="
								+ reportInForm.getRepFreqId() + ")");
			}
			/** ������ڣ���ݣ���ѯ���� */
			if (reportInForm.getYear() != null) {
				where.append(" and ri.year=" + reportInForm.getYear());
			}
			/** ������ڣ��·ݣ���ѯ���� */
			if (reportInForm.getTerm() != null) {
				where.append(" and ri.term=" + reportInForm.getTerm());
			}

			/** ��ӱ���鿴Ȩ�ޣ������û�������ӣ�
			 * ���������ݿ��ж� */
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
			// �ر����ݿ�����
			if (conn != null)
				conn.closeSession();
		}
		return count;
	}

	/**
	 * ����oracle�﷨(upper) ��Ҫ�޸� ���Ը� 2011-12-21
	 * ʹ��hibernate
	 * ��ѯ���Ե����������ļ���¼��
	 * 
	 * @param reportInForm
	 *            ����FormBean
	 * @param offset
	 *            ƫ����
	 * @param limit
	 *            ÿҳ��ʾ��¼��
	 * @param operator
	 *            ��ǰ��¼�û�
	 * @return List ���Ե����������ļ������
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

			/** ��ѯ����״̬Ϊ���ͨ������ */
			StringBuffer where = new StringBuffer(" and ri.checkFlag="
					+ com.fitech.net.config.Config.CHECK_FLAG_PASS);

			/** ��ӱ����Ų�ѯ���������Դ�Сдģ����ѯ�� */
			/**����oracle�﷨ ��Ҫ�޸� ���Ը� 2011-12-21**/
			if (reportInForm.getChildRepId() != null
					&& !reportInForm.getChildRepId().equals("")) {
				where
						.append(" and upper(ri.MChildReport.comp_id.childRepId) like upper('%"
								+ reportInForm.getChildRepId().trim() + "%')");
			}
			/** ��ӱ������Ʋ�ѯ������ģ����ѯ�� */
			if (reportInForm.getRepName() != null
					&& !reportInForm.getRepName().equals("")) {
				where.append(" and ri.repName like '%"
						+ reportInForm.getRepName().trim() + "%'");
			}
			/** ���ģ�����ͣ�ȫ��/����/��֧����ѯ���� */
			if (reportInForm.getFrOrFzType() != null
					&& !reportInForm.getFrOrFzType().equals(
							Config.DEFAULT_VALUE)) {
				where.append(" and ri.MChildReport.frOrFzType='"
						+ reportInForm.getFrOrFzType() + "'");
			}
			/** ��ӱ���Ƶ�ȣ���/��/����/�꣩��ѯ���� */
			if (reportInForm.getRepFreqId() != null
					&& !String.valueOf(reportInForm.getRepFreqId()).equals(
							Config.DEFAULT_VALUE)) {
				where
						.append(" and ri.MDataRgType.dataRangeId in (select M.comp_id.dataRangeId from MActuRep M "
								+ "where M.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and M.comp_id.versionId=ri.MChildReport.comp_id.versionId "
								+ "and M.comp_id.repFreqId="
								+ reportInForm.getRepFreqId() + ")");
			}
			/** ������ڣ���ݣ���ѯ���� */
			if (reportInForm.getYear() != null) {
				where.append(" and ri.year=" + reportInForm.getYear());
			}
			/** ������ڣ��·ݣ���ѯ���� */
			if (reportInForm.getTerm() != null) {
				where.append(" and ri.term=" + reportInForm.getTerm());
			}

			/** ��ӱ���鿴Ȩ�ޣ������û�������ӣ� 
			 * ���������ݿ��ж�*/
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
					// ���ñ���ID��ʶ��
					aditing.setRepInId(reportInRecord.getRepInId());
					// ���ñ������
					aditing.setYear(reportInRecord.getYear());
					// ���ñ�������
					aditing.setTerm(reportInRecord.getTerm());
					// ���ñ�������
					aditing.setRepName(reportInRecord.getRepName());
					// ���ñ���������
					aditing.setReportDate(reportInRecord.getReportDate());
					// ���ñ�����
					aditing.setChildRepId(reportInRecord.getMChildReport()
							.getComp_id().getChildRepId());
					// ���ñ���汾��
					aditing.setVersionId(reportInRecord.getMChildReport()
							.getComp_id().getVersionId());
					// ���ñ����������
					aditing.setCurrName(reportInRecord.getMCurr().getCurName());
					MActuRep mActuRep = GetFreR(reportInRecord);
					if (mActuRep != null) {
						// ���ñ��Ϳھ�
						aditing.setDataRgTypeName(mActuRep.getMDataRgType()
								.getDataRgDesc());
						// ���ñ���Ƶ��
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
			// �ر����ݿ�����
			if (conn != null)
				conn.closeSession();
		}
		return resList;
	}

	/**
	 * ����oracle�÷�(upper) ��Ҫ�޸� ���Ը� 2011-12-21
	 * �����˵Ĳ����ܼ�¼��
	 * 
	 * @param reportInForm
	 * @param operator
	 * @return int �ܼ�¼��
	 */
	public static int getRecordCountOfmanualRecheck(ReportInForm reportInForm,
			Operator operator) {
		int count = 0;
		DBConn conn = null;
		Session session = null;

		if (reportInForm == null || operator == null)
			return count;

		try {
			// ��ѯ����HQL������
			StringBuffer hql = new StringBuffer(
					"select count(*) from ReportIn ri WHERE "
							+ "ri.times=(select max(r.times) from ReportIn r where r.MChildReport.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and "
							+ "r.MChildReport.comp_id.versionId=ri.MChildReport.comp_id.versionId and r.MDataRgType.dataRangeId=ri.MDataRgType.dataRangeId and r.orgId=ri.orgId and "
							+ "r.MCurr.curId=ri.MCurr.curId and r.year=ri.year and r.term=ri.term and r.checkFlag=ri.checkFlag)");

			/** ��ѯ����״̬Ϊδ���˱��� */
			StringBuffer where = new StringBuffer(" and ri.checkFlag="
					+ com.fitech.net.config.Config.CHECK_FLAG_UNCHECK);

			/** ��ӱ����Ų�ѯ���������Դ�Сдģ����ѯ�� */
			/**����oracle�÷� ��Ҫ�޸� ���Ը� 2011-12-21**/
			if (reportInForm.getChildRepId() != null
					&& !reportInForm.getChildRepId().equals("")) {
				where
						.append(" and upper(ri.MChildReport.comp_id.childRepId) like upper('%"
								+ reportInForm.getChildRepId().trim() + "%')");
			}
			/** ��ӱ������Ʋ�ѯ������ģ����ѯ�� */
			if (reportInForm.getRepName() != null
					&& !reportInForm.getRepName().equals("")) {
				where.append(" and ri.repName like '%"
						+ reportInForm.getRepName().trim() + "%'");
			}
			/** ���ģ�����ͣ�ȫ��/����/��֧����ѯ���� */
			if (reportInForm.getFrOrFzType() != null
					&& !reportInForm.getFrOrFzType().equals(
							Config.DEFAULT_VALUE)) {
				where.append(" and ri.MChildReport.frOrFzType='"
						+ reportInForm.getFrOrFzType() + "'");
			}
			/** ��ӱ���Ƶ�ȣ���/��/����/�꣩��ѯ���� */
			if (reportInForm.getRepFreqId() != null
					&& !String.valueOf(reportInForm.getRepFreqId()).equals(
							Config.DEFAULT_VALUE)) {
				where
						.append(" and ri.MDataRgType.dataRangeId in (select M.comp_id.dataRangeId from MActuRep M "
								+ "where M.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and M.comp_id.versionId=ri.MChildReport.comp_id.versionId "
								+ "and M.comp_id.repFreqId="
								+ reportInForm.getRepFreqId() + ")");
			}
			/** ������ڣ���ݣ���ѯ���� */
			if (reportInForm.getYear() != null) {
				where.append(" and ri.year=" + reportInForm.getYear());
			}
			/** ������ڣ��·ݣ���ѯ���� */
			if (reportInForm.getTerm() != null) {
				where.append(" and ri.term=" + reportInForm.getTerm());
			}
			/** ��ӻ�����ѯ���� */
			if (!StringUtil.isEmpty(reportInForm.getOrgId()) 
					&& !reportInForm.getOrgId().equals(Config.DEFAULT_VALUE)) {
				where.append(" and ri.orgId='" + reportInForm.getOrgId().trim()
						+ "'");
			}

			/** ��ӱ���fu��Ȩ�ޣ������û�������ӣ�
			 * ���������ݿ��ж� */
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
			// �ر����ݿ�����
			if (conn != null)
				conn.closeSession();
		}
		return count;
	}

	/**
	 * ����oracle�﷨(upper) ��Ҫ�޸� ���Ը� 2011-12-21
	 * ʹ��hibernate
	 * �����˵Ĳ��Ҽ�¼����
	 * 
	 * @param reportInForm
	 *            ҳ��FormBean
	 * @param offset
	 *            ���ݿ��ѯ��ʼλ
	 * @param limit
	 *            ��ѯ��¼��
	 * @param operator
	 *            ��ǰ��¼�û�
	 * @return List ��ѯ��������
	 */
	public static List selectOfManualRecheck(ReportInForm reportInForm,
			int offset, int limit, Operator operator) {
		List resList = null;
		DBConn conn = null;
		Session session = null;

		if (reportInForm == null || operator == null)
			return resList;

		try {
			// ��ѯ����HQL������
			StringBuffer hql = new StringBuffer(
					"from ReportIn ri WHERE "
							+ "ri.times=(select max(r.times) from ReportIn r where r.MChildReport.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and "
							+ "r.MChildReport.comp_id.versionId=ri.MChildReport.comp_id.versionId and r.MDataRgType.dataRangeId=ri.MDataRgType.dataRangeId and r.orgId=ri.orgId and "
							+ "r.MCurr.curId=ri.MCurr.curId and r.year=ri.year and r.term=ri.term and r.checkFlag=ri.checkFlag)");

			/** ��ѯ����״̬Ϊδ���˱��� */
			StringBuffer where = new StringBuffer(" and ri.checkFlag="
					+ com.fitech.net.config.Config.CHECK_FLAG_UNCHECK);

			/** ��ӱ����Ų�ѯ���������Դ�Сдģ����ѯ�� */
			/**����oracle�﷨ ��Ҫ�޸� ���Ը� 2011-12-21**/
			if (reportInForm.getChildRepId() != null
					&& !reportInForm.getChildRepId().equals("")) {
				where
						.append(" and upper(ri.MChildReport.comp_id.childRepId) like upper('%"
								+ reportInForm.getChildRepId().trim() + "%')");
			}
			/** ��ӱ������Ʋ�ѯ������ģ����ѯ�� */
			if (reportInForm.getRepName() != null
					&& !reportInForm.getRepName().equals("")) {
				where.append(" and ri.repName like '%"
						+ reportInForm.getRepName().trim() + "%'");
			}
			/** ���ģ�����ͣ�ȫ��/����/��֧����ѯ���� */
			if (reportInForm.getFrOrFzType() != null
					&& !reportInForm.getFrOrFzType().equals(
							Config.DEFAULT_VALUE)) {
				where.append(" and ri.MChildReport.frOrFzType='"
						+ reportInForm.getFrOrFzType() + "'");
			}
			/** ��ӱ���Ƶ�ȣ���/��/����/�꣩��ѯ���� */
			if (reportInForm.getRepFreqId() != null
					&& !String.valueOf(reportInForm.getRepFreqId()).equals(
							Config.DEFAULT_VALUE)) {
				where
						.append(" and ri.MDataRgType.dataRangeId in (select M.comp_id.dataRangeId from MActuRep M "
								+ "where M.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and M.comp_id.versionId=ri.MChildReport.comp_id.versionId "
								+ "and M.comp_id.repFreqId="
								+ reportInForm.getRepFreqId() + ")");
			}
			/** ������ڣ���ݣ���ѯ���� */
			if (reportInForm.getYear() != null) {
				where.append(" and ri.year=" + reportInForm.getYear());
			}
			/** ������ڣ��·ݣ���ѯ���� */
			if (reportInForm.getTerm() != null) {
				where.append(" and ri.term=" + reportInForm.getTerm());
			}
			/** ��ӻ�����ѯ���� */
			if (!StringUtil.isEmpty(reportInForm.getOrgId())
					&& !reportInForm.getOrgId().equals(Config.DEFAULT_VALUE)) {
				where.append(" and ri.orgId='" + reportInForm.getOrgId().trim()
						+ "'");
			}

			/** ��ӱ���fu��Ȩ�ޣ������û�������ӣ� 
			 * ���������ݿ��ж�*/
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
					// ���ñ������״̬
					aditing.setCheckFlag(reportInRecord.getCheckFlag());
					// ���ñ��У����
					if (reportInRecord.getTblOuterValidateFlag() != null)
						aditing.setTblOuterValidateFlag(reportInRecord
								.getTblOuterValidateFlag());
					// ���ñ��ͻ�������
					/**����hibernate ���Ը� 2011-12-21**/
					orgNet = StrutsOrgNetDelegate.selectOne(reportInRecord
							.getOrgId());
					if (orgNet != null){
						aditing.setOrgName(orgNet.getOrgName());
						aditing.setOrgId(orgNet.getOrgId());
					}
					// ���ñ���ID��ʶ��
					aditing.setRepInId(reportInRecord.getRepInId());
					// ���ñ������
					aditing.setYear(reportInRecord.getYear());
					// ���ñ�������
					aditing.setTerm(reportInRecord.getTerm());
					// ���ñ�������
					aditing.setRepName(reportInRecord.getRepName());
					// ���ñ���������
					aditing.setReportDate(reportInRecord.getReportDate());
					// ���ñ�����
					aditing.setChildRepId(reportInRecord.getMChildReport()
							.getComp_id().getChildRepId());
					// ���ñ���汾��
					aditing.setVersionId(reportInRecord.getMChildReport()
							.getComp_id().getVersionId());
					// �����쳣�仯��־
					aditing.setAbmormityChangeFlag(reportInRecord
							.getAbmormityChangeFlag());
					// ���ñ����������
					aditing.setCurrName(reportInRecord.getMCurr().getCurName());
					aditing.setCurId(reportInRecord.getMCurr().getCurId());
					
					MActuRep mActuRep = GetFreR(reportInRecord);
					if (mActuRep != null) {
						// ���ñ��Ϳھ�
						aditing.setDataRgTypeName(mActuRep.getMDataRgType()
								.getDataRgDesc());
						// ���ñ��Ϳھ�
						aditing.setDataRangeId(mActuRep.getMDataRgType()
								.getDataRangeId());
						// ���ñ���Ƶ��
						aditing.setActuFreqName(mActuRep.getMRepFreq()
								.getRepFreqName());
						// ���ñ���Ƶ��
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
			// �ر����ݿ�����
			if (conn != null)
				conn.closeSession();
		}
		return resList;
	}

	/**
	 * ����oracle�﷨(upper) ��Ҫ�޸� ���Ը� 2011-12-21
	 * ʹ��hibernate
	 * ����ͨ��ȫ������
	 * 
	 * @author jcm
	 * @param reportInForm
	 *            ����FormBean
	 * @param operator
	 *            ��ǰ��¼�û�
	 * @return List ��ѯ�����
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

			/** ��ѯ����״̬Ϊδ��˱��� */
			StringBuffer where = new StringBuffer(" and ri.checkFlag="
					+ com.fitech.net.config.Config.CHECK_FLAG_UNCHECK);

			/** ��ӱ����Ų�ѯ���������Դ�Сдģ����ѯ�� */
			/**����oracle�﷨(upper) ��Ҫ�޸� ���Ը� 2011-12-21**/
			if (reportInForm.getChildRepId() != null
					&& !reportInForm.getChildRepId().equals("")) {
				where
						.append(" and upper(ri.MChildReport.comp_id.childRepId) like upper('%"
								+ reportInForm.getChildRepId().trim() + "%')");
			}
			/** ��ӱ������Ʋ�ѯ������ģ����ѯ�� */
			if (reportInForm.getRepName() != null
					&& !reportInForm.getRepName().equals("")) {
				where.append(" and ri.repName like '%"
						+ reportInForm.getRepName().trim() + "%'");
			}
			/** ���ģ�����ͣ�ȫ��/����/��֧����ѯ���� */
			if (reportInForm.getFrOrFzType() != null
					&& !reportInForm.getFrOrFzType().equals(
							Config.DEFAULT_VALUE)) {
				where.append(" and ri.MChildReport.frOrFzType='"
						+ reportInForm.getFrOrFzType() + "'");
			}
			/** ��ӱ���Ƶ�ȣ���/��/����/�꣩��ѯ���� */
			if (reportInForm.getRepFreqId() != null
					&& !String.valueOf(reportInForm.getRepFreqId()).equals(
							Config.DEFAULT_VALUE)) {
				where
						.append(" and ri.MDataRgType.dataRangeId in (select M.comp_id.dataRangeId from MActuRep M "
								+ "where M.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and M.comp_id.versionId=ri.MChildReport.comp_id.versionId "
								+ "and M.comp_id.repFreqId="
								+ reportInForm.getRepFreqId() + ")");
			}
			/** ������ڣ���ݣ���ѯ���� */
			if (reportInForm.getYear() != null) {
				where.append(" and ri.year=" + reportInForm.getYear());
			}
			/** ������ڣ��·ݣ���ѯ���� */
			if (reportInForm.getTerm() != null) {
				where.append(" and ri.term=" + reportInForm.getTerm());
			}
			/** ��ӻ�����ѯ���� */
			if (reportInForm.getOrgId() != null
					&& !reportInForm.getOrgId().equals(Config.DEFAULT_VALUE)) {
				where.append(" and ri.orgId='" + reportInForm.getOrgId().trim()
						+ "'");
			}

			/** ��ӱ������Ȩ�ޣ������û�������ӣ� 
			 * ���������ݿ��ж�*/
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
			// �ر����ݿ�����
			if (conn != null)
				conn.closeSession();
		}
		return resList;
	}

    /**
     * ��ʹ��hibernate ���Ը� 2011-12-21
     * ���ݲ�����ñ���
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
//				rep_freq = "('��','��','����','��')";
//			else if (term == 6)
//				rep_freq = "('��','��','����')";
//			else if (term == 3 || term == 9)
//				rep_freq = "('��','��')";
//			else
//				rep_freq = "('��')";
//			String strTerm =String.valueOf(term);
//			if(term<10)  
//			 strTerm ="0"+term;
			
			String hql="from ReportIn a where a.year=" + year 
				+ " and a.term=" + term 
//				+ " and a.repFreqName in"+rep_freq
				+ " and a.checkFlag=" + checkFlag
				+ " and times=1"
				+ " and a.orgId='"+operator.getOrgId()+"'";
			
			/**	�ӱ����� */
			if(reportInForm.getRepName() != null){
				hql += " and a.repName like '%" + reportInForm.getRepName() + "%'";
			}
			/**���ϱ���Ȩ��
			 * ���������ݿ��ж�*/
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
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * ����ǰ̨ҳ��Ĳ�ѯ������û�ѡ�и��ĵļ�¼д���˱�־Ϊ5
	 * 
	 * @author ����
	 * @param orgArray
	 *            �������Ļ���id��string���������
	 * @param checkSign
	 *            ���������û�ѡ�����˱�־
	 * @param reportInPersistence
	 *            �־û�����ReportIn
	 * @param he
	 *            HibernateException ���쳣��׽�׳�
	 * @param e
	 *            Exception ���쳣��׽�׳�
	 * @param result
	 *            boolean�ͱ���,���³ɹ�����true,���򷵻�false
	 */
	public static boolean updateRecheck(
			com.cbrc.smis.form.ReportInForm reportInForm) throws Exception {
		// ���±�־
		boolean result = true;
		// ��������
		DBConn conn = null;
		Session session = null;
		// reportform���Ƿ��в���
		if (reportInForm == null) {
			return false;
		}

		try {
			conn = new DBConn();
			session = conn.beginTransaction();

			// ѭ�����־û����󴫵�form����Ļ���id����Ĳ���
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

			// �쳣�Ĳ�׽����
		} catch (HibernateException he) {
			result = false;
			log.printStackTrace(he);
		} catch (Exception e) {
			result = false;
			log.printStackTrace(e);
		} finally {
			// ���conn������Ȼ����,�������ǰ���񲢶Ͽ�����
			if (conn != null)
				conn.endTransaction(result);
		}
		// ���³ɹ����ذ�!!!
		return result;
	}
	
	/***
	 * ��ʹ��hibernate ���Ը� 2011-12-21
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
