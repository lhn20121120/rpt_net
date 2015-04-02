package com.fitech.institution.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.util.FitechException;
import com.fitech.institution.po.BakTableInfoVo;
import com.fitech.institution.service.ExecuteSqlService;

public class ExecuteSqlServiceImpl implements ExecuteSqlService {

	private FitechException log = new FitechException(
			ExecuteSqlServiceImpl.class);

	@Override
	public boolean execSQL(Map<String, String> sqlMap) throws Exception {
		DBConn conn = null;
		Session session = null;
		Connection connection = null;
		PreparedStatement ps = null;
		PreparedStatement psBatch = null;
		int batchAddSize = 1000;
		boolean flag  = false;
		try {

			conn = new DBConn();
			session = conn.openSession();
			connection = session.connection();
			connection.setAutoCommit(false);

			String querySysParamSql = "select par_value from SYS_PARAMETER where par_name='BATCHSIZE'";
			psBatch = connection.prepareStatement(querySysParamSql);
			ResultSet addBatch = psBatch.executeQuery();
			while (addBatch.next()) {
				batchAddSize = addBatch.getInt(1);
			}
			psBatch.close();

			Set<String> sets = sqlMap.keySet();
			Iterator<String> its = sets.iterator();

			int count = 0;
			while (its.hasNext()) {
				count++;
				String sql = its.next();
				System.out.println("exec : " + sql);

				ps = connection.prepareStatement(sql);
				ps.execute();
				ps.close();
				if (count % batchAddSize == 0) {
					connection.commit();
				}

			}

			// ps.close();
			connection.commit();
			flag = true;
		} catch (Exception e) {
			log.printStackTrace(e);
			e.printStackTrace();
		} finally {
			if (null != conn) {
				conn.closeSession();
			}
		}
		return flag;
	}

	@Override
	public void bakData(Map<String, String> mCellMap,
			Map<String, String> mCellToMap) {
		DBConn conn = null;
		Session session = null;
		Connection connection = null;
		PreparedStatement ps = null;
		PreparedStatement psTo = null;
		StringBuilder mCellPartStr = new StringBuilder(
				"insert into AF_VALIDATEFORMULA (formula_id, formula_value, formula_name, validate_type_id, template_id, version_id, cell_id ) values (");
		StringBuilder mCellToPartStr = new StringBuilder(
				"insert into AF_VALIDATEFORMULA_STANDARD (formula_id, formula_value, formula_name, validate_type_id, template_id, version_id, cell_id ) values (");
		try {

			conn = new DBConn();
			session = conn.openSession();
			connection = session.connection();
			connection.setAutoCommit(false);

			String queryMCellSql = "select formula_id, formula_value, formula_name, validate_type_id, template_id, version_id, cell_id from AF_VALIDATEFORMULA";
			StringBuilder wholeMCellStr = new StringBuilder();
			ps = connection.prepareStatement(queryMCellSql);
			ResultSet mcellSet = ps.executeQuery();
			while (mcellSet.next()) {
				wholeMCellStr = mCellPartStr.append(mcellSet.getString(1))
						.append(",");
				wholeMCellStr = wholeMCellStr.append(
						"'" + mcellSet.getString(2) + "'").append(",");
				wholeMCellStr = wholeMCellStr.append(
						"'" + mcellSet.getString(3) + "'").append(",");
				wholeMCellStr = wholeMCellStr.append(mcellSet.getString(4))
						.append(",");
				wholeMCellStr = wholeMCellStr.append(
						"'" + mcellSet.getString(5) + "'").append(",");
				wholeMCellStr = wholeMCellStr.append(
						"'" + mcellSet.getString(6) + "'").append(",");
				wholeMCellStr = wholeMCellStr.append(mcellSet.getString(7))
						.append(")\n");
				mCellMap.put(mCellPartStr.toString(), null);
				wholeMCellStr = new StringBuilder();
				mCellPartStr = new StringBuilder(
						"insert into AF_VALIDATEFORMULA (formula_id, formula_value, formula_name, validate_type_id, template_id, version_id, cell_id ) values (");

			}

			ps.close();
			connection.commit();

			String queryMCellToSql = "select formula_id, formula_value, formula_name, validate_type_id, template_id, version_id, cell_id from AF_VALIDATEFORMULA_STANDARD";
			StringBuilder wholeMCellToStr = new StringBuilder();
			psTo = connection.prepareStatement(queryMCellToSql);
			ResultSet mcellToSet = psTo.executeQuery();
			while (mcellToSet.next()) {
				wholeMCellToStr = mCellToPartStr
						.append(mcellToSet.getString(1)).append(",");
				wholeMCellToStr = wholeMCellToStr.append(
						"'" + mcellToSet.getString(2) + "'").append(",");
				wholeMCellToStr = wholeMCellToStr.append(
						"'" + mcellToSet.getString(3) + "'").append(",");
				wholeMCellToStr = wholeMCellToStr.append(
						mcellToSet.getString(4)).append(",");
				wholeMCellToStr = wholeMCellToStr.append(
						"'" + mcellToSet.getString(5) + "'").append(",");
				wholeMCellToStr = wholeMCellToStr.append(
						"'" + mcellToSet.getString(6) + "'").append(",");
				wholeMCellToStr = wholeMCellToStr.append(
						mcellToSet.getString(7)).append(")\n");

				mCellToMap.put(mCellToPartStr.toString(), null);
				wholeMCellToStr = new StringBuilder();
				mCellToPartStr = new StringBuilder(
						"insert into AF_VALIDATEFORMULA_STANDARD (formula_id, formula_value, formula_name, validate_type_id, template_id, version_id, cell_id ) values (");
			}
			psTo.close();
			connection.commit();

		} catch (Exception e) {
			log.printStackTrace(e);
			e.printStackTrace();
		} finally {

			if (null != conn) {
				conn.closeSession();
			}

		}
	}

	@Override
	public Boolean bakTable(String tableName, String backTableName) {
		DBConn conn = null;
		Session session = null;
		Connection connection = null;
		// PreparedStatement ps = null;
		boolean flag = false;
		PreparedStatement psTo = null;
		try {
			conn = new DBConn();
			session = conn.beginTransaction();
			connection = session.connection();
			String querySysParamSql = "create table " + backTableName
					+ " as select * from " + tableName;
			psTo = connection.prepareStatement(querySysParamSql);
			psTo.executeQuery();
			log.println("bak table " + tableName + " to " + backTableName
					+ "success!");
			flag = true;
		} catch (Exception e) {
			log.printStackTrace(e);
			e.printStackTrace();
		} finally {
			if (null != conn) {
				conn.endTransaction(flag);
			}
		}
		return flag;
	}

	@Override
	public Boolean saveBakInfo(String bakTime, String tableName ,String backTableName) {
		DBConn conn = null;
		Session session = null;
		Connection connection = null;
		// PreparedStatement ps = null;
		boolean flag = false;
		PreparedStatement psTo = null;
		try {
			conn = new DBConn();
			session = conn.beginTransaction();
			connection = session.connection();
			String querySysParamSql = "insert into BAK_TABLE_INFO (bak_time ,table_Name ,bak_table_name)values('"+bakTime+"','"+tableName+"','"+backTableName+"')";
			psTo = connection.prepareStatement(querySysParamSql);
			psTo.executeQuery();
			flag = true;
		} catch (Exception e) {
			log.printStackTrace(e);
			e.printStackTrace();
		} finally {
			if (null != conn) {
				conn.endTransaction(flag);
			}
		}
		return flag;
	}

	@Override
	public Boolean rollBakTable(String tableName, String bakTableName) {
		DBConn conn = null;
		Session session = null;
		Connection connection = null;
		boolean flag = false;
		PreparedStatement psTo = null;
		try {
			conn = new DBConn();
			session = conn.beginTransaction();
			connection = session.connection();
			truncateTable(tableName);
			String querySysParamSql = "insert into "+tableName+" (SELECT * FROM "+bakTableName+" )";
			log.println(querySysParamSql);
			psTo = connection.prepareStatement(querySysParamSql);
			psTo.executeQuery();
			flag = true;
		} catch (Exception e) {
			log.printStackTrace(e);
			e.printStackTrace();
		} finally {
			if(psTo!= null){
				try {
					psTo.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (null != conn) {
				conn.endTransaction(flag);
			}
		}
		return flag;
	}

	
	@Override
	public Boolean truncateTable(String tableName) {
		DBConn conn = null;
		Session session = null;
		Connection connection = null;
		boolean flag = false;
		PreparedStatement tuncatePs = null;
		try {
			conn = new DBConn();
			session = conn.openSession();
			connection = session.connection();
			String truncateSql = "truncate table  "+tableName;
			tuncatePs = connection.prepareStatement(truncateSql);
			tuncatePs.executeQuery();
			flag = true;
		} catch (Exception e) {
			log.printStackTrace(e);
			e.printStackTrace();
		} finally {
			if(tuncatePs!= null){
				try {
					tuncatePs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (null != conn) {
				conn.closeSession();
			}
		}
		return flag;
	}
	
	@Override
	public Boolean dropTable(String tableName) {
		DBConn conn = null;
		Session session = null;
		Connection connection = null;
		boolean flag = false;
		PreparedStatement tuncatePs = null;
		try {
			conn = new DBConn();
			session = conn.beginTransaction();
			connection = session.connection();
			String truncateSql = "drop table "+tableName;
			tuncatePs = connection.prepareStatement(truncateSql);
			tuncatePs.executeQuery();
			flag = true;
		} catch (Exception e) {
			log.printStackTrace(e);
			e.printStackTrace();
		} finally {
			if(tuncatePs!= null){
				try {
					tuncatePs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (null != conn) {
				conn.closeSession();
			}
		}
		return flag;
	}
	

	@Override
	public List<BakTableInfoVo> getBakInfoList() {
		List<BakTableInfoVo> bakList = new ArrayList<BakTableInfoVo>();
		DBConn conn = null;
		Session session = null;
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			conn = new DBConn();
			session = conn.beginTransaction();
			connection = session.connection();
			String sql = "select * from  BAK_TABLE_INFO";
			ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			BakTableInfoVo vo = null;
			while (rs.next()) {
				vo  = new BakTableInfoVo();
				vo.setBakTime(rs.getString(1));
				vo.setTableName(rs.getString(2));
				vo.setBakTableName(rs.getString(3));
				bakList.add(vo);
			}
		} catch (Exception e) {
			log.printStackTrace(e);
			e.printStackTrace();
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (null != conn) {
				conn.endTransaction(true);
			}
		}
		if(bakList.size()== 0){
			bakList = null;
		}
		return bakList;
	}

	@Override
	public Boolean deleteBak(String bakTime, String tableName) {
		String backTableName  = "av"+"_"+bakTime; 
    	String sbackTableName = "avd"+"_"+bakTime;
		DBConn conn = null;
		Session session = null;
		Connection connection = null;
		boolean flag = false;
		PreparedStatement psTo = null;
		try {
			dropTable(backTableName);
			dropTable(sbackTableName);
			conn = new DBConn();
			session = conn.beginTransaction();
			connection = session.connection();
			String querySysParamSql = "delete from  BAK_TABLE_INFO  t where t.bak_time = '"+bakTime+"' and t.table_name = '"+tableName+"'";
			log.println(querySysParamSql);
			psTo = connection.prepareStatement(querySysParamSql);
			psTo.executeQuery();
			flag = true;
		} catch (Exception e) {
			log.printStackTrace(e);
			flag = false;
		} finally {
			if(psTo!= null){
				try {
					psTo.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (null != conn) {
				conn.endTransaction(flag);
			}
		}
		return flag;
		
	}
	
}
