package com.fitech.model.etl.service.impl;

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

import com.fitech.framework.core.service.BaseServiceException;
import com.fitech.model.etl.common.ETLUtil;
import com.fitech.model.etl.model.vo.ETLDBInfo;
import com.fitech.model.etl.service.IETLJdbcService;
import com.fitech.model.etl.service.IETLProParamService;

public class ETLJdbcServiceImpl extends JdbcDaoSupport implements IETLJdbcService{
	
	public Log log = LogFactory.getLog(ETLJdbcServiceImpl.class);
	
	private IETLProParamService etlProParamService;
	
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
	/**
	 *  String pro_load(String orgId,Date etlDate)
	  * @Title: executeProcedure 
	  * @author: xfc100
	  * @date: Feb 29, 2012  4:42:10 PM
	  * @param procedureExpress
	  * @return String
	 */
	public String executeProcedure(final String procedureExpress,final Map paramMap) throws BaseServiceException{
		String sql = (String)ETLUtil.parseProBody(procedureExpress)[0]; 
		//使用   Object execute(String callString, CallableStatementCallback action)接口   
		Object obj = getJdbcTemplate().execute(sql,new CallableStatementCallback(){   
		    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {   
		    	List<String[]> paramList = (List<String[]>)ETLUtil.parseProBody(procedureExpress)[1];
	    		for(int i=0;i<paramList.size()-1;i++){
	    			String[] params = paramList.get(i);
	    			String type = params[0].toLowerCase();
	    			Object value = paramMap.get(params[1].toLowerCase());//取出参数值
	    			if(type.equals("date"))
	    				cs.setDate(i+1, (java.sql.Date)value);
	    			 else
	    				if(type.equals("string"))
	    					cs.setString(i+1, (String)value);
	    				else
	    					cs.setInt(i+1, Integer.parseInt((String)value));
	    		}
		        cs.registerOutParameter(paramList.size(), Types.VARCHAR); 
		        cs.execute();
		        String returnValue = cs.getString(paramList.size());   
		        return returnValue;   
		     }      
		 });   
		 return (String)obj;  
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
	public ETLDBInfo getETLDBInfo()throws Exception{
		ETLDBInfo info = new ETLDBInfo();
		DatabaseMetaData metaData = this.getConnection().getMetaData();
		String proName = metaData.getDatabaseProductName().toLowerCase();
		if(proName.indexOf("oracle")!=-1)
			info.setProductName("oracle");
		else
			if(proName.indexOf("sql")!=-1)
				info.setProductName("sqlserver");
		info.setVersionId(metaData.getDatabaseProductVersion());
		info.setDriverName(metaData.getDriverName());
		info.setDriverVersion(metaData.getDriverVersion());
		info.setDburl(metaData.getURL());
		info.setUserName(metaData.getUserName());
		return info;
	}
}
