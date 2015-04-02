
package com.fitech.net.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.util.FitechException;
import com.fitech.net.form.OrgLayerForm;
import com.fitech.net.hibernate.OrgLayer;
/**
 *@StrutsOrgLayerDelegate  机构级别Delegate
 * 
 * @author jcm
 */
public class StrutsOrgLayerDelegate {
	//Catch到本类的抛出的所有异常
	private static FitechException log=new FitechException(StrutsOrgLayerDelegate.class);

	/**
	 * 新增机构级别
	 * @param orgLayerForm OrgLayerForm
     * @return boolean result,新增成功返回true,否则返回false
     * @exception Exception，捕捉异常处理 
     */
   public static  boolean create (OrgLayerForm orgLayerForm) throws Exception {
	
	   boolean result=false;				//置result标记
	   OrgLayer orgLayerPersistence = new OrgLayer();
	   	   
	   if (orgLayerForm == null || orgLayerForm.getOrg_layer_name().equals("")) 
		   return result;
	   
	   //连接对象的初始化
	   DBConn conn=null;
	   //会话对象的初始化
	   Session session=null;
	   try{
		   
		   TranslatorUtil.copyVoToPersistence(orgLayerPersistence,orgLayerForm);
		   //实例化连接对象
		   conn =new DBConn();
		   //会话对象为连接对象的事务属性
		   session=conn.beginTransaction();
		
		   //会话对象保存持久层对象
		   session.save(orgLayerPersistence);
		   session.flush();
		   //标志为true
		   result=true;
	   }
	   catch(HibernateException e){
		   //持久层的异常被捕捉
		   result = false;
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
    * @param   orgLayerForm   包含查询的条件信息（机构级别ID，机构级别名称）
    */
   
   public static int getRecordCount(OrgLayerForm orgLayerForm)
   {
	   int count=0;
	   
	   //连接对象和会话对象初始化
	   DBConn conn=null;				
	   Session session=null;
	   
	   //查询条件HQL的生成
	   StringBuffer hql = new StringBuffer("select count(*) from OrgLayer ol");						
	   StringBuffer where = new StringBuffer("");
	   
	   if (orgLayerForm != null) {
		   // 查找条件的判断,查找名称不可为空
			if (orgLayerForm.getOrg_layer_name() != null && !orgLayerForm.getOrg_layer_name().equals(""))
				where.append((where.toString().equals("")?"":" and ") + "ol.orgLayerName like '%" + orgLayerForm.getOrg_layer_name() + "%'");
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
    * 机构级别的查询
    * 
    * @param orgLayerForm OrgLayerForm 查询表单对象
    * @return List 如果查找到记录，返回List记录集；否则，返回null
    */
   public static List select(OrgLayerForm orgLayerForm,int offset,int limit){
	   
	   //List集合的定义 
	   List refVals = null;		
		   
	   //连接对象和会话对象初始化
	   DBConn conn = null;				
	   Session session = null;
	   
	   //	 查询条件HQL的生成
	   StringBuffer hql = new StringBuffer("from OrgLayer ol");						
	   StringBuffer where = new StringBuffer("");
	 
	   if (orgLayerForm != null) {
		   // 查找条件的判断,查找名称不可为空
			if (orgLayerForm.getOrg_layer_name() != null && !orgLayerForm.getOrg_layer_name().equals(""))
				where.append((where.toString().equals("")?"":" and ") + "ol.orgLayerName like '%" + orgLayerForm.getOrg_layer_name() + "%'");
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
		    	 OrgLayerForm orgLayerFormTemp = new OrgLayerForm();
		    	 OrgLayer orgLayerPersistence = (OrgLayer)it.next();
		         TranslatorUtil.copyPersistenceToVo(orgLayerPersistence, orgLayerFormTemp);
		         refVals.add(orgLayerFormTemp);
		      }
    	   }
      }catch(HibernateException he){
    	  refVals = null;
    	  log.printStackTrace(he);
      }catch(Exception e){
    	  refVals = null;  
    	  log.printStackTrace(e);
      }finally{
    	  //如果连接存在，则断开，结束会话，返回
    	  if(conn != null) conn.closeSession();
      }
         return refVals;
   }
   
   
   /**
    * 更新OrgLayerForm对象
    *
    * @param   orgLayerForm   OrgLayerForm 存放数据的对象
    * @exception   Exception  如果OrgLayerForm更新失败,则捕捉抛出异常
    */
   public static boolean update (OrgLayerForm orgLayerForm) throws Exception {
	   boolean result = false;
		DBConn conn = null;
		Session session = null;

		OrgLayer orgLayerPersistence = new OrgLayer();

		if (orgLayerForm == null) {
			return result;
		}
		try {
			if (orgLayerForm.getOrg_layer_name() == null
					|| orgLayerForm.getOrg_layer_name().equals("")) {
				return result;
			}
			conn = new DBConn();
			session = conn.beginTransaction();

			TranslatorUtil.copyVoToPersistence(orgLayerPersistence,	orgLayerForm);
			session.update(orgLayerPersistence);

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
	 * @param orgLayerForm  The OrgLayerForm  保持数据的传递
	 * @exception Exception 如果 OrgLayerForm 对象丢失则抛出异常.
	 */
   public static boolean  edit (OrgLayerForm orgLayerForm) throws Exception {
	   boolean result = false;
	   DBConn conn = null;
	   Session session = null;
	  
	   OrgLayer orgLayerPersistence = new OrgLayer ();
	   orgLayerForm = new OrgLayerForm();

	   if (orgLayerForm == null ){
		   return  result;
	   }
	   
	   try{
		   if (orgLayerForm.getOrg_layer_name() == null || orgLayerForm.getOrg_layer_name().equals(""))
			   return result;
		   conn = new DBConn();
		   session = conn.beginTransaction();
	   
		   orgLayerPersistence = (OrgLayer)session.load(OrgLayer.class, orgLayerForm.getOrg_layer_name());
	
		   TranslatorUtil.copyVoToPersistence(orgLayerPersistence, orgLayerForm);
	         
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
    * @param   orgLayerForm   OrgLayerForm 查询表单的对象
    * @return   boolean  如果删除成功则返回true,否则false
    */  
   public static boolean remove (OrgLayerForm orgLayerForm) throws Exception {
       //置标志result
	   boolean result=false;
	   //连接和会话对象的初始化
	   DBConn conn=null;
	   Session session=null;
	 
	   //orgLayerForm是否为空,返回result
	   if (orgLayerForm == null || orgLayerForm.getOrg_layer_id() == null) 
		   return result;
	  
	     try{
	       //	连接对象和会话对象初始化
		   conn=new DBConn();
		   session=conn.beginTransaction();
		   
		   OrgLayer orgLayer=(OrgLayer)session.load(OrgLayer.class,orgLayerForm.getOrg_layer_id());
		   //会话对象删除持久层对象
		   session.delete(orgLayer);
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
	  * @param orgLayerForm OrgLayerForm实例化对象
	  * @return OrgLayerForm 包含一条记录
	  */
   
   public static OrgLayerForm selectOne(OrgLayerForm orgLayerForm){
	   
	   DBConn conn = null;
	   Session session = null;
	   
	   if(orgLayerForm != null){
		   try{
			   conn = new DBConn();
			   session = conn.openSession();
			   OrgLayer orgLayerPersistence = (OrgLayer)session.load(OrgLayer.class,orgLayerForm.getOrg_layer_id());
			   TranslatorUtil.copyPersistenceToVo(orgLayerPersistence,orgLayerForm);
		   }catch(HibernateException he){
			   log.printStackTrace(he);
		   }catch(Exception e){
			   log.printStackTrace(e);
		   }finally{
			   //如果连接存在，则断开，结束会话，返回
			   if(conn != null) conn.closeSession();
		   }
	   }
	   return orgLayerForm;
   }



	/**
	 * 查询所有记录
	 * @return List 查询到的记录条数	
	 */
	public static List findAll () throws Exception {
		
		List refVals = null;
		DBConn conn = null;
		Session session = null;
		StringBuffer hql = new StringBuffer("from OrgLayer");
		
		try{
			conn = new DBConn();
			session = conn.openSession();
			List list = session.find(hql.toString());
			
			if(list != null){
				refVals = new ArrayList();
				for(Iterator iter = list.iterator();iter.hasNext();){
					OrgLayerForm orgLayerFormTemp = new OrgLayerForm();
					OrgLayer orgLayerPersistence = (OrgLayer)iter.next();
					TranslatorUtil.copyPersistenceToVo(orgLayerPersistence,orgLayerFormTemp);
					refVals.add(orgLayerFormTemp);
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
}
