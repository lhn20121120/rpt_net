package com.cbrc.auth.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class JdbcBaseDao {

	/** 连接对象 */
	protected Connection con;
	/** 预编译 */
	protected PreparedStatement ps;
	/** 结果集 */
	protected ResultSet rs;
	/** 资源文件对象 */
	private static Properties prop = new Properties();

	
	public Connection getCon() {
		this.getConn();
		return con;
	}
	public void setCon(Connection con) {
		this.con = con;
	}
	public void getConn() {
		//add by wmm,将数据库的连接改为数据源
		
		String portalJNDI="";
		String server_type="";
		Context envContext=null;
		DataSource ds=null;
		try {
			prop.load(getClass().getClassLoader().getResourceAsStream("application.properties"));
			portalJNDI=prop.getProperty("portal_jndi");
			server_type=prop.getProperty("server_type");
			Context initContext = new InitialContext();
			if("tomcat".equalsIgnoreCase(server_type)){
				ds=(DataSource)((Context)initContext.lookup("java:/comp/env")).lookup(portalJNDI);
			}else{
				
				ds = (DataSource)initContext.lookup(portalJNDI);
			}
			
	        // 通过数据源对象获得一个连接  
            this.con = ds.getConnection();  
//			Class.forName(prop.getProperty("portal_dirver"));
//			this.con = DriverManager.getConnection(prop.getProperty("portal_url"), prop
//					.getProperty("portal_username"), prop.getProperty("portal_pwd"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 关闭数据库连接
	 * 
	 * @throws SQLException
	 *             数据库异常
	 * 
	 */
	protected void close() {
		try {
			if (rs != null) {
				rs.close();
			}
			if (ps != null) {
				ps.close();
			}
			if (con != null) {
				con.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
