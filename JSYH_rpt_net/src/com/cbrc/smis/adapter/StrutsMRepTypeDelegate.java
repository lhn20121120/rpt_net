
package com.cbrc.smis.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.form.MRepTypeForm;
import com.cbrc.smis.hibernate.MRepType;
import com.cbrc.smis.util.FitechException;
/**
 * @StrutsMRepTypeDelegate  报表类型表单Delegate
 * 
 * @author 唐磊
 */
public class StrutsMRepTypeDelegate {
	//Catch到本类的抛出的所有异常
	private static FitechException log=new FitechException(StrutsMRepTypeDelegate.class);

/**
    * 报表类别新增
    * 
    * @param mRepTypeForm MRepTypeForm
    * @return boolean result,新增成功返回true,否则返回false
    * @exception Exception，捕捉异常处理
    */
   public static  boolean create (MRepTypeForm mRepTypeForm) throws Exception {
	
	   boolean result=false;				//置result标记
	   MRepType mRepTypePersistence = new MRepType ();
	   	   
	   if (mRepTypeForm==null ) 
	   {
		   return  result;
	   }
	   //连接对象的初始化
	   DBConn conn=null;
	   //会话对象的初始化
	   Session session=null;
	   
	   try
	   {
		   //表示层到持久层的CopyTo方法(McurUnitPresistence持久层对象的实例,mCurUnitForm表示层对象)
		   if (mRepTypeForm.getRepTypeName()==null  || mRepTypeForm.getRepTypeName().equals(""))
		   {
			   return result;
		   }
		   
		   TranslatorUtil.copyVoToPersistence(mRepTypePersistence,mRepTypeForm);
		   //实例化连接对象
		   conn =new DBConn();
		   //会话对象为连接对象的事务属性
		   session=conn.beginTransaction();
		  
		   //会话对象保存持久层对象
		   session.save(mRepTypePersistence);
		   session.flush();
		   TranslatorUtil.copyPersistenceToVo(mRepTypePersistence,mRepTypeForm);
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
	   
   }

   /**
    * 取得按条件查询到的记录条数
    * @return int 查询到的记录条数	
    * @param   mRepTypeForm   包含查询的条件信息（报表类别，报表类别名称）
    */
   
   public static int getRecordCount(MRepTypeForm mRepTypeForm)
   {
	   int count=0;
	   
	   //连接对象和会话对象初始化
	   DBConn conn=null;				
	   Session session=null;
	   
	   //	 查询条件HQL的生成
	   StringBuffer hql = new StringBuffer("select count(*) from MRepType mrt");						
	   StringBuffer where = new StringBuffer("");
	   
	   if (mRepTypeForm != null) {
		   // 查找条件的判断,查找名称不可为空
			if (mRepTypeForm.getRepTypeName() != null && !mRepTypeForm.getRepTypeName().equals(""))
				where.append((where.toString().equals("")?"":" and ") + "mrt.repTypeName like '%" + mRepTypeForm.getRepTypeName()+ "%'");
	   }
		  
      try
      {	   //List集合的操作
    	  //初始化
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
    * 查找报表类别
    * 
    * @param mRepTypeForm MRepTypeForm 查询表单对象
    * @return List 如果查找到记录，返回List记录集；否则，返回null
    */
   public static List select(MRepTypeForm mRepTypeForm,int offset,int limit){
	   
	   //	 List集合的定义 
	   List refVals=null;		
		   
	   //连接对象和会话对象初始化
	   DBConn conn=null;				
	   Session session=null;
	   
	   //	 查询条件HQL的生成
	   StringBuffer hql = new StringBuffer("from MRepType mrt ");						
	   StringBuffer where = new StringBuffer("");
	   
	   if (mRepTypeForm != null)
	   {
		   // 查找条件的判断,查找名称不可为空
		   if (mRepTypeForm.getRepTypeName() != null && !mRepTypeForm.getRepTypeName().equals(""))
				where.append((where.toString().equals("") ? "" : " and ") + "mrt.repTypeName like '%" + mRepTypeForm.getRepTypeName() + "%'");
	   }

      try
      {	   //List集合的操作
    	  //初始化
    	  hql.append((where.toString().equals("") ? "" : " where ") + where.toString());
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
		         MRepTypeForm mRepTypeFormTemp = new MRepTypeForm();
		         MRepType mRepTypePersistence = (MRepType)it.next();
		         TranslatorUtil.copyPersistenceToVo(mRepTypePersistence, mRepTypeFormTemp);
		         refVals.add(mRepTypeFormTemp);
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
   
   
   /**
    * 更新 MRepTypeForm 对象
    *
    * @param   mRepTypeForm  MRepTypeForm 对象的数据更新
    * @exception   Exception   如果MRepTypeForm 对象没有更新
    */
   public static boolean update (MRepTypeForm mRepTypeForm) throws Exception {
	   boolean result = false;
		DBConn conn = null;
		Session session = null;

		MRepType mRepTypePersistence = new MRepType();

		if (mRepTypeForm == null) {
			return result;
		}
		try {
			if (mRepTypeForm.getRepTypeName()== null
					|| mRepTypeForm.getRepTypeName().equals("")) {
				return result;
			}
			conn = new DBConn();
			session = conn.beginTransaction();

			TranslatorUtil.copyVoToPersistence(mRepTypePersistence,
					mRepTypeForm);
			session.update(mRepTypePersistence);

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
	 * @param mRepTypeForm  MRepTypeForm 返回一个数据的对象
	 * @exception Exception 如果MRepTypeForm 对象没有数据
	 */
   public static boolean  edit (MRepTypeForm mRepTypeForm) throws Exception {
	   boolean result=false;
	   DBConn conn=null;
	   Session session=null;
	  
	   MRepType mRepTypePersistence = new MRepType ();
	   mRepTypeForm=new MRepTypeForm();
	   mRepTypeForm.getRepTypeName();
	   
	   if (mRepTypeForm==null ) 
	   {
		   return  result;
	   }
	   
	   try
	   {
	   if (mRepTypeForm.getRepTypeName()==null && mRepTypeForm.getRepTypeName().equals(""))
	   {
		   return result;
	   }
	   conn=new DBConn();
	   session=conn.beginTransaction();
	   
	   
	   mRepTypePersistence = (MRepType)session.load(MRepType.class, mRepTypeForm.getRepTypeName());

	   TranslatorUtil.copyVoToPersistence(mRepTypePersistence, mRepTypeForm);
         
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
    * @param   mRepTypeForm   MRepTypeForm 查询表单的对象
    * @return   boolean  如果删除成功则返回true,否则false
    */  
   public static boolean remove (MRepTypeForm mRepTypeForm) throws Exception {
      //置标志result
	   boolean result=false;
	   //连接和会话对象的初始化
	   DBConn conn=null;
	   Session session=null;
	 
	   //mCurUnit是否为空,返回result
	   if (mRepTypeForm==null || mRepTypeForm.getRepTypeId()==null) return result;
	  
	     try{
	    	 //	连接对象和会话对象初始化
		   conn=new DBConn();
		   session=conn.beginTransaction();
		   //将mRepTypeForm的货币单位的货币主键传入HQL中查询
		   MRepType mRepType=(MRepType)session.load(MRepType.class,mRepTypeForm.getRepTypeId());
		   //会话对象删除持久层对象
		   session.delete(mRepType);
		   //session.flush();
		   
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
  * 查询一条记录,返回到EditAction中
  * @param mRepTypeForm MRepTypeForm
  * @return MRepTypeForm 包含一条记录
  */
   
public static MRepTypeForm selectOne(MRepTypeForm mRepTypeForm){
	   
	   //连接对象和会话对象初始化
	   DBConn conn=null;				
	   Session session=null;
	   	     
	   if (mRepTypeForm != null) 
		   // System.out.println("mRepTypeForm:"+mRepTypeForm.getRepTypeId()+"===="+mRepTypeForm.getRepTypeName());
      try
      {	   
    	  if (mRepTypeForm.getRepTypeId() != null && !mRepTypeForm.getRepTypeId().equals(""))
    	  //conn对象的实例化		  
    	  conn=new DBConn();
    	  //打开连接开始会话
    	  session=conn.openSession();
    	
    	  MRepType mRepTypePersistence = (MRepType)session.load(MRepType.class, mRepTypeForm.getRepTypeId());
    	 TranslatorUtil.copyPersistenceToVo(mRepTypePersistence, mRepTypeForm);
    	
      }catch(HibernateException he){
    	  log.printStackTrace(he);
      }catch(Exception e){
    	  log.printStackTrace(e);
      }finally{
    	  //如果连接存在，则断开，结束会话，返回
    	  if(conn!=null) conn.closeSession();
      }
	    return mRepTypeForm;
   }



   /**
    * 获取所有的报表类型信息列表
    *
    * @return List
    * @exception   Exception   If the MRepTypeForm objects cannot be retrieved.
    */
   public static List findAll () throws Exception {
      List retVals = null;
      
      DBConn conn=null;
      
      try{
    	  conn=new DBConn();
    	  
		  List list=conn.openSession().find("from MRepType");

	      if(list!=null && list.size()>0){
	    	  retVals=new ArrayList();
		      for (Iterator it = list.iterator(); it.hasNext(); ) {
		         MRepTypeForm mRepTypeFormTemp = new MRepTypeForm();
		         com.cbrc.smis.hibernate.MRepType mRepTypePersistence = (com.cbrc.smis.hibernate.MRepType)it.next();
		         TranslatorUtil.copyPersistenceToVo(mRepTypePersistence, mRepTypeFormTemp);
		         retVals.add(mRepTypeFormTemp);
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
    * 获取FR所有的报表类型信息列表
    *
    * @return List
    * @exception   Exception   If the MRepTypeForm objects cannot be retrieved.
    */
   public static List findAllFR () throws Exception {
      List retVals = null;
      
      DBConn conn=null;
      
      try{
    	  conn=new DBConn();
    	  
		  List list=conn.openSession().find("from MRepType");

	      if(list!=null && list.size()>0){
	    	  retVals=new ArrayList();
		      for (Iterator it = list.iterator(); it.hasNext(); ) {
		         MRepTypeForm mRepTypeFormTemp = new MRepTypeForm();
		         com.cbrc.smis.hibernate.MRepType mRepTypePersistence = (com.cbrc.smis.hibernate.MRepType)it.next();
		         TranslatorUtil.copyPersistenceToVo(mRepTypePersistence, mRepTypeFormTemp);
		         retVals.add(mRepTypeFormTemp);
		      }
	      }
      }catch(HibernateException he){
    	  log.printStackTrace(he);
      }finally{
    	  if(conn!=null) conn.closeSession();
      }
      
      return retVals;
   }
}
