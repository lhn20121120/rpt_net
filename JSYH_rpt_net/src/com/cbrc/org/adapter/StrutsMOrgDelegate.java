
package com.cbrc.org.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.org.dao.DBConn;
import com.cbrc.org.form.MOrgForm;
import com.cbrc.org.hibernate.MOrg;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechException;
import com.fitech.net.form.OrgNetForm;
import com.fitech.net.hibernate.OrgNet;

/**
 * This is a delegate class to handle interaction with the backend persistence layer of hibernate. 
 * It has a set of methods to handle persistence for MOrg data (i.e. 
 * com.cbrc.org.form.MOrgForm objects).
 * 
 * @author 数据采集组
 */
public class StrutsMOrgDelegate {
	private static FitechException log=new FitechException(StrutsMOrgDelegate.class);
	
	/**
	 * 根据机构类型串获取对应的机构列表
	 * 
	 * @param orgCls String 机构类型串
	 * @return List
	 */
	public static List getOrgs(String orgCls){
		List resList=null;
		
		if(orgCls==null) return resList;
		
		DBConn conn=null;
		
		try{
			String hql="from MOrg org where org.orgClsId in (" + orgCls + ")";
			conn=new DBConn();
			List list=conn.openSession().find(hql);
			if(list!=null && list.size()>0){
				resList=new ArrayList();
				MOrg mOrg=null;
				for(int i=0;i<list.size();i++){
					mOrg=(MOrg)list.get(i);
					MOrgForm form=new MOrgForm();
					TranslatorUtil.copyPersistenceToVo(mOrg,form);
					resList.add(form);
				}
			}
		}catch(HibernateException he){
			log.printStackTrace(he);
		}catch(Exception e){
			log.printStackTrace(e);
		}finally{
			if(conn!=null) conn.closeSession();
		}
		
		return resList;
	}
	
	/**
	 * 在org中是否有orgClId
	 * 
	 * @author zhangxinke
	 */
	public static int getMOrgFromorgClsId(String orgClId) {
		int result = 0;
		DBConn conn = null;
		Session session = null;
		try {
			conn = new DBConn();
			session = conn.openSession();
			if(orgClId==null||orgClId.equals(""))
				return -1;
			
			String hql =  "select count(*) from MOrg op where op.orgClsId='"+ orgClId.toString()+"'";
			
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
	 * 根据机构ID获得机构信息
	 * 
	 * @author rds 
	 * @serialData 2005-12-23 17:48 
	 * 
	 * @param orgId String 机构ID
	 * @return MOrgForm
	 */	
	public static MOrgForm getMOrg(String orgId){
		MOrgForm mOrgForm=null;
		
		if(orgId==null) return mOrgForm;
		
		DBConn conn=null;
		
		try
        {
			conn=new DBConn();
			MOrg mOrg=(MOrg)conn.openSession().load(MOrg.class,orgId);
           
			if(mOrg!=null)
            {
                mOrgForm = new MOrgForm();
                TranslatorUtil.copyPersistenceToVo(mOrg,mOrgForm);
            }
        }
        catch(HibernateException he)
        {
            mOrgForm =null;
			log.printStackTrace(he);
		}
        catch(Exception e)
        {
            mOrgForm =null;
			log.printStackTrace(e);
		}
        finally
        {
			if(conn!=null) conn.closeSession();
		}
		
		return mOrgForm;
	}
	/**
	 * 根据机构ID获得机构信息
	 * 
	 * @author rds 
	 * @serialData 2005-12-23 17:48 
	 * 
	 * @param orgId String 机构ID
	 * @return MOrgForm
	 */	
	public static OrgNet getOrgNet(String orgId){
		OrgNet mOrg=null;
		
		if(orgId==null) return mOrg;
		
		 com.cbrc.smis.dao.DBConn conn=null;
		
		try
        {
			conn=new  com.cbrc.smis.dao.DBConn();
			 mOrg=(OrgNet)conn.openSession().load(OrgNet.class,orgId);
           
			
        }
        catch(HibernateException he)
        {
            mOrg =null;
			log.printStackTrace(he);
		}
        catch(Exception e)
        {
            mOrg =null;
			log.printStackTrace(e);
		}
        finally
        {
			if(conn!=null) conn.closeSession();
		}
		
		return mOrg;
	}
	
	/**
	 * 根据名称获得机构信息
	 * 
	 * @author rds 
	 * @serialData 2005-12-07 
	 * 
	 * @param orgName String 机构名称
	 * @return MOrgForm
	 */	
	public static MOrgForm getOrgInfo(String orgName){
		MOrgForm mOrgForm=null;
		
		if(orgName==null) return mOrgForm;
		
		DBConn conn=null;
		
		try{
			String hql="from MOrg org where org.orgName='" + orgName + "'";
			conn=new DBConn();
			List list=conn.openSession().find(hql);
			if(list!=null && list.size()>0){
				MOrg mOrg=(MOrg)list.get(0);
				TranslatorUtil.copyPersistenceToVo(mOrg,mOrgForm);
			}
		}catch(HibernateException he){
			log.printStackTrace(he);
		}catch(Exception e){
			log.printStackTrace(e);
		}finally{
			if(conn!=null) conn.closeSession();
		}
		
		return mOrgForm;
	}
	
	/**
	 * 根据机构串获取所对应的机构类型列表
	 * 
	 * @author rds
	 * @serialData 2005-12-10
	 * 
	 * @param orgs String 机构串(逗号分隔)
	 * @return List
	 */
	public static List getOrgCls(String orgs){
		List resList=null;
		
		if(orgs==null) return resList;
		
		DBConn conn=null;
		
		try{
			String hql="select distinct mo.orgClsId from MOrg mo where mo.orgId in (" + orgs + ")";
			
			conn=new DBConn();
			List list=conn.openSession().find(hql);
			if(list!=null && list.size()>0){
				resList=new ArrayList();
				for(int i=0;i<list.size();i++){
					if(list.get(i)!=null) resList.add((String)list.get(i));
				}
			}
		}catch(HibernateException he){
			log.printStackTrace(he);
			resList=null;
		}finally{
			if(conn!=null) conn.closeSession();
		}
		
		return resList;
	}		
	public static MOrgForm create(MOrgForm mOrgForm) throws Exception {
		return null;
	}
   /**
    * Create a new com.cbrc.org.form.MOrgForm object and persist (i.e. insert) it.
    * @author 唐磊
    *
    * @param   mOrgForm   The object containing the data for the new com.cbrc.org.form.MOrgForm object
    * @exception   Exception   If the new com.cbrc.org.form.MOrgForm object cannot be created or persisted.
    */
   /*public static boolean create(MOrgForm mOrgForm) throws Exception {
		boolean result = false;
		DBConn conn = null;
		Session session = null;
		List recList = null;

		// 如果Form有无数据，失败返回false;
		if (mOrgForm == null) {
			return result;
		}
		
		// 有无request请求
		if (mOrgForm.getOrgClsId() != null && !mOrgForm.getOrgClsId().equals("")) {
			recList = new ArrayList();
			recList = findAll();
			System.out
					.println("ReclISt In StrutsMOrgDelegate=================================="
							+ recList.size());
			if (recList != null && recList.size() != 0) {
				MOrg mOrgPersistence = new MOrg();
				TranslatorUtil.copyPersistenceToVo(mOrgPersistence, mOrgForm);
			}

			for (int i = 0; i <= recList.size(); i++) {
				System.out
						.println("recList In StrutsMOrgDelegate=============================="
								+ recList.size());

				result = true;
			}
		}
		return result;

	}*/
      
   

   /**
	 * Update (i.e. persist) an existing com.cbrc.org.form.MOrgForm object.
	 * 
	 * @param mOrgForm
	 *            The com.cbrc.org.form.MOrgForm object containing the data to
	 *            be updated
	 * @exception Exception
	 *                If the com.cbrc.org.form.MOrgForm object cannot be
	 *                updated/persisted.
	 */
   public static com.cbrc.org.form.MOrgForm update (com.cbrc.org.form.MOrgForm mOrgForm) throws Exception {
      com.cbrc.org.hibernate.MOrg mOrgPersistence = new com.cbrc.org.hibernate.MOrg ();
      TranslatorUtil.copyVoToPersistence(mOrgPersistence, mOrgForm);
javax.naming.InitialContext ctx = new javax.naming.InitialContext();
// TODO: Make adapter get SessionFactory jndi name by ant task argument?
net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory)ctx.lookup("java:AirlineHibernateFactory");
net.sf.hibernate.Session session = factory.openSession();
net.sf.hibernate.Transaction tx = session.beginTransaction();
session.update(mOrgPersistence);
tx.commit();
session.close();
      TranslatorUtil.copyPersistenceToVo(mOrgPersistence, mOrgForm);
      return mOrgForm;
   }

   /**
    * Retrieve an existing com.cbrc.org.form.MOrgForm object for editing.
    *
    * @param   mOrgForm   The com.cbrc.org.form.MOrgForm object containing the data used to retrieve the object (i.e. the primary key info).
    * @exception   Exception   If the com.cbrc.org.form.MOrgForm object cannot be retrieved.
    */
   public static com.cbrc.org.form.MOrgForm edit (com.cbrc.org.form.MOrgForm mOrgForm) throws Exception {
      com.cbrc.org.hibernate.MOrg mOrgPersistence = new com.cbrc.org.hibernate.MOrg ();
      TranslatorUtil.copyVoToPersistence(mOrgPersistence, mOrgForm);
javax.naming.InitialContext ctx = new javax.naming.InitialContext();
// TODO: Make adapter get SessionFactory jndi name by ant task argument?
net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory)ctx.lookup("java:AirlineHibernateFactory");
net.sf.hibernate.Session session = factory.openSession();
net.sf.hibernate.Transaction tx = session.beginTransaction();
mOrgPersistence = (com.cbrc.org.hibernate.MOrg)session.load(com.cbrc.org.hibernate.MOrg.class, mOrgPersistence.getOrgId());
tx.commit();
session.close();
      TranslatorUtil.copyPersistenceToVo(mOrgPersistence, mOrgForm);
      return mOrgForm;
   }

   /**
    * Remove (delete) an existing com.cbrc.org.form.MOrgForm object.
    *
    * @param   mOrgForm   The com.cbrc.org.form.MOrgForm object containing the data to be deleted.
    * @exception   Exception   If the com.cbrc.org.form.MOrgForm object cannot be removed.
    */  
   public static void remove (com.cbrc.org.form.MOrgForm mOrgForm) throws Exception {
      com.cbrc.org.hibernate.MOrg mOrgPersistence = new com.cbrc.org.hibernate.MOrg ();
      TranslatorUtil.copyVoToPersistence(mOrgPersistence, mOrgForm);
javax.naming.InitialContext ctx = new javax.naming.InitialContext();
// TODO: Make adapter get SessionFactory jndi name by ant task argument?
net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory)ctx.lookup("java:AirlineHibernateFactory");
net.sf.hibernate.Session session = factory.openSession();
net.sf.hibernate.Transaction tx = session.beginTransaction();
// TODO: is this really needed?
mOrgPersistence = (com.cbrc.org.hibernate.MOrg)session.load(com.cbrc.org.hibernate.MOrg.class, mOrgPersistence.getOrgId());
session.delete(mOrgPersistence);
tx.commit();
session.close();
   }

   /**
    * 取得所有机构记录
    *	
    * @author rds 
    * @exception   Exception   If the com.cbrc.org.form.MOrgForm objects cannot be retrieved.
    */
   public static List findAll () throws Exception {
	   List retVals = null;
	      
	      DBConn conn=null;
	      
	      try{
	    	  conn=new DBConn();
	    	  List list=conn.openSession().find("from MOrg");

	    	  if(list!=null && list.size()>0){
	    		  retVals=new ArrayList();
	    		  for(int i=0;i<list.size();i++){
	    			  MOrg mOrg = (MOrg)list.get(i);
	    			  MOrgForm mOrgForm = new MOrgForm();
	    			  TranslatorUtil.copyPersistenceToVo(mOrg,mOrgForm);
	    			  retVals.add(mOrgForm);
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
    * 获得指定机构类别下的机构总数
    * 
    * @param orgClsId String 机构类别
    * @return int
    */
   public static int getRecordCount(String orgClsId){
	   if(orgClsId==null) return 0;
	   MOrgForm mOrgForm=new MOrgForm();
	   mOrgForm.setOrgClsId(orgClsId);
	   return getRecordCount(mOrgForm);
   }
   
   /**
    * 取得按条件查询到的记录条数
    * @author 唐磊
    * @return int 查询到的记录条数	
    * @param   mCurrForm   包含查询的条件信息（机构名称ID，机构类型名称）
    */
   
   public static int getRecordCount(MOrgForm mOrgForm)
   {
	   int count=0;
	   
	   //连接对象和会话对象初始化
	   DBConn conn=null;				
	   Session session=null;
	   
	   //	 查询条件HQL的生成
	   StringBuffer hql = new StringBuffer("select count(*) from MOrg mo where 1=1");						
	   StringBuffer where = new StringBuffer("");
	   
	   if (mOrgForm != null) {
		   // 查找条件的判断,查找名称不可为空
		   if(mOrgForm.getOrgClsId()!=null && !mOrgForm.getOrgClsId().equals(""))
			   where.append(" and mo.orgClsId='"+mOrgForm.getOrgClsId()+"'");
           if (mOrgForm.getOrgName() != null && !mOrgForm.getOrgName().equals(""))
                where.append(" and mo.orgName like '%" + mOrgForm.getOrgName() + "%'");
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
    * 根据条件（机构分类id，机构名称）查找记录
    * @author 姚捷
    * @param   mOrgForm MOrgForm   查询条件
    * @param offset int 偏移量
    * @param limit int 取记录条数  0时表示取全部记录  
    * @exception   Exception   If the com.cbrc.org.form.MOrgForm objects cannot be retrieved.
    */
   public static List select (MOrgForm mOrgForm,int offset,int limit) throws Exception {
	   //List集合的定义 
       List refVals=null;       
           
       //连接对象和会话对象初始化
       DBConn conn=null;                
       Session session=null;
       
       //    查询条件HQL的生成
       StringBuffer hql = new StringBuffer("from MOrg mo where 1=1");                       
       StringBuffer where = new StringBuffer("");
       if (mOrgForm != null) {
           // 查找条件的判断,查找名称不可为空
           if(mOrgForm.getOrgClsId()!=null && !mOrgForm.getOrgClsId().equals(""))
               where.append(" and mo.orgClsId='"+mOrgForm.getOrgClsId()+"'");
           
           if (mOrgForm.getOrgName() != null && !mOrgForm.getOrgName().equals(""))
                where.append(" and mo.orgName like '%" + mOrgForm.getOrgName() + "%'");
         }
              
       try
          {    //List集合的操作
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
                      MOrgForm mOrgFormTemp = new MOrgForm();
                     MOrg mOrgPersistence = (MOrg)it.next();
                     TranslatorUtil.copyPersistenceToVo(mOrgPersistence, mOrgFormTemp);
                     mOrgFormTemp.setOrgClsName(StrutsMOrgClDelegate.getOrgClsName(mOrgPersistence.getOrgClsId()));
                     refVals.add(mOrgFormTemp);
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
    * 已使用hibernate 卞以刚 2011-12-21
    * 依据传过来的机构分类id找到其对应的机构id列表
    * @author 唐磊
    * @param orgClsId
    * @return
    * @throws Exception
    */
   public static List select (String orgClsId) throws Exception
   {
	   //List集合的定义 
	   List refVals=null;		   
	   //连接对象和会话对象初始化
	   DBConn conn=null;				
	   Session session=null;
	   //查询条件HQL的生成	  
       if (orgClsId!=null && !orgClsId.toString().equals(""))
       {
           StringBuffer hql = new StringBuffer("from MOrg mo where 1=1");                       
           StringBuffer where = new StringBuffer("");
           where.append(" and mo.orgClsId='" +orgClsId+"'");
           try
           {   //List集合的操作
              //初始化
              
              hql.append(where.toString());
              
              //conn对象的实例化        
              conn=new DBConn();
              //打开连接开始会话
              session=conn.openSession();
              //添加集合至Session
              //List list=session.find(hql.toString());
              Query query=session.createQuery(hql.toString());
              
              List list=query.list();
              
              if (list!=null){
                  refVals = new ArrayList();
                  //循环读取数据库符合条件记录
                  for (Iterator it = list.iterator(); it.hasNext(); ) {
                      MOrgForm mOrgFormTemp = new MOrgForm();
                     MOrg mOrgPersistence = (MOrg)it.next();
                     TranslatorUtil.copyPersistenceToVo(mOrgPersistence, mOrgFormTemp);
                     refVals.add(mOrgFormTemp);
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
	   }	 
	    return refVals;
      }
   
   /**
    * 已使用hibernate 卞以刚 2011-12-21
    * 通过机构id查找他的记录
    * @author 姚捷
    * @param orgId String 机构id 
    * @return  MOrgForm
    * @throws Exception
    */
   public static OrgNetForm selectOne (String orgId) throws Exception
   {
	   
       //返回值
       OrgNetForm result = null;       
       //连接对象和会话对象初始化
       com.cbrc.smis.dao.DBConn  conn=null;                
       Session session=null;
       //查询条件HQL的生成   
       if (orgId!=null && !orgId.toString().equals(""))
       { 
           try
           { 
               conn=new com.cbrc.smis.dao.DBConn ();
               //打开连接开始会话
               session=conn.openSession();
               //查询条件
               StringBuffer hql = new StringBuffer("from OrgNet mo where mo.orgId='" +orgId.trim()+"'");                       
               Query query=session.createQuery(hql.toString());
              
               List list=query.list();
              if (list!=null&&list.size()!=0)
              {
                  result = new OrgNetForm();
                  
                  OrgNet mOrgPersistence = (OrgNet)list.get(0);
                  
                  TranslatorUtil.copyPersistenceToVo(mOrgPersistence, result);
              }
          }catch(HibernateException he){
              result = null;
              he.printStackTrace();
             
              log.printStackTrace(he);
          }catch(Exception e){
              result = null;  
              
              log.printStackTrace(e);
          }finally{
              //如果连接存在，则断开，结束会话，返回
              if(conn!=null) conn.closeSession();
          }
       }     
        return result;      
   }
   
/**根据传过来的机构分类IDorgClsId查找机构orgId生成list集合,通过转换为一个字符串传回StrutsReportInDelegate
 * @author 唐磊
 */ 
   public static String selectOrgId (String orgClsId) throws Exception
   {
   	List list=null;
   	list=new ArrayList();
   	String strOrgId="";
   	/**已使用hibernate 卞以刚 2011-12-21**/
   	list=StrutsMOrgDelegate.select(orgClsId);
   	try{
   		if (list!=null && list.size()>0){
   			for(int i=0;i<list.size();i++){
   				strOrgId+=(Config.SPLIT_SYMBOL_SIGNLE_QUOTES+((MOrgForm)list.get(i)).getOrgId()+Config.SPLIT_SYMBOL_SIGNLE_QUOTES+Config.SPLIT_SYMBOL_COMMA);
   			}
   			/** 去除字符串最后的"," */
   			if(strOrgId.substring(strOrgId.length()-1).equals(Config.SPLIT_SYMBOL_COMMA))
   			{
   				strOrgId=strOrgId.substring(0,strOrgId.length()-1);
   			}
   		}
   			return strOrgId;
   		}catch(Exception e){
   			log.printStackTrace(e);
   		}
   		return strOrgId;
   	}
   
   /**
    * 已使用hibernate 卞以刚 2011-12-21
    * 报表审核中根据机构名称Orgname查找OrgId的集合
    * @author 唐磊
    * @param orgName
    * @return list　机构id的集合
    * @throws Exception
    */
   public static List selectOrgNames (String orgName) throws Exception
   {
	   //List集合的定义 
	   List refVals=null;		   
	   //连接对象和会话对象初始化
	   com.cbrc.smis.dao.DBConn conn=null;				
	   Session session=null;
	   //查询条件HQL的生成	  
       if (orgName!=null && !orgName.toString().equals(""))
       {
           StringBuffer hql = new StringBuffer("from OrgNet mo where 1=1");                       
           StringBuffer where = new StringBuffer("");
           where.append(" and mo.orgName like '%" +orgName+"%'");
           try
           {   //List集合的操作
              //初始化
              
              hql.append(where.toString());
              
              //conn对象的实例化        
              conn=new com.cbrc.smis.dao.DBConn();
              //打开连接开始会话
              session=conn.openSession();
              //添加集合至Session
              //List list=session.find(hql.toString());
              Query query=session.createQuery(hql.toString());
              
              List list=query.list();
              
              if (list!=null){
                  refVals = new ArrayList();
                  //循环读取数据库符合条件记录
                  for (Iterator it = list.iterator(); it.hasNext(); ) {
                      OrgNetForm mOrgFormTemp = new OrgNetForm();
                     OrgNet mOrgPersistence = (OrgNet)it.next();
                     TranslatorUtil.copyPersistenceToVo(mOrgPersistence, mOrgFormTemp);
                     refVals.add(mOrgFormTemp);
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
	   }	 
	    return refVals;
      }
   
   
//   /**根据StrutsMOrgDelegate中的selectOrgNames（）找到的机构名集合转换为字符串
//    * @author 唐磊
//    */
//   public static String selectOrgIdToString (String orgName) throws Exception
//   {
//   	List list=null;
//   	list=new ArrayList();
//   	String strOrgName="";
//   	
//   	list=StrutsMOrgDelegate.selectOrgNames(orgName);
//
//   	for(int i=0;i<list.size();i++){
//   		strOrgName+=(Config.SPLIT_SYMBOL_SIGNLE_QUOTES+((OrgNetForm)list.get(i)).getOrg_id() +Config.SPLIT_SYMBOL_SIGNLE_QUOTES+Config.SPLIT_SYMBOL_COMMA);	
//   	}
//
//    /** 去除字符串最后的"," **/
//    	
//    	if(strOrgName.length()>0 && strOrgName.substring(strOrgName.length()-1).equals(Config.SPLIT_SYMBOL_COMMA))
//    	{
//    		strOrgName=strOrgName.substring(0,strOrgName.length()-1);
//    	}
//    	return strOrgName;
//   	}
   
   
   /**
    * 已使用hibernate 卞以刚 2011-12-21
    * 得到orgclsid对应的全部orgid
    * @author 曹发根
    * @param orgclsid
    * @return
    */
   public static String getOrgids(String orgclsid){
	      StringBuffer result=new StringBuffer("");
	      DBConn conn=null;                
	      Session session=null;
	      try
       { 
           conn=new DBConn();
           //打开连接开始会话
           session=conn.openSession();
           //查询条件
           StringBuffer hql = new StringBuffer("from MOrg mo where mo.orgClsId='" +orgclsid+"'"); 
           Query query=session.createQuery(hql.toString());
          
           List list=query.list();
          
          if (list!=null)for(int i=0;i<list.size();i++){
              MOrg mOrgPersistence = (MOrg)list.get(i);
              result.append((result.toString().equals("") ? "" : ",")
      				+"'"+mOrgPersistence.getOrgId()+"'");
          }
      }catch(HibernateException he){
          log.printStackTrace(he);
      }catch(Exception e){
          log.printStackTrace(e);
      }finally{
          //如果连接存在，则断开，结束会话，返回
          if(conn!=null) conn.closeSession();
      }
 
		  return result.toString();
}
   /**
    * 已使用hibernate 卞以刚 2011-12-21
    * @param orgName
    * @return 名称为orgName的orgId串
    */
	public static String getOrgid(String orgName){
		 StringBuffer result=new StringBuffer("");
	      DBConn conn=null;                
	      Session session=null;
	      try
      { 
          conn=new DBConn();
          //打开连接开始会话
          session=conn.openSession();
          //查询条件
          StringBuffer hql = new StringBuffer("from MOrg mo where mo.orgName='"+orgName+"'");                       
          Query query=session.createQuery(hql.toString());
         
          List list=query.list();
         
         if (list!=null)for(int i=0;i<list.size();i++){
             MOrg mOrgPersistence = (MOrg)list.get(i);
             result.append((result.toString().equals("") ? "" : ",")
     				+"'"+mOrgPersistence.getOrgId()+"'");
         }
     }catch(HibernateException he){
         log.printStackTrace(he);
     }catch(Exception e){
         log.printStackTrace(e);
     }finally{
         //如果连接存在，则断开，结束会话，返回
         if(conn!=null) conn.closeSession();
     }
		  return result.toString();
	}
 /**
  * 已使用hibernate 卞以刚 2011-12-21
  *   从ORG那张表里查询，区别于上面那个程序
  */
	
	public static String getOrgNetid(String orgName){
		 StringBuffer result=new StringBuffer("");
		 com.cbrc.smis.dao.DBConn conn=null;                
	      Session session=null;
	      try
     { 
         conn=new com.cbrc.smis.dao.DBConn();
         //打开连接开始会话
         session=conn.openSession();
         //查询条件
         StringBuffer hql = new StringBuffer("from OrgNet m where m.orgName='"+orgName+"'");                       
         Query query=session.createQuery(hql.toString());
        
         List list=query.list();
        
        if (list!=null)for(int i=0;i<list.size();i++){
            OrgNet mOrgPersistence = (OrgNet)list.get(i);
            result.append((result.toString().equals("") ? "" : ",")
    				+"'"+mOrgPersistence.getOrgId()+"'");
        }
    }catch(HibernateException he){
        log.printStackTrace(he);
    }catch(Exception e){
        log.printStackTrace(e);
    }finally{
        //如果连接存在，则断开，结束会话，返回
        if(conn!=null) conn.closeSession();
    }
		  return result.toString();
	}
	 /**
	  * 已使用hibernate 卞以刚 2011-12-21
	    * 根据从StrutsMRepRangeDelegate中传来的orgid查询orgClsId
	    * 唐磊
	    */

	   public static String selectOrgClsId(String orgId){
		   String orgClsId="";
		   DBConn conn=null;
		   Session session=null;
		   
		   if (orgId!=null){
			   try{
				   String hql="from MOrg mo where 1=1";
				   hql+=" and mo.orgId='"+orgId+"'";
				   conn=new DBConn();
				   session=conn.openSession();
				   List list=session.find(hql.toString());
				   if(list!=null&&list.size()>0){
					   MOrg mOrgPersistence=(MOrg)list.get(0);
					   orgClsId=mOrgPersistence.getOrgClsId().toString();
				   }
			   }catch(HibernateException he){
				   log.printStackTrace(he);
			   }catch(Exception e){
				   log.printStackTrace(e);
			   }finally{
				   if(conn!=null)conn.closeSession();
			   }
		   }
		   return orgClsId;
	   }
	   
	   /**
	    * 已使用hibernate 卞以刚 2011-12-21
	    * 唐磊
	    * 根据传来的orgClsid查询orgid
	    */
	   public static String findOrgIdByOrgClsId(String orgClsId) throws Exception{
		   String strOrgId="";
		   DBConn conn=null;
		   Session session=null;
		   if (orgClsId != null && !orgClsId.equals("")) {
			try {
				conn = new DBConn();
				session = conn.openSession();

				String hql = " from MOrg mo where 1=1";
				hql += " and mo.orgClsId='" + orgClsId + "'";

				Query query = session.createQuery(hql.toString());
				List list = query.list();

				if (list != null && list.size() > 0) {
					MOrg mOrgPersistence=(MOrg)list.get(0);
					strOrgId=mOrgPersistence.getOrgId();
				}

			} catch (HibernateException he) {
				log.printStackTrace(he);
			} catch (Exception e) {
				log.printStackTrace(e);
			} finally {
				if (conn != null)
					conn.closeSession();
			}
		}
		   return strOrgId;
	   }
       /**
        * 已使用hibernate 卞以刚 2011-12-21
        * 根据机构id串取得相应的机构信息
        * @author 姚捷
        * @param orgIds 机构id串 
        * @return
        * @throws Exception
        */
       public static List getOrgInfoByOrgIdString(String orgIds) throws Exception
       {
           List result = null;
        
           if(orgIds==null || orgIds.equals("")) 
                return result;
                   
           DBConn conn=null;
            
           try{
                String hql="from MOrg mo where mo.orgId in (" + orgIds + ")";
                
                conn=new DBConn();
                List list=conn.openSession().find(hql);
                if (list!=null)
                {
                    result = new ArrayList();
                    //循环读取数据库符合条件记录
                    for (Iterator it = list.iterator(); it.hasNext(); ) 
                    {
                       MOrgForm mOrgFormTemp = new MOrgForm();
                       MOrg mOrgPersistence = (MOrg)it.next();
                       TranslatorUtil.copyPersistenceToVo(mOrgPersistence, mOrgFormTemp);
                       result.add(mOrgFormTemp);
                    }
                 }
            }
            catch(HibernateException he)
            {
                log.printStackTrace(he);
                result=null;
            }finally{
                if(conn!=null) conn.closeSession();
            }
            
            return result;
           
       }
}

