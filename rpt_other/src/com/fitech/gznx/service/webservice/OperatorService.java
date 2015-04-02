package com.fitech.gznx.service.webservice;

import javax.jws.WebService;

import com.fitech.papp.portal.model.pojo.WebServiceUser;
@WebService 
public interface OperatorService {
	/**
	 * 鏂板鐢ㄦ埛淇℃伅
	 * @param operatorForm
	 * @return String  "1" : 鎴愬姛   "0" :澶辫触
	 * @throws Exception
	 */
	public String insertUser(WebServiceUser operator);
	/**
	 * 鏇存柊鐢ㄦ埛淇℃伅
	 * @param operator
	 * @return String  "1" : 鎴愬姛   "0" :澶辫触
	 * @throws Exception
	 */
	public String updateUser(WebServiceUser operator);
	/**
	 * 鏇存柊鐢ㄦ埛淇℃伅
	 * @param operator
	 * @return String  "1" : 鎴愬姛   "0" :澶辫触
	 * @throws Exception
	 */
	public String deleteUser(WebServiceUser operator);
	/**
	 * 鍒ゆ柇鐢ㄦ埛鏄惁瀛樺湪
	 * @param userLoginId
	 * @return String  "1" : 鎴愬姛   "0" :澶辫触
	 * @throws Exception
	 */
	public String existsUser(String userLoginId);
	/**
	 * 注销用户
	 * @param userName
	 * @return
	 */
	public String logOut(String userName);
}
