package com.cbrc.smis.adapter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.form.JhbOrgForm;
import com.cbrc.smis.form.OrgForm;
import com.cbrc.smis.hibernate.Org;
import com.cbrc.smis.hibernate.OrgActuType;
import com.cbrc.smis.util.FitechException;
import com.fitech.net.form.OrgTypeForm;
import com.fitech.net.hibernate.OrgType;
/**
 * 频度类别机构表操作类
 * 
 * @author rds
 * @date 2006-02-06
 */
public class StrutsOrgDelegate {
	private static FitechException log=new FitechException(StrutsOrgDelegate.class);
	/**
	 * @author jhb
	 * @param orgid
	 * @return OrgForm
	 */
	public static com.cbrc.smis.form.OrgForm getOatId(String orgid) {
		OrgForm orgform = null;

		if (orgid == null)
			return null;

		DBConn conn = null;

		try {
			conn = new DBConn();
			Org org = (Org) conn.openSession().get(Org.class, orgid);
			if (org != null) {
				orgform = new OrgForm();
				TranslatorUtil.copyPersistenceToVo(org, orgform);
			}
		} catch (HibernateException he) {
			log.printStackTrace(he);
			orgform = null;
		} catch (Exception e) {
			log.printStackTrace(e);
			orgform = null;
		} finally {
			if (conn != null)
				conn.closeSession();
		}

		return orgform;
	}
	
	/**
	 * @author jhb
	 * @param orgid
	 * @return OrgForm
	 */
	public static List getOrg(String orgname) {
		List reslist=null;
		DBConn conn = null;

		try {
			String hql="from Org o where o.orgName='" + orgname + "'";
			conn=new DBConn();
			List list=conn.openSession().find(hql);
			if (list!=null && list.size()>0) {
				reslist=new ArrayList();
				for(int i=0;i<list.size();i++)
				{
					Org org=(Org)list.get(i);
					 JhbOrgForm orgform=new  JhbOrgForm();
					TranslatorUtil.copyPersistenceToVo(org,orgform);
					reslist.add(orgform);
				}
			}
		} catch (HibernateException he) {
			log.printStackTrace(he);
			
		} catch (Exception e) {
			log.printStackTrace(e);
		
		} finally {
			if (conn != null)
				conn.closeSession();
			
		}

		return reslist;
	}
	/**
	 * 根据机构类型获得所有的机构ID列表
	 * 
	 * @author rds
	 * @date 2006-02-07
	 * 
	 * @param orgClsId
	 *            String 机构类型ID
	 * @return List
	 */
	public static List getOrgIdsByOrgClsId(String orgClsId){
		ArrayList resList=null;
		
		if(orgClsId==null) return resList;
		
		DBConn conn=null;
		Session session=null;
		
		try{
			conn=new DBConn();
			session=conn.openSession();
			String hql="from Org o where o.orgClsId='" + orgClsId + "'";
			List list=session.find(hql);
			if(list!=null && list.size()>0){
				resList=new ArrayList();
				Org org=null;
				for(int i=0;i<list.size();i++){
					org=(Org)list.get(i);
					resList.add(org.getOrgId());
				}
			}
		}catch(HibernateException he){
			resList=null;
			log.printStackTrace(he);
		}catch(Exception e){
			resList=null;
			log.printStackTrace(e);
		}finally{
			if(conn!=null) conn.closeSession();
		}
		
		return resList;
	}
	
	/**
	 * 更新机构信息
	 * 
	 * @author rds
	 * @date 2006-02-07
	 * 
	 * @param orgForm OrgForm 
	 * @return boolean
	 */
	public static boolean update(OrgForm orgForm){
		boolean res=false;
		
		if(orgForm==null) return res;
		if(orgForm.getOrgId()==null) return res;
			
		DBConn conn=null;
		Session session=null;
		
		try{
			conn=new DBConn();
			session=conn.beginTransaction();
			
			Org org=(Org)session.get(Org.class,orgForm.getOrgId());
			if(org!=null){
				OrgActuType orgActuType=new OrgActuType();
				orgActuType.setOATId(orgForm.getOATId());
				org.setOrgActuType(orgActuType);
				session.update(org);
				res=true;
			}
		}catch(HibernateException he){
			res=false;
			log.printStackTrace(he);
		}catch(Exception e){
			res=false;
			log.printStackTrace(e);
		}finally{
			if(conn!=null) conn.endTransaction(res);
		}
		
		return res;
	}
	
	/**
	 * 批量更新机构信息
	 * 
	 * @author rds
	 * @date 2006-02-07
	 * 
	 * @param orgIds List 机构ID列表
	 * @param oatId Integer 频度类别ID
	 * @return boolean
	 */
	public static boolean updatePatch(List orgIds,Integer oatId){
		boolean res=true;
		
		if(orgIds==null) return res;
		if(orgIds.size()<=0) return res;
			
		DBConn conn=null;
		Session session=null;
		
		try{
			conn=new DBConn();
			session=conn.beginTransaction();
			
			for(int i=0;i<orgIds.size();i++){
				Org org=(Org)session.get(Org.class,(String)orgIds.get(i));
				
				if(org!=null){
					OrgActuType orgActuType=new OrgActuType();
					orgActuType.setOATId(oatId);
					org.setOrgActuType(orgActuType);
					session.update(org);
				}
			}
		}catch(HibernateException he){
			res=false;
			log.printStackTrace(he);
		}catch(Exception e){
			res=false;
			log.printStackTrace(e);
		}finally{
			if(conn!=null) conn.endTransaction(res);
		}
		
		return res;
	}
	/**
	 * 获取机构信息列表
	 * 
	 * @author rds
	 * @date 2006-02-06
	 * 
	 * @param orgForm OrgForm
	 * @param offset int 起始位置 
	 * @param limit int 偏移量
	 * @return List 
	 */
	public static List findAll(OrgForm orgForm,int offset,int limit){
		List results=null;
		
		DBConn conn=null;
		Session session=null;
		
		StringBuffer where=new StringBuffer("");
		
		try{
			if(orgForm!=null){
				if(orgForm.getOrgSrhName()!=null && !orgForm.getOrgSrhName().equals("")){
					where.append((where.toString().equals("")?"":" and ") + 
							" orgName='" + orgForm.getOrgSrhName() + "'");
				}
				if(orgForm.getOrgClsId()!=null && !orgForm.getOrgClsId().equals("")){
					where.append((where.toString().equals("")?"":" and ") + 
							" orgClsId='" + orgForm.getOrgClsId() + "'");
				}
			}
			
			conn=new DBConn();
			session=conn.openSession();
			
			String hql="from Org o" + (where.toString().equals("")?"":" where " + where.toString());
			
			Query query=session.createQuery(hql);
			query.setFirstResult(offset);
			query.setMaxResults(limit);
			List list=query.list();
			
			if(list!=null && list.size()>0){
				results=new ArrayList();
				Org org=null;
				for(int i=0;i<list.size();i++){
					org=(Org)list.get(i);
					OrgForm _orgForm=new OrgForm();
					TranslatorUtil.copyPersistenceToVo(org,_orgForm);
					results.add(_orgForm);
				}
			}
		}catch(HibernateException he){
			results=null;
			log.printStackTrace(he);
		}catch(Exception e){
			results=null;
			log.printStackTrace(e);
		}finally{
			if(conn!=null) conn.closeSession();
		}
		
		return results;
	}
	/**
	 * 获得机构总数
	 * 
	 * @author rds
	 * @date 2006-02-06
	 * 
	 * @param orgForm OrgForm
	 * @return int
	 */
	public static int getCount(OrgForm orgForm){
		int count=0;
		
		DBConn conn=null;
		Session session=null;
		
		StringBuffer where=new StringBuffer("");
		
		try{
			if(orgForm!=null){
				if(orgForm.getOrgSrhName()!=null && !orgForm.getOrgSrhName().equals("")){
					where.append((where.toString().equals("")?"":" and ") + 
							" orgName='" + orgForm.getOrgSrhName() + "'");
				}
				if(orgForm.getOrgClsId()!=null && !orgForm.getOrgClsId().equals("")){
					where.append((where.toString().equals("")?"":" and ") + 
							" orgClsId='" + orgForm.getOrgClsId() + "'");
				}
			}
			
			conn=new DBConn();
			session=conn.openSession();
						
			String hql="select count(*) from Org o" + (where.toString().equals("")?"":" where " + where.toString());
			
			List list=session.find(hql);
			if(list!=null && list.size()==1){
				count=((Integer)list.get(0)).intValue();
			}
		}catch(HibernateException he){
			count=0;
			log.printStackTrace(he);
		}catch(Exception e){
			count=0;
			log.printStackTrace(e);
		}finally{
			if(conn!=null) conn.closeSession();
		}
		
		return count;
	}
	
	/**
	 * 获取机构信息列表
	 * 
	 * @author rds
	 * @date 2006-02-06
	 * 
	 * @param orgForm OrgForm
	 * @param offset int 起始位置 
	 * @param limit int 偏移量
	 * @return List 
	 */
	
	
	/**
	 * 批量更新
	 * 
	 * @param mOrgs
	 * @return
	 */
	public static boolean createPatchMOrg(List mOrgs) {
		boolean result = false;
		
		if(mOrgs==null) return result;
		
		DBConn conn = null;
		Session session = null;
		
		try {
			conn = new DBConn();
			session = conn.beginTransaction();
				
			OrgForm orgForm=null;
			
			String hql="";
			for(int i=0;i<mOrgs.size();i++){
				orgForm=(OrgForm)mOrgs.get(i);
				Org mOrgPersistence = null;
				
				hql="from Org o where o.orgId='" + orgForm.getOrgId() + "'";
				List list=session.find(hql);
				Object object=null;
				if(list!=null && list.size()>0) object=list.get(0);
				
				if(object==null){
					mOrgPersistence=new Org();
					TranslatorUtil.copyVoToPersistence(mOrgPersistence, orgForm);
					session.save(mOrgPersistence);
				}else{
					mOrgPersistence=(Org)object;
					TranslatorUtil.copyVoToPersistence(mOrgPersistence, orgForm);
					session.update(mOrgPersistence);
				}								
				session.flush();
				
				result = true;
			}
		} catch (Exception e) {
			result = false;
			log.printStackTrace(e);
		} finally {
			if (conn != null) conn.endTransaction(result);
		}
		
		return result;
	}
	/**
	 * 取得所有机构记录
	 *	
	 * @author rds 
	 * @exception   Exception   If the com.cbrc.org.form.MOrgForm objects cannot be retrieved.
	 */
	 public static List findAll () throws Exception {
		List retVals = null;
		      
		DBConn conn=null;
		      
		try{
			conn=new DBConn();
			List list=conn.openSession().find("from Org");

			if(list!=null && list.size()>0){
		    	retVals=new ArrayList();
		    	for(int i=0;i<list.size();i++){
		    		Org org = (Org)list.get(i);
		    		OrgForm orgForm = new OrgForm();
		    		TranslatorUtil.copyPersistenceToVo(org,orgForm);
		    		retVals.add(orgForm);
		    	}
		    }
		}catch(HibernateException he){
		    log.printStackTrace(he);
		}finally{
		    if(conn!=null) conn.closeSession();
		}
		return retVals;
	 }
	 
	 /**
		 * 取得机构级别
		 *	
		 * @author rds 
		 * @exception   Exception   If the com.cbrc.org.form.MOrgForm objects cannot be retrieved.
		 */
		 public static List findOrgLevel () throws Exception {
			List retVals = null;
			DBConn conn=null;
			      
			try{
				conn=new DBConn();
				List list=conn.openSession().find("from OrgType ot order by ot.orgTypeId");

				if(list!=null && list.size()>0){
			    	retVals=new ArrayList();
			    	for(int i=0;i<list.size();i++){
			    		OrgType orgType = (OrgType)list.get(i);
			    		OrgTypeForm orgTypeForm = new OrgTypeForm();
			    		TranslatorUtil.copyPersistenceToVo(orgType,orgTypeForm);
			    		retVals.add(orgTypeForm);
			    	}
			    }
			}catch(HibernateException he){
			    log.printStackTrace(he);
			}finally{
			    if(conn!=null) conn.closeSession();
			}
			return retVals;
		 }
         
         /**
          * 查找当前机构的下级机构
          *@author gongming
          *@date    2007-10-11
          * @param orgId    String      本级机构Id
          * @return List
          * @throws Exception
          */
         public static List findSubOrgs(String orgId) 
         {
            List orgLst = null;
            DBConn conn=null;
            Session session = null;    
            try{
                if(orgId != null){
                conn=new DBConn();
                session = conn.openSession();
                String hql = "from OrgNet t where t.preOrgId ='" + orgId+"'";
                orgLst = session.createQuery(hql).list();
               }
            }catch(HibernateException he){
                log.printStackTrace(he);
            }finally{
                if(conn!=null) conn.closeSession();
            }
            return orgLst;
         }
         /**
          * 获取机构名称
          * @param orgId
          * @return
          */
         public static String getOrgName(String orgId){
     		DBConn conn=null;
     		Session session=null;
     		String result = "";
     		try{
     			conn=new DBConn();
     			session=conn.openSession();
     			Connection con = session.connection();
     			Statement stat = con.createStatement();
     			String sql = "select org_name from org where org_id='" + orgId + "'";
     			ResultSet rs = stat.executeQuery(sql);
     			if(rs.next())
     				result = rs.getString("org_name");
     		}catch(HibernateException he){
     			log.printStackTrace(he);
     		}catch(Exception e){
     			log.printStackTrace(e);
     		}finally{
     			if(conn!=null) conn.closeSession();
     		}
     		return result;
     	}
}
