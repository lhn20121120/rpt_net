package com.fitech.gznx.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.cbrc.smis.jdbc.FitechConnection;
import com.fitech.gznx.common.DateUtil;

public class ETLTaskDelegate {
	public static  boolean isOver(String  date){
		boolean flag = true ;
		FitechConnection connFactory = null;
		//DBConn conn = null;
		Statement state = null;
		ResultSet rs = null;
		Connection connection = null;
		if (date == null || date.equals("")) {
			
			flag = false;
			return flag;
		}
		//把日期按照月频度转成实际月末的日期
		date = DateUtil.getFreqDateLast(date,1);
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
		Date time = null ,Ddate= null;
		try {
			Ddate = format.parse(date);
			time= format.parse(format.format(new Date()));
		} catch (Exception e) {
		   e.printStackTrace();
		}
		if(Ddate.compareTo(time)>0){
			flag = false;
			return flag;
		}
		try {
			connFactory = new FitechConnection();
			connection = connFactory.getConn();
			state = connection.createStatement();
			String selectHQL = "select t.task_id from ETL_TASK_MONI t where t.exec_flag = 1 and t.over_flag = 0 and t.task_term ='"+date+"'";
			System.out.println("selectHQL="+selectHQL);
			rs = state.executeQuery(selectHQL);
			if (rs != null && rs.next()) {
				flag = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			try {
				if(rs!=null){
					rs.close();
					rs = null;
				}
				if(state!=null){
					state.close();
					state = null;
				}
				if(!connection.isClosed()){
					connection.close();
					connection = null;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return flag;
		
	}
}
