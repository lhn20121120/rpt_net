package com.fitech.papp.webservice.service;


import com.fitech.papp.webservice.pojo.WebSysUser;

/**
 * 用户同步组件接口
 * @author Hu
 *
 */
public interface UserService {
	/**
	 * 新增用户信息
	 * @param operatorForm
	 * @return String  "0" : 成功   "3" :失败   "1":登录ID存在   "2"根据工号判断用户存在
	 * @throws Exception
	 */
	public String insertUser(WebSysUser user);
	/**
	 * 更新用户信息
	 * @param operatorForm
	 * @return String  "1" : 成功   "0" :失败
	 * @throws Exception
	 */
	public String updateUser(WebSysUser user) ;
	/**
	 * 删除用户信息
	 * @param operatorForm
	 * @return String  "1" : 成功   "0" :失败
	 * @throws Exception
	 */
	public String deleteUser(WebSysUser user) ;
	/**
	 * 判断用户ID判断用户是否存在
	 * @param userLoginId
	 * @return String  "1" : 存在   "0" :不存在
	 * @throws Exception
	 */
	public String existsUser(String userLoginId);
	
	/**
	 * 通过工号判断用户是否存在
	 * @param userLoginId
	 * @return String  "2" : 存在   "0" :不存在
	 * @throws Exception
	 */
	public String existsUserByEmployeeId(String userLoginId);
}
