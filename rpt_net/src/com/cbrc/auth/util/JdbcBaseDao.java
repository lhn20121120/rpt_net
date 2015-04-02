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

	/** ���Ӷ��� */
	protected Connection con;
	/** Ԥ���� */
	protected PreparedStatement ps;
	/** ����� */
	protected ResultSet rs;
	/** ��Դ�ļ����� */
	private static Properties prop = new Properties();

	
	public Connection getCon() {
		this.getConn();
		return con;
	}
	public void setCon(Connection con) {
		this.con = con;
	}
	public void getConn() {
		//add by wmm,�����ݿ�����Ӹ�Ϊ����Դ
		
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
			
	        // ͨ������Դ������һ������  
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
	 * �ر����ݿ�����
	 * 
	 * @throws SQLException
	 *             ���ݿ��쳣
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
