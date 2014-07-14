package com.fitech.gznx.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import com.cbrc.smis.adapter.TranslatorUtil;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.form.MActuRepForm;
import com.cbrc.smis.hibernate.MActuRep;
import com.cbrc.smis.hibernate.MActuRepPK;
import com.cbrc.smis.hibernate.MRepFreq;
import com.cbrc.smis.util.FitechException;
import com.fitech.gznx.po.AfTemplateFreqRelation;
import com.fitech.gznx.po.AfTemplateFreqRelationId;

public class StrutsAFActuRepDelegate {
	
	private static FitechException log=new FitechException(StrutsAFCellFormuDelegate.class);
	
	/**
	 * 上报频度设置
	 * 根据传过来的内容,向数据库插入记录
	 * 
	 * @author 姚捷
	 *
	 * @param   mActuRepForm   MActuRepForm 包含子报表id,版本号,数据范围id,上报频度id,正常时间,延迟时间
	 * @exception   Exception   If the new com.cbrc.smis.form.MActuRepForm object cannot be created or persisted.
	 */
	public static boolean create(com.cbrc.smis.form.MActuRepForm mActuRepForm)
			throws Exception {
		boolean result = false;

		if (mActuRepForm == null)
			return result;
		DBConn conn = null;
		Session session = null;

		String childRepId = mActuRepForm.getChildRepId();
		String versionId = mActuRepForm.getVersionId();
		Integer[] dataRangeIds = mActuRepForm.getDataRangeIds();
		Integer[] repFreqIds = mActuRepForm.getRepFreqIds();
		Integer[] normalTimes = mActuRepForm.getNormalTimes();
		Integer[] delayTimes = mActuRepForm.getDelayTimes();

		if (childRepId != null && !childRepId.equals("") && childRepId != null
				&& !versionId.equals("")) {
			conn = new DBConn();

			try {
				session = conn.beginTransaction();

				String hql = "from AfTemplateFreqRelation mcr where mcr.id.templateId='"
						+ childRepId + "' and mcr.id.versionId='"
						+ versionId + "'" ;
				
				session.delete(hql);

				for (int i = 0; i < dataRangeIds.length; i++) {
					boolean flag = false;
					for(int j=0;j<repFreqIds.length;j++){
						if(dataRangeIds[i].intValue() == repFreqIds[j].intValue()){
							flag = true;
							break;
						}
					}
					if (flag && dataRangeIds[i].intValue() != 0
							&& dataRangeIds[i].intValue() != -1
							&& normalTimes[i].intValue() != 0 ) {
						AfTemplateFreqRelationId mActuRepPK = new AfTemplateFreqRelationId();
						
						mActuRepPK.setTemplateId(childRepId);
						mActuRepPK.setVersionId(versionId);
						mActuRepPK.setRepFreqId(dataRangeIds[i]);
						
	
							AfTemplateFreqRelation mActuRep = new AfTemplateFreqRelation();
							mActuRep.setId(mActuRepPK);
							mActuRep.setNormalTime(new Long(normalTimes[i]));
							mActuRep.setLaterTime(new Long(delayTimes[i]));
							session.save(mActuRep);
						
						
						session.flush();
						result = true;
					}
				}
			} catch (HibernateException he) {
				log.printStackTrace(he);
				result = false;
			} catch (Exception e) {
				log.printStackTrace(e);
				result = false;
			} finally {
				if (conn != null)
					conn.endTransaction(result);
			}
		}
		return result;
	}
	
	/**
	 * 已使用Hibernate 卞以刚 2011-12-27
	 * 影响对象：AfTemplateFreqRelation MRepFreq
	 * 根据子报表id和版本号id查询出该子报表的上报频度和数据范围信息
	 * @author 姚捷
	 *
	 * @param  childRepId String 子报表id
	 * @param  versionId String 版本号
	 * @return List 查询出的记录  
	 * @exception   Exception   If the com.cbrc.smis.form.MActuRepForm objects cannot be retrieved.
	 */
	public static List select(String childRepId, String versionId)
			throws Exception {
		List result = null;
		DBConn conn = null;
		Session session = null;
		if (childRepId != null && !childRepId.equals("") && versionId != null
				&& !versionId.equals("")) {
			try {
				conn = new DBConn();
				session = conn.openSession();
				String hql = "from AfTemplateFreqRelation mar where mar.id.templateId='"
						+ childRepId + "' and mar.id.versionId='"
						+ versionId + "'";
				List list = session.createQuery(hql).list();
				result = new ArrayList();

				for (Iterator it = list.iterator(); it.hasNext();) {
					MActuRepForm form = new MActuRepForm();
					AfTemplateFreqRelation freqrelation = (AfTemplateFreqRelation) it.next();
					form.setVersionId(freqrelation.getId().getTemplateId());
					form.setChildRepId(freqrelation.getId().getVersionId());
					form.setRepFreqId(freqrelation.getId().getRepFreqId());
					form.setNormalTime(freqrelation.getNormalTime().intValue());
					form.setDelayTime(freqrelation.getLaterTime().intValue());
					/**已使用hibernate 卞以刚 2011-12-27
					 * 影响对象：MRepFreq*/
					form.setRepFreqName(getRepFreqName(freqrelation.getId().getRepFreqId()));
					result.add(form);
				}
			} catch (Exception e) {
				result = null;
				log.printStackTrace(e);
			} finally {
				if (conn != null)
					conn.closeSession();
			}
		}
		return result;
	}
	
	/**
	 * 已使用hibernate 卞以刚 2011-12-27
	 * 影响对象：MRepFreq
	 * */
	public static String getRepFreqName(Integer repFreqId) {
		DBConn conn = null;
		Session session = null;
		String name = null;
		try {
			conn = new DBConn();
			session = conn.openSession();
			String hql = "from MRepFreq a where a.repFreqId="
					+repFreqId ;
			List list = session.createQuery(hql).list();
			name = ((MRepFreq)list.get(0)).getRepFreqName();
		} catch (Exception e) {
			name = null;
			log.printStackTrace(e);
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return name;
	}

}
