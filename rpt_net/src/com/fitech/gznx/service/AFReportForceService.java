package com.fitech.gznx.service;

import java.util.ArrayList;
import java.util.List;

import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.fitech.gznx.po.AfReportForceRep;
import com.fitech.gznx.po.AfReportForceRepId;

/**
 * 强制上报操作类
 * @author 姜明青
 *	2013-06-20
 */
public class AFReportForceService {
	
	private DBConn dbconn;
	private Session session;

	/**
	 * 添加要强制上报的报表
	 * @param afReportForce 
	 * @throws Exception
	 */
	public void addAFReportForce(AfReportForceRep forceRep) throws Exception{
		if(forceRep == null){
			throw new Exception("无选中报表!");
		}
		List<AfReportForceRep> list =findAFReportForce(forceRep.getId().getRepId(),forceRep.getId().getForceTypeId());
		open();  //打开连接池
		if(list.size()>0){
			session.saveOrUpdate(forceRep);
		}else{
			session.save(forceRep);
		}
		dbconn.endTransaction(true);
		dbconn.closeSession();
	}
	/**
	 * 查询此报表是否已经强制上报了
	 * @param repId  报表编号
	 * @return
	 * @throws Exception
	 */
	public List<AfReportForceRep> findAFReportForce(Long repId,Long typeId) throws Exception{
		if(repId == null){
			return null;
		}
		List<AfReportForceRep> list = new ArrayList<AfReportForceRep>();
		dbconn = new DBConn();
		session = dbconn.openSession();
		try {
			StringBuffer hql = new StringBuffer();
			 hql.append("FROM AfReportForceRep a WHERE a.id.repId ="+repId);
			if(typeId !=null){
				hql.append(" and a.id.forceTypeId = "+typeId);
			}
			Query query = session.createQuery(hql.toString());
			list = query.list(); 
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			dbconn.closeSession();
		}
		return list;
	}
	/**
	 * 删除要强制上报的报表
	 * @param repId
	 * @throws Exception
	 */
	public void deleteAFReportForce(Long repId,Long typeId) throws Exception{
		if(repId == null){
			throw new Exception("无选中的报表!");
		}
		AfReportForceRep force = new AfReportForceRep(new AfReportForceRepId());
		force.getId().setRepId(repId);
		force.getId().setForceTypeId(typeId);
		try {
			open();
			session.delete(force);
			dbconn.endTransaction(true);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			dbconn.closeSession();
		}
	}
	/**
	 * 与数据库连接
	 */
	public void open(){
		dbconn = new DBConn();
		session = dbconn.beginTransaction();
	}
}
