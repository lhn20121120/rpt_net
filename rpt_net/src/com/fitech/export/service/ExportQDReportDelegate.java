package com.fitech.export.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.hibernate.Criteria;
import net.sf.hibernate.Session;
import net.sf.hibernate.expression.Order;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.hibernate.MCurUnit;
import com.cbrc.smis.hibernate.MCurr;
import com.cbrc.smis.hibernate.MDataRgType;
import com.cbrc.smis.jdbc.FitechConnection;
import com.cbrc.smis.other.Aditing;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.gznx.common.DateUtil;
import com.fitech.gznx.common.StringUtil;
import com.fitech.gznx.dao.DaoModel;
import com.fitech.gznx.form.AFReportForm;
import com.fitech.gznx.po.AfReport;
import com.fitech.gznx.service.AFReportProductDelegate;


public class ExportQDReportDelegate  extends DaoModel{


	private static FitechException log = new FitechException(
			ExportQDReportDelegate.class);
	private static FitechMessages messages=null;

	/**
	 * 已使用hibernate 卞以刚 2011-12-22
	 * 影响对象：AfReport
	 * 获得实际数据报表对象
	 * 
	 * @param conn
	 *            Connection 数据库连接
	 * @param repInId
	 *            String 实际数据报表ID
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
				afReportForm.setWriter(afRep.getWriter());
				afReportForm.setChecker(afRep.getChecker());
				afReportForm.setPrincipal(afRep.getPrincipal());
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
	
	public static String getDataRagName(Integer dataRangeId){
		DBConn conn = null;
		try {
			conn = new DBConn();
			String hql = "select m.dataRgDesc  from MDataRgType m where m.dataRangeId="+dataRangeId;
			return (String)conn.openSession().createQuery(hql).uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return null;
	}
	
	public static String getFreqName(Integer freqId){
		DBConn conn = null;
		try {
			conn = new DBConn();
			String hql = "select m.repFreqName  from MRepFreq m where m.repFreqId="+freqId;
			return (String)conn.openSession().createQuery(hql).uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return null;
	}

	
	public static String getcurName(Integer curId){
		DBConn conn = null;
		try {
			conn = new DBConn();
			String hql = "select m.curName  from MCurr m where m.curId="+curId;
			return (String)conn.openSession().createQuery(hql).uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return null;
	}


	
	public static Map getBaseInfo() {
		
        DBConn dbConn = null;
        Session session = null;
        Map baseMap = new HashMap(); 
        
        try{
            dbConn  = new DBConn();
            session = dbConn.openSession();
            Map map = null;
            
            Criteria criteria = session.createCriteria(MCurr.class);
            criteria.addOrder(Order.desc("curId"));
            List<MCurr> mCurrlist = criteria.list() ;
            if(mCurrlist!=null){
            	map = new HashMap()  ;
            	for(MCurr mCurr:mCurrlist){
            		map.put(mCurr.getCurId(), mCurr.getCurName()) ;
            	}
            }
            baseMap.put("MCurr", map) ;
            
            
            criteria = session.createCriteria(MCurUnit.class);
            criteria.addOrder(Order.desc("curUnit"));
            List<MCurUnit> curUnitList = criteria.list() ;
            if(curUnitList!=null){
            	map = new HashMap()  ;
            	for(MCurUnit mCurUnit:curUnitList){
            		map.put(mCurUnit.getCurUnit(),mCurUnit.getCurUnitName()) ;
            	}
            }
            baseMap.put("MCurUnit", map) ;
          
            map = new HashMap();
            map.put(1, "境内");
            map.put(2, "法人");
            map.put(3, "并表");
//            criteria = session.createCriteria(MDataRgType.class);
//            criteria.addOrder(Order.desc("dataRangeId"));
//            List<MDataRgType> rangeList = criteria.list() ;
//            if(rangeList!=null){
//            	map = new HashMap()  ;
//            	for(MDataRgType range:rangeList){
//            		map.put(range.getDataRangeId(),range.getDataRgDesc()) ;
//            	}
//            }
            baseMap.put("Range", map) ;
            
        }catch (Exception e){
            log.printStackTrace(e);
        }finally{
            if(dbConn != null)
                dbConn.closeSession();
        }
		
		return baseMap;
	}


	public static int getdataPartCount(String childRepId, String versionId) {
		
		int count = 0 ;
		
		FitechConnection connFactory = null;
		Statement state = null;
		ResultSet rs = null;
		Connection connection = null;
		
        try{
			connFactory = new FitechConnection();
			connection = connFactory.getConn();
			state = connection.createStatement();
			
            String sql="select count(1) from report_part t " +
            			" where t.template_id='"+childRepId+"' and t.version_id='"+versionId+"'" ;
            rs = state.executeQuery(sql) ;
            
            while(rs.next()){
            	count = rs.getInt(1) ;
            }
            
         }catch (Exception e){
            log.printStackTrace(e);
        }finally{
        	try{
	        	if(rs!=null){
	        		rs.close(); 
	        	}
	        	if(state!=null){
	        		state.close() ;
	        	}
	        	if(connection!=null){
	        		connection.close() ;
	        	}
        	}catch(Exception e){
        		e.printStackTrace(); 
        	}
        }
		
		return count;
	}


	public static List getCellList(String childRepId, String versionId, Integer repInId) {
		
		FitechConnection connFactory = null;
		Statement state = null;
		ResultSet rs = null;
		Connection connection = null;
		
        List cellList = new ArrayList() ;
        
        try{
			connFactory = new FitechConnection();
			connection = connFactory.getConn();
			state = connection.createStatement();
			
            String sql="select m.row_id,m.col_id,i.report_value from report_in_info i, m_cell m " +
		            	"where i.cell_id = m.cell_id and i.rep_in_id = " + repInId +
		            		" and m.version_id='"+versionId+"' and m.child_rep_id='"+childRepId+"' " +
		            	" order by m.row_id asc,m.col_id asc" ;
            System.out.println("StrutsExportReportDataDelegate[getCellList]:" + sql);
            rs = state.executeQuery(sql) ;
            
            Object[] objects = null ;
            while(rs.next()){
            	objects = new Object[3] ;
            	objects[0] = rs.getObject(1);
            	objects[1] = rs.getObject(2);
            	objects[2] = rs.getObject(3);
            	cellList.add(objects) ;
            }
            
         }catch (Exception e){
            log.printStackTrace(e);
        }finally{
        	try{
	        	if(rs!=null){
	        		rs.close(); 
	        	}
	        	if(state!=null){
	        		state.close() ;
	        	}
	        	if(connection!=null){
	        		connection.close() ;
	        	}
        	}catch(Exception e){
        		e.printStackTrace(); 
        	}
        }
		
		return cellList;
	}
	
	
	
	private static int getTableColCount(Connection conn ,String templateId) {
		ResultSet rs = null;
		int result = 0;
		try {
			Statement state = conn.createStatement();
			result = 0;
			String sql="SELECT count(0) FROM user_tab_columns t where t.TABLE_NAME = 'AF_QD_"+templateId+"'" ;
			rs = state.executeQuery(sql) ;
			if(rs.next()){
				result  = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				rs= null;
			}
		}
		return result;
	}
	
public static List<Object[]> getCellList(String templateId, String repInId) {
		
		FitechConnection connFactory = null;
		Statement state = null;
		ResultSet rs = null;
		Connection connection = null;
        List cellList = new ArrayList() ;
        try{
			connFactory = new FitechConnection();
			connection = connFactory.getConn();
			state = connection.createStatement();
			int colCount  = getTableColCount(connection , templateId);
            String sql="SELECT * FROM AF_QD_"+templateId+" t where t.rep_id ="+repInId +" order by t.row_id" ;
            System.out.println(sql);
            rs = state.executeQuery(sql) ;
            Object[] objects = null ;
            while(rs.next()){
            	objects = new Object[colCount] ;
            	for (int i = 0; i < objects.length; i++) {
					 objects[i] = rs.getObject(i+1);
            	}
            	cellList.add(objects) ;
            }
         }catch (Exception e){
            log.printStackTrace(e);
        }finally{
        	try{
	        	if(rs!=null){
	        		rs.close(); 
	        	}
	        	if(state!=null){
	        		state.close() ;
	        	}
	        	if(connection!=null){
	        		connection.close() ;
	        	}
        	}catch(Exception e){
        		e.printStackTrace(); 
        	}
        }
		return cellList;
	}
	
	public static FitechException getLog() {
		return log;
	}

	public static List getCellListWithPart(String childRepId, String versionId) {
		
		FitechConnection connFactory = null;
		Statement state = null;
		ResultSet rs = null;
		Connection connection = null;
		
        List cellList = new ArrayList() ;
        
        try{
			connFactory = new FitechConnection();
			connection = connFactory.getConn();
			state = connection.createStatement();
			
            String sql=" select rp.part,m.row_id,m.col_id,rif.report_value from report_in_info rif ,m_cell m,report_part rp" +
		               " where rif.cell_id=m.cell_id and m.child_rep_id=rp.template_id and m.version_id=rp.version_id  and m.cell_name=rp.cell_name " +
		            		" and m.version_id='"+versionId+"' and m.child_rep_id='"+childRepId+"'  " +
		               " order by rp.part asc,m.row_id asc,m.col_id asc " ;
            System.out.println("[getCellListWithPart] : " + sql);
            rs = state.executeQuery(sql) ;
            
            Object[] objects = null ;
            while(rs.next()){
            	objects = new Object[4] ;
            	objects[0] = rs.getObject(1);
            	objects[1] = rs.getObject(2);
            	objects[2] = rs.getObject(3);
            	objects[3] = rs.getObject(4);
            	cellList.add(objects) ;
            }
            
         }catch (Exception e){
            log.printStackTrace(e);
        }finally{
        	try{
	        	if(rs!=null){
	        		rs.close(); 
	        	}
	        	if(state!=null){
	        		state.close() ;
	        	}
	        	if(connection!=null){
	        		connection.close() ;
	        	}
        	}catch(Exception e){
        		e.printStackTrace(); 
        	}
        }
        
        return cellList ;
	}




	public static int getReportStyle(String repId) {
		int reportStyle = 1 ;
		FitechConnection connFactory = null;
		Statement state = null;
		ResultSet rs = null;
		Connection connection = null;
        try{
			connFactory = new FitechConnection();
			connection = connFactory.getConn();
			state = connection.createStatement();
            String sql="SELECT b.report_style FROM af_report a join af_template b on a.template_id = b.template_id and a.version_id = b.version_id where a.rep_id  = '"+repId+"' ";
            rs = state.executeQuery(sql) ;
            if(rs.next()){
            	reportStyle = rs.getInt("report_style") ;
            }
         }catch (Exception e){
            log.printStackTrace(e);
        }finally{
        	try{
	        	if(rs!=null){
	        		rs.close(); 
	        	}
	        	if(state!=null){
	        		state.close() ;
	        	}
	        	if(connection!=null){
	        		connection.close() ;
	        	}
        	}catch(Exception e){
        		e.printStackTrace(); 
        	}
        }
		return reportStyle;
	}

	
	public static List selectQDReportList(AFReportForm reportInForm,
			Operator operator, int offset, int limit,String reportFlg ,int flag ) {    	 
    	List resList = new ArrayList();
    	DBConn conn = null;
    	Session session = null;
    	try{
    		if(reportInForm != null ){
    			conn = new DBConn();
    			session = conn.beginTransaction();
    			// 处理月报及以上情况
    			String[] dates = DateUtil.getFreqDateLast(reportInForm.getDate(),1).split("-");

    			int yueyear = Integer.parseInt(dates[0]);
    			int yueterm = Integer.parseInt(dates[1]);
    			int yueday = Integer.parseInt(dates[2]);
    			String[] yestoday = reportInForm.getDate().split("-");
    			int term = Integer.parseInt(yestoday[1]);
    			int year = Integer.parseInt(yestoday[0]);
    			int day = Integer.parseInt(yestoday[2]);
    			
    			// 处理旬报情况
    			String[] xundate = DateUtil.getFreqDateLast(reportInForm.getDate(),5).split("-");
    			int xunterm = Integer.parseInt(xundate[1]);
    			int xunyear = Integer.parseInt(xundate[0]);
    			int xunday = Integer.parseInt(xundate[2]);
    		
    			String rep_freq="";
    			if (yueterm == 12)
					// rep_freq = "('月','季','半年','年')";
					rep_freq = com.fitech.gznx.common.Config.FREQ_MONTH
							+ Config.SPLIT_SYMBOL_COMMA
							+ com.fitech.gznx.common.Config.FREQ_SEASON
							+ Config.SPLIT_SYMBOL_COMMA
							+ com.fitech.gznx.common.Config.FREQ_HALFYEAR
							+ Config.SPLIT_SYMBOL_COMMA
							+ com.fitech.gznx.common.Config.FREQ_YEAR;
				else if (yueterm == 6)
					// rep_freq = "('月','季','半年')";
					rep_freq = com.fitech.gznx.common.Config.FREQ_MONTH
							+ Config.SPLIT_SYMBOL_COMMA
							+ com.fitech.gznx.common.Config.FREQ_SEASON
							+ Config.SPLIT_SYMBOL_COMMA
							+ com.fitech.gznx.common.Config.FREQ_HALFYEAR;
				else if (yueterm == 3 || yueterm == 9)
					// rep_freq = "('月','季')";
					rep_freq = com.fitech.gznx.common.Config.FREQ_MONTH
							+ Config.SPLIT_SYMBOL_COMMA
							+ com.fitech.gznx.common.Config.FREQ_SEASON;
				else if(term == 1)
					rep_freq = com.fitech.gznx.common.Config.FREQ_MONTH
					+ Config.SPLIT_SYMBOL_COMMA
					+ com.fitech.gznx.common.Config.FREQ_YEARBEGAIN;
				else
					// rep_freq = "('月')";
					rep_freq = com.fitech.gznx.common.Config.FREQ_MONTH
							.toString();
    

    			StringBuffer sql=null;
    			/**已增加数据库判断*/
    			if(Config.DB_SERVER_TYPE.equals("sqlserver"))
    				sql= new StringBuffer("select top "+(offset+limit)+" a.org_id,d.org_name,a.template_id," +
						"a.version_id,a.template_name,a.rep_freq_id,a.cur_id , a.cur_name,a.rep_freq_name,b.check_flag,b.rep_id,a.supplement_flag from " +
						" VIEW_AF_REPORT a left join af_org d on a.org_id=d.org_id left join (select * from af_report   where times=1) b " +
								"    on a.org_id=b.org_id and a.template_id=b.template_id and a.version_id=b.version_id and a.cur_id=b.cur_id and a.rep_freq_id=b.rep_freq_id ");
    			if(Config.DB_SERVER_TYPE.equals("oracle"))
    				sql= new StringBuffer("select t.*,rownum from (select  a.org_id,d.org_name,a.template_id," +
						"a.version_id,a.template_name,a.rep_freq_id,a.cur_id , a.cur_name,a.rep_freq_name,b.check_flag,b.rep_id,a.supplement_flag from " +
						" VIEW_AF_REPORT a left join af_org d on a.org_id=d.org_id left join (select * from af_report   where times=1) b " +
								"    on a.org_id=b.org_id and a.template_id=b.template_id and a.version_id=b.version_id and a.cur_id=b.cur_id and a.rep_freq_id=b.rep_freq_id ");
    			if(Config.DB_SERVER_TYPE.equals("db2"))
    				sql= new StringBuffer("select * from ( select t.*,row_number() over(order by t.ORG_ID,t.supplement_flag,t.template_id,t.VERSION_ID) as rownum from (select  a.org_id,d.org_name,a.template_id," +
						"a.version_id,a.template_name,a.rep_freq_id,a.cur_id , a.cur_name,a.rep_freq_name,b.check_flag,b.rep_id,a.supplement_flag from " +
						" VIEW_AF_REPORT a left join af_org d on a.org_id=d.org_id left join (select * from af_report   where times=1) b " +
								"    on a.org_id=b.org_id and a.template_id=b.template_id and a.version_id=b.version_id and a.cur_id=b.cur_id and a.rep_freq_id=b.rep_freq_id ");				
				/**查询报表状态为审核通过报表*/
				StringBuffer where = new StringBuffer();
				where.append(" where (((('"	+ reportInForm.getDate() + "' between a.start_date and a.end_date"
								+ " and a.rep_freq_id=6 and b.year="+year+" and b.term="+term+" and b.day="+ day +") or ('"
								+ reportInForm.getDate()
								+ "' between a.start_date and a.end_date"
								+ " and a.rep_freq_id=5 and b.year="+xunyear+" and b.term="+xunterm+" and b.day="+ xunday +") or ('"
								+ reportInForm.getDate()
								+ "' between a.start_date and a.end_date"
								+ " and a.rep_freq_id in (" + rep_freq
								// 加入机构
								+ ") and  ((b.year="+yueyear+" and b.term="+yueterm+" and b.day="+yueday+" and a.rep_freq_id!=9) or (b.year="+yueyear+" and b.term=1 and b.day=1 and a.rep_freq_id=9)))) "
								//杭州联合银行修改，杭州联合银行无审核流程，顾此处修改为上报之后就能查看  上报后的CHECK_FLAG=0
								//+" and b.check_flag="+Config.RH_EXP_REP_FLAG
								+" and b.check_flag=1 "
								+" and a.is_report!=3) or ('"
								+ reportInForm.getDate()
								+ "' between a.start_date and a.end_date"
								+ " and (a.rep_freq_id in (" + rep_freq +",6)");
								// 加入机构
								//+  "or (a.rep_freq_id=5 and CONVERT(int,DAY(getdate()))="+ xunday +")) and a.is_report=3))and  a.template_type='"+reportFlg+"' ");
				// 加入机构  已增加数据库判断
				if(Config.DB_SERVER_TYPE.equals("oracle"))
					where.append(" or (a.rep_freq_id=5 and to_char(sysdate,'dd')="+ xunday +")) and a.is_report=3))and  a.template_type='"+reportFlg+"' ");
				if(Config.DB_SERVER_TYPE.equals("db2")){
					where.append(" or (a.rep_freq_id=5 and day(current date)="+ xunday +")) and a.is_report=3))and  a.template_type='"+reportFlg+"' ");
				}
				if(Config.DB_SERVER_TYPE.equals("sqlserver"))
					where.append(" or (a.rep_freq_id=5 and CONVERT(int,DAY(getdate()))="+ xunday +")) and a.is_report=3))and  a.template_type='"+reportFlg+"' ");
				/**添加报表名称查询条件（模糊查询）*/
				if(reportInForm.getRepName() != null && !reportInForm.getRepName().equals("")){
					where.append(" and a.template_name like '%" + reportInForm.getRepName().trim() + "%'");
				}
				
				/**添加报送频度（月/季/半年/年）查询条件*/
				if(!StringUtil.isEmpty(reportInForm.getRepFreqId()) && !String.valueOf(reportInForm.getRepFreqId()).equals(Config.DEFAULT_VALUE)){
					where.append(" and a.rep_freq_id=" + reportInForm.getRepFreqId() );
				}
				if(!StringUtil.isEmpty(reportInForm.getOrgId()) && !reportInForm.getOrgId().equals(Config.DEFAULT_VALUE)){
					where.append(" and a.ORG_ID='" + reportInForm.getOrgId().trim() + "'");
				}
				/** 添加报表编号查询条件（忽略大小写模糊查询） */
				if (reportInForm.getTemplateId() != null && !reportInForm.getTemplateId().equals("")) {
//					if(Config.SYSTEM_SCHEMA_FLAG==1){
//						where.append(" and a.TEMPLATE_ID in ("
//								+ reportInForm.getTemplateId().trim() + ")");
//					}else{
//						
//					}
					where.append(" and upper(a.TEMPLATE_ID) like upper('%"
							+ reportInForm.getTemplateId().trim() + "%')");
				}
		
				/** 报表辅助类型 */
				if (!StringUtil.isEmpty(reportInForm.getBak1()) && !reportInForm.getBak1().trim().equals(
								Config.DEFAULT_VALUE)) {
					where.append(" and a.BAK1 in (" + reportInForm.getBak1()+")");
				}
				/** 报表类型 */
				if (reportInForm.getIsReport() != null
						&& !reportInForm.getIsReport().equals(
								Integer.valueOf(Config.DEFAULT_VALUE))) {
					where.append(" and a.IS_REPORT=" + reportInForm.getIsReport());
				}
				
				/**添加报表查看权限（超级用户不用添加）
				 * 已增加数据库判断*/
				if(flag ==0){
					if (operator.isSuperManager() == false){
						if (operator.getChildRepSearchPopedom() != null &&!operator.getChildRepSearchPopedom().equals(""))
						{
							if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
								where.append(" and a.ORG_ID||a.template_id in (select ORG_REP_ID  from VIEW_ORG_REP where POW_TYPE=2 and USER_ID="+operator.getOperatorId()+")");
							if(Config.DB_SERVER_TYPE.equals("sqlserver"))
								where.append(" and a.ORG_ID+a.template_id in (select ORG_REP_ID  from VIEW_ORG_REP where POW_TYPE=2 and USER_ID="+operator.getOperatorId()+")");
						}
					}
				}else{
					if (operator.isSuperManager() == false){
						if (operator.getChildRepReportPopedom() != null &&!operator.getChildRepReportPopedom().equals(""))
						{
							if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
								where.append(" and a.ORG_ID||a.template_id in ( select viewOrgRep.org_Rep_Id from View_Org_Rep viewOrgRep where viewOrgRep.pow_Type = 2 and viewOrgRep.user_Id = '"+operator.getOperatorId()+"')");
							if(Config.DB_SERVER_TYPE.equals("sqlserver"))
								where.append(" and a.ORG_ID+a.template_id in ( select viewOrgRep.org_Rep_Id from View_Org_Rep viewOrgRep where viewOrgRep.pow_Type = 2 and viewOrgRep.user_Id = '"+operator.getOperatorId()+"')");
						}
					}
				}
				/**添加报表批次查询 */
				if (!StringUtil.isEmpty(reportInForm.getSupplementFlag()) && !reportInForm.getSupplementFlag().equals(Config.DEFAULT_VALUE)) {
					if(reportInForm.getSupplementFlag().equals("2")){
						where.append(" and (a.supplement_flag is null or a.supplement_flag=" + reportInForm.getSupplementFlag()+")");
					} else {
						where.append(" and a.supplement_flag=" + reportInForm.getSupplementFlag());
					}
				}
//				if(Config.SYSTEM_SCHEMA_FLAG==1){
//					where.append(AFReportProductDelegate.toExpPbocSql());
//				}
				if(Config.DB_SERVER_TYPE.equals("oracle"))
					sql.append(where.toString() + " order by a.ORG_ID,a.supplement_flag,a.template_id,a.VERSION_ID ) t where rownum<="+(offset+limit));
				if(Config.DB_SERVER_TYPE.equals("db2"))
					sql.append(where.toString() + " order by a.ORG_ID,a.supplement_flag,a.template_id,a.VERSION_ID ) t ) where rownum<="+(offset+limit));
				if(Config.DB_SERVER_TYPE.equals("sqlserver"))
					sql.append(where.toString() + " order by a.ORG_ID,a.supplement_flag,a.template_id,a.VERSION_ID");
				//System.out.println("sql:"+sql);
				Connection connection =session.connection();
				System.out.println(sql);
				ResultSet rs=connection.createStatement().executeQuery(sql.toString());
				int i=1;
				while(rs.next()){
					if(i<=offset){
						i++;
						continue;
					}
					Aditing aditing  = new Aditing();


					aditing.setRepName(rs.getString("TEMPLATE_NAME"));
					aditing.setActuFreqID(rs.getInt("REP_FREQ_ID"));
					aditing.setCurId(rs.getInt("CUR_ID"));
					//设置报表报送日期
//						aditing.setReportDate(reportInRecord.getReportDate());
					//设置报表编号
					aditing.setChildRepId(rs.getString("TEMPLATE_ID"));
					//设置报表版本号
					aditing.setVersionId(rs.getString("VERSION_ID"));
					//设置报表币种名称
					aditing.setCurrName(rs.getString("CUR_NAME"));
					int batchId = rs.getInt("SUPPLEMENT_FLAG");
					if(batchId != 0){
						aditing.setBatchId(String.valueOf(batchId));
					}else{
						aditing.setBatchId("2");
					}
					
					if (aditing.getActuFreqID() != null) {
						// yyyy-mm-dd 根据日期确定该日期具体的期数日期
						String trueDate = DateUtil
								.getFreqDateLast(reportInForm.getDate(),
										aditing.getActuFreqID());
						aditing.setYear(Integer.valueOf(trueDate.substring(0, 4)));
						aditing.setTerm(Integer.valueOf(trueDate.substring(5, 7)));
						aditing.setDay(Integer.valueOf(trueDate.substring(8, 10)));
					}
					
						//设置报送口径
				//	aditing.setDataRgTypeName(rs.getString("DATA_RG_DESC"));
					
						//设置报送频度
					aditing.setActuFreqName(rs.getString("REP_FREQ_NAME"));
					if(rs.getString("REP_ID") != null){
					aditing.setRepInId(rs.getInt("REP_ID"));
					} else {
						aditing.setRepInId(0);
					}
					if(rs.getString("CHECK_FLAG") == null ){
						if(aditing.getRepInId() != null)
							aditing.setRepInId(0);
					}else
						aditing.set_checkFlag(Short.valueOf(rs.getString("CHECK_FLAG")));
					
					aditing.setOrgName(rs.getString("ORG_NAME"));
					aditing.setOrgId(rs.getString("ORG_ID"));

					resList.add(aditing);						
		    	}
    		}	
    	}catch(Exception e){
    		e.printStackTrace();
    	}finally{
    	
    		if(conn != null) conn.closeSession();
    	}
    	return resList;
    }
	
	public static Long getStartRow(String templateId, String versionId ){
		DBConn conn = null;
		try {
			conn = new DBConn();
			String hql = "select m.startRow  from QdCellinfo m where m.id.templateId='"+templateId+"' and m.id.versionId='"+versionId+"'";
			return (Long)conn.openSession().createQuery(hql).uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return null;
	}
}
