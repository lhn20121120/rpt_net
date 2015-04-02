package com.fitech.gznx.service.webservice;

import javax.jws.WebService;

import com.fitech.papp.portal.model.pojo.WebServiceUser;
@WebService 
public interface OperatorService {
	/**
	 * 新增用户信息
	 * @param operatorForm
	 * @return String  "1" : 成功   "0" :失败
	 * @throws Exception
	 */
	public String insertUser(WebServiceUser operator);
	/**
	 * 更新用户信息
	 * @param operator
	 * @return String  "1" : 成功   "0" :失败
	 * @throws Exception
	 */
	public String updateUser(WebServiceUser operator);
	/**
	 * 更新用户信息
	 * @param operator
	 * @return String  "1" : 成功   "0" :失败
	 * @throws Exception
	 */
	public String deleteUser(WebServiceUser operator);
	/**
	 * 判断用户是否存在
	 * @param userLoginId
	 * @return String  "1" : 成功   "0" :失败
	 * @throws Exception
	 */
	public String existsUser(String userLoginId);
	/**
	 * ע���û�
	 * @param userName
	 * @return
	 */
	public String logOut(String userName);
	/**
	 * ע������Ӧ�õ��û���Ϣ
	 * @param userName
	 * @return
	 */
	public String logOutAll(String userName);
	
}
