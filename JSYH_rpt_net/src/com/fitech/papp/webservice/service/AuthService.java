package com.fitech.papp.webservice.service;

/**
 * 认证同步组件接口
 * 
 * 
 * 
 */
public interface AuthService {

	/**
	 * 0成功 1失败
	 * 
	 * @param sessionId
	 * @param username 加密过后的用户名，要进行解密
	 * @return
	 */
	public String setSession(String sessionId, String username);

	/**
	 * 通过sessionId查找是否存在此用户
	 * 
	 * @param sessionId
	 * @return 0不存在  如果存在，返回加密过后的用户名
	 */
	public String isExist(String sessionId);
}
