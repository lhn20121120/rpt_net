package com.fitech.model.worktask.service;

import java.util.List;

import javax.jws.WebService;

import com.fitech.model.commoninfo.security.Operator;
@WebService
public interface InfoMessionService {
	/**
	 * 对用户类表下的用户执行指定的提醒操作(该方法为客户化方案也可以什么都不做)
	 * @param operList
	 * @return String  "1" : 成功   "0" :失败
	 * @throws Exception
	 */
	public String infoMessionUsers(List<Operator> operList)throws Exception;
	/**
	 * 对用户类表下的用户传送commoninfo中的指定信息(该方法为客户化方案也可以什么都不做)
	 * @param operList
	 * @param commonInfo 
	 * @return
	 * @throws Exception
	 */
	public String infoMessionUserByCommon(List<Operator> operList,String commonInfo)throws Exception;
}
