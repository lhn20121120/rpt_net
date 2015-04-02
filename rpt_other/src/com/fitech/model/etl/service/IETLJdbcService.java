package com.fitech.model.etl.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.fitech.framework.core.service.BaseServiceException;
import com.fitech.model.etl.model.vo.ETLDBInfo;

public interface IETLJdbcService {

	public String queryForJdbcStr(String sql) throws BaseServiceException;

	public Map queryForJdbcMap(String sql) throws BaseServiceException;

	public List queryForJdbcList(String sql) throws BaseServiceException;

	public Integer queryForJdbcIntger(String sql) throws BaseServiceException;

	public BigDecimal queryForJdbcDecemal(String sql) throws BaseServiceException;
	
	public String executeProcedure(final String procedureExpress,final Map paramMap) throws BaseServiceException;
	
	public String execPro()throws BaseServiceException;
	
	public JdbcTemplate createJdbcTemplate();
	
	public ETLDBInfo getETLDBInfo()throws Exception;
}
