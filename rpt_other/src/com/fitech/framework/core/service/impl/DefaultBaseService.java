package com.fitech.framework.core.service.impl;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fitech.framework.core.dao.BaseDaoException;
import com.fitech.framework.core.dao.IBaseDao;
import com.fitech.framework.core.service.BaseServiceException;
import com.fitech.framework.core.service.IBaseService;
import com.fitech.framework.core.web.PageResults;
public class DefaultBaseService<T, PK extends Serializable> implements
		IBaseService<T, PK> {

	protected static Log log = null;

	public DefaultBaseService(Class clazz) {
		log = LogFactory.getLog(clazz);
	}

	public static void loadLazyProperties(Object o, String... properties) {
		if (properties != null && properties.length > 0) {
			for (String pro : properties) {
				try {
					Hibernate.initialize(PropertyUtils.getProperty(o, pro));
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void loadLazyPropertiesForCollection(Collection<?> list,
			String... properties) {
		if (list != null && list.size() > 0) {
			for (Object o : list) {
				loadLazyProperties(o, properties);
			}
		}
	}

	protected IBaseDao<T, PK> objectDao;

	public DefaultBaseService() {
		super();
	}

	public DefaultBaseService(IBaseDao<T, PK> dao) {
		this.objectDao = dao;
	}

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public void create(List<T> newInstance) throws BaseServiceException {
		try {
			for (int i = 0; newInstance != null && i < newInstance.size(); i++) {
				this.objectDao.create(newInstance.get(i));

			}
		} catch (BaseDaoException e) {
			throw new BaseServiceException(e);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public void create(T newInstance) throws BaseServiceException {
		try {
			this.objectDao.create(newInstance);
		} catch (BaseDaoException e) {
			throw new BaseServiceException(e);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public void delete(List<T> persistentObject) throws BaseServiceException {
		try {
			for (int i = 0; persistentObject != null
					&& i < persistentObject.size(); i++) {
				this.objectDao.delete(persistentObject.get(i));
			}

		} catch (BaseDaoException e) {
			throw new BaseServiceException(e);
		}

	}

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public void delete(T persistentObject) throws BaseServiceException {
		try {
			this.objectDao.delete(persistentObject);
		} catch (BaseDaoException e) {
			throw new BaseServiceException(e);
		}

	}

	public List<T> findAll() throws BaseServiceException {
		try {
			return this.objectDao.findAll();
		} catch (BaseDaoException e) {
			throw new BaseServiceException(e);
		}
	}

	public List findAll(DetachedCriteria detachedCriteria) {
		return this.objectDao.findAllByCriteria(detachedCriteria);
	}

	public PageResults<T> findAll(int pageNo, int pageSize)
			throws BaseServiceException {
		try {
			return this.objectDao.findAll(pageNo, pageSize);
		} catch (BaseDaoException e) {
			throw new BaseServiceException(e);
		}
	}

	public List findListByHsql(String hsql, Map paraMap)
			throws BaseServiceException {
		try {
			return this.objectDao.findListByHsql(hsql, paraMap);
		} catch (BaseDaoException e) {
			throw new BaseServiceException(e);
		}
	}

	public Object findObject(String hsql) throws BaseServiceException {
		return this.objectDao.findObject(hsql);
	}

	public List findListBySql(String sql, Map paraMap)
			throws BaseServiceException {
		try {
			return this.objectDao.findListBySql(sql, paraMap);
		} catch (BaseDaoException e) {
			throw new BaseServiceException(e);
		}
	}

	public PageResults<T> findPageByCriteria(DetachedCriteria detachedCriteria,
			int pageSize, int pageNo) throws BaseServiceException {
		try {
			return this.objectDao.findPageByCriteria(detachedCriteria,
					pageSize, pageNo);
		} catch (BaseDaoException e) {
			throw new BaseServiceException(e);
		}
	}

	public PageResults<T> findPageByCriteria(DetachedCriteria detachedCriteria,
			int pageSize, int pageNo, Order orderBy)
			throws BaseServiceException {
		try {
			return this.objectDao.findPageByCriteria(detachedCriteria,
					pageSize, pageNo, orderBy);
		} catch (BaseDaoException e) {
			throw new BaseServiceException(e);
		}
	}

	public PageResults findPageByHsql(String hsql, Map paraMap, int pageSize,
			int pageNo) throws BaseServiceException {
		try {
			return this.objectDao.findPageByHsql(hsql, paraMap, pageSize,
					pageNo);
		} catch (BaseDaoException e) {
			throw new BaseServiceException(e);
		}
	}

	public PageResults findPageBySql(String sql, Map paraMap, int pageSize,
			int pageNo) throws BaseServiceException {
		try {
			return this.objectDao.findPageBySql(sql, paraMap, pageSize, pageNo);
		} catch (BaseDaoException e) {
			throw new BaseServiceException(e);
		}
	}
	
	public List findResult(String sql, Map paraMap, int begin,
			int maxResult){
			return this.objectDao.findResult(sql, paraMap, begin, maxResult);
	}
	public PageResults findPageByHsql(Class entityClass,Map paramMap,Map orderMap, int pageSize,int pageNo) throws BaseServiceException{
		try {
			return this.objectDao.findPageByHsql(entityClass, paramMap, orderMap,pageSize,pageNo);
		} catch (BaseDaoException e) {
			throw new BaseServiceException(e);
		}
	}
	public IBaseDao<T, PK> getObjectDao() {
		return objectDao;
	}

	public T read(PK id, String... properties) throws BaseServiceException {
		try {
			T ret = this.objectDao.read(id);
			loadLazyProperties(ret, properties);
			return ret;
		} catch (BaseDaoException e) {
			throw new BaseServiceException(e);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public void saveOrUpdate(List<T> entity) throws BaseServiceException {
		try {
			for (int i = 0; entity != null && i < entity.size(); i++) {
				this.objectDao.saveOrUpdate(entity.get(i));
			}
		} catch (BaseDaoException e) {
			throw new BaseServiceException(e);
		}

	}

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public void saveOrUpdate(T entity) throws BaseServiceException {
		try {
			this.objectDao.saveOrUpdate(entity);
		} catch (BaseDaoException e) {
			throw new BaseServiceException(e);
		}

	}

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public void save(T entity) throws BaseServiceException {
		try {
			this.objectDao.save(entity);
		} catch (BaseDaoException e) {
			throw new BaseServiceException(e);
		}

	}
	
	public void setObjectDao(IBaseDao<T, PK> objectDao) {
		this.objectDao = objectDao;
	}

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public void update(List<T> transientObject) throws BaseServiceException {
		try {

			for (int i = 0; transientObject != null
					&& i < transientObject.size(); i++) {
				this.objectDao.update(transientObject.get(i));
			}

		} catch (BaseDaoException e) {
			throw new BaseServiceException(e);
		}

	}

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public void update(T transientObject) throws BaseServiceException {
		try {
			this.objectDao.update(transientObject);
		} catch (BaseDaoException e) {
			throw new BaseServiceException(e);
		}

	}

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public int updateByHsql(String hsql, Map paraMap)
			throws BaseServiceException {
		try {
			return this.objectDao.updateByHsql(hsql, paraMap);
		} catch (BaseDaoException e) {
			throw new BaseServiceException(e);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public int updateBysql(String sql) throws BaseServiceException {
		try {
			return this.objectDao.updateBysql(sql);
		} catch (BaseDaoException e) {
			throw new BaseServiceException(e);
		}
	}

}
