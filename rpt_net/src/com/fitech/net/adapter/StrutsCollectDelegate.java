package com.fitech.net.adapter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.smis.adapter.StrutsReportInDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.form.ReportInInfoForm;
import com.cbrc.smis.hibernate.MChildReportPK;
import com.cbrc.smis.hibernate.ReportIn;
import com.cbrc.smis.hibernate.ReportInInfo;
import com.cbrc.smis.hibernate.ReportInInfoPK;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.fitech.net.collect.bean.CollectReport;
import com.fitech.net.collect.bean.ReportMapping;
import com.fitech.net.hibernate.CollectReal;
import com.fitech.net.hibernate.OrgNet;
/**
*
* <p>Title: StrutsCollectDelegate </p>
*
* <p>Description 汇总数据库操作代理类，包括汇总单个报报表和汇总所有有上报数据的报表</p>
*
* <p>Copyright: Copyright (c) 2007</p>
*
* <p>Company: </p>
*
* @author   龚明
* @date     2009-09-12
* @version 1.0
*/

public class StrutsCollectDelegate {

    private static FitechException log=new FitechException(StrutsCollectDelegate.class);
    
    /**
     * 生成汇总报表
     * @param reportsIds        String      实际上报子报表Id字符串
     * @param reportIn          ReportIn    新生成的内网表单表
     * @param operator          Operator    操作员
     * 
     * @return  汇总成功返回true  否则返回false
     */
    public static boolean saveCollectValue(String reportsIds,ReportIn reportIn,Operator operator){
        boolean save = false;
        DBConn conn=null;
        Session session=null;
        if(reportsIds == null || reportIn == null || operator == null) return false;       
        try{            
            //判断是否是总行级的操作员，给出是否使用映射关系汇总
            OrgNet org = StrutsOrgNetDelegate.selectOne(operator.getOrgId());
            boolean pre_Org_Id = false;
            String preOrgId = org.getPreOrgId();
            if("-1".equals(preOrgId))
                pre_Org_Id = true;
                
            conn = new DBConn();
            session = conn.beginTransaction();
            if(save(session,reportsIds,reportIn,pre_Org_Id)){
                session.flush();
                save = true;
            }
        }catch(Exception he){
           log.printStackTrace(he);
        }finally{              
            if (conn!=null) conn.endTransaction(save);         
        }
        return save;
    }
 
    /**
     * 生成所有汇总报表
     * @param repInIdList List   数组集合 
     * @param operator Operator 操作员
     * 
     * @return 汇总成功返回true  否则返回false
     */
    public static boolean saveAllCollectValue(List repInIdLst,Operator operator){
        boolean save = false;
        DBConn conn=null;
        Session session=null;
        
        if(repInIdLst == null || repInIdLst.isEmpty() || operator == null) 
             return  false;
        
        try{           
            //判断是否是总行级的操作员，给出是否使用映射关系汇总
            OrgNet org = StrutsOrgNetDelegate.selectOne(operator.getOrgId());
            boolean pre_Org_Id = false;
            String preOrgId = org.getPreOrgId();
            if("-1".equals(preOrgId))
                pre_Org_Id = true;
            conn = new DBConn();
            session = conn.beginTransaction();
            Iterator itr = repInIdLst.iterator();
            while(itr.hasNext()){
                Object rep[] = (Object[]) itr.next();
                save(session,rep[0].toString(),(ReportIn)rep[1],pre_Org_Id);
            }                   
            session.flush();
            save = true;
        }catch(Exception he){
           log.printStackTrace(he);
        }finally{              
            if (conn!=null) conn.endTransaction(save);         
        }
        return save;
    }
    
    /**
     * 内部公用汇总数据方法
     * @param session           Session
     * @param reportsIds        String      实际上报子报表Id字符串
     * @param reportIn          ReportIn    新生成的内网表单表
     * @param preOrgId          boolean     是否总行操作员
     * @throws Exception
     */
    private static boolean save(Session session,String reportsIds,ReportIn reportIn,boolean preOrgId) throws Exception{
        boolean save = false;
        // 汇总加总关系的上报数据
        StringBuffer hql = new StringBuffer();
        if(Config.DB_SERVER_TYPE.equals("oracle")){
        	 hql.append("select t.cell_Id,trim(to_char(round(sum(to_number(t.report_Value)),2),'999999999999990.99')) from Report_In_Info t");
             hql.append(" left join M_Cell m on t.cell_Id = m.cell_Id where m.data_Type=2 and m.collect_Type=" + Config.COLLECT_TYPE_SUM);
             hql.append(" and t.rep_In_Id in (" + reportsIds + ") group by t.cell_Id");
             //汇总求平均值的
             hql.append(" union all");
             hql.append(" select t1.cell_Id,trim(to_char(round(avg(to_number(t1.report_Value)),2),'999999999999990.99')) from Report_In_Info t1");
             hql.append(" left join M_cell m1 on t1.cell_Id = m1.cell_Id where m1.collect_Type=" + Config.COLLECT_TYPE_AVG + " and");
             hql.append(" t1.rep_In_Id in (" + reportsIds + ") group by t1.cell_Id");
             //汇总求最大值的
             hql.append(" union all");
             hql.append(" select t2.cell_Id,trim(to_char(round(max(to_number(t2.report_Value)),2),'999999999999990.99')) from Report_In_Info t2");
             hql.append("  left join M_Cell m2 on t2.cell_Id = m2.cell_Id where m2.collect_Type=" + Config.COLLECT_TYPE_MAX + " and");
             hql.append(" t2.rep_In_Id in (" + reportsIds + ") group by t2.cell_Id");
             //汇总求最小值的
             hql.append(" union all");
             hql.append(" select t3.cell_Id,trim(convert(varchar,round(min(convert(numeric(38,2),t3.report_Value)),2),'999999999999990.99')) from Report_In_Info t3");
             hql.append("  left join M_Cell m3 on t3.cell_Id = m3.cell_Id where m3.collect_Type=" + Config.COLLECT_TYPE_MIN+" and");
             hql.append(" t3.rep_In_Id in (" + reportsIds + ") group by t3.cell_Id");
        }
        if(Config.DB_SERVER_TYPE.equals("db2")){
          	 hql.append("select t.cell_Id,trim(char(round(sum(decimal(t.report_Value)),2),'999999999999990.99')) from Report_In_Info t");
               hql.append(" left join M_Cell m on t.cell_Id = m.cell_Id where m.data_Type=2 and m.collect_Type=" + Config.COLLECT_TYPE_SUM);
               hql.append(" and t.rep_In_Id in (" + reportsIds + ") group by t.cell_Id");
               //汇总求平均值的
               hql.append(" union all");
               hql.append(" select t1.cell_Id,trim(char(round(avg(decimal(t1.report_Value)),2),'999999999999990.99')) from Report_In_Info t1");
               hql.append(" left join M_cell m1 on t1.cell_Id = m1.cell_Id where m1.collect_Type=" + Config.COLLECT_TYPE_AVG + " and");
               hql.append(" t1.rep_In_Id in (" + reportsIds + ") group by t1.cell_Id");
               //汇总求最大值的
               hql.append(" union all");
               hql.append(" select t2.cell_Id,trim(char(round(max(decimal(t2.report_Value)),2),'999999999999990.99')) from Report_In_Info t2");
               hql.append("  left join M_Cell m2 on t2.cell_Id = m2.cell_Id where m2.collect_Type=" + Config.COLLECT_TYPE_MAX + " and");
               hql.append(" t2.rep_In_Id in (" + reportsIds + ") group by t2.cell_Id");
               //汇总求最小值的
               hql.append(" union all");
               hql.append(" select t3.cell_Id,trim(char(round(min(decimal(t3.report_Value)),2),'999999999999990.99')) from Report_In_Info t3");
               hql.append("  left join M_Cell m3 on t3.cell_Id = m3.cell_Id where m3.collect_Type=" + Config.COLLECT_TYPE_MIN+" and");
               hql.append(" t3.rep_In_Id in (" + reportsIds + ") group by t3.cell_Id");
          }
        if(Config.DB_SERVER_TYPE.equals("sqlserver")){
        	 hql.append("select t.cell_Id,trim(convert(varchar,round(sum(convert(numeric(38,2),t.report_Value)),2),'999999999999990.99')) from Report_In_Info t");
             hql.append(" left join M_Cell m on t.cell_Id = m.cell_Id where m.data_Type=2 and m.collect_Type=" + Config.COLLECT_TYPE_SUM);
             hql.append(" and t.rep_In_Id in (" + reportsIds + ") group by t.cell_Id");
             //汇总求平均值的
             hql.append(" union all");
             hql.append(" select t1.cell_Id,trim(convert(varchar,round(avg(convert(numeric(38,2),t1.report_Value)),2),'999999999999990.99')) from Report_In_Info t1");
             hql.append(" left join M_cell m1 on t1.cell_Id = m1.cell_Id where m1.collect_Type=" + Config.COLLECT_TYPE_AVG + " and");
             hql.append(" t1.rep_In_Id in (" + reportsIds + ") group by t1.cell_Id");
             //汇总求最大值的
             hql.append(" union all");
             hql.append(" select t2.cell_Id,trim(convert(varchar,round(max(convert(numeric(38,2),t2.report_Value)),2),'999999999999990.99')) from Report_In_Info t2");
             hql.append("  left join M_Cell m2 on t2.cell_Id = m2.cell_Id where m2.collect_Type=" + Config.COLLECT_TYPE_MAX + " and");
             hql.append(" t2.rep_In_Id in (" + reportsIds + ") group by t2.cell_Id");
             //汇总求最小值的
             hql.append(" union all");
             hql.append(" select t3.cell_Id,trim(convert(varchar,round(min(convert(numeric(38,2),t3.report_Value)),2),'999999999999990.99')) from Report_In_Info t3");
             hql.append("  left join M_Cell m3 on t3.cell_Id = m3.cell_Id where m3.collect_Type=" + Config.COLLECT_TYPE_MIN+" and");
             hql.append(" t3.rep_In_Id in (" + reportsIds + ") group by t3.cell_Id");
        }
        
        // 单元格映射集合
        Map cellIdMap = null;
        ReportMapping rMapping = null;
        ResultSet sumSet = session.connection().createStatement().executeQuery(hql.toString());
        //更新reportInInfo
        boolean updateValue = true;
        if (sumSet.isBeforeFirst()) {
            // 如果是总行就按表格映射关系重新配置汇总单元格 Id
            if (preOrgId) {
                // 查询上报映射表
                String strHql = "from ReportMapping t where t.srcRepId =:srcRepId and ";
                       strHql += "t.srcVersionId =:srcVersionId";
                Query query = session.createQuery(strHql);
                query.setString("srcRepId", reportIn.getMChildReport().getComp_id().getChildRepId());
                query.setString("srcVersionId", reportIn.getMChildReport().getComp_id().getVersionId());
              
                rMapping = (ReportMapping) query.uniqueResult();
                //如果汇总的报表存在映射关系
                if(rMapping != null){
                    //重新设置reportIn
                    MChildReportPK key = new MChildReportPK();
                    key.setChildRepId(rMapping.getTarRepId());
                    key.setVersionId(rMapping.getTarVersionId());
                    reportIn.getMChildReport().setComp_id(key);

                    // 获取源单元格 Id和目标单元格Id
                    StringBuffer sbfSql = new StringBuffer();
                    sbfSql.append("select c.cell_Id,d.cell_Id from Cell_Mapping a");
                    sbfSql.append(" left join Report_Mapping b on a.rep_map_Id = b.rep_map_Id");
                    sbfSql.append(" left join M_Cell c on a.src_cell_name = c.cell_name and c.child_rep_id = b.src_rep_id");
                    sbfSql.append(" left join M_Cell d on a.tar_cell_name = d.cell_name and d.child_rep_id = b.tar_rep_id");
                    sbfSql.append(" where b.rep_map_id = ? and b.src_version_id = ?");
                    sbfSql.append(" and b.tar_varsion_id = ? and c.cell_Id is not null");
                    sbfSql.append(" and d.cell_Id is not null");
                    
                    PreparedStatement pStmt = session.connection().prepareStatement(sbfSql.toString());
                    pStmt.setInt(1, rMapping.getRepMapId().intValue());
                    pStmt.setString(2,rMapping.getSrcVersionId());
                    pStmt.setString(3,rMapping.getTarVersionId());
                    ResultSet rs = pStmt.executeQuery();
                    
                    cellIdMap = new HashMap();
                    while (rs.next()) {
                        //key源单元格Id value目标单元格Id
                        cellIdMap.put(new Integer(rs.getInt(1)), new Integer(rs.getInt(2)));
                    }
                }
            }
            // 保存内网实际表单表
            
            reportIn.setTimes(new Integer("-1"));
          
            //保存上报汇总表
            CollectReport report = new CollectReport();
            report.getId().setVersionId(reportIn.getMChildReport().getComp_id().getVersionId());
            report.getId().setChildRepId(reportIn.getMChildReport().getComp_id().getChildRepId());
            
            report.getId().setYear(reportIn.getYear());
            report.getId().setTerm(reportIn.getTerm());
            report.getId().setOrgId(reportIn.getOrgId());
            
            //获取当前报表的当期汇总记录
            CollectReport temp = (CollectReport)session.get(CollectReport.class,report.getId());
          
            //如果没有当前报表的当期汇总记录,表Report_In,Report_In_Info,Collet_Report均插入记录
            if(temp == null){
                  session.save(reportIn); 
                  report.setRepInId(reportIn.getRepInId());
                  session.save(report);
                  updateValue = false;
            }else{
                //只更新表Report_In_Info
                reportIn.setRepInId(temp.getRepInId());
            }
           
            while (sumSet.next()) {                
                ReportInInfo value = new ReportInInfo();
                ReportInInfoPK key = new ReportInInfoPK();
                // 单元格 Id
                Integer cellId = new Integer(sumSet.getInt(1));
                key.setCellId(cellId);
                // 如果报表映射关系存在，映射单元格Id 的集合存在
                if (rMapping != null){
                    if (cellIdMap != null && !cellIdMap.isEmpty()) {
                        //判断是否有要替换的单元格
                        if(cellIdMap.containsKey(cellId)){
                            key.setCellId((Integer) cellIdMap.get(cellId));
                        }else{
                            continue;
                        }
                    } else {
                        continue;
                    }
                }
                key.setRepInId(reportIn.getRepInId());
                value.setComp_id(key);
                value.setReportValue(sumSet.getString(2));               
                
                //保存上报数据    
                if(updateValue) session.update(value);
                else session.save(value);
            }
            save = true;
        }
        return save;
    }
   
    /**
     * 转汇总数据为上报数据
     * @param reportIn      ReportIn        内网表单表
     * @return  成功返回true        失败返回false
     */
    public static boolean transCollectDatat(ReportIn reportIn){
    	boolean translate = false;
    	DBConn conn=null;
    	Session session=null;
        
    	try{            
    		conn = new DBConn();
    		session = conn.beginTransaction();
    		ReportIn report_In =StrutsReportInDelegate.getReportIn(reportIn.getMChildReport().getComp_id().getChildRepId(),reportIn.getMChildReport().getComp_id().getVersionId(),
    				reportIn.getOrgId(),reportIn.getYear(),reportIn.getTerm(),reportIn.getMDataRgType().getDataRangeId(),
    				reportIn.getMCurr().getCurId(),new Integer(1));
    		
    		//如果数据库中已经有这条记录，则更新这条记录
    		//report_In为上报的报表记录
    		if(report_In!=null){	
    			report_In.setCheckFlag(com.fitech.net.config.Config.CHECK_FLAG_AFTERSAVE);
 				session.update(report_In);
 				String deleteHql="from ReportInInfo rii where rii.comp_id.repInId="+report_In.getRepInId().intValue();
 				session.delete(deleteHql);
 				session.flush();
 			}else{
 			//如果数据库中没有这条记录，即目前没有载入报表，是直接点击"转上报数据"
 				report_In=reportIn;
 				report_In.setRepInId(null);
 				report_In.setTimes(new Integer("1"));
 				report_In.setCheckFlag(com.fitech.net.config.Config.CHECK_FLAG_AFTERSAVE);
                session.save(report_In);
                session.flush();
 			}
             
    		//判断有对应汇总数值在Report_In_Info表中
    		Set reportInInfos = reportIn.getReportInInfos();
    		if(reportInInfos != null){
    			Iterator itr = reportInInfos.iterator();
    			while(itr.hasNext()){
    				ReportInInfo  value = (ReportInInfo)itr.next();
    				value.getComp_id().setRepInId(report_In.getRepInId());
    				//保存上报数据值到Report_In_Info中
    				session.save(value);
    			}
    			session.flush();
    		}  
    		translate = true;
    	}catch(Exception he){
    		log.printStackTrace(he);
    	}finally{
    		if (conn!=null) conn.endTransaction(translate);
    	} 
    	return translate;
    }
    
    /**
     *<p>描述:汇总方法入口,成功返回记录ID 失败返回 null</p>
     *<p>参数:crId 关联ID orgId 机构Id  subOrgIds 下级机构ID</p>
     *<p>日期：2007-10-29</p>
     *<p>作者：曹发根</p>
     */
    public Integer doCollect(String childRepId,String versionId,String orgId,
    		String subOrgIds,Integer year,Integer term,Integer dataRangeId,Integer curId){
    	
    	Integer repInId=null;
    	DBConn conn=null;
    	Session session=null;
    	Connection connection=null;
    	PreparedStatement ps=null;
    	Statement stmt = null;
    	boolean result=false;
    	List sqlList = null;
    	
    	try{
    		conn = new DBConn();
            session = conn.beginTransaction();
            connection=session.connection();
            connection.setAutoCommit(false);
            stmt=connection.createStatement();
           
            ps=connection.prepareStatement("select REP_IN_ID FROM REPORT_IN where CHILD_REP_ID=? and VERSION_ID=? and DATA_RANGE_ID=? " +
            				"and TIMES=? and ORG_ID=? and YEAR=? and TERM=? and CUR_ID=? ");
            ps.setString(1, childRepId);
            ps.setString(2, versionId);
            ps.setInt(3, dataRangeId.intValue());
            ps.setInt(4, -1);
            ps.setString(5, orgId);
            ps.setInt(6,year.intValue());
            ps.setInt(7, term.intValue());
            ps.setInt(8, curId.intValue());
            
            ResultSet rs=ps.executeQuery();
            if(rs!= null && rs.next()){
            	repInId = new Integer(rs.getInt("REP_IN_ID"));
            	stmt.addBatch("delete from REPORT_IN_INFO where REP_IN_ID="+repInId);
            }else{  //第一次汇总数据....
            	String seqOracle="select SEQ_REPORT_IN.nextval from dual";
            	String seqDB2="values nextval for SEQ_REPORT_IN";
            	String sqlSqlserver="select max(rep_in_id)+1 from REPORT_IN";
            	if(Config.DB_SERVER_TYPE.equals("oracle")){
        			rs=stmt.executeQuery(seqOracle);
        		}
        		if(Config.DB_SERVER_TYPE.equals("db2")){
        			rs=stmt.executeQuery(seqDB2);
        		}
        		if(Config.DB_SERVER_TYPE.equals("sqlserver"))
        			rs=stmt.executeQuery(sqlSqlserver);
        		if(rs.next()){
        			//获得新的报送id的序列
        			repInId=new Integer(rs.getInt(1));
        		}
        		
        		String reptName=""; //报表名称
        		ps = connection.prepareStatement("select a.REPORT_NAME,b.REP_CN_NAME from M_CHILD_REPORT a left join M_MAIN_REP b on a.REP_ID=b.REP_ID where a.CHILD_REP_ID=? and a.VERSION_ID=?");
        		ps.setString(1,childRepId);
        		ps.setString(2,versionId);
        		rs = ps.executeQuery();
        		if(rs.next()){
            		if(rs.getString(1).equals(rs.getString(2))){
            			reptName=rs.getString(1);
            		}else{
            			reptName=rs.getString(2)+":"+rs.getString(1);
            		}
        		}

        		stmt.addBatch("insert into REPORT_IN(CHILD_REP_ID, VERSION_ID, DATA_RANGE_ID, " +
            				  " TIMES,ORG_ID,YEAR,TERM,CUR_ID,REP_IN_ID,CHECK_FLAG,REP_NAME) values('"+ childRepId +
            				  "','"+ versionId +"',"+ dataRangeId +",-1,'"+ orgId +"',"+ year +","+ term +","+ curId +
            				  ","+ repInId +",3,'"+ reptName +"')");
            }
           
            //查看模板的汇总方式
            ps=connection.prepareStatement("select IS_ORDER,REAL_REP_ID,REAL_VERSION_ID,ORDER_COL_ID,TOTAL_ROW,START_ROW FROM COLLECT_REAL " +
            		"where CHILD_REP_ID=? and VERSION_ID=? and DATA_RANGE_ID=? and CUR_ID=?");
            ps.setString(1, childRepId);
            ps.setString(2, versionId);
            ps.setInt(3, dataRangeId.intValue());
            ps.setInt(4, curId.intValue());
            rs=ps.executeQuery();
            if(rs.next()){
            	if(rs.getInt(1)!=1){
            		sqlList = doCommonCollect(connection, repInId, childRepId, versionId,orgId, subOrgIds, year, term, curId);
            	}else{
            		sqlList = doOrderCollect(connection, repInId, childRepId, versionId, subOrgIds, year, term, curId,rs.getString("ORDER_COL_ID"),
            				rs.getInt("TOTAL_ROW"),rs.getInt("START_ROW"),rs.getString("REAL_REP_ID"),rs.getString("REAL_VERSION_ID"));
            	}
            	if(sqlList == null) return repInId = null;
            	if(sqlList.size() == 0) return new Integer(-1);
            	
            	for(int i=0;i<sqlList.size();i++){
        			stmt.addBatch((String)sqlList.get(i));
        		}
            }
            stmt.executeBatch();
            result = true;
    	}catch(Exception e){
    		result = false;
    		repInId = null;    		
    		e.printStackTrace();
    	}finally{
    		try {
				if(result == true) connection.commit();
				else connection.rollback();
				connection.setAutoCommit(true);
//				if(stmt != null) stmt.close();
//				if(ps != null) ps.close();
//				if(connection != null) connection.close();
//				if(session != null) session.close();
				if(conn != null) conn.closeSession();
			} catch (SQLException e1) {
				e1.printStackTrace();
			} 
    	}
    	return repInId;
    }
    /**
     *<p>描述:普通报表</p>
     *<p>参数:</p>
     *<p>日期：2007-10-29</p>
     *<p>作者：曹发根</p>
     */
    private List doCommonCollect(Connection connection,Integer repInId,String childRepId,String versionId,String orgId,String subOrgIds,Integer year,Integer term,Integer curId ){
    	List sqlList = new ArrayList();
    	ResultSet rs = null;
    	ResultSet rsVal = null;
    	PreparedStatement ps = null;
    	PreparedStatement ppst = null;
    	
    	try{
    		ps=connection.prepareStatement("select b.CELL_ID,a.CELL_NAME,a.CD_SQL from COLLECT_DETAIL a left join M_CELL b on " +
    				"a.CELL_NAME=b.CELL_NAME and a.CHILD_REP_ID=b.CHILD_REP_ID and a.VERSION_ID=b.VERSION_ID " +
					"where a.CHILD_REP_ID=? and a.VERSION_ID=? and a.CD_TYPE=1");
    		ps.setString(1,childRepId);
    		ps.setString(2,versionId);
    		rs=ps.executeQuery();
    		while(rs.next()){
    			String sql=rs.getString("CD_SQL");
    			int cellId=rs.getInt("CELL_ID");
    			if(sql!=null){
    				sql=replace(sql,"$YEAR", year.toString());
    				sql=replace(sql,"$TERM", term.toString());
    				sql=replace(sql,"$SUBORG", subOrgIds.toString());
    				sql=replace(sql,"$ORG", orgId);
    				sql=replace(sql,"$CUR_ID",curId.toString());
    				
    				String insertSql="insert into REPORT_IN_INFO (REP_IN_ID,CELL_ID,REPORT_VALUE) select "+repInId+","+cellId+",VAL from("+sql+")";
    				sqlList.add(insertSql);
    			}
    		}
    		//需要汇总的单元格
    		String sumCell="select c.cell_id as tar_cell_Id,b.cell_id as map_cell_id,MAP_CHILD_REP_ID,MAP_VERSION_ID from  COLLECT_DETAIL a " +
    				"left join m_cell b on a.MAP_CHILD_REP_ID=b.CHILD_REP_ID and a.MAP_VERSION_ID=b.VERSION_ID and a.MAP_CELL_NAME=b.CELL_NAME " +
    				"left join m_cell c on a.CHILD_REP_ID=c.CHILD_REP_ID and a.VERSION_ID=c.VERSION_ID and a.CELL_NAME=c.CELL_NAME " +
    				"where CD_TYPE=2 and a.CHILD_REP_ID=? and a.VERSION_ID=?";
    		
    		ps=connection.prepareStatement(sumCell);
    		ps.setString(1,childRepId);
    		ps.setString(2,versionId);
    		rs=ps.executeQuery();
    		Integer mapCellId = null;
    		String mapChildRepId = null;
    		String mapVersionId = null;
    		Integer cellId = null;
    		
    		String sql="";
    		if(Config.DB_SERVER_TYPE.equals("oracle")){
    			sql = "select sum(to_number(a.REPORT_VALUE)) as report_value from report_in_info a left join report_in b " +
			  	  "on a.REP_IN_ID=b.REP_IN_ID where  b.CHECK_FLAG =" ;
			  	if(com.cbrc.smis.common.Config.IS_NEED_CHECK==1){
			  		sql += com.fitech.net.config.Config.CHECK_FLAG_PASS;
			  	}else{
			  		sql += com.fitech.net.config.Config.CHECK_FLAG_UNCHECK;
			  	}
			  	sql += " and b.ORG_ID IN ("+subOrgIds+") and b.TERM=? and b.YEAR=? " +
			  	  "and b.CHILD_REP_ID=? and b.VERSION_ID=? and a.CELL_ID=? and a.REPORT_VALUE is not null";
    		}
    		if(Config.DB_SERVER_TYPE.equals("sqlserver")){
    			sql = "select sum(convert(numeric(38,3),a.REPORT_VALUE)) as report_value from report_in_info a left join report_in b " +
			  	  "on a.REP_IN_ID=b.REP_IN_ID where  b.CHECK_FLAG =" ;
			  	if(com.cbrc.smis.common.Config.IS_NEED_CHECK==1){
			  		sql += com.fitech.net.config.Config.CHECK_FLAG_PASS;
			  	}else{
			  		sql += com.fitech.net.config.Config.CHECK_FLAG_UNCHECK;
			  	}
			  	sql += "and b.ORG_ID IN ("+subOrgIds+") and b.TERM=? and b.YEAR=? " +
			  	  "and b.CHILD_REP_ID=? and b.VERSION_ID=? and a.CELL_ID=? and a.REPORT_VALUE is not null";
    		}
    		if(Config.DB_SERVER_TYPE.equals("db2")){
    			sql = "select sum(decimal(a.REPORT_VALUE)) as report_value from report_in_info a left join report_in b " +
			  	  "on a.REP_IN_ID=b.REP_IN_ID where  b.CHECK_FLAG =" ;
			  	if(com.cbrc.smis.common.Config.IS_NEED_CHECK==1){
			  		sql += com.fitech.net.config.Config.CHECK_FLAG_PASS;
			  	}else{
			  		sql += com.fitech.net.config.Config.CHECK_FLAG_UNCHECK;
			  	}
			  	  sql += "and b.ORG_ID IN ("+subOrgIds+") and b.TERM=? and b.YEAR=? " +
			  	  "and b.CHILD_REP_ID=? and b.VERSION_ID=? and a.CELL_ID=? and a.REPORT_VALUE is not null";
    		}
    		ppst = connection.prepareStatement(sql);
    		while(rs.next()){
    			cellId = new Integer(rs.getInt("tar_cell_id"));    			
    			mapCellId = new Integer(rs.getInt("map_cell_id"));
    			mapChildRepId = rs.getString("map_child_rep_id");
    			mapVersionId = rs.getString("map_version_id");
    			
    			ppst.setInt(1,term.intValue());
    			ppst.setInt(2,year.intValue());
    			ppst.setString(3,mapChildRepId);
    			ppst.setString(4,mapVersionId);
    			ppst.setInt(5,mapCellId.intValue());

        		rsVal = ppst.executeQuery();
        		if(rsVal.next()){
        			if(rsVal.getString("report_value") == null) continue;
        			sqlList.add("insert into REPORT_IN_INFO(REP_IN_ID,CELL_ID,REPORT_VALUE) values("+ repInId +","+ cellId +",'"+rsVal.getString("report_value")+"')");
        		}
    		}
    		
    		/** 原有汇总方式，插入数据用的是select再insert方式，现多数据测试的情况下，效率没有上面的方式高，暂时保留
    		String sumCell="select c.cell_id as tar_cell_Id,b.cell_id as map_cell_id,MAP_CHILD_REP_ID,MAP_VERSION_ID from  COLLECT_DETAIL a " +
						   "left join m_cell b on a.MAP_CHILD_REP_ID=b.CHILD_REP_ID and a.MAP_VERSION_ID=b.VERSION_ID and a.MAP_CELL_NAME=b.CELL_NAME " +
						   "left join m_cell c on a.CHILD_REP_ID=c.CHILD_REP_ID and a.VERSION_ID=c.VERSION_ID and a.CELL_NAME=c.CELL_NAME " +
						   "where CD_TYPE=2 and a.CHILD_REP_ID='"+childRepId+"' and a.VERSION_ID='"+versionId+"'";
    		System.out.println("得到需要汇总的单元格的sql="+sumCell);
    		//源数
    		String mapRep="select a.CELL_ID,a.REPORT_VALUE,b.CHILD_REP_ID,b.VERSION_ID from report_in_info a left join report_in b on a.REP_IN_ID=b.REP_IN_ID " +
    				"where b.CHECK_FLAG=1 " +
    				"and b.ORG_ID in("+subOrgIds+") " +
    				"and b.TERM="+term+" and  b.YEAR="+year+" and a.REPORT_VALUE is not null  ";
    		
    		String selectSql="";
    		if(Config.DB_SERVER_TYPE.equals("db2")){
    			selectSql="select a.tar_cell_Id cell_id,replace(ltrim(replace(rtrim(char(decimal(sum(double(b.report_Value)),16,2))),'0',' ')),' ','0') val" +
				" from ("+sumCell+") a left join ("+mapRep+") b " +
						"on a.map_cell_id=b.CELL_ID and a.MAP_CHILD_REP_ID=b.CHILD_REP_ID and a.MAP_VERSION_ID=b.VERSION_ID group by a.tar_cell_Id  ";
		
    		}
    		if(Config.DB_SERVER_TYPE.equals("oracle")){
    			selectSql="select a.tar_cell_Id cell_id,trim(to_char(round(sum(to_number(b.report_Value)),2),'999999999999990.99')) val" +
				" from ("+sumCell+") a left join ("+mapRep+") b " +
						"on a.map_cell_id=b.CELL_ID and a.MAP_CHILD_REP_ID=b.CHILD_REP_ID and a.MAP_VERSION_ID=b.VERSION_ID group by a.tar_cell_Id  ";
    		}
    		
    		String exeSql="";
    		if(Config.DB_SERVER_TYPE.equals("db2")){
    			exeSql="insert into REPORT_IN_INFO (REP_IN_ID,CELL_ID,REPORT_VALUE) select  "+repInId+","+"cell_id,val from("+selectSql+") as temp";
    		}
    		if(Config.DB_SERVER_TYPE.equals("oracle")){
    			exeSql="insert into REPORT_IN_INFO (REP_IN_ID,CELL_ID,REPORT_VALUE) select  "+repInId+","+"cell_id,val from("+selectSql+")";
    		}

    		sqlList.add(exeSql);
    		*/
    	}catch(Exception e){
    		sqlList = null;
    		e.printStackTrace();
    	}finally{
    		try {
    			if(rsVal != null) rsVal.close();    			
				if(rs != null) rs.close();
				if(ppst != null) ppst.close();
				if(ps != null) ps.close();
			} catch (SQLException e) {
				sqlList = null;
				e.printStackTrace();
			}
    	}
    	return sqlList;
    }
    /**
     *<p>描述:普通报表</p>
     *<p>参数:</p>
     *<p>日期：2007-10-29</p>
     *<p>作者：曹发根</p>
     */
    private List doOrderCollect(Connection conn,Integer repInId,String childRepId,String versionId,String subOrgIds,Integer year,Integer term,Integer curId,String orderColId,int totalRow,int startRow,String realRepId,String realVersionId ){
    	List sqlList = new ArrayList();
    	PreparedStatement ppst = null;
    	PreparedStatement ps = null;
    	ResultSet rs = null;
    	ResultSet rsTemp = null;
    	try{
    		String orderSql="";
    		//排序的单元格
    		if(Config.DB_SERVER_TYPE.equals("sqlserver"))
    		{
    			orderSql = "select CELL_ID,REP_IN_ID,CELL_NAME,ROW_ID,COL_ID from (select a.cell_id,a.REP_IN_ID,c.CELL_NAME, c.ROW_ID,c.COL_ID " +
				   "row_number() over(order by (convert(numeric(38,3),a.REPORT_VALUE)  desc)) as rownum from report_in_info a left join report_in b on a.REP_IN_ID=b.REP_IN_ID left join m_cell c on a.CELL_ID=c.CELL_ID " +
				   "where b.CHECK_FLAG=1 and c.COL_ID=? and ROW_ID<? and b.CHILD_REP_ID=? and b.VERSION_ID=? and b.ORG_ID in("+subOrgIds+") " +
				   "and TERM=? and YEAR=? and a.REPORT_VALUE is not null) d where d.rownum<=?";
    		}
    		if(Config.DB_SERVER_TYPE.equals("db2")){
    			orderSql = "select a.cell_id,a.REP_IN_ID,c.CELL_NAME, c.ROW_ID,c.COL_ID from report_in_info a " +
    					   "left join report_in b on a.REP_IN_ID=b.REP_IN_ID left join m_cell c on a.CELL_ID=c.CELL_ID " +
    					   "where b.CHECK_FLAG=1 and c.COL_ID=? and ROW_ID<? and b.CHILD_REP_ID=? and b.VERSION_ID=? and b.ORG_ID in("+subOrgIds+") " +
    					   "and TERM=? and YEAR=? and a.REPORT_VALUE is not null order by double(a.REPORT_VALUE) desc fetch first ? rows only";;
    		}else if(Config.DB_SERVER_TYPE.equals("oracle")){
    			orderSql = "select CELL_ID,REP_IN_ID,CELL_NAME,ROW_ID,COL_ID from (select a.cell_id,a.REP_IN_ID,c.CELL_NAME, c.ROW_ID,c.COL_ID " +
    					   "from report_in_info a left join report_in b on a.REP_IN_ID=b.REP_IN_ID left join m_cell c on a.CELL_ID=c.CELL_ID " +
    					   "where b.CHECK_FLAG=1 and c.COL_ID=? and ROW_ID<? and b.CHILD_REP_ID=? and b.VERSION_ID=? and b.ORG_ID in("+subOrgIds+") " +
    					   "and TERM=? and YEAR=? and a.REPORT_VALUE is not null order by to_number(a.REPORT_VALUE) desc) where rownum<=?";
    		}
    		ppst = conn.prepareStatement(orderSql);
    		ppst.setString(1,orderColId);
    		ppst.setInt(2,(totalRow+startRow));
    		ppst.setString(3,realRepId);
    		ppst.setString(4,realVersionId);
    		ppst.setInt(5,term.intValue());
    		ppst.setInt(6,year.intValue());
    		ppst.setInt(7,totalRow);
    		rs = ppst.executeQuery();
    		
    		if(rs != null){
    			/**拼入库SQL语句*/
    			ps = conn.prepareStatement("select c.CELL_ID,a.REPORT_VALUE from REPORT_IN_INFO a left join M_CELL b on a.CELL_ID=b.CELL_ID " +
    								"left join (select CELL_ID,COL_ID,ROW_ID from m_cell where CHILD_REP_ID=? and VERSION_ID=?) c on c.COL_ID=b.COL_ID " +
    								"where a.REP_IN_ID=? and c.ROW_ID=? and b.ROW_ID=? and a.REPORT_VALUE is not null");
    			ps.setString(1,childRepId);
    			ps.setString(2,versionId);
    			for(int i=0;i<totalRow&&rs.next();i++){
        			ps.setInt(3,rs.getInt("REP_IN_ID"));
        			ps.setInt(4,(startRow+i));
        			ps.setInt(5,rs.getInt("ROW_ID"));
        			rsTemp = ps.executeQuery();
        			while(rsTemp.next()){
        				sqlList.add("insert into REPORT_IN_INFO (REP_IN_ID,CELL_ID,REPORT_VALUE)values("+repInId+","+rsTemp.getInt(1)+",'"+rsTemp.getString(2)+"')");
        			}
        		}
    		}
    	}catch(Exception e){
    		sqlList = null;
    		e.printStackTrace();
    	}finally{
    		try {
//    			if()
//				if(rs != null) rs.close();
//				if(rsTemp != null) rsTemp.close();
//				if(ps != null) ps.close();
				if(ppst != null) ppst.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				sqlList = null;
				e.printStackTrace();
			}
    	}
    	return sqlList;
    }
    
    /**
	 * 根据参数查找该报表的关联报表
	 * 
	 * @param childRepId
	 * @param versionId
	 * @param dataRangeId
	 * @param curId
	 * @return CollectReal
	 */
	public static CollectReal getRealReportInfo(String childRepId, String versionId, Integer dataRangeId, Integer curId){
		CollectReal collectReal = null;
		DBConn conn = null;
		Session session = null;

		try{
			conn = new DBConn();
			session = conn.openSession();

			String hql = "from CollectReal cr where cr.childRepId=:childRepId and cr.versionId=:versionId  and cr.dataRangeId=:dataRangeId and " + "cr.curId=:curId ";

			Query query = session.createQuery(hql);
			query.setString("childRepId", childRepId);
			query.setString("versionId", versionId);
			query.setInteger("dataRangeId", dataRangeId.intValue());
			query.setInteger("curId", curId.intValue());

			List list = query.list();

			if (list != null && list.size() > 0){
				collectReal = (CollectReal) list.get(0);
			}
		}catch (HibernateException he){
			log.printStackTrace(he);
		}catch (Exception e){
			collectReal = null;
			log.printStackTrace(e);
		}finally{
			// 如果连接存在，则断开，结束会话，返回
			if (conn != null)
				conn.closeSession();
		}
		return collectReal;
	}
	
	/**
	 * 查找报表状态
	 * @param collectRea
	 * @param year
	 * @param term
	 * @param childOrgIds
	 * @return
	 */
	public static Integer getReportState(String childRepId,String versionId,Integer dataRangeId,int year,int term,Integer curId,String orgid){
		Integer state = null;
		DBConn conn = null;
		Session session = null;
		
		if(childRepId == null || childRepId.equals("")
				|| versionId == null || versionId.equals("")
				|| orgid == null || orgid.equals(""))
			return state;
		String hql="select count(*) from ReportIn ri where  "
			 	+" ri.MChildReport.comp_id.childRepId='"+childRepId+"'"
				+" and ri.MChildReport.comp_id.versionId='"+versionId+"'"
				+" and ri.MDataRgType.dataRangeId="+dataRangeId
				+" and ri.year="+year
				+" and ri.term="+term
				+" and ri.MCurr.curId="+curId
				+" and ri.orgId='"+orgid+"'"
				+" and ri.times>0"
				+" and ri.checkFlag=" ;
//		if(com.cbrc.smis.common.Config.IS_NEED_CHECK==1){
//			hql += com.fitech.net.config.Config.CHECK_FLAG_PASS;
//	  	}else{
	  		hql += com.fitech.net.config.Config.CHECK_FLAG_UNCHECK;
//	  	}
	
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
	 * 查找报表id
	 * @param collectRea
	 * @param year
	 * @param term
	 * @param childOrgIds
	 * @return
	 */
	public static Integer getReportId(String childRepId,String versionId,Integer dataRangeId,int year,int term,Integer curId,String orgid){
		Integer state = null;
		DBConn conn = null;
		Session session = null;
		
		if(childRepId == null || childRepId.equals("")
				|| versionId == null || versionId.equals("")
				|| orgid == null || orgid.equals(""))
			return state;
		String hql="select ri.repInId from ReportIn ri where  "
			 	+" ri.MChildReport.comp_id.childRepId='"+childRepId+"'"
				+" and ri.MChildReport.comp_id.versionId='"+versionId+"'"
				+" and ri.MDataRgType.dataRangeId="+dataRangeId
				+" and ri.year="+year
				+" and ri.term="+term
				+" and ri.MCurr.curId="+curId
				+" and ri.orgId='"+orgid+"'"
				+" and ri.times>0"
				+" and ri.checkFlag in (" + com.fitech.net.config.Config.CHECK_FLAG_PASS+","+com.fitech.net.config.Config.CHECK_FLAG_UNCHECK+")";
			/*	if(com.cbrc.smis.common.Config.IS_NEED_CHECK==1){
					hql += " and ri.checkFlag = " + com.fitech.net.config.Config.CHECK_FLAG_PASS;
			  	}else{
			  		hql += " and ri.checkFlag = "+ com.fitech.net.config.Config.CHECK_FLAG_UNCHECK;
			  	}*/
				//+" and ri.checkFlag =" + com.fitech.net.config.Config.CHECK_FLAG_PASS;
	
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
	 *<p>描述:根据管理报表找主报表</p>
	 *<p>参数:</p>
	 *<p>日期：2008-1-8</p>
	 *<p>作者：曹发根</p>
	 */
	public CollectReal getRealByRealReport(String realRepId,String realVerId){
		CollectReal collectReal = null;
		DBConn conn = null;
		Session session = null;

		try{
			conn = new DBConn();
			session = conn.openSession();

			String hql = "from CollectReal cr where cr.childRepId not like '%F%' and cr.realRepId=:realRepId and cr.realVerId=:realVerId ";

			Query query = session.createQuery(hql);
			query.setString("realRepId", realRepId);
			query.setString("realVerId", realVerId);

			List list = query.list();
			if (list != null && list.size() > 0){
				collectReal = (CollectReal) list.get(0);
			}
		}catch (HibernateException he){
			log.printStackTrace(he);
		}catch (Exception e){
			collectReal = null;
			log.printStackTrace(e);
		}finally{
			// 如果连接存在，则断开，结束会话，返回
			if (conn != null)
				conn.closeSession();
		}
		return collectReal;
	}
	
	/**
	 * 
	 * @param strSource
	 * @param strFrom
	 * @param strTo
	 * @return
	 */
	public static String replace(String strSource, String strFrom, String strTo) {
		// 如果要替换的子串为空，则直接返回源串
		if (strFrom == null || strFrom.equals(""))
			return strSource;
		String strDest = "";
		// 要替换的子串长度
		int intFromLen = strFrom.length();
		int intPos;
		// 循环替换字符串
		while ((intPos = strSource.indexOf(strFrom)) != -1) {
			// 获取匹配字符串的左边子串
			strDest = strDest + strSource.substring(0, intPos);
			// 加上替换后的子串
			strDest = strDest + strTo;
			// 修改源串为匹配子串后的子串
			strSource = strSource.substring(intPos + intFromLen);
		}
		// 加上没有匹配的子串
		strDest = strDest + strSource;
		// 返回
		return strDest;
	}
	
	/**
	 * 单列市与直属省行汇总数据查看
	 * 
	 * @param reportInForm 报表form
	 * @return List 汇总数据
	 */
	public static List doSingleCollect(ReportInForm reportInForm){
		if(reportInForm == null 
				|| reportInForm.getYear() == null || reportInForm.getTerm() == null
				|| reportInForm.getOrgId() == null || "".equals(reportInForm.getOrgId())
				|| reportInForm.getCurId() == null || reportInForm.getDataRangeId() == null 
				|| reportInForm.getVersionId() == null || reportInForm.getChildRepId() == null) 
			return null;
		
		List cellList = null;
    	DBConn conn = null;
    	Session session = null;
    	Connection _conn = null;
    	PreparedStatement ps = null;
    	PreparedStatement ppst = null;
    	Statement stmt = null;
    	ResultSet rs = null;
    	ResultSet strs = null;
    	try{
    		conn = new DBConn();
            session = conn.beginTransaction();
            _conn = session.connection();
            stmt = _conn.createStatement();
            
            /**查看报表汇总方式（排序/累加）*/
            ps = _conn.prepareStatement("select IS_ORDER,REAL_REP_ID,REAL_VERSION_ID,ORDER_COL_ID,TOTAL_ROW,START_ROW FROM COLLECT_REAL " +
            		"where CHILD_REP_ID=? and VERSION_ID=? and DATA_RANGE_ID=? and CUR_ID=?");
            ps.setString(1, reportInForm.getChildRepId());
            ps.setString(2, reportInForm.getVersionId());
            ps.setInt(3, reportInForm.getDataRangeId().intValue());
            ps.setInt(4, reportInForm.getCurId().intValue());
            rs = ps.executeQuery();
            if(rs != null && rs.next()){
            	cellList = new ArrayList();
            	ReportInInfoForm reportInInfoForm = null;
            	if(rs.getInt(1)!=1){ //非排序汇总方式
            		/**有汇总公式的汇总单元格*/
            		ps = _conn.prepareStatement("select b.CELL_ID,b.COL_ID,b.ROW_ID,a.CELL_NAME,a.CD_SQL from COLLECT_DETAIL a left " +
            				"join M_CELL b on a.CELL_NAME=b.CELL_NAME and a.CHILD_REP_ID=b.CHILD_REP_ID and a.VERSION_ID=b.VERSION_ID " +
        					"where a.CHILD_REP_ID=? and a.VERSION_ID=? and a.CD_TYPE=1");
            		ps.setString(1,reportInForm.getChildRepId());
            		ps.setString(2,reportInForm.getVersionId());
            		rs = ps.executeQuery();
            		while(rs.next()){
            			String sql=rs.getString("CD_SQL");
            			String reportValue = null;
            			if(sql!=null){
            				sql=replace(sql,"$YEAR", reportInForm.getYear().toString());
            				sql=replace(sql,"$TERM", reportInForm.getTerm().toString());
            				sql=replace(sql,"$SUBORG", reportInForm.getOrgId());
            				sql=replace(sql,"$CUR_ID",reportInForm.getCurId().toString());
            				strs = stmt.executeQuery(sql);
            				if(strs != null && strs.next()){
            					reportValue = strs.getString("VAL");
            				}
            				
            				reportInInfoForm = new ReportInInfoForm();
                			reportInInfoForm.setCellId(new Integer(rs.getInt("CELL_ID")));
                			reportInInfoForm.setColId(rs.getString("COL_ID"));
                			reportInInfoForm.setRowId(new Integer(rs.getInt("ROW_ID")));
                			reportInInfoForm.setCellName(rs.getString("CELL_NAME"));
                			reportInInfoForm.setReportValue(reportValue);
            				
            				cellList.add(reportInInfoForm);
            			}
            		}
            		
            		//累加汇总单元格
            		String sumCell="select b.CELL_ID,b.COL_ID,b.ROW_ID,a.CELL_NAME,a.CD_SQL from COLLECT_DETAIL a left " +
    							   "join M_CELL b on a.CELL_NAME=b.CELL_NAME and a.CHILD_REP_ID=b.CHILD_REP_ID and a.VERSION_ID=b.VERSION_ID " +
    							   "where a.CHILD_REP_ID=? and a.VERSION_ID=? and a.CD_TYPE=2";
            		
            		ps = _conn.prepareStatement(sumCell);
            		ps.setString(1,reportInForm.getChildRepId());
            		ps.setString(2,reportInForm.getVersionId());
            		rs = ps.executeQuery();
            		String sql="";
            		if(Config.DB_SERVER_TYPE.equals("oracle"))
            			sql= "select sum(to_number(a.REPORT_VALUE)) as VAL from report_in_info a left join report_in b " +
        			  	  	 "on a.REP_IN_ID=b.REP_IN_ID where b.CHECK_FLAG=1 and b.ORG_ID IN ("+ reportInForm.getOrgId() +") and b.TERM=? " +
        			  	  	 "and b.YEAR=? and b.CHILD_REP_ID=? and b.VERSION_ID=? and a.CELL_ID=? and a.REPORT_VALUE is not null";
            		if(Config.DB_SERVER_TYPE.equals("db2"))
            			sql= "select sum(decimal(a.REPORT_VALUE)) as VAL from report_in_info a left join report_in b " +
        			  	  	 "on a.REP_IN_ID=b.REP_IN_ID where b.CHECK_FLAG=1 and b.ORG_ID IN ("+ reportInForm.getOrgId() +") and b.TERM=? " +
        			  	  	 "and b.YEAR=? and b.CHILD_REP_ID=? and b.VERSION_ID=? and a.CELL_ID=? and a.REPORT_VALUE is not null";
            		if(Config.DB_SERVER_TYPE.equals("sqlserver"))
            			sql= "select sum(convert(numeric(38,3),a.REPORT_VALUE)) as VAL from report_in_info a left join report_in b " +
   			  	  	 		 "on a.REP_IN_ID=b.REP_IN_ID where b.CHECK_FLAG=1 and b.ORG_ID IN ("+ reportInForm.getOrgId() +") and b.TERM=? " +
   			  	  	 		 "and b.YEAR=? and b.CHILD_REP_ID=? and b.VERSION_ID=? and a.CELL_ID=? and a.REPORT_VALUE is not null";
            		ppst = _conn.prepareStatement(sql);
            		while(rs.next()){
            			String reportValue = null;
            			ppst.setInt(1,reportInForm.getTerm().intValue());
            			ppst.setInt(2,reportInForm.getYear().intValue());
            			ppst.setString(3,reportInForm.getChildRepId());
            			ppst.setString(4,reportInForm.getVersionId());
            			ppst.setInt(5,rs.getInt("CELL_ID"));
                		strs = ppst.executeQuery();
                		if(strs != null && strs.next()){
        					reportValue = strs.getString("VAL");
        				}
                		
                		reportInInfoForm = new ReportInInfoForm();
            			reportInInfoForm.setCellId(new Integer(rs.getInt("CELL_ID")));
            			reportInInfoForm.setColId(rs.getString("COL_ID"));
            			reportInInfoForm.setRowId(new Integer(rs.getInt("ROW_ID")));
            			reportInInfoForm.setCellName(rs.getString("CELL_NAME"));
            			reportInInfoForm.setReportValue(reportValue);
        				
        				cellList.add(reportInInfoForm);                		
            		}
            	}else{ //排序汇总方式
//            		sqlList = doOrderCollect(connection, repInId, childRepId, versionId, subOrgIds, year, term, curId,rs.getString("ORDER_COL_ID"),
//            				rs.getInt("TOTAL_ROW"),rs.getInt("START_ROW"),rs.getString("REAL_REP_ID"),rs.getString("REAL_VERSION_ID"));
            	}
            }
    	}catch(Exception e){
    		cellList = null;	
    		e.printStackTrace();
    	}finally{
    		try {
				if(stmt != null) stmt.close();
				if(ps != null) ps.close();
				if(_conn != null) _conn.close();
				if(session != null) session.close();
				if(conn != null) conn.closeSession();
			} catch (SQLException e1) {
				e1.printStackTrace();
			} catch (HibernateException e) {
				e.printStackTrace();
			}
    	}
		
		
		return cellList;
	}
}