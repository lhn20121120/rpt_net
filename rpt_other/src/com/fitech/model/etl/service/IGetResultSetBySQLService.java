package com.fitech.model.etl.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.fitech.framework.core.service.IBaseService;

public interface IGetResultSetBySQLService extends IBaseService{
	/***
	 * 根据SQL查询得出集合
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public List getListBySQL(String sql) throws Exception;
	
	/***
	 * 执行存储过程
	 * @param proc
	 * @return
	 * @throws Exception
	 */
	public List getListByProc(String proc) throws Exception;
	
	/***
	 * 执行update,delete,insert 操作
	 * 
	 */
	
	public boolean execSQL(String sql, Connection conn) throws Exception;
	public boolean execSQL(String sql, String[] params,Connection conn) throws Exception ;
	public List<String> getInsertSQL(String tableName) throws Exception;

	public Connection getConnection() throws SQLException;

}
