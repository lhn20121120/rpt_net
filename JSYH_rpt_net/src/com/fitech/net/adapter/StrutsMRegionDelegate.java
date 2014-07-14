
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
 *@StrutsMRegionDelegate  地区Delegate
 * 
 * @author jcm
 */
public class StrutsMRegionDelegate {
	//Catch到本类的抛出的所有异常
	private static FitechException log=new FitechException(StrutsMRegionDelegate.class);

	/**
	 * 新增地区
	 * @param mRegionForm MRegionForm
     * @exception Exception，捕捉异常处理 
     * @return int result  0--操作失败  1--操作成功  2--要添加的指定级别的机构类别已经存在
     */
   public static int create (MRegionForm mRegionForm) throws Exception {
	
	   int result = 0;				//置result标记
	   boolean bool = false;
	   MRegion mRegionPersistence = new MRegion();
	   	   
	   if (mRegionForm == null || mRegionForm.getRegion_name().equals("")) 
		   return result;
	   
	   //连接对象的初始化
	   DBConn conn=null;
	   //会话对象的初始化
	   Session session=null;
	   try{		   
		   TranslatorUtil.copyVoToPersistence(mRegionPersistence,mRegionForm);
		   //实例化连接对象
		   conn =new DBConn();
		   //会话对象为连接对象的事务属性
		   session=conn.beginTransaction();
		   
		   String hql = "from MRegion mr where mr.regionName='" + mRegionForm.getRegion_name() + "' and mr.preRegionId = '" + mRegionForm.getPre_region_id()+"'";
		   List list = session.find(hql);
		   
		   if(list != null && list.size() > 0){
			   result = 2;        //要增加的机构地区已经存在
			   bool = true;
			   return result;
		   }
		
		   //会话对象保存持久层对象
		   session.save(mRegionPersistence);
		   session.flush();
		   
		   result=1;       //增加成功
		   bool = true;
		   
	   }
	   catch(HibernateException e){
		   //持久层的异常被捕捉
		   result = 0;
		   bool = false;
		   log.printStackTrace(e);
	   }
	   finally{
		   //如果连接状态有,则断开,结束事务,返回
		   if(conn!=null) conn.endTransaction(bool);
	   }
	   return result;
	   
   }

   /**
    * 取得按条件查询到的记录条数
    * @return int 查询到的记录条数	
    * @param   mRegionForm   包含查询的条件信息（地区ID，地区名称）
    */
   
   public static int getRecordCount(MRegionForm mRegionForm)
   {
	   int count=0;
	   
	   //连接对象和会话对象初始化
	   DBConn conn=null;				
	   Session session=null;
	   
	   //查询条件HQL的生成
	   StringBuffer hql = new StringBuffer("select count(*) from MRegion mr");						
	   StringBuffer where = new StringBuffer("");
	   
	   if (mRegionForm != null) {
		   // 查找条件的判断,查找名称不可为空
			if (mRegionForm.getRegion_name() != null && !mRegionForm.getRegion_name().equals(""))
				where.append((where.toString().equals("")?"":" and ") + "mr.regionName like '%" + mRegionForm.getRegion_name() + "%'");
	   }
		  
      try {
    	  hql.append((where.toString().equals("")?"":" where ") + where.toString());
    	  //conn对象的实例化		  
    	  conn=new DBConn();
    	  //打开连接开始会话
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
    	  //如果连接存在，则断开，结束会话，返回
    	  if(conn!=null) conn.closeSession();
      }
         return count;
   }
   
   /**
    * 取得按条件查询到的记录条数
    * @return int 查询到的记录条数	
    * @param   mRegionForm   包含查询的条件信息（地区ID，地区名称）
    */
   
   public static int getRecordCount(MRegionForm mRegionForm,Operator operator)
   {
	   int count=0;
	   
	   //连接对象和会话对象初始化
	   DBConn conn=null;				
	   Session session=null;
	   
	   //查询条件HQL的生成
	   StringBuffer hql = new StringBuffer("select count(*) from MRegion mr");						
	   StringBuffer where = new StringBuffer("");
	   
	   if (mRegionForm != null) {
		   // 查找条件的判断,查找名称不可为空
			if (mRegionForm.getRegion_name() != null && !mRegionForm.getRegion_name().equals(""))
				where.append((where.toString().equals("")?"":" and ") + "mr.regionName like '%" + mRegionForm.getRegion_name() + "%'");
	   }
		 
	   where.append((where.toString().equals("") ? "" : " and ") + "mr.setOrgId = '" + operator.getOrgId() + "'");
	  
	   try {
		   hql.append((where.toString().equals("")?"":" where ") + where.toString());
		   //conn对象的实例化		  
		   conn=new DBConn();
		   //打开连接开始会话
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
		   //如果连接存在，则断开，结束会话，返回
		   if(conn!=null) conn.closeSession();
	   }
	   return count;
   }
   
   /**
    * 地区查询
    * 
    * @param mRegionForm MRegionForm 查询表单对象
    * @return List 如果查找到记录，返回List记录集；否则，返回null
    */
   public static List select(MRegionForm mRegionForm,int offset,int limit){
	   
	   //List集合的定义 
	   List refVals = null;		
		   
	   //连接对象和会话对象初始化
	   DBConn conn = null;				
	   Session session = null;
	   
	   //	 查询条件HQL的生成
	   StringBuffer hql = new StringBuffer("from MRegion mr");						
	   StringBuffer where = new StringBuffer("");
	 
	   if (mRegionForm != null) {
		   // 查找条件的判断,查找名称不可为空
			if (mRegionForm.getRegion_name() != null && !mRegionForm.getRegion_name().equals(""))
				where.append((where.toString().equals("")?"":" and ") + "mr.regionName like '%" + mRegionForm.getRegion_name() + "%'");
	   }
		  
      try {
    	  hql.append((where.toString().equals("")?"":" where ") + where.toString());
    	  //conn对象的实例化		  
    	  conn = new DBConn();
    	  //打开连接开始会话
    	  session = conn.openSession();
		  Query query = session.createQuery(hql.toString());
		  query.setFirstResult(offset).setMaxResults(limit);
		  List list = query.list();
		  
    	  if (list != null){
    		  refVals = new ArrayList();
    		  //循环读取数据库符合条件记录
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
    	  //如果连接存在，则断开，结束会话，返回
    	  if(conn != null) conn.closeSession();
      }
         return refVals;
   }
   
   /**
    * 地区查询
    * 
    * @param mRegionForm MRegionForm 查询表单对象
    * @return List 如果查找到记录，返回List记录集；否则，返回null
    */
   public static List select(MRegionForm mRegionForm,int offset,int limit,Operator operator){
	   
	   //List集合的定义 
	   List refVals = null;		
		   
	   //连接对象和会话对象初始化
	   DBConn conn = null;				
	   Session session = null;
	   
	   //	 查询条件HQL的生成
	   StringBuffer hql = new StringBuffer("from MRegion mr");						
	   StringBuffer where = new StringBuffer("");
	 
	   if (mRegionForm != null) {
		   // 查找条件的判断,查找名称不可为空
			if (mRegionForm.getRegion_name() != null && !mRegionForm.getRegion_name().equals(""))
				where.append((where.toString().equals("")?"":" and ") + "mr.regionName like '%" + mRegionForm.getRegion_name() + "%'");
	   }
	
	   where.append((where.toString().equals("") ? "" : " and ") + "mr.setOrgId = '" + operator.getOrgId() + "'");
	   try {
		   hql.append((where.toString().equals("")?"":" where ") + where.toString());
		   //conn对象的实例化		  
		   conn = new DBConn();
		   //打开连接开始会话
		   session = conn.openSession();
		   Query query = session.createQuery(hql.toString());
		   query.setFirstResult(offset).setMaxResults(limit);	  
		   List list = query.list();
		  
		   if (list != null){
			   refVals = new ArrayList();
			   //循环读取数据库符合条件记录		      
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
		   //如果连接存在，则断开，结束会话，返回   	  
		   if(conn != null) conn.closeSession();      
	   } 
	   return refVals;
   }
   
   
   /**
    * 更新MRegionForm对象
    *
    * @param   mRegionForm   MRegionForm 存放数据的对象
    * @exception   Exception  如果MRegionForm更新失败,则捕捉抛出异常
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
	 * 编辑操作
	 * 
	 * @param mRegionForm  The MRegionForm  保持数据的传递
	 * @exception Exception 如果 MRegionForm 对象丢失则抛出异常.
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
    * 删除操作
    *
    * @param   mRegionForm   MRegionForm 查询表单的对象
    * @return   boolean  如果删除成功则返回true,否则false
    */  
   public static boolean remove (MRegionForm mRegionForm) throws Exception {
       //置标志result
	   boolean result=false;
	   //连接和会话对象的初始化
	   DBConn conn=null;
	   Session session=null;
	 
	   //mRegionForm是否为空,返回result
	   if (mRegionForm == null || mRegionForm.getRegion_id() == null) 
		   return result;
	  
	     try{
	       //	连接对象和会话对象初始化
		   conn=new DBConn();
		   session=conn.beginTransaction();
		   
		   MRegion mRegion=(MRegion)session.load(MRegion.class,mRegionForm.getRegion_id());
		   //会话对象删除持久层对象
		   session.delete(mRegion);
		   session.flush();
		   
		   //删除成功，置为true
		   result=true;
		  }
	   catch(HibernateException he){
		   //捕捉本类的异常,抛出
		   log.printStackTrace(he);
	   }finally{
		   //如果由连接则断开连接，结束会话，返回
		   if (conn!=null) conn.endTransaction(result);
	   }
	     return result;
   }

   
	 /**
	  * 查询一条记录
	  * @param mRegionForm MRegionForm实例化对象
	  * @return MRegionForm 包含一条记录
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
			   //如果连接存在，则断开，结束会话，返回
			   if(conn != null) conn.closeSession();
		   }
	   }
	   return mRegionForm;
   }



	/**
	 * 已使用hibernate 卞以刚 2011-12-22
	 * 查询所有记录
	 * @return List 查询到的记录条数	
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
			//如果连接存在，则断开，结束会话，返回
			if(conn != null) conn.closeSession();
		}
		return refVals;
	}
	
	/**
	 * 查找上级地区
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
			//如果连接存在，则断开，结束会话，返回
			if(conn != null) conn.closeSession();
		}
		return mRegionResult;
	}
	
	/**
	 * 查找一条记录
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
			//如果连接存在，则断开，结束会话，返回
			if(conn != null) conn.closeSession();
		}
		return mRegionResult;
	}
	
	/**
	 * 查找属于同一类型机构的地区
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
			//如果连接存在，则断开，结束会话，返回
			if(conn != null) conn.closeSession();
		}
		return resultList;
	}
	
	/**
	 * 查询下一级子地区
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
	 * 查询下一级子地区
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
	 * 根据机构查询出该机构的下级地区
	 * 
	 * @param 机构ID 查询表单对象
	 * @return  list 如果查找到记录，返回list；否则，返回null
	 */	   
	public static List getNextRegionList(String orgId){    
		List rlist=null;		
		if(orgId==null) return null;
		
		//连接对象和会话对象初始化		
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
				//循环读取数据库符合条件记录			    
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
			//如果连接存在，则断开，结束会话，返回	    	
			if(conn != null) conn.closeSession();	    
		}	    
		return  rlist;	   
	}
}
