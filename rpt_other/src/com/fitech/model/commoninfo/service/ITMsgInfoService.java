package com.fitech.model.commoninfo.service;

import java.util.List;

import com.fitech.framework.core.service.IBaseService;
import com.fitech.framework.core.web.PageResults;
import com.fitech.model.commoninfo.model.pojo.TMsgInfo;
import com.fitech.model.worktask.security.Operator;

public interface ITMsgInfoService extends IBaseService<TMsgInfo,Integer>{
	/***
	 * 查出自己的消息信息
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public PageResults<TMsgInfo> findMsgByOwn(String userId,Integer msgType,int pageSize,int pageNo,String where) throws Exception;
	/***
	 * 查处公告信息
	 * @return
	 * @throws Exception
	 */
	public PageResults<TMsgInfo> findPubMsg(int pageSize,int pageNo,String pubWhere) throws Exception;
	
	/**
	 * 根据阅读状态查询消息信息
	 * @param msgState 阅读状态0为未读，1为已读
	 * @return
	 * @throws Exception
	 */
	public Integer notReadCount(Integer msgState,String userId,Integer msgType) throws Exception;
	
	/***
	 * 发布公告
	 * @param msgInfo
	 * @return
	 * @throws Exception
	 */
	public boolean savePubMsg(TMsgInfo msgInfo) throws Exception;
	
	/***
	 * 查询未同步的消息
	 * @param fileNameFlag
	 * @return
	 * @throws Exception
	 */
	public List<TMsgInfo> findNotSynFileName(Integer synflag) throws Exception;
	
	/***
	 * 查询根据邮件类型查出邮件信息
	 * @param userId
	 * @param msgType
	 * @param msgState
	 * @return
	 * @throws Exception
	 */
	public PageResults<TMsgInfo> findOwnModel(String userId,Integer msgState,int pageSize,int pageNo) throws Exception;
	
	/***
	 * 查询草稿的个数
	 * @param userId
	 * @param msgState
	 * @return
	 * @throws Exception
	 */
	public Integer findCountModel(String userId,Integer msgState) throws Exception;
	
	/***
	 * 根据ID检索单个消息
	 * @param info
	 * @return
	 * @throws Exception
	 */
	public TMsgInfo findMsgInfoById(Integer msgId) throws Exception;
	
	/***
	 * 设置邮件为以读
	 * @param msgState
	 * @throws Exception
	 */
	public void updateMsgInfoReadered(Integer msgId,Integer msgState) throws Exception;
	
	/***
	 * 查询发送的邮件
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public PageResults<TMsgInfo> findOwnPublishMsg(String userId,int pageSize,int pageNo) throws Exception;
	
	/**
	 * 保存消息
	 * @param content
	 * @param operator
	 * @throws Exception
	 */
	public void saveTMsgInfo(String content,Operator operator) throws Exception;
	
	public String createUserDataJSON(Operator operator) throws Exception;
	public String createRoleDataJSON(Operator operator) throws Exception;
	
	public List<TMsgInfo> getUnreadNotice(long userId) throws Exception;
	
	public boolean isRead(String msgId) throws Exception;
}
