package com.fitech.institution.adapter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.util.FitechException;
import com.fitech.gznx.po.AfReport;
import com.fitech.gznx.po.AfValidateformula;

public class AFValidateFormulaDelegate {

	private static FitechException log = new FitechException(
			AFValidateFormulaDelegate.class);

	/**
	 * jdbc技术 无特殊语法 不需要修改 卞以刚 2011-12-26 影响表：af_validateformula
	 * 获取报表的[表内表间校验/表内表间的计算]关系表达式列表
	 * 
	 * @param conn
	 *            Connection 数据连接
	 * @param reportIn
	 *            ReportIn 内网表单表
	 * @return List 表达式集合
	 * @throws Exception
	 */
	public static List getCellFormus(Connection orclCon, AfReport reportIn,
			Integer formuType) throws Exception {

		ArrayList resList = null;
		PreparedStatement pStmt = null;
		ResultSet plRS = null;

		try {
			if (reportIn.getTemplateId() != null
					&& reportIn.getVersionId() != null) {

				// 查询某一张报表的所有单元格表达式
				String plSql = "select a.formula_id,a.formula_value,a.formula_name from af_validateformula a "
						+ " where a.template_id = ? and a.version_id = ? and a.validate_type_id="
						+ formuType;

				// System.out.println(plSql
				// +"="+reportIn.getTemplateId()+"-"+reportIn.getVersionId());
				pStmt = orclCon.prepareStatement(plSql.toUpperCase());
				pStmt.setString(1, reportIn.getTemplateId());
				pStmt.setString(2, reportIn.getVersionId());

				plRS = pStmt.executeQuery();

				resList = new ArrayList();
				while (plRS.next()) {
					AfValidateformula cellFormu = new AfValidateformula();
					cellFormu.setFormulaId(plRS.getLong("formula_id"
							.toUpperCase()));
					cellFormu.setFormulaValue(plRS.getString("formula_value"
							.toUpperCase()));
					cellFormu.setFormulaName(plRS.getString("formula_name"
							.toUpperCase()));
					cellFormu.setValidateTypeId(formuType.longValue());
					resList.add(cellFormu);
				}
			}
		} catch (SQLException sqle) {
			throw new Exception(sqle.getMessage());
		} finally {
			if (plRS != null)
				plRS.close();
			if (pStmt != null)
				pStmt.close();
		}
		return resList;
	}

	public static List<AfValidateformula> findAllById(String templateId,
			String versionId) {
		DBConn conn = null;
		Session session = null;
		List<AfValidateformula> list = null;
		try {
			String hql = " from AfValidateformula t where t.templateId='"
					+ templateId + "' and t.versionId = '" + versionId + "' order by t.validateTypeId ,t.formulaId desc";
			conn = new DBConn();

			session = conn.beginTransaction();
			list = session.createQuery(hql).list();
			conn.endTransaction(true);
			return list;
		} catch (Exception e) {
			log.printStackTrace(e);
			return null;
		} finally {
			if (conn != null)
				conn.closeSession();
		}
	}

	public static List<AfValidateformula> findZdyById(String templateId ,String versionId) {
		DBConn conn = null;
		Session session = null;
		List<AfValidateformula> list = null;
		try {
			String hql = " from AfValidateformula t where t.templateId='"
					+ templateId + "' and t.versionId = '"+versionId+"' and"
					+ " t.formulaId not in (select c.formulaId from AfValidateFormulaStandard  c where c.templateId='"
					+ templateId + "' and c.versionId = '"+versionId+"') order by t.validateTypeId ,t.formulaId desc " ;
			conn = new DBConn();

			session = conn.beginTransaction();
			list = session.createQuery(hql).list();
			conn.endTransaction(true);
			return list;
		} catch (Exception e) {
			log.printStackTrace(e);
			return null;
		} finally {
			if (conn != null)
				conn.closeSession();
		}
	}

	public static List<String> getFormulasById(List<AfValidateformula> list) {
		List<String> values = new ArrayList<String>();
		// List<AfValidateformula> list = findAllById(templateId,versionId);
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			AfValidateformula afValidateformula = (AfValidateformula) iterator
					.next();
			values.add(afValidateformula.getFormulaValue());
		}
		return values;
	}

}
