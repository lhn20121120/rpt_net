package com.fitech.dataCollect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import com.cbrc.smis.adapter.StrutsListingTableDelegate;
import com.cbrc.smis.adapter.StrutsMRepRangeDelegate;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.form.MActuRepForm;
import com.cbrc.smis.form.MRepRangeForm;
import com.cbrc.smis.hibernate.MRepFreq;
import com.cbrc.smis.jdbc.FitechConnection;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.fitech.net.config.Config;
import com.fitech.net.form.CollectOrgInfoForm;
import com.fitech.net.hibernate.OrgNet;

public class DB2ExcelHandler {
	private static FitechException log = new FitechException();
	private FitechConnection connectionFactory = new FitechConnection(); 
	
	/**
	 * 通过频度名称取得它对应的频度id
	 * @param String 报送频度名称的字符串，多个之间用“，”号隔开
	 * @return String 这些频度名称所对应的id串，之间用“，”号隔开
	 * */
	public String getRepFreqID_from_freqName(String repFreqNames){
		String repFreqIds = "";
		DBConn conn = null;
    	Session session = null;
    	
    	try {
    		if(repFreqNames == null || repFreqNames.equals(""))
    			return null;
			conn = new DBConn();
			session = conn.beginTransaction();
			
			String hql = "from MRepFreq mrf where mrf.repFreqName in " + repFreqNames;
			List rep_freqList = session.createQuery(hql).list();
			
			if(rep_freqList == null && rep_freqList.size() ==0)
				return null;
			
			for(int i=0;i<rep_freqList.size();i++){
				MRepFreq mrepFreq = (MRepFreq)rep_freqList.get(i);
				repFreqIds += (repFreqIds.equals("") ? "" : ",") + mrepFreq.getRepFreqId();
			}
		}catch(Exception e){
    		log.printStackTrace(e);
    		repFreqIds=null;
    		if(conn != null) {    			
    			conn.endTransaction(true);
    		}
    	}finally {
    		if(conn != null){
    			conn.closeSession();
    		}    		
    	}
    	
		return repFreqIds;		
	}
	
	/***
	 * 通过频度id串，取得这些频度下的所以报表的集合
	 * @param repFreqIds String 频度id串
	 * @return 属于这些频度下的所以报表的集合 List中每个元素是MRepRangeForm对象(子报表id，版本号，机构id，机构类别id等)
	 */
	public List getReport_from_repFreq(String repFreqIds){
		List reportList = null;
		
		DBConn conn = null;
    	Session session = null;
    	
    	try {
    		if(repFreqIds == null || repFreqIds.equals(""))
    			return null;
			conn = new DBConn();
			session = conn.beginTransaction();
			/**取出的报表是不重复的*/
			String hql = "select distinct mar.comp_id.childRepId,mar.MChildReport.reportName,"+
							"mar.comp_id.versionId,mar.comp_id.dataRangeId,mar.MDataRgType.dataRgDesc,"+
							"mar.comp_id.repFreqId,mar.MRepFreq.repFreqName from MActuRep mar where mar.comp_id.repFreqId in ("+repFreqIds+") "+
							"order by mar.comp_id.childRepId";
			List repList = session.createQuery(hql).list();
			
			if(repList == null && repList.size() == 0)
				return null;
			else
				reportList = new ArrayList();
			
			for(int i=0;i<repList.size();i++){
				Object[] item = (Object[])repList.get(i);
				MActuRepForm mActuRep = new MActuRepForm();
				mActuRep.setChildRepId((String)item[0]);
				mActuRep.setReportName((String)item[1]);
				mActuRep.setVersionId((String)item[2]);
				mActuRep.setDataRangeId((Integer)item[3]);
				mActuRep.setDataRgDesc((String)item[4]);
				mActuRep.setRepFreqId((Integer)item[5]);
				mActuRep.setRepFreqName((String)item[6]);
				reportList.add(mActuRep);
			}
		} catch(Exception e){
    		log.printStackTrace(e);
    		reportList=null;
    		if(conn != null) 
    			conn.endTransaction(true);
    	}finally{
    		if(conn != null) conn.closeSession();
    		
    	}
		return reportList;
			
	}
	
	public List getReport_from_repFreq(String repFreqIds,int year,int term,Operator operator){
		List reportList = null;
		
		DBConn conn = null;
    	Session session = null;
    	
    	try {
    		if(repFreqIds == null || repFreqIds.equals(""))
    			return null;
    		
    		Calendar calendar = Calendar.getInstance();
			calendar.set(year,term-1,1);
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String validate_date = format.format(calendar.getTime());
			
			conn = new DBConn();
			session = conn.beginTransaction();
			/**取出的报表是不重复的*/
			StringBuffer hql = new StringBuffer("select distinct mar.comp_id.childRepId,mar.MChildReport.reportName,"+
							"mar.comp_id.versionId,mar.comp_id.dataRangeId,mar.MDataRgType.dataRgDesc,"+
							"mar.comp_id.repFreqId,mar.MRepFreq.repFreqName from MActuRep mar where mar.comp_id.repFreqId in ("+repFreqIds+")"+
							" and '" + validate_date + "' between mar.MChildReport.startDate and mar.MChildReport.endDate");
			/**加上报表汇总权限（汇总与本行报送权限一致）*/
			if(operator == null) return reportList;
			// 汇总与查看权限一致
			if(operator.getChildRepCheckPodedom() == null || operator.getChildRepCheckPodedom().equals(""))
				return reportList;
		
			hql.append(" and '" + operator.getOrgId() + "'|| mar.comp_id.childRepId in (" + operator.getChildRepSearchPopedom()+ ")");	
			hql.append("order by mar.comp_id.childRepId");
			
			List repList = session.createQuery(hql.toString()).list();
			
			if(repList == null && repList.size() == 0)
				return null;
			else
				reportList = new ArrayList();
			
			for(int i=0;i<repList.size();i++){
				Object[] item = (Object[])repList.get(i);
				MActuRepForm mActuRep = new MActuRepForm();
				mActuRep.setChildRepId((String)item[0]);
				mActuRep.setReportName((String)item[1]);
				mActuRep.setVersionId((String)item[2]);
				mActuRep.setDataRangeId((Integer)item[3]);
				mActuRep.setDataRgDesc((String)item[4]);
				mActuRep.setRepFreqId((Integer)item[5]);
				mActuRep.setRepFreqName((String)item[6]);
				reportList.add(mActuRep);
			}
		} catch(Exception e){
    		log.printStackTrace(e);
    		reportList=null;
    		if(conn != null) 
    			conn.endTransaction(true);
    	}finally{
    		if(conn != null) conn.closeSession();
    		
    	}
		return reportList;
			
	}
	
	/***
	 * 该月该报表已经报送上来的有效报表的机构数量
	 * @param childRepId 子报表id
	 * @param versionId 版本号id
	 * @param dataRangeId  数据范围id
	 * @param year  年份
	 * @param term  期数
	 * @return  int 有效报表的机构数量
	 */
	public int getAvailabilityOrgNum(String childRepId,String versionId,Integer dataRangeId,int year,int term,String childOrgIds,Integer curId){
		int orgNum = 0;
		DBConn conn = null;
		Session session = null;

		String hql = "select count(*) from ReportIn ri where ri.checkFlag="+Config.CHECK_FLAG_PASS +" and "
				+ "ri.times=(select max(r.times) from ReportIn r where "
				+ "ri.MChildReport.comp_id.childRepId=r.MChildReport.comp_id.childRepId and "
				+ "ri.MChildReport.comp_id.versionId=r.MChildReport.comp_id.versionId and "
				+ "ri.MDataRgType.dataRangeId=r.MDataRgType.dataRangeId and "
				+ "ri.orgId=r.orgId and ri.MCurr.curId=r.MCurr.curId and "
				+ "ri.year=r.year and ri.term=r.term and r.checkFlag not in ("+ com.fitech.net.config.Config.CHECK_FLAG_AFTERJY +","+ com.fitech.net.config.Config.CHECK_FLAG_UNREPORT+"))"
				+" and ri.MChildReport.comp_id.childRepId='"+childRepId+"'"
				+" and ri.MChildReport.comp_id.versionId='"+versionId+"'"
				+" and ri.MDataRgType.dataRangeId="+dataRangeId
				+" and ri.year="+year
				+" and ri.term="+term
				+" and ri.MCurr.curId="+curId
				+" and ri.orgId in ("+childOrgIds+")";

		try {
			conn = new DBConn();
			session = conn.openSession();
			List result = session.createQuery(hql).list();
			if(result!=null && result.size()!=0)
			{
				orgNum = ((Integer)result.get(0)).intValue();
			}
		} catch (Exception e) 
		{
			log.printStackTrace(e);
			orgNum = 0;
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return orgNum;
	}
	
	/**
	 * 查询审核通过的机构字串
	 * @param childRepId
	 * @param versionId
	 * @param dataRangeId
	 * @param year
	 * @param term
	 * @param curId
	 * @param childOrgIds
	 * @return
	 */
	public List getAvailabilityOrgIds(String childRepId,String versionId,Integer dataRangeId,int year,int term,Integer curId,String childOrgIds){
		List result = null;
		DBConn conn = null;
		Session session = null;
		
		if(childRepId == null || childRepId.equals("")
				|| versionId == null || versionId.equals("")
				|| childOrgIds == null || childOrgIds.equals(""))
			return result;
		
		String hql = "from OrgNet org where org.orgId in (select distinct ri.orgId from ReportIn ri where ri.checkFlag=" + Config.CHECK_FLAG_PASS + " and "
				+ "ri.times=(select max(r.times) from ReportIn r where "
				+ "ri.MChildReport.comp_id.childRepId=r.MChildReport.comp_id.childRepId and "
				+ "ri.MChildReport.comp_id.versionId=r.MChildReport.comp_id.versionId and "
				+ "ri.MDataRgType.dataRangeId=r.MDataRgType.dataRangeId and "
				+ "ri.orgId=r.orgId and ri.MCurr.curId=r.MCurr.curId and "
				+ "ri.year=r.year and ri.term=r.term and r.checkFlag not in ("+ com.fitech.net.config.Config.CHECK_FLAG_AFTERJY 
				+","+ com.fitech.net.config.Config.CHECK_FLAG_UNREPORT+"))"
				+" and ri.MChildReport.comp_id.childRepId='"+childRepId+"'"
				+" and ri.MChildReport.comp_id.versionId='"+versionId+"'"
				+" and ri.MDataRgType.dataRangeId="+dataRangeId
				+" and ri.year="+year
				+" and ri.term="+term
				+" and ri.orgId in ("+childOrgIds+"))";
		conn = new DBConn();
		session = conn.openSession();
		try {
			List list = session.createQuery(hql).list();
			
			if(list != null && list.size()>0){
				result = new ArrayList();
				
				for(int i=0;i<list.size();i++){
					OrgNet org = (OrgNet)list.get(i);
					result.add(org);
				}				
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
	 * 查询审核通过的机构数量
	 * @param childRepId
	 * @param versionId
	 * @param dataRangeId
	 * @param year
	 * @param term
	 * @param curId
	 * @param childOrgIds
	 * @return
	 */
	public int getAvailabilityOrgIdCount(String childRepId,String versionId,Integer dataRangeId,int year,int term,Integer curId,String childOrgIds){
		int result = 0;
		DBConn conn = null;
		Session session = null;
		
		if(childRepId == null || childRepId.equals("")
				|| versionId == null || versionId.equals("")
				|| childOrgIds == null || childOrgIds.equals(""))
			return result;		
		String hql="select count(ri.repInId) from ReportIn ri where  "
		 	+" ri.MChildReport.comp_id.childRepId='"+childRepId+"'"
			+" and ri.MChildReport.comp_id.versionId='"+versionId+"'"
			+" and ri.MDataRgType.dataRangeId="+dataRangeId
			+" and ri.year="+year
			+" and ri.term="+term
			+" and ri.MCurr.curId="+curId
			+" and ri.orgId in ("+childOrgIds+")"
			+" and ri.times>0"
			+" and ri.checkFlag=" + com.fitech.net.config.Config.CHECK_FLAG_PASS;
		
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
	 * 得到应报机构列表
	 * @param childRepId
	 * @param versionId
	 * @param childOrgIds
	 * @return
	 */
	public List getMustOrgList(String childRepId,String versionId,String childOrgIds){
		List result = null;	
		DBConn conn = null;
		Session session = null;
		
		if(childRepId == null || childRepId.equals("")
				|| versionId == null || versionId.equals("")
				|| childOrgIds == null || childOrgIds.equals(""))
			return result;
		
		try{
			conn=new DBConn();
			session=conn.openSession();
			
			String sql="from OrgNet org where org.orgId in (select distinct M.comp_id.orgId from MRepRange M where M.comp_id.orgId in (" + childOrgIds + ")  and M.comp_id.childRepId='" + childRepId + "' "			
						+"  and  M.comp_id.versionId='" + versionId + "')";
			List list=session.createQuery(sql).list();
			
			if(list!=null && list.size()>0){
				result = new ArrayList();
				for(int j=0;j<list.size();j++){
					OrgNet org = (OrgNet)list.get(j);
					result.add(org);
				}				
			}			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(conn!=null)			
				conn.closeSession();
		}
		return result;
	}
	/**
	 * 得到应报机构列表的数量
	 * @param childRepId
	 * @param versionId
	 * @param childOrgIds
	 * @return
	 */
	public int getMustOrgCount(String childRepId,String versionId,String childOrgIds){
		int  count = 0;	
		DBConn conn = null;
		Session session = null;
		
		if(childRepId == null || childRepId.equals("")
				|| versionId == null || versionId.equals("")
				|| childOrgIds == null || childOrgIds.equals(""))
			return count;
		
		try{
			conn=new DBConn();
			session=conn.openSession();
			
			String sql="select count(org.orgId) from OrgNet org where org.orgId in (select distinct M.comp_id.orgId from MRepRange M where M.comp_id.orgId in (" + childOrgIds + ")  and M.comp_id.childRepId='" + childRepId + "' "			
						+"  and  M.comp_id.versionId='" + versionId + "')";
			List list=session.createQuery(sql).list();
			
			if(list!=null && list.size()>0){
					count = ((Integer)list.get(0)).intValue();
								
			}			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(conn!=null)			
				conn.closeSession();
		}
		return count;
	}
	/**
	 * 根据报送频度，把月份转换成相应的期数
	 * 
	 * @param month 月份
	 * @param dataRangeId 频度id
	 * @return
	 */
	public int month_to_term(int month,Integer repFreqId){
		int term = 0;
		switch(repFreqId.intValue()){
		
		case 1:  //月报
			term = month;
			break;
		case 2: //季报
			term = month/3;
			break;
		case 3: //半年报
			term = month/6;
			break;
		case 4: //年报
			term = month/12;
			break;
		}
		return term;	
	}
	
	/***
	 * 需要汇总的各个分行的报表集合
	 * @param childRepId 子报表id String
	 * @param versionId 版本号 String
	 * @param dataRangeId	数据范围id Integer
	 * @param year 年份 int
	 * @param term 期数	 int
	 * @return
	 */
	public List needCollectReports(String childRepId,String versionId,Integer dataRangeId,int year,int term,String childOrgIds,Integer curId){
		List result = null;
		DBConn conn = null;
		Session session = null;

		String hql = "from ReportIn ri where ri.checkFlag="+Config.CHECK_FLAG_PASS +" and "
				+ "ri.times=(select max(r.times) from ReportIn r where "
				+ "ri.MChildReport.comp_id.childRepId=r.MChildReport.comp_id.childRepId and "
				+ "ri.MChildReport.comp_id.versionId=r.MChildReport.comp_id.versionId and "
				+ "ri.MDataRgType.dataRangeId=r.MDataRgType.dataRangeId and "
				+ "ri.orgId=r.orgId and ri.MCurr.curId=r.MCurr.curId and "
				+ "ri.year=r.year and ri.term=r.term and r.checkFlag not in ("+ com.fitech.net.config.Config.CHECK_FLAG_AFTERJY +
				","+ com.fitech.net.config.Config.CHECK_FLAG_UNREPORT+"))"
				+" and ri.MChildReport.comp_id.childRepId='"+childRepId+"'"
				+" and ri.MChildReport.comp_id.versionId='"+versionId+"'"
				+" and ri.MDataRgType.dataRangeId="+dataRangeId
				+" and ri.year="+year
				+" and ri.term="+term
				+" and ri.MCurr.curId="+curId
				+" and ri.orgId in ("+childOrgIds+")";
		
		try {
			conn = new DBConn();
			session = conn.openSession();
			result = session.createQuery(hql).list();
			
		} catch (Exception e) {
			log.printStackTrace(e);
			result =null;
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return result;
		
	}
	
	/***
	 * 根据需要汇总的实际子报表id串，汇总数据，并返回汇总好的数据(点对点报表)
	 * @param repInIds
	 * 
	 * @return List 汇总好的点对点的报表数据
	 */
	public List getCollect_P2PReport(String repInIds){
		List p2pDataList = new ArrayList();
		DBConn conn = null;
		Session session = null;

		String hql = "select r.MCell.cellName,r.MCell.rowId,r.MCell.colId,r.MCell.dataType,r.reportValue"
					+" from ReportInInfo r where r.comp_id.repInId in("+repInIds+") order by r.MCell.cellName";
		
		try{
			conn = new DBConn();
			session = conn.openSession();
			List list = session.createQuery(hql).list();
			if(list==null || list.size()==0)
				return null;
			
			/**用于把所有的相同单元格汇总*/
			HashMap map = new HashMap();
			for(int i=0; i<list.size();i++){
				Object[] item = (Object[])list.get(i);
				String cellName = (String)item[0];
				Integer rowId = (Integer)item[1];
				String colId = (String)item[2];
				Integer dataType = (Integer)item[3];
				String cellValue = (String)item[4];
				
				/**单元格不存在则添加到集合中*/
				if(!map.containsKey(cellName)){
					P2PRep_Data data = new P2PRep_Data();
					data.setRowId(rowId);
					data.setColId(colId);
					data.setDataType(String.valueOf(dataType));
					
					/**如果不是数值则为0*/
					if(!Util.isDouble(cellValue))
						cellValue="0";
					data.setValue(cellValue);
					map.put(cellName,data);		
				}
				/**如果该单元格已经存在，则把加上新的值*/
				else{
					P2PRep_Data data = (P2PRep_Data)map.get(cellName);
					String newValue = data.getValue();
					
					try{
						/**如果不是数值则为0*/
						if(!Util.isDouble(newValue))
							newValue="0";
						double new_cell_value = Double.parseDouble(cellValue)+Double.parseDouble(newValue);
						
						data.setValue(String.valueOf(new_cell_value));
					}catch(Exception e){
						//e.printStackTrace();
						/**如果该类型不是数值型，则不做累加工作*/
					}	
				}
			}
			Set keySet = map.keySet();
			Iterator it = keySet.iterator();
			/**循环把这些单元格信息加到集合中*/
			while(it.hasNext()){
				p2pDataList.add((P2PRep_Data)map.get((String)it.next()));
			}
		} catch (Exception e) {
			log.printStackTrace(e);
			p2pDataList =null;
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		
		
		return p2pDataList;
		
	}
	
	/**
	 * 根据需要汇总的实际子报表id串，汇总数据，并返回汇总好的数据（清单式报表）
	 * 
	 * @param repInIds
	 * @param childRepId
	 * @param versionId
	 * @return
	 */
	public List getCollect_BillReport(String repInIds,String childRepId,String versionId){
		List billDataList = new ArrayList();
		ResultSet rs = null;
        Connection conn = null;
        String cols=null;
        
        try {
        	conn=connectionFactory.getConn();
            String tableName = this.get_BillReport_TbName(childRepId,versionId);
                        
            ResourceBundle resource=ResourceBundle.getBundle("com.cbrc.smis.db2xml.QDColsResources");
            
            try{
            	cols=resource.getString(childRepId + versionId);
            }catch(Exception e){
            	cols=null;
            }
            
            if((tableName!=null && !tableName.equals("")) && (cols!=null && !cols.equals(""))){              
                String sql = "select * from " + tableName + " where Rep_In_ID in(" + repInIds + 
                	") order by rep_in_id,col1";   
                PreparedStatement pstmt = conn.prepareStatement(sql);
                rs = pstmt.executeQuery();
                
            }
            if(rs!=null){          	   	 
          	     ResultSetMetaData dmd=rs.getMetaData();
 
          	   	 while(rs.next()){
          	     	StringBuffer str=new StringBuffer("");
          	   	 	for(int j=1;j<=dmd.getColumnCount();j++){
          	   	 		String columnName=dmd.getColumnName(j);
          	   	 		//从有效得字段开始取
          	   	 	   if (columnName.substring(0, 1).equalsIgnoreCase("c")&&!columnName.equalsIgnoreCase("Col1")){
          	   	 		   /**如果字段为空值，则赋值为空字串*/
          	   	 		   String value = rs.getString(columnName);
          	   	 		   if(value==null)
          	   	 			   value="";
          	   	 		   else
          	   	 			   value =value.trim();
          	   	 		   
          	   	 		   str.append(str.equals("") ? value:","+value);   
          	   	 	   }
          	   	 	  
          	   	 	} 
          	   	 	BillRep_Data billRep=new BillRep_Data();
          	   	    billRep.setData(str.toString());
          	   	    billDataList.add(billRep);
          	   	 }
          	 }
          	rs.close();
        }catch (SQLException e) {            
            log.printStackTrace(e);
            connectionFactory.close();
        }catch(Exception e){
        	log.printStackTrace(e);
        	connectionFactory.close();
        }
		
		return billDataList;
		
	}

    /**
     * 根据清单式报表的id和版本号，取得该清单式报表所对应的数据库表名
     * @param childReportId String  子报表id(清单式的)
     * @param versionId String 版本号id
     * @return 返回该清单式报表所对应的数据库表名 
     */
    public String get_BillReport_TbName(String childReportId,String versionId){
       try {
           return StrutsListingTableDelegate.getTalbeName(childReportId,versionId);
       } catch (Exception e) {
            log.printStackTrace(e);
       }
       return "";
    }
    
    /***
     * 根据模板编号和版本号，取得该模板填报范围内的机构id的集合
     * @param childRepId
     * @param versionId
     * @return
     */
	public List getRepOrgIds(String childRepId,String versionId){
		/**取得需要填报该报表的所有机构*/
    	List orgList = StrutsMRepRangeDelegate.findAll(childRepId,versionId);
    	
    	/**该报表所对应的机构id集合*/
    	if(orgList==null || orgList.size()==0)
    		return null;
    	List orgIds = new ArrayList();
    	
    	for(int i=0 ; i<orgList.size() ;i++)
    	{
    		orgIds.add(((MRepRangeForm)orgList.get(i)).getOrgId().trim());
    	}
    	return orgIds;
		
	}
	
	/***
	 * 取得该报表一个数据范围一个频度下所对应的频度类型
	 * @param childRepId 子报表id
	 * @param versionId 版本号id
	 * @param dataRangeId 数据范围id
	 * @param repFreqId 频度id
	 * @return 频度类型字串，之间用逗号隔开
	 */
	public String getOatIds(String childRepId,String versionId, Integer dataRangeId ,Integer repFreqId){
		DBConn conn = null;
    	Session session = null;
		String otaIds = "";
    	try {
			conn = new DBConn();
			session = conn.beginTransaction();
			
			/**取得该报表一个数据范围一个频度下所对应的频度类型*/
			String hql = "select distinct mar.comp_id.OATId from MActuRep mar "+
						"where mar.MChildReport.comp_id.childRepId='"+childRepId+"' "+
						" and mar.MChildReport.comp_id.versionId='"+versionId+"' "+
						" and mar.MDataRgType.dataRangeId="+dataRangeId+
						" and mar.MRepFreq.repFreqId="+repFreqId;
			
			List repList = session.createQuery(hql).list();
			
			if(repList == null && repList.size() ==0)
				return null;
			
			for(int i=0;i<repList.size();i++){
				otaIds +=(otaIds.equals("") ? "" : ",") + (Integer)repList.get(i);
			}		
		} catch(Exception e){
    		log.printStackTrace(e);
    		otaIds = "";
    		if(conn != null) 
    			conn.endTransaction(true);
    	}finally{
    		if(conn != null) 
    			conn.closeSession();
    		
    	}
    	return otaIds;
		
	}
	
	/**
	 *  得到汇总机构信息集合
	 * 
	 * @param mustOrgs
	 * @param doOrgs
	 * @return
	 */
	public List getCollectOrgInfos(List mustOrgs,List doOrgs){
		List collectOrgInfos=new ArrayList();
		if(mustOrgs==null && doOrgs==null) return collectOrgInfos;
		CollectOrgInfoForm coif=null;
		OrgNet mustOrg=null;
		OrgNet doOrg=null;
		//mustOrgs为空，doOrgs不为空
		if(mustOrgs == null){
			if(doOrgs != null){
				for(int i=0;i<doOrgs.size();i++)
				{
					coif=new CollectOrgInfoForm();
					doOrg=(OrgNet)doOrgs.get(i);
					coif.setNeedOrgName("");
					coif.setActuOrgName(doOrg.getOrgName());
					collectOrgInfos.add(coif);
				}
			}
		}
		//doOrgs为空，mustOrgs不为空
		if(doOrgs==null){
			if(mustOrgs != null){
				for(int i=0;i<mustOrgs.size();i++)
				{
					coif=new CollectOrgInfoForm();
					mustOrg=(OrgNet)mustOrgs.get(i);
					coif.setNeedOrgName(mustOrg.getOrgName());
					coif.setActuOrgName("");
					collectOrgInfos.add(coif);
				}
			}
		}
		//mustOrgs，doOrgs都不为空
		if(mustOrgs != null && doOrgs != null){
			int mSize=mustOrgs.size();
			int dSize=doOrgs.size();
			if(mSize>dSize){
				for(int j=0;j<mSize;j++)
				{
					coif=new CollectOrgInfoForm();
					mustOrg=(OrgNet)mustOrgs.get(j);
					coif.setNeedOrgName(mustOrg.getOrgName());
					if(mustOrgIsInActuOrgs(mustOrg,doOrgs)){
						coif.setActuOrgName(mustOrg.getOrgName());
					}
					if(!mustOrgIsInActuOrgs(mustOrg,doOrgs)){
						coif.setActuOrgName("未报");
					}
					collectOrgInfos.add(coif);
				}
			}
			if(dSize>mSize){
				for(int j=0;j<dSize;j++)
				{
					coif=new CollectOrgInfoForm();
					mustOrg=(OrgNet)mustOrgs.get(j);
					doOrg=(OrgNet)doOrgs.get(j);
					coif.setActuOrgName(doOrg.getOrgName());
					if(mustOrg!=null)
					{
						coif.setNeedOrgName(mustOrg.getOrgName());
					}else{
						coif.setNeedOrgName("");
					}
					collectOrgInfos.add(coif);
				}
			}
			if(dSize==mSize){
				for(int j=0;j<dSize;j++)
				{
					coif=new CollectOrgInfoForm();
					mustOrg=(OrgNet)mustOrgs.get(j);
					doOrg=(OrgNet)doOrgs.get(j);
					coif.setActuOrgName(doOrg.getOrgName());
					coif.setNeedOrgName(mustOrg.getOrgName());
					collectOrgInfos.add(coif);
				}
			}
		}
		return collectOrgInfos;
	}
	
	/**
	 * 判断该应报机构在实报机构集合中是否存在
	 * 
	 * @param mustOrg
	 * @param actuOrgs
	 * @return
	 */
	public static boolean mustOrgIsInActuOrgs(OrgNet mustOrg,List actuOrgs){
		boolean flag=true;
		if(mustOrg==null) return false;
		if(actuOrgs==null || actuOrgs.size()==0) 
			return false;
		
		for(int i=0;i<actuOrgs.size();i++){
			if(mustOrg.getOrgId().equals(((OrgNet)actuOrgs.get(i)).getOrgId())){
				return true;
			}
		}
		
		flag=false;
		return flag;
	}
}
