package com.fitech.model.worktask.service.impl.custom;

import java.util.List;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

import com.fitech.model.commoninfo.security.Operator;
import com.fitech.model.worktask.service.InfoMessionService;
import com.fitech.model.worktask.service.InfoMessionUserService;
import com.fitech.model.worktask.webservice.impl.BaseWebService;

public class InfoMessionUserServiceImplForZhyh extends BaseWebService implements InfoMessionUserService{

	@Override
	public String infoMessionUserByCommon(List<Operator> operList,
			String commonInfo) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String infoMessionUsers(List<Operator> operList) throws Exception {
		// TODO Auto-generated method stub
		String result = SUCCESS_FLAG;
		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();  
        factory.setServiceClass(InfoMessionService.class);
        factory.setAddress("http://localhost:30082/ZHJGBS_ESB/services/InfoMessionService");
        InfoMessionService service = (InfoMessionService)factory.create();
        try{
        	service.infoMessionUsers(operList);
        }catch(Exception e){
        	e.printStackTrace();
        	result = FAIL_FLAG;
        }
		return result;
	}
}
