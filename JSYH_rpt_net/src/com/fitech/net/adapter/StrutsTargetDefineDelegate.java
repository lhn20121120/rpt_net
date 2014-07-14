package com.fitech.net.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.util.FitechException;
import com.fitech.net.form.TargetBusinessForm;
import com.fitech.net.form.TargetDefineForm;
import com.fitech.net.form.TargetNormalForm;
import com.fitech.net.hibernate.MBusiness;
import com.fitech.net.hibernate.MNormal;
import com.fitech.net.hibernate.TargetDefine;

public class StrutsTargetDefineDelegate{
	
	//Catch��������׳��������쳣
	private static FitechException log=new FitechException(StrutsMRegionDelegate.class);

	/**
	 * �������е�ҵ������
	 * @return
	 */
	public static List findAllBusiness()
	{
		List refVals = null;
		DBConn conn = null;
		Session session = null;
		StringBuffer hql = new StringBuffer("from MBusiness");
		
		try{
			conn = new DBConn();
			session = conn.openSession();
			List list = session.find(hql.toString());
			
			if(list != null){
				refVals = new ArrayList();
				for(Iterator iter = list.iterator();iter.hasNext();){
					TargetBusinessForm targetBusinessForm = new TargetBusinessForm();
					MBusiness business = (MBusiness)iter.next();
					TranslatorUtil.copyPersistenceToVo(business,targetBusinessForm);
					
					refVals.add(targetBusinessForm);
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
	 * �������е�����
	 * @return
	 */
	public static List findAllNormal()
	{
		List refVals = null;
		DBConn conn = null;
		Session session = null;
		StringBuffer hql = new StringBuffer("from MNormal mn");
		
		try{
			conn = new DBConn();
			session = conn.openSession();
			List list = session.find(hql.toString());
			
			if(list != null){
				refVals = new ArrayList();
				for(Iterator iter = list.iterator();iter.hasNext();){
					TargetNormalForm targetNormalForm = new TargetNormalForm();
					MNormal normal = (MNormal)iter.next();
					TranslatorUtil.copyPersistenceToVo(normal,targetNormalForm);
					
					refVals.add(targetNormalForm);
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
	 * ɾ��
	 */
	public static boolean remove(TargetDefineForm form)
	{
		//�ñ�־result		   
		boolean result=false;
		//���ӺͻỰ����ĳ�ʼ��		   
		DBConn conn=null;		
		Session session=null;
		
		//orgTypeForm�Ƿ�Ϊ��,����result		
		if (form == null || form.getTargetDefineId() == null) 
			return result;
				
		try{		
			//���Ӷ���ͻỰ�����ʼ��			
			conn=new DBConn();			
			session=conn.beginTransaction();
			TargetDefine orgLayer=(TargetDefine)session.load(TargetDefine.class,form.getTargetDefineId());
			
			//�Ự����ɾ���־ò����			
			session.delete(orgLayer);			
			session.flush();			
			//ɾ���ɹ�����Ϊtrue			
			result=true;
			
		}catch(HibernateException he){		
			//��׽������쳣,�׳�			  
			log.printStackTrace(he);		   
		}finally{			   
			//�����������Ͽ����ӣ������Ự������		
			if (conn!=null) conn.endTransaction(result);		  
		}		
		return result;		
	}
	
	/**
	 * ȡ�ð�������ѯ���ļ�¼����
	 * @return int ��ѯ���ļ�¼����	
	 * @param   
	 */
	public static int getRecordCount(TargetDefineForm targetDefineForm){		   
		int count=0;
//		Map map=null;
		
		//���Ӷ���ͻỰ�����ʼ��		
		DBConn conn=null;						
		Session session=null;
		
		//��ѯ����HQL������		
		StringBuffer hql = new StringBuffer("select count(*) from TargetDefine ot");		
		StringBuffer where = new StringBuffer("");
		
		if (targetDefineForm != null) {		
			// �����������ж�,�������Ʋ���Ϊ��			
			if (targetDefineForm.getDefineName() != null && !targetDefineForm.getDefineName().equals(""))			
				where.append((where.toString().equals("") ? "" : " and ") + "ot.defineName like '%" + targetDefineForm.getDefineName() + "%'");

			if(targetDefineForm.getBusinessId() != null && !targetDefineForm.getBusinessId().equals(""))			
				where.append((where.toString().equals("") ? "" : " and ") + "ot.mbusiness.businessId = " + targetDefineForm.getBusinessId());

			if(targetDefineForm.getNormalId() != null && !targetDefineForm.getNormalId().equals(""))			
				where.append((where.toString().equals("") ? "" : " and ") + "ot.mnormal.normalId = " + targetDefineForm.getNormalId());
		}
		try {	    
			hql.append((where.toString().equals("")?"":" where ") + where.toString());	    	
			//conn�����ʵ����		  	    	
			conn=new DBConn();	    	
			//�����ӿ�ʼ�Ự	    	
			session=conn.openSession();
			List list=session.find(hql.toString());
			
			//���а汾�ŵ�ָ�궼��ʾ
			if(list != null && list.size()>0)
				count=((Integer)list.get(0)).intValue();
			
//			map=new HashMap();
//			if(list!=null && list.size()>0){
//				for(int i=0;i<list.size();i++){
//					//���˵��ظ���
//					 TargetDefine td=(TargetDefine)list.get(i);
//					 map.put(td.getDefineName(), td);	
//				}
//			}
		}catch(HibernateException he){	    
			log.printStackTrace(he);
			he.printStackTrace();	      
		}catch(Exception e){	    
			log.printStackTrace(e);
			e.printStackTrace();	      
		}finally{	    
			//������Ӵ��ڣ���Ͽ��������Ự������	    	
			if(conn!=null) conn.closeSession();	      
		}	
//		if(map!=null) return map.size();
		return count;	  
	}
	
	/**
	 * @param 
	 * @return List ������ҵ���¼������List��¼�������򣬷���null
	 */
	public static List select(TargetDefineForm targetDefineForm,int offset,int limit,String orgId){
		List refVals = null;		
		
		//���Ӷ���ͻỰ�����ʼ��		
		DBConn conn = null;						
		Session session = null;		   
		
		//��ѯ����HQL������		
		StringBuffer hql = new StringBuffer("from TargetDefine ot");								
		StringBuffer where = new StringBuffer("");
		
		if (targetDefineForm != null) {		
			// �����������ж�,�������Ʋ���Ϊ��			
			if (targetDefineForm.getDefineName() != null && !targetDefineForm.getDefineName().equals(""))			
				where.append((where.toString().equals("")?"":" and ") + "ot.defineName like '%" + targetDefineForm.getDefineName() + "%'");
								
			if(targetDefineForm.getBusinessId() != null && !targetDefineForm.getBusinessId().equals(""))					
				where.append((where.toString().equals("") ? "" : " and ") + "ot.mbusiness.businessId = " + targetDefineForm.getBusinessId());				
				
			if(targetDefineForm.getNormalId() != null && !targetDefineForm.getNormalId().equals(""))			
				where.append((where.toString().equals("") ? "" : " and ") + "ot.mnormal.normalId = " + targetDefineForm.getNormalId());		   
		}
		try {	    
			hql.append((where.toString().equals("")?"":" where ") + where.toString()).append(" order by ot.defineName,ot.mnormal.normalId,ot.targetDefineId");	    	
			//conn�����ʵ����		  	    	
			conn = new DBConn();	    	
			//�����ӿ�ʼ�Ự	    	
			session = conn.openSession();
			
			Query query = session.createQuery(hql.toString());		
			query.setFirstResult(offset).setMaxResults(limit);
			//ָ�����а汾����ʾ
			List list = query.list();
			if(list != null && list.size() > 0){
				refVals = new ArrayList();
				TargetDefineForm targetDefineTempForm = null;
				TargetDefine targetDefinePersistence = null;
				for(Iterator iter=list.iterator();iter.hasNext();){
					targetDefineTempForm = new TargetDefineForm();			    	
					targetDefinePersistence = (TargetDefine)iter.next();			        
					TranslatorUtil.copyPersistenceToVo(targetDefinePersistence, targetDefineTempForm);
					
					//ȡ�øû����Ƿ�ѡ��			        
					if(StrutsTargetRangeDelegate.Exit(orgId,targetDefineTempForm.getTargetDefineId()))			        
						targetDefineTempForm.setWarn("1");			        
					else 
						targetDefineTempForm.setWarn("0");
					
					refVals.add(targetDefineTempForm);
				}
			}
			
//			List list = query.list();
//			if (list != null){
//				//����ָ�ֻ꣬��ʾ���°汾��ָ�꣨ͬ��ָ������в�ͬ�İ汾��
//				Map map=new HashMap();
//				for(int i=0;i<list.size();i++){
//					TargetDefine td=(TargetDefine)list.get(i);
//					if(map.containsKey(td.getDefineName())){
//						Integer map_version=new Integer(((TargetDefine)map.get(td.getDefineName())).getVersion());
//						Integer list_version=new Integer(td.getVersion());
//						if(list_version.intValue()>map_version.intValue()) map.put(td.getDefineName(), td);
//					}else{
//						map.put(td.getDefineName(), td);
//					}
//				}
//				refVals = new ArrayList();	    		  
//				//ѭ����ȡ���ݿ����������¼		
//				int limitNum=offset+limit;
//				int i=0;
//				for(Iterator it = map.values().iterator(); it.hasNext(); ) {
//					i++;
//					if(i>=offset && i<limitNum){//����ƫ��������ȡ��Ҫ��ʾ�Ľ����
//						TargetDefineForm orgTypeFormTemp = new TargetDefineForm();			    	
//						TargetDefine orgTypePersistence = (TargetDefine)it.next();			        
//						TranslatorUtil.copyPersistenceToVo(orgTypePersistence, orgTypeFormTemp);
//				        
//						//ȡ�øû����Ƿ�ѡ��			        
//						if(StrutsTargetRangeDelegate.Exit(orgId,orgTypeFormTemp.getTargetDefineId()))			        
//							orgTypeFormTemp.setWarn("1");			        
//						else 
//							orgTypeFormTemp.setWarn("0");
//						
//						refVals.add(orgTypeFormTemp);	
//					}else{it.next();}
//				}	    	 
//			}	      
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
	
	public static int create (TargetDefineForm targetDefineForm) throws Exception {
		int result = 0;				//��result���		
		boolean bool = false;		
		TargetDefine targetDefinePersistence = new TargetDefine();
		
		if (targetDefineForm == null || targetDefineForm.getDefineName().equals("")) 		
			return result;
		   		   
		//���Ӷ���ĳ�ʼ��		
		DBConn conn=null;		
		//�Ự����ĳ�ʼ��		
		Session session=null;
		
		try{			   		
			TranslatorUtil.copyVoToPersistence(targetDefinePersistence,targetDefineForm);			
			//ʵ�������Ӷ���			
			conn =new DBConn();			
			//�Ự����Ϊ���Ӷ������������			
			session=conn.beginTransaction();
			
			//�Ự���󱣴�־ò����			
			session.save(targetDefinePersistence);			
			targetDefineForm.setTargetDefineId(targetDefinePersistence.getTargetDefineId());		
			session.flush();	
			
			result=1;       //���ӳɹ�			
			bool = true;			   		   
		}catch(HibernateException e){		
			//�־ò���쳣����׽			
			result = 0;			
			bool = false;			
			log.printStackTrace(e);	
			e.printStackTrace();
		}finally{		
			//�������״̬��,��Ͽ�,��������,����			
			if(conn!=null) conn.endTransaction(bool);		   
		}		
		return result;		
	}
	
	public static TargetDefineForm  selectone(TargetDefineForm form)
	{		   	
		if(form.getTargetDefineId()==null) return null;
		
		DBConn conn=null;		
		Session session=null;
		
		try{		
			conn=new DBConn();			
			session=conn.openSession();
			
			TargetDefine define=(TargetDefine)session.load(TargetDefine.class,form.getTargetDefineId());			
			TranslatorUtil.copyPersistenceToVo(define,form);		   
		}catch(Exception e){			
			log.printStackTrace(e);			
			e.printStackTrace();		   
		}finally{			
			if(conn!=null) conn.closeSession();		
		}
		return form;	   
	}
	   
	public static boolean update(TargetDefineForm targetdefineForm)
	{	
		boolean result = false;		
		DBConn conn = null;		
		Session session = null;

		TargetDefine targetDefinePersistence = new TargetDefine();

		if (targetdefineForm == null) {
			return result;			
		}		
		try {		
			if (targetdefineForm.getDefineName() == null 
					|| targetdefineForm.getDefineName().equals("")) {					
				return result;
			}			
			conn = new DBConn();			
			session = conn.beginTransaction();

			TranslatorUtil.copyVoToPersistence(targetDefinePersistence,	targetdefineForm);	
			session.update(targetDefinePersistence);
			result = true;
			
		} catch (HibernateException he) {		
			he.printStackTrace();
			log.printStackTrace(he);			
		} finally {		
			if (conn != null)				
				conn.endTransaction(result);			
		}		
		return result;	   
	}
	   /**
	    * ����falg
	    * @param id
	    * @param falg
	    * @return
	    */
	   public static boolean UpdateFlag(String id,String falg)
	   {
		   boolean result=false;
		   DBConn conn=null;
		   Session session=null;
		   if(id==null || id.equals("") || falg==null || falg.equals(""))return false;
		   
		   try
		   {
			   conn=new DBConn();
			   session=conn.beginTransaction();
			   TargetDefine target=(TargetDefine)session.load(TargetDefine.class,Integer.valueOf(id.trim()));
			   target.setWarn(falg);
			   session.save(target);
			   session.flush();
			   result=true;
		   }
		   catch(Exception e)
		   {
			   log.printStackTrace(e);
			   result=false;
		   }
		   finally
		   {
			   if(conn!=null)conn.endTransaction(result);
		   }
		   
		   return result;
	   }
	public static boolean Exit(TargetDefineForm targetdefineForm)
	{
		boolean result=false;
		DBConn conn=null;
		Session session=null;
		if(targetdefineForm==null || targetdefineForm.getDefineName()==null || targetdefineForm.getDefineName().equals(""))
			return false;
		try
		{
			String sql="from TargetDefine t where t.defineName='"+targetdefineForm.getDefineName()+"'";
			conn=new DBConn();
			session=conn.beginTransaction();
			List list=session.find(sql);			
			if(list!=null && list.size()>0 )
				result=true;
			else
				result=false;
				
		}
		catch(Exception e)
		{
			log.printStackTrace(e);
			result=false;
		}
		finally
		{
		 if(conn!=null)conn.closeSession();	
		}
		return result;
		
	}
	
	/**
	 * ��������ָ��
	 * @param 
	 * @return List ������ҵ���¼������List��¼�������򣬷���null
	 */
	public static List selectAll(TargetDefineForm targetDefineForm,String orgId){
		List refVals = null;		
		
		//���Ӷ���ͻỰ�����ʼ��		
		DBConn conn = null;						
		Session session = null;		   
		
		//��ѯ����HQL������		
		StringBuffer hql = new StringBuffer("from TargetDefine ot");								
		StringBuffer where = new StringBuffer("");
		
		if (targetDefineForm != null) {		
			// �����������ж�,�������Ʋ���Ϊ��			
			if (targetDefineForm.getDefineName() != null && !targetDefineForm.getDefineName().equals(""))			
				where.append((where.toString().equals("")?"":" and ") + "ot.defineName like '%" + targetDefineForm.getDefineName() + "%'");
								
			if(targetDefineForm.getBusinessId() != null && !targetDefineForm.getBusinessId().equals(""))					
				where.append((where.toString().equals("") ? "" : " and ") + "ot.mbusiness.businessId = " + targetDefineForm.getBusinessId());				
				
			if(targetDefineForm.getNormalId() != null && !targetDefineForm.getNormalId().equals(""))			
				where.append((where.toString().equals("") ? "" : " and ") + "ot.mnormal.normalId = " + targetDefineForm.getNormalId());		   
		}
		try {	    
			hql.append((where.toString().equals("")?"":" where ") + where.toString()).append(" order by ot.targetDefineId");	    	
			//conn�����ʵ����		  	    	
			conn = new DBConn();	    	
			//�����ӿ�ʼ�Ự	    	
			session = conn.openSession();
			
			Query query = session.createQuery(hql.toString());			
			List list = query.list();
			
			if (list != null){	    	
				refVals = new ArrayList();	    		  
				//ѭ����ȡ���ݿ����������¼			    
				for(Iterator it = list.iterator(); it.hasNext(); ) {			    
					TargetDefineForm orgTypeFormTemp = new TargetDefineForm();			    	
					TargetDefine orgTypePersistence = (TargetDefine)it.next();			        
					TranslatorUtil.copyPersistenceToVo(orgTypePersistence, orgTypeFormTemp);
			        
					//ȡ�øû����Ƿ�ѡ��			        
					if(StrutsTargetRangeDelegate.Exit(orgId,orgTypeFormTemp.getTargetDefineId()))			        
						orgTypeFormTemp.setWarn("1");			        
					else 
						orgTypeFormTemp.setWarn("0");
					
					refVals.add(orgTypeFormTemp);			     
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
	 * ����ָ�����ƺͰ汾�Ų�ѯ
	 * @param defineName
	 * @param version
	 * @return
	 */
	public static List selectTargetDefine(String defineName,String version){
        List refVals = null;		
		
		//���Ӷ���ͻỰ�����ʼ��		
		DBConn conn = null;						
		Session session = null;		   
		
		//��ѯ����HQL������		
		String hql = new String("from TargetDefine ot where ot.defineName='"+defineName+"' and ot.version='"+version+"'");								
          
			//conn�����ʵ����		  	    	
			conn = new DBConn();	    	
			//�����ӿ�ʼ�Ự	    	
			session = conn.openSession();
	    try{
			Query query = session.createQuery(hql);			
			return query.list();
		}catch(HibernateException he){	    
			refVals = null;
	    	he.printStackTrace();
			log.printStackTrace(he);
			return null;
		}catch(Exception e){	    
			refVals = null;  
			e.printStackTrace();
			log.printStackTrace(e);	  
			return null;
		}finally{	    	
			//������Ӵ��ڣ���Ͽ��������Ự������	    	
			if(conn != null) conn.closeSession();	     
		}	      
	}
}