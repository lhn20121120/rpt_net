package config;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;

public class TestSQL {
	DBConn dbConn = null;
	Session session = null;
	Connection conn = null;
	Statement state = null;
	ResultSet rs = null;
	public static void main(String[] args) {
		TestSQL t = new TestSQL();
		try {
			t.test();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void test() throws Exception{
		try {
			dbConn = new DBConn();
			session = dbConn.openSession();
			conn = session.connection();
			state = conn.createStatement();
			String s = "select case when 0>1 then 1 else 0 end from dual";
			rs = state.executeQuery(s);
			
			if(rs.next())
				System.out.println(rs.getObject(1).toString());
		} catch (Exception e) {
			
			e.printStackTrace();
			throw e;
		}finally{
			try {
				if(rs!=null){
					rs.close();
					rs = null;
				}
				if(state!=null){
					state.close();
					state = null;
				}
				if(conn!=null && !conn.isClosed()){
					conn.close();
					conn = null;
				}
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
			if(session!=null){
				dbConn.closeSession();
			}
			if(dbConn!=null){
				dbConn = null;
			}
		}
	}
}
