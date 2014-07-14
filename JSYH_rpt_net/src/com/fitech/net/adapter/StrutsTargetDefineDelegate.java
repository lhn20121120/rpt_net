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
	
	//Catch到本类的抛出的所有异常
	private static FitechException log=new FitechException(StrutsMRegionDelegate.class);

	/**
	 * 查找所有的业务类型
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
			//如果连接存在，则断开，结束会话，返回
			if(conn != null) conn.closeSession();
		}
		return refVals;
	}
	/**
	 * 查找所有的类型
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
			//如果连接存在，则断开，结束会话，返回
			if(conn != null) conn.closeSession();
		}
		return refVals;
	}
	
	/**
	 * 删除
	 */
	public static boolean remove(TargetDefineForm form)
	{
		//置标志result		   
		boolean result=false;
		//连接和会话对象的初始化		   
		DBConn conn=null;		
		Session session=null;
		
		//orgTypeForm是否为空,返回result		
		if (form == null || form.getTargetDefineId() == null) 
			return result;
				
		try{		
			//连接对象和会话对象初始化			
			conn=new DBConn();			
			session=conn.beginTransaction();
			TargetDefine orgLayer=(TargetDefine)session.load(TargetDefine.class,form.getTargetDefineId());
			
			//会话对象删除持久层对象			
			session.delete(orgLayer);			
			session.flush();			
			//删除成功，置为true			
			result=true;
			
		}catch(HibernateException he){		
			//捕捉本类的异常,抛出			  
			log.printStackTrace(he);		   
		}finally{			   
			//如果由连接则断开连接，结束会话，返回		
			if (conn!=null) conn.endTransaction(result);		  
		}		
		return result;		
	}
	
	/**
	 * 取得按条件查询到的记录条数
	 * @return int 查询到的记录条数	
	 * @param   
	 */
	public static int getRecordCount(TargetDefineForm targetDefineForm){		   
		int count=0;
//		Map map=null;
		
		//连接对象和会话对象初始化		
		DBConn conn=null;						
		Session session=null;
		
		//查询条件HQL的生成		
		StringBuffer hql = new StringBuffer("select count(*) from TargetDefine ot");		
		StringBuffer where = new StringBuffer("");
		
		if (targetDefineForm != null) {		
			// 查找条件的判断,查找名称不可为空			
			if (targetDefineForm.getDefineName() != null && !targetDefineForm.getDefineName().equals(""))			
				where.append((where.toString().equals("") ? "" : " and ") + "ot.defineName like '%" + targetDefineForm.getDefineName() + "%'");

			if(targetDefineForm.getBusinessId() != null && !targetDefineForm.getBusinessId().equals(""))			
				where.append((where.toString().equals("") ? "" : " and ") + "ot.mbusiness.businessId = " + targetDefineForm.getBusinessId());

			if(targetDefineForm.getNormalId() != null && !targetDefineForm.getNormalId().equals(""))			
				where.append((where.toString().equals("") ? "" : " and ") + "ot.mnormal.normalId = " + targetDefineForm.getNormalId());
		}
		try {	    
			hql.append((where.toString().equals("")?"":" where ") + where.toString());	    	
			//conn对象的实例化		  	    	
			conn=new DBConn();	    	
			//打开连接开始会话	    	
			session=conn.openSession();
			List list=session.find(hql.toString());
			
			//所有版本号的指标都显示
			if(list != null && list.size()>0)
				count=((Integer)list.get(0)).intValue();
			
//			map=new HashMap();
//			if(list!=null && list.size()>0){
//				for(int i=0;i<list.size();i++){
//					//过滤掉重复的
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
			//如果连接存在，则断开，结束会话，返回	    	
			if(conn!=null) conn.closeSession();	      
		}	
//		if(map!=null) return map.size();
		return count;	  
	}
	
	/**
	 * @param 
	 * @return List 如果查找到记录，返回List记录集；否则，返回null
	 */
	public static List select(TargetDefineForm targetDefineForm,int offset,int limit,String orgId){
		List refVals = null;		
		
		//连接对象和会话对象初始化		
		DBConn conn = null;						
		Session session = null;		   
		
		//查询条件HQL的生成		
		StringBuffer hql = new StringBuffer("from TargetDefine ot");								
		StringBuffer where = new StringBuffer("");
		
		if (targetDefineForm != null) {		
			// 查找条件的判断,查找名称不可为空			
			if (targetDefineForm.getDefineName() != null && !targetDefineForm.getDefineName().equals(""))			
				where.append((where.toString().equals("")?"":" and ") + "ot.defineName like '%" + targetDefineForm.getDefineName() + "%'");
								
			if(targetDefineForm.getBusinessId() != null && !targetDefineForm.getBusinessId().equals(""))					
				where.append((where.toString().equals("") ? "" : " and ") + "ot.mbusiness.businessId = " + targetDefineForm.getBusinessId());				
				
			if(targetDefineForm.getNormalId() != null && !targetDefineForm.getNormalId().equals(""))			
				where.append((where.toString().equals("") ? "" : " and ") + "ot.mnormal.normalId = " + targetDefineForm.getNormalId());		   
		}
		try {	    
			hql.append((where.toString().equals("")?"":" where ") + where.toString()).append(" order by ot.defineName,ot.mnormal.normalId,ot.targetDefineId");	    	
			//conn对象的实例化		  	    	
			conn = new DBConn();	    	
			//打开连接开始会话	    	
			session = conn.openSession();
			
			Query query = session.createQuery(hql.toString());		
			query.setFirstResult(offset).setMaxResults(limit);
			//指标所有版本均显示
			List list = query.list();
			if(list != null && list.size() > 0){
				refVals = new ArrayList();
				TargetDefineForm targetDefineTempForm = null;
				TargetDefine targetDefinePersistence = null;
				for(Iterator iter=list.iterator();iter.hasNext();){
					targetDefineTempForm = new TargetDefineForm();			    	
					targetDefinePersistence = (TargetDefine)iter.next();			        
					TranslatorUtil.copyPersistenceToVo(targetDefinePersistence, targetDefineTempForm);
					
					//取得该机构是否选中			        
					if(StrutsTargetRangeDelegate.Exit(orgId,targetDefineTempForm.getTargetDefineId()))			        
						targetDefineTempForm.setWarn("1");			        
					else 
						targetDefineTempForm.setWarn("0");
					
					refVals.add(targetDefineTempForm);
				}
			}
			
//			List list = query.list();
//			if (list != null){
//				//过滤指标，只显示最新版本的指标（同名指标可以有不同的版本）
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
//				//循环读取数据库符合条件记录		
//				int limitNum=offset+limit;
//				int i=0;
//				for(Iterator it = map.values().iterator(); it.hasNext(); ) {
//					i++;
//					if(i>=offset && i<limitNum){//根据偏移量，截取需要显示的结果集
//						TargetDefineForm orgTypeFormTemp = new TargetDefineForm();			    	
//						TargetDefine orgTypePersistence = (TargetDefine)it.next();			        
//						TranslatorUtil.copyPersistenceToVo(orgTypePersistence, orgTypeFormTemp);
//				        
//						//取得该机构是否选中			        
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
			//如果连接存在，则断开，结束会话，返回	    	
			if(conn != null) conn.closeSession();	     
		}	    
		return refVals;	   
	}
	
	public static int create (TargetDefineForm targetDefineForm) throws Exception {
		int result = 0;				//置result标记		
		boolean bool = false;		
		TargetDefine targetDefinePersistence = new TargetDefine();
		
		if (targetDefineForm == null || targetDefineForm.getDefineName().equals("")) 		
			return result;
		   		   
		//连接对象的初始化		
		DBConn conn=null;		
		//会话对象的初始化		
		Session session=null;
		
		try{			   		
			TranslatorUtil.copyVoToPersistence(targetDefinePersistence,targetDefineForm);			
			//实例化连接对象			
			conn =new DBConn();			
			//会话对象为连接对象的事务属性			
			session=conn.beginTransaction();
			
			//会话对象保存持久层对象			
			session.save(targetDefinePersistence);			
			targetDefineForm.setTargetDefineId(targetDefinePersistence.getTargetDefineId());		
			session.flush();	
			
			result=1;       //增加成功			
			bool = true;			   		   
		}catch(HibernateException e){		
			//持久层的异常被捕捉			
			result = 0;			
			bool = false;			
			log.printStackTrace(e);	
			e.printStackTrace();
		}finally{		
			//如果连接状态有,则断开,结束事务,返回			
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
	    * 更新falg
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
	 * 查找所有指标
	 * @param 
	 * @return List 如果查找到记录，返回List记录集；否则，返回null
	 */
	public static List selectAll(TargetDefineForm targetDefineForm,String orgId){
		List refVals = null;		
		
		//连接对象和会话对象初始化		
		DBConn conn = null;						
		Session session = null;		   
		
		//查询条件HQL的生成		
		StringBuffer hql = new StringBuffer("from TargetDefine ot");								
		StringBuffer where = new StringBuffer("");
		
		if (targetDefineForm != null) {		
			// 查找条件的判断,查找名称不可为空			
			if (targetDefineForm.getDefineName() != null && !targetDefineForm.getDefineName().equals(""))			
				where.append((where.toString().equals("")?"":" and ") + "ot.defineName like '%" + targetDefineForm.getDefineName() + "%'");
								
			if(targetDefineForm.getBusinessId() != null && !targetDefineForm.getBusinessId().equals(""))					
				where.append((where.toString().equals("") ? "" : " and ") + "ot.mbusiness.businessId = " + targetDefineForm.getBusinessId());				
				
			if(targetDefineForm.getNormalId() != null && !targetDefineForm.getNormalId().equals(""))			
				where.append((where.toString().equals("") ? "" : " and ") + "ot.mnormal.normalId = " + targetDefineForm.getNormalId());		   
		}
		try {	    
			hql.append((where.toString().equals("")?"":" where ") + where.toString()).append(" order by ot.targetDefineId");	    	
			//conn对象的实例化		  	    	
			conn = new DBConn();	    	
			//打开连接开始会话	    	
			session = conn.openSession();
			
			Query query = session.createQuery(hql.toString());			
			List list = query.list();
			
			if (list != null){	    	
				refVals = new ArrayList();	    		  
				//循环读取数据库符合条件记录			    
				for(Iterator it = list.iterator(); it.hasNext(); ) {			    
					TargetDefineForm orgTypeFormTemp = new TargetDefineForm();			    	
					TargetDefine orgTypePersistence = (TargetDefine)it.next();			        
					TranslatorUtil.copyPersistenceToVo(orgTypePersistence, orgTypeFormTemp);
			        
					//取得该机构是否选中			        
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
			//如果连接存在，则断开，结束会话，返回	    	
			if(conn != null) conn.closeSession();	     
		}	    
		return refVals;	   
	}
	
	/**
	 * 根据指标名称和版本号查询
	 * @param defineName
	 * @param version
	 * @return
	 */
	public static List selectTargetDefine(String defineName,String version){
        List refVals = null;		
		
		//连接对象和会话对象初始化		
		DBConn conn = null;						
		Session session = null;		   
		
		//查询条件HQL的生成		
		String hql = new String("from TargetDefine ot where ot.defineName='"+defineName+"' and ot.version='"+version+"'");								
          
			//conn对象的实例化		  	    	
			conn = new DBConn();	    	
			//打开连接开始会话	    	
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
			//如果连接存在，则断开，结束会话，返回	    	
			if(conn != null) conn.closeSession();	     
		}	      
	}
}