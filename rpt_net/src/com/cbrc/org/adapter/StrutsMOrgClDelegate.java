
package com.cbrc.org.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.org.form.MOrgClForm;
import com.cbrc.org.hibernate.MOrgCl;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.util.FitechException;
import com.fitech.net.form.OrgNetForm;
import com.fitech.net.hibernate.OrgNet;
/**
 * This is a delegate class to handle interaction with the backend persistence layer of hibernate. 
 * It has a set of methods to handle persistence for MOrgCl data (i.e. 
 * com.cbrc.org.form.MOrgClForm objects).
 * 
 * @author <strong>Generated by Middlegen.</strong>
 */
public class StrutsMOrgClDelegate {

	private static FitechException log=new FitechException(StrutsMOrgClDelegate.class);
	
	/**
	 * 根据机构类别ID获取机构类别名称
	 * 
	 * @author rds
	 * @date 2006-02-06
	 * 
	 * @param orgClsId String 
	 * @return String
	 */
	public static String getOrgClsName(String orgCls){
		String orgClsName="";
		
		if(orgCls==null) return orgClsName;
		
		DBConn conn=null;
		
		try{
			conn=new DBConn();
			
			Object obj=conn.openSession().get(MOrgCl.class,orgCls);
			
			if(obj!=null){
				MOrgCl mOrgCl=(MOrgCl)obj;
				orgClsName=mOrgCl.getOrgClsNm();
			}
		}catch(HibernateException he){
			log.printStackTrace(he);
			orgClsName="";
		}catch(Exception e){
			log.printStackTrace(e);
			orgClsName="";
		}finally{
			if(conn!=null) conn.closeSession();
		}
		
		return orgClsName;
	}
	
	/**
	 * 在org中是否有orgClId
	 * 
	 * @author zhangxinke
	 */
	public static int getorgClsId(String orgClId) {
		int result = 0;
		DBConn conn = null;
		Session session = null;
		try {
			conn = new DBConn();
			session = conn.openSession();
			if(orgClId==null||orgClId.equals(""))
				return -1;
			
			String hql =  "select count(*) from MOrgCl op where op.orgClsId='"+ orgClId.toString()+"'";
			
			//// System.out.println("========="+hql);
			Query query = session.createQuery(hql);
			
			//// System.out.println("query");
			List list = query.list();
			if (list != null && list.size() != 0) {
				result =((Integer) list.get(0)).intValue();
			}
		} catch (Exception e) {			
			log.printStackTrace(e);
			
			
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return result;
	}
	
	
	/**
	 * 在org中是否有orgClId
	 * 
	 * @author 
	 */
	public static boolean getMOrgFromDeptId(String orgClId) {
		boolean result = false;
		DBConn conn = null;
		Session session = null;
		try {
			conn = new DBConn();
			session = conn.openSession();
			String hql = "select * from MOrgCl op where op.MOrgCl.orgClsId="
					+ orgClId;
			Query query = session.createQuery(hql);

			List list = query.list();
			if (list != null && list.size() != 0) {
				result =true;
			}
		} catch (Exception e) {			
			log.printStackTrace(e);
			
			
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return result;
	}
	
	/**
	 * 根据机构串获取所对应的机构类型列表
	 * 
	 * @author rds
	 * @serialData 2005-12-10
	 * 
	 * @param orgCls String 机构分类串(逗号分隔)
	 * @return List
	 */
	public static List findOrgCls(String orgCls){
		List resList=null;
		
		if(orgCls==null || orgCls.equals("")) return resList;
		
		DBConn conn=null;
		
		try{
			String hql="from MOrgCl moc where moc.orgClsId in (" + orgCls + ")";
			// System.out.println(hql);
			conn=new DBConn();
			
			List list=conn.openSession().find(hql);
			if(list!=null && list.size()>0){
				resList=new ArrayList();
				for(int i=0;i<list.size();i++){
					if(list.get(i)!=null){
						MOrgCl mOrgCl=(MOrgCl)list.get(i);
						MOrgClForm form=new MOrgClForm();
						TranslatorUtil.copyPersistenceToVo(mOrgCl,form);
						resList.add(form);
					}
				}
			}
		}catch(HibernateException he){
			log.printStackTrace(he);
			resList=null;
		}catch(Exception e){
			log.printStackTrace(e);
			resList=null;
		}finally{
			if(conn!=null) conn.closeSession();
		}
		
		return resList;
	}
	
   /**
    * Create a new com.cbrc.org.form.MOrgClForm object and persist (i.e. insert) it.
    *
    * @param   mOrgClForm   The object containing the data for the new com.cbrc.org.form.MOrgClForm object
    * @exception   Exception   If the new com.cbrc.org.form.MOrgClForm object cannot be created or persisted.
    */
	
	 /**
	    *  插入一条部门记录
	    *   
	    * @param   departmentForm  包含要插入的部门名称
	    * @exception   Exception   If the new com.cbrc.auth.form.DepartmentForm object cannot be created or persisted.
	    */
	   public static boolean create (MOrgClForm mOrgClForm) throws Exception {
	       
	       boolean result = false;
	      DBConn conn =null;
	      Session session =null;
	      if(mOrgClForm!=null)
	      {
	          try
	          {
	              conn = new DBConn();
	              session = conn.beginTransaction(); 
	              
	              MOrgCl mOrgCl = new MOrgCl();
	              mOrgCl.setOrgClsId(mOrgClForm.getOrgClsId());
	              mOrgCl.setOrgClsNm(mOrgClForm.getOrgClsNm());
	              mOrgCl.setOrgClsType(mOrgClForm.getOrgClsType());
	    //          dept.setProductUser(StrutsProductUserDelegate.getCurrentSysUser());
//	              dept.setProductUser(StrutsProductUserDelegate.getCurrentSysUser());
	              session.save(mOrgCl);
	             // conn.endTransaction(true);
	              // System.out.println(mOrgCl);
	              session.flush();
	              // System.out.println(mOrgCl);
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

   /*public static MOrgClForm  create (MOrgClForm mOrgClForm) throws Exception {
	   List list=null;				//置result标记
	   MOrgCl mOrgClPersistence = new MOrgCl ();
	   	   
	   if (mOrgClForm==null || mOrgClForm.getOrgClsId().equals("")) 
	   {
		   return mOrgClForm;
	   }
	   //连接对象的初始化
	   DBConn conn=null;
	   //会话对象的初始化
	   Session session=null;
	   
	   try
	   {
		   //表示层到持久层的CopyTo方法(McurUnitPresistence持久层对象的实例,mCurrForm表示层对象)
		   if (mOrgClForm.getOrgClsId()==null  || mOrgClForm.getOrgClsId().equals(""))
		   {
			   return mOrgClForm;
		   }
		
		   TranslatorUtil.copyVoToPersistence(mCurrPersistence,mCurrForm);
		   //实例化连接对象
		   conn =new DBConn();
		   //会话对象为连接对象的事务属性
		   session=conn.beginTransaction();
		   	//mCurrForm对象的实例化   
		    mCurrForm=new MCurrForm();
		   mCurrForm.getCurName();
		   
		   //会话对象保存持久层对象
		   session.save(mCurrPersistence);
		   //标志为true
		   result=true;
	   }
	   catch(HibernateException e)
	   {
		   //持久层的异常被捕捉
		   log.printStackTrace(e);
	   }
	   finally{
		   //如果连接状态有,则断开,结束事务,返回
		   if(conn!=null) conn.endTransaction(result);
	   }
	   return result;
	   
   }*/

	
	   

   /**
    * Update (i.e. persist) an existing com.cbrc.org.form.MOrgClForm object.
    *
    * @param   mOrgClForm   The com.cbrc.org.form.MOrgClForm object containing the data to be updated
    * @exception   Exception   If the com.cbrc.org.form.MOrgClForm object cannot be updated/persisted.
    */
   public static com.cbrc.org.form.MOrgClForm update (com.cbrc.org.form.MOrgClForm mOrgClForm) throws Exception {
      com.cbrc.org.hibernate.MOrgCl mOrgClPersistence = new com.cbrc.org.hibernate.MOrgCl ();
      TranslatorUtil.copyVoToPersistence(mOrgClPersistence, mOrgClForm);
      javax.naming.InitialContext ctx = new javax.naming.InitialContext();
      // TODO: Make adapter get SessionFactory jndi name by ant task argument?
      net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory)ctx.lookup("java:AirlineHibernateFactory");
      net.sf.hibernate.Session session = factory.openSession();
      net.sf.hibernate.Transaction tx = session.beginTransaction();
      session.update(mOrgClPersistence);
      tx.commit();
      session.close();
      TranslatorUtil.copyPersistenceToVo(mOrgClPersistence, mOrgClForm);
      return mOrgClForm;
   }
   
   /**
    * 修改部门信息
    * @author 姚捷
    * @param   departmentForm  DepartmentForm 包含需要更新的部门id和更新的部门名称
    * @return boolean 修改是否成功
    * @exception   Exception   
    */
   public static boolean update2 (MOrgClForm mOrgClForm) throws Exception {
    
       boolean result = false;
       DBConn conn =null;
       Session session =null;
       if(mOrgClForm!=null)
       {
           try
           {
               conn = new DBConn();
               session = conn.beginTransaction(); 
               
               MOrgCl mOrgCl = (MOrgCl)session.load(MOrgCl.class,mOrgClForm.getOrgClsId());
               mOrgCl.setOrgClsNm((mOrgClForm.getOrgClsNm()));
               mOrgCl.setOrgClsType((mOrgClForm.getOrgClsType()));
               // System.out.println("The mOrgCl is" +  mOrgCl );              
               session.update(mOrgCl);
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
    * Retrieve an existing com.cbrc.org.form.MOrgClForm object for editing.
    *
    * @param   mOrgClForm   The com.cbrc.org.form.MOrgClForm object containing the data used to retrieve the object (i.e. the primary key info).
    * @exception   Exception   If the com.cbrc.org.form.MOrgClForm object cannot be retrieved.
    */
   public static com.cbrc.org.form.MOrgClForm edit (com.cbrc.org.form.MOrgClForm mOrgClForm) throws Exception {
      com.cbrc.org.hibernate.MOrgCl mOrgClPersistence = new com.cbrc.org.hibernate.MOrgCl ();
      TranslatorUtil.copyVoToPersistence(mOrgClPersistence, mOrgClForm);
javax.naming.InitialContext ctx = new javax.naming.InitialContext();
// TODO: Make adapter get SessionFactory jndi name by ant task argument?
net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory)ctx.lookup("java:AirlineHibernateFactory");
net.sf.hibernate.Session session = factory.openSession();
net.sf.hibernate.Transaction tx = session.beginTransaction();
mOrgClPersistence = (com.cbrc.org.hibernate.MOrgCl)session.load(com.cbrc.org.hibernate.MOrgCl.class, mOrgClPersistence.getOrgClsId());
tx.commit();
session.close();
      TranslatorUtil.copyPersistenceToVo(mOrgClPersistence, mOrgClForm);
      return mOrgClForm;
   }

   /**
    * Remove (delete) an existing com.cbrc.org.form.MOrgClForm object.
    *
    * @param   mOrgClForm   The com.cbrc.org.form.MOrgClForm object containing the data to be deleted.
    * @exception   Exception   If the com.cbrc.org.form.MOrgClForm object cannot be removed.
    */  
   public static void remove(com.cbrc.org.form.MOrgClForm mOrgClForm) throws Exception {
      com.cbrc.org.hibernate.MOrgCl mOrgClPersistence = new com.cbrc.org.hibernate.MOrgCl ();
      TranslatorUtil.copyVoToPersistence(mOrgClPersistence, mOrgClForm);
javax.naming.InitialContext ctx = new javax.naming.InitialContext();
// TODO: Make adapter get SessionFactory jndi name by ant task argument?
net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory)ctx.lookup("java:AirlineHibernateFactory");
net.sf.hibernate.Session session = factory.openSession();
net.sf.hibernate.Transaction tx = session.beginTransaction();
// TODO: is this really needed?
mOrgClPersistence = (com.cbrc.org.hibernate.MOrgCl)session.load(com.cbrc.org.hibernate.MOrgCl.class, mOrgClPersistence.getOrgClsId());
session.delete(mOrgClPersistence);
tx.commit();
session.close();
   }

   /**
    * 删除机构分类信息
    * @author 张新科
    * @param   
    * @return  boolean 删除是否成功
    * @exception   Exception   If the com.cbrc.auth.form.DepartmentForm object cannot be removed.
    */  
   public static boolean remove2(MOrgClForm mOrgClForm) throws Exception {
     boolean result = false;
     
     DBConn conn =null;
     Session session =null;
     if(mOrgClForm!=null)
     {
         try
         {
             conn = new DBConn();
             session = conn.beginTransaction(); 
             
             MOrgCl mOrgCl = (MOrgCl)session.load(MOrgCl.class,mOrgClForm.getOrgClsId());
             session.delete(mOrgCl);
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
    * 取得所有机构分类类型记录
    *
    * @exception   Exception   If the com.cbrc.org.form.MOrgClForm objects cannot be retrieved.
    */
   public static List findAll () throws Exception {
      List retVals = null;
      
      com.cbrc.org.dao.DBConn conn=null;
      
      try{
    	  conn=new com.cbrc.org.dao.DBConn();
    	 Session session = conn.openSession();
          
          Query query = session.createQuery(" from MOrgCl ");
    	  List list=query.list();
          if(list!=null && list.size()>0){
    		  retVals=new ArrayList();
    		  for(int i=0;i<list.size();i++){
    			  MOrgCl mOrgCl = (MOrgCl)list.get(i);
    			  MOrgClForm mOrgClForm = new MOrgClForm();
    			  TranslatorUtil.copyPersistenceToVo(mOrgCl,mOrgClForm);
    			  retVals.add(mOrgClForm);
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
    * 取得所有机构分类类型记录
    *
    * @exception   Exception   If the com.cbrc.org.form.MOrgClForm objects cannot be retrieved.
    */
   public static List findAllOrgList () throws Exception {
      List retVals = null;
      
      DBConn conn=null;	
      try{

    					      
	     conn=new DBConn();
    	  List list=conn.openSession().find(" from OrgNet ");
          if(list!=null && list.size()>0){
    		  retVals=new ArrayList();
    		  for(int i=0;i<list.size();i++){
    			  OrgNet orgNet = (OrgNet)list.get(i);
    			  OrgNetForm orgNetForm = new OrgNetForm();
    			  TranslatorUtil.copyPersistenceToVo(orgNet,orgNetForm);
    			  retVals.add(orgNetForm);
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
    * 获取机构类别信息列表
    * 
    * @param 机构类别ID字符串
    * @List 
    */
    public static List findAll (String orgClsIds) throws Exception {
	      List retVals = null;
	      
	      DBConn conn=null;
	      
	      try{
	    	  conn=new DBConn();
	    	  List list=conn.openSession().find("from MOrgCl moc where moc.orgClsId in (" + orgClsIds + ")");

	    	  if(list!=null && list.size()>0){
	    		  retVals=new ArrayList();
	    		  for(int i=0;i<list.size();i++){
	    			  MOrgCl mOrgCl = (MOrgCl)list.get(i);
	    			  MOrgClForm mOrgClForm = new MOrgClForm();
	    			  TranslatorUtil.copyPersistenceToVo(mOrgCl,mOrgClForm);
	    			  retVals.add(mOrgClForm);
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
     * 分页显示记录
     * @author zhangxinke
     * @param  offset int 偏移量
     * @param  limit int 取最大记录数
     * @return  List 记录集合
     * @exception   Exception  
     */
    public static List select (int offset,int limit) throws Exception {
       List result = null;
       DBConn conn = null;
       Session session = null;
       try
       {
           conn = new DBConn();
           session = conn.openSession();
       
           Query query = session.createQuery("from MOrgCl moc");
           query.setFirstResult(offset);
           query.setMaxResults(limit);
           
           List list = query.list();
           if(list!=null && list.size()!=0)
           {
        	  
        	   result = new ArrayList();
               for(Iterator it = list.iterator(); it.hasNext();)
               {
                   MOrgClForm departmentFormTemp = new MOrgClForm();
                   MOrgCl departmentPersistence = (MOrgCl)it.next();
                   TranslatorUtil.copyPersistenceToVo(departmentPersistence, departmentFormTemp);
                   result.add(departmentFormTemp);         
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
       return result;
    }
    
   /**
    * Retrieve a set of existing com.cbrc.org.form.MOrgClForm objects for editing.
    *
    * @param   mOrgClForm   The com.cbrc.org.form.MOrgClForm object containing the data used to retrieve the objects (i.e. the criteria for the retrieval).
    * @exception   Exception   If the com.cbrc.org.form.MOrgClForm objects cannot be retrieved.
    */
   public static List select (MOrgClForm mOrgClForm,int offset,int limit) throws Exception {
//		 List集合的定义 
	   List refVals=null;		
		   
	   //连接对象和会话对象初始化
	   DBConn conn=null;				
	   Session session=null;
	   
	   //	 查询条件HQL的生成
	   StringBuffer hql = new StringBuffer("from MOrgCl moc ");						
	   StringBuffer where = new StringBuffer("");
	   
	   if (mOrgClForm == null) {
		   // 查找条件的判断,查找名称不可为空
		return refVals;
	   }
		  
      try
      {	   //List集合的操作
    	  //初始化
    	  hql.append(where.toString());
    	  //conn对象的实例化		  
    	  conn=new DBConn();
    	  //打开连接开始会话
    	  session=conn.openSession();
    	  //添加集合至Session
		  //List list=session.find(hql.toString());
		  Query query=session.createQuery(hql.toString());
		  query.setFirstResult(offset).setMaxResults(limit);
		  List list=query.list();
		  
    	  if (list!=null){
    		  refVals = new ArrayList();
    		  //循环读取数据库符合条件记录
		      for (Iterator it = list.iterator(); it.hasNext(); ) {
		         MOrgClForm mOrgClFormTemp = new MOrgClForm();
		         MOrgCl mOrgClPersistence = (MOrgCl)it.next();
		         TranslatorUtil.copyPersistenceToVo(mOrgClPersistence, mOrgClFormTemp);
		         refVals.add(mOrgClFormTemp);
		      }
    	   }
      }catch(HibernateException he){
    	  refVals=null;
    	  log.printStackTrace(he);
      }catch(Exception e){
    	  refVals=null;  
    	  log.printStackTrace(e);
      }finally{
    	  //如果连接存在，则断开，结束会话，返回
    	  if(conn!=null) conn.closeSession();
      }
         return refVals;
   }
   
   public static int getRecordCount() throws Exception
   {
       int result =0;
       DBConn conn =null;
       Session session =null;
       try
       {
           conn = new DBConn();
           session = conn.openSession();
           
           Query query = session.createQuery("select count(*) from MOrgCl dept");
           List list = query.list();
     
           if(list!=null && list.size()!=0)
               result = ((Integer)list.get(0)).intValue();
           
       }
       catch(Exception e)
       {
           log.printStackTrace(e);
           result = 0;
       }
       finally{
           if(conn!=null)
               conn.closeSession();
       }
      
       return result;
   }
   
   /**
    * 取得按条件查询到的记录条数
    * @return int 查询到的记录条数	
    * @param   mCurrForm   包含查询的条件信息（子机构名称ID，子机构类型名称）
    */
   
   public static int getRecordCount(MOrgClForm mOrgClForm)
   {
	   int count=0;
	   
	   //连接对象和会话对象初始化
	   DBConn conn=null;				
	   Session session=null;
	   
	   //	 查询条件HQL的生成
	   StringBuffer hql = new StringBuffer("select count(*) from MOrgCl moc");						
	   StringBuffer where = new StringBuffer("");
	   
	   if (mOrgClForm != null) {
		   // 查找条件的判断,查找名称不可为空
		   return count;
		}
		  
      try
      {	   //List集合的操作
    	  //初始化
    	  hql.append(where.toString());
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
    * 根据从StrutsMOrgDelegate中传来的orgClsId查询orgClsNm
    * 唐磊
    */

   public static List selectOrgClsNm(String orgClsId){
	   List list=null;
	   DBConn conn=null;
	   Session session=null;
	   List retVals=new ArrayList();
	   if (orgClsId!=null){
		   try{
			   String hql="from MOrgCl moc where 1=1";
			   hql+=" and moc.orgClsId='"+orgClsId+"'";
			   
			   conn=new DBConn();
			   session=conn.openSession();
			   list=session.find(hql.toString());
			   
			   if(list!=null&&list.size()>0){
				   MOrgCl mOrgClPersistence=(MOrgCl)list.get(0);
				   retVals.add(mOrgClPersistence);
			   }
		   }catch(HibernateException he){
			   log.printStackTrace(he);
		   }catch(Exception e){
			   log.printStackTrace(e);
		   }finally{
			   if(conn!=null)conn.closeSession();
		   }
	   }
	   return retVals;
   }
   
   public static boolean UpdateOrgClsName(MOrgClForm mOrgClForm){
	   boolean result=false;
	   DBConn conn=null;
	   Session session=null;
	   
	   if(mOrgClForm!=null && mOrgClForm.equals("")){
		   return result;
	   }
	  
	   try{
		   conn=new DBConn();
		   session=conn.beginTransaction();
		   if(mOrgClForm.getOrgClsId()!=null && !mOrgClForm.getOrgClsId().equals("")
				   && mOrgClForm.getOrgClsNm()!=null && !mOrgClForm.getOrgClsNm().equals("")){
			   MOrgCl mOrgClPersistence=(MOrgCl)session.get(MOrgCl.class, mOrgClForm);
			   session.update(mOrgClPersistence);
			   result=true;
		   }
	   }catch(HibernateException he){
		   log.printStackTrace(he);
	   }catch(Exception e){
		   log.printStackTrace(e);
	   }finally{
		   if(conn!=null)conn.endTransaction(result);
	   }
	   return result;
   }
   
  
	
	
	
   
}