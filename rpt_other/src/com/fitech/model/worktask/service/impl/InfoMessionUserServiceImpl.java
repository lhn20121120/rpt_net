package com.fitech.model.worktask.service.impl;

import java.util.List;

import com.fitech.model.commoninfo.security.Operator;
import com.fitech.model.worktask.service.InfoMessionUserService;
import com.fitech.model.worktask.webservice.impl.BaseWebService;

public class InfoMessionUserServiceImpl extends BaseWebService implements InfoMessionUserService{

	@Override
	public String infoMessionUserByCommon(List<Operator> operList,
			String commonInfo) throws Exception {
		// TODO Auto-generated method stub
		return SUCCESS_FLAG;
	}

	@Override
	public String infoMessionUsers(List<Operator> operList) throws Exception {
		// TODO Auto-generated method stub
		return SUCCESS_FLAG;
	}

	

}
