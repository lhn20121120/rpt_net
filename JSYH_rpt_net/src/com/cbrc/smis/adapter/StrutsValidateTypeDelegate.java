
package com.cbrc.smis.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.form.ValidateTypeForm;
import com.cbrc.smis.hibernate.ValidateType;
import com.cbrc.smis.util.FitechException;
/**
 *@StrutsValidateTypeDelegate 校验类别表单Delegate
 * 
 * @author 唐磊
 */
public class StrutsValidateTypeDelegate {
	//Catch到本类的抛出的所有异常
	private static FitechException log=new FitechException(StrutsValidateTypeDelegate.class);

/**
    * 校验类别新增
    * 
    * @param mRepFreqForm ValidateTypeForm
    * @return boolean result,新增成功返回true,否则返回false
    * @exception Exception，捕捉异常处理
    */
   public static  boolean create (ValidateTypeForm validateTypeForm) throws Exception {
	
	   boolean result=false;				//置result标记
	   ValidateType validateTypePersistence = new ValidateType ();
	   	   
	   if (validateTypeForm==null || validateTypeForm.getValidateTypeName().equals("")) 
	   {
		   return  result;
	   }
	   //连接对象的初始化
	   DBConn conn=null;
	   //会话对象的初始化
	   Session session=null;
	   
	   try
	   {
		   //表示层到持久层的CopyTo方法(McurUnitPresistence持久层对象的实例,validateTypeForm表示层对象)
		   if (validateTypeForm.getValidateTypeName()==null  || validateTypeForm.getValidateTypeName().equals(""))
		   {
			   return result;
		   }
		
		   TranslatorUtil.copyVoToPersistence(validateTypePersistence,validateTypeForm);
		   //实例化连接对象
		   conn =new DBConn();
		   //会话对象为连接对象的事务属性
		   session=conn.beginTransaction();
		   
		   //会话对象保存持久层对象
		   session.save(validateTypePersistence);
		   session.flush();
		   TranslatorUtil.copyPersistenceToVo(validateTypePersistence,validateTypeForm);
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
    * @param   validateTypeForm   包含查询的条件信息（校验类别ID，校验类别名称）
    */
   
   public static int getRecordCount(ValidateTypeForm validateTypeForm)
   {
	   int count=0;
	   
	   //连接对象和会话对象初始化
	   DBConn conn=null;				
	   Session session=null;
	   
	   //	 查询条件HQL的生成
	   StringBuffer hql = new StringBuffer("select count(*) from ValidateType vt");						
	   StringBuffer where = new StringBuffer("");
	   
	   if (validateTypeForm != null) {
		   // 查找条件的判断,查找名称不可为空
			if (validateTypeForm.getValidateTypeName() != null && !validateTypeForm.getValidateTypeName().equals(""))
				where.append((where.toString().equals("")?"":" and ") + "vt.validateTypeName like '%" + validateTypeForm.getValidateTypeName() + "%'");
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
    * 校验类别的查询
    * 
    * @param validateTypeForm ValidateTypeForm 查询表单对象
    * @return List 如果查找到记录，返回List记录集；否则，返回null
    */
   public static List select(ValidateTypeForm validateTypeForm,int offset,int limit){
	   
	   //List集合的定义 
	   List refVals=null;		   
	   //连接对象和会话对象初始化
	   DBConn conn=null;				
	   Session session=null;
	   
	   //查询条件HQL的生成
	   StringBuffer hql = new StringBuffer("from ValidateType vt");						
	   StringBuffer where = new StringBuffer("");	   
	   if (validateTypeForm != null) {
		   // 查找条件的判断,查找名称不可为空
			if (validateTypeForm.getValidateTypeName() != null && !validateTypeForm.getValidateTypeName().equals(""))
				where.append((where.toString().equals("")?"":" and ") + "vt.validateTypeName like '%" + validateTypeForm.getValidateTypeName() + "%'");
	   }
		  
      try{    	     	
    	  hql.append((where.toString().equals("")?"":" where ") + where.toString());
    	  hql.append(" order by vt.validateTypeName");
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
		         ValidateTypeForm validateTypeFormTemp = new ValidateTypeForm();
		         ValidateType validateTypePersistence = (ValidateType)it.next();
		         TranslatorUtil.copyPersistenceToVo(validateTypePersistence, validateTypeFormTemp);
		         refVals.add(validateTypeFormTemp);
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
    * 更新ValidateTypeForm对象
    *
    * @param   validateTypeForm   ValidateTypeForm 存放数据的对象
    * @exception   Exception  如果ValidateTypeForm更新失败,则捕捉抛出异常
    */
   public static boolean update (ValidateTypeForm validateTypeForm) throws Exception {
	   boolean result = false;
		DBConn conn = null;
		Session session = null;

		ValidateType validateTypePersistence = new ValidateType();

		if (validateTypeForm == null) {
			return result;
		}
		try {
			if (validateTypeForm.getValidateTypeName() == null
					|| validateTypeForm.getValidateTypeName().equals("")) {
				return result;
			}
			conn = new DBConn();
			session = conn.beginTransaction();

			TranslatorUtil.copyVoToPersistence(validateTypePersistence,
					validateTypeForm);
			session.update(validateTypePersistence);

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
	 * @param validateTypeForm  The ValidateTypeForm  保持数据的传递
	 * @exception Exception 如果 ValidateTypeForm 对象丢失则抛出异常.
	 */
   public static boolean  edit (ValidateTypeForm validateTypeForm) throws Exception {
	   boolean result=false;
	   DBConn conn=null;
	   Session session=null;
	  
	   ValidateType validateTypePersistence = new ValidateType ();
	   validateTypeForm=new ValidateTypeForm();
	   validateTypeForm.getValidateTypeName();
	   
	   if (validateTypeForm==null ) 
	   {
		   return  result;
	   }
	   
	   try
	   {
	   if (validateTypeForm.getValidateTypeName()==null && validateTypeForm.getValidateTypeName().equals(""))
	   {
		   return result;
	   }
	   conn=new DBConn();
	   session=conn.beginTransaction();
	   
	   
	   validateTypePersistence = (ValidateType)session.load(ValidateType.class, validateTypeForm.getValidateTypeName());

	   TranslatorUtil.copyVoToPersistence(validateTypePersistence, validateTypeForm);
         
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
    * @param   validateTypeForm   ValidateTypeForm 查询表单的对象
    * @return   boolean  如果删除成功则返回true,否则false
    */  
   public static boolean remove (ValidateTypeForm validateTypeForm) throws Exception {
      //置标志result
	   boolean result=false;
	   //连接和会话对象的初始化
	   DBConn conn=null;
	   Session session=null;
	 
	   //validateType是否为空,返回result
	   if (validateTypeForm==null && validateTypeForm.getValidateTypeId()==null) return result;
	  
	     try{
	    	 //	连接对象和会话对象初始化
		   conn=new DBConn();
		   session=conn.beginTransaction();
		   //将validateTypeForm的货币单位的货币主键传入HQL中查询
		   ValidateType validateType=(ValidateType)session.load(ValidateType.class,validateTypeForm.getValidateTypeId());
		   TranslatorUtil.copyPersistenceToVo(validateType,validateTypeForm);
		   //会话对象删除持久层对象
		   session.delete(validateType);
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
  * 查询一条记录,返回到EditAction中
  * @param validateTypeForm ValidateTypeForm实例化对象
  * @return ValidateTypeForm 包含一条记录
  */
   
public static ValidateTypeForm selectOne(ValidateTypeForm validateTypeForm){
	   
	   //连接对象和会话对象初始化
	   DBConn conn=null;				
	   Session session=null;
	   	     
	   if (validateTypeForm != null) 
	 try
      {	   
    	  if (validateTypeForm.getValidateTypeId() != null && !validateTypeForm.getValidateTypeId().equals(""))
    	  //conn对象的实例化		  
    	  conn=new DBConn();
    	  //打开连接开始会话
    	  session=conn.openSession();
    	
    	  ValidateType validateTypePersistence = (ValidateType)session.load(ValidateType.class, validateTypeForm.getValidateTypeId());
    	
    	  TranslatorUtil.copyPersistenceToVo(validateTypePersistence, validateTypeForm);
    	
      }catch(HibernateException he){
    	  log.printStackTrace(he);
      }catch(Exception e){
    	  log.printStackTrace(e);
      }finally{
    	  //如果连接存在，则断开，结束会话，返回
    	  if(conn!=null) conn.closeSession();
      }
	    return validateTypeForm;
   }



   /**
    * Retrieve all existing ValidateTypeForm objects.
    *
    * @exception   Exception   If the ValidateTypeForm objects cannot be retrieved.
    */
   public static List findAll () throws Exception {
      List retVals = new ArrayList();
javax.naming.InitialContext ctx = new javax.naming.InitialContext();
// TODO: Make adapter get SessionFactory jndi name by ant task argument?
net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory)ctx.lookup("java:AirlineHibernateFactory");
net.sf.hibernate.Session session = factory.openSession();
net.sf.hibernate.Transaction tx = session.beginTransaction();
retVals.addAll(session.find("from com.cbrc.smis.hibernate.ValidateType"));
tx.commit();
session.close();
      ArrayList validateType_vos = new ArrayList();
      for (Iterator it = retVals.iterator(); it.hasNext(); ) {
         ValidateTypeForm validateTypeFormTemp = new ValidateTypeForm();
         com.cbrc.smis.hibernate.ValidateType validateTypePersistence = (com.cbrc.smis.hibernate.ValidateType)it.next();
         TranslatorUtil.copyPersistenceToVo(validateTypePersistence, validateTypeFormTemp);
         validateType_vos.add(validateTypeFormTemp);
      }
      retVals = validateType_vos;
      return retVals;
   }
   
   /*根据StrutsDataValidateInfoDelegate中的selectValidateTypeId方法查询ValidateType表单中的Validate_Type_Name
    * @author 唐磊
    * @param reportInParticularForm
    * @validateTypeName 返回的为查找的校验类型名称
    
   
   public static String selectValidateTypeName(ReportInParticularForm reportInParticularForm){
	   String validateTypeName="";
	   DBConn conn=null;
	   Session session=null;
	   List list=null;
	   int typeId=StrutsDataValidateInfoDelegate.selectValidateTypeId(reportInParticularForm);
	   try {
			if (typeId != 0&& typeId>0) {
				conn = new DBConn();
				session = conn.openSession();

				String hql = "from ValidateType vt where 1=1";
				hql += " and vi.validateTypeId="
						+ typeId +"";

				Query query = session.createQuery(hql.toString());
				list = query.list();
				if (list != null && list.size() != 0) {
					validateTypeName = ((ValidateType) list.get(0)).getValidateTypeName();
					// System.out.println("validateTypeName in <<selectValidateTypeName() of StrutsValidateTypeDelegate>>========================="+validateTypeName);
				}

			}
   	}catch(HibernateException he){
   		log.printStackTrace(he);
   	}catch(Exception e){
   		log.printStackTrace(e);
   	}finally{
   		if (conn!=null)conn.closeSession();
   	}
   	return validateTypeName;
   }*/
}

