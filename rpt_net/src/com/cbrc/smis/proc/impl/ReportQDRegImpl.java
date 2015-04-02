package com.cbrc.smis.proc.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.cbrc.smis.proc.po.ReportIn;

public class ReportQDRegImpl {

	/**
	 * 清单式报表的正则表达式的校验
	 * 
	 * @param conn Connection 数据库连接
	 * @param reportIn ReportIn 实际数据报表对象
	 * @param colFormu String 正则表达式
	 * @param table String 数据表名称
	 * @return boolean 校验正确，返回true;否则，返回false
	 * @exception Exception
	 */
	public static boolean isRegexRight(Connection conn, 
			ReportIn reportIn,
			String colFormu, 
			String table) throws Exception {
		if(conn==null)
			return false;
		int colLeft = colFormu.indexOf(Expression.SPLIT_SYMBOL_LEFT_MID_KUOHU);
		int colRight = colFormu.indexOf(Expression.SPLIT_SYMBOL_RIGHT_MID_HUOHU);
		int regLeft = colFormu.indexOf(Expression.SPLIT_SYMBOL_LEFT_MID_KUOHU,colRight + 1);
		int regRight = colFormu.lastIndexOf(Expression.SPLIT_SYMBOL_RIGHT_MID_HUOHU);
		
		String colName = colFormu.substring(colLeft + 1, colRight);
		String reg = colFormu.substring(regLeft + 1, regRight);
		
		List results = getResults(conn, colName, table, reportIn.getRepInId());
		
		for (int i = 0; i < results.size(); i++) {
			String result = (String) results.get(i);
			if (result == null)	result = "";
			result = result.trim();
			if (Pattern.matches(reg, result) == false) return false;
		}

		return true;
	}

	/**
	 * 得到清单式报表某列的值集
	 * 
	 * @param conn Connection 数据库连接
	 * @param colName String 列的名称
	 * @param table String 数据表名称
	 * @param repInId Integer 实际数据报表的Id
	 * @return List
	 * @exception Exception
	 */

	private static List getResults(Connection conn, String colName,
			String table, Integer repInId) throws Exception {
		List results = new ArrayList();
		String type = "";
		if (colName.indexOf("_") >= 0) {
			type = colName.substring(colName.indexOf("_") + 1);
			colName = colName.substring(0, colName.indexOf("_"));
		}
		String sql = "select " + colName.toUpperCase() + " from " + table.toUpperCase()
				+ " where rep_in_id=".toUpperCase() + repInId.intValue()
				+ " and replace(col1,' ','')<>'合计'".toUpperCase()
				+ (type.equals("") ? "" : " and type='".toUpperCase() + type + "'");
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					results.add(rs.getString(colName));
				}
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		}

		return results;
	}

	/**
	 * 清单式报表某列求和的校验
	 * 
	 * @param conn Connection 数据库连接
	 * @param reportIn ReportIn 实际数据报表对象
	 * @param colFormu String 求和校验表达式
	 * @param table String 数据表名称
	 * @param pointNumber Integer 小数点后保留几位
	 * @return boolean 校验正确，返回true;否则，返回false
	 * @exception Exception
	 */

	public static boolean isSumRight(Connection conn, ReportIn reportIn,
			String colFormu, String table, Integer pointNumber)
			throws Exception {
		if(conn==null)
			return false;
		int posS = 0, posE = 0;

		posS = colFormu.indexOf(Expression.SPLIT_SYMBOL_LEFT_MID_KUOHU);
		posE = colFormu.indexOf(Expression.SPLIT_SYMBOL_RIGHT_MID_HUOHU);
		String cellName = null;
		String cellValue = null;
		while (posS >= 0 && posE >= 0) {
			cellName = colFormu.substring(posS + 1, posE);
			if (cellName.startsWith("∑")) {
				cellValue = getSumValue(conn, cellName.substring(1),table,reportIn.getRepInId());
			} else if (cellName.startsWith("T.")) {
				cellValue = getTotalCellValue(conn, table, reportIn	.getRepInId().intValue(), cellName.substring(2));
			} else {
				return false;
			}
			cellValue = Expression.format(cellValue);
			colFormu = colFormu.substring(0, posS)
					+ (cellValue.indexOf("-") == 0 ? "("
							+ String.valueOf(cellValue) + ")" : String.valueOf(cellValue)) + colFormu.substring(posE + 1);
			posS = colFormu.indexOf(Expression.SPLIT_SYMBOL_LEFT_MID_KUOHU);
			posE = colFormu.indexOf(Expression.SPLIT_SYMBOL_RIGHT_MID_HUOHU);
		}
		
		return Expression.isEquals(conn, colFormu, pointNumber);
	}

	/**
	 * 清单式报表某列的求和
	 * 
	 * @param conn Connection 数据库连接
	 * @param colName String 列名
	 * @param table String 数据表名称
	 * @param repInId Integer 实际数据报表的Id
	 * @return String 返回和值
	 * @exception Exception
	 */

	private static String getSumValue(Connection conn, String colName,
			String table, Integer repInId) throws Exception {
		String sumValue = "0";
		String type = "";
		if (colName.indexOf("_") >= 0) {
			type = colName.substring(colName.indexOf("_") + 1);
			colName = colName.substring(0, colName.indexOf("_"));
		}
		String sql = "select sum(double(" + colName + ")) from " + table
				+ " where rep_in_id=" + repInId.intValue()
				+ " and replace(col1,' ','')<>'合计'"
				+ (type.equals("") ? "" : " and type='" + type + "'");
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs != null && rs.next()) {
				sumValue = Double.toString(rs.getDouble(1));
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		}
		return sumValue;
	}

	/**
	 * 得到清单式报表合计栏的值
	 * 
	 * @param conn Connection 数据库连接
	 * @param colName String 列名
	 * @param table String 数据表名称
	 * @param repInId Integer 实际数据报表的Id
	 * @return String 返回合计栏的值
	 * @exception Exception
	 */

	public static String getTotalCellValue(Connection conn, String table,
			int repInId, String colName) throws Exception {
		String res = "0";

		if (conn == null || table == null || colName == null)
			return res;

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String type = "";
		if (colName.indexOf("_") >= 0) {
			type = colName.substring(colName.indexOf("_") + 1);
			colName = colName.substring(0, colName.indexOf("_"));
		}
		try {
			String sql = "select " + colName .toUpperCase()+ " from " + table.toUpperCase()
					+ " where rep_in_id=" .toUpperCase()+ repInId
					+ " and replace(col1,' ','')='合计'".toUpperCase()
					+ (type.equals("") ? "" : " and type='".toUpperCase() + type + "'");

			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs != null && rs.next()) {
				res = rs.getString(colName);
			}
		} catch (SQLException sqle) {
			throw new Exception(sqle.getMessage());
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		}

		if (res == null || res.trim().equals(""))
			res = "0";

		return res;
	}
	
}
