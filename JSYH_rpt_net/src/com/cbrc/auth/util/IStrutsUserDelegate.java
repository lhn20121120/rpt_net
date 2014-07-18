package com.cbrc.auth.util;

import com.cbrc.org.entity.SysUser;

import java.util.List;


public interface IStrutsUserDelegate {

    /***
     * 查找所有用户
     * @param userName
     * @return
     */
    List getUserList(String userName);
    /***
     * 增加用户的关联关系
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
     * 删除用户信息
     * @param userName
     * @return
     */
    boolean deleteUser(String userName);
    /***
     *修改用户信息
     * @param user
     * @return
     */
    boolean updateUser(SysUser user);
}
