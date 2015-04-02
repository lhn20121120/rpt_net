package com.cbrc.smis.adapter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.org.adapter.StrutsMOrgDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.common.ConfigOncb;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.db2xml.P2P_Report_Data;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.form.ReportInInfoForm;
import com.cbrc.smis.hibernate.MActuRep;
import com.cbrc.smis.hibernate.ReportAgainSet;
import com.cbrc.smis.hibernate.ReportIn;
import com.cbrc.smis.hibernate.ReportInInfo;
import com.cbrc.smis.hibernate.ReportInInfoPK;
import com.cbrc.smis.other.Aditing;
import com.cbrc.smis.proc.impl.Expression;
import com.cbrc.smis.proc.impl.LogInImpl;
import com.cbrc.smis.proc.impl.Report;
import com.cbrc.smis.proc.impl.ReportDDImpl;
import com.cbrc.smis.proc.impl.ReportImpl;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechLog;
import com.fitech.net.adapter.StrutsOrgNetDelegate;
import com.fitech.net.collect.bean.CollectReport;
import com.fitech.net.form.OrgNetForm;
import com.fitech.net.hibernate.OrgNet;

/**
 * �鿴��ǰ�����ı�����Ϣ This is a delegate class to handle interaction with the backend
 * persistence layer of hibernate. It has a set of methods to handle persistence
 * for ReportInInfo data (i.e. com.cbrc.smis.form.ReportInInfoForm objects).
 * 
 * @author
 */
public class StrutsReportInInfoDelegate
{
	private static FitechException log = new FitechException(StrutsReportInInfoDelegate.class);

	/**
	 * ����쳣�仯��Ϣ�б�
	 * 
	 * @author rds
	 * @date 2006-01-03 0:07
	 * 
	 * @param repInId
	 *            Integer ʵ�����ݱ���ID
	 * @param childRepId
	 *            String �ӱ���ID
	 * @param versionId
	 *            String �汾��
	 * @return List
	 */
	public static List find(Integer repInId, String childRepId, String versionId, String orgId)
	{
		List resList = null;

		if (repInId == null || childRepId == null || versionId == null)
			return resList;

		DBConn conn = null;

		try
		{
			String hql = "from ReportInInfo ri where ri.comp_id.repInId=" + repInId
					+ " and ri.comp_id.cellId in (select ac.comp_id.cellId from AbnormityChange ac where " + " ac.comp_id.childRepId='" + childRepId + "' and "
					+ " ac.comp_id.versionId='" + versionId + "' and " + " ac.orgId='" + orgId + "'" + ")";
			conn = new DBConn();
			List list = conn.openSession().find(hql);
			if (list != null && list.size() > 0)
			{
				resList = new ArrayList();
				ReportInInfo reportInInfo = null;
				for (int i = 0; i < list.size(); i++)
				{
					reportInInfo = (ReportInInfo) list.get(i);
					ReportInInfoForm reportInInfoForm = new ReportInInfoForm();
					TranslatorUtil.copyPersistenceToVo(reportInInfo, reportInInfoForm);
					resList.add(reportInInfoForm);
				}
			}
		}
		catch (HibernateException he)
		{
			resList = null;
			log.printStackTrace(he);
		}
		catch (Exception e)
		{
			resList = null;
			log.printStackTrace(e);
		}
		finally
		{
			if (conn != null)
				conn.closeSession();
		}

		return resList;
	}

	/**
	 * Create a new com.cbrc.smis.form.ReportInInfoForm object and persist (i.e.
	 * insert) it.
	 * 
	 * @param reportInInfoForm
	 *            The object containing the data for the new
	 *            com.cbrc.smis.form.ReportInInfoForm object
	 * @exception Exception
	 *                If the new com.cbrc.smis.form.ReportInInfoForm object
	 *                cannot be created or persisted.
	 */
	public static com.cbrc.smis.form.ReportInInfoForm create(com.cbrc.smis.form.ReportInInfoForm reportInInfoForm) throws Exception
	{
		com.cbrc.smis.hibernate.ReportInInfo reportInInfoPersistence = new com.cbrc.smis.hibernate.ReportInInfo();
		TranslatorUtil.copyVoToPersistence(reportInInfoPersistence, reportInInfoForm);
		javax.naming.InitialContext ctx = new javax.naming.InitialContext();
		// TODO: Make adapter get SessionFactory jndi name by ant task argument?
		net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory) ctx.lookup("java:AirlineHibernateFactory");
		net.sf.hibernate.Session session = factory.openSession();
		net.sf.hibernate.Transaction tx = session.beginTransaction();
		// TODO error?: reportInInfoPersistence =
		// (com.cbrc.smis.hibernate.ReportInInfo)session.save(reportInInfoPersistence);
		session.save(reportInInfoPersistence);
		tx.commit();
		session.close();
		TranslatorUtil.copyPersistenceToVo(reportInInfoPersistence, reportInInfoForm);
		return reportInInfoForm;
	}

	/**
	 * Update (i.e. persist) an existing com.cbrc.smis.form.ReportInInfoForm
	 * object.
	 * 
	 * @param reportInInfoForm
	 *            The com.cbrc.smis.form.ReportInInfoForm object containing the
	 *            data to be updated
	 * @exception Exception
	 *                If the com.cbrc.smis.form.ReportInInfoForm object cannot
	 *                be updated/persisted.
	 */
	public static com.cbrc.smis.form.ReportInInfoForm update(com.cbrc.smis.form.ReportInInfoForm reportInInfoForm) throws Exception
	{
		com.cbrc.smis.hibernate.ReportInInfo reportInInfoPersistence = new com.cbrc.smis.hibernate.ReportInInfo();
		TranslatorUtil.copyVoToPersistence(reportInInfoPersistence, reportInInfoForm);
		javax.naming.InitialContext ctx = new javax.naming.InitialContext();
		// TODO: Make adapter get SessionFactory jndi name by ant task argument?
		net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory) ctx.lookup("java:AirlineHibernateFactory");
		net.sf.hibernate.Session session = factory.openSession();
		net.sf.hibernate.Transaction tx = session.beginTransaction();
		session.update(reportInInfoPersistence);
		tx.commit();
		session.close();
		TranslatorUtil.copyPersistenceToVo(reportInInfoPersistence, reportInInfoForm);
		return reportInInfoForm;
	}

	/**
	 * Retrieve an existing com.cbrc.smis.form.ReportInInfoForm object for
	 * editing.
	 * 
	 * @param reportInInfoForm
	 *            The com.cbrc.smis.form.ReportInInfoForm object containing the
	 *            data used to retrieve the object (i.e. the primary key info).
	 * @exception Exception
	 *                If the com.cbrc.smis.form.ReportInInfoForm object cannot
	 *                be retrieved.
	 */
	public static com.cbrc.smis.form.ReportInInfoForm edit(com.cbrc.smis.form.ReportInInfoForm reportInInfoForm) throws Exception
	{
		com.cbrc.smis.hibernate.ReportInInfo reportInInfoPersistence = new com.cbrc.smis.hibernate.ReportInInfo();
		TranslatorUtil.copyVoToPersistence(reportInInfoPersistence, reportInInfoForm);
		javax.naming.InitialContext ctx = new javax.naming.InitialContext();
		// TODO: Make adapter get SessionFactory jndi name by ant task argument?
		net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory) ctx.lookup("java:AirlineHibernateFactory");
		net.sf.hibernate.Session session = factory.openSession();
		net.sf.hibernate.Transaction tx = session.beginTransaction();
		tx.commit();
		session.close();
		TranslatorUtil.copyPersistenceToVo(reportInInfoPersistence, reportInInfoForm);
		return reportInInfoForm;
	}

	/**
	 * Remove (delete) an existing com.cbrc.smis.form.ReportInInfoForm object.
	 * 
	 * @param reportInInfoForm
	 *            The com.cbrc.smis.form.ReportInInfoForm object containing the
	 *            data to be deleted.
	 * @exception Exception
	 *                If the com.cbrc.smis.form.ReportInInfoForm object cannot
	 *                be removed.
	 */
	public static void remove(com.cbrc.smis.form.ReportInInfoForm reportInInfoForm) throws Exception
	{
		com.cbrc.smis.hibernate.ReportInInfo reportInInfoPersistence = new com.cbrc.smis.hibernate.ReportInInfo();
		TranslatorUtil.copyVoToPersistence(reportInInfoPersistence, reportInInfoForm);
		javax.naming.InitialContext ctx = new javax.naming.InitialContext();
		// TODO: Make adapter get SessionFactory jndi name by ant task argument?
		net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory) ctx.lookup("java:AirlineHibernateFactory");
		net.sf.hibernate.Session session = factory.openSession();
		net.sf.hibernate.Transaction tx = session.beginTransaction();
		// TODO: is this really needed?
		session.delete(reportInInfoPersistence);
		tx.commit();
		session.close();
	}

	/**
	 * Retrieve all existing com.cbrc.smis.form.ReportInInfoForm objects.
	 * 
	 * @exception Exception
	 *                If the com.cbrc.smis.form.ReportInInfoForm objects cannot
	 *                be retrieved.
	 */
	public static List findAll() throws Exception
	{
		List retVals = new ArrayList();
		javax.naming.InitialContext ctx = new javax.naming.InitialContext();
		// TODO: Make adapter get SessionFactory jndi name by ant task argument?
		net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory) ctx.lookup("java:AirlineHibernateFactory");
		net.sf.hibernate.Session session = factory.openSession();
		net.sf.hibernate.Transaction tx = session.beginTransaction();
		retVals.addAll(session.find("from com.cbrc.smis.hibernate.ReportInInfo"));
		tx.commit();
		session.close();
		ArrayList reportInInfo_vos = new ArrayList();
		for (Iterator it = retVals.iterator(); it.hasNext();)
		{
			com.cbrc.smis.form.ReportInInfoForm reportInInfoFormTemp = new com.cbrc.smis.form.ReportInInfoForm();
			com.cbrc.smis.hibernate.ReportInInfo reportInInfoPersistence = (com.cbrc.smis.hibernate.ReportInInfo) it.next();
			TranslatorUtil.copyPersistenceToVo(reportInInfoPersistence, reportInInfoFormTemp);
			reportInInfo_vos.add(reportInInfoFormTemp);
		}
		retVals = reportInInfo_vos;
		return retVals;
	}

	/**
	 * Retrieve a set of existing com.cbrc.smis.form.ReportInInfoForm objects
	 * for editing.
	 * 
	 * @param reportInInfoForm
	 *            The com.cbrc.smis.form.ReportInInfoForm object containing the
	 *            data used to retrieve the objects (i.e. the criteria for the
	 *            retrieval).
	 * @exception Exception
	 *                If the com.cbrc.smis.form.ReportInInfoForm objects cannot
	 *                be retrieved.
	 */
	public static List select(com.cbrc.smis.form.ReportInInfoForm reportInInfoForm) throws Exception
	{
		List retVals = new ArrayList();
		javax.naming.InitialContext ctx = new javax.naming.InitialContext();
		// TODO: Make adapter get SessionFactory jndi name by ant task argument?
		net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory) ctx.lookup("java:AirlineHibernateFactory");
		net.sf.hibernate.Session session = factory.openSession();
		net.sf.hibernate.Transaction tx = session.beginTransaction();
		retVals.addAll(session.find("from com.cbrc.smis.hibernate.ReportInInfo as obj1 where obj1.reportValue='" + reportInInfoForm.getReportValue() + "'"));
		retVals.addAll(session.find("from com.cbrc.smis.hibernate.ReportInInfo as obj1 where obj1.thanPrevRise='" + reportInInfoForm.getThanPrevRise() + "'"));
		retVals.addAll(session.find("from com.cbrc.smis.hibernate.ReportInInfo as obj1 where obj1.thanSameRise='" + reportInInfoForm.getThanSameRise() + "'"));
		retVals.addAll(session.find("from com.cbrc.smis.hibernate.ReportInInfo as obj1 where obj1.thanSameFall='" + reportInInfoForm.getThanSameFall() + "'"));
		retVals.addAll(session.find("from com.cbrc.smis.hibernate.ReportInInfo as obj1 where obj1.thanPrevFall='" + reportInInfoForm.getThanPrevFall() + "'"));
		tx.commit();
		session.close();
		ArrayList reportInInfo_vos = new ArrayList();
		for (Iterator it = retVals.iterator(); it.hasNext();)
		{
			com.cbrc.smis.form.ReportInInfoForm reportInInfoFormTemp = new com.cbrc.smis.form.ReportInInfoForm();
			com.cbrc.smis.hibernate.ReportInInfo reportInInfoPersistence = (com.cbrc.smis.hibernate.ReportInInfo) it.next();
			TranslatorUtil.copyPersistenceToVo(reportInInfoPersistence, reportInInfoFormTemp);
			reportInInfo_vos.add(reportInInfoFormTemp);
		}
		retVals = reportInInfo_vos;
		return retVals;
	}

	/**
	 * This method will return all objects referenced by ReportIn
	 */
	public static List getReportIn(com.cbrc.smis.form.ReportInInfoForm reportInInfoForm) throws Exception
	{
		List retVals = new ArrayList();
		com.cbrc.smis.hibernate.ReportInInfo reportInInfoPersistence = null;
		javax.naming.InitialContext ctx = new javax.naming.InitialContext();
		// TODO: Make adapter get SessionFactory jndi name by ant task argument?
		net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory) ctx.lookup("java:AirlineHibernateFactory");
		net.sf.hibernate.Session session = factory.openSession();
		net.sf.hibernate.Transaction tx = session.beginTransaction();
		tx.commit();
		session.close();
		retVals.add(reportInInfoPersistence.getReportIn());
		ArrayList REP_IN_ID_vos = new ArrayList();
		for (Iterator it = retVals.iterator(); it.hasNext();)
		{
			com.cbrc.smis.form.ReportInForm REP_IN_ID_Temp = new com.cbrc.smis.form.ReportInForm();
			com.cbrc.smis.hibernate.ReportIn REP_IN_ID_PO = (com.cbrc.smis.hibernate.ReportIn) it.next();
			TranslatorUtil.copyPersistenceToVo(REP_IN_ID_PO, REP_IN_ID_Temp);
			REP_IN_ID_vos.add(REP_IN_ID_Temp);
		}
		retVals = REP_IN_ID_vos;
		return retVals;
	}

	/**
	 * This method will return all objects referenced by MCell
	 */
	public static List getMCell(com.cbrc.smis.form.ReportInInfoForm reportInInfoForm) throws Exception
	{
		List retVals = new ArrayList();
		com.cbrc.smis.hibernate.ReportInInfo reportInInfoPersistence = null;
		javax.naming.InitialContext ctx = new javax.naming.InitialContext();
		// TODO: Make adapter get SessionFactory jndi name by ant task argument?
		net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory) ctx.lookup("java:AirlineHibernateFactory");
		net.sf.hibernate.Session session = factory.openSession();
		net.sf.hibernate.Transaction tx = session.beginTransaction();
		tx.commit();
		session.close();
		retVals.add(reportInInfoPersistence.getMCell());
		ArrayList CELL_ID_vos = new ArrayList();
		for (Iterator it = retVals.iterator(); it.hasNext();)
		{
			com.cbrc.smis.form.MCellForm CELL_ID_Temp = new com.cbrc.smis.form.MCellForm();
			com.cbrc.smis.hibernate.MCell CELL_ID_PO = (com.cbrc.smis.hibernate.MCell) it.next();
			TranslatorUtil.copyPersistenceToVo(CELL_ID_PO, CELL_ID_Temp);
			CELL_ID_vos.add(CELL_ID_Temp);
		}
		retVals = CELL_ID_vos;
		return retVals;
	}

	/**
	 * author cb �÷������ڽ���ReportInInfo����һ����¼
	 * 
	 * @param cellId
	 * @param repInId
	 * @param reportValue
	 * @throws Exception
	 */
	public static void insertReportInInfo(Integer cellId, Integer repInId, String reportValue, String zipFileName, String xmlFileName) throws Exception
	{

		DBConn dBConn = null;

		Session session = null;

		ReportInInfo reportInInfo = new ReportInInfo();

		ReportInInfoPK comp_id = new ReportInInfoPK(); // new ������

		comp_id.setCellId(cellId);

		comp_id.setRepInId(repInId);

		reportInInfo.setComp_id(comp_id); // ������SET��ȥ

		reportInInfo.setReportValue(reportValue);
		// // System.out.println("cellId:" + cellId + "\trepInId:" + repInId +
		// "\treportValue:" + reportValue);
		try
		{

			dBConn = new DBConn();

			session = dBConn.beginTransaction();

			session.save(reportInInfo);
			dBConn.endTransaction(true);
			// ������Զ����ļ���
			// Procedure.runZDJS(repInId);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			dBConn.endTransaction(false);

			String errorMessage = "�������ڴ洢" + zipFileName + "���ļ��е�" + xmlFileName;

			errorMessage = errorMessage + "�ļ����" + "<" + cellId.intValue() + repInId.intValue() + ">" + "��Ԫ��ʱ";

			FitechLog.writeLog(Config.LOG_SYSTEM_SAVEDATA, ConfigOncb.HANDLER, errorMessage, "OVER");
		}
		finally
		{

			if (session != null)

				dBConn.closeSession();
		}

	}

	/**
	 * ���ݱ���id��ѯ���ñ�������м�¼
	 * 
	 * @author Ҧ��
	 * @param reportInId
	 *            Integer ʵ�ʱ���id
	 * @return List ��ѯ�������м�¼
	 */
	public static List getReportData(Integer reportInId)
	{
		List result = new ArrayList();
		DBConn conn = null;
		Session session = null;

		if (reportInId != null)
		{
			try
			{
				conn = new DBConn();
				session = conn.openSession();
				StringBuffer hql = new StringBuffer("from ReportInInfo repInfo where repInfo.comp_id.repInId=" + reportInId
						+ " order by repInfo.MCell.rowId,repInfo.MCell.colId");
				List cellDatas = session.createQuery(hql.toString()).list();
				if (cellDatas != null && cellDatas.size() != 0)
				{
					for (Iterator it = cellDatas.iterator(); it.hasNext();)
					{
						ReportInInfo reportInfo = (ReportInInfo) it.next();
						String cellRow = "";
						String cellCol = "";
						String cellName = "";
						if (reportInfo.getMCell() != null)
						{
							cellRow = reportInfo.getMCell().getRowId().toString();
							cellCol = reportInfo.getMCell().getColId().toString();
							cellName = reportInfo.getMCell().getCellName();
						}
						String value = reportInfo.getReportValue();
						P2P_Report_Data data = new P2P_Report_Data(cellRow, cellCol, value, cellName);
						result.add(data);
					}
				}
			}
			catch (Exception e)
			{
				result = null;
				log.printStackTrace(e);
			}
			finally
			{
				if (conn != null)
					conn.closeSession();
			}
		}
		return result;
	}

	/**
	 * ����鿴�Ĳ����ܼ�¼��
	 * 
	 * @author ����
	 */
	public static int getRecordCountOfmanual(ReportInInfoForm reportInInfoForm, Operator operator)
	{

		int count = 0;

		// ���Ӷ���ͻỰ�����ʼ��
		DBConn conn = null;
		Session session = null;

		try
		{

			conn = new DBConn();
			session = conn.openSession();

			String childRepIDStr = "";

			String sql = "select mrr.MChildReport.comp_id.childRepId from MRepRange mrr where mrr.comp_id.orgId='" + operator.getOrgId() + "'";

			/** ���ϱ����Ƿ��Ѿ����� */

			sql += " and  mrr.MChildReport.isPublic = 1";

			List reportInList = session.find(sql);
			if (reportInList != null && reportInList.size() > 0)
			{

				for (int j = 0; j < reportInList.size(); j++)
				{
					childRepIDStr += "'" + reportInList.get(j) + "',";
				}

			}

			// ��ѯ����HQL������
			StringBuffer hql = new StringBuffer(
					"select count(*) from ReportIn ri where ri.checkFlag!=4 and "
							+ "ri.times=1");
			StringBuffer where = new StringBuffer("");
			where.append(" and ri.checkFlag not in (" + com.fitech.net.config.Config.CHECK_FLAG_UNREPORT + ","
					+ com.fitech.net.config.Config.CHECK_FLAG_AFTERJY + ")");
			if (reportInInfoForm == null)
				return count;

			/** �����������ж� */
			/*if (reportInInfoForm.getOrgName() != null && !reportInInfoForm.getOrgName().equals(""))
			{
				String orgIds = StrutsOrgNetDelegate.selectOrgIdToString(reportInInfoForm.getOrgName());
				if (orgIds != null && !orgIds.equals(""))
					where.append(" and ri.orgId in (" + orgIds + ")");
			}*/
             ////////////////////////////////////////
            //2008-02-19 ����
            String orgId = reportInInfoForm.getOrgId();
            if(orgId != null && !"".equals(orgId))
                where.append(" and ri.orgId ='" + orgId +"'");
            ////////////////////////////////////////
			if (reportInInfoForm.getYear() != null && reportInInfoForm.getYear().intValue() > 0)
			{
				where.append(" and ri.year=" + reportInInfoForm.getYear());
			}
			if (reportInInfoForm.getTerm() != null && reportInInfoForm.getTerm().intValue() > 0)
			{
				where.append(" and ri.term=" + reportInInfoForm.getTerm());
			}
			/** ��ʼʱ��������ж� */
			if (reportInInfoForm.getStartDate() != null && !reportInInfoForm.getStartDate().equals(""))
			{
				where.append(" and ri.reportDate>='" + reportInInfoForm.getStartDate() + "'");
			}
			/** ����ʱ����ж� */
			if (reportInInfoForm.getEndDate() != null && !reportInInfoForm.getEndDate().equals(""))
			{
				where.append(" and ri.reportDate<='" + reportInInfoForm.getEndDate() + "'");
			}

			/** ��־��Χ�������ж� */
			if (reportInInfoForm.getAllFlags() != null && !reportInInfoForm.getAllFlags().equals(""))
			{
				switch ((Integer.valueOf(reportInInfoForm.getAllFlags())).intValue())
				{
					case -1: // ���δͨ��
						where.append(" and ri.checkFlag=" + Config.CHECK_FLAG_NO);
						break;
					case 0: // δ���
						where.append(" and ri.checkFlag=" + Config.CHECK_FLAG_UN);
						break;
					case 1: // ���ͨ��
						where.append(" and ri.checkFlag=" + Config.CHECK_FLAG_OK);
						break;
					case 4: // �쳣��־�������ж�
						where.append(" and ri.abmormityChangeFlag=" + Config.ABMORMITY_FLAG_NO);
						break;
				}
			}

			/** �������Ƶ��ж� */
			if (reportInInfoForm.getRepName() != null && !reportInInfoForm.getRepName().equals(""))
			{
				where.append(" and ri.repName like '%" + reportInInfoForm.getRepName() + "%'");
			}

			/** ���ϱ���鿴Ȩ�� */
			if (operator == null)
				return count;
			if (operator.isSuperManager() == false)
			{
				if (operator.getChildRepSearchPopedom() == null || operator.getChildRepSearchPopedom().equals(""))
					return count;
				/**���������ݿ��ж�*/
				if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
					where.append(" and ltrim(rtrim(ri.orgId)) || ri.MChildReport.comp_id.childRepId in(" + operator.getChildRepSearchPopedom() + ")");
				if(Config.DB_SERVER_TYPE.equals("sqlserver"))
					where.append(" and ltrim(rtrim(ri.orgId)) + ri.MChildReport.comp_id.childRepId in(" + operator.getChildRepSearchPopedom() + ")");
			}

		where.append(" and ri.MChildReport.comp_id.childRepId in(" + childRepIDStr.trim().substring(0, childRepIDStr.trim().length() - 1) + ")");

			hql.append(where.toString());

			List list = session.find(hql.toString());
			if (list != null && list.size() != 0)
			{
				count = ((Integer) list.get(0)).intValue();
			}

		}
		catch (HibernateException he)
		{
			log.printStackTrace(he);
		}
		catch (Exception e)
		{
			log.printStackTrace(e);
		}
		finally
		{
			// ������Ӵ��ڣ���Ͽ��������Ự������
			if (conn != null)
				conn.closeSession();
		}
		return count;
	}
	/**
	 * ����鿴�Ĳ����ܼ�¼��
	 * 
	 * @author wh
	 */
	public static int getAnalyRecordCountOfmanual(ReportInInfoForm reportInInfoForm, Operator operator)
	{

		int count = 0;

		// ���Ӷ���ͻỰ�����ʼ��
		DBConn conn = null;
		Session session = null;

		try
		{

			conn = new DBConn();
			session = conn.openSession();

			String childRepIDStr = "";

			String sql = "select mrr.MChildReport.comp_id.childRepId from MRepRange mrr where mrr.comp_id.orgId='" + operator.getOrgId() + "'";

			/** ���ϱ����Ƿ��Ѿ����� */

			sql += " and  mrr.MChildReport.isPublic = 1";

			List reportInList = session.find(sql);
			if (reportInList != null && reportInList.size() > 0)
			{

				for (int j = 0; j < reportInList.size(); j++)
				{
					childRepIDStr += "'" + reportInList.get(j) + "',";
				}

			}

			// ��ѯ����HQL������
			StringBuffer hql = new StringBuffer(
					"select count(*) from ReportIn ri where ri.MChildReport.comp_id.versionId='0690' and ri.orgId='" + operator.getOrgId() + "'  and " 
							+ "ri.times=(select max(r.times) from ReportIn r where r.MChildReport.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and "
							+ "r.MChildReport.comp_id.versionId=ri.MChildReport.comp_id.versionId and r.MDataRgType.dataRangeId=ri.MDataRgType.dataRangeId and r.orgId=ri.orgId and "
							+ "r.MCurr.curId=ri.MCurr.curId and r.year=ri.year and r.term=ri.term and r.checkFlag not in ("
							+ com.fitech.net.config.Config.CHECK_FLAG_AFTERJY + "," + com.fitech.net.config.Config.CHECK_FLAG_UNREPORT + ","
							+ com.fitech.net.config.Config.CHECK_FLAG_AFTERSAVE + "))");

			StringBuffer where = new StringBuffer("");
			where.append(" and ri.checkFlag not in (" + com.fitech.net.config.Config.CHECK_FLAG_UNREPORT + ","
					+ com.fitech.net.config.Config.CHECK_FLAG_AFTERJY + ")");
			if (reportInInfoForm == null)
				return count;

			/** �����������ж� */
			if (reportInInfoForm.getYear() != null && reportInInfoForm.getYear().intValue() > 0)
			{
				where.append(" and ri.year=" + reportInInfoForm.getYear());
			}
			if (reportInInfoForm.getTerm() != null && reportInInfoForm.getTerm().intValue() > 0)
			{
				where.append(" and ri.term=" + reportInInfoForm.getTerm());
			}
			/** ��ʼʱ��������ж� */
			if (reportInInfoForm.getStartDate() != null && !reportInInfoForm.getStartDate().equals(""))
			{
				where.append(" and ri.reportDate>='" + reportInInfoForm.getStartDate() + "'");
			}
			/** ����ʱ����ж� */
			if (reportInInfoForm.getEndDate() != null && !reportInInfoForm.getEndDate().equals(""))
			{
				where.append(" and ri.reportDate<='" + reportInInfoForm.getEndDate() + "'");
			}

			/** ��־��Χ�������ж� */
			if (reportInInfoForm.getAllFlags() != null && !reportInInfoForm.getAllFlags().equals(""))
			{
				where.append(" and ri.checkFlag=" + Config.CHECK_FLAG_OK);
			}

			/** �������Ƶ��ж� */
			if (reportInInfoForm.getRepName() != null && !reportInInfoForm.getRepName().equals(""))
			{
				where.append(" and ri.repName like '%" + reportInInfoForm.getRepName() + "%'");
			}

			/** ���ϱ���鿴Ȩ�� */
			if (operator == null)
				return count;
			if (operator.isSuperManager() == false)
			{
				if (operator.getChildRepSearchPopedom() == null || operator.getChildRepSearchPopedom().equals(""))
					return count;
				if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
					where.append(" and ltrim(rtrim(ri.orgId)) || ri.MChildReport.comp_id.childRepId in(" + operator.getChildRepSearchPopedom() + ")");
				if(Config.DB_SERVER_TYPE.equals("sqlserver"))
					where.append(" and ltrim(rtrim(ri.orgId)) + ri.MChildReport.comp_id.childRepId in(" + operator.getChildRepSearchPopedom() + ")");
			}

		where.append(" and ri.MChildReport.comp_id.childRepId in(" + childRepIDStr.trim().substring(0, childRepIDStr.trim().length() - 1) + ")");

			hql.append(where.toString());

			List list = session.find(hql.toString());
			if (list != null && list.size() != 0)
			{
				count = ((Integer) list.get(0)).intValue();
			}

		}
		catch (HibernateException he)
		{
			log.printStackTrace(he);
		}
		catch (Exception e)
		{
			log.printStackTrace(e);
		}
		finally
		{
			// ������Ӵ��ڣ���Ͽ��������Ự������
			if (conn != null)
				conn.closeSession();
		}
		return count;
	}
	/**
	 * ����鿴�Ĳ����ܼ�¼��
	 * 
	 * @author ���� wh��
	 */
	public static int getRecordCountOfmanualSHCK(ReportInInfoForm reportInInfoForm, Operator operator)
	{

		int count = 0;

		// ���Ӷ���ͻỰ�����ʼ��
		DBConn conn = null;
		Session session = null;

		try
		{

			conn = new DBConn();
			session = conn.openSession();

			String childRepIDStr = "";

			String sql = "select mrr.MChildReport.comp_id.childRepId from MRepRange mrr where mrr.comp_id.orgId='" + operator.getOrgId() + "'";

			/** ���ϱ����Ƿ��Ѿ����� */

			sql += " and  mrr.MChildReport.isPublic = 1";

			List reportInList = session.find(sql);
			if (reportInList != null && reportInList.size() > 0)
			{

				for (int j = 0; j < reportInList.size(); j++)
				{
					childRepIDStr += "'" + reportInList.get(j) + "',";
				}

			}

			// ��ѯ����HQL������
			StringBuffer hql = new StringBuffer(
					"select count(*) from ReportIn ri where "
							+ "ri.times=(select max(r.times) from ReportIn r where r.MChildReport.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and "
							+ "r.MChildReport.comp_id.versionId=ri.MChildReport.comp_id.versionId and r.MDataRgType.dataRangeId=ri.MDataRgType.dataRangeId and r.orgId=ri.orgId and "
							+ "r.MCurr.curId=ri.MCurr.curId and r.year=ri.year and r.term=ri.term and r.checkFlag not in ("
							+ com.fitech.net.config.Config.CHECK_FLAG_AFTERJY + "," + com.fitech.net.config.Config.CHECK_FLAG_UNREPORT + ","
							+ com.fitech.net.config.Config.CHECK_FLAG_AFTERSAVE + "))");

			StringBuffer where = new StringBuffer("");
			where.append(" and ri.checkFlag not in (" + com.fitech.net.config.Config.CHECK_FLAG_UNREPORT + ","
					+ com.fitech.net.config.Config.CHECK_FLAG_AFTERJY + ")");
			if (reportInInfoForm == null)
				return count;

			/** �����������ж� */
			if (reportInInfoForm.getOrgName() != null && !reportInInfoForm.getOrgName().equals(""))
			{
				String orgIds = StrutsOrgNetDelegate.selectOrgIdToString(reportInInfoForm.getOrgName());
				if (orgIds != null && !orgIds.equals(""))
					where.append(" and ri.orgId in (" + orgIds + ")");
			}
			if (reportInInfoForm.getYear() != null && reportInInfoForm.getYear().intValue() > 0)
			{
				where.append(" and ri.year=" + reportInInfoForm.getYear());
			}
			if (reportInInfoForm.getTerm() != null && reportInInfoForm.getTerm().intValue() > 0)
			{
				where.append(" and ri.term=" + reportInInfoForm.getTerm());
			}
			/** ��ʼʱ��������ж� */
			if (reportInInfoForm.getStartDate() != null && !reportInInfoForm.getStartDate().equals(""))
			{
				where.append(" and ri.reportDate>='" + reportInInfoForm.getStartDate() + "'");
			}
			/** ����ʱ����ж� */
			if (reportInInfoForm.getEndDate() != null && !reportInInfoForm.getEndDate().equals(""))
			{
				where.append(" and ri.reportDate<='" + reportInInfoForm.getEndDate() + "'");
			}

			/** ��־��Χ�������ж� */
			if (reportInInfoForm.getAllFlags() != null && !reportInInfoForm.getAllFlags().equals(""))
			{
				switch ((Integer.valueOf(reportInInfoForm.getAllFlags())).intValue())
				{
					case -1: // ���δͨ��
						where.append(" and ri.checkFlag=" + Config.CHECK_FLAG_NO);
						break;
					case 0: // δ���
						where.append(" and ri.checkFlag=" + Config.CHECK_FLAG_UN);
						break;
					case 1: // ���ͨ��
						where.append(" and ri.checkFlag=" + Config.CHECK_FLAG_OK);
						break;
					case 4: // �쳣��־�������ж�
						where.append(" and ri.abmormityChangeFlag=" + Config.ABMORMITY_FLAG_NO);
						break;
				}
			}

			/** �������Ƶ��ж� */
			if (reportInInfoForm.getRepName() != null && !reportInInfoForm.getRepName().equals(""))
			{
				where.append(" and ri.repName like '%" + reportInInfoForm.getRepName() + "%'");
			}

			/** ���ϱ���鿴Ȩ�� */
			if (operator == null)
				return count;
			if (operator.isSuperManager() == false)
			{
				if (operator.getChildRepSearchPopedom() == null || operator.getChildRepSearchPopedom().equals(""))
					return count;
				/**���������ݿ��ж�*/
				if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
					where.append(" and ltrim(rtrim(ri.orgId)) || ri.MChildReport.comp_id.childRepId in(" + operator.getChildRepSearchPopedom() + ")");
				if(Config.DB_SERVER_TYPE.equals("sqlserver"))
					where.append(" and ltrim(rtrim(ri.orgId)) + ri.MChildReport.comp_id.childRepId in(" + operator.getChildRepSearchPopedom() + ")");
			}

	//		where.append(" and ri.MChildReport.comp_id.childRepId in(" + childRepIDStr.trim().substring(0, childRepIDStr.trim().length() - 1) + ")");

			hql.append(where.toString());

			List list = session.find(hql.toString());
			if (list != null && list.size() != 0)
			{
				count = ((Integer) list.get(0)).intValue();
			}

		}
		catch (HibernateException he)
		{
			log.printStackTrace(he);
		}
		catch (Exception e)
		{
			log.printStackTrace(e);
		}
		finally
		{
			// ������Ӵ��ڣ���Ͽ��������Ự������
			if (conn != null)
				conn.closeSession();
		}
		return count;
	}

	/**
	 * ����鿴�Ĳ��Ҽ�¼����
	 * 
	 * @author ����
	 * @param reportInInfoForm
	 * @param offset
	 * @param limit
	 * @param operator
	 * @return
	 * @throws Exception
	 */
	public static List selectOfManual(ReportInInfoForm reportInInfoForm, int offset, int limit, Operator operator) throws Exception
	{
		List retvals = null;
		DBConn conn = null;
		Session session = null;

		try
		{

			conn = new DBConn();
			session = conn.openSession();

			String childRepIDStr = "";

			String sql = "select mrr.MChildReport.comp_id.childRepId from MRepRange mrr where mrr.comp_id.orgId='" + operator.getOrgId() + "'";

			/** ���ϱ����Ƿ��Ѿ����� */

			sql += " and  mrr.MChildReport.isPublic = 1";

			List reportInList = session.find(sql);
			if (reportInList != null && reportInList.size() > 0)
			{

				for (int j = 0; j < reportInList.size(); j++)
				{
					childRepIDStr += "'" + reportInList.get(j) + "',";
				}

			}

			StringBuffer hql = new StringBuffer(
					"from ReportIn ri where ri.checkFlag!=4 and "
							+ "ri.times=1");
			StringBuffer where = new StringBuffer("");
			where.append(" and ri.checkFlag not in (" + com.fitech.net.config.Config.CHECK_FLAG_UNREPORT + ","
					+ com.fitech.net.config.Config.CHECK_FLAG_AFTERJY + ")");
			if (reportInInfoForm == null)
				return retvals;

			/** �����������ж� */
			/*if (reportInInfoForm.getOrgName() != null && !reportInInfoForm.getOrgName().equals(""))
			{
				String orgIds = StrutsOrgNetDelegate.selectOrgIdToString(reportInInfoForm.getOrgName());
				if (orgIds != null && !orgIds.equals(""))
					where.append(" and ri.orgId in (" + orgIds + ")");
			}*/
            
            ////////////////////////////////////////
            //2008-02-19 ����
            String orgId = reportInInfoForm.getOrgId();
            if(orgId != null && !"".equals(orgId))
                where.append(" and ri.orgId ='" + orgId +"'");
            ////////////////////////////////////////
			/** �������������ж� */
			if (reportInInfoForm.getYear() != null && reportInInfoForm.getYear().intValue() > 0)
			{
				where.append(" and ri.year=" + reportInInfoForm.getYear());
			}
			if (reportInInfoForm.getTerm() != null && reportInInfoForm.getTerm().intValue() > 0)
			{
				where.append(" and ri.term=" + reportInInfoForm.getTerm());
			}
			/** ��ʼʱ��������ж� */
			if (reportInInfoForm.getStartDate() != null && !reportInInfoForm.getStartDate().equals(""))
			{
				where.append(" and ri.reportDate>='" + reportInInfoForm.getStartDate() + "'");
			}
			/** ����ʱ����ж� */
			if (reportInInfoForm.getEndDate() != null && !reportInInfoForm.getEndDate().equals(""))
			{
				where.append(" and ri.reportDate<='" + reportInInfoForm.getEndDate() + "'");
			}
			/** ��־��Χ�������ж� */
			if (reportInInfoForm.getAllFlags() != null && !reportInInfoForm.getAllFlags().equals(""))
			{
				switch ((Integer.valueOf(reportInInfoForm.getAllFlags())).intValue())
				{
					case -1: // ���δͨ��
						where.append(" and ri.checkFlag=" + Config.CHECK_FLAG_NO);
						break;
					case 0: // δ���
						where.append(" and ri.checkFlag=" + Config.CHECK_FLAG_UN);
						break;
					case 1: // ���ͨ��
						where.append(" and ri.checkFlag=" + Config.CHECK_FLAG_OK);
						break;

					case 4: // �쳣��־�������ж�
						where.append(" and ri.abmormityChangeFlag=" + Config.ABMORMITY_FLAG_NO);
						break;
				}
			}
			/** �������Ƶ��ж� */
			if (reportInInfoForm.getRepName() != null && !reportInInfoForm.getRepName().equals(""))
			{
				where.append(" and ri.repName like '%" + reportInInfoForm.getRepName() + "%'");
			}

			/** ���ϱ���鿴Ȩ�� */
			if (operator == null)
				return retvals;
			if (operator.isSuperManager() == false)
			{
				if (operator.getChildRepSearchPopedom() == null || operator.getChildRepSearchPopedom().equals(""))
					return retvals;
				/**���������ݿ��ж�*/
				if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
					where.append(" and ltrim(rtrim(ri.orgId)) || ri.MChildReport.comp_id.childRepId in(" + operator.getChildRepSearchPopedom() + ")");
				if(Config.DB_SERVER_TYPE.equals("sqlserver"))
					where.append(" and ltrim(rtrim(ri.orgId)) + ri.MChildReport.comp_id.childRepId in(" + operator.getChildRepSearchPopedom() + ")");
			}
			where.append(" and ri.MChildReport.comp_id.childRepId in(" + childRepIDStr.trim().substring(0, childRepIDStr.trim().length() - 1) + ")");
			hql.append(where.toString() + " order by ri.year,ri.term,ri.orgId");
			
			// ��Ӽ�����Session

			Query query = session.createQuery(hql.toString());
			query.setFirstResult(offset).setMaxResults(limit);
			List list = query.list();

			if (list != null)
			{
				retvals = new ArrayList();
				// ѭ����ȡ���ݿ����������¼
				for (Iterator it = list.iterator(); it.hasNext();)
				{
					// ʵ����һ���м���Aditing��������ҳ��������ֶ���
					Aditing aditing = new Aditing();
					// ���ɳ־û�����
					ReportIn reportInPersistence = (ReportIn) it.next();
					// �����ҵ�����˱�־set��aditing����
					aditing.setCheckFlag(reportInPersistence.getCheckFlag());
					// �жϴ�MOrg���в�ѯ�ļ�¼�Ƿ�Ϊ��
					if ((StrutsMOrgDelegate.selectOne(reportInPersistence.getOrgId())) != null
							&& !(StrutsMOrgDelegate.selectOne(reportInPersistence.getOrgId())).equals(""))
					{
						// �����Ϊ��,�򽫴�MOrg�в��ҵ��Ļ�����set��aditing����
						aditing.setOrgName((StrutsMOrgDelegate.selectOne(reportInPersistence.getOrgId())).getOrg_name());
					}
					// �����ҵ��ı���id����aditing����
					aditing.setRepInId(reportInPersistence.getRepInId());
					aditing.setYear(reportInPersistence.getYear());
					aditing.setTerm(reportInPersistence.getTerm());
					// �����ҵı�������set��adtiting����
					// if
					// (reportInPersistence.getRepName().equals(reportInPersistence.getMChildReport().getReportName()))
					aditing.setRepName(reportInPersistence.getRepName());
					/*
					 * else aditing.setRepName(reportInPersistence.getRepName() +
					 * reportInPersistence.getMChildReport().getReportName());
					 */
					// �����ҵı�������set��aditing����
					aditing.setReportDate(reportInPersistence.getReportDate());
					// �����ҵ��ӱ���ID�Ž�aditing����
					aditing.setChildRepId(reportInPersistence.getMChildReport().getComp_id().getChildRepId());
					// �����ҵİ汾��set��aditing����
					aditing.setVersionId(reportInPersistence.getMChildReport().getComp_id().getVersionId());
					aditing.setCurrName(reportInPersistence.getMCurr().getCurName());
					MActuRep mActuRep = StrutsReportInDelegate.GetFreR(reportInPersistence);
					if (mActuRep != null)
					{
						// ���Ϳھ�
						aditing.setDataRgTypeName(mActuRep.getMDataRgType().getDataRgDesc());
						// ����Ƶ��
						aditing.setActuFreqName(mActuRep.getMRepFreq().getRepFreqName());
					}
					retvals.add(aditing);
				}
			}
		}
		catch (HibernateException he)
		{
			retvals = null;
			log.printStackTrace(he);
		}
		catch (Exception e)
		{
			retvals = null;
			log.printStackTrace(e);
		}
		finally
		{
			// ������Ӵ��ڣ���Ͽ��������Ự������
			if (conn != null)
				conn.closeSession();
		}
		return retvals;
	}

	/**
	 * ����鿴�Ĳ��Ҽ�¼����
	 * 
	 * @param reportInInfoForm
	 * @param operator
	 * @return
	 * @throws Exception
	 */
	public static List selectOfManuals(ReportInInfoForm reportInInfoForm, Operator operator) throws Exception{
		List retvals = null;
		DBConn conn = null;
		Session session = null;
		String childRepSearchPopedom = null;
		
		try{
			conn = new DBConn();
			session = conn.openSession();			
			StringBuffer hql = new StringBuffer(
					"from ReportIn ri where "
							+ "ri.times=1");
			StringBuffer where = new StringBuffer("");
			where.append(" and ri.checkFlag not in (" + com.fitech.net.config.Config.CHECK_FLAG_UNREPORT + ","
					+ com.fitech.net.config.Config.CHECK_FLAG_AFTERJY + ")");
			if (reportInInfoForm == null)
				return retvals;

			/** �����������ж� */
			if (reportInInfoForm.getOrgName() != null && !reportInInfoForm.getOrgName().equals("")){
				String orgIds = StrutsOrgNetDelegate.selectOrgIdToString(reportInInfoForm.getOrgName());
				if (orgIds != null && !orgIds.equals(""))
					where.append(" and ri.orgId in (" + orgIds + ")");
			}
			/** �������������ж� */
			if (reportInInfoForm.getYear() != null && reportInInfoForm.getYear().intValue() > 0){
				where.append(" and ri.year=" + reportInInfoForm.getYear());
			}
			if (reportInInfoForm.getTerm() != null && reportInInfoForm.getTerm().intValue() > 0){
				where.append(" and ri.term=" + reportInInfoForm.getTerm());
			}
			/** ��ʼʱ��������ж� */
			if (reportInInfoForm.getStartDate() != null && !reportInInfoForm.getStartDate().equals("")){
				where.append(" and ri.reportDate>='" + reportInInfoForm.getStartDate() + "'");
			}
			/** ����ʱ����ж� */
			if (reportInInfoForm.getEndDate() != null && !reportInInfoForm.getEndDate().equals("")){
				where.append(" and ri.reportDate<='" + reportInInfoForm.getEndDate() + "'");
			}
			/** ��־��Χ�������ж� */
			if (reportInInfoForm.getAllFlags() != null && !reportInInfoForm.getAllFlags().equals("")){
				switch ((Integer.valueOf(reportInInfoForm.getAllFlags())).intValue()){
					case -1: // ���δͨ��
						where.append(" and ri.checkFlag=" + Config.CHECK_FLAG_NO);
						break;
					case 0: // δ���
						where.append(" and ri.checkFlag=" + Config.CHECK_FLAG_UN);
						break;
					case 1: // ���ͨ��
						where.append(" and ri.checkFlag=" + Config.CHECK_FLAG_OK);
						break;

					case 4: // �쳣��־�������ж�
						where.append(" and ri.abmormityChangeFlag=" + Config.ABMORMITY_FLAG_NO);
						break;
				}
			}
			/** �������Ƶ��ж� */
			if (reportInInfoForm.getRepName() != null && !reportInInfoForm.getRepName().equals("")){
				where.append(" and ri.repName like '%" + reportInInfoForm.getRepName() + "%'");
			}

			/** ���ϱ���鿴Ȩ�� */
			if (operator == null)
				return retvals;
			childRepSearchPopedom = operator.getChildRepSearchPopedom();
			if (operator.isSuperManager() == false){
				if (childRepSearchPopedom == null || childRepSearchPopedom.equals(""))
					return retvals;
			}
			hql.append(where.toString() + " order by ri.year,ri.term,ri.orgId");
			

			Query query = session.createQuery(hql.toString());
			List list = query.list();

			if (list != null){
				retvals = new ArrayList();				
				if(operator.isSuperManager() == false){
					String childRepId = null;
					for (Iterator it = list.iterator(); it.hasNext();){						
						ReportIn reportInPersistence = (ReportIn) it.next();
						childRepId = reportInPersistence.getMChildReport().getComp_id().getChildRepId();
						if(childRepSearchPopedom.indexOf(reportInPersistence.getOrgId()+childRepId) <= -1)
							continue;
						
						Aditing aditing = new Aditing();
						aditing.setCheckFlag(reportInPersistence.getCheckFlag());
						if ((StrutsMOrgDelegate.selectOne(reportInPersistence.getOrgId())) != null
								&& !(StrutsMOrgDelegate.selectOne(reportInPersistence.getOrgId())).equals(""))
							aditing.setOrgName((StrutsMOrgDelegate.selectOne(reportInPersistence.getOrgId())).getOrg_name());
						
						aditing.setRepInId(reportInPersistence.getRepInId());
						aditing.setYear(reportInPersistence.getYear());
						aditing.setTerm(reportInPersistence.getTerm());
						aditing.setRepName(reportInPersistence.getRepName());						
						aditing.setReportDate(reportInPersistence.getReportDate());
						aditing.setChildRepId(childRepId);
						aditing.setVersionId(reportInPersistence.getMChildReport().getComp_id().getVersionId());
						aditing.setCurrName(reportInPersistence.getMCurr().getCurName());
						MActuRep mActuRep = StrutsReportInDelegate.GetFreR(reportInPersistence);
						if (mActuRep != null){
							aditing.setDataRgTypeName(mActuRep.getMDataRgType().getDataRgDesc());
							aditing.setActuFreqName(mActuRep.getMRepFreq().getRepFreqName());
						}
						retvals.add(aditing);
					}
				}else{
					for (Iterator it = list.iterator(); it.hasNext();){
						Aditing aditing = new Aditing();
						ReportIn reportInPersistence = (ReportIn) it.next();
						aditing.setCheckFlag(reportInPersistence.getCheckFlag());
						if ((StrutsMOrgDelegate.selectOne(reportInPersistence.getOrgId())) != null
								&& !(StrutsMOrgDelegate.selectOne(reportInPersistence.getOrgId())).equals(""))
							aditing.setOrgName((StrutsMOrgDelegate.selectOne(reportInPersistence.getOrgId())).getOrg_name());
						
						aditing.setRepInId(reportInPersistence.getRepInId());
						aditing.setYear(reportInPersistence.getYear());
						aditing.setTerm(reportInPersistence.getTerm());
						aditing.setRepName(reportInPersistence.getRepName());						
						aditing.setReportDate(reportInPersistence.getReportDate());
						aditing.setChildRepId(reportInPersistence.getMChildReport().getComp_id().getChildRepId());
						aditing.setVersionId(reportInPersistence.getMChildReport().getComp_id().getVersionId());
						aditing.setCurrName(reportInPersistence.getMCurr().getCurName());
						MActuRep mActuRep = StrutsReportInDelegate.GetFreR(reportInPersistence);
						if (mActuRep != null){
							aditing.setDataRgTypeName(mActuRep.getMDataRgType().getDataRgDesc());
							aditing.setActuFreqName(mActuRep.getMRepFreq().getRepFreqName());
						}
						retvals.add(aditing);
					}
				}				
			}
		}
		catch (HibernateException he)
		{
			retvals = null;
			log.printStackTrace(he);
		}
		catch (Exception e)
		{
			retvals = null;
			log.printStackTrace(e);
		}
		finally
		{
			// ������Ӵ��ڣ���Ͽ��������Ự������
			if (conn != null)
				conn.closeSession();
		}
		return retvals;
	}
	
	/**
	 * ����鿴�Ĳ��Ҽ�¼����
	 * 
	 * @author ����
	 * @param reportInInfoForm
	 * @param offset
	 * @param limit
	 * @param operator
	 * @return
	 * @throws Exception
	 */
	public static List selectAnalyOfManual(ReportInInfoForm reportInInfoForm, Operator operator) throws Exception
	{
		List retvals = null;
		DBConn conn = null;
		Session session = null;
		String childRepStr = "";
		try
		{

			conn = new DBConn();
			session = conn.openSession();
			
			
			String seql = "select distinct ab.comp_id.childRepId   from com.cbrc.smis.hibernate.AbnormityChange ab";
			List abList = session.find(seql);
			if (abList != null && abList.size() > 0)
			{

				for (int j = 0; j < abList.size(); j++)
				{
					childRepStr += "'" + abList.get(j) + "',";
				}

			}
			
			

			String childRepIDStr = "";

			String sql = "select mrr.MChildReport.comp_id.childRepId from MRepRange mrr where mrr.comp_id.orgId='" + operator.getOrgId() + "'" +
					"  and  mrr.MChildReport.comp_id.childRepId in(" + childRepStr.trim().substring(0, childRepStr.trim().length() - 1) + ")";
		
			/** ���ϱ����Ƿ��Ѿ����� */

			sql += " and  mrr.MChildReport.isPublic = 1";

			List reportInList = session.find(sql);
			if (reportInList != null && reportInList.size() > 0)
			{

				for (int j = 0; j < reportInList.size(); j++)
				{
					childRepIDStr += "'" + reportInList.get(j) + "',";
				}

			}
			java.util.Calendar calendar = java.util.Calendar.getInstance();		

			if(reportInInfoForm.getYear() ==null ) 		
				reportInInfoForm.setYear(new Integer(calendar.get(java.util.Calendar.YEAR)));		   
			if(reportInInfoForm.getTerm()==null )		
				reportInInfoForm.setTerm(new Integer(calendar.get(java.util.Calendar.MONTH)+1));
			
			
			reportInInfoForm.setVersionId( StrutsMRepRangeDelegate.getFRVersionId(reportInInfoForm.getYear().toString(),reportInInfoForm.getTerm().toString(),operator.getOrgId()));

			StringBuffer hql = new StringBuffer(
					"from ReportIn ri where ri.MChildReport.comp_id.versionId='"+reportInInfoForm.getVersionId()+"'   and ri.orgId='" + operator.getOrgId() + "'  and "
							+ "ri.times>0 ");

			StringBuffer where = new StringBuffer("");
			where.append(" and ri.checkFlag not in (" + com.fitech.net.config.Config.CHECK_FLAG_UNREPORT + ","
					+ com.fitech.net.config.Config.CHECK_FLAG_AFTERJY + ")");
			if (reportInInfoForm == null)
				return retvals;

			/** �����������ж� */
			if (reportInInfoForm.getOrgName() != null && !reportInInfoForm.getOrgName().equals(""))
			{
				String orgIds = StrutsOrgNetDelegate.selectOrgIdToString(reportInInfoForm.getOrgName());
				if (orgIds != null && !orgIds.equals(""))
					where.append(" and ri.orgId in (" + orgIds + ")");
			}
			/** �������������ж� */
			if (reportInInfoForm.getYear() != null && reportInInfoForm.getYear().intValue() > 0)
			{
				where.append(" and ri.year=" + reportInInfoForm.getYear());
			}
			if (reportInInfoForm.getTerm() != null && reportInInfoForm.getTerm().intValue() > 0)
			{
				where.append(" and ri.term=" + reportInInfoForm.getTerm());
			}
			/** ��ʼʱ��������ж� */
			if (reportInInfoForm.getStartDate() != null && !reportInInfoForm.getStartDate().equals(""))
			{
				where.append(" and ri.reportDate>='" + reportInInfoForm.getStartDate() + "'");
			}
			/** ����ʱ����ж� */
			if (reportInInfoForm.getEndDate() != null && !reportInInfoForm.getEndDate().equals(""))
			{
				where.append(" and ri.reportDate<='" + reportInInfoForm.getEndDate() + "'");
			}
			/** ��־��Χ�������ж� */
			if (reportInInfoForm.getAllFlags() != null && !reportInInfoForm.getAllFlags().equals(""))
			{
				 // ���ͨ��
				 where.append(" and ri.checkFlag=" + Config.CHECK_FLAG_OK);
			}
			/** �������Ƶ��ж� */
			if (reportInInfoForm.getRepName() != null && !reportInInfoForm.getRepName().equals(""))
			{
				where.append(" and ri.repName like '%" + reportInInfoForm.getRepName() + "%'");
			}

			/** ���ϱ���鿴Ȩ�� */
			if (operator == null)
				return retvals;
			if (operator.isSuperManager() == false)
			{
				if (operator.getChildRepSearchPopedom() == null || operator.getChildRepSearchPopedom().equals(""))
					return retvals;
				/**���������ݿ��ж�*/
				if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
					where.append(" and ltrim(rtrim(ri.orgId)) || ri.MChildReport.comp_id.childRepId in(" + operator.getChildRepSearchPopedom() + ")");
				if(Config.DB_SERVER_TYPE.equals("sqlserver"))
					where.append(" and ltrim(rtrim(ri.orgId)) + ri.MChildReport.comp_id.childRepId in(" + operator.getChildRepSearchPopedom() + ")");
			}
			where.append(" and ri.MChildReport.comp_id.childRepId in(" + childRepIDStr.trim().substring(0, childRepIDStr.trim().length() - 1) + ")");
			hql.append(where.toString() + " order by ri.year,ri.term,ri.orgId");
			
			// ��Ӽ�����Session

			Query query = session.createQuery(hql.toString());
		//	query.setFirstResult(offset).setMaxResults(limit);
			List list = query.list();

			if (list != null)
			{
				retvals = new ArrayList();
				// ѭ����ȡ���ݿ����������¼
				for (Iterator it = list.iterator(); it.hasNext();)
				{
					// ʵ����һ���м���Aditing��������ҳ��������ֶ���
					Aditing aditing = new Aditing();
					// ���ɳ־û�����
					ReportIn reportInPersistence = (ReportIn) it.next();
					// �����ҵ�����˱�־set��aditing����
					aditing.setCheckFlag(reportInPersistence.getCheckFlag());
					// �жϴ�MOrg���в�ѯ�ļ�¼�Ƿ�Ϊ��
					if ((StrutsMOrgDelegate.selectOne(reportInPersistence.getOrgId())) != null
							&& !(StrutsMOrgDelegate.selectOne(reportInPersistence.getOrgId())).equals(""))
					{
						// �����Ϊ��,�򽫴�MOrg�в��ҵ��Ļ�����set��aditing����
						aditing.setOrgName((StrutsMOrgDelegate.selectOne(reportInPersistence.getOrgId())).getOrg_name());
					}
					// �����ҵ��ı���id����aditing����
					aditing.setOrgId(reportInPersistence.getOrgId());
					aditing.setRepInId(reportInPersistence.getRepInId());
					aditing.setYear(reportInPersistence.getYear());
					aditing.setTerm(reportInPersistence.getTerm());
					// �����ҵı�������set��adtiting����
					// if
					// (reportInPersistence.getRepName().equals(reportInPersistence.getMChildReport().getReportName()))
					aditing.setRepName(reportInPersistence.getRepName());
					/*
					 * else aditing.setRepName(reportInPersistence.getRepName() +
					 * reportInPersistence.getMChildReport().getReportName());
					 */
					// �����ҵı�������set��aditing����
					aditing.setReportDate(reportInPersistence.getReportDate());
					// �����ҵ��ӱ���ID�Ž�aditing����
					aditing.setChildRepId(reportInPersistence.getMChildReport().getComp_id().getChildRepId());
					// �����ҵİ汾��set��aditing����
					aditing.setVersionId(reportInPersistence.getMChildReport().getComp_id().getVersionId());
					aditing.setCurrName(reportInPersistence.getMCurr().getCurName());
					MActuRep mActuRep = StrutsReportInDelegate.GetFreR(reportInPersistence);
					if (mActuRep != null)
					{
						// ���Ϳھ�
						aditing.setDataRgTypeName(mActuRep.getMDataRgType().getDataRgDesc());
						// ����Ƶ��
						aditing.setActuFreqName(mActuRep.getMRepFreq().getRepFreqName());
					}
					retvals.add(aditing);
				}
			}
		}
		catch (HibernateException he)
		{
			retvals = null;
			log.printStackTrace(he);
		}
		catch (Exception e)
		{
			retvals = null;
			log.printStackTrace(e);
		}
		finally
		{
			// ������Ӵ��ڣ���Ͽ��������Ự������
			if (conn != null)
				conn.closeSession();
		}
		return retvals;
	}
	/**
	 * ����鿴�Ĳ��Ҽ�¼����
	 * 
	 * @author ����
	 * @param reportInInfoForm
	 * @param offset
	 * @param limit
	 * @param operator
	 * @return
	 * @throws Exception
	 */
	public static List selectOfManualSHCK(ReportInInfoForm reportInInfoForm, int offset, int limit, Operator operator) throws Exception
	{
		List retvals = null;
		DBConn conn = null;
		Session session = null;

		try
		{

			conn = new DBConn();
			session = conn.openSession();

			String childRepIDStr = "";

			String sql = "select mrr.MChildReport.comp_id.childRepId from MRepRange mrr where mrr.comp_id.orgId='" + operator.getOrgId() + "'";

			/** ���ϱ����Ƿ��Ѿ����� */

			sql += " and  mrr.MChildReport.isPublic = 1";

			List reportInList = session.find(sql);
			if (reportInList != null && reportInList.size() > 0)
			{

				for (int j = 0; j < reportInList.size(); j++)
				{
					childRepIDStr += "'" + reportInList.get(j) + "',";
				}

			}

			StringBuffer hql = new StringBuffer(
					"from ReportIn ri where "
							+ "ri.times=(select max(r.times) from ReportIn r where r.MChildReport.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and "
							+ "r.MChildReport.comp_id.versionId=ri.MChildReport.comp_id.versionId and r.MDataRgType.dataRangeId=ri.MDataRgType.dataRangeId and r.orgId=ri.orgId and "
							+ "r.MCurr.curId=ri.MCurr.curId and r.year=ri.year and r.term=ri.term and r.checkFlag not in ("
							+ com.fitech.net.config.Config.CHECK_FLAG_AFTERJY + "," + com.fitech.net.config.Config.CHECK_FLAG_UNREPORT + ","
							+ com.fitech.net.config.Config.CHECK_FLAG_AFTERSAVE + "))");

			StringBuffer where = new StringBuffer("");
			where.append(" and ri.checkFlag not in (" + com.fitech.net.config.Config.CHECK_FLAG_UNREPORT + ","
					+ com.fitech.net.config.Config.CHECK_FLAG_AFTERJY + ")");
			if (reportInInfoForm == null)
				return retvals;

			/** �����������ж� */
			if (reportInInfoForm.getOrgName() != null && !reportInInfoForm.getOrgName().equals(""))
			{
				String orgIds = StrutsOrgNetDelegate.selectOrgIdToString(reportInInfoForm.getOrgName());
				if (orgIds != null && !orgIds.equals(""))
					where.append(" and ri.orgId in (" + orgIds + ")");
			}
			/** �������������ж� */
			if (reportInInfoForm.getYear() != null && reportInInfoForm.getYear().intValue() > 0)
			{
				where.append(" and ri.year=" + reportInInfoForm.getYear());
			}
			if (reportInInfoForm.getTerm() != null && reportInInfoForm.getTerm().intValue() > 0)
			{
				where.append(" and ri.term=" + reportInInfoForm.getTerm());
			}
			/** ��ʼʱ��������ж� */
			if (reportInInfoForm.getStartDate() != null && !reportInInfoForm.getStartDate().equals(""))
			{
				where.append(" and ri.reportDate>='" + reportInInfoForm.getStartDate() + "'");
			}
			/** ����ʱ����ж� */
			if (reportInInfoForm.getEndDate() != null && !reportInInfoForm.getEndDate().equals(""))
			{
				where.append(" and ri.reportDate<='" + reportInInfoForm.getEndDate() + "'");
			}
			/** ��־��Χ�������ж� */
			if (reportInInfoForm.getAllFlags() != null && !reportInInfoForm.getAllFlags().equals(""))
			{
				switch ((Integer.valueOf(reportInInfoForm.getAllFlags())).intValue())
				{
					case -1: // ���δͨ��
						where.append(" and ri.checkFlag=" + Config.CHECK_FLAG_NO);
						break;
					case 0: // δ���
						where.append(" and ri.checkFlag=" + Config.CHECK_FLAG_UN);
						break;
					case 1: // ���ͨ��
						where.append(" and ri.checkFlag=" + Config.CHECK_FLAG_OK);
						break;

					case 4: // �쳣��־�������ж�
						where.append(" and ri.abmormityChangeFlag=" + Config.ABMORMITY_FLAG_NO);
						break;
				}
			}
			/** �������Ƶ��ж� */
			if (reportInInfoForm.getRepName() != null && !reportInInfoForm.getRepName().equals(""))
			{
				where.append(" and ri.repName like '%" + reportInInfoForm.getRepName() + "%'");
			}

			/** ���ϱ���鿴Ȩ�� */
			if (operator == null)
				return retvals;
			if (operator.isSuperManager() == false)
			{
				if (operator.getChildRepSearchPopedom() == null || operator.getChildRepSearchPopedom().equals(""))
					return retvals;
				/**���������ݿ��ж�*/
				if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
					where.append(" and ltrim(rtrim(ri.orgId)) || ri.MChildReport.comp_id.childRepId in(" + operator.getChildRepSearchPopedom() + ")");
				if(Config.DB_SERVER_TYPE.equals("sqlserver"))
					where.append(" and ltrim(rtrim(ri.orgId)) + ri.MChildReport.comp_id.childRepId in(" + operator.getChildRepSearchPopedom() + ")");
			}
		//	where.append(" and ri.MChildReport.comp_id.childRepId in(" + childRepIDStr.trim().substring(0, childRepIDStr.trim().length() - 1) + ")");
			hql.append(where.toString() + " order by ri.year,ri.term,ri.orgId");

			// ��Ӽ�����Session

			Query query = session.createQuery(hql.toString());
			query.setFirstResult(offset).setMaxResults(limit);
			List list = query.list();

			if (list != null)
			{
				retvals = new ArrayList();
				// ѭ����ȡ���ݿ����������¼
				for (Iterator it = list.iterator(); it.hasNext();)
				{
					// ʵ����һ���м���Aditing��������ҳ��������ֶ���
					Aditing aditing = new Aditing();
					// ���ɳ־û�����
					ReportIn reportInPersistence = (ReportIn) it.next();
					// �����ҵ�����˱�־set��aditing����
					aditing.setCheckFlag(reportInPersistence.getCheckFlag());
					// �жϴ�MOrg���в�ѯ�ļ�¼�Ƿ�Ϊ��
					if ((StrutsMOrgDelegate.selectOne(reportInPersistence.getOrgId())) != null
							&& !(StrutsMOrgDelegate.selectOne(reportInPersistence.getOrgId())).equals(""))
					{
						// �����Ϊ��,�򽫴�MOrg�в��ҵ��Ļ�����set��aditing����
						aditing.setOrgName((StrutsMOrgDelegate.selectOne(reportInPersistence.getOrgId())).getOrg_name());
					}
					// �����ҵ��ı���id����aditing����
					aditing.setRepInId(reportInPersistence.getRepInId());
					aditing.setYear(reportInPersistence.getYear());
					aditing.setTerm(reportInPersistence.getTerm());
					// �����ҵı�������set��adtiting����
					// if
					// (reportInPersistence.getRepName().equals(reportInPersistence.getMChildReport().getReportName()))
					aditing.setRepName(reportInPersistence.getRepName());
					/*
					 * else aditing.setRepName(reportInPersistence.getRepName() +
					 * reportInPersistence.getMChildReport().getReportName());
					 */
					// �����ҵı�������set��aditing����
					aditing.setReportDate(reportInPersistence.getReportDate());
					// �����ҵ��ӱ���ID�Ž�aditing����
					aditing.setChildRepId(reportInPersistence.getMChildReport().getComp_id().getChildRepId());
					// �����ҵİ汾��set��aditing����
					aditing.setVersionId(reportInPersistence.getMChildReport().getComp_id().getVersionId());
					aditing.setCurrName(reportInPersistence.getMCurr().getCurName());
					MActuRep mActuRep = StrutsReportInDelegate.GetFreR(reportInPersistence);
					if (mActuRep != null)
					{
						// ���Ϳھ�
						aditing.setDataRgTypeName(mActuRep.getMDataRgType().getDataRgDesc());
						// ����Ƶ��
						aditing.setActuFreqName(mActuRep.getMRepFreq().getRepFreqName());
					}
					retvals.add(aditing);
				}
			}
		}
		catch (HibernateException he)
		{
			retvals = null;
			log.printStackTrace(he);
		}
		catch (Exception e)
		{
			retvals = null;
			log.printStackTrace(e);
		}
		finally
		{
			// ������Ӵ��ڣ���Ͽ��������Ự������
			if (conn != null)
				conn.closeSession();
		}
		return retvals;
	}

	/**
	 * ���� ���ݴ�����reportid������еļ�¼
	 * 
	 * @reportInId
	 * @resList ����һ���������м�¼��list����(���������Ϣ��ϸ��ѯ==����) �쳣�仯�е�ʵ�ʱ仯���(��Ե�ʽ��)
	 */
	public static List getAllReportInInfo(Integer reportInId)
	{
		List retVals = null;
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		if (reportInId != null)
		{
			try
			{

				String hhql = "select r.cell_id, r.report_value ,m.col_id,m.row_id,m.cell_name  from report_in_info r"
						+ " left join  M_cell m on  r.cell_id=m.cell_id where r.rep_in_id=? and r.report_value is not null";
				con = new com.cbrc.smis.proc.jdbc.FitechConnection().getConnect();
				stmt = con.prepareStatement(hhql.toUpperCase());
				stmt.setInt(1, reportInId.intValue());

				rs = stmt.executeQuery();
				if (rs != null)
				{
					retVals = new ArrayList();
					while (rs.next())
					{
						int cellId=rs.getInt(1);
						String reportValue = rs.getString(2);
						String colId = rs.getString(3);
						int rowId = rs.getInt(4);
						String cellName = rs.getString("cell_name");
						ReportInInfoForm reportInInfoForm = new ReportInInfoForm();
						reportInInfoForm.setCellId(new Integer(cellId));
						reportInInfoForm.setReportValue(reportValue);
						reportInInfoForm.setColId(colId);
						reportInInfoForm.setCellName(cellName);
						reportInInfoForm.setRowId(new Integer(rowId));
						retVals.add(reportInInfoForm);

					}
				}
			}
			catch (Exception e)
			{
				log.printStackTrace(e);
				try
				{
					con.close();
				}
				catch (SQLException e1)
				{
					// TODO �Զ����� catch ��
					e1.printStackTrace();
				}
			}
			finally
			{
				if (con != null)
					try
					{
						con.close();
					}
					catch (SQLException e)
					{
						// TODO �Զ����� catch ��
						e.printStackTrace();
					}
			}
		}
		return retVals;
	}
	/**
	 * ���ݱ���ID��ȡ��Ԫ������
	  * @Title: getAllReportInInfoCount 
	  * @date: Jan 7, 2013  4:26:01 PM
	  * @param reportInId
	  * @return int
	 */
	public static int getAllReportInInfoCount(String reportInId){
		int result = 0;
		// ���ӺͻỰ����ĳ�ʼ��
		DBConn conn = null;
		Session session = null;
		if(reportInId!=null)
		{
			try {
				conn = new DBConn();
				session = conn.openSession();
				String hsql = "select count(0) from ReportInInfo where comp_id.repInId="+reportInId;
				result = (Integer)session.find(hsql).get(0);
			} catch (HibernateException e) {
				// TODO Auto-generated catch block
				log.printStackTrace(e);
			}finally {
				// �����������Ͽ����ӣ������Ự������
				if (conn != null)
					conn.closeSession();
			}
		}
		return result;
	}
	/**
	 * �����ӱ���ID�Ͱ汾�Ż�ñ��������Ϣ
	 * 
	 * @author rds
	 * @date 2006-01-09
	 * 
	 * @param childRepId
	 *            String �ӱ���ID
	 * @param versionId
	 *            String �汾��
	 * @param exceptCells
	 *            String �������
	 * @return List
	 */
	public static List getCols(String childRepId, String versionId, String exceptCells)
	{
		if (childRepId == null || versionId == null)
			return null;

		List cols = null;
		DBConn conn = null;
		try
		{
			String hql = "select distinct mc.colId from MCell mc where mc.MChildReport.comp_id.childRepId='" + childRepId + "' and "
					+ "mc.MChildReport.comp_id.versionId='" + versionId + "' " + (!exceptCells.equals("") ? " and mc.colId not in (" + exceptCells + ")" : "")
					+ "order by mc.colId";
			conn = new DBConn();
			cols = conn.openSession().find(hql);
		}
		catch (HibernateException he)
		{
			log.printStackTrace(he);
			cols = null;
		}
		catch (Exception e)
		{
			log.printStackTrace(e);
			cols = null;
		}
		finally
		{
			if (conn != null)
				conn.closeSession();
		}

		return cols;
	}

	/**
	 * �޸ĵ�Ԫ��ֵ author jcm
	 * 
	 */

	public static boolean updateReportInInfo(List reportInInList)
	{

		boolean bool = false;
		DBConn conn = null;
		Session session = null;

		if (reportInInList != null && reportInInList.size() > 0)
		{
			try
			{
				conn = new DBConn();
				session = conn.beginTransaction();

				for (int i = 0; i < reportInInList.size(); i++)
				{
					ReportInInfoForm reportInInfoForm = (ReportInInfoForm) reportInInList.get(i);
					ReportInInfo reportInInfo = new ReportInInfo();
					TranslatorUtil.copyVoToPersistence2(reportInInfo, reportInInfoForm);
					session.update(reportInInfo);
				}
				bool = true;
			}
			catch (HibernateException he)
			{
				log.printStackTrace(he);
				bool = false;
			}
			catch (Exception e)
			{
				log.printStackTrace(e);
				bool = false;
			}
			finally
			{
				if (conn != null)
					conn.endTransaction(bool);
			}
		}
		return bool;
	}

	public static int getMyOrgReportInCount(ReportInInfoForm reportInInfoForm, Operator operator)
	{

		int count = 0;

		// ���Ӷ���ͻỰ�����ʼ��
		DBConn conn = null;
		Session session = null;

		try
		{

			conn = new DBConn();
			session = conn.openSession();

			String childRepIDStr = "";

			String sql = "select mrr.MChildReport.comp_id.childRepId from MRepRange mrr where mrr.comp_id.orgId='" + operator.getOrgId() + "'";

			/** ���ϱ����Ƿ��Ѿ����� */

			sql += " and  mrr.MChildReport.isPublic = 1";

			List reportInList = session.find(sql);
			if (reportInList != null && reportInList.size() > 0)
			{

				for (int j = 0; j < reportInList.size(); j++)
				{
					childRepIDStr += "'" + reportInList.get(j) + "',";
				}

			}

			// ��ѯ����HQL������
			StringBuffer hql = new StringBuffer(
					"select count(*) from ReportIn ri WHERE "
							+ "ri.times=(select max(r.times) from ReportIn r where r.MChildReport.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and "
							+ "r.MChildReport.comp_id.versionId=ri.MChildReport.comp_id.versionId and r.MDataRgType.dataRangeId=ri.MDataRgType.dataRangeId and r.orgId=ri.orgId and "
							+ "r.MCurr.curId=ri.MCurr.curId and r.year=ri.year and r.term=ri.term and r.checkFlag not in ("
							+ com.fitech.net.config.Config.CHECK_FLAG_AFTERJY + "," + com.fitech.net.config.Config.CHECK_FLAG_UNREPORT + ","
							+ com.fitech.net.config.Config.CHECK_FLAG_AFTERSAVE + "))");

			StringBuffer where = new StringBuffer("");
			where.append("  and ri.checkFlag not in (" + com.fitech.net.config.Config.CHECK_FLAG_UNREPORT + ","
					+ com.fitech.net.config.Config.CHECK_FLAG_AFTERJY + ")");
			if (reportInInfoForm == null)
				return count;

			if (reportInInfoForm.getYear() != null && reportInInfoForm.getYear().intValue() > 0)
			{
				where.append(" and ri.year=" + reportInInfoForm.getYear());
			}
			if (reportInInfoForm.getTerm() != null && reportInInfoForm.getTerm().intValue() > 0)
			{
				where.append(" and ri.term=" + reportInInfoForm.getTerm());
			}
			// ��ʼʱ��������ж�
			if (reportInInfoForm.getStartDate() != null && !reportInInfoForm.getStartDate().equals(""))
			{
				where.append(" and ri.reportDate>='" + reportInInfoForm.getStartDate() + "'");
			}
			// ����ʱ����ж�
			if (reportInInfoForm.getEndDate() != null && !reportInInfoForm.getEndDate().equals(""))
			{
				where.append(" and ri.reportDate<='" + reportInInfoForm.getEndDate() + "'");
			}
			// ��־��Χ�������ж�
			if (reportInInfoForm.getAllFlags() != null && !reportInInfoForm.getAllFlags().equals(""))
			{
				switch ((Integer.valueOf(reportInInfoForm.getAllFlags())).intValue())
				{
					case -1: // ���δ
						where.append(" and ri.checkFlag=" + Config.CHECK_FLAG_NO);
						break;
					case 0: // δ���
						where.append(" and ri.checkFlag=" + Config.CHECK_FLAG_UN);
						break;
					case 1: // ���ͨ��
						where.append(" and ri.checkFlag=" + Config.CHECK_FLAG_OK);
						break;
					case 4:
					{
						where.append(" and ri.abmormityChangeFlag=" + Config.ABMORMITY_FLAG_NO);
						break;
					}
				}
			}
			// �������Ƶ��ж�
			if (reportInInfoForm.getRepName() != null && !reportInInfoForm.getRepName().equals(""))
			{
				where.append(" and ri.repName like '%" + reportInInfoForm.getRepName() + "%'");
			}

			/** ���ϱ���鿴Ȩ�� */
			if (operator == null)
				return count;
			if (operator.isSuperManager() == false)
			{
				if (operator.getChildRepSearchPopedom() == null || operator.getChildRepSearchPopedom().equals(""))
					return count;
				/**���������ݿ��ж�*/
				if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
					where.append(" and '" + operator.getOrgId() + "'||ri.MChildReport.comp_id.childRepId in (" + operator.getChildRepSearchPopedom() + ")");
				if(Config.DB_SERVER_TYPE.equals("sqlserver"))
					where.append(" and '" + operator.getOrgId() + "'+ri.MChildReport.comp_id.childRepId in (" + operator.getChildRepSearchPopedom() + ")");
			}
			where.append(" and ri.MChildReport.comp_id.childRepId in(" + childRepIDStr.trim().substring(0, childRepIDStr.trim().length() - 1) + ")");
			where.append(" and ri.orgId ='" + operator.getOrgId() + "'");
			hql.append(where.toString());

			List list = session.find(hql.toString());

			if (list != null && list.size() != 0)
			{
				count = ((Integer) list.get(0)).intValue();
			}
		}
		catch (HibernateException he)
		{
			log.printStackTrace(he);
		}
		catch (Exception e)
		{
			log.printStackTrace(e);
		}
		finally
		{
			// ������Ӵ��ڣ���Ͽ��������Ự������
			if (conn != null)
				conn.closeSession();
		}
		return count;

	}

	public static List getMyOrgReportInList(ReportInInfoForm reportInInfoForm, int offset, int limit, Operator operator) throws Exception
	{
		List retvals = null;
		DBConn conn = null;
		Session session = null;

		try
		{

			conn = new DBConn();
			session = conn.openSession();

			String childRepIDStr = "";

			String sql = "select mrr.MChildReport.comp_id.childRepId from MRepRange mrr where mrr.comp_id.orgId='" + operator.getOrgId() + "'";

			/** ���ϱ����Ƿ��Ѿ����� */

			sql += " and  mrr.MChildReport.isPublic = 1";

			List reportInList = session.find(sql);
			if (reportInList != null && reportInList.size() > 0)
			{

				for (int j = 0; j < reportInList.size(); j++)
				{
					childRepIDStr += "'" + reportInList.get(j) + "',";
				}

			}

			StringBuffer hql = new StringBuffer(
					"from ReportIn ri where "
							+ "ri.times=(select max(r.times) from ReportIn r where r.MChildReport.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and "
							+ "r.MChildReport.comp_id.versionId=ri.MChildReport.comp_id.versionId and r.MDataRgType.dataRangeId=ri.MDataRgType.dataRangeId and r.orgId=ri.orgId and "
							+ "r.MCurr.curId=ri.MCurr.curId and r.year=ri.year and r.term=ri.term and r.checkFlag not in ("
							+ com.fitech.net.config.Config.CHECK_FLAG_AFTERJY + "," + com.fitech.net.config.Config.CHECK_FLAG_UNREPORT + ","
							+ com.fitech.net.config.Config.CHECK_FLAG_AFTERSAVE + "))");

			StringBuffer where = new StringBuffer("");
			where.append("  and ri.checkFlag not in (" + com.fitech.net.config.Config.CHECK_FLAG_UNREPORT + ","
					+ com.fitech.net.config.Config.CHECK_FLAG_AFTERJY + ")");
			if (reportInInfoForm == null)
			{
				return retvals;
			}

			if (reportInInfoForm.getYear() != null && reportInInfoForm.getYear().intValue() > 0)
			{
				where.append(" and ri.year=" + reportInInfoForm.getYear());
			}
			if (reportInInfoForm.getTerm() != null && reportInInfoForm.getTerm().intValue() > 0)
			{
				where.append(" and ri.term=" + reportInInfoForm.getTerm());
			}
			// ��ʼʱ��������ж�
			if (reportInInfoForm.getStartDate() != null && !reportInInfoForm.getStartDate().equals(""))
			{
				where.append(" and ri.reportDate>='" + reportInInfoForm.getStartDate() + "'");
			}

			// ����ʱ����ж�
			if (reportInInfoForm.getEndDate() != null && !reportInInfoForm.getEndDate().equals(""))
			{
				where.append(" and ri.reportDate<='" + reportInInfoForm.getEndDate() + "'");
			}

			// ��־��Χ�������ж�
			if (reportInInfoForm.getAllFlags() != null && !reportInInfoForm.getAllFlags().equals(""))
			{
				switch ((Integer.valueOf(reportInInfoForm.getAllFlags())).intValue())
				{
					case -1: // ���δͨ��
						where.append(" and ri.checkFlag=" + Config.CHECK_FLAG_NO);
						break;
					case 0: // δ���
						where.append(" and ri.checkFlag=" + Config.CHECK_FLAG_UN);
						break;
					case 1: // ���ͨ��
						where.append(" and ri.checkFlag=" + Config.CHECK_FLAG_OK);
						break;
					case 4:
					{
						where.append(" and ri.abmormityChangeFlag=" + Config.ABMORMITY_FLAG_NO);
						break;
					}
				}
			}
			// �������Ƶ��ж�
			if (reportInInfoForm.getRepName() != null && !reportInInfoForm.getRepName().equals(""))
			{
				where.append(" and ri.repName like '%" + reportInInfoForm.getRepName() + "%'");
			}

			/** ���ϱ���鿴Ȩ�� */
			if (operator == null)
				return retvals;
			if (operator.isSuperManager() == false)
			{
				if (operator.getChildRepSearchPopedom() == null || operator.getChildRepSearchPopedom().equals(""))
					return retvals;
				/**���������ݿ��ж�*/
				if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
					where.append(" and '" + operator.getOrgId() + "'||ri.MChildReport.comp_id.childRepId in (" + operator.getChildRepSearchPopedom() + ")");
				if(Config.DB_SERVER_TYPE.equals("sqlserver"))
					where.append(" and '" + operator.getOrgId() + "'+ri.MChildReport.comp_id.childRepId in (" + operator.getChildRepSearchPopedom() + ")");
			}

			// ��ǰ����Ȩ��
			where.append(" and ri.orgId ='" + operator.getOrgId() + "'");

			where.append(" and ri.MChildReport.comp_id.childRepId in(" + childRepIDStr.trim().substring(0, childRepIDStr.trim().length() - 1) + ")");

			hql.append(where.toString() + " order by ri.year,ri.term");

			Query query = session.createQuery(hql.toString());
			query.setFirstResult(offset).setMaxResults(limit);
			List list = query.list();

			if (list != null)
			{
				retvals = new ArrayList();
				// ѭ����ȡ���ݿ����������¼
				for (Iterator it = list.iterator(); it.hasNext();)
				{
					// ʵ����һ���м���Aditing��������ҳ��������ֶ���
					Aditing aditing = new Aditing();
					// ���ɳ־û�����
					ReportIn reportInPersistence = (ReportIn) it.next();
					// �����ҵ�����˱�־set��aditing����
					aditing.setCheckFlag(reportInPersistence.getCheckFlag());
					// �жϴ�MOrg���в�ѯ�ļ�¼�Ƿ�Ϊ��
					if ((StrutsMOrgDelegate.selectOne(reportInPersistence.getOrgId())) != null
							&& !(StrutsMOrgDelegate.selectOne(reportInPersistence.getOrgId())).equals(""))
					{
						// �����Ϊ��,�򽫴�MOrg�в��ҵ��Ļ�����set��aditing����
						aditing.setOrgName((StrutsMOrgDelegate.selectOne(reportInPersistence.getOrgId())).getOrg_name());
					}
					// �����ҵ��ı���id����aditing����
					aditing.setRepInId(reportInPersistence.getRepInId());
					aditing.setYear(reportInPersistence.getYear());
					aditing.setTerm(reportInPersistence.getTerm());
					aditing.setTblInnerValidateFlag(reportInPersistence.getTblInnerValidateFlag());
					// �����ҵı�������set��adtiting����
					// if
					// (reportInPersistence.getRepName().equals(reportInPersistence.getMChildReport().getReportName()))
					aditing.setRepName(reportInPersistence.getRepName());
					// else
					// aditing.setRepName(reportInPersistence.getRepName() + "-"
					// + reportInPersistence.getMChildReport().getReportName());
					// �����ҵı�������set��aditing����
					aditing.setReportDate(reportInPersistence.getReportDate());
					// �����ҵ��ӱ���ID�Ž�aditing����
					aditing.setChildRepId(reportInPersistence.getMChildReport().getComp_id().getChildRepId());
					// �����ҵİ汾��set��aditing����
					aditing.setVersionId(reportInPersistence.getMChildReport().getComp_id().getVersionId());
					aditing.setCurrName(reportInPersistence.getMCurr().getCurName());

					// ������ҳ����������м�¼��adtiing����add��arraylist�����з���action
					MActuRep mActuRep = StrutsReportInDelegate.GetFreR(reportInPersistence);
					if (mActuRep != null)
					{
						// ���Ϳھ�
						aditing.setDataRgTypeName(mActuRep.getMDataRgType().getDataRgDesc());
						// ����Ƶ��
						aditing.setActuFreqName(mActuRep.getMRepFreq().getRepFreqName());
					}

					if (reportInPersistence.getCheckFlag().toString().equals(com.fitech.net.config.Config.CHECK_FLAG_FAILED.toString()))
					{
						Set reportAgainSets = reportInPersistence.getReportAgainSets();
						String cause = "";
						if (reportAgainSets != null && reportAgainSets.size() > 0)
						{
							for (Iterator iterSet = reportAgainSets.iterator(); iterSet.hasNext();)
							{
								ReportAgainSet againSet = (ReportAgainSet) iterSet.next();
								cause = cause.equals("") ? againSet.getCause() : "\n" + cause + againSet.getCause();
							}
						}
						aditing.setWhy(cause);
					}
					retvals.add(aditing);
				}
			}
		}
		catch (HibernateException he)
		{
			retvals = null;
			log.printStackTrace(he);
		}
		catch (Exception e)
		{
			retvals = null;
			log.printStackTrace(e);
		}
		finally
		{
			// ������Ӵ��ڣ���Ͽ��������Ự������
			if (conn != null)
				conn.closeSession();
		}
		return retvals;
	}

	/**
	 * �޸ĵ�Ԫ��ֵ jcm
	 * 
	 * @param cellId
	 * @param repInId
	 * @param reportValue
	 * @throws Exception
	 */
	public static void updateReportInInfo(Integer cellId, Integer repInId, String reportValue) throws Exception
	{

		DBConn dBConn = null;
		Session session = null;

		ReportInInfo reportInInfo = new ReportInInfo();
		ReportInInfoPK comp_id = new ReportInInfoPK(); // new ������

		comp_id.setCellId(cellId);
		comp_id.setRepInId(repInId);
		reportInInfo.setComp_id(comp_id); // ������SET��ȥ
		reportInInfo.setReportValue(reportValue);

		try
		{

			dBConn = new DBConn();
			session = dBConn.beginTransaction();
			session.update(reportInInfo);
			dBConn.endTransaction(true);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			dBConn.endTransaction(false);
		}
		finally
		{
			if (session != null)
				dBConn.closeSession();
		}

	}

	public static ReportInInfoForm getNeedReportInInfoForm(Integer repInId, Integer cellId)
	{
		ReportInInfoForm riif = new ReportInInfoForm();
		DBConn conn = new DBConn();
		Session session = conn.openSession();
		String hql = "from ReportInInfo rii where rii.comp_id.cellId=" + cellId + " and rii.comp_id.repInId=" + repInId;
		List list = null;
		try
		{
			list = session.find(hql);
		}
		catch (HibernateException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			if (conn != null)
				conn.closeSession();
		}
		if (list == null)
		{
			return null;
		}
		if (list.size() <= 0)
		{
			return null;
		}
		ReportInInfo rii_temp = (ReportInInfo) list.get(0);
		if (rii_temp != null)
			try
			{
				TranslatorUtil.copyPersistenceToVo(rii_temp, riif);
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return riif;
	}

	/**
	 * ����鿴�Ĳ����ܼ�¼��
	 *  
	 * @param reportInInfoForm ���������Ϣ
	 * @param operator ��¼�û�
	 * @author jcm
	 * @return int ��¼��
	 */
	public static int getSubOrgReportRecordCount(ReportInInfoForm reportInInfoForm, Operator operator){
		int count = 0;
		DBConn conn = null;
		Session session = null;
		try{
			conn = new DBConn();
			session = conn.openSession();			

			//��ѯ����HQL������
			StringBuffer hql = new StringBuffer(
					"select count(*) from ReportIn ri WHERE "
							+ "ri.times=1");
			StringBuffer where = new StringBuffer("");
			where.append("  and ri.checkFlag =" + com.fitech.net.config.Config.CHECK_FLAG_PASS + "");
			if (reportInInfoForm == null)
				return count;

			// �����������ж�
			if (reportInInfoForm.getOrgId() != null && !reportInInfoForm.getOrgId().equals("")){
				where.append(" and ri.orgId ='" + reportInInfoForm.getOrgId() + "'");
			}
			if (reportInInfoForm.getYear() != null && reportInInfoForm.getYear().intValue() > 0){
				where.append(" and ri.year=" + reportInInfoForm.getYear());
			}
			if (reportInInfoForm.getTerm() == null || reportInInfoForm.getTerm().intValue() <= 0){
				if (reportInInfoForm.getTimes() != null && reportInInfoForm.getTimes().intValue() > 0)
					where.append(" and ri.term between 1 and " + reportInInfoForm.getTimes() + "");
			}else{
				if (reportInInfoForm.getTimes() == null || reportInInfoForm.getTimes().intValue() <= 0)
					where.append(" and ri.term between " + reportInInfoForm.getTerm() + " and 12");
				else
					where.append(" and ri.term between " + reportInInfoForm.getTerm() + " and " + reportInInfoForm.getTimes() + "");
			}

			//�������Ƶ��ж�
			if (reportInInfoForm.getRepName() != null && !reportInInfoForm.getRepName().equals("")){
				where.append(" and ri.repName like '%" + reportInInfoForm.getRepName() + "%'");
			}

			/** ���ϱ���鿴Ȩ�� */
			if (operator == null)
				return count;
			if (operator.isSuperManager() == false){
				if (operator.getChildRepSearchPopedom() == null || operator.getChildRepSearchPopedom().equals(""))
					return count;
				/**���������ݿ��ж�*/
				if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
					where.append(" and rtrim(ltrim(ri.orgId)) || ri.MChildReport.comp_id.childRepId in (" + operator.getChildRepSearchPopedom() + ")");
				if(Config.DB_SERVER_TYPE.equals("sqlserver"))
					where.append(" and rtrim(ltrim(ri.orgId)) + ri.MChildReport.comp_id.childRepId in (" + operator.getChildRepSearchPopedom() + ")");
			}
			/** ȥ�����е�Ȩ�� */
			where.append(" and ri.orgId not in ('" + operator.getOrgId() + "')");
			where.append(" and ri.MChildReport.comp_id.childRepId in(select distinct mrr.MChildReport.comp_id.childRepId from MRepRange mrr where mrr.MChildReport.isPublic = 1)");
			hql.append(where.toString());
session.find(operator.getChildRepSearchPopedom());
			List list = session.find(hql.toString());
			if (list != null && list.size() != 0){
				count = ((Integer) list.get(0)).intValue();
			}

		}catch (HibernateException he){
			count = 0;
			log.printStackTrace(he);
		}catch (Exception e){
			count = 0;
			log.printStackTrace(e);
		}finally{
			// ������Ӵ��ڣ���Ͽ��������Ự������
			if (conn != null)
				conn.closeSession();
		}
		return count;
	}

	/**
	 * �鿴��֧��������
	 * 
	 * @param reportInInfoForm �������FormBean
	 * @param offset ���ݿ��¼����ʼλ��
	 * @param limit ��Ҫ���ҵļ�¼��
	 * @param operator ��¼�û�
	 * @return List ��¼����
	 */
	public static List getSubOrgReportRecord(ReportInInfoForm reportInInfoForm, int offset, int limit, Operator operator){
		List retvals = null;
		DBConn conn = null;
		Session session = null;
		try{
			conn = new DBConn();
			session = conn.openSession();
			
			StringBuffer hql = new StringBuffer(
					"from ReportIn ri WHERE "
							+ "ri.times=1");
			StringBuffer where = new StringBuffer("");
			where.append("  and ri.checkFlag =" + com.fitech.net.config.Config.CHECK_FLAG_PASS + "   ");
			if (reportInInfoForm == null)
				return retvals;

			if (reportInInfoForm.getOrgId() != null && !reportInInfoForm.getOrgId().equals("")){
				where.append(" and ri.orgId ='" + reportInInfoForm.getOrgId() + "'");
			}if (reportInInfoForm.getYear() != null && reportInInfoForm.getYear().intValue() > 0){
				where.append(" and ri.year=" + reportInInfoForm.getYear());
			}if (reportInInfoForm.getTerm() == null || reportInInfoForm.getTerm().intValue() <= 0){
				if (reportInInfoForm.getTimes() != null && reportInInfoForm.getTimes().intValue() > 0)
					where.append(" and ri.term between 1 and " + reportInInfoForm.getTimes() + "");
			}else{
				if (reportInInfoForm.getTimes() == null || reportInInfoForm.getTimes().intValue() <= 0)
					where.append(" and ri.term between " + reportInInfoForm.getTerm() + " and 12");
				else
					where.append(" and ri.term between " + reportInInfoForm.getTerm() + " and " + reportInInfoForm.getTimes() + "");
			}

			//�������Ƶ��ж�
			if (reportInInfoForm.getRepName() != null && !reportInInfoForm.getRepName().equals("")){
				where.append(" and ri.repName like '%" + reportInInfoForm.getRepName() + "%'");
			}

			/** ���ϱ���鿴Ȩ�� */
			if (operator == null)
				return retvals;
			if (operator.isSuperManager() == false){
				if (operator.getChildRepSearchPopedom() == null || operator.getChildRepSearchPopedom().equals(""))
					return retvals;
				/**���������ݿ��ж�*/
				if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
					where.append(" and rtrim(ltrim(ri.orgId)) || ri.MChildReport.comp_id.childRepId in (" + operator.getChildRepSearchPopedom() + ")");
				if(Config.DB_SERVER_TYPE.equals("sqlserver"))
					where.append(" and rtrim(ltrim(ri.orgId)) + ri.MChildReport.comp_id.childRepId in (" + operator.getChildRepSearchPopedom() + ")");
			}
			/** ȥ�����е�Ȩ�� */
			where.append(" and ri.orgId not in ('" + operator.getOrgId() + "')");
			where.append(" and ri.MChildReport.comp_id.childRepId in(select distinct mrr.MChildReport.comp_id.childRepId from MRepRange mrr where mrr.MChildReport.isPublic = 1)");
			hql.append(where.toString() + " order by ri.year,ri.term,ri.orgId");


			Query query = session.createQuery(hql.toString());
			query.setFirstResult(offset).setMaxResults(limit);
			List list = query.list();

			if (list != null && list.size() > 0){
				retvals = new ArrayList();
				for (Iterator it = list.iterator(); it.hasNext();){
					Aditing aditing = new Aditing();
					ReportIn reportInPersistence = (ReportIn) it.next();
					aditing.setCheckFlag(reportInPersistence.getCheckFlag());
					OrgNetForm orgNetForm = StrutsMOrgDelegate.selectOne(reportInPersistence.getOrgId());
					if (orgNetForm != null && orgNetForm.getOrg_name() != null)
						aditing.setOrgName(orgNetForm.getOrg_name());
					aditing.setOrgId(reportInPersistence.getOrgId());
					// �����ҵ��ı���id����aditing����
					aditing.setRepInId(reportInPersistence.getRepInId());
					aditing.setYear(reportInPersistence.getYear());
					aditing.setTerm(reportInPersistence.getTerm());					
					aditing.setRepName(reportInPersistence.getRepName());					
					// �����ҵı�������set��aditing����
					aditing.setReportDate(reportInPersistence.getReportDate());
					// �����ҵ��ӱ���ID�Ž�aditing����
					aditing.setChildRepId(reportInPersistence.getMChildReport().getComp_id().getChildRepId());
					// �����ҵİ汾��set��aditing����
					aditing.setVersionId(reportInPersistence.getMChildReport().getComp_id().getVersionId());
					aditing.setCheckFlag(reportInPersistence.getCheckFlag());
					aditing.setCurrName(reportInPersistence.getMCurr().getCurName());

					MActuRep mActuRep = StrutsReportInDelegate.GetFreR(reportInPersistence);
					if (mActuRep != null){
						aditing.setDataRgTypeName(mActuRep.getMDataRgType().getDataRgDesc());
						aditing.setActuFreqName(mActuRep.getMRepFreq().getRepFreqName());
					}
					retvals.add(aditing);
				}
			}
		}catch (HibernateException he){
			retvals = null;
			log.printStackTrace(he);
		}catch (Exception e){
			retvals = null;
			log.printStackTrace(e);
		}finally{
			if (conn != null)
				conn.closeSession();
		}
		return retvals;
	}	

	/**
	 * �鿴��֧��������
	 * 
	 * @param reportInInfoForm �������FormBean
	 * @param offset ���ݿ��¼����ʼλ��
	 * @param limit ��Ҫ���ҵļ�¼��
	 * @param operator ��¼�û�
	 * @return List ��¼����
	 */
	public static List getSubOrgReportRecords(ReportInInfoForm reportInInfoForm, Operator operator){
		List retvals = null;
		DBConn conn = null;
		Session session = null;
		String childRepSearchPopedom = null;
		try{
			conn = new DBConn();
			session = conn.openSession();
			
			StringBuffer hql = new StringBuffer(
					"from ReportIn ri WHERE "
							+ "ri.times=(select max(r.times) from ReportIn r where r.MChildReport.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and "
							+ "r.MChildReport.comp_id.versionId=ri.MChildReport.comp_id.versionId and r.MDataRgType.dataRangeId=ri.MDataRgType.dataRangeId and r.orgId=ri.orgId and "
							+ "r.MCurr.curId=ri.MCurr.curId and r.year=ri.year and r.term=ri.term and r.checkFlag not in ("
							+ com.fitech.net.config.Config.CHECK_FLAG_AFTERJY + "," + com.fitech.net.config.Config.CHECK_FLAG_UNREPORT + ","
							+ com.fitech.net.config.Config.CHECK_FLAG_AFTERSAVE + "))");

			StringBuffer where = new StringBuffer("");
			where.append("  and ri.checkFlag =" + com.fitech.net.config.Config.CHECK_FLAG_PASS + "   ");
			if (reportInInfoForm == null)
				return retvals;

			if (reportInInfoForm.getOrgId() != null && !reportInInfoForm.getOrgId().equals("")){
				where.append(" and ri.orgId ='" + reportInInfoForm.getOrgId() + "'");
			}if (reportInInfoForm.getYear() != null && reportInInfoForm.getYear().intValue() > 0){
				where.append(" and ri.year=" + reportInInfoForm.getYear());
			}if (reportInInfoForm.getTerm() == null || reportInInfoForm.getTerm().intValue() <= 0){
				if (reportInInfoForm.getTimes() != null && reportInInfoForm.getTimes().intValue() > 0)
					where.append(" and ri.term between 1 and " + reportInInfoForm.getTimes() + "");
			}else{
				if (reportInInfoForm.getTimes() == null || reportInInfoForm.getTimes().intValue() <= 0)
					where.append(" and ri.term between " + reportInInfoForm.getTerm() + " and 12");
				else
					where.append(" and ri.term between " + reportInInfoForm.getTerm() + " and " + reportInInfoForm.getTimes() + "");
			}
			
			/** ���ϱ���鿴Ȩ�� */
			if (operator == null)
				return retvals;
			childRepSearchPopedom = operator.getChildRepSearchPopedom();
			if (operator.isSuperManager() == false){
				if (childRepSearchPopedom == null || childRepSearchPopedom.equals(""))
					return retvals;				
			}
			
			//�������Ƶ��ж�
			if (reportInInfoForm.getRepName() != null && !reportInInfoForm.getRepName().equals("")){
				where.append(" and ri.repName like '%" + reportInInfoForm.getRepName() + "%'");
			}
			
			/** ȥ�����е�Ȩ�� */
			where.append(" and ri.orgId not in ('" + operator.getOrgId() + "')");
			where.append(" and ri.MChildReport.comp_id.childRepId in(select distinct mrr.MChildReport.comp_id.childRepId from MRepRange mrr where mrr.MChildReport.isPublic = 1)");
			hql.append(where.toString() + " order by ri.year,ri.term,ri.orgId");

		//	conn = new DBConn();
		//	session = conn.openSession();

			Query query = session.createQuery(hql.toString());
//			query.setFirstResult(offset).setMaxResults(limit);
			List list = query.list();
			
			if(operator.isSuperManager() == false){
				String childRepId = null;
				if (list != null && list.size() > 0){
					retvals = new ArrayList();
					for (Iterator it = list.iterator(); it.hasNext();){						
						ReportIn reportInPersistence = (ReportIn) it.next();
						childRepId = reportInPersistence.getMChildReport().getComp_id().getChildRepId();
						if(childRepSearchPopedom.indexOf(reportInPersistence.getOrgId()+childRepId) <= -1)
							continue;

						Aditing aditing = new Aditing();
						aditing.setCheckFlag(reportInPersistence.getCheckFlag());
						OrgNetForm orgNetForm = StrutsMOrgDelegate.selectOne(reportInPersistence.getOrgId());
						if (orgNetForm != null && orgNetForm.getOrg_name() != null)
							aditing.setOrgName(orgNetForm.getOrg_name());
						aditing.setOrgId(reportInPersistence.getOrgId());
						// �����ҵ��ı���id����aditing����
						aditing.setRepInId(reportInPersistence.getRepInId());
						aditing.setYear(reportInPersistence.getYear());
						aditing.setTerm(reportInPersistence.getTerm());					
						aditing.setRepName(reportInPersistence.getRepName());					
						// �����ҵı�������set��aditing����
						aditing.setReportDate(reportInPersistence.getReportDate());
						// �����ҵ��ӱ���ID�Ž�aditing����
						aditing.setChildRepId(childRepId);
						// �����ҵİ汾��set��aditing����
						aditing.setVersionId(reportInPersistence.getMChildReport().getComp_id().getVersionId());
						aditing.setCheckFlag(reportInPersistence.getCheckFlag());
						aditing.setCurrName(reportInPersistence.getMCurr().getCurName());

						MActuRep mActuRep = StrutsReportInDelegate.GetFreR(reportInPersistence);
						if (mActuRep != null){
							aditing.setDataRgTypeName(mActuRep.getMDataRgType().getDataRgDesc());
							aditing.setActuFreqName(mActuRep.getMRepFreq().getRepFreqName());
						}
						retvals.add(aditing);
					}
				}
			}else{
				if (list != null && list.size() > 0){
					retvals = new ArrayList();
					for (Iterator it = list.iterator(); it.hasNext();){
						Aditing aditing = new Aditing();
						ReportIn reportInPersistence = (ReportIn) it.next();
						
						
						aditing.setCheckFlag(reportInPersistence.getCheckFlag());
						OrgNetForm orgNetForm = StrutsMOrgDelegate.selectOne(reportInPersistence.getOrgId());
						if (orgNetForm != null && orgNetForm.getOrg_name() != null)
							aditing.setOrgName(orgNetForm.getOrg_name());
						aditing.setOrgId(reportInPersistence.getOrgId());
						// �����ҵ��ı���id����aditing����
						aditing.setRepInId(reportInPersistence.getRepInId());
						aditing.setYear(reportInPersistence.getYear());
						aditing.setTerm(reportInPersistence.getTerm());					
						aditing.setRepName(reportInPersistence.getRepName());					
						// �����ҵı�������set��aditing����
						aditing.setReportDate(reportInPersistence.getReportDate());
						// �����ҵ��ӱ���ID�Ž�aditing����
						aditing.setChildRepId(reportInPersistence.getMChildReport().getComp_id().getChildRepId());
						// �����ҵİ汾��set��aditing����
						aditing.setVersionId(reportInPersistence.getMChildReport().getComp_id().getVersionId());
						aditing.setCheckFlag(reportInPersistence.getCheckFlag());
						aditing.setCurrName(reportInPersistence.getMCurr().getCurName());

						MActuRep mActuRep = StrutsReportInDelegate.GetFreR(reportInPersistence);
						if (mActuRep != null){
							aditing.setDataRgTypeName(mActuRep.getMDataRgType().getDataRgDesc());
							aditing.setActuFreqName(mActuRep.getMRepFreq().getRepFreqName());
						}
						retvals.add(aditing);
					}
				}
			}
			
		}catch (HibernateException he){
			retvals = null;
			log.printStackTrace(he);
		}catch (Exception e){
			retvals = null;
			log.printStackTrace(e);
		}finally{
			if (conn != null)
				conn.closeSession();
		}
		return retvals;
	}
	
	/**
	 * ��ѯ�������б���
	 * 
	 * @param reportInInfoForm
	 * @param operator
	 * @param subOrgs
	 * @return
	 * @throws Exception
	 */
	public static List getSubOrgReports(ReportInInfoForm reportInInfoForm, Operator operator) throws Exception{
		List retvals = null;
		DBConn conn = null;
		Session session = null;

		StringBuffer hql = new StringBuffer(
				"from ReportIn ri WHERE "
						+ "ri.times=(select max(r.times) from ReportIn r where r.MChildReport.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and "
						+ "r.MChildReport.comp_id.versionId=ri.MChildReport.comp_id.versionId and r.MDataRgType.dataRangeId=ri.MDataRgType.dataRangeId and r.orgId=ri.orgId and "
						+ "r.MCurr.curId=ri.MCurr.curId and r.year=ri.year and r.term=ri.term and r.checkFlag not in ("
						+ com.fitech.net.config.Config.CHECK_FLAG_AFTERJY + "," + com.fitech.net.config.Config.CHECK_FLAG_UNREPORT + ","
						+ com.fitech.net.config.Config.CHECK_FLAG_AFTERSAVE + "))");

		StringBuffer where = new StringBuffer("");
		where.append("  and ri.checkFlag =" + com.fitech.net.config.Config.CHECK_FLAG_PASS + "   ");
		if (reportInInfoForm == null)
			return retvals;

		try{
			if (reportInInfoForm.getOrgId() != null && !reportInInfoForm.getOrgId().equals("")){
				where.append(" and ri.orgId ='" + reportInInfoForm.getOrgId() + "'");
			}
			if (reportInInfoForm.getYear() != null && reportInInfoForm.getYear().intValue() > 0){
				where.append(" and ri.year=" + reportInInfoForm.getYear());
			}
			if (reportInInfoForm.getTerm() == null || reportInInfoForm.getTerm().intValue() <= 0){
				if (reportInInfoForm.getTimes() != null && reportInInfoForm.getTimes().intValue() > 0)
					where.append(" and ri.term between 1 and " + reportInInfoForm.getTimes() + "");
			}else{
				if (reportInInfoForm.getTimes() == null || reportInInfoForm.getTimes().intValue() <= 0)
					where.append(" and ri.term between " + reportInInfoForm.getTerm() + " and 12");
				else
					where.append(" and ri.term between " + reportInInfoForm.getTerm() + " and " + reportInInfoForm.getTimes() + "");
			}

			// �������Ƶ��ж�
			if (reportInInfoForm.getRepName() != null && !reportInInfoForm.getRepName().equals(""))
				where.append(" and ri.repName like '%" + reportInInfoForm.getRepName() + "%'");

			/** ���ϱ���鿴Ȩ�� */
			if (operator == null)
				return retvals;
			if (operator.isSuperManager() == false){
				if (operator.getChildRepSearchPopedom() == null || operator.getChildRepSearchPopedom().equals(""))
					return retvals;
				/**���������ݿ��ж�*/
				if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
					where.append(" and rtrim(ltrim(ri.orgId)) || ri.MChildReport.comp_id.childRepId in (" + operator.getChildRepSearchPopedom() + ")");
				if(Config.DB_SERVER_TYPE.equals("sqlserver"))
					where.append(" and rtrim(ltrim(ri.orgId)) + ri.MChildReport.comp_id.childRepId in (" + operator.getChildRepSearchPopedom() + ")");
			}
			/** ȥ�����е�Ȩ�� */
			where.append(" and ri.orgId not in ('" + operator.getOrgId() + "')");
			hql.append(where.toString() + " order by ri.year,ri.term,ri.orgId");

			conn = new DBConn();
			session = conn.openSession();

			Query query = session.createQuery(hql.toString());
			List list = query.list();

			if (list != null && list.size() > 0){
				retvals = new ArrayList();
				// ѭ����ȡ���ݿ����������¼
				for (Iterator it = list.iterator(); it.hasNext();){
					Aditing aditing = new Aditing();
					ReportIn reportInPersistence = (ReportIn) it.next();
					aditing.setCheckFlag(reportInPersistence.getCheckFlag());
					OrgNetForm orgNetForm = StrutsMOrgDelegate.selectOne(reportInPersistence.getOrgId());
					if (orgNetForm != null && orgNetForm.getOrg_name() != null)
						aditing.setOrgName(orgNetForm.getOrg_name());
					aditing.setOrgId(reportInPersistence.getOrgId());
					// �����ҵ��ı���id����aditing����
					aditing.setRepInId(reportInPersistence.getRepInId());
					aditing.setYear(reportInPersistence.getYear());
					aditing.setTerm(reportInPersistence.getTerm());
					// �����ҵı�������set��adtiting����
					aditing.setRepName(reportInPersistence.getRepName());
					// �����ҵı�������set��aditing����
					aditing.setReportDate(reportInPersistence.getReportDate());
					// �����ҵ��ӱ���ID�Ž�aditing����
					aditing.setChildRepId(reportInPersistence.getMChildReport().getComp_id().getChildRepId());
					// �����ҵİ汾��set��aditing����
					aditing.setVersionId(reportInPersistence.getMChildReport().getComp_id().getVersionId());
					aditing.setCheckFlag(reportInPersistence.getCheckFlag());
					aditing.setCurrName(reportInPersistence.getMCurr().getCurId().toString());
					MActuRep mActuRep = StrutsReportInDelegate.GetFreR(reportInPersistence);
					if (mActuRep != null){
						aditing.setDataRgId(mActuRep.getMDataRgType().getDataRangeId());
						aditing.setDataRgTypeName(mActuRep.getMDataRgType().getDataRgDesc());
						aditing.setActuFreqName(mActuRep.getMRepFreq().getRepFreqName());
					}
					retvals.add(aditing);
				}
			}
		}catch (HibernateException he){
			retvals = null;
			log.printStackTrace(he);
		}catch (Exception e){
			retvals = null;
			log.printStackTrace(e);
		}finally{
			if (conn != null)
				conn.closeSession();
		}
		return retvals;
	}

	public static CollectReport reportToCollect(Integer reportID){
		DBConn conn = null;
		Session session = null;

		if (reportID != null){
			try{
				conn = new DBConn();
				session = conn.openSession();
				String sql = "from ReportIn rep where rep.repInId = " + reportID;
				Query query = session.createQuery(sql);
				List list = query.list();
				ReportIn report = null;
				if (list != null)
					report = (ReportIn) list.get(0);
				else
					return null;
				CollectReport collect = null;
				sql = "from CollectReport col where col.id.orgId=" + report.getOrgId() + " and col.id.childRepId='"
						+ report.getMChildReport().getComp_id().getChildRepId() + "' and col.id.versionId="
						+ report.getMChildReport().getComp_id().getVersionId() + " and col.id.year=" + report.getYear() + " and " + " col.id.term="
						+ report.getTerm();
				Query colQuery = session.createQuery(sql);
				List col = colQuery.list();
				if (col != null && col.size() > 0)
					collect = (CollectReport) col.get(0);
				else
					return null;
				conn.closeSession();

				return collect;
			}catch (Exception e){
				e.printStackTrace();
				return null;
			}
		}else
			return null;
	}
	
	/**
	 * JDBC���� ����� ���Ը�	2011-12-21
	 * �õ���Ӧ���ܵı���
	 * @param reportID
	 * @return
	 */
	public static ReportInForm getReportToCollect(Integer reportID){
		ReportInForm reportInForm = null;
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		if (reportID != null){
			try{
				String hhql = "select rep.rep_in_id,rep.child_rep_id,rep.version_id,rep.cur_id,rep.data_range_id from report_in rep left join (select t.term,t.child_rep_id," +
							  "t.org_id,t.version_id,t.year,t.times,t.cur_id,t.data_range_id from report_in t where t.rep_in_id ="+reportID+") b on rep.term=b.term where " +
							  "rep.term=b.term and rep.child_rep_id=b.child_rep_id and rep.org_id=b.org_id and rep.data_range_id=b.data_range_id " +
							  "and rep.cur_id=b.cur_id and rep.version_id=b.version_id and rep.year=b.year and rep.times=-1";
				
				
				con = new com.cbrc.smis.proc.jdbc.FitechConnection().getConnect();
				stmt = con.prepareStatement(hhql.toUpperCase());
				rs = stmt.executeQuery();
				
				if (rs != null){
					if (rs.next()){
						reportInForm = new ReportInForm();
						int repInId = rs.getInt(1);
						String childRepId = rs.getString(2);
						String versionId = rs.getString(3);
						
						reportInForm.setRepInId(new Integer(repInId));
						reportInForm.setChildRepId(childRepId);
						reportInForm.setVersionId(versionId);
					}
				}
			}catch (Exception e){
				reportInForm = null;
				log.printStackTrace(e);
				try{
					con.close();
				}catch (SQLException e1){
					// TODO �Զ����� catch ��
					e1.printStackTrace();
				}
			}finally{
				try{
					if (con != null) con.close();
				}catch (SQLException e){
					// TODO �Զ����� catch ��
					e.printStackTrace();
				}
			}
		}
		return reportInForm;
	}

	
	/**
	 * ���㱨����쳣  ��������ֵ��,���뵽���ݿ���
	 * 
	 * @param repInId ʵ�����ݱ���ID
	 * @return void
	 */
	public static boolean  calculateYCBH(Aditing aditing) throws SQLException,Exception{
		final  int SCALE=4;
		Connection conn = null;
		boolean result=false;
		//�쳣�仯��־  -1 û���쳣�仯, 1 û������ֵ  2.û������ͬ��ֵ,  0,����
		try{
			conn=new com.cbrc.smis.proc.jdbc.FitechConnection().getConnect();
	
			if(aditing	==null) return result;
			int repInId=aditing.getRepInId().intValue();
			com.cbrc.smis.proc.po.ReportIn reportIn=ReportImpl.getReportIn(conn,repInId);
			/**��������**/
	
				List acList=ReportImpl.getAbnormityChangeList(conn,
						aditing.getChildRepId(),
						aditing.getVersionId(),
						aditing.getOrgId(),
						Report.REPORT_STYLE_DD); 
				
				if(acList!=null && acList.size()>0){
					com.cbrc.smis.proc.po.AbnormityChange ac=null;
					double cellValue=0,prevCellValue=0,sameCellValue=0;
					float prevPercent=0,samePercent=0;
					/** �������� �½�*/ 
					Float prevRiseStandard,prevFallStandard;
					/** ����ͬ������ �½�*/ 
					Float sameRiseStandard,sameFallStandard;
					int abnormityChangeFlag=com.cbrc.smis.proc.impl.Report.ABNORMITY_CHANGE_FLAG_OK;
					String repFreqNm=aditing.getActuFreqName();   //�����Ƶ��
				//	repFreqNm=ReportImpl.getRepActuName(conn,repInId);
					boolean prevValue = false;
					boolean sameValue = false;
					for(int i=0;i<acList.size();i++){
						ac=(com.cbrc.smis.proc.po.AbnormityChange)acList.get(i);
						// ����ֵ
						cellValue=ReportDDImpl.getCellValue(conn,repInId,ac.getCellId());
						Double pvalue=ReportDDImpl.getPrevTermCellValue(conn,reportIn,ac.getCellId(),repFreqNm);
						if(pvalue!=null){
							prevCellValue=pvalue.doubleValue();
							prevValue = true;
						}
						Double sValue =ReportDDImpl.getLastYearSameTermCellValue(conn,reportIn,ac.getCellId(),repFreqNm);
						if(sValue != null){
							sameCellValue=sValue.doubleValue();
							sameValue = true;
						}
						
						//// System.out.println(cellValue + "\t" + prevCellValue + "\t" + sameCellValue);
						/**���������ݵıȽ�**/
						if(prevValue){
							if(cellValue!=prevCellValue){
								prevPercent=prevCellValue==0?200:Expression.round(cellValue,prevCellValue,SCALE)*100;
								if(prevPercent>100){  //����
									if((prevPercent/100)>ac.getPrevRiseStandard()){
										prevRiseStandard=new Float(Expression.round(prevPercent,100,2));
										abnormityChangeFlag=Report.ABNORMITY_CHANGE_FLAG_NO;
									}else{
										prevRiseStandard=new Float(Expression.round(prevPercent,100,2));
									}
									prevFallStandard=null;
								}else{  //�½�
									prevRiseStandard=null;							
									if((prevPercent/100)<ac.getPrevFallStandard()){
										prevFallStandard=new Float(Expression.round(prevPercent,100,2));
										abnormityChangeFlag=Report.ABNORMITY_CHANGE_FLAG_NO;
									}else{
										prevFallStandard=new Float(Expression.round(prevPercent,100,2));
									}
								}
							}else{  //û���쳣�仯
								prevRiseStandard=null;
								prevFallStandard=null;
							}
						}else{ // û������ֵ
							prevRiseStandard = null ;
							prevFallStandard = null ;
						}
						/**������ͬ�ڵıȽ�**/
						if(sameValue){
							if(cellValue!=sameCellValue){
								samePercent=sameCellValue==0?200:Expression.round(cellValue,sameCellValue,SCALE)*100;						
								if(samePercent>100){  //����
									if(samePercent/100>ac.getSameRiseStandard()){
										sameRiseStandard=new Float(Expression.round(samePercent,100,2));
										abnormityChangeFlag=Report.ABNORMITY_CHANGE_FLAG_NO;
									}else{
										sameRiseStandard=new Float(Expression.round(samePercent,100,2));
									}
									sameFallStandard=null;
								}else{ //�½�
									sameRiseStandard=null;
									if(samePercent/100<ac.getSameFallStandard()){
										sameFallStandard=new Float(Expression.round(samePercent,100,2));
										abnormityChangeFlag=Report.ABNORMITY_CHANGE_FLAG_NO;
									}else{
										sameFallStandard=new Float(Expression.round(samePercent,100,2));
									}
								}
							}else{ //û���쳣�仯
								sameRiseStandard=null;
								sameFallStandard=null;
							}
						}else{
							sameRiseStandard =null;
							sameFallStandard = null;
						}
						/**���µ�Ԫ����쳣��Ϣ**/
						if(abnormityChangeFlag==Report.ABNORMITY_CHANGE_FLAG_NO){
							ReportDDImpl.updateValue(conn,repInId,ac.getCellId(),prevRiseStandard,prevFallStandard,sameRiseStandard,sameFallStandard);
							result=true;
						}
					}
					/**����ʵ�����ݱ���쳣�仯״̬��ʶ**/
					if(ReportImpl.updateFlag(conn,repInId,"Abmormity_Change_Flag",abnormityChangeFlag)==true){
						//LogInImpl.writeLog(conn,"���㱨��[Rep_In_Id:" + repInId + "]�쳣�仯�ɹ�!");
					}else{
						LogInImpl.writeLog(conn,"���㱨��[Rep_In_Id:" + repInId + "]�쳣�仯ʧ��!");
					}
				}
			conn.commit();
		}
		catch (Exception e)
		{
			log.printStackTrace(e);
			try
			{
				conn.close();
			}
			catch (SQLException e1)
			{
				// TODO �Զ����� catch ��
				e1.printStackTrace();
			}
			
		}
		finally
		{
			if (conn != null)
				try
				{
					conn.close();
				}
				catch (SQLException e)
				{
					// TODO �Զ����� catch ��
					e.printStackTrace();
				}
		}
		return result;
	}
	
	/**
	 * ����鿴�ܼ�¼������Ϣ��ѯ�˵��£�
	 * 
	 * @param reportInInfoForm ����formBean
	 * @param operator ��ǰ��¼�û�
	 * @return int ��¼��
	 */
	public static int searchReportCount(ReportInInfoForm reportInInfoForm, Operator operator){
		int count = 0;
		DBConn conn = null;
		Session session = null;		

		try{
			if (reportInInfoForm == null || operator == null)
				return count;
			
			conn = new DBConn();
			session = conn.openSession();

			//��ѯ����HQL������
			StringBuffer hql = new StringBuffer(
					"select count(*) from ReportIn ri where "
							+ "ri.times=(select max(r.times) from ReportIn r where r.MChildReport.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and "
							+ "r.MChildReport.comp_id.versionId=ri.MChildReport.comp_id.versionId and r.MDataRgType.dataRangeId=ri.MDataRgType.dataRangeId " 
							+ "and r.orgId=ri.orgId and r.MCurr.curId=ri.MCurr.curId and r.year=ri.year and r.term=ri.term and r.checkFlag=ri.checkFlag) " 
							+ "and ri.checkFlag in (" + Config.CHECK_FLAG_OK +"," + Config.CHECK_FLAG_NO +"," + Config.CHECK_FLAG_UN + ")");			

			StringBuffer where = new StringBuffer("");
			
			/**��ӱ����Ų�ѯ���������Դ�Сдģ����ѯ��*/
			if(reportInInfoForm.getChildRepId() != null && !reportInInfoForm.getChildRepId().equals("")){				
				where.append(" and upper(ri.MChildReport.comp_id.childRepId) like upper('%" 
						+ reportInInfoForm.getChildRepId().trim() + "%')");
			}
			/**��ӱ������Ʋ�ѯ������ģ����ѯ��*/
			if(reportInInfoForm.getRepName() != null && !reportInInfoForm.getRepName().equals("")){
				where.append(" and ri.repName like '%" + reportInInfoForm.getRepName().trim() + "%'");
			}
			/**���ģ�����ͣ�ȫ��/����/��֧����ѯ����*/
			if(reportInInfoForm.getFrOrFzType() != null && !reportInInfoForm.getFrOrFzType().equals(Config.DEFAULT_VALUE)){
				where.append(" and ri.MChildReport.frOrFzType='" + reportInInfoForm.getFrOrFzType() + "'");
			}
			/**��ӱ���Ƶ�ȣ���/��/����/�꣩��ѯ����*/
			if(reportInInfoForm.getRepFreqId() != null && !String.valueOf(reportInInfoForm.getRepFreqId()).equals(Config.DEFAULT_VALUE)){
				where.append(" and ri.MDataRgType.dataRangeId in (select M.comp_id.dataRangeId from MActuRep M " 
						+ "where M.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and M.comp_id.versionId=ri.MChildReport.comp_id.versionId " 
						+ "and M.comp_id.repFreqId=" + reportInInfoForm.getRepFreqId() + ")");
			}
			/**������ڣ���ݣ���ѯ����*/
			if(reportInInfoForm.getYear() != null){
				where.append(" and ri.year=" + reportInInfoForm.getYear());
			}
			/**������ڣ��·ݣ���ѯ����*/
			if(reportInInfoForm.getTerm() != null){
				where.append(" and ri.term=" + reportInInfoForm.getTerm());
			}
			/**��ӻ�����ѯ����*/
			if(reportInInfoForm.getOrgId() != null && !reportInInfoForm.getOrgId().equals(Config.DEFAULT_VALUE)){
				where.append(" and ri.orgId='" + reportInInfoForm.getOrgId().trim() + "'");
			}
			/**��ӱ���״̬��δ���/���ͨ��/���δͨ������ѯ����*/
			if(reportInInfoForm.getCheckFlag() != null && !String.valueOf(reportInInfoForm.getCheckFlag()).equals(Config.DEFAULT_VALUE)){
				where.append(" and ri.checkFlag=" + reportInInfoForm.getCheckFlag());
			}				

			/**��ӱ���鿴Ȩ�ޣ������û�������ӣ�*/
			if (operator.isSuperManager() == false){
				if (operator.getChildRepSearchPopedom() == null || operator.getChildRepSearchPopedom().equals(""))
					return count;
				/**���������ݿ��ж�*/
				if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
					where.append(" and ltrim(rtrim(ri.orgId)) || ri.MChildReport.comp_id.childRepId in(" + operator.getChildRepSearchPopedom() + ")");
				if(Config.DB_SERVER_TYPE.equals("sqlserver"))
					where.append(" and ltrim(rtrim(ri.orgId)) + ri.MChildReport.comp_id.childRepId in(" + operator.getChildRepSearchPopedom() + ")");
			}
			hql.append(where.toString());
			
			conn = new DBConn();
			session = conn.openSession();
			List list = session.find(hql.toString());
			if (list != null && list.size() != 0) 
				count = ((Integer) list.get(0)).intValue();
		} catch (HibernateException he){
			count = 0;
			log.printStackTrace(he);
		} catch (Exception e){
			count = 0;
			log.printStackTrace(e);
		} finally {
			//�ر�����
			if (conn != null)
				conn.closeSession();
		}
		return count;
	}
	
	/**
	 * ����鿴��¼��Ϣ����Ϣ��ѯ�˵��£�
	 * 
	 * @param reportInInfoForm ����formBean
	 * @param offset ��¼��ʼλ��
	 * @param limit ��Ҫ��ȡ��¼��
	 * @param operator ��ǰ��¼�û�
	 * @return List �����ѯ�����
	 */
	public static List searchReportRecord(ReportInInfoForm reportInInfoForm, int offset, int limit, Operator operator){
		List resList = null;
		DBConn conn = null;
		Session session = null;

		try{
			if (reportInInfoForm == null || operator == null)
				return resList;
			
			conn = new DBConn();
			session = conn.openSession();
			
			//��ѯ����HQL������
			StringBuffer hql = new StringBuffer(
					"from ReportIn ri where "
							+ "ri.times=1 " 
							+ "and ri.checkFlag in (" + Config.CHECK_FLAG_OK +"," + Config.CHECK_FLAG_NO +"," + Config.CHECK_FLAG_UN + ")");

			StringBuffer where = new StringBuffer("");
			
			/**��ӱ����Ų�ѯ���������Դ�Сдģ����ѯ��*/
			if(reportInInfoForm.getChildRepId() != null && !reportInInfoForm.getChildRepId().equals("")){				
				where.append(" and upper(ri.MChildReport.comp_id.childRepId) like upper('%" 
						+ reportInInfoForm.getChildRepId().trim() + "%')");
			}
			/**��ӱ������Ʋ�ѯ������ģ����ѯ��*/
			if(reportInInfoForm.getRepName() != null && !reportInInfoForm.getRepName().equals("")){
				where.append(" and ri.repName like '%" + reportInInfoForm.getRepName().trim() + "%'");
			}
			/**���ģ�����ͣ�ȫ��/����/��֧����ѯ����*/
			if(reportInInfoForm.getFrOrFzType() != null && !reportInInfoForm.getFrOrFzType().equals(Config.DEFAULT_VALUE)){
				where.append(" and ri.MChildReport.frOrFzType='" + reportInInfoForm.getFrOrFzType() + "'");
			}
			/**��ӱ���Ƶ�ȣ���/��/����/�꣩��ѯ����*/
			if(reportInInfoForm.getRepFreqId() != null && !String.valueOf(reportInInfoForm.getRepFreqId()).equals(Config.DEFAULT_VALUE)){
				where.append(" and ri.MDataRgType.dataRangeId in (select M.comp_id.dataRangeId from MActuRep M " 
						+ "where M.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and M.comp_id.versionId=ri.MChildReport.comp_id.versionId " 
						+ "and M.comp_id.repFreqId=" + reportInInfoForm.getRepFreqId() + ")");
			}
			/**������ڣ���ݣ���ѯ����*/
			if(reportInInfoForm.getYear() != null){
				where.append(" and ri.year=" + reportInInfoForm.getYear());
			}
			/**������ڣ��·ݣ���ѯ����*/
			if(reportInInfoForm.getTerm() != null){
				where.append(" and ri.term=" + reportInInfoForm.getTerm());
			}
			/**��ӻ�����ѯ����*/
			if(reportInInfoForm.getOrgId() != null && !reportInInfoForm.getOrgId().equals("") && !reportInInfoForm.getOrgId().equals(Config.DEFAULT_VALUE)){
				where.append(" and ri.orgId='" + reportInInfoForm.getOrgId().trim() + "'");
			}
			/**��ӱ���״̬��δ���/���ͨ��/���δͨ������ѯ����*/
			if(reportInInfoForm.getCheckFlag() != null && !String.valueOf(reportInInfoForm.getCheckFlag()).equals(Config.DEFAULT_VALUE)){
				where.append(" and ri.checkFlag=" + reportInInfoForm.getCheckFlag());
			}

			/**��ӱ���鿴Ȩ�ޣ������û�������ӣ�*/			
			if (operator.isSuperManager() == false){
				if (operator.getChildRepSearchPopedom() == null || operator.getChildRepSearchPopedom().equals(""))
					return resList;
				/**���������ݿ��ж�*/
				if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
					where.append(" and ltrim(rtrim(ri.orgId)) || ri.MChildReport.comp_id.childRepId in(" + operator.getChildRepSearchPopedom() + ")");
				if(Config.DB_SERVER_TYPE.equals("sqlserver"))
					where.append(" and ltrim(rtrim(ri.orgId)) + ri.MChildReport.comp_id.childRepId in(" + operator.getChildRepSearchPopedom() + ")");
			}			
			hql.append(where.toString() + " order by ri.year,ri.term,ri.orgId");
			
			conn = new DBConn();
			session = conn.openSession();

			Query query = session.createQuery(hql.toString());
			query.setFirstResult(offset).setMaxResults(limit);
			List list = query.list();

			if (list != null){
				resList = new ArrayList();
				ReportIn reportInRecord = null;
				Aditing aditing = null;
				OrgNet orgNet = null;				
				for (Iterator it = list.iterator(); it.hasNext();){					
					aditing = new Aditing();
					reportInRecord = (ReportIn) it.next();
					
					//���ñ������״̬
					aditing.setCheckFlag(reportInRecord.getCheckFlag());
					//���ñ��ͻ���������
					aditing.setOrgId(reportInRecord.getOrgId());
					orgNet = StrutsOrgNetDelegate.selectOne(reportInRecord.getOrgId());
					if(orgNet != null) aditing.setOrgName(orgNet.getOrgName());
					//���ñ���ID��ʶ��
					aditing.setRepInId(reportInRecord.getRepInId());
					//���ñ������
					aditing.setYear(reportInRecord.getYear());
					//���ñ�������
					aditing.setTerm(reportInRecord.getTerm());
					//���ñ�������
					aditing.setRepName(reportInRecord.getRepName());
					//���ñ���������
					aditing.setReportDate(reportInRecord.getReportDate());
					//���ñ�����
					aditing.setChildRepId(reportInRecord.getMChildReport().getComp_id().getChildRepId());
					//���ñ���汾��
					aditing.setVersionId(reportInRecord.getMChildReport().getComp_id().getVersionId());
					//���ñ����������
					aditing.setCurrName(reportInRecord.getMCurr().getCurName());
					MActuRep mActuRep = StrutsReportInDelegate.GetFreR(reportInRecord);
					if (mActuRep != null){
						//���ñ��Ϳھ�
						aditing.setDataRgTypeName(mActuRep.getMDataRgType().getDataRgDesc());
						//���ñ���Ƶ��
						aditing.setActuFreqName(mActuRep.getMRepFreq().getRepFreqName());
					}
					resList.add(aditing);
				}
			}
		} catch (HibernateException he){
			resList = null;
			log.printStackTrace(he);
		} catch (Exception e){
			resList = null;
			log.printStackTrace(e);
		} finally{
			//�ر����ݿ�����
			if (conn != null)
				conn.closeSession();
		}
		return resList;
	}
	
	/**
	 * ������oracle�﷨ �����޸� ���Ը� 2011-12-22
	 * ��ʹ��hibernate
	 * Ӱ�����ReportIn
	 * ����鿴��¼��Ϣ����Ϣ��ѯ�˵��£�
	 * 
	 * @param reportInInfoForm ����formBean
	 * @param operator ��ǰ��¼�û�
	 * @return List �����ѯ�����
	 */
	public static List searchReportRecord(ReportInInfoForm reportInInfoForm, Operator operator){
		List resList = null;
		DBConn conn = null;
		Session session = null;

		try{
			if (reportInInfoForm == null || operator == null)
				return resList;
			
			conn = new DBConn();
			session = conn.openSession();
			
			//��ѯ����HQL������
			StringBuffer hql = new StringBuffer(
					"from ReportIn ri where "
							+ "ri.times=(select max(r.times) from ReportIn r where r.MChildReport.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and "
							+ "r.MChildReport.comp_id.versionId=ri.MChildReport.comp_id.versionId and r.MDataRgType.dataRangeId=ri.MDataRgType.dataRangeId " 
							+ "and r.orgId=ri.orgId and r.MCurr.curId=ri.MCurr.curId and r.year=ri.year and r.term=ri.term and r.checkFlag=ri.checkFlag) " 
							+ "and ri.checkFlag in (" + Config.CHECK_FLAG_OK +"," + Config.CHECK_FLAG_NO +"," + Config.CHECK_FLAG_UN + ")");

			StringBuffer where = new StringBuffer("");
			
			/**��ӱ����Ų�ѯ���������Դ�Сдģ����ѯ��*/
			if(reportInInfoForm.getChildRepId() != null && !reportInInfoForm.getChildRepId().equals("")){				
				where.append(" and upper(ri.MChildReport.comp_id.childRepId) like upper('%" 
						+ reportInInfoForm.getChildRepId().trim() + "%')");
			}
			/**��ӱ������Ʋ�ѯ������ģ����ѯ��*/
			if(reportInInfoForm.getRepName() != null && !reportInInfoForm.getRepName().equals("")){
				where.append(" and ri.repName like '%" + reportInInfoForm.getRepName().trim() + "%'");
			}
			/**���ģ�����ͣ�ȫ��/����/��֧����ѯ����*/
			if(reportInInfoForm.getFrOrFzType() != null && !reportInInfoForm.getFrOrFzType().equals("") && !reportInInfoForm.getFrOrFzType().equals(Config.DEFAULT_VALUE)){
				where.append(" and ri.MChildReport.frOrFzType='" + reportInInfoForm.getFrOrFzType() + "'");
			}
			/**��ӱ���Ƶ�ȣ���/��/����/�꣩��ѯ����*/
			if(reportInInfoForm.getRepFreqId() != null && !reportInInfoForm.getRepFreqId().equals("") && !String.valueOf(reportInInfoForm.getRepFreqId()).equals(Config.DEFAULT_VALUE)){
				where.append(" and ri.MDataRgType.dataRangeId in (select M.comp_id.dataRangeId from MActuRep M " 
						+ "where M.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and M.comp_id.versionId=ri.MChildReport.comp_id.versionId " 
						+ "and M.comp_id.repFreqId=" + reportInInfoForm.getRepFreqId() + ")");
			}
			/**������ڣ���ݣ���ѯ����*/
			if(reportInInfoForm.getYear() != null && !reportInInfoForm.getYear().equals("")){
				where.append(" and ri.year=" + reportInInfoForm.getYear());
			}
			/**������ڣ��·ݣ���ѯ����*/
			if(reportInInfoForm.getTerm() != null && !reportInInfoForm.getTerm().equals("")){
				where.append(" and ri.term=" + reportInInfoForm.getTerm());
			}
			/**��ӻ�����ѯ����*/
			if(reportInInfoForm.getOrgId() != null && !reportInInfoForm.getOrgId().equals("") && !reportInInfoForm.getOrgId().equals(Config.DEFAULT_VALUE)){
				where.append(" and ri.orgId='" + reportInInfoForm.getOrgId().trim() + "'");
			}
			/**��ӱ���״̬��δ���/���ͨ��/���δͨ������ѯ����*/
			if(reportInInfoForm.getCheckFlag() != null && !reportInInfoForm.getCheckFlag().equals("") && !String.valueOf(reportInInfoForm.getCheckFlag()).equals(Config.DEFAULT_VALUE)){
				where.append(" and ri.checkFlag=" + reportInInfoForm.getCheckFlag());
			}

			/**��ӱ���鿴Ȩ�ޣ������û�������ӣ�*/			
			if (operator.isSuperManager() == false){
				if (operator.getChildRepSearchPopedom() == null || operator.getChildRepSearchPopedom().equals(""))
					return resList;
				/**���������ݿ��ж�*/
				if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
					where.append(" and ltrim(rtrim(ri.orgId)) || ri.MChildReport.comp_id.childRepId in(" + operator.getChildRepSearchPopedom() + ")");
				if(Config.DB_SERVER_TYPE.equals("sqlserver"))
					where.append(" and ltrim(rtrim(ri.orgId)) + ri.MChildReport.comp_id.childRepId in(" + operator.getChildRepSearchPopedom() + ")");
			}			
			hql.append(where.toString());
			
			conn = new DBConn();
			session = conn.openSession();

			Query query = session.createQuery(hql.toString());
			List list = query.list();
			if (list != null && list.size() > 0){
				resList = new ArrayList();
				ReportIn reportInRecord = null;
				ReportInForm reportInFormTemp = null;
				
				for (Iterator it = list.iterator(); it.hasNext();){					
					reportInRecord = (ReportIn) it.next();
					reportInFormTemp = new ReportInForm();
					TranslatorUtil.copyPersistenceToVo(reportInRecord, reportInFormTemp);
					resList.add(reportInFormTemp);
				}
			}
		} catch (HibernateException he){
			resList = null;
			log.printStackTrace(he);
		} catch (Exception e){
			resList = null;
			log.printStackTrace(e);
		} finally{
			//�ر����ݿ�����
			if (conn != null)
				conn.closeSession();
		}
		return resList;
	}
	/**
	 * jdbc���� ������oracle�﷨ ���ܲ���Ҫ�޸�
	 * ���Ը� 2011-12-26
	 * ���� ���ݴ�����reportid������еļ�¼
	 * 
	 * @reportInId
	 * @resList ����һ���������м�¼��list����(���������Ϣ��ϸ��ѯ==����) �쳣�仯�е�ʵ�ʱ仯���(��Ե�ʽ��)
	 */
	public static List getAllNXReportInInfo(Integer reportInId,String reportFlg)
	{
		List retVals = null;
		DBConn conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		if (reportInId != null)
		{
			try
			{
				
				String hhql = null;
				if(reportFlg.equals("2")){
					hhql ="select d.cell_id,d.cell_data,c.col_num,c.row_num from af_pbocreportdata d ,  af_cellinfo c  where d.cell_id=c.cell_id and d.rep_id=? and d.cell_data is not null";
				}else{
					hhql ="select d.cell_id,d.cell_data,c.col_num,c.row_num from af_otherreportdata d , af_cellinfo c  where d.cell_id=c.cell_id and d.rep_id=? and d.cell_data is not null";
				}
				conn = new DBConn();
				Session session = conn.openSession();

				stmt = session.connection().prepareStatement(hhql.toUpperCase());
				stmt.setInt(1, reportInId.intValue());

				rs = stmt.executeQuery();
				if (rs != null)
				{
					retVals = new ArrayList();
					while (rs.next())
					{
						int cellId=rs.getInt(1);
						String reportValue = rs.getString(2);
						String colId = rs.getString(3);
						int rowId = rs.getInt(4);

						ReportInInfoForm reportInInfoForm = new ReportInInfoForm();
						reportInInfoForm.setCellId(new Integer(cellId));
						reportInInfoForm.setReportValue(reportValue);
						reportInInfoForm.setColId(colId);
						reportInInfoForm.setRowId(new Integer(rowId));
						retVals.add(reportInInfoForm);

					}
				}
			}
			catch (Exception e)
			{
				log.printStackTrace(e);
			
			}
			finally
			{
				if (conn != null)
					conn.closeSession();
			}
		}
		return retVals;
	}

}