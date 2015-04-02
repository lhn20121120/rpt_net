package com.fitech.framework.core.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

public interface BaseJdbcService {

	public String queryForJdbcStr(String sql) throws BaseServiceException;

	public Map queryForJdbcMap(String sql) throws BaseServiceException;

	public List queryForJdbcList(String sql) throws BaseServiceException;

	public Integer queryForJdbcIntger(String sql) throws BaseServiceException;

	public BigDecimal queryForJdbcDecemal(String sql) throws BaseServiceException;
	
	public String execPro()throws BaseServiceException;
	
	public JdbcTemplate createJdbcTemplate();
}
