
package com.fitech.net.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.fitech.net.form.MRegionForm;
import com.fitech.net.hibernate.MRegion;
/**
 *@StrutsMRegionDelegate  ����Delegate
 * 
 * @author jcm
 */
public class StrutsMRegionDelegate {
	//Catch��������׳��������쳣
	private static FitechException log=new FitechException(StrutsMRegionDelegate.class);

	/**
	 * ��������
	 * @param mRegionForm MRegionForm
     * @exception Exception����׽�쳣���� 
     * @return int result  0--����ʧ��  1--�����ɹ�  2--Ҫ��ӵ�ָ������Ļ�������Ѿ�����
     */
   public static int create (MRegionForm mRegionForm) throws Exception {
	
	   int result = 0;				//��result���
	   boolean bool = false;
	   MRegion mRegionPersistence = new MRegion();
	   	   
	   if (mRegionForm == null || mRegionForm.getRegion_name().equals("")) 
		   return result;
	   
	   //���Ӷ���ĳ�ʼ��
	   DBConn conn=null;
	   //�Ự����ĳ�ʼ��
	   Session session=null;
	   try{		   
		   TranslatorUtil.copyVoToPersistence(mRegionPersistence,mRegionForm);
		   //ʵ�������Ӷ���
		   conn =new DBConn();
		   //�Ự����Ϊ���Ӷ������������
		   session=conn.beginTransaction();
		   
		   String hql = "from MRegion mr where mr.regionName='" + mRegionForm.getRegion_name() + "' and mr.preRegionId = '" + mRegionForm.getPre_region_id()+"'";
		   List list = session.find(hql);
		   
		   if(list != null && list.size() > 0){
			   result = 2;        //Ҫ���ӵĻ��������Ѿ�����
			   bool = true;
			   return result;
		   }
		
		   //�Ự���󱣴�־ò����
		   session.save(mRegionPersistence);
		   session.flush();
		   
		   result=1;       //���ӳɹ�
		   bool = true;
		   
	   }
	   catch(HibernateException e){
		   //�־ò���쳣����׽
		   result = 0;
		   bool = false;
		   log.printStackTrace(e);
	   }
	   finally{
		   //�������״̬��,��Ͽ�,��������,����
		   if(conn!=null) conn.endTransaction(bool);
	   }
	   return result;
	   
   }

   /**
    * ȡ�ð�������ѯ���ļ�¼����
    * @return int ��ѯ���ļ�¼����	
    * @param   mRegionForm   ������ѯ��������Ϣ������ID���������ƣ�
    */
   
   public static int getRecordCount(MRegionForm mRegionForm)
   {
	   int count=0;
	   
	   //���Ӷ���ͻỰ�����ʼ��
	   DBConn conn=null;				
	   Session session=null;
	   
	   //��ѯ����HQL������
	   StringBuffer hql = new StringBuffer("select count(*) from MRegion mr");						
	   StringBuffer where = new StringBuffer("");
	   
	   if (mRegionForm != null) {
		   // �����������ж�,�������Ʋ���Ϊ��
			if (mRegionForm.getRegion_name() != null && !mRegionForm.getRegion_name().equals(""))
				where.append((where.toString().equals("")?"":" and ") + "mr.regionName like '%" + mRegionForm.getRegion_name() + "%'");
	   }
		  
      try {
    	  hql.append((where.toString().equals("")?"":" where ") + where.toString());
    	  //conn�����ʵ����		  
    	  conn=new DBConn();
    	  //�����ӿ�ʼ�Ự
    	  session=conn.openSession();
    	  List list=session.find(hql.toString());
    	  if(list!=null && list.size()==1){
    		  count=((Integer)list.get(0)).intValue();
    	  }
      }catch(HibernateException he){
    	  log.printStackTrace(he);
      }catch(Exception e){
    	  log.printStackTrace(e);
      }finally{
    	  //������Ӵ��ڣ���Ͽ��������Ự������
    	  if(conn!=null) conn.closeSession();
      }
         return count;
   }
   
   /**
    * ȡ�ð�������ѯ���ļ�¼����
    * @return int ��ѯ���ļ�¼����	
    * @param   mRegionForm   ������ѯ��������Ϣ������ID���������ƣ�
    */
   
   public static int getRecordCount(MRegionForm mRegionForm,Operator operator)
   {
	   int count=0;
	   
	   //���Ӷ���ͻỰ�����ʼ��
	   DBConn conn=null;				
	   Session session=null;
	   
	   //��ѯ����HQL������
	   StringBuffer hql = new StringBuffer("select count(*) from MRegion mr");						
	   StringBuffer where = new StringBuffer("");
	   
	   if (mRegionForm != null) {
		   // �����������ж�,�������Ʋ���Ϊ��
			if (mRegionForm.getRegion_name() != null && !mRegionForm.getRegion_name().equals(""))
				where.append((where.toString().equals("")?"":" and ") + "mr.regionName like '%" + mRegionForm.getRegion_name() + "%'");
	   }
		 
	   where.append((where.toString().equals("") ? "" : " and ") + "mr.setOrgId = '" + operator.getOrgId() + "'");
	  
	   try {
		   hql.append((where.toString().equals("")?"":" where ") + where.toString());
		   //conn�����ʵ����		  
		   conn=new DBConn();
		   //�����ӿ�ʼ�Ự
		   session=conn.openSession();
		   List list=session.find(hql.toString());
		   if(list!=null && list.size()==1){
			   count=((Integer)list.get(0)).intValue();
		   }
	   }catch(HibernateException he){
		   log.printStackTrace(he);
	   }catch(Exception e){
		   log.printStackTrace(e);
	   }finally{
		   //������Ӵ��ڣ���Ͽ��������Ự������
		   if(conn!=null) conn.closeSession();
	   }
	   return count;
   }
   
   /**
    * ������ѯ
    * 
    * @param mRegionForm MRegionForm ��ѯ������
    * @return List ������ҵ���¼������List��¼�������򣬷���null
    */
   public static List select(MRegionForm mRegionForm,int offset,int limit){
	   
	   //List���ϵĶ��� 
	   List refVals = null;		
		   
	   //���Ӷ���ͻỰ�����ʼ��
	   DBConn conn = null;				
	   Session session = null;
	   
	   //	 ��ѯ����HQL������
	   StringBuffer hql = new StringBuffer("from MRegion mr");						
	   StringBuffer where = new StringBuffer("");
	 
	   if (mRegionForm != null) {
		   // �����������ж�,�������Ʋ���Ϊ��
			if (mRegionForm.getRegion_name() != null && !mRegionForm.getRegion_name().equals(""))
				where.append((where.toString().equals("")?"":" and ") + "mr.regionName like '%" + mRegionForm.getRegion_name() + "%'");
	   }
		  
      try {
    	  hql.append((where.toString().equals("")?"":" where ") + where.toString());
    	  //conn�����ʵ����		  
    	  conn = new DBConn();
    	  //�����ӿ�ʼ�Ự
    	  session = conn.openSession();
		  Query query = session.createQuery(hql.toString());
		  query.setFirstResult(offset).setMaxResults(limit);
		  List list = query.list();
		  
    	  if (list != null){
    		  refVals = new ArrayList();
    		  //ѭ����ȡ���ݿ����������¼
		      for(Iterator it = list.iterator(); it.hasNext(); ) {
		    	 MRegionForm mRegionFormTemp = new MRegionForm();
		    	 MRegion mRegionPersistence = (MRegion)it.next();
		         TranslatorUtil.copyPersistenceToVo(mRegionPersistence, mRegionFormTemp);
		         if(mRegionFormTemp.getPre_region_id() != null){
		        	 MRegion preMRegion = selectOne(mRegionFormTemp.getPre_region_id());
					 if(preMRegion != null)
						 mRegionFormTemp.setPre_region_name(preMRegion.getRegionName());
		         }
		         refVals.add(mRegionFormTemp);
		      }
    	   }
      }catch(HibernateException he){
    	  refVals = null;
    	  he.printStackTrace();
    	  log.printStackTrace(he);
      }catch(Exception e){
    	  refVals = null;  
    	  e.printStackTrace();
    	  log.printStackTrace(e);
      }finally{
    	  //������Ӵ��ڣ���Ͽ��������Ự������
    	  if(conn != null) conn.closeSession();
      }
         return refVals;
   }
   
   /**
    * ������ѯ
    * 
    * @param mRegionForm MRegionForm ��ѯ������
    * @return List ������ҵ���¼������List��¼�������򣬷���null
    */
   public static List select(MRegionForm mRegionForm,int offset,int limit,Operator operator){
	   
	   //List���ϵĶ��� 
	   List refVals = null;		
		   
	   //���Ӷ���ͻỰ�����ʼ��
	   DBConn conn = null;				
	   Session session = null;
	   
	   //	 ��ѯ����HQL������
	   StringBuffer hql = new StringBuffer("from MRegion mr");						
	   StringBuffer where = new StringBuffer("");
	 
	   if (mRegionForm != null) {
		   // �����������ж�,�������Ʋ���Ϊ��
			if (mRegionForm.getRegion_name() != null && !mRegionForm.getRegion_name().equals(""))
				where.append((where.toString().equals("")?"":" and ") + "mr.regionName like '%" + mRegionForm.getRegion_name() + "%'");
	   }
	
	   where.append((where.toString().equals("") ? "" : " and ") + "mr.setOrgId = '" + operator.getOrgId() + "'");
	   try {
		   hql.append((where.toString().equals("")?"":" where ") + where.toString());
		   //conn�����ʵ����		  
		   conn = new DBConn();
		   //�����ӿ�ʼ�Ự
		   session = conn.openSession();
		   Query query = session.createQuery(hql.toString());
		   query.setFirstResult(offset).setMaxResults(limit);	  
		   List list = query.list();
		  
		   if (list != null){
			   refVals = new ArrayList();
			   //ѭ����ȡ���ݿ����������¼		      
			   for(Iterator it = list.iterator(); it.hasNext(); ) {		    	
				   MRegionForm mRegionFormTemp = new MRegionForm();		    	 
				   MRegion mRegionPersistence = (MRegion)it.next();		         
				   TranslatorUtil.copyPersistenceToVo(mRegionPersistence, mRegionFormTemp);		         
				   if(mRegionFormTemp.getPre_region_id() != null){		        	
					   MRegion preMRegion = selectOne(mRegionFormTemp.getPre_region_id());					 
					   if(preMRegion != null)						
						   mRegionFormTemp.setPre_region_name(preMRegion.getRegionName());		         
				   }		         
				   refVals.add(mRegionFormTemp);      
			   }   	   
		   }     
	   }catch(HibernateException he){   	  
		   refVals = null;    	  
		   he.printStackTrace();    	  
		   log.printStackTrace(he);      
	   }catch(Exception e){    	
		   refVals = null;      	  
		   e.printStackTrace();    	  
		   log.printStackTrace(e);      
	   }finally{    	
		   //������Ӵ��ڣ���Ͽ��������Ự������   	  
		   if(conn != null) conn.closeSession();      
	   } 
	   return refVals;
   }
   
   
   /**
    * ����MRegionForm����
    *
    * @param   mRegionForm   MRegionForm ������ݵĶ���
    * @exception   Exception  ���MRegionForm����ʧ��,��׽�׳��쳣
    */
   public static boolean update (MRegionForm mRegionForm) throws Exception {
	   boolean result = false;
		DBConn conn = null;
		Session session = null;

		MRegion mRegionPersistence = new MRegion();

		if (mRegionForm == null) {
			return result;
		}
		try {
			if (mRegionForm.getRegion_name() == null
					|| mRegionForm.getRegion_name().equals("")) {
				return result;
			}
			conn = new DBConn();
			session = conn.beginTransaction();

			TranslatorUtil.copyVoToPersistence(mRegionPersistence,	mRegionForm);
			session.update(mRegionPersistence);

			result = true;
		} catch (HibernateException he) {
			log.printStackTrace(he);
		} finally {
			if (conn != null)
				conn.endTransaction(result);
		}
		return result;
	}

   /**
	 * �༭����
	 * 
	 * @param mRegionForm  The MRegionForm  �������ݵĴ���
	 * @exception Exception ��� MRegionForm ����ʧ���׳��쳣.
	 */
   public static boolean  edit (MRegionForm mRegionForm) throws Exception {
	   boolean result = false;
	   DBConn conn = null;
	   Session session = null;
	  
	   MRegion mRegionPersistence = new MRegion ();
	   mRegionForm = new MRegionForm();

	   if (mRegionForm == null ){
		   return  result;
	   }
	   
	   try{
		   if (mRegionForm.getRegion_name() == null || mRegionForm.getRegion_name().equals(""))
			   return result;
		   conn = new DBConn();
		   session = conn.beginTransaction();
	   
		   mRegionPersistence = (MRegion)session.load(MRegion.class, mRegionForm.getRegion_name());
	
		   TranslatorUtil.copyVoToPersistence(mRegionPersistence, mRegionForm);
	         
	   }catch(HibernateException he){
		   log.printStackTrace(he);
	   }finally {
		   if(conn!=null) conn.endTransaction(result);
	   }
	       return result;
   }

   /**
    * ɾ������
    *
    * @param   mRegionForm   MRegionForm ��ѯ���Ķ���
    * @return   boolean  ���ɾ���ɹ��򷵻�true,����false
    */  
   public static boolean remove (MRegionForm mRegionForm) throws Exception {
       //�ñ�־result
	   boolean result=false;
	   //���ӺͻỰ����ĳ�ʼ��
	   DBConn conn=null;
	   Session session=null;
	 
	   //mRegionForm�Ƿ�Ϊ��,����result
	   if (mRegionForm == null || mRegionForm.getRegion_id() == null) 
		   return result;
	  
	     try{
	       //	���Ӷ���ͻỰ�����ʼ��
		   conn=new DBConn();
		   session=conn.beginTransaction();
		   
		   MRegion mRegion=(MRegion)session.load(MRegion.class,mRegionForm.getRegion_id());
		   //�Ự����ɾ���־ò����
		   session.delete(mRegion);
		   session.flush();
		   
		   //ɾ���ɹ�����Ϊtrue
		   result=true;
		  }
	   catch(HibernateException he){
		   //��׽������쳣,�׳�
		   log.printStackTrace(he);
	   }finally{
		   //�����������Ͽ����ӣ������Ự������
		   if (conn!=null) conn.endTransaction(result);
	   }
	     return result;
   }

   
	 /**
	  * ��ѯһ����¼
	  * @param mRegionForm MRegionFormʵ��������
	  * @return MRegionForm ����һ����¼
	  */
   
   public static MRegionForm selectOne(MRegionForm mRegionForm){
	   
	   DBConn conn = null;
	   Session session = null;
	   
	   if(mRegionForm != null){
		   try{
			   conn = new DBConn();
			   session = conn.openSession();
			   MRegion mRegionPersistence = (MRegion)session.load(MRegion.class,mRegionForm.getRegion_id());
			   TranslatorUtil.copyPersistenceToVo(mRegionPersistence,mRegionForm);
		   }catch(HibernateException he){
			   log.printStackTrace(he);
		   }catch(Exception e){
			   log.printStackTrace(e);
		   }finally{
			   //������Ӵ��ڣ���Ͽ��������Ự������
			   if(conn != null) conn.closeSession();
		   }
	   }
	   return mRegionForm;
   }



	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-22
	 * ��ѯ���м�¼
	 * @return List ��ѯ���ļ�¼����	
	 */
	public static List findAll () throws Exception {
		
		List refVals = null;
		DBConn conn = null;
		Session session = null;
		StringBuffer hql = new StringBuffer("from MRegion m order by m.regionId");
		
		try{
			conn = new DBConn();
			session = conn.openSession();
			List list = session.find(hql.toString());
			
			if(list != null){
				refVals = new ArrayList();
				for(Iterator iter = list.iterator();iter.hasNext();){
					MRegionForm mRegionFormTemp = new MRegionForm();
					MRegion mRegionPersistence = (MRegion)iter.next();
					TranslatorUtil.copyPersistenceToVo(mRegionPersistence,mRegionFormTemp);
//					MRegion mRegion = selectOne(mRegionPersistence.getRegionId());
//					if(mRegion != null){
//						mRegionFormTemp.setPre_region_name(mRegion.getRegionName());
//					}
					refVals.add(mRegionFormTemp);
				}
			}
		}catch(HibernateException he){
			refVals = null;
			log.printStackTrace(he);
		}catch(Exception e){
			refVals = null ;
			log.printStackTrace(e);
		}finally{
			//������Ӵ��ڣ���Ͽ��������Ự������
			if(conn != null) conn.closeSession();
		}
		return refVals;
	}
	
	/**
	 * �����ϼ�����
	 */
	public static MRegion findPreMRegionId(String regionId){
		MRegion mRegionResult = null;
		DBConn conn = null;
		Session session = null;
		
		try{
			conn = new DBConn();
			session = conn.openSession();
			
			String hql = "select min(regionId) from MRegion mr where mr.regionId > " + regionId ;
			
			List list = session.find(hql.toString());
			
			if(list != null && list.size() > 0){
				mRegionResult = (MRegion)list.get(0);
			}
		}catch(HibernateException he){
			log.printStackTrace(he);
		}catch(Exception e){
			mRegionResult = null ;
			log.printStackTrace(e);
		}finally{
			//������Ӵ��ڣ���Ͽ��������Ự������
			if(conn != null) conn.closeSession();
		}
		return mRegionResult;
	}
	
	/**
	 * ����һ����¼
	 */
	public static MRegion selectOne(String regionId){
		MRegion mRegionResult = null;
		DBConn conn = null;
		Session session = null;
		
		try{
			conn = new DBConn();
			session = conn.openSession();
			
			String hql = "from MRegion mr where mr.regionId = '" + regionId +"'";
			
			List list = session.find(hql.toString());
			
			if(list != null && list.size() > 0){
				mRegionResult = (MRegion)list.get(0);
			}
		}catch(HibernateException he){
			log.printStackTrace(he);
		}catch(Exception e){
			mRegionResult = null ;
			log.printStackTrace(e);
		}finally{
			//������Ӵ��ڣ���Ͽ��������Ự������
			if(conn != null) conn.closeSession();
		}
		return mRegionResult;
	}
	
	/**
	 * ��������ͬһ���ͻ����ĵ���
	 */
	public static List selectRegions(Integer orgTypeId){
		List resultList = null;
		DBConn conn = null;
		Session session = null;
		
		try{
			conn = new DBConn();
			session = conn.openSession();
			
			String hql = "from MRegion mr where mr.orgType.orgTypeId = " + orgTypeId ;
			
			List list = session.find(hql.toString());
			
			if(list != null && list.size() > 0){
				resultList = new ArrayList();
				for(int i=0;i<list.size();i++){
					MRegionForm mRegionFormTemp = new MRegionForm();
					MRegion mRegionPersistence = (MRegion)list.get(i);
					TranslatorUtil.copyPersistenceToVo(mRegionPersistence,mRegionFormTemp);
					MRegion mRegion = selectOne(mRegionPersistence.getPreRegionId());
					if(mRegion != null){
						mRegionFormTemp.setPre_region_name(mRegion.getRegionName());
					}
					resultList.add(mRegionFormTemp);
				}
			}
		}catch(HibernateException he){
			log.printStackTrace(he);
		}catch(Exception e){
			resultList = null ;
			log.printStackTrace(e);
		}finally{
			//������Ӵ��ڣ���Ͽ��������Ự������
			if(conn != null) conn.closeSession();
		}
		return resultList;
	}
	
	/**
	 * ��ѯ��һ���ӵ���
	 */
	public static List getLowerRegions(String regionId)
	{
		List list=null;
		DBConn conn=new DBConn();
		Session session=conn.openSession();
		String hql="from MRegion mr where mr.preRegionId='"+regionId+"'";
		try {
			list=session.find(hql);
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(conn!=null) conn.closeSession();
		}
		
		
		return list;
	}
	
	/**
	 * ��ѯ��һ���ӵ���
	 */
	public static List getLowerMRegionForms(String regionId)
	{
		List relist=null;
		DBConn conn=new DBConn();
		Session session=conn.openSession();
		String hql="from MRegion mr where mr.preRegionId='"+regionId+"'";
		try {
			List list=session.find(hql);
			if(list!=null){
				relist=new ArrayList();
				for (int i = 0; i < list.size(); i++) {
					MRegion mregion=(MRegion)list.get(i);
					MRegionForm mrg=new MRegionForm();
					com.fitech.net.adapter.TranslatorUtil.copyPersistenceToVo(mregion, mrg);
					relist.add(mrg);
				}
			}
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(conn!=null) conn.closeSession();
		}
		return relist;
	}
	
	/**
	 * ���ݻ�����ѯ���û������¼�����
	 * 
	 * @param ����ID ��ѯ������
	 * @return  list ������ҵ���¼������list�����򣬷���null
	 */	   
	public static List getNextRegionList(String orgId){    
		List rlist=null;		
		if(orgId==null) return null;
		
		//���Ӷ���ͻỰ�����ʼ��		
		DBConn conn = null;						 
		Session session = null;
		
		String hql = new String("from MRegion mrg where mrg.preRegionId=(select ont.region.regionId  from OrgNet ont where ont.orgId = '" + orgId + "')");   	    
		try {	  	    
			conn = new DBConn();	    	
			session = conn.openSession();
			
			Query query = session.createQuery(hql.toString());			
			List list = query.list();
			
			if (list != null){			
				rlist = new ArrayList();	    		 
				//ѭ����ȡ���ݿ����������¼			    
				for(Iterator it = list.iterator(); it.hasNext(); ) {			    
					MRegionForm mRegionFormTemp = new MRegionForm();			    	
					MRegion mRegionPersistence = (MRegion)it.next();			        
					TranslatorUtil.copyPersistenceToVo(mRegionPersistence, mRegionFormTemp);
			        
					if(mRegionFormTemp.getPre_region_id() != null){			        
						MRegion preMRegion = StrutsMRegionDelegate.selectOne(mRegionFormTemp.getPre_region_id());						
						if(preMRegion != null)						
							mRegionFormTemp.setPre_region_name(preMRegion.getRegionName());			         
					}			        
					rlist.add(mRegionFormTemp);			      
				}	    	   
			}	      
		}catch(HibernateException he){	    
			rlist=null;	    	
			he.printStackTrace();	    	
			log.printStackTrace(he);	      
		}catch(Exception e){	    
			rlist=null;	    	
			e.printStackTrace();	    	
			log.printStackTrace(e);	    
		}finally{	    
			//������Ӵ��ڣ���Ͽ��������Ự������	    	
			if(conn != null) conn.closeSession();	    
		}	    
		return  rlist;	   
	}
}
