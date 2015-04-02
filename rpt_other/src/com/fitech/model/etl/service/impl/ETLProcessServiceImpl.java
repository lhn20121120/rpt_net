package com.fitech.model.etl.service.impl;

import java.io.File;

import com.fitech.framework.core.service.BaseServiceException;
import com.fitech.framework.core.service.impl.DefaultBaseService;
import com.fitech.model.etl.model.pojo.EtlTaskInfo;
import com.fitech.model.etl.service.IETLJdbcService;
import com.fitech.model.etl.service.IETLProcessService;

public class ETLProcessServiceImpl  extends DefaultBaseService<EtlTaskInfo, Integer> implements IETLProcessService{

	private IETLJdbcService etlJdbcService;
	
	public IETLJdbcService getEtlJdbcService() {
		return etlJdbcService;
	}
	public void setEtlJdbcService(IETLJdbcService etlJdbcService) {
		this.etlJdbcService = etlJdbcService;
	}
	public void executeScript(File script)throws BaseServiceException {
		
	}
	public void executeProcedure(String procName)throws BaseServiceException {
	}
	public void executeProgram(String className)throws BaseServiceException {
		
	}
}
