package com.cbrc.smis.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.form.MDataRgTypeForm;
import com.cbrc.smis.hibernate.MDataRgType;
import com.cbrc.smis.hibernate.MRepFreq;
import com.cbrc.smis.util.FitechException;

/**
 * @StrutsMDataRgTypeDelegate 数据范围表单Delegate
 * 
 * @author 唐磊
 */
public class StrutsMDataRgTypeDelegate {
    //Catch到本类的抛出的所有异常
    private static FitechException log = new FitechException(
            StrutsMDataRgTypeDelegate.class);
    
    /**
     * 
     * 
     */
    public static String getdatename(Integer dataid){
    	if(dataid==null)return null;
    	 String dataname=null; 
    	DBConn conn=null;
    	Session session=null;
    //	// System.out.println("dataid is "+dataid);
    	try{
    		
    		String hql="from MDataRgType mdrt where mdrt.dataRangeId="+dataid;
    		conn=new DBConn();
    		List list=conn.openSession().find(hql);
    		if(list!= null &&list.size()>0)
    		{ 
    			MDataRgTypeForm mDataRgTypeForm =new MDataRgTypeForm();
    			MDataRgType mDataRgTypePersistence=(MDataRgType)list.get(0);
    		    TranslatorUtil.copyPersistenceToVo(mDataRgTypePersistence,mDataRgTypeForm);
    		    dataname=mDataRgTypeForm.getDataRgDesc();
    		}
    	}catch(Exception e)
    		{
    			dataname=null;
    			log.printStackTrace(e);
    		}finally{
    			if(conn!=null)
    				conn.closeSession();
    		}
    	return dataname;
    }

    /**
     * 数据范围新增
     * 
     * @param mDataRgTypeForm
     *            MDataRgTypeForm
     * @return boolean result,新增成功返回true,否则返回false
     * @exception Exception，捕捉异常处理
     */
    public static boolean create(MDataRgTypeForm mDataRgTypeForm)
            throws Exception {

        boolean result = false; //置result标记
        MDataRgType mDataRgTypePersistence = new MDataRgType();

        if (mDataRgTypeForm == null
                || mDataRgTypeForm.getDataRgDesc().equals("")) {
            return result;
        }
        //连接对象的初始化
        DBConn conn = null;
        //会话对象的初始化
        Session session = null;

        try {
            //表示层到持久层的CopyTo方法(McurUnitPresistence持久层对象的实例,mDataRgTypeForm表示层对象)
            if (mDataRgTypeForm.getDataRgDesc() == null
                    || mDataRgTypeForm.getDataRgDesc().equals("")) {
                return result;
            }

            TranslatorUtil.copyVoToPersistence(mDataRgTypePersistence,mDataRgTypeForm);
            //实例化连接对象
            conn = new DBConn();
            //会话对象为连接对象的事务属性
            session = conn.beginTransaction();
            //mDataRgTypeForm对象的实例化
           
            //会话对象保存持久层对象
            session.save(mDataRgTypePersistence);
            session.flush();
            TranslatorUtil.copyPersistenceToVo(mDataRgTypePersistence,mDataRgTypeForm);
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
     * @param mDataRgTypeForm
     *            包含查询的条件信息（数据范围ID，数据范围）
     */

    public static int getRecordCount(MDataRgTypeForm mDataRgTypeForm) {
        int count = 0;

        //连接对象和会话对象初始化
        DBConn conn = null;
        Session session = null;

        //	 查询条件HQL的生成
        StringBuffer hql = new StringBuffer(
                "select count(*) from MDataRgType mdrt");
        StringBuffer where = new StringBuffer("");

        if (mDataRgTypeForm != null) {
            // 查找条件的判断,查找名称不可为空
            if (mDataRgTypeForm.getDataRgDesc() != null
                    && !mDataRgTypeForm.getDataRgDesc().equals(""))
                where.append((where.toString().equals("") ? "" : " and ")
                        + "mdrt.dataRgDesc like '%"
                        + mDataRgTypeForm.getDataRgDesc() + "%'");
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
     * 数据范围的查询
     * 
     * @param mDataRgTypeForm
     *            MDataRgTypeForm 查询表单对象
     * @return List 如果查找到记录，返回List记录集；否则，返回null
     */
    public static List select(MDataRgTypeForm mDataRgTypeForm, int offset,
            int limit) {

        //	 List集合的定义
        List refVals = null;

        //连接对象和会话对象初始化
        DBConn conn = null;
        Session session = null;

        //	 查询条件HQL的生成
        StringBuffer hql = new StringBuffer("from MDataRgType mdrt");
        StringBuffer where = new StringBuffer("");
      
        if (mDataRgTypeForm != null) {
            // 查找条件的判断,查找名称不可为空
            if (mDataRgTypeForm.getDataRgDesc() != null
                    && !mDataRgTypeForm.getDataRgDesc().equals(""))
                where.append((where.toString().equals("") ? "" : " and ")
                        + "mdrt.dataRgDesc like '%"
                        + mDataRgTypeForm.getDataRgDesc() + "%'");
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
                    MDataRgTypeForm mDataRgTypeFormTemp = new MDataRgTypeForm();
                    MDataRgType mDataRgTypePersistence = (MDataRgType) it
                            .next();
                    TranslatorUtil.copyPersistenceToVo(mDataRgTypePersistence,
                            mDataRgTypeFormTemp);
                    refVals.add(mDataRgTypeFormTemp);
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
     * 更新MDataRgTypeForm对象
     * 
     * @param mDataRgTypeForm
     *            MDataRgTypeForm 存放数据的对象
     * @exception Exception
     *                如果MDataRgTypeForm更新失败,则捕捉抛出异常
     */
    public static boolean update(MDataRgTypeForm mDataRgTypeForm)
            throws Exception {
        boolean result = false;
        DBConn conn = null;
        Session session = null;

        MDataRgType mDataRgTypePersistence = new MDataRgType();

        if (mDataRgTypeForm == null) {
            return result;
        }
        try {
            if (mDataRgTypeForm.getDataRgDesc() == null
                    || mDataRgTypeForm.getDataRgDesc().equals("")) {
                return result;
            }
            conn = new DBConn();
            session = conn.beginTransaction();

            TranslatorUtil.copyVoToPersistence(mDataRgTypePersistence,
                    mDataRgTypeForm);
            session.update(mDataRgTypePersistence);

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
     * @param mDataRgTypeForm
     *            The MDataRgTypeForm 保持数据的传递
     * @exception Exception
     *                如果 MDataRgTypeForm 对象丢失则抛出异常.
     */
    public static boolean edit(MDataRgTypeForm mDataRgTypeForm)
            throws Exception {
        boolean result = false;
        DBConn conn = null;
        Session session = null;

        MDataRgType mDataRgTypePersistence = new MDataRgType();
        mDataRgTypeForm = new MDataRgTypeForm();
        mDataRgTypeForm.getDataRgDesc();

        if (mDataRgTypeForm == null) {
            return result;
        }

        try {
            if (mDataRgTypeForm.getDataRgDesc() == null
                    && mDataRgTypeForm.getDataRgDesc().equals("")) {
                return result;
            }
            conn = new DBConn();
            session = conn.beginTransaction();

            mDataRgTypePersistence = (MDataRgType) session.load(
                    MDataRgType.class, mDataRgTypeForm.getDataRgDesc());

            TranslatorUtil.copyVoToPersistence(mDataRgTypePersistence,
                    mDataRgTypeForm);

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
     * @param mDataRgTypeForm
     *            MDataRgTypeForm 查询表单的对象
     * @return boolean 如果删除成功则返回true,否则false
     */
    public static boolean remove(MDataRgTypeForm mDataRgTypeForm)
            throws Exception {
        //置标志result
        boolean result = false;
        //连接和会话对象的初始化
        DBConn conn = null;
        Session session = null;

        //mDataRgType是否为空,返回result
        if (mDataRgTypeForm == null && mDataRgTypeForm.getDataRangeId() == null)
            return result;

        try {
            //	连接对象和会话对象初始化
            conn = new DBConn();
            session = conn.beginTransaction();
            //将mDataRgTypeForm的货币单位的货币主键传入HQL中查询
            MDataRgType mDataRgType = (MDataRgType) session.load(
                    MDataRgType.class, mDataRgTypeForm.getDataRangeId());
            //会话对象删除持久层对象
            TranslatorUtil.copyPersistenceToVo(mDataRgType,mDataRgTypeForm);
            session.delete(mDataRgType);
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
     * @param mDataRgTypeForm
     *            MDataRgTypeForm实例化对象
     * @return MDataRgTypeForm 包含一条记录
     */

    public static MDataRgTypeForm selectOne(MDataRgTypeForm mDataRgTypeForm) {

        //连接对象和会话对象初始化
        DBConn conn = null;
        Session session = null;

        if (mDataRgTypeForm != null)
           
        try {
            if (mDataRgTypeForm.getDataRangeId() != null
                    && !mDataRgTypeForm.getDataRangeId().equals(""))
                //conn对象的实例化
                conn = new DBConn();
            //打开连接开始会话
            session = conn.openSession();

            MDataRgType mDataRgTypePersistence = (MDataRgType) session.load(
                    MDataRgType.class, mDataRgTypeForm.getDataRangeId());

            TranslatorUtil.copyPersistenceToVo(mDataRgTypePersistence,
                    mDataRgTypeForm);

        } catch (HibernateException he) {
            log.printStackTrace(he);
        } catch (Exception e) {
            log.printStackTrace(e);
        } finally {
            //如果连接存在，则断开，结束会话，返回
            if (conn != null)
                conn.closeSession();
        }
        return mDataRgTypeForm;
    }

    /**
     * 查询所有记录
     * 
     * @return List
     * @exception Exception
     *                If the MDataRgTypeForm objects cannot be retrieved.
     */
    public static List findAll() throws Exception {

        //	 List集合的定义
        List refVals = null;

        //连接对象和会话对象初始化
        DBConn conn = null;
        Session session = null;

        //	 查询条件HQL的生成
        StringBuffer hql = new StringBuffer("from MDataRgType mrt order by mrt.dataRangeId");
        try {
            //conn对象的实例化
            conn = new DBConn();
            //打开连接开始会话
            session = conn.openSession();
            //添加集合至Session
            //List list=session.find(hql.toString());
            Query query = session.createQuery(hql.toString());
            List list = query.list();
            if (list != null) {
                refVals = new ArrayList();
                //循环读取数据库符合条件记录
                for (Iterator it = list.iterator(); it.hasNext();) {
                    MDataRgTypeForm mDataRgTypeFormTemp = new MDataRgTypeForm();
                    MDataRgType mDataRgTypePersistence = (MDataRgType) it
                            .next();
                    TranslatorUtil.copyPersistenceToVo(mDataRgTypePersistence,
                            mDataRgTypeFormTemp);
                    refVals.add(mDataRgTypeFormTemp);
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
     * @author  cb
     * 
     * 根据输入的ID号找到对应的数据范围对象
     * 
     * @param dataRgId  数据范围ID号
     * @return
     * @throws Exception
     */
    public static MDataRgType getMDataRgTypeOncb(Integer dataRgId)
            throws Exception {

        DBConn dBConn = null;

        Session session = null;

        MDataRgType mrt = null;

        try {

            dBConn = new DBConn();

            session = dBConn.openSession();

            mrt = (MDataRgType) session.get(MDataRgType.class, dataRgId);

        } catch (Exception e) {

        	mrt = null;
        	
         // e.printStackTrace();
        }
        finally{
            
            if(session!=null)
                
                dBConn.closeSession();
        }

        return  mrt;
    }
    /**
     * 已使用hibernate 卞以刚 2011-12-21
     * 影响对象：MRepFreq
     * 得到报送频度
     * @param repFreqId
     * @return
     * @throws Exception
     */
    public  MRepFreq getActuRepFlag(Integer repFreqId) throws Exception {

    	MRepFreq mRepFreq = null;
    	
        //连接对象和会话对象初始化
        DBConn conn = null;
        Session session = null;

        //	 查询条件HQL的生成
        StringBuffer hql = new StringBuffer("from MRepFreq mrt where mrt.repFreqId="+ repFreqId );
        try {
            //conn对象的实例化
            conn = new DBConn();
            //打开连接开始会话
            session = conn.openSession();
            //添加集合至Session
            //List list=session.find(hql.toString());
            Query query = session.createQuery(hql.toString());
            List list = query.list();
            if (list != null && list.size() > 0) {
            	mRepFreq = (MRepFreq)list.get(0);
            }
        } catch (HibernateException he) {
        	mRepFreq = null;
            log.printStackTrace(he);
        } catch (Exception e) {
        	mRepFreq = null;
            log.printStackTrace(e);
        } finally {
            //如果连接存在，则断开，结束会话，返回
            if (conn != null)
                conn.closeSession();
        }
        return mRepFreq;
    }
    
    /***
     * 已使用hibernate 卞以刚 2011-12-21
     * @param dataRangeId
     * @return
     * @throws Exception
     */
    public static MDataRgType selectOneByName(String dataRangeId) throws Exception {

    	MDataRgType mDataRgType = null;
    	
        //连接对象和会话对象初始化
        DBConn conn = null;
        Session session = null;

        //	 查询条件HQL的生成
        StringBuffer hql = new StringBuffer("from MDataRgType mrt where mrt.dataRangeId="+ dataRangeId );
        try {
            //conn对象的实例化
            conn = new DBConn();
            //打开连接开始会话
            session = conn.openSession();
            //添加集合至Session
            //List list=session.find(hql.toString());
            Query query = session.createQuery(hql.toString());
            List list = query.list();
            if (list != null && list.size() > 0) {
                mDataRgType = (MDataRgType)list.get(0);
            }
        } catch (HibernateException he) {
        	mDataRgType = null;
            log.printStackTrace(he);
        } catch (Exception e) {
        	mDataRgType = null;
            log.printStackTrace(e);
        } finally {
            //如果连接存在，则断开，结束会话，返回
            if (conn != null)
                conn.closeSession();
        }
        return mDataRgType;
    }
    
    /**
     * 根据报送口径名称查询报送口径对象
     * 
     * @param dataRgDesc 报送口径名称
     * @return MDataRgType 报送口径对象
     * @throws Exception
     */
    public static MDataRgType selectDataRange(String dataRgDesc) throws Exception {
    	MDataRgType mDataRgType = null;
        DBConn conn = null;
        Session session = null;

        StringBuffer hql = new StringBuffer("from MDataRgType mrt where mrt.dataRgDesc='"+ dataRgDesc +"'");
        try {
            conn = new DBConn();
            session = conn.openSession();
            Query query = session.createQuery(hql.toString());
            List list = query.list();
            if (list != null && list.size() > 0) {
                mDataRgType = (MDataRgType)list.get(0);
            }
        } catch (HibernateException he) {
        	mDataRgType = null;
            log.printStackTrace(he);
        } catch (Exception e) {
        	mDataRgType = null;
            log.printStackTrace(e);
        } finally {
            //如果连接存在，则断开，结束会话，返回
            if (conn != null)
                conn.closeSession();
        }
        return mDataRgType;
    }
}