package com.fitech.framework.core.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.impl.CriteriaImpl;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.fitech.framework.core.dao.BaseDaoException;
import com.fitech.framework.core.dao.IBaseDao;
import com.fitech.framework.core.web.PageResults;

public class DefaultBaseDao<T, PK extends Serializable> extends
		HibernateDaoSupport implements IBaseDao<T, PK> {
	protected Class<T> type;

	@SuppressWarnings("unchecked")
	public DefaultBaseDao() {
		type = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
	}

	/**
	 * 跟据对象新增数据
	 * 
	 * @param newInstance
	 *            新增对象
	 * @throws BaseDaoException
	 *             DAO的专用Exception
	 */
	public void create(Object newInstance) throws BaseDaoException {
		this.getHibernateTemplate().save(newInstance);
	}

	/**
	 * 跟据ID删除表中数据
	 * 
	 * @param id
	 *            预删除数据的ID字段
	 * @return 是否存在数据并删除成功
	 * @throws BaseDaoException
	 *             DAO的专用Exception
	 */
	public boolean delete(final PK id) throws BaseDaoException {

		T entity = this.read(id);
		if (entity == null) {
			return false;
		}
		this.delete(entity);
		return true;
	}

	public void delete(Class clazz, Serializable id) {

		getHibernateTemplate().delete(getObject(clazz, id));
	}
	public void deleteObjects(String delSql) {
		String _hsql = delSql.replaceFirst("delete", "").trim();
		List list = this.getHibernateTemplate().find(_hsql);
		this.getHibernateTemplate().deleteAll(list);
	} 
	
	public Object getObject(Class clazz, Serializable id) {
		return getHibernateTemplate().get(clazz, id);
	}

	/**
     * 
     */
	public void delete(Object persistentObject) throws BaseDaoException {
		this.getHibernateTemplate().delete(persistentObject);
	}

	/**
	 * 查询所有表项
	 * 
	 * @return 以EUser为元素类型的List对象
	 * @throws BaseDaoException
	 *             DAO的专用Exception
	 */
	@SuppressWarnings("unchecked")
	public List<T> findAll() throws BaseDaoException {
		return this.findAll(null);
	}

	public PageResults<T> findAll(int pageNo, int pageSize)
			throws BaseDaoException {
		return findAll(null, pageNo, pageSize);
	}

	public List<T> findAll(List<PK> ids) throws BaseDaoException {
		return findAll(ids, 1, Integer.MAX_VALUE).getResults();
	}

	public PageResults<T> findAll(List<PK> ids, int pageNo, int pageSize)
			throws BaseDaoException {
		DetachedCriteria dCriteria = DetachedCriteria.forClass(type);
		if (insertIdsInRangToDetachedCriteria(dCriteria, "id", ids)) {
			PageResults<T> ret = new PageResults<T>();
			ret.setPageSize(pageSize);
			ret.setCurrentPage(pageNo);
			return ret;
		}
		return this.findPageByCriteria(dCriteria, pageSize, pageNo);
	}

	/**
	 * 查询，支持单表及多表
	 * 注意：setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY)
	 * 并没有包含在此方法内
	 * 
	 * @param detachedCriteria
	 * @return
	 */
	public List findAllByCriteria(final DetachedCriteria detachedCriteria) {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException {
				Criteria criteria = detachedCriteria
						.getExecutableCriteria(session);
				criteria
						.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
				return criteria.list();
			}
		});
	}

	public List<PK> findAllIds() throws BaseDaoException {
		DetachedCriteria dCriteria = DetachedCriteria.forClass(type);
		dCriteria.setProjection(Projections.id());
		return this.findAllByCriteria(dCriteria);
	}

	/**
	 * 查询单表行数，仅对单表支持(即detachedCriteria不能有projection)
	 * 
	 * 
	 * @param detachedCriteria
	 * @return
	 */
	protected int findCountByCriteria(final DetachedCriteria detachedCriteria) {
		Integer count = (Integer) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						Criteria criteria = detachedCriteria
								.getExecutableCriteria(session);
						return criteria.setProjection(Projections.rowCount())
								.uniqueResult();
					}
				});
		return count.intValue();
	}

	/**
	 * 查询单表行数，仅对单表支持(detachedCriteria必须有返回数量的Projection)
	 * 如：dCriteria.setProjection(Projections.countDistinct("id.userId"));
	 * 
	 * @param detachedCriteria
	 * @return
	 */
	protected int findCountByCriteriaUsingCountDistinct(
			final DetachedCriteria detachedCriteria) {
		Integer count = (Integer) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						Criteria criteria = detachedCriteria
								.getExecutableCriteria(session);
						return criteria.uniqueResult();
					}
				});
		return count.intValue();
	}

	public List findListByHsql(String hsql, Map paraMap) {
		List restList = null;
		if (hsql != null) {
			Session session = getSession();
			org.hibernate.Query query = session.createQuery(hsql);
			setQueryParameters(query, paraMap);

			restList = query.list();
			releaseSession(session);
		}
		return restList;
	}
	
	public List findListByCache(String hsql, Map paraMap) {
		List restList = null;
		if (hsql != null) {
			Session session = getSession();
			org.hibernate.Query query = session.createQuery(hsql);
			setQueryParameters(query, paraMap);
			query.setCacheable(true);
			restList = query.list();
			releaseSession(session);
		}
		return restList;
	}
	
	public Object findObject(String hsql){
		List list = null ;
		Object result = null;
		list = this.getHibernateTemplate().find(hsql);
		if(list!=null && list.size()!=0)
			result = list.get(0);
		return result;
	}
	
	public List findListBySql(String sql, Map paraMap) {
		List restList = null;
		System.out.println(sql);
		if (sql != null) {
			Session session = getSession();
			org.hibernate.Query query = session.createSQLQuery(sql);
			setQueryParameters(query, paraMap);

			restList = query.list();
			releaseSession(session);
		}
		return restList;
	}

	/**
	 * 非排序应用的单表的分页查询，仅对单表支持(即detachedCriteria的projection必须都是其type类的属性)，
	 * 对于关联对象的复合查询存在问题; 如果需要分页排序请使用带OrderBy的另一个方法，强行使用此方法会出现意想不到的问题
	 * 
	 * @param detachedCriteria
	 * @param pageSize
	 * @param pageNo
	 * @return
	 */
	public PageResults findPageByCriteria(
			final DetachedCriteria detachedCriteria, final int pageSize,
			final int pageNo) {
		return findPageByCriteria(detachedCriteria, -1, pageSize, pageNo, null);
	}

	/**
	 * 指定总数的分页查询
	 * 
	 * @param detachedCriteria
	 * @param total
	 *            当total>=0时，detachedCriteria可以允许包含关联表的Projection；否则不允许，如强行使用会出现意想不到的问题
	 * @param pageSize
	 * @param pageNo
	 * @return
	 */
	protected PageResults findPageByCriteria(
			final DetachedCriteria detachedCriteria, final int total,
			final int pageSize, final int pageNo) {
		return findPageByCriteria(detachedCriteria, total, pageSize, pageNo,
				null);
	}

	/**
	 * 分页查询，当total>=0时，detachedCriteria可以允许包含关联表的Projection；否则不允许，
	 * 如强行使用会出现意想不到的问题
	 * 
	 * @param detachedCriteria
	 * @param total
	 * @param pageSize
	 * @param pageNo
	 * @param orderBy
	 * @return
	 */
	protected PageResults findPageByCriteria(
			final DetachedCriteria detachedCriteria, final int total,
			final int pageSize, final int pageNo, final Order orderBy) {
		if (pageNo <= 0 || pageSize <= 0)
			throw new IllegalArgumentException("pageNo or pageSize should >0");

		return (PageResults) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						int totalCount = 0;
						CriteriaImpl criteria = (CriteriaImpl) detachedCriteria
								.getExecutableCriteria(session);
						Projection projection = criteria.getProjection();

						if (total < 0) {
							// 得到符合条件的所查的单表总数,Projections.rowCount()对多表关联的时候可能返回重复的结果，会造成数量不准确
							// 如A,B是多对多关系，有一关系表C，当我使用关联表查询select C.A_id from
							// C,A,B where...的时候，显然A.id会出现重复数据
							Integer totalCountObject = (Integer) criteria
									.setProjection(Projections.rowCount())
									.uniqueResult();
							totalCount = (totalCountObject == null) ? 0
									: totalCountObject.intValue();
						} else {
							totalCount = total;
						}
						// 查询结果集比当前页小时，页面设置为第一页

						PageResults retValue = new PageResults();

						retValue.setCurrentPage(pageNo);

						retValue.setPageSize(pageSize);
						retValue.setTotalCount(totalCount);
						retValue.resetPageNo();
						List items = new ArrayList();
						if (totalCount == 0) {
							retValue.setResults(items);
						} else {

							if (orderBy != null) {
								// 解决排序问题
								List ids = new ArrayList();
								String orderStr = orderBy.toString().split(" ")[0];
								ids = criteria
										.setProjection(
												Projections
														.projectionList()
														.add(
																Projections
																		.distinct(Projections
																				.id()))
														.add(
																Projections
																		.property(orderStr)))
										.addOrder(orderBy).setFirstResult(
												retValue.getResultsFrom())
										.setMaxResults(retValue.getPageSize())
										.list();
								List idss = new ArrayList();
								for (int i = 0; i < ids.size(); i++) {
									Object[] o = (Object[]) ids.get(i);
									idss.add(o[0]);
								}
								ids = idss;

								criteria.setProjection(projection);
								criteria
										.setResultTransformer(
												CriteriaSpecification.DISTINCT_ROOT_ENTITY)
										.add(Restrictions.in("id", ids));
								items = criteria.setFirstResult(0)
										.setMaxResults(Integer.MAX_VALUE)
										.list();
							} else {
								// 不排序的实现
								criteria.setProjection(projection);
								criteria
										.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
								items = criteria.setFirstResult(
										retValue.getResultsFrom())
										.setMaxResults(retValue.getPageSize())
										.list();
							}

							retValue.setResults(items);

						}

						return retValue;
					}
				});
	}

	/**
	 * 支持排序的单表分页查询，仅对单表支持(即detachedCriteria的projection必须都是其type类的属性)，
	 * 对于关联对象的复合查询存在问题，
	 * 
	 * 
	 * @param detachedCriteria
	 * @param pageSize
	 * @param pageNo
	 * @param orderBy
	 * @return
	 */
	public PageResults findPageByCriteria(
			final DetachedCriteria detachedCriteria, int pageSize, int pageNo,
			final Order orderBy) {
		return findPageByCriteria(detachedCriteria, -1, pageSize, pageNo,
				orderBy);
	}

	public PageResults findPageByHsql(String hsql, Map paraMap, int pageSize,
			int pageNo) throws BaseDaoException {
		PageResults retValue = new PageResults();
		if (hsql != null) {
			Session session = getSession();
			org.hibernate.Query query = session.createQuery(hsql);
			setQueryParameters(query, paraMap);

			retValue.setCurrentPage(pageNo);

			retValue.setPageSize(pageSize);

			ScrollableResults scrollableResults = query.scroll();
			scrollableResults.last();
			if (scrollableResults.getRowNumber() >= 0) {
				retValue.setTotalCount(scrollableResults.getRowNumber() + 1);
			} else {
				retValue.setTotalCount(0);
			}
			retValue.resetPageNo();
			if (retValue.getTotalCount() == 0) {
				retValue.setResults(new ArrayList());
			} else {
				retValue.setResults(query.setFirstResult(
						retValue.getResultsFrom()).setMaxResults(pageSize)
						.list());
			}
			releaseSession(session);
		}
		return retValue;
	}
	
	public List findResult(String sql,Map paraMap, int begin, int maxresult){
		List retValue = new ArrayList();
		if (sql != null) {
			Session session = getSession();
			org.hibernate.Query query = session.createSQLQuery(sql);
			setQueryParameters(query, paraMap);


			retValue = query.setFirstResult(begin).setMaxResults(maxresult).list();
			releaseSession(session);
		}
		return retValue;
	}
	public PageResults findPageByHsql(Class entityClass,Map paramMap,Map orderMap, int pageSize,int pageNo) throws BaseDaoException {
		PageResults result = null;
		StringBuffer hsql = new StringBuffer("from " + entityClass.getSimpleName() + " where 1=1");
		Iterator iter = paramMap.keySet().iterator();
		while(iter.hasNext()){
			Object key = iter.next();
			Object value = paramMap.get(key);
			if(!key.toString().endsWith("="))
				if(value.toString().indexOf("%")==-1)
					hsql.append(" and " + key + "=" + value.toString());
				else
					hsql.append(" and " + key + " like " + value.toString() + "");
			else
				hsql.append(" and " + value);
		}
		hsql.append(" ");
		iter = orderMap.keySet().iterator();
		StringBuffer orderStr = new StringBuffer("");
		while(iter.hasNext()){
			Object key = iter.next();
			String value = (String)orderMap.get(key);
			orderStr.append("," + key  + " " + value);
		}
		if(!orderStr.toString().equals(""))
			hsql.append(" order by " + orderStr.substring(1));
			
		return this.findPageByHsql(hsql.toString(), null, pageSize, pageNo);
	}
	public PageResults findPageBySql(String sql, Map paraMap, int pageSize,
			int pageNo) throws BaseDaoException {
		PageResults retValue = new PageResults();
		if (sql != null) {
			Session session = getSession();
			org.hibernate.Query query = session.createSQLQuery(sql);
			setQueryParameters(query, paraMap);
			
			retValue.setCurrentPage(pageNo);
			retValue.setPageSize(pageSize);
			ScrollableResults scrollableResults = query.scroll();
			scrollableResults.last();
			if (scrollableResults.getRowNumber() >= 0) {
				retValue.setTotalCount(scrollableResults.getRowNumber() + 1);
			} else {
				retValue.setTotalCount(0);
			}
			retValue.resetPageNo();
			if (retValue.getTotalCount() == 0) {
				retValue.setResults(new ArrayList());
			} else {
				retValue.setResults(query.setFirstResult(
						retValue.getResultsFrom()).setMaxResults(pageSize)
						.list());
			}
			releaseSession(session);
		}
		return retValue;
	}

	
	public PageResults findPageBySql(Class cl,String sql, Map paraMap, int pageSize,
			int pageNo) throws BaseDaoException {
		PageResults retValue = new PageResults();
		if (sql != null) {
			Session session = getSession();
			SQLQuery query = session.createSQLQuery(sql);
			setQueryParameters(query, paraMap);
			query.addEntity(cl);
			retValue.setCurrentPage(pageNo);
			retValue.setPageSize(pageSize);
			ScrollableResults scrollableResults = query.scroll();
			scrollableResults.last();
			if (scrollableResults.getRowNumber() >= 0) {
				retValue.setTotalCount(scrollableResults.getRowNumber() + 1);
			} else {
				retValue.setTotalCount(0);
			}
			retValue.resetPageNo();
			if (retValue.getTotalCount() == 0) {
				retValue.setResults(new ArrayList());
			} else {
				retValue.setResults(query.setFirstResult(
						retValue.getResultsFrom()).setMaxResults(pageSize)
						.list());
			}
			releaseSession(session);
		}
		return retValue;
	}
	
	/**
	 * 加入Restrictions.in(inProperty, ids)
	 * 
	 * @param <ID>
	 * @param dCriteria
	 * @param inProperty
	 * @param ids
	 * @return true表示ids不空且为空，需要返回空的查询结果；否则为false
	 */
	protected <ID> boolean insertIdsInRangToDetachedCriteria(
			DetachedCriteria dCriteria, String inProperty, List<ID> ids) {
		if (ids != null) {
			if (ids.size() == 0) {
				return true;
			} else {
				dCriteria.add(Restrictions.in(inProperty, ids));
			}
		}
		return false;

	}

	public T read(PK id) throws BaseDaoException {
		return (T) this.getHibernateTemplate().get(type, id);
	}

	public Object findObject(Class clazz, String name, Object value) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(clazz);
		criteria.add(Restrictions.eq(name, value));
		criteria.setMaxResults(1);
		Object o =  criteria.uniqueResult();
		releaseSession(session);
		return o;
	}

	public List findAllObject(Class clazz) {
		return getHibernateTemplate().loadAll(clazz);
	}

	/**
	 * 持久化PO对象
	 * 
	 * @param entity
	 *            PO对象
	 * @throws BaseDaoException
	 *             DAO的专用Exception
	 */
	public void saveOrUpdate(Object entity) throws BaseDaoException {
		this.getHibernateTemplate().saveOrUpdate(entity);
	}
	public void save(Object entity) throws BaseDaoException {
		this.getHibernateTemplate().save(entity);
	}
	protected void setQueryParameters(org.hibernate.Query query, Map paraMap) {
		if ((query != null) && (paraMap != null) && (!(paraMap.isEmpty()))) {
			List namedParms = Arrays.asList(query.getNamedParameters());
			Iterator iter = paraMap.keySet().iterator();
			while (iter.hasNext()) {
				String paraName = (String) iter.next();
				if (namedParms.contains(paraName)) {
					Object value = paraMap.get(paraName);
					if (value instanceof List)
						query.setParameterList(paraName, (List) value);
					else
						query.setParameter(paraName, value);
				}
			}
		}
	}

	public void update(Object transientObject) throws BaseDaoException {
		this.getHibernateTemplate().update(transientObject);

	}

	public int updateByHsql(String hsql, Map paraMap) throws BaseDaoException {
		if (hsql != null) {
			Session session = getSession();
			Query query = session.createQuery(hsql);
			setQueryParameters(query, paraMap);
			int i = query.executeUpdate();
			releaseSession(session);
			return i;
		}
		return 0;
	}

	public int updateBysql(final String sql) throws BaseDaoException {
		return (Integer) this.getHibernateTemplate().execute(
				new HibernateCallback() {

					public Integer doInHibernate(Session sess)
							throws HibernateException, SQLException {
						int i = sess.createSQLQuery(sql).executeUpdate();
						return i;

					}

				});
	}

	public int countByHql(String hql) {
		try {
			Session session = getSession();
			session.createQuery(hql).list();
			releaseSession(session);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public List findByHql(String hql, int firstResult, int maxResult) {
		Session session = getSession();
		Query query = session.createQuery(hql);

		query.setFirstResult(firstResult);
		query.setMaxResults(maxResult);
		List lst = query.list();
		releaseSession(session);
		return lst;
	}

	public int countBySql(String sql) {
		Session session = getSession();
		org.hibernate.Query query = session.createSQLQuery(sql);
		int i =((Integer) query.list().get(0)).intValue();
		releaseSession(session);
		return i;

	}

	public List findBySql(String sql, int firstResult, int maxResult) {
		Session session = getSession();
		org.hibernate.Query query = session.createSQLQuery(sql);
		query.setFirstResult(firstResult);
		query.setMaxResults(maxResult);
		List lst = query.list();
		releaseSession(session);
		return lst;
	}

	@Override
	public Object findBysql(Class cl, String sql) {
		Session session = getSession();
		SQLQuery query = session.createSQLQuery(sql);
		
		query.addEntity(cl);
		List list = query.list();
		
		releaseSession(session);
		if(list!=null && list.size()>0)
			return list.get(0);
		return null;
	}
	
}
