package com.cbrc.auth.util;

import java.util.List;

import com.cbrc.org.entity.SysBaseInfo;
import com.cbrc.org.entity.SysUser;
import com.cbrc.org.entity.SysUserRole;


public interface IStrutsUserDelegate {

	/***
	 * ���������û�
	 * @param userName
	 * @return
	 */
	List getUserList(String userName);
	/***
	 * �����û��Ĺ�����ϵ
	 * @return
	 */
	boolean insertAssociation(String userName);
	/***
	 * 
	 * @param user
	 * @return
	 */
	boolean insertUser(SysUser user);
	/***
	 * ɾ���û���Ϣ
	 * @param userName
	 * @return
	 */
	boolean deleteUser(String userName);
	/***
	 *�޸��û���Ϣ
	 * @param user
	 * @return
	 */
	boolean updateUser(SysUser user);
}
