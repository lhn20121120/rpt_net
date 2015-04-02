package com.fitech.framework.core.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.fitech.framework.core.web.PageResults;

/**
 * @author ahxu
 * 
 */
public interface IBaseDao<T, PK extends Serializable> {
	void create(Object newInstance) throws BaseDaoException;

	void delete(Class clazz, Serializable id);

	void delete(Object persistentObject) throws BaseDaoException;
	
	public void deleteObjects(String delSql) ;

	List<T> findAll() throws BaseDaoException;

	PageResults<T> findAll(int pageNo, int pageSize) throws BaseDaoException;

	Object findObject(Class clazz, String name, Object value);

	List findListByHsql(String hsql, Map paraMap) throws BaseDaoException;

	List findListBySql(String sql, Map paraMap) throws BaseDaoException;

	PageResults findPageByCriteria(final DetachedCriteria detachedCriteria,
			final int pageSize, final int pageNo) throws BaseDaoException;

	PageResults findPageByCriteria(final DetachedCriteria detachedCriteria,
			final int pageSize, final int pageNo, final Order orderBy)
			throws BaseDaoException;

	PageResults findPageByHsql(String hsql, Map paraMap, int pageSize,
			int pageNo) throws BaseDaoException;

	PageResults findPageBySql(String sql, Map paramMap, int pageSize, int pageNo)
			throws BaseDaoException;
	
	PageResults findPageBySql(Class cl,String sql, Map paramMap, int pageSize, int pageNo)
	throws BaseDaoException;
	public PageResults findPageByHsql(Class entityClass,Map paraMap,Map orderMap, int pageSize,int pageNo) throws BaseDaoException ;

	List findAllObject(Class clazz);

	T read(PK id) throws BaseDaoException;

	void saveOrUpdate(Object entity) throws BaseDaoException;
	
	void save(Object entity) throws BaseDaoException;

	void update(Object transientObject) throws BaseDaoException;

	int updateByHsql(String hsql, Map paraMap) throws BaseDaoException;

	int updateBysql(String sql) throws BaseDaoException;

	public List findAllByCriteria(final DetachedCriteria detachedCriteria);

	public int countByHql(String hql);

	public List findByHql(String hql, int firstResult, int maxResult);

	public int countBySql(String sql);

	public List findBySql(String sql, int firstResult, int maxResult);
	
	public Object findBysql(Class cl,String sql);
	
	public Object findObject(String hsql);
	
	public List findResult(String sql,Map paraMap, int begin, int maxresult);
	
	public HibernateTemplate getHibernateTemplate();
	
}