
package com.fitech.net.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.util.FitechException;
import com.fitech.net.form.OrgTypeForm;
import com.fitech.net.hibernate.MRegion;
import com.fitech.net.hibernate.OrgType;
/**
 *@StrutsOrgLayerDelegate  机构级别Delegate
 * 
 * @author jcm
 */
public class StrutsOrgTypeDelegate {
	//Catch到本类的抛出的所有异常
	private static FitechException log=new FitechException(StrutsOrgTypeDelegate.class);

	/**
	 * 新增机构类别
	 * @param orgTypeForm OrgTypeForm
     * @exception Exception，捕捉异常处理 
     * @return int result  0--操作失败  1--操作成功  2--要添加的指定级别的机构类别已经存在
     */
   public static int create (OrgTypeForm orgTypeForm) throws Exception {
	
	   int result = 0;				//置result标记
	   boolean bool = false;
	   OrgType orgTypePersistence = new OrgType();
	   	   
	   if (orgTypeForm == null || orgTypeForm.getOrg_type_name().equals("")) 
		   return result;
	   
	   //连接对象的初始化
	   DBConn conn=null;
	   //会话对象的初始化
	   Session session=null;
	   try{
		   orgTypeForm.setOrg_type_id(orgTypeForm.getOrg_layer_id());
		   
		   TranslatorUtil.copyVoToPersistence(orgTypePersistence,orgTypeForm);
		   //实例化连接对象
		   conn =new DBConn();
		   //会话对象为连接对象的事务属性
		   session=conn.beginTransaction();
		   
		   String hql = "from OrgType ot where ot.orgTypeId = " + orgTypeForm.getOrg_type_id();
		   List list = session.find(hql);
		   
		   if(list != null && list.size() > 0){
			   result = 2;        //要增加的机构级别已经存在
			   bool = true;
			   return result;
		   }
		
		   //会话对象保存持久层对象
		   session.save(orgTypePersistence);
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
    * @param   orgTypeForm   包含查询的条件信息（机构类别ID，机构类别名称）
    */
   
   public static int getRecordCount(OrgTypeForm orgTypeForm)
   {
	   int count=0;
	   
	   //连接对象和会话对象初始化
	   DBConn conn=null;				
	   Session session=null;
	   
	   //查询条件HQL的生成
	   StringBuffer hql = new StringBuffer("select count(*) from OrgType ot");						
	   StringBuffer where = new StringBuffer("");
	   
	   if (orgTypeForm != null) {
		   // 查找条件的判断,查找名称不可为空
			if (orgTypeForm.getOrg_type_name() != null && !orgTypeForm.getOrg_type_name().equals(""))
				where.append((where.toString().equals("")?"":" and ") + "ot.orgTypeName like '%" + orgTypeForm.getOrg_type_name() + "%'");
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
    * 上报频度的查询
    * 
    * @param orgTypeForm OrgTypeForm 查询表单对象
    * @return List 如果查找到记录，返回List记录集；否则，返回null
    */
   public static List select(OrgTypeForm orgTypeForm,int offset,int limit){
	   
	   //List集合的定义 
	   List refVals = null;		
		   
	   //连接对象和会话对象初始化
	   DBConn conn = null;				
	   Session session = null;
	   
	   //	 查询条件HQL的生成
	   StringBuffer hql = new StringBuffer("from OrgType ot");						
	   StringBuffer where = new StringBuffer("");
	 
	   if (orgTypeForm != null) {
		   // 查找条件的判断,查找名称不可为空
			if (orgTypeForm.getOrg_type_name() != null && !orgTypeForm.getOrg_type_name().equals(""))
				where.append((where.toString().equals("")?"":" and ") + "ot.orgTypeName like '%" + orgTypeForm.getOrg_type_name() + "%'");
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
		    	 OrgTypeForm orgTypeFormTemp = new OrgTypeForm();
		    	 OrgType orgTypePersistence = (OrgType)it.next();
		         TranslatorUtil.copyPersistenceToVo(orgTypePersistence, orgTypeFormTemp);
		         refVals.add(orgTypeFormTemp);
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
    * 更新OrgTypeForm对象
    *
    * @param   orgTypeForm   OrgTypeForm 存放数据的对象
    * @exception   Exception  如果OrgTypeForm更新失败,则捕捉抛出异常
    */
   public static boolean update (OrgTypeForm orgTypeForm) throws Exception {
	   boolean result = false;
		DBConn conn = null;
		Session session = null;

		OrgType orgTypePersistence = new OrgType();

		if (orgTypeForm == null) {
			return result;
		}
		try {
			if (orgTypeForm.getOrg_type_name() == null
					|| orgTypeForm.getOrg_type_name().equals("")) {
				return result;
			}
			conn = new DBConn();
			session = conn.beginTransaction();

			TranslatorUtil.copyVoToPersistence(orgTypePersistence,	orgTypeForm);
			session.update(orgTypePersistence);

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
	 * @param orgTypeForm  The OrgTypeForm  保持数据的传递
	 * @exception Exception 如果 OrgTypeForm 对象丢失则抛出异常.
	 */
   public static boolean  edit (OrgTypeForm orgTypeForm) throws Exception {
	   boolean result = false;
	   DBConn conn = null;
	   Session session = null;
	  
	   OrgType orgTypePersistence = new OrgType ();
	   orgTypeForm = new OrgTypeForm();

	   if (orgTypeForm == null ){
		   return  result;
	   }
	   
	   try{
		   if (orgTypeForm.getOrg_type_name() == null || orgTypeForm.getOrg_type_name().equals(""))
			   return result;
		   conn = new DBConn();
		   session = conn.beginTransaction();
	   
		   orgTypePersistence = (OrgType)session.load(OrgType.class, orgTypeForm.getOrg_type_name());
	
		   TranslatorUtil.copyVoToPersistence(orgTypePersistence, orgTypeForm);
	         
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
    * @param   orgTypeForm   OrgTypeForm 查询表单的对象
    * @return   boolean  如果删除成功则返回true,否则false
    */  
   public static boolean remove (OrgTypeForm orgTypeForm) throws Exception {
       //置标志result
	   boolean result=false;
	   //连接和会话对象的初始化
	   DBConn conn=null;
	   Session session=null;
	 
	   //orgTypeForm是否为空,返回result
	   if (orgTypeForm == null || orgTypeForm.getOrg_type_id() == null) 
		   return result;
	  
	     try{
	       //	连接对象和会话对象初始化
		   conn=new DBConn();
		   session=conn.beginTransaction();
		   
		   OrgType orgLayer=(OrgType)session.load(OrgType.class,orgTypeForm.getOrg_type_id());
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
	  * @param orgTypeForm OrgTypeForm实例化对象
	  * @return OrgTypeForm 包含一条记录
	  */
   
   public static OrgTypeForm selectOne(OrgTypeForm orgTypeForm){
	   
	   DBConn conn = null;
	   Session session = null;
	   
	   if(orgTypeForm != null){
		   try{
			   conn = new DBConn();
			   session = conn.openSession();
			   OrgType orgTypePersistence = (OrgType)session.load(OrgType.class,orgTypeForm.getOrg_type_id());
			   TranslatorUtil.copyPersistenceToVo(orgTypePersistence,orgTypeForm);
		   }catch(HibernateException he){
			   log.printStackTrace(he);
		   }catch(Exception e){
			   log.printStackTrace(e);
		   }finally{
			   //如果连接存在，则断开，结束会话，返回
			   if(conn != null) conn.closeSession();
		   }
	   }
	   return orgTypeForm;
   }

	/**
	 * 查找一条记录
	 */
	public static OrgType selectOne(Integer orgTypeId){
		OrgType orgType = null;
		DBConn conn = null;
		Session session = null;
		
		try{
			conn = new DBConn();
			session = conn.openSession();
			
			String hql = "from OrgType mr where mr.orgTypeId = " + orgTypeId ;
			
			List list = session.find(hql.toString());
			
			if(list != null && list.size() > 0){
				orgType = (OrgType)list.get(0);
			}
		}catch(HibernateException he){
			log.printStackTrace(he);
		}catch(Exception e){
			orgType = null ;
			log.printStackTrace(e);
		}finally{
			//如果连接存在，则断开，结束会话，返回
			if(conn != null) conn.closeSession();
		}
		return orgType;
	}

	/**
	 * 查询所有记录
	 * @return List 查询到的记录条数	
	 */
	public static List findAll () throws Exception {
		
		List refVals = null;
		DBConn conn = null;
		Session session = null;
		StringBuffer hql = new StringBuffer("from OrgType t where t.orgTypeId!=1 and t.orgTypeId!=-99");
		
		try{
			conn = new DBConn();
			session = conn.openSession();
			List list = session.find(hql.toString());
			
			if(list != null){
				refVals = new ArrayList();
				for(Iterator iter = list.iterator();iter.hasNext();){
					OrgTypeForm orgTypeFormTemp = new OrgTypeForm();
					OrgType orgTypePersistence = (OrgType)iter.next();
					TranslatorUtil.copyPersistenceToVo(orgTypePersistence,orgTypeFormTemp);
					OrgType preOrgType = findPreOrgTypeId(orgTypePersistence.getOrgTypeId());
					if(preOrgType != null){
						orgTypeFormTemp.setPre_orgType_id(preOrgType.getOrgTypeId());
						orgTypeFormTemp.setPre_orgType_name(preOrgType.getOrgTypeName());
					}
					refVals.add(orgTypeFormTemp);
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
	
	public static List findSubOrgTypes(String orgId) throws Exception{
		List refVals = null;
		DBConn conn = null;
		Session session = null;
		StringBuffer hql = new StringBuffer("from OrgType ot where ot.orgTypeId > (select on.orgType.orgTypeId from OrgNet on where on.orgId='" + orgId + "')");
	//	StringBuffer hql = new StringBuffer("from OrgType ot ");
		try{
			conn = new DBConn();
			session = conn.openSession();
			List list = session.find(hql.toString());
			
			if(list != null){
				refVals = new ArrayList();
				for(Iterator iter = list.iterator();iter.hasNext();){
					OrgTypeForm orgTypeFormTemp = new OrgTypeForm();
					OrgType orgTypePersistence = (OrgType)iter.next();
					TranslatorUtil.copyPersistenceToVo(orgTypePersistence,orgTypeFormTemp);
					OrgType preOrgType = findPreOrgTypeId(orgTypePersistence.getOrgTypeId());
					if(preOrgType != null){
						orgTypeFormTemp.setPre_orgType_id(preOrgType.getOrgTypeId());
						orgTypeFormTemp.setPre_orgType_name(preOrgType.getOrgTypeName());
					}
					refVals.add(orgTypeFormTemp);
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
	 * 已使用hibernate 卞以刚 2011-12-22
	 * 查找上一级机构类型
	 */
	public static OrgType findPreOrgTypeId(Integer orgTypeId){
		OrgType orgTypeResult = null;
		DBConn conn = null;
		Session session = null;
		
		try{
			conn = new DBConn();
			session = conn.openSession();
			
			String hql = "select min(orgTypeId) from OrgType ot where ot.orgTypeId > " + orgTypeId ;
			
			List list = session.find(hql.toString());
			
			if(list != null && list.size() > 0){
				orgTypeResult = (OrgType)list.get(0);
			}
		}catch(HibernateException he){
			log.printStackTrace(he);
			he.printStackTrace();
		}catch(Exception e){
			orgTypeResult = null ;
			e.printStackTrace();
			log.printStackTrace(e);
		}finally{
			//如果连接存在，则断开，结束会话，返回
			if(conn != null) conn.closeSession();
		}
		return orgTypeResult;
	}
	
	/**
	 * 查找上一级机构类型
	 */
	public static OrgType findPreOrgTypeId1(Integer orgTypeId){
		OrgType orgTypeResult = null;
		DBConn conn = null;
		Session session = null;
		List list=null;
		try{
			conn = new DBConn();
			session = conn.openSession();
			
			String hql = "select from OrgType ot where ot.orgTypeId < " + orgTypeId ;
			
			list = session.find(hql.toString());
			
			if(list != null && list.size() > 0){
				orgTypeResult = (OrgType)list.get(0);
			}
		}catch(HibernateException he){
			log.printStackTrace(he);
			he.printStackTrace();
		}catch(Exception e){
			orgTypeResult = null ;
			e.printStackTrace();
			log.printStackTrace(e);
		}finally{
			//如果连接存在，则断开，结束会话，返回
			if(conn != null) conn.closeSession();
		}
		if(list==null) return null;
		if(list.size()==0) return null;
		return (OrgType)list.get(list.size()-1);
	}
	
	public static OrgType findMaxOrgTyp(){
		OrgType orgTypeResult = null;
		DBConn conn = null;
		Session session = null;
		
		try{
			conn = new DBConn();
			session = conn.openSession();
			
			String hql = "from OrgType orgType where orgType.orgTypeId in (select min(ot.orgTypeId) from OrgType ot)";
			
			List list = session.find(hql);
			
			if(list != null && list.size() > 0){
				orgTypeResult = (OrgType)list.get(0);
			}
		}catch(HibernateException he){
			log.printStackTrace(he);
		}catch(Exception e){
			orgTypeResult = null;
			e.printStackTrace();
			log.printStackTrace(e);
		}finally{
			if(conn != null) conn.closeSession();
		}
		return orgTypeResult;
	}
}
