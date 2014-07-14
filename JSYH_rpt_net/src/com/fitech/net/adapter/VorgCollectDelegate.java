package com.fitech.net.adapter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.smis.adapter.StrutsMActuRepDelegate;
import com.cbrc.smis.adapter.StrutsMChildReportDelegate;
import com.cbrc.smis.adapter.StrutsMCurrDelegate;
import com.cbrc.smis.adapter.StrutsMDataRgTypeDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.hibernate.ReportIn;
import com.cbrc.smis.other.Aditing;
import com.cbrc.smis.security.Operator;
import com.fitech.gznx.po.AfOrg;
import com.fitech.gznx.service.AFOrgDelegate;
import com.fitech.net.hibernate.ViewOrgRep;

/**
 * 1104报表虚拟机构汇总情况数据库操作类
 * **/
public class VorgCollectDelegate {
	/**
	 * 根据机构Id获取机构需汇总报表的应报数(bad)
	 * **/
	public static List getVorgReps(String orgId,int year,int term){
		List result=null;
		DBConn conn=null;
		Session session=null;
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String rep_freq="";
		try{
			/**
			 * 判断频度
			 * */
			
 			if(term == 12)
 				rep_freq = "('1','2','3','4')";
 			else if(term == 6)
 				rep_freq = "('1','2','3')";
 			else if(term == 3 || term == 9)
 				rep_freq = "('1','2')";
 			else rep_freq = "('1')";
 			
			String sql="select VREP.CHILD_REP_ID,VREP.VERSION_ID,VREP.DATA_RANGE_ID,VREP.REP_FREQ_ID,COUNT(0) AS YB from view_m_report vrep "
						+" inner join "
						+"( select * from view_m_report vmrep where vmrep.ORG_ID in ("
						+"select t.org_id as  org_id from af_collect_relation t where t.collect_id=?)  ) c" 
						+"on c.child_rep_id=vrep.CHILD_REP_ID and c.version_id=vrep.VERSION_ID and c.data_range_id=vrep.DATA_RANGE_ID and c.REP_FREQ_ID=vrep.REP_FREQ_ID"
						+"where vrep.ORG_ID=? and vrep.REP_FREQ_ID in (?) GROUP BY VREP.CHILD_REP_ID,VREP.VERSION_ID,VREP.DATA_RANGE_ID,VREP.REP_FREQ_ID,VREP.CUR_ID";
			conn=new DBConn();
			session=conn.openSession();
			con=session.connection();
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, orgId);
			pstmt.setString(2,rep_freq);
			rs=pstmt.executeQuery();
			while(rs.next()){
				
			}
		}catch(Exception e){
			
		}finally{
			if (conn!=null)
				conn.closeSession();
		}
		return result;
	}
	/**
	 * 根据机构ID获取该机构需汇总的报表已报情况(bad)
	 * **/
	public static List getVorgIsReps(String orgId,int year,int term){
		List result=null;
		DBConn conn=null;
		Session session=null;
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		try{
			String rep_freq="";
 			if(term == 12)
 				rep_freq = "('1','2','3','4')";
 			else if(term == 6)
 				rep_freq = "('1','2','3')";
 			else if(term == 3 || term == 9)
 				rep_freq = "('1','2')";
 			else rep_freq = "('1')";
 			
			String sql="SELECT CHILD_REP_ID,VERSION_ID,DATA_RANGE_ID,COUNT(0) AS SB FROM REPORT_IN REP"
						+" WHERE  REP.YEAR=? AND REP.TERM=? AND REP.TIMES=1 and vrep.REP_FREQ_ID in (?) AND REP.ORG_ID IN (select t.org_id as  org_id from af_collect_relation t where t.collect_id=?)"
						+" GROUP BY CHILD_REP_ID,VERSION_ID,DATA_RANGE_ID";
			conn=new DBConn();
			session=conn.openSession();
			con=session.connection();
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, rep_freq);
			pstmt.setString(2, orgId);
			rs=pstmt.executeQuery();
			while(rs.next()){
				
			}
		}catch(Exception e){
			
		}finally{
			if (conn!=null)
				conn.closeSession();
		}
		return result;
	}
	/**
	 * oralce语法 需要修改 卞以刚 2011-12-22
	 * 根据机构ID，报表年份，月份，查询虚拟机构需汇总报表，报送情况(ok)
	 * */
	public static List getVorgReport(String orgId,Integer year,Integer term) throws Exception{
		List result=null;
		DBConn conn=null;
		Session session=null;
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		Aditing aditing=null;
		try{
			result=new ArrayList();
			String rep_freq="";
 			if(term == 12)
 				rep_freq = "'1','2','3','4'";
 			else if(term == 6)
 				rep_freq = "'1','2','3'";
 			else if(term == 3 || term == 9)
 				rep_freq = "'1','2'";
 			else rep_freq = "'1'";
 			
 			String sql="";
 			/**已增加数据库判断 卞以刚 2012-01-18*/
 			if(Config.DB_SERVER_TYPE.equals("oracle"))
 			{
 				sql="select a.*,nvl(b.SB,0) as SB from (";
 			}
 			if(Config.DB_SERVER_TYPE.equals("sqlserver"))
 				sql="select a.*,isnull(b.SB,0) as SB from (";
			sql+=" select VREP.CHILD_REP_ID,VREP.VERSION_ID,VREP.DATA_RANGE_ID,VREP.REP_FREQ_ID,vrep.CUR_NAME,COUNT(0) AS YB from view_m_report vrep"+
				" inner join ( select * from view_m_report vmrep where vmrep.ORG_ID in ("+
				" select t.org_id as  org_id from af_collect_relation t where t.collect_id='"+orgId+"' )  ) c" +
				" on c.child_rep_id=vrep.CHILD_REP_ID and c.version_id=vrep.VERSION_ID and c.data_range_id=vrep.DATA_RANGE_ID and c.REP_FREQ_ID=vrep.REP_FREQ_ID"+
				" where vrep.ORG_ID='"+orgId+"' and vrep.REP_FREQ_ID in ("+rep_freq+") GROUP BY VREP.CHILD_REP_ID,VREP.VERSION_ID,VREP.DATA_RANGE_ID,VREP.REP_FREQ_ID,VREP.CUR_ID,vrep.CUR_NAME) a"+
				" left join" +
				" (SELECT CHILD_REP_ID,VERSION_ID,DATA_RANGE_ID,COUNT(0) AS SB FROM REPORT_IN REP "+
				" WHERE  REP.YEAR="+year.intValue()+" AND REP.TERM="+term.intValue()+" AND REP.TIMES=1 and rep.check_flag=1 AND REP.ORG_ID IN (select t.org_id as  org_id from af_collect_relation t where t.collect_id='"+orgId+"')"+
				" GROUP BY CHILD_REP_ID,VERSION_ID,DATA_RANGE_ID) b on a.CHILD_REP_ID=b.child_rep_id and a.version_id=b.version_id and a.data_range_id=b.data_range_id";
 			
// 			String sql="select a.*,nvl(b.SB,0) as SB from ("+
// 						" select VREP.CHILD_REP_ID,VREP.VERSION_ID,VREP.DATA_RANGE_ID,VREP.REP_FREQ_ID,vrep.CUR_NAME,COUNT(0) AS YB from view_m_report vrep"+
// 						" inner join ( select * from view_m_report vmrep where vmrep.ORG_ID in ("+
// 						" select t.org_id as  org_id from af_collect_relation t where t.collect_id=? )  ) c" +
// 						" on c.child_rep_id=vrep.CHILD_REP_ID and c.version_id=vrep.VERSION_ID and c.data_range_id=vrep.DATA_RANGE_ID and c.REP_FREQ_ID=vrep.REP_FREQ_ID"+
// 						" where vrep.ORG_ID=? and vrep.REP_FREQ_ID in (?) GROUP BY VREP.CHILD_REP_ID,VREP.VERSION_ID,VREP.DATA_RANGE_ID,VREP.REP_FREQ_ID,VREP.CUR_ID,vrep.CUR_NAME) a"+
// 						" left join" +
// 						" (SELECT CHILD_REP_ID,VERSION_ID,DATA_RANGE_ID,COUNT(0) AS SB FROM REPORT_IN REP "+
// 						" WHERE  REP.YEAR=? AND REP.TERM=? AND REP.TIMES=? AND REP.ORG_ID IN (select t.org_id as  org_id from af_collect_relation t where t.collect_id=?)"+
// 						" GROUP BY CHILD_REP_ID,VERSION_ID,DATA_RANGE_ID) b on a.CHILD_REP_ID=b.child_rep_id and a.version_id=b.version_id and a.data_range_id=b.data_range_id";
 			conn=new DBConn();
			session=conn.openSession();
			con=session.connection();
			pstmt=con.prepareStatement(sql);
//			pstmt.setString(1, orgId);
//			pstmt.setString(2, orgId);
//			pstmt.setString(3, rep_freq);
//			pstmt.setInt(4, year.intValue());
//			pstmt.setInt(5, term.intValue());
//			pstmt.setInt(6, 1);
//			pstmt.setString(7, orgId);
			rs=pstmt.executeQuery();
			while(rs.next()){
				aditing=new Aditing();
				aditing.set_childRepId(rs.getString(1));
				aditing.setVersionId(rs.getString(2));
				aditing.setDataRgId(new Integer(rs.getInt(3)));
				aditing.setActuFreqID(new Integer(rs.getInt(4)));
				aditing.setCurrName(rs.getString(5));
				aditing.setNeedOrgCount(new Integer(rs.getInt(6)));
				aditing.setDonum(new Integer(rs.getInt(7)));
				aditing.setOrgId(orgId);
				aditing.setRepName(StrutsMChildReportDelegate.getname(aditing.getChildRepId(), aditing.getVersionId()));
				aditing.setYear(year);
				aditing.setTerm(term);
				aditing.setOrgName(StrutsOrgNetDelegate.getOrgName(aditing.getOrgId()));
				aditing.setDataRgTypeName(StrutsMDataRgTypeDelegate.getdatename(aditing.getDataRgId()));
				aditing.setCurId(StrutsMCurrDelegate.getMCurr(aditing.getCurrName()).getCurId());
				if(aditing.getActuFreqID()==1)
					aditing.setActuFreqName("月");
				if(aditing.getActuFreqID()==2)
					aditing.setActuFreqName("季");
				if(aditing.getActuFreqID()==3)
					aditing.setActuFreqName("半年");
				if(aditing.getActuFreqID()==4)
					aditing.setActuFreqName("年");
				if (isCollected(aditing.getChildRepId(),aditing.getVersionId(),aditing.getDataRgId(),aditing.getOrgId(),year,term))
					aditing.setIsCollected(new Integer(1));
				else 
					aditing.setIsCollected(new Integer(0));
				aditing.setRepInFo(aditing.getChildRepId()+"["+aditing.getDonum()+"/"+aditing.getNeedOrgCount()+"]");
				
				String timesSql="from ReportIn ri where  "
					 	+" ri.MChildReport.comp_id.childRepId='"+ aditing.getChildRepId()+"'"
						+" and ri.MChildReport.comp_id.versionId='"+aditing.getVersionId()+"'"
					//	+" and ri.MDataRgType.dataRangeId="+aditing.getDataRangeId()
						+" and ri.year="+year
						+" and ri.term="+term
						+" and ri.MCurr.curId="+aditing.getCurId()
						+" and ri.orgId='"+aditing.getOrgId()+"'"
						+" and ri.times=-1";
					List list = session.find(timesSql);
					
				
					if(list != null && list.size() > 0){
						ReportIn reportIn = (ReportIn)list.get(0);        							
						aditing.setRepInId(reportIn.getRepInId());
						aditing.setIsCollected(new Integer(1));
						
					}else{
						aditing.setIsCollected(new Integer(0));
					}
				
				result.add(aditing);
			}
		}catch(Exception e){
			throw e;
		}finally{
			if (conn!=null)
				conn.closeSession();
		}
		return result;
	}
	/**
	 * 根据 报表id,版本号，机构Id,口径Id,报表年份、月份 查询报表是否已汇总
	 * **/
	private static boolean isCollected(String childRepId,String versionId,Integer dataRangeId,String orgId,int year,int term)throws Exception{
		boolean result=false;
		DBConn conn=null;
		Session session=null;
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		try{
			String sql="select rep.times from report_in rep where rep.org_id=? and rep.child_rep_id=? and rep.version_id=? and rep.data_range_id=? and rep.year=? and rep.term=? and rep.times=-1";
			conn=new DBConn();
			session=conn.openSession();
			con=session.connection();
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, orgId);
			pstmt.setString(2, childRepId);
			pstmt.setString(3, versionId);
			pstmt.setInt(4, dataRangeId.intValue());
			pstmt.setInt(5, year);
			pstmt.setInt(6, term);
			rs=pstmt.executeQuery();
			if (rs.next())
				result=true;
		}catch(Exception e){
			throw e;
		}finally{
			if (conn!=null)
				conn.closeSession();
		}
		return result;
	}
	/**
	 * 已使用hibernate 卞以刚 2011-12-21
	 * 根据登录用户，查找拥有的权限机构ID
	 * **/
	public static String getOrgIds(Operator op)throws Exception{
		String orgIds=null;
		DBConn conn=null;
		Session session=null;
		Query query=null;
		List aforgLst=null;
		ViewOrgRep org=null;
		try{
			String hql=op.getChildRepReportPopedom();
			hql=hql.substring(hql.indexOf("from"));
			hql="select distinct viewOrgRep.orgId "+hql;
			conn=new DBConn();
			session=conn.openSession();
			query=session.createQuery(hql);
			aforgLst=query.list();
			if (aforgLst!=null && aforgLst.size()>0){
				for(int i=0;i<aforgLst.size();i++){
					if (orgIds!=null && !orgIds.equals("")){
						orgIds=orgIds+","+(String)aforgLst.get(i);
					}else{
						orgIds=(String)aforgLst.get(i);
					}
				}
			}
		}catch(Exception e){
			throw e;
		}finally{
			if (conn!=null)
				conn.closeSession();
		}
		
		return orgIds;
	}
	/**
	 * jdbc技术 需要修改 卞以刚 2011-12-22
	 * 获取报表应报机构
	 * */
	public static List getRepOrg(String childrepId,String versionId,Integer dataRgId,Integer year,Integer term,Integer curId,String perOrgId)throws Exception{
		List result=null;
		DBConn conn=null;
		Session session=null;
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		Aditing aditing=null;
		
		AfOrg ao = AFOrgDelegate.getOrgInfo(perOrgId);
		
		try{
			result=new ArrayList();
			conn=new DBConn();
			session=conn.openSession();
			con=session.connection();
			
			String sql = "";
			
			if(ao!=null && ao.getIsCollect().toString().equals("1")){
				
				sql=" select vmrep.ORG_ID,vmrep.CHILD_REP_ID,vmrep.VERSION_ID," +
						"vmrep.DATA_RANGE_ID,vmrep.REP_FREQ_ID,vmrep.CUR_ID from view_m_report vmrep "+
						"inner join m_rep_range mrr on mrr.child_rep_id=vmrep.CHILD_REP_ID " +
						"and mrr.version_id=vmrep.VERSION_ID and mrr.org_id='"+perOrgId+"'"+
						"where vmrep.ORG_ID in (select afcr.org_id from af_collect_relation afcr " +
						"where afcr.collect_id='"+perOrgId+"') and vmrep.CHILD_REP_ID='"+childrepId+"' " +
								"and vmrep.VERSION_ID='"+versionId+"' " +
								"and vmrep.CUR_ID='"+curId+"' and vmrep.DATA_RANGE_ID="+dataRgId;
			}else{
			
				sql="select vmrep.ORG_ID,vmrep.CHILD_REP_ID,vmrep.VERSION_ID," +
						" vmrep.DATA_RANGE_ID,vmrep.REP_FREQ_ID,vmrep.CUR_ID from view_m_report vmrep " +
						" where vmrep.ORG_ID in " +
						" (select ag.org_id from af_org ag where ag.pre_org_id='"+ perOrgId + "')" +
						" and vmrep.CHILD_REP_ID=" +
						" (select cr.real_rep_id from collect_real cr " +
						" where cr.child_rep_id='"+ childrepId + "' and cr.cur_id='"+curId+"' and cr.version_id='"+versionId +"')" +
						" and vmrep.VERSION_ID='"+versionId +"' and vmrep.CUR_ID='"+curId+"' and vmrep.DATA_RANGE_ID="+ dataRgId;
			}
			//////////////////////////////////////////// 2013-11-05  添加curid条件
			pstmt=con.prepareStatement(sql);
//			pstmt.setString(1, perOrgId);
//			pstmt.setString(2, perOrgId);
//			pstmt.setString(3, childrepId);
//			pstmt.setString(4, versionId);
//			pstmt.setInt(5, dataRgId.intValue());
			rs=pstmt.executeQuery();
			while(rs.next()){
				aditing=new Aditing();
				aditing.setOrgId(rs.getString(1));
				aditing.setChildRepId(rs.getString(2));
				aditing.setVersionId(rs.getString(3));
				aditing.setDataRgId(rs.getInt(4));
				aditing.setActuFreqID(rs.getInt(5));
				aditing.setCurId(rs.getInt(6));
				aditing.setOrgName(AFOrgDelegate.selectOne(aditing.getOrgId()).getOrgName());
				if(isReped(aditing.getOrgId(), aditing.getChildRepId(), aditing.getVersionId(), aditing.getDataRgId(),aditing.getCurId() ,year, term))
					aditing.setIsPass(new Integer(1));
				else
					aditing.setIsPass(new Integer(0));
				result.add(aditing);
			}
		}catch(Exception e){
			throw e;
		}finally{
			if(conn!=null)
				conn.closeSession();
		}
		return result;
	}
	/**
	 * 根据机构ID，ChildRepId,版本号，口径ID，报表年份，报表月份，判断报表是否已经上报
	 * */
	private static boolean isReped(String orgId,String childRepId,String versionId,Integer dataRgId,Integer curId,Integer year,Integer term)throws Exception{
		boolean result=false;
		DBConn conn =null;
		Session session=null;
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		try{
			String sql=" select * from report_in rep where rep.org_id=? and rep.child_rep_id=? and rep.version_id=? and rep.cur_id=? and rep.year=? and rep.term=? and rep.data_range_id=? and rep.times=1 and rep.check_flag=1";
			conn=new DBConn();
			session=conn.openSession();
			con=session.connection();
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, orgId);
			pstmt.setString(2, childRepId);
			pstmt.setString(3, versionId);
			pstmt.setInt(4, curId);
			pstmt.setInt(5, year.intValue());
			pstmt.setInt(6, term.intValue());
			pstmt.setInt(7, dataRgId.intValue());
			rs=pstmt.executeQuery();
			if (rs.next())
				result=true;
		}catch(Exception e){
			result=false;
			throw e;
		}finally{
			if(conn!=null)
				conn.closeSession();
		}
		
		return result;
	}
}
