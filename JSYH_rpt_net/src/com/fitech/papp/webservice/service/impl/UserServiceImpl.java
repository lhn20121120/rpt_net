package com.fitech.papp.webservice.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;

import com.fitech.gznx.service.webservice.OperatorService;
import com.fitech.papp.portal.model.pojo.WebServiceUser;
import com.fitech.papp.webservice.pojo.WebSysUser;
import com.fitech.papp.webservice.service.UserService;

/**
 * 用户同步组件 访问路径http://localhost:8080/portal/services/UserWebService
 * 
 * @author Chris
 * 
 */
public class UserServiceImpl implements UserService {
	public static Logger log = Logger.getLogger(UserServiceImpl.class);

	OperatorService operaService;

	public void setOperaService(OperatorService operaService) {
		this.operaService = operaService;
	}

	@Override
	public String existsUser(String userLoginId) {
		String s = operaService.existsUser(userLoginId);
		if (s.equals("SUCCESS_FLAG")) {
			return "1";
		} else {
			return "0";
		}
	}

	@Override
	public String existsUserByEmployeeId(String employeeId) {
		String s = operaService.existsUserByEmployeeId(employeeId);
		if (s.equals("SUCCESS_FLAG")) {
			return "2";
		} else {
			return "0";
		}
	}

	@Override
	public String deleteUser(WebSysUser operator) {
		String result = "1";
		try {
			WebServiceUser wsu = new WebServiceUser();
			BeanUtils.copyProperties(operator, wsu);
			operaService.deleteUser(wsu);
		} catch (Exception e) {
			result = "0";
		}
		return result;
	}

	@Override
	public String insertUser(WebSysUser operator) {
		if (existsUser(operator.getUsername()).equals("1")) {
			return "1";
		}
		if (existsUserByEmployeeId(operator.getEmployeeId()).equals("2")) {
			return "2";
		}
		String result = "0";
		try {
			WebServiceUser wsu = new WebServiceUser();
			wsu.setUsername(operator.getUsername());
			wsu.setMobilePhone(operator.getTelphoneNumber());
			wsu.setOrgId(operator.getOrgId());
			wsu.setFirstname(operator.getRealName());
			wsu.setPassword(operator.getPassword());
			wsu.setIsSuper(operator.getIsSuper());
			operaService.insertUser(wsu);
		} catch (Exception e) {
			e.printStackTrace();
			result = "3";
		}
		return result;
	}

	@Override
	public String updateUser(WebSysUser operator) {
		try {
			WebServiceUser wsu = new WebServiceUser();
			wsu.setUsername(operator.getUsername());
			wsu.setMobilePhone(operator.getTelphoneNumber());
			wsu.setOrgId(operator.getOrgId());
			wsu.setFirstname(operator.getRealName());
			wsu.setPassword(operator.getPassword());
			operaService.updateUser(wsu);
			return "1";
		} catch (Exception e) {
			return "0";
		}
	}
}
