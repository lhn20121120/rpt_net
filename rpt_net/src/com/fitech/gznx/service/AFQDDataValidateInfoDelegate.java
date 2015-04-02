package com.fitech.gznx.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.form.DataValidateInfoForm;
import com.cbrc.smis.util.FitechException;

public class AFQDDataValidateInfoDelegate {

	private static FitechException log = new FitechException(
			AFQDDataValidateInfoDelegate.class);
	
	/**
	 * 查询所有的校验信息
	 * 
	 * @author rds 
	 * @date 2006-01-02 23:14
	 * 
	 * @param repInId Integer 实际数据表ID
	 * @param validateTypeId 校验类别ID
	 * @return List
	 */
	public static List find(Integer repInId){
		List resList=null;
		
		if(repInId==null) return resList;
				
		DBConn conn=null;
		//create
		Session session = null;
		Connection connection = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try{
			conn=new DBConn();
			
			session = conn.openSession();
			connection = session.connection();
			stmt = connection.createStatement();

			StringBuffer sql = null;

			sql = new StringBuffer();
			sql.append("select m.formula_name,m.validate_type_id,t.result,t.cause,")
				.append("t.source_value,t.target_value ")
				.append("from af_qd_datavalidateinfo t,af_validateformula m ")
				.append("where t.FORMULA_ID=m.FORMULA_ID and t.rep_id=")
				.append(repInId);
			
			rs = stmt.executeQuery(sql.toString().toUpperCase());

			resList = new ArrayList();

			while (rs.next()) {
//				QDDataValidateInfoForm form = new QDDataValidateInfoForm();
				DataValidateInfoForm form = new DataValidateInfoForm();
				form.setCellFormuView(rs.getString(1));
				form.setValidateTypeId(rs.getInt(2));
				form.setValidateTypeName(rs.getInt(2) == 1 ? "表内校验" : "表间校验");
				form.setResult(Integer.valueOf(rs.getInt(3)));
				form.setCause(rs.getString(4));
				
				form.setSourceValue(rs.getString(5));
				form.setTargetValue(rs.getString(6));
	
				resList.add(form);
			}

		}catch(HibernateException he){
			resList=null;
			log.printStackTrace(he);
		}catch(Exception e){
			resList=null;
			log.printStackTrace(e);
		}finally{
			if(conn!=null) conn.closeSession();
		}
		
		return resList;
	}
	
}
