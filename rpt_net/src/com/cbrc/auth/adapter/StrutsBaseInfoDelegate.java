package com.cbrc.auth.adapter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.cbrc.auth.util.IStrutsUserDelegate;
import com.cbrc.auth.util.JdbcBaseDao;
import com.cbrc.org.entity.SysBaseInfo;
import com.cbrc.org.entity.SysUser;
import com.cbrc.org.entity.SysUserRole;
import com.cbrc.smis.util.FitechException;

public class StrutsBaseInfoDelegate extends JdbcBaseDao implements IStrutsUserDelegate {

	private static FitechException log = new FitechException(StrutsUserDelegate.class);
	
	/***
	 * 删除用户已经关联关系
	 */
	@Override
	public boolean deleteUser(String userName) {
		if(userName =="")return false;
		String sql ="delete from sys_user_baseinfo where user_id='"+userName+"'" ;
//		String sql2 = "delete from sys_user where username='"+userName+"'";
		boolean result = false;
		Connection con = this.getCon();
		Statement st;
		try {
			st = con.createStatement();
			st.addBatch(sql);
//			st.addBatch(sql2);
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

	/***
	 * 修改用户信息
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

	/***
	 * 增加用户关联关系
	 */
	@Override
	public boolean insertAssociation(String userName) {
		SysBaseInfo baseInfo = new SysBaseInfo();
		baseInfo.setUserId(userName);
		baseInfo.setMainType("bspt");
		String sql="";
		boolean result = false;
		Connection con = this.getCon();
		if(baseInfo.getUserId()!= null){
			sql = "insert into sys_user_baseinfo values('"+baseInfo.getUserId()+"','"+baseInfo.getMainType()+"')";
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
}
