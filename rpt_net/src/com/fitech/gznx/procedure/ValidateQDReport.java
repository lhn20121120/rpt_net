package com.fitech.gznx.procedure;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import net.sf.hibernate.Session;
import com.cbrc.smis.dao.DBConn;

/**使用jdbc 可能不需要修改  卞以刚 2011-12-21**/
/**
 * 清单式校验（银监会）
 * 
 * @author Dennis Yee
 *
 */
public class ValidateQDReport {

	public boolean bnValidate(Integer repInId) {

		DBConn dbconn = null;
		Session session = null;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		ResultSet rs_in = null;

		try {
			dbconn = new DBConn();
			session = dbconn.openSession();
			conn = session.connection();
			stmt = conn.createStatement();

			StringBuffer sql = null;
			String childrepId = "";
			String versionId = "";
			int year = 0;
			int month = 0;
			int cellFormuId = 0;
			String cellFormu = "";
			String sign = "";
			String[] items = null;
			int qdEndCol = 0;
			int qdStartRow = 0;
			boolean flag = true;
			int seqno = 0;
			int rowcount = 0;
			boolean result = true;

			sql = new StringBuffer();
			sql.append("select * from report_in where rep_in_id=").append(
					repInId);
			rs = stmt.executeQuery(sql.toString().toUpperCase());
			if (!rs.next()) {
				return false;
			}
			childrepId = rs.getString("CHILD_REP_ID");
			versionId = rs.getString("VERSION_ID");
			year = rs.getInt("YEAR");
			month = rs.getInt("TERM");

			sql = new StringBuffer();
			sql.append("select * from qd_cellinfo where TEMPLATE_ID='")
					.append(childrepId).append("' and VERSION_ID='").append(
							versionId).append("'");
			rs = stmt.executeQuery(sql.toString().toUpperCase());
			if (!rs.next()) {
				return false;
			}
			qdEndCol = this.convertColStringToNum(rs.getString("END_COL"));
			qdStartRow = rs.getInt("START_ROW");

			sql = new StringBuffer();
			sql.append("delete from qd_data_validate_info where rep_in_id=")
					.append(repInId);
			stmt.execute(sql.toString().toUpperCase());

			sql = new StringBuffer();
			sql.append(
					"select count(*) from qd_report_in_info where rep_in_id=")
					.append(repInId).append(" and col_id='").append(qdEndCol)
					.append("'");
			rs = stmt.executeQuery(sql.toString().toUpperCase());

			if (!rs.next()) {
				return false;
			}
			rowcount = rs.getInt(1);

			sql = new StringBuffer();
			sql
					.append(
							"select * from m_cell_formu t where t.cell_formu_id in (select m.cell_formu_id from m_cell_to_formu m where m.child_rep_id='")
					.append(childrepId).append("' and m.version_id='").append(
							versionId).append("')");
			rs = stmt.executeQuery(sql.toString().toUpperCase());

			while (rs.next()) {
				cellFormuId = rs.getInt("CELL_FORMU_ID");
				cellFormu = rs.getString("CELL_FORMU");

				if (cellFormu.indexOf("<=") != -1) {
					sign = "<=";
				} else if (cellFormu.indexOf(">=") != -1) {
					sign = ">=";
				} else if (cellFormu.indexOf("<") != -1) {
					sign = "<";
				} else if (cellFormu.indexOf(">") != -1) {
					sign = ">";
				} else if (cellFormu.indexOf("=") != -1) {
					sign = "=";
				} else {
					return false;
				}

				items = cellFormu.split(sign);
				
				//添加保存值
				String source = "";
				String target = "";

				for (int row = qdStartRow; row < qdStartRow + rowcount; row++) {
					
					try {
						float[] values = new float[items.length];

						for (int i = 0; i < items.length; i++) {
							String formula = items[i];
							formula = this.insertValue(formula, row, repInId);
							
							//添加保存值
							if(i == 0){
								source = formula;
							}else if(i == 1){
								target = formula;
							}
							
							values[i] = this.resolveFormula(formula);
						}

						if (sign.equals("<=")) {
							if (!(values[0] <= values[1] && values[1] <= values[2])) {
								flag = false;
								result = false;
							} else {
								flag = true;
							}
						} else if (sign.equals(">=")) {
							if (!(values[0] >= values[1] && values[1] >= values[2])) {
								flag = false;
								result = false;
							} else {
								flag = true;
							}
						} else if (sign.equals("<")) {
							if (!(values[0] < values[1] && values[1] < values[2])) {
								flag = false;
								result = false;
							} else {
								flag = true;
							}
						} else if (sign.equals(">")) {
							if (!(values[0] > values[1] && values[1] > values[2])) {
								flag = false;
								result = false;
							} else {
								flag = true;
							}
						}
					} catch (Exception e) {
						flag = false;
						result = false;
						e.printStackTrace();
					}

					if (!flag) {

						sql = new StringBuffer();
						sql
								.append("insert into qd_data_validate_info(")
								.append("rep_in_id,cell_formu_id,result,validate_type_id,cause,")
								.append("source_value,target_value) values (")
								.append(repInId).append(",")
								.append(cellFormuId).append(",-1,1,'第")
								.append(row).append("行','")
								.append(source).append("','")
								.append(target).append("')");
						//stmt.execute(sql.toString().toUpperCase());
						stmt.addBatch(sql.toString());
					}
				}
				
				stmt.executeBatch();
			}

			
			int valResult =  result ? 1 : -1;
			
				sql = new StringBuffer();
				sql.append("update report_in set TBL_OUTER_VALIDATE_FLAG=1,TBL_INNER_VALIDATE_FLAG=")
						.append(valResult)
						.append(" where rep_in_id=")
						.append(repInId);
				stmt.execute(sql.toString().toUpperCase());

			conn.commit();

			return result;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (rs_in != null)
					rs_in.close();
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
		}
	}

	private String insertValue(String formula, int row, Integer repId)
			throws NumberFormatException, Exception {

		if (formula.indexOf("[") == -1) {
			return formula;
		} else {
			formula = formula.substring(0, formula.indexOf("["))
					+ this.getValue(Integer.parseInt(formula.substring(formula
							.indexOf("[") + 4, formula.indexOf("]"))), row,
							repId)
					+ formula.substring(formula.indexOf("]") + 1);
			return this.insertValue(formula, row, repId);
		}
	}

	private float getValue(int col, int row, Integer repId) throws Exception {

		DBConn dbconn = null;
		Session session = null;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {

			dbconn = new DBConn();
			session = dbconn.openSession();
			conn = session.connection();
			stmt = conn.createStatement();

			float value = 0;

			StringBuffer sql = null;
			sql = new StringBuffer();
			sql.append("select * from qd_report_in_info where rep_in_id=")
					.append(repId).append(" and col_id='").append(col).append(
							"' and row_id=").append(row);
			rs = stmt.executeQuery(sql.toString().toUpperCase());
			if(rs.next()){
				value = Float.parseFloat(rs.getString("REPORT_VALUE"));
			} 			

			return value;

		} catch (Exception e) {
			throw e;
		} finally {
			try {
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
		}
	}

	private float resolveFormula(String formula) {

		if (formula == null || formula.indexOf("null") != -1
				|| formula.indexOf("NULL") != -1) {
			return 0;
		}

		float result = 0;

		float temp = 0;
		String formula_temp;
		String temp_s = "";

		boolean isZheNian = false;

		if (formula.indexOf("(") != -1) {

			formula_temp = formula.substring(formula.indexOf("(") + 1);
			formula = formula.substring(0, formula.indexOf("("));

			if (formula_temp.indexOf("(") == -1) {

				temp = resolveFormula(formula_temp.substring(0, formula_temp
						.indexOf(")")));

				formula_temp = String.valueOf(temp)
						+ formula_temp.substring(formula_temp.indexOf(")") + 1);
				formula = formula + formula_temp;
			} else if (formula_temp.substring(0, formula_temp.indexOf("("))
					.indexOf(")") != -1) {
				temp = resolveFormula(formula_temp.substring(0, formula_temp
						.indexOf(")")));

				formula_temp = String.valueOf(temp)
						+ formula_temp.substring(formula_temp.indexOf(")") + 1);
				formula = formula
						+ formula_temp.substring(0, formula_temp.indexOf("("))
						+ String.valueOf(resolveFormula(formula_temp
								.substring(formula_temp.indexOf("("))));
			} else {
				String formula_in = "";
				boolean sign = true;
				int sign_right = 1;
				while (sign) {
					if (formula_temp.indexOf("(") != -1) {
						if (formula_temp.indexOf(")") < formula_temp
								.indexOf("(")
								&& sign_right == 1) {
							formula_in = formula_temp.substring(0, formula_temp
									.indexOf(")"));
							formula_temp = formula_temp.substring(formula_temp
									.indexOf(")") + 1);
							sign = false;
						} else if (formula_temp.indexOf(")") < formula_temp
								.indexOf("(")
								&& sign_right != 1) {
							formula_in += formula_temp.substring(0,
									formula_temp.indexOf(")") + 1);
							formula_temp = formula_temp.substring(formula_temp
									.indexOf(")") + 1);
							sign_right--;
						} else {
							formula_in += formula_temp.substring(0,
									formula_temp.indexOf("(") + 1);
							formula_temp = formula_temp.substring(formula_temp
									.indexOf("(") + 1);
							sign_right++;
						}
					} else {
						if (sign_right != 1) {
							formula_in += formula_temp.substring(0,
									formula_temp.indexOf(")") + 1);
							formula_temp = formula_temp.substring(formula_temp
									.indexOf(")") + 1);
							sign_right--;
						} else {
							formula_in += formula_temp.substring(0,
									formula_temp.indexOf(")"));
							formula_temp = formula_temp.substring(formula_temp
									.indexOf(")") + 1);
							sign = false;
						}
					}
				}
				temp = resolveFormula(formula_in);

				formula = formula + String.valueOf(temp) + formula_temp;
			}
		}

		formula = formula.replaceAll("\\+-", "\\-");
		formula = formula.replaceAll("--", "+");
		formula = formula.replaceAll("\\*-", "\\*\\$");
		formula = formula.replaceAll("/-", "/\\$");

		if (formula.indexOf("+") != -1) {
			String[] items = formula.split("\\+");
			result = 0;
			for (int i = 0; i < items.length; i++) {
				result += resolveFormula(items[i]);
			}
		} else if (formula.indexOf("-") != -1) {
			String[] items = formula.split("-");

			result = resolveFormula(items[0]);

			for (int i = 1; i < items.length; i++) {

				result -= resolveFormula(items[i]);

			}
		} else if (formula.indexOf("/") != -1) {
			String[] items = formula.split("/");

			result = resolveFormula(items[0]);

			for (int i = 1; i < items.length; i++) {

				temp = resolveFormula(items[i]);

				result /= temp;
			}
		} else if (formula.indexOf("*") != -1) {
			String[] items = formula.split("\\*");
			result = 1;
			for (int i = 0; i < items.length; i++) {

				result *= resolveFormula(items[i]);

			}
		} else {
			formula = formula.replaceAll("\\$", "-");
			if (formula.trim().equals("")) {
				result = 0;
			} else {
				result = Float.parseFloat(formula);
			}
		}

		return result;
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
