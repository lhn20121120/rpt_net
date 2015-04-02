package com.fitech.model.worktask.webservice.impl;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;


public class BaseWebService {
	
	public static final String SUCCESS_FLAG = "1";
	
	public static final String FAIL_FLAG = "0";
	
	public Object execute(){
		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();  
        factory.setServiceClass(this.getClass());  
        factory.setAddress("http://localhost:8080/rpt_net/services/OperatorService"); 
		Object object = factory.create();
		return object;
	}
}
