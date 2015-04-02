package com.fitech.institution.adapter;

import java.util.List;

import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.util.FitechException;
import com.fitech.gznx.dao.DaoModel;
import com.fitech.institution.po.AfValidateFormulaStandard;

public class AfValidateFormulaStandardDelegate extends DaoModel{
	private static FitechException log = new FitechException(
			AfValidateFormulaStandardDelegate.class);
	public static List<AfValidateFormulaStandard> findAllById(String templateId ,String versionId) {
		DBConn conn = null;
		Session session = null;
		List<AfValidateFormulaStandard> list = null;
		try {
			String hql = " from AfValidateFormulaStandard t where t.templateId='"
					+ templateId + "' and t.versionId = '"+versionId+"'" ;
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
}
