package com.fitech.gznx.service;

import java.util.ArrayList;
import java.util.List;

import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.fitech.gznx.po.AfReportForceRep;
import com.fitech.gznx.po.AfReportForceRepId;

/**
 * ǿ���ϱ�������
 * @author ������
 *	2013-06-20
 */
public class AFReportForceService {
	
	private DBConn dbconn;
	private Session session;

	/**
	 * ���Ҫǿ���ϱ��ı���
	 * @param afReportForce 
	 * @throws Exception
	 */
	public void addAFReportForce(AfReportForceRep forceRep) throws Exception{
		if(forceRep == null){
			throw new Exception("��ѡ�б���!");
		}
		List<AfReportForceRep> list =findAFReportForce(forceRep.getId().getRepId(),forceRep.getId().getForceTypeId());
		open();  //�����ӳ�
		if(list.size()>0){
			session.saveOrUpdate(forceRep);
		}else{
			session.save(forceRep);
		}
		dbconn.endTransaction(true);
		dbconn.closeSession();
	}
	/**
	 * ��ѯ�˱����Ƿ��Ѿ�ǿ���ϱ���
	 * @param repId  ������
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
	 * ɾ��Ҫǿ���ϱ��ı���
	 * @param repId
	 * @throws Exception
	 */
	public void deleteAFReportForce(Long repId,Long typeId) throws Exception{
		if(repId == null){
			throw new Exception("��ѡ�еı���!");
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
	 * �����ݿ�����
	 */
	public void open(){
		dbconn = new DBConn();
		session = dbconn.beginTransaction();
	}
}
