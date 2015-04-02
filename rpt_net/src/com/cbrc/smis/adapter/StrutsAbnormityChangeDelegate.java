
package com.cbrc.smis.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.form.AbnormityChangeForm;
import com.cbrc.smis.hibernate.AbnormityChange;
import com.cbrc.smis.other.TemplateAbnormityChange;
import com.cbrc.smis.util.FitechException;
import com.fitech.net.adapter.StrutsOrgNetDelegate;
import com.fitech.net.hibernate.OrgNet;
/**
 * This is a delegate class to handle interaction with the backend persistence layer of hibernate. 
 * It has a set of methods to handle persistence for AbnormityChange data (i.e. 
 * com.cbrc.smis.form.AbnormityChangeForm objects).
 * 
 * @author <strong>Generated by Middlegen.</strong>
 */
public class StrutsAbnormityChangeDelegate {
	private static FitechException log = new FitechException(StrutsAbnormityChangeDelegate.class);
	
	/**
	 *  根据子报ID和版本号和机构类别ID获得机构总数
	 * 
	 * @author rds
	 * @date 2005-12-24 15:11
	 *  
	 * @param childRepId String 子报表ID
	 * @param versionId String 版本号
	 * @param orgClsId String 机构类别ID
	 * @return int
	 */
	public static int getCount(String childRepId,String versionId,String orgClsId){
		int count=0;
		
		if(childRepId==null || versionId==null) return count;
		
		DBConn conn=null;
		
		try{
			String hql="select count(*)" +
				"from AbnormityChange ac where ac.comp_id.childRepId='" + childRepId + "' and " +
				" ac.comp_id.versionId='" + versionId + 
				"' and ac.orgClsId='" + orgClsId + "'";
			
			conn=new DBConn();
			List list=conn.openSession().find(hql);
			if(list!=null && list.size()>0){
				count=list.get(0)!=null?((Integer)list.get(0)).intValue():0;
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
	 * 获取机构类别和其机构总数
	 * 
	 * @author rds
	 * @serialData 2005-12-24 10:50
	 *  
	 * @param childRepId String 子报表ID
	 * @param versionId String 版本号
	 * @return List
	 */
	public static List getOrgClsCountInfo(String childRepId,String versionId){
		List resList=null;
		
		if(childRepId==null || versionId==null) return null;
		
		DBConn conn=null;
		
		try{
			String hql="select ac.orgClsId,count(ac.orgClsId)" +
				"from AbnormityChange ac where ac.comp_id.childRepId='" + childRepId + "' and " +
				" ac.comp_id.versionId='" + versionId + "' group by ac.orgClsId";
			
			conn=new DBConn();
			List list=conn.openSession().find(hql);
			if(list!=null && list.size()>0){
				resList=new ArrayList();
				for(int i=0;i<list.size();i++){
					resList.add((Object[])list.get(i));
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
	 * 获取异常标准变化标准
	 * 
	 * @param childRepId String 子报表ID
	 * @param versionId String 版本号
	 * @return List
	 */
	public static List findAll(String childRepId,String versionId){
		List resList=null;
		
		if(childRepId==null || versionId==null) return null;
		
		DBConn conn=null;
		
		try{
			String hql="select distinct mc.cellId,mc.cellName,ac.prevRiseStandard,ac.prevFallStandard," + 
				"ac.sameRiseStandard,ac.sameFallStandard " +
				"from AbnormityChange ac,MCell mc  where ac.comp_id.childRepId='" + childRepId + "' and " +
				" ac.comp_id.versionId='" + versionId + "' and " +
				"ac.comp_id.cellId=mc.cellId";
			
			conn=new DBConn();
			List list=conn.openSession().find(hql);
			if(list!=null && list.size()>0){
				resList=new ArrayList();
				for(int i=0;i<list.size();i++){
					Object[] objs=(Object[])list.get(i);
					AbnormityChangeForm form=new AbnormityChangeForm();
					form.setCellId(objs[0]!=null?(Integer)objs[0]:new Integer(0));
					form.setCellName(objs[1]!=null?(String)objs[1]:"");
					form.setPrevFallStandard(objs[2]!=null?(Float)objs[2]:new Float(0.0f));
					form.setPrevRiseStandard(objs[3]!=null?(Float)objs[3]:new Float(0.0f));
					form.setSameFallStandard(objs[4]!=null?(Float)objs[4]:new Float(0.0f));
					form.setSameRiseStandard(objs[5]!=null?(Float)objs[5]:new Float(0.0f));
					resList.add(form);
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
	 * 获取异常标准变化标准
	 * 
	 * @author rds
	 * @serialData 2005-12-24 10:54
	 * 
	 * @param childRepId String 子报表ID
	 * @param versionId String 版本号
	 * @param orgClsId String 机构类别
	 * @return List
	 */
	public static List findAll(String childRepId,String versionId,String orgClsId){
		List resList=null;
		
		if(childRepId==null || versionId==null) return null;
		
		DBConn conn=null;
		
		try{
			String hql="from AbnormityChange ac where ac.comp_id.childRepId='" + 
				childRepId + "' and " +
				" ac.comp_id.versionId='" + versionId + "' and " +
				" ac.orgClsId='" + orgClsId + "'";
			
			conn=new DBConn();
			List list=conn.openSession().find(hql);
			if(list!=null && list.size()>0){
				resList=new ArrayList();
				AbnormityChange abnormityChange=null;
				for(int i=0;i<resList.size();i++){
					abnormityChange=(AbnormityChange)resList.get(i);
					AbnormityChangeForm form=new AbnormityChangeForm();
					TranslatorUtil.copyPersistenceToVo(abnormityChange,form);
					resList.add(form);
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
	 * 批量更新异常变化的标准操作
	 * 
	 * @author rds 
	 * @serialData 
	 * 
	 * @param orgs List 机构列表
	 * @param forms List AbnormityChangeForm列表
	 * @return boolean 操作成功，返回true;否则，返回false
	 */
	public static boolean updatePatch(String orgId,List orgs,List forms){
		boolean result=false;
		
		if(orgs==null || forms==null) return result;
		
		DBConn conn=null;
		Session session=null;
		
		try{
			conn=new DBConn();
			session=conn.beginTransaction();
			
			AbnormityChangeForm form=null;
			form=(AbnormityChangeForm)forms.get(0);
			if(delete(session,form.getChildRepId(),form.getVersionId())==false)
				throw new Exception ("更新前的异常标准的删除失败!");
			
			for(int i=0;i<forms.size();i++){
				form=(AbnormityChangeForm)forms.get(i);
				OrgNet mOrgForm=null;
				for(int j=0;j<orgs.size();j++){
					mOrgForm=(OrgNet)orgs.get(j);
					form.setOrgId(mOrgForm.getOrgId());
					form.setOrgCls("ok");
					AbnormityChange persistence=new AbnormityChange();
					TranslatorUtil.copyVoToPersistence(persistence,form);
					session.save(persistence);
					session.flush();
				}
			}
			result=true;
		}catch(HibernateException he){
			result=false;
			log.printStackTrace(he);
		}catch(Exception e){
			result=false;
			log.printStackTrace(e);
		}finally{
			if(conn!=null) conn.endTransaction(result);
		}
		
		return result;
	}
	/**
	 * 批量更新异常变化的标准操作
	 * 
	 * @author rds 
	 * @serialData 
	 * 
	 * @param orgs List 机构列表
	 * @param forms List AbnormityChangeForm列表
	 * @return boolean 操作成功，返回true;否则，返回false
	 */
	public static boolean updatePatch(String orgId,List forms){
		boolean result=false;
		
		if( forms==null) return result;
		
		DBConn conn=null;
		Session session=null;
		
		try{
			conn=new DBConn();
			session=conn.beginTransaction();
			
			AbnormityChangeForm form=null;
			form=(AbnormityChangeForm)forms.get(0);
			if(delete(session,form.getChildRepId(),form.getVersionId())==false)
				throw new Exception ("更新前的异常标准的删除失败!");
			
			for(int i=0;i<forms.size();i++){
				form=(AbnormityChangeForm)forms.get(i);
					form.setOrgId(orgId);
					form.setOrgCls("ok");
					AbnormityChange persistence=new AbnormityChange();
					TranslatorUtil.copyVoToPersistence(persistence,form);
					session.save(persistence);
					session.flush();
				
			}
			result=true;
		}catch(HibernateException he){
			result=false;
			log.printStackTrace(he);
		}catch(Exception e){
			result=false;
			log.printStackTrace(e);
		}finally{
			if(conn!=null) conn.endTransaction(result);
		}
		
		return result;
	}
	
	/**
	 * 批量插入异常变化的标准操作
	 * 
	 * @author rds 
	 * @serialData 
	 * 
	 * @param orgs List 机构列表
	 * @param forms List AbnormityChangeForm列表
	 * @return boolean 操作成功，返回true;否则，返回false
	 */
	public static boolean savePatch(List orgs,List forms){
		boolean result=false;
		
		if(orgs.size()==0)// System.out.println("orgs  0");
		if(forms.size()==0)// System.out.println("forms 0");
		if(orgs==null || forms==null || orgs.size()==0 || forms.size()==0) { System.out.println("odokokokokokok");return result;}
		
		DBConn conn=null;
		Session session=null;
		
		try{
			conn=new DBConn();
			session=conn.beginTransaction();
			
			AbnormityChangeForm form=null;
			form=(AbnormityChangeForm)forms.get(0);

			for(int i=0;i<forms.size();i++){
				form=(AbnormityChangeForm)forms.get(i);
				com.fitech.net.hibernate.OrgNet mOrgForm=null;
				for(int j=0;j<orgs.size();j++){
					mOrgForm=(com.fitech.net.hibernate.OrgNet)orgs.get(j);
					form.setOrgId(mOrgForm.getOrgId());
					form.setOrgCls("ok");
					AbnormityChange persistence=new AbnormityChange();
					TranslatorUtil.copyVoToPersistence(persistence,form);
					/*// System.out.println("CellId:" + persistence.getComp_id().getCellId());
					// System.out.println("ChildRepId:" + persistence.getComp_id().getChildRepId());
					// System.out.println("OrgId:" + persistence.getComp_id().getOrgId());
					// System.out.println("VersionId:" + persistence.getComp_id().getVersionId());*/
					session.save(persistence);
					session.flush();
				}
			}
			result=true;
		}catch(HibernateException he){
			result=false;
			log.printStackTrace(he);
		}catch(Exception e){
			result=false;
			log.printStackTrace(e);
		}finally{
			if(conn!=null) conn.endTransaction(result);
		}
		
		return result;
	}
	
   /**
    * 根据子报表ID和版本号删除其异常变化标准
    * 
    * @param session Session
    * @param childRepId String 子报表ID
    * @param versionId String 版本号
    * @return boolean 删除成功，返回true;否则，返回false 
    */
	private static boolean delete_old(String orgId,Session session,String childRepId,String versionId){
		if(session==null || childRepId==null || versionId==null) return false;
		
		boolean res=false;
		
		
		try{
			session.delete("from AbnormityChange ac where ac.comp_id.childRepId='" + childRepId + 
					"' and ac.comp_id.versionId='" + versionId + "'"+
					"  and  ac.comp_id.orgId in (" + StrutsOrgNetDelegate.selectAllLowerOrgIds(orgId)+")");
			session.flush();
			
			res=true;
		}catch(HibernateException he){
			log.printStackTrace(he);
			res=false;
		}catch(Exception e){
			log.printStackTrace(e);
			res=false;
		}
		
		return res;
	}
	
	/**
	    * 根据子报表ID和版本号删除其异常变化标准
	    * 
	    * @param session Session
	    * @param childRepId String 子报表ID
	    * @param versionId String 版本号
	    * @return boolean 删除成功，返回true;否则，返回false 
	    */
		private static boolean delete(String orgId,Session session,String childRepId,String versionId){
			if(session==null || childRepId==null || versionId==null) return false;
			
			boolean res=false;
			
			
			try{
				String sqlStr="from AbnormityChange ac where ac.comp_id.childRepId='" + childRepId + 
				"' and ac.comp_id.versionId='" + versionId + "'"+
				"  and  ac.comp_id.orgId in (" + StrutsOrgNetDelegate.selectAllLowerOrgIds(orgId)+",'"+orgId+"')";
				session.delete(sqlStr);
				String ss=StrutsOrgNetDelegate.selectAllLowerOrgIds(orgId);
				session.flush();
				
				res=true;
			}catch(HibernateException he){
				log.printStackTrace(he);
				res=false;
			}catch(Exception e){
				log.printStackTrace(e);
				res=false;
			}
			
			return res;
		}
		/**
		    * 根据子报表ID和版本号删除其异常变化标准
		    * 
		    * @param session Session
		    * @param childRepId String 子报表ID
		    * @param versionId String 版本号
		    * @return boolean 删除成功，返回true;否则，返回false 
		    */
			private static boolean delete(Session session,String childRepId,String versionId){
				if(session==null || childRepId==null || versionId==null) return false;
				
				boolean res=false;
				
				
				try{
					String sqlStr="from AbnormityChange ac where ac.comp_id.childRepId='" + childRepId + 
					"' and ac.comp_id.versionId='" + versionId + "'";
					session.delete(sqlStr);
					session.flush();
					
					res=true;
				}catch(HibernateException he){
					log.printStackTrace(he);
					res=false;
				}catch(Exception e){
					log.printStackTrace(e);
					res=false;
				}
				
				return res;
			}
	
   /**
    * Create a new com.cbrc.smis.form.AbnormityChangeForm object and persist (i.e. insert) it.
    *
    * @param   abnormityChangeForm   The object containing the data for the new com.cbrc.smis.form.AbnormityChangeForm object
    * @exception   Exception   If the new com.cbrc.smis.form.AbnormityChangeForm object cannot be created or persisted.
    */
   public static com.cbrc.smis.form.AbnormityChangeForm create (com.cbrc.smis.form.AbnormityChangeForm abnormityChangeForm) throws Exception {
      com.cbrc.smis.hibernate.AbnormityChange abnormityChangePersistence = new com.cbrc.smis.hibernate.AbnormityChange ();
      TranslatorUtil.copyVoToPersistence(abnormityChangePersistence, abnormityChangeForm);
javax.naming.InitialContext ctx = new javax.naming.InitialContext();
// TODO: Make adapter get SessionFactory jndi name by ant task argument?
net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory)ctx.lookup("java:AirlineHibernateFactory");
net.sf.hibernate.Session session = factory.openSession();
net.sf.hibernate.Transaction tx = session.beginTransaction();
// TODO error?: abnormityChangePersistence = (com.cbrc.smis.hibernate.AbnormityChange)session.save(abnormityChangePersistence);
session.save(abnormityChangePersistence);
tx.commit();
session.close();
      TranslatorUtil.copyPersistenceToVo(abnormityChangePersistence, abnormityChangeForm);
      return abnormityChangeForm;
   }

   /**
    * Update (i.e. persist) an existing com.cbrc.smis.form.AbnormityChangeForm object.
    *
    * @param   abnormityChangeForm   The com.cbrc.smis.form.AbnormityChangeForm object containing the data to be updated
    * @exception   Exception   If the com.cbrc.smis.form.AbnormityChangeForm object cannot be updated/persisted.
    */
   public static com.cbrc.smis.form.AbnormityChangeForm update (com.cbrc.smis.form.AbnormityChangeForm abnormityChangeForm) throws Exception {
      com.cbrc.smis.hibernate.AbnormityChange abnormityChangePersistence = new com.cbrc.smis.hibernate.AbnormityChange ();
      TranslatorUtil.copyVoToPersistence(abnormityChangePersistence, abnormityChangeForm);
javax.naming.InitialContext ctx = new javax.naming.InitialContext();
// TODO: Make adapter get SessionFactory jndi name by ant task argument?
net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory)ctx.lookup("java:AirlineHibernateFactory");
net.sf.hibernate.Session session = factory.openSession();
net.sf.hibernate.Transaction tx = session.beginTransaction();
session.update(abnormityChangePersistence);
tx.commit();
session.close();
      TranslatorUtil.copyPersistenceToVo(abnormityChangePersistence, abnormityChangeForm);
      return abnormityChangeForm;
   }

   /**
    * Retrieve an existing com.cbrc.smis.form.AbnormityChangeForm object for editing.
    *
    * @param   abnormityChangeForm   The com.cbrc.smis.form.AbnormityChangeForm object containing the data used to retrieve the object (i.e. the primary key info).
    * @exception   Exception   If the com.cbrc.smis.form.AbnormityChangeForm object cannot be retrieved.
    */
   public static com.cbrc.smis.form.AbnormityChangeForm edit (com.cbrc.smis.form.AbnormityChangeForm abnormityChangeForm) throws Exception {
      com.cbrc.smis.hibernate.AbnormityChange abnormityChangePersistence = new com.cbrc.smis.hibernate.AbnormityChange ();
      TranslatorUtil.copyVoToPersistence(abnormityChangePersistence, abnormityChangeForm);
javax.naming.InitialContext ctx = new javax.naming.InitialContext();
// TODO: Make adapter get SessionFactory jndi name by ant task argument?
net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory)ctx.lookup("java:AirlineHibernateFactory");
net.sf.hibernate.Session session = factory.openSession();
net.sf.hibernate.Transaction tx = session.beginTransaction();
tx.commit();
session.close();
      TranslatorUtil.copyPersistenceToVo(abnormityChangePersistence, abnormityChangeForm);
      return abnormityChangeForm;
   }

   /**
    * Remove (delete) an existing com.cbrc.smis.form.AbnormityChangeForm object.
    *
    * @param   abnormityChangeForm   The com.cbrc.smis.form.AbnormityChangeForm object containing the data to be deleted.
    * @exception   Exception   If the com.cbrc.smis.form.AbnormityChangeForm object cannot be removed.
    */  
   public static void remove (com.cbrc.smis.form.AbnormityChangeForm abnormityChangeForm) throws Exception {
      com.cbrc.smis.hibernate.AbnormityChange abnormityChangePersistence = new com.cbrc.smis.hibernate.AbnormityChange ();
      TranslatorUtil.copyVoToPersistence(abnormityChangePersistence, abnormityChangeForm);
javax.naming.InitialContext ctx = new javax.naming.InitialContext();
// TODO: Make adapter get SessionFactory jndi name by ant task argument?
net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory)ctx.lookup("java:AirlineHibernateFactory");
net.sf.hibernate.Session session = factory.openSession();
net.sf.hibernate.Transaction tx = session.beginTransaction();
// TODO: is this really needed?
session.delete(abnormityChangePersistence);
tx.commit();
session.close();
   }

   /**
    * Retrieve all existing com.cbrc.smis.form.AbnormityChangeForm objects.
    *
    * @exception   Exception   If the com.cbrc.smis.form.AbnormityChangeForm objects cannot be retrieved.
    */
   public static List findAll () throws Exception {
      List retVals = new ArrayList();
javax.naming.InitialContext ctx = new javax.naming.InitialContext();
// TODO: Make adapter get SessionFactory jndi name by ant task argument?
net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory)ctx.lookup("java:AirlineHibernateFactory");
net.sf.hibernate.Session session = factory.openSession();
net.sf.hibernate.Transaction tx = session.beginTransaction();
retVals.addAll(session.find("from com.cbrc.smis.hibernate.AbnormityChange"));
tx.commit();
session.close();
      ArrayList abnormityChange_vos = new ArrayList();
      for (Iterator it = retVals.iterator(); it.hasNext(); ) {
         com.cbrc.smis.form.AbnormityChangeForm abnormityChangeFormTemp = new com.cbrc.smis.form.AbnormityChangeForm();
         com.cbrc.smis.hibernate.AbnormityChange abnormityChangePersistence = (com.cbrc.smis.hibernate.AbnormityChange)it.next();
         TranslatorUtil.copyPersistenceToVo(abnormityChangePersistence, abnormityChangeFormTemp);
         abnormityChange_vos.add(abnormityChangeFormTemp);
      }
      retVals = abnormityChange_vos;
      return retVals;
   }

   /**
    * 点对点式报表异常变化信息查看
    * @author 姚捷
    * @param  childRepId String 子报表id
    * @param  versionId String 版本号id
    * @return List 返回查询的记录
    * @exception   Exception   If the com.cbrc.smis.form.AbnormityChangeForm objects cannot be retrieved.
    */
   public static List select (String childRepId, String versionId) throws Exception {
      
      List result = null;
      DBConn conn = null;
      Session session = null;
      if(childRepId!=null && !childRepId.equals("") && versionId!=null && !versionId.equals(""))
      {
          String hql = "select distinct ac.MCell.cellName,ac.prevRiseStandard,ac.prevFallStandard,ac.sameRiseStandard,ac.sameFallStandard "
                      +"from AbnormityChange ac where ac.comp_id.childRepId ='"
                      +childRepId+"' and ac.comp_id.versionId='"+versionId+"'";
          conn = new DBConn();
          session = conn.openSession();
          List list = null;
          try
          {
             list = session.createQuery(hql).list();
             if(list!=null && list.size()!=0)
             {
                 result = new ArrayList();
                 for (Iterator it = list.iterator(); it.hasNext(); ) 
                 {
                    TemplateAbnormityChange item = new TemplateAbnormityChange();
                    Object[] record = (Object[])it.next();
                    item.setItemName((String)record[0]);
                    item.setPrevRiseStandard((Float)record[1]);
                    item.setPrevFallStandard((Float)record[2]);
                    item.setSameRiseStandard((Float)record[3]);
                    item.setSameFallStandard((Float)record[4]);
                    result.add(item);
                 }       
             }
          }
          catch(Exception e)
          {
              result = null;
             log.printStackTrace(e);
          }
          finally{
              if(conn!=null)
                  conn.closeSession();
          }
      }
      return result;
   }

   /**
    * This method will return all objects referenced by MOrg
    */
   public static List getMOrg(com.cbrc.smis.form.AbnormityChangeForm abnormityChangeForm) throws Exception {
      List retVals = new ArrayList();
     /* com.cbrc.smis.hibernate.AbnormityChange abnormityChangePersistence = null;
javax.naming.InitialContext ctx = new javax.naming.InitialContext();
// TODO: Make adapter get SessionFactory jndi name by ant task argument?
net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory)ctx.lookup("java:AirlineHibernateFactory");
net.sf.hibernate.Session session = factory.openSession();
net.sf.hibernate.Transaction tx = session.beginTransaction();
tx.commit();
session.close();
retVals.add(abnormityChangePersistence.getMOrg());
      ArrayList ORG_ID_vos = new ArrayList();
      for (Iterator it = retVals.iterator(); it.hasNext(); ) {
         com.cbrc.smis.form.MOrgForm ORG_ID_Temp = new com.cbrc.smis.form.MOrgForm();
         com.cbrc.smis.hibernate.MOrg ORG_ID_PO = (com.cbrc.smis.hibernate.MOrg)it.next();
         TranslatorUtil.copyPersistenceToVo(ORG_ID_PO, ORG_ID_Temp);
         ORG_ID_vos.add(ORG_ID_Temp);
      }
      retVals = ORG_ID_vos;*/
      return retVals;
    }
   /**
    * This method will return all objects referenced by MCell
    */
   public static List getMCell(com.cbrc.smis.form.AbnormityChangeForm abnormityChangeForm) throws Exception {
      List retVals = new ArrayList();
      com.cbrc.smis.hibernate.AbnormityChange abnormityChangePersistence = null;
javax.naming.InitialContext ctx = new javax.naming.InitialContext();
// TODO: Make adapter get SessionFactory jndi name by ant task argument?
net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory)ctx.lookup("java:AirlineHibernateFactory");
net.sf.hibernate.Session session = factory.openSession();
net.sf.hibernate.Transaction tx = session.beginTransaction();
tx.commit();
session.close();
retVals.add(abnormityChangePersistence.getMCell());
      ArrayList CELL_ID_vos = new ArrayList();
      for (Iterator it = retVals.iterator(); it.hasNext(); ) {
         com.cbrc.smis.form.MCellForm CELL_ID_Temp = new com.cbrc.smis.form.MCellForm();
         com.cbrc.smis.hibernate.MCell CELL_ID_PO = (com.cbrc.smis.hibernate.MCell)it.next();
         TranslatorUtil.copyPersistenceToVo(CELL_ID_PO, CELL_ID_Temp);
         CELL_ID_vos.add(CELL_ID_Temp);
      }
      retVals = CELL_ID_vos;
      return retVals;
    }
   /**
    * This method will return all objects referenced by MChildReport
    */
   public static List getMChildReport(com.cbrc.smis.form.AbnormityChangeForm abnormityChangeForm) throws Exception {
      List retVals = new ArrayList();
      com.cbrc.smis.hibernate.AbnormityChange abnormityChangePersistence = null;
javax.naming.InitialContext ctx = new javax.naming.InitialContext();
// TODO: Make adapter get SessionFactory jndi name by ant task argument?
net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory)ctx.lookup("java:AirlineHibernateFactory");
net.sf.hibernate.Session session = factory.openSession();
net.sf.hibernate.Transaction tx = session.beginTransaction();
tx.commit();
session.close();
retVals.add(abnormityChangePersistence.getMChildReport());
      ArrayList CHILD_REP_ID_vos = new ArrayList();
      for (Iterator it = retVals.iterator(); it.hasNext(); ) {
         com.cbrc.smis.form.MChildReportForm CHILD_REP_ID_Temp = new com.cbrc.smis.form.MChildReportForm();
         com.cbrc.smis.hibernate.MChildReport CHILD_REP_ID_PO = (com.cbrc.smis.hibernate.MChildReport)it.next();
         TranslatorUtil.copyPersistenceToVo(CHILD_REP_ID_PO, CHILD_REP_ID_Temp);
         CHILD_REP_ID_vos.add(CHILD_REP_ID_Temp);
      }
      retVals = CHILD_REP_ID_vos;
      return retVals;
    }
   
   /**
	 * 获取异常标准变化标准
	 * 
	 * @param childRepId String 子报表ID
	 * @param versionId String 版本号
	 * @return List
	 */
	public static List findAllList(String childRepId,String versionId,String orgId){
		List resList=null;
		
		if(childRepId==null || versionId==null) return null;
		
		DBConn conn=null;
		
		try{
			String hql="select distinct mc.cellId,mc.cellName,ac.prevRiseStandard,ac.prevFallStandard," + 
				"ac.sameRiseStandard,ac.sameFallStandard " +
				"from AbnormityChange ac,MCell mc  where ac.comp_id.childRepId='" + childRepId + "' and " +
				" ac.comp_id.versionId='" + versionId + "' and " +
				"ac.comp_id.cellId=mc.cellId  and ac.orgId in (" + 
				 StrutsOrgNetDelegate.selectAllLowerOrgIds(orgId)+",'"+orgId+"')";
			
			conn=new DBConn();
			List list=conn.openSession().find(hql);
			if(list!=null && list.size()>0){
				resList=new ArrayList();
				for(int i=0;i<list.size();i++){
					Object[] objs=(Object[])list.get(i);
					AbnormityChangeForm form=new AbnormityChangeForm();
					form.setCellId(objs[0]!=null?(Integer)objs[0]:new Integer(0));
					form.setCellName(objs[1]!=null?(String)objs[1]:"");
					form.setPrevFallStandard(objs[2]!=null?(Float)objs[2]:new Float(0.0f));
					form.setPrevRiseStandard(objs[3]!=null?(Float)objs[3]:new Float(0.0f));
					form.setSameFallStandard(objs[4]!=null?(Float)objs[4]:new Float(0.0f));
					form.setSameRiseStandard(objs[5]!=null?(Float)objs[5]:new Float(0.0f));					
					resList.add(form);
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
	
	public static List findAll2(String childRepId, String versionId){
    	List resList=null;
    	
    	if(childRepId==null || versionId==null) return null;
    	
    	DBConn conn=null;
    	
    	try{
    		String hql="from AbnormityChange mrr where mrr.comp_id.childRepId='" + childRepId + 
    			"' and mrr.comp_id.versionId='" + versionId + "'";
    		
    		conn=new DBConn();
    		List list=conn.openSession().find(hql);
    		if(list!=null && list.size()>0){
    			resList=new ArrayList();
    			AbnormityChange abnormityChange=null;
    			for(int i=0;i<list.size();i++){
    				abnormityChange=(AbnormityChange)list.get(i);
    				AbnormityChangeForm abnormityChangeForm=new AbnormityChangeForm();
    				TranslatorUtil.copyPersistenceToVo(abnormityChange,abnormityChangeForm);
    				resList.add(abnormityChangeForm);
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
}
