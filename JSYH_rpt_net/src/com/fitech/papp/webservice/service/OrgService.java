package com.fitech.papp.webservice.service;

import com.fitech.papp.webservice.pojo.WebSysOrg;




/**
 * 平台机构同步维护客户风险
 * @author huajj
 *	2014-2-26 下午4:40:22
 */
public interface OrgService {
	
	/**
	 * 新增机构信息
	 * 
	 * @param org
	 * @return String 
	 * "0" : 成功 
	 * "1" : 机构已经存在   
	 * "2" : 记录未通过客户风险校验    
	 * "3" : 系统错误 新增未完成
	 * "4" : 新增的是总行，客户风险中已经有总行 不允许出现多个总行
	 * @throws Exception
	 */
	public String insertOrg(WebSysOrg org);

	/**
	 * 更新机构信息
	 * 
	 * @param org
	 * @return String 
	 *  "0": 成功
	 *  "1": 机构不存在   
	 *  "2": 记录未通过客户风险校验   
	 *  "3": 系统错误 新增未完成
	 * @throws Exception
	 */
	public String updateOrg(WebSysOrg org);

	/**
	 * 删除机构信息
	 * 
	 * @param org
	 * @return String
	 *  "0"：   成功   
	 *  "1":  机构不存在 不允许删除     
	 *  "2"：  机构存在 下级机构，不允许删除   
	 *  "3":  系统错误 新增未完成
	 * @throws Exception
	 */
	public String deleteOrg(WebSysOrg org);
	
	
	
}
