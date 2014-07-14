package com.fitech.gznx.procedure;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import java.util.List;
import java.util.Map;

import net.sf.hibernate.Session;
import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;

import com.cbrc.smis.dao.DBConn;
import com.fitech.gznx.common.StringUtil;
import com.fitech.gznx.po.AfQdValidateformula;
import com.fitech.gznx.po.AfReport;
import com.fitech.specval.util.ValMethod;

public class QDValidate {
	
	/***
	 * jdbc���� ������oracle�﷨ ����Ҫ�޸� 
	 * ���Ը� 2011-12-23
	 * @param repInId
	 * @return
	 */
	public static boolean bnValidate(Integer repInId) {

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
			String templateId = "";
			String versionId = "";

			int qdEndCol = 0;
			int qdStartRow = 0;
			int qdEndRow = 0;
			boolean flag = true;
			// ��ŵ�Ԫ��ֵ
			Map cellValue = null;
			int rowcount = 0;
			boolean result = true;

			sql = new StringBuffer();
			sql.append("select * from af_report where rep_id=").append(repInId);
			/***
			 * jdbc���� ������oracle�﷨ ����Ҫ�޸� 
			 * ���Ը� 2011-12-23
			 */
			rs = stmt.executeQuery(sql.toString().toUpperCase());
			if (!rs.next()) {
				return false;
			}
			templateId = rs.getString("TEMPLATE_ID");
			versionId = rs.getString("VERSION_ID");

			sql = new StringBuffer();
			sql.append("delete from af_qd_datavalidateinfo where rep_id=")
					.append(repInId);
			/***
			 * jdbc���� ������oracle�﷨ ����Ҫ�޸� 
			 * ���Ը� 2011-12-23
			 */
			stmt.execute(sql.toString().toUpperCase());
			String tablename = "AF_QD_"+templateId.toUpperCase().trim();
			sql = new StringBuffer();
			sql.append("select count(*),MIN(row_id),MAX(row_id) from ").append(tablename).append(" where rep_id=")
					.append(repInId);
			/***
			 * jdbc���� ������oracle�﷨ ����Ҫ�޸� 
			 * ���Ը� 2011-12-23
			 */
			rs = stmt.executeQuery(sql.toString().toUpperCase());

			if (!rs.next()) {
				return false;
			}
			rowcount = rs.getInt(1);
			qdStartRow = rs.getInt(2);
			qdEndRow = rs.getInt(3);
			sql = new StringBuffer();
			sql.append("select * from af_qd_validateformula m where m.template_id='")
					.append(templateId).append("' and m.version_id='").append(
							versionId).append("'");
			/***
			 * jdbc���� ������oracle�﷨ ����Ҫ�޸� 
			 * ���Ը� 2011-12-23
			 */
			rs = stmt.executeQuery(sql.toString().toUpperCase());
			List<AfQdValidateformula> qdformulList = new ArrayList();
			while (rs.next()) {

				AfQdValidateformula qdformul = new AfQdValidateformula();
				// ��ʽID
				qdformul.setFormulaId(rs.getInt("FORMULA_ID"));
				// У�鹫ʽ
				qdformul.setFormuValue(rs.getString("FORMU_VALUE"));				
				// ������Ϣ
				qdformul.setValidateMessage(rs.getString("VALIDATE_MESSAGE"));
				// �к�
				qdformul.setColNum(rs.getString("COL_NUM"));
				// ��ʽ����
				qdformul.setFormuType(rs.getInt("FORMU_TYPE"));
				
				qdformulList.add(qdformul);
				
			}
			List sqlExcuteList = new ArrayList();
			for(AfQdValidateformula qdformul:qdformulList){
				String comFormula = null;
				String cellFormu = qdformul.getFormuValue();
				if(StringUtil.isEmpty(cellFormu)){
					continue;
				} else {
					comFormula = cellFormu.toLowerCase();
				}
				int formulaType = qdformul.getFormuType();
				if(formulaType==1){
					sql = new StringBuffer();
					sql.append("select ").append(qdformul.getColNum()).append(",row_id from ").append(tablename).append(" where rep_id=")
					.append(repInId);
					/***
					 * jdbc���� ������oracle�﷨ ����Ҫ�޸� 
					 * ���Ը� 2011-12-23
					 */
					rs = stmt.executeQuery(sql.toString().toUpperCase());
					cellValue = new HashMap();
					while (rs.next()) {
						cellValue.put(rs.getInt(2), rs.getString(1));
					}
					Map regionMap = null;
					if(comFormula.contains("regionid")){
						regionMap = new HashMap();
						sql = new StringBuffer();
						/***
						 * jdbc���� ������oracle�﷨ ����Ҫ�޸� 
						 * ���Ը� 2011-12-23
						 */
						sql.append("select distinct region_id as re from v_region t");
						rs = stmt.executeQuery(sql.toString().toUpperCase());
						while(rs.next()){		
							String reginid = rs.getString(1);
							regionMap.put(reginid, reginid);
						}
					}
					for (int row = qdStartRow; row <= qdEndRow; row++) {	
						// �ж�У�麯��
						if(comFormula.contains("charlength")){
							// �ַ�����У��
							flag = ValMethod.CharLength(cellFormu, (String)cellValue.get(row));
						} else if(comFormula.contains("charcontain")){
							// �ַ�����������У��
							flag = ValMethod.CharContain(cellFormu, (String)cellValue.get(row));
						} else if(comFormula.contains("charfilter")){
							// �ַ�����У��
							flag = ValMethod.charFilter(cellFormu, (String)cellValue.get(row));
						} else if(comFormula.contains("charformat")){
							// �ַ���ʽУ��
							flag = ValMethod.charFormat((String)cellValue.get(row));
						} else if(comFormula.contains("numlength")){
							// ��ֵ����У��
							flag = ValMethod.numLength(cellFormu, (String)cellValue.get(row));
						} else if(comFormula.contains("numsize")){
							// ��ֵ��СУ��
							flag = ValMethod.numSize(cellFormu, (String)cellValue.get(row));
						} else if(comFormula.contains("charrepeat")){
							// �ַ��ظ���У��
							flag = ValMethod.charRepeat((String)cellValue.get(row));
						} else if(comFormula.contains("dateformat")){
							// ���ڸ�ʽУ��
							flag = ValMethod.dateFormat(cellFormu, (String)cellValue.get(row));
						} else if(comFormula.contains("regionid")){
							// ��������У��
							flag = ValMethod.regionId((String)cellValue.get(row),regionMap);														
						} else if(comFormula.contains("numfilter")){
							// ��λ��ֵ����У��
							flag = ValMethod.numFilter(cellFormu, (String)cellValue.get(row));
						} else {
							flag = true;
						}
						if (!flag) {
							result = false;
							sql = new StringBuffer();
							sql.append("insert into af_qd_datavalidateinfo(rep_id,formula_id,result,validate_type_id,cause) values (")
								.append(repInId).append(",").append(qdformul.getFormulaId()).append(",-1,1,'��").append(row).
								append("��").append(qdformul.getValidateMessage()).append("')");						
							sqlExcuteList.add(sql.toString());							
						}
					}
					cellValue.clear();
				} else if(formulaType==1){
					if(comFormula.contains("samecolrepeat")){
						// ͬ���ظ�У��
						result = sameColrepeat(qdformul,stmt,qdStartRow,qdEndRow,repInId);
					} else if(comFormula.contains("samerowrepeat")){
						// ͬ���ظ�У��
						result = sameRowrepeat(qdformul,stmt,qdStartRow,qdEndRow,repInId);
					} else if(comFormula.contains("morecolnumsize")){
						// ͬ�ж�����ֵ��СУ��
						result = moreColNumSize(qdformul,stmt,qdStartRow,qdEndRow,repInId);
					} else if(comFormula.contains("if")){
						result = ifThen(qdformul,stmt,qdStartRow,qdEndRow,repInId);
					}  else if(comFormula.contains("creditval")){
						// ͬ�ж�����ֵ��СУ��
						result = creditval(qdformul,stmt,qdStartRow,qdEndRow,repInId);
					} else {
						result = true;
					}

				}
				
				for(int i =0;i<sqlExcuteList.size();i++){
					stmt.addBatch((String) sqlExcuteList.get(i));
				}
				stmt.executeBatch();

			}

			int valResult =  result ? 1 : -1;
			
			if (result) {
				sql = new StringBuffer();
				sql.append("update af_report set TBL_INNER_VALIDATE_FLAG=")
						.append(valResult)		
						.append(" where rep_id=")
						.append(repInId);
				/***
				 * jdbc���� ������oracle�﷨ ����Ҫ�޸� 
				 * ���Ը� 2011-12-23
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
	
	public static boolean bjValidate(Integer repInId) {

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
			String templateId = "";
			String versionId = "";

			int qdStartRow = 0;
			int qdEndRow = 0;

			boolean result = true;

			AfReport reportIn = (AfReport) session.load(
					AfReport.class, repInId.longValue());
			
			if (reportIn == null) {
				return false;
			}
			templateId = reportIn.getTemplateId();
			versionId = reportIn.getVersionId();

			sql = new StringBuffer();
			sql.append("delete from af_qd_datavalidateinfo where rep_id=")
					.append(repInId);
			stmt.execute(sql.toString().toUpperCase());
			String tablename = "AF_QD_"+templateId.toUpperCase().trim();
			sql = new StringBuffer();
			sql.append("select count(*),MIN(row_id),MAX(row_id) from ").append(tablename).append(" where rep_id=")
					.append(repInId);
			rs = stmt.executeQuery(sql.toString().toUpperCase());

			if (!rs.next()) {
				return false;
			}

			qdStartRow = rs.getInt(2);
			qdEndRow = rs.getInt(3);
			sql = new StringBuffer();
			sql.append("select * from af_qd_validateformula m where m.template_id='")
					.append(templateId).append("' and m.version_id='").append(
							versionId).append("' and m.formu_type=3");
			rs = stmt.executeQuery(sql.toString().toUpperCase());
			List<AfQdValidateformula> qdformulList = new ArrayList();
			while (rs.next()) {

				AfQdValidateformula qdformul = new AfQdValidateformula();
				// ��ʽID
				qdformul.setFormulaId(rs.getInt("FORMULA_ID"));
				// У�鹫ʽ
				qdformul.setFormuValue(rs.getString("FORMU_VALUE"));				
				// ������Ϣ
				qdformul.setValidateMessage(rs.getString("VALIDATE_MESSAGE"));
				// �к�
				qdformul.setColNum(rs.getString("COL_NUM"));
				// ��ʽ����
				qdformul.setFormuType(rs.getInt("FORMU_TYPE"));
				
				qdformulList.add(qdformul);
				
			}

			for(AfQdValidateformula qdformul:qdformulList){
				String comFormula = null;
				String cellFormu = qdformul.getFormuValue();
				if(StringUtil.isEmpty(cellFormu)){
					continue;
				} else {
					comFormula = cellFormu.toLowerCase();
				}
				int formulaType = qdformul.getFormuType();
				if(formulaType==3){
					// �ж�У�麯��
					if(comFormula.contains("bancth")){
						// ����ܷ�ƽ��У��
						result = bancth(qdformul,stmt,qdStartRow,qdEndRow,reportIn);
					}
				}
			}

			int valResult =  result ? 1 : -1;
			
			if (result) {
				sql = new StringBuffer();
				sql.append("update af_report set TBL_OUTER_VALIDATE_FLAG=")
						.append(valResult)		
						.append(" where rep_id=")
						.append(repInId);
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
	

	private static boolean bancth(AfQdValidateformula qdformul, Statement stmt,
			int qdStartRow, int qdEndRow, AfReport reportIn) {

		boolean result = true;
		int repInId = reportIn.getRepId().intValue();
		String tablename = "AF_QD_"+qdformul.getTemplateId().toUpperCase().trim();
		
		
		String formula = qdformul.getFormuValue();
		
		if(formula.indexOf(",")==-1){
			result =  false;
		}else{
			try {
				formula = fetchValue(formula);
				String[] formuArr = formula.split(",");
				int arrLen = formuArr.length;
				if(arrLen<5){
					return false;					
				}
				StringBuffer sql = new StringBuffer();
				sql.append("select rep_id from af_report where template_id='").append(formuArr[2]).
					append("' and REP_FREQ_ID=").append(reportIn.getRepFreqId()).append(" and CUR_ID=").
					append(reportIn.getCurId()).append(" and ORG_ID=").append(reportIn.getOrgId()).
					append(" and YEAR=").append(reportIn.getYear()).append(" and TERM=").append(reportIn.getTerm()).
					append(" and TIMES=").append(reportIn.getTimes());
				ResultSet rs = stmt.executeQuery(sql.toString().toUpperCase());
				if (!rs.next()) {
					return false;
				}
				int torepID = rs.getInt(1);
				// ȡ�ö�Ӧ�ı���
				String toTempalteName = "AF_QD_"+formuArr[2];
				int col1 = convertColStringToNum(formuArr[0])+1;
				int col2 = convertColStringToNum(formuArr[1])+1;
				int col3 = convertColStringToNum(formuArr[3])+1;
				int col4 = convertColStringToNum(formuArr[4])+1;
				
				String colNameA1 = "COL"+col1;
				String colNameA2 = "COL"+col2;
				String colNameB1 = "COL"+col3;
				String colNameB2 = "COL"+col4;
				
				sql = new StringBuffer();
				sql.append("select distinct ").append(colNameA1).append(" from ").append(tablename).append(" where rep_id=")
				.append(repInId).append(" order by row_id");
				rs = null;			
				rs = stmt.executeQuery(sql.toString().toUpperCase());
				Map colNameAMap = new HashMap();
				while (rs.next()) {					
					colNameAMap.put(rs.getString(1), rs.getString(1));
				}
				sql = new StringBuffer();
				sql.append("select distinct t.").append(colNameA1).append(" from ").append(tablename).append(" t where t.rep_id=")
				.append(repInId).append(" and t.").append(colNameA2).append("=(SELECT SUM(d.").append(colNameB2).append(") FROM ").
				append(toTempalteName).append(" d where d.rep_id=").append(torepID).append(" and d.").append(colNameB1).append("=t.").
				append(colNameA1).append(")  order by row_id");
				rs = null;			
				rs = stmt.executeQuery(sql.toString().toUpperCase());
				List sqlExcuteList = new ArrayList();
				while (rs.next()) {
					String cellPID = rs.getString(1);
					if(colNameAMap.containsKey(cellPID)){
						colNameAMap.remove(cellPID);
					}
				}
				rs.close();
				rs = null;
				for (Iterator it = colNameAMap.values().iterator(); it.hasNext();) {
					result = false;
					sql = new StringBuffer();
					sql.append("insert into af_qd_datavalidateinfo(rep_id,formula_id,result,validate_type_id,cause) values (")
						.append(repInId).append(",").append(qdformul.getFormulaId()).append(",-1,1,'").append((String)it.next())
						.append(qdformul.getValidateMessage()).append("')");						
					stmt.addBatch(sql.toString());					
				}
				stmt.executeBatch();				
				
			} catch (SQLException e) {
				result = false;
				e.printStackTrace();
			}
		}
		return result;
	
	}

	/**
	 * ͬ���ظ�У��	SameColrepeat([str])
	 * @param qdformul
	 * @param stmt
	 * @param qdStartRow
	 * @param qdEndRow
	 * @param repInId
	 * @return
	 */
	private static boolean sameColrepeat(AfQdValidateformula qdformul,
			Statement stmt, int qdStartRow, int qdEndRow,int repInId) {
		boolean result = true;
		String tablename = "AF_QD_"+qdformul.getTemplateId().toUpperCase().trim();
		String formulaValue = qdformul.getFormuValue();
		StringBuffer sql = new StringBuffer();
		sql.append("select ").append(qdformul.getColNum()).append(",row_id from ").append(tablename).append(" where rep_id=")
		.append(repInId).append(" order by row_id");
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery(sql.toString().toUpperCase());
			Map cellValue = new HashMap();
			List sqlExcuteList = new ArrayList();
			while (rs.next()) {
				String celldata = rs.getString(1);
				if(StringUtil.isEmpty(celldata)){
					continue;
				} else {
					celldata = celldata.trim();
				}
				if(cellValue.containsKey(celldata)){
					int row = rs.getInt(2);
					result = false;
					sql = new StringBuffer();
					sql.append("insert into af_qd_datavalidateinfo(rep_id,formula_id,result,validate_type_id,cause) values (")
						.append(repInId).append(",").append(qdformul.getFormulaId()).append(",-1,1,'��").append(row).
						append("��").append(qdformul.getValidateMessage()).append("')");						
					sqlExcuteList.add(sql.toString());		
				} else {
					cellValue.put(celldata, celldata);
				}
				
			}
			rs.close();
			rs = null;
			if(sqlExcuteList != null && sqlExcuteList.size()>0){
				for(int i =0;i<sqlExcuteList.size();i++){
					stmt.addBatch((String) sqlExcuteList.get(i));
				}
				stmt.executeBatch();
			}
		} catch (SQLException e) {
			result = false;
			e.printStackTrace();
		}
		
		return result;
	}
	


	/**
	 * ͬ���ظ�У��	SameRowrepeat��[str],[str],<str>, ...��<br>
	 * @param qdformul
	 * @param stmt
	 * @param qdStartRow
	 * @param qdEndRow
	 * @param repInId
	 * @return
	 */
	private static boolean sameRowrepeat(AfQdValidateformula qdformul,
			Statement stmt, int qdStartRow, int qdEndRow,int repInId) {
		boolean result = true;
		String tablename = "AF_QD_"+qdformul.getTemplateId().toUpperCase().trim();
		
		
		String formula = qdformul.getFormuValue();
		
		if(formula.indexOf(",")==-1){
			result =  false;
		}else{
			try {
				formula = fetchValue(formula);
				String[] formuArr = formula.split(",");
				int arrLen = formuArr.length;
				StringBuffer sql = new StringBuffer();
				sql.append("select row_id");
				for(int i=0;i<arrLen;i++){
					int col = convertColStringToNum(formuArr[i]);
					sql.append(",COL").append(col+1);
				}
				sql.append(" from ").append(tablename).append(" where rep_id=")
				.append(repInId).append(" order by row_id");
				ResultSet rs = null;			
				rs = stmt.executeQuery(sql.toString().toUpperCase());

				List sqlExcuteList = new ArrayList();
				while (rs.next()) {
					String orgData = null;
					for(int i=2;i<arrLen+2;i++){
						if(i==2){
							orgData =  rs.getString(2);
							if(orgData==null){
								orgData = "";
							}
							orgData = orgData.trim();
						} else {
							String celldata = rs.getString(i);
							if(celldata==null){
								celldata = "";
							}
							celldata = celldata.trim();
							if(celldata.equals(orgData)){
								int row = rs.getInt(1);
								result = false;
								sql = new StringBuffer();
								sql.append("insert into af_qd_datavalidateinfo(rep_id,formula_id,result,validate_type_id,cause) values (")
									.append(repInId).append(",").append(qdformul.getFormulaId()).append(",-1,1,'��").append(row).
									append("��").append(qdformul.getValidateMessage()).append("')");						
								sqlExcuteList.add(sql.toString());		
							}							
						}
					}
				}
				rs.close();
				rs = null;
				if(sqlExcuteList != null && sqlExcuteList.size()>0){
					for(int i =0;i<sqlExcuteList.size();i++){
						stmt.addBatch((String) sqlExcuteList.get(i));
					}
					stmt.executeBatch();
				}
			} catch (SQLException e) {
				result = false;
				e.printStackTrace();
			}
		}
		return result;
	}

	
	/**
	 * ���к�ת��Ϊ����
	 * 
	 * @param ref
	 * @return
	 * 
	 */
	private static int convertColStringToNum(String ref) {
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
	/**
	 * ͬ�ж�����ֵ��СУ��	MoreColNumSize([str],[str],<str>,��,[con]) <br>
	 * @param qdformul
	 * @param stmt
	 * @param qdStartRow
	 * @param qdEndRow
	 * @param repInId
	 * @return
	 */
	private static boolean moreColNumSize(AfQdValidateformula qdformul,
			Statement stmt, int qdStartRow, int qdEndRow,int repInId) {
		boolean result = true;
		String tablename = "AF_QD_"+qdformul.getTemplateId().toUpperCase().trim();
		
		
		String formula = qdformul.getFormuValue();
		
		if(formula.indexOf(",")==-1){
			result =  false;
		}else{
			try {
				formula = fetchValue(formula);
				String[] formuArr = formula.split(",");
				int arrLen = formuArr.length;
				// �ж�����
				String role = formuArr[arrLen-1];
				if(StringUtil.isEmpty(role)){
					return false;
				}
				Evaluator evaluator = new Evaluator();
				
				StringBuffer sql = new StringBuffer();
				sql.append("select row_id");
				for(int i=0;i<arrLen-1;i++){
					int col = convertColStringToNum(formuArr[i]);
					sql.append(",COL").append(col+1);
				}
				sql.append(" from ").append(tablename).append(" where rep_id=")
				.append(repInId).append(" order by row_id");
				ResultSet rs = null;			
				rs = stmt.executeQuery(sql.toString().toUpperCase());

				List sqlExcuteList = new ArrayList();
				while (rs.next()) {	
					for(int i=2;i<arrLen+1;i++){
						String celldata = rs.getString(i);
						if(celldata==null){
							celldata = "";
						}
						celldata = celldata.trim();
						if(evaluator.evaluate(celldata+role).equals("0.0")){
							int row = rs.getInt(1);
							result = false;
							sql = new StringBuffer();
							sql.append("insert into af_qd_datavalidateinfo(rep_id,formula_id,result,validate_type_id,cause) values (")
								.append(repInId).append(",").append(qdformul.getFormulaId()).append(",-1,1,'��").append(row).
								append("��").append(qdformul.getValidateMessage()).append("')");						
							sqlExcuteList.add(sql.toString());
						}
					}
				}
				rs.close();
				rs = null;
				if(sqlExcuteList != null && sqlExcuteList.size()>0){
					for(int i =0;i<sqlExcuteList.size();i++){
						stmt.addBatch((String) sqlExcuteList.get(i));
					}
					stmt.executeBatch();
				}
			} catch (SQLException e) {
				result = false;
				e.printStackTrace();
			} catch (EvaluationException e) {
				result = false;
				e.printStackTrace();
			}
		}
		return result;
	}

	private static String fetchValue(String str){
		//String str = "charlen(3-23,Y)";
		String str1 = null;
		int startIndex = str.indexOf("(");
		int endIndex = str.lastIndexOf(")");
		str1 = str.substring(startIndex+1, endIndex);
		return str1;
	}

	private  static double getValue(int col, int row, Integer repId) throws Exception {

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
	 /**
	  * ֤��У��
	  * @param value ���ֵ֤
	  * @param type ���֤����
	  * @param regionid ��������
	  * @return
	  */
	public static boolean creditval(AfQdValidateformula qdformul,
			Statement stmt, int qdStartRow, int qdEndRow,int repInId){
		boolean result = true;
		String tablename = "AF_QD_"+qdformul.getTemplateId().toUpperCase().trim();

		String formula = qdformul.getFormuValue();
		

		try {
			formula = fetchValue(formula);
			String[] formuArr = formula.split(",");
			int arrLen = formuArr.length;
			StringBuffer sql = null;
			ResultSet rs = null;
			Map regionMap = new HashMap();
			sql = new StringBuffer();
			sql.append("select distinct substr(region_id,1,2) as re from v_region t");
			rs = stmt.executeQuery(sql.toString().toUpperCase());
			while(rs.next()){		
				String reginid = rs.getString(1);
				regionMap.put(reginid, reginid);
			}
			rs.close();
			// ȡ�ö�Ӧ������				
			sql = new StringBuffer();
			sql.append("select row_id");
			for(int i=0;i<arrLen;i++){
				int col = convertColStringToNum(formuArr[i]);
				sql.append(",COL").append(col+1);
			}
			sql.append(" from ").append(tablename).append(" where rep_id=")
			.append(repInId).append(" order by row_id");
						
			rs = stmt.executeQuery(sql.toString().toUpperCase());

			List sqlExcuteList = new ArrayList();
			while (rs.next()) {
				if(arrLen==1){
					String value = rs.getString(2);
					result = senfenValidate(value,regionMap);
				} else {
					char type = rs.getString(2).trim().charAt(0);
					String value = rs.getString(3);
					int row = rs.getInt(1);
					
					if(type=='A'){						
						result = senfenValidate(value,regionMap);
					}else if(type=='K'){
						if(value==null || value.trim().equals("")){//K����Ϊ��,
							result= true;
						}else{
							return false;
						}
					}else if(type>='B'&& type<='I'){
						if(value==null || value.trim().equals("")){//���valueֵΪ��
							return false;
						}else{
							//�ж��Ƿ���ȫ�ظ�
							char[] c = value.toCharArray();
							char temp = ' ';
							int count = 0;
							for(int i=1;i<c.length;i++){
								 temp = c[i-1];
								 if(temp==c[i]){//n���ظ��ı���n-i��
									 count++;									 
								 }
							}
							count = count + 1;//��ʾ�ж��ٸ�Ԫ���ظ�

							if(count==value.length()){//ȫ��Ԫ���ظ�
								result = false;	
							}
						}
					}else{
						return false;
					}
				}
			}
		} catch (SQLException e) {
			result = false;
			e.printStackTrace();
		}

		return result;
	}
	
	 /**
	  * ���֤У��
	  * @param value ���ֵ֤
	  * @param type ���֤����
	  * @param regionid ��������
	  * @return
	  */
	public static boolean senfenValidate(String value,Map regionMap){
		boolean result = false;

		if(value==null || value.trim().equals("")){//���valueֵΪ��
			return false;
		}else{
			char[] c = value.toCharArray();
			if(value.length()==10 || value.length()==11){//����Ϊ10λ��11λ
				//Ϊ�ж��Ƿ�����ַ�
				for(int i=0;i<value.length();i++){
					char t = c[i];
					if((t>='a'&& t<='z')||(t>='A'&&t<='Z')||(t>='0'&&t<='9')||
						t=='('||t==')'||t=='#'||t=='%'||t=='*'||t=='-'||t=='_'||
						t=='<'||t=='>'||t=='&'||t=='.'||t=='/'||t=='\\'){		
						result = true;
					}else{
						return false;
					}
				}
				//�ж��Ƿ���ȫ�ظ�
				char temp = ' ';
				int count = 0;
				for(int i=1;i<c.length;i++){
					 temp = c[i-1];
					 if(temp==c[i]){//n���ظ��ı���n-i��
						 count++;
						 
					 }
				}
				count = count + 1;//��ʾ�ж��ٸ�Ԫ���ظ�

				if(count==value.length()){//ȫ��Ԫ���ظ�
					return false;	
				}else{
					result = true;
				}
			}else if(value.length()==15){//����Ϊ15λ
				//Ϊ�ж��Ƿ�ֻ������
				for(int i=0;i<value.length();i++){
					char t = c[i];
					if(t>='0'&&t<='9'){		
						result = true;
					}else{
						return false;
					}
				}
				//�ж�ǰ2λ�Ƿ�����������
				String v = value.substring(0, 2);
				if(!regionMap.containsKey(v)){
					return false;
				}
				
			}else if(value.length()==18){//����Ϊ18λ
				//Ϊ�ж�ǰ17λ�Ƿ�ֻ������
				for(int i=0;i<17;i++){
					char t = c[i];
					if(t>='0'&&t<='9'){		
						result = true;
					}else{
						return false;
					}
				}
				//�ж����һλ
				if((value.charAt(17)>='0'&&value.charAt(17)<='9')||value.charAt(17)=='X'){
					result = true;
				}else{
					return false;
				}
				//�ж����¹淶
				String date = value.substring(6, 14);
				boolean isFomat = dateFormat(date);
				if(!isFomat){
					return false;
				}else{
					result = true;
				}
				//�ж�ǰ2λ�Ƿ�����������
				String v = value.substring(0, 2);
				
				if(!regionMap.containsKey(v)){
					return false;
				}
				
				//��18λУ��
				boolean resultvalid = valid18Bit(value);
				if(!resultvalid){
					return false;
				}else{
					result= true;
				}
			}else{//�������Ȳ�����
				return false;
			}
		}
		return result;
	}
	
	/**
	 * ���ڸ�ʽУ��
	 */
	private static boolean dateFormat(String date){
		 try {
			 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");//�Զ���ĸ�ʽ
            dateFormat.setLenient(false);
            dateFormat.parse(date);
            return true;
		 } catch (Exception e) {
       	return false;
		 }
	}
	/**
	 * ��18λУ��
	 */
	private static boolean valid18Bit(String value){
		char[] ch = value.toCharArray();
		int[] in =new int[18];
		for(int i=0;i<17;i++){
			 in[i] =Integer.parseInt(String.valueOf(ch[i])) ;
		}

		//17λ���ֱ���Բ�ͬ��ϵ��,��� |7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2 
		int sum = in[0]*7 + in[1]*9 + in[2]*10 + in[3]*5 +in[4]*8 +
					in[5]*4 + in[6]*2+ in[7]*1 + in[8]*6 + in[9]*3 +
					in[10]*7 + in[11]*9 + in[12]*10 + in[13]*5 +in[14]*8 +
					in[15]*4 + in[16]*2;


		//�üӳ����ͳ���11���������Ƕ��� |1 0 X 9 8 7 6 5 4 3 2
		int yu = sum % 11;

		Map map = new HashMap();
		map.put(0, '1');
		map.put(1, '0');
		map.put(2, 'X');
		map.put(3, '9');
		map.put(4, '8');
		map.put(5, '7');
		map.put(6, '6');
		map.put(7, '5');
		map.put(8, '4');
		map.put(9, '3');
		map.put(10,'2');
		
		if(map.get(yu).toString().charAt(0)==value.charAt(17)){
			return true;
			
		}else{
			return false;
			
		}
		
	}
	/**
	 * 	ͬ�ж����߼��ж�У��	IF([str],[con])THEN {[fun]}<br>
	 * 	[str] �кţ��硰A��
	 *  [con] �Ƚ��ַ����硰B��
	 *  <br>������A�е��ڡ�A��ʱ��B�а����֤����У�� IF{A,"A"} THEN{B,creditval()}
	 * @param formula ��ʽ
	 * @param cellMap ��Ԫ��map
	 * @param cowCount ������ (table af_qd_cellinfo��ά��)
	 * @return
	 */
	public static boolean ifThen(AfQdValidateformula qdformul,
			Statement stmt, int qdStartRow, int qdEndRow,int repInId){
		
		boolean result = true;
		String tablename = "AF_QD_"+qdformul.getTemplateId().toUpperCase().trim();

		String res = ""; //��¼�ڼ��в�����
		String formula = qdformul.getFormuValue();
		if(formula == null || formula.trim().equals(""))
			return result;
		try{
			formula = formula.split("if")[1];
			
			
			String spCond[] = formula.trim().split("then");
			
			//û������ֱ�ӷ���
			if(spCond.length!=2) 
				return false;
			spCond[0] = fetchFormula(spCond[0], "{", "}");
			spCond[1] = fetchFormula(spCond[1], "{", "}");
			
			String ifs[] = spCond[0].split(",");
			if(ifs.length!=2) 
				return false;
			
			String thens[] = spCond[1].split(",");
			if(ifs.length<2) 
				return false;
	
			Evaluator evaluator = new Evaluator();	
			
			StringBuffer sql = new StringBuffer();
			sql.append("select row_id");

			int col1 = convertColStringToNum(ifs[0]);
			sql.append(",COL").append(col1+1);
			int col2 = convertColStringToNum(thens[0]);
			sql.append(",COL").append(col2+1);

			sql.append(" from ").append(tablename).append(" where rep_id=")
			.append(repInId).append(" order by row_id");
			ResultSet rs = null;			
			rs = stmt.executeQuery(sql.toString().toUpperCase());

			List sqlExcuteList = new ArrayList();
			while (rs.next()) {			
				int row = rs.getInt(1);
				String ifValuecol = rs.getString(2);
				String thenValuecol = rs.getString(3);
				
				//�ǱȽ���
				if(isCompare(ifs[1])){
					//�����������+1
					if (!evaluator.evaluate(ifValuecol+ifs[1]).equals("0.0")) {
						for(int i =1;i<thens.length;i++){
							boolean flag = chooseVal(thens[i], thenValuecol);
							if(!flag){
								result = false;
								sql = new StringBuffer();
								sql.append("insert into af_qd_datavalidateinfo(rep_id,formula_id,result,validate_type_id,cause) values (")
									.append(repInId).append(",").append(qdformul.getFormulaId()).append(",-1,1,'��").append(row).
									append("��").append(qdformul.getValidateMessage()).append("')");						
								sqlExcuteList.add(sql.toString());	
							}
						}
					}
				}else if(ifs[1].equals(ifValuecol)){
					for(int i =1;i<thens.length;i++){
						boolean flag = chooseVal(thens[i], thenValuecol);
						if(!flag){
							result = false;
							sql = new StringBuffer();
							sql.append("insert into af_qd_datavalidateinfo(rep_id,formula_id,result,validate_type_id,cause) values (")
								.append(repInId).append(",").append(qdformul.getFormulaId()).append(",-1,1,'��").append(row).
								append("��").append(qdformul.getValidateMessage()).append("')");						
							sqlExcuteList.add(sql.toString());	
						}
					}
					
				}
				
			}
			rs.close();
			rs = null;
			if(sqlExcuteList != null && sqlExcuteList.size()>0){
				for(int i =0;i<sqlExcuteList.size();i++){
					stmt.addBatch((String) sqlExcuteList.get(i));
				}
				stmt.executeBatch();
			}
		} catch (Exception e){
			e.printStackTrace();
			return false;
		}
		
		
		if(res.trim().equals("")) 
			result = true;
		else
			result = false;
		
		return result;
	}
	/**
	 * ��춽�����ʽ��̖�е��ַ���
	 * @param formula ��ʽ
	 * @param leftAmbit ���
	 * @param rightAmbit �ҽ�
	 * @return
	 */
	private static String fetchFormula(String formula,String leftAmbit,String rightAmbit){

		int startIndex = formula.indexOf(leftAmbit);
		int endIndex = formula.lastIndexOf(rightAmbit);
		formula = formula.substring(startIndex+1, endIndex);
		return formula;
	}
	/**
	 * �Ƿ��ǱȽ�
	 * @param str 
	 * @return ��Ϊtrue
	 */
	private  static  boolean isCompare(String str){

		if(str==null || str.trim().equals(""))
			return false;
			
		if(str.contains(">") || str.contains(">") || str.contains("="))
			return true;
		
		
		return false;
	}
	
	/**
	 * ����formula��ѡ��
	 * @param formula У�鹫ʽ
	 * @param value	У��ֵ
	 * @param type ֤������
	 * @param regionidlist ��������
	 */
	public static boolean chooseVal(String formula,String value){
		
		boolean result = false;
		
		if(formula == null || formula.trim().equals(""))
			return result;
		else{
			if(formula.contains("charlength")){
				result = ValMethod.CharLength(formula, value);
			}else if(formula.contains("charcontain")){
				result = ValMethod.CharContain(formula, value);
			}else if(formula.contains("charfilter")){
				result = ValMethod.charFilter(formula, value);
			}else if(formula.contains("charformat")){
				result = ValMethod.charFormat(value);
			}else if(formula.contains("numlength")){
				result = ValMethod.numLength(formula, value);
			}else if(formula.contains("numsize")){
				result = ValMethod.numSize(formula, value);
			}else if(formula.contains("charrepeat")){
				result = ValMethod.charRepeat(value);
			}else if(formula.contains("dateformat")){
				result = ValMethod.dateFormat(formula, value);
			}else{
				result = false;
			}
		}
		
		return result;
	}

}
