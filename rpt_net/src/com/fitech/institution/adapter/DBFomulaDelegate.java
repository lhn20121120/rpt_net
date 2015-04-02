package com.fitech.institution.adapter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.smis.common.DateUtil;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.util.FitechException;
import com.fitech.gznx.common.StringUtil;
import com.fitech.gznx.po.AfValidateformula;
import com.fitech.institution.po.AfValidateFormulaStandard;
import com.fitech.institution.po.BingbiaoRelationVo;

public class DBFomulaDelegate {
	private static FitechException log = new FitechException(
			DBFomulaDelegate.class);

	public static String queryColNum(String[] params, String templateId,
			String versionId) {
		DBConn conn = null;
		Session session = null;
		String colNum = null;
		List list  = new ArrayList();
		try {
			conn = new DBConn();
			session = conn.openSession();

			String hql = "select t.colNum from AfCellinfo t ,AfTemplateFreqRelation a ,MRepFreq m where "
					+ "t.templateId=:templateId "
					+ "and t.cellPid=:pid "
					+ "and t.colName=:colName "
					+ "and t.versionId=:versionId "
					+ "and m.repFreqName=:repFreqName "
					+ "and t.templateId = a.id.templateId "
					+ "and t.versionId  = a.id.versionId "
					+ "and a.id.repFreqId = m.repFreqId";

			Query query = session.createQuery(hql);
			query.setParameter("templateId", templateId);
			query.setParameter("pid", params[0]);
			// System.out.println(params[0]);
			query.setParameter("colName", params[3] + params[1]);
			query.setParameter("versionId", versionId);
			query.setParameter("repFreqName", params[4]);
			list = query.list();
			if(list.size() == 0){
				colNum = null;
			}else {
				if(list.size()>1){
					colNum = templateId+"_"+versionId+":"+params[0]+"存在垃圾数据，多个单元格指向同一指标！请检查af_cellInfo表信息！";
//					System.out.println(colNum);
				}else{
					colNum = (String) list.get(0);
				}
			}
		}catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return colNum;
	}

	public static List<String> getAllTargetByTemplateId(String templateId) {
		DBConn conn = null;
		Session session = null;
		List<String> list = new ArrayList<String>();
		try {
			conn = new DBConn();
			session = conn.openSession();
			String sql = "SELECT a.cell_pid ,a.version_id ,a.* FROM af_cellinfo a where a.template_id='"
					+ templateId + "'";
			// String sql =
			// "select a.template_id,max(a.version_id) version_id from af_cellinfo a where a.cell_pid='"
			// + params[0] + "' group by a.template_id";
			log.println(sql);
			ResultSet rs = session.connection().createStatement()
					.executeQuery(sql);
			while ((rs.next())) {
				list.add(rs.getString("cell_pid"));
			}
		} catch (Exception e) {
			log.printStackTrace(e);
			if (conn != null)
				conn.closeSession();
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return list;
	}

	public static Map<String, List<String>> queryAllTemplateToHql(
			Map<String, List<String>> map) {
		Map ts = new HashMap();

		DBConn conn = null;
		Session session = null;
		try {
			conn = new DBConn();
			session = conn.openSession();
			for (Iterator localIterator = map.keySet().iterator(); localIterator
					.hasNext();) {
				String templateId = (String) localIterator.next();
				String versionId = queryVersion(templateId);
				String hql = "from AfValidateformula a where a.templateId='"
						+ templateId + "' and a.versionId='" + versionId + "'";
				List fls = new ArrayList();
				List formulas = session.createQuery(hql).list();
				for (int i = 0; i < formulas.size(); ++i)
					fls.add(((AfValidateformula) formulas.get(i))
							.getFormulaValue());

				ts.put(templateId + "_" + versionId, fls);
			}
		} catch (Exception e) {
			log.printStackTrace(e);

			if (conn != null)
				conn.closeSession();
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return ts;
	}

	public static Map<String, List<String>> queryAllTemplateToSql(
			Map<String, List<String>> map) {
		Map ts = new HashMap();
		DBConn conn = null;
		Session session = null;
		try {
			conn = new DBConn();
			session = conn.openSession();
			for (Iterator localIterator = map.keySet().iterator(); localIterator
					.hasNext();) {
				String templateId = (String) localIterator.next();
				String versionId = queryVersion(templateId);
				String sql = "select formula_value from af_validateformula_bak a where a.template_id='"
						+ templateId + "' and a.version_id='" + versionId + "'";
				List fls = new ArrayList();
				ResultSet rs = session.connection().createStatement()
						.executeQuery(sql);
				while (rs.next())
					fls.add(rs.getString("formula_value"));

				ts.put(templateId + "_" + versionId, fls);
			}
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return ts;
	}
/**
 * 查询模版是否有效并发布
 * @param templateId
 * @param versionId
 * @return
 */
	public static boolean queryTemplateIsValid(String templateId,
			String versionId) {
		DBConn conn = null;
		Session session = null;
		boolean result = true;
		try {
			conn = new DBConn();
			session = conn.openSession();

			String hql = "select count(*) from AfTemplate a where a.id.templateId=:templateId  and a.id.versionId=:versionId and :sysdate between a.startDate and a.endDate and a.usingFlag = 1";

			Query query = session.createQuery(hql);
			String sysdate = DateUtil.toSimpleDateFormat(new Date(),
					"yyyy-MM-dd");
			query.setParameter("templateId", templateId);
			query.setParameter("versionId", versionId);
			query.setParameter("sysdate", sysdate);
			int count = ((Integer) query.uniqueResult()).intValue();
			if (count <= 0)
				result = false;
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return result;
	}

	public static String queryFreqId(String freqName) {
		DBConn conn = null;
		Session session = null;
		String freqId = null;
		try {
			conn = new DBConn();
			session = conn.openSession();

			String sql = "select m.rep_freq_id from m_rep_freq m where m.rep_freq_name='"
					+ freqName + "'";
			ResultSet rs = session.connection().createStatement()
					.executeQuery(sql);
			if (!(rs.next()))
				freqId = rs.getString("rep_freq_id");
		} catch (Exception e) {
			log.printStackTrace(e);

			if (conn != null)
				conn.closeSession();
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return freqId;
	}

	public static String queryVersion(String templateId) {
		DBConn conn = null;
		Session session = null;
		String versionId = null;
		try {
			conn = new DBConn();
			session = conn.openSession();

			String sql = "select max(a.version_id) as version_id   from af_template a where a.template_id='"
					+ templateId + "' group by a.template_id";
			ResultSet rs = session.connection().createStatement()
					.executeQuery(sql);
			if (rs.next())
				versionId = rs.getString("version_id");
		} catch (Exception e) {
			log.printStackTrace(e);

			if (conn != null)
				conn.closeSession();
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return versionId;
	}

	public static void dropTempTable() {
		DBConn conn = null;
		Session session = null;
		boolean result = false;
		String sql = "";
		try {
			conn = new DBConn();
			session = conn.beginTransaction();
			try {
				sql = "drop table af_validateformula_bak";

				session.connection().createStatement().executeUpdate(sql);
			} catch (Exception e) {
			}
			result = true;
		} catch (Exception e) {
			log.printStackTrace(e);

			if (conn != null)
				return;
			conn.endTransaction(result);
		} finally {
			if (conn != null)
				conn.endTransaction(result);
		}
	}

	public static void createTempTable() {
		DBConn conn = null;
		Session session = null;
		boolean result = false;
		try {
			conn = new DBConn();
			session = conn.beginTransaction();

			String sql = "create table AF_VALIDATEFORMULA_BAK (  formula_id       INTEGER not null,  formula_value    VARCHAR2(2000), formula_name     VARCHAR2(4000), validate_type_id INTEGER not null, template_id      VARCHAR2(10) not null, version_id       VARCHAR2(10) not null, cell_id          INTEGER not null)";

			session.connection().createStatement().executeUpdate(sql);
			result = true;
		} catch (Exception e) {
			log.printStackTrace(e);

			if (conn != null)
				return;
			conn.endTransaction(result);
		} finally {
			if (conn != null)
				conn.endTransaction(result);
		}
	}

	public static boolean saveFormulaInfoToTemTable(String templateId,
			List<String> formulas) {
		DBConn conn = null;
		Session session = null;
		boolean result = false;
		try {
			conn = new DBConn();
			session = conn.beginTransaction();
			Statement st = session.connection().createStatement();
			StringBuffer buffer = new StringBuffer();
			String versionId = queryVersion(templateId);
			for (Iterator localIterator = formulas.iterator(); localIterator
					.hasNext();) {
				String formula = (String) localIterator.next();
				Integer validate_type_id = Integer.valueOf((formula
						.indexOf("_") > 0) ? 2 : 1);
				buffer.append("insert into af_validateformula_bak values(seq_af_validateformula.nextval,'"
						+ formula
						+ "','"
						+ formula
						+ "',"
						+ validate_type_id
						+ ",'" + templateId + "','" + versionId + "',1" + ")");
				st.addBatch(buffer.toString());
				buffer.setLength(0);
			}
			st.executeBatch();
			result = true;
		} catch (Exception e) {
			log.printStackTrace(e);

			if (conn != null)
				conn.endTransaction(result);
		} finally {
			if (conn != null)
				conn.endTransaction(result);
		}
		return result;
	}

	public static void saveAfValidateFormula(AfValidateformula formula) {
		DBConn conn = null;
		Session session = null;
		boolean result = false;
		try {
			conn = new DBConn();
			session = conn.beginTransaction();

			session.save(formula);
			result = true;
		} catch (Exception e) {
			log.printStackTrace(e);

			if (conn != null)
				return;
			conn.endTransaction(result);
		} finally {
			if (conn != null)
				conn.endTransaction(result);
		}
	}

	public static List<String> queryFormulaIdsByReportFlag(String reportFlag) {
		DBConn conn = null;
		Session session = null;
		List formulaIds = new ArrayList();
		try {
			conn = new DBConn();
			session = conn.openSession();

			String sql = "select formula_id from AF_PRE_FORMULA where report_flag='"
					+ reportFlag + "'";
			ResultSet rs = session.connection().createStatement()
					.executeQuery(sql);
			while (rs.next())
				formulaIds.add(rs.getString("formula_id"));
		} catch (Exception e) {
			log.printStackTrace(e);

			if (conn != null)
				conn.closeSession();
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return formulaIds;
	}

	public static boolean saveAfPreFormula(String reportFlag, String formulaId) {
		DBConn conn = null;
		Session session = null;
		boolean result = false;
		try {
			conn = new DBConn();
			session = conn.beginTransaction();
			Statement st = session.connection().createStatement();
			String sql = "insert into AF_PRE_FORMULA values('" + reportFlag
					+ "','" + formulaId + "')";
			st.executeUpdate(sql);
			result = true;
		} catch (Exception e) {
			log.printStackTrace(e);

			if (conn != null)
				conn.endTransaction(result);
		} finally {
			if (conn != null)
				conn.endTransaction(result);
		}
		return result;
	}

	public static boolean delAfPreFormula(String reportFlag, String formulaIds) {
		DBConn conn = null;
		Session session = null;
		boolean result = false;
		if (StringUtil.isEmpty(formulaIds))
			return result;
		try {
			conn = new DBConn();
			session = conn.beginTransaction();
			Statement st = session.connection().createStatement();
			String sql = "delete from AF_PRE_FORMULA where report_flag='"
					+ reportFlag + "' and formula_id in(" + formulaIds + ")";
			st.executeUpdate(sql);
			result = true;
		} catch (Exception e) {
			log.printStackTrace(e);

			if (conn != null)
				conn.endTransaction(result);
		} finally {
			if (conn != null)
				conn.endTransaction(result);
		}
		return result;
	}

	public static void delStanardProductFormulas(String reportFlag) {
		DBConn conn = null;
		Session session = null;
		boolean result = false;
		try {
			conn = new DBConn();
			session = conn.beginTransaction();
			Statement st = session.connection().createStatement();
			StringBuffer buffer = new StringBuffer();
			buffer.append("select FORMULA_ID from AF_PRE_FORMULA where REPORT_FLAG='"
					+ reportFlag + "'");
			ResultSet rs = st.executeQuery(buffer.toString());
			buffer.setLength(0);
			while (rs.next()) {
				buffer.append("delete from AF_PRE_FORMULA where FORMULA_ID='"
						+ rs.getString("FORMULA_ID") + "'");
				st.addBatch(buffer.toString());
				buffer.setLength(0);
				buffer.append("delete from af_validateformula a where a.formula_id="
						+ Long.valueOf(rs.getString("FORMULA_ID")));
				st.addBatch(buffer.toString());
				buffer.setLength(0);
			}
			if (buffer.length() > 0)
				st.executeBatch();

			result = true;
		} catch (Exception e) {
			log.printStackTrace(e);

			if (conn != null)
				return;
			conn.endTransaction(result);
		} finally {
			if (conn != null)
				conn.endTransaction(result);
		}
	}

	public static boolean updateInstitution(String templateId,
			List<String> formulas, Map<String, String> viewMap) {
		DBConn conn = null;
		Session session = null;
		boolean result = false;
		Statement st = null;
		try {
			conn = new DBConn();
			session = conn.beginTransaction();
			String versionId = queryVersion(templateId);
			if (versionId == null) {
				log.println(templateId + "模版不存在！");
				return result;
			}
			// List<AfValidateformula> insertList = new
			// ArrayList<AfValidateformula>();
			// List<AfValidateformula> updataList = new
			// ArrayList<AfValidateformula>();
			// List<AfValidateFormulaStandard> sdandardList = new
			// ArrayList<AfValidateFormulaStandard>();
			List<String> isusedFormulaValue = new ArrayList<String>();
			List<AfValidateformula> isusedList = AFValidateFormulaDelegate
					.findAllById(templateId, versionId);
			if (isusedList != null && isusedList.size() != 0) {
				isusedFormulaValue = AFValidateFormulaDelegate
						.getFormulasById(isusedList);
			}
			Connection conection = session.connection();
			st = conection.createStatement();
			String sql = "update af_validateformula e set e.version_id = '9999' where e.formula_id in("
					+ " SELECT a.formula_id FROM af_validateformula_standard a where "
					+ " a.template_id = '"
					+ templateId
					+ "'"
					+ " and a.version_id = '"
					+ versionId
					+ "')"
					+ " and e.template_id = '"
					+ templateId
					+ "'"
					+ " and e.version_id = '" + versionId + "'";
			st.executeUpdate(sql);
			sql = "update af_validateformula_standard e set e.version_id = '9999' where "
					+ " e.template_id = '"
					+ templateId
					+ "'"
					+ " and e.version_id = '" + versionId + "'";
			st.executeUpdate(sql);
			// conection.commit();
			for (Iterator localIterator = formulas.iterator(); localIterator
					.hasNext();) {
				AfValidateformula isertAfValidateformula = new AfValidateformula();
				AfValidateFormulaStandard isertAfValidateformulaSastandard = new AfValidateFormulaStandard();
				String formula = (String) localIterator.next();
				if (isusedFormulaValue.contains(formula)) {
					for (Iterator iterator2 = isusedList.iterator(); iterator2
							.hasNext();) {
						AfValidateformula afValidateformula = (AfValidateformula) iterator2
								.next();
						if (afValidateformula.getFormulaValue().equals(formula)) {
							// isusedList.remove(afValidateformula);
							afValidateformula.setFormulaName(viewMap
									.get(formula));
							// formulas.remove(formula);
							session.update(afValidateformula);
							// updataList.add(afValidateformula);
							org.apache.commons.beanutils.BeanUtils.copyProperties(isertAfValidateformulaSastandard ,afValidateformula);
							AfValidateFormulaStandard afs = (AfValidateFormulaStandard) session
									.get(isertAfValidateformulaSastandard
											.getClass(),
											isertAfValidateformulaSastandard
													.getFormulaId());
							if (afs != null) {
								afs.setVersionId(isertAfValidateformulaSastandard
										.getVersionId());
								afs.setFormulaName(viewMap.get(formula));
								session.update(afs);
							} else {
								session.save(isertAfValidateformulaSastandard);
							}
							break;
						}
					}
				} else {
					isertAfValidateformula.setFormulaValue(formula);
					isertAfValidateformula.setFormulaName(viewMap.get(formula));
					isertAfValidateformula.setTemplateId(templateId);
					isertAfValidateformula.setVersionId(versionId);
					Integer validate_type_id = Integer.valueOf((formula
							.indexOf("_") > 0) ? 2 : 1);
					isertAfValidateformula.setValidateTypeId(Long
							.parseLong(validate_type_id + ""));
					isertAfValidateformula.setCellId(1L);
					// insertList.add(isertAfValidateformula);
					session.save(isertAfValidateformula);
					org.apache.commons.beanutils.BeanUtils.copyProperties(isertAfValidateformulaSastandard ,isertAfValidateformula);
					session.save(isertAfValidateformulaSastandard);
				}
			}
			result = true;
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			if (st != null) {
				try {
					st.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null)
				conn.endTransaction(result);
		}
		return result;
	}



	public static List<String> findFreqListByTemplateId(String templateId) {
		String versionId = queryVersion(templateId);
		DBConn conn = null;
		Session session = null;
		List<String> freqList = new ArrayList<String>();
		ResultSet rs = null;
		try {
			conn = new DBConn();
			session = conn.openSession();
			String sql = "SELECT m.rep_freq_name FROM af_template_freq_relation a , m_rep_freq m where"
					+ " a.template_id ='"
					+ templateId
					+ "' and a.version_id = '"
					+ versionId
					+ "' and  a.rep_freq_id = m.rep_freq_id";
			rs = session.connection().createStatement().executeQuery(sql);
			while (rs.next())
				freqList.add(rs.getString("rep_freq_name"));
		} catch (Exception e) {
			log.printStackTrace(e);

		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				rs = null;
			}
			if (conn != null)
				conn.closeSession();
			conn = null;
		}
		return freqList;
	}

	public static List<String> getAllStandardTemplateId() {
		DBConn conn = null;
		Session session = null;
		List<String> list = new ArrayList<String>();
		try {
			conn = new DBConn();
			session = conn.openSession();
			String sql = "SELECT distinct (t.template_id ) as compId FROM  AF_TEMPLATE_BINGBIAO_RELATION  t ";
			log.println(sql);
			ResultSet rs = session.connection().createStatement()
					.executeQuery(sql);
			while ((rs.next())) {
				list.add(rs.getString("compId"));
			}
		} catch (Exception e) {
			log.printStackTrace(e);
			if (conn != null)
				conn.closeSession();
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return list;
	}

	public static List<String> getAllTemplateId() {
		DBConn conn = null;
		Session session = null;
		List<String> list = new ArrayList<String>();
		try {
			conn = new DBConn();
			session = conn.openSession();
			String sql = "SELECT distinct (t.report_name ) as compId FROM  AF_TEMPLATE_BINGBIAO_RELATION  t ";
			log.println(sql);
			ResultSet rs = session.connection().createStatement()
					.executeQuery(sql);
			while ((rs.next())) {
				list.add(rs.getString("compId"));
			}
		} catch (Exception e) {
			log.printStackTrace(e);
			if (conn != null)
				conn.closeSession();
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return list;
	}

	
	/**
	 * 
	 * 根据标准模版号和 币种查询出实际reportName
	 * 
	 * @param curr
	 * @param templateId
	 * @return
	 */
	public static String getTemplateId(String curr, String templateId) {
		DBConn conn = null;
		Session session = null;
		String reportName = "";
		try {
			conn = new DBConn();
			session = conn.openSession();
			String sql = "SELECT t.report_Name as compId FROM  AF_TEMPLATE_BINGBIAO_RELATION  t where t.curr_id = '"
					+ curr.trim()
					+ "' and "
					+ "t.template_id='"
					+ templateId
					+ "'";
			log.println(sql);
			ResultSet rs = session.connection().createStatement()
					.executeQuery(sql);
			if ((rs.next())) {
				reportName = rs.getString("compId");
			}
		} catch (Exception e) {
			log.printStackTrace(e);
			if (conn != null)
				conn.closeSession();
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return reportName;
	}

	public static List<BingbiaoRelationVo> getAllRelationByTemplateId(
			String templateId) {
		DBConn conn = null;
		Session session = null;
		List<BingbiaoRelationVo> list = new ArrayList<BingbiaoRelationVo>();
		try {
			conn = new DBConn();
			session = conn.openSession();
			String sql = "SELECT t.col_Name_standard  ,t.col_name   ,t.report_Name ,t.curr_id FROM  AF_TEMPLATE_BINGBIAO_RELATION  t where t.template_id = '"
					+ templateId + "'";
			log.println(sql);
			ResultSet rs = session.connection().createStatement()
					.executeQuery(sql);
			while ((rs.next())) {
				BingbiaoRelationVo vo = new BingbiaoRelationVo();
				vo.setColName(rs.getString("col_name"));
				vo.setColNameStandard(rs.getString("col_Name_standard"));
				vo.setReportName(rs.getString("reportName"));
				vo.setCurrId(rs.getString("curr_id"));
				list.add(vo);
			}
		} catch (Exception e) {
			log.printStackTrace(e);
			if (conn != null)
				conn.closeSession();
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return list;
	}

	public static List<String[]> queryTemplateStandard(String[] params) {
		DBConn conn = null;
		Session session = null;
		String[] templates = null;
		List<String[]> list  = new ArrayList<String[]>();
		try {
			conn = new DBConn();
			session = conn.openSession();
			
			String sysdate = DateUtil.toSimpleDateFormat(new Date(),
					"yyyy-MM-dd");
			String sql = "select a.template_id,max(a.version_id) version_id from af_cellinfo a "
					+ "left join af_template  b on   "
					+ "a.template_id = b.template_id "
					+ "and a.version_id = b.version_id "
					+ "left join Af_Template_Freq_Relation afr "
					+ " on afr.template_id = b.template_id "
					+ "and afr.version_id = b.version_id "
					+ "left join M_Rep_Freq m "
					+ " on afr.rep_Freq_Id = m.rep_Freq_Id "
					+ " where a.cell_pid='"
					+ params[0] 
					+ "' and m.rep_freq_name = '"+params[4].trim()
//					+ "' and a.col_name = '"+params[3].trim()+params[1].trim()
					+ "' and to_date('"+sysdate+"' , 'yyyy-MM-dd') between  to_date(b.start_Date, 'yyyy-MM-dd') and to_date( b.end_Date, 'yyyy-MM-dd') "
					+ "   and b.using_flag = 1  group by a.template_id";
			log.println(sql);
			ResultSet rs = session.connection().createStatement()
					.executeQuery(sql);
			while (rs.next()) {
				templates = new String[2];
				templates[0] = rs.getString("template_id");
				templates[1] = rs.getString("version_id");
				list.add(templates);
			}
		} catch (Exception e) {
			log.printStackTrace(e);
			if (conn != null)
				conn.closeSession();
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		if(list.size()==0)
			list = null;
		return list;
	}

	public static List< String[]> queryTemplateBingbiao(String[] params) {
		DBConn conn = null;
		Session session = null;
		String[] templates = null;
		List< String[]> list  = new   ArrayList<String[]>();
		List<String> templateIdsList = DBFomulaDelegate.getAllStandardTemplateId();
		try {
			conn = new DBConn();
			session = conn.openSession();
			String sql = "";
			sql = "select a.template_id,max(a.version_id) version_id from af_cellinfo a  "
					+ "inner join AF_TEMPLATE_BINGBIAO_RELATION b  "
					+ "on a.col_name = b.col_name_standard "
					+ "and a.template_id = b.report_name left join af_template c "
					+ "on  a.template_id = c.template_id "
					+ "and a.version_id = c.version_id "
					+ "left join Af_Template_Freq_Relation afr "
					+ " on afr.template_id = c.template_id "
					+ "and afr.version_id = c.version_id "
					+ "left join M_Rep_Freq m "
					+ " on afr.rep_Freq_Id = m.rep_Freq_Id "
					+ "where a.cell_pid='"
					+ params[0]
					+ "' and m.rep_freq_name = '"+params[4]
					+"' and  b.col_name_standard = '"
					+ params[3]
					+ params[1]
					+ "' and b.curr_id = '"
					+ params[2]
					+ "' and c.using_flag = 1  group by a.template_id";
			log.println(sql);
			ResultSet rs = session.connection().createStatement()
					.executeQuery(sql);
			while (rs.next()) {
				templates = new String[2];
				templates[0] = rs.getString("template_id");
				templates[1] = rs.getString("version_id");
				list.add(templates);
			}
		} catch (Exception e) {
			log.printStackTrace(e);
			if (conn != null)
				conn.closeSession();
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		if(list.size()==0){
			list = null;
		}
		return list;
	}

	public static String[] queryTemplate(String[] params) {
		List<String []> list  = null;
		String[] templates = new String [2];
		list = queryTemplateStandard(params);
		if (list == null) {//通用方式查询
			return null;
		}else{
			if (list.size() == 1) {
				templates = list.get(0);
			}else{
				list = queryTemplateBingbiao(params);
				if (list == null){
					templates[0]="请配置AF_TEMPLATE_BINGBIAO_RELATION表基础信息！";
				}else{
					if(list.size() == 1) {
						templates = list.get(0);
					}else{
						templates[0]="请检查AF_TEMPLATE_BINGBIAO_RELATION表基础信息，存在多表找到相同指标！";
					}
				}
			}
		}
		return templates ;
	}
	
	public static Map<String, String> getRelationMap() {
		DBConn conn = null;
		Session session = null;
		Map<String, String> map = new HashMap<String, String>();
		try {
			conn = new DBConn();
			session = conn.openSession();
			String sql = "SELECT t.report_Name ,t.template_Id FROM  AF_TEMPLATE_BINGBIAO_RELATION  t";
			log.println(sql);
			ResultSet rs = session.connection().createStatement()
					.executeQuery(sql);
			while ((rs.next())) {
				map.put(rs.getString("report_Name"),
						rs.getString("template_Id"));
			}
		} catch (Exception e) {
			log.printStackTrace(e);
			if (conn != null)
				conn.closeSession();
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return map;
	}

}