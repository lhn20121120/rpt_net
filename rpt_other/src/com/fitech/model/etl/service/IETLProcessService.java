package com.fitech.model.etl.service;

import java.io.File;

import com.fitech.framework.core.service.BaseServiceException;
import com.fitech.framework.core.service.IBaseService;
import com.fitech.model.etl.model.pojo.EtlTaskInfo;

public interface IETLProcessService extends IBaseService<EtlTaskInfo, Integer> {

	public void executeScript(File script) throws BaseServiceException;

	public void executeProcedure(String procName) throws BaseServiceException;

	public void executeProgram(String className) throws BaseServiceException;
}
