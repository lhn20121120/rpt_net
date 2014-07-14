package com.fitech.gznx.service;

import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.util.FitechException;
import com.fitech.gznx.common.StringUtil;
import com.fitech.gznx.po.QdCellinfo;
import com.fitech.gznx.po.QdCellinfoId;

public class AFQDCellInfoDelegate {
	
	private static FitechException log = new FitechException(
			AFQDCellInfoDelegate.class);
	
	/**
	 * 得到一个清单式报表对象
	 * 
	 * @param orgId
	 * @return
	 */
	public static QdCellinfo getCellInfo(String templateId,String versionId) {

		DBConn dbconn = null;

		Session session = null;

		if (StringUtil.isEmpty(templateId) || StringUtil.isEmpty(versionId)) {
			return null;
		}
		QdCellinfoId cellInfoId = new QdCellinfoId();
		cellInfoId.setTemplateId(templateId);
		cellInfoId.setVersionId(versionId);
		try {
			dbconn = new DBConn();

			session = dbconn.openSession();

			QdCellinfo ao = (QdCellinfo) session.get(QdCellinfo.class, cellInfoId);

			return ao;

		} catch (Exception e) {

			log.printStackTrace(e);

			return null;

		} finally {

			if (session != null)

				dbconn.closeSession();
		}
	}
}
