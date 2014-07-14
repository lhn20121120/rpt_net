package com.fitech.gznx.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.hibernate.Session;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.cbrc.smis.dao.DBConn;
import com.fitech.gznx.common.StringUtil;

/**
 * 清单式报表数据入库
 * 
 * @author Dennis Yee
 *
 */
public class QDDataDelegate {
	/**
	 * jdbc 需要修改 卞以刚 2011-12-21
	 * 将清单式报表数据入库--qd_report_in_info
	 * 
	 * @param childrepId
	 * @param versionId
	 * @param repId
	 * @param path
	 * @return
	 */
	public boolean qdIntoDB(String childrepId, String versionId, String repId,
			String path) {
		FileInputStream inStream = null;

		DBConn dbconn = null;
		Session session = null;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		boolean flag = false;

		try {
			// 读取excel文件
			inStream = new FileInputStream(path);
			HSSFWorkbook workbook = new HSSFWorkbook(inStream);
			// 取得清单式数据库信息
			String sql = "select * from qd_cellinfo where template_id='"
					+ childrepId + "' and version_id='" + versionId + "'";
			dbconn = new DBConn();
			session = dbconn.openSession();
			conn = session.connection();
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql.toUpperCase());

			if (!rs.next()) {
				return false;
			}

			HSSFSheet sheet = workbook.getSheetAt(0);

			int row = rs.getInt("START_ROW");
			// int col = rs.getInt("QD_COL_COUNT");

			int flagcol =  this.convertColStringToNum(rs.getString("FLAG_COL"));
			int startcol = this.convertColStringToNum(rs.getString("START_COL"));
			int endcol = this.convertColStringToNum(rs.getString("END_COL"));
			String endFlag = rs.getString("END_FLAG");

			int rownum = row -1;
			HSSFCell excelcell = null;
			// 删除无效数据
			sql = "delete from qd_report_in_info where rep_in_id=" + repId;
			stmt.addBatch(sql.toUpperCase());

			while (true) {
				if(sheet.getRow(rownum) == null || sheet.getRow(rownum).getCell((short) flagcol) == null){
					break;
				}
				excelcell = sheet.getRow(rownum).getCell((short) flagcol);
				if (this.getCellasString(excelcell).indexOf(endFlag) != -1) {
					break;
				}

				for (short i = (short) (startcol-1); i <= endcol; i++) {
					excelcell = sheet.getRow(rownum).getCell(i);
					String value = this.getCellasString(excelcell);
					if(StringUtil.isEmpty(value)){
						continue;
					}
					String colId = "";
					if ((i) > 25) {// 说明有"AB"这种列
						int intColId = i - 25;
						colId = "A" + (char) (intColId + 65 - 1);
					} else {
						colId = String.valueOf((char) (i + 65));
					}
					sql = "INSERT INTO QD_REPORT_IN_INFO(CELL_NAME,REP_IN_ID,ROW_ID,COL_ID,REPORT_VALUE) VALUES ('"
							+ colId
							+ (rownum + 1)
							+ "',"
							+ repId
							+ ","
							+ (rownum + 1)
							+ ",'"
							+ (i + 1)
							+ "','"
							+ value.trim()
							+ "')";
					stmt.addBatch(sql);
				}
				rownum++;
			}

			stmt.executeBatch();
			flag = true;

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
			return false;
		} finally {
			try {
				if (flag == true)
					conn.commit();
				else
					conn.rollback();
				conn.setAutoCommit(flag);
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
				if (session != null)
					session.close();
				if (dbconn != null)
					dbconn.closeSession();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(inStream != null){
				try {
					inStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}			
		}

	}

	/**
	 * jdbc技术 可能需要修改 卞以刚 2011-12-22
	 * 将清单式报表数据入库
	 * 
	 * @param childrepId
	 * @param versionId
	 * @param repId
	 * @param path
	 * @return
	 */
	public boolean qdIntoDBNx(String childrepId, String versionId, String repId,
			String path) {
		FileInputStream inStream = null;

		DBConn dbconn = null;
		Session session = null;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		boolean flag = false;

		try {
			// 读取excel文件
			inStream = new FileInputStream(path);
			HSSFWorkbook workbook = new HSSFWorkbook(inStream);
			// 取得清单式数据库信息
			StringBuffer sql = new StringBuffer();
			sql.append("select * from qd_cellinfo where template_id='"
					+ childrepId + "' and version_id='" + versionId + "'");
			dbconn = new DBConn();
			session = dbconn.openSession();
			conn = session.connection();
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			//此处转换成大写造成苏州银行补录模板不能正确载入
			//rs = stmt.executeQuery(sql.toString().toUpperCase());
			//2012-12-15修改 去除大小写
			rs = stmt.executeQuery(sql.toString());

			if (!rs.next()) {
				return false;
			}

			HSSFSheet sheet = workbook.getSheetAt(0);

			int row = rs.getInt("START_ROW") - 1;
//			int row = rs.getInt("START_ROW") ;//武汉行修改，原因：发现清单式报表载入后多出了标题栏
			// int col = rs.getInt("QD_COL_COUNT");

			int flagcol =  this.convertColStringToNum(rs.getString("FLAG_COL"));
			int startcol = this.convertColStringToNum(rs.getString("START_COL"));
			int endcol = this.convertColStringToNum(rs.getString("END_COL"));
			String endFlag = rs.getString("END_FLAG");

			int rownum = row;
			HSSFCell excelcell = null;
			String tablename = "AF_QD_"+childrepId.toUpperCase().trim();
			// 删除无效数据
			sql = new StringBuffer();
			sql.append("delete from "+tablename+" where rep_id=" + repId);
			stmt.addBatch(sql.toString().toUpperCase());
			StringBuffer insertsql = new StringBuffer();
			insertsql.append("INSERT INTO ").append(tablename).append("(REP_ID,ROW_ID");
			for(int i=(short)startcol+1;i<=endcol+1;i++){
				insertsql.append(",COL").append(i);
			}
			insertsql.append(") VALUES (");
			while (true) {
				
				if(sheet.getRow(rownum) == null ){
					break;
				}
				excelcell = sheet.getRow(rownum).getCell((short) flagcol);
				
				//读到endflag就停止
				if (this.getCellasString(excelcell).indexOf(endFlag) != -1) {
					break;
				}
				sql = new StringBuffer();
				sql.append(insertsql).append(repId).append(",").append(rownum+1);
				boolean insetflg = false;
				for (int i = startcol; i <= endcol; i++) {
					excelcell = sheet.getRow(rownum).getCell((short)i);
					String value = "";
					if (excelcell!=null&&excelcell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC && HSSFDateUtil.isCellDateFormatted(excelcell)) {
						Date date = excelcell.getDateCellValue();         
					    DateFormat formater = new SimpleDateFormat("yyyy-MM-dd");         
					    value=formater.format(date);
					}else{
						value = this.getCellasString(excelcell);
					}
					if(StringUtil.isEmpty(value)){
						sql.append(",null");
						continue;
					}					
					sql.append(",'").append(value.trim()).append("'");
					insetflg = true;
				}
				sql.append(")");
				if(insetflg)
					stmt.addBatch(sql.toString());
				rownum++;
			}

			stmt.executeBatch();
			flag = true;

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
			return false;
		} finally {
			try {
				if (flag == true)
					conn.commit();
				else
					conn.rollback();
				conn.setAutoCommit(flag);
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
				if (session != null)
					session.close();
				if (dbconn != null)
					dbconn.closeSession();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(inStream != null){
				try {
					inStream.close();
				} catch (IOException e) {					
					e.printStackTrace();
				}
			}
			
		}

	}
	
	private String getCellasString(HSSFCell cell) {
		if (cell == null)
			return "";

		String contents = null;
		if (cell.getCellType() == HSSFCell.CELL_TYPE_FORMULA
				|| cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			try {
				contents = String.valueOf(cell.getNumericCellValue());
				if (contents.equals("#DIV/0!") || contents.equals("NaN"))
					return "";
				if ("0.0".equals(contents))
					contents = "0.00";
			} catch (Exception e) {
				contents = "0.00";
			}

			boolean isPresentNumber = false;
			/* 千位分割符数据处理 */
			if (contents.indexOf(",") > -1)
				contents = contents.replaceAll(",", "");
			/* 百分数数据处理 */
			if (contents.indexOf("%") > -1
					&& contents.indexOf("%") == contents.length() - 1) {
				isPresentNumber = true;
				contents = parsePresentToDouble(contents);
			}
			/* 科学计数法数据处理 */
			if (contents.indexOf('E') > -1 || contents.indexOf('e') > -1) {
				try {
					// 如果是数字格式将其格式化为小数点后两位
					if (!isPresentNumber)
						contents = new java.text.DecimalFormat("##0.000000")
								.format(Double.parseDouble(contents))
								.toString();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(contents.equals("NaN")){
	        	   contents = "0.00";
			}
		} else if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
			contents = cell.getStringCellValue();
			int length = contents.length() - 1;
			if (!contents.equals("") && contents.charAt(length) == '%') {
				String ss = contents.substring(0, length);
				if (this.isFloat(ss)) {
					float cont = Float.parseFloat(ss);
					BigDecimal b1 = new BigDecimal(Float.toString(cont));
					BigDecimal b2 = new BigDecimal(new Integer(100).toString());
					cont = b1.divide(b2).floatValue();
					contents = String.valueOf(cont);
				}
			}
		} else if (cell.getCellType() == HSSFCell.CELL_TYPE_BLANK)
			contents = "";

		return contents;
	}

	public boolean isFloat(String input) {
		try {
			Float.parseFloat(input);
			return true;
		} catch (Exception exception) {
			return false;
		}
	}

	/**
	 * 将百分数转换位小数
	 * 
	 * @param presentNumber
	 * @return
	 */
	private String parsePresentToDouble(String presentNumber) {
		String contents = presentNumber
				.substring(0, presentNumber.indexOf("%"));
		String sign = "";
		if (contents.substring(0, 1).equals("+")
				|| contents.substring(0, 1).equals("-")) {
			sign = String.valueOf(contents.charAt(0));
			contents = contents.substring(1);
		}
		int dotIndex = contents.indexOf(".");

		if (dotIndex == -1)
			contents = "0." + contents;
		else if (dotIndex == 0)
			contents = "0.00" + contents.substring(1);
		else if (dotIndex == 1)
			contents = "0.0" + contents.substring(0, 1) + contents.substring(2);
		else if (dotIndex == 2)
			contents = "0." + contents.substring(0, 2) + contents.substring(3);
		else {
			StringBuffer contentsStr = new StringBuffer(contents);
			contentsStr.insert(dotIndex - 2, ".");
			contentsStr.deleteCharAt(dotIndex + 1);
			contents = contentsStr.toString();
		}
		return new java.text.DecimalFormat("##0.000000").format(
				Double.parseDouble(sign + contents)).toString();
	}
	
	/**
	 * 将列号转换为数字
	 * 
	 * @param ref
	 * @return
	 * 
	 */
	private int convertColStringToNum(String ref) {
		int retval = 0;
		int pos = 0;
		for (int k = ref.length() - 1; k > -1; k--) {
			char thechar = ref.charAt(k);
			if (pos == 0)
				retval += Character.getNumericValue(thechar) - 9;
			else
				retval += (Character.getNumericValue(thechar) - 9) * (pos * 26);
			pos++;
		}

		return retval - 1;
	}
}
