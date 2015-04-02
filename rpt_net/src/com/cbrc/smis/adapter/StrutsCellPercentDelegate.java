package com.cbrc.smis.adapter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.util.FitechException;
/**
 * 报表的百分比设置表操作类
 * 
 * @author rds
 * @date 2006-02-21
 */
public class StrutsCellPercentDelegate {
	private static FitechException log=new FitechException(StrutsCellPercentDelegate.class);
	
	/**
	 * 根据子报表ID和版本号获得需将百分数转换成小数的列或单元格名称列表
	 * 
	 * @auther rds
	 * @date 2006-02-21
	 * 
	 * @param childRepId String 子报表ID
	 * @param versionId String 版本号
	 * @return List
	 */
	public static List getPercentCellOrCol(String childRepId,String versionId){
		ArrayList resList=null;
		
		if(childRepId==null || versionId==null) return resList;
		
		DBConn conn=null;
		Connection _conn=null;
		
		try{
			conn=new DBConn();
			Session session=conn.openSession();
			_conn=session.connection();
			String hql="select cell_name from cell_percent where child_rep_id='".toUpperCase() + 
				childRepId + "' and version_id='".toUpperCase() + versionId + "'";
			PreparedStatement pstmt=_conn.prepareStatement(hql);
			ResultSet rs=pstmt.executeQuery();
			if(rs!=null){
				resList=new ArrayList();
				String cellName="";
				while(rs.next()){
					cellName=rs.getString("cell_name".toUpperCase());
					if(cellName!=null && !cellName.equals("")) resList.add(cellName.toUpperCase());
				}
			}
			rs.close();
			pstmt.close();
		}catch(HibernateException he){
			resList=null;
			log.printStackTrace(he);
		}catch(Exception e){
			resList=null;
			log.printStackTrace(e);
		}finally{
			if(_conn!=null)
				try{
					_conn.close();
				}catch(Exception e){}
			if(conn!=null) conn.closeSession();
		}
		
		return resList;
	}
}
