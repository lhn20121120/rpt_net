package com.fitech.gznx.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.smis.adapter.StrutsMActuRepDelegate;
import com.cbrc.smis.adapter.StrutsMCurrDelegate;
import com.cbrc.smis.adapter.StrutsMRepFreqDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.form.ReportAgainSetForm;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.hibernate.MActuRep;
import com.cbrc.smis.hibernate.ReportAgainSet;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.fitech.gznx.common.DateUtil;
import com.fitech.gznx.common.StringUtil;
import com.fitech.gznx.form.AFReportForm;
import com.fitech.gznx.po.AfReportAgain;
import com.fitech.net.adapter.StrutsOrgNetDelegate;

/**
 * �����ر�������
 * 
 * @author YeE
 * 
 */
public class AFReportAgainDelegate {

	private static FitechException log = new FitechException(
			AFReportAgainDelegate.class);

	/**
	 * �������ڻ�õ�ǰӦ������Ƶ��
	 * 
	 * @param date
	 *            ��������
	 * @return Ƶ�ȣ���ʽΪ1,2...��
	 */
	private static String getFreq(String date) {

		String rep_freq = "";
		// ��ʽ��Ƶ��
		if (date != null && !date.equals("")) {

			// �����±����������
			String[] dates = DateUtil.getLastMonth(date).split("-");

			int year = Integer.parseInt(dates[0]);
			int term = Integer.parseInt(dates[1]);
			// int day = Integer.parseInt(dates[2]);

			if (term == 12)
				// rep_freq = "('��','��','����','��')";
				rep_freq = com.fitech.gznx.common.Config.FREQ_MONTH
						+ Config.SPLIT_SYMBOL_COMMA
						+ com.fitech.gznx.common.Config.FREQ_SEASON
						+ Config.SPLIT_SYMBOL_COMMA
						+ com.fitech.gznx.common.Config.FREQ_HALFYEAR
						+ Config.SPLIT_SYMBOL_COMMA
						+ com.fitech.gznx.common.Config.FREQ_YEAR;
			else if (term == 6)
				// rep_freq = "('��','��','����')";
				rep_freq = com.fitech.gznx.common.Config.FREQ_MONTH
						+ Config.SPLIT_SYMBOL_COMMA
						+ com.fitech.gznx.common.Config.FREQ_SEASON
						+ Config.SPLIT_SYMBOL_COMMA
						+ com.fitech.gznx.common.Config.FREQ_HALFYEAR;
			else if (term == 3 || term == 9)
				// rep_freq = "('��','��')";
				rep_freq = com.fitech.gznx.common.Config.FREQ_MONTH
						+ Config.SPLIT_SYMBOL_COMMA
						+ com.fitech.gznx.common.Config.FREQ_SEASON;
			else
				// rep_freq = "('��')";
				rep_freq = com.fitech.gznx.common.Config.FREQ_MONTH.toString();
		}

		return rep_freq;
	}

	/**
	 * ���ݴ������ڻ����Чʱ�������еı���sql
	 * @param afReportForm
	 * @return sql
	 */
	/*private static String getReportDateSql(AFReportForm afReportForm){
		
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
					+ ") or "
					+ "(ri.year=" + Integer.valueOf(yearbegain.substring(0, 4))
					+ " and ri.term=" + Integer.valueOf(yearbegain.substring(5, 7)) 
					+ " and ri.day=" +Integer.valueOf(yearbegain.substring(8, 10))
					+ " and ri.repFreqId =" + com.fitech.gznx.common.Config.FREQ_YEARBEGAIN
					+ "))" ;
		
		return datesql;
	}*/
private static String getReportDateSql(AFReportForm afReportForm){
		
		if(afReportForm.getDate() == null) return null;
		
		String datesql = "";
		// �����±����������
//		String[] dates = DateUtil.getLastMonth(afReportForm.getDate()).split("-");
		String[] dates = afReportForm.getDate().split("-");

		int year = Integer.parseInt(dates[0]);
		int term = Integer.parseInt(dates[1]);
		int day = Integer.parseInt(dates[2]);

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
		//�����ܡ�Ѯ���챨Ƶ�ȵĹ���--BEGIN	
		int weekday=DateUtil.getDayOfWeek(year, term, day);
//		if(weekday==5) rep_freq+=Config.SPLIT_SYMBOL_COMMA+com.fitech.gznx.common.Config.FREQ_WEEK;
		if(day==10||day==20){
			rep_freq+=Config.SPLIT_SYMBOL_COMMA+com.fitech.gznx.common.Config.FREQ_TENDAY;
		}
		rep_freq+=Config.SPLIT_SYMBOL_COMMA+com.fitech.gznx.common.Config.FREQ_MONTH_EXPRESS;
		//�����ܡ�Ѯ���챨Ƶ�ȵĹ���--END		
		String tenday = DateUtil.getFreqDateLast(afReportForm.getDate(),com.fitech.gznx.common.Config.FREQ_TENDAY);
		String termday = DateUtil.getFreqDateLast(afReportForm.getDate(),com.fitech.gznx.common.Config.FREQ_MONTH);
		String yearbegain = DateUtil.getFreqDateLast(afReportForm.getDate(),com.fitech.gznx.common.Config.FREQ_YEARBEGAIN);
		

		datesql = " and ((ri.year=" + Integer.valueOf(dates[0])
				+ " and ri.term=" + Integer.valueOf(dates[1])
				+ " and ri.day=" + Integer.valueOf(dates[2])
				+ " and ri.repFreqId in(" + com.fitech.gznx.common.Config.FREQ_DAY + (weekday==5?","+com.fitech.gznx.common.Config.FREQ_WEEK:"")
				+ ")) or ";
		if(day==10||day==20){datesql += "(ri.year=" + Integer.valueOf(tenday.substring(0, 4))
				+ " and ri.term=" + Integer.valueOf(tenday.substring(5, 7)) 
				+ " and ri.day=" +Integer.valueOf(tenday.substring(8, 10))
				+ " and ri.repFreqId=" + com.fitech.gznx.common.Config.FREQ_TENDAY 
				+ ") or ";}
		if(term==1&&day==1){datesql += "(ri.year=" + Integer.valueOf(yearbegain.substring(0, 4))
				+ " and ri.term=" + Integer.valueOf(yearbegain.substring(5, 7)) 
				+ " and ri.day=" +Integer.valueOf(yearbegain.substring(8, 10))
				+ " and ri.repFreqId =" + com.fitech.gznx.common.Config.FREQ_YEARBEGAIN
				+ ") or " ;}
		datesql += "(ri.year=" + Integer.valueOf(termday.substring(0, 4))
				+ " and ri.term=" + Integer.valueOf(termday.substring(5, 7)) 
				+ " and ri.day=" +Integer.valueOf(termday.substring(8, 10))
				+ " and ri.repFreqId in (" + rep_freq + ")" 
				+ "))";
		
		return datesql;
	}
	/**
	 * ��ʹ��Hibernate ���Ը� 2011-12-27
	 * Ӱ�����AfTemplate AfReportAgain
	 * �ر������ѯ����ѯ�������ر��ı����¼����
	 * 
	 * @param reportInForm
	 *            ����FormBean
	 * @param operator
	 *            ��ǰ��¼�û���Ϣ
	 * @return int ��ѯ��¼��
	 */
	public static int getCount(AFReportForm reportInForm, Operator operator) {
		int count = 0;
		DBConn conn = null;
		Session session = null;
		if (reportInForm == null || operator == null)
			return count;

		String rep_freq = "";
		// ��ʽ��Ƶ��
		if (reportInForm.getDate() != null
				&& !reportInForm.getDate().equals(""))
			rep_freq = getFreq(reportInForm.getDate());

		try {
			StringBuffer hql = new StringBuffer(
					"select count(a) from AfReportAgain a,AfTemplate at where 1=1"//a.afReport.forseReportAgainFlag="
							//+ Config.FORSE_REPORT_AGAIN_FLAG_1 
							+ " and at.id.templateId=a.afReport.templateId and at.id.versionId=a.afReport.versionId");

			StringBuffer where = new StringBuffer("");

			/** ��ӱ����Ų�ѯ���������Դ�Сдģ����ѯ�� */
			if (reportInForm.getTemplateId() != null
					&& !reportInForm.getTemplateId().equals("")) {
				where.append(" and upper(a.afReport.templateId) like upper('%"
						+ reportInForm.getTemplateId().trim() + "%')");
			}
			/** ��ӱ������Ʋ�ѯ������ģ����ѯ�� */
			if (reportInForm.getRepName() != null
					&& !reportInForm.getRepName().equals("")) {
				where.append(" and a.afReport.repName like '%"
						+ reportInForm.getRepName().trim() + "%'");
			}
			/** ���ģ�����Ͳ�ѯ���� */
			if (reportInForm.getBak1() != null
					&& !reportInForm.getBak1().equals(Config.DEFAULT_VALUE)
					&& !reportInForm.getBak1().trim().equals("")) {
				where.append(" and at.bak1 in (" + reportInForm.getBak1()+")");
			}
			/** ��ӱ���Ƶ�Ȳ�ѯ���� */
			if (!StringUtil.isEmpty(reportInForm.getRepFreqId())
					&& !String.valueOf(reportInForm.getRepFreqId()).equals(
							Config.DEFAULT_VALUE)) {
				where.append(" and a.afReport.repFreqId="
						+ reportInForm.getRepFreqId());
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
			/** ���������ѯ���� */
			if (reportInForm.getDate() != null
					&& !reportInForm.getDate().equals("")) {
				where.append(getReportDateSql(reportInForm).replaceAll("ri", "a.afReport"));
			}
			/** ��ӻ�����ѯ���� */
			if (!StringUtil.isEmpty(reportInForm.getOrgId())
					&& !reportInForm.getOrgId().equals(Config.DEFAULT_VALUE)) {
				where.append(" and a.afReport.orgId='"
						+ reportInForm.getOrgId().trim() + "'");
			}

			/** ��ӱ������Ȩ��[�ر�Ȩ��=���Ȩ��]�������û�������ӣ�
			 * ���������ݿ��ж� */
			if (operator.isSuperManager() == false) {
				if (operator.getChildRepCheckPodedom() == null
						|| operator.getChildRepCheckPodedom().equals(""))
					return count;
				if(Config.DB_SERVER_TYPE.equals("oracle"))
					where.append(" and ltrim(rtrim(a.afReport.orgId)) || a.afReport.templateId in("
								+ operator.getChildRepCheckPodedom() + ")");
				if(Config.DB_SERVER_TYPE.equals("sqlserver"))
					where.append(" and ltrim(rtrim(a.afReport.orgId)) + a.afReport.templateId in("
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
	 * ��ʹ��hibernate ���Ը� 2011-12-27
	 * Ӱ�����AfReportAgain AfTemplate
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
	public static List getRecord(AFReportForm reportInForm, int offset,
			int limit, Operator operator) {
		List resList = null;
		DBConn conn = null;
		Session session = null;

		if (reportInForm == null || operator == null)
			return resList;

		try {
			StringBuffer hql = new StringBuffer(
				"select a from AfReportAgain a,AfTemplate at where 1=1 "//a.afReport.forseReportAgainFlag="
				//+ Config.FORSE_REPORT_AGAIN_FLAG_1
				+ " and at.id.templateId=a.afReport.templateId and at.id.versionId=a.afReport.versionId");

			StringBuffer where = new StringBuffer("");
		
			/** ��ӱ����Ų�ѯ���������Դ�Сдģ����ѯ�� */
			if (reportInForm.getTemplateId() != null
					&& !reportInForm.getTemplateId().equals("")) {
				where.append(" and upper(a.afReport.templateId) like upper('%"
						+ reportInForm.getTemplateId().trim() + "%')");
			}
			/** ��ӱ������Ʋ�ѯ������ģ����ѯ�� */
			if (reportInForm.getRepName() != null
					&& !reportInForm.getRepName().equals("")) {
				where.append(" and a.afReport.repName like '%"
						+ reportInForm.getRepName().trim() + "%'");
			}
			/** ���ģ�����Ͳ�ѯ���� */
			if (reportInForm.getBak1() != null
					&& !reportInForm.getBak1().equals(Config.DEFAULT_VALUE)
					&& !reportInForm.getBak1().trim().equals("")) {
				where.append(" and at.bak1 in (" + reportInForm.getBak1()+")");
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
			/** ��ӱ���Ƶ�Ȳ�ѯ���� */
			if (!StringUtil.isEmpty(reportInForm.getRepFreqId())
					&& !String.valueOf(reportInForm.getRepFreqId()).equals(
							Config.DEFAULT_VALUE)) {
				where.append(" and a.afReport.repFreqId="
						+ reportInForm.getRepFreqId());
			}
			/** ���������ѯ���� */
			if (reportInForm.getDate() != null
					&& !reportInForm.getDate().equals("")) {
				where.append(getReportDateSql(reportInForm).replaceAll("ri", "a.afReport"));
			}
			/** ��ӻ�����ѯ���� */
			if (!StringUtil.isEmpty(reportInForm.getOrgId())
					&& !reportInForm.getOrgId().equals(Config.DEFAULT_VALUE)) {
				where.append(" and a.afReport.orgId='"
						+ reportInForm.getOrgId().trim() + "'");
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
		
			/** ��ӱ������Ȩ��[�ر�Ȩ��=���Ȩ��]�������û�������ӣ�
			 * ���������ݿ��ж� */
			if (operator.isSuperManager() == false) {
				if (operator.getChildRepCheckPodedom() == null
						|| operator.getChildRepCheckPodedom().equals(""))
					return resList;
				if(Config.DB_SERVER_TYPE.equals("oracle"))
					where.append(" and ltrim(rtrim(a.afReport.orgId)) || a.afReport.templateId in("
								+ operator.getChildRepCheckPodedom() + ")");
				if(Config.DB_SERVER_TYPE.equals("sqlserver"))
					where.append(" and ltrim(rtrim(a.afReport.orgId)) + a.afReport.templateId in("
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
					
					AfReportAgain afReportAgain = (AfReportAgain) list.get(i);
					ReportAgainSetForm setform = new ReportAgainSetForm();
					
					setform.setRasId(afReportAgain.getRasId().intValue());
					setform.setRepInId(afReportAgain.getRepId().intValue());
					setform.setCause(afReportAgain.getCause());
					setform.setSetAgainDate(afReportAgain.getSetDate());
					
					setform.setRepName(afReportAgain.getAfReport().getRepName());
					setform.setChildRepId(afReportAgain.getAfReport().getTemplateId());
					setform.setVersionId(afReportAgain.getAfReport().getVersionId());
					setform.setOrgName(AFOrgDelegate.selectOne(
							afReportAgain.getAfReport().getOrgId()).getOrgName());
					setform.setCurrName(StrutsMCurrDelegate.getISMCurr(
							afReportAgain.getAfReport().getCurId().toString()).getCurName() );

					setform.setActuFreqName(StrutsMRepFreqDelegate.selectOne(
							afReportAgain.getAfReport().getRepFreqId().intValue()).getRepFreqName());
					//setform.setDataRangeDes(items[10].toString());
//					setform.setRepDate(afReportAgain.getAfReport().getReportDate());
					setform.setReportDate(afReportAgain.getAfReport().getYear()+ "-"
							+afReportAgain.getAfReport().getTerm() + "-"
							+afReportAgain.getAfReport().getDay());

					
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
	 * ��ʹ��hibernate ���Ը� 2011-12-27
	 * Ӱ�����AfReportAgain
	 * �����ر���Ϣ
	 * @param repInIds
	 * @param cause
	 * @param templateType
	 * @return
	 */
    public static boolean insert(Integer[] repInIds,String cause,Integer templateType){
		boolean result=false;
		if(repInIds==null || cause==null) return result;
		
		DBConn conn=null;
		Session session=null;
		try{
			conn=new DBConn();
			session=conn.beginTransaction();
			
			for(int i=0;i<repInIds.length;i++){
				AfReportAgain reportAgainSetPersistence = new AfReportAgain();
				reportAgainSetPersistence.setCause(cause);
				reportAgainSetPersistence.setRepId(repInIds[i].longValue());
				reportAgainSetPersistence.setSetDate(new Date());
				reportAgainSetPersistence.setTemplateType(templateType.longValue());
								
				session.save(reportAgainSetPersistence);
			}
			session.flush();
			result=true;
			
		}catch(HibernateException he){
			result=false;
			log.printStackTrace(he);
		}catch(Exception e){
			result=false;
			log.printStackTrace(e);
		}finally{
			if(conn!=null) conn.endTransaction(result);
		}
		
		return result;
	}
	
}
