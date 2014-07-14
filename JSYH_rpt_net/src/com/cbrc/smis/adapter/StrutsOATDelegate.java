package com.cbrc.smis.adapter;

import java.util.ArrayList;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.form.OrgActuTypeForm;
import com.cbrc.smis.hibernate.OrgActuType;
import com.cbrc.smis.util.FitechException;
/**
 * Ƶ���������м���
 * 
 * @author rds
 * @date 2006-02-06
 */
public class StrutsOATDelegate {
	private static FitechException log=new FitechException();
	
	/**
	 * ���Ƶ���������
	 * 
	 * @author rds
	 * @date 2006-02-06
	 * 
	 * @return int
	 */
	public static int getCount(){
		int count=0;
		
		DBConn conn=null;
		Session session=null;
		
		try{
			conn=new DBConn();
			session=conn.openSession();
			
			String hql="select count(*) from OrgActuType oat";
			
			List list=session.find(hql);
			if(list!=null && list.size()==1){
				count=((Integer)list.get(0)).intValue();
			}
		}catch(HibernateException he){
			log.printStackTrace(he);
		}catch(Exception e){
			log.printStackTrace(e);
		}finally{
			if(conn!=null) conn.closeSession();
		}
		
		return count;
	}
	
	/**
	 * ��ȡƵ�������Ϣ�б�
	 * 
	 * @author rds
	 * @date 2006-02-06
	 * 
	 * @param offset int ��ʼλ�� 
	 * @param limit int ƫ����
	 * @return List 
	 */
	public static List findAll(int offset,int limit){
		List results=null;
		
		DBConn conn=null;
		Session session=null;
		
		try{
			conn=new DBConn();
			session=conn.openSession();
			
			String hql="from OrgActuType oat";
			Query query=session.createQuery(hql);
			query.setFirstResult(offset);
			query.setMaxResults(limit);
			List list=query.list();
			if(list!=null && list.size()>0){
				results=new ArrayList();
				OrgActuType orgActuType=null;
				for(int i=0;i<list.size();i++){
					orgActuType=(OrgActuType)list.get(i);
					OrgActuTypeForm orgActuTypeForm=new OrgActuTypeForm();
					TranslatorUtil.copyPersistenceToVo(orgActuType,orgActuTypeForm);
					results.add(orgActuTypeForm);
				}
			}
		}catch(HibernateException he){
			log.printStackTrace(he);
		}catch(Exception e){
			log.printStackTrace(e);
		}finally{
			if(conn!=null) conn.closeSession();
		}
		
		return results;
	}
	
	/**
	 * ��ȡƵ�������Ϣ�б�
	 * 
	 * @author rds
	 * @date 2006-02-06
	 * 
	 * @return List 
	 */
	public static List findAll(){
		List results=null;
		
		DBConn conn=null;
		Session session=null;
		
		try{
			conn=new DBConn();
			session=conn.openSession();
			
			String hql="from OrgActuType oat";
			Query query=session.createQuery(hql);
			
			List list=query.list();
			if(list!=null && list.size()>0){
				results=new ArrayList();
				OrgActuType orgActuType=null;
				for(int i=0;i<list.size();i++){
					orgActuType=(OrgActuType)list.get(i);
					OrgActuTypeForm orgActuTypeForm=new OrgActuTypeForm();
					TranslatorUtil.copyPersistenceToVo(orgActuType,orgActuTypeForm);
					results.add(orgActuTypeForm);
				}
			}
		}catch(HibernateException he){
			log.printStackTrace(he);
		}catch(Exception e){
			log.printStackTrace(e);
		}finally{
			if(conn!=null) conn.closeSession();
		}
		
		return results;
	}

	/**
	 * ��ȡƵ�������Ϣ
	 * 
	 * @author rds
	 * @date 2006-02-06
	 * 
	 * @return List 
	 */
	public static Integer getFirstOATId(){
		Integer OATId=null;
		
		DBConn conn=null;
		Session session=null;
		
		try{
			conn=new DBConn();
			session=conn.openSession();
			
			String hql="from OrgActuType oat order by oat.OATId";
			Query query=session.createQuery(hql);
			
			List list=query.list();
			if(list!=null && list.size()>0){				
				OrgActuType orgActuType=null;				
				orgActuType=(OrgActuType)list.get(0);
				OATId=orgActuType.getOATId();
			}
		}catch(HibernateException he){
			log.printStackTrace(he);
		}catch(Exception e){
			log.printStackTrace(e);
		}finally{
			if(conn!=null) conn.closeSession();
		}
		
		return OATId;
	}

	/**
	 * �½�һ��Ƶ������¼
	 * 
	 * @author gujie
	 * @date 2006-02-06
	 * 
	 * @param OrgActuTypeForm ������
	 * @return �Ƿ�ɹ���Ϣ
	 *
	 **/
	public static boolean create (OrgActuTypeForm oatActionForm) throws Exception {
        
	       boolean result = false;
	       DBConn conn =null;
	       Session session =null;
	       if(oatActionForm!=null)
	       {
	           try
	           {
	               conn = new DBConn();
	               session = conn.beginTransaction(); 
	               
	               OrgActuType orgActuType = new OrgActuType();
	               orgActuType.setOATName(oatActionForm.getOATName());
	               orgActuType.setOATMemo(oatActionForm.getOATMemo());
	                          
	               session.save(orgActuType);
	               session.flush();
	               result = true;
	           }
	           catch(Exception e)
	           {
	               log.printStackTrace(e);
	               result = false;
	           }
	           finally{
	               if(conn!=null)
	                   conn.endTransaction(result);
	           }
	       }
	       return result;
	    }
	/**
	 * ɾ��һ��Ƶ������¼
	 * @author gujie
	 * @date 2006-02-06
	 * 
	 * @param OrgActuTypeForm ������
	 * @return �Ƿ�ɹ���Ϣ 
	 * 
	 * 
	 **/
	public static boolean remove(OrgActuTypeForm orgActuTypeForm)throws Exception
	   {
		   boolean result = false;
		   DBConn conn = null;
		   Session session = null;
		   if(orgActuTypeForm!= null){
			   try{
				   conn = new DBConn();
				   session = conn.beginTransaction();
				   OrgActuType orgActuType = (OrgActuType)session.load(OrgActuType.class,orgActuTypeForm.getOATId());
				   session.delete(orgActuType);
				   session.flush();
				   result = true;
			   }
			   catch(Exception e){
				   e.printStackTrace();
				   result = false;
			   }
			   finally{
				   if(conn!=null)
					   conn.endTransaction(result);
			   }
		   }
		   return result;
	   }
	/**
	 * ȡ��һ��Ƶ������¼��������Ϣ
	 * @author gujie
	 * @date 2006-02-07
	 * 
	 * 
	 * @param Ƶ������¼��ID��
	 * @return ��ϸ��Ϣ
	 * 
	 * **/
	public static OrgActuTypeForm getOATDetail(Integer OATId)
	   {
		OrgActuTypeForm  orgActuTypeForm = null;

	       DBConn conn = null;
	       Session session = null;
	       try
	       {
	           if(OATId!=null)
	           {
	               conn = new DBConn();
	               session = conn.openSession();
	           
	               Query query = session.createQuery("from OrgActuType oat where oat.OATId="+OATId);
	               
	               List list = query.list();
	               if(list!=null && list.size()!=0)
	               {
	            	   orgActuTypeForm = new OrgActuTypeForm();
	            	   OrgActuType orgActuTypePersistence = (OrgActuType)list.get(0);
	                   TranslatorUtil.copyPersistenceToVo(orgActuTypePersistence, orgActuTypeForm);
	               }
	           }
	       }
	       catch(Exception e)
	       {
	    	   orgActuTypeForm = null;
	           log.printStackTrace(e);
	       }
	       finally{
	           if(conn!=null)
	              conn.closeSession();
	       }
	       return orgActuTypeForm;
	 
	   }
	/**
	 * @author gujie
	 * @date 2006-02-07
	 * 
	 * @param OrgActuTypeForm ������
	 * @return �޸ĳɹ�������Ϣ
	 * **/
	public static boolean update (OrgActuTypeForm orgActuTypeForm) throws Exception {
	     
        boolean result = false;
        DBConn conn =null;
        Session session =null;
        if(orgActuTypeForm!=null)
        {
            try
            {
                if(orgActuTypeForm!=null)
                {
                    conn = new DBConn();
                    session = conn.beginTransaction(); 
                    
                    OrgActuType orgActuType = (OrgActuType)session.load(OrgActuType.class,orgActuTypeForm.getOATId());
                    //TranslatorUtil.copyVoToPersistence(operator,operatorForm);
                    orgActuType.setOATName(orgActuTypeForm.getOATName());
                    orgActuType.setOATMemo(orgActuTypeForm.getOATMemo());           
                    session.update(orgActuType);
                    session.flush();
                    result = true;
                }
            }
            catch(Exception e)
            {
                log.printStackTrace(e);
                result = false;
            }
            finally{
                if(conn!=null)
                    conn.endTransaction(result);
            }
        }
       return result;
    }
}
