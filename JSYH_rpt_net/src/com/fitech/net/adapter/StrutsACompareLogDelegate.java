package com.fitech.net.adapter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.hibernate.ReportIn;
import com.cbrc.smis.util.FitechException;
import com.fitech.net.form.ACompareLogFrom;
import com.fitech.net.hibernate.ACompareLog;
import com.fitech.net.hibernate.ACompareLogKey;

/**
 *������
 *���ڣ�2007-12-12
 *���ߣ��ܷ���
 */
public class StrutsACompareLogDelegate {
//	Catch��������׳��������쳣
	private static FitechException log=new FitechException(StrutsACompareLogDelegate.class);
	/**
	 * ��������������ݿ��д��ڵı����б�
	 * @param ��ѯ���� year��term��orgId��repName�����������cType���Ա����ͣ�
	 * @return
	 */
	public static List getExistRecords(Integer year,Integer term,String orgId,String repName,Integer cType, int offset, int limit)
	{
		List resultList = null;
		if(orgId==null||orgId.equals(""))return null;

		// ���Ӷ���ͻỰ�����ʼ��
		DBConn conn = null;
		Session session = null;
		

		// ��ѯ����HQL������
		StringBuffer hql =new StringBuffer( "from ReportIn ri ");
		hql.append("where  ri.times=1 and ri.orgId='"+orgId+"'");
		if(year!=null){
			hql.append(" and ri.year="+year);
		}
		if(term!=null){
			hql.append(" and ri.term="+term);
		}
		if(repName!=null&&!repName.equals("")){
			hql.append(" and ri.repName like '%"+repName+"%'");
		}
		

		try
		{ 
			
			// List���ϵĲ���
			conn = new DBConn();
			// �����ӿ�ʼ�Ự
			session = conn.openSession();
			Query query = session.createQuery(hql.toString());
			query.setFirstResult(offset);
			query.setMaxResults(limit);
			List list = query.list();
			resultList=new ArrayList();
			for(int i=0;i<list.size();i++)
			{
				ReportIn ri=(ReportIn)list.get(i);
				ACompareLogFrom v=new ACompareLogFrom();
				v.setChildRepId(ri.getMChildReport().getComp_id().getChildRepId());
				v.setVersionId(ri.getMChildReport().getComp_id().getVersionId());
				v.setRepName(ri.getRepName());
				v.setTerm(ri.getTerm());
				v.setYear(ri.getYear());
				v.setRepInId(ri.getRepInId());
				v.setCurrName(ri.getMCurr().getCurName());
				v.setDataRgTypeName(ri.getMDataRgType().getDataRgDesc());
				ACompareLogKey key=new ACompareLogKey(ri.getRepInId(),cType);
				ACompareLog acLog=(ACompareLog)session.get(ACompareLog.class, key);
				if(acLog!=null){
					v.setAcState(acLog.getAcState());
					v.setAcRepInId(acLog.getAcRepInId());
					v.setAcLog(acLog.getAcLog());
				}
				resultList.add(v);
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
		return resultList;
	}
	/**
	 * ��������������ݿ��д��ڵı�����Ŀ
	 * @param ��ѯ���� year��term��orgId��repName�����������cType���Ա����ͣ�
	 * @return
	 */
	public static int getExistRecordsCount(Integer year,Integer term,String orgId,String repName,Integer cType)
	{
		int result = 0;
		if(orgId==null||orgId.equals(""))return 0;

		// ���Ӷ���ͻỰ�����ʼ��
		DBConn conn = null;
		Session session = null;
		

		// ��ѯ����HQL������
		StringBuffer hql =new StringBuffer( "select count(*) from ReportIn ri ");
		hql.append("where  ri.times=1 and ri.orgId='"+orgId+"'");
		if(year!=null){
			hql.append(" and ri.year="+year);
		}
		if(term!=null){
			hql.append(" and ri.term="+term);
		}
		if(repName!=null&&!repName.equals("")){
			hql.append(" and ri.repName like '%"+repName+"%'");
		}
		

		try
		{ 
			
			// List���ϵĲ���
			conn = new DBConn();
			// �����ӿ�ʼ�Ự
			session = conn.openSession();
			Query query = session.createQuery(hql.toString());
			List list = query.list();
			if(list!=null && list.size()==1)
				result=((Integer)list.get(0)).intValue();
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
		return result;
	}
	/**
	 *<p>����:�ȶ����ű���</p>
	 *<p>����:repInIds ����ID��ɵ��ַ�����cType�ȶ����ͣ�1���ܱȶԣ�2ETL�ȶԣ�</p>
	 *<p>���ڣ�2007-12-12</p>
	 *<p>���ߣ��ܷ���</p>
	 */
	public static void compare(String repInIds,Integer acType){
		if(repInIds==null||repInIds.equals(""))return;
//		 ���Ӷ���ͻỰ�����ʼ��
		DBConn conn = null;
		Session session = null;
		try{ 
//			 List���ϵĲ���
			conn = new DBConn();
			session=conn.beginTransaction();
			// �����ӿ�ʼ�Ự
			Connection connection=session.connection();
			String repInId[]=repInIds.split(",");
			//��öԱȵı���IDsql
			String cSql="select a.REP_IN_ID from REPORT_IN a ,REPORT_IN b where b.REP_IN_ID=?" +
					" and a.CHILD_REP_ID=b.CHILD_REP_ID and a.VERSION_ID=b.VERSION_ID and a.YEAR=b.YEAR and a.TERM=b.TERM " +
					"and a.ORG_ID=b.ORG_ID and a.DATA_RANGE_ID=b.DATA_RANGE_ID and a.CUR_ID=b.CUR_ID and a.TIMES=?";
			PreparedStatement pst=connection.prepareStatement(cSql);
			ResultSet rs=null;
			for(int i=0;i<repInId.length;i++){
				//�ȶԱ���ID
				int cRepInId=0;
				pst.setInt(1, Integer.parseInt(repInId[i]));
				//���ܱȶ�
				if(acType.equals(new Integer(1))){
					pst.setInt(2, -1);
				}
				//ETL�ȶ�
				if(acType.equals(new Integer(2))){
					pst.setInt(2, -2);
				}
				rs=pst.executeQuery();
				if(rs.next()){
					cRepInId=rs.getInt(1);
				}
				
				
				if(cRepInId>0){
					boolean re=compare(new Integer(repInId[i]),new Integer(cRepInId),acType,connection);
					if(re){
						ACompareLogKey key=new ACompareLogKey(new Integer(repInId[i]),acType);
						if(session.get(ACompareLog.class, key)==null){
							ACompareLog acLog=new ACompareLog();
							acLog.setKey(key);
							acLog.setAcState(new Integer(1));
							acLog.setAcLog("һ��");
							acLog.setAcRepInId(new Integer(cRepInId));
							session.save(acLog);
						}else{
							ACompareLog acLog=(ACompareLog)session.get(ACompareLog.class, key);
							acLog.setAcState(new Integer(1));
							acLog.setAcLog("һ��");
							acLog.setAcRepInId(new Integer(cRepInId));
							session.update(acLog);
						}
						
					}else{
						ACompareLogKey key=new ACompareLogKey(new Integer(repInId[i]),acType);
						if(session.get(ACompareLog.class, key)==null){
							ACompareLog acLog=new ACompareLog();
							acLog.setKey(key);
							acLog.setAcState(new Integer(1));
							acLog.setAcLog("��һ��");
							acLog.setAcRepInId(new Integer(cRepInId));
							session.save(acLog);
						}else{
							ACompareLog acLog=(ACompareLog)session.get(ACompareLog.class, key);
							acLog.setAcState(new Integer(1));
							acLog.setAcLog("��һ��");
							acLog.setAcRepInId(new Integer(cRepInId));
							session.update(acLog);
						}
					}
				}else{
					ACompareLogKey key=new ACompareLogKey(new Integer(repInId[i]),acType);
					if(session.get(ACompareLog.class, key)==null){
						ACompareLog acLog=new ACompareLog();
						acLog.setKey(key);
						acLog.setAcState(new Integer(1));
						acLog.setAcLog("��������");
						session.save(acLog);
					}else{
						ACompareLog acLog=(ACompareLog)session.get(ACompareLog.class, key);
						acLog.setAcState(new Integer(1));
						acLog.setAcLog("��������");
						session.update(acLog);
					}
				}
				
				
			}
			
			conn.endTransaction(true);
			
		}catch(Exception e){
			log.printStackTrace(e);
		}finally{
			if(conn!=null)conn.closeSession();
		}
		
	}
	/**
	 *<p>����:�ȶ����ű���,��ȫ��ͬ����true ���򷵻�false</p>
	 *<p>����:repInId ����Id��cRepInId���������ݻ�ETL���ݣ���cType�ȶ����ͣ�1���ܱȶԣ�2ETL�ȶԣ�</p>
	 *<p>���ڣ�2007-12-12</p>
	 *<p>���ߣ��ܷ���</p>
	 */
	private static boolean compare(Integer repInId,Integer cRepInId,Integer cType,Connection connection){
		boolean re=false;

		try
		{ 
			
			
			String compareSql="select count(*) from report_in_info a left join  report_in_info b on a.CELL_ID=b.CELL_ID " +
					"where a.REP_IN_ID=? and b.REP_IN_ID=? and a.REPORT_VALUE!=b.REPORT_VALUE";
			PreparedStatement pst=connection.prepareStatement(compareSql.toUpperCase());
			pst.setInt(1, repInId.intValue());
			pst.setInt(2, cRepInId.intValue());
			ResultSet rs=pst.executeQuery();
			if(rs.next()){
				int reInt=rs.getInt(1);
				if(reInt>0){
					re=false;					
				}else{
					re=true;
				}
			}
			pst.close();
			rs.close();
			
		}
		catch (Exception e)
		{
			log.printStackTrace(e);
		}
		return re;
	}

}

