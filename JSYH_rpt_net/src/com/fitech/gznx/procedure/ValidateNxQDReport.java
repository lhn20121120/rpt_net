package com.fitech.gznx.procedure;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;

/**
 * 新增清单式校验（农信部分）
 * 
 * @author Dennis Yee
 *
 */
public class ValidateNxQDReport {
	
	/***
	 * jdbc技术 无特殊oracle语法 不需要修改 
	 * 卞以刚 2011-12-23
	 */
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
			sql.append("select * from af_report where rep_id=").append(repInId);
			/***
			 * jdbc技术 无特殊oracle语法 不需要修改 
			 * 卞以刚 2011-12-23
			 */
			rs = stmt.executeQuery(sql.toString().toUpperCase());
			if (!rs.next()) {
				return false;
			}
			childrepId = rs.getString("TEMPLATE_ID");
			versionId = rs.getString("VERSION_ID");

			sql = new StringBuffer();
			/***
			 * jdbc技术 无特殊oracle语法 不需要修改 
			 * 卞以刚 2011-12-23
			 */
			sql.append("delete from af_qd_datavalidateinfo where rep_id=")
					.append(repInId);
			stmt.execute(sql.toString().toUpperCase());
			String tablename = "AF_QD_"+childrepId.toUpperCase().trim();
			sql = new StringBuffer();
			sql.append("select count(*),MIN(row_id) from ").append(tablename).append(" where rep_id=")
					.append(repInId);
			/***
			 * jdbc技术 无特殊oracle语法 不需要修改 
			 * 卞以刚 2011-12-23
			 */
			rs = stmt.executeQuery(sql.toString().toUpperCase());

			if (!rs.next()) {
				return false;
			}
			rowcount = rs.getInt(1);
			qdStartRow = rs.getInt(2);
			sql = new StringBuffer();
			sql.append(
							"select * from af_validateformula m where m.template_id='")
					.append(childrepId).append("' and m.version_id='").append(
							versionId).append("'");
			/***
			 * jdbc技术 无特殊oracle语法 不需要修改 
			 * 卞以刚 2011-12-23
			 */
			rs = stmt.executeQuery(sql.toString().toUpperCase());

			while (rs.next()) {
				cellFormuId = rs.getInt("FORMULA_ID");
				cellFormu = rs.getString("FORMULA_VALUE");

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
						double[] values = new double[items.length];

						for (int i = 0; i < items.length; i++) {
							String formula = items[i];
							formula = this.insertValue(formula, row, repInId);
							
							//添加保存值
							if(i == 0){
								source = formula;
							}else if(i == 1){
								target = formula;
							}else{
								continue;
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
					}

					if (!flag) {

						sql = new StringBuffer();
						sql
								.append("insert into af_qd_datavalidateinfo(rep_id,formula_id,result,validate_type_id,cause,")
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
			
			if (result) {
				sql = new StringBuffer();
				sql.append("update af_report set TBL_OUTER_VALIDATE_FLAG=1,TBL_INNER_VALIDATE_FLAG=")
						.append(valResult)		
						.append(" where rep_id=")
						.append(repInId);
				/***
				 * jdbc技术 无特殊oracle语法 不需要修改 
				 * 卞以刚 2011-12-23
				 */
				stmt.execute(sql.toString().toUpperCase());
			}

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
	
	
	private double getValue(int col, int row, Integer repId) throws Exception {

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

			double value = 0;

			StringBuffer sql = null;
			sql = new StringBuffer();
			sql.append("select template_id from af_report where rep_id=")
					.append(repId);
			rs = stmt.executeQuery(sql.toString().toUpperCase());
			String templateId = "";
			if(rs.next()){
				templateId = rs.getString("TEMPLATE_ID");
				templateId = "AF_QD_"+templateId;
			}
			sql = new StringBuffer();
			String colname = "COL"+col;
			sql.append("select ").append(colname).append(" from ").append(templateId).append(" where rep_id=")
					.append(repId).append("' and row_id=").append(row);
			rs = stmt.executeQuery(sql.toString().toUpperCase());
			if(rs.next()){
				try{
					value = Double.parseDouble(rs.getString(colname));
				}catch(Exception e){
					value = 0;
					e.printStackTrace();
				}
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

	private double resolveFormula(String formula) {

		if (formula == null || formula.indexOf("null") != -1
				|| formula.indexOf("NULL") != -1) {
			return 0;
		}

		double result = 0;

		double temp = 0;
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
				result = Double.parseDouble(formula);
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
