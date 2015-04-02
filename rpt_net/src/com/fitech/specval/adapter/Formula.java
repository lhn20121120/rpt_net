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


public class Formula {
	public static List getFormula(){
		List list = new ArrayList();
		Connection conn = null;
		PreparedStatement pStmt = null;
		ResultSet rs = null; 
		try {
			conn = new com.cbrc.smis.proc.jdbc.FitechConnection().getConnect();
			String sql = "select t.spe_val_id,t.val_formula from spec_validate_info t";
			pStmt = conn.prepareStatement(sql);
			rs = pStmt.executeQuery();
			while(rs.next()){
				Mula mula = new Mula();
				mula.setId(rs.getInt("spe_val_id"));
				mula.setFormula(rs.getString("val_formula"));
				
				list.add(mula);
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
		List list = getFormula();
		Mula mula = new Mula();
		for(Iterator it = list.iterator();it.hasNext();){
			mula = (Mula)it.next();
			System.out.println(mula.getId()+","+mula.getFormula());
		}
		
		

	}

}
