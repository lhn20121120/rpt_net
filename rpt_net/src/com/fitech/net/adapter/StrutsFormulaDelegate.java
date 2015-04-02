package com.fitech.net.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.util.FitechException;
import com.fitech.net.form.EtlIndexForm;
import com.fitech.net.hibernate.EtlIndex;

/**
 * 对公式进行操作
 * @author wh
 *
 */
public class StrutsFormulaDelegate {
//	Catch到本类的抛出的所有异常
    private static FitechException log = new FitechException(StrutsVParameterDelegate.class);
	/**
	 * 查询所有指标公式的信息,,
	 * @return  List
	 */
	public static List selectTargetFormula(){
		 //	List集合的定义
        List resList = null;

        //连接对象和会话对象初始化
        DBConn conn = null;
        Session session = null;
        try {
            //conn对象的实例化
            conn = new DBConn();
            //打开连接开始会话
            session = conn.openSession();
            //添加集合至Session
            //List list=session.find(hql.toString());
           String sql="from EtlIndex etl";

            List list = conn.openSession().find(sql);

            if (list != null) {
            	resList = new ArrayList();
                //循环读取数据库符合条件记录
                for (Iterator it = list.iterator(); it.hasNext();) {
                	EtlIndexForm etlIndexForm = new EtlIndexForm();
                	EtlIndex etlIndexPersistence = (EtlIndex)it.next();
                   TranslatorUtil.copyPersistenceToVo(etlIndexPersistence,
                    		etlIndexForm);
                    resList.add(etlIndexForm);
                }
            }
        } catch (HibernateException he) {
        	resList = null;
            log.printStackTrace(he);
        } catch (Exception e) {
        	resList = null;
            log.printStackTrace(e);
        } finally {
            //如果连接存在，则断开，结束会话，返回
            if (conn != null)
                conn.closeSession();
        }
        return resList;
	}
	 /**
     * 取得按条件查询到的记录条数
     * 
     * @return int 查询到的记录条数
     * @param EtlIndexForm
     *            包含查询的条件信息（指标名称）
     */

    public static int getEtlIndexRecordCount(EtlIndexForm etlIndexForm) {
        int count = 0;

        //连接对象和会话对象初始化
        DBConn conn = null;
        Session session = null;

        //	 查询条件HQL的生成
        StringBuffer hql = new StringBuffer("select count(*) from EtlIndex etlIndex");
        StringBuffer where = new StringBuffer("");

        if (etlIndexForm != null) {
            // 查找条件的判断,查找名称不可为空
            if (etlIndexForm.getIndexName() != null
                    && !etlIndexForm.getIndexName().equals(""))
                where.append((where.toString().equals("") ? "" : " and ")
                        + "etlIndex.indexName like '%" + etlIndexForm.getIndexName() + "%'");
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
     * 取数公式指标的查询
     * 
     * @param  etlIndexForm
     *            etlIndexForm 查询表单对象
     * @return List 如果查找到记录，返回List记录集；否则，返回null
     */
    public static List selectEtlIndex(EtlIndexForm etlIndexForm, int offset, int limit) {

        //	List集合的定义
        List refVals = null;

        //连接对象和会话对象初始化
        DBConn conn = null;
        Session session = null;

        //	查询条件HQL的生成
        StringBuffer hql = new StringBuffer("from EtlIndex etlIndex");
        StringBuffer where = new StringBuffer("");
        if (etlIndexForm != null) {
            // 查找条件的判断,查找名称不可为空
            if (etlIndexForm.getIndexName() != null
                    && !etlIndexForm.getIndexName().equals(""))
                where.append((where.toString().equals("") ? "" : " and ")
                        + "etlIndex.indexName like '%" + etlIndexForm.getIndexName() + "%'");
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
                	EtlIndexForm etlForm = new EtlIndexForm();
                	EtlIndex etlIndexPersistence = (EtlIndex)it.next();
                    TranslatorUtil.copyPersistenceToVo(etlIndexPersistence,
                    		etlForm);
                    refVals.add(etlForm);
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
     * 删除操作
     * 
     * @param EtlIndexForm
     *            EtlIndexForm 查询表单的对象
     * @return boolean 如果删除成功则返回true,否则false
     */
    public static boolean removeEtlIndex(EtlIndexForm etlIndexForm) throws Exception {
        //置标志result
        boolean result = false;
        //连接和会话对象的初始化
        DBConn conn = null;
        Session session = null;

        //mCurr是否为空,返回result
        if (etlIndexForm == null && etlIndexForm.getIndexName() == null)
            return result;

        try {
            //	连接对象和会话对象初始化
            conn = new DBConn();
            session = conn.beginTransaction();
            //将vParamForm的货币单位的货币主键传入HQL中查询
            EtlIndex etlIndex = (EtlIndex) session.load(EtlIndex.class, etlIndexForm.getIndexName());
            //会话对象删除持久层对象
            session.delete(etlIndex);
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
     * @param etlIndexForm
     *            etlIndexForm实例化对象
     * @return etlIndexForm 包含一条记录
     */

    public static EtlIndexForm selectOneEtlIndex(EtlIndexForm etlIndexForm) {

        //连接对象和会话对象初始化
        DBConn conn = null;
        Session session = null;

        if (etlIndexForm != null)
         
        try {
            if (etlIndexForm.getIndexName() != null
                    && !etlIndexForm.getIndexName().equals(""))
                //conn对象的实例化
                conn = new DBConn();
            //打开连接开始会话
            session = conn.openSession();

            EtlIndex mCurrPersistence = (EtlIndex) session.load(EtlIndex.class,
            		etlIndexForm.getIndexName());

            TranslatorUtil.copyPersistenceToVo(mCurrPersistence, etlIndexForm);

        } catch (HibernateException he) {
            log.printStackTrace(he);
        } catch (Exception e) {
            log.printStackTrace(e);
        } finally {
            //如果连接存在，则断开，结束会话，返回
            if (conn != null)
                conn.closeSession();
        }
        return etlIndexForm;
    }
    /**
     * 更新指标公式
     * 
     * @param EtlIndexForm
     *            EtlIndexForm 存放数据的对象
     * @exception Exception
     *                如果VParameterForm更新失败,则捕捉抛出异常
     */
    public static boolean updateEtlIndex(EtlIndexForm etlIndexForm) throws Exception {
        boolean result = false;
        DBConn conn = null;
        Session session = null;

        EtlIndex etlIndexPersistence = new EtlIndex();

        if (etlIndexForm == null) {
            return result;
        }
        try {
            if (etlIndexForm.getIndexName() == null
                    || etlIndexForm.getIndexName().equals("")) {
                return result;
            }
            conn = new DBConn();
            session = conn.beginTransaction();

            TranslatorUtil.copyVoToPersistence(etlIndexPersistence, etlIndexForm);
            
            session.update(etlIndexPersistence);

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
    * 查找该指标名是否已经存在
    * @param etlIndexForm
    * @return
    */
   public static boolean isExist(EtlIndexForm etlIndexForm) {

   	boolean bool = false;
       //连接对象和会话对象初始化
       DBConn conn = null;
       Session session = null;

       if (etlIndexForm == null) return bool;         
       try {
       	
       	String hql = "select count(*) from EtlIndex etl where etl.indexName='"+ etlIndexForm.getIndexName()+"'";
       	
       	conn = new DBConn();
           session = conn.openSession();

           List list = session.find(hql.toString());
           if (list != null && list.size() > 0) {
        	   int size=((Integer)list.get(0)).intValue();
        	   if(size>0){
        		   bool = true;
        	   }
               
           }


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
    *  新增指标
    * 
    * @param mRepFreqForm etlIndexForm
    * @return boolean result,新增成功返回true,否则返回false
    * @exception Exception，捕捉异常处理
    */
   public static boolean createEtlIndex(EtlIndexForm etlIndexForm) throws Exception {

       boolean result = false; //置result标记
       EtlIndex etlIndexPersistence = new EtlIndex();

       if (etlIndexForm == null) {
           return result;
       }
       DBConn conn = null;        
       Session session = null;

       try {
           //表示层到持久层的CopyTo方法(McurUnitPresistence持久层对象的实例,vParamForm表示层对象)            
           TranslatorUtil.copyVoToPersistence(etlIndexPersistence, etlIndexForm);
           //实例化连接对象
           conn = new DBConn();
           session = conn.beginTransaction();
          
           session.save(etlIndexPersistence);            
           session.flush();
           
           result = true;
       } catch (HibernateException e) {
       	result = false;
           log.printStackTrace(e);
       } finally {
           //如果连接状态有,则断开,结束事务,返回
           if (conn != null)
               conn.endTransaction(result);
       }
       return result;
   }

}
