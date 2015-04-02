package com.fitech.gznx.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.apache.commons.beanutils.BeanUtils;

import com.cbrc.smis.adapter.StrutsMChildReportDelegate;
import com.cbrc.smis.adapter.StrutsMCurrDelegate;
import com.cbrc.smis.adapter.StrutsMRepFreqDelegate;
import com.cbrc.smis.adapter.StrutsMRepRangeDelegate;
import com.cbrc.smis.adapter.StrutsReportInDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.hibernate.MChildReport;
import com.cbrc.smis.hibernate.MChildReportPK;
import com.cbrc.smis.hibernate.MCurr;
import com.cbrc.smis.hibernate.MDataRgType;
import com.cbrc.smis.hibernate.MRepFreq;
import com.cbrc.smis.hibernate.ReportIn;
import com.cbrc.smis.jdbc.FitechConnection;
import com.cbrc.smis.other.Aditing;
import com.cbrc.smis.proc.impl.Report;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.gznx.common.DateUtil;
import com.fitech.gznx.common.PageListInfo;
import com.fitech.gznx.common.StringUtil;
import com.fitech.gznx.dao.DaoModel;
import com.fitech.gznx.form.AFPBOCReportForm;
import com.fitech.gznx.form.AFReportForm;
import com.fitech.gznx.po.AfOrg;
import com.fitech.gznx.po.AfReport;
import com.fitech.gznx.po.AfReportAgain;
import com.fitech.gznx.po.AfTemplate;
import com.fitech.gznx.po.AfValidateformula;
import com.fitech.gznx.po.AfViewReport;
import com.fitech.gznx.po.ViewWorkTaskInfo;
import com.fitech.gznx.po.ViewWorkTaskInfoId;

public class AFReportDelegate  extends DaoModel{


	private static FitechException log = new FitechException(
			AFReportDelegate.class);
	private static FitechMessages messages=null;

	/**
	 * ���ݴ������ڻ����Чʱ���ڵı���sql
	 * @param afReportForm
	 * @return sql
	 */
	private static String getValidDateSql(AFReportForm afReportForm){
		
		if(afReportForm.getDate() == null) return null;
		String datesql = "";
		// �����±����������
//		String[] dates = DateUtil.getLastMonth(afReportForm.getDate()).split("-");
		String[] dates = afReportForm.getDate().split("-");

		//int year = Integer.parseInt(dates[0]);
		int term = Integer.parseInt(dates[1]);
		//int day = Integer.parseInt(dates[2]);

		String rep_freq = "";
		if (term == 12)
			rep_freq = com.fitech.gznx.common.Config.FREQ_MONTH
					+ Config.SPLIT_SYMBOL_COMMA
					+ com.fitech.gznx.common.Config.FREQ_SEASON
					+ Config.SPLIT_SYMBOL_COMMA
					+ com.fitech.gznx.common.Config.FREQ_HALFYEAR
					+ Config.SPLIT_SYMBOL_COMMA
					+ com.fitech.gznx.common.Config.FREQ_YEAR
					+ Config.SPLIT_SYMBOL_COMMA
					+  com.fitech.gznx.common.Config.FREQ_DAY;
		else if (term == 6)
			rep_freq = com.fitech.gznx.common.Config.FREQ_MONTH
					+ Config.SPLIT_SYMBOL_COMMA
					+ com.fitech.gznx.common.Config.FREQ_SEASON
					+ Config.SPLIT_SYMBOL_COMMA
					+ com.fitech.gznx.common.Config.FREQ_HALFYEAR
					+ Config.SPLIT_SYMBOL_COMMA
					+  com.fitech.gznx.common.Config.FREQ_DAY;
		else if (term == 3 || term == 9)
			rep_freq = com.fitech.gznx.common.Config.FREQ_MONTH
					+ Config.SPLIT_SYMBOL_COMMA
					+ com.fitech.gznx.common.Config.FREQ_SEASON
					+ Config.SPLIT_SYMBOL_COMMA
					+  com.fitech.gznx.common.Config.FREQ_DAY;
		else if (term == 1)
			rep_freq = com.fitech.gznx.common.Config.FREQ_MONTH
					+ Config.SPLIT_SYMBOL_COMMA
					+ com.fitech.gznx.common.Config.FREQ_YEARBEGAIN
					+ Config.SPLIT_SYMBOL_COMMA
					+  com.fitech.gznx.common.Config.FREQ_DAY;
		else
			rep_freq = com.fitech.gznx.common.Config.FREQ_MONTH.toString()
					+ Config.SPLIT_SYMBOL_COMMA
			+  com.fitech.gznx.common.Config.FREQ_DAY;
		
//		// �ձ���ѯ
//		 datesql = " and (('"
//			+ afReportForm.getDate()
//			+ "' between a.startDate and a.endDate"
//			+ " and a.comp_id.repFreqId in ("
//			+ com.fitech.gznx.common.Config.FREQ_DAY
//			// Ѯ����ѯ
//			+ ")) or ('"
//			+ DateUtil.getLastTenDay(afReportForm.getDate())
//			+ "' between a.startDate and a.endDate"
//			+ " and a.comp_id.repFreqId in ("
//			+ com.fitech.gznx.common.Config.FREQ_TENDAY
//			// �±�������
//			+ ")) or ('"
//			+ DateUtil.getLastMonth(afReportForm.getDate())
//			+ "' between a.startDate and a.endDate"
//			+ " and a.comp_id.repFreqId in (" + rep_freq
//			// �������
//			+ ")))";
		 datesql = " and ('"
				+ afReportForm.getDate()
				+ "' between a.startDate and a.endDate"
				+ " and a.comp_id.repFreqId in ("
				+ com.fitech.gznx.common.Config.FREQ_DAY + Config.SPLIT_SYMBOL_COMMA
				+ com.fitech.gznx.common.Config.FREQ_TENDAY + Config.SPLIT_SYMBOL_COMMA
				+ rep_freq
				+ "))";
		
		
		return datesql;
	}
	
	/**
	 * ���ݴ������ڻ����Чʱ�������еı���sql
	 * @param afReportForm
	 * @return sql
	 */
	private static String getReportDateSql(AFReportForm afReportForm){
		
		if(afReportForm.getDate() == null) return null;
		
		String datesql = "";
		// �����±����������
//		String[] dates = DateUtil.getLastMonth(afReportForm.getDate()).split("-");
		String[] dates = afReportForm.getDate().split("-");

		//int year = Integer.parseInt(dates[0]);
		int term = Integer.parseInt(dates[1]);
		//int day = Integer.parseInt(dates[2]);

		String rep_freq = "";
		if (term == 12)
			rep_freq = com.fitech.gznx.common.Config.FREQ_MONTH
					+ Config.SPLIT_SYMBOL_COMMA
					+ com.fitech.gznx.common.Config.FREQ_SEASON
					+ Config.SPLIT_SYMBOL_COMMA
					+ com.fitech.gznx.common.Config.FREQ_HALFYEAR
					+ Config.SPLIT_SYMBOL_COMMA
					+ com.fitech.gznx.common.Config.FREQ_YEAR;
		else if (term == 6)
			rep_freq = com.fitech.gznx.common.Config.FREQ_MONTH
					+ Config.SPLIT_SYMBOL_COMMA
					+ com.fitech.gznx.common.Config.FREQ_SEASON
					+ Config.SPLIT_SYMBOL_COMMA
					+ com.fitech.gznx.common.Config.FREQ_HALFYEAR;
		else if (term == 3 || term == 9)
			rep_freq = com.fitech.gznx.common.Config.FREQ_MONTH
					+ Config.SPLIT_SYMBOL_COMMA
					+ com.fitech.gznx.common.Config.FREQ_SEASON;
//		else if (term == 1)
//			rep_freq = com.fitech.gznx.common.Config.FREQ_MONTH
//					+ Config.SPLIT_SYMBOL_COMMA
//					+ com.fitech.gznx.common.Config.FREQ_YEARBEGAIN;
		else
			rep_freq = com.fitech.gznx.common.Config.FREQ_MONTH.toString();
		
		String tenday = DateUtil.getFreqDateLast(afReportForm.getDate(),com.fitech.gznx.common.Config.FREQ_TENDAY);
		String termday = DateUtil.getFreqDateLast(afReportForm.getDate(),com.fitech.gznx.common.Config.FREQ_MONTH);
		String yearbegain = DateUtil.getFreqDateLast(afReportForm.getDate(),com.fitech.gznx.common.Config.FREQ_YEARBEGAIN);
		

		datesql = " and ((ri.year=" + Integer.valueOf(dates[0])
					+ " and ri.term=" + Integer.valueOf(dates[1])
					+ " and ri.day=" + Integer.valueOf(dates[2])
					+ " and ri.repFreqId=" + com.fitech.gznx.common.Config.FREQ_DAY 
					+ ") or "
					+ "(ri.year=" + Integer.valueOf(tenday.substring(0, 4))
					+ " and ri.term=" + Integer.valueOf(tenday.substring(5, 7)) 
					+ " and ri.day=" +Integer.valueOf(tenday.substring(8, 10))
					+ " and ri.repFreqId=" + com.fitech.gznx.common.Config.FREQ_TENDAY 
					+ ") or "
					+ "(ri.year=" + Integer.valueOf(termday.substring(0, 4))
					+ " and ri.term=" + Integer.valueOf(termday.substring(5, 7)) 
					+ " and ri.day=" +Integer.valueOf(termday.substring(8, 10))
					+ " and ri.repFreqId in (" + rep_freq + ")" 
					+ ")" ;
				if(term==1){
					datesql += " or "
					+ "(ri.year=" + Integer.valueOf(yearbegain.substring(0, 4))
					+ " and ri.term=" + Integer.valueOf(yearbegain.substring(5, 7)) 
					+ " and ri.day=" +Integer.valueOf(yearbegain.substring(8, 10))
					+ " and ri.repFreqId =" + com.fitech.gznx.common.Config.FREQ_YEARBEGAIN
					+ ")";
				}
				datesql += ")" ;
		return datesql;
	}
	
	/**
	 * ���ݴ������ڻ����Чʱ�������еı���sql
	 * @param afReportForm
	 * @return sql
	 */
	private static String getReportDateTSql(AFReportForm afReportForm){
		
		if(afReportForm.getDate() == null) return null;
		
		String datesql = "";
		// �����±����������
//		String[] dates = DateUtil.getLastMonth(afReportForm.getDate()).split("-");
		String[] dates = afReportForm.getDate().split("-");

		//int year = Integer.parseInt(dates[0]);
		int term = Integer.parseInt(dates[1]);
		//int day = Integer.parseInt(dates[2]);

		String rep_freq = "";
		if (term == 12)
			rep_freq = com.fitech.gznx.common.Config.FREQ_MONTH
					+ Config.SPLIT_SYMBOL_COMMA
					+ com.fitech.gznx.common.Config.FREQ_SEASON
					+ Config.SPLIT_SYMBOL_COMMA
					+ com.fitech.gznx.common.Config.FREQ_HALFYEAR
					+ Config.SPLIT_SYMBOL_COMMA
					+ com.fitech.gznx.common.Config.FREQ_YEAR;
		else if (term == 6)
			rep_freq = com.fitech.gznx.common.Config.FREQ_MONTH
					+ Config.SPLIT_SYMBOL_COMMA
					+ com.fitech.gznx.common.Config.FREQ_SEASON
					+ Config.SPLIT_SYMBOL_COMMA
					+ com.fitech.gznx.common.Config.FREQ_HALFYEAR;
		else if (term == 3 || term == 9)
			rep_freq = com.fitech.gznx.common.Config.FREQ_MONTH
					+ Config.SPLIT_SYMBOL_COMMA
					+ com.fitech.gznx.common.Config.FREQ_SEASON;
//		else if (term == 1)
//			rep_freq = com.fitech.gznx.common.Config.FREQ_MONTH
//					+ Config.SPLIT_SYMBOL_COMMA
//					+ com.fitech.gznx.common.Config.FREQ_YEARBEGAIN;
		else
			rep_freq = com.fitech.gznx.common.Config.FREQ_MONTH.toString();
		
		String tenday = DateUtil.getFreqDateLast(afReportForm.getDate(),com.fitech.gznx.common.Config.FREQ_TENDAY);
		String termday = DateUtil.getFreqDateLast(afReportForm.getDate(),com.fitech.gznx.common.Config.FREQ_MONTH);
		String yearbegain = DateUtil.getFreqDateLast(afReportForm.getDate(),com.fitech.gznx.common.Config.FREQ_YEARBEGAIN);
		

		datesql = " and ((year=" + Integer.valueOf(dates[0])
					+ " and term=" + Integer.valueOf(dates[1])
					+ " and day=" + Integer.valueOf(dates[2])
					+ " and rep_freq_id=" + com.fitech.gznx.common.Config.FREQ_DAY 
					+ ") or "
					+ "(year=" + Integer.valueOf(tenday.substring(0, 4))
					+ " and term=" + Integer.valueOf(tenday.substring(5, 7)) 
					+ " and day=" +Integer.valueOf(tenday.substring(8, 10))
					+ " and rep_freq_id=" + com.fitech.gznx.common.Config.FREQ_TENDAY 
					+ ") or "
					+ "(year=" + Integer.valueOf(termday.substring(0, 4))
					+ " and term=" + Integer.valueOf(termday.substring(5, 7)) 
					+ " and day=" +Integer.valueOf(termday.substring(8, 10))
					+ " and rep_freq_id in (" + rep_freq + ")" 
					+ ")" ;
				if(term==1){
					datesql += " or "
					+ "(year=" + Integer.valueOf(yearbegain.substring(0, 4))
					+ " and term=" + Integer.valueOf(yearbegain.substring(5, 7)) 
					+ " and day=" +Integer.valueOf(yearbegain.substring(8, 10))
					+ " and rep_freq_id =" + com.fitech.gznx.common.Config.FREQ_YEARBEGAIN
					+ ")";
				}
				datesql += ")" ;
		return datesql;
	}
	/***************************************************************************
	 * �������������ͳ��(Ӧ��ͳ��)
	 * 
	 * @param orgId
	 *            ����ID
	 * @param reportInForm
	 *            ����formBean
	 * @author jcm
	 * @return OrgNetForm ����formBean
	 */
	public static int selectOrgReportStatisticsYB(String orgId, String date ,Integer templateType ) {
		int count = 0;
		DBConn conn = null;
		Session session = null;

		if (orgId == null || orgId.equals("") || date == null || date == null)
			return count;

		try {

			AFReportForm afReportForm = new AFReportForm();
			afReportForm.setDate(date);
			
			/** �����������ͳ��SQL(Ӧ��ͳ��) */
			String hql = "select count(*) from AfViewReport a WHERE "
					+ "a.comp_id.orgId='"
					+ orgId
					+ "' and a.isReport="
					+ com.fitech.gznx.common.Config.TEMPLATE_REPORT
					+ " and a.templateType="
					+ templateType
					+ getValidDateSql(afReportForm);

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
	/*public static int selectOrgReportStatisticsFH(String orgId, String date,
			Integer templateType) {
		int count = 0;
		DBConn conn = null;
		Session session = null;

		if (orgId == null || orgId.equals("") || date == null || templateType == null)
			return count;

		try {
			AFReportForm afReportForm = new AFReportForm();
			afReportForm.setDate(date);
			
			*//** �����������ͳ��SQL(���ͱ�����ͨ����) *//*
			String hql = "select count(*) from AfReport ri,AfViewReport a WHERE "
					+ "a.comp_id.templateId=ri.templateId and "
					+ "a.comp_id.versionId=ri.versionId and "
					+ "a.comp_id.orgId=ri.orgId and "
					+ "a.comp_id.repFreqId=ri.repFreqId and "
					+ "a.comp_id.curId=ri.curId and "
					+ "ri.times=(select max(r.times) from AfReport r WHERE "
					+ "r.templateId=ri.templateId and "
					+ "r.versionId=ri.versionId and "
					+ "r.orgId=ri.orgId and r.repFreqId=ri.repFreqId and "
					+ "r.curId=ri.curId and r.year=ri.year and r.term=ri.term and r.day=ri.day "
					+ "and r.checkFlag=ri.checkFlag )"
					+ " and a.isReport="
					+ com.fitech.gznx.common.Config.TEMPLATE_REPORT
					+ " and a.templateType="
					+ templateType
					+ getReportDateSql(afReportForm)
					+ " and ri.orgId='" + orgId
					+ "' and ri.checkFlag in (" +
					// ���ϸ���
					com.fitech.net.config.Config.CHECK_FLAG_AFTERRECHECK + ")";

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
	 * �������������ͳ��(�ѱ���ǩͳ��)
	 * 
	 * @param orgId
	 *            ����ID
	 * @param reportInForm
	 *            ����formBean
	 * @author jcm
	 * @return OrgNetForm ����formBean
	 */
	public static int selectOrgReportStatisticsSQ(String orgId, String date,
			Integer templateType) {
		int count = 0;
		DBConn conn = null;
		Session session = null;

		if (orgId == null || orgId.equals("") || date == null || templateType == null)
			return count;

		try {
			AFReportForm afReportForm = new AFReportForm();
			afReportForm.setDate(date);
			
			/** �����������ͳ��SQL(���ͱ�����˺�ͨ����) */
			String hql = "select count(*) from AfReport ri,AfViewReport a WHERE "
					+ "a.comp_id.templateId=ri.templateId and "
					+ "a.comp_id.versionId=ri.versionId and "
					+ "a.comp_id.orgId=ri.orgId and "
					+ "a.comp_id.repFreqId=ri.repFreqId and "
					+ "a.comp_id.curId=ri.curId and "
					+ "ri.times=(select max(r.times) from AfReport r WHERE "
					+ "r.templateId=ri.templateId and "
					+ "r.versionId=ri.versionId and "
					+ "r.orgId=ri.orgId and r.repFreqId=ri.repFreqId and "
					+ "r.curId=ri.curId and r.year=ri.year and r.term=ri.term and r.day=ri.day "
					+ "and r.checkFlag=ri.checkFlag )"
					+ " and a.isReport="
					+ com.fitech.gznx.common.Config.TEMPLATE_REPORT
					+ " and a.templateType="
					+ templateType
					+ getReportDateSql(afReportForm)
					+ " and ri.orgId='" + orgId
					+ "' and ri.checkFlag in (" +
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
	 * �������������ͳ��(�ѱ�ͳ��)
	 * 
	 * @param orgId
	 *            ����ID
	 * @param reportInForm
	 *            ����formBean
	 * @author jcm
	 * @return OrgNetForm ����formBean
	 */
	public static int selectOrgReportStatisticsBS(String orgId, String date,
			Integer templateType) {
		int count = 0;
		DBConn conn = null;
		Session session = null;

		if (orgId == null || orgId.equals("") || date == null || templateType == null)
			return count;

		try {
			AFReportForm afReportForm = new AFReportForm();
			afReportForm.setDate(date);
			
			/** �����������ͳ��SQL(���ͱ����ѱ��͵�) */
			String hql = "select count(*) from AfReport ri,AfViewReport a WHERE "
					+ "a.comp_id.templateId=ri.templateId and "
					+ "a.comp_id.versionId=ri.versionId and "
					+ "a.comp_id.orgId=ri.orgId and "
					+ "a.comp_id.repFreqId=ri.repFreqId and "
					+ "a.comp_id.curId=ri.curId and "
					+ "ri.times=(select max(r.times) from AfReport r WHERE "
					+ "r.templateId=ri.templateId and "
					+ "r.versionId=ri.versionId and "
					+ "r.orgId=ri.orgId and r.repFreqId=ri.repFreqId and "
					+ "r.curId=ri.curId and r.year=ri.year and r.term=ri.term and r.day=ri.day "
					+ "and r.checkFlag=ri.checkFlag )"
					+ " and a.isReport="
					+ com.fitech.gznx.common.Config.TEMPLATE_REPORT
					+ " and a.templateType="
					+ templateType
					+ getReportDateSql(afReportForm)
					+ " and ri.orgId='" + orgId
					+ "' and ri.checkFlag in ("
					+ com.fitech.net.config.Config.CHECK_FLAG_UNCHECK 
					+ Config.SPLIT_SYMBOL_COMMA + Config.CHECK_FLAG_PASS+")";

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
	 * �������������ͳ��(�ر�ͳ��)
	 * 
	 * @param orgId
	 *            ����ID
	 * @param reportInForm
	 *            ����formBean
	 * @author jcm
	 * @return OrgNetForm ����formBean
	 */
	public static int selectOrgReportStatisticsCB(String orgId, String date,
			Integer templateType) {
		int count = 0;
		DBConn conn = null;
		Session session = null;

		if (orgId == null || orgId.equals("") || date == null || templateType == null)
			return count;

		try {
			AFReportForm afReportForm = new AFReportForm();
			afReportForm.setDate(date);
			
			/** �����������ͳ��SQL(���ͱ����ر���) */
				String hql = "select count(*) from AfReport ri,AfViewReport a WHERE "
					+ "a.comp_id.templateId=ri.templateId and "
					+ "a.comp_id.versionId=ri.versionId and "
					+ "a.comp_id.orgId=ri.orgId and "
					+ "a.comp_id.repFreqId=ri.repFreqId and "
					+ "a.comp_id.curId=ri.curId and "
					+ "ri.times=(select max(r.times) from AfReport r WHERE "
					+ "r.templateId=ri.templateId and "
					+ "r.versionId=ri.versionId and "
					+ "r.orgId=ri.orgId and r.repFreqId=ri.repFreqId and "
					+ "r.curId=ri.curId and r.year=ri.year and r.term=ri.term and r.day=ri.day "
					+ "and r.checkFlag=ri.checkFlag ) "
					+ "and a.isReport="
					+ com.fitech.gznx.common.Config.TEMPLATE_REPORT
					+ " and a.templateType="
					+ templateType
					+ getReportDateSql(afReportForm)
					+ " and ri.orgId='" + orgId
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
	 * ����ʱ�估Ȩ��ȡ�õ���Ӧ�������List
	 * 
	 * @param AFReportForm
	 * @param operator
	 * @return List adting������Ϣ������
	 */
	public static List selectNeedReportList (AFReportForm afReportForm,
			Operator operator) throws Exception{

		List resList = new ArrayList();
		DBConn conn = null;
		Session session = null;

		//Ӧ�������ȴ��뵱ǰ�û�����
		if(afReportForm.getOrgId() == null) afReportForm.setOrgId(operator.getOrgId());
		
		try {
			if (afReportForm != null
					&& afReportForm.getTemplateType() != null) {
				conn = new DBConn();
				session = conn.beginTransaction();

				String hql = "from AfViewReport a where a.templateType='"
						+ afReportForm.getTemplateType()+"'"
						+ getValidDateSql(afReportForm);
//						+ " and a.comp_id.orgId='" + operator.getOrgId() + "'";

				/** ���ϱ���Ȩ�� �����Ȩ��
				 * ���������ݿ��ж�*/
				if (operator.isSuperManager() == false) {
					if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
						hql += " and a.comp_id.orgId||a.comp_id.templateId in ("
							+ operator.getChildRepReportPopedom() + ") and a.comp_id.orgId||a.comp_id.templateId in("
							+operator.getChildRepCheckPodedom()+")";
					if(Config.DB_SERVER_TYPE.equals("sqlserver"))
						hql += " and a.comp_id.orgId+a.comp_id.templateId in ("
							+ operator.getChildRepReportPopedom() + ")  and a.comp_id.orgId+a.comp_id.templateId in("
							+operator.getChildRepCheckPodedom()+")";
				}
				
				/** ����������� */
				if (afReportForm.getTemplateId() != null
						&& !afReportForm.getTemplateId().equals("")) {
					hql += " and a.comp_id.templateId like '%" + afReportForm.getTemplateId()+"%'";
				}

				if (afReportForm.getRepName() != null && !"".equals(afReportForm.getRepName().trim())) {
					hql += " and a.templateName like '%" + afReportForm.getRepName() + "%' ";
				}
				/** ���������� */
				if (afReportForm.getBak1() != null
						&& !afReportForm.getBak1().trim().equals(
								Config.DEFAULT_VALUE)
						&& !afReportForm.getBak1().equals("")) {
					hql += " and a.bak1 in (" + afReportForm.getBak1()+")";
				}
				/** ����������� */
				if (afReportForm.getOrgId() != null
						&& !afReportForm.getOrgId().trim().equals(Config.DEFAULT_VALUE)
						&& !afReportForm.getOrgId().trim().equals("")) {
					hql += " and a.comp_id.orgId in ('" + afReportForm.getOrgId()+"')";
				}
				/**
				 * ��ӱ������β�ѯ����
				 */
				if (afReportForm.getSupplementFlag()!=null&&!"".equals(afReportForm.getSupplementFlag())) {
					int supplement_flag=new Integer(afReportForm.getSupplementFlag()).intValue();
					if(supplement_flag!=-999){
						hql+=" and a.supplementFlag= "+supplement_flag;
					}
				}
				/** ����Ƶ�� */
				if (afReportForm.getRepFreqId() != null
						&& !afReportForm.getRepFreqId().trim().equals(Config.DEFAULT_VALUE)
						&& !afReportForm.getRepFreqId().equals("")) {
					hql += " and a.comp_id.repFreqId =" + afReportForm.getRepFreqId();
				}
				if (afReportForm.getIsReport() != null
						&& !afReportForm.getIsReport().toString().equals(Config.DEFAULT_VALUE)
						&& !afReportForm.getIsReport().equals("")) {
					hql += " and a.isReport=" + afReportForm.getIsReport();
				}else{
					hql += " and a.isReport=" + com.fitech.gznx.common.Config.TEMPLATE_REPORT;
				}
				
						
						
					hql += " order by a.comp_id.templateId, a.comp_id.repFreqId";

				Query query = session.createQuery(hql);
				// query.setFirstResult(offset).setMaxResults(limit);

				List list = query.list();
				for (int i = 0; i < list.size(); i++) {
					// if((i>0&&i<=offset)||i>(offset+limit)) continue;
					AfViewReport viewMReport = (AfViewReport) list.get(i);

					Aditing aditing = new Aditing();
					// aditing.setDataRgTypeName(viewMReport.getDataRgTypeName());
					aditing.setActuFreqName(viewMReport.getRepFreqName());
					aditing.setActuFreqID(viewMReport.getComp_id()
							.getRepFreqId());
					aditing.setTemplateId(viewMReport.getComp_id()
							.getTemplateId());
					aditing.setVersionId(viewMReport.getComp_id()
							.getVersionId());
					aditing.setRepName(viewMReport.getTemplateName());

					if (viewMReport.getComp_id().getRepFreqId() != null) {
						// yyyy-mm-dd ��������ȷ�������ھ������������
						String trueDate = DateUtil.getFreqDateLast(afReportForm
								.getDate(), viewMReport.getComp_id()
								.getRepFreqId());
						aditing.setYear(Integer.valueOf(trueDate
								.substring(0, 4)));
						aditing.setTerm(Integer.valueOf(trueDate
								.substring(5, 7)));
						aditing.setDay(Integer.valueOf(trueDate
								.substring(8, 10)));
					}

					aditing.setCurrName(viewMReport.getCurName());
					aditing.setCurId(viewMReport.getComp_id().getCurId());
					// aditing.setDataRgId(viewMReport.getComp_id().getDataRangeId());

					AfOrg org = (AfOrg) session.get(AfOrg.class, viewMReport
							.getComp_id().getOrgId());
					if(org==null)
						throw new Exception("����Ϊ�գ�������AF_TEMPLATE_ORG_RELATION�����Ƿ�����쳣");
					aditing.setOrgName(org.getOrgName());
					aditing.setOrgId(org.getOrgId());
					
					//  ���������õ�times��ֵ,�����-1���ܹ���
					// 	����Ƿ��Ѿ��ϱ�
					String timesSql="from AfReport ri where  "
					 	+" ri.templateId='"+ aditing.getTemplateId()+"'"
						+" and ri.versionId='"+aditing.getVersionId()+"'"
					//	+" and ri.MDataRgType.dataRangeId="+aditing.getDataRangeId()
						+" and ri.year="+aditing.getYear()
						+" and ri.term="+aditing.getTerm()
						+" and ri.day="+aditing.getDay()
						+" and ri.repFreqId="+aditing.getActuFreqID()
						+" and ri.curId="+aditing.getCurId()
						+" and ri.orgId='"+aditing.getOrgId()+"'"
						+" and ri.times=1"
						+" and ri.checkFlag=" + Config.CHECK_FLAG_PASS;
					List rlist = session.find(timesSql);
					
					if(rlist != null && rlist.size() > 0){
						AfReport reportIn = (AfReport)rlist.get(0);        							
						aditing.setRepInId(reportIn.getRepId().intValue());
						aditing.setIsCollected(new Integer(1));
						
					}else{
						aditing.setIsCollected(new Integer(0));
					}
						
					resList.add(aditing);

				}
			}
		} catch (HibernateException he) {
			log.printStackTrace(he);
			if (conn != null)
				conn.endTransaction(true);
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return resList;
	}

	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-26
	 * Ӱ�����AfViewReport AfReport
	 * <p>
	 * ����:�����Ҫ��������
	 * </p>
	 * <p>
	 * ����:
	 * </p>
	 * <p>
	 * ���ڣ�2008-1-9
	 * </p>
	 * <p>
	 * ���ߣ��ܷ���
	 * </p>
	 */
	public static PageListInfo selectNeedReportRecord(AFReportForm afReportForm,
			Operator operator,int curPage) {
		List result = new ArrayList();
		DBConn conn = null;
		Session session = null;
		PageListInfo pageListInfo = null;
		Transaction tran =null;
		try {

			conn = new DBConn();
			session = conn.openSession();
			tran = session.beginTransaction();
			String hql = "from AfViewReport a where a.templateType='"
					+ afReportForm.getTemplateType()+ "'"
					+ getValidDateSql(afReportForm);
					
					//+ " and a.comp_id.orgId='" + operator.getOrgId() + "'";

			/** ���ϱ���Ȩ�� 
			 * ���������ݿ��ж�*/
			if (operator.isSuperManager() == false) {
				if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
					hql += " and a.comp_id.orgId ||a.comp_id.templateId in ("
						+ operator.getChildRepReportPopedom() + ")";
				if(Config.DB_SERVER_TYPE.equals("sqlserver"))
					hql += " and a.comp_id.orgId +a.comp_id.templateId in ("
						+ operator.getChildRepReportPopedom() + ")";
			}
			String repName = afReportForm.getRepName();
			if (repName != null && !"".equals(repName.trim())) {
				hql += " and a.templateName like '%" + repName + "%' ";
			}
			
			if (afReportForm.getOrgId() != null && !afReportForm.getOrgId().equals("")
					&& !afReportForm.getOrgId().equals(Config.DEFAULT_VALUE)
					&& !afReportForm.getOrgId().trim().equals("")) {
				hql += " and a.comp_id.orgId='" + afReportForm.getOrgId() + "'";
			}

			if(afReportForm.getRepFreqId()!= null && !afReportForm.getRepFreqId().equals("")
					&& !afReportForm.getRepFreqId().equals(Config.DEFAULT_VALUE)) {
				hql += " and a.comp_id.repFreqId=" + afReportForm.getRepFreqId();
			}
			
			/** ��ӱ����Ų�ѯ���������Դ�Сдģ����ѯ�� */
			if (afReportForm.getTemplateId() != null && !afReportForm.getTemplateId().equals("")) {
				hql += " and upper(a.comp_id.templateId) like upper('%"
						+ afReportForm.getTemplateId().trim() + "%')";
			}
			
			/** ���������� */
			if (!StringUtil.isEmpty(afReportForm.getBak1()) && !afReportForm.getBak1().trim().equals(
							Config.DEFAULT_VALUE)) {
				hql += " and a.bak1 in (" + afReportForm.getBak1()+") ";
			}
			/**
			 * ��ӱ������β�ѯ����
			 */
			if (afReportForm.getSupplementFlag()!=null&&!"".equals(afReportForm.getSupplementFlag())) {
				int supplement_flag=new Integer(afReportForm.getSupplementFlag()).intValue();
				if(supplement_flag!=-999){
					hql+=" and a.supplementFlag= "+supplement_flag;
				}
			}

			hql += " and a.isReport="
					+ com.fitech.gznx.common.Config.TEMPLATE_REPORT;
					/**order by �����;ۺϺ�����ͻ �Ѿ�ע�� ���Ը� 2011-12-28
					 * ���������ݿ��ж�*/
			if(Config.DB_SERVER_TYPE.equals("oracle"))
				hql+= " order by a.comp_id.repFreqId,a.comp_id.templateId";
			if(Config.DB_SERVER_TYPE.equals("sqlserver") || Config.DB_SERVER_TYPE.equals("db2"))
				hql+="";
			/**��ʹ��hibernate ���Ը� 2011-12-21**/
			pageListInfo = findByPageWithSQL(session,hql,Config.PER_PAGE_ROWS,curPage);
			List list=pageListInfo.getList();
			
			for (int i = 0; i < list.size(); i++) {
				AfViewReport viewMReport = (AfViewReport) list.get(i);
				Aditing aditing = new Aditing();
				aditing.setTemplateId(viewMReport.getComp_id().getTemplateId());
				aditing.setActuFreqName(viewMReport.getRepFreqName());
				aditing.setActuFreqID(viewMReport.getComp_id().getRepFreqId());
				aditing.setChildRepId(viewMReport.getComp_id().getTemplateId());
				aditing.setVersionId(viewMReport.getComp_id().getVersionId());
				aditing.setRepName(viewMReport.getTemplateName());
				if (viewMReport.getComp_id().getRepFreqId() != null) {
					// yyyy-mm-dd ��������ȷ�������ھ������������
					String trueDate = DateUtil
							.getFreqDateLast(afReportForm.getDate(),
									viewMReport.getComp_id().getRepFreqId());
					aditing.setYear(Integer.valueOf(trueDate.substring(0, 4)));
					aditing.setTerm(Integer.valueOf(trueDate.substring(5, 7)));
					aditing.setDay(Integer.valueOf(trueDate.substring(8, 10)));
				}
				aditing.setCurrName(viewMReport.getCurName());
				aditing.setCurId(viewMReport.getComp_id().getCurId());
				aditing.setOrgId(viewMReport.getComp_id().getOrgId());
				aditing.setOrgName(AFOrgDelegate.getOrgInfo(
						viewMReport.getComp_id().getOrgId()).getOrgName());
				aditing.setReportStyle(viewMReport.getReportStyle());
				
				String sql = "from AfReport ri where ri.times=1 "
						+ " and ri.versionId='" + aditing.getVersionId()
						+ "' and ri.templateId='" + aditing.getChildRepId()
						+ "' and ri.curId=" + aditing.getCurId()
						+ " and ri.year=" + aditing.getYear() + " and ri.term="
						+ aditing.getTerm() + " and ri.day=" + aditing.getDay()
						+ " and ri.orgId='" + aditing.getOrgId()
						+ "' and ri.repFreqId=" + aditing.getActuFreqID();
				List reportInList = session.find(sql);
				if (reportInList != null && reportInList.size() > 0) {
					AfReport report = (AfReport) reportInList.get(0);
					if (report.getCheckFlag() != null
							&& report.getCheckFlag().toString().equals(
									Config.CHECK_FLAG_NO.toString())) {
						Set reportAgainSets = report.getReportAgainSets();
						String cause = "";
						int rasId = 0;
						if (reportAgainSets != null
								&& reportAgainSets.size() > 0) {
							for (Iterator iterSet = reportAgainSets.iterator(); iterSet
									.hasNext();) {
								AfReportAgain againSet = (AfReportAgain) iterSet.next();
								if (againSet.getRasId().intValue() > rasId) {
									cause = againSet.getCause();
									rasId = againSet.getRasId().intValue();
								}
							}
						}
						aditing.setWhy(cause);
					}
					aditing.setCheckFlag(report.getCheckFlag().shortValue());
					aditing.setRepInId(report.getRepId().intValue());
					if (report.getTblInnerValidateFlag() != null)
						aditing.setTblInnerValidateFlag(report
								.getTblInnerValidateFlag().shortValue());
					if (report.getTblOuterValidateFlag() != null)
						aditing.setTblOuterValidateFlag(report
								.getTblOuterValidateFlag().shortValue());
				} else {
					
					aditing.setCheckFlag(Config.CHECK_FLAG_UNREPORT);
					AfReport afReprot = new AfReport();
					BeanUtils.copyProperties(afReprot,aditing);
					afReprot.setTimes(new Long(new String("1")));
					afReprot.setRepFreqId(new Long (aditing.getActuFreqID()));
					afReprot.setReportDate(new Date());
					session.save(afReprot);
//					String hql2 = "select  ri.repId  from AfReport ri where ri.times=1 " 
//						+ " and ri.versionId='" + aditing.getVersionId()
//						+ "' and ri.templateId='" + aditing.getChildRepId()
//						+ "' and ri.year=" + aditing.getYear() 
//						+ " and ri.term="+ aditing.getTerm()
//						+ " and ri.day=" + aditing.getDay()
//						+ " and ri.orgId='" + aditing.getOrgId()
//						+ "' and ri.repFreqId=" + aditing.getActuFreqID();
//					Query query = session.createQuery(hql2);
//					Long repId = ((Long) query.uniqueResult());
					aditing.setRepInId(new Integer(afReprot.getRepId().intValue()));
				}
				tran.commit();
				result.add(aditing);
			}
			
			pageListInfo.setList(result);
		} catch (Exception he) {
			try {
				tran.rollback();
			} catch (HibernateException e) {
				e.printStackTrace();
			}
			log.printStackTrace(he);
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return pageListInfo;
	}

	
	/**
	 * ���ݲ�����ѯ�� ������Ҫ���͵ı���
	 * Ϊ��ȫ����ʾ ���� ÿҳ�����ݶ���Ϊ1000��
	 * @param afReportForm
	 * @param operator
	 * @param curPage
	 * @return
	 * Administrator
	 * 2013-3-20 ����04:46:02
	 * PageListInfo
	 */
	
	
	
	public static PageListInfo selectNeedReports(AFReportForm afReportForm,
			Operator operator,int curPage) {
		List result = new ArrayList();
		DBConn conn = null;
		Session session = null;
		PageListInfo pageListInfo = null;
		Transaction tran =null;
		try {

			conn = new DBConn();
			session = conn.openSession();
			tran = session.beginTransaction();
			String hql = "from AfViewReport a where a.templateType='"
					+ afReportForm.getTemplateType()+"'"
					+ getValidDateSql(afReportForm);
					//+ " and a.comp_id.orgId='" + operator.getOrgId() + "'";

			/** ���ϱ���Ȩ�� 
			 * ���������ݿ��ж�*/
			if (operator.isSuperManager() == false) {
				if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
					hql += " and a.comp_id.orgId ||a.comp_id.templateId in ("
						+ operator.getChildRepReportPopedom() + ")";
				if(Config.DB_SERVER_TYPE.equals("sqlserver"))
					hql += " and a.comp_id.orgId +a.comp_id.templateId in ("
						+ operator.getChildRepReportPopedom() + ")";
			}
			String repName = afReportForm.getRepName();
			if (repName != null && !"".equals(repName.trim())) {
				hql += " and a.templateName like '%" + repName + "%' ";
			}
			
			if (afReportForm.getOrgId() != null && !afReportForm.getOrgId().equals("")
					&& !afReportForm.getOrgId().equals(Config.DEFAULT_VALUE)
					&& !afReportForm.getOrgId().trim().equals("")) {
				hql += " and a.comp_id.orgId='" + afReportForm.getOrgId() + "'";
			}

			if(afReportForm.getRepFreqId()!= null && !afReportForm.getRepFreqId().equals("")
					&& !afReportForm.getRepFreqId().equals(Config.DEFAULT_VALUE)) {
				hql += " and a.comp_id.repFreqId=" + afReportForm.getRepFreqId();
			}
			
			/** ��ӱ����Ų�ѯ���������Դ�Сдģ����ѯ�� */
			if (afReportForm.getTemplateId() != null && !afReportForm.getTemplateId().equals("")) {
				hql += " and upper(a.comp_id.templateId) like upper('%"
						+ afReportForm.getTemplateId().trim() + "%')";
			}
			
			/** ���������� */
			if (!StringUtil.isEmpty(afReportForm.getBak1()) && !afReportForm.getBak1().trim().equals(
							Config.DEFAULT_VALUE)) {
				hql += " and a.bak1 in (" + afReportForm.getBak1()+") ";
			}

			hql += " and a.isReport="
					+ com.fitech.gznx.common.Config.TEMPLATE_REPORT;
					/**order by �����;ۺϺ�����ͻ �Ѿ�ע�� ���Ը� 2011-12-28
					 * ���������ݿ��ж�*/
			if(Config.DB_SERVER_TYPE.equals("oracle"))
				hql+= " order by a.comp_id.repFreqId,a.comp_id.templateId";
			if(Config.DB_SERVER_TYPE.equals("sqlserver") || Config.DB_SERVER_TYPE.equals("db2"))
				hql+="";

//			Query query = session.createQuery(hql);
//			List list = query.list();
			/**��ʹ��hibernate ���Ը� 2011-12-21**/
			pageListInfo = findByPageWithSQL(session,hql,1000,curPage);
			// int arraySize = limit + offset;
			// if(list.size() < (limit + offset)) arraySize = list.size();
			// for(int i=offset;i<arraySize;i++){
			List list=pageListInfo.getList();
			
			for (int i = 0; i < list.size(); i++) {
				AfViewReport viewMReport = (AfViewReport) list.get(i);
				Aditing aditing = new Aditing();
				// aditing.setDataRgTypeName(viewMReport.getDataRgTypeName());
				aditing.setTemplateId(viewMReport.getComp_id().getTemplateId());
				aditing.setActuFreqName(viewMReport.getRepFreqName());
				aditing.setActuFreqID(viewMReport.getComp_id().getRepFreqId());
				aditing.setChildRepId(viewMReport.getComp_id().getTemplateId());
				aditing.setVersionId(viewMReport.getComp_id().getVersionId());
				aditing.setRepName(viewMReport.getTemplateName());
				if (viewMReport.getComp_id().getRepFreqId() != null) {
					// yyyy-mm-dd ��������ȷ�������ھ������������
					String trueDate = DateUtil
							.getFreqDateLast(afReportForm.getDate(),
									viewMReport.getComp_id().getRepFreqId());
					aditing.setYear(Integer.valueOf(trueDate.substring(0, 4)));
					aditing.setTerm(Integer.valueOf(trueDate.substring(5, 7)));
					aditing.setDay(Integer.valueOf(trueDate.substring(8, 10)));
				}
				aditing.setCurrName(viewMReport.getCurName());
				aditing.setCurId(viewMReport.getComp_id().getCurId());
				aditing.setOrgId(viewMReport.getComp_id().getOrgId());
				aditing.setOrgName(AFOrgDelegate.getOrgInfo(
						viewMReport.getComp_id().getOrgId()).getOrgName());
				// aditing.setDataRgId(viewMReport.getComp_id().getDataRangeId());
				aditing.setReportStyle(viewMReport.getReportStyle());
				
				String sql = "from AfReport ri where ri.times=1 "
						+ " and ri.versionId='" + aditing.getVersionId()
						+ "' and ri.templateId='" + aditing.getChildRepId()
						+ "' and ri.curId=" + aditing.getCurId()
						+ " and ri.year=" + aditing.getYear() + " and ri.term="
						+ aditing.getTerm() + " and ri.day=" + aditing.getDay()
						+ " and ri.orgId='" + aditing.getOrgId()
						+ "' and ri.repFreqId=" + aditing.getActuFreqID();
//				System.out.println("sql ="+sql);
				List reportInList = session.find(sql);
				if (reportInList != null && reportInList.size() > 0) {
					AfReport report = (AfReport) reportInList.get(0);
					if (report.getCheckFlag() != null
							&& report.getCheckFlag().toString().equals(
									Config.CHECK_FLAG_NO.toString())) {
						Set reportAgainSets = report.getReportAgainSets();
						String cause = "";
						int rasId = 0;
						if (reportAgainSets != null
								&& reportAgainSets.size() > 0) {
							for (Iterator iterSet = reportAgainSets.iterator(); iterSet
									.hasNext();) {
								AfReportAgain againSet = (AfReportAgain) iterSet.next();
								if (againSet.getRasId().intValue() > rasId) {
									cause = againSet.getCause();
									rasId = againSet.getRasId().intValue();
								}
							}
						}
						aditing.setWhy(cause);
					}
					aditing.setCheckFlag(report.getCheckFlag().shortValue());
					aditing.setRepInId(report.getRepId().intValue());
					if (report.getTblInnerValidateFlag() != null)
						aditing.setTblInnerValidateFlag(report
								.getTblInnerValidateFlag().shortValue());
					if (report.getTblOuterValidateFlag() != null)
						aditing.setTblOuterValidateFlag(report
								.getTblOuterValidateFlag().shortValue());
				} else {
					
					aditing.setCheckFlag(Config.CHECK_FLAG_UNREPORT);
					AfReport afReprot = new AfReport();
//					ReportInForm reportInForm = new ReportInForm();
					BeanUtils.copyProperties(afReprot,aditing);
					afReprot.setTimes(new Long(new String("1")));
					afReprot.setRepFreqId(new Long (aditing.getActuFreqID()));
					afReprot.setReportDate(new Date());
//					StrutsReportInDelegate strutsReportInDelegate=new StrutsReportInDelegate();
//					strutsReportInDelegate.insertNewReport(reportInForm);
					session.saveOrUpdate(afReprot);
					
					String hql2 = "select  ri.repId  from AfReport ri where ri.times=1 " 
						+ " and ri.versionId='" + aditing.getVersionId()
						+ "' and ri.templateId='" + aditing.getChildRepId()
						+ "' and ri.year=" + aditing.getYear() 
						+ " and ri.term="+ aditing.getTerm()
						+ " and ri.day=" + aditing.getDay()
						+ " and ri.orgId='" + aditing.getOrgId()
						+ "' and ri.repFreqId=" + aditing.getActuFreqID();
					System.out.println("~~~~~~~~~~~"+hql2);
					Query query = session.createQuery(hql2);
					Long repId = ((Long) query.uniqueResult());
					System.out.println(repId);
					aditing.setRepInId( Integer.parseInt(String.valueOf(repId)));
				}
				tran.commit();
				result.add(aditing);
			}
			
			pageListInfo.setList(result);
		} catch (Exception he) {
			try {
				tran.rollback();
			} catch (HibernateException e) {
				e.printStackTrace();
			}
			log.printStackTrace(he);
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return pageListInfo;
	}

	
	
	/**
	 * 
	 * �����Ҫ��������
	 *
	 */
	public static List getReortStat(AFReportForm afReportForm) {
		List result = new ArrayList();
		DBConn conn = null;
		Session session = null;

		if(afReportForm.getDate()==null || afReportForm.getOrgId()==null)
			return null;
		
		try {

			conn = new DBConn();
			session = conn.openSession();

			String hql = "from AfViewReport a where a.templateType='"
					+ afReportForm.getTemplateType()+"' "
					+ getValidDateSql(afReportForm)
					+ " and a.comp_id.orgId='" + afReportForm.getOrgId() + "'"
					+ " and a.isReport="
					+ com.fitech.gznx.common.Config.TEMPLATE_REPORT
					+ " order by a.comp_id.repFreqId,a.comp_id.templateId,a.comp_id.curId ";

			Query query = session.createQuery(hql);
			List list = query.list();

			for (int i = 0; i < list.size(); i++) {
				AfViewReport viewMReport = (AfViewReport) list.get(i);
				Aditing aditing = new Aditing();
				aditing.setActuFreqName(viewMReport.getRepFreqName());
				aditing.setActuFreqID(viewMReport.getComp_id().getRepFreqId());
				aditing.setChildRepId(viewMReport.getComp_id().getTemplateId());
				aditing.setVersionId(viewMReport.getComp_id().getVersionId());
				aditing.setRepName(viewMReport.getTemplateName());
				if (viewMReport.getComp_id().getRepFreqId() != null) {
					// yyyy-mm-dd ��������ȷ�������ھ������������
					String trueDate = DateUtil
							.getFreqDateLast(afReportForm.getDate()+"-01",
									viewMReport.getComp_id().getRepFreqId());
					aditing.setYear(Integer.valueOf(trueDate.substring(0, 4)));
					aditing.setTerm(Integer.valueOf(trueDate.substring(5, 7)));
					aditing.setDay(Integer.valueOf(trueDate.substring(8, 10)));
				}
				aditing.setCurrName(viewMReport.getCurName());
				aditing.setCurId(viewMReport.getComp_id().getCurId());
				aditing.setOrgId(viewMReport.getComp_id().getOrgId());
				aditing.setOrgName(AFOrgDelegate.getOrgInfo(
						viewMReport.getComp_id().getOrgId()).getOrgName());

				String sql = "from AfReport ri where ri.times=1"
						+ " and ri.versionId='" + aditing.getVersionId()
						+ "' and ri.templateId='" + aditing.getChildRepId()
						+ "' and ri.curId=" + aditing.getCurId()
						+ " and ri.year=" + aditing.getYear() + " and ri.term="
						+ aditing.getTerm() + " and ri.day=" + aditing.getDay()
						+ " and ri.orgId='" + aditing.getOrgId()
						+ "' and ri.repFreqId=" + aditing.getActuFreqID();

				List reportInList = session.find(sql);

				if (reportInList != null && reportInList.size() > 0) {
					AfReport report = (AfReport) reportInList.get(0);
					
					if(report.getForseReportAgainFlag()!=null)
						aditing.setForseReportAgainFlag(report.getForseReportAgainFlag());

					aditing.setCheckFlag(report.getCheckFlag().shortValue());
					aditing.setRepInId(report.getRepId().intValue());
					aditing.setReportDate(report.getReportDate());
					
				} else {
					aditing.setCheckFlag(Config.CHECK_FLAG_UNREPORT);
					
				}
				result.add(aditing);

			}

		} catch (Exception he) {
			log.printStackTrace(he);

		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return result;
	}
	
	/**
	 * 
	 * �����Ҫ��������
	 *
	 */
	public static List getRepReortStat(AFReportForm afReportForm) {
		List result = new ArrayList();
		DBConn conn = null;
		Session session = null;
		Connection cont=null;
		ResultSet rs = null;
		List rList = null;
		if(afReportForm.getDate()==null || afReportForm.getOrgId()==null)
			return null;
		
		try {
			String busiLine = afReportForm.getTemplateType().equals("2")?"rhtx":"qttx";
			conn = new DBConn();
			String sql = "";
			if(com.cbrc.smis.common.Config.SYSTEM_SCHEMA_FLAG==0){
				
			}else{
				sql+=" select distinct mr.rep_freq_id,mf.rep_freq_name, " +
					"    g.org_id,g.org_name,ri.template_id," +
					"    ri.REP_NAME,ri.rep_id," +
					"    ri.version_id,wm.late_rerep_date," +
					"    wm.late_rep_date,wm.return_desc" +
					"   from af_report ri join af_org g" +
					"   on g.org_id=ri.org_id" +
					"   join af_template_freq_relation mr" +
					"   on mr.template_id = ri.template_id" +
					" and mr.rep_freq_id = ri.rep_freq_id"+
					"   and mr.version_id = ri.version_id" +
					"    join work_task_rep_force ras" +
					"    on ras.rep_in_id = ri.rep_id" +
					"    and ras.org_id = ri.org_id" +
					"    join m_rep_freq mf" +
					"    on mf.rep_freq_id = mr.rep_freq_id" +
					"    join work_task_node_moni wm" +
					"    on ras.task_moni_id = wm.task_moni_id" +
					"    and ras.node_id = wm.node_id" +
					"    and ras.org_id = wm.org_id" +
					"    join work_task_moni wtm on wtm.task_moni_id=wm.task_moni_id "+
					"    where wm.busi_line='"+busiLine+"' and wm.perform_number>1 and wm.rerep_flag=1 and wtm.year="+afReportForm.getDate().substring(0, 4)  +"  and wtm.term="+afReportForm.getDate().substring(5, 7)+
					" and ri.year="+ afReportForm.getDate().substring(0, 4) +" and ri.term=" +afReportForm.getDate().substring(5, 7) +
					"    and ras.node_id in (select node_id from work_task_node_info" +
					"          where node_id not in (select pre_node_id from work_task_node_info))" ;
				
				if (Config.DB_SERVER_TYPE.equals("sqlserver")) {
					sql+=" and ri.template_id+'-'+ri.version_id in " ;
					sql+=" (select at.template_id+'-'+at.version_id from af_template at " ;
				}
				if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2")) {
					sql+=" and ri.template_id||'-'||ri.version_id in " ;
					sql+=" (select at.template_id||'-'||at.version_id from af_template at " ;
				}
				
				sql+=" where at.template_type='" + afReportForm.getTemplateType()+ "')"+
				" and g.org_id ='"+afReportForm.getOrgId()+"'";
			}
			
			cont = conn.openSession().connection();
			rs = cont.createStatement().executeQuery(sql);
			
			rList = new ArrayList();
			Aditing aditing = null;
			
			while(rs.next()){
				aditing = new Aditing();
				aditing.setChildRepId(rs.getString("template_id"));
				aditing.setRepName(rs.getString("REP_NAME"));
				aditing.setVersionId(rs.getString("VERSION_ID"));
				aditing.setActuFreqID(Integer.valueOf(rs.getInt("REP_FREQ_ID")));
				aditing.setActuFreqName(rs.getString("REP_FREQ_NAME"));
				aditing.setWhy(rs.getString("return_desc"));
				aditing.setOrgId(rs.getString("org_id"));
				aditing.setOrgName(rs.getString("org_name"));
				aditing.setRepDay(rs.getInt("LATE_REREP_DATE"));
				aditing.setFinalLaterDate(rs.getDate("LATE_REP_DATE"));
				aditing.setRepInId(Integer.valueOf(rs.getInt("rep_id")));
				aditing.setYear(Integer.parseInt(afReportForm.getDate().substring(0, 4)));
				aditing.setTerm(Integer.valueOf(afReportForm.getDate().substring(5, 7)));
				
				rList.add(aditing);
				
			}
			
		} catch (Exception he) {
			log.printStackTrace(he);

		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return rList;
	}
	
	/**
	 * �����·ݲ��Ƶ��
	 * @param term �·�
	 * @return
	 */
	private static String freqs(int term){
		
		String rep_freq = "";
		
		if (term<1 || term>12) return rep_freq;
		
		if (term == 12)
			rep_freq = "1,2,3,4";
		else if (term == 6)
			rep_freq = "1,2,3";
		else if (term == 3 || term == 9)
			rep_freq = "1,2";
		else
			rep_freq = "1";
		
		return rep_freq;
	}
	
	/**
	 * 
	 * �����Ҫ��������
	 *
	 */
	public static List getLaterReortStat(AFReportForm afReportForm) {
		List result = new ArrayList();
		DBConn conn = null;
		List rList = null;
		Session session = null;
    	Connection cont = null;
    	Statement stmt = null;
    	ResultSet rs = null;
		if(afReportForm.getDate()==null || afReportForm.getOrgId()==null)
			return null;
		
		try {

			//��ø���Ƶ��
			String freqs = freqs(Integer.valueOf(afReportForm.getDate().substring(5, 7)));
			conn = new DBConn();
			session = conn.openSession();
			
			String busiLine = afReportForm.getTemplateType().equals("2")?"rhtx":"qttx";

			String sql = "select nrr.org_id,rir.template_id,rir.version_id," +
			"moni.end_date as report_date, " + //������ע�ͣ���ԭ���ñ����������޸�Ϊ��������������
				 //	"rir.report_date,"+
							" (select REP_FREQ_NAME from m_rep_freq where REP_FREQ_ID=nrr.rep_freq_id)REP_FREQ_NAME"+
							" ,(select org_name from af_org where org_id = nrr.org_id)org_name"+
					        " ,nrr.template_name,moni.late_rep_date from (select vm.template_id, "+
					        " vm.version_id,vm.org_id,vm.normal_time,vm.template_name,"+
					        " vm.rep_freq_id from view_af_report vm"+
					        " where vm.REP_FREQ_ID in ("+freqs+")"+
					         "  and vm.template_type = '"+afReportForm.getTemplateType()+"'"+
					         "  and ('"+ afReportForm.getDate()+"-15' between vm.start_date and vm.end_date)) nrr"+
					         "  join (select ri.template_id, ri.version_id, ri.org_id, ri.report_date"+
					         "  from af_report ri where year = "+ afReportForm.getDate().substring(0, 4) +" and term = "+afReportForm.getDate().substring(5, 7)+
					         "  and ri.template_id || '-' || ri.version_id in"+
					         "  (select ri.template_id || '-' || ri.version_id"+
					         "  from af_template at where at.template_type = '"+afReportForm.getTemplateType()+"')) rir"+
					         "  on nrr.template_id = rir.template_id"+
					         "  and nrr.version_id = rir.version_id"+
					         "  and nrr.org_id = rir.org_id"+
					         "  join (select m.org_id, om.template_id, m.late_rep_date,m.end_date"+
					         "   from work_task_node_moni m"+
					         "   join work_task_node_object_moni om"+
					         "     on om.task_moni_id = m.task_moni_id"+
					         "    and om.org_id = m.org_id"+
					         "     and om.node_id = m.node_id"+
					         "   join work_task_moni wm"+
					         "     on wm.task_moni_id = m.task_moni_id"+
					         "   where m.final_exec_flag = 1"+
					         "    and wm.year = "+ afReportForm.getDate().substring(0, 4) +" and wm.term = "+afReportForm.getDate().substring(5, 7)+
					         "    and m.node_id in  " +
					         "   (select wtnm.node_id-2 from (select *  " +
			                  "     from work_task_node_info " +
			                   "   where node_id not in " +
			                       "     (select pre_node_id from work_task_node_info)) wtni join work_task_node_moni wtnm on wtni.node_id=wtnm.node_id where  wtnm.node_flag=3 and wtnm.final_exec_flag=1 and m.org_id=wtnm.org_id and m.task_moni_id=wtnm.task_moni_id)" +
					         "    and m.busi_line = '"+busiLine+"'"+
					         "    and m.node_flag = 4 and m.end_date>m.late_rep_date"+
					         "    and om.node_io_flag = 1) moni"+
					         "   on moni.org_id = rir.org_id"+
					         "  and moni.template_id = rir.template_id"+
					         "  and nrr.ORG_ID = '"+afReportForm.getOrgId()+"'";
			
			cont = conn.openSession().connection();
			rs = cont.createStatement().executeQuery(sql);
			
			rList = new ArrayList();
			Aditing aditing = null;
			
			while(rs.next()){
				aditing = new Aditing();
				aditing.setChildRepId(rs.getString("template_id"));
				aditing.setVersionId(rs.getString("VERSION_ID"));
				aditing.setRepName(rs.getString("template_name"));
				aditing.setOrgName(rs.getString("org_name"));
				Date d1 = rs.getTimestamp("REPORT_DATE");
				Date d2 = rs.getTimestamp("late_rep_date");
				if(Config.REPORT_TIME_UNIT.equals(Config.REPORT_TIME_UNIT_DAY)){
					int repDay = DateUtil.getDays(DateUtil.toSimpleDateFormat(d2, "yyyy-MM-dd"), 
							DateUtil.toSimpleDateFormat(d1, "yyyy-MM-dd"));
					aditing.setLaterDay(repDay);
				}else if(Config.REPORT_TIME_UNIT.equals(Config.REPORT_TIME_UNIT_HOUR)){
					long diff = d1.getTime()-d2.getTime();
					long nd = 1000*60*60;
					long nh = 1000*60*60*24;
				
					Long hour = diff/nd;
					aditing.setLaterDay(hour.intValue());
				}
				aditing.setActuFreqName(rs.getString("REP_FREQ_NAME"));
				aditing.setRealRepTime(rs.getTimestamp("REPORT_DATE"));
				aditing.setSureRepTime(rs.getTimestamp("late_rep_date"));
				aditing.setYear(Integer.parseInt(afReportForm.getDate().substring(0, 4)));
				aditing.setTerm(Integer.valueOf(afReportForm.getDate().substring(5, 7)));
				
				rList.add(aditing);
				
			}
			
		} catch (Exception he) {
			log.printStackTrace(he);

		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return rList;
	}
	
	
	/**
	 * �õ���Ӧ���ܵı���
	 * 
	 * @param reportID
	 * @return
	 */
	public static ReportInForm getReportToCollect(Integer reportID) {
		ReportInForm reportInForm = null;
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		if (reportID != null) {
			try {
				String hhql = "select rep.rep_in_id,rep.child_rep_id,rep.version_id,"
						+ "rep.cur_id,rep.data_range_id from report_in rep "
						+ "left join (select t.term,t.child_rep_id,"
						+ "t.org_id,t.version_id,t.year,t.times,t.cur_id,t.data_range_id from report_in t "
						+ "where t.rep_in_id ="
						+ reportID
						+ ") b on rep.term=b.term where "
						+ "rep.term=b.term and rep.child_rep_id=b.child_rep_id and rep.org_id=b.org_id"
						+ " and rep.data_range_id=b.data_range_id "
						+ "and rep.cur_id=b.cur_id and rep.version_id=b.version_id "
						+ "and rep.year=b.year and rep.times=-1";
				con = new com.cbrc.smis.proc.jdbc.FitechConnection()
						.getConnect();
				stmt = con.prepareStatement(hhql.toUpperCase());
				rs = stmt.executeQuery();

				if (rs != null) {
					if (rs.next()) {
						reportInForm = new ReportInForm();
						int repInId = rs.getInt(1);
						String childRepId = rs.getString(2);
						String versionId = rs.getString(3);

						reportInForm.setRepInId(new Integer(repInId));
						reportInForm.setChildRepId(childRepId);
						reportInForm.setVersionId(versionId);
					}
				}
			} catch (Exception e) {
				reportInForm = null;
				log.printStackTrace(e);
				try {
					con.close();
				} catch (SQLException e1) {
					// TODO �Զ����� catch ��
					e1.printStackTrace();
				}
			} finally {
				try {
					if (con != null)
						con.close();
				} catch (SQLException e) {
					// TODO �Զ����� catch ��
					e.printStackTrace();
				}
			}
		}
		return reportInForm;
	}

	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-27
	 * Ӱ�����AfReport
	 * �õ���Ӧ���ܵı���
	 * 
	 * @param reportID
	 * @return
	 */
	public static ReportInForm getReportIn(Integer reportId) {
		ReportInForm reportInForm = null;

		if (reportId == null)
			return null;

		DBConn conn = null;

		try {
			conn = new DBConn();
			AfReport reportIn = (AfReport) conn.openSession().get(
					AfReport.class, new Long(reportId));
			if (reportIn != null) {
				reportInForm = new ReportInForm();
				reportInForm.setChildRepId(reportIn.getTemplateId());
				reportInForm.setVersionId(reportIn.getVersionId());
				reportInForm.setRepName(reportIn.getRepName());
				reportInForm.setRepInId(reportIn.getRepId().intValue());
				reportInForm.setRepFreqId(reportIn.getRepFreqId().intValue());
				reportInForm.setOrgId(reportIn.getOrgId());
				reportInForm.setCurId(reportIn.getCurId().intValue());
				reportInForm.setYear(reportIn.getYear().intValue());
				reportInForm.setTerm(reportIn.getTerm().intValue());
				reportInForm.setDay(reportIn.getDay().intValue());

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
	 * ��ʹ��hibernate ���Ը� 2011-12-22
	 * Ӱ�����AfReport
	 * ���ʵ�����ݱ������
	 * 
	 * @param conn
	 *            Connection ���ݿ�����
	 * @param repInId
	 *            String ʵ�����ݱ���ID
	 * @param ReportIn
	 * @return Exception
	 */
	public static AFReportForm getReportIn(String repInId) throws Exception {

		AFReportForm afReportForm = null;
		DBConn conn = null;

		try {
			conn = new DBConn();
			Session session = conn.openSession();

			AfReport afRep = (AfReport) session.get(AfReport.class, Long.valueOf(repInId));
			
			if (afRep == null) 
				return null;
			else{
				afReportForm = new AFReportForm();
				afReportForm.setTemplateId(afRep.getTemplateId());
				afReportForm.setVersionId(afRep.getVersionId());
				afReportForm.setRepName(afRep.getRepName());
				afReportForm.setRepId(afRep.getRepId().toString());
				afReportForm.setRepFreqId(afRep.getRepFreqId().toString());
				afReportForm.setOrgId(afRep.getOrgId());
				afReportForm.setCurId(afRep.getCurId().toString());
				afReportForm.setYear(afRep.getYear().toString());
				afReportForm.setTerm(afRep.getTerm().toString());
				afReportForm.setDay(afRep.getDay().toString());
				afReportForm.setTimes(afRep.getTimes().toString());
			}

		} catch (Exception e) {
			afReportForm = null;
			throw new Exception(e.getMessage());
		} finally {
			if (conn != null)
				conn.closeSession();
		}

		return afReportForm;
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
	public AfReport insertNewReport(AFReportForm reportInForm) throws Exception {

		if (reportInForm == null)
			return null;
		AfReport reportIn = null;
		boolean result = false;
		if(reportInForm.getTimes()==null){
			reportInForm.setTimes("1");
		}
		if(reportInForm.getCheckFlag()==null){
			reportInForm.setCheckFlag(Config.CHECK_FLAG_UNREPORT.toString());
		}
		DBConn conn = null;
		try {
			conn = new DBConn();
			Session session = conn.beginTransaction();

			// ��ѯ���ݿ����Ƿ��Ѿ���������ͬ��¼
			/**��ʹ��hibernate ���Ը� 2011-12-21**/
			reportIn = getReportIn(reportInForm);

			// ������ݿ����Ѿ������˸�����¼����ֱ�ӷ��ظļ�¼����Ϣ�������µĲ��������������������
			if (reportIn != null) {
				result = true;
				return reportIn;
			}
			// ����������µ���Ϣ
			reportIn = new AfReport();
			com.fitech.gznx.util.TranslatorUtil.copyVoToPersistence(reportIn,
					reportInForm);
			session.save(reportIn);
			System.out.println("����afreport"+reportIn.getRepId());
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
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * Ӱ�����AfReport
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
	public static AfReport getReportIn(AFReportForm afReportForm) {

		AfReport reportIn = null;
		if (afReportForm == null)
			return reportIn;
		
		if (afReportForm.getOrgId() == null || afReportForm.getYear() == null
				|| afReportForm.getTerm() == null
				|| afReportForm.getDay() == null
				|| afReportForm.getRepFreqId() == null
				|| afReportForm.getCurId() == null)
			return reportIn;

		DBConn conn = null;
		try {
			conn = new DBConn();
			Session session = conn.beginTransaction();
			// ��ѯ�Ƿ�������ؼ�¼��Ϣ
			StringBuffer selectHQL = new StringBuffer("from AfReport ri where ");
			selectHQL.append("ri.templateId=:templateId and ");
			selectHQL.append("ri.versionId=:versionId and ");
			selectHQL.append("ri.orgId=:orgId and ");
			selectHQL.append("ri.curId=:curId and ");
			selectHQL.append("ri.year=:year and ");
			selectHQL.append("ri.term=:term and ");
			selectHQL.append("ri.day=:day and ");
			selectHQL.append("ri.repFreqId=:repFreqId ");
			//selectHQL.append(" and ri.times=:times");

			Query query = session.createQuery(selectHQL.toString());

			query.setString("templateId", afReportForm.getTemplateId());
			query.setString("versionId", afReportForm.getVersionId());
			query.setString("orgId", afReportForm.getOrgId().trim());
			query.setInteger("curId", Integer.valueOf(afReportForm.getCurId()));
			query.setInteger("year", Integer.valueOf(afReportForm.getYear()));
			query.setInteger("term", Integer.valueOf(afReportForm.getTerm()));
			query.setInteger("day", Integer.valueOf(afReportForm.getDay()));
			
			query.setInteger("repFreqId", Integer.valueOf(afReportForm
					.getRepFreqId()));
			//query.setInteger("times", Integer.valueOf(afReportForm.getTimes()));

			List queryList = query.list();

			// ������ݿ����Ѿ������˸�����¼����ֱ�ӷ��ظļ�¼����Ϣ�������µĲ��������������������
			if (queryList != null && queryList.size() > 0) {
				reportIn = (AfReport) queryList.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			reportIn = null;
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return reportIn;

	}

	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * ���ʵ�����ݱ������
	 * 
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

	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * Ӱ�����AfReport  AfTemplate
	 * ���ʵ�����ݱ������
	 * 
	 * @param conn
	 *            Connection ���ݿ�����
	 * @param repInId
	 *            int ʵ�����ݱ���ID
	 * @param ReportIn
	 * @return Exception
	 */
	public static Integer getReportStyle(Long repInId) throws Exception {

		Integer reportStyle = null;

		DBConn conn = null;

		try {
			conn = new DBConn();
			Session session = conn.openSession();

			String hql = "select aft.reportStyle from AfReport afr,AfTemplate aft where afr.repId=" + repInId
							+ " and afr.templateId=aft.id.templateId and afr.versionId=aft.id.versionId";

			List list = session.find(hql.toString());

			if (list != null && list.size() > 0) {
				Long re = (Long) list.get(0);
				reportStyle = Integer.valueOf(String.valueOf(re));
			}

		} catch (Exception e) {
			reportStyle = null;
			throw new Exception(e.getMessage());
		} finally {
			if (conn != null)
				conn.closeSession();
		}

		return reportStyle;
	}
	public static Integer getReportInStyle(Long repInId) throws Exception {
		
		Integer reportStyle = null;
		
		DBConn conn = null;
		
		try {
			conn = new DBConn();
			Session session = conn.openSession();
			
			String hql = "select aft.reportStyle from ReportIn afr,AfTemplate aft where afr.repInId=" + repInId
			+ " and afr.MChildReport.comp_id.childRepId=aft.id.templateId and afr.MChildReport.comp_id.versionId=aft.id.versionId";
			
			List list = session.find(hql.toString());
			
			if (list != null && list.size() > 0) {
				Long re = (Long) list.get(0);
				reportStyle = Integer.valueOf(String.valueOf(re));
			}
			
		} catch (Exception e) {
			reportStyle = null;
			throw new Exception(e.getMessage());
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		
		return reportStyle;
	}
	
	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * �޸ı���ı�־λ
	 * 
	 * @param repInId
	 * @return
	 */
	public boolean updateReportInCheckFlag(Integer repInId, Short checkFlag) {
		// ���±�־
		boolean result = false;
		// ��������
		DBConn conn = null;
		Session session = null;

		try {
			conn = new DBConn();
			session = conn.beginTransaction();

			AfReport reportInPersistence = (AfReport) session.load(
					AfReport.class, repInId.longValue());

			if (reportInPersistence == null)
				return result;

			reportInPersistence.setCheckFlag(checkFlag.longValue());
			reportInPersistence.setReportDate(new Date());
			if(//checkFlag.equals(Config.CHECK_FLAG_RECHECKFAILED) || 
					checkFlag.equals(Config.CHECK_FLAG_NO) ){
				reportInPersistence.setTblInnerValidateFlag(null);
				reportInPersistence.setTblOuterValidateFlag(null);
			}
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
	 * ����afreportָ�������е�״̬
	 * @param repInId
	 * @param checkFlag
	 * @return
	 */
	public static boolean updateAfReportInCheckFlag(Integer repInId, Short checkFlag) {
		// ���±�־
		boolean result = false;
		// ��������
		DBConn conn = null;
		Session session = null;

		try {
			conn = new DBConn();
			session = conn.beginTransaction();
			AfReport reportInPersistence = (AfReport) session.get(AfReport.class, repInId.longValue());
			if (reportInPersistence == null)
				return result;
			reportInPersistence.setCheckFlag(checkFlag.longValue());
			if(//checkFlag.equals(Config.CHECK_FLAG_RECHECKFAILED) || 
					checkFlag.equals(Config.CHECK_FLAG_NO) ){
				reportInPersistence.setTblInnerValidateFlag(null);
				reportInPersistence.setTblOuterValidateFlag(null);
			}
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
	 * ��ʹ��hibernate ���Ը� 2011-12-27
	 * Ӱ�����AfReport
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
	public static boolean update(AFReportForm reportInForm)
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
			int bjvalidateflg = getSysParameter("SQ_VALIDATE_BJ");
			
			conn = new DBConn();
			session = conn.beginTransaction();

			// ѭ�����־û����󴫵�form����Ļ���id����Ĳ���
			if (reportInForm.getRepInIdArray() != null
					&& !reportInForm.getRepInIdArray().equals("")) {
				for (int i = 0; i < reportInForm.getRepInIdArray().length; i++) {
					AfReport reportInPersistence = (AfReport) session.load(
							AfReport.class, reportInForm.getRepInIdArray()[i].longValue());
					if(bjvalidateflg == 1 && !reportInForm.getCheckFlag()
							.equals("-5")){
						Short BJValidateFlag = reportInPersistence.getTblOuterValidateFlag()!=null ? reportInPersistence.getTblOuterValidateFlag().shortValue() : -1;
						if (BJValidateFlag == null
								|| !BJValidateFlag.equals(new Short("1"))) {
							messages.add(reportInPersistence.getTemplateId()+"���У�鲻ͨ����������ǩͨ���ñ���");

							result = false;
							break;
							// return new ActionForward(path.toString());
						}
					}
					// reportInPersistence.setCheckFlag(reportInForm.getCheckSignOption()[i]);
					reportInPersistence.setCheckFlag(Long.valueOf(reportInForm.getCheckFlag()));
					// ��������ʱ��
					reportInPersistence.setVerifyDate(new Date());
					
					if (reportInForm.getCheckFlag()
							.equals(com.fitech.net.config.Config.CHECK_FLAG_FAILED//CHECK_FLAG_RECHECKFAILED
									)) {
						reportInPersistence.setTblInnerValidateFlag(null);
						reportInPersistence.setTblOuterValidateFlag(null);
					}
					// System.out.println("checkFlag:" +
					// reportInForm.getCheckFlag());
					if (reportInForm.getForseReportAgainFlag() != null)
						reportInPersistence
								.setForseReportAgainFlag(Long.valueOf(reportInForm
										.getForseReportAgainFlag()));

					session.update(reportInPersistence);
					session.flush();
					//repOutIds[i] = reportInPersistence.getRepOutId();
				}
				//reportInForm.setRepOutIds(repOutIds);
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
	 * new�����˵Ĳ����ܼ�¼��
	 * 
	 * @param reportInForm
	 * @param operator
	 * @return int �ܼ�¼��
	 */
	public static int getRecordCountOfmanualRecheck(AFReportForm reportInForm,
			Operator operator) {
		int count = 0;
		DBConn conn = null;
		Session session = null;

		if (reportInForm == null || operator == null)
			return count;
		
		try {
			// ��ѯ����HQL������
			StringBuffer hql = new StringBuffer(
					"select count(*) from AfReport ri WHERE "
							+ "ri.times=(select max(r.times) from AfReport r where "
							+ "r.templateId=ri.templateId and r.versionId=ri.versionId "
							+ "and r.orgId=ri.orgId and r.curId=ri.curId "
							+ "and r.year=ri.year and r.term=ri.term and r.day=ri.day "
							+ "and r.checkFlag=ri.checkFlag)");

			/** ��ѯ����״̬Ϊδ���˱��� */
			StringBuffer where = new StringBuffer(" and ri.checkFlag="
					+ com.fitech.net.config.Config.CHECK_FLAG_UNCHECK);

			/** ��ӱ����Ų�ѯ���������Դ�Сдģ����ѯ�� */
			if (reportInForm.getTemplateId() != null
					&& !reportInForm.getTemplateId().equals("")) {
				where.append(" and upper(ri.templateId) like upper('%"
						+ reportInForm.getTemplateId().trim() + "%')");
			}
			/** ��ӱ������Ʋ�ѯ������ģ����ѯ�� */
			if (reportInForm.getRepName() != null
					&& !reportInForm.getRepName().equals("")) {
				where.append(" and ri.repName like '%"
						+ reportInForm.getRepName().trim() + "%'");
			}

			/** ��ӱ���Ƶ�Ȳ�ѯ���� */
			if (reportInForm.getRepFreqId() != null
					&& !String.valueOf(reportInForm.getRepFreqId()).equals(
							Config.DEFAULT_VALUE)) {
				where.append(" and ri.repFreqId=" + reportInForm.getRepFreqId());
			}

			/** ������ѯ */
			if (reportInForm.getDate() != null
					&& !reportInForm.getDate().equals("")) {
				where.append(getValidDateSql(reportInForm).replaceAll("a\\.", "ri.").replaceAll("comp_id.", ""));
			}
			/** ��ӻ�����ѯ���� */
			if (reportInForm.getOrgId() != null
					&& !reportInForm.getOrgId().equals(Config.DEFAULT_VALUE)
					&& !reportInForm.getOrgId().trim().equals("")) {

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
					where.append(" and ltrim(rtrim(ri.orgId)) || ri.templateId in("
							+ operator.getChildRepVerifyPopedom() + ")");
				if(Config.DB_SERVER_TYPE.equals("sqlserver"))
					where.append(" and ltrim(rtrim(ri.orgId)) + ri.templateId in("
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
	 * new�����˵Ĳ��Ҽ�¼����
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
	public static List selectOfManualRecheck(AFReportForm reportInForm,
			int offset, int limit, Operator operator) {
		List resList = null;
		DBConn conn = null;
		Session session = null;

		if (reportInForm == null || operator == null)
			return resList;

		try {
			// ��ѯ����HQL������
			StringBuffer hql = new StringBuffer(
					"from AfReport ri WHERE "
							+ "ri.times=(select max(r.times) from AfReport r where "
							+ "r.templateId=ri.templateId and r.versionId=ri.versionId "
							+ "and r.orgId=ri.orgId and r.curId=ri.curId "
							+ "and r.year=ri.year and r.term=ri.term and r.day=ri.day "
							+ "and r.checkFlag=ri.checkFlag)");

			/** ��ѯ����״̬Ϊδ���˱��� */
			StringBuffer where = new StringBuffer(" and ri.checkFlag="
					+ com.fitech.net.config.Config.CHECK_FLAG_UNCHECK);

			/** ��ӱ����Ų�ѯ���������Դ�Сдģ����ѯ�� */
			if (reportInForm.getTemplateId() != null
					&& !reportInForm.getTemplateId().equals("")) {
				where.append(" and upper(ri.templateId) like upper('%"
						+ reportInForm.getTemplateId().trim() + "%')");
			}
			/** ��ӱ������Ʋ�ѯ������ģ����ѯ�� */
			if (reportInForm.getRepName() != null
					&& !reportInForm.getRepName().equals("")) {
				where.append(" and ri.repName like '%"
						+ reportInForm.getRepName().trim() + "%'");
			}
			/** ������ѯ */
			if (reportInForm.getDate() != null
					&& !reportInForm.getDate().equals("")) {
				where.append(getValidDateSql(reportInForm).replaceAll("a\\.", "ri.").replaceAll("comp_id.", ""));
			}

			/** ��ӱ���Ƶ�ȣ���/��/����/�꣩��ѯ���� */
			if (reportInForm.getRepFreqId() != null
					&& !String.valueOf(reportInForm.getRepFreqId()).equals(
							Config.DEFAULT_VALUE)) {
				where.append(" and ri.repFreqId=" + reportInForm.getRepFreqId());
			}

			/** ��ӻ�����ѯ���� */
			if (reportInForm.getOrgId() != null
					&& !reportInForm.getOrgId().equals(Config.DEFAULT_VALUE)
					&& !reportInForm.getOrgId().trim().equals("")) {

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
					where.append(" and ltrim(rtrim(ri.orgId)) || ri.templateId in("
							+ operator.getChildRepVerifyPopedom() + ")");
				if(Config.DB_SERVER_TYPE.equals("sqlserver"))
					where.append(" and ltrim(rtrim(ri.orgId)) + ri.templateId in("
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
				AfOrg orgNet = null;
				AfReport reportInRecord = null;
				for (Iterator it = list.iterator(); it.hasNext();) {

					aditing = new Aditing();
					reportInRecord = (AfReport) it.next();

					// ���ñ������״̬
					aditing.setCheckFlag(reportInRecord.getCheckFlag()
							.shortValue());

					// ���ñ��У����
					if (reportInRecord.getTblOuterValidateFlag() != null)
						aditing.setTblOuterValidateFlag(reportInRecord
								.getTblOuterValidateFlag().shortValue());

					// ���ñ��ͻ�������
					orgNet = AFOrgDelegate.selectOne(reportInRecord.getOrgId());
					if (orgNet != null)
						aditing.setOrgName(orgNet.getOrgName());
					// ���ñ���ID��ʶ��
					aditing.setRepInId(reportInRecord.getRepId().intValue());

					// ���ñ������
					aditing.setYear(reportInRecord.getYear().intValue());
					// ���ñ�������
					aditing.setTerm(reportInRecord.getTerm().intValue());
					// ���ñ�����
					aditing.setDay(reportInRecord.getDay().intValue());

					// ���ñ�������
					aditing.setRepName(reportInRecord.getRepName());
					// ���ñ���������
					aditing.setReportDate(reportInRecord.getReportDate());
					// ���ñ�����
					aditing.setChildRepId(reportInRecord.getTemplateId());
					// ���ñ���汾��
					aditing.setVersionId(reportInRecord.getVersionId());
					// �����쳣�仯��־
//					aditing.setAbmormityChangeFlag(reportInRecord
//							.getAbmormityChangeFlag().intValue());

					// ���ñ����������
					MCurr mcr = StrutsMCurrDelegate.getISMCurr(reportInRecord
							.getCurId().toString());
					aditing.setCurrName(mcr.getCurName());
					aditing.setCurId(reportInRecord.getCurId().intValue());

					// MActuRep mActuRep = GetFreR(reportInRecord);

					// if (mActuRep != null) {
					// ���ñ��Ϳھ�
					// aditing.setDataRgTypeName(mActuRep.getMDataRgType()
					// .getDataRgDesc());
					MRepFreq mrf = StrutsMRepFreqDelegate
							.selectOne(reportInRecord.getRepFreqId().intValue());

					// ���ñ���Ƶ��
					aditing.setActuFreqName(mrf.getRepFreqName());
					aditing.setActuFreqID(reportInRecord.getRepFreqId().intValue());
					// }
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
	 * ��ʹ��hibernate ���Ը� 2011-12-27
	 * Ӱ�����AfReport AfTemplate
	 * new������ǩ�Ĳ����ܼ�¼��
	 * 
	 * @param reportInForm
	 * @param operator
	 * @return int �ܼ�¼��
	 */
	public static int getRecordCountOfmanual(AFReportForm reportInForm,
			Operator operator , String checkFlags) {
		int count = 0;
		DBConn conn = null;
		Session session = null;

		if (reportInForm == null || operator == null || checkFlags == null)
			return count;

		try {
			// ��ѯ����HQL������
			StringBuffer hql = new StringBuffer(
					"select count(*) from AfReport ri,AfTemplate at WHERE "
							+ "ri.times=1 and ri.templateId=at.id.templateId "
							+ "and ri.versionId=at.id.versionId and at.isReport=2");

			/** ��ѯ����״̬Ϊδ���˱��� */
			StringBuffer where = new StringBuffer(" and ri.checkFlag in ("
					+ checkFlags +") and at.templateType='" 
					+ reportInForm.getTemplateType()+"' and at.isReport="
					+ com.fitech.gznx.common.Config.TEMPLATE_REPORT);

			/** ��ӱ����Ų�ѯ���������Դ�Сдģ����ѯ�� */
			if (reportInForm.getTemplateId() != null
					&& !reportInForm.getTemplateId().equals("")) {
				where.append(" and upper(ri.templateId) like upper('%"
						+ reportInForm.getTemplateId().trim() + "%')");
			}
			/** ��ӱ������Ʋ�ѯ������ģ����ѯ�� */
			if (reportInForm.getRepName() != null
					&& !reportInForm.getRepName().equals("")) {
				where.append(" and ri.repName like '%"
						+ reportInForm.getRepName().trim() + "%'");
			}
			/** ��ӱ������Ͳ�ѯ���� */
			if (reportInForm.getBak1() != null
					&& !reportInForm.getBak1().equals("")) {
				where.append(" and at.bak1 in (" + reportInForm.getBak1().trim()+")");
			}
			
			/** ������ѯ */
			if (reportInForm.getDate() != null
					&& !reportInForm.getDate().equals("")) {
				where.append(getReportDateSql(reportInForm));
			}

			/** ��ӱ���Ƶ�Ȳ�ѯ���� */
			if (!StringUtil.isEmpty(reportInForm.getRepFreqId())
					&& !String.valueOf(reportInForm.getRepFreqId()).equals(
							Config.DEFAULT_VALUE)) {
				where.append(" and ri.repFreqId=" + reportInForm.getRepFreqId());
			}

			/** ��ӻ�����ѯ���� */
			if (reportInForm.getOrgId() != null
					&& !reportInForm.getOrgId().equals(Config.DEFAULT_VALUE)
					&& !reportInForm.getOrgId().trim().equals("")) {
				where.append(" and ri.orgId='" + reportInForm.getOrgId().trim()
						+ "'");
			}

			/** ��ӱ���fu��Ȩ�ޣ������û�������ӣ� 
			 * ���������ݿ��ж�*/
			if (operator.isSuperManager() == false) {
				if (operator.getChildRepCheckPodedom() == null
						|| operator.getChildRepCheckPodedom().equals(""))
					return count;
				if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
					where.append(" and ltrim(rtrim(ri.orgId)) || ri.templateId in("
							+ operator.getChildRepCheckPodedom() + ")");
				if(Config.DB_SERVER_TYPE.equals("sqlserver"))
					where.append(" and ltrim(rtrim(ri.orgId)) + ri.templateId in("
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
	 * �������ͨ���ĺ�û����˹�������
	 * @param reportInForm
	 * @param operator
	 * @param checkFlags
	 * @param cheFlag
	 * @return
	 */
	public static int getRecordCountSchemaFlag(AFReportForm reportInForm,
			Operator operator , String checkFlags) {
		int count = 0;
		DBConn conn = null;
		Session session = null;

		if (reportInForm == null || operator == null || checkFlags == null)
			return count;

		try {
			// ��ѯ����HQL������
			StringBuffer hql = new StringBuffer(
					"select count(*) from AfReport ri,AfTemplate at WHERE "
							+ "ri.times=1 and ri.templateId=at.id.templateId "
							+ "and ri.versionId=at.id.versionId and at.isReport=2");

			/** ��ѯ����״̬Ϊδ���˱��� */
			StringBuffer where = new StringBuffer(" and ri.checkFlag in ("
					+ checkFlags +") and at.templateType=" 
					+ reportInForm.getTemplateType()+" and at.isReport="
					+ com.fitech.gznx.common.Config.TEMPLATE_REPORT);

			/** ��ӱ����Ų�ѯ���������Դ�Сдģ����ѯ�� */
			if (reportInForm.getTemplateId() != null
					&& !reportInForm.getTemplateId().equals("")) {
				where.append(" and upper(ri.templateId) like upper('%"
						+ reportInForm.getTemplateId().trim() + "%')");
			}
			/** ��ӱ������Ʋ�ѯ������ģ����ѯ�� */
			if (reportInForm.getRepName() != null
					&& !reportInForm.getRepName().equals("")) {
				where.append(" and ri.repName like '%"
						+ reportInForm.getRepName().trim() + "%'");
			}
			/** ��ӱ������Ͳ�ѯ���� */
			if (reportInForm.getBak1() != null
					&& !reportInForm.getBak1().equals("")) {
				where.append(" and at.bak1 in (" + reportInForm.getBak1().trim()+")");
			}
			
			/** ������ѯ */
			if (reportInForm.getDate() != null
					&& !reportInForm.getDate().equals("")) {
				where.append(getReportDateSql(reportInForm));
			}

			/** ��ӱ���Ƶ�Ȳ�ѯ���� */
			if (!StringUtil.isEmpty(reportInForm.getRepFreqId())
					&& !String.valueOf(reportInForm.getRepFreqId()).equals(
							Config.DEFAULT_VALUE)) {
				where.append(" and ri.repFreqId=" + reportInForm.getRepFreqId());
			}

			/** ��ӻ�����ѯ���� */
			if (reportInForm.getOrgId() != null
					&& !reportInForm.getOrgId().equals(Config.DEFAULT_VALUE)
					&& !reportInForm.getOrgId().trim().equals("")) {
				where.append(" and ri.orgId='" + reportInForm.getOrgId().trim()
						+ "'");
			}

			/** ��ӱ���fu��Ȩ�ޣ������û�������ӣ� 
			 * ���������ݿ��ж�*/
			if (operator.isSuperManager() == false) {
				if (operator.getChildRepCheckPodedom() == null
						|| operator.getChildRepCheckPodedom().equals(""))
					return count;
				if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
					where.append(" and ltrim(rtrim(ri.orgId)) || ri.templateId in("
							+ operator.getChildRepCheckPodedom() + ")");
				if(Config.DB_SERVER_TYPE.equals("sqlserver"))
					where.append(" and ltrim(rtrim(ri.orgId)) + ri.templateId in("
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
	 * ��ʹ��hibernate ���Ը� 2011-12-27
	 * Ӱ�����AfReport AfTemplate
	 * new������ǩ�Ĳ��Ҽ�¼����
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
	public static List selectOfManual(AFReportForm reportInForm, int offset,
			int limit, Operator operator,String checkFlags) {
		List resList = null;
		DBConn conn = null;
		Session session = null;

		if (reportInForm == null || operator == null)
			return resList;
		
		try {
			// ��ѯ����HQL������
			StringBuffer hql = new StringBuffer(
					"select ri from AfReport ri, AfTemplate at WHERE "
							+ "ri.times=1 and ri.templateId=at.id.templateId "
							+ "and ri.versionId=at.id.versionId");

			/** ��ѯ����״̬Ϊδ���˱��� */
			StringBuffer where = new StringBuffer(" and ri.checkFlag in ("
					+ checkFlags +") and at.templateType='" 
					+ reportInForm.getTemplateType()+"' and at.isReport="
					+ com.fitech.gznx.common.Config.TEMPLATE_REPORT);

			/** ��ӱ����Ų�ѯ���������Դ�Сдģ����ѯ�� */
			if (reportInForm.getTemplateId() != null
					&& !reportInForm.getTemplateId().equals("")) {
				where.append(" and upper(ri.templateId) like upper('%"
						+ reportInForm.getTemplateId().trim() + "%')");
			}
			/** ��ӱ������Ʋ�ѯ������ģ����ѯ�� */
			if (reportInForm.getRepName() != null
					&& !reportInForm.getRepName().equals("")) {
				where.append(" and ri.repName like '%"
						+ reportInForm.getRepName().trim() + "%'");
			}
			
			/** ��ӱ������Ͳ�ѯ���� */
			if (reportInForm.getBak1() != null
					&& !reportInForm.getBak1().equals("")) {
				where.append(" and at.bak1 in (" + reportInForm.getBak1().trim() +")");
			}
			/**
			 * ��ӱ������β�ѯ����
			 */
			if (reportInForm.getSupplementFlag()!=null&&!"".equals(reportInForm.getSupplementFlag())) {
				int supplement_flag=new Integer(reportInForm.getSupplementFlag()).intValue();
				if(supplement_flag!=-999){
					where.append(" and at.supplementFlag= "+supplement_flag);
				}
			}
			/** ������ѯ */
			if (reportInForm.getDate() != null
					&& !reportInForm.getDate().equals("")) {
				where.append(getReportDateSql(reportInForm));
			}

			/** ��ӱ���Ƶ�ȣ���/��/����/�꣩��ѯ���� */
			if (!StringUtil.isEmpty(reportInForm.getRepFreqId())
					&& !String.valueOf(reportInForm.getRepFreqId()).equals(
							Config.DEFAULT_VALUE)) {
				where.append(" and ri.repFreqId=" + reportInForm.getRepFreqId());
			}

			/** ��ӻ�����ѯ���� */
			if (reportInForm.getOrgId() != null
					&& !reportInForm.getOrgId().equals(Config.DEFAULT_VALUE)
					&& !reportInForm.getOrgId().trim().equals("")) {
				where.append(" and ri.orgId='" + reportInForm.getOrgId().trim()
						+ "'");
			}

			/** ��ӱ���fu��Ȩ�ޣ������û�������ӣ�
			 * ���������ݿ��ж� */
			if (operator.isSuperManager() == false) {
				if (operator.getChildRepCheckPodedom() == null
						|| operator.getChildRepCheckPodedom().equals(""))
					return resList;
				if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
					where.append(" and ltrim(rtrim(ri.orgId)) || ri.templateId in("
							+ operator.getChildRepCheckPodedom() + ")");
				if(Config.DB_SERVER_TYPE.equals("sqlserver"))
					where.append(" and ltrim(rtrim(ri.orgId)) + ri.templateId in("
							+ operator.getChildRepCheckPodedom() + ")");
			}

			hql.append(where.toString()
							+ " order by ri.year,ri.term,ri.day,ri.orgId,ri.templateId ");

			conn = new DBConn();
			session = conn.openSession();
			Query query = session.createQuery(hql.toString());
			query.setFirstResult(offset).setMaxResults(limit);
			List list = query.list();

			if (list != null && list.size() > 0) {
				resList = new ArrayList();
				Aditing aditing = null;
				AfOrg orgNet = null;
				AfReport reportInRecord = null;
				for (Iterator it = list.iterator(); it.hasNext();) {
					aditing = new Aditing();
					reportInRecord = (AfReport) it.next();
					// ���ñ������״̬
					aditing.setCheckFlag(reportInRecord.getCheckFlag()
							.shortValue());

					// ���ñ��У����
					if (reportInRecord.getTblOuterValidateFlag() != null)
						aditing.setTblOuterValidateFlag(reportInRecord
								.getTblOuterValidateFlag().shortValue());

					aditing.setOrgId(reportInRecord.getOrgId());
					
					// ���ñ��ͻ�������
					orgNet = AFOrgDelegate.selectOne(reportInRecord.getOrgId());
					if (orgNet != null)
						aditing.setOrgName(orgNet.getOrgName());
					
					// ���ñ���ID��ʶ��
					aditing.setRepInId(reportInRecord.getRepId().intValue());

					// ���ñ������
					aditing.setYear(reportInRecord.getYear().intValue());
					// ���ñ�������
					aditing.setTerm(reportInRecord.getTerm().intValue());
					// ���ñ�����
					aditing.setDay(reportInRecord.getDay().intValue());

					// ���ñ�������
					aditing.setRepName(reportInRecord.getRepName());
					// ���ñ���������
					aditing.setReportDate(reportInRecord.getReportDate());
					// ���ñ�����
					aditing.setChildRepId(reportInRecord.getTemplateId());
					// ���ñ���汾��
					aditing.setVersionId(reportInRecord.getVersionId());
					// �����쳣�仯��־
//					aditing.setAbmormityChangeFlag(reportInRecord
//							.getAbmormityChangeFlag().intValue());

					// ���ñ����������
					MCurr mcr = StrutsMCurrDelegate.getISMCurr(reportInRecord
							.getCurId().toString());
					aditing.setCurrName(mcr.getCurName());
					aditing.setCurId(reportInRecord.getCurId().intValue());

					// MActuRep mActuRep = GetFreR(reportInRecord);

					// if (mActuRep != null) {
					// ���ñ��Ϳھ�
					// aditing.setDataRgTypeName(mActuRep.getMDataRgType()
					// .getDataRgDesc());
					MRepFreq mrf = StrutsMRepFreqDelegate
							.selectOne(reportInRecord.getRepFreqId().intValue());

					// ���ñ���Ƶ��
					aditing.setActuFreqName(mrf.getRepFreqName());
					aditing.setActuFreqID(reportInRecord.getRepFreqId().intValue());
					// }
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
	 * ������˹��ĺ�û����˹�������
	 * @param reportInForm
	 * @param offset
	 * @param limit
	 * @param operator
	 * @param checkFlags
	 * @param checkFlag
	 * @return
	 */
	public static List selectOfSchemaFlag(AFReportForm reportInForm, int offset,
			int limit, Operator operator,String checkFlags) {
		List resList = null;
		DBConn conn = null;
		Session session = null;

		if (reportInForm == null || operator == null)
			return resList;
		
		try {
			// ��ѯ����HQL������
			StringBuffer hql = new StringBuffer(
					"select ri from AfReport ri, AfTemplate at WHERE "
							+ "ri.times=1 and ri.templateId=at.id.templateId "
							+ "and ri.versionId=at.id.versionId");

			/** ��ѯ����״̬Ϊδ���˱��� */
			StringBuffer where = new StringBuffer(" and ri.checkFlag in ("
					+ checkFlags +") and at.templateType=" 
					+ reportInForm.getTemplateType()+" and at.isReport="
					+ com.fitech.gznx.common.Config.TEMPLATE_REPORT);

			/** ��ӱ����Ų�ѯ���������Դ�Сдģ����ѯ�� */
			if (reportInForm.getTemplateId() != null
					&& !reportInForm.getTemplateId().equals("")) {
				where.append(" and upper(ri.templateId) like upper('%"
						+ reportInForm.getTemplateId().trim() + "%')");
			}
			/** ��ӱ������Ʋ�ѯ������ģ����ѯ�� */
			if (reportInForm.getRepName() != null
					&& !reportInForm.getRepName().equals("")) {
				where.append(" and ri.repName like '%"
						+ reportInForm.getRepName().trim() + "%'");
			}
			
			/** ��ӱ������Ͳ�ѯ���� */
			if (reportInForm.getBak1() != null
					&& !reportInForm.getBak1().equals("")) {
				where.append(" and at.bak1 in (" + reportInForm.getBak1().trim() +")");
			}
			/**
			 * ��ӱ������β�ѯ����
			 */
			if (reportInForm.getSupplementFlag()!=null&&!"".equals(reportInForm.getSupplementFlag())) {
				int supplement_flag=new Integer(reportInForm.getSupplementFlag()).intValue();
				if(supplement_flag!=-999){
					where.append(" and at.supplementFlag= "+supplement_flag);
				}
			}
			/** ������ѯ */
			if (reportInForm.getDate() != null
					&& !reportInForm.getDate().equals("")) {
				where.append(getReportDateSql(reportInForm));
			}

			/** ��ӱ���Ƶ�ȣ���/��/����/�꣩��ѯ���� */
			if (!StringUtil.isEmpty(reportInForm.getRepFreqId())
					&& !String.valueOf(reportInForm.getRepFreqId()).equals(
							Config.DEFAULT_VALUE)) {
				where.append(" and ri.repFreqId=" + reportInForm.getRepFreqId());
			}

			/** ��ӻ�����ѯ���� */
			if (reportInForm.getOrgId() != null
					&& !reportInForm.getOrgId().equals(Config.DEFAULT_VALUE)
					&& !reportInForm.getOrgId().trim().equals("")) {
				where.append(" and ri.orgId='" + reportInForm.getOrgId().trim()
						+ "'");
			}

			/** ��ӱ���fu��Ȩ�ޣ������û�������ӣ�
			 * ���������ݿ��ж� */
			if (operator.isSuperManager() == false) {
				if (operator.getChildRepCheckPodedom() == null
						|| operator.getChildRepCheckPodedom().equals(""))
					return resList;
				if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
					where.append(" and ltrim(rtrim(ri.orgId)) || ri.templateId in("
							+ operator.getChildRepCheckPodedom() + ")");
				if(Config.DB_SERVER_TYPE.equals("sqlserver"))
					where.append(" and ltrim(rtrim(ri.orgId)) + ri.templateId in("
							+ operator.getChildRepCheckPodedom() + ")");
			}

			hql.append(where.toString()
							+ " order by ri.year,ri.term,ri.day,ri.orgId,ri.templateId ");

			conn = new DBConn();
			session = conn.openSession();
			Query query = session.createQuery(hql.toString());
			query.setFirstResult(offset).setMaxResults(limit);
			List list = query.list();

			if (list != null && list.size() > 0) {
				resList = new ArrayList();
				Aditing aditing = null;
				AfOrg orgNet = null;
				AfReport reportInRecord = null;
				for (Iterator it = list.iterator(); it.hasNext();) {
					aditing = new Aditing();
					reportInRecord = (AfReport) it.next();
					// ���ñ������״̬
					aditing.setCheckFlag(reportInRecord.getCheckFlag()
							.shortValue());

					// ���ñ��У����
					if (reportInRecord.getTblOuterValidateFlag() != null)
						aditing.setTblOuterValidateFlag(reportInRecord
								.getTblOuterValidateFlag().shortValue());

					aditing.setOrgId(reportInRecord.getOrgId());
					
					// ���ñ��ͻ�������
					orgNet = AFOrgDelegate.selectOne(reportInRecord.getOrgId());
					if (orgNet != null)
						aditing.setOrgName(orgNet.getOrgName());
					
					// ���ñ���ID��ʶ��
					aditing.setRepInId(reportInRecord.getRepId().intValue());

					// ���ñ������
					aditing.setYear(reportInRecord.getYear().intValue());
					// ���ñ�������
					aditing.setTerm(reportInRecord.getTerm().intValue());
					// ���ñ�����
					aditing.setDay(reportInRecord.getDay().intValue());

					// ���ñ�������
					aditing.setRepName(reportInRecord.getRepName());
					// ���ñ���������
					aditing.setReportDate(reportInRecord.getReportDate());
					// ���ñ�����
					aditing.setChildRepId(reportInRecord.getTemplateId());
					// ���ñ���汾��
					aditing.setVersionId(reportInRecord.getVersionId());
					// �����쳣�仯��־
//					aditing.setAbmormityChangeFlag(reportInRecord
//							.getAbmormityChangeFlag().intValue());

					// ���ñ����������
					MCurr mcr = StrutsMCurrDelegate.getISMCurr(reportInRecord
							.getCurId().toString());
					aditing.setCurrName(mcr.getCurName());
					aditing.setCurId(reportInRecord.getCurId().intValue());

					// MActuRep mActuRep = GetFreR(reportInRecord);

					// if (mActuRep != null) {
					// ���ñ��Ϳھ�
					// aditing.setDataRgTypeName(mActuRep.getMDataRgType()
					// .getDataRgDesc());
					MRepFreq mrf = StrutsMRepFreqDelegate
							.selectOne(reportInRecord.getRepFreqId().intValue());

					// ���ñ���Ƶ��
					aditing.setActuFreqName(mrf.getRepFreqName());
					aditing.setActuFreqID(reportInRecord.getRepFreqId().intValue());
					// }
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
	 * ��ʹ��hibernate ���Ը� 2011-12-27
	 * Ӱ�����AfReport  AfTemplate
	 * ���ݴ��������ѯ�ϱ����ݵ���ر���id
	 * 
	 * @param reportInForm
	 *            ҳ��FormBean
	 * @param operator
	 *            ��ǰ��¼�û�
	 * @return Integer[] ��ѯ������
	 */
	public static Integer[] reRecheckPassReportIds(AFReportForm reportInForm, Operator operator,Integer checkFlag) {
		Integer[] repIds = null;
		DBConn conn = null;
		Session session = null;

		if (reportInForm == null || operator == null)
			return repIds;
		
		try {
			// ��ѯ����HQL������
			StringBuffer hql = new StringBuffer(
					"select ri.repId from AfReport ri, AfTemplate at WHERE "
							+ "ri.times=1 and ri.templateId=at.id.templateId "
							+ "and ri.versionId=at.id.versionId");

			/** ��ѯ����״̬Ϊδ���˱��� */
			StringBuffer where = new StringBuffer(" and ri.checkFlag="
					+ checkFlag +" and at.templateType='" 
					+ reportInForm.getTemplateType()+"'");

			/** ��ӱ����Ų�ѯ���������Դ�Сдģ����ѯ�� */
			if (reportInForm.getTemplateId() != null
					&& !reportInForm.getTemplateId().equals("")) {
				where.append(" and upper(ri.templateId) like upper('%"
						+ reportInForm.getTemplateId().trim() + "%')");
			}
			/** ��ӱ������Ʋ�ѯ������ģ����ѯ�� */
			if (reportInForm.getRepName() != null
					&& !reportInForm.getRepName().equals("")) {
				where.append(" and ri.repName like '%"
						+ reportInForm.getRepName().trim() + "%'");
			}
			
			/** ��ӱ������Ͳ�ѯ���� */
			if (reportInForm.getBak1() != null
					&& !reportInForm.getBak1().equals("")) {
				where.append(" and at.bak1 in (" + reportInForm.getBak1().trim() +")");
			}
			
			/** ������ѯ */
			if (reportInForm.getDate() != null
					&& !reportInForm.getDate().equals("")) {
				where.append(getReportDateSql(reportInForm));
			}

			/** ��ӱ���Ƶ�ȣ���/��/����/�꣩��ѯ���� */
			if (reportInForm.getRepFreqId() != null
					&& !String.valueOf(reportInForm.getRepFreqId()).equals(
							Config.DEFAULT_VALUE)) {
				where.append(" and ri.repFreqId=" + reportInForm.getRepFreqId());
			}

			/** ��ӻ�����ѯ���� */
			if (reportInForm.getOrgId() != null
					&& !reportInForm.getOrgId().equals(Config.DEFAULT_VALUE)
					&& !reportInForm.getOrgId().trim().equals("")) {
				where.append(" and ri.orgId='" + reportInForm.getOrgId().trim()
						+ "'");
			}
			
			/** ��ӱ��״̬λ��ѯ���� */
			if (reportInForm.getTblOuterValidateFlag() != null
					&& !reportInForm.getTblOuterValidateFlag().equals("")) {
				where.append(" and ri.tblOuterValidateFlag=" + reportInForm.getTblOuterValidateFlag());
			}

			/** ��ӱ���fu��Ȩ�ޣ������û�������ӣ�
			 * ���������ݿ��ж� */
			if (operator.isSuperManager() == false) {
				if (operator.getChildRepCheckPodedom() == null
						|| operator.getChildRepCheckPodedom().equals(""))
					return repIds;
				if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
					where.append(" and ltrim(rtrim(ri.orgId)) || ri.templateId in("
							+ operator.getChildRepCheckPodedom() + ")");
				if(Config.DB_SERVER_TYPE.equals("sqlserver"))
					where.append(" and ltrim(rtrim(ri.orgId)) + ri.templateId in("
							+ operator.getChildRepCheckPodedom() + ")");
			}

			hql.append(where.toString());

			conn = new DBConn();
			session = conn.openSession();
			List list = session.createQuery(hql.toString()).list();
			if (list != null && list.size() > 0) {
				repIds = new Integer[list.size()];
				for (int i=0;i<list.size();i++) {					
					Long repId = (Long) list.get(i);	
					repIds[i] = repId.intValue();
				}
			}
		} catch (HibernateException he) {
			repIds = null;
			log.printStackTrace(he);
		} catch (Exception e) {
			repIds = null;
			log.printStackTrace(e);
		} finally {
			// �ر����ݿ�����
			if (conn != null)
				conn.closeSession();
		}
		return repIds;
	}
	
	/**
	 * hibernate ����Ҫ�޸� ���Ը� 2011-12-27
	 * Ӱ����� AfReport AfTemplate
	 * ��У������б������ر�����ʵid��rep_id����list
	 * 
	 * @param reportInForm
	 *            ҳ��FormBean
	 * @param operator
	 *            ��ǰ��¼�û�
	 * @return List ��ѯ��������
	 */
	public static List<Integer> selectOfManualAll(AFReportForm reportInForm, Operator operator , String checkFlgs) {
		List<Integer> resList = null;
		DBConn conn = null;
		Session session = null;

		if (reportInForm == null || operator == null)
			return resList;

		try {
			// ��ѯ����HQL������
			StringBuffer hql = new StringBuffer(
					"select ri from AfReport ri, AfTemplate at WHERE "
					+ "ri.times=1 and ri.templateId=at.id.templateId "
					+ "and ri.versionId=at.id.versionId");

			/** ��ѯ����״̬Ϊδ���˱��� */
			StringBuffer where = new StringBuffer(" and ri.checkFlag in ("
					+ com.fitech.net.config.Config.CHECK_FLAG_UNCHECK +") and at.templateType='" 
					+ reportInForm.getTemplateType()+"' and at.isReport="
					+ com.fitech.gznx.common.Config.TEMPLATE_REPORT);

			/** ��ӱ����Ų�ѯ���������Դ�Сдģ����ѯ�� */
			if (reportInForm.getTemplateId() != null
					&& !reportInForm.getTemplateId().equals("")) {
				where.append(" and upper(ri.templateId) like upper('%"
						+ reportInForm.getTemplateId().trim() + "%')");
			}
			/** ��ӱ������Ʋ�ѯ������ģ����ѯ�� */
			if (reportInForm.getRepName() != null
					&& !reportInForm.getRepName().equals("")) {
				where.append(" and ri.repName like '%"
						+ reportInForm.getRepName().trim() + "%'");
			}
			/** ��ӱ������Ͳ�ѯ���� */
			if (reportInForm.getBak1() != null
					&& !reportInForm.getBak1().equals("")) {
				where.append(" and at.bak1=" + reportInForm.getBak1().trim());
			}
			/** ������ѯ */
			if (reportInForm.getDate() != null
					&& !reportInForm.getDate().equals("")) {
				where.append(getValidDateSql(reportInForm).replaceAll("a.startDate", "at.startDate")
						.replaceAll("a.endDate", "at.endDate").replaceAll("a.comp_id", "ri"));
			}

			/** ��ӱ���Ƶ�ȣ���/��/����/�꣩��ѯ���� */
			if (reportInForm.getRepFreqId() != null
					&& !String.valueOf(reportInForm.getRepFreqId()).equals(
							Config.DEFAULT_VALUE)) {
				where.append(" and ri.repFreqId=" + reportInForm.getRepFreqId());
			}

			/** ��ӻ�����ѯ���� */
			if (reportInForm.getOrgId() != null
					&& !reportInForm.getOrgId().equals(Config.DEFAULT_VALUE)
					&& !reportInForm.getOrgId().trim().equals("")) {

				where.append(" and ri.orgId='" + reportInForm.getOrgId().trim()
						+ "'");
			}

			/** ��ӱ���fu��Ȩ�ޣ������û�������ӣ� */
			if (operator.isSuperManager() == false) {
				if (operator.getChildRepVerifyPopedom() == null
						|| operator.getChildRepVerifyPopedom().equals(""))
					return resList;
				if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
					where.append(" and ltrim(rtrim(ri.orgId)) || ri.templateId in("
							+ operator.getChildRepVerifyPopedom() + ")");
				if(Config.DB_SERVER_TYPE.equals("sqlserver"))
					where.append(" and ltrim(rtrim(ri.orgId)) + ri.templateId in("
							+ operator.getChildRepVerifyPopedom() + ")");
			}

			hql
					.append(where.toString()
							+ " order by ri.tblOuterValidateFlag,ri.year,ri.term,ri.orgId");

			conn = new DBConn();
			session = conn.openSession();
			Query query = session.createQuery(hql.toString());
			List list = query.list();

			if (list != null && list.size() > 0) {
				resList = new ArrayList<Integer>();
				AfReport reportInRecord = null;
				for (Iterator it = list.iterator(); it.hasNext();) {
					reportInRecord = (AfReport) it.next();
					resList.add(reportInRecord.getRepId().intValue());
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
	 * new����ǰ̨ҳ��Ĳ�ѯ������û�ѡ�и��ĵļ�¼д��˱�־Ϊ1
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
					if (reportInForm
							.getCheckFlag()
							.equals(com.fitech.net.config.Config.CHECK_FLAG_FAILED
									//com.fitech.net.config.Config.CHECK_FLAG_RECHECKFAILED
									)) {
						reportInPersistence.setTblInnerValidateFlag(null);
						reportInPersistence.setTblOuterValidateFlag(null);
					}
					// System.out.println("checkFlag:" +
					// reportInForm.getCheckFlag());
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

	/**
	 * <p>
	 * ����:����豨����
	 */
	public static List<Aditing> selectReport(AFReportForm afReportForm,
			Operator operator) {

		List<Aditing> result = new ArrayList<Aditing>();
		DBConn conn = null;
		Session session = null;
		try {

			conn = new DBConn();
			session = conn.openSession();

			String hql = "from AfViewReport a where a.templateType="
					+ afReportForm.getTemplateType()
					// �ձ���ѯ
					+ getValidDateSql(afReportForm);
					//+ " and a.comp_id.orgId='" + operator.getOrgId() + "'";

			/** ���ϱ���Ȩ��
			 * ���������ݿ��ж� */
			if (operator.isSuperManager() == false) {
				if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
					hql += " and a.comp_id.orgId||a.comp_id.templateId in ("
							+ operator.getChildRepReportPopedom() + ")";
				if(Config.DB_SERVER_TYPE.equals("sqlserver"))
					hql += " and a.comp_id.orgId+a.comp_id.templateId in ("
							+ operator.getChildRepReportPopedom() + ")";
			}
			String repName = afReportForm.getRepName();
			if (repName != null && !"".equals(repName.trim())) {
				hql += " and a.reportName like '%" + repName + "%' ";
			}

			hql += " and a.is_report="
					+ com.fitech.gznx.common.Config.TEMPLATE_REPORT
					+ " order by a.comp_id.templateId, a.comp_id.repFreqId";

			Query query = session.createQuery(hql);
			List list = query.list();

			for (int i = 0; i < list.size(); i++) {
				AfViewReport viewMReport = (AfViewReport) list.get(i);
				Aditing aditing = new Aditing();
				// aditing.setDataRgTypeName(viewMReport.getDataRgTypeName());
				aditing.setActuFreqName(viewMReport.getRepFreqName());
				aditing.setActuFreqID(viewMReport.getComp_id().getRepFreqId());
				aditing.setTemplateId(viewMReport.getComp_id().getTemplateId());
				aditing.setVersionId(viewMReport.getComp_id().getVersionId());
				aditing.setRepName(viewMReport.getTemplateName());
				if (viewMReport.getComp_id().getRepFreqId() != null) {
					// yyyy-mm-dd ��������ȷ�������ھ������������
					String trueDate = DateUtil
							.getFreqDateLast(afReportForm.getDate(),
									viewMReport.getComp_id().getRepFreqId());
					aditing.setYear(Integer.valueOf(trueDate.substring(0, 4)));
					aditing.setTerm(Integer.valueOf(trueDate.substring(5, 7)));
					aditing.setDay(Integer.valueOf(trueDate.substring(8, 10)));
				}
				aditing.setCurrName(viewMReport.getCurName());
				aditing.setCurId(viewMReport.getComp_id().getCurId());
				// aditing.setDataRgId(viewMReport.getComp_id().getDataRangeId());
				result.add(aditing);
			}
		} catch (Exception he) {
			log.printStackTrace(he);

		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return result;
	}

	/**
	 * jdbc���� ������oracle�﷨ ����Ҫ�޸�
	 * ���Ը� 2011-12-26
	 * ����ʵ�����ݱ��״̬��Ϣ
	 * 
	 * @param conn
	 *            Connection
	 * @param repInId
	 *            Integer
	 * @param flagName
	 *            String ״̬��ʶ��
	 * @param flagValue
	 *            int ״ֵ̬
	 * @return boolean ���²����ɹ�������true;���򣬷���false
	 * @exception Exception
	 */
	public static boolean updateFlag(Connection conn, Integer repInId,
			String flagName, int flagValue) throws Exception {

		boolean result = false;

		if (conn == null)
			return false;
		Statement pstmt = null;

		try {

			String sql = "update af_report set " + flagName + "="
					+ flagValue + " where rep_id=" + repInId;

			pstmt = conn.createStatement();
			
			if (pstmt.executeUpdate(sql) > 0)
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
	 * ��ʹ��hibernate ���Ը� 2011-12-26
	 * Ӱ�����AfTemplate AfReport
	 * ��Ҫ����ȫ��У��ı���(�����ϱ���������)
	 * 
	 * @param reportInForm
	 * @param operator
	 *            ��ǰ��¼�û�
	 * @return List У��ı���ID�б�
	 * @throws Exception
	 */
	public static List selectAllReports(AFReportForm reportInForm,
			Operator operator) throws Exception {
		List retvals = null;
		DBConn conn = null;
		Session session = null;

		if (reportInForm.getDate() == null 
				|| reportInForm.getOrgId() == null)
			return retvals;

		try {
			// ��ѯ����HQL������
			StringBuffer hql = new StringBuffer(
					"select ri.repId from AfReport ri, AfTemplate at WHERE "
							+ "ri.times=1 and ri.templateId=at.id.templateId "
							+ "and ri.versionId=at.id.versionId ");

			/** ��ѯ����״̬Ϊδ���˱��� */
			StringBuffer where = new StringBuffer(" and ri.checkFlag in ("
					+ com.fitech.net.config.Config.CHECK_FLAG_AFTERJY 
					+ ","
					+ com.fitech.net.config.Config.CHECK_FLAG_AFTERSAVE 
					+ ") and at.templateType='" 
					+ reportInForm.getTemplateType()+"'");

			/** ��ӱ����Ų�ѯ���������Դ�Сдģ����ѯ�� */
			if (reportInForm.getTemplateId() != null
					&& !reportInForm.getTemplateId().equals("")) {
				where.append(" and upper(ri.templateId) like upper('%"
						+ reportInForm.getTemplateId().trim() + "%')");
			}
			/** ��ӱ������Ʋ�ѯ������ģ����ѯ�� */
			if (reportInForm.getRepName() != null
					&& !reportInForm.getRepName().equals("")) {
				where.append(" and ri.repName like '%"
						+ reportInForm.getRepName().trim() + "%'");
			}
			
			/** ��ӱ������Ͳ�ѯ���� */
			if (reportInForm.getBak1() != null
					&& !reportInForm.getBak1().equals("")) {
				where.append(" and at.bak1 in (" + reportInForm.getBak1().trim() +")");
			}
			
			/** ������ѯ */
			if (reportInForm.getDate() != null
					&& !reportInForm.getDate().equals("")) {
				where.append(getReportDateSql(reportInForm));
			}

			/** ��ӱ���Ƶ�ȣ���/��/����/�꣩��ѯ���� */
			if (reportInForm.getRepFreqId() != null
					&& !String.valueOf(reportInForm.getRepFreqId()).equals(
							Config.DEFAULT_VALUE)) {
				where.append(" and ri.repFreqId=" + reportInForm.getRepFreqId());
			}

			/** ��ӻ�����ѯ���� */
			if (reportInForm.getOrgId() != null
					&& !reportInForm.getOrgId().equals(Config.DEFAULT_VALUE)
					&& !reportInForm.getOrgId().trim().equals("")) {

				where.append(" and ri.orgId='" + reportInForm.getOrgId().trim()
						+ "'");
			}

			/** ��ӱ�����Ȩ�ޣ������û�������ӣ� 
			 * ���������ݿ��ж�*/
			if (operator.isSuperManager() == false) {
				if (operator.getChildRepVerifyPopedom() == null
						|| operator.getChildRepVerifyPopedom().equals(""))
					return retvals;
				if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
					where.append(" and ltrim(rtrim(ri.orgId)) || ri.templateId in("
							+ operator.getChildRepReportPopedom() + ")");
				if(Config.DB_SERVER_TYPE.equals("sqlserver"))
					where.append(" and ltrim(rtrim(ri.orgId)) + ri.templateId in("
							+ operator.getChildRepReportPopedom() + ")");
			}

			conn = new DBConn();
			session = conn.openSession();
			Query query = session.createQuery(hql.toString()+where.toString());

			List list = query.list();
			
			if (list != null && list.size() > 0) {
				
				retvals = new ArrayList();
				
				for (Iterator it = list.iterator(); it.hasNext();) {
					retvals.add((Long) it.next());
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

	/**
	 * jdbc���� oracle�﷨(rownum) ��Ҫ�޸� ���Ը� 2011-12-22
	 * Ӱ���VIEW_AF_REPORT af_report ORG_REP_ID
	 * ���������ݿ��ж�
	 * ���޸�Ϊsqlserver top  ���Ը� 2011-12-27 ������
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
	public static List getReportStatRecord(AFReportForm reportInForm,
			int offset, int limit, Operator operator) {
		List resList = new ArrayList();
		DBConn conn = null;
		Session session = null;

		if (reportInForm == null || operator == null)
			return resList;

		try {
			StringBuffer sql=null;
			if(Config.DB_SERVER_TYPE.equals("sqlserver"))
			{
				 sql= new StringBuffer(
						//select t.*,rownum from (
						"select top "+(offset + limit)+" a.ORG_ID,d.ORG_NAME,a.TEMPLATE_ID,a.VERSION_ID,a.TEMPLATE_TYPE,"
								+ "a.TEMPLATE_NAME,a.CUR_NAME,a.CUR_ID,a.REP_FREQ_NAME,a.REP_FREQ_ID,a.IS_REPORT,a.BAK1"
								 + ",ri.YEAR,ri.TERM,ri.DAY,ri.CHECK_FLAG,ri.REP_ID "
								+ " from VIEW_AF_REPORT a left join af_org d on a.org_id=d.org_id "
								+ "left join (select * from af_report   where times=1 "+getReportDateTSql(reportInForm)+") ri "
								+ "on a.ORG_ID=ri.ORG_ID and a.TEMPLATE_ID=ri.TEMPLATE_ID "
								+ "and a.VERSION_ID=ri.VERSION_ID and a.CUR_ID=ri.CUR_ID and a.REP_FREQ_ID=ri.REP_FREQ_ID");
			}
			if(Config.DB_SERVER_TYPE.equals("oracle"))
			{
				 sql= new StringBuffer(
							"select t.*,nvl(rf.FORCE_TYPE_ID,5) as FORCE_TYPE_ID,rownum from (select  a.ORG_ID,d.ORG_NAME,a.TEMPLATE_ID,a.VERSION_ID,a.TEMPLATE_TYPE,"
									+ "a.TEMPLATE_NAME,a.CUR_NAME,a.CUR_ID,a.REP_FREQ_NAME,a.REP_FREQ_ID,a.IS_REPORT,a.BAK1"
									 + ",ri.YEAR,ri.TERM,ri.DAY,ri.CHECK_FLAG,ri.REP_ID "
									+ " from VIEW_AF_REPORT a left join af_org d on a.org_id=d.org_id "
									+ "left join (select * from af_report where times=1 "+getReportDateTSql(reportInForm)+") ri "
									+ "on a.ORG_ID=ri.ORG_ID and a.TEMPLATE_ID=ri.TEMPLATE_ID "
									+ "and a.VERSION_ID=ri.VERSION_ID and a.CUR_ID=ri.CUR_ID and a.REP_FREQ_ID=ri.REP_FREQ_ID");
			}
			if(Config.DB_SERVER_TYPE.equals("db2")){
				 sql= new StringBuffer(
							"select * from ( select t.*,nvl(rf.FORCE_TYPE_ID,5) as FORCE_TYPE_ID,row_number() over(order by t.ORG_ID,t.TEMPLATE_ID,t.VERSION_ID) as rownum from (select  a.ORG_ID,d.ORG_NAME,a.TEMPLATE_ID,a.VERSION_ID,a.TEMPLATE_TYPE,"
									+ "a.TEMPLATE_NAME,a.CUR_NAME,a.CUR_ID,a.REP_FREQ_NAME,a.REP_FREQ_ID,a.IS_REPORT,a.BAK1"
									 + ",ri.YEAR,ri.TERM,ri.DAY,ri.CHECK_FLAG,ri.REP_ID "
									+ " from VIEW_AF_REPORT a left join af_org d on a.org_id=d.org_id "
									+ "left join (select * from af_report where times=1 "+getReportDateTSql(reportInForm)+") ri "
									+ "on a.ORG_ID=ri.ORG_ID and a.TEMPLATE_ID=ri.TEMPLATE_ID "
									+ "and a.VERSION_ID=ri.VERSION_ID and a.CUR_ID=ri.CUR_ID and a.REP_FREQ_ID=ri.REP_FREQ_ID");
			}
			StringBuffer where = new StringBuffer();
			if(StringUtil.isEmpty(reportInForm.getIsLeader())){
				where.append(" where 1=1 ");
				if(reportInForm.getTemplateType()!=null && !reportInForm.getTemplateType().equals(""))
					where.append(" and a.TEMPLATE_TYPE='"+ reportInForm.getTemplateType()+"' ");
				where.append(getValidDateSql(reportInForm).replaceAll("comp_id.repFreqId", "REP_FREQ_ID").
								replaceAll("startDate", "START_DATE").replaceAll("endDate", "END_DATE"));

			} else if(reportInForm.getIsLeader().equals("1")){
				where.append(" where a.IS_LEADER="
						+ reportInForm.getIsLeader()
						+ getValidDateSql(reportInForm).replaceAll("comp_id.repFreqId", "REP_FREQ_ID").
								replaceAll("startDate", "START_DATE").replaceAll("endDate", "END_DATE"));
			}
			/** ��ӱ����Ų�ѯ���������Դ�Сдģ����ѯ�� */
			if (reportInForm.getTemplateId() != null
					&& !reportInForm.getTemplateId().equals("")) {
				where.append(" and upper(a.TEMPLATE_ID) like upper('%"
						+ reportInForm.getTemplateId().trim() + "%')");
			}
			/** ��ӱ������Ʋ�ѯ������ģ����ѯ�� */
			if (reportInForm.getRepName() != null
					&& !reportInForm.getRepName().equals("")) {
				where.append(" and a.TEMPLATE_NAME like '%"
						+ reportInForm.getRepName().trim() + "%'");
			}

			/** ��ӱ���Ƶ�ȣ���/��/����/�꣩��ѯ���� */
			if (reportInForm.getRepFreqId() != null
					&& !String.valueOf(reportInForm.getRepFreqId()).equals(
							Config.DEFAULT_VALUE)) {
				where.append(" and a.REP_FREQ_ID="
						+ reportInForm.getRepFreqId());
			}
			if (reportInForm.getOrgId() != null
					&& !reportInForm.getOrgId().equals(Config.DEFAULT_VALUE)
					&& !reportInForm.getOrgId().trim().equals("")) {

				where.append(" and a.ORG_ID='" + reportInForm.getOrgId().trim()
						+ "'");
			}

			 /** ��ӱ���״̬��δ���/���ͨ��/���δͨ������ѯ���� */
			 if (reportInForm.getCheckFlag() != null
			 && !String.valueOf(reportInForm.getCheckFlag()).equals(
			 Config.DEFAULT_VALUE)) {
				 // ��ʱ��-10����δ�ϱ�
				 if (reportInForm.getCheckFlag().equals("-10")) {
					 where.append(" and (ri.CHECK_FLAG is null or ri.CHECK_FLAG >1) ");
				 } else if(reportInForm.getCheckFlag().equals("-8")){
					 where.append(" and  a.IS_REPORT=3");
				 } else {
					 where.append(" and ri.CHECK_FLAG in (" + reportInForm.getCheckFlag()+")");
				 }
			 }

			/** ���������� */
			if (reportInForm.getBak1() != null
					&& !reportInForm.getBak1().trim().equals(
							Config.DEFAULT_VALUE)
					&& !reportInForm.getBak1().equals("")) {
				where.append(" and a.BAK1 in (" + reportInForm.getBak1()+")");
			}
			/** �������� */
			if (reportInForm.getIsReport() != null
					&& !reportInForm.getIsReport().equals(
							Integer.valueOf(Config.DEFAULT_VALUE))) {
				where.append(" and a.IS_REPORT=" + reportInForm.getIsReport());
			}
			/**��������*/
			if(reportInForm.getSupplementFlag() !=null && !reportInForm.getSupplementFlag().equals("-999")){
				where.append(" and supplement_Flag= "+reportInForm.getSupplementFlag());
			}
			
			/**��������(bengin)*/
			if(reportInForm.getTaskId() != null && !reportInForm.getTaskId().equals("-999")){
				where.append(" and upper(a.TEMPLATE_ID) in (select v.template_id from view_work_task_template v where v.task_id="+reportInForm.getTaskId());
				if(reportInForm.getOrgId() != null && !reportInForm.getOrgId().equals(com.fitech.gznx.common.Config.HEAD_ORG_ID)
						&& !reportInForm.getOrgId().equals(Config.DEFAULT_VALUE)
						&& !reportInForm.getOrgId().trim().equals("")){
					System.out.println("---"+reportInForm.getOrgId());
					where.append(" and v.org_id='"+reportInForm.getOrgId()+"') ");
				}else{
					where.append(") ");
				}
			}
			/**��������(end)*/

			/** ��ӱ���鿴Ȩ�ޣ������û�������ӣ� 
			 * ���������ݿ��ж�*/
			if (operator.isSuperManager() == false) {
				if (operator.getChildRepSearchPopedom() != null
						&& !operator.getChildRepSearchPopedom().equals("")){
					if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
						where.append(" and a.ORG_ID||a.TEMPLATE_ID in "
										+ "(select ORG_REP_ID  from VIEW_ORG_REP where POW_TYPE=2 and USER_ID="
										+ operator.getOperatorId() + ")");
					if(Config.DB_SERVER_TYPE.equals("sqlserver"))
						where.append(" and a.ORG_ID+a.TEMPLATE_ID in "
										+ "(select ORG_REP_ID  from VIEW_ORG_REP where POW_TYPE=2 and USER_ID="
										+ operator.getOperatorId() + ")");
				}
			}

			if(Config.DB_SERVER_TYPE.equals("sqlserver"))
			{
				sql.append(where.toString()
							+ " order by a.ORG_ID,a.TEMPLATE_ID,a.VERSION_ID");
			}
			if(Config.DB_SERVER_TYPE.equals("oracle"))
			{
				sql.append(where.toString()
						+ " order by a.ORG_ID,a.TEMPLATE_ID,a.VERSION_ID ) t left join af_report_force_rep rf on t.rep_id = rf.rep_id where rownum<="+(offset+limit));
			}
			if(Config.DB_SERVER_TYPE.equals("db2")){
				sql.append(where.toString()
						+ " order by a.ORG_ID,a.TEMPLATE_ID,a.VERSION_ID ) t left join af_report_force_rep rf on t.rep_id = rf.rep_id ) p where rownum<="+(offset+limit));
			}
			//�ж�ǿ���ϱ�����
			if(reportInForm.getIsFlag() != null && !reportInForm.getIsFlag().equals("all")){
				if(reportInForm.getIsFlag().equals("force")){
					sql.append(" and nvl(rf.FORCE_TYPE_ID,5) in(-1,1,2)");
				}
				if(reportInForm.getIsFlag().equals("normal")){
					sql.append(" and nvl(rf.FORCE_TYPE_ID,5) not in(-1,1,2)");
				}
			}
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

				// ���ñ�������
				aditing.setRepName(rs.getString("TEMPLATE_NAME"));
				// ���ñ���������
				// aditing.setReportDate(reportInRecord.getReportDate());
				// ���ñ�����
				aditing.setChildRepId(rs.getString("TEMPLATE_ID"));
				// ���ñ���汾��
				aditing.setVersionId(rs.getString("VERSION_ID"));
				// ���ñ����������
				aditing.setCurrName(rs.getString("CUR_NAME"));
				// ���ñ������
				aditing.setCurId(rs.getInt("CUR_ID"));
				// ���ñ����ǿ���ϱ���״̬
				aditing.setIsFlag(rs.getInt("FORCE_TYPE_ID"));
				// ���ñ��Ϳھ�
				// aditing.setDataRgTypeName(rs.getString("DATA_RG_DESC"));
				// ���ñ���Ƶ��
				aditing.setActuFreqName(rs.getString("REP_FREQ_NAME"));

				aditing.setActuFreqID(rs.getInt("REP_FREQ_ID"));


				if (aditing.getActuFreqID() != null) {
					// yyyy-mm-dd ��������ȷ�������ھ������������
					String trueDate = DateUtil.getFreqDateLast(reportInForm
							.getDate(), aditing.getActuFreqID());
					aditing.setYear(Integer.valueOf(trueDate.substring(0, 4)));
					aditing.setTerm(Integer.valueOf(trueDate.substring(5, 7)));
					aditing.setDay(Integer.valueOf(trueDate.substring(8, 10)));
				}

				aditing.setOrgName(rs.getString("ORG_NAME"));
				aditing.setOrgId(rs.getString("ORG_ID"));
				


				if (rs.getInt("REP_ID") != 0) {
					//AfReport afr = (AfReport) lst.get(0);

					aditing.setRepInId(rs.getInt("REP_ID"));

					if (rs.getString("CHECK_FLAG") == null ||
							rs.getInt("CHECK_FLAG") > 1) {						
							aditing.setRepInId(null);
						
					} else {
						aditing.setRepInId(rs.getInt("REP_ID"));
						aditing.set_checkFlag(Short.valueOf(rs.getString("CHECK_FLAG")));

					}
					//��ѯ�౨��ֱ����0���Թ�ǰ̨��ѯ
				} else 
					if (rs.getString("IS_REPORT").equals(com.fitech.gznx.common.Config.TEMPLATE_VIEW)) {
					
					aditing.setRepInId(0);
					aditing.set_checkFlag((short)-8);
					
				} else {
					aditing.setRepInId(null);
				}
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
	 * JDBC���� ������oracle�﷨ ���Ը� 2011-12-27
	 * Ӱ���VIEW_AF_REPORT af_report VIEW_ORG_REP
	 * ����ͳ������
	 * 
	 * @author �ܷ���
	 * @param reportInForm
	 *            ����FormBean
	 * @param operator
	 *            ��ǰ��¼�û�
	 * @return ����ͳ������
	 */
	public static int getReportStatCount(AFReportForm reportInForm,
			Operator operator) {
		int count = 0;
		Connection conn = null;
		FitechConnection connFactory = null;
		Statement stmt = null;

		if (reportInForm == null || operator == null)
			return count;

		try {

			StringBuffer sql = new StringBuffer(
					"select count(*) from VIEW_AF_REPORT a left join af_org d on a.org_id=d.org_id "
					+ "left join (select * from af_report where times=1 "+getReportDateTSql(reportInForm)+") ri "
					+ "on a.ORG_ID=ri.ORG_ID and a.TEMPLATE_ID=ri.TEMPLATE_ID "
					+ "and a.VERSION_ID=ri.VERSION_ID and a.CUR_ID=ri.CUR_ID and a.REP_FREQ_ID=ri.REP_FREQ_ID left join af_report_force_rep rf on ri.rep_id = rf.rep_id");

			// + "left join af_report b "
			// + "on a.ORG_ID=b.ORG_ID and a.TEMPLATE_ID=b.TEMPLATE_ID "
			// + "and a.VERSION_ID=b.VERSION_ID and a.CUR_ID=b.CUR_ID and
			// a.REP_FREQ_ID=b.REP_FREQ_ID");

//			String[] rDate = reportInForm.getDate().split("-"); // �ձ���ѯ���
//			String[] tdDate = DateUtil.getLastTenDay(reportInForm.getDate())
//					.split("-"); // Ѯ����ѯ���
			StringBuffer where = new StringBuffer();
			if(StringUtil.isEmpty(reportInForm.getIsLeader())){
				where.append(" where 1=1 ");
				if(reportInForm.getTemplateType()!=null && !reportInForm.getTemplateType().equals(""))
					where.append(" and  a.TEMPLATE_TYPE='"+reportInForm.getTemplateType()+"' ");
				where.append(getValidDateSql(reportInForm).replaceAll("comp_id.repFreqId", "REP_FREQ_ID").
								replaceAll("startDate", "START_DATE").replaceAll("endDate", "END_DATE"));
			}else if(reportInForm.getIsLeader().equals("1")){
				where.append(" where a.IS_LEADER="
						+ reportInForm.getIsLeader()
						+ getValidDateSql(reportInForm).replaceAll("comp_id.repFreqId", "REP_FREQ_ID").
								replaceAll("startDate", "START_DATE").replaceAll("endDate", "END_DATE"));
			}

			/** ��ӱ����Ų�ѯ���������Դ�Сдģ����ѯ�� */
			if (reportInForm.getTemplateId() != null
					&& !reportInForm.getTemplateId().equals("")) {
				where.append(" and upper(a.TEMPLATE_ID) like upper('%"
						+ reportInForm.getTemplateId().trim() + "%')");
			}
			/** ��ӱ������Ʋ�ѯ������ģ����ѯ�� */
			if (reportInForm.getRepName() != null
					&& !reportInForm.getRepName().equals("")) {
				where.append(" and a.TEMPLATE_NAME like '%"
						+ reportInForm.getRepName().trim() + "%'");
			}

			/** ��ӱ���Ƶ�ȣ���/��/����/�꣩��ѯ���� */
			if (reportInForm.getRepFreqId() != null
					&& !String.valueOf(reportInForm.getRepFreqId()).equals(
							Config.DEFAULT_VALUE)) {
				where.append(" and a.REP_FREQ_ID="
						+ reportInForm.getRepFreqId());
			}
			
			/**��������*/
			if(reportInForm.getSupplementFlag() !=null && !reportInForm.getSupplementFlag().equals("-999")){
				where.append(" and supplement_Flag= "+reportInForm.getSupplementFlag());
			}
			/**��������(bengin)*/
			if(reportInForm.getTaskId() != null && !reportInForm.getTaskId().equals("-999")){
				where.append(" and upper(a.TEMPLATE_ID) in (select v.template_id from view_work_task_template v where v.task_id="+reportInForm.getTaskId());
				if(reportInForm.getOrgId() != null && !reportInForm.getOrgId().equals(com.fitech.gznx.common.Config.HEAD_ORG_ID)
						&& !reportInForm.getOrgId().equals(Config.DEFAULT_VALUE)
						&& !reportInForm.getOrgId().trim().equals("")){
					System.out.println("---"+reportInForm.getOrgId());
					where.append(" and v.org_id='"+reportInForm.getOrgId()+"') ");
				}else{
					where.append(") ");
				}
			}
			/**��������(end)*/
			
			if (reportInForm.getOrgId() != null
					&& !reportInForm.getOrgId().equals(Config.DEFAULT_VALUE)
					&& !reportInForm.getOrgId().trim().equals("")) {

				where.append(" and a.ORG_ID='" + reportInForm.getOrgId().trim()
						+ "'");
			}
			
			 /** ��ӱ���״̬��δ���/���ͨ��/���δͨ������ѯ���� */
			 if (reportInForm.getCheckFlag() != null
			 && !String.valueOf(reportInForm.getCheckFlag()).equals(
			 Config.DEFAULT_VALUE)) {
				 // ��ʱ��-10����δ�ϱ�
				 if (reportInForm.getCheckFlag().equals("-10")) {
					 where.append(" and (ri.CHECK_FLAG is null or ri.CHECK_FLAG >1) ");
				 } else if(reportInForm.getCheckFlag().equals("-8")){
					 where.append(" and  a.IS_REPORT=3");
				 } else {
					 where.append(" and ri.CHECK_FLAG in (" + reportInForm.getCheckFlag()+")");
				 }
			 }

			/** ���������� */
			if (reportInForm.getBak1() != null
					&& !reportInForm.getBak1().trim().equals(
							Config.DEFAULT_VALUE)
					&& !reportInForm.getBak1().equals("")) {
				where.append(" and a.bak1 in (" + reportInForm.getBak1()+")");
			}
			/** �������� */
			if (reportInForm.getIsReport() != null
					&& !reportInForm.getIsReport().equals(
							Integer.valueOf(Config.DEFAULT_VALUE))) {
				where.append(" and a.is_Report=" + reportInForm.getIsReport());
			}
			/** ��ӱ���鿴Ȩ�ޣ������û�������ӣ� 
			 * ���������ݿ��ж�*/
			if (operator.isSuperManager() == false) {
				if (operator.getChildRepSearchPopedom() != null
						&& !operator.getChildRepSearchPopedom().equals(""))
				{
					if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
					{
						where.append(" and a.ORG_ID||a.TEMPLATE_ID in (select ORG_REP_ID "
										+ " from VIEW_ORG_REP where POW_TYPE=2 and USER_ID="
										+ operator.getOperatorId() + ")");
					}
					if(Config.DB_SERVER_TYPE.equals("sqlserver"))
					{
						where.append(" and a.ORG_ID+a.TEMPLATE_ID in (select ORG_REP_ID "
								+ " from VIEW_ORG_REP where POW_TYPE=2 and USER_ID="
								+ operator.getOperatorId() + ")");
					}
				}
			}
			
			/**����Ƿ��ǲ�¼�ı����ѯ*/
			
//			String templatename = StrutsCodeLibDelegate.getCodeLib(com.fitech.gznx.common.Config.CUSTOM_SEARCH,"1").getCodeName();
//			where.append(" and a.TEMPLATE_NAME!='"+templatename+"'");
			sql.append(where.toString());
			//�ж�ǿ���ϱ�����
			if(reportInForm.getIsFlag() != null && !reportInForm.getIsFlag().equals("all")){
				//��ѯȫ����ǿ���ϱ��ı���
				if(reportInForm.getIsFlag().equals("force")){
					sql.append(" and rf.FORCE_TYPE_ID in(-1,1,2)");
				}
				//��ѯû��ǿ���ϱ��ı���
				if(reportInForm.getIsFlag().equals("normal")){
					sql.append(" and rf.FORCE_TYPE_ID is null");
				}
			}
			// + " order by a.ORG_ID,a.TEMPLATE_ID,a.VERSION_ID ) t ");

			// System.out.println("sql: "+sql);
//			conn = new DBConn();
//			session = conn.openSession();
//			Connection connection = session.connection();
			connFactory = new FitechConnection();
			
			conn = connFactory.getConn();
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql.toString());

			if (rs.next()) {
				count = rs.getInt(1);
			}
		} catch(Exception e){
			try {
				count = 0;
				log.printStackTrace(e);
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();			
		}finally{
				try {
				if(stmt!=null)
					stmt.close();
				if (conn != null){
					conn.close();
				}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return count;
	}
	
	/**
	 * �½��ر������趨
	 * 
	 * @author �ܷ���
	 * @return true �½��ɹ���false�½�ʧ�ܣ�
	 * @throws HibernateException
	 */
	public static boolean ForseReportAgainSetting(AFReportForm reportInForm)
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
			AfReport reportIn = (AfReport) session.load(AfReport.class, Long.valueOf(reportInForm.getRepId()));
			if (reportIn == null)
				return result;
			
			reportIn.setForseReportAgainFlag(Config.FORSE_REPORT_AGAIN_FLAG_1.longValue());
			reportIn.setCheckFlag(Config.CHECK_FLAG_FAILED.longValue());
					//Config.CHECK_FLAG_RECHECKFAILED.longValue());
			reportIn.setTblOuterValidateFlag(null);
			reportIn.setTblInnerValidateFlag(null);
			
			reportIn.setCheckFlag(Config.CHECK_FLAG_FAILED.longValue());
			
			reportIn.setReReportTimes(reportIn.getReReportTimes()==null ? 
										(long)1 : reportIn.getReReportTimes().longValue()+1);
			
			AfReportAgain reportAgainSet = new AfReportAgain();
			reportAgainSet.setCause(reportInForm.getCause());
			reportAgainSet.setSetDate(new Date());
			reportAgainSet.setRepId(reportIn.getRepId());
			reportAgainSet.setTemplateType(Long.valueOf(reportInForm.getTemplateType()));
			
			session.save(reportIn);
			session.save(reportAgainSet);
			
			//session.flush();
			tx.commit();
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
	 * ��ʹ��hibernate ���Ը� 2011-12-27
	 * Ӱ�����AfReport AfReportAgain
	 * �����ر�
	 * 
	 * @param pId String �ַ�������
	 * @param cause
	 *            String �����ر�ԭ��
	 * @return �ɹ�����true ʧ�ܷ���false
	 */
	public static boolean batchAgain(String pId[], String cause ,Integer templateType)
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
				
				AfReport reportIn = null;
				
				for (int i = 0; i < pId.length; i++) {
					reportIn = (AfReport) session.load(AfReport.class, Long.valueOf((pId[i])));
					
					if (reportIn != null) {
						reportIn.setForseReportAgainFlag(Config.FORSE_REPORT_AGAIN_FLAG_1.longValue());
						reportIn.setCheckFlag(Config.CHECK_FLAG_FAILED.longValue());
								//Config.CHECK_FLAG_RECHECKFAILED.longValue());
						reportIn.setReReportTimes(reportIn.getReReportTimes()==null ? 
								(long)1 : reportIn.getReReportTimes().longValue()+1);
						reportIn.setTblOuterValidateFlag(null);
						reportIn.setTblInnerValidateFlag(null);
						
						AfReportAgain reportAgainSet = new AfReportAgain();
						reportAgainSet.setCause(cause);
						reportAgainSet.setSetDate(new Date());
						reportAgainSet.setRepId(reportIn.getRepId());
						reportAgainSet.setTemplateType(templateType.longValue());

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
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * ��ѯ���ͨ���Ļ�������
	 * @param childRepId
	 * @param versionId
	 * @param year
	 * @param term
	 * @param day
	 * @param curId
	 * @param repFreqId
	 * @param childOrgIds
	 * @param orgId 
	 * @param type ��������
	 * @param ignoreCollectOrg ͳ���ж��¼����ϱ�����ʱ���Ƿ�������еĻ��ܻ���
	 * @return
	 */
	public static int getAvailabilityOrgIdCount(String childRepId,String versionId,
			Integer year, Integer term ,Integer day , Integer curId, 
			Integer repFreqId ,String childOrgIds,String orgId, Integer type,boolean ignoreCollectOrg){
		
		int result = 0;
		DBConn conn = null;
		Session session = null;
		
		if(childRepId == null || childRepId.equals("")
				|| versionId == null || versionId.equals("")
				)
			return result;
		
		String hql = null;
		//����ȡ�������
		if (type.equals(Integer.valueOf(1))){
			hql = "select count(ri.repId) from AfReport ri,AfOrg org where ri.orgId=org.orgId "
				+ " and org.isCollect=1"
			 	+ " and ri.templateId='" + childRepId
				+ "' and ri.versionId='" + versionId
				+ "' and ri.year="+ year
				+ " and ri.term="+ term
				+ " and ri.day="+ day
				+ " and ri.curId="+ curId
				+ " and ri.repFreqId="+ repFreqId
				//+ " and ri.orgId in ("+ childOrgIds + ")"
				+ " and ri.times>0"
				+ " and ri.checkFlag in(" + (Config.IS_NEED_CHECK==1 ? "1" : "0,1") + ")";
		}
		//�������ȡ������ʵ���������
		else if(type.equals(Integer.valueOf(2))){
			if(false && AFOrgDelegate.isExistAttrOrgId(orgId))//����û��������¼��������������ȡ���ܵĻ�������
				hql = "select count(af.id.orgId) from  AfCollectRelation af where af.id.collectId='" + orgId + "'";
			else{
				if(!ignoreCollectOrg)
					hql = "select count(ri.repId) from AfReport ri,AfCollectRelation afc where "
						+ "ri.orgId=afc.id.orgId and afc.id.collectId='" + orgId
					 	+ "' and ri.templateId='" + childRepId
						+ "' and ri.versionId='" + versionId
						+ "' and ri.year="+ year
						+ " and ri.term="+ term
						+ " and ri.day="+ day
						+ " and ri.curId="+ curId
						+ " and ri.repFreqId="+ repFreqId
			//			+ " and ri.orgId='"+ orgId + "'"
						+ " and ri.times>0"
						+ " and ri.checkFlag in(" + (Config.IS_NEED_CHECK==1 ? "1" : "0,1") + ")";
				else
					hql = "select count(ri.repId) from AfReport ri,AfCollectRelation afc,AfOrg org where "
						+ "ri.orgId=afc.id.orgId and afc.id.collectId='" + orgId
						+ "' and afc.id.orgId=org.orgId and org.isCollect=0"
					 	+ " and ri.templateId='" + childRepId
						+ "' and ri.versionId='" + versionId
						+ "' and ri.year="+ year
						+ " and ri.term="+ term
						+ " and ri.day="+ day
						+ " and ri.curId="+ curId
						+ " and ri.repFreqId="+ repFreqId
			//			+ " and ri.orgId='"+ orgId + "'"
						+ " and ri.times>0"
						+ " and ri.checkFlag in(" + (Config.IS_NEED_CHECK==1 ? "1" : "0,1") + ")";
			}
		}
		//�ӻ���
		else{
			hql = "select count(ri.repId) from AfReport ri where"
			 	+ " ri.templateId='" + childRepId
				+ "' and ri.versionId='" + versionId
				+ "' and ri.year="+ year
				+ " and ri.term="+ term
				+ " and ri.day="+ day
				+ " and ri.curId="+ curId
				+ " and ri.repFreqId="+ repFreqId
				//+ " and ri.orgId in ("+ childOrgIds + ")"
				+ " and ri.orgId in (select af.orgId from AfOrg af where af.preOrgId='"+ orgId+"')"
				+ " and ri.times>0"
				+ " and ri.checkFlag in(" + (Config.IS_NEED_CHECK==1 ? "1" : "0,1") + ")";
		}

		
		conn = new DBConn();
		session = conn.openSession();
		try {
			List list = session.createQuery(hql).list();
			
			if(list != null && list.size()>0){
				result = ((Integer)list.get(0)).intValue();
			}
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(conn!=null) conn.closeSession();
		}
		return result;
	}
	
	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * ��ѯ���ͨ���Ļ�������
	 * @param childRepId
	 * @param versionId
	 * @param year
	 * @param term
	 * @param day
	 * @param curId
	 * @param repFreqId
	 * @param childOrgIds
	 * @param orgId 
	 * @param type ��������
	 * @param ignoreCollectOrg ͳ���ж��¼����ϱ�����ʱ���Ƿ�������еĻ��ܻ���
	 * @return
	 */
	public static int getSimpleAvailabilityOrgIdCount(String childRepId,String versionId,
			Integer year, Integer term ,Integer day , Integer curId, 
			Integer repFreqId,String orgId){
		
		int result = 0;
		DBConn conn = null;
		Session session = null;
		
		if(childRepId == null || childRepId.equals("")
				|| versionId == null || versionId.equals("")
				)
			return result;
		
		String hql="from AfReport ri where  "
			 	+" ri.templateId='"+ childRepId+"'"
				+" and ri.versionId='"+versionId+"'"
			//	+" and ri.MDataRgType.dataRangeId="+aditing.getDataRangeId()
				+" and ri.year="+ year
				+" and ri.term="+ term
				+" and ri.day="+ day
				+" and ri.repFreqId="+repFreqId
				+" and ri.curId="+curId
				+" and ri.orgId='"+orgId+"'"
				+" and ri.times=1"
				+" and ri.checkFlag in(0,1)";
		
		conn = new DBConn();
		session = conn.openSession();
		try {
			List list = session.createQuery(hql).list();
			
			if(list != null && list.size()>0){
				result = list.size();
			}
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(conn!=null) conn.closeSession();
		}
		return result;
	}
	
	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * ���ұ���״̬
	 * @param childRepId
	 * @param versionId
	 * @param repFreqId
	 * @param year
	 * @param term
	 * @param day
	 * @param curId
	 * @param orgid
	 * @return
	 */
	public static Integer getReportState(String childRepId,String versionId,Integer repFreqId,int year,int term,int day,Integer curId,String orgid){
		Integer state = null;
		DBConn conn = null;
		Session session = null;
		
		if(childRepId == null || childRepId.equals("")
				|| versionId == null || versionId.equals("")
				|| orgid == null || orgid.equals(""))
			return state;
		String hql="select count(*) from AfReport ri where  "
			 	+" ri.templateId='"+childRepId+"'"
				+" and ri.versionId='"+versionId+"'"
				+" and ri.repFreqId="+repFreqId
				+" and ri.year="+year
				+" and ri.term="+term
				+" and ri.day="+day
				+" and ri.curId="+curId
				+" and ri.orgId='"+orgid+"'"
				+" and ri.times>0"
				+" and ri.checkFlag=" + com.fitech.net.config.Config.CHECK_FLAG_PASS;
	
		conn = new DBConn();
		session = conn.openSession();

		try {
			  Query query = session.createQuery(hql);
	          List list = query.list();
	    
	          if(list!=null && list.size()!=0)
	        	  state = (Integer)list.get(0);
	          
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(conn!=null) conn.closeSession();
		}
		return state;
	}
	
	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * ��ʵ�ʱ�����޸�
	 * 
	 * @author jcm 
	 * 
	 * @param reportIn
	 * @throws Exception
	 */
	public static boolean updateReport(AfReport reportIn)
			throws Exception {

		boolean bool = false;
		DBConn dBConn = null;
		Session session = null;

		try {
			dBConn = new DBConn();
			session = dBConn.beginTransaction();
			reportIn.setCheckFlag(com.fitech.net.config.Config.CHECK_FLAG_UNCHECK.longValue());
			reportIn.setForseReportAgainFlag(null);
			reportIn.setReportDate(new Date());
			
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
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * ��ʵ�ʱ�����޸�
	 * 
	 * @author jcm 
	 * 
	 * @param reportIn
	 * @throws Exception
	 */
	public static boolean updateReport(List reportList)
			throws Exception {

		boolean bool = false;
		DBConn dBConn = null;
		Session session = null;

		AfReport rep = null;
		
		try {
			dBConn = new DBConn();
			session = dBConn.beginTransaction();
			
			if(reportList!=null && reportList.size()>0) {
				for (Iterator it = reportList.iterator(); it.hasNext();){
					rep = (AfReport) it.next();

					rep.setCheckFlag(com.fitech.net.config.Config.CHECK_FLAG_UNCHECK.longValue());
					rep.setForseReportAgainFlag(null);
					rep.setReportDate(new Date());
					//reportIn.setReportDate(new Date());
				
				session.update(rep);
				}
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
	
	/***
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * @param reportMap
	 * @param operator
	 * @return
	 */
	public static String insertYJHReport(Map reportMap, Operator operator) {
		FitechMessages messages = new FitechMessages();
		ReportInForm reportInForm = null;
		ReportIn reportInTemp = null;
		String childRepId = (String) reportMap.get("templateId");
		String versionId = (String) reportMap.get("versionId");
		String dataRangeid = String.valueOf(reportMap.get("RangeID")) ;
		String orgId = (String) reportMap.get("OrgID");
		String time = (String) reportMap.get("ReptDate");

		String year = null;
		String term = null;
		if(!StringUtil.isEmpty(time)){
			year = time.substring(0, 4);
			term = time.substring(4, 6);
		}
		String reportName =null;

		try {
		// �����������
		/**��ʹ��hibernate ���Ը� 2011-12-21
		 * Ӱ�����MChildReport**/
		reportName =new StrutsMChildReportDelegate().getMChildReport(childRepId, versionId)
				.getReportName();
		/**��ñ���ı��֣�Ŀǰ��֧�ֶ���ֵ��������ɣ�*/
		String curId = String.valueOf(reportMap.get("CCY"));
		if (curId == null || curId.equals(""))
			curId = "1";
		/**��ʹ��hibernate ���Ը� 2011-12-21
		 * Ӱ�����MCurr**/
		 MCurr mCurr = StrutsMCurrDelegate.getISMCurr(curId.equals("01")?"1":curId);		
				
		if(dataRangeid == null){
			messages.add(reportName+"���ɿھ�����");
			return null;
		}
		
		/**�ж��Ƿ��иñ�������ɷ�Χ*/
		/**��ʹ��hibernate ���Ը� 2011-12-21
		 * Ӱ�����MRepRange**/
		if(StrutsMRepRangeDelegate.getMRepRanageOncb(orgId,childRepId,versionId) == null){
			messages.add(reportName+"���ɷ�Χ�����ڣ�");
			return null;
		}
		
		/**�ж��û��Ƿ��иñ��������Ȩ��*/
		if(operator.isSuperManager() == false){
			if(operator.getChildRepReportPopedom() == null || operator.getChildRepReportPopedom().equals("")){
				messages.add(reportName+"ȱ������Ȩ�ޣ�");							
				return null;
			}						
			/**��ʹ��hibernate ���Ը� 2011-12-21
			 * Ӱ�����MRepRange**/
			if(StrutsMRepRangeDelegate.getMRepRanage(orgId,childRepId,versionId,operator) == null){
				messages.add(reportName+"ȱ������Ȩ�ޣ�");
				return null;
			}
		}
		
		/**�ж����ɿھ��Ƿ���ȷ*/
		/**��ʹ��hibernate ���Ը� 2011-12-21
		 * Ӱ�����MActuRep MChildReport**/
		if(StrutsReportInDelegate.isExistDataRange(dataRangeid,childRepId,versionId) == false){
			messages.add(reportName+"���ɿھ�����");
			return null;
		}
		
		/**�ж�����Ƶ���Ƿ����*/
		/**��ʹ��hibernate ���Ը� 2011-12-21
		 * Ӱ�����MActuRep MChildReport MDataRgType MRepFreq*/
		if(StrutsReportInDelegate.isExistDataRange(Integer.valueOf(dataRangeid),childRepId
				,versionId,new Integer(term)) == false){
			messages.add(reportName+term+"�·ݲ���Ҫ���ɣ�");
			return null;
		}
		
		Integer repInId = null;
		//Ԥ�Ȳ����¼�¼
		reportInForm = new ReportInForm();
		reportInForm.setChildRepId(childRepId);
		reportInForm.setVersionId(versionId);
		reportInForm.setDataRangeId(Integer.valueOf(dataRangeid));
		reportInForm.setCurId(new Integer(curId));
		reportInForm.setYear(new Integer(year));
		reportInForm.setTerm(new Integer(term));
		reportInForm.setOrgId(orgId);
		reportInForm.setTimes(new Integer(1));
		reportInForm.setReportDate(new Date());
		reportInForm.setCheckFlag(com.fitech.net.config.Config.CHECK_FLAG_AFTERSAVE);
		reportInForm.setCurName(mCurr.getCurName());
		reportInForm.setRepName(reportName);
		reportInForm.setTblInnerValidateFlag(((short) 0));
		reportInForm.setTblOuterValidateFlag(((short) 0));
		
		// ��������
		ReportIn reportIn = insertNewyjhReport(reportInForm);
		if (reportIn != null)
			repInId = reportIn.getRepInId();
			return String.valueOf(repInId);
		}catch (Exception e){
			messages.add(reportName+"����ʧ�ܣ�");
			e.printStackTrace();
			return null;
		}
		
	}

	/***
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * Ӱ�����AfTemplate AfReport
	 * @param reportMap
	 * @param operator
	 * @param reportFlg
	 * @return
	 */
	public static String insertNXReport(Map reportMap, Operator operator,
			String reportFlg) {
		FitechMessages messages = new FitechMessages();
		AFReportForm reportInForm = null;
		ReportIn reportInTemp = null;
		String childRepId = (String) reportMap.get("templateId");
		String versionId = (String) reportMap.get("versionId");
		String dataRangeid = String.valueOf(reportMap.get("RangeID")) ;
		String orgId = (String) reportMap.get("OrgID");
		String time = (String) reportMap.get("ReptDate");
		
		String repFreqId = String.valueOf(reportMap.get("Freq"));
		String year = null;
		String term = null;
		String day = null;
		if(!StringUtil.isEmpty(time)){
			year = time.substring(0, 4);
			term = time.substring(4, 6);
			day = time.substring(6, 8);
		}
		String reportName =null;
		boolean result = false;
		AfTemplate afTemplateInfo = null;
		try {
		// �����������
		/**��ʹ��hibernate ���Ը� 2011-12-21
		 * Ӱ�����AfTemplate**/
		afTemplateInfo =AFReportProductDelegate.getAFTemplate(childRepId, versionId);
		reportName =afTemplateInfo.getTemplateName();
		
		/**��ñ���ı��֣�Ŀǰ��֧�ֶ���ֵ��������ɣ�*/
		String curId = String.valueOf(reportMap.get("CCY"));
		if (curId == null || curId.equals(""))
			curId = "1";
		 
		Integer repInId = null;
		//Ԥ�Ȳ����¼�¼
		reportInForm = new AFReportForm();
		reportInForm.setTemplateId(childRepId);
		reportInForm.setVersionId(versionId);
		reportInForm.setDataRangeId(dataRangeid);
		reportInForm.setCurId(curId);
		reportInForm.setYear(year);
		reportInForm.setTerm(term);
		reportInForm.setMonth(term);
		reportInForm.setRepFreqId(repFreqId);
		reportInForm.setDay(day);
		reportInForm.setRepName(reportName);
		reportInForm.setTimes("1");
		if(afTemplateInfo.getIsReport()!=null && afTemplateInfo.getIsReport().intValue()==1){
			reportInForm.setCheckFlag(com.fitech.net.config.Config.CHECK_FLAG_PASS.toString());
		} else {
			reportInForm.setCheckFlag(com.fitech.net.config.Config.CHECK_FLAG_AFTERSAVE.toString());
		}		
		reportInForm.setReportDate(new Date());
		
		reportInForm.setOrgId(orgId);
		
		reportInForm.setReportDate(new Date());
		
		reportInForm.setRepName(reportName);
		reportInForm.setTblInnerValidateFlag("0");
		reportInForm.setTblOuterValidateFlag("0");
		// ���뱨����Ϣ
		/**��ʹ��hibernate ���Ը� 2011-12-27
		 * Ӱ�����AfReport**/
		AfReport reportIn = insertNewReport(reportInForm,reportFlg);
		return String.valueOf(reportIn.getRepId());
		
		}catch (Exception e){
			messages.add(reportName+"����ʧ�ܣ�");
			e.printStackTrace();
			return null;
		}	
		
	}
	
	/***
	 * ��ʹ��hibernate ���Ը� 2011-12-27
	 * Ӱ�����AfReport
	 * @param reportInForm
	 * @param reportFlg
	 * @return
	 */
	private static AfReport insertNewReport(AFReportForm reportInForm,  String reportFlg) {
		if (reportInForm == null)
			return null;
		AfReport reportIn = null;
		//��ѯ���ݿ����Ƿ��Ѿ���������ͬ��¼
		reportIn = getReportIn(reportInForm);
		//������ݿ����Ѿ������˸�����¼����ֱ�ӷ��ظļ�¼����Ϣ�������µĲ��������������������
		if(reportIn!=null)
		{
			// ��ѯ�౨��
			return reportIn;
			
		}
		boolean result = false;

		DBConn conn = null;
		try
		{
			conn = new DBConn();
			Session session = conn.beginTransaction();
			
			
			//����������µ���Ϣ
			reportIn = new AfReport();
			copyVoToPersistence(reportIn, reportInForm);
			session.save(reportIn);
			result = true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			result = false;
		}
		finally
		{
			if (conn != null)
				conn.endTransaction(result);
		}
		return reportIn;
	}
	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-22
	 * �޸ı����ϱ���־
	 * @param repId
	 * @param reportFlg
	 * @return
	 */
	public static boolean updateNewReport( String repId,String reportFlg) {
		if (repId == null)
			return false;
				
		boolean result = false;

		DBConn conn = null;
		try
		{
			conn = new DBConn();
			Session session = conn.beginTransaction();
			
			if(reportFlg.equals(com.fitech.gznx.common.Config.CBRC_REPORT)){
				//����������µ���Ϣ
				ReportIn reportIn = new ReportIn();
				reportIn = (ReportIn) session.load(ReportIn.class, Integer.valueOf(repId));
				if(reportIn != null ){
					reportIn.setTblInnerValidateFlag(null);
					reportIn.setTblOuterValidateFlag(null);
					reportIn.setCheckFlag(Config.CHECK_FLAG_AFTERSAVE);
					//reportIn.setForseReportAgainFlag(null);
					session.update(reportIn);
				}		
			} else {
				//����������µ���Ϣ
				AfReport reportIn = new AfReport();
				reportIn = (AfReport) session.load(AfReport.class, Long.valueOf(repId));
				if(reportIn != null ){
					reportIn.setTblInnerValidateFlag(null);
					reportIn.setTblOuterValidateFlag(null);
					reportIn.setCheckFlag(new Long(Config.CHECK_FLAG_AFTERSAVE));
					//reportIn.setForseReportAgainFlag(null);
					session.update(reportIn);
				}			
			}
			
			session.flush();
			result = true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			result = false;
		}
		finally
		{
			if (conn != null)
				conn.endTransaction(result);
		}
		return result;
	}


	private static void copyVoToPersistence(AfReport reportIn,
			AFReportForm reportInForm) {
		reportIn.setTemplateId(reportInForm.getTemplateId());
		reportIn.setVersionId(reportInForm.getVersionId());
		reportIn.setCurId(new Long(reportInForm.getCurId()));
		reportIn.setYear(new Long(reportInForm.getYear()));
		reportIn.setTerm(new Long(reportInForm.getMonth()));
		reportIn.setDay(new Long(reportInForm.getDay()));
		reportIn.setOrgId(reportInForm.getOrgId());
		reportIn.setCheckFlag(Long.valueOf(reportInForm.getCheckFlag()));
		reportIn.setTimes(new Long(reportInForm.getTimes()));
		reportIn.setRepFreqId(new Long(reportInForm.getRepFreqId()));
		reportIn.setWriter(reportInForm.getWriter());
		reportIn.setRepName(reportInForm.getRepName());
		reportIn.setReportDate(reportInForm.getReportDate());
		reportIn.setTblInnerValidateFlag(new Long(reportInForm.getTblInnerValidateFlag()));
		reportIn.setTblOuterValidateFlag(new Long(reportInForm.getTblOuterValidateFlag()));
	}
	
	/**
	 * ��ʹ��Hibernate ���Ը� 2011-12-27
	 * Ӱ�����ReportIn ReportIn.MChildReport ReportIn.MCurr ReportIn.MDataRgType
	 * ���汨�������Ϣ(��ǰ���������򸲸Ǹļ�¼)
	 * 
	 * @author Yao
	 * @param reportInForm
	 * @return
	 * @throws Exception
	 * 
	 */
	public static ReportIn insertNewyjhReport(ReportInForm reportInForm) throws Exception {
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
			/**��ʹ��Hibernate ���Ը� 2011-12-27
			 * Ӱ�����ReportIn ReportIn.MChildReport ReportIn.MCurr ReportIn.MDataRgType**/
			reportIn = getReportIn(childRepId, versionId, orgId, year, term,
					dataRangeId, curId, new Integer(1));
			// ������ݿ����Ѿ������˸�����¼����ֱ�ӷ��ظļ�¼����Ϣ�������µĲ��������������������
			if (reportIn != null) {
				result = true;
				return reportIn;
			}
			// ����������µ���Ϣ
			reportIn = new ReportIn();
			copyVoToPersistence(reportIn, reportInForm);
			/**��ʹ��hibernate ���Ը� 2011-12-27
			 * Ӱ�����ReportIn*/
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
	
    private static void copyVoToPersistence(
            com.cbrc.smis.hibernate.ReportIn reportInPersistence,
            com.cbrc.smis.form.ReportInForm reportInForm) throws Exception {
        // Persistence layer specific implementation
        reportInPersistence.setRepInId(reportInForm.getRepInId());
        
        if(reportInForm.getChildRepId()!=null && reportInForm.getVersionId()!=null)
        {
        	 MChildReport mchildReport = new MChildReport();
             mchildReport.setComp_id(new MChildReportPK(reportInForm.getChildRepId(),reportInForm.getVersionId())); 
             reportInPersistence.setMChildReport(mchildReport);
        }
        if(reportInForm.getDataRangeId()!=null)
        	reportInPersistence.setMDataRgType(new MDataRgType(reportInForm.getDataRangeId()));
  
        if(reportInForm.getCurId()!=null)
        	reportInPersistence.setMCurr(new MCurr(reportInForm.getCurId()));
        
        reportInPersistence.setTerm(reportInForm.getTerm());
        reportInPersistence.setTimes(reportInForm.getTimes());
        reportInPersistence.setOrgId(reportInForm.getOrgId());
        reportInPersistence.setYear(reportInForm.getYear());
        reportInPersistence.setTblOuterValidateFlag(reportInForm
                .getTblOuterValidateFlag());
        reportInPersistence.setReportDataWarehouseFlag(reportInForm
                .getReportDataWarehouseFlag());
        reportInPersistence.setTblInnerValidateFlag(reportInForm
                .getTblInnerValidateFlag());
        reportInPersistence.setRepName(reportInForm.getRepName());
        reportInPersistence.setCheckFlag(reportInForm.getCheckFlag());
        reportInPersistence.setPackage(reportInForm.getPackage());
 
        reportInPersistence.setReportDate((java.util.Date) reportInForm
                .getReportDate());
        reportInPersistence.setAbmormityChangeFlag(reportInForm
                .getAbmormityChangeFlag());
        reportInPersistence.setRepRangeFlag(reportInForm.getRepRangeFlag());
        reportInPersistence.setForseReportAgainFlag(reportInForm
                .getForseReportAgainFlag());
        reportInPersistence.setLaterReportDay(reportInForm.getLaterReportDay());
        reportInPersistence.setNotReportFlag(reportInForm.getNotReportFlag());
        reportInPersistence.setWriter(reportInForm.getWriter());
        reportInPersistence.setChecker(reportInForm.getChecker());
        reportInPersistence.setPrincipal(reportInForm.getPrincipal());
        reportInPersistence.setTblOuterInvalidateCause(reportInForm
                .getTblOuterInvalidateCause());
    }
    
	/**
	 * ��ʹ��Hibernate ���Ը� 2011-12-27
	 * Ӱ�����ReportIn ReportIn.MChildReport ReportIn.MCurr ReportIn.MDataRgType
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
	private static ReportIn getReportIn(String childRepId, String versionId,
			String orgId, Integer year, Integer term, Integer dataRangeId,
			Integer curId, Integer times) {
		if (orgId == null || year == null || term == null
				|| dataRangeId == null || curId == null)
			return null;
		ReportIn reportIn = null;

		DBConn conn = null;
		try {
			conn = new DBConn();
			Session session = conn.openSession();
			// ��ѯ�Ƿ�������ؼ�¼��Ϣ
			StringBuffer selectHQL = new StringBuffer(
					"from ReportIn ri where  ");
			selectHQL
					.append("ri.MChildReport.comp_id.versionId=:versionId and ");
			selectHQL
					.append("ri.MChildReport.comp_id.childRepId=:childRepId and ");
			selectHQL.append("ri.orgId=:orgId and ");
			selectHQL.append("ri.MCurr.curId=:mCurrId and ");
			selectHQL.append("ri.MDataRgType.dataRangeId=:dataRangeId and ");
			selectHQL.append("ri.year=:year and ");
			selectHQL.append("ri.term=:term and ");
			selectHQL.append("ri.times=:times");

			Query query = session.createQuery(selectHQL.toString());
			query.setString("childRepId", childRepId);
			query.setString("versionId", versionId);
			query.setString("orgId", orgId.trim());
			query.setInteger("mCurrId", curId.intValue());
			query.setInteger("dataRangeId", dataRangeId.intValue());
			query.setInteger("year", year.intValue());
			query.setInteger("term", term.intValue());
			query.setInteger("times", times.intValue());
			List queryList = query.list();

			// ������ݿ����Ѿ������˸�����¼����ֱ�ӷ��ظļ�¼����Ϣ�������µĲ��������������������
			if (queryList != null && queryList.size() > 0) {
				reportIn = (ReportIn) queryList.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			reportIn = null;
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return reportIn;

	}
	
    /**
     * ���ҵ���ͬ����ת�ϱ����ݵ�����ʵ�ʱ������
     * @param repInId       Integer         ����ʵ�ʱ���Id
     * @return  AfReport
     */
    public static AfReport findHasTranslation(Integer repInId) {
        AfReport reportIn = null;       
        DBConn conn=null;
        Session session=null;    
        
        if( repInId == null ) return null;     
            
        try{
            conn = new DBConn();
            session = conn.openSession();
            
            //��ȡת�ϱ����ݵ�����ʵ�ʱ������
            AfReport _reportIn = (AfReport)session.get(AfReport.class, repInId.longValue());
            
            if(_reportIn != null){
            	
                String hql = "from AfReport t where t.times = (select max(p.times) "
                        + "from AfReport p where "
                        + "p.templateId='" + _reportIn.getTemplateId()
                        + "' and p.versionId='" + _reportIn.getVersionId()
                        + "' and p.year=" + _reportIn.getYear()
                        + " and p.term=" + _reportIn.getTerm()
                        + " and p.day=" + _reportIn.getDay()
                        + " and p.orgId='" + _reportIn.getOrgId()
                        + "' and p.repFreqId=" + _reportIn.getRepFreqId()
                        + " and p.curId=" + _reportIn.getCurId()
                        + " and p.times > 0)"
                        + " and t.templateId='" + _reportIn.getTemplateId()
                        + "' and t.versionId='" + _reportIn.getVersionId()
                        + "' and t.year=" + _reportIn.getYear()
                        + " and t.term=" + _reportIn.getTerm()
                        + " and t.day=" + _reportIn.getDay()
                        + " and t.orgId='" + _reportIn.getOrgId() +"'"
                        + "' and t.repFreqId=" + _reportIn.getRepFreqId()
                        + " and t.curId=" + _reportIn.getCurId();
                        
                reportIn = (AfReport)session.createQuery(hql).uniqueResult();
            }
            
        }catch(Exception he){
           log.printStackTrace(he);
        }finally{              
            if (conn!=null) conn.closeSession();         
        }
        return reportIn;
    }
    
    /**
     * ������������AfReport
     * @param repInId  Long  ����ʵ�ʱ���Id
     * @return
     */
    public static AfReport findById(Long repInId) {
    	
        AfReport reportIn = null;       
        DBConn conn=null;
        Session session=null;    
        
        if (repInId == null) return null;     
            
        try {
            conn = new DBConn();
            session = conn.openSession();
            reportIn = (AfReport)session.get(AfReport.class,repInId);
            
        } catch(Exception he) {
        	
           log.printStackTrace(he);
           
        } finally {
        	
            if (conn!=null) conn.closeSession();
        }
        return reportIn;
    }
    
    public static AfReport findReport(AfReport afReport) {
        AfReport reportIn = null;       
        DBConn conn=null;
        Session session=null;    
        
        if( afReport == null ) return null;     
            
        try{
            conn = new DBConn();
            session = conn.openSession();
                        
            	
            String hql = "from AfReport t where t.times = 1"
                    + " and t.templateId='" + afReport.getTemplateId()
                    + "' and t.versionId='" + afReport.getVersionId()
                    + "' and t.year=" + afReport.getYear()
                    + " and t.term=" + afReport.getTerm()
                    + " and t.day=" + afReport.getDay()
                    + " and t.orgId='" + afReport.getOrgId() +"'"
                    + " and t.repFreqId=" + afReport.getRepFreqId()
                    + " and t.curId=" + afReport.getCurId();
            
            reportIn = (AfReport)session.createQuery(hql).uniqueResult();
            
            
        }catch(Exception he){
           log.printStackTrace(he);
        }finally{              
            if (conn!=null) conn.closeSession();         
        }
        return reportIn;
    }
    
	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-26
	 * Ӱ�����AfTemplate AfReport
	 * ������ڱ��ͱ���
	 * 
	 * @param reportInForm
	 *            ҳ��FormBean
	 * @param operator
	 *            ��ǰ��¼�û�
	 * @return List ��ѯ��������
	 */
	public static List reportReports(AFReportForm reportInForm, Operator operator,Integer checkFlag) {
		List reps = null;
		DBConn conn = null;
		Session session = null;

		if (reportInForm == null || operator == null)
			return reps;
		
		try {
			// ��ѯ����HQL������
			StringBuffer hql = new StringBuffer(
					"select ri from AfReport ri, AfTemplate at WHERE "
							+ "ri.times=1 and ri.templateId=at.id.templateId "
							+ "and ri.versionId=at.id.versionId ");

			/** ��ѯ����״̬Ϊδ���˱��� */
			StringBuffer where = new StringBuffer(" and ri.checkFlag="
					+ checkFlag +" and at.templateType='" 
					+ reportInForm.getTemplateType()+"'");

			/** ��ӱ����Ų�ѯ���������Դ�Сдģ����ѯ�� */
			if (reportInForm.getTemplateId() != null
					&& !reportInForm.getTemplateId().equals("")) {
				where.append(" and upper(ri.templateId) like upper('%"
						+ reportInForm.getTemplateId().trim() + "%')");
			}
			/** ��ӱ������Ʋ�ѯ������ģ����ѯ�� */
			if (reportInForm.getRepName() != null
					&& !reportInForm.getRepName().equals("")) {
				where.append(" and ri.repName like '%"
						+ reportInForm.getRepName().trim() + "%'");
			}
			
			/** ��ӱ������Ͳ�ѯ���� */
			if (reportInForm.getBak1() != null
					&& !reportInForm.getBak1().equals("")) {
				where.append(" and at.bak1 in (" + reportInForm.getBak1().trim() +")");
			}
			
			/** ������ѯ */
			if (reportInForm.getDate() != null
					&& !reportInForm.getDate().equals("")) {
				where.append(getReportDateSql(reportInForm));
			}

			/** ��ӱ���Ƶ�ȣ���/��/����/�꣩��ѯ���� */
			if (reportInForm.getRepFreqId() != null
					&& !String.valueOf(reportInForm.getRepFreqId()).equals(
							Config.DEFAULT_VALUE)) {
				where.append(" and ri.repFreqId=" + reportInForm.getRepFreqId());
			}

			/** ��ӻ�����ѯ���� */
			if (reportInForm.getOrgId() != null
					&& !reportInForm.getOrgId().equals(Config.DEFAULT_VALUE)
					&& !reportInForm.getOrgId().trim().equals("")) {

				where.append(" and ri.orgId='" + reportInForm.getOrgId().trim()
						+ "'");
			}

			/** ��ӱ�����Ȩ�ޣ������û�������ӣ� 
			 * ���������ݿ��ж�*/
			if (operator.isSuperManager() == false) {
				if (operator.getChildRepVerifyPopedom() == null
						|| operator.getChildRepVerifyPopedom().equals(""))
					return reps;
				if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
					where.append(" and ltrim(rtrim(ri.orgId)) || ri.templateId in("
							+ operator.getChildRepReportPopedom() + ")");
				if(Config.DB_SERVER_TYPE.equals("sqlserver"))
					where.append(" and ltrim(rtrim(ri.orgId)) + ri.templateId in("
							+ operator.getChildRepReportPopedom() + ")");
			}

			hql.append(where.toString());

			conn = new DBConn();
			session = conn.openSession();
			Query query = session.createQuery(hql.toString());
			List list = query.list();

			if (list != null && list.size() > 0) {
				reps = new ArrayList();
				reps = list;
			}
		} catch (HibernateException he) {
			reps = null;
			log.printStackTrace(he);
		} catch (Exception e) {
			reps = null;
			log.printStackTrace(e);
		} finally {
			// �ر����ݿ�����
			if (conn != null)
				conn.closeSession();
		}
		return reps;
	}
    
	/**
	 * ����hibernate ���Ը� 2011-12-22
	 * Ӱ�����AfViewReport AfReport
	 * <p> 
	 * ����:�����Ҫ��������
	 * </p>
	 * <p>
	 * ����:
	 * </p>
	 * <p>
	 * ���ڣ�2008-1-9
	 * </p>
	 * <p>
	 * ���ߣ��ܷ���
	 * </p>
	 */
	public static PageListInfo selectNeedGatherReportRecord(AFReportForm afReportForm,
			Operator operator,int curPage) {
		List result = new ArrayList();
		DBConn conn = null;
		Session session = null;
		PageListInfo pageListInfo = null;
		try {

			conn = new DBConn();
			session = conn.openSession();

			String hql = "from AfViewReport a where a.templateType='"
					+ afReportForm.getTemplateType()+"'"
					+ getValidDateSql(afReportForm);

			/** ���ϱ���Ȩ�� 
			 * ���������ݿ��ж�*/
			if (operator.isSuperManager() == false) {
				if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
					hql += " and a.comp_id.orgId ||a.comp_id.templateId in ("
							+ operator.getChildRepReportPopedom() + ")";
				if(Config.DB_SERVER_TYPE.equals("sqlserver"))
					hql += " and a.comp_id.orgId + a.comp_id.templateId in ("
							+ operator.getChildRepReportPopedom() + ")";
			}
			String repName = afReportForm.getRepName();
			if (repName != null && !"".equals(repName.trim())) {
				hql += " and a.templateName like '%" + repName + "%' ";
			}
			
			if (afReportForm.getOrgId() != null && !afReportForm.getOrgId().equals("")
					&& !afReportForm.getOrgId().equals(Config.DEFAULT_VALUE)
					&& !afReportForm.getOrgId().trim().equals("")) {

				hql += " and a.comp_id.orgId='" + afReportForm.getOrgId() + "'";
			}

			if(afReportForm.getRepFreqId()!= null && !afReportForm.getRepFreqId().equals("")
					&& !afReportForm.getRepFreqId().equals(Config.DEFAULT_VALUE)) {
				hql += " and a.comp_id.repFreqId=" + afReportForm.getRepFreqId();
			}
//			String templatename = StrutsCodeLibDelegate.getCodeLib(com.fitech.gznx.common.Config.CUSTOM_SEARCH,"1").getCodeName();
//			hql += " and a.templateName!='"+templatename+"' ";
			hql += " and a.isReport="
					+ com.fitech.gznx.common.Config.TEMPLATE_ANALYSIS;
			/**order by ���;ۺϺ�����ͻ ��ע�� ���Ը� 2011-12-29
			 * ���������ݿ��ж�**/
			if(Config.DB_SERVER_TYPE.equals("oracle"))
				hql+= " order by a.comp_id.templateId, a.comp_id.repFreqId";
			if(Config.DB_SERVER_TYPE.equals("sqlserver") || Config.DB_SERVER_TYPE.equals("db2"))
				hql+="";
			
			/**��ʹ��hibernate ���Ը� 2011-12-21**/
			pageListInfo = findByPageWithSQL(session,hql,Config.PER_PAGE_ROWS,curPage);

			List list=pageListInfo.getList();
			for (int i = 0; i < list.size(); i++) {
				AfViewReport viewMReport = (AfViewReport) list.get(i);
				Aditing aditing = new Aditing();
				aditing.setActuFreqName(viewMReport.getRepFreqName());
				aditing.setActuFreqID(viewMReport.getComp_id().getRepFreqId());
				aditing.setChildRepId(viewMReport.getComp_id().getTemplateId());
				aditing.setVersionId(viewMReport.getComp_id().getVersionId());
				aditing.setRepName(viewMReport.getTemplateName());
				if (viewMReport.getComp_id().getRepFreqId() != null) {
					// yyyy-mm-dd ��������ȷ�������ھ������������
					String trueDate = DateUtil
							.getFreqDateLast(afReportForm.getDate(),
									viewMReport.getComp_id().getRepFreqId());
					aditing.setYear(Integer.valueOf(trueDate.substring(0, 4)));
					aditing.setTerm(Integer.valueOf(trueDate.substring(5, 7)));
					aditing.setDay(Integer.valueOf(trueDate.substring(8, 10)));
				}
				aditing.setCurrName(viewMReport.getCurName());
				aditing.setCurId(viewMReport.getComp_id().getCurId());
				aditing.setOrgId(viewMReport.getComp_id().getOrgId());
				AfOrg afOrg = AFOrgDelegate.getOrgInfo(viewMReport.getComp_id().getOrgId());
				aditing.setOrgName(afOrg.getOrgName());
				aditing.setReportStyle(viewMReport.getReportStyle());
				String sql = "from AfReport ri where ri.times=1"
						+ " and ri.versionId='" + aditing.getVersionId()
						+ "' and ri.templateId='" + aditing.getChildRepId()
						+ "' and ri.curId=" + aditing.getCurId()
						+ " and ri.year=" + aditing.getYear() + " and ri.term="
						+ aditing.getTerm() + " and ri.day=" + aditing.getDay()
						+ " and ri.orgId='" + aditing.getOrgId()
						+ "' and ri.repFreqId=" + aditing.getActuFreqID();

				List reportInList = session.find(sql);

				if (reportInList != null && reportInList.size() > 0) {
					AfReport report = (AfReport) reportInList.get(0);
					aditing.setCheckFlag(report.getCheckFlag().shortValue());
					aditing.setRepInId(report.getRepId().intValue());
				} else {
					aditing.setCheckFlag(Config.CHECK_FLAG_UNREPORT);
				}
				result.add(aditing);
			}
			pageListInfo.setList(result);
		} catch (Exception he) {
			log.printStackTrace(he);

		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return pageListInfo;
	}

	
	
	
	public static Aditing getReportIn(String templateId ,String  versionId ,String orgId ,String date){
		if (templateId == null||versionId ==null||orgId==null)
			return null;
		Aditing aditing = null;
		DBConn conn = null;
		List list = null;
		try {
			conn = new DBConn();
			Session session = conn.openSession();
			String hql = "from AfViewReport a where a.comp_id.templateId='"+templateId+"' and a.comp_id.versionId='"+versionId+"' and a.comp_id.orgId='"+orgId+"'";
			Query query = session.createQuery(hql);
			list = query.list();
			//System.out.println(list.size());
			AfViewReport reportIn = (AfViewReport) list.get(0);
			if (reportIn != null) {
				AfViewReport viewMReport = (AfViewReport) list.get(0);
				aditing = new Aditing();
				aditing.setTemplateId(viewMReport.getComp_id().getTemplateId());
				aditing.setActuFreqName(viewMReport.getRepFreqName());
				aditing.setActuFreqID(viewMReport.getComp_id().getRepFreqId());
				aditing.setChildRepId(viewMReport.getComp_id().getTemplateId());
				aditing.setVersionId(viewMReport.getComp_id().getVersionId());
				aditing.setRepName(viewMReport.getTemplateName());
				if (viewMReport.getComp_id().getRepFreqId() != null) {
					// yyyy-mm-dd ��������ȷ�������ھ������������
					String trueDate = DateUtil
							.getFreqDateLast(date+"-01",
									viewMReport.getComp_id().getRepFreqId());
					aditing.setYear(Integer.valueOf(trueDate.substring(0, 4)));
					aditing.setTerm(Integer.valueOf(trueDate.substring(5, 7)));
					aditing.setDay(Integer.valueOf(trueDate.substring(8, 10)));
				}
				aditing.setCurrName(viewMReport.getCurName());
				aditing.setCurId(viewMReport.getComp_id().getCurId());
				aditing.setOrgId(viewMReport.getComp_id().getOrgId());
				aditing.setOrgName(AFOrgDelegate.getOrgInfo(
						viewMReport.getComp_id().getOrgId()).getOrgName());


			}
		} catch (HibernateException he) {
			log.printStackTrace(he);
			aditing = null;
		} catch (Exception e) {
			log.printStackTrace(e);
			aditing = null;
		} finally {
			if (conn != null)
				conn.closeSession();
		}

		return aditing;
	

	}
	/**
	 * ȡ�ñ���ID
	 * @param pbocForm
	 * @param templateId
	 * @param versionId
	 * @param reqfreqId 
	 * @return
	 */
	public static String getReportId(AFPBOCReportForm pbocForm,
			String templateId, String versionId, String reqfreqId) {		
		// TODO Auto-generated method stub
		String repInId = null;
		DBConn conn = null;
		Session session = null;
		try {
			if(pbocForm != null && templateId != null && versionId != null){
			conn = new DBConn();
			session = conn.beginTransaction();
			String[] dates = pbocForm.getDate().split("-");
			String sql = "select a.rep_id from af_report a where a.template_id='"+templateId+"' and a.version_id='"+versionId+"' and a.org_id='"+pbocForm.getOrgId()+"' and a.rep_freq_id='"+reqfreqId+"' and a.term='"+dates[1]+"' and a.year='"+dates[0]+"' and a.rep_name is not null";
			Connection connection =session.connection();
			ResultSet rs=connection.createStatement().executeQuery(sql);
			while(rs.next()){
				repInId=rs.getString("rep_id");
			}
			}
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return repInId;
	}
	
	/**
	 * jdbc���� ������oracle�﷨ ����Ҫ�޸� ���Ը� 2011-12-21
	 * ȡ�ñ���ID
	 * @param pbocForm
	 * @param templateId
	 * @param versionId
	 * @param reqfreqId 
	 * @return
	 */
	public static int getSysParameter(String parameter) {		
		// TODO Auto-generated method stub
		int repInId = 0;
		DBConn conn = null;
		Session session = null;
		try {
			if(parameter != null ){
			conn = new DBConn();
			session = conn.beginTransaction();

			String sql = "select a.par_value from sys_parameter a where a.par_name='"+parameter+"'";
			Connection connection =session.connection();
			ResultSet rs=connection.createStatement().executeQuery(sql);
			if(rs.next()){
				repInId=rs.getInt(1);
			}
			}
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return repInId;
	}

	public static FitechMessages getMessages() {
		return messages;
	}

	public static void setMessages(FitechMessages messages) {
		AFReportDelegate.messages = messages;
	}
//	/**
//	 * ��ѯ���е�����
//	 * @return ���񼯺�
//	 */
//	public static List getTaskInfo(){
//		List resList = null;
//		DBConn conn = null;
//		Session session = null;
//		try {
//			conn = new DBConn();
//			session = conn.openSession();
//			String hql = "from ViewWorkTaskInfo v";
//			Query query = session.createQuery(hql);
//			resList = query.list();
//		} catch (Exception e) {
//			log.printStackTrace(e);
//		}finally{
//			if (conn != null)
//				conn.closeSession();
//		}
//		return resList;
//	}
	/**
	 * ��ѯ���е�����
	 * @return ���񼯺�
	 */
	public static List getTaskInfo(String reportFlag){
		List resList = new ArrayList();
		DBConn conn = null;
		Session session = null;
		ResultSet rs =null;
		try {
			if(StringUtil.isEmpty(reportFlag))
				return null;
			String busi_line_id = null;
			if("1".equals(reportFlag)){
				busi_line_id = "yjtx";
			}else if("2".equals(reportFlag)){
				busi_line_id = "rhtx";
			}else if("3".equals(reportFlag)){
				busi_line_id = "qttx";
			}
			conn = new DBConn();
			session = conn.openSession();
//			String hql = "from ViewWorkTaskInfo v";
			String sql = "select w.task_id,w.task_name from work_task_info w where w.busi_line_id='"+busi_line_id+"'";
			rs = session.connection().createStatement().executeQuery(sql);
			if(rs==null)return null;
			while(rs.next()){
				ViewWorkTaskInfo v = new ViewWorkTaskInfo();
				ViewWorkTaskInfoId vid = new ViewWorkTaskInfoId();
				vid.setTaskId(rs.getInt("task_id"));
				vid.setTaskName(rs.getString("task_name"));
				v.setId(vid);
				resList.add(v);
			}
		} catch (Exception e) {
			log.printStackTrace(e);
		}finally{
			try {
				if(rs!=null)
					rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (conn != null)
				conn.closeSession();
		}
		return resList;
	}
	/**
	 * ����reportIn����Ϣ
	 * @param repId
	 */
	public static Integer saveReportInfo(Long repInId)throws Exception{
		DBConn conn = null;
		Integer maxRepId =null;
		Connection orclCon = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			conn = new DBConn();
			orclCon = conn.openSession().connection();
			st = orclCon.createStatement();
			orclCon.setAutoCommit(false);
			if(repInId!=null && !repInId.equals("")){
				AfReport afReport = getReportIn(repInId);
				
				if(afReport!=null){
					String sql = "select max(rep_in_id) as rep_in_id from report_in";
					rs = st.executeQuery(sql);
					if(rs.next()){
						maxRepId = rs.getInt("rep_in_id");
					}
					if(maxRepId==null){
						maxRepId = 1;
					}else
						maxRepId+=100000;
					StringBuffer buffer = new StringBuffer();
					buffer.append("insert into report_in(");
					buffer.append("REP_IN_ID,CHILD_REP_ID");            
					buffer.append(",TERM,TIMES,ORG_ID,YEAR,");                       
					buffer.append("REP_NAME,CHECK_FLAG,");                 
					buffer.append("VERSION_ID,REPORT_DATE,data_range_id,cur_id");                                     
					buffer.append(")values(");
					buffer.append(maxRepId+",'"+afReport.getTemplateId()+"',");		
					buffer.append(afReport.getTerm().intValue()+","+afReport.getTimes().intValue()+",'");
					buffer.append(afReport.getOrgId()+"',"+afReport.getYear().intValue()+",'");
					buffer.append(afReport.getRepName()+"',"+afReport.getCheckFlag().intValue()+",'");
					buffer.append(afReport.getVersionId()+"',to_date('"+DateUtil.toSimpleDateFormat(afReport.getReportDate(),"yyyy-MM-dd")+"','yyyy-MM-dd'),"+1+","+afReport.getCurId().intValue());
					buffer.append(")");
					
					st.addBatch(buffer.toString());
					
					AfTemplate af = AFTemplateDelegate.getTemplate(afReport.getTemplateId(), 
									afReport.getVersionId());
					buffer = new StringBuffer();
					buffer.append("insert into m_child_report(child_rep_id,version_id,report_name,rep_id,report_style,is_public,FRORFZTYPE)");
					buffer.append("values('"+afReport.getTemplateId()+"','"+afReport.getVersionId()+"','");
					buffer.append(afReport.getRepName()+"',"+repInId.intValue()+","+af.getReportStyle()+","+1+",1");
					buffer.append(")");
					st.addBatch(buffer.toString());
					
					buffer = new StringBuffer();
					buffer.append("insert into m_rep_range (org_id,child_rep_id,version_id)");
					buffer.append("values('"+afReport.getOrgId()+"','"+afReport.getTemplateId()+"','"+afReport.getVersionId()+"')");
					st.addBatch(buffer.toString());
					
					buffer= new StringBuffer();
					buffer.append("insert into m_main_rep(rep_id,rep_cn_name)");
					buffer.append("values("+repInId.intValue()+",'"+afReport.getRepName()+"')");
					st.addBatch(buffer.toString());
					
					buffer=new StringBuffer();
					buffer.append("insert into m_actu_rep(data_range_id,child_rep_id,version_id,REP_FREQ_ID,oat_id)");
					buffer.append("values("+1+",'"+afReport.getTemplateId()+"','"+afReport.getVersionId()+"',");
					buffer.append(afReport.getRepFreqId().intValue()+","+1);
					buffer.append(")");
					st.addBatch(buffer.toString());
					
					st.executeBatch();
					orclCon.commit();
					return maxRepId;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			if(orclCon!=null)
				orclCon.rollback();
		} finally {
			try {
				if(rs!=null)
					rs.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			try {
				if(st != null){
					st.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if(orclCon!=null){
					orclCon.close();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (conn != null)
				conn.closeSession();
		}
		return null;
	}
	
	/**
	 * ����MCell����Ϣ
	 * @param repId
	 */
	public static boolean saveMCellInfo(Map cells,String templateId
			,String versionId,Integer repInId)throws Exception{
		boolean res=false;
		if(cells==null || cells.size()==0 ||
				templateId==null || versionId==null)
			return res;
		DBConn conn = null;
		Integer maxRepId =null;
		Connection orclCon = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			conn = new DBConn();
			List<Integer> cellIds = new ArrayList<Integer>();
			orclCon = conn.openSession().connection();
			st = orclCon.createStatement();
			orclCon.setAutoCommit(false);
			String sql = "select max(cell_id) as cell_id from m_cell";
			rs = st.executeQuery(sql);
			Integer cellId=null;
			if(rs.next())
				cellId=rs.getInt("cell_id");
			if(cellId==null)
				cellId=1;
			else
				cellId+=100000;
			for(Object obj : cells.keySet()){
				cellIds.add(cellId);
				String key = (String)obj;
				String value = (String)cells.get(key);
				StringBuffer buffer = new StringBuffer();
				buffer.append("insert into m_cell(cell_id,cell_name,CHILD_REP_ID,VERSION_ID)");
				buffer.append("values("+cellId+",'"+key+"','"+templateId+"','"+versionId+"')");
				st.addBatch(buffer.toString());
				cellId++;
			} 
			st.executeBatch();
	
			orclCon.commit();
		
			for(Integer rcellId : cellIds){
				StringBuffer buffer = new StringBuffer();
				String cellName= getCellName(rcellId,orclCon);
				if(cellName==null)continue;
				buffer.append("insert into report_in_info(cell_id,rep_in_id,report_value)");
				buffer.append("values("+rcellId+","+repInId+",'"+cells.get(cellName)+"')");
				st.addBatch(buffer.toString());
			}
			st.executeBatch();
			orclCon.commit();
			res=true;
		} catch (Exception e) {
			e.printStackTrace();
			res=false;
			if(orclCon!=null)
				orclCon.rollback();
		} finally {
			try {
				if(rs!=null)
					rs.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			try {
				if(st != null){
					st.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if(orclCon!=null){
					orclCon.close();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (conn != null)
				conn.closeSession();
		}
		return res;
	}
	
	
	public static String getCellName(Integer rcellId,Connection con){
		Statement st=null;
		ResultSet rs=null;
		try {
			st =con.createStatement();
			String sql = "select cell_name from m_cell where cell_id="+rcellId;
			rs = st.executeQuery(sql);
			String cellName=null;
			if(rs.next())
				cellName = rs.getString("cell_name");
			return cellName;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				if(rs!=null){
					rs.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(st!=null){
					st.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * ����m_cell_formu����Ϣ
	 * @param repId
	 */
	public static boolean saveMCellFormus(List formus,String templateId,String versionId)throws Exception{
		boolean res=false;
		if(formus==null || formus.size()==0)
			return res;
		DBConn conn = null;
		Integer maxCellFormuId =null;
		Connection orclCon = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			conn = new DBConn();
			List<Integer> formuIds = new ArrayList<Integer>();
			orclCon = conn.openSession().connection();
			st = orclCon.createStatement();
			orclCon.setAutoCommit(false);
			String sql = "select max(cell_formu_id) as cell_formu_id from m_cell_formu";
			rs = st.executeQuery(sql);
			if(rs.next())
				maxCellFormuId=rs.getInt("cell_formu_id");
			if(maxCellFormuId==null)
				maxCellFormuId=1;
			else
				maxCellFormuId+=100000;
			for(Object obj : formus){
				if(obj instanceof AfValidateformula){
					formuIds.add(maxCellFormuId);
					AfValidateformula afVFormu = (AfValidateformula)obj;
					StringBuffer buffer = new StringBuffer();
					buffer.append("insert into m_cell_formu(cell_formu_id,cell_formu,formu_type,cell_formu_view)");
					buffer.append("values("+maxCellFormuId+",'"+afVFormu.getFormulaValue()+"',"+afVFormu.getValidateTypeId().intValue()+",'"+afVFormu.getFormulaName()+"')");
					st.addBatch(buffer.toString());
					maxCellFormuId++;
				}
			}
			st.executeBatch();
			orclCon.commit();
			
			Integer cellToFormuId = null;
			sql="select max(cell_to_formu_id) as cell_to_formu_id  from m_cell_to_formu";
			rs = st.executeQuery(sql);
			if(rs.next())
				cellToFormuId=rs.getInt("cell_to_formu_id");
			if(cellToFormuId==null)
				cellToFormuId=1;
			else
				cellToFormuId+=100000;
			for(Integer mformuId : formuIds){
				StringBuffer buffer = new StringBuffer();
				buffer.append("insert into m_cell_to_formu (cell_to_formu_id,cell_formu_id,child_rep_id,version_id)");
				buffer.append("values("+cellToFormuId+","+mformuId+",'"+templateId+"','"+versionId+"')");
				st.addBatch(buffer.toString());
			}
			st.executeBatch();
			orclCon.commit();
			res=true;
		} catch (Exception e) {
			e.printStackTrace();
			res=false;
			if(orclCon!=null)
				orclCon.rollback();
		} finally {
			try {
				if(rs!=null)
					rs.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			try {
				if(st != null){
					st.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if(orclCon!=null){
					orclCon.close();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (conn != null)
				conn.closeSession();
		}
		return res;
	}
	
	/**
	 * 
	 * @param repInId
	 * @return
	 */
	public static boolean deleteReportInfo(Integer repInId,String childRepId,String versionId){
		boolean res=false;
		if(repInId==null)
			return res;
		DBConn conn = null;
		Integer maxCellFormuId =null;
		Connection orclCon = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			conn = new DBConn();
			List<Integer> formuIds = new ArrayList<Integer>();
			orclCon = conn.openSession().connection();
			st = orclCon.createStatement();
			orclCon.setAutoCommit(false);

			String sql = "delete from data_validate_info where rep_in_id="+repInId;
			st.addBatch(sql);
			
			sql="delete from m_actu_rep where child_rep_id='"+childRepId+"'";
			st.addBatch(sql);
			
			sql="delete from m_rep_range where child_rep_id='"+childRepId+"'";
			st.addBatch(sql);
			
			sql="delete from m_main_rep where rep_id in("
				+"select rep_id from m_child_report where child_rep_id='"+childRepId+"')";
			st.addBatch(sql);
			
			sql="delete from m_child_report where child_rep_id='"+childRepId+"'";
			st.addBatch(sql);
			
			sql="delete from m_cell_formu where cell_formu_id in("
				+"select cell_formu_id from m_cell_to_formu where child_rep_id='"+childRepId+"')";
			st.addBatch(sql);
			
			sql="delete from m_cell_to_formu where child_rep_id='"+childRepId+"'";
			st.addBatch(sql);
			
			sql="delete from report_in_info r where r.cell_id in("
				+"select m.cell_id from m_cell m where m.child_rep_id='"+childRepId+"')";
			st.addBatch(sql);
			
			sql="delete from m_cell where child_rep_id='"+childRepId+"'";
			st.addBatch(sql);
			
			sql="delete from report_in where rep_in_id="+repInId;
			st.addBatch(sql);
			
			st.executeBatch();
			
			orclCon.commit();
			res=true;
		} catch (Exception e) {
			e.printStackTrace();
			res=false;
			try {
				if(orclCon!=null)
					orclCon.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			try {
				if(rs!=null)
					rs.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			try {
				if(st != null){
					st.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if(orclCon!=null){
					orclCon.close();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (conn != null)
				conn.closeSession();
		}
		return res;
	}
	
	/**
	 * 
	 * @param repInId
	 * @return
	 */
	public static Long getCellFormuId(String cellFormuValue){
		DBConn conn = null;
		try {
			conn = new DBConn();
			String hql = "select f.formulaId from AfValidateformula f where f.formulaValue='"+cellFormuValue+"'";
			return (Long)conn.openSession().createQuery(hql).uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return null;
	}
	
	public static AfReport getAfReport(String templateId,String versionId){
		DBConn conn = null;
		try {
			conn = new DBConn();
			String hql = "from AfReport a where a.versionId='"+versionId+"' and a.templateId='"+templateId+"'";
			List<AfReport> afs = conn.openSession().createQuery(hql).list();
			if(afs!=null && afs.size()>0)
				return afs.get(0);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return null;
	}
	
	public static String getCellFormuValue(Integer cellforMuId){
		DBConn conn = null;
		try {
			conn = new DBConn();
			String hql = "select m.cellFormu from MCellFormu m where m.cellFormuId="+cellforMuId;
			return (String)conn.openSession().createQuery(hql).uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return null;
	}
}
