package com.fitech.model.worktask.common;

import java.util.ArrayList;
import java.util.List;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

import com.fitech.gznx.service.webservice.OperatorService;
import com.fitech.model.commoninfo.security.Operator;
import com.fitech.model.worktask.service.InfoMessionService;

public class WorkTaskUtil {

	public static Object getWebServices(Class objclass,String webServiceURL){
		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(objclass);  
        factory.setAddress(webServiceURL); 
		Object object = factory.create();
		return object;
	}
	public static void TEST(String[] args){
		InfoMessionService service = (InfoMessionService) getWebServices(InfoMessionService.class,"http://localhost:7001/ZHJGBS_ESB/services/InfoMessionService");
		try {
			Operator oper = new Operator();
			oper.setIdentificationNumber("138293829028");
			List<Operator> list = new ArrayList();
			list.add(oper);
			service.infoMessionUsers(list);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args){
		OperatorService service = (OperatorService) getWebServices(OperatorService.class,"http://localhost:7001/portal/services/OperatorService");
		try {
			service.logOut("admin");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
