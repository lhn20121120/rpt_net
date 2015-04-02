package com.fitech.gznx.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import org.apache.struts.util.LabelValueBean;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.util.FitechException;
import com.fitech.gznx.common.Config;
import com.fitech.gznx.common.StringUtil;
import com.fitech.gznx.po.AfCellinfo;

public class StrutsAFCellDelegate {

	private static FitechException log = new FitechException(
			StrutsAFCellDelegate.class);
	
    /**
     * 获得单元格ID
     * 
     * @author rds
     * @serialData 2005-12-09
     * 
     * @param childRepId
     *            String 子报表ID
     * @param versionId
     *            String 版本号
     * @param cellName
     *            String 单元格名称
     * @return Integer 如果没有查询到单元格，返回null
     */
    public static Integer getCellId(String childRepId, String versionId,
            String cellName) {
        Integer cellId = null;
        if (childRepId == null || versionId == null || cellName == null)
            return cellId;

        DBConn conn = null;
        try {
            String hql = "select mc.cellId from AfCellinfo mc where mc.templateId='"
                    + childRepId
                    + "'"
                    + " and mc.versionId='"
                    + versionId
                    + "'" + " and mc.cellName='" + cellName + "' order by mc.cellId desc";
            	// System.out.println(hql);
            conn = new DBConn();

            List list = conn.openSession().find(hql);
            if (list != null && list.size() > 0) {
            	cellId = ((Long) list.get(0)).intValue();
            }
        } catch (HibernateException he) {
        	cellId = null;
            log.printStackTrace(he);
        } finally {
            if (conn != null)
                conn.closeSession();
        }

        return cellId;
    }
    
    /**
     * 获得单元格ID
     * 
     * @author rds
     * @serialData 2005-12-09
     * 
     * @param childRepId
     *            String 子报表ID
     * @param versionId
     *            String 版本号
     * @param cellName
     *            String 单元格名称
     * @return Integer 如果没有查询到单元格，返回null
     */
    public static Integer getCellPid(String childRepId, String versionId,
            String cellPName) {
        Integer cellId = null;
        if (childRepId == null || versionId == null || cellPName == null)
            return cellId;

        DBConn conn = null;
        try {
        	
        	String[] cellName = cellPName.split("\\.");
        	
            String hql = "select mc.cellId from AfCellinfo mc where mc.templateId='"
                    + childRepId
                    + "' and mc.versionId='"
                    + versionId
                    + "' and mc.colNum='" 
                    + cellName[0].trim() + "' "
                    + " and mc.cellPid='" 
                    + cellName[1].trim()
                    + "' order by mc.cellId desc";
            	// System.out.println(hql);
            conn = new DBConn();

            List list = conn.openSession().find(hql);
            
            if (list != null && list.size() > 0) {
            	cellId = ((Long) list.get(0)).intValue();
            }
        } catch (HibernateException he) {
        	cellId = null;
            log.printStackTrace(he);
        } finally {
            if (conn != null)
                conn.closeSession();
        }

        return cellId;
    }
    
    /**
     * 获得单元格ID
     * 
     * @author rds
     * @serialData 2005-12-09
     * 
     * @param childRepId
     *            String 子报表ID
     * @param versionId
     *            String 版本号
     * @param cellName
     *            String 单元格名称
     * @return Integer 如果没有查询到单元格，返回null
     */
    public static Map getAFCellIdMap(String childRepId, String versionId) {
    	Map cellMap = new HashMap();

        if (childRepId == null || versionId == null )
            return cellMap;

        DBConn conn = null;
        try {
            String hql = "select mc.cellId,mc.cellName from AfCellinfo mc where mc.templateId='"
                    + childRepId
                    + "'"
                    + " and mc.versionId='"
                    + versionId
                    + "' order by mc.cellId desc";
            
            conn = new DBConn();

            List<Object[]> list = conn.openSession().find(hql);
            
        	for(Object[] cellInfo:list){
        		int cellId = ((Long)cellInfo[0]).intValue();
        		String cellName = (String) cellInfo[1];
        		if(!cellMap.containsKey(cellName)){
        			cellMap.put(cellName, cellId);
        		}
        	}            	
           
        } catch (HibernateException he) {
        	cellMap = null;
            log.printStackTrace(he);
        } finally {
            if (conn != null)
                conn.closeSession();
        }

        return cellMap;
    }
    
    /**
     * 获得单元格ID
     * 
     * @author rds
     * @serialData 2005-12-09
     * 
     * @param childRepId
     *            String 子报表ID
     * @param versionId
     *            String 版本号
     * @param cellName
     *            String 单元格名称
     * @return Integer 如果没有查询到单元格，返回null
     */
    public static Map getMCellIdMap(String childRepId, String versionId) {
    	Map cellMap = new HashMap();

        if (childRepId == null || versionId == null )
            return cellMap;

        DBConn conn = null;
        try {
            String hql = "select mc.cellId,mc.cellName from MCell mc where mc.MChildReport.comp_id.childRepId='"
                    + childRepId
                    + "'"
                    + " and mc.MChildReport.comp_id.versionId='"
                    + versionId
                    + "' order by mc.cellId desc";
            
            conn = new DBConn();

            List<Object[]> list = conn.openSession().find(hql);
            
        	for(Object[] cellInfo:list){
        		int cellId = (Integer)cellInfo[0];
        		String cellName = (String) cellInfo[1];
        		if(!cellMap.containsKey(cellName)){
        			cellMap.put(cellName, cellId);
        		}
        	}           
        } catch (HibernateException he) {
        	cellMap = null;
            log.printStackTrace(he);
        } finally {
            if (conn != null)
                conn.closeSession();
        }

        return cellMap;
    }
    public static List getColNameList(String templateId,String versionId){
		List resList=null;
		
		if(templateId==null || versionId==null) 
			return resList;
		
		DBConn conn=null;
		
		try{
			String hql="select distinct c.colNum,c.colName from AfCellinfo c where c.templateId='" +templateId+"' and " +
					" c.versionId='"+versionId+"'";
			conn=new DBConn();
			List list=conn.openSession().find(hql);
			if(list!=null && list.size()>0){
				resList=new ArrayList();
				resList.add(new LabelValueBean("",""));
				for(int i=0;i<list.size();i++){
					Object[] os=(Object[])list.get(i);
					//int coln = convertColStringToNum((String)os[0]);
					resList.add(new LabelValueBean((String)os[1],(String)os[0]));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(conn!=null) conn.closeSession();
		}
		return resList;
    }
    
    public static String getColNamebyId(String templateId,String versionId,String colID){
		String resList=null;
		
		if(templateId==null || versionId==null || StringUtil.isEmpty(colID)) 
			return resList;
		
		DBConn conn=null;
		
		try{
			String hql="select distinct c.colName from AfCellinfo c where c.templateId='" +templateId+"' and " +
					" c.versionId='"+versionId+"' and c.colNum='"+colID+"'";
			conn=new DBConn();
			List list=conn.openSession().find(hql);
			if(list!=null && list.size()>0){
				
				resList=(String)list.get(0);			
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(conn!=null) conn.closeSession();
		}
		return resList;
    }
    
    /***
     * 查处单元格横向和纵向名称
     * @param templateId
     * @param versionId
     * @param cellName
     * @return
     */
    public static String getColNameAndRowNamebyId(String templateId,String versionId,String cellName){
		
    	String colNameRowName = "";
    	
		if(templateId==null || versionId==null || StringUtil.isEmpty(cellName)) 
			return null;
		
		DBConn conn=null;
		
		try{
			String hql="from AfCellinfo c where c.templateId='" +templateId+"' and " +
					" c.versionId='"+versionId+"' and c.cellName='"+cellName+"'";
			conn=new DBConn();
			List list=conn.openSession().find(hql);
			AfCellinfo cellInfo = null;
			if(list!=null && list.size()>0){
				
				cellInfo=(AfCellinfo)list.get(0);			
			}
			if(cellInfo!=null)
				colNameRowName = cellInfo.getColName()+"_"+cellInfo.getRowName();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(conn!=null) conn.closeSession();
		}
		return colNameRowName;
    }
    
	/**
     * 将列号转换为数字
     * 
     * @param ref
     * @return
     */
    private static int convertColStringToNum(String ref) {
	int retval = 0;
	int pos = 0;
	ref = ref.trim();
	for (int k = ref.length() - 1; k > -1; k--) {
	    char thechar = ref.charAt(k);
	    if (pos == 0)
		retval += Character.getNumericValue(thechar) - 9;
	    else
		retval += (Character.getNumericValue(thechar) - 9) * (pos * 26);
	    pos++;
	}
	return retval - 1;
    }
    public static List getAllCell(String templateId,String versionId,String reportFlg){
		List cellList=null;
		
		if(templateId==null || versionId==null) 
			return cellList;
		
		DBConn conn=null;
		
		try{
			String hql="";
			if(reportFlg.equals(Config.CBRC_REPORT)){
				hql="from MCell c where c.MChildReport.comp_id.childRepId='" +templateId+"' and " +
				" c.MChildReport.comp_id.versionId='"+versionId+"'";
			} else {
				hql="from AfCellinfo c where c.templateId='" +templateId+"' and " +
				" c.versionId='"+versionId+"'";
			}
			conn=new DBConn();
			cellList=conn.openSession().find(hql);
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(conn!=null) conn.closeSession();
		}
		return cellList;
    }
    
    
    
    
    
    public static List getCellList(String templateId,String versionId,String reportFlg){
		List resList=null;
		
		if(templateId==null || versionId==null) 
			return resList;
		
		DBConn conn=null;
		
		try{
			String hql="";
			if(reportFlg.equals(Config.CBRC_REPORT)){
				hql="select distinct c.rowId,c.colId,c.cellId from MCell c where c.MChildReport.comp_id.childRepId='" +templateId+"' and " +
				" c.MChildReport.comp_id.versionId="+versionId+"";
			} else {
				hql="select distinct c.rowNum,c.colNum,c.cellId from AfCellinfo c where c.templateId='" +templateId+"' and " +
				" c.versionId="+versionId+"";
			}
			conn=new DBConn();
			resList=conn.openSession().find(hql);
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(conn!=null) conn.closeSession();
		}
		return resList;
    }
    
    /***
     * 已使用hibernate技术 卞以刚 2011-12-22
     * 影响对象：MCell || AfCellinfo
     * @param templateId
     * @param versionId
     * @param reportFlg
     * @return
     */
    public static List getCellNoSList(String templateId,String versionId,String reportFlg){
		List resList=null;
		
		if(templateId==null || versionId==null) 
			return resList;
		
		DBConn conn=null;
		
		try{
			String hql="";
			if(reportFlg.equals(Config.CBRC_REPORT)){
				hql="select distinct c.rowId,c.colId,c.cellId from MCell c where c.MChildReport.comp_id.childRepId='" +templateId+"' and " +
				" c.MChildReport.comp_id.versionId='"+versionId+"'";
			} else {
				hql="select distinct c.rowNum,c.colNum,c.cellId from AfCellinfo c where c.templateId='" +templateId+"' and " +
				" c.versionId='"+versionId+"'";//+" and c.colName not like '上期%'";
			}
			conn=new DBConn();
			resList=conn.openSession().find(hql);
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(conn!=null) conn.closeSession();
		}
		return resList;
    }
    
    /***
     * 已使用hibernate 卞以刚 2011-12-27
     * 影响对象： AfCellinfo
     * @param childRepId
     * @param versionId
     * @param pid
     * @return
     */
	public static boolean getCellId(String childRepId, String versionId,
			String[] pid) {
        if (childRepId == null || versionId == null || pid == null)
            return false;
        String colName = pid[0];
        String rpid = pid[1];
        DBConn conn = null;
        try {
            String hql = "select mc.cellId from AfCellinfo mc where mc.templateId='"
                    + childRepId
                    + "'"
                    + " and mc.versionId='"
                    + versionId
                    + "'" + " and mc.colNum='" + colName.trim() + "' and mc.cellPid='" + rpid.trim() + "' order by mc.cellId desc";
            	// System.out.println(hql);
            conn = new DBConn();

            List list = conn.openSession().find(hql);
            if (list != null && list.size() > 0) {
            	return true;
            } else {
            	return false;
            }
        } catch (HibernateException he) {
            log.printStackTrace(he);
            return false;
        } finally {
            if (conn != null)
                conn.closeSession();
        }

    }
	
	/**
	 * 判断在af_cellinfo表中是否存在填表人，或负责人这样的字符
	 * @param templateId
	 * @param upName
	 * @return
	 */
	public static boolean isUpPropertyName(String templateId,String upName){
		boolean res = false;
		 if (templateId == null || upName == null)
	            return false;
	        DBConn conn = null;
	        try {
	            String hql = "from AfCellinfo af where af.templateId=:templateId and af.colName =:upName or af.rowName=:rowName";
	            conn = new DBConn();
	            Session session = conn.openSession();
	            Query query = session.createQuery(hql)
	            	.setParameter("templateId", templateId)
	            	.setParameter("upName", upName)
	            	.setParameter("rowName", upName);
	            if(query.list()!=null && query.list().size()>0)
	            	res = true;
	        } catch (Exception e) {
	            log.printStackTrace(e);
	            e.printStackTrace();
	            return false;
	        } finally {
	            if (conn != null)
	                conn.closeSession();
	        }
		return res;
	}

	
	
	 /***
     * 查处单元格CellName
     * @param cellPid
     * @param colNum
     * @return String
     */
    public static String getCellName(String cellPid,String colNum){
		
    	String cellName = "";
    	
		if(StringUtil.isEmpty(cellPid)|| StringUtil.isEmpty(colNum)) 
			return null;
		
		DBConn conn=null;
		
		try{
			String hql="select c.cellName from AfCellinfo c where c.cellPid='" +cellPid+"' and " +
					" c.colNum='"+colNum+"'";
			conn=new DBConn();
			List list=conn.openSession().find(hql);
			
			if(list!=null && list.size()>0){
				
				cellName=(String)list.get(0);			
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(conn!=null) conn.closeSession();
		}
		return cellName;
    }
}
