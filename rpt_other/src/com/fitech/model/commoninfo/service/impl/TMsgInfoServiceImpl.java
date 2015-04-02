package com.fitech.model.commoninfo.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.fitech.framework.core.service.BaseServiceException;
import com.fitech.framework.core.service.impl.DefaultBaseService;
import com.fitech.framework.core.util.DateUtil;
import com.fitech.framework.core.web.PageResults;
import com.fitech.model.commoninfo.common.CommonInfoConfig;
import com.fitech.model.commoninfo.model.pojo.TMsgInfo;
import com.fitech.model.commoninfo.service.ITMsgInfoService;
import com.fitech.model.worktask.security.Operator;

public class TMsgInfoServiceImpl extends DefaultBaseService<TMsgInfo, Integer> implements ITMsgInfoService {
	
	/***
	 * 查出自己的消息信息
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public PageResults<TMsgInfo> findMsgByOwn(String userId,Integer msgType,int pageSize,int pageNo,String where) throws Exception {
		if(userId==null || userId.equals(""))
			throw new Exception("该用户不正确");
		String hql = "from TMsgInfo m where m.msgType!=1 and m.status=1 and m.msgState=1 and m.touserId='"+userId+"'";
		String whereSql = "";
		/*if(where!=null && !where.equals(""))
			whereSql = " and (m.userName like '%"+where+"%' or m.startTime like '%"+where+"%' or m.msgTitle like '%"+where+"%' or m.content like '%"+where+"%')";
		hql+=whereSql;*/
		hql+=" order by m.msgState, m.startTime desc";
		PageResults<TMsgInfo> msgResult = this.findPageByHsql(hql,null,pageSize,pageNo);
		
		return msgResult;
	}

	/***
	 * 查处公告信息
	 * @return
	 * @throws Exception
	 */
	public PageResults<TMsgInfo> findPubMsg(int pageSize,int pageNo,String where) throws Exception {
		String hql = "from TMsgInfo m where m.msgState=1 and m.status=1 and m.touserId='"+where+"'";//1 代表已读，0代表未读
		/*String whereSql = "";
		if(where!=null && !where.equals(""))
			whereSql = " and (m.userName like '%"+where+"%' or m.startTime like '%"+where+"%' or m.msgTitle like '%"+where+"%' or m.content like '%"+where+"%')";
		hql+=whereSql;*/
		hql+=" order by m.msgState, m.startTime desc";
		PageResults<TMsgInfo> msgResult = this.findPageByHsql(hql,null,pageSize,pageNo);
		return msgResult;
	}

	/**
	 * 根据阅读状态查询消息信息
	 * @param msgState 阅读状态0为未读，1为已读
	 * @return
	 * @throws Exception
	 */
	public Integer notReadCount(Integer msgState,String userId,Integer msgType) throws Exception {
		String hql = "from TMsgInfo m where m.msgState="+msgState+" and m.touserId='"+userId+"' and m.touserType=2  and m.msgType="+msgType;
		List infoList = this.findListByHsql(hql, null);
		Integer count = 0;
		if(infoList==null || infoList.size()==0)
			return count;
		count = (Integer)infoList.size();
		return count;
		
		
	}
	
	
	
	/***
	 * 查询未同步的信息
	 * @param fileNameFlag
	 * @return
	 * @throws Exception
	 */
	public List<TMsgInfo> findNotSynFileName(Integer synflag)
			throws Exception {
		String hql = "from TMsgInfo m where m.synflag="+synflag+" and m.msgState!=2";
		List<TMsgInfo> msgList = null;
		msgList = this.findListByHsql(hql, null);
	
		return msgList;
	}
	
	/***
	 * 发布公告
	 * @param msgInfo
	 * @return
	 * @throws Exception
	 */
	public boolean savePubMsg(TMsgInfo msgInfo) throws Exception {
		if(msgInfo==null)
			throw new Exception("数据异常");
		try {
			this.saveOrUpdate(msgInfo);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("发送公告失败");
		}
		return true;
	}
	
	/**
	 * 查出所有草稿信息
	 */
	public PageResults<TMsgInfo> findOwnModel(String userId,
			Integer msgState,int pageSize,int pageNo) throws Exception {
		String hql = "from TMsgInfo m where m.userId='"+userId+"'  and m.msgState="+msgState+"  order by m.startTime desc";
		List<TMsgInfo> msgList = null;
		PageResults<TMsgInfo> msgResult = this.findPageByHsql(hql,null,pageSize,pageNo);
		return msgResult;
	}
	
	

	public Integer findCountModel(String userId, Integer msgState)
			throws Exception {
		String hql = "from TMsgInfo m where m.userId='"+userId+"'  and m.msgState="+msgState+"  order by m.startTime desc";
		List<TMsgInfo> obj = this.findListByHsql(hql,null);
		if(obj==null)
			return 0;
		return obj.size();
	}
	
	
	/***
	 * 根据ID检索单个消息信息
	 */
	public TMsgInfo findMsgInfoById(Integer msgId) throws Exception {
		if(msgId==null || msgId.equals(""))
			return null;
		String hql = "from TMsgInfo m where m.msgId="+msgId;
		Object obj = this.findObject(hql);
		if(obj==null)
			return null;
		TMsgInfo m=(TMsgInfo)obj;
		if(m.getContent().contains("\r\n")){
			m.setContent(m.getContent().replace("\r\n", "<br/>"));
		}
		return m;
	}

	public void updateMsgInfoReadered(Integer msgId,Integer msgState) throws Exception {
		String hql = "update TMsgInfo t set t.msgState="+msgState+" where t.msgId="+msgId;
		this.updateByHsql(hql, null);
		
	}
	
	/**
	 * 查询发送的邮件
	 */
	public PageResults<TMsgInfo> findOwnPublishMsg(String userId,int pageSize,int pageNo)
			throws Exception {
		String hql = "from TMsgInfo m where m.userId='"+userId+
		"' and m.status=0 and m.msgState!=2 order by m.startTime desc";
		PageResults<TMsgInfo> results = this.findPageByHsql(hql, null, pageSize, pageNo);
		return results;
	}

	public void saveTMsgInfo(String content,Operator operator) throws Exception {
		//保存邮件信息固定信息数据
		TMsgInfo tmsgInfo = null;
		if(tmsgInfo==null)
			tmsgInfo = new TMsgInfo();
		
		
		String startTime = DateUtil.getToday("yyyy-MM-dd HH:mm:ss");
		String selDate = startTime.substring(0, 10);
		String hql = "";
		hql = "from TMsgInfo m where m.msgTitle='"+CommonInfoConfig.MSG_CONTENT+"' and m.startTime like '"+selDate+"%'";
		List<TMsgInfo> msgInfoList=this.findListByHsql(hql, null);
		if(msgInfoList!=null && msgInfoList.size()>0){
			TMsgInfo msg = msgInfoList.get(0);
			msg.setStartTime(startTime);
			msg.setMsgState(0);
			String con = msg.getContent().replaceAll(",\\["+content+"\\]", "").replaceAll("\\["+content+"\\]", "");
			msg.setContent("["+content+"],"+con);
			this.update(msg);
			return;
		}
			
		//保存用户ID 用户姓名
		tmsgInfo.setUserId(operator.getOperatorId()+"");
		tmsgInfo.setUserName(operator.getUserName());
		//发件机构为银监局
		tmsgInfo.setUserType(2);
		
		tmsgInfo.setStartTime(startTime);//发件邮件时间
		tmsgInfo.setTouserType(1);//收件人为机构
		tmsgInfo.setContent("["+content+"]"+CommonInfoConfig.MSG_CONTENT);
		tmsgInfo.setFileNameFlag(0);//附件未同步
		tmsgInfo.setSynflag(0);//邮件未同步
		tmsgInfo.setUserType(2);//发件机构为银监局
		tmsgInfo.setMsgState(0);
		tmsgInfo.setMsgType(1);//默认设为公告信息
		tmsgInfo.setMsgTitle(CommonInfoConfig.MSG_CONTENT);
		this.save(tmsgInfo);
		
	}

	@Override
	public String createUserDataJSON(Operator operator) throws Exception {
		// TODO Auto-generated method stub
		List orgList = null;
	//	if (operator.isSuperManager())// 超级用户查询所有机构
			orgList =getAllFirstOrg();
		//else
			// 查询用户机构
			//orgList = getFirstOrgById(operator.getOrgId());
		StringBuffer treeJSON = new StringBuffer("[");

		for (int i = 0; i < orgList.size(); i++) {
			String[] strs = (String[]) orgList.get(i);
			treeJSON.append("{\"id\":\""+strs[0]+"\",");
			treeJSON.append("\"text\":"+"\""+strs[1]+"\"");
			
			treeJSON.append(iteratorCreate(strs[0],null,operator.getOperatorId().toString()));//加载机构下的所有用户

			treeJSON.append("}");
			if (i != orgList.size() - 1)
				treeJSON.append(",");
		}

		treeJSON.append("]");
		System.out.println(treeJSON);
		return treeJSON.toString();
	}
	
	private String iteratorCreate(String id ,List list ,String userId) {
		List userList =getAllUsersInOrg(id);
		
		if (userList == null || userList.size() == 0)
			return "";
		StringBuffer treeJSON = new StringBuffer(",\"children\":[");
		for (int i = 0; i < userList.size(); i++) {
			String[] strs = (String[]) userList.get(i);
			treeJSON.append("{\"id\":\"" + strs[0]+strs[1] + "\",\"text\":\"" +strs[1]+ "\"");
			if(strs[0].indexOf("_")==-1){//如果不存在_,则说明当前strs中存放的是子机构，则遍历
				
				treeJSON.append(iteratorCreate(strs[0],null,userId.toString()));//加载机构下的所有用户
			}
			treeJSON.append("}");
			if (i != userList.size() - 1)
				treeJSON.append(",");

		}
	
		treeJSON.append("]");
		
		return treeJSON.toString();
	}
	public String createRoleDataJSON(Operator operator) throws Exception {
		// TODO Auto-generated method stub
		List roleList = null;
		//	if (operator.isSuperManager())// 超级用户查询所有机构
		roleList =getAllRoles();
		//else
		// 查询用户机构
		//orgList = getFirstOrgById(operator.getOrgId());
		StringBuffer treeJSON = new StringBuffer("[");
		
		for (int i = 0; i < roleList.size(); i++) {
			String[] strs = (String[]) roleList.get(i);
			treeJSON.append("{\"id\":\""+strs[0]+"\",");
			treeJSON.append("\"text\":"+"\""+strs[1]+"\"");
			
			treeJSON.append(iteratorCreateRole(strs[0],null,operator.getOperatorId().toString()));//加载角色下的所有用户
			
			treeJSON.append("}");
			if (i != roleList.size() - 1)
				treeJSON.append(",");
		}
		
		treeJSON.append("]");
		System.out.println(treeJSON);
		return treeJSON.toString();
	}
	public List getAllRoles(){
		
		List result=new ArrayList();
		
		String hql="select vr.id.roleId,vr.id.roleName  from ViewWorktaskRole vr ";
		try {
			
			List roleList=this.findListByHsql(hql, null);
			for (int i = 0; roleList!=null&&i < roleList.size(); i++) {
				
				Object[] objs=(Object[])roleList.get(i);
				
				String[] strs=new String[2];
				
				strs[0]=(Long)objs[0]+"";
				
				strs[1]=(String)objs[1];
				
				result.add(strs);
				
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		
		return result;
	}
	
	public List getUsersbyRole(String roleId){
		
		List result=new ArrayList();
		
		String hql="select ur.id.userId,opr.id.firstName,opr.id.lastName  from UserRole ur ,Operator opr where ur.id.userId=opr.id.userId and  ur.id.roleId="+roleId;
		try {
			
			List userRoleList=this.findListByHsql(hql, null);
			for (int i = 0; userRoleList!=null&&i < userRoleList.size(); i++) {
				
				Object[] objs=(Object[])userRoleList.get(i);
				
				String[] strs=new String[2];
				
				strs[0]=(Long)objs[0]+"";
				
				strs[1]="";
				if(null!=objs[1]&&!"".equals((String)objs[1])){
					strs[1]+=(String)objs[1];
				}
				else{
					strs[1]+="";
				}
				if(null!=objs[2]&&!"".equals((String)objs[2])){
					strs[1]+=(String)objs[2];
				}
				else{
					strs[1]+="";
				}
				
				result.add(strs);
				
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		
		return result;
	}
	
	private String iteratorCreateRole(String id ,List list ,String userId) {
		List userList =getUsersbyRole(id);
		
		if (userList == null || userList.size() == 0)
			return "";
		StringBuffer treeJSON = new StringBuffer(",\"children\":[");
		for (int i = 0; i < userList.size(); i++) {
			String[] strs = (String[]) userList.get(i);
			treeJSON.append("{\"id\":\"" + strs[0]+"_"+strs[1] + "\",\"text\":\"" +strs[1]+ "\"");
			treeJSON.append("}");
			if (i != userList.size() - 1)
				treeJSON.append(",");
			
		}
		treeJSON.append("]");
		return treeJSON.toString();
	}
	public List getAllFirstOrg() {

		List result = null;
		List list = null;
		String hsql = "select distinct oi.id.orgId,oi.id.orgName,oi.id.isCollect,oi.id.preOrgId from ViewWorktaskOrg oi ,ViewWorkTaskOperator vo where oi.id.orgId=vo.id.orgId and oi.id.orgLevel=1 " +
				" order by oi.id.isCollect";

		result = new ArrayList();

		try {
			list = this.findListByHsql(hsql, null);
		} catch (BaseServiceException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < list.size(); i++) {

			Object[] os = (Object[]) list.get(i);

			String[] strs = new String[4];

			strs[0] = ((String) os[0]).trim();

			strs[1] = ((String) os[1]).trim();

			strs[3] = ((String) os[3]).trim();

			Integer integer = (Integer) os[2];

			if (integer == null
					|| integer.toString().equals(CommonInfoConfig.NOT_IS_COLLECT))

				strs[2] = CommonInfoConfig.NOT_IS_COLLECT;

			else

				strs[2] = CommonInfoConfig.IS_COLLECT;

			result.add(strs);
		}

		if (result.size() == 0)

			result = null;

		return result;
	}

	public List getFirstOrgById(String orgId) {

		List result = null;

		result = new ArrayList();

		String hsql = "select oi.id.orgId,oi.id.orgName,oi.id.isCollect from ViewWorktaskOrg oi where oi.id.orgId = "
				+ orgId;

		List list;
		try {
			list = this.findListByHsql(hsql, null);

			for (int i = 0; i < list.size(); i++) {

				Object[] os = (Object[]) list.get(i);

				String[] strs = new String[3];

				strs[0] = ((String) os[0]).trim();

				strs[1] = ((String) os[1]).trim();

				Integer integer = new Integer(os[2].toString());

				if (integer == null
						|| integer.toString().equals(
								CommonInfoConfig.NOT_IS_COLLECT))

					strs[2] = CommonInfoConfig.NOT_IS_COLLECT;

				else

					strs[2] = CommonInfoConfig.IS_COLLECT;

				result.add(strs);
			}
		} catch (BaseServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (result.size() == 0)

			result = null;

		return result;
	}

	public List getChildListByOrgId(String id ,String userId) {
		List result = null;
		result = new ArrayList();
		String hsql = "select oi.id.orgId,oi.id.orgName,oi.id.isCollect ,oi.id.preOrgId from ViewWorktaskOrg oi where oi.id.preOrgId='"
				+ id + "' ";
		List list = null;
		try {
			list = this.findListByHsql(hsql, null);
			
			for (int i = 0; i < list.size(); i++) {

				Object[] os = (Object[]) list.get(i);

				String[] strs = new String[3];

				strs[0] = ((String) os[0]).trim();

				strs[1] = ((String) os[1]).trim();

				Integer integer = new Integer(os[2].toString());

				if (integer == null
						|| integer.toString().equals(
								CommonInfoConfig.NOT_IS_COLLECT))

					strs[2] = CommonInfoConfig.NOT_IS_COLLECT;

				else

					strs[2] = CommonInfoConfig.IS_COLLECT;
				result.add(strs);
			}
		} catch (BaseServiceException e) {
			e.printStackTrace();
			this.log.error(e.getMessage());
		}
		if (result.size() == 0)

			result = null;

		return result;

	}
	//查找机构下的所有用户
	public List getAllUsersInOrg(String orgId){
		List result =new ArrayList();
		String hql="select vo.id.userId,vo.id.firstName,vo.id.lastName  from ViewWorkTaskOperator vo where vo.id.orgId='"+orgId+"'";
		String childHql= "select oi.id.orgId,oi.id.orgName,oi.id.isCollect ,oi.id.preOrgId from ViewWorktaskOrg oi where oi.id.preOrgId='"+orgId+"' ";
		try {
			List list=this.findListByHsql(hql, null);
			List childList=this.findListByHsql(childHql, null);
			
			for (int i = 0; list!=null&&i <list.size(); i++) {
				
				Object[] os=(Object[])list.get(i);
				
				String[] strs=new String[2];
				
				strs[0]=(Long)os[0]+"_";//如果是用户则加_,用以区分用户和子机构
				strs[1]="";
				if(null!=os[1]&&!"".equals((String)os[1])){
					strs[1]+=(String)os[1];
				}
				else{
					strs[1]+="";
				}
				if(null!=os[2]&&!"".equals((String)os[2])){
					strs[1]+=(String)os[2];
				}
				else{
					strs[1]+="";
				}
				
				
				result.add(strs);
			}
			for (int i = 0; childList!=null&&i <childList.size(); i++) {
				
				Object[] os=(Object[])childList.get(i);
				
				String[] strs=new String[2];
				
				strs[0]=(String)os[0]+"";//如果是子机构，则不加_，用以区分用户和子机构
				strs[1]=(String)os[1];
				
				
				
				result.add(strs);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public List<TMsgInfo> getUnreadNotice(long userId) throws Exception {
		// TODO Auto-generated method stub
		List<TMsgInfo> result=new ArrayList<TMsgInfo>();
		String hql="from TMsgInfo t where t.touserId='"+userId+"' and t.status=1 and t.msgState=0  order by t.startTime desc";
		try {
			List<TMsgInfo> msgList=this.findListByHsql(hql, null);
			for (int i = 0; i < msgList.size(); i++) {
				TMsgInfo tmsInfo=msgList.get(i);
				result.add(tmsInfo);
				
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return result;
	}

	@Override
	public boolean isRead(String msgId) throws Exception {
		// TODO Auto-generated method stub
		boolean flag=false;
		String updateHql="from TMsgInfo t where t.msgId="+msgId;
		try {
			TMsgInfo msgInfo=(TMsgInfo)this.findObject(updateHql);
			if(msgInfo!=null){
				msgInfo.setMsgState(1);
				this.update(msgInfo);
				flag=true;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			flag=false;
		}
		return flag;
	}
	
}
