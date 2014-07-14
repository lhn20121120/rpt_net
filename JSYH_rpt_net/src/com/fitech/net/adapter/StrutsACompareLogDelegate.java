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
 *描述：
 *日期：2007-12-12
 *作者：曹发根
 */
public class StrutsACompareLogDelegate {
//	Catch到本类的抛出的所有异常
	private static FitechException log=new FitechException(StrutsACompareLogDelegate.class);
	/**
	 * 根据条件获得数据库中存在的报表列表
	 * @param 查询参数 year，term，orgId，repName；必须参数：cType（对比类型）
	 * @return
	 */
	public static List getExistRecords(Integer year,Integer term,String orgId,String repName,Integer cType, int offset, int limit)
	{
		List resultList = null;
		if(orgId==null||orgId.equals(""))return null;

		// 连接对象和会话对象初始化
		DBConn conn = null;
		Session session = null;
		

		// 查询条件HQL的生成
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
			
			// List集合的操作
			conn = new DBConn();
			// 打开连接开始会话
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
			// 如果连接存在，则断开，结束会话，返回
			if (conn != null)
				conn.closeSession();
		}
		return resultList;
	}
	/**
	 * 根据条件获得数据库中存在的报表数目
	 * @param 查询参数 year，term，orgId，repName；必须参数：cType（对比类型）
	 * @return
	 */
	public static int getExistRecordsCount(Integer year,Integer term,String orgId,String repName,Integer cType)
	{
		int result = 0;
		if(orgId==null||orgId.equals(""))return 0;

		// 连接对象和会话对象初始化
		DBConn conn = null;
		Session session = null;
		

		// 查询条件HQL的生成
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
			
			// List集合的操作
			conn = new DBConn();
			// 打开连接开始会话
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
			// 如果连接存在，则断开，结束会话，返回
			if (conn != null)
				conn.closeSession();
		}
		return result;
	}
	/**
	 *<p>描述:比对两张报表</p>
	 *<p>参数:repInIds 报表ID组成的字符串，cType比对类型（1汇总比对，2ETL比对）</p>
	 *<p>日期：2007-12-12</p>
	 *<p>作者：曹发根</p>
	 */
	public static void compare(String repInIds,Integer acType){
		if(repInIds==null||repInIds.equals(""))return;
//		 连接对象和会话对象初始化
		DBConn conn = null;
		Session session = null;
		try{ 
//			 List集合的操作
			conn = new DBConn();
			session=conn.beginTransaction();
			// 打开连接开始会话
			Connection connection=session.connection();
			String repInId[]=repInIds.split(",");
			//获得对比的报表IDsql
			String cSql="select a.REP_IN_ID from REPORT_IN a ,REPORT_IN b where b.REP_IN_ID=?" +
					" and a.CHILD_REP_ID=b.CHILD_REP_ID and a.VERSION_ID=b.VERSION_ID and a.YEAR=b.YEAR and a.TERM=b.TERM " +
					"and a.ORG_ID=b.ORG_ID and a.DATA_RANGE_ID=b.DATA_RANGE_ID and a.CUR_ID=b.CUR_ID and a.TIMES=?";
			PreparedStatement pst=connection.prepareStatement(cSql);
			ResultSet rs=null;
			for(int i=0;i<repInId.length;i++){
				//比对报表ID
				int cRepInId=0;
				pst.setInt(1, Integer.parseInt(repInId[i]));
				//汇总比对
				if(acType.equals(new Integer(1))){
					pst.setInt(2, -1);
				}
				//ETL比对
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
							acLog.setAcLog("一致");
							acLog.setAcRepInId(new Integer(cRepInId));
							session.save(acLog);
						}else{
							ACompareLog acLog=(ACompareLog)session.get(ACompareLog.class, key);
							acLog.setAcState(new Integer(1));
							acLog.setAcLog("一致");
							acLog.setAcRepInId(new Integer(cRepInId));
							session.update(acLog);
						}
						
					}else{
						ACompareLogKey key=new ACompareLogKey(new Integer(repInId[i]),acType);
						if(session.get(ACompareLog.class, key)==null){
							ACompareLog acLog=new ACompareLog();
							acLog.setKey(key);
							acLog.setAcState(new Integer(1));
							acLog.setAcLog("不一致");
							acLog.setAcRepInId(new Integer(cRepInId));
							session.save(acLog);
						}else{
							ACompareLog acLog=(ACompareLog)session.get(ACompareLog.class, key);
							acLog.setAcState(new Integer(1));
							acLog.setAcLog("不一致");
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
						acLog.setAcLog("报表不存在");
						session.save(acLog);
					}else{
						ACompareLog acLog=(ACompareLog)session.get(ACompareLog.class, key);
						acLog.setAcState(new Integer(1));
						acLog.setAcLog("报表不存在");
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
	 *<p>描述:比对两张报表,完全相同返回true 否则返回false</p>
	 *<p>参数:repInId 报表Id，cRepInId（汇总数据或ETL数据），cType比对类型（1汇总比对，2ETL比对）</p>
	 *<p>日期：2007-12-12</p>
	 *<p>作者：曹发根</p>
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

