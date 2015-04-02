/*
 * Created on 2005-12-17
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.cbrc.smis.adapter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.hibernate.Criteria;
import net.sf.hibernate.Session;
import net.sf.hibernate.expression.Order;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.hibernate.MCurUnit;
import com.cbrc.smis.hibernate.MCurr;
import com.cbrc.smis.jdbc.FitechConnection;
import com.cbrc.smis.util.FitechException;

@SuppressWarnings({"rawtypes","unchecked"})
public class StrutsExportReportDataDelegate {
    
    private static FitechException log=new FitechException(StrutsExportReportDataDelegate.class);

    
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
//              map = new HashMap()  ;
//              for(MDataRgType range:rangeList){
//                  map.put(range.getDataRangeId(),range.getDataRgDesc()) ;
//              }
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
    
    
    
    public static FitechException getLog() {
        return log;
    }

    public static void setLog(FitechException log) {
        StrutsExportReportDataDelegate.log = log;
    }


    public static List getCellListWithPart(String childRepId, String versionId, Integer repInId) {
        
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
                            " and rif.rep_in_id = " + repInId +
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


    public static Integer getUnit(String childRepId, String versionId) {
        int unit  = 2;
        FitechConnection connFactory = null;
        Statement state = null;
        ResultSet rs = null;
        Connection connection = null;
        try{
            connFactory = new FitechConnection();
            connection = connFactory.getConn();
            state = connection.createStatement();
            String sql="SELECT t.cur_unit FROM  m_child_report t  where t.child_rep_id='"+childRepId+"' and t.version_id='"+versionId+"'" ;
            rs = state.executeQuery(sql) ;
            if(rs.next()){
                unit = rs.getInt(1) ;
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
        return unit;
    }


    /**
     * add by ywz
     * @return versionMap
     */
    public static Map<String,String> getVersionMap()
    {
        Map<String,String> versionMap = new HashMap<String,String>();
        FitechConnection connFactory = null;
        Statement state = null;
        ResultSet rs = null;
        Connection connection = null;
        try{
            connFactory = new FitechConnection();
            connection = connFactory.getConn();
            state = connection.createStatement();
            String sql="SELECT t.repid,t.oriversion,t.newversion FROM  cbrc_version_mapping t" ;
            rs = state.executeQuery(sql) ;
            while(rs.next()){
                versionMap.put(rs.getString(1)+"_"+rs.getString(2), rs.getString(3));
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
        return versionMap;
    }
}