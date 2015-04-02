package com.fitech.framework.core.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fitech.framework.core.web.PageResults;

/**
 * 
 * Spring中事务的属性
1.传播行为
spring定义了7种不同的传播行为。
1)PROPAGATION_MANDATORY
  表明方法必须要在事务中运行。如果事务不存在，抛出异常
2)PROPAGATION_NESTED
  表示如果当前已经存在一个事务，则该方法应当运行在一个嵌套的事务中。被嵌套的事务可以从当前事务中单独地提交或回滚。如果当前事务不存在，就相当于PROPAGATION_REQUIRED
3)PROPAGATION_NEVER
表示当前的方法不应该运行在一个事务上下文中。如果当前存在一个事务，则会抛出一个异常。
4)PROPAGATION_NOT_SUPPORTED
表示该方法不应该在事务中运行。如果一个现有的事务正在运行中。它将在该方法的运行期间被挂起
5)PROPAGATION_REQUIRED
表示当前方法必须运行在一个事务中。如果一个现有的事务正在运行中，该方法将运行在这个事务中。否则的话，就要开始一个新的事务。
6)PROPAGATION_REQUIRES_NEW
表示当前方法必须运行在它自己的事务中。如果一个现有事务在运行的话，将在这个方法运行期间被挂起。
7)PROPAGATION_SUPPORTS
表示当前方法不需要事务处理环境，但如果有一个事务已经在运行的话，这个方法也可以在这个事务中运行
2.隔离级别
多个事务并发运行的关系。因为并发会导致以下问题
.脏读(Dirty read) 一个事务读取了被另一个事务改写但还没提交的数据。
.不可重复读(Nonrepeatable read) 一个事务执行相同的查询两次或两次以上，但每次查询结果都不同时。这由于另一个并发事务在两次查询之间更新了数据
.幻读(Phantom read)
当一个事务读取几行记录后，另一个并发事务插入一些记录，就发生了幻读
spring定义了以下5种隔离关系
1)ISOLATION_DEFAULT
使用后端数据库默认得隔离级别
2)ISOLATION_READ_UNCOMMITTED
允许你读取还未提交的改变了的数据。可能导致脏读，不可重复读，幻读
3)ISOLATION_READ_COMMITTTED
运行在并发事务已经提价后读取。可防止脏读，但不可重复读，幻读都有可能发生
4)ISOLATION_REPEATABLE_READ
对相同字段的多次读取结果是一致的，除非数据被事务本身。可防止脏读，不可重复读，但幻读仍可能发生。
5)ISOLATION_SERIALIZABLE
完全服从ACID的隔离级别，确保不发生脏读，不可重复读，幻读。它的典型做法就是完全锁定在事务中涉及的数据表。因此它是最慢地。
3.只读提示
通过只读属性，数据库可以对事务进行优化。
4.事务超时时间
设置了的话，事务在超过这个时间后就自动回滚。避免占用资源过久

@Transactional 注解的属性

属性 类型 描述 
propagation  枚举型：Propagation  可选的传播性设置 
isolation 枚举型：Isolation  可选的隔离性级别（默认值：ISOLATION_DEFAULT） 
readOnly 布尔型 读写型事务 vs. 只读型事务 
timeout int型（以秒为单位） 事务超时 
rollbackFor 一组 Class 类的实例，必须是Throwable 的子类 一组异常类，遇到时 必须 进行回滚。默认情况下checked exceptions不进行回滚，仅unchecked exceptions（即RuntimeException的子类）才进行事务回滚。 
rollbackForClassname 一组 Class 类的名字，必须是Throwable的子类 一组异常类名，遇到时 必须 进行回滚 
noRollbackFor 一组 Class 类的实例，必须是Throwable 的子类 一组异常类，遇到时 必须不 回滚。 
noRollbackForClassname 一组 Class 类的名字，必须是Throwable 的子类 一组异常类，遇到时 必须不 回滚 

 */
@Transactional
public interface IBaseService<T, PK extends Serializable> {

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    void create(List<T> newInstance) throws BaseServiceException;

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    void create(T newInstance) throws BaseServiceException;

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    void delete(List<T> persistentObject) throws BaseServiceException;

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    void delete(T persistentObject) throws BaseServiceException;

    List<T> findAll() throws BaseServiceException;

    PageResults<T> findAll(int pageNo, int pageSize) throws BaseServiceException;

    List findListByHsql(String hsql, Map paraMap) throws BaseServiceException;

    List findListBySql(String sql, Map paraMap) throws BaseServiceException;
    
    public Object findObject(String hsql)throws BaseServiceException;

    PageResults<T> findPageByCriteria(final DetachedCriteria d, final int pageSize, final int pageNo)
            throws BaseServiceException;

    PageResults<T> findPageByCriteria(final DetachedCriteria d, final int pageSize, final int pageNo,
            final Order orderBy) throws BaseServiceException;

    PageResults findPageByHsql(String hsql, Map paraMap, int pageSize, int pageNo) throws BaseServiceException;

    PageResults findPageBySql(String sql, Map paraMap, int pageSize, int pageNo) throws BaseServiceException;
    
    PageResults findPageByHsql(Class entityClass,Map paramMap,Map orderMap, int pageSize,int pageNo) throws BaseServiceException;

    T read(PK id, String... properties) throws BaseServiceException;

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    void saveOrUpdate(List<T> entity) throws BaseServiceException;

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    void saveOrUpdate(T entity) throws BaseServiceException;
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public void save(T entity) throws BaseServiceException;

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    void update(List<T> transientObject) throws BaseServiceException;

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    void update(T transientObject) throws BaseServiceException;

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    int updateByHsql(String hsql, Map paraMap) throws BaseServiceException;

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    int updateBysql(String sql) throws BaseServiceException;
   
}
