package com.fitech.model.worktask.service.impl;

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

import org.hibernate.HibernateException;
import org.hibernate.Session;
import com.fitech.framework.core.service.impl.DefaultBaseService;
import com.fitech.model.worktask.model.pojo.WorkTaskInfo;
import com.fitech.model.worktask.service.ISequence;


public class SequenceServiceImpl extends DefaultBaseService<WorkTaskInfo, String> implements ISequence {

	@Override
	public void batchInitSequense(String str) {
		SequenceServiceImpl c = new SequenceServiceImpl();
		Session session = this.objectDao.getHibernateTemplate().getSessionFactory().getCurrentSession();
		Connection con = null;
		Statement st=null;
		List list = new ArrayList();
		String[] s = str.split(";");
		try {
			con = session.connection();
			st = con.createStatement();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		for(int k=0;k<s.length;k++){
			String[] strs = s[k].split(",");
			int i = c.getSequenceNextval(st,strs[1], strs[0]);
			c.createOrDropSequence(st,strs[0], i+1);
		}
	}

	@Override
	public void createOrDropSequence(Statement st, String name, Integer minvalue) {
		String sql1 = "drop sequence seq_"+name;
		String sql2 = "create sequence seq_"+name+" minvalue 1 maxvalue 100000000 start with "+minvalue+" increment by 1 cache 20";
		try {
			st.executeQuery(sql1);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			st.executeQuery(sql2);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Integer getSequenceNextval(Statement st, String id, String name) {
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
	
}
