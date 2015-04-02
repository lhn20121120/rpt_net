package com.fitech.model.worktask.webservice.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

import com.fitech.gznx.service.webservice.OperatorService;
import com.fitech.model.worktask.webservice.IWorkTaskMoniWebService;

@WebService(endpointInterface = "com.fitech.model.worktask.webservice.IWorkTaskMoniWebService",serviceName = "WorkTaskMoniWebService")
public class WorkTaskMoniWebServiceImpl extends BaseWebService implements IWorkTaskMoniWebService{

	@Override
	public String pendingWorkTask() {
		// TODO Auto-generated method stub
		System.out.println("aaaaaaaaaaaa");
		return null;
	}
	public static void main(String[] args) throws Exception {  
        //创建WebService客户端代理工厂  
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();  
        //注册WebService接口  
        factory.setServiceClass(IWorkTaskMoniWebService.class);  
        //设置WebService地址  
        factory.setAddress("http://localhost:8080/rpt_other/services/WorkTaskMoniWebService");  
        IWorkTaskMoniWebService service = (IWorkTaskMoniWebService)factory.create();
        System.out.println("invoke webservice...");
      
        System.out.println(service.pendingWorkTask());     
    }  
	
}
