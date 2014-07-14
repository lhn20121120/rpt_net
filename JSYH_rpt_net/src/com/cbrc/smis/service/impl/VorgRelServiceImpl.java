package com.cbrc.smis.service.impl;


import java.util.ArrayList;
import java.util.List;

import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.org.entity.SysFlag;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.service.IVorgRelService;
import com.fitech.gznx.po.vOrgRel;

public class VorgRelServiceImpl implements IVorgRelService {

	@Override
	public vOrgRel findVOrgRelByOrgId(String orgId) throws Exception {
		DBConn conn=null;
    	Session session=null;
    	conn = new DBConn();
    	session = conn.openSession();
    	String hql = "from vOrgRel v where v.id.orgId='"+orgId+"'";
    	vOrgRel orgRel = null;
		try {
			Object obj = session.createQuery(hql).uniqueResult();
			if(obj!=null)
				orgRel = (vOrgRel)obj;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(conn!=null)
				conn.closeSession();
		}
    	
		return orgRel;
	}

	@Override
	public void saveVorgRel(vOrgRel rel) throws Exception {
		DBConn conn=null;
    	Session session=null;
    	conn = new DBConn();
    	session = conn.openSession();
    	conn.beginTransaction();
    	try {
			session.save(rel);
			conn.endTransaction(true);
		} catch (Exception e) {
			conn.endTransaction(false);
			e.printStackTrace();
		}finally{
			if(conn!=null)
				conn.closeSession();
		}
	}
	
	@Override
	public List<vOrgRel> searchPreRelList(String orgId) throws Exception{
		DBConn conn=null;
    	Session session=null;
    	conn = new DBConn();
    	session = conn.openSession();
    	
    	List<vOrgRel> relSetList = null;
		//�����������ӳ����Ϣ
		String hql = "from vOrgRel v where v.id.orgId!='"+orgId+"'";
		List<vOrgRel> relList = session.createQuery(hql).list();
		
		//���û����������õ������ϼ�������ӽ�����
		relSetList = new ArrayList<vOrgRel>();//ʵ����
		for(vOrgRel v : relList){//��������
			if(v.getPreOrgid()==null || v.getPreOrgid().equals("") || v.getPreOrgid()=="0")
				relSetList.add(v);//����������ֱ����ӽ�����
			else{
				VorgRelServiceImpl service = new VorgRelServiceImpl();
				//�жϻ����Ƿ����������
				boolean canSet = service.canSetOrgRel(session, v.getPreOrgid(), orgId);
				if(canSet)//��������ӽ�����
					relSetList.add(v);
			}
			
		}
		return relSetList;
	}
	
	/***
	 * ���ݵ�����������ѯ�û����������õ��ϼ���������ȷ���ϼ���������������
	 * @param session ������
	 * @param orgId ���ڲ�ѯ�Ļ���ID
	 * @param checkOrgId ���ڱȽϵĻ���ID
	 * @return
	 * @throws Exception
	 */
	private boolean canSetOrgRel(Session session,String orgId,String checkOrgId) throws Exception{
		if(orgId.equals(checkOrgId))
			return false;
		String hql = "from vOrgRel v where v.id.orgId='"+orgId+"'";
		vOrgRel rel = null;
		try {
			rel = (vOrgRel)session.createQuery(hql).uniqueResult();
		} catch (Exception e) {
			return false;
		}
		
		if(rel!=null && rel.getPreOrgid()!=null && !rel.getPreOrgid().equals("") && rel.getPreOrgid()!="0")
			return canSetOrgRel(session, rel.getPreOrgid(),checkOrgId);
		
		return true;
	}

	@Override
	public List<vOrgRel> findVorgRelAll() throws Exception {
		DBConn conn=null;
    	Session session=null;
    	conn = new DBConn();
    	session = conn.openSession();
    	
		String hql = "from vOrgRel";
		List<vOrgRel> relList = session.createQuery(hql).list();
		
		conn.closeSession();
		return relList;
	}

	@Override
	public void updateVorgRel(vOrgRel rel) throws Exception {
		DBConn conn=null;
    	Session session=null;
    	conn = new DBConn();
    	session = conn.openSession();
    	conn.beginTransaction();
    	try {
			session.update(rel);
			conn.endTransaction(true);
		} catch (Exception e) {
			conn.endTransaction(false);
			e.printStackTrace();
		}finally{
				
			if(conn!=null)
				conn=null;
		}
		
	}

	@Override
	public List<vOrgRel> findVorgRelAll(int pageNo, int pageSize)
			throws Exception {
		DBConn conn=null;
    	Session session=null;
    	conn = new DBConn();
    	session = conn.openSession();
    	
		String hql = "from vOrgRel";
		List<vOrgRel> relList = null;
		Query query = session.createQuery(hql);
		query.setFirstResult(pageNo);
		query.setMaxResults(pageSize);
		
		relList = query.list();
		conn.closeSession();
		return relList;
	}

	@Override
	public int selectCount() throws Exception {
		int result =0;
		String hql = "select count(*) from vOrgRel";
		DBConn conn=null;
    	Session session=null;
    	conn = new DBConn();
    	session = conn.openSession();
    	Query query = session.createQuery(hql);
    	List list = query.list();
    	if(list!=null && list.size()>0)
    		 result = ((Integer)list.get(0)).intValue();
    	
    	conn.closeSession();
		return result;
	}

	@Override
	public void deleteVorgRel(vOrgRel rel) throws Exception {
		DBConn conn=null;
    	Session session=null;
    	conn = new DBConn();
    	session = conn.openSession();
    	conn.beginTransaction();
    	try {
			if(rel!=null)
				session.delete(rel);
			conn.endTransaction(true);
		} catch (Exception e) {
			conn.endTransaction(false);
			e.printStackTrace();
		}finally{
			if(conn!=null)
				conn=null;
			
		}
	}

	@Override
	public List<SysFlag> findAllSysFlag() throws Exception {
		DBConn conn=null;
    	Session session=null;
    	conn = new DBConn();
    	session = conn.openSession();
    	String hql = "from SysFlag";
    	List<SysFlag> flagList = null;
    	try {
    		flagList = session.createQuery(hql).list();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(conn!=null)
				conn.closeSession();
		}
    	return flagList;
    	
	}
	
	
}
