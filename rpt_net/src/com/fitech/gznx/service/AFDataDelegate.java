package com.fitech.gznx.service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import oracle.jdbc.driver.OracleTypes;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.proc.util.FitechUtil;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.gznx.entity.CollResultBean;
import com.fitech.gznx.po.AfOrg;
import com.fitech.gznx.po.AfReport;
import com.fitech.gznx.po.AfTemplate;
import com.fitech.gznx.po.AfTemplateCollRule;
import com.fitech.gznx.po.AfTemplateCollRuleId;
import com.fitech.gznx.procedure.ProcedureHandle;
import com.fitech.gznx.procedure.QDValidate;
import com.fitech.gznx.procedure.ValidateNxQDReport;

/***
 * ��jdbc���� ��Ҫ�޸� ���Ը� 2011-12-21
 * @author Administrator
 *
 */
public class AFDataDelegate {

	private static FitechException log = new FitechException(AFDataDelegate.class);
	
    /**
     * jdbc���� ��Ҫ�޸� ���Ը� 2011-12-21
     *  oracle�﷨ ��Ҫ�޸� ���Ը� 2011-12-27
     *<p>����:���ܷ������,�ɹ����ؼ�¼ID ʧ�ܷ��� null</p>
     *<p>����:crId ����ID orgId ����Id  subOrgIds �¼�����ID</p>
     *<p>���ڣ�2007-10-29</p>
     *<p>���ߣ��ܷ���</p>
     * @param childRepId
     * @param versionId
     * @param orgId
     * @param subOrgIds
     * @param year
     * @param term
     * @param day
     * @param repFreqId
     * @param curId
     * @param templateType
     * @return
     */
	public Integer doCollect(String childRepId,String versionId,String orgId,
    		String subOrgIds,Integer year,Integer term,Integer day,Integer repFreqId,Integer curId,Integer templateType,String tartOrgId){
    	
    	Integer repInId=null;
    	DBConn conn=null;
    	Session session=null;
    	Connection connection=null;
    	PreparedStatement ps=null;
    	Statement stmt = null;
    	boolean result=false;
    	List sqlList = null;
    	
    	String dataTabName = null;
		if (templateType.toString().equals(com.fitech.gznx.common.Config.PBOC_REPORT)) {
			dataTabName ="AF_PBOCREPORTDATA";	
		} else {
			dataTabName ="AF_OTHERREPORTDATA";
		}
    	
    	try{
    		conn = new DBConn();
            session = conn.beginTransaction();
            connection=session.connection();
            connection.setAutoCommit(false);
            stmt=connection.createStatement();
            /**jdbc���� ��Ҫ�޸� ���Ը� 2011-12-21**/
            ps=connection.prepareStatement("SELECT REP_ID,CHECK_FLAG,tbl_inner_validate_flag,tbl_outer_validate_flag FROM AF_REPORT where TEMPLATE_ID=? and VERSION_ID=? and REP_FREQ_ID=? " +
            				"and TIMES=? and ORG_ID=? and YEAR=? and TERM=? and DAY=? and CUR_ID=? ");
            ps.setString(1, childRepId);
            ps.setString(2, versionId);
            ps.setInt(3, repFreqId.intValue());
            ps.setInt(4, 1);
            ps.setString(5, orgId);
            ps.setInt(6,year.intValue());
            ps.setInt(7, term.intValue());
            ps.setInt(8, day.intValue());
            ps.setInt(9, curId.intValue());
            ResultSet rs=ps.executeQuery();
            
            if(rs!= null && rs.next()){
            	
            	repInId = new Integer(rs.getInt("REP_ID"));
            	int checkFlag = rs.getInt("CHECK_FLAG");
            	int inner_check = rs.getInt("tbl_inner_validate_flag");
            	int outer_check = rs.getInt("tbl_outer_validate_flag");
            	boolean isColl = true; //Ĭ����Ҫ�ݹ����
            	/**
            	 * ���������������Ҫ���²������
            	 * 1.��У��δ�ύ"
            	 * 2.���ύδ���
            	 * 3.�Ѿ����ͨ��
            	 */
            	if(!tartOrgId.equals(orgId)){//�޸�ΪֻҪ���ܾ����¶���ȫ�����ǻ��ܣ������м�Ļ��������ں���״̬
	            	if(checkFlag==0 || checkFlag==1 )
	            		isColl = false;
	            	else
	            		if(checkFlag==2)
	            			if((inner_check==1 && com.cbrc.smis.common.Config.UP_VALIDATE_BJ.intValue()==0) 
	            					|| (inner_check==1  && com.cbrc.smis.common.Config.UP_VALIDATE_BJ.intValue()==1 && outer_check==1) )
	            				isColl = false;		
	            	if(!isColl)
	            		return repInId;
            	}
            	stmt.addBatch("update AF_REPORT set CHECK_FLAG=4,tbl_inner_validate_flag=1,tbl_outer_validate_flag=1 where REP_ID="+repInId);
            	stmt.addBatch("delete from " +dataTabName+" where REP_ID="+repInId);
            	
            }else{  //��һ�λ�������....
            	
            	/***
            	 * oracle�﷨ ��Ҫ�޸� ���Ը� 2011-12-27
            	 */
            	String seqOracle="select SEQ_AF_REPORT.nextval from dual";
             	String seqDB2="values nextval form SEQ_AF_REPORT";
            	String seqSqlserver="select max(REP_ID)+1 from AF_REPORT";
            	
            	if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2")){
        			rs=stmt.executeQuery(seqOracle);
        		}
        		if(Config.DB_SERVER_TYPE.equals("db2")){
        			rs=stmt.executeQuery(seqDB2);
        		}  
        		if(Config.DB_SERVER_TYPE.equals("sqlserver"))
        			rs=stmt.executeQuery(seqSqlserver);
        		if(rs.next()){
        			//����µı���id������
        			repInId=new Integer(rs.getInt(1));
        		}
        		
        		String reptName=""; //��������
        		/**jdbc���� ��Ҫ�޸� ���Ը� 2011-12-21**/
        		ps = connection.prepareStatement("select a.TEMPLATE_NAME from AF_TEMPLATE a" +
        				" where a.TEMPLATE_ID=? and a.VERSION_ID=?");
        		ps.setString(1,childRepId);
        		ps.setString(2,versionId);
        		rs = ps.executeQuery();
        		
        		if(rs.next()){
            			reptName=rs.getString(1);
        		}
        		/**jdbc���� ��Ҫ�޸� ���Ը� 2011-12-21**/
        		stmt.addBatch("insert into AF_REPORT(TEMPLATE_ID, VERSION_ID, REP_FREQ_ID, TIMES,ORG_ID," +
        				"YEAR,TERM,DAY,CUR_ID,REP_ID,CHECK_FLAG,REP_NAME) values('"
        				+ childRepId + "','" + versionId + "'," + repFreqId + ",1,'" + orgId + "'," 
        				+ year + "," + term + "," + day + "," + curId + "," + repInId + ",4,'" + reptName +"')");
            }
           
            //�鿴ģ��Ļ��ܷ�ʽ
//            ps=connection.prepareStatement("select IS_ORDER,REAL_REP_ID,REAL_VERSION_ID,ORDER_COL_ID,TOTAL_ROW,START_ROW FROM COLLECT_REAL " +
//            		"where CHILD_REP_ID=? and VERSION_ID=? and DATA_RANGE_ID=? and CUR_ID=?");
//            ps.setString(1, childRepId);
//            ps.setString(2, versionId);
//            ps.setInt(3, repFreqId.intValue());
//            ps.setInt(4, curId.intValue());
//            rs=ps.executeQuery();
//            if(rs.next()){
            	
            	sqlList = doCommonCollect(connection, repInId, childRepId, versionId, 
            			 subOrgIds, year, term, day, repFreqId, curId,templateType);
            	
            	if(sqlList == null) return repInId = null;
            	if(sqlList.size() == 0) return repInId;//���ｫ����ֵ�޸�Ϊֱ�ӷ���repInId,Դ���뷵��-1
            	
            	for(int i=0;i<sqlList.size();i++){
        			stmt.addBatch((String)sqlList.get(i));
        		}
//            }
            stmt.executeBatch();
            result = true;
    	}catch(Exception e){
    		result = false;
    		repInId = null;    		
    		e.printStackTrace();
    	}finally{
    		try {
				if(result == true) 
					connection.commit();
				else 
					connection.rollback();
				
				connection.setAutoCommit(true);
				
				
				if(connection != null) 
					connection.close();
				
			} catch (SQLException e1) {
				e1.printStackTrace();
			} 
    	}
    	return repInId;
    }
    
    /**
     * jdbc���� ��Ҫ�޸� ���Ը� 2011-12-21
     * ��2011-12-27���޸ģ����sqlserver���ݿ��ѯsql
     * ���������ݿ��ж�
     *<p>����:��ͨ����</p>
     *<p>����:</p>
     *<p>���ڣ�2007-10-29</p>
     *<p>���ߣ��ܷ���</p>
     */
    private List doCommonCollect(Connection connection,Integer repInId,String childRepId,String versionId,
    		String subOrgIds,Integer year,Integer term,Integer day, Integer repFreqId, Integer curId,Integer templateType){
    	
    	List sqlList = new ArrayList();
    	
    	String dataTabName = null;
		if (templateType.toString().equals(com.fitech.gznx.common.Config.PBOC_REPORT)) {
			dataTabName ="AF_PBOCREPORTDATA";	
		} else {
			dataTabName ="AF_OTHERREPORTDATA";
		}
    	
    	
    	ResultSet rs = null;
    	ResultSet rsVal = null;
    	PreparedStatement ps = null;
    	PreparedStatement ppst = null;
    	
    	try{
    		//��������
    		if(Config.IS_IGNORE_FLAG==1){
    			//��Ҫ���ܵĵ�Ԫ��
        		String sumCell="select CELL_ID from AF_CELLINFO a " +
        				"where a.TEMPLATE_ID=? and a.VERSION_ID=?";
        		/**jdbc���� ��Ҫ�޸� ���Ը� 2011-12-21**/
        		ps=connection.prepareStatement(sumCell);
        		ps.setString(1,childRepId);
        		ps.setString(2,versionId);
        		rs=ps.executeQuery();

        		Long cellId = null;
        		/**jdbc���� ��Ҫ�޸� ���Ը� 2011-12-21
        		 * ��2011-12-27���޸ģ����sqlserver���ݿ��ѯsql
        		 * ���������ݿ��ж�**/
        		String sql="";
        		if(Config.DB_SERVER_TYPE.equals("oracle"))
        			sql= "select sum(to_number(a.CELL_DATA)) as report_value from " + dataTabName + " a left join (select af.*,org.is_collect from AF_REPORT af join AF_ORG org on af.org_id=org.org_id) b " +
    			  	  "on a.REP_ID=b.REP_ID where ((b.is_collect=0 and b.CHECK_FLAG in(" + (Config.IS_NEED_CHECK==1 ? "1" : "0,1") + ")) or (b.is_collect=1 and b.CHECK_FLAG in(2,4,1,0))) and b.ORG_ID IN (" +subOrgIds+ ") and b.YEAR=? and b.TERM=? and b.DAY=? " +
    			  	  "and b.TEMPLATE_ID=? and b.VERSION_ID=? and CUR_ID=? and REP_FREQ_ID=? and a.CELL_ID=? and a.CELL_DATA is not null";
        		if(Config.DB_SERVER_TYPE.equals("sqlserver"))
        			sql= "select sum(convert(numeric(28,2),a.CELL_DATA)) as report_value from " + dataTabName + " a left join AF_REPORT b " +
    			  	  "on a.REP_ID=b.REP_ID where ((b.is_collect=0 and b.CHECK_FLAG in(" + (Config.IS_NEED_CHECK==1 ? "1" : "0,1") + ")) or (b.is_collect=1 and b.CHECK_FLAG in(2,4,1,0))) and b.ORG_ID IN (" +subOrgIds+ ") and b.YEAR=? and b.TERM=? and b.DAY=? " +
    			  	  "and b.TEMPLATE_ID=? and b.VERSION_ID=? and CUR_ID=? and REP_FREQ_ID=? and a.CELL_ID=? and a.CELL_DATA is not null";
        		if(Config.DB_SERVER_TYPE.equals("db2"))
        			sql= "select sum(decimal(a.CELL_DATA)) as report_value from " + dataTabName + " a left join (select af.*,org.is_collect from AF_REPORT af join AF_ORG org on af.org_id=org.org_id) b " +
    			  	  "on a.REP_ID=b.REP_ID where ((b.is_collect=0 and b.CHECK_FLAG in(" + (Config.IS_NEED_CHECK==1 ? "1" : "0,1") + ")) or (b.is_collect=1 and b.CHECK_FLAG in(2,4,1,0))) and b.ORG_ID IN (" +subOrgIds+ ") and b.YEAR=? and b.TERM=? and b.DAY=? " +
    			  	  "and b.TEMPLATE_ID=? and b.VERSION_ID=? and CUR_ID=? and REP_FREQ_ID=? and a.CELL_ID=? and a.CELL_DATA is not null";
        		
        		ppst = connection.prepareStatement(sql);
        		
        		while(rs.next()){
        			
        			cellId = rs.getLong("CELL_ID");
        			
        			ppst.setInt(1,year.intValue());
        			ppst.setInt(2,term.intValue());
        			ppst.setInt(3,day.intValue());
        			
        			
        			
        			ppst.setString(4,childRepId);
        			ppst.setString(5,versionId);
        			ppst.setInt(6, curId.intValue());
        			ppst.setInt(7, repFreqId.intValue());
        			ppst.setLong(8, cellId.longValue());
        			
            		//System.out.println(sql+"--"+cellId.intValue());
        			
        			
        			
        			
            		rsVal = ppst.executeQuery();
            		if(rsVal.next()){
            			System.out.println(rsVal.getString("REPORT_VALUE"));
            			
            			if(rsVal.getString("report_value") == null) continue;
            			/**jdbc���� ��Ҫ�޸� ���Ը� 2011-12-21**/
            			sqlList.add("insert into " + dataTabName + "(REP_ID,CELL_ID,CELL_DATA) values("
            					+ repInId + "," + cellId + ",'" + rsVal.getString("REPORT_VALUE")+"')");
            		}
        		}
    		}else{
    			//��Ҫ���ܵĵ�Ԫ��
        		String sumCell="select CELL_ID from AF_CELLINFO a " +
        				"where a.TEMPLATE_ID=? and a.VERSION_ID=?";
        		/**jdbc���� ��Ҫ�޸� ���Ը� 2011-12-21**/
        		ps=connection.prepareStatement(sumCell);
        		ps.setString(1,childRepId);
        		ps.setString(2,versionId);
        		rs=ps.executeQuery();

        		Long cellId = null;
        		/**jdbc���� ��Ҫ�޸� ���Ը� 2011-12-21
        		 * ��2011-12-27���޸ģ����sqlserver���ݿ��ѯsql
        		 * ���������ݿ��ж�**/
        		String sql="";
        		if(Config.DB_SERVER_TYPE.equals("oracle"))
        			sql= "select sum(to_number(a.CELL_DATA)) as report_value from " + dataTabName + " a left join (select af.*,org.is_collect from AF_REPORT af join AF_ORG org on af.org_id=org.org_id) b " +
    			  	  "on a.REP_ID=b.REP_ID where ((b.is_collect=0 ) or (b.is_collect=1 and b.CHECK_FLAG in(2,4,1,0))) and b.ORG_ID IN (" +subOrgIds+ ") and b.YEAR=? and b.TERM=? and b.DAY=? " +
    			  	  "and b.TEMPLATE_ID=? and b.VERSION_ID=? and CUR_ID=? and REP_FREQ_ID=? and a.CELL_ID=? and a.CELL_DATA is not null";
        		if(Config.DB_SERVER_TYPE.equals("sqlserver"))
        			sql= "select sum(convert(numeric(28,2),a.CELL_DATA)) as report_value from " + dataTabName + " a left join AF_REPORT b " +
    			  	  "on a.REP_ID=b.REP_ID where ((b.is_collect=0) or (b.is_collect=1 and b.CHECK_FLAG in(2,4,1,0))) and b.ORG_ID IN (" +subOrgIds+ ") and b.YEAR=? and b.TERM=? and b.DAY=? " +
    			  	  "and b.TEMPLATE_ID=? and b.VERSION_ID=? and CUR_ID=? and REP_FREQ_ID=? and a.CELL_ID=? and a.CELL_DATA is not null";
        		if(Config.DB_SERVER_TYPE.equals("db2"))
        			sql= "select sum(decimal(a.CELL_DATA)) as report_value from " + dataTabName + " a left join (select af.*,org.is_collect from AF_REPORT af join AF_ORG org on af.org_id=org.org_id) b " +
    			  	  "on a.REP_ID=b.REP_ID where ((b.is_collect=0 ) or (b.is_collect=1 and b.CHECK_FLAG in(2,4,1,0))) and b.ORG_ID IN (" +subOrgIds+ ") and b.YEAR=? and b.TERM=? and b.DAY=? " +
    			  	  "and b.TEMPLATE_ID=? and b.VERSION_ID=? and CUR_ID=? and REP_FREQ_ID=? and a.CELL_ID=? and a.CELL_DATA is not null";
        		
        		ppst = connection.prepareStatement(sql);
        		
        		while(rs.next()){
        			
        			cellId = rs.getLong("CELL_ID");
        			
        			ppst.setInt(1,year.intValue());
        			ppst.setInt(2,term.intValue());
        			ppst.setInt(3,day.intValue());
        			
        			
        			
        			ppst.setString(4,childRepId);
        			ppst.setString(5,versionId);
        			ppst.setInt(6, curId.intValue());
        			ppst.setInt(7, repFreqId.intValue());
        			ppst.setLong(8, cellId.longValue());
        			
            		//System.out.println(sql+"--"+cellId.intValue());
        			
        			
        			
        			
            		rsVal = ppst.executeQuery();
            		if(rsVal.next()){
            			System.out.println(rsVal.getString("REPORT_VALUE"));
            			
            			if(rsVal.getString("report_value") == null) continue;
            			/**jdbc���� ��Ҫ�޸� ���Ը� 2011-12-21**/
            			sqlList.add("insert into " + dataTabName + "(REP_ID,CELL_ID,CELL_DATA) values("
            					+ repInId + "," + cellId + ",'" + rsVal.getString("REPORT_VALUE")+"')");
            		}
        		}
    		}
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
     * ����������ת�ϱ�����
     * @param reportedRepId �Ѿ��ϱ��ı���id
     * @param needToReportRepId ��ת�ϱ��ı���id
     * @param templateType ��������
     * @return
     */
    public String transCollectDataToReport(AfReport reportedRep, AfReport needToReportRep, Integer templateType) {
    	String result = "";
    	boolean translate = false;
    	
    	DBConn conn=null;
    	Session session=null;
    	Connection connection=null;
    	Statement stmt = null;
    	
		String dataTabName = "";
		
		if (templateType.toString().equals(com.fitech.gznx.common.Config.PBOC_REPORT)) {
			dataTabName ="AF_PBOCREPORTDATA";	
		} else {
			dataTabName ="AF_OTHERREPORTDATA";
		}
    	
    	try{            
    		conn = new DBConn();
    		session = conn.beginTransaction();
    		
            connection=session.connection();
            connection.setAutoCommit(false);
            stmt=connection.createStatement();
            ResultSet rs = null;
            
            Long needToReportRepId = needToReportRep.getRepId();
            
    		//������ݿ����Ѿ���������¼�������������¼
    		//reportedRepΪ�ϱ��ı����¼
    		if(reportedRep!=null){
    			
    			reportedRep.setCheckFlag(com.fitech.net.config.Config.CHECK_FLAG_AFTERSAVE.longValue());
 				session.update(reportedRep);
 				
 				
 				String deleteHql= "from ";
 					deleteHql += dataTabName.equals("AF_PBOCREPORTDATA") ? "AfPbocreportdata" : "AfOtherreportdata";
 					deleteHql += " rii where rii.id.repId=" + reportedRep.getRepId();
 				
 				session.delete(deleteHql);
 				session.flush();
 				
 			}else{
 				
 				//������ݿ���û��������¼����Ŀǰû�����뱨����ֱ�ӵ��"ת�ϱ�����"
 				reportedRep = needToReportRep; 	
 				String seqSql = "";
 				if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
 					seqSql = "select SEQ_AF_REPORT.nextval from dual";
 				if(Config.DB_SERVER_TYPE.equals("db2"))
 					seqSql = "values nextval form SEQ_AF_REPORT";
 				if(Config.DB_SERVER_TYPE.equals("sqlserver"))
 					seqSql = "select max(REP_ID)+1 from AF_REPORT";
 				rs = stmt.executeQuery(seqSql);
 				
 				Long repId = null;
        		if(rs.next()){
        			//����µı���id������
        			repId = Long.valueOf(rs.getInt(1));
        		}
 				
 				reportedRep.setRepId(repId);
 				reportedRep.setTimes(Long.valueOf("1"));
 				reportedRep.setCheckFlag(com.fitech.net.config.Config.CHECK_FLAG_AFTERSAVE.longValue());
                session.save(reportedRep);
                session.flush();
 			}
             
    		//�ж��ж�Ӧ������ֵ��Report_In_Info����
//    		Set reportInInfos = reportIn.getReportInInfos();
//    		if(reportInInfos != null){
//    			Iterator itr = reportInInfos.iterator();
//    			while(itr.hasNext()){
//    				ReportInInfo  value = (ReportInInfo)itr.next();
//    				value.getComp_id().setRepInId(report_In.getRepInId());
//    				//�����ϱ�����ֵ��Report_In_Info��
//    				session.save(value);
//    			}
//    			session.flush();
//    		}  
        	
            String sql = "select count(*) from " + dataTabName +" where rep_id=" + needToReportRepId;
            
            rs = stmt.executeQuery(sql.toUpperCase());
            if(rs!=null&& rs.next()){
            	if(rs.getInt(1)>0){
            		sql = "INSERT INTO "+ dataTabName 
            				+" (REP_ID,CELL_ID,CELL_DATA) SELECT " 
            				+ reportedRep.getRepId()+ ",CELL_ID,CELL_DATA FROM "
            				+ dataTabName + " WHERE REP_ID=" + needToReportRepId;
            	
            		stmt.execute(sql);
            	}
        		translate = true;
        		result = "���ܳɹ�����ת���������ϴ����鿴��";
        		
            }else{
            	result = "������ʵ�����ݣ��޷����ܣ�";
            }
    		
    	}catch(Exception he){
    		
    		result = "���ܳ����޷����ܣ�";
    		log.printStackTrace(he);
    		
    	}finally{
    		try{
				if(translate == true) 
					connection.commit();
				else 
					connection.rollback();
				
				connection.setAutoCommit(true);
				
				if(stmt != null) 
					stmt.close();
				if(connection != null) 
					connection.close();
				if(session != null) 
					session.close();
				if(conn != null) 
					conn.closeSession();
//	    		if (conn!=null) 
//	    			conn.endTransaction(translate);
    		}catch(SQLException e){
    			log.printStackTrace(e);
    		}catch(HibernateException ex){
    			log.printStackTrace(ex);
    		}
    	} 
    	
    	return result;
    }
    
    
	/**
	 * jdbc���� ��Ҫ�޸� ���Ը� 2011-12-21
	 * �����嵥ʽ�������
	 * 
	 * @param repInId Integer ʵ�����ݱ�ID
	 * @param validateTypeId У�����ID
	 * @return Integer ����Id
	 */
	public Integer collectQDReport(String templateId, String versionId,
			Integer year,Integer term,Integer day,Integer repFreqId,Integer curId,String orgId,
			String subOrgIds){
		
		Integer qdReportId =null;
		
		if(subOrgIds==null) return qdReportId;
				
		DBConn conn=null;
		Session session = null;
		Connection connection = null;
		CallableStatement callstmt = null;
		//ResultSet rs = null;
		
		String date = year + "-"
				+ (term.intValue()<10 ? "0"+term : term.toString()) 
				+ "-"
				+ (day.intValue()<10 ? "0"+day : day.toString());
		
		try{
			//���reportids
			String reportIds = this.getReportIdArrays(templateId, versionId, 
					year, term, day, repFreqId, curId, subOrgIds);
			
			conn=new DBConn();
			
			session = conn.openSession();
			connection = session.connection();

			String sql = "{call PROC_QD_REPORT_DEAL(?,?,?,?,?,?,?,?,?)}";

			callstmt = connection.prepareCall(sql);

			/*
			 *   ** �������ƣ�PROC_QD_REPORT_DEAL 

				  ** ��    ����	�����ơ�         	   ������ ��     	 ��˵���� 
				  **1           p_tempid           varchar2        ģ���
				  **2           p_versionid        varchar2        �汾��
				  **3           p_dateid           varchar2        ��������
				  **4           p_freqid           varchar2        Ƶ��
				  **5           p_curid            varchar2        ���ֺ�
				  **6           p_orgid            varchar2        ������
				  **7           p_repid            varchar2        ϵͳ�ڵı���ID�� ������ж��ID�ţ��ԡ�|���ָ���    
				  **8   �� �� ֵ��r_flag             varchar2        ״̬��־λ
				  **9           r_repid            varchar2        ������
			 */
			
			callstmt.setString(1, templateId);
			callstmt.setString(2, versionId);
			callstmt.setString(3, date);
			callstmt.setString(4, repFreqId.toString());
			callstmt.setString(5, curId.toString());
			callstmt.setString(6, orgId.toString());
			callstmt.setString(7, reportIds);
			
			callstmt.registerOutParameter(8, OracleTypes.VARCHAR);
			callstmt.registerOutParameter(9, OracleTypes.VARCHAR);

			String flag = null;
			String reportId = null;
			
			callstmt.executeQuery();

			flag = callstmt.getString(8);
			reportId = callstmt.getString(9);

			if(reportId!=null && !reportId.equals("")) 
				qdReportId = Integer.valueOf(reportId);
				
		}catch(HibernateException he){
			qdReportId=null;
			log.printStackTrace(he);
		}catch(Exception e){
			qdReportId=null;
			log.printStackTrace(e);
		}finally{
			if(conn!=null) conn.closeSession();
		}
		
		return qdReportId;
	}
    
	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * ������֪������õ������ϱ��ı���ids���м���|�ָ�
	 * 
	 * @param childReportId
	 *            String �Ա���id
	 * @param versionId
	 *            String �汾��
	 * @return ����ids
	 */
	public String getReportIdArrays(String templateId, String versionId,
			Integer year,Integer term,Integer day,Integer repFreqId,Integer curId,
			String subOrgIds) {
		
		String reportIdArrays = "";

		List retVals = null;
		DBConn conn = null;

		if (templateId != null && !templateId.equals("")
				&& versionId != null && !versionId.equals("")) {
			try {
				
				StringBuffer hql = new StringBuffer("select ar.repId from AfReport ar where 1=1");
				
				hql.append(" and ar.templateId = '" + templateId + "'");
				hql.append(" and ar.versionId ='" + versionId + "'");
				hql.append(" and ar.year =" + year);
				hql.append(" and ar.term =" + term);
				hql.append(" and ar.day =" + day);
				hql.append(" and ar.curId =" + curId);
				hql.append(" and ar.repFreqId =" + repFreqId);
				hql.append(" and ar.orgId in (" + subOrgIds + ")");
				hql.append(" and ar.times=1");
				conn = new DBConn();

				Session session = conn.openSession();
				Query query = session.createQuery(hql.toString());

				retVals = query.list();

				
				if (retVals != null && retVals.size() != 0) {
					for(int i = 0 ; i < retVals.size(); i++){
						
	                    reportIdArrays += !reportIdArrays.equals("") ?
	                            "|" + retVals.get(i).toString() : 
	                            	retVals.get(i).toString();
					}
					
				}
			} catch (HibernateException he) {
				log.printStackTrace(he);
			} finally {
				if (conn != null)
					conn.closeSession();
			}
		}

		return reportIdArrays;
	}
	
    /**
     * jdbc���� ��Ҫ�޸� ���Ը� 2011-12-21
     *<p>����:���ܷ������,�ɹ����ؼ�¼ID ʧ�ܷ��� null()ֻ֧�ֵ�һģ��Ļ���</p>
     * @param childRepId
     * @param versionId
     * @param orgId
     * @param subOrgIds
     * @param year
     * @param term
     * @param day
     * @param repFreqId
     * @param curId
     * @param templateType
     * @return
     */
	public Integer doCollectCBRC(String childRepId,String versionId,String orgId,
    		String subOrgIds,Integer year,Integer term,
    		Integer dataRangeId,Integer curId,Integer templateType) throws Exception{
    	
    	Integer repInId=null;
    	DBConn conn=null;
    	Session session=null;
    	Connection connection=null;
    	PreparedStatement ps=null;
    	Statement stmt = null;
    	boolean result=false;
    	List sqlList = null;
    	
    	//�������ݱ���
    	String dataTabName = "REPORT_IN_INFO";
		
    	try{
    		conn = new DBConn();
            session = conn.beginTransaction();
            connection=session.connection();
            connection.setAutoCommit(false);
            stmt=connection.createStatement();
           
            /**jdbc���� ��Ҫ�޸� ���Ը� 2011-12-21**/
            ps=connection.prepareStatement("SELECT REP_IN_ID,check_flag FROM REPORT_IN where CHILD_REP_ID=? and VERSION_ID=? and DATA_RANGE_ID=? " +
            				"and TIMES=? and ORG_ID=? and YEAR=? and TERM=? and CUR_ID=? ");
            ps.setString(1, childRepId);
            ps.setString(2, versionId);
            ps.setInt(3, dataRangeId.intValue());
            ps.setInt(4, 1);
            ps.setString(5, orgId);
            ps.setInt(6,year.intValue());
            ps.setInt(7, term.intValue());
            ps.setInt(8, curId.intValue());
            
            ResultSet rs=ps.executeQuery();
            
            if(rs!= null && rs.next()){
            	
            	repInId = new Integer(rs.getInt("REP_IN_ID"));
            	Integer check_flag=new Integer(rs.getInt("check_flag"));
            	if(check_flag.intValue()!=1){
            		stmt.addBatch("update REPORT_IN set CHECK_FLAG=4,TBL_INNER_VALIDATE_FLAG=1,TBL_OUTER_VALIDATE_FLAG=1 where REP_IN_ID="+repInId);
                	stmt.addBatch("delete from " +dataTabName+" where REP_IN_ID="+repInId);
            	}else{
            		throw new Exception("���������ͨ�����������ٻ��ܣ�");
            		//return null;
            	}
            }else{  //��һ�λ�������....
            	/**jdbc���� ��Ҫ�޸� ���Ը� 2011-12-21**/
            	String seqOracle="select SEQ_REPORT_IN.nextval from dual";
            	String seqDB2="values nextval for SEQ_REPORT_IN";
            	String seqSqlServer="select max(REP_IN_ID)+1 from REPORT_IN";
            	
            	if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2")){
        			rs=stmt.executeQuery(seqOracle);
        		}
        		if(Config.DB_SERVER_TYPE.equals("db2")){
        			rs=stmt.executeQuery(seqDB2);
        		} 
        		if(Config.DB_SERVER_TYPE.equals("sqlserver"))
        			rs=stmt.executeQuery(seqSqlServer);                                                                
        		if(rs.next()){
        			//����µı���id������
        			repInId=new Integer(rs.getInt(1));
        		}
        		
        		String reptName=""; //��������
        		/**jdbc���� ��Ҫ�޸� ���Ը� 2011-12-21**/
        		ps = connection.prepareStatement("select a.TEMPLATE_NAME from AF_TEMPLATE a" +
        				" where a.TEMPLATE_ID=? and a.VERSION_ID=?");
        		ps.setString(1,childRepId);
        		ps.setString(2,versionId);
        		rs = ps.executeQuery();
        		
        		if(rs.next()){
            			reptName=rs.getString(1);
        		}
        		/**jdbc���� ��Ҫ�޸� ���Ը� 2011-12-21**/
        		stmt.addBatch("insert into REPORT_IN(CHILD_REP_ID, VERSION_ID, DATA_RANGE_ID, TIMES,ORG_ID," +
        				"YEAR,TERM,CUR_ID,REP_IN_ID,CHECK_FLAG,REP_NAME) values('"
        				+ childRepId + "','" + versionId + "'," + dataRangeId + ",-1,'" + orgId + "'," 
        				+ year + "," + term + "," + curId + "," + repInId + ",4,'" + reptName +"')");
            }
           
            //�鿴ģ��Ļ��ܷ�ʽ
//            ps=connection.prepareStatement("select IS_ORDER,REAL_REP_ID,REAL_VERSION_ID,ORDER_COL_ID,TOTAL_ROW,START_ROW FROM COLLECT_REAL " +
//            		"where CHILD_REP_ID=? and VERSION_ID=? and DATA_RANGE_ID=? and CUR_ID=?");
//            ps.setString(1, childRepId);
//            ps.setString(2, versionId);
//            ps.setInt(3, repFreqId.intValue());
//            ps.setInt(4, curId.intValue());
//            rs=ps.executeQuery();
//            if(rs.next()){
            	
            	sqlList = doCommonCollectCBRC(connection, repInId, childRepId, versionId, 
            			orgId, subOrgIds, year, term, dataRangeId, curId,templateType);
            	
            	if(sqlList == null) return repInId = null;
            	if(sqlList.size() == 0) return new Integer(-1);
            	
            	for(int i=0;i<sqlList.size();i++){
        			stmt.addBatch((String)sqlList.get(i));
        		}
//            }
            stmt.executeBatch();
            result = true;
    	}catch(Exception e){
    		result = false;
    		repInId = null;    		
    		e.printStackTrace();
    		throw e;
    	}finally{
    		try {
				if(result == true) 
					connection.commit();
				else 
					connection.rollback();
				
				connection.setAutoCommit(true);
				
				if(stmt != null) 
					stmt.close();
				if(ps != null) 
					ps.close();
				if(connection != null) 
					connection.close();
				if(session != null) 
					session.close();
				if(conn != null) 
					conn.closeSession();
				
			} catch (SQLException e1) {
				e1.printStackTrace();
			} catch (HibernateException e) {
				e.printStackTrace();
			}
    	}
    	return repInId;
    }
    
	
    /**
     * jdbc���� ��Ҫ�޸� ���Ը� 2011-12-21
     *<p>����:��ͨ����</p>
     *<p>����:</p>
     */
    private List doCommonCollectCBRC(Connection connection,Integer repInId,String childRepId,String versionId,
    		String orgId,String subOrgIds,Integer year,Integer term, Integer dataRangeId, Integer curId,Integer templateType){
    	
    	List sqlList = new ArrayList();
    	
    	String dataTabName = "REPORT_IN_INFO";
    	
    	ResultSet rs = null;
    	ResultSet rsVal = null;
    	PreparedStatement ps = null;
    	PreparedStatement ppst = null;
    	
    	try{

    		//��Ҫ���ܵĵ�Ԫ��
    		/**jdbc���� ��Ҫ�޸� ���Ը� 2011-12-21**/
    		String sumCell="select CELL_ID from M_CELL a " +
    				"where a.CHILD_REP_ID=? and a.VERSION_ID=?";
    		
    		ps=connection.prepareStatement(sumCell);
    		ps.setString(1,childRepId);
    		ps.setString(2,versionId);
    		rs=ps.executeQuery();

    		Long cellId = null;
    		String sql="";
    		/**jdbc���� ��Ҫ�޸� ���Ը� 2011-12-21
    		 * ���������ݿ��ж�**/
    		if(Config.DB_SERVER_TYPE.equals("oracle"))
    			sql= "select sum(to_number(nvl(a.REPORT_VALUE,0))) as report_value from " + dataTabName + " a left join REPORT_IN b " +
			  	  "on a.REP_IN_ID=b.REP_IN_ID where b.CHECK_FLAG=1 and b.ORG_ID IN (" +subOrgIds+ ") and b.YEAR=? and b.TERM=? " +
			  	  "and b.CHILD_REP_ID=? and b.VERSION_ID=? and CUR_ID=? and DATA_RANGE_ID=? and a.CELL_ID=? and a.REPORT_VALUE is not null";
    		if(Config.DB_SERVER_TYPE.equals("db2"))
    			sql= "select sum(decimal(nvl(a.REPORT_VALUE,0))) as report_value from " + dataTabName + " a left join REPORT_IN b " +
			  	  "on a.REP_IN_ID=b.REP_IN_ID where b.CHECK_FLAG=1 and b.ORG_ID IN (" +subOrgIds+ ") and b.YEAR=? and b.TERM=? " +
			  	  "and b.CHILD_REP_ID=? and b.VERSION_ID=? and CUR_ID=? and DATA_RANGE_ID=? and a.CELL_ID=? and a.REPORT_VALUE is not null";
    		
    		if(Config.DB_SERVER_TYPE.equals("sqlserver"))
    			sql= "select sum(convert(int,isnull(a.REPORT_VALUE,0))) as report_value from " + dataTabName + " a left join REPORT_IN b " +
			  	  "on a.REP_IN_ID=b.REP_IN_ID where b.CHECK_FLAG=1 and b.ORG_ID IN (" +subOrgIds+ ") and b.YEAR=? and b.TERM=? " +
			  	  "and b.CHILD_REP_ID=? and b.VERSION_ID=? and CUR_ID=? and DATA_RANGE_ID=? and a.CELL_ID=? and a.REPORT_VALUE is not null";
    		
    		ppst = connection.prepareStatement(sql);
    		
    		while(rs.next()){

    			cellId = rs.getLong("CELL_ID");
    			
    			ppst.setInt(1,year.intValue());
    			ppst.setInt(2,term.intValue());
    			
    			ppst.setString(3,childRepId);
    			ppst.setString(4,versionId);
    			ppst.setInt(5, curId.intValue());
    			ppst.setInt(6, dataRangeId.intValue());
    			ppst.setLong(7, cellId.longValue());
    			
        		//System.out.println(sql+"--"+cellId.intValue());

        		rsVal = ppst.executeQuery();
        		if(rsVal.next()){
        			
        			if(rsVal.getString("report_value") == null) continue;
        			/**jdbc���� ��Ҫ�޸� ���Ը� 2011-12-21**/
        			sqlList.add("insert into " + dataTabName + "(REP_IN_ID,CELL_ID,REPORT_VALUE) values("
        					+ repInId + "," + cellId + ",'" + rsVal.getString("REPORT_VALUE")+"')");
        		}
    		}
    		
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
	 * jdbc���� ��Ҫ�޸� ���Ը� 2011-12-21
	 * ����ɼ�����������ģ��<br>
	 * @param templateId
	 * @param versionId
	 * @param year
	 * @param term
	 * @param day
	 * @param repFreqId
	 * @param curId
	 * @param orgId
	 * @param subOrgIds
	 * @return
	 */
	public Integer transformerReportToModoule(String templateId, String versionId,
			Integer year,Integer term,Integer day,Integer repFreqId,Integer curId,String orgId,
			String subOrgIds){
		
		Integer statusFlag =null;
		
		if(subOrgIds==null) return statusFlag;
		
		DBConn conn=null;
		Session session = null;
		Connection connection = null;
		CallableStatement callstmt = null;
		
		String date = year + "-"
				+ (term.intValue()<10 ? "0"+term : term.toString()) 
				+ "-"
				+ (day.intValue()<10 ? "0"+day : day.toString());
		
		try{
			//���reportids
			String reportIds = this.getReportIdArrays(templateId, versionId, 
					year, term, day, repFreqId, curId, subOrgIds);
			
			conn=new DBConn();
			
			session = conn.openSession();
			connection = session.connection();

			String sql = "{call PROC_"+ templateId +"(?,?,?,?,?,?,?,?,?)}";

			callstmt = connection.prepareCall(sql);

			/*
			 *   ** �������ƣ�PROC_QD_REPORT_DEAL 

				  ** ��    ����	�����ơ�         	   ������ ��     	 ��˵���� 
				  **1           p_tempid           varchar2        ģ���
				  **2           p_versionid        varchar2        �汾��
				  **3           p_dateid           varchar2        ��������
				  **4           p_freqid           varchar2        Ƶ��
				  **5           p_curid            varchar2        ���ֺ�
				  **6           p_orgid            varchar2        ������
				  **7           p_repid            varchar2        ϵͳ�ڵı���ID�� ������ж��ID�ţ��ԡ�|���ָ���    
				  **8   �� �� ֵ��	r_flag             varchar2        ״̬��־λ
				  **9           r_repid            varchar2        ������
			 */
			
			callstmt.setString(1, templateId);
			callstmt.setString(2, versionId);
			callstmt.setString(3, date);
			callstmt.setString(4, repFreqId.toString());
			callstmt.setString(5, curId.toString());
			callstmt.setString(6, orgId.toString());
			callstmt.setString(7, reportIds);
			
			callstmt.registerOutParameter(8, OracleTypes.VARCHAR);
			callstmt.registerOutParameter(9, OracleTypes.VARCHAR);

			String flag = null;
			String reportId = null;
			
			callstmt.executeQuery();

			flag = callstmt.getString(8);
			reportId = callstmt.getString(9);

			if(reportId!=null && !reportId.equals("")) 
				statusFlag = Integer.valueOf(reportId);
				
		}catch(HibernateException he){
			statusFlag=null;
			log.printStackTrace(he);
		}catch(Exception e){
			statusFlag=null;
			log.printStackTrace(e);
		}finally{
			if(conn!=null) conn.closeSession();
		}
		
		return statusFlag;
	}
	/**
	 * ��ȡ����ģ��������ܹ����
	 * @param templateId
	 * @param versionId
	 * @param orgId
	 * @return
	 */
	public static AfTemplateCollRule findAfTemplateCollRule(String templateId,String versionId,String orgId){
		AfTemplateCollRule result = null;
		DBConn dbconn = null;
		Session session = null;
		try {
			dbconn = new DBConn();
			session = dbconn.openSession();
			AfTemplateCollRuleId id  = new AfTemplateCollRuleId();
			id.setTemplateId(templateId);
			id.setVersionId(versionId);
			id.setOrgId(orgId);
			result = (AfTemplateCollRule) session.get(AfTemplateCollRule.class, id);
		} catch (Exception e) {
			result = null;
			log.printStackTrace(e);
		} finally {
			if (session != null)
				dbconn.closeSession();
		}
		return result;
	}
	/**
	 * �ж��Ƿ����������
	 * @param templateId
	 * @param versionId
	 * @param orgId
	 * @return
	 * @throws Exception
	 */
	public static AfTemplateCollRule findCollectGacha(String templateId,String versionId,String orgId)throws Exception{
		AfTemplateCollRule result = null;
		DBConn dbconn = null;
		Session session = null;
		try {
			dbconn = new DBConn();
			session = dbconn.openSession();
			String hsql = "from AfTemplateCollRule a where a.id.templateId='" + templateId + "' and a.id.versionId='" + versionId + "' and a.id.orgId='" + orgId + "'";
			List<AfTemplateCollRule> list = session.find(hsql);
			if(list.size()>0)
				result = list.get(0);
			dbconn.closeSession();
		}catch(Exception e){
			log.printStackTrace(e);
			if(dbconn!=null)
				dbconn.closeSession();
			throw e;
		}
		return result;
	}
	public static String strBeset(Object obj){
		String result = "";
		if(obj instanceof String)
			result = "'" + (String)obj + "'";
		if(obj instanceof List){
			List<String> list = (List)obj;
			StringBuffer sb = new StringBuffer("");
			for(int i=0;i<list.size();i++){
				sb.append(",'" + list.get(i).trim() + "'");
			}
			if(list.size()>0)
				result = sb.toString().substring(1);
		}
		if(obj instanceof String[]){
			String[] strs = (String[])obj;
			StringBuffer sb = new StringBuffer("");
			for(int i=0;i<strs.length;i++){
				sb.append(",'" + strs[i].trim() + "'");
			}
			if(strs.length>0)
				result = sb.toString().substring(1);
		}
		return result;
	}
	/**
	 * �������
	 * @param templateCollRule ���ܹ������
	 * @param strArr
	 * @param messages
	 * @param afDataDele
	 * @param templateType
	 * @param childOrgIds
	 * @param afOrgDele
	 * @param colllResult
	 * @param operator
	 * @param isIterator
	 * @return
	 * @throws Exception
	 */
	public static boolean collectDataByGacha(
			AfTemplateCollRule templateCollRule, String[] strArr,
			FitechMessages messages, AFDataDelegate afDataDele,
			Integer templateType, String childOrgIds, AFOrgDelegate afOrgDele,
			CollResultBean collResult, Operator operator, boolean isIterator) throws Exception {
		boolean result = true;
		DBConn  conn = new DBConn();
		Session session = null;
		Connection con = null;
		boolean commitFlag = true;
		Statement stat = null;
		StringBuffer sql = new StringBuffer("");
		AfReport afReport = null;
		String childRepIdAll = strArr[0].trim();
		String versionIdAll = strArr[1].trim();
		String orgIdAll = strArr[2].trim();
		Integer curIdAll = new Integer(strArr[3].trim());
		Integer repFreqIdAll = new Integer(strArr[4].trim());
		Integer donum = new Integer(strArr[5].trim()); // �������ܼ�¼�Ƿ��������ϱ�
		Integer year = new Integer(strArr[6].trim());
		Integer term1 = new Integer(strArr[7].trim());
		Integer day = new Integer(strArr[8].trim());
		AfOrg org = null;
		try{
			if(Config.IS_IGNORE_FLAG==1){
				conn = new DBConn();
				session = conn.beginTransaction();
				con = session.connection();
				stat = con.createStatement();
				String[] params =  FitechUtil.parseFomual(templateCollRule.getCollFormula());
				String parentOrgId = params[0];
				int collSchema = templateCollRule.getCollSchema();
				org = AFOrgDelegate.getOrgInfo(orgIdAll);
				AfTemplate template = (AfTemplate)session.find("from AfTemplate a where a.id.templateId=" + strBeset(childRepIdAll) + " and a.id.versionId=" + strBeset(versionIdAll)).get(0);//��ȡģ�����
				switch (collSchema){
				case Config.HZLX_GCHZ:{//�������
					String hsql = null;
					hsql=("from AfReport a where a.templateId=" + strBeset(childRepIdAll) + " and a.versionId=" + 
							strBeset(versionIdAll) + " and a.orgId=" + strBeset(orgIdAll) + " and a.repFreqId=" + repFreqIdAll + " and a.year=" + year + " and a.term=" + term1 + " and a.day=" + day);
					List<AfReport>  afReportList= session.find(hsql);//��ȡ���ͱ���Ϣ��
					if(afReportList.size()>0){
						afReport = afReportList.get(0);
						afReport.setCheckFlag(new Long(Config.CHECK_FLAG_AFTERSAVE));
						afReport.setTblInnerValidateFlag(null);
						afReport.setTblOuterValidateFlag(null);
						session.update(afReport);
						String del = "delete from af_pbocreportdata where rep_id=" + afReport.getRepId();
						stat.execute(del);//ɾ���Ѿ����ڵı���Ԫ������
					}
					else{
						afReport = new AfReport();
						afReport.setTemplateId(childRepIdAll);
						afReport.setVersionId(versionIdAll);
						afReport.setOrgId(orgIdAll);
						afReport.setRepFreqId(new Long(repFreqIdAll));
						afReport.setYear(new Long(year));
						afReport.setTerm(new Long(term1));
						afReport.setDay(new Long(day));
						afReport.setCheckFlag(new Long(4));
						afReport.setTimes(new Long(1));
						afReport.setCurId(new Long(curIdAll));
						afReport.setRepName(template.getTemplateName());
						afReport.setCheckFlag(new Long(Config.CHECK_FLAG_AFTERSAVE));//�����״̬��Ϊδ����
						afReport.setTblInnerValidateFlag(null);
						afReport.setTblOuterValidateFlag(null);
						session.save(afReport);
					}
					String orgIds = "";
					if(params[1].equals(Config.HZGS_TJH)){//����ۼ����ǳ������������ͬ����
						AfOrg parentOrg = (AfOrg)session.get(AfOrg.class, parentOrgId);
						if(parentOrg.getIsCollect().intValue()==1)//����ϼ����������������ݻ��ܹ�ϵ��ȡͬ����
							hsql = "select cr.id.orgId from AfCollectRelation cr where cr.id.orgId!=" + strBeset(orgIdAll) + " and cr.id.collectId=" + strBeset(parentOrgId);
						else// ����ֱ�Ӹ��ݻ������ȡͬ����
							hsql = "select af.orgId from AfOrg af where af.preOrgId='" + parentOrgId + "'";
						List list = session.find(hsql);
						orgIds = strBeset(list);
//							collectDataIter(strArr,messages, afDataDele,
//									 templateType,childOrgIds, afOrgDele,collResult,operator,isIterator,orgId);
					}else{//����ۼ�����ָ����ͬ����
						String[] strs =params[1].trim().split(",");
						orgIds = strBeset(strs);
					}
					String dataTble = templateType.toString().equals(com.fitech.gznx.common.Config.PBOC_REPORT) ? "af_pbocreportdata" : "af_otherreportdata";
					if(Config.DB_SERVER_TYPE.equals("oracle")){
						sql.append(" insert into " + dataTble + "\n");
						sql.append(" select " + afReport.getRepId() + ",a1.cell_id,to_char(to_number(a1.report_value)-case when a2.report_value is null then 0.00 else to_number(a2.report_value) end) from " + "\n");
						sql.append(" (" + "\n");
						sql.append(" select p.cell_id,sum(to_number(p.CELL_DATA)) as report_value from " + "\n");
						sql.append(" (select * from  af_report a where a.template_id=" + strBeset(childRepIdAll) +" and a.version_id=" + strBeset(versionIdAll) + " and a.org_id=" +  strBeset(parentOrgId) +" and a.rep_freq_id=" + repFreqIdAll + " and a.check_flag in(" + (Config.IS_NEED_CHECK==1 ? "1" : "0,1") + ") and a.year=" + year + " and a.term=" + term1 + " and a.day=" + day + " )ar" + "\n");
						sql.append(" join af_pbocreportdata p on ar.rep_id=p.rep_id group by p.cell_id" + "\n");
						sql.append(" )a1 left join" + "\n");
						sql.append(" (" + "\n");
						sql.append(" select  p.cell_id,sum(to_number(p.CELL_DATA))as report_value from " + "\n");
						sql.append(" (select * from  af_report a where a.template_id=" + strBeset(childRepIdAll) +" and a.version_id=" + strBeset(versionIdAll) + " and a.org_id in(" +  orgIds +") and a.rep_freq_id=" + repFreqIdAll + " and a.check_flag in(" + (Config.IS_NEED_CHECK==1 ? "1" : "0,1") + ") and a.year=" + year + " and a.term=" + term1 + " and a.day=" + day + " )ar" + "\n");
						sql.append(" join af_pbocreportdata p on ar.rep_id=p.rep_id group by p.cell_id" + "\n");
						sql.append(" ) a2" + "\n");
						sql.append(" on a1.cell_id=a2.cell_id" + "\n");
					}
					if(Config.DB_SERVER_TYPE.equals("db2")){
						sql.append(" insert into " + dataTble + "\n");
						sql.append(" select " + afReport.getRepId() + ",a1.cell_id,a1.report_value-a2.report_value  from " + "\n");
						sql.append(" (" + "\n");
						sql.append(" select p.cell_id,sum(to_number(p.CELL_DATA)) as report_value from " + "\n");
						sql.append(" (select * from  af_report a where a.template_id=" + strBeset(childRepIdAll) +" and a.version_id=" + strBeset(versionIdAll) + " and a.org_id=" +  strBeset(parentOrgId) +" and a.rep_freq_id=" + repFreqIdAll + " and a.check_flag in(" + (Config.IS_NEED_CHECK==1 ? "1" : "0,1") + ") and a.year=" + year + " and a.term=" + term1 + " and a.day=" + day + " )ar" + "\n");
						sql.append(" join af_pbocreportdata p on ar.rep_id=p.rep_id group by p.cell_id" + "\n");
						sql.append(" )a1 left join" + "\n");
						sql.append(" (" + "\n");
						sql.append(" select  p.cell_id,sum(to_number(p.CELL_DATA))as report_value from " + "\n");
						sql.append(" (select * from  af_report a where a.template_id=" + strBeset(childRepIdAll) +" and a.version_id=" + strBeset(versionIdAll) + " and a.org_id in(" +  orgIds +") and a.rep_freq_id=" + repFreqIdAll + " and a.check_flag in(" + (Config.IS_NEED_CHECK==1 ? "1" : "0,1") + ") and a.year=" + year + " and a.term=" + term1 + " and a.day=" + day + " )ar" + "\n");
						sql.append(" join af_pbocreportdata p on ar.rep_id=p.rep_id group by p.cell_id" + "\n");
						sql.append(" ) a2" + "\n");
						sql.append(" on a1.cell_id=a2.cell_id" + "\n");
	        		}  
	        		if(Config.DB_SERVER_TYPE.equals("sqlserver")){
	        			sql.append(" insert into " + dataTble + "\n");
						sql.append(" select " + afReport.getRepId() + ",a1.cell_id,a1.report_value-a2.report_value  from " + "\n");
						sql.append(" (" + "\n");
						sql.append(" select p.cell_id,sum(to_number(p.CELL_DATA)) as report_value from " + "\n");
						sql.append(" (select * from  af_report a where a.template_id=" + strBeset(childRepIdAll) +" and a.version_id=" + strBeset(versionIdAll) + " and a.org_id=" +  strBeset(parentOrgId) +" and a.rep_freq_id=" + repFreqIdAll + " and a.check_flag in(" + (Config.IS_NEED_CHECK==1 ? "1" : "0,1") + ") and a.year=" + year + " and a.term=" + term1 + " and a.day=" + day + " )ar" + "\n");
						sql.append(" join af_pbocreportdata p on ar.rep_id=p.rep_id group by p.cell_id" + "\n");
						sql.append(" )a1 left join" + "\n");
						sql.append(" (" + "\n");
						sql.append(" select  p.cell_id,sum(to_number(p.CELL_DATA))as report_value from " + "\n");
						sql.append(" (select * from  af_report a where a.template_id=" + strBeset(childRepIdAll) +" and a.version_id=" + strBeset(versionIdAll) + " and a.org_id in(" +  orgIds +") and a.rep_freq_id=" + repFreqIdAll + " and a.check_flag in(" + (Config.IS_NEED_CHECK==1 ? "1" : "0,1") + ") and a.year=" + year + " and a.term=" + term1 + " and a.day=" + day + " )ar" + "\n");
						sql.append(" join af_pbocreportdata p on ar.rep_id=p.rep_id group by p.cell_id" + "\n");
						sql.append(" ) a2" + "\n");
						sql.append(" on a1.cell_id=a2.cell_id" + "\n");
	        		}
	        		stat.execute(sql.toString());
					break;
				}
				}
			}else{
				conn = new DBConn();
				session = conn.beginTransaction();
				con = session.connection();
				stat = con.createStatement();
				String[] params =  FitechUtil.parseFomual(templateCollRule.getCollFormula());
				String parentOrgId = params[0];
				int collSchema = templateCollRule.getCollSchema();
				org = AFOrgDelegate.getOrgInfo(orgIdAll);
				AfTemplate template = (AfTemplate)session.find("from AfTemplate a where a.id.templateId=" + strBeset(childRepIdAll) + " and a.id.versionId=" + strBeset(versionIdAll)).get(0);//��ȡģ�����
				switch (collSchema){
				case Config.HZLX_GCHZ:{//�������
					String hsql = null;
					hsql=("from AfReport a where a.templateId=" + strBeset(childRepIdAll) + " and a.versionId=" + 
							strBeset(versionIdAll) + " and a.orgId=" + strBeset(orgIdAll) + " and a.repFreqId=" + repFreqIdAll + " and a.year=" + year + " and a.term=" + term1 + " and a.day=" + day);
					List<AfReport>  afReportList= session.find(hsql);//��ȡ���ͱ���Ϣ��
					if(afReportList.size()>0){
						afReport = afReportList.get(0);
						afReport.setCheckFlag(new Long(Config.CHECK_FLAG_AFTERSAVE));
						afReport.setTblInnerValidateFlag(null);
						afReport.setTblOuterValidateFlag(null);
						session.update(afReport);
						String del = "delete from af_pbocreportdata where rep_id=" + afReport.getRepId();
						stat.execute(del);//ɾ���Ѿ����ڵı���Ԫ������
					}
					else{
						afReport = new AfReport();
						afReport.setTemplateId(childRepIdAll);
						afReport.setVersionId(versionIdAll);
						afReport.setOrgId(orgIdAll);
						afReport.setRepFreqId(new Long(repFreqIdAll));
						afReport.setYear(new Long(year));
						afReport.setTerm(new Long(term1));
						afReport.setDay(new Long(day));
						afReport.setCheckFlag(new Long(4));
						afReport.setTimes(new Long(1));
						afReport.setCurId(new Long(curIdAll));
						afReport.setRepName(template.getTemplateName());
						afReport.setCheckFlag(new Long(Config.CHECK_FLAG_AFTERSAVE));//�����״̬��Ϊδ����
						afReport.setTblInnerValidateFlag(null);
						afReport.setTblOuterValidateFlag(null);
						session.save(afReport);
					}
					String orgIds = "";
					if(params[1].equals(Config.HZGS_TJH)){//����ۼ����ǳ������������ͬ����
						AfOrg parentOrg = (AfOrg)session.get(AfOrg.class, parentOrgId);
						if(parentOrg.getIsCollect().intValue()==1)//����ϼ����������������ݻ��ܹ�ϵ��ȡͬ����
							hsql = "select cr.id.orgId from AfCollectRelation cr where cr.id.orgId!=" + strBeset(orgIdAll) + " and cr.id.collectId=" + strBeset(parentOrgId);
						else// ����ֱ�Ӹ��ݻ������ȡͬ����
							hsql = "select af.orgId from AfOrg af where af.preOrgId='" + parentOrgId + "'";
						List list = session.find(hsql);
						orgIds = strBeset(list);
//							collectDataIter(strArr,messages, afDataDele,
//									 templateType,childOrgIds, afOrgDele,collResult,operator,isIterator,orgId);
					}else{//����ۼ�����ָ����ͬ����
						String[] strs =params[1].trim().split(",");
						orgIds = strBeset(strs);
					}
					String dataTble = templateType.toString().equals(com.fitech.gznx.common.Config.PBOC_REPORT) ? "af_pbocreportdata" : "af_otherreportdata";
					if(Config.DB_SERVER_TYPE.equals("oracle")){
						sql.append(" insert into " + dataTble + "\n");
						sql.append(" select " + afReport.getRepId() + ",a1.cell_id,to_char(to_number(a1.report_value)-case when a2.report_value is null then 0.00 else to_number(a2.report_value) end) from " + "\n");
						sql.append(" (" + "\n");
						sql.append(" select p.cell_id,sum(to_number(p.CELL_DATA)) as report_value from " + "\n");
						sql.append(" (select * from  af_report a where a.template_id=" + strBeset(childRepIdAll) +" and a.version_id=" + strBeset(versionIdAll) + " and a.org_id=" +  strBeset(parentOrgId) +" and a.rep_freq_id=" + repFreqIdAll + " and a.year=" + year + " and a.term=" + term1 + " and a.day=" + day + " )ar" + "\n");
						sql.append(" join af_pbocreportdata p on ar.rep_id=p.rep_id group by p.cell_id" + "\n");
						sql.append(" )a1 left join" + "\n");
						sql.append(" (" + "\n");
						sql.append(" select  p.cell_id,sum(to_number(p.CELL_DATA))as report_value from " + "\n");
						sql.append(" (select * from  af_report a where a.template_id=" + strBeset(childRepIdAll) +" and a.version_id=" + strBeset(versionIdAll) + " and a.org_id in(" +  orgIds +") and a.rep_freq_id=" + repFreqIdAll + " and a.year=" + year + " and a.term=" + term1 + " and a.day=" + day + " )ar" + "\n");
						sql.append(" join af_pbocreportdata p on ar.rep_id=p.rep_id group by p.cell_id" + "\n");
						sql.append(" ) a2" + "\n");
						sql.append(" on a1.cell_id=a2.cell_id" + "\n");
					}
					if(Config.DB_SERVER_TYPE.equals("db2")){
						sql.append(" insert into " + dataTble + "\n");
						sql.append(" select " + afReport.getRepId() + ",a1.cell_id,a1.report_value-a2.report_value  from " + "\n");
						sql.append(" (" + "\n");
						sql.append(" select p.cell_id,sum(to_number(p.CELL_DATA)) as report_value from " + "\n");
						sql.append(" (select * from  af_report a where a.template_id=" + strBeset(childRepIdAll) +" and a.version_id=" + strBeset(versionIdAll) + " and a.org_id=" +  strBeset(parentOrgId) +" and a.rep_freq_id=" + repFreqIdAll + " and a.year=" + year + " and a.term=" + term1 + " and a.day=" + day + " )ar" + "\n");
						sql.append(" join af_pbocreportdata p on ar.rep_id=p.rep_id group by p.cell_id" + "\n");
						sql.append(" )a1 left join" + "\n");
						sql.append(" (" + "\n");
						sql.append(" select  p.cell_id,sum(to_number(p.CELL_DATA))as report_value from " + "\n");
						sql.append(" (select * from  af_report a where a.template_id=" + strBeset(childRepIdAll) +" and a.version_id=" + strBeset(versionIdAll) + " and a.org_id in(" +  orgIds +") and a.rep_freq_id=" + repFreqIdAll + " and a.year=" + year + " and a.term=" + term1 + " and a.day=" + day + " )ar" + "\n");
						sql.append(" join af_pbocreportdata p on ar.rep_id=p.rep_id group by p.cell_id" + "\n");
						sql.append(" ) a2" + "\n");
						sql.append(" on a1.cell_id=a2.cell_id" + "\n");
	        		}  
	        		if(Config.DB_SERVER_TYPE.equals("sqlserver")){
	        			sql.append(" insert into " + dataTble + "\n");
						sql.append(" select " + afReport.getRepId() + ",a1.cell_id,a1.report_value-a2.report_value  from " + "\n");
						sql.append(" (" + "\n");
						sql.append(" select p.cell_id,sum(to_number(p.CELL_DATA)) as report_value from " + "\n");
						sql.append(" (select * from  af_report a where a.template_id=" + strBeset(childRepIdAll) +" and a.version_id=" + strBeset(versionIdAll) + " and a.org_id=" +  strBeset(parentOrgId) +" and a.rep_freq_id=" + repFreqIdAll + " and a.year=" + year + " and a.term=" + term1 + " and a.day=" + day + " )ar" + "\n");
						sql.append(" join af_pbocreportdata p on ar.rep_id=p.rep_id group by p.cell_id" + "\n");
						sql.append(" )a1 left join" + "\n");
						sql.append(" (" + "\n");
						sql.append(" select  p.cell_id,sum(to_number(p.CELL_DATA))as report_value from " + "\n");
						sql.append(" (select * from  af_report a where a.template_id=" + strBeset(childRepIdAll) +" and a.version_id=" + strBeset(versionIdAll) + " and a.org_id in(" +  orgIds +") and a.rep_freq_id=" + repFreqIdAll + " and a.year=" + year + " and a.term=" + term1 + " and a.day=" + day + " )ar" + "\n");
						sql.append(" join af_pbocreportdata p on ar.rep_id=p.rep_id group by p.cell_id" + "\n");
						sql.append(" ) a2" + "\n");
						sql.append(" on a1.cell_id=a2.cell_id" + "\n");
	        		}
	        		stat.execute(sql.toString());
					break;
				}
				}
			}
		}catch(Exception e){
			log.printStackTrace(e);
			e.printStackTrace();
			commitFlag = false;
			messages.add(org.getOrgName() + childRepIdAll + "�������ʧ�ܣ�");
		}finally{
			conn.endTransaction(commitFlag);
		}
		if(commitFlag){//������ܳɹ���ֱ��У�飬������У����
			result = validate(afReport.getRepId().toString(),operator,templateType);
			if(!result)
				messages.add(org.getOrgName() + childRepIdAll + "������ܽ���������У��ʧ�ܣ���");
			else{
				if(Config.SYSTEM_SCHEMA_FLAG==0){//ֻ����ϵͳģʽΪ����ģʽʱ���ſ�������ܻὫ����״̬��Ϊ�ϸ�
					if(org.getIsCollect().intValue()==1)
						result = AFReportDelegate.updateAfReportInCheckFlag(afReport.getRepId().intValue(),Config.CHECK_FLAG_PASS);
				}
			}
		}else
			result = commitFlag;
		return result;
	}
	/**
	 *  �ݹ����
	 * @param request
	 * @param messages
	 * @param afDataDele
	 * @param templateType
	 * @param isMulti
	 * @param childOrgIds
	 * @param afOrgDele
	 * @param mustOrgs
	 * @throws Exception
	 */
	public static boolean collectDataIter(String[] strArr,
			FitechMessages messages, AFDataDelegate afDataDele,
			Integer templateType, String childOrgIds, AFOrgDelegate afOrgDele,
			CollResultBean collResult,Operator operator,boolean isIterator,String tartOrgId) throws Exception {
		// int succeed = 0; // �ɹ���
		// int failure = 0; // ʧ����
		boolean result = true;
		List mustOrgs;
		String childRepIdAll = strArr[0].trim();
		String versionIdAll = strArr[1].trim();
		String orgIdAll = strArr[2].trim();
		Integer curIdAll = new Integer(strArr[3].trim());
		Integer repFreqIdAll = new Integer(strArr[4].trim());
		Integer donum = new Integer(strArr[5].trim()); // �������ܼ�¼�Ƿ��������ϱ�
		Integer year = new Integer(strArr[6].trim());
		Integer term1 = new Integer(strArr[7].trim());
		Integer day = new Integer(strArr[8].trim());
		AfOrg org = AFOrgDelegate.getOrgInfo(orgIdAll);
		if (false && donum.intValue() == 0) { // ��ʱ�����Ƿ����ϱ������ж�
			messages.add(org.getOrgName() + childRepIdAll + "�������ʧ�ܣ����ϱ����ݣ���<br/>");
			collResult.setFailure(collResult.getFailure()+1);
			result = false;
		} else {

			// ���ܹ���
			Integer collectRule = null;

			// if(templateType.toString().equals(com.fitech.gznx.common.Config.PBOC_REPORT)){

			// orgId = request.getParameter("orgId");

			// ��õ�ǰ�ñ����ͻ�������Ϣ
			/**
			 * ��ʹ��hibernate ���Ը� 2011-12-21 Ӱ�����AfOrg
			 **/
			AfOrg reportOrg = AFOrgDelegate.selectOne(orgIdAll);

			// ���е�ȡ�����������
			// if
			// (reportOrg.getPreOrgId().equals(com.fitech.gznx.common.Config.TOPBANK)){
			// collectRule = 1;
			// }
			// �������ȡ��ʵ��������
			// else
			if (reportOrg.getIsCollect().equals(
					Long.valueOf(com.fitech.gznx.common.Config.IS_COLLECT))) {
				collectRule = 2;
			}
			// ������������
			else {
				collectRule = 3;
			}
			// }else{
			// collectRule = 0;
			// }

			// �����������⴦��(����ʱȡ���ܻ����趨�Ļ��ܹ�ϵ������㼶����)
			if (templateType.toString().equals(
					com.fitech.gznx.common.Config.PBOC_REPORT)
					&& reportOrg.getPreOrgId().equals(
							com.fitech.gznx.common.Config.TOPBANK))
				collectRule = 2;

			// �õ�һ�ű���ı��ͻ����б�
			// if(templateType.toString().equals(com.fitech.gznx.common.Config.PBOC_REPORT)){
			/**
			 * ��ʹ��hibernate ���Ը� 2011-12-21 Ӱ�����AfOrg AfViewReport
			 * AfCollectRelation
			 **/
			mustOrgs = afOrgDele.getMustOrgList(childRepIdAll, versionIdAll,childOrgIds, orgIdAll, collectRule);
			if(mustOrgs==null || mustOrgs.size()==0){//���û���¼�����
				collResult.setSucceed(collResult.getSucceed()+1);
				return result;
			}
			String mustchildOrgIds = "";
			for (int j = 0; j < mustOrgs.size(); j++) {
				boolean iterResult = true;
				AfOrg afOrg = (AfOrg) mustOrgs.get(j);
				String[] parArrNew = new String[strArr.length];
				for(int i=0;i<parArrNew.length;i++)
					parArrNew[i] = strArr[i];
				parArrNew[2] = afOrg.getOrgId();
				int donumChild = AFReportDelegate.getAvailabilityOrgIdCount(childRepIdAll, versionIdAll,
						year,term1,day,
						curIdAll, repFreqIdAll,childOrgIds,afOrg.getOrgId(),collectRule,false);
				AfTemplateCollRule rule = AFDataDelegate.findCollectGacha(parArrNew[0], parArrNew[1], parArrNew[2]);
				if (afOrg.getIsCollect().intValue() == 1 && isIterator) {
					parArrNew[5] = String.valueOf(donumChild);
					if(rule==null){//��ͨ����
						iterResult = collectDataIter(parArrNew, messages, afDataDele,
								templateType, childOrgIds, afOrgDele, collResult,operator,isIterator,tartOrgId);//�ݹ鴦���¼�����
						int num = AFReportDelegate.getSimpleAvailabilityOrgIdCount(childRepIdAll, versionIdAll, year, 
								term1, day, curIdAll, repFreqIdAll, afOrg.getOrgId());
						if(num<=0){
							messages.add(afOrg.getOrgName() + childRepIdAll + "�������ʧ�ܣ����ϱ����ݣ���<br/>");
						}
					}
					else//�������
						iterResult = AFDataDelegate.collectDataByGacha(rule,parArrNew,messages, afDataDele,
								 templateType,childOrgIds, afOrgDele,collResult,operator,isIterator);
				}else{
					if(rule!=null && isIterator)//����������������ǰ̨ѡ��İ�ťʱ�ݹ鰴ť
						iterResult = AFDataDelegate.collectDataByGacha(rule,parArrNew,messages, afDataDele,
								 templateType,childOrgIds, afOrgDele,collResult,operator,isIterator);
				}
				if(iterResult)//����¼��л��ܳɹ�
					mustchildOrgIds += mustchildOrgIds.equals("") ? "'" + afOrg.getOrgId()+ "'" : ",'" + afOrg.getOrgId() + "'";
//				else //��ǰ���ǵ�ֻҪ���¼�����û�л��ܽ����ľ���ʧ�ܣ����ڽ���ע�͵�
//					result = false;
			}
			// }
			Integer reportInID = null;
			// �����嵥�����ֱ���
			/**
			 * ��ʹ��hibernate ���Ը� 2011-12-27 Ӱ�����AfTemplate
			 **/
			int reportStyle = AFTemplateDelegate.getReportStyle(childRepIdAll,
					versionIdAll);

			if (reportStyle == Config.REPORT_STYLE_QD.intValue()) {
				// �嵥ʽ����
				/** jdbc���� ��Ҫ�޸� ���Ը� 2011-12-27 **/
				reportInID = afDataDele.collectQDReport(childRepIdAll,
						versionIdAll, year, term1, day, repFreqIdAll, curIdAll,
						orgIdAll, mustchildOrgIds);

			} else {
				/** oracle�﷨ ��Ҫ�޸� ���Ը� 2011-12-27 */
				reportInID = afDataDele.doCollect(childRepIdAll, versionIdAll,
						orgIdAll, mustchildOrgIds, year, term1, day, repFreqIdAll,
						curIdAll, templateType,tartOrgId);
			}
			
			if (reportInID == null) {//������صı���IDΪ��
				messages.add(org.getOrgName() + childRepIdAll + "�������ʧ�ܣ����������쳣����<br/>");
				collResult.setFailure(collResult.getFailure()+1);
				result = false;
			} else {
				if (reportInID.intValue() == -1) {//������صı���IDΪ-1
					messages.add(org.getOrgName() + childRepIdAll + "�������ʧ�ܣ�δ���û��ܹ�ϵ����<br/>");
					collResult.setFailure(collResult.getFailure()+1);
					result = false;
				}else{
					boolean vr = validate(reportInID.toString(),operator,templateType);
					if(!vr){//����ʧ��
						if(donum!=0)
							messages.add(org.getOrgName() + childRepIdAll + "������ܽ���������У��ʧ�ܣ���<br/>");
						collResult.setFailure(collResult.getFailure()+1);
						result = vr;
					}else{//���У��ɹ������״̬Ϊ���ͨ��
						//if(org.getIsCollect().intValue()==1 && !org.getOrgId().trim().equals(tartOrgId.trim()))
						if(!org.getOrgId().trim().equals(tartOrgId.trim()))//����״̬ȥ���Ƿ��ǻ��ܻ����ж�
							result = AFReportDelegate.updateAfReportInCheckFlag(reportInID,Config.CHECK_FLAG_PASS);
					}
				}
			}
			if(result)
				collResult.setSucceed(collResult.getSucceed()+1);//���ܳɹ�+1
			else
				collResult.equals(collResult.getFailure()+1);//����ʧ��+1
		}
		return result;
	}

	public static boolean validate(String reportInID, Operator operator,
			Integer templateType) throws Exception {
		boolean resultBL = false;
		boolean resultBJ = false;
		Integer repInId = new Integer(reportInID);
		Integer reportStyle = AFReportDelegate.getReportStyle(new Long(
				reportInID));
		// ��㡢�嵥�ж�
		if (reportStyle.toString().equals(
				com.fitech.gznx.common.Config.REPORT_DD)) {
			if (com.cbrc.smis.common.Config.UP_VALIDATE_BN
					.equals(new Integer(1))) {
				/***
				 * ��oracle�﷨(nextval) ��Ҫ�޸� ���Ը� 2011-12-26
				 */
				resultBL = ProcedureHandle.runBNJY(repInId, operator
						.getOperatorName(), templateType);
			} else {
				resultBL = true;
			}
			// 2007-10-17�����ӱ��У�� ����껣�
			if (com.cbrc.smis.common.Config.UP_VALIDATE_BJ
							.equals(new Integer(1))) {
				/***
				 * ��oracle�﷨(nextval) ��Ҫ�޸� ���Ը� 2011-12-26
				 */
				resultBJ = ProcedureHandle.runBJJY(repInId, operator
						.getOperatorName(), templateType);
			} else {
				// �嵥ʽ����
				if (com.cbrc.smis.common.Config.UP_VALIDATE_BN
						.equals(new Integer(1))) {
					// ������ű���У��
					if (com.fitech.gznx.common.Config.OTHER_REPORT
							.equals(String.valueOf(templateType.intValue()))) {
						/***
						 * jdbc���� ������oracle�﷨ ����Ҫ�޸� ���Ը� 2011-12-23
						 */
						resultBL = QDValidate.bnValidate(repInId);
					} else {
						/***
						 * jdbc���� ������oracle�﷨ ����Ҫ�޸� ���Ը� 2011-12-23
						 */
						resultBL = new ValidateNxQDReport().bnValidate(repInId);
					}

				} else {
					resultBL = true;
				}
				resultBJ=resultBL;
				// 2007-10-17�����ӱ��У�� ����껣�               ������ʲô��˼��
//				if (resultBL
//						&& com.cbrc.smis.common.Config.UP_VALIDATE_BJ
//								.equals(new Integer(1))) {
//					resultBJ = true;
//				} else {
//					resultBJ = true;
//				}
			}
			if(resultBL && resultBJ)
				resultBJ = true;
			else
				resultBJ = false;
		}
		AFReportDelegate afr = new AFReportDelegate();
		/** ��ʹ��Hibernate ���Ը� 2011-12-26 **/
		afr.updateReportInCheckFlag(repInId, Config.CHECK_FLAG_AFTERJY);
		return resultBJ;
	}
}
