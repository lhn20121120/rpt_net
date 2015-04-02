package com.fitech.model.etl.service;

import java.util.Date;
import java.util.Map;

public interface IETLProParamService {

	public Map getProParams();
	
	public Map getProParams(Date etlDate);
	
	public void setTaskDate(String taskDate);
}
