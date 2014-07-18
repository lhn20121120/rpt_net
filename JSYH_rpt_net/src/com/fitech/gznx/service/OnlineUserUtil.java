package com.fitech.gznx.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.security.Operator;
import com.fitech.gznx.util.OnlineUser;



public class OnlineUserUtil {
	/**
	 * ��ǰ�����û��б�
	 */
	private static Map onlineUserMap = new HashMap();
	private static Map users=new HashMap();
    private static Map sessionMap = new HashMap();

	/**
	 * ���������û�
	 * 
	 * @param operator
	 */
	public static void addOnlineUser(Operator operator)
	{
		if (operator != null)
		{
			String userName = operator.getUserName();
			String ipAdd = operator.getIpAdd();
			String sessionId = operator.getSessionId();
			if (sessionId != null && !sessionId.equals(""))
			{
				if (!users.containsKey(userName) || !users.containsKey(ipAdd))
				{
					OnlineUser online = new OnlineUser();
					online.setIpAdd(ipAdd);
					online.setSessionId(sessionId);
					online.setUserName(userName);
					onlineUserMap.put(sessionId, operator);
					users.put(userName,online);
				}
			}
		}
		
	}

	/**
	 * ɾ�������û�
	 * 
	 * @param operator
	 */
	public static void removeOnlineUser(Operator operator)
	{
		if (operator != null)
		{
			String sessionId = operator.getSessionId();
			String username = operator.getUserName();
			if (sessionId != null && !sessionId.equals(""))
			{
				if (onlineUserMap.containsKey(sessionId))
				{
					onlineUserMap.remove(sessionId);
					users.remove(username);
				}
			}
		}
	}
	/**
	 * 1.29 yql xiugai
	 * @param sessionId
	 */
	public static void removeOnlineUser(String sessionId)
	{
		if (sessionId != null && !sessionId.equals(""))
		{	
			Operator operator = (Operator)onlineUserMap.get(sessionId);
			String username = operator.getUserName();		
			if (onlineUserMap.containsKey(sessionId) && operator != null)
			{
				users.remove(username);
				onlineUserMap.remove(sessionId);
			}
			}
	}
	/**
	 * 1.29 yql xiugai
	 * @param sessionId
	 */
	public static void removeOnlineUserByName(String username)
	{
		if (username != null && !username.equals(""))
		{	
			OnlineUser online = (OnlineUser) users.get(username);
			String sessionId = online.getSessionId();
			Operator operator = (Operator)onlineUserMap.get(sessionId);
			if (onlineUserMap.containsKey(sessionId) && operator != null)
			{
				users.remove(username);
				onlineUserMap.remove(sessionId);
			}
		}
	}
	/**
	 * ��������û����б�
	 * 
	 * @return
	 */
	public static List getOnlineUserList()
	{
		List userList = new ArrayList();
		if (onlineUserMap != null && !onlineUserMap.isEmpty())
		{
			Collection userCollenction = onlineUserMap.values();
			Iterator it = userCollenction.iterator();
			while (it.hasNext())
			{
				userList.add((Operator) it.next());
			}
		}
		return userList;
	}
	
	/**
	 * ��õ�ǰ�����û�����
	 * @return
	 */
	public static int getOnlineUserCount()
	{
		return onlineUserMap.size();
	}
	/**
	 * �жϸ��û��ǲ����Ѿ���¼
	 * @param username
	 * @return
	 */
	public static boolean viewOnlineUser(String username)
	{
		boolean existUser=false; 
		existUser = users.containsValue(username);
		return existUser;
	}

	public static void removeUserName(String userName) {
		users.remove(userName);		
	}
	
	 /**
     * 判断该用户工号是不是已经登录
     * @param employeeId
     * @return
     */
    public static boolean viewOnlineEmployeeId(String employeeId)
    {
        boolean existUser=false;
        existUser = users.containsKey(employeeId);
        return existUser;
    }
    
    /**
     * 提供根据用户远程删除用户
     * @param userName
     */
    public static void removeOnlineUserByRemote(String userName){
        HttpSession session = (HttpSession)sessionMap.get(userName);
        if(session==null)
            return;
        Operator operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
        removeOnlineUser(operator);
        session.removeAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
        sessionMap.remove(userName);
        if(session.isNew())
            session.invalidate();
    }
}
