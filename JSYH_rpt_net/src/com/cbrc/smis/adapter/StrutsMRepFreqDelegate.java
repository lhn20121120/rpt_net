package com.cbrc.smis.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.form.MRepFreqForm;
import com.cbrc.smis.hibernate.MRepFreq;
import com.cbrc.smis.util.FitechException;

/**
 * @StrutsMRepFreqDelegate 上报频度表单Delegate
 * 
 * @author 唐磊
 */
public class StrutsMRepFreqDelegate {
	// Catch到本类的抛出的所有异常
	private static FitechException log = new FitechException(
			StrutsMRepFreqDelegate.class);

	/**
	 * 上报频度新增
	 * 
	 * @param mRepFreqForm
	 *            MRepFreqForm
	 * @return boolean result,新增成功返回true,否则返回false
	 * @exception Exception，捕捉异常处理
	 */
	public static boolean create(MRepFreqForm mRepFreqForm) throws Exception {

		boolean result = false; // 置result标记
		MRepFreq mRepFreqPersistence = new MRepFreq();

		if (mRepFreqForm == null || mRepFreqForm.getRepFreqName().equals("")) {
			return result;
		}
		// 连接对象的初始化
		DBConn conn = null;
		// 会话对象的初始化
		Session session = null;

		try {
			// 表示层到持久层的CopyTo方法(McurUnitPresistence持久层对象的实例,mRepFreqForm表示层对象)
			if (mRepFreqForm.getRepFreqName() == null
					|| mRepFreqForm.getRepFreqName().equals("")) {
				return result;
			}

			TranslatorUtil.copyVoToPersistence(mRepFreqPersistence,
					mRepFreqForm);
			// 实例化连接对象
			conn = new DBConn();
			// 会话对象为连接对象的事务属性
			session = conn.beginTransaction();
			// mRepFreqForm对象的实例化

			// 会话对象保存持久层对象
			session.save(mRepFreqPersistence);
			session.flush();
			TranslatorUtil.copyPersistenceToVo(mRepFreqPersistence,
					mRepFreqForm);
			// 标志为true
			result = true;
		} catch (HibernateException e) {
			// 持久层的异常被捕捉
			log.printStackTrace(e);
		} finally {
			// 如果连接状态有,则断开,结束事务,返回
			if (conn != null)
				conn.endTransaction(result);
		}
		return result;

	}

	/**
	 * 取得按条件查询到的记录条数
	 * 
	 * @return int 查询到的记录条数
	 * @param mRepFreqForm
	 *            包含查询的条件信息（上报频度ID，上报频度名称）
	 */

	public static int getRecordCount(MRepFreqForm mRepFreqForm) {
		int count = 0;

		// 连接对象和会话对象初始化
		DBConn conn = null;
		Session session = null;

		// 查询条件HQL的生成
		StringBuffer hql = new StringBuffer(
				"select count(*) from MRepFreq mdrt");
		StringBuffer where = new StringBuffer("");

		if (mRepFreqForm != null) {
			// 查找条件的判断,查找名称不可为空
			if (mRepFreqForm.getRepFreqName() != null
					&& !mRepFreqForm.getRepFreqName().equals(""))
				where.append((where.toString().equals("") ? "" : " and ")
						+ "mdrt.repFreqName like '%"
						+ mRepFreqForm.getRepFreqName() + "%'");
		}

		try { // List集合的操作
			// 初始化
			hql.append((where.toString().equals("") ? "" : " where ")
					+ where.toString());
			// conn对象的实例化
			conn = new DBConn();
			// 打开连接开始会话
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
			// 如果连接存在，则断开，结束会话，返回
			if (conn != null)
				conn.closeSession();
		}
		return count;
	}

	/**
	 * 上报频度的查询
	 * 
	 * @param mRepFreqForm
	 *            MRepFreqForm 查询表单对象
	 * @return List 如果查找到记录，返回List记录集；否则，返回null
	 */
	public static List select(MRepFreqForm mRepFreqForm, int offset, int limit) {

		// List集合的定义
		List refVals = null;

		// 连接对象和会话对象初始化
		DBConn conn = null;
		Session session = null;

		// 查询条件HQL的生成
		StringBuffer hql = new StringBuffer("from MRepFreq mrf");
		StringBuffer where = new StringBuffer("");

		if (mRepFreqForm != null) {
			// 查找条件的判断,查找名称不可为空
			if (mRepFreqForm.getRepFreqName() != null
					&& !mRepFreqForm.getRepFreqName().equals(""))
				where.append((where.toString().equals("") ? "" : " and ")
						+ "mrf.repFreqName like '%"
						+ mRepFreqForm.getRepFreqName() + "%'");
		}

		try { // List集合的操作
			// 初始化

			hql.append((where.toString().equals("") ? "" : " where ")
					+ where.toString());
			// conn对象的实例化
			conn = new DBConn();
			// 打开连接开始会话
			session = conn.openSession();
			// 添加集合至Session
			// List list=session.find(hql.toString());
			Query query = session.createQuery(hql.toString());
			query.setFirstResult(offset).setMaxResults(limit);
			List list = query.list();

			if (list != null) {
				refVals = new ArrayList();
				// 循环读取数据库符合条件记录
				for (Iterator it = list.iterator(); it.hasNext();) {
					MRepFreqForm mRepFreqFormTemp = new MRepFreqForm();
					MRepFreq mRepFreqPersistence = (MRepFreq) it.next();
					TranslatorUtil.copyPersistenceToVo(mRepFreqPersistence,
							mRepFreqFormTemp);
					refVals.add(mRepFreqFormTemp);
				}
			}
		} catch (HibernateException he) {
			refVals = null;
			log.printStackTrace(he);
		} catch (Exception e) {
			refVals = null;
			log.printStackTrace(e);
		} finally {
			// 如果连接存在，则断开，结束会话，返回
			if (conn != null)
				conn.closeSession();
		}
		return refVals;
	}

	/**
	 * 更新MRepFreqForm对象
	 * 
	 * @param mRepFreqForm
	 *            MRepFreqForm 存放数据的对象
	 * @exception Exception
	 *                如果MRepFreqForm更新失败,则捕捉抛出异常
	 */
	public static boolean update(MRepFreqForm mRepFreqForm) throws Exception {
		boolean result = false;
		DBConn conn = null;
		Session session = null;

		MRepFreq mRepFreqPersistence = new MRepFreq();

		if (mRepFreqForm == null) {
			return result;
		}
		try {
			if (mRepFreqForm.getRepFreqName() == null
					|| mRepFreqForm.getRepFreqName().equals("")) {
				return result;
			}
			conn = new DBConn();
			session = conn.beginTransaction();

			TranslatorUtil.copyVoToPersistence(mRepFreqPersistence,
					mRepFreqForm);
			session.update(mRepFreqPersistence);

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
	 * @param mRepFreqForm
	 *            The MRepFreqForm 保持数据的传递
	 * @exception Exception
	 *                如果 MRepFreqForm 对象丢失则抛出异常.
	 */
	public static boolean edit(MRepFreqForm mRepFreqForm) throws Exception {
		boolean result = false;
		DBConn conn = null;
		Session session = null;

		MRepFreq mRepFreqPersistence = new MRepFreq();
		mRepFreqForm = new MRepFreqForm();
		mRepFreqForm.getRepFreqName();

		if (mRepFreqForm == null) {
			return result;
		}

		try {
			if (mRepFreqForm.getRepFreqName() == null
					&& mRepFreqForm.getRepFreqName().equals("")) {
				return result;
			}
			conn = new DBConn();
			session = conn.beginTransaction();

			mRepFreqPersistence = (MRepFreq) session.load(MRepFreq.class,
					mRepFreqForm.getRepFreqName());

			TranslatorUtil.copyVoToPersistence(mRepFreqPersistence,
					mRepFreqForm);

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
	 * @param mRepFreqForm
	 *            MRepFreqForm 查询表单的对象
	 * @return boolean 如果删除成功则返回true,否则false
	 */
	public static boolean remove(MRepFreqForm mRepFreqForm) throws Exception {
		// 置标志result
		boolean result = false;
		// 连接和会话对象的初始化
		DBConn conn = null;
		Session session = null;

		// mRepFreq是否为空,返回result
		if (mRepFreqForm == null && mRepFreqForm.getRepFreqId() == null)
			return result;

		try {
			// 连接对象和会话对象初始化
			conn = new DBConn();
			session = conn.beginTransaction();
			// 将mRepFreqForm的货币单位的货币主键传入HQL中查询
			MRepFreq mRepFreq = (MRepFreq) session.load(MRepFreq.class,
					mRepFreqForm.getRepFreqId());
			TranslatorUtil.copyPersistenceToVo(mRepFreq, mRepFreqForm);
			// 会话对象删除持久层对象
			session.delete(mRepFreq);
			session.flush();

			// 删除成功，置为true
			result = true;
		} catch (HibernateException he) {
			// 捕捉本类的异常,抛出
			log.printStackTrace(he);
		} finally {
			// 如果由连接则断开连接，结束会话，返回
			if (conn != null)
				conn.endTransaction(result);
		}
		return result;
	}

	/**
	 * 查询一条记录,返回到EditAction中
	 * 
	 * @param mRepFreqForm
	 *            MRepFreqForm实例化对象
	 * @return MRepFreqForm 包含一条记录
	 */

	public static MRepFreqForm selectOne(MRepFreqForm mRepFreqForm) {

		// 连接对象和会话对象初始化
		DBConn conn = null;
		Session session = null;

		if (mRepFreqForm != null)
			try {
				if (mRepFreqForm.getRepFreqId() != null
						&& !mRepFreqForm.getRepFreqId().equals(""))
					// conn对象的实例化
					conn = new DBConn();
				// 打开连接开始会话
				session = conn.openSession();

				MRepFreq mRepFreqPersistence = (MRepFreq) session.load(
						MRepFreq.class, mRepFreqForm.getRepFreqId());

				TranslatorUtil.copyPersistenceToVo(mRepFreqPersistence,
						mRepFreqForm);

			} catch (HibernateException he) {
				log.printStackTrace(he);
			} catch (Exception e) {
				log.printStackTrace(e);
			} finally {
				// 如果连接存在，则断开，结束会话，返回
				if (conn != null)
					conn.closeSession();
			}
		return mRepFreqForm;
	}

	/**
	 * 查询一条记录,返回到EditAction中
	 * 
	 * @param mRepFreqForm
	 *            MRepFreqForm实例化对象
	 * @return MRepFreqForm 包含一条记录
	 */

	public static MRepFreq selectOne(Integer repFreqId) {

		MRepFreq mRepFreqPersistence = null;
		// 连接对象和会话对象初始化
		DBConn conn = null;
		Session session = null;

		if (repFreqId == null || repFreqId.equals(""))
			return mRepFreqPersistence;

		try {
			// conn对象的实例化
			conn = new DBConn();
			// 打开连接开始会话
			session = conn.openSession();

			mRepFreqPersistence = (MRepFreq) session.load(
					MRepFreq.class, repFreqId);

		} catch (HibernateException he) {
			log.printStackTrace(he);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			// 如果连接存在，则断开，结束会话，返回
			if (conn != null)
				conn.closeSession();
		}
		return mRepFreqPersistence;
	}

	/**
	 * 查询所有记录
	 * 
	 * @return List 查询到的记录条数
	 */
	public static List findAll() throws Exception {

		// List集合的定义
		List refVals = null;
		// 连接对象和会话对象初始化
		DBConn conn = null;
		Session session = null;
		// 查询条件HQL的生成
		String rep_freq = "('月','季','半年','年')";
		// StringBuffer hql = new StringBuffer("from MRepFreq a where
		// a.repFreqName in ").append(rep_freq);
		StringBuffer hql = new StringBuffer("from MRepFreq a ");

		try {

			// conn对象的实例化
			conn = new DBConn();
			// 打开连接开始会话
			session = conn.openSession();
			// 添加集合至Session
			List list = session.find(hql.toString());

			if (list != null) {
				refVals = new ArrayList();
				// 循环读取数据库符合条件记录
				for (Iterator it = list.iterator(); it.hasNext();) {
					MRepFreqForm mRepFreqFormTemp = new MRepFreqForm();
					MRepFreq mRepFreqPersistence = (MRepFreq) it.next();
					TranslatorUtil.copyPersistenceToVo(mRepFreqPersistence,
							mRepFreqFormTemp);
					refVals.add(mRepFreqFormTemp);
				}
			}
		} catch (HibernateException he) {
			refVals = null;
			log.printStackTrace(he);
		} catch (Exception e) {
			refVals = null;
			log.printStackTrace(e);
		} finally {
			// 如果连接存在，则断开，结束会话，返回
			if (conn != null)
				conn.closeSession();
		}
		return refVals;
	}

	/**
	 * 查询所有记录
	 * 
	 * @return List 查询到的记录条数
	 */
	public static List findAllFeq() throws Exception {

		// List集合的定义
		List refVals = null;
		// 连接对象和会话对象初始化
		DBConn conn = null;
		Session session = null;
		// 查询条件HQL的生成
		String rep_freq = "('日','旬','月','季','半年','年')";
		StringBuffer hql = new StringBuffer(
				"from MRepFreq a where a.repFreqName in ").append(rep_freq);

		try {

			// conn对象的实例化
			conn = new DBConn();
			// 打开连接开始会话
			session = conn.openSession();
			// 添加集合至Session
			List list = session.find(hql.toString());

			if (list != null) {
				refVals = new ArrayList();
				// 循环读取数据库符合条件记录
				for (Iterator it = list.iterator(); it.hasNext();) {
					MRepFreqForm mRepFreqFormTemp = new MRepFreqForm();
					MRepFreq mRepFreqPersistence = (MRepFreq) it.next();
					TranslatorUtil.copyPersistenceToVo(mRepFreqPersistence,
							mRepFreqFormTemp);
					refVals.add(mRepFreqFormTemp);
				}
			}
		} catch (HibernateException he) {
			refVals = null;
			log.printStackTrace(he);
		} catch (Exception e) {
			refVals = null;
			log.printStackTrace(e);
		} finally {
			// 如果连接存在，则断开，结束会话，返回
			if (conn != null)
				conn.closeSession();
		}
		return refVals;
	}
	
	/**
	 * 查询所有记录
	 * 
	 * @return List 查询到的记录条数
	 */
	public static List findRHAllFeq() throws Exception {

		// List集合的定义
		List refVals = null;
		// 连接对象和会话对象初始化
		DBConn conn = null;
		Session session = null;
		// 查询条件HQL的生成
		String rep_freq = "('日','旬','月','季','半年','年','年初结转','周(人行)','旬(人行)','快(人行)')";
		StringBuffer hql = new StringBuffer(
				"from MRepFreq a where a.repFreqName in ").append(rep_freq);

		try {
			// conn对象的实例化
			conn = new DBConn();
			// 打开连接开始会话
			session = conn.openSession();
			// 添加集合至Session
			List list = session.find(hql.toString());

			if (list != null) {
				refVals = new ArrayList();
				// 循环读取数据库符合条件记录
				for (Iterator it = list.iterator(); it.hasNext();) {
					MRepFreqForm mRepFreqFormTemp = new MRepFreqForm();
					MRepFreq mRepFreqPersistence = (MRepFreq) it.next();
					TranslatorUtil.copyPersistenceToVo(mRepFreqPersistence,
							mRepFreqFormTemp);
					refVals.add(mRepFreqFormTemp);
				}
			}
		} catch (HibernateException he) {
			refVals = null;
			log.printStackTrace(he);
		} catch (Exception e) {
			refVals = null;
			log.printStackTrace(e);
		} finally {
			// 如果连接存在，则断开，结束会话，返回
			if (conn != null)
				conn.closeSession();
		}
		return refVals;
	}

}
