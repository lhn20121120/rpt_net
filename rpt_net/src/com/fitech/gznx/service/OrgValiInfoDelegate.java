package com.fitech.gznx.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.util.FitechException;
import com.fitech.gznx.po.AfReport;
import com.fitech.gznx.po.OrgValiInfo;

public class OrgValiInfoDelegate {

	private static FitechException log = new FitechException(
			OrgValiInfoDelegate.class);

	/**
	 * 插入机构校验结果
	 * 2012-09-19
	 * @author Nick
	 * @param CollectCustomForm
	 *            包含要插入的基本信息
	 * @return boolean 是否插入成功
	 * @throws SQLException 
	 */
	public static boolean add(List<Map<String,String>> list,AfReport reportIn,String id ) throws SQLException {

		boolean result = false;
		DBConn conn = null;
		Session session = null;
		
		if ( list.size()==0) {
			return false;
		}else{
			result=delete(id);
			System.out.println(result);
		}
		try {
			conn = new DBConn();
			session = conn.beginTransaction();

			OrgValiInfo cr = null; 

			for (int i = 0; i < list.size(); i++) {
				cr = new OrgValiInfo(); 
				cr.setRep_id(id);
				cr.setTemplate_id(list.get(i).get("template_id")); 
				cr.setCell_name(list.get(i).get("cell_name"));
				cr.setOrg_id(list.get(i).get("org_id"));
				cr.setCell_data(list.get(i).get("cell_data"));
				cr.setOrg_name(list.get(i).get("org_name"));
				cr.setRol_name(list.get(i).get("name"));
				if(id.equals(list.get(i).get("rep_id"))){
					cr.setFlag("1");
				}else{
					cr.setFlag("0");
				}
				// 保存校验关系
				session.save(cr);
				session.flush();
			}
			session.flush();
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			if (conn != null)
				conn.endTransaction(result);
		}
		return result;
	}

	/**
	 * 删除校验结果
	 * 
	 * @author Nick
	 * @param orgId
	 *            String 删除校验关系的rep_id
	 * @return boolean 是否删除成功
	 * @throws SQLException 
	 */
	public static boolean delete(String ids) throws SQLException {

		boolean result = true;
		DBConn conn = null;
		Session session = null;
		try {
			conn = new DBConn();
			session = conn.beginTransaction();
			String[] id=ids.split(",");
			for(int i=0;i<id.length;i++){
			String hql = "from OrgValiInfo cr where cr.rep_id=?";
			Query query = session.createQuery(hql);
			query.setString(0, id[i]);
			Iterator iter = query.iterate();

			while (iter.hasNext()) {
				OrgValiInfo cr = (OrgValiInfo) iter.next();
				session.delete(cr);
			}
			session.flush();
			}
		} catch (Exception e) {
			log.printStackTrace(e);
			result = false;
		} finally {
			if (conn != null)
				conn.endTransaction(result);
		}
		return result;
	}
	/**
	 * 查询校验关系
	 * 
	 * @author Nick
	 * @param orgId
	 * @throws SQLException 
	 */
	public static List<OrgValiInfo> getValiResults(String ids) throws SQLException {
		List<OrgValiInfo> list=new ArrayList<OrgValiInfo>(); 
		List l=new ArrayList();
		boolean result = true;
		DBConn conn = null;
		Session session = null;
		try {
			conn = new DBConn();
			session = conn.beginTransaction();
			String[] id=ids.split(",");
			for(int i=0;i<id.length;i++){
			String hql = " from OrgValiInfo cr where cr.rep_id='"+id[i]+"' order by cr.template_id,cr.cell_name,cr.org_id";
			Query query = session.createQuery(hql); 
			l=query.list();
			for(int j=0;j<l.size();j++){
				OrgValiInfo o=(OrgValiInfo)l.get(j); 
				list.add(o);
			} 
			}
		} catch (Exception e) {
			log.printStackTrace(e);
			result = false;
		} finally {
			if (conn != null)
				conn.endTransaction(result);
		}
		return list;
	}
}
