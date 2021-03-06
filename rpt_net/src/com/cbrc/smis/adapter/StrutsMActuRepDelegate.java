package com.cbrc.smis.adapter;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.form.MActuRepForm;
import com.cbrc.smis.hibernate.MActuRep;
import com.cbrc.smis.hibernate.MActuRepPK;
import com.cbrc.smis.util.FitechException;

/**
 * This is a delegate class to handle interaction with the backend persistence layer of hibernate. 
 * It has a set of methods to handle persistence for MActuRep data (i.e. 
 * com.cbrc.smis.form.MActuRepForm objects).
 * 
 * @author <strong>Generated by Middlegen.</strong>
 */
public class StrutsMActuRepDelegate {
	private static FitechException log = new FitechException(
			StrutsMActuRepDelegate.class);
	/**
	 * @author jhb
	 * @param childrepid, versionid,datarangeid,oatid 
	 * @return String freq
	 */
	public static Integer getfreq(String childrepid, String versionid,
			Integer datarangeid, Integer oatid) {
		Integer freq=null;
		if (childrepid == null || datarangeid == null || versionid == null
				|| oatid == null) {
			return null;
		}
		DBConn conn = null;
		try {
			String hql = "from MActuRep mcr where mcr.comp_id.childRepId='"
					+ childrepid + "'" + " and mcr.comp_id.dataRangeId="
					+ datarangeid + " and mcr.comp_id.versionId='" + versionid
					+ "'" + " and mcr.comp_id.OATId=" + oatid;
		//	// System.out.println(hql);
			conn = new DBConn();
			List list = conn.openSession().find(hql);
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					MActuRep mActuRep = (MActuRep) list.get(i);
					MActuRepForm mActuRepForm = new MActuRepForm();
					TranslatorUtil.copyPersistenceToVo(mActuRep, mActuRepForm);
					freq=mActuRepForm.getRepFreqId();
				}
			}

		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return freq;
	}
	
	/**
	 * 
	 * 
	 * @param childrepidsql
	 * @param versionidsql
	 * @param datarangeidsql
	 * @param oatid
	 * @return
	 *//*
	public static List getfreq(String cbversionsql, String cbchildrepidsql, String cbdatarangesql, Integer oatid) {
		List reslist = null;
		if (cbversionsql == null || cbchildrepidsql == null || cbdatarangesql == null
				|| oatid == null) {
			return null;
		}
		DBConn conn = null;
		try {
			// System.out.println("asdaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
			String hql = "from MActuRep mcr where mcr.comp_id.childRepId in("
					+ cbchildrepidsql + ") and mcr.comp_id.dataRangeId in ("
					+ cbdatarangesql + ") and mcr.comp_id.versionId in (" + cbversionsql
					+ ")and mcr.comp_id.OATId=" + oatid;
			// System.out.println(hql);
			conn = new DBConn();
			List list = conn.openSession().find(hql);
			if (list != null && list.size() > 0) {
				reslist = new ArrayList();
				for (int i = 0; i < list.size(); i++) {
					MActuRep mActuRep = (MActuRep) list.get(i);
					MActuRepForm mActuRepForm = new MActuRepForm();
					TranslatorUtil.copyPersistenceToVo(mActuRep, mActuRepForm);
					reslist.add(mActuRepForm);
				}
			}

		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return reslist;
	}*/
	/**
	 * @author jhb
	 * @param childrepid, versionid,datarangeid,oatid 
	 * @return MActuRepForm
	 */
	public static List getnormaltime(String childrepid, String versionid,
			Integer datarangeid, Integer oatid) {
		List reslist = null;
		if (childrepid == null || datarangeid == null || versionid == null
				|| oatid == null) {
			return null;
		}
		DBConn conn = null;
		try {
			String hql = "from MActuRep mcr where mcr.comp_id.childRepId='"
					+ childrepid + "'" + " and mcr.comp_id.dataRangeId="
					+ datarangeid + " and mcr.comp_id.versionId='" + versionid
					+ "'" + " and mcr.comp_id.OATId=" + oatid;
			//// System.out.println(hql);
			conn = new DBConn();
			List list = conn.openSession().find(hql);
			if (list != null && list.size() > 0) {
				reslist = new ArrayList();
				for (int i = 0; i < list.size(); i++) {
					MActuRep mActuRep = (MActuRep) list.get(i);
					MActuRepForm mActuRepForm = new MActuRepForm();
					TranslatorUtil.copyPersistenceToVo(mActuRep, mActuRepForm);
					reslist.add(mActuRepForm);
				}
			}

		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return reslist;
	}

	/**
	 *@author jhb
	 * @param freq
	 * @return reslist 该机构当月所要上报的所有报表
	 */
	public static List getrep(Integer repfreqid, String versionsql,
			String childrepsql,Integer oat) {

		List reslist = new ArrayList();
		int pindu = repfreqid.intValue()+1;
		
		if (repfreqid == null || versionsql == null || childrepsql == null)
			return null;
		DBConn conn = null;
		try {
		//	// System.out.println("pindu is :"+pindu);
			StringBuffer hql = new StringBuffer(
					"from MActuRep mcr where mcr.comp_id.childRepId ='"+ childrepsql.trim() + "' and mcr.comp_id.versionId ='"+ versionsql.trim() + "'and mcr.comp_id.OATId="+oat);
			StringBuffer where = new StringBuffer("");
			if (pindu == 1)
				where.append(" and mcr.comp_id.repFreqId in(1,2,3,4)");
			   if (pindu == 4)
				where.append(" and mcr.comp_id.repFreqId in(1,2)");
			else if (pindu == 7)
				where.append(" and mcr.comp_id.repFreqId in(1,2,3)");
			else if (pindu == 10)
				where.append(" and mcr.comp_id.repFreqId in(1,2)");
			/*else if (pindu == 12)
				where.append(" and mcr.comp_id.repFreqId in(1,2,3,4)");*/
			else 
				where.append(" and mcr.comp_id.repFreqId =1");
			hql.append(where.toString());
		//	// System.out.println(" hql is :" + hql.toString());
			conn = new DBConn();
			List list = conn.openSession().find(hql.toString());
			for (int i = 0; i < list.size(); i++) {
				MActuRep mActuRep = (MActuRep) list.get(i);
				MActuRepForm mActuRepForm = new MActuRepForm();
				TranslatorUtil.copyPersistenceToVo(mActuRep, mActuRepForm);
				reslist.add(mActuRepForm);
			}

		} catch (Exception e) {
			reslist = null;
			log.printStackTrace(e);
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		if(reslist!=null&& reslist.size()>0)
		return reslist;
		else 
			return null;

	}

	/**
	 * 获得报表的报送频度和报送时间
	 * 
	 * @author rds
	 * @serialData 2005-12-22 13:37
	 * 
	 * @param childRepId String 子报表ID
	 * @param versionId String 版本号
	 * @return List
	 */
	public static List getMActuRep(String childRepId, String versionId) {
		List resList = null;

		if (childRepId == null || versionId == null)
			return null;

		DBConn conn = null;
		try {
			String hql = "from MActuRep mcr where mcr.comp_id.childRepId='"
					+ childRepId + "'" + " and mcr.comp_id.versionId='"
					+ versionId + "'";
			conn = new DBConn();

			List list = conn.openSession().find(hql);

			if (list != null && list.size() > 0) {
				resList = new ArrayList();
				for (int i = 0; i < list.size(); i++) {
					MActuRep mActuRep = (MActuRep) list.get(i);
					MActuRepForm mActuRepForm = new MActuRepForm();
					TranslatorUtil.copyPersistenceToVo(mActuRep, mActuRepForm);
					/*// System.out.println("childRepId:" + mActuRepForm.getChildRepId());
					 // System.out.println("versionId:" + mActuRepForm.getVersionId());
					 // System.out.println("DataRangeId:" + mActuRepForm.getDataRangeId());
					 // System.out.println("RepFreqId:" + mActuRepForm.getRepFreqId());
					 // System.out.println("OrgTypeId:" + mActuRepForm.getOATId());*/
					resList.add(mActuRepForm);
				}
			}
		} catch (HibernateException he) {
			log.printStackTrace(he);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			if (conn != null)
				conn.closeSession();
		}

		return resList;
	}

	/**
	 * 已使用hibernate 卞以刚 2011-12-22
	 * 获得报表的报送频度和报送时间
	 * 
	 * @author rds
	 * @serialData 2005-12-22 13:37
	 * 
	 * @param childRepId String 子报表ID
	 * @param versionId String 版本号
	 * @param OATId Integer 频度类别ID
	 * @return List
	 */
	public static List getMActuRep(String childRepId, String versionId,
			Integer OATId) {
		List resList = null;

		if (childRepId == null || versionId == null )
			return null;

		DBConn conn = null;
		try {
			String hql = "from MActuRep mcr where mcr.comp_id.childRepId='"
					+ childRepId + "'" + " and mcr.comp_id.versionId='"
					+ versionId + "'" ;//+ " and mcr.comp_id.OATId=" + OATId;
			conn = new DBConn();

			List list = conn.openSession().find(hql);
			MActuRep mActuRep = null;
			if (list != null && list.size() > 0) {
				resList = new ArrayList();
				for (int i = 0; i < list.size(); i++) {
					mActuRep = (MActuRep) list.get(i);
					MActuRepForm mActuRepForm = new MActuRepForm();
					TranslatorUtil.copyPersistenceToVo(mActuRep, mActuRepForm);
					resList.add(mActuRepForm);
				}
			}
		} catch (HibernateException he) {
			log.printStackTrace(he);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			if (conn != null)
				conn.closeSession();
		}

		return resList;
	}

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
		Integer OATId = mActuRepForm.getOATId();
		Integer[] dataRangeIds = mActuRepForm.getDataRangeIds();
		Integer[] repFreqIds = mActuRepForm.getRepFreqIds();
		Integer[] normalTimes = mActuRepForm.getNormalTimes();
		Integer[] delayTimes = mActuRepForm.getDelayTimes();

		if (childRepId != null && !childRepId.equals("") && childRepId != null
				&& !versionId.equals("")) {
			conn = new DBConn();

			try {
				session = conn.beginTransaction();

				String hql = "from MActuRep mcr where mcr.comp_id.childRepId='"
						+ childRepId + "' and mcr.comp_id.versionId='"
						+ versionId + "'" ;
				
				session.delete(hql);

				for (int i = 0; i < dataRangeIds.length; i++) {
					if (dataRangeIds[i].intValue() != 0
							&& repFreqIds[i].intValue() != -1
							&& normalTimes[i].intValue() != 0 ) {
						MActuRepPK mActuRepPK = new MActuRepPK();
						mActuRepPK.setDataRangeId(dataRangeIds[i]);
						mActuRepPK.setChildRepId(childRepId);
						mActuRepPK.setVersionId(versionId);
						mActuRepPK.setRepFreqId(repFreqIds[i]);
						mActuRepPK.setOATId(Integer.valueOf("2"));

						/*MActuRepForm addMActuRepForm = new MActuRepForm();
						 addMActuRepForm.setChildRepId(childRepId);
						 addMActuRepForm.setVersionId(versionId);
						 addMActuRepForm.setDataRangeId(dataRangeIds[i]);
						 addMActuRepForm.setRepFreqId(repFreqIds[i]);
						 addMActuRepForm.setNormalTime(normalTimes[i]);
						 if (delayTimes[i].intValue() != 0)
						 addMActuRepForm.setDelayTime(delayTimes[i]);
						 addMActuRepForm.setOATId(OATId);*/
//						Object object = session.get(MActuRep.class, mActuRepPK);
//						if (object != null) {
//							MActuRep mActuRep = (MActuRep) object;
//							mActuRep.setNormalTime(normalTimes[i]);
//							mActuRep.setDelayTime(delayTimes[i]);
//							session.update(mActuRep);
//						} else {
							MActuRep mActuRep = new MActuRep();
							mActuRep.setComp_id(mActuRepPK);
							mActuRep.setNormalTime(normalTimes[i]);
							mActuRep.setDelayTime(delayTimes[i]);
							session.save(mActuRep);
						//}
						//TranslatorUtil.copyVoToPersistence(mActuRep,addMActuRepForm);

						/*// System.out.println("ChildRepId:" + mActuRep.getComp_id().getChildRepId());
						 // System.out.println("versionId:" + mActuRep.getComp_id().getVersionId());
						 // System.out.println("getRepFreqId:" + mActuRep.getComp_id().getRepFreqId());
						 // System.out.println("getRepFreqId:" + mActuRep.getComp_id().getRepFreqId());*/
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
	 * 已使用hibernate 卞以刚 2011-12-22
	 * 更新报表的数据范围和报送频度设定
	 * 
	 * @author rds
	 * @serialData 2005-12-22 15:01
	 * 
	 * @param mActuRepForm MActuRepForm
	 * @return 更新成功，返回true;否则，返回false
	 */
	public static boolean update(MActuRepForm mActuRepForm) {
		boolean result = false;

		if (mActuRepForm == null)
			return result;

		DBConn conn = null;
		Session session = null;

		String childRepId = mActuRepForm.getChildRepId();
		String versionId = mActuRepForm.getVersionId();
		Integer OATId = mActuRepForm.getOATId();
		Integer[] dataRangeIds = mActuRepForm.getDataRangeIds();
		Integer[] repFreqIds = mActuRepForm.getRepFreqIds();
		Integer[] normalTimes = mActuRepForm.getNormalTimes();
		Integer[] delayTimes = mActuRepForm.getDelayTimes();

		if ((childRepId != null && !childRepId.equals(""))
				&& (childRepId != null && !versionId.equals(""))) {
			try {
				conn = new DBConn();
				session = conn.beginTransaction();
				// System.out.println(childRepId+versionId);
				String hql = "from MActuRep mcr where mcr.comp_id.childRepId='"
						+ childRepId + "' and mcr.comp_id.versionId='"
						+ versionId + "'" ;//+ " and mcr.comp_id.OATId=" + OATId;

				session.delete(hql);
                 session.flush();
                
				for (int i = 0; i < dataRangeIds.length; i++) {
					if (dataRangeIds[i].intValue() != 0
							&& repFreqIds[i].intValue() != -1
							&& normalTimes[i].intValue() != 0) {
						MActuRepForm addMActuRepForm = new MActuRepForm();
						addMActuRepForm.setChildRepId(childRepId);
						addMActuRepForm.setVersionId(versionId);
						addMActuRepForm.setDataRangeId(dataRangeIds[i]);
						addMActuRepForm.setRepFreqId(repFreqIds[i]);
						addMActuRepForm.setNormalTime(normalTimes[i]);
						addMActuRepForm.setOATId(Integer.valueOf("3"));
						if (delayTimes[i].intValue() != 0)
							addMActuRepForm.setDelayTime(delayTimes[i]);
						
						MActuRep mActuRep = new MActuRep();
						TranslatorUtil.copyVoToPersistence(mActuRep,
								addMActuRepForm);
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
	 * 已使用hibernate 卞以刚 2011-12-22
	 * 更新报表的数据范围和报送频度设定
	 * 
	 * @author rds
	 * @serialData 2005-12-22 15:01
	 * 
	 * @param mActuRepForm MActuRepForm
	 * @return 更新成功，返回true;否则，返回false
	 */
	public static String findTemplateFreq(MActuRepForm mActuRepForm) {
		String freqStr = "";

		if (freqStr == null)
			return null;

		DBConn conn = null;
		Session session = null;
		
		Integer[] dataRangeIds = mActuRepForm.getDataRangeIds();
		Integer[] repFreqIds = mActuRepForm.getRepFreqIds();
		Integer[] normalTimes = mActuRepForm.getNormalTimes();
		Integer[] delayTimes = mActuRepForm.getDelayTimes();
		String childRepId = mActuRepForm.getChildRepId();
		String versionId = mActuRepForm.getVersionId();
		try {
			conn = new DBConn();
			session = conn.openSession();
			String freqIds = "";
			for (int i = 0; i < dataRangeIds.length; i++) {
				if (dataRangeIds[i].intValue() != 0
						&& repFreqIds[i].intValue() != -1
						&& normalTimes[i].intValue() != 0) {
					if(!"".equals(freqIds))
						freqIds+=",";
					freqIds+=repFreqIds[i];
				}
			}
			if(!"".equals(freqIds)){
				String sql = "select rep_freq_id from m_actu_rep where rep_freq_id not in("+freqIds+") and child_rep_id='"+childRepId+"'";
				ResultSet rs = session.connection().createStatement().executeQuery(sql);
				if(rs!=null){
					while(rs.next()){
						if(!"".equals(freqStr)){
							freqStr+=",";
						}
						freqStr+=rs.getInt("rep_freq_id");
					}
				}
			}
		}catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return freqStr;
	}

	/**
	 * Retrieve an existing com.cbrc.smis.form.MActuRepForm object for editing.
	 *
	 * @param   mActuRepForm   The com.cbrc.smis.form.MActuRepForm object containing the data used to retrieve the object (i.e. the primary key info).
	 * @exception   Exception   If the com.cbrc.smis.form.MActuRepForm object cannot be retrieved.
	 */
	public static com.cbrc.smis.form.MActuRepForm edit(
			com.cbrc.smis.form.MActuRepForm mActuRepForm) throws Exception {
		com.cbrc.smis.hibernate.MActuRep mActuRepPersistence = new com.cbrc.smis.hibernate.MActuRep();
		TranslatorUtil.copyVoToPersistence(mActuRepPersistence, mActuRepForm);
		javax.naming.InitialContext ctx = new javax.naming.InitialContext();
		// TODO: Make adapter get SessionFactory jndi name by ant task argument?
		net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory) ctx
				.lookup("java:AirlineHibernateFactory");
		net.sf.hibernate.Session session = factory.openSession();
		net.sf.hibernate.Transaction tx = session.beginTransaction();
		tx.commit();
		session.close();
		TranslatorUtil.copyPersistenceToVo(mActuRepPersistence, mActuRepForm);
		return mActuRepForm;
	}

	/**
	 * Remove (delete) an existing com.cbrc.smis.form.MActuRepForm object.
	 *
	 * @param   mActuRepForm   The com.cbrc.smis.form.MActuRepForm object containing the data to be deleted.
	 * @exception   Exception   If the com.cbrc.smis.form.MActuRepForm object cannot be removed.
	 */
	public static void remove(com.cbrc.smis.form.MActuRepForm mActuRepForm)
			throws Exception {
		com.cbrc.smis.hibernate.MActuRep mActuRepPersistence = new com.cbrc.smis.hibernate.MActuRep();
		TranslatorUtil.copyVoToPersistence(mActuRepPersistence, mActuRepForm);
		javax.naming.InitialContext ctx = new javax.naming.InitialContext();
		// TODO: Make adapter get SessionFactory jndi name by ant task argument?
		net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory) ctx
				.lookup("java:AirlineHibernateFactory");
		net.sf.hibernate.Session session = factory.openSession();
		net.sf.hibernate.Transaction tx = session.beginTransaction();
		// TODO: is this really needed?
		session.delete(mActuRepPersistence);
		tx.commit();
		session.close();
	}

	/**
	 * Retrieve all existing com.cbrc.smis.form.MActuRepForm objects.
	 *
	 * @exception   Exception   If the com.cbrc.smis.form.MActuRepForm objects cannot be retrieved.
	 */
	public static List findAll() throws Exception {
		List retVals = new ArrayList();
		//javax.naming.InitialContext ctx = new javax.naming.InitialContext();
		// TODO: Make adapter get SessionFactory jndi name by ant task argument?
		//net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory) ctx
		//		.lookup("java:AirlineHibernateFactory");
		//net.sf.hibernate.Session session = factory.openSession();
		DBConn conn=new DBConn();
        Session session = null;
        session=conn.openSession();
        net.sf.hibernate.Transaction tx = session.beginTransaction();
		retVals.addAll(session.find("from com.cbrc.smis.hibernate.MActuRep"));
		tx.commit();
		session.close();
		ArrayList mActuRep_vos = new ArrayList();
		for (Iterator it = retVals.iterator(); it.hasNext();) {
			com.cbrc.smis.form.MActuRepForm mActuRepFormTemp = new com.cbrc.smis.form.MActuRepForm();
			com.cbrc.smis.hibernate.MActuRep mActuRepPersistence = (com.cbrc.smis.hibernate.MActuRep) it
					.next();
			TranslatorUtil.copyPersistenceToVo(mActuRepPersistence,
					mActuRepFormTemp);
			mActuRep_vos.add(mActuRepFormTemp);
		}
		retVals = mActuRep_vos;
		return retVals;
	}

	/**
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
				String hql = "from MActuRep mar where mar.comp_id.childRepId='"
						+ childRepId + "' and mar.comp_id.versionId='"
						+ versionId + "'";
				List list = session.createQuery(hql).list();
				result = new ArrayList();

				for (Iterator it = list.iterator(); it.hasNext();) {
					MActuRepForm form = new MActuRepForm();
					MActuRep mActuRepPersistence = (MActuRep) it.next();
					TranslatorUtil.copyPersistenceToVo(mActuRepPersistence,
							form);
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
	 * This method will return all objects referenced by MDataRgType
	 */
	public static List getMDataRgType(
			com.cbrc.smis.form.MActuRepForm mActuRepForm) throws Exception {
		List retVals = new ArrayList();
		com.cbrc.smis.hibernate.MActuRep mActuRepPersistence = null;
		javax.naming.InitialContext ctx = new javax.naming.InitialContext();
		// TODO: Make adapter get SessionFactory jndi name by ant task argument?
		net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory) ctx
				.lookup("java:AirlineHibernateFactory");
		net.sf.hibernate.Session session = factory.openSession();
		net.sf.hibernate.Transaction tx = session.beginTransaction();
		tx.commit();
		session.close();
		retVals.add(mActuRepPersistence.getMDataRgType());
		ArrayList DATA_RANGE_ID_vos = new ArrayList();
		for (Iterator it = retVals.iterator(); it.hasNext();) {
			com.cbrc.smis.form.MDataRgTypeForm DATA_RANGE_ID_Temp = new com.cbrc.smis.form.MDataRgTypeForm();
			com.cbrc.smis.hibernate.MDataRgType DATA_RANGE_ID_PO = (com.cbrc.smis.hibernate.MDataRgType) it
					.next();
			TranslatorUtil.copyPersistenceToVo(DATA_RANGE_ID_PO,
					DATA_RANGE_ID_Temp);
			DATA_RANGE_ID_vos.add(DATA_RANGE_ID_Temp);
		}
		retVals = DATA_RANGE_ID_vos;
		return retVals;
	}

	/**
	 * This method will return all objects referenced by MRepFreq
	 */
	public static List getMRepFreq(com.cbrc.smis.form.MActuRepForm mActuRepForm)
			throws Exception {
		List retVals = new ArrayList();
		com.cbrc.smis.hibernate.MActuRep mActuRepPersistence = null;
		javax.naming.InitialContext ctx = new javax.naming.InitialContext();
		// TODO: Make adapter get SessionFactory jndi name by ant task argument?
		net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory) ctx
				.lookup("java:AirlineHibernateFactory");
		net.sf.hibernate.Session session = factory.openSession();
		net.sf.hibernate.Transaction tx = session.beginTransaction();
		tx.commit();
		session.close();
		retVals.add(mActuRepPersistence.getMRepFreq());
		ArrayList REP_FREQ_ID_vos = new ArrayList();
		for (Iterator it = retVals.iterator(); it.hasNext();) {
			com.cbrc.smis.form.MRepFreqForm REP_FREQ_ID_Temp = new com.cbrc.smis.form.MRepFreqForm();
			com.cbrc.smis.hibernate.MRepFreq REP_FREQ_ID_PO = (com.cbrc.smis.hibernate.MRepFreq) it
					.next();
			TranslatorUtil
					.copyPersistenceToVo(REP_FREQ_ID_PO, REP_FREQ_ID_Temp);
			REP_FREQ_ID_vos.add(REP_FREQ_ID_Temp);
		}
		retVals = REP_FREQ_ID_vos;
		return retVals;
	}

	/**
	 * This method will return all objects referenced by MChildReport
	 */
	public static List getMChildReport(
			com.cbrc.smis.form.MActuRepForm mActuRepForm) throws Exception {
		List retVals = new ArrayList();
		com.cbrc.smis.hibernate.MActuRep mActuRepPersistence = null;
		javax.naming.InitialContext ctx = new javax.naming.InitialContext();
		// TODO: Make adapter get SessionFactory jndi name by ant task argument?
		net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory) ctx
				.lookup("java:AirlineHibernateFactory");
		net.sf.hibernate.Session session = factory.openSession();
		net.sf.hibernate.Transaction tx = session.beginTransaction();
		tx.commit();
		session.close();
		retVals.add(mActuRepPersistence.getMChildReport());
		ArrayList CHILD_REP_ID_vos = new ArrayList();
		for (Iterator it = retVals.iterator(); it.hasNext();) {
			com.cbrc.smis.form.MChildReportForm CHILD_REP_ID_Temp = new com.cbrc.smis.form.MChildReportForm();
			com.cbrc.smis.hibernate.MChildReport CHILD_REP_ID_PO = (com.cbrc.smis.hibernate.MChildReport) it
					.next();
			TranslatorUtil.copyPersistenceToVo(CHILD_REP_ID_PO,
					CHILD_REP_ID_Temp);
			CHILD_REP_ID_vos.add(CHILD_REP_ID_Temp);
		}
		retVals = CHILD_REP_ID_vos;
		return retVals;
	}
	
	public static boolean getfreq(String childrepid, String versionid,Integer datarangeid,String term) {
		boolean bool = false;
		if (childrepid == null || datarangeid == null || versionid == null){
			return false;
		}
		
		String rep_freq = "";
		if(term.equals("12"))
			rep_freq = "('月','季','半年','年')";
		else if(term.equals("6"))
			rep_freq = "('月','季','半年')";
		else if(term.equals("3") || term.equals("9"))
			rep_freq = "('月','季')";
		else rep_freq = "('月')";
		
		DBConn conn = null;
		try {
			String hql = "from MActuRep mcr where mcr.comp_id.childRepId='"
					+ childrepid + "'" + " and mcr.comp_id.dataRangeId="
					+ datarangeid + " and mcr.comp_id.versionId='" + versionid 
					+ "' and mcr.MRepFreq.repFreqName in " + rep_freq;
			conn = new DBConn();
			List list = conn.openSession().find(hql);
			if (list != null && list.size() > 0) {
				bool = true;
			}
		} catch (Exception e) {
			bool = false;
			log.printStackTrace(e);
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return bool;
	}
	/**
	 * 根据报表ID、版本号获得报送频度
	 * 
	 * @param childRepId 报表ID
	 * @param versionId 版本号
	 * @param term 报表期数
	 * @return List 报送频度信息
	 */
	public static List getMActuReps(String childRepId,String versionId,String term){
		if(childRepId == null || versionId == null 
				|| term == null) return null;
		
		List mActuReps = null;
		String rep_freq = "";
		if(term.equals("12"))
			rep_freq = "('月','季','半年','年')";
		else if(term.equals("6"))
			rep_freq = "('月','季','半年')";
		else if(term.equals("3") || term.equals("9"))
			rep_freq = "('月','季')";
		else rep_freq = "('月')";
		
		DBConn conn = null;
		try{
			String hql = "from MActuRep mcr where mcr.comp_id.childRepId='"+childRepId+"' " +
					"and mcr.comp_id.versionId='"+versionId+"' and mcr.MRepFreq.repFreqName in " + rep_freq;
			conn = new DBConn();
			List list = conn.openSession().find(hql);
			if(list != null && list.size() > 0){
				mActuReps = new ArrayList();
				MActuRep mActuRep = null;
				MActuRepForm mActuRepForm = null;
				for(Iterator iter=list.iterator();iter.hasNext();){
					mActuRep = (MActuRep)iter.next();
					
					mActuRepForm = new MActuRepForm();
					TranslatorUtil.copyPersistenceToVo(mActuRep, mActuRepForm);
					mActuReps.add(mActuRepForm);
				}
			}
		}catch (Exception ex){
			mActuReps = null;
			log.printStackTrace(ex);
		}finally{
			if(conn != null) conn.closeSession();
		}
		return mActuReps;
	}
}
