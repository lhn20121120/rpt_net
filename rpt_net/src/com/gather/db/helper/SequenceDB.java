package com.gather.db.helper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.auth.adapter.TranslatorUtil;
import com.cbrc.auth.form.OperatorForm;
import com.cbrc.auth.hibernate.Operator;
import com.cbrc.smis.adapter.StrutsMChildReportDelegate;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.util.FitechException;
import com.fitech.gznx.po.AfOrg;
import com.cbrc.smis.common.Config;


public class SequenceDB {
	
	private static FitechException log = new FitechException(StrutsMChildReportDelegate.class);
	
	public static void batchInitSequense(String str){
		Connection con = null;
		Statement st;
		SequenceDB  c = new SequenceDB();
		List list = new ArrayList();
		DBConn dbconn = new DBConn();
		String[] s = str.split(";");
		try {
			Session session = dbconn.openSession();
			con = session.connection();
			st = con.createStatement();
			for(int k=0;k<s.length;k++){
				String[] strs = s[k].split(",");
				int i = c.getSequenceNextval(st,strs[1], strs[0]);
				c.createOrDropSequence(st,strs[0], i+1);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			dbconn.closeSession();
		}
	}
	public Integer getSequenceNextval(Statement st,String id,String name){
		int count =0;
		String sql="select max("+id+") from "+name+"";
		try {
			ResultSet set =st.executeQuery(sql);
			while(set.next()){
				count = set.getInt(1);
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return count;
	}
	public void createOrDropSequence(Statement st,String name,Integer minvalue){
		String sql1 = "drop sequence seq_"+name;
		String sql2="";
		if(Config.DB_SERVER_TYPE.equals("db2")){
			 sql2 ="create sequence seq_"+name+" start with "+minvalue+" increment by 1 no maxvalue no cycle cache 20";
		}
		if(Config.DB_SERVER_TYPE.equals("oracle")){
			 sql2 = "create sequence seq_"+name+" minvalue 1 maxvalue 100000000 start with "+minvalue+" increment by 1 cache 20";
		}
		try {
			st.execute(sql1);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
		}
		try {
			st.execute(sql2);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void searchOrgId(String orgId){
		if(orgId==null || "".equals(orgId))
			return;
		DBConn conn = null;
		Session session = null;
		
		try {
			conn = new DBConn();
			session = conn.openSession();

			String hql = "select a from AfOrg a where a.preOrgId='0' and a.orgLevel=1 and a.isCollect=0";

			Query query = session.createQuery(hql);

			AfOrg org = (AfOrg)query.uniqueResult();
			
			if(org!=null){
				if(!orgId.equals(org.getOrgId())){
					updateOrgId(org.getOrgId(),orgId);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.closeSession();
			}
		}
	}
	
	public static boolean updateOrgId(String oldOrgId,String newOrgId){
		boolean result = false;
		DBConn db = null;
		Connection conn = null;
		Statement st = null;
		try {
			db = new DBConn();
			conn = db.beginTransaction().connection(); 
			conn.setAutoCommit(false);
			st = conn.createStatement();

			String sql = "update af_org set org_id='"+newOrgId+"' where org_id='"+oldOrgId+"'";
			st.addBatch(sql);
			
			sql = "update org set org_id='"+newOrgId+"' where org_id='"+oldOrgId+"'";
			st.addBatch(sql);
			

			sql = "update operator set org_id='"+newOrgId+"',set_org_id='"+newOrgId+"' where org_id='"+oldOrgId+"' and set_org_id='"+oldOrgId+"'";
			st.addBatch(sql);
			

			sql = "update department set org_id='"+newOrgId+"' where org_id='"+oldOrgId+"'";
			st.addBatch(sql);
			

			sql = "update m_user_grp set set_org_id='"+newOrgId+"' where set_org_id='"+oldOrgId+"'";
			st.addBatch(sql);
			

			sql = "update role set set_org_id='"+newOrgId+"' where set_org_id='"+oldOrgId+"'";
			st.addBatch(sql);
			

			sql = "update m_region set set_org_id='"+newOrgId+"' where set_org_id='"+oldOrgId+"'";
			st.addBatch(sql);
			
			int[] res = st.executeBatch();
			result=true;
			
			if(result)
				conn.commit();
		} catch (Exception e) {
			result = false;
			try {
				if(conn!=null){
					conn.rollback();
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			log.printStackTrace(e);
		} finally {
			try {
				if(st!=null)
					st.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(conn!=null)
					conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (db != null) db.closeSession();
		}
		return result;
	}
	
	
	public static void main(String[] args) {
		System.out.println(11);
	}
}
