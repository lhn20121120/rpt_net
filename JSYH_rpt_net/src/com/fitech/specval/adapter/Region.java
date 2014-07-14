package com.fitech.specval.adapter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.jdbc.FitechConnection;


public class Region {
	public static List getRegionid(){
		List list = new ArrayList();
		Connection conn = null;
		PreparedStatement pStmt = null;
		ResultSet rs = null; 
		String re = null;
		try {
			conn = new com.cbrc.smis.proc.jdbc.FitechConnection().getConnect();
			String sql = "select distinct substr(region_id,1,2) as re from v_region t";
			pStmt = conn.prepareStatement(sql);
			rs = pStmt.executeQuery();
			while(rs.next()){
				re = rs.getString("re");
				list.add(re);
			}
		} catch (Exception e) {
			list = null;
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
		}
		return list;
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List list = getRegionid();
		for(Iterator it = list.iterator();it.hasNext();){
			String s = (String)it.next();
			System.out.println(s);
		}
		
		

	}

}
