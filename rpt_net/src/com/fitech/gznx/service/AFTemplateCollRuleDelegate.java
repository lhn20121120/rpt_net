package com.fitech.gznx.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.auth.hibernate.MPurOrg;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.form.AfTemplateCollRuleForm;
import com.cbrc.smis.other.Aditing;
import com.cbrc.smis.proc.util.FitechUtil;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.fitech.gznx.common.Config;
import com.fitech.gznx.common.DateUtil;
import com.fitech.gznx.form.AFReportForm;
import com.fitech.gznx.form.OrgInfoForm;
import com.fitech.gznx.po.AfCollectRelation;
import com.fitech.gznx.po.AfOrg;
import com.fitech.gznx.po.AfReport;
import com.fitech.gznx.po.AfTemplateCollRep;
import com.fitech.gznx.po.AfTemplateCollRule;
import com.fitech.gznx.po.AfTemplateCollRuleId;
import com.fitech.gznx.po.AfTemplateOrgRelation;
import com.fitech.gznx.po.AfTemplateOuterRep;
import com.fitech.gznx.po.AfViewReport;
import com.fitech.gznx.treexml.BaseOrgTreeIterator;
import com.fitech.gznx.treexml.OrgTreeInterface;
import com.fitech.gznx.util.TranslatorUtil;
import com.fitech.net.form.OrgNetForm;

public class AFTemplateCollRuleDelegate {

	private static FitechException log = new FitechException(
			AFOrgDelegate.class);

	/**
	 * 判断汇总规则表里是同级行还是自定义
	 * 
	 */
	public static String getCollSchemaName(String orgId, String templateId,
			String versionId) {
		String result = "HZTJH";
		List list = null;
		// 连接对象和会话对象初始化
		DBConn conn = null;
		Session session = null;

		try {
			// conn对象的实例化
			conn = new DBConn();
			// 打开连接开始会话
			session = conn.openSession();
			String hsql = "select af.collFormula from AfTemplateCollRule af where af.id.templateId='"
					+ templateId
					+ "' and af.id.orgId='"
					+ orgId
					+ "'  and af.id.versionId='" + versionId + "'";
			Query query = session.createQuery(hsql);
			list = query.list();
			if (list != null && list.size() > 0) {
				result = (String) list.get(0);
				result = FitechUtil.parseFomual(result)[1];
				if (!"HZTJH".equals(result)) {
					result = "CUSTOM_ORG";
				}
			}
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			// 如果连接存在，则断开，结束会话
			if (conn != null)
				conn.closeSession();
		}
		return result;
	}

	public static String getCollFormulaName(String orgId, String templateId,
			String versionId) {
		String result = "";
		List list = null;
		// 连接对象和会话对象初始化
		DBConn conn = null;
		Session session = null;

		try {
			// conn对象的实例化
			conn = new DBConn();
			// 打开连接开始会话
			session = conn.openSession();
			String hsql = "select af.collFormula from AfTemplateCollRule af where af.id.templateId='"
					+ templateId
					+ "' and af.id.orgId='"
					+ orgId
					+ "'  and af.id.versionId='" + versionId + "'";
			Query query = session.createQuery(hsql);
			list = query.list();
			if (list != null && list.size() > 0) {
				result = (String) list.get(0);
			}
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			// 如果连接存在，则断开，结束会话
			if (conn != null)
				conn.closeSession();
		}
		return result;
	}

	public static String getSameLevelOrgIds(AfOrg afOrg) {
		String orgIds = "";
		DBConn conn = null;
		Session session = null;
		try {
			conn = new DBConn();
			session = conn.openSession();
			String hql = "from AfOrg  a where a.orgLevel="
					+ afOrg.getOrgLevel() + "  and a.orgId<>'"
					+ afOrg.getOrgId() + "'";
			Query query = session.createQuery(hql);
			List list = query.list();
			for (int i = 0; list != null && i < list.size(); i++) {
				AfOrg afOrg1 = (AfOrg) list.get(i);
				if (i != list.size() - 1) {
					orgIds += afOrg1.getOrgId() + ",";
				} else {
					orgIds += afOrg1.getOrgId();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (session != null) {
				conn.closeSession();
			}
		}
		return orgIds;
	}

	public static List getSameLevelOrgList(AfOrg afOrg) {
		String orgIds = "";
		DBConn conn = null;
		Session session = null;
		List resList = null;
		try {
			conn = new DBConn();
			session = conn.openSession();
			String hql = "from AfOrg  a where a.orgLevel="
					+ afOrg.getOrgLevel() + "  and a.orgId<>'"
					+ afOrg.getOrgId() + "'";
			Query query = session.createQuery(hql);
			resList = query.list();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (session != null) {
				conn.closeSession();
			}
		}
		return resList;
	}

	
	/**
	 * 已使用hibernate 卞以刚 2011-12-21 增加机构信息
	 * 
	 * @author Nick
	 * @param orgInfoForm
	 *            OrgInfoForm对象
	 * @param userId
	 *            String 增加机构的用户ID
	 * @return boolean 是否增加成功
	 */
	public static boolean add(AfTemplateCollRuleForm orgInfoForm, String userId) {
		boolean result = false;
		List list = null;
		// 连接对象和会话对象初始化
		DBConn conn = null;
		Session session = null;

		// 查询条件HQL的生成
		try {
			// conn对象的实例化
			conn = new DBConn();
			// 打开连接开始会话
			session = conn.beginTransaction();

			/** 1 增加机构信息 */
			// 得到父节点的详细信息

			AfTemplateCollRule orgRule = new AfTemplateCollRule();
			AfTemplateCollRuleId orgRuleId = new AfTemplateCollRuleId();
			orgRuleId.setOrgId(orgInfoForm.getOrg_id());
			orgRuleId.setTemplateId(orgInfoForm.getTemplate_id());
			orgRuleId.setVersionId(orgInfoForm.getVersion_id());
			orgRule.setCollFormula(orgInfoForm.getColl_formula());
			orgRule.setCollSchema(Integer.valueOf(orgInfoForm.getColl_schema()));
			orgRule.setId(orgRuleId);
			session.save(orgRule);
			session.flush();
			result = true;
		} catch (Exception e) {
			log.printStackTrace(e);
			result = false;
		} finally {
			// 如果连接存在，则断开，结束会话，返回
			if (conn != null)
				conn.endTransaction(result);
		}
		return result;
	}

	/**
	 * 已使用hibernate 卞以刚 2011-12-21 增加机构信息
	 * 
	 * @author Nick
	 * @param orgInfoForm
	 *            OrgInfoForm对象
	 * @param userId
	 *            String 增加机构的用户ID
	 * @return boolean 是否增加成功
	 */
	public static boolean delete(AfTemplateCollRuleForm afRuleForm,
			String userId) {
		boolean result = false;
		List list = null;
		// 连接对象和会话对象初始化
		DBConn conn = null;
		Session session = null;

		// 查询条件HQL的生成
		try {
			// conn对象的实例化
			conn = new DBConn();
			// 打开连接开始会话
			session = conn.beginTransaction();
			String hql = "from AfTemplateCollRule t where t.id.templateId=? and t.id.versionId=? and t.id.orgId=?";
			Query query = session.createQuery(hql);
			query.setString(0, afRuleForm.getTemplate_id());
			query.setString(1, afRuleForm.getVersion_id());
			query.setString(2, afRuleForm.getOrg_id());
			list = query.list();
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				AfTemplateCollRule obj = (AfTemplateCollRule) iterator.next();
				session.delete(obj);
			}

			session.flush();
			result = true;
		} catch (Exception e) {
			log.printStackTrace(e);
			result = false;
		} finally {
			// 如果连接存在，则断开，结束会话，返回
			if (conn != null)
				conn.endTransaction(result);
		}
		return result;
	}

	/**
	 * 已使用hibernate 卞以刚 2011-12-21 更新机构信息
	 * 
	 * @author Nick
	 * @param orgInfoForm
	 *            OrgInfoForm对象
	 * @return boolean 更新机构是否成功
	 */
	public static boolean updateOrgInfo(OrgInfoForm orgInfoForm) {
		boolean result = false;
		// 连接对象和会话对象初始化
		DBConn conn = null;
		Session session = null;

		try {
			// conn对象的实例化
			conn = new DBConn();
			// 打开连接开始会话
			session = conn.beginTransaction();

			AfOrg orgInfo = (AfOrg) session.get(AfOrg.class,
					orgInfoForm.getOrgId());

			orgInfo.setOrgName(orgInfoForm.getOrgName());
			orgInfo.setOrgOuterId(orgInfoForm.getOrgOuterId());

			if (orgInfoForm.getOrgLevel() != null)
				orgInfo.setOrgLevel(Long.valueOf(orgInfoForm.getOrgLevel()));

			orgInfo.setOrgType(orgInfoForm.getOrgType());

			if (orgInfoForm.getOrgRegion() != null)
				orgInfo.setRegionId(Long.valueOf(orgInfoForm.getOrgRegion()));

			// 更新机构信息
			session.update(orgInfo);

			session.flush();
			result = true;
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			// 如果连接存在，则断开，结束会话，返回
			if (conn != null)
				conn.endTransaction(result);
		}
		return result;
	}

	/**
	 * 已使用hibernate 卞以刚 2011-12-21 删除机构信息[如果是虚拟机构，则要先删除汇总关系]
	 * 
	 * @author Nick
	 * @param orgInfoForm
	 *            OrgInfoForm对象
	 * @return boolean 删除机构是否成功
	 */
	public static boolean delete(OrgInfoForm orgInfoForm) {
		boolean result = true;
		// 连接对象和会话对象初始化
		DBConn conn = null;
		Session session = null;

		List lst = null;

		try {
			// conn对象的实例化
			conn = new DBConn();
			// 打开连接开始会话
			session = conn.beginTransaction();

			AfOrg orgInfo = (AfOrg) session.get(AfOrg.class,
					orgInfoForm.getOrgId());

			if (orgInfo == null) {
				return false;
			}
			// 如果是虚拟机构，则删除CollectId为当前机构ID的记录
			// 如果是真实机构，则删除OrgId为当前机构ID的记录
			if (orgInfo.getIsCollect().equals(Long.valueOf(Config.IS_COLLECT))) {
				// 虚拟机构
				String hql = "from AfCollectRelation cr where cr.id.collectId=?";
				Query query = session.createQuery(hql);
				query.setString(0, orgInfo.getOrgId());
				List crLst = query.list();

				if (crLst != null && crLst.size() > 0) {
					for (int i = 0; i < crLst.size(); i++) {
						AfCollectRelation cr = (AfCollectRelation) crLst.get(i);
						session.delete(cr);
					}
				}
			} else {
				// 真实机构
				String hql = "from AfCollectRelation cr where cr.id.orgId=?";
				Query query = session.createQuery(hql);
				query.setString(0, orgInfo.getOrgId());
				List crLst = query.list();

				if (crLst != null && crLst.size() > 0) {
					for (int i = 0; i < crLst.size(); i++) {
						AfCollectRelation cr = (AfCollectRelation) crLst.get(i);
						session.delete(cr);
					}
				}

				// // 自动删除机构和指标集对应关系
				// hql = "from MeasureOrgMapping mom where mom.id.orgId=?";
				// lst = session.createQuery(hql).setString(0,
				// orgInfo.getOrgId())
				// .list();
				// for (int i = 0; i < lst.size(); i++) {
				// session.delete(lst.get(i));
				// }
			}

			// // 自动删除该机构的所有机构权限记录
			// String hql = "from UserOrgPurview uop where uop.id.orgId=?";
			// lst = session.createQuery(hql).setString(0, orgInfo.getOrgId())
			// .list();
			// for (int i = 0; i < lst.size(); i++) {
			// session.delete(lst.get(i));
			// }

			// 删除机构时，删除对应的MPurOrg中的记录
			String mpohql = "from MPurOrg mpo where mpo.comp_id.org.orgId=?";
			Query mpoquery = session.createQuery(mpohql);
			mpoquery.setString(0, orgInfo.getOrgId());
			List mpoLst = mpoquery.list();

			if (mpoLst != null && mpoLst.size() > 0) {
				for (int i = 0; i < mpoLst.size(); i++) {
					MPurOrg mpo = (MPurOrg) mpoLst.get(i);
					session.delete(mpo);
				}
			}

			// 删除机构时，删除对应的AfTemplateOrgRelation中的记录
			String sql1 = "from AfTemplateOrgRelation ator where ator.id.orgId=?";

			Query atorquery = session.createQuery(sql1);
			atorquery.setString(0, orgInfo.getOrgId());
			List atorLst = atorquery.list();

			if (atorLst != null && atorLst.size() > 0) {
				for (int i = 0; i < atorLst.size(); i++) {
					AfTemplateOrgRelation ator = (AfTemplateOrgRelation) atorLst
							.get(i);
					session.delete(ator);
				}
			}
			String sql2 = "from AfTemplateCollRep ator where ator.id.orgId=?";
			Query atorquery2 = session.createQuery(sql2);
			atorquery2.setString(0, orgInfo.getOrgId());
			atorLst = atorquery2.list();
			if (atorLst != null && atorLst.size() > 0) {
				for (int i = 0; i < atorLst.size(); i++) {
					AfTemplateCollRep ator = (AfTemplateCollRep) atorLst.get(i);
					session.delete(ator);
				}
			}
			String sql3 = "from AfTemplateOuterRep ator where ator.id.orgId=?";
			Query atorquery3 = session.createQuery(sql3);
			atorquery3.setString(0, orgInfo.getOrgId());
			atorLst = atorquery3.list();
			if (atorLst != null && atorLst.size() > 0) {
				for (int i = 0; i < atorLst.size(); i++) {
					AfTemplateOuterRep ator = (AfTemplateOuterRep) atorLst
							.get(i);
					session.delete(ator);
				}
			}
			session.delete(orgInfo);
			session.flush();
		} catch (Exception e) {
			log.printStackTrace(e);
			result = false;
		} finally {
			// 如果连接存在，则断开，结束会话，返回
			if (conn != null)
				conn.endTransaction(result);
		}
		return result;
	}

	/**
	 * 已使用hibernate 卞以刚 2011-12-21 根据ID和机构权限找出一家机构的子机构
	 * 
	 * @param orgId
	 * @param orgTemp
	 * @return
	 */
	public static List getChildList(String orgId) {

		DBConn dbconn = null;

		Session session = null;

		Query query = null;

		if (orgId == null)
			return null;

		try {

			dbconn = new DBConn();

			session = dbconn.openSession();

			StringBuffer sb = new StringBuffer(
					"from AfOrg oi where oi.preOrgId='" + orgId + "' ");

			query = session.createQuery(sb.toString());

			List list = query.list();

			return list;

		} catch (Exception e) {

			log.printStackTrace(e);

			return null;

		} finally {

			if (session != null)

				dbconn.closeSession();
		}

	}

	/**
	 * 已使用hibernate 卞以刚 2011-12-21 根据机构ID得到所有的下属用户
	 * 
	 * @param orgId
	 *            String
	 * 
	 * @return boolean 是否存在下属用户
	 */
	public static boolean hasUsers(String orgId) {
		DBConn conn = null;
		Session session = null;
		boolean result = true;

		try {
			conn = new DBConn();
			session = conn.openSession();

			String hql = "from Operator ui where ui.org.orgId=?";
			List list = session.createQuery(hql).setString(0, orgId).list();
			if (list != null && list.size() == 0) {
				result = false;
			}
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			if (session != null)
				conn.closeSession();
		}
		return result;
	}

	/**
	 * 已使用hibernate 卞以刚 2011-12-22 生成机构树的XML文件
	 * 
	 * @author Nick
	 * @return boolean 生成机构树是否成功
	 */
	public static boolean makeOrgTree() {

		boolean result = false;
		/** 生成XML */
		OrgTreeInterface orgTree = new BaseOrgTreeIterator();
		/** 已使用hibernate 卞以刚 2011-12-22 **/
		if (orgTree.createTreeForTagXml() && orgTree.createTreeForVorgRelXml()) {
			result = true;
		}
		return result;
	}

	/**
	 * 已使用hibernate 卞以刚 2011-12-21
	 * <p>
	 * 描述:查询子机构数量
	 * </p>
	 * <p>
	 * 参数:
	 * </p>
	 * <p>
	 * 日期：2008-1-3
	 * </p>
	 * <p>
	 * 作者：曹发根
	 * </p>
	 */
	public static int selectSubOrgCount(OrgInfoForm orgInfoForm) {
		int count = 0;
		DBConn conn = null;
		Session session = null;

		if (orgInfoForm == null)
			return count;
		try {
			StringBuffer hql = new StringBuffer(
					"select count(*) from AfOrg oi where oi.preOrgId='"
							+ orgInfoForm.getParentOrgId() + "'");
			StringBuffer where = new StringBuffer();

			// 加入机构名称查询条件
			if (orgInfoForm.getOrgName() != null
					&& !orgInfoForm.getOrgName().equals(""))
				where.append("and ont.orgName like '%"
						+ orgInfoForm.getOrgName() + "%'");
			hql.append(where.toString());

			conn = new DBConn();
			session = conn.openSession();
			Query query = session.createQuery(hql.toString());
			List list = query.list();

			if (list != null && list.size() == 1) {
				count = ((Integer) list.get(0)).intValue();
			}
		} catch (HibernateException he) {

			he.printStackTrace();
			log.printStackTrace(he);
		} catch (Exception e) {
			e.printStackTrace();
			log.printStackTrace(e);
		} finally {
			// 如果连接存在，则断开，结束会话，返回
			if (conn != null)
				conn.closeSession();
		}
		return count;
	}

	/**
	 * 已使用hibernate 卞以刚 2011-12-21 获得下级机构列表信息
	 * 
	 * @param orgNetForm
	 * @param offset
	 * @param limit
	 * @return
	 */
	public static List selectSubOrgList(OrgInfoForm orgInfoForm, int offset,
			int limit) {
		if (orgInfoForm == null)
			return null;

		List refVals = null;
		DBConn conn = null;
		Session session = null;

		try {
			// 查询条件HQL的生成
			StringBuffer hql = new StringBuffer(
					"from AfOrg ont where ont.preOrgId='"
							+ orgInfoForm.getParentOrgId() + "'");
			StringBuffer where = new StringBuffer();

			// 加入机构名称查询条件
			if (orgInfoForm.getOrgName() != null
					&& !orgInfoForm.getOrgName().equals(""))
				where.append("and ont.orgName like '%"
						+ orgInfoForm.getOrgName() + "%'");
			where.append(" order by ont.orgName,ont.orgLevel");
			hql.append(where.toString());

			conn = new DBConn();
			session = conn.openSession();
			Query query = session.createQuery(hql.toString());
			query.setFirstResult(offset).setMaxResults(limit);
			List list = query.list();

			if (list != null) {
				refVals = new ArrayList();
				OrgInfoForm orgInfoFormTemp = null;
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						AfOrg afOrg = (AfOrg) list.get(i);
						orgInfoFormTemp = new OrgInfoForm();
						TranslatorUtil.copyPersistenceToVo(afOrg,
								orgInfoFormTemp);

						refVals.add(orgInfoFormTemp);
					}
				}
			}
		} catch (HibernateException he) {
			refVals = null;
			he.printStackTrace();
			log.printStackTrace(he);
		} catch (Exception e) {
			refVals = null;
			e.printStackTrace();
			log.printStackTrace(e);
		} finally {
			// 如果连接存在，则断开，结束会话，返回
			if (conn != null)
				conn.closeSession();
		}
		return refVals;
	}

	/**
	 * 已使用hibernate 卞以刚 2011-12-21
	 * <p>
	 * 描述:查询子机构数量
	 * <p>
	 * 作者：曹发根
	 * </p>
	 */
	public static int selectSubOrgCount(OrgInfoForm orgInfoForm,
			Operator operator, Integer templateType) {
		int count = 0;
		DBConn conn = null;
		Session session = null;

		if (operator == null)
			return count;
		try {

			StringBuffer hql = new StringBuffer(
					"select count(*) from AfOrg oi where oi.orgId in ("
							+ operator.getChildRepSearchPopedom().replace(
									"orgRepId", "orgId")
							+ " and viewOrgRep.childRepId in (select tmpl.id.templateId from AfTemplate tmpl where tmpl.templateType='"
							+ templateType + "'))");

			StringBuffer where = new StringBuffer();

			// 加入机构名称查询条件
			if (orgInfoForm.getOrgName() != null
					&& !orgInfoForm.getOrgName().equals(""))
				where.append("and oi.orgName like '%"
						+ orgInfoForm.getOrgName() + "%'");
			hql.append(where.toString());

			conn = new DBConn();
			session = conn.openSession();
			Query query = session.createQuery(hql.toString());
			List list = query.list();

			if (list != null && list.size() == 1) {
				count = ((Integer) list.get(0)).intValue();
			}
		} catch (HibernateException he) {

			he.printStackTrace();
			log.printStackTrace(he);
		} catch (Exception e) {
			e.printStackTrace();
			log.printStackTrace(e);
		} finally {
			// 如果连接存在，则断开，结束会话，返回
			if (conn != null)
				conn.closeSession();
		}
		return count;
	}

	/**
	 * 已使用hibernate 卞以刚 2011-12-21 获得下级机构列表信息
	 * 
	 * @param orgNetForm
	 * @param offset
	 * @param limit
	 * @return
	 */
	public static List selectSubOrgList(OrgInfoForm orgInfoForm,
			Operator operator, Integer templateType, int offset, int limit) {
		if (operator == null)
			return null;

		List refVals = null;
		DBConn conn = null;
		Session session = null;

		try {
			// 查询条件HQL的生成
			StringBuffer hql = new StringBuffer(
					"from AfOrg ont where ont.orgId in ("
							+ operator.getChildRepSearchPopedom().replace(
									"orgRepId", "orgId")
							+ " and viewOrgRep.childRepId in (select tmpl.id.templateId from AfTemplate tmpl where tmpl.templateType='"
							+ templateType + "'))");

			StringBuffer where = new StringBuffer();

			// 加入机构名称查询条件
			if (orgInfoForm.getOrgName() != null
					&& !orgInfoForm.getOrgName().equals(""))
				where.append("and ont.orgName like '%"
						+ orgInfoForm.getOrgName() + "%'");

			where.append(" order by ont.orgId,ont.orgLevel");
			hql.append(where.toString());

			conn = new DBConn();
			session = conn.openSession();
			Query query = session.createQuery(hql.toString());
			query.setFirstResult(offset).setMaxResults(limit);
			List list = query.list();

			if (list != null) {
				refVals = new ArrayList();
				OrgInfoForm orgInfoFormTemp = null;
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						AfOrg afOrg = (AfOrg) list.get(i);
						orgInfoFormTemp = new OrgInfoForm();
						TranslatorUtil.copyPersistenceToVo(afOrg,
								orgInfoFormTemp);

						refVals.add(orgInfoFormTemp);
					}
				}
			}
		} catch (HibernateException he) {
			refVals = null;
			he.printStackTrace();
			log.printStackTrace(he);
		} catch (Exception e) {
			refVals = null;
			e.printStackTrace();
			log.printStackTrace(e);
		} finally {
			// 如果连接存在，则断开，结束会话，返回
			if (conn != null)
				conn.closeSession();
		}
		return refVals;
	}

	/**
	 * 已使用hibernate 卞以刚 2011-12-21 获取分支机构列表
	 * 
	 * @param preOrgId
	 *            上级机构ID
	 * @return List
	 */
	public static List selectSubOrgList(String preOrgId) {
		if (preOrgId == null)
			return null;
		DBConn conn = null;
		Session session1 = null;
		List orgNetList = null;

		try {
			conn = new DBConn();
			session1 = conn.openSession();

			orgNetList = new ArrayList();

			String hql = "from AfOrg ont where ont.preOrgId = '"
					+ preOrgId.trim() + "'";

			List list = session1.find(hql.toString());

			if (list != null && list.size() > 0) {
				OrgInfoForm orgNetFormTemp = null;
				AfOrg orgNetPersistence = null;

				for (int i = 0; i < list.size(); i++) {
					orgNetPersistence = (AfOrg) list.get(i);
					orgNetFormTemp = new OrgInfoForm();
					TranslatorUtil.copyPersistenceToVo(orgNetPersistence,
							orgNetFormTemp);

					orgNetList.add(orgNetFormTemp);
				}
			}
		} catch (HibernateException he) {
			log.printStackTrace(he);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			// 如果连接存在，则断开，结束会话，返回
			if (conn != null)
				conn.closeSession();
		}
		return orgNetList;
	}

	/**
	 * 已使用hibernate 卞以刚 2011-12-21 影响对象：AfOrg 根据机构id查询机构信息
	 * 
	 * @param orgId
	 *            机构id
	 * @return AfOrg
	 */
	public static AfOrg selectOne(String orgId) {
		AfOrg orgNetResult = null;
		DBConn conn = null;
		Session session = null;

		try {
			conn = new DBConn();
			session = conn.openSession();

			String hql = "from AfOrg ont where ont.orgId = '" + orgId.trim()
					+ "'";
			List list = session.find(hql.toString());

			if (list != null && list.size() > 0) {
				orgNetResult = (AfOrg) list.get(0);
			}
		} catch (HibernateException he) {
			log.printStackTrace(he);
		} catch (Exception e) {
			orgNetResult = null;
			log.printStackTrace(e);
		} finally {
			// 如果连接存在，则断开，结束会话，返回
			if (conn != null)
				conn.closeSession();
		}
		return orgNetResult;
	}

	/**
	 * 已使用hibernate 卞以刚 2011-12-21 影响对象：AfOrg MPurOrg 根据用户组ID取得该用户组已经设置过的机构
	 * 
	 * @param userGrpId
	 *            用户组ID
	 * @param powType
	 *            权限类型
	 * @return
	 */
	public static List getUserGrpOrgPopedom(Long userGrpId, Integer powType) {
		List result = null;
		DBConn conn = null;
		Session session = null;
		try {
			if (userGrpId != null) {
				conn = new DBConn();
				session = conn.openSession();
				String hql = "select distinct on.orgId,on.orgName from AfOrg on where on.orgId in (select distinct mpo.comp_id.org.orgId from MPurOrg mpo "
						+ "where mpo.comp_id.MUserGrp.userGrpId="
						+ userGrpId
						+ " and mpo.comp_id.powType=" + powType + ")";

				Query query = session.createQuery(hql);
				List list = query.list();
				if (list != null && list.size() > 0) {
					result = new ArrayList();
					for (int i = 0; i < list.size(); i++) {
						Object[] object = (Object[]) list.get(i);
						OrgNetForm form = new OrgNetForm();
						form.setOrg_id(object[0].toString());
						form.setOrg_name(object[1].toString());
						result.add(form);
					}
				}
			}
		} catch (Exception ex) {
			result = null;
			log.printStackTrace(ex);
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return result;
	}

	/**
	 * 已使用hibernate 卞以刚 2011-12-21 影响对象：AfOrg 取得当前机构的下级机构(添加支持虚拟机构)
	 * 
	 * @param orgId
	 * @return
	 */
	public static String selectSubOrgIds(String orgId) {

		String lowerOrgIds = "";
		DBConn conn = null;
		Session session = null;

		try {
			conn = new DBConn();
			session = conn.openSession();

			String hql = "from AfOrg ont where ont.preOrgId = '" + orgId.trim()
					+ "' or ont.isCollect=1";

			List list = session.find(hql.toString());

			if (list != null && list.size() > 0) {

				for (int i = 0; i < list.size(); i++) {
					AfOrg orgNet = (AfOrg) list.get(i);
					lowerOrgIds = lowerOrgIds.equals("") ? "'"
							+ orgNet.getOrgId() + "'" : lowerOrgIds + "," + "'"
							+ orgNet.getOrgId() + "'";
				}
			}
		} catch (HibernateException he) {
			log.printStackTrace(he);
		} catch (Exception e) {
			lowerOrgIds = null;
			log.printStackTrace(e);
		} finally {
			// 如果连接存在，则断开，结束会话，返回
			if (conn != null)
				conn.closeSession();
		}
		return lowerOrgIds;
	}

	/**
	 * 已使用hibernate 卞以刚 2011-12-21 影响对象：AfOrg 根据机构id，查询该机构下所有子机构
	 * 
	 * @param orgId
	 * @return
	 */
	public static String selectAllLowerOrgIds(String orgId) {
		String allLowerOrgIds = "";
		String lowerOrgIds = "'" + orgId.trim() + "'";
		DBConn conn = null;
		Session session = null;
		try {
			conn = new DBConn();
			session = conn.openSession();
			while (true) {
				String temp = lowerOrgIds;
				lowerOrgIds = "";
				String hql = "from AfOrg ont where ont.preOrgId in (" + temp
						+ ")";

				List list = session.find(hql.toString());

				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						AfOrg orgNet = (AfOrg) list.get(i);
						lowerOrgIds = lowerOrgIds.equals("") ? "'"
								+ orgNet.getOrgId() + "'" : lowerOrgIds + ",'"
								+ orgNet.getOrgId() + "'";
					}
				}

				if (lowerOrgIds.equals("")) {
					hql = "from AfOrg ont where ont.isCollect=1";

					list = session.find(hql.toString());

					if (list != null && list.size() > 0) {
						for (int i = 0; i < list.size(); i++) {
							AfOrg orgNet = (AfOrg) list.get(i);
							lowerOrgIds = lowerOrgIds.equals("") ? "'"
									+ orgNet.getOrgId() + "'" : lowerOrgIds
									+ ",'" + orgNet.getOrgId() + "'";
						}
					}
					allLowerOrgIds = lowerOrgIds.equals("") ? allLowerOrgIds
							: allLowerOrgIds + "," + lowerOrgIds;
					break;
				} else
					allLowerOrgIds = allLowerOrgIds.equals("") ? lowerOrgIds
							: allLowerOrgIds + "," + lowerOrgIds;
			}
		} catch (HibernateException he) {
			log.printStackTrace(he);
		} catch (Exception e) {
			lowerOrgIds = null;
			log.printStackTrace(e);
		} finally {
			// 如果连接存在，则断开，结束会话，返回
			if (conn != null)
				conn.closeSession();
		}
		return allLowerOrgIds;
	}

	/**
	 * 已使用hibernate 卞以刚 2011-12-21 根据机构ID取得相应机构信息
	 * 
	 * @param orgIds
	 * @return List 机构列表
	 */
	public static List selectOrgByIds(String orgIds, String reportflg) {
		List result = null;
		DBConn conn = null;
		Session session = null;

		try {
			conn = new DBConn();
			session = conn.openSession();
			orgIds = orgIds
					+ " and viewOrgRep.childRepId in (select t.id.templateId from "
					+ "AfTemplate t where t.templateType=" + reportflg
					+ ") group by viewOrgRep.orgId ";
			String hql = "from AfOrg ont where ont.orgId in(" + orgIds
					+ ") order by ont.regionId,ont.preOrgId,ont.orgId";
			List list = session.find(hql.toString());

			if (list != null && list.size() > 0) {
				result = new ArrayList();
				for (int i = 0; i < list.size(); i++) {
					AfOrg orgNet = (AfOrg) list.get(i);
					result.add(orgNet);
				}
			}
		} catch (HibernateException he) {
			log.printStackTrace(he);
		} catch (Exception e) {
			result = null;
			log.printStackTrace(e);
		} finally {
			// 如果连接存在，则断开，结束会话，返回
			if (conn != null)
				conn.closeSession();
		}
		return result;
	}

	/**
	 * 已使用hibernate 卞以刚 2011-12-21 影响对象：AfOrg 根据机构ID取得相应机构信息
	 * 
	 * @param orgIds
	 * @return List 机构列表
	 */
	public static List selectOrgByIds(String orgIds) {
		List result = null;
		DBConn conn = null;
		Session session = null;

		try {
			conn = new DBConn();
			session = conn.openSession();
			String hql = "from AfOrg ont where ont.orgId in(" + orgIds
					+ ") order by ont.orgId";
			List list = session.find(hql.toString());

			if (list != null && list.size() > 0) {
				result = new ArrayList();
				for (int i = 0; i < list.size(); i++) {
					AfOrg orgNet = (AfOrg) list.get(i);
					result.add(orgNet);
				}
			}
		} catch (HibernateException he) {
			log.printStackTrace(he);
		} catch (Exception e) {
			result = null;
			log.printStackTrace(e);
		} finally {
			// 如果连接存在，则断开，结束会话，返回
			if (conn != null)
				conn.closeSession();
		}
		return result;
	}

	/**
	 * 已使用hibernate 卞以刚 2011-12-21 根据机构id，查询该机构下所有子机构
	 * 
	 * @param orgId
	 * @return
	 */
	public static String selectRegionId(String orgId) {
		String regionId = "";
		String lowerOrgIds = "'" + orgId.trim() + "'";
		DBConn conn = null;
		Session session = null;
		try {
			conn = new DBConn();
			session = conn.openSession();

			String hql = "from AfOrg ont where ont.orgId=" + orgId;

			List list = session.find(hql.toString());

			if (list != null && list.size() > 0) {
				AfOrg orgNet = (AfOrg) list.get(0);
				regionId = orgNet.getRegionId() != null ? String.valueOf(orgNet
						.getRegionId()) : "";

			}

		} catch (HibernateException he) {
			log.printStackTrace(he);
		} catch (Exception e) {
			lowerOrgIds = null;
			log.printStackTrace(e);
		} finally {
			// 如果连接存在，则断开，结束会话，返回
			if (conn != null)
				conn.closeSession();
		}
		return regionId;
	}

	/**
	 * 已使用hibernate 卞以刚 2011-12-21 得到应报机构列表的数量
	 * 
	 * @param templateId
	 *            模板id
	 * @param versionId
	 *            版本号
	 * @param orgId
	 *            该报表报送机构
	 * @param childOrgIds
	 *            用户机构权限
	 * @param type
	 *            汇总类型
	 * @return int
	 */
	public int getMustOrgCount(String templateId, String versionId,
			String childOrgIds, String orgId, Integer type) {
		int count = 0;
		DBConn conn = null;
		Session session = null;

		if (templateId == null || templateId.equals("") || versionId == null
				|| versionId.equals("") || childOrgIds == null
				|| childOrgIds.equals(""))
			return count;

		try {
			conn = new DBConn();
			session = conn.openSession();
			String sql = "";
			// 总行汇虚拟机构
			if (type.equals(Integer.valueOf(1))
					|| type.equals(Integer.valueOf(4))) {
				sql = "select count(org.orgId) from AfOrg org where org.orgId in "
						+ "(select distinct M.id.orgId from AfViewReport M where "
						+
						// "M.comp_id.orgId in (" + childOrgIds + ") and " +
						"M.id.templateId='"
						+ templateId
						+ "' "
						+ " and  M.id.versionId='" + versionId + "'";
				// ") and org.isCollect=1";
				if (type.equals(Integer.valueOf(1)))
					sql += ") and org.isCollect=1 and org.orgType='"
							+ com.fitech.gznx.common.Config.COLLECT_ORG_PARENT_ID
							+ "'";
				if (type.equals(Integer.valueOf(4)))
					sql += ") and org.isCollect=1 and org.orgType='"
							+ com.fitech.gznx.common.Config.COLLECT_ORG_PARENT_QT_ID
							+ "'";

			}
			// 虚拟机构汇分行
			else if (type.equals(Integer.valueOf(2))) {
				sql = "select count(org.orgId) from AfOrg org where org.orgId in "
						+ "(select distinct M.id.orgId from AfViewReport M,AfCollectRelation afc"
						+ " where afc.id.orgId=M.id.orgId "
						+
						// " and M.id.orgId in (" + childOrgIds + ")" +
						" and M.id.templateId='"
						+ templateId
						+ "' and M.id.versionId='"
						+ versionId
						+ "'"
						+ " and afc.id.collectId='" + orgId + "')";

			}
			// 分行汇支行
			else {
				/*
				 * sql =
				 * "select count(org.orgId) from AfOrg org where org.orgId in "
				 * +
				 * "(select distinct M.id.orgId from AfViewReport M where M.id.orgId in "
				 * + "(" + childOrgIds + ")  and M.id.templateId='" + templateId
				 * + "' " +" and M.id.versionId='" + versionId + "')";
				 */
				sql = "select count(org.orgId) from AfOrg org where org.orgId in "
						+ "(select distinct M.id.orgId from AfViewReport M where M.id.orgId in "
						+ "(select aor.orgId from AfOrg aor where aor.preOrgId='"
						+ orgId
						+ "')"
						+
						// " and aor.orgId in (" + childOrgIds + ")) " +
						" and M.id.templateId='"
						+ templateId
						+ "' "
						+ " and M.id.versionId='" + versionId + "')";

			}

			List list = session.createQuery(sql).list();

			if (list != null && list.size() > 0) {
				count = ((Integer) list.get(0)).intValue();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return count;
	}

	/**
	 * 已使用hibernate 卞以刚 2011-12-21 影响对象：AfOrg AfViewReport AfCollectRelation
	 * 得到应报机构列表
	 * 
	 * @param childRepId
	 * @param versionId
	 * @param childOrgIds
	 * @return
	 */
	public List getMustOrgList(String templateId, String versionId,
			String childOrgIds, String orgId, Integer type) {
		List result = null;
		DBConn conn = null;
		Session session = null;

		if (templateId == null || templateId.equals("") || versionId == null
				|| versionId.equals("") || childOrgIds == null
				|| childOrgIds.equals(""))
			return result;

		try {
			conn = new DBConn();
			session = conn.openSession();

			String sql = "";

			// 总行汇虚拟机构
			if (type.equals(Integer.valueOf(1))
					|| type.equals(Integer.valueOf(4))) {
				sql = "select org from AfOrg org where org.orgId in "
						+ "(select distinct M.comp_id.orgId from AfViewReport M where "
						// + "M.comp_id.orgId in (" + childOrgIds + ")  and "
						+ "M.comp_id.templateId='" + templateId + "' "
						+ "  and  M.comp_id.versionId='" + versionId + "'";
				// + ") and org.isCollect=1";
				if (type.equals(Integer.valueOf(1)))
					sql += ") and org.isCollect=1 and org.orgType='"
							+ com.fitech.gznx.common.Config.COLLECT_ORG_PARENT_ID
							+ "'";
				if (type.equals(Integer.valueOf(4)))
					sql += ") and org.isCollect=1 and org.orgType='"
							+ com.fitech.gznx.common.Config.COLLECT_ORG_PARENT_QT_ID
							+ "'";

			}
			// 虚拟机构汇分行
			else if (type.equals(Integer.valueOf(2))
					|| type.equals(Integer.valueOf(5))) {
				sql = "select org from AfOrg org where org.orgId in "
						+ "(select distinct M.id.orgId from AfViewReport M,AfCollectRelation afc"
						+ " where afc.id.orgId=M.id.orgId "
						+
						// " and M.id.orgId in (" + childOrgIds + ")" +
						" and M.id.templateId='" + templateId
						+ "' and M.id.versionId='" + versionId + "'"
						+ " and afc.id.collectId='" + orgId + "')";

			}
			// 分行汇支行
			else {
				sql = "select org from AfOrg org where org.orgId in "
						+ "(select distinct M.id.orgId from AfViewReport M where M.id.orgId in "
						+
						// "(" + childOrgIds + ")
						"(select aor.orgId from AfOrg aor where aor.preOrgId='"
						+ orgId + "')"
						+
						// "' and aor.orgId in (" + childOrgIds + ")) " +
						"and M.id.templateId='" + templateId + "' "
						+ "and M.id.versionId='" + versionId + "')";

			}
			StringBuffer sb = new StringBuffer(sql);
			sb.append(" and (org.orgId in (select r.id.orgId from AfTemplateCollRep r where r.id.templateId='"
					+ templateId + "' and r.id.versionId='" + versionId + "'))");
			List list = session.createQuery(sb.toString()).list();

			if (list != null && list.size() > 0) {
				result = new ArrayList();
				for (int j = 0; j < list.size(); j++) {
					AfOrg org = (AfOrg) list.get(j);
					result.add(org);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return result;
	}

	/**
	 * jdbc技术 需要修改 卞以刚 2011-12-21 修改所有表的机构信息
	 * 
	 * @param orgInfoForm
	 * @return
	 */
	public static boolean updateAllOrgInfo(OrgInfoForm orgInfoForm) {
		StringBuffer addsql = null;
		DBConn conn = null;
		Session session = null;
		Connection connection = null;
		Statement stmt = null;
		boolean result = false;
		try {
			conn = new DBConn();
			session = conn.beginTransaction();
			connection = session.connection();
			connection.setAutoCommit(false);
			stmt = connection.createStatement();
			addsql = new StringBuffer();

			addsql.append("update AF_ORG set PRE_ORG_ID=")
					.append(orgInfoForm.getNewOrgId())
					.append(" where PRE_ORG_ID=")
					.append(orgInfoForm.getNewOrgId());
			stmt.addBatch(addsql.toString().toLowerCase());

			addsql = new StringBuffer();
			addsql.append("update ORG set PRE_ORG_ID=")
					.append(orgInfoForm.getNewOrgId())
					.append(" where PRE_ORG_ID=")
					.append(orgInfoForm.getNewOrgId());
			stmt.addBatch(addsql.toString().toLowerCase());

			addsql = new StringBuffer();
			addsql.append("update AF_REPORT set ORG_ID=")
					.append(orgInfoForm.getNewOrgId()).append(" where ORG_ID=")
					.append(orgInfoForm.getOrgId());
			stmt.addBatch(addsql.toString().toLowerCase());

			addsql = new StringBuffer();
			addsql.append("update REPORT_IN set ORG_ID=")
					.append(orgInfoForm.getNewOrgId()).append(" where ORG_ID=")
					.append(orgInfoForm.getOrgId());
			stmt.addBatch(addsql.toString().toLowerCase());

			addsql = new StringBuffer();
			addsql.append("update DEPARTMENT set ORG_ID=")
					.append(orgInfoForm.getNewOrgId()).append(" where ORG_ID=")
					.append(orgInfoForm.getOrgId());
			stmt.addBatch(addsql.toString().toLowerCase());

			addsql = new StringBuffer();
			addsql.append("update M_PUR_ORG set ORG_ID=")
					.append(orgInfoForm.getNewOrgId()).append(" where ORG_ID=")
					.append(orgInfoForm.getOrgId());
			stmt.addBatch(addsql.toString().toLowerCase());

			addsql = new StringBuffer();
			addsql.append("update m_rep_range set ORG_ID=")
					.append(orgInfoForm.getNewOrgId()).append(" where ORG_ID=")
					.append(orgInfoForm.getOrgId());
			stmt.addBatch(addsql.toString().toLowerCase());

			addsql = new StringBuffer();
			addsql.append("update m_user_grp set set_org_id=")
					.append(orgInfoForm.getNewOrgId())
					.append(" where set_org_id=")
					.append(orgInfoForm.getOrgId());
			stmt.addBatch(addsql.toString().toLowerCase());

			addsql = new StringBuffer();
			addsql.append("update OPERATOR set ORG_ID=")
					.append(orgInfoForm.getNewOrgId()).append(" where ORG_ID=")
					.append(orgInfoForm.getOrgId());
			stmt.addBatch(addsql.toString().toLowerCase());

			addsql = new StringBuffer();
			addsql.append("update report_in set ORG_ID=")
					.append(orgInfoForm.getNewOrgId()).append(" where ORG_ID=")
					.append(orgInfoForm.getOrgId());
			stmt.addBatch(addsql.toString().toLowerCase());

			addsql = new StringBuffer();
			addsql.append("update AF_ORG set ORG_NAME='")
					.append(orgInfoForm.getOrgName()).append("',REGION_ID=")
					.append(orgInfoForm.getOrgRegion()).append(",ORG_ID=")
					.append(orgInfoForm.getNewOrgId()).append(" where ORG_ID=")
					.append(orgInfoForm.getOrgId());
			stmt.addBatch(addsql.toString().toLowerCase());

			addsql = new StringBuffer();
			addsql.append("update ORG set ORG_NAME='")
					.append(orgInfoForm.getOrgName()).append("',REGION_ID=")
					.append(orgInfoForm.getOrgRegion()).append(",ORG_ID=")
					.append(orgInfoForm.getNewOrgId()).append(" where ORG_ID=")
					.append(orgInfoForm.getOrgId());
			stmt.addBatch(addsql.toString().toLowerCase());

			addsql = new StringBuffer();
			addsql.append("update AF_COLLECT_RELATION set ORG_ID=")
					.append(orgInfoForm.getNewOrgId()).append(" where ORG_ID=")
					.append(orgInfoForm.getOrgId());
			stmt.addBatch(addsql.toString().toLowerCase());

			addsql = new StringBuffer();
			addsql.append("update AF_TEMPLATE_ORG_RELATION set ORG_ID=")
					.append(orgInfoForm.getNewOrgId()).append(" where ORG_ID=")
					.append(orgInfoForm.getOrgId());
			stmt.addBatch(addsql.toString().toLowerCase());

			stmt.executeBatch();
			result = true;
		} catch (Throwable e1) {
			e1.printStackTrace();
			result = false;
		} finally {
			try {
				if (result == true) {
					connection.commit();
				} else {
					connection.rollback();
				}
				connection.setAutoCommit(result);
				if (stmt != null)
					stmt.close();
				if (connection != null)
					connection.close();
				if (session != null)
					session.close();
				if (conn != null)
					conn.closeSession();
			} catch (SQLException e1) {
				e1.printStackTrace();
			} catch (HibernateException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 查询总行机构
	 * 
	 * @return
	 */
	public static String findTopOrg() {
		DBConn conn = null;
		Session session = null;
		List resultList = null;

		try {
			conn = new DBConn();
			session = conn.openSession();

			String hql = "select oi.orgId from AfOrg oi where oi.preOrgId='0' and oi.isCollect=0";
			List list = session.createQuery(hql).list();
			if (list != null && list.size() != 0) {
				resultList = new ArrayList();
				for (int i = 0; i < list.size(); i++) {
					resultList.add((String) list.get(i));
				}
			}

		} catch (Exception e) {
			log.printStackTrace(e);
			return null;
		} finally {
			if (session != null)
				conn.closeSession();
		}
		return (String) resultList.get(0);
	}

	/**
	 * 判断是否存在
	 * 
	 * @param orgId
	 * @return
	 */
	public static boolean isExistCollRule(AfTemplateCollRuleForm afCollRule) {
		boolean result = false;
		List list = null;
		// 连接对象和会话对象初始化
		DBConn conn = null;
		Session session = null;

		try {
			// conn对象的实例化
			conn = new DBConn();
			// 打开连接开始会话
			session = conn.openSession();
			String hsql = "from AfTemplateCollRule af where af.id.templateId='"
					+ afCollRule.getTemplate_id() + "' and af.id.orgId='"
					+ afCollRule.getOrg_id() + "'  and af.id.versionId='"
					+ afCollRule.getVersion_id() + "'";
			Query query = session.createQuery(hsql);
			list = query.list();
			if (list != null && list.size() > 0) {
				result = true;
			}
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			// 如果连接存在，则断开，结束会话
			if (conn != null)
				conn.closeSession();
		}
		return result;
	}

	public static boolean isContainsOrgId(String orgId, String templateId,
			String subOrgId) {
		// TODO Auto-generated method stub

		boolean result = false;
		List list = null;
		// 连接对象和会话对象初始化
		DBConn conn = null;
		Session session = null;

		try {
			// conn对象的实例化
			conn = new DBConn();
			// 打开连接开始会话AFTemplateCollRule
			session = conn.openSession();
			String hsql = "from AfTemplateCollRule af where af.id.templateId=? and af.id.orgId =?";
			Query query = session.createQuery(hsql);
			query.setString(0, templateId);
			query.setString(1, orgId);
			list = query.list();
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				AfTemplateCollRule object = (AfTemplateCollRule) iterator
						.next();
				String collFormula = object.getCollFormula();
				collFormula = com.cbrc.smis.proc.util.FitechUtil
						.parseFomual(collFormula)[1];
				if (null != collFormula && !"".equals(collFormula)) {
					String[] orgIds = collFormula.split(",");
					for (int i = 0; i < orgIds.length; i++) {
						if (orgIds[i].equals(subOrgId)) {
							result = true;
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			// 如果连接存在，则断开，结束会话
			if (conn != null)
				conn.closeSession();
		}
		return result;

	}

	public static boolean updateAfTemplateCollRule(
			AfTemplateCollRuleForm afRuleForm) {
		// TODO Auto-generated method stub
		boolean result = false;
		List list = null;
		// 连接对象和会话对象初始化
		DBConn conn = null;
		Session session = null;

		// 查询条件HQL的生成
		try {
			// conn对象的实例化
			conn = new DBConn();
			// 打开连接开始会话
			session = conn.beginTransaction();

			/** 1 增加机构信息 */
			// 得到父节点的详细信息

			AfTemplateCollRule orgRule = new AfTemplateCollRule();
			AfTemplateCollRuleId orgRuleId = new AfTemplateCollRuleId();
			orgRuleId.setOrgId(afRuleForm.getOrg_id());
			orgRuleId.setTemplateId(afRuleForm.getTemplate_id());
			orgRuleId.setVersionId(afRuleForm.getVersion_id());
			orgRule.setCollFormula(afRuleForm.getColl_formula());
			orgRule.setCollSchema(Integer.valueOf(afRuleForm.getColl_schema()));
			orgRule.setId(orgRuleId);
			session.update(orgRule);
			session.flush();
			result = true;
		} catch (Exception e) {
			log.printStackTrace(e);
			result = false;
		} finally {
			// 如果连接存在，则断开，结束会话，返回
			if (conn != null)
				conn.endTransaction(result);
		}
		return result;

	}

	public static int getGaChaDoNUM(String childRepId, String versionId,
			Integer year, Integer term, Integer day, Integer curId,
			Integer repFreqId, String orgId, boolean ignoreCollectOrg,
			String orgIds) {

		int result = 0;
		DBConn conn = null;
		Session session = null;

		if (childRepId == null || childRepId.equals("") || versionId == null
				|| versionId.equals("") || orgIds == null || orgIds.equals(""))
			return result;

		String hql = null;

		hql = "select count(ri.repId) from AfReport ri where"
				+ " ri.templateId='"
				+ childRepId
				+ "' and ri.versionId='"
				+ versionId
				+ "' and ri.year="
				+ year
				+ " and ri.term="
				+ term
				+ " and ri.day="
				+ day
				+ " and ri.curId="
				+ curId
				+ " and ri.repFreqId="
				+ repFreqId
				+ " and ri.orgId in ( "
				+ orgIds
				+ ")"
				+ " and ri.times>0"
				+ " and ri.checkFlag in("
				+ (com.cbrc.smis.common.Config.IS_NEED_CHECK == 1 ? "1" : "0,1")
				+ ")";

		conn = new DBConn();
		session = conn.openSession();
		try {
			List list = session.createQuery(hql).list();

			if (list != null && list.size() > 0) {
				result = ((Integer) list.get(0)).intValue();
			}
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return result;
	}

	private static String getValidDateSql(AFReportForm afReportForm) {

		if (afReportForm.getDate() == null)
			return null;

		String datesql = "";
		// 处理月报及以上情况
		// String[] dates =
		// DateUtil.getLastMonth(afReportForm.getDate()).split("-");
		String[] dates = afReportForm.getDate().split("-");

		// int year = Integer.parseInt(dates[0]);
		int term = Integer.parseInt(dates[1]);
		// int day = Integer.parseInt(dates[2]);

		String rep_freq = "";
		if (term == 12)
			rep_freq = com.fitech.gznx.common.Config.FREQ_MONTH
					+ Config.SPLIT_SYMBOL_COMMA
					+ com.fitech.gznx.common.Config.FREQ_SEASON
					+ Config.SPLIT_SYMBOL_COMMA
					+ com.fitech.gznx.common.Config.FREQ_HALFYEAR
					+ Config.SPLIT_SYMBOL_COMMA
					+ com.fitech.gznx.common.Config.FREQ_YEAR
					+ Config.SPLIT_SYMBOL_COMMA
					+ com.fitech.gznx.common.Config.FREQ_DAY;
		else if (term == 6)
			rep_freq = com.fitech.gznx.common.Config.FREQ_MONTH
					+ Config.SPLIT_SYMBOL_COMMA
					+ com.fitech.gznx.common.Config.FREQ_SEASON
					+ Config.SPLIT_SYMBOL_COMMA
					+ com.fitech.gznx.common.Config.FREQ_HALFYEAR
					+ Config.SPLIT_SYMBOL_COMMA
					+ com.fitech.gznx.common.Config.FREQ_DAY;
		else if (term == 3 || term == 9)
			rep_freq = com.fitech.gznx.common.Config.FREQ_MONTH
					+ Config.SPLIT_SYMBOL_COMMA
					+ com.fitech.gznx.common.Config.FREQ_SEASON
					+ Config.SPLIT_SYMBOL_COMMA
					+ com.fitech.gznx.common.Config.FREQ_DAY;
		else if (term == 1)
			rep_freq = com.fitech.gznx.common.Config.FREQ_MONTH
					+ Config.SPLIT_SYMBOL_COMMA
					+ com.fitech.gznx.common.Config.FREQ_YEARBEGAIN
					+ Config.SPLIT_SYMBOL_COMMA
					+ com.fitech.gznx.common.Config.FREQ_DAY;
		else
			rep_freq = com.fitech.gznx.common.Config.FREQ_MONTH.toString()
					+ Config.SPLIT_SYMBOL_COMMA
					+ com.fitech.gznx.common.Config.FREQ_DAY;

		// // 日报查询
		// datesql = " and (('"
		// + afReportForm.getDate()
		// + "' between a.startDate and a.endDate"
		// + " and a.comp_id.repFreqId in ("
		// + com.fitech.gznx.common.Config.FREQ_DAY
		// // 旬报查询
		// + ")) or ('"
		// + DateUtil.getLastTenDay(afReportForm.getDate())
		// + "' between a.startDate and a.endDate"
		// + " and a.comp_id.repFreqId in ("
		// + com.fitech.gznx.common.Config.FREQ_TENDAY
		// // 月报及以上
		// + ")) or ('"
		// + DateUtil.getLastMonth(afReportForm.getDate())
		// + "' between a.startDate and a.endDate"
		// + " and a.comp_id.repFreqId in (" + rep_freq
		// // 加入机构
		// + ")))";
		datesql = " and ('" + afReportForm.getDate()
				+ "' between a.startDate and a.endDate"
				+ " and a.comp_id.repFreqId in ("
				+ com.fitech.gznx.common.Config.FREQ_DAY
				+ Config.SPLIT_SYMBOL_COMMA
				+ com.fitech.gznx.common.Config.FREQ_TENDAY
				+ Config.SPLIT_SYMBOL_COMMA + rep_freq + "))";

		return datesql;
	}

	public static List findAllGaChaList(AFReportForm afReportForm,
			Operator operator) {
		List gaChaList = new ArrayList();
		List list = null;
		DBConn conn = null;
		Session session = null;
		boolean result = true;
		// 查询条件HQL的生成
		// if(afReportForm.getOrgId() == null)
		// afReportForm.setOrgId(operator.getOrgId());

		try {
			if (afReportForm != null && afReportForm.getTemplateType() != null) {
				conn = new DBConn();
				session = conn.beginTransaction();

				String hql = "select a  from AfViewReport a ,  AfTemplateCollRule v where  a.comp_id.orgId=v.id.orgId and "
						+ "a.comp_id.templateId = v.id.templateId and a.comp_id.versionId = v.id.versionId and a.templateType="
						+ afReportForm.getTemplateType()
						+ getValidDateSql(afReportForm);
				// + " and a.comp_id.orgId='" + operator.getOrgId() + "'";

				/**
				 * 加上报送权限 已增加数据库判断
				 */
				if (operator.isSuperManager() == false) {
					if (com.cbrc.smis.common.Config.DB_SERVER_TYPE
							.equals("oracle"))
						hql += " and a.comp_id.orgId||a.comp_id.templateId in ("
								+ operator.getChildRepReportPopedom() + ")";
					if (com.cbrc.smis.common.Config.DB_SERVER_TYPE
							.equals("sqlserver"))
						hql += " and a.comp_id.orgId+a.comp_id.templateId in ("
								+ operator.getChildRepReportPopedom() + ")";
				}
				/** 报表机构类型 */
				if (afReportForm.getTemplateId() != null
						&& !afReportForm.getTemplateId().equals("")) {
					hql += " and a.comp_id.templateId like '%"
							+ afReportForm.getTemplateId() + "%'";
				}

				if (afReportForm.getRepName() != null
						&& !"".equals(afReportForm.getRepName().trim())) {
					hql += " and a.templateName like '%"
							+ afReportForm.getRepName() + "%' ";
				}
				/** 报表辅助类型 */
				if (afReportForm.getBak1() != null
						&& !afReportForm
								.getBak1()
								.trim()
								.equals(com.cbrc.smis.common.Config.DEFAULT_VALUE)
						&& !afReportForm.getBak1().equals("")) {
					hql += " and a.bak1 in (" + afReportForm.getBak1() + ")";
				}
				/** 报表机构类型 */
				if (afReportForm.getOrgId() != null
						&& !afReportForm
								.getOrgId()
								.trim()
								.equals(com.cbrc.smis.common.Config.DEFAULT_VALUE)
						&& !afReportForm.getOrgId().trim().equals("")) {
					hql += " and a.comp_id.orgId in ('"
							+ afReportForm.getOrgId() + "')";
				}
				/**
				 * 添加报表批次查询条件
				 */
				if (afReportForm.getSupplementFlag() != null
						&& !"".equals(afReportForm.getSupplementFlag())) {
					int supplement_flag = new Integer(
							afReportForm.getSupplementFlag()).intValue();
					if (supplement_flag != -999) {
						hql += " and a.supplementFlag= " + supplement_flag;
					}
				}
				/** 报表频度 */
				if (afReportForm.getRepFreqId() != null
						&& !afReportForm
								.getRepFreqId()
								.trim()
								.equals(com.cbrc.smis.common.Config.DEFAULT_VALUE)
						&& !afReportForm.getRepFreqId().equals("")) {
					hql += " and a.comp_id.repFreqId ="
							+ afReportForm.getRepFreqId();
				}
				if (afReportForm.getIsReport() != null
						&& !afReportForm
								.getIsReport()
								.toString()
								.equals(com.cbrc.smis.common.Config.DEFAULT_VALUE)
						&& !afReportForm.getIsReport().equals("")) {
					hql += " and a.isReport=" + afReportForm.getIsReport();
				} else {
					hql += " and a.isReport="
							+ com.fitech.gznx.common.Config.TEMPLATE_REPORT;
				}

				hql += " order by a.comp_id.templateId, a.comp_id.repFreqId";

				Query query = session.createQuery(hql);
				// query.setFirstResult(offset).setMaxResults(limit);

				list = query.list();
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						// if((i>0&&i<=offset)||i>(offset+limit)) continue;
						AfViewReport viewMReport = (AfViewReport) list.get(i);

						Aditing aditing = new Aditing();
						// aditing.setDataRgTypeName(viewMReport.getDataRgTypeName());
						aditing.setActuFreqName(viewMReport.getRepFreqName());
						aditing.setActuFreqID(viewMReport.getComp_id()
								.getRepFreqId());
						aditing.setTemplateId(viewMReport.getComp_id()
								.getTemplateId());
						aditing.setVersionId(viewMReport.getComp_id()
								.getVersionId());
						aditing.setRepName(viewMReport.getTemplateName());

						if (viewMReport.getComp_id().getRepFreqId() != null) {
							// yyyy-mm-dd 根据日期确定该日期具体的期数日期
							String trueDate = DateUtil.getFreqDateLast(
									afReportForm.getDate(), viewMReport
											.getComp_id().getRepFreqId());
							aditing.setYear(Integer.valueOf(trueDate.substring(
									0, 4)));
							aditing.setTerm(Integer.valueOf(trueDate.substring(
									5, 7)));
							aditing.setDay(Integer.valueOf(trueDate.substring(
									8, 10)));
						}

						aditing.setCurrName(viewMReport.getCurName());
						aditing.setCurId(viewMReport.getComp_id().getCurId());
						// aditing.setDataRgId(viewMReport.getComp_id().getDataRangeId());

						AfOrg org = (AfOrg) session.get(AfOrg.class,
								viewMReport.getComp_id().getOrgId());
						if (org == null)
							throw new Exception(
									"机构为空，请检查库表AF_TEMPLATE_ORG_RELATION数据是否存在异常");
						aditing.setOrgName(org.getOrgName());
						aditing.setOrgId(org.getOrgId());

//						// （废弃）得到times的值,如果是-1汇总过的
						// 查出是否已经上报
						String timesSql = "from AfReport ri where  "
								+ " ri.templateId='"
								+ aditing.getTemplateId()
								+ "'"
								+ " and ri.versionId='"
								+ aditing.getVersionId()
								+ "'"
								// +" and ri.MDataRgType.dataRangeId="+aditing.getDataRangeId()
								+ " and ri.year=" + aditing.getYear()
								+ " and ri.term=" + aditing.getTerm()
								+ " and ri.day=" + aditing.getDay()
								+ " and ri.repFreqId="
								+ aditing.getActuFreqID() + " and ri.curId="
								+ aditing.getCurId() + " and ri.orgId='"
								+ aditing.getOrgId() + "'" + " and ri.times=1"
								+ " and ri.checkFlag="
								+ com.cbrc.smis.common.Config.CHECK_FLAG_PASS;
						List rlist = session.find(timesSql);
//
						if (rlist != null && rlist.size() > 0) {
							AfReport reportIn = (AfReport) rlist.get(0);
							aditing.setRepInId(reportIn.getRepId().intValue());
							aditing.setIsCollected(new Integer(1));

						} else {
							aditing.setIsCollected(new Integer(0));
						}
						gaChaList.add(aditing);
					}
				}
			}
		} catch (HibernateException he) {
			log.printStackTrace(he);
			if (conn != null)
				conn.endTransaction(true);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		// System.out.println(gaChaList.size());
		return gaChaList;
	}

	
	/**
	 * 获取除自身外的其他同级行
	 * @param orgId
	 * @return
	 */
	public static List getSameLevelOrgList(String  orgId) {
		DBConn conn = null;
		Session session = null;
		List resList = null;
		try {
			conn = new DBConn();
			session = conn.openSession();
			String hql = "from AfCollectRelation a where a.id.collectId =( select t.id.collectId from AfCollectRelation t where t.id.orgId = '"+orgId+"') and a.id.orgId<>'"
					+ orgId + "'"; 
			Query query = session.createQuery(hql);
			resList = query.list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				conn.closeSession();
			}
		}
		return resList;
	}
	/**
	 * 获取需要报送的报表数
	 * @param collFormula
	 * @param orgId
	 * @return
	 */
	public static int getNeedReportNUM(String collFormula,String orgId) {
		System.out.println(collFormula);
		int needNUM = 0;
		try {
			String[] params = FitechUtil.parseFomual(collFormula);
			if(params[1].toUpperCase().equals("HZTJH")){
				needNUM = getSameLevelOrgList(orgId).size()+1;
			}else{
				String[] needOrgIds = params[1].split(",");
				needNUM = needOrgIds.length + 1;
			}
			
			System.out.println(needNUM);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return needNUM;
	}
/**
 * 获取需要报送的所有机构的字符串
 * @param collFormula
 * @param orgId
 * @return needOrgIds
 */
	public static String getNeedOrgIds(String collFormula ,String orgId) {
		String needOrgIds = "";
		List orgList = null;
		try {
			String[] params = FitechUtil.parseFomual(collFormula);
			needOrgIds += "'" + params[0] + "',";
			if(params[1].toUpperCase().equals("HZTJH")){
				orgList = getSameLevelOrgList(orgId);
				for (Iterator iterator = orgList.iterator(); iterator.hasNext();) {
					AfCollectRelation org = (AfCollectRelation) iterator.next();
					needOrgIds += "'"+org.getId().getOrgId()+"',";
				}
			}else{
				String[] p1 = params[1].split(",");
				
				for (int i = 0; i < p1.length; i++) {
					needOrgIds += "'" + p1[i] + "',";
				}
			}
			needOrgIds = needOrgIds.substring(0, needOrgIds.length() - 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("needOrgIds" + needOrgIds);
		return needOrgIds;
	}
	/**
	 * 获取需要报送的所有机构的集合
	 * @param collFormula
	 * @param orgId
	 * @return needOrgIds
	 */
	public static List getNeedOrgs(String collFormula ,String orgId) {
		List needOrgs = null;
		String needOrgsIds = getNeedOrgIds(collFormula,orgId);
		DBConn conn = null;
		Session session = null;
		boolean result = true;
		// 查询条件HQL的生成
		try {
			// conn对象的实例化
			conn = new DBConn();
			session = conn.beginTransaction();
			String hql = "from AfOrg o where o.orgId in (" + needOrgsIds + ")";
			Query query = session.createQuery(hql);
			needOrgs = query.list();
		} catch (Exception e) {
			result = false;
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.endTransaction(result);
		}
		// System.out.println(gaChaList.size());
		return needOrgs;
	}

}
