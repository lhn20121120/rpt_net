package com.cbrc.auth.adapter;

import com.cbrc.auth.util.IStrutsUserDelegate;
import com.cbrc.auth.util.JdbcBaseDao;
import com.cbrc.org.entity.SysBaseInfo;
import com.cbrc.org.entity.SysUser;
import com.cbrc.org.entity.SysUserRole;
import com.cbrc.smis.util.FitechException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StrutsUserDelegate extends JdbcBaseDao implements IStrutsUserDelegate {

    private static FitechException log = new FitechException(StrutsUserDelegate.class);


    /**
     * 删除用户
     * @param userName
     * @return
     */
    @Override
    public boolean deleteUser(String userName) {
        if(userName =="")return false;
        String sql ="delete from sys_user_role where user_name ='"+userName+"'" ;
        String sql2 = "delete from sys_user where username='"+userName+"'";
        boolean result = false;
        Connection con = this.getCon();
        Statement st;
        try {
            st = con.createStatement();
            st.addBatch(sql);
            st.addBatch(sql2);
            int[] list =st.executeBatch();
            result= true;
            if(list.length<=0)result = false;
        } catch (SQLException e) {
            result = false;
            log.printStackTrace(e);
        }finally{
            this.close();
        }
        return result;
    }
    /***
     * 查询所有用户信息
     */
    @Override
    public List getUserList(String userName) {
        String sql = "select * from sys_user where ";
        if(userName != null && !userName.trim().equals("")){
            sql +=" username ='"+userName+"'";
        }
        ResultSet rs;
        PreparedStatement ps;
        List<SysBaseInfo> list = new ArrayList<SysBaseInfo>();
        try {
            ps = this.getCon().prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                SysBaseInfo user = new SysBaseInfo();
                user.setUserId(rs.getString(1));
                user.setMainType(rs.getString(2));
                list.add(user);
            }
        } catch (SQLException e) {
            log.printStackTrace(e);
        }finally{
            this.close();
        }
        return list;
    }
    /***
     * 增加用户关联关系
     */
    @Override
    public boolean insertAssociation(String userName) {
        SysUserRole role = new SysUserRole();
        role.setRoleId(11);
        role.setUserName(userName);
        String sql="";
        boolean result = false;
        Connection con = this.getCon();
        if(role.getUserName()!= null){
            sql = "insert into sys_user_role values('"+role.getRoleId()+"','"+role.getUserName()+"')";
        }
        PreparedStatement ps;
        try {
            ps=con.prepareStatement(sql);
            int count = ps.executeUpdate();
            if(count>0)result=true;
        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            log.printStackTrace(e);
        }finally{
            this.close();
        }
        return result;
    }
    /***
     * 增加用户信息
     */
    @Override
    public boolean insertUser(SysUser user) {
        String sql="";
        boolean result = false;
        Connection con = this.getCon();
        if(user != null){
            sql = "insert into sys_user values('"+user.getUserName()+"','"+user.getPassWord()+"','"+user.getRealName()+"'," +
                    "'"+user.getIsSuper()+"','"+user.getOrgId()+"',to_date('"+user.getUpdateDate()+"','yyyy-MM-dd')," +
                    "'"+user.getTelphoneNumber()+"','"+user.getDeparTment()+"','"+user.getEmail()+"','"+user.getAddress()+"','"+user.getPostCode()+"')";
        }
        PreparedStatement ps;
        try {
            ps=con.prepareStatement(sql);
            int count = ps.executeUpdate();
            if(count>0)result=true;
        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException e1) {
                log.printStackTrace(e);
            }
            log.printStackTrace(e);
        }finally{
            this.close();
        }
        return result;
    }

    /**
     * 修改用户信息
     * @param user
     * @return
     */
    @Override
    public boolean updateUser(SysUser user) {
        if(user == null) return false;
        String sql = "update sys_user set password='"+user.getPassWord()+"', real_name='"+user.getRealName()+"'" +
                ",is_super='"+user.getIsSuper()+"',org_id='"+user.getOrgId()+"',update_date=to_date('"+user.getUpdateDate()+"','yyyy-MM-dd')" +
                ",telphone_number='"+user.getTelphoneNumber()+"',department='"+user.getDeparTment()+"',email='"+user.getEmail()+"'" +
                ",address='"+user.getAddress()+"',postcode='"+user.getPostCode()+"' where username='"+user.getUserName()+"'";
        boolean result = false;
        Connection con = this.getCon();
        Statement st;
        try {
            st = con.createStatement();
            int count = st.executeUpdate(sql);
            if(count>0)result = true;
        } catch (SQLException e) {
            result = false;
            log.printStackTrace(e);
        }finally{
            this.close();
        }
        return result;
    }
}
