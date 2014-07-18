package com.fitech.gznx.service.webservice.impl;



import java.util.Date;
import java.util.List;

import javax.jws.WebService;

import net.sf.hibernate.Session;

import com.cbrc.auth.adapter.StrutsOperatorDelegate;
import com.cbrc.auth.form.OperatorForm;
import com.cbrc.auth.hibernate.Operator;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.util.FitechException;
import com.fitech.gznx.service.OnlineUserUtil;
import com.fitech.gznx.service.webservice.OperatorService;
import com.fitech.gznx.service.webservice.impl.BaseWebService;
import com.fitech.net.hibernate.OrgNet;
import com.fitech.papp.portal.model.pojo.WebServiceUser;

@WebService(endpointInterface = "com.fitech.gznx.service.webservice.OperatorService",serviceName = "OperatorService")   
public class OperatorServiceImpl extends BaseWebService implements OperatorService {
	public static FitechException log = new FitechException(
			OperatorServiceImpl.class);

	@Override
	public String existsUser(String userLoginId) {
		String result = FAIL_FLAG; 
		if(OnlineUserUtil.viewOnlineUser(userLoginId))
			result = SUCCESS_FLAG;
		return result;
	}
	
	@Override
	public String existsUserByEmployeeId(String employeeId) {
		String result = FAIL_FLAG; 
		if(OnlineUserUtil.viewOnlineEmployeeId(employeeId))
			result = SUCCESS_FLAG;
		return result;
	}
	
	@Override
	public String deleteUser(WebServiceUser operator){
		// TODO Auto-generated method stub
		DBConn conn = null;
		Session session = null;
		boolean rs = true;
		String hsql = "";
		try {
			conn = new com.cbrc.smis.dao.DBConn();
			session = conn.beginTransaction();
			hsql = "from com.cbrc.auth.hibernate.Operator o where o.userName='" + operator.getUsername() + "'";
			List list = session.find(hsql);
			if(list.size()>0){
				Operator entity = (Operator)list.get(0);
				OperatorForm operatorForm = new OperatorForm();
				operatorForm.setUserId(entity.getUserId());
				StrutsOperatorDelegate.remove(operatorForm,false);
			//	session.delete(entity);
			}
		}catch(Exception e){
			log.printStackTrace(e);
			rs = false;
		}finally{
			conn.endTransaction(rs);
		}
		String result = rs ? SUCCESS_FLAG : FAIL_FLAG;
		return result;
	}

	@Override
	public String insertUser(WebServiceUser operator){
		// TODO Auto-generated method stub
		System.out.println(operator.getPassword()+"--------------");
		String result = FAIL_FLAG;
		DBConn conn = null;
		Session session = null;
		String hsql = "";
		Operator entity = null;
		try {
			conn = new com.cbrc.smis.dao.DBConn();
			session = conn.openSession();
			hsql = "from com.cbrc.auth.hibernate.Operator o where o.userName='" + operator.getUsername() + "'";
			List list = session.find(hsql);
			if(list.size()>0)
				entity = (Operator)list.get(0);
			conn.closeSession();
		}catch(Exception e){
			log.printStackTrace(e);
			conn.closeSession();
			return result;
		}
		if(entity!=null){
			result = SUCCESS_FLAG;
			return result;
		}
		OperatorForm form = new OperatorForm();
		form.setUserName(operator.getUsername());
		form.setFirstName(operator.getFirstname());
		form.setPassword(operator.getPassword());
		form.setLastName("");       
		form.setMail("");       
		form.setIdentificationNumber(operator.getMobilePhone());       
		form.setEmployeeNumber("");       
		form.setTitle("");       
		form.setEmployeeType("");       
		form.setBranch("");       
		form.setAddress("");       
		form.setPostalAddress("");       
		form.setPostalCode("");       
		form.setFax("");       
		form.setTelephoneNumber("");   
		if(operator.getIsSuper().equals("true"))
			form.setSuperManager("1");      
		else
			form.setSuperManager("0");     
		form.setSex("");       
		form.setAge("");       
		form.setGroupNumber("");       
		form.setUpdateTime(new Date());
		form.setOrgId(operator.getOrgId());
		try {
			if(StrutsOperatorDelegate.create(form,false))
				result = SUCCESS_FLAG;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = FAIL_FLAG;
		}
		return result;
	}

	@Override
	public String updateUser(WebServiceUser operator){
		// TODO Auto-generated method stub
		DBConn conn = null;
		Session session = null;
		boolean rs = true;
		String hsql = "";
		try {
			conn = new com.cbrc.smis.dao.DBConn();
			session = conn.beginTransaction();
			hsql = "from com.cbrc.auth.hibernate.Operator o where o.userName='" + operator.getUsername() + "'";
			List list = session.find(hsql);
			if(list.size()>0){
				Operator entity = (Operator)list.get(0);
				entity.setUserName(operator.getUsername());
				if(operator.getMobilePhone()!=null){
					entity.setTelephoneNumber(operator.getMobilePhone());
				}
				if(operator.getFirstname()!=null){
					entity.setFirstName(operator.getFirstname());
				}
				if(operator.getOrgId()!=null){
					OrgNet org=new OrgNet();
					org.setOrgId(operator.getOrgId());
					entity.setOrg(org);
					}
				
				if(null!=operator.getIsSuper()&&operator.getIsSuper().equals("true"))
					entity.setSuperManager("1");      
				else
					entity.setSuperManager("0"); 
				
				session.update(entity);
				
			}
		}catch(Exception e){
			log.printStackTrace(e);
			rs = false;
		}finally{
			conn.endTransaction(rs);
		}
		String result = rs ? SUCCESS_FLAG : FAIL_FLAG;
		return result;
	}

	@Override
	public String logOut(String userName) {
		// TODO Auto-generated method stub
		String result = SUCCESS_FLAG;
		try{
			OnlineUserUtil.removeOnlineUserByRemote(userName);
		}catch(Exception e){
			result = FAIL_FLAG; 
			log.printStackTrace(e);
		}
		return result;
	}

	@Override
	public String logOutAll(String userName) {
		// TODO Auto-generated method stub
		
		return null;
	}

	
}
