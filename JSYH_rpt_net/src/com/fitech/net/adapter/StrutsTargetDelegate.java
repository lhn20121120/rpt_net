package com.fitech.net.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.Criteria;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.util.FitechException;
import com.fitech.net.form.TargetBusinessForm;
import com.fitech.net.form.TargetNormalForm;
import com.fitech.net.hibernate.MBusiness;
import com.fitech.net.hibernate.MNormal;

/**
 * 操作指标分析
 * @author masclnj
 *
 */
public class StrutsTargetDelegate {
	//Catch到本类的抛出的所有异常
	private static FitechException log=new FitechException(StrutsTargetDelegate.class);
	
	/**
	 * 判断指标业务类型名称是否已经存在
	 * 
	 * @param normalName 指标业务类型名称
	 * @return boolean true-存在 false-不存在
	 */
	public static boolean isMNormalExist(String normalName){
		if(normalName == null || normalName.equals("")) return false;
		boolean bool = false;
		DBConn conn = null;
		Session session = null;
		
		String hql = "from MNormal mn where mn.normalName='"+ normalName +"'";
		try {
            conn = new DBConn();
            session = conn.openSession();
            List list = session.find(hql.toString());
            if (list != null && list.size() >0 ) 
            	bool = true;
        } catch (HibernateException he) {
        	bool = false;
            log.printStackTrace(he);
        } catch (Exception e) {
        	bool = false;
            log.printStackTrace(e);
        } finally {
            //如果连接存在，则断开，结束会话，返回
            if (conn != null)
                conn.closeSession();
        }
		return bool;
	}
	
	/**
     * 取得按条件查询到的记录条数
     * 
     * @return int 查询到的记录条数
     * @param TargetNormalForm 包含查询的条件信息（指标业务名称）
     *            
     */
    public static int getRecordCount(TargetNormalForm targetNormal) {
        int count = 0;

        //连接对象和会话对象初始化
        DBConn conn = null;
        Session session = null;

        //	 查询条件HQL的生成
        StringBuffer hql = new StringBuffer("select count(*) from MNormal mn");
        StringBuffer where = new StringBuffer("");

        if (targetNormal != null) {
            // 查找条件的判断,查找名称不可为空
            if (targetNormal.getNormalName() != null
                    && !targetNormal.getNormalName().equals(""))
                where.append((where.toString().equals("") ? "" : " and ")
                        + "mn.normalName like '%" + targetNormal.getNormalName() + "%'");
        }

        try { //List集合的操作
            //初始化
            hql.append((where.toString().equals("") ? "" : " where ")
                    + where.toString());
            //conn对象的实例化
            conn = new DBConn();
            //打开连接开始会话
            session = conn.openSession();
            List list = session.find(hql.toString());
            if (list != null && list.size() == 1) {
                count = ((Integer) list.get(0)).intValue();
            }

        } catch (HibernateException he) {
            log.printStackTrace(he);
        } catch (Exception e) {
            log.printStackTrace(e);
        } finally {
            //如果连接存在，则断开，结束会话，返回
            if (conn != null)
                conn.closeSession();
        }
        return count;
    }	
    
    /**
     * 取得按条件查询到的记录条数
     * 
     * @return int 查询到的记录条数
     * @param TargetNormalForm 包含查询的条件信息（指标业务名称）
     *            
     */

    public static int getRecordCount(TargetBusinessForm targetBusiness) {
        int count = 0;

        //连接对象和会话对象初始化
        DBConn conn = null;
        Session session = null;

        //	 查询条件HQL的生成
        StringBuffer hql = new StringBuffer("select count(*) from MBusiness mb");
        StringBuffer where = new StringBuffer("");

        if (targetBusiness != null) {
            // 查找条件的判断,查找名称不可为空
            if (targetBusiness.getBusinessName() != null
                    && !targetBusiness.getBusinessName().equals(""))
                where.append((where.toString().equals("") ? "" : " and ")
                        + "mb.businessName like '%" + targetBusiness.getBusinessName() + "%'");
        }

        try { //List集合的操作
            //初始化
            hql.append((where.toString().equals("") ? "" : " where ")
                    + where.toString());
            //conn对象的实例化
            conn = new DBConn();
            //打开连接开始会话
            session = conn.openSession();
            List list = session.find(hql.toString());
            if (list != null && list.size() == 1) {
                count = ((Integer) list.get(0)).intValue();
            }

        } catch (HibernateException he) {
            log.printStackTrace(he);
        } catch (Exception e) {
            log.printStackTrace(e);
        } finally {
            //如果连接存在，则断开，结束会话，返回
            if (conn != null)
                conn.closeSession();
        }
        return count;
    }
    
    /**
     * 指标业务的查询
     * 
     * @param TargetNormalForm 查询表单对象
     *            
     * @return List 如果查找到记录，返回List记录集；否则，返回null
     */
    public static List select(TargetNormalForm targetNormal, int offset, int limit) {

        //		 List集合的定义
        List refVals = null;

        //连接对象和会话对象初始化
        DBConn conn = null;
        Session session = null;

        //	 查询条件HQL的生成
        StringBuffer hql = new StringBuffer("from MNormal mn");
        StringBuffer where = new StringBuffer("");
        if (targetNormal != null) {
            // 查找条件的判断,查找名称不可为空
            if (targetNormal.getNormalName() != null
                    && !targetNormal.getNormalName().equals(""))
                where.append((where.toString().equals("") ? "" : " and ")
                        + "mn.normalName like '%" + targetNormal.getNormalName() + "%'");
        }

        try { //List集合的操作
            //初始化
            
            hql.append((where.toString().equals("") ? "" : " where ")
                    + where.toString());
          
            //conn对象的实例化
            conn = new DBConn();
            //打开连接开始会话
            session = conn.openSession();
            //添加集合至Session
            //List list=session.find(hql.toString());
            Query query = session.createQuery(hql.toString());
            query.setFirstResult(offset).setMaxResults(limit);
            List list = query.list();

            if (list != null) {
                refVals = new ArrayList();
                //循环读取数据库符合条件记录
                for (Iterator it = list.iterator(); it.hasNext();) {
                	TargetNormalForm targetNormalTemp = new TargetNormalForm();
                    MNormal targetNormalPersistence = (MNormal) it.next();
                    TranslatorUtil.copyPersistenceToVo(targetNormalPersistence,
                    		targetNormalTemp);
                    refVals.add(targetNormalTemp);
                }
            }
        } catch (HibernateException he) {
            refVals = null;
            log.printStackTrace(he);
        } catch (Exception e) {
            refVals = null;
            log.printStackTrace(e);
        } finally {
            //如果连接存在，则断开，结束会话，返回
            if (conn != null)
                conn.closeSession();
        }
        return refVals;
    }
    
    /**
     * 指标业务的查询
     * 
     * @param TargetNormalForm 查询表单对象
     *            
     * @return List 如果查找到记录，返回List记录集；否则，返回null
     */
    public static List select(TargetBusinessForm targetBusiness, int offset, int limit) {

        //		 List集合的定义
        List refVals = null;

        //连接对象和会话对象初始化
        DBConn conn = null;
        Session session = null;

        //	 查询条件HQL的生成
        StringBuffer hql = new StringBuffer("from MBusiness mb");
        StringBuffer where = new StringBuffer("");
        if (targetBusiness != null) {
            // 查找条件的判断,查找名称不可为空
            if (targetBusiness.getBusinessName() != null
                    && !targetBusiness.getBusinessName().equals(""))
                where.append((where.toString().equals("") ? "" : " and ")
                        + "mb.businessName like '%" + targetBusiness.getBusinessName() + "%'");
        }

        try { //List集合的操作
            //初始化
            
            hql.append((where.toString().equals("") ? "" : " where ")
                    + where.toString());
          
            //conn对象的实例化
            conn = new DBConn();
            //打开连接开始会话
            session = conn.openSession();
            //添加集合至Session
            //List list=session.find(hql.toString());
            Query query = session.createQuery(hql.toString());
            query.setFirstResult(offset).setMaxResults(limit);
            List list = query.list();

            if (list != null) {
                refVals = new ArrayList();
                //循环读取数据库符合条件记录
                for (Iterator it = list.iterator(); it.hasNext();) {
                	TargetBusinessForm targetBusinessTemp = new TargetBusinessForm();
                    MBusiness targetBusinessPersistence = (MBusiness) it.next();
                    TranslatorUtil.copyPersistenceToVo(targetBusinessPersistence,
                    		targetBusinessTemp);
                    refVals.add(targetBusinessTemp);
                }
            }
        } catch (HibernateException he) {
            refVals = null;
            log.printStackTrace(he);
        } catch (Exception e) {
            refVals = null;
            log.printStackTrace(e);
        } finally {
            //如果连接存在，则断开，结束会话，返回
            if (conn != null)
                conn.closeSession();
        }
        return refVals;
    }

    /**
     * 指标业务新增
     * 
     * @param TargetNormalForm           
     * @return boolean result,新增成功返回true,否则返回false
     * @exception Exception，捕捉异常处理
     */
    public static boolean create(TargetNormalForm targetNormalForm) throws Exception {

        boolean result = false; //置result标记
        MNormal targetNormalPersistence = new MNormal();

        if (targetNormalForm == null || targetNormalForm.getNormalName().equals("")) {
            return result;
        }
        //连接对象的初始化
        DBConn conn = null;
        //会话对象的初始化
        Session session = null;

        try {
            //表示层到持久层的CopyTo方法(McurUnitPresistence持久层对象的实例,mCurrForm表示层对象)
            if (targetNormalForm.getNormalName() == null
                    || targetNormalForm.getNormalName().equals("")) {
                return result;
            }

            TranslatorUtil.copyVoToPersistence(targetNormalPersistence, targetNormalForm);
            //实例化连接对象
            conn = new DBConn();
            //会话对象为连接对象的事务属性
            session = conn.beginTransaction();
            //mCurrForm对象的实例化
            targetNormalForm = new TargetNormalForm();
            targetNormalForm.getNormalName();

            //会话对象保存持久层对象
            session.save(targetNormalPersistence);
            //标志为true
            result = true;
        } catch (HibernateException e) {
            //持久层的异常被捕捉
            log.printStackTrace(e);
        } finally {
            //如果连接状态有,则断开,结束事务,返回
            if (conn != null)
                conn.endTransaction(result);
        }
        return result;

    }
    
    /**
     * 指标业务新增
     * 
     * @param TargetNormalForm           
     * @return boolean result,新增成功返回true,否则返回false
     * @exception Exception，捕捉异常处理
     */
    public static boolean create(TargetBusinessForm targetBusinessForm) throws Exception {

        boolean result = false; //置result标记
        MBusiness targetBusinessPersistence = new MBusiness();

        if (targetBusinessForm == null || targetBusinessForm.getBusinessName().equals("")) {
            return result;
        }
        //连接对象的初始化
        DBConn conn = null;
        //会话对象的初始化
        Session session = null;

        try {
            //表示层到持久层的CopyTo方法(McurUnitPresistence持久层对象的实例,mCurrForm表示层对象)
            if (targetBusinessForm.getBusinessName() == null
                    || targetBusinessForm.getBusinessName().equals("")) {
                return result;
            }

            TranslatorUtil.copyVoToPersistence(targetBusinessPersistence, targetBusinessForm);
            //实例化连接对象
            conn = new DBConn();
            //会话对象为连接对象的事务属性
            session = conn.beginTransaction();
            //mCurrForm对象的实例化
            targetBusinessForm = new TargetBusinessForm();
            targetBusinessForm.getBusinessName();

            //会话对象保存持久层对象
            session.save(targetBusinessPersistence);
            //标志为true
            result = true;
        } catch (HibernateException e) {
            //持久层的异常被捕捉
            log.printStackTrace(e);
        } finally {
            //如果连接状态有,则断开,结束事务,返回
            if (conn != null)
                conn.endTransaction(result);
        }
        return result;

    }
    /**
     * 指标业务删除
     * 
     * @param TargetNormalForm           
     * @return boolean result,删除成功返回true,否则返回false
     * @exception Exception，捕捉异常处理
     */
    public static boolean delete(TargetBusinessForm targetBusinessForm) throws Exception {

        boolean result = false; //置result标记
        MBusiness targetBusinessPersistence = new MBusiness();

        if (targetBusinessForm == null || targetBusinessForm.getBusinessId().equals("")) {
            return result;
        }
        //连接对象的初始化
        DBConn conn = null;
        //会话对象的初始化
        Session session = null;

        try {
            //表示层到持久层的CopyTo方法(McurUnitPresistence持久层对象的实例,mCurrForm表示层对象)
           
        	if (targetBusinessForm.getBusinessId() == null
                   ) {
                return result;
            }

            TranslatorUtil.copyVoToPersistence(targetBusinessPersistence, targetBusinessForm);
            //实例化连接对象
            conn = new DBConn();
            //会话对象为连接对象的事务属性
            session = conn.beginTransaction();
            //mCurrForm对象的实例化
            targetBusinessForm = new TargetBusinessForm();
            targetBusinessForm.getBusinessName();

            //会话对象保存持久层对象
            session.delete(targetBusinessPersistence);
            //标志为true
            result = true;
        } catch (HibernateException e) {
            //持久层的异常被捕捉
            log.printStackTrace(e);
        } finally {
            //如果连接状态有,则断开,结束事务,返回
            if (conn != null)
                conn.endTransaction(result);
        }
        return result;

    }
    
    
    
    
    /**
     * 查询一条记录,返回到EditAction中
     * 
     * @param targetNormal
     *            
     * @return targetNormal 包含一条记录
     */

    public static TargetNormalForm selectOne(TargetNormalForm targetNormal) {
    	
        //连接对象和会话对象初始化
        DBConn conn = null;
        Session session = null;

        if (targetNormal != null)
         
        try {
            if (targetNormal.getNormalId() != null
                    && !targetNormal.getNormalId().equals(""))
                //conn对象的实例化
                conn = new DBConn();
            //打开连接开始会话
            session = conn.openSession();

            MNormal targetNormalPersistence = (MNormal) session.load(MNormal.class,
            		targetNormal.getNormalId());

            TranslatorUtil.copyPersistenceToVo(targetNormalPersistence, targetNormal);

        } catch (HibernateException he) {
            log.printStackTrace(he);
        } catch (Exception e) {
            log.printStackTrace(e);
        } finally {
            //如果连接存在，则断开，结束会话，返回
            if (conn != null)
                conn.closeSession();
        }
        return targetNormal;
    }
    
    /**
     * 查询一条记录,返回到EditAction中
     * 
     * @param targetNormal
     *            
     * @return targetNormal 包含一条记录
     */

    public static TargetBusinessForm selectOne(TargetBusinessForm targetBusiness) {
    	
        //连接对象和会话对象初始化
        DBConn conn = null;
        Session session = null;

        if (targetBusiness != null)
         
        try {
            if (targetBusiness.getBusinessId() != null
                    && !targetBusiness.getBusinessId().equals(""))
                //conn对象的实例化
                conn = new DBConn();
            //打开连接开始会话
            session = conn.openSession();

            MBusiness targetBusinessPersistence = (MBusiness) session.load(MBusiness.class,
            		targetBusiness.getBusinessId());

            TranslatorUtil.copyPersistenceToVo(targetBusinessPersistence, targetBusiness);

        } catch (HibernateException he) {
            log.printStackTrace(he);
        } catch (Exception e) {
            log.printStackTrace(e);
        } finally {
            //如果连接存在，则断开，结束会话，返回
            if (conn != null)
                conn.closeSession();
        }
        return targetBusiness;
    }

    /**
     * 更新指标任务
     * 
     * @param mCurrForm
     *            MCurrForm 存放数据的对象
     * @exception Exception
     *                如果MCurrForm更新失败,则捕捉抛出异常
     */
    public static boolean update(TargetNormalForm targetNormal) throws Exception {
        boolean result = false;
        DBConn conn = null;
        Session session = null;

        MNormal targetNormalPersistence = new MNormal();

        if (targetNormal == null) {
            return result;
        }
        try {
            if (targetNormal.getNormalName() == null
                    || targetNormal.getNormalName().equals("")) {
                return result;
            }
            conn = new DBConn();
            session = conn.beginTransaction();

            TranslatorUtil.copyVoToPersistence(targetNormalPersistence, targetNormal);            
           
            session.update(targetNormalPersistence);            

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
     * 更新指标任务
     * 
     * @param mCurrForm
     *            MCurrForm 存放数据的对象
     * @exception Exception
     *                如果MCurrForm更新失败,则捕捉抛出异常
     */
    public static boolean update(TargetBusinessForm targetBusiness) throws Exception {
        boolean result = false;
        DBConn conn = null;
        Session session = null;

        MBusiness targetBusinessPersistence = new MBusiness();

        if (targetBusiness == null) {
            return result;
        }
        try {
            if (targetBusiness.getBusinessName() == null
                    || targetBusiness.getBusinessName().equals("")) {
                return result;
            }
            conn = new DBConn();
            session = conn.beginTransaction();

            TranslatorUtil.copyVoToPersistence(targetBusinessPersistence, targetBusiness);            
           
            session.update(targetBusinessPersistence);            

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
     * 指标业务删除操作
     * 
     * @param TargetNormalForm
     *            TargetNormalForm 查询表单的对象
     * @return boolean 如果删除成功则返回true,否则false
     */
    public static boolean remove(TargetNormalForm targetNormal) throws Exception {
        //置标志result
        boolean result = false;
        //连接和会话对象的初始化
        DBConn conn = null;
        Session session = null;

        //mCurr是否为空,返回result
        if (targetNormal == null && targetNormal.getNormalId() == null)
            return result;

        try {
            //	连接对象和会话对象初始化
            conn = new DBConn();
            session = conn.beginTransaction();
            //将mCurrForm的货币单位的货币主键传入HQL中查询
            MNormal targetNormals = (MNormal) session.load(MNormal.class, targetNormal
                    .getNormalId());
            //会话对象删除持久层对象
            session.delete(targetNormals);
            session.flush();

            //删除成功，置为true
            result = true;
        } catch (HibernateException he) {
            //捕捉本类的异常,抛出
            log.printStackTrace(he);
        } finally {
            //如果由连接则断开连接，结束会话，返回
            if (conn != null)
                conn.endTransaction(result);
        }
        return result;
    }
    
    /**
     * 指标业务删除操作
     * 
     * @param TargetNormalForm
     *            TargetNormalForm 查询表单的对象
     * @return boolean 如果删除成功则返回true,否则false
     */
    public static boolean remove(TargetBusinessForm targetBusiness) throws Exception {
        //置标志result
        boolean result = false;
        //连接和会话对象的初始化
        DBConn conn = null;
        Session session = null;

        //mCurr是否为空,返回result
        if (targetBusiness == null && targetBusiness.getBusinessId() == null)
            return result;

        try {
            //	连接对象和会话对象初始化
            conn = new DBConn();
            session = conn.beginTransaction();
            //将mCurrForm的货币单位的货币主键传入HQL中查询
            MBusiness targetBusiness2 = (MBusiness) session.load(MBusiness.class, targetBusiness
                    .getBusinessId());
            //会话对象删除持久层对象
            session.delete(targetBusiness2);
            session.flush();

            //删除成功，置为true
            result = true;
        } catch (HibernateException he) {
            //捕捉本类的异常,抛出
            log.printStackTrace(he);
        } finally {
            //如果由连接则断开连接，结束会话，返回
            if (conn != null)
                conn.endTransaction(result);
        }
        return result;
    }

    /**
     * 查询所有的指标类型
     * @author  gongming
     * @date    2008-06-19
     * @return List
     */
    public static List findAll(){
        List tarLst = null;
        DBConn dbCon = null;
        Session s = null;
        try{
            dbCon = new DBConn();
            s = dbCon.openSession();
            Criteria c = s.createCriteria(MBusiness.class);
            tarLst = c.list();      
        }catch (Exception e){
            log.printStackTrace(e);
        }finally{
            if(dbCon != null)
                dbCon.closeSession();
        }
        return tarLst;
    }
    /**
     *  
     */
    public static List selectInfo(String id){
    	List result=null;
    	DBConn dbCon = null;
        Session s = null;
    	try{
    		dbCon = new DBConn();
    		s=dbCon.openSession();
    	
    		System.out.println(id);
    		result=s.createQuery("from TargetDefine a where a.mbusiness.businessId="+id).list();
    	//	String hql="from MBusiness b join TargetDefine d on b.mbusiness=d.mbusiness where b.NM_BUSINESSSORTID="+id;
    	//	result=s.getSessionFactory().openSession().;
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	
    	return result;
    }
    /**
     *  
     * @param id
     * @return
     */
    public static List selectNormalInfo(String id){
    	List result=null;
    	DBConn dbCon = null;
        Session s = null;
    	try{
    		dbCon = new DBConn();
    		s=dbCon.openSession();
    	
    		System.out.println(id);
    		result=s.createQuery("from TargetDefine a where a.mnormal.normalId="+id).list();
    	//	String hql="from MBusiness b join TargetDefine d on b.mbusiness=d.mbusiness where b.NM_BUSINESSSORTID="+id;
    	//	result=s.getSessionFactory().openSession().;
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	
    	return result;
    }
}