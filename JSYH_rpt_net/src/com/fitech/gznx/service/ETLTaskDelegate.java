package com.fitech.gznx.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
		/**���ǵ��ձ�����������˲�����ô�ж�
		//�����ڰ�����Ƶ��ת��ʵ����ĩ������
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
		}*/
		try {
			if(DateUtil.getFormatDate(DateUtil.dateAdd(date,1),"yyyy-MM-dd").getTime()>new Date().getTime()){//�жϴ���ʱ����Ƿ���ڵ�ǰʱ��
				return false;
			}
			connFactory = new FitechConnection();
			connection = connFactory.getConn();
			state = connection.createStatement();
			String selectHQL = "select t.task_id from ETL_TASK_MONI t where t.exec_flag = 1 and t.over_flag = 0 and t.task_term ='"+date+"'";
			selectHQL+="and not exists (select 1 from ETL_TASK_INFO e where INSTR(task_name, 'KHFX') = 1 and e.task_id = t.task_id)";//���οͻ����յ�����
//			System.out.println("selectHQL="+selectHQL);
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
				if(connection!=null&&!connection.isClosed()){
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
