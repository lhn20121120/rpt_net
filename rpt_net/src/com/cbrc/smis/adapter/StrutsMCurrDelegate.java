package com.cbrc.smis.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.form.MCurrForm;
import com.cbrc.smis.hibernate.MCurr;
import com.cbrc.smis.util.FitechException;

/**
 * @StrutsMCurrDelegate 币种表单Delegate对象
 * 
 * @author 唐磊
 */
public class StrutsMCurrDelegate {
    //Catch到本类的抛出的所有异常
    private static FitechException log = new FitechException(
            StrutsMCurrDelegate.class);

    /**
     * 校验类别新增
     * 
     * @param mRepFreqForm
     *            MCurrForm
     * @return boolean result,新增成功返回true,否则返回false
     * @exception Exception，捕捉异常处理
     */
    public static boolean create(MCurrForm mCurrForm) throws Exception {

        boolean result = false; //置result标记
        MCurr mCurrPersistence = new MCurr();

        if (mCurrForm == null || mCurrForm.getCurName().equals("")) {
            return result;
        }
        //连接对象的初始化
        DBConn conn = null;
        //会话对象的初始化
        Session session = null;

        try {
            //表示层到持久层的CopyTo方法(McurUnitPresistence持久层对象的实例,mCurrForm表示层对象)
            if (mCurrForm.getCurName() == null
                    || mCurrForm.getCurName().equals("")) {
                return result;
            }

            TranslatorUtil.copyVoToPersistence(mCurrPersistence, mCurrForm);
            //实例化连接对象
            conn = new DBConn();
            //会话对象为连接对象的事务属性
            session = conn.beginTransaction();
            //mCurrForm对象的实例化
            mCurrForm = new MCurrForm();
            mCurrForm.getCurName();

            //会话对象保存持久层对象
            session.save(mCurrPersistence);
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
     * 取得按条件查询到的记录条数
     * 
     * @return int 查询到的记录条数
     * @param mCurrForm
     *            包含查询的条件信息（币种ID，币种名称）
     */

    public static int getRecordCount(MCurrForm mCurrForm) {
        int count = 0;

        //连接对象和会话对象初始化
        DBConn conn = null;
        Session session = null;

        //	 查询条件HQL的生成
        StringBuffer hql = new StringBuffer("select count(*) from MCurr mc");
        StringBuffer where = new StringBuffer("");

        if (mCurrForm != null) {
            // 查找条件的判断,查找名称不可为空
            if (mCurrForm.getCurName() != null
                    && !mCurrForm.getCurName().equals(""))
                where.append((where.toString().equals("") ? "" : " and ")
                        + "mc.curName like '%" + mCurrForm.getCurName() + "%'");
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
     * 币种的查询
     * 
     * @param mCurrForm
     *            MCurrForm 查询表单对象
     * @return List 如果查找到记录，返回List记录集；否则，返回null
     */
    public static List select(MCurrForm mCurrForm, int offset, int limit) {

        //		 List集合的定义
        List refVals = null;

        //连接对象和会话对象初始化
        DBConn conn = null;
        Session session = null;

        //	 查询条件HQL的生成
        StringBuffer hql = new StringBuffer("from MCurr mc");
        StringBuffer where = new StringBuffer("");
        if (mCurrForm != null) {
            // 查找条件的判断,查找名称不可为空
            if (mCurrForm.getCurName() != null
                    && !mCurrForm.getCurName().equals(""))
                where.append((where.toString().equals("") ? "" : " and ")
                        + "mc.curName like '%" + mCurrForm.getCurName() + "%'");
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
                    MCurrForm mCurrFormTemp = new MCurrForm();
                    MCurr mCurrPersistence = (MCurr) it.next();
                    TranslatorUtil.copyPersistenceToVo(mCurrPersistence,
                            mCurrFormTemp);
                    refVals.add(mCurrFormTemp);
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
     * 更新币种
     * 
     * @param mCurrForm
     *            MCurrForm 存放数据的对象
     * @exception Exception
     *                如果MCurrForm更新失败,则捕捉抛出异常
     */
    public static boolean update(MCurrForm mCurrForm) throws Exception {
        boolean result = false;
        DBConn conn = null;
        Session session = null;

        MCurr mCurrPersistence = new MCurr();

        if (mCurrForm == null) {
            return result;
        }
        try {
            if (mCurrForm.getCurName() == null
                    || mCurrForm.getCurName().equals("")) {
                return result;
            }
            conn = new DBConn();
            session = conn.beginTransaction();

            TranslatorUtil.copyVoToPersistence(mCurrPersistence, mCurrForm);
            session.update(mCurrPersistence);

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
     * @param mCurrForm
     *            The MCurrForm 保持数据的传递
     * @exception Exception
     *                如果 MCurrForm 对象丢失则抛出异常.
     */
    public static boolean edit(MCurrForm mCurrForm) throws Exception {
        boolean result = false;
        DBConn conn = null;
        Session session = null;

        MCurr mCurrPersistence = new MCurr();
        mCurrForm = new MCurrForm();
        mCurrForm.getCurName();

        if (mCurrForm == null) {
            return result;
        }

        try {
            if (mCurrForm.getCurName() == null
                    && mCurrForm.getCurName().equals("")) {
                return result;
            }
            conn = new DBConn();
            session = conn.beginTransaction();

            mCurrPersistence = (MCurr) session.load(MCurr.class, mCurrForm
                    .getCurName());

            TranslatorUtil.copyVoToPersistence(mCurrPersistence, mCurrForm);

        } catch (HibernateException he) {
            log.printStackTrace(he);
        } finally {
            if (conn != null)
                conn.endTransaction(result);
        }
        return result;
    }

    /**
     * 删除操作
     * 
     * @param mCurrForm
     *            MCurrForm 查询表单的对象
     * @return boolean 如果删除成功则返回true,否则false
     */
    public static boolean remove(MCurrForm mCurrForm) throws Exception {
        //置标志result
        boolean result = false;
        //连接和会话对象的初始化
        DBConn conn = null;
        Session session = null;

        //mCurr是否为空,返回result
        if (mCurrForm == null && mCurrForm.getCurId() == null)
            return result;

        try {
            //	连接对象和会话对象初始化
            conn = new DBConn();
            session = conn.beginTransaction();
            //将mCurrForm的货币单位的货币主键传入HQL中查询
            MCurr mCurr = (MCurr) session.load(MCurr.class, mCurrForm
                    .getCurId());
            //会话对象删除持久层对象
            session.delete(mCurr);
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
     * 查询一条记录,返回到EditAction中
     * 
     * @param mCurrForm
     *            MCurrForm实例化对象
     * @return MCurrForm 包含一条记录
     */

    public static MCurrForm selectOne(MCurrForm mCurrForm) {

        //连接对象和会话对象初始化
        DBConn conn = null;
        Session session = null;

        if (mCurrForm != null)
         
        try {
            if (mCurrForm.getCurId() != null
                    && !mCurrForm.getCurId().equals(""))
                //conn对象的实例化
                conn = new DBConn();
            //打开连接开始会话
            session = conn.openSession();

            MCurr mCurrPersistence = (MCurr) session.load(MCurr.class,
                    mCurrForm.getCurId());

            TranslatorUtil.copyPersistenceToVo(mCurrPersistence, mCurrForm);

        } catch (HibernateException he) {
            log.printStackTrace(he);
        } catch (Exception e) {
            log.printStackTrace(e);
        } finally {
            //如果连接存在，则断开，结束会话，返回
            if (conn != null)
                conn.closeSession();
        }
        return mCurrForm;
    }

    /**
     * Retrieve all existing MCurrForm objects.
     * 
     * @exception Exception
     *                If the MCurrForm objects cannot be retrieved.
     */
    public static List findAll() throws Exception {
        List retVals = new ArrayList();
        DBConn conn=new DBConn();
        Session session = null;
        session=conn.openSession();
        net.sf.hibernate.Transaction tx = session.beginTransaction();
        retVals.addAll(session.find("from com.cbrc.smis.hibernate.MCurr m order by m.curId"));
        tx.commit();
        session.close();
        ArrayList mCurr_vos = new ArrayList();
        for (Iterator it = retVals.iterator(); it.hasNext();) {
            MCurrForm mCurrFormTemp = new MCurrForm();
            com.cbrc.smis.hibernate.MCurr mCurrPersistence = (com.cbrc.smis.hibernate.MCurr) it
                    .next();
            TranslatorUtil.copyPersistenceToVo(mCurrPersistence, mCurrFormTemp);
            mCurr_vos.add(mCurrFormTemp);
        }
        retVals = mCurr_vos;
        return retVals;
    }
    
    /***
     * 已使用hibernate 卞以刚 2011-12-21
     * @param curName
     * @return
     * @throws Exception
     */
    public static MCurr getMCurr(String curName) throws Exception {

        MCurr mCurr = null;

        DBConn dBConn = null;

        Session session = null;
        
        Query  query = null;
        
        List l = null;
        
        String hsql = "from MCurr mc where mc.curName=:curName";

        try {

            dBConn = new DBConn();

            session = dBConn.openSession();

            query = session.createQuery(hsql);
            
            query.setString("curName",curName);
            
            l = query.list();
            
            int  size = l.size();
            
            if(size!=0)
                
                mCurr = (MCurr)l.get(0);

        } catch (HibernateException e) {

            log.printStackTrace(e);

        }
        finally{
            
            if(session!=null)
                
                dBConn.closeSession();
        }

        return   mCurr;
    }
    /***
     * 已使用hibernate 卞以刚 2011-12-21
     * 影响对象：MCurr
     * @param curId
     * @return
     * @throws Exception
     */
    public static MCurr getISMCurr(String curId) throws Exception {

        MCurr mCurr = null;
        DBConn dBConn = null;
        try {
            dBConn = new DBConn();
            String hsql = "from MCurr mc where mc.curId="+curId;
            List list = dBConn.openSession().find(hsql);
            if(list!=null && list.size()!=0)
                
                mCurr = (MCurr)list.get(0);

        } catch (HibernateException e) {

            log.printStackTrace(e);

        }finally {
                //如果连接存在，则断开，结束会话，返回
                if (dBConn != null)
                	dBConn.closeSession();
        }

        return   mCurr;
    }
}