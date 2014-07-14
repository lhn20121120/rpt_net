
package com.cbrc.fitech.org;

import java.util.ArrayList;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import com.cbrc.org.form.MOrgForm;
import com.cbrc.org.hibernate.MOrg;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.util.FitechException;


public class StrutsMOrgDelegate {
	private static FitechException log=new FitechException(StrutsMOrgDelegate.class);
	
	/**
	 * 得到所有的银行信息
	 * @author jhb
	 * @return List
	 */
	public static List getOrgs(){
		List resList=null;
		DBConn conn=null;
		
		try{
			String hql="from MOrg org where 1=1";
			conn=new DBConn();
			List list=conn.openSession().find(hql);
			if(list!=null && list.size()>0){
				resList=new ArrayList();
				MOrg mOrg=null;
				for(int i=0;i<list.size();i++){
					mOrg=(MOrg)list.get(i);
					MOrgForm form=new MOrgForm();
					TranslatorUtil.copyPersistenceToVo(mOrg,form);
					resList.add(form);
				}
			}
		}catch(HibernateException he){
			log.printStackTrace(he);
		}catch(Exception e){
			log.printStackTrace(e);
		}finally{
			if(conn!=null) conn.closeSession();
		}

		return resList;
	}
	/**
	 * 添加银行信息
	 * @author jhb
	 * @return List
	 */
	public static void  addorg(MOrgForm orgform){
		DBConn conn=null;
		Session session=null;
		MOrg mOrg=null;
		try{
			conn=new DBConn();
			session=conn.beginTransaction();
			mOrg=new MOrg();
			TranslatorUtil.copyVoToPersistence(mOrg,orgform);
			session.save(mOrg);
		}catch(Exception e)
		{   
			e.printStackTrace();
		}finally{
			
			if(conn!=null)conn.endTransaction(true);
		}	
	}
	/**
	 * 添加银行信息
	 * @author jhb
	 * @return List
	 */
	public static void  edit(MOrgForm orgform){
		DBConn conn=null;
		Session session=null;
		MOrg mOrg=null;
		try{
			conn=new DBConn();
			session=conn.beginTransaction();
			mOrg=new MOrg();
			TranslatorUtil.copyVoToPersistence(mOrg,orgform);
			session.update(mOrg);
			session.flush();
		}catch(Exception e)
		{   
			e.printStackTrace();
		}finally{
			
			if(conn!=null)conn.endTransaction(true);
		}	
	}
	/**
	 * 删除银行信息
	 * @author jhb
	 */
	public static void delete(String orgId){
     DBConn conn=null;
     Session session=null;
     try{
    	 conn=new DBConn();
    	 session=conn.beginTransaction();
    	 MOrg mOrg=(MOrg)conn.openSession().load(MOrg.class,orgId);
        session.delete(mOrg);
     }catch(Exception e)
     {
    	 e.printStackTrace();
     }finally{
    	 if(conn!=null)conn.endTransaction(true);
     }
	
	}
	/**
	 * 根据机构ID获得机构信息
	 * 
	 * @author jhb
	 * @serialData 2005-12-23 17:48 
	 * 
	 * @param orgId String 机构ID
	 * @return MOrgForm
	 */	
	public static MOrgForm getMOrg(String orgId){
		MOrgForm mOrgForm=null;
		
		if(orgId==null) return mOrgForm;
		
		DBConn conn=null;
		
		try
        {
			conn=new DBConn();
			MOrg mOrg=(MOrg)conn.openSession().load(MOrg.class,orgId);
           
			if(mOrg!=null)
            {
                mOrgForm = new MOrgForm();
                TranslatorUtil.copyPersistenceToVo(mOrg,mOrgForm);
            }
        }
        catch(HibernateException he)
        {
            mOrgForm =null;
			log.printStackTrace(he);
		}
        catch(Exception e)
        {
            mOrgForm =null;
			log.printStackTrace(e);
		}
        finally
        {
			if(conn!=null) conn.closeSession();
		}
		
		return mOrgForm;
	}
	
	/**
	 * 根据名称获得机构信息
	 * 
	 * @author rds 
	 * @serialData 2005-12-07 
	 * 
	 * @param orgName String 机构名称
	 * @return MOrgForm
	 *//*	
	public static MOrgForm getOrgInfo(String orgName){
		MOrgForm mOrgForm=null;
		
		if(orgName==null) return mOrgForm;
		
		DBConn conn=null;
		
		try{
			String hql="from MOrg org where org.orgName='" + orgName + "'";
			conn=new DBConn();
			List list=conn.openSession().find(hql);
			if(list!=null && list.size()>0){
				MOrg mOrg=(MOrg)list.get(0);
				TranslatorUtil.copyPersistenceToVo(mOrg,mOrgForm);
			}
		}catch(HibernateException he){
			log.printStackTrace(he);
		}catch(Exception e){
			log.printStackTrace(e);
		}finally{
			if(conn!=null) conn.closeSession();
		}
		
		return mOrgForm;
	}
	
	*//**
	 * 根据机构串获取所对应的机构类型列表
	 * 
	 * @author rds
	 * @serialData 2005-12-10
	 * 
	 * @param orgs String 机构串(逗号分隔)
	 * @return List
	 *//*
	public static List getOrgCls(String orgs){
		List resList=null;
		
		if(orgs==null) return resList;
		
		DBConn conn=null;
		
		try{
			String hql="select distinct mo.orgClsId from MOrg mo where mo.orgId in (" + orgs + ")";
			
			conn=new DBConn();
			List list=conn.openSession().find(hql);
			if(list!=null && list.size()>0){
				resList=new ArrayList();
				for(int i=0;i<list.size();i++){
					if(list.get(i)!=null) resList.add((String)list.get(i));
				}
			}
		}catch(HibernateException he){
			log.printStackTrace(he);
			resList=null;
		}finally{
			if(conn!=null) conn.closeSession();
		}
		
		return resList;
	}
	
	public static MOrgForm create(MOrgForm mOrgForm) throws Exception {
		return null;
	}*/
   
}

