package com.fitech.framework.core.service.impl;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.fitech.framework.core.service.BaseJdbcService;
import com.fitech.framework.core.service.BaseServiceException;

public class BaseJdbcServiceImpl extends JdbcDaoSupport implements BaseJdbcService{
	
	public Log log = LogFactory.getLog(BaseJdbcServiceImpl.class);
	
	public static DecimalFormat DATA_FORMAT = new DecimalFormat("###,###.##");
	
	public String queryForJdbcStr(String sql) throws BaseServiceException {
		List list = this.getJdbcTemplate().queryForList(sql);
		String result = null;
		if(list!=null && list.size()>0){
			Map map = (Map) list.get(0);
			Set set = map.keySet();
			Iterator iter = set.iterator();
			if (iter.hasNext()) {
				Object o = (Object) iter.next();
				result = ((String) map.get(o)).trim();
			}
		}
		return result;
	}

	public List queryForJdbcList(String sql) throws BaseServiceException {

		return this.getJdbcTemplate().queryForList(sql);
	}
	public Map queryForJdbcMap(String sql) throws BaseServiceException {
		Map result = null;
		List list = this.getJdbcTemplate().queryForList(sql);
		if (list.size() != 0)
			result = (Map) list.get(0);
		return result;
	}

	public Integer queryForJdbcIntger(String sql) throws BaseServiceException {
		Integer result = null;
		List list = this.getJdbcTemplate().queryForList(sql);
		if (list.size() != 0) {
			Map map = (Map) list.get(0);
			Set set = map.keySet();
			Iterator iter = set.iterator();
			if (iter.hasNext()) {
				Object o = (Object) iter.next();
				result = ((BigDecimal) map.get(o)).intValue();
			}
		}
		return result;
	}
	public BigDecimal queryForJdbcDecemal(String sql) throws BaseServiceException {
		BigDecimal result = null;
		List list = this.getJdbcTemplate().queryForList(sql);
		if (list.size() != 0) {
			Map map = (Map) list.get(0);
			Set set = map.keySet();
			Object o = (Object) set.iterator().next();
			result = (BigDecimal) map.get(o);
		}
		return result;
	}

	public String queryForJdbcDecemalFormat(String sql) throws BaseServiceException {
		String result = null;
		List list = this.getJdbcTemplate().queryForList(sql);
		if (list.size() != 0) {
			Map map = (Map) list.get(0);
			Set set = map.keySet();
			Object o = (Object) set.iterator().next();
			BigDecimal bd = (BigDecimal) map.get(o);
			result = DATA_FORMAT.format(bd);
		}
		return result;
	}
	public String execPro()throws BaseServiceException{
		String sql = "{call pro_load_test(?,?)}";   
	    Object obj = getJdbcTemplate().execute(sql,new CallableStatementCallback(){   
	        public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {   
	            cs.setString(1,"kkk");   
	            cs.registerOutParameter(2, Types.VARCHAR);   
	            cs.execute();   
	            return cs.getString(2);   
	        }      
	    });   
	    return (String)obj;    
	}
	
	@Override
	public JdbcTemplate createJdbcTemplate() {
		return this.getJdbcTemplate();
	}
}
