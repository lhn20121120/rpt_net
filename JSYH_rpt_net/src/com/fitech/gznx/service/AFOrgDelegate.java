package com.fitech.gznx.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.auth.hibernate.MPurOrg;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.fitech.gznx.common.Config;
import com.fitech.gznx.form.OrgInfoForm;
import com.fitech.gznx.po.AfCollectRelation;
import com.fitech.gznx.po.AfOrg;
import com.fitech.gznx.po.AfTemplateCollRep;
import com.fitech.gznx.po.AfTemplateOrgRelation;
import com.fitech.gznx.po.AfTemplateOuterRep;
import com.fitech.gznx.treexml.BaseOrgTreeIterator;
import com.fitech.gznx.treexml.OrgTreeInterface;
import com.fitech.gznx.util.TranslatorUtil;
import com.fitech.net.form.OrgNetForm;
import com.fitech.net.hibernate.OrgNet;

public class AFOrgDelegate {

	private static FitechException log = new FitechException(
			AFOrgDelegate.class);

	/**
	 * 已使用hibernate 卞以刚 2011-12-21
	 * 影响对象：AfOrg
	 * 得到一个机构对象
	 * 
	 * @param orgId
	 * @return
	 */
	public static AfOrg getOrgInfo(String orgId) {

		DBConn dbconn = null;

		Session session = null;

		if (orgId == null || orgId.equals("")) {
			return null;
		}

		try {
			dbconn = new DBConn();

			session = dbconn.openSession();

			AfOrg ao = (AfOrg) session.get(AfOrg.class, orgId);

			return ao;

		} catch (Exception e) {

			log.printStackTrace(e);

			return null;

		} finally {

			if (session != null)

				dbconn.closeSession();
		}
	}

	/**
	 * 已使用hibernate  卞以刚 2011-12-21
	 * 更新机构信息
	 * 
	 * @author Nick
	 * @param orgInfoForm
	 *            OrgInfoForm对象
	 * @return boolean 更新机构是否成功
	 */
	public static boolean setFirstNode(OrgInfoForm orgInfoForm) {
		boolean result = false;
		// 连接对象和会话对象初始化
		DBConn conn = null;
		Session session = null;

		try {
			// conn对象的实例化
			conn = new DBConn();
			// 打开连接开始会话
			session = conn.beginTransaction();

			AfOrg orgInfo = (AfOrg) session.get(AfOrg.class, orgInfoForm
					.getOrgId());
			// 只要更新该机构的preOrgId属性
			orgInfo.setPreOrgId(Config.COLLECT_ORG_PARENT_ID);
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
	 * 已使用hibernate  卞以刚 2011-12-21
	 * 获得所有机构的信息的ID列表
	 * 
	 * @return
	 */
	public static List getAllOrgList() {
		DBConn conn = null;
		Session session = null;
		List resultList = null;

		try {
			conn = new DBConn();
			session = conn.openSession();

			String hql = "select oi.orgId from AfOrg oi";
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
		return resultList;
	}

	/**
	 * 已使用hibernate  卞以刚 2011-12-21
	 * 根据用户名取得该用户的机构权限信息
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public static List getUserOrgPurview(String userId) throws Exception {
		if (userId == null || userId.equals(""))
			return null;

		List result = null;
		DBConn conn = null;
		Session session = null;

		try {
			conn = new DBConn();
			session = conn.openSession();

			String hql = "from AfOrg afo where afo.orgId in "
					+ "(select opr.org.orgId from Operator opr where opr.userName =? ) "
					+ "order by afo.orgId";

			Query query = session.createQuery(hql);
			query.setString(0, userId);
			List list = query.list();

			if (list != null && list.iterator().hasNext()) {
				result = new ArrayList();
				for (int i = 0; i < list.size(); i++) {
					AfOrg item = (AfOrg) list.get(i);
					result.add(item.getOrgId().toString());
				}
			}

		} catch (Exception e) {
			result = null;
			log.printStackTrace(e);
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return result;
	}
	/**
	 * 已使用hibernate  卞以刚 2011-12-21
	 * 根据用户名取得该用户的机构权限信息
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public static List getUserOrgPurviewHZRule(String userId) throws Exception {
		if (userId == null || userId.equals(""))
			return null;
		
		List result = null;
		DBConn conn = null;
		Session session = null;
		
		try {
			conn = new DBConn();
			session = conn.openSession();
			
			String hql = "from AfOrg afo where afo.orgId in "
				+ "(select opr.org.orgId from Operator opr where opr.userName =? ) "
				+ "order by afo.orgId";
			
			Query query = session.createQuery(hql);
			query.setString(0, userId);
			List list = query.list();
			
			if (list != null && list.iterator().hasNext()) {
				result = new ArrayList();
				for (int i = 0; i < list.size(); i++) {
					AfOrg item = (AfOrg) list.get(i);
					result.add(item);
				}
			}
			
		} catch (Exception e) {
			result = null;
			log.printStackTrace(e);
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return result;
	}

	/**
	 * 已使用hibernate  卞以刚 2011-12-21
	 * title:该方法用于根据orgId返回其下属机构列表列表 author:chenbing date:2008-2-19
	 * 
	 * @param orgId
	 * @return List:LableValueBean:机构ID,机构名称
	 */
	public static List getChildListByOrgId(String orgId) {

		DBConn dbconn = null;

		Session session = null;

		Query query = null;

		List result = null;

		if (orgId == null)
			return null;

		try {

			result = new ArrayList();

			dbconn = new DBConn();

			session = dbconn.openSession();

			StringBuffer sb = new StringBuffer(
					"select oi.orgId,oi.orgName,oi.isCollect ,oi.preOrgId from AfOrg oi where oi.preOrgId='"
							+ orgId + "' ");

			query = session.createQuery(sb.toString());

			List list = query.list();

  			for (int i = 0; i < list.size(); i++) {

				Object[] os = (Object[]) list.get(i);

				String[] strs = new String[4];

				strs[0] = ((String) os[0]).trim();

				strs[1] = ((String) os[1]).trim();
				strs[3] = ((String) os[3]).trim();

				Long integer = (Long) os[2];

				if (integer == null
						|| integer.toString().equals(Config.NOT_IS_COLLECT))

					strs[2] = Config.NOT_IS_COLLECT;

				else

					strs[2] = Config.IS_COLLECT;

				result.add(strs);

			}
			if (result.size() == 0)

				result = null;

		} catch (Exception e) {

			log.printStackTrace(e);

			result = null;

		} finally {

			if (session != null)

				dbconn.closeSession();
		}
		return result;
	}
	
	public static List getChildListByRelOrgId(String orgId,String sysFlag) {

		DBConn dbconn = null;

		Session session = null;

		Query query = null;

		List result = null;

		if (orgId == null)
			return null;

		try {

			result = new ArrayList();

			dbconn = new DBConn();

			session = dbconn.openSession();

			StringBuffer sb = new StringBuffer(
					"select oi.id.orgId,oi.orgNm from vOrgRel oi where oi.preOrgid='"
							+ orgId + "' and oi.id.sysFlag='"+sysFlag+"' order by oi.id.orgId");

			query = session.createQuery(sb.toString());

			List list = query.list();

			for (int i = 0; i < list.size(); i++) {

				Object[] os = (Object[]) list.get(i);

				String[] strs = new String[3];

				strs[0] = ((String) os[0]).trim();

				strs[1] = ((String) os[1]).trim();


				result.add(strs);

			}
			if (result.size() == 0)

				result = null;

		} catch (Exception e) {

			log.printStackTrace(e);

			result = null;

		} finally {

			if (session != null)

				dbconn.closeSession();
		}
		return result;
	}
	/**
	 * 已使用hibernate  卞以刚 2011-12-21
	 * title:该方法用于返回所有的顶层机构 author:chenbing date:2008-2-19
	 * 
	 * @return
	 */
	public static List getAllFirstOrg() {

		DBConn dbconn = null;

		Session session = null;

		Query query = null;

		List result = null;

		String hsql = "select oi.orgId,oi.orgName,oi.isCollect,oi.preOrgId from AfOrg oi where oi.preOrgId is null or oi.preOrgId = '0' ";

		try {

			result = new ArrayList();

			dbconn = new DBConn();

			session = dbconn.openSession();

			query = session.createQuery(hsql);

			List list = query.list();

			for (int i = 0; i < list.size(); i++) {

				Object[] os = (Object[]) list.get(i);

				String[] strs = new String[4];

				strs[0] = ((String) os[0]).trim();

				strs[1] = ((String) os[1]).trim();
				
				strs[3] = ((String) os[3]).trim();

				Long integer = (Long) os[2];

				if (integer == null
						|| integer.toString().equals(Config.NOT_IS_COLLECT))

					strs[2] = Config.NOT_IS_COLLECT;

				else

					strs[2] = Config.IS_COLLECT;

				result.add(strs);
			}
			//查询出所有顶层虚拟机构（在AfCollectRelation表中查询所有没有ColletId ，或者colletid为实体机构的虚拟机构）
			hsql = "select oi.orgId,oi.orgName,oi.isCollect ,oi.preOrgId from AfOrg oi " +
					"where oi.orgId  not in " +
						"(select a.id.orgId from AfCollectRelation a where a.id.collectId not in " +
							"(select c.orgId from AfOrg c where c.preOrgId !='"
								+ Config.COLLECT_ORG_PARENT_ID
							+ "'))  and oi.preOrgId = '"
					+ Config.COLLECT_ORG_PARENT_ID + "'";
			
			query = session.createQuery(hsql);

			list = query.list();

			for (int i = 0; i < list.size(); i++) {

				Object[] os = (Object[]) list.get(i);

				String[] strs = new String[4];

				strs[0] = ((String) os[0]).trim();

				strs[1] = ((String) os[1]).trim();
				
				strs[3] = ((String) os[3]).trim();
				
				Long integer = (Long) os[2];

				if (integer == null
						|| integer.toString().equals(Config.NOT_IS_COLLECT))

					strs[2] = Config.NOT_IS_COLLECT;

				else

					strs[2] = Config.IS_COLLECT;

				result.add(strs);
			}

			if (result.size() == 0)

				result = null;

		} catch (Exception e) {

			log.printStackTrace(e);

			result = null;

		} finally {

			if (session != null)

				dbconn.closeSession();
		}
		return result;
	}
	
	
	/**
	 * 已使用hibernate  卞以刚 2011-12-21
	 * title:该方法用于返回所有的顶层机构 author:chenbing date:2008-2-19
	 * 
	 * @return
	 */
	public static List getFirstOrgById(String orgId) {

		DBConn dbconn = null;

		Session session = null;

		Query query = null;

		List result = null;

		String hsql = "select oi.orgId,oi.orgName,oi.isCollect from AfOrg oi where oi.orgId ='"+orgId+"'";

		try {

			result = new ArrayList();

			dbconn = new DBConn();

			session = dbconn.openSession();

			query = session.createQuery(hsql);

			List list = query.list();

			for (int i = 0; i < list.size(); i++) {

				Object[] os = (Object[]) list.get(i);

				String[] strs = new String[3];

				strs[0] = ((String) os[0]).trim();

				strs[1] = ((String) os[1]).trim();

				Long integer = (Long) os[2];

				if (integer == null
						|| integer.toString().equals(Config.NOT_IS_COLLECT))

					strs[2] = Config.NOT_IS_COLLECT;

				else

					strs[2] = Config.IS_COLLECT;

				result.add(strs);
			}
			if (result.size() == 0)

				result = null;

		} catch (Exception e) {

			log.printStackTrace(e);

			result = null;

		} finally {

			if (session != null)

				dbconn.closeSession();
		}
		return result;
	}
	
	/***
	 * 生成机构关系的第一层为SYS_FLAG的值
	 * @return
	 */
	public static List getAllFirstVorgRel() {

		DBConn dbconn = null;

		Session session = null;

		Query query = null;

		List result = null;

		String hsql = "select oi.id.sysFlag from vOrgRel oi group by oi.id.sysFlag";

		try {

			result = new ArrayList();

			dbconn = new DBConn();

			session = dbconn.openSession();

			query = session.createQuery(hsql);

//			List list = query.list();
//
//			for (int i = 0; i < list.size(); i++) {
//
//				Object[] os = (Object[]) list.get(i);
//
//				String[] strs = new String[3];
//
//				strs[0] = ((String) os[0]).trim();
//				
//				result.add(strs);
//			}
			Object[] os = query.list().toArray();
			for(int i=0;i<os.length;i++){
				result.add(os[i]);
			}

			if (result.size() == 0)

				result = null;

		} catch (Exception e) {

			log.printStackTrace(e);

			result = null;

		} finally {

			if (session != null)

				dbconn.closeSession();
		}
		return result;
	}
	
	/***
	 * 生成机构关系的第二层为PRE_ORGID为null 或者为空的
	 * @return
	 */
	public static List getAllSecondVorgRel(String sysFlag) {

		DBConn dbconn = null;

		Session session = null;

		Query query = null;

		List result = null;
		//机构生成关系树构造
		//String hsql = "select oi.id.orgId,oi.orgNm from vOrgRel oi where oi.id.sysFlag='"+sysFlag+"'";
		String hsql = "select oi.id.orgId,oi.orgNm from vOrgRel oi where oi.id.sysFlag='"+sysFlag+"' and (oi.preOrgid is null or oi.preOrgid='' or oi.preOrgid=0)";
		try {

			result = new ArrayList();

			dbconn = new DBConn();

			session = dbconn.openSession();

			query = session.createQuery(hsql);

			List list = query.list();

			for (int i = 0; i < list.size(); i++) {

				Object[] os = (Object[]) list.get(i);

				String[] strs = new String[3];

				strs[0] = ((String) os[0]).trim();

				strs[1] = ((String) os[1]).trim();


				result.add(strs);
			}

			if (result.size() == 0)

				result = null;

		} catch (Exception e) {

			log.printStackTrace(e);

			result = null;

		} finally {

			if (session != null)

				dbconn.closeSession();
		}
		return result;
	}
	
	/**
	 * 已使用hibernate  卞以刚 2011-12-21
	 * 影响对象：AfOrg
	 * 查询子机构信息
	 * @param orgId
	 * @param session
	 * @return
	 */
	public static List selectLowerOrgList(String orgId,HttpSession session){
		if(orgId==null) return null;
		DBConn conn = null;
		Session session1 = null;
		List orgNetList = null;
		
		try{
			conn = new DBConn();
			session1 = conn.openSession();						
			orgNetList = new ArrayList();
			
			String hql = "from AfOrg ont where ont.preOrgId='" + orgId.trim() + "' or ont.isCollect=1"; 
			
			List list = session1.find(hql.toString());
			HashMap Map=null;
			if(session.getAttribute("SelectedOrgIds")!=null){			  
				Map=(HashMap)session.getAttribute("SelectedOrgIds");			  
			}
			if(list != null && list.size() > 0){
				for(int i=0;i<list.size();i++){
					AfOrg aforg = (AfOrg)list.get(i);
					OrgNet orgNet = new OrgNet();
					orgNet.setOrgId(aforg.getOrgId());
					orgNet.setOrgName(aforg.getOrgName());
					//orgNet.setOrgType(aforg.getOrgType());
					orgNet.setPreOrgId(aforg.getPreOrgId());
					//orgNet.setRegion(aforg.getRegionId());
					orgNet.setSetOrgId(aforg.getSetOrgId());
					if(Map!=null){						
						if(Map.containsKey(orgNet.getOrgId())){
							orgNet.setPreOrgId("true");//姑且用pre org标志是否选中							
						}
					}
					orgNetList.add(orgNet);
				}
			}
		}catch(HibernateException he){
			log.printStackTrace(he);
		}catch(Exception e){
			log.printStackTrace(e);
		}finally{
			//如果连接存在，则断开，结束会话，返回
			if(conn != null) conn.closeSession();
		}
		return orgNetList;
	}
	public static List selectLowerAfOrgList(String orgId,HttpSession session){
		if(orgId==null) return null;
		DBConn conn = null;
		Session session1 = null;
		List orgNetList = null;
		
		try{
			conn = new DBConn();
			session1 = conn.openSession();						
			orgNetList = new ArrayList();
			
			String hql = "from AfOrg ont where ont.preOrgId='" + orgId.trim() + "'"; 
			
			List list = session1.find(hql.toString());
			HashMap Map=null;
			if(session.getAttribute("SelectedOrgIds")!=null){			  
				Map=(HashMap)session.getAttribute("SelectedOrgIds");			  
			}
			if(list != null && list.size() > 0){
				for(int i=0;i<list.size();i++){
					AfOrg aforg = (AfOrg)list.get(i);
					/*OrgNet orgNet = new OrgNet();
					orgNet.setOrgId(aforg.getOrgId());
					orgNet.setOrgName(aforg.getOrgName());
					//orgNet.setOrgType(aforg.getOrgType());
					orgNet.setPreOrgId(aforg.getPreOrgId());
					//orgNet.setRegion(aforg.getRegionId());
					orgNet.setSetOrgId(aforg.getSetOrgId());*/
					if(Map!=null){						
						if(Map.containsKey(aforg.getOrgId())){
							aforg.setPreOrgId("true");//姑且用pre org标志是否选中							
						}
					}
					orgNetList.add(aforg);
				}
			}
		}catch(HibernateException he){
			log.printStackTrace(he);
		}catch(Exception e){
			log.printStackTrace(e);
		}finally{
			//如果连接存在，则断开，结束会话，返回
			if(conn != null) conn.closeSession();
		}
		return orgNetList;
	}
	
	/**
	 * 已使用hibernate  卞以刚 2011-12-21
	 * 判断是否有相同的机构代码
	 * 
	 * @author Nick
	 * @param orgInfoForm
	 *            OrgInfoForm对象
	 * @return boolean 是否存在
	 */
	public static boolean isExistSameOrgId(OrgInfoForm orgInfoForm) {
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
			Query query = session.createQuery("from AfOrg oi where oi.orgId=?");
			query.setString(0, orgInfoForm.getOrgId());
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

	/**
	 * 已使用hibernate  卞以刚 2011-12-21
	 * 判断是否有相同的机构名称
	 * 
	 * @author Nick
	 * @param orgInfoForm
	 *            OrgInfoForm对象
	 * @return boolean 是否存在
	 */
	public static boolean isExistSameOrgName(OrgInfoForm orgInfoForm) {
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
			Query query = session
					.createQuery("from AfOrg oi where oi.orgName=?");
			query.setString(0, orgInfoForm.getOrgName());
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

	/**
	 * 已使用hibernate  卞以刚 2011-12-21
	 * 增加机构信息
	 * 
	 * @author Nick
	 * @param orgInfoForm
	 *            OrgInfoForm对象
	 * @param userId
	 *            String 增加机构的用户ID
	 * @return boolean 是否增加成功
	 */
	public static boolean add(OrgInfoForm orgInfoForm, String userId) {
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
			AfOrg parentOrgInfo = AFOrgDelegate.getOrgInfo(orgInfoForm.getParentOrgId());

			AfOrg orgInfo = new AfOrg();

			orgInfo.setOrgId(orgInfoForm.getOrgId());
			orgInfo.setOrgName(orgInfoForm.getOrgName());

//			if (orgInfoForm.getOrgLevel() != null)
//				orgInfo.setOrgLevel(Long.valueOf(orgInfoForm.getOrgType()));

			if (orgInfoForm.getOrgType() != null){
				orgInfo.setOrgType(orgInfoForm.getOrgType());
				orgInfo.setOrgLevel(Long.valueOf(orgInfoForm.getOrgType()));
			}
			
			if (orgInfoForm.getOrgRegion() != null)
				orgInfo.setRegionId(orgInfoForm.getOrgRegion());

			if (orgInfoForm.getSetOrgId() != null)
				orgInfo.setSetOrgId(orgInfoForm.getSetOrgId());
			
			orgInfo.setOrgOuterId(orgInfoForm.getOrgOuterId());

			if (orgInfoForm.getParentOrgId() != null
					&& !orgInfoForm.getParentOrgId().equals("")) {
				orgInfo.setPreOrgId(orgInfoForm.getParentOrgId());
			} else {
				orgInfo.setPreOrgId(Config.COLLECT_ORG_PARENT_ID);
			}
			//判断虚拟机构是否需要汇总
			if (!orgInfoForm.getOrgType().equals(Config.COLLECT_ORG_PARENT_ID) && !orgInfoForm.getOrgType().equals(Config.COLLECT_ORG_PARENT_QT_ID)) {
				orgInfo.setIsCollect(Long.valueOf(Config.NOT_IS_COLLECT));
			} else {
				orgInfo.setIsCollect(Long.valueOf(Config.IS_COLLECT));
			}

			session.save(orgInfo);

			// /** 2 增加机构的用户具有该机构的机构权限 */
			// UserOrgPurview uop = new UserOrgPurview();
			// UserOrgPurviewId id = new UserOrgPurviewId();
			//
			// id.setOrgId(orgInfoForm.getOrgId());
			// UserInfo ui = StrutsUserInfoDelegate.selectOne(userId);
			// id.setUserInfo(ui);
			// uop.setId(id);
			// session.save(uop);
			//
			// /** 3如果增加的是真实机构，需要增加指标集对象关系 */
			// if (orgInfo.getIsCollect().equals(
			// new Integer(Config.NOT_IS_COLLECT))) {
			// // 增加指标集对象关系
			// List lstMA = StrutsMeasureAttributeDelegate
			// .selectMAList(orgInfo);
			// MeasureOrgMapping mom = null;
			// MeasureOrgMappingId momId = null;
			// if (lstMA != null && lstMA.size() > 0) {
			// for (int i = 0; i < lstMA.size(); i++) {
			// MeasureAttribute ma = (MeasureAttribute) lstMA.get(i);
			//
			// mom = new MeasureOrgMapping();
			// momId = new MeasureOrgMappingId();
			//
			// momId.setMeasureAttribute(ma);
			// momId.setOrgId(orgInfo.getOrgId());
			//
			// mom.setId(momId);
			// session.save(mom);
			// }
			// }
			//
			// }

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
	 * 已使用hibernate  卞以刚 2011-12-21
	 * 更新机构信息
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

			AfOrg orgInfo = (AfOrg) session.get(AfOrg.class, orgInfoForm
					.getOrgId());

			orgInfo.setOrgName(orgInfoForm.getOrgName());
			orgInfo.setOrgOuterId(orgInfoForm.getOrgOuterId());
			
			if (orgInfoForm.getOrgLevel() != null)
				orgInfo.setOrgLevel(Long.valueOf(orgInfoForm.getOrgLevel()));
			
			orgInfo.setOrgType(orgInfoForm.getOrgType());
			
			if (orgInfoForm.getOrgRegion() != null)
				orgInfo.setRegionId(orgInfoForm.getOrgRegion());
			
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
	 * 已使用hibernate  卞以刚 2011-12-21
	 * 删除机构信息[如果是虚拟机构，则要先删除汇总关系]
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

			AfOrg orgInfo = (AfOrg) session.get(AfOrg.class, orgInfoForm
					.getOrgId());

			if (orgInfo == null) {
				return false;
			}
			// 如果是虚拟机构，则删除CollectId为当前机构ID的记录
			// 如果是真实机构，则删除OrgId为当前机构ID的记录
//			if (orgInfo.getIsCollect().equals(Long.valueOf(Config.IS_COLLECT))) {
				// 虚拟机构
				String hql = "from AfCollectRelation cr where cr.id.collectId=? or cr.id.orgId=?";//2013-12-10:LuYueFei删除所有包含此机构ID的记录
				Query query = session.createQuery(hql);
				query.setString(0, orgInfo.getOrgId());query.setString(1, orgInfo.getOrgId());//2013-12-10:LuYueFei删除所有包含此机构ID的记录
				List crLst = query.list();

				if (crLst != null && crLst.size() > 0) {
					for (int i = 0; i < crLst.size(); i++) {
						AfCollectRelation cr = (AfCollectRelation) crLst.get(i);
						session.delete(cr);
					}
				}
/**			} else {
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
			}*/

			// // 自动删除该机构的所有机构权限记录
			// String hql = "from UserOrgPurview uop where uop.id.orgId=?";
			// lst = session.createQuery(hql).setString(0, orgInfo.getOrgId())
			// .list();
			// for (int i = 0; i < lst.size(); i++) {
			// session.delete(lst.get(i));
			// }


			//删除机构时，删除对应的MPurOrg中的记录
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
			
			//删除机构时，删除对应的AfTemplateOrgRelation中的记录
			String sql1 = "from AfTemplateOrgRelation ator where ator.id.orgId=?";
			
			Query atorquery = session.createQuery(sql1);
			atorquery.setString(0, orgInfo.getOrgId());
			List atorLst = atorquery.list();

			if (atorLst != null && atorLst.size() > 0) {
				for (int i = 0; i < atorLst.size(); i++) {
					AfTemplateOrgRelation ator = (AfTemplateOrgRelation) atorLst.get(i);
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
					AfTemplateOuterRep ator = (AfTemplateOuterRep) atorLst.get(i);
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
	 * 已使用hibernate  卞以刚 2011-12-21
	 * 根据ID和机构权限找出一家机构的子机构
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
	 * 已使用hibernate  卞以刚 2011-12-21
	 * 根据机构ID得到所有的下属用户
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
	 *已使用hibernate 卞以刚 2011-12-22
	 * 生成机构树的XML文件
	 * 
	 * @author Nick
	 * @return boolean 生成机构树是否成功
	 */
	public static boolean makeOrgTree() {

		boolean result = false;
		/** 生成XML */
		OrgTreeInterface orgTree = new BaseOrgTreeIterator();
		/**已使用hibernate 卞以刚 2011-12-22**/
		if (orgTree.createTreeForTagXml() && orgTree.createTreeForVorgRelXml()) {
			result = true;
		}
		return result;
	}

	/**
	 * 已使用hibernate  卞以刚 2011-12-21
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
	 * 已使用hibernate  卞以刚 2011-12-21
	 * 获得下级机构列表信息
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
	 * 已使用hibernate  卞以刚 2011-12-21
	 * <p>
	 * 描述:查询子机构数量
	 * <p>
	 * 作者：曹发根
	 * </p>
	 */
	public static int selectSubOrgCount(OrgInfoForm orgInfoForm, Operator operator ,Integer templateType) {
		int count = 0;
		DBConn conn = null;
		Session session = null;

		if (operator == null)
			return count;
		try {
			
			
			StringBuffer hql = new StringBuffer(
					"select count(*) from AfOrg oi where oi.orgId in ("
							+ operator.getChildRepSearchPopedom().replace("orgRepId", "orgId") 
							+" and viewOrgRep.childRepId in (select tmpl.id.templateId from AfTemplate tmpl where tmpl.templateType=" 
							+ templateType +"))");
			
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
	 * 已使用hibernate  卞以刚 2011-12-21
	 * 获得下级机构列表信息
	 * 
	 * @param orgNetForm
	 * @param offset
	 * @param limit
	 * @return
	 */
	public static List selectSubOrgList(OrgInfoForm orgInfoForm, Operator operator, Integer templateType, int offset,
			int limit) {
		if (operator == null)
			return null;

		List refVals = null;
		DBConn conn = null;
		Session session = null;

		try {
			// 查询条件HQL的生成
			StringBuffer hql = new StringBuffer(
					"from AfOrg ont where ont.orgId in ("
					+ operator.getChildRepSearchPopedom().replace("orgRepId", "orgId") 
					+" and viewOrgRep.childRepId in (select tmpl.id.templateId from AfTemplate tmpl where tmpl.templateType=" 
					+ templateType +"))");
			
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
	 * 已使用hibernate  卞以刚 2011-12-21
	 * 获取分支机构列表
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
	 * 已使用hibernate 卞以刚 2011-12-21
	 * 影响对象：AfOrg
	 * 根据机构id查询机构信息
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
	 * 已使用hibernate 卞以刚 2011-12-21
	 * 影响对象：AfOrg MPurOrg
	 * 根据用户组ID取得该用户组已经设置过的机构
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
	 * 已使用hibernate 卞以刚 2011-12-21
	 * 影响对象：AfOrg
	 * 取得当前机构的下级机构(添加支持虚拟机构)
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
	 * 已使用hibernate 卞以刚 2011-12-21
	 * 影响对象：AfOrg
	 * 根据机构id，查询该机构下所有子机构
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
				String hql = "from AfOrg ont where ont.preOrgId in (" + temp + ")";

				List list = session.find(hql.toString());

				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						AfOrg orgNet = (AfOrg) list.get(i);
						lowerOrgIds = lowerOrgIds.equals("") ? "'"
								+ orgNet.getOrgId() + "'" : lowerOrgIds + ",'"
								+ orgNet.getOrgId() + "'";
					}
				}
				
				if (lowerOrgIds.equals("")){
					hql = "from AfOrg ont where ont.isCollect=1";

					list = session.find(hql.toString());
		
					if (list != null && list.size() > 0) {
						for (int i = 0; i < list.size(); i++) {
							AfOrg orgNet = (AfOrg) list.get(i);
							lowerOrgIds = lowerOrgIds.equals("") ? "'"
									+ orgNet.getOrgId() + "'" : lowerOrgIds + ",'"
									+ orgNet.getOrgId() + "'";
						}
					}
					allLowerOrgIds = lowerOrgIds.equals("") ? allLowerOrgIds
							: allLowerOrgIds + "," + lowerOrgIds;
					break;
				}else
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
	 * 已使用hibernate 卞以刚 2011-12-21
	 * 根据机构ID取得相应机构信息
	 * 
	 * @param orgIds
	 * @return List 机构列表
	 */
	public static List selectOrgByIds(String orgIds,String reportflg) {
		List result = null;
		DBConn conn = null;
		Session session = null;

		try {
			conn = new DBConn();
			session = conn.openSession();
			orgIds = orgIds +" and viewOrgRep.childRepId in (select t.id.templateId from " +
					"AfTemplate t where t.templateType="+reportflg+") group by viewOrgRep.orgId ";
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
	 * 已使用hibernate 卞以刚 2011-12-21
	 * 影响对象：AfOrg
	 * 根据机构ID取得相应机构信息
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
	 * 已使用hibernate 卞以刚 2011-12-21
	 * 根据机构id，查询该机构下所有子机构
	 * @param orgId
	 * @return
	 */
	public static String selectRegionId(String orgId){
		String regionId="";
		String lowerOrgIds = "'"+orgId.trim()+"'";
		DBConn conn = null;
		Session session = null;
		try{
			conn = new DBConn();
			session = conn.openSession();

			String hql = "from AfOrg ont where ont.orgId=" + orgId; 
			
			List list = session.find(hql.toString());
						
			if(list != null && list.size() > 0){
				AfOrg orgNet = (AfOrg)list.get(0);
				regionId = orgNet.getRegionId() != null?String.valueOf(orgNet.getRegionId()):"";
				
			}
				
			
		}catch(HibernateException he){
			log.printStackTrace(he);
		}catch(Exception e){
			lowerOrgIds = null ;
			log.printStackTrace(e);
		}finally{
			//如果连接存在，则断开，结束会话，返回
			if(conn != null) conn.closeSession();
		}
		return regionId;
	}

	/**
	 * 已使用hibernate 卞以刚 2011-12-21
	 * 得到应报机构列表的数量
	 * @param templateId 模板id 
	 * @param versionId 版本号
	 * @param orgId 该报表报送机构
	 * @param childOrgIds 用户机构权限
	 * @param type 汇总类型
	 * @return int
	 */
	public int getMustOrgCount(String templateId, String versionId, String childOrgIds,
			String orgId, Integer type){
		int  count = 0;	
		DBConn conn = null;
		Session session = null;
		
		if(templateId == null || templateId.equals("")
				|| versionId == null || versionId.equals("")
				)
			return count;
		
		try{
			conn=new DBConn();
			session=conn.openSession();
			String sql = "";
			//总行汇虚拟机构
			if (type.equals(Integer.valueOf(1)) || type.equals(Integer.valueOf(4))){
				sql = "select count(org.orgId) from AfOrg org where org.orgId in " +
					"(select distinct M.id.orgId from AfViewReport M where " +
					//"M.comp_id.orgId in (" + childOrgIds + ") and " +
					"M.id.templateId='" + templateId + "' " +
					" and  M.id.versionId='" + versionId + "'";
					//") and org.isCollect=1";
					if(type.equals(Integer.valueOf(1)))
						sql += ") and org.isCollect=1 and org.orgType='"+com.fitech.gznx.common.Config.COLLECT_ORG_PARENT_ID+"'";
					if(type.equals(Integer.valueOf(4)))
						sql += ") and org.isCollect=1 and org.orgType='"+com.fitech.gznx.common.Config.COLLECT_ORG_PARENT_QT_ID+"'";
					
			}
			//虚拟机构汇分行
			else if(type.equals(Integer.valueOf(2))){
				sql = "select count(org.orgId) from AfOrg org where org.orgId in " +
				"(select distinct M.id.orgId from AfViewReport M,AfCollectRelation afc" +
				" where afc.id.orgId=M.id.orgId " +
				//" and M.id.orgId in (" + childOrgIds + ")" +
				" and M.id.templateId='" + templateId + 
				"' and M.id.versionId='" + versionId + "'" +
				" and afc.id.collectId='"+ orgId +"')";
			
			}else if(type.equals(Integer.valueOf(8))){//轧差汇总
				String formula = com.fitech.gznx.service.AFTemplateCollRuleDelegate.getCollFormulaName(orgId, templateId, versionId);
				if(formula!=null&&formula.length()>0){
					return com.fitech.gznx.service.AFTemplateCollRuleDelegate.getNeedReportNUM(formula,orgId);
				}
			}
			//分行汇支行
			else{
/*				sql = "select count(org.orgId) from AfOrg org where org.orgId in " +
				"(select distinct M.id.orgId from AfViewReport M where M.id.orgId in " +
				"(" + childOrgIds + ")  and M.id.templateId='" + templateId + "' "			
					+" and M.id.versionId='" + versionId + "')";*/
				sql = "select count(org.orgId) from AfOrg org where org.orgId in " +
				"(select distinct M.id.orgId from AfViewReport M where M.id.orgId in " +
				"(select aor.orgId from AfOrg aor where aor.preOrgId='"+ orgId + "')" +
				//" and aor.orgId in (" + childOrgIds + ")) " +
				" and M.id.templateId='" + templateId + "' " +
				" and M.id.versionId='" + versionId + "')";
			
			}
			
			List list = session.createQuery(sql).list();
			
			if(list!=null && list.size()>0){
					count = ((Integer)list.get(0)).intValue();
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(conn!=null)			
				conn.closeSession();
		}
		return count;
	}
	
	/**
	 * 已使用hibernate 卞以刚 2011-12-21
	 * 影响对象：AfOrg AfViewReport AfCollectRelation
	 * 得到应报机构列表
	 * @param childRepId
	 * @param versionId
	 * @param childOrgIds
	 * @return
	 */
	public List getMustOrgList(String templateId, String versionId, String childOrgIds,
			String orgId, Integer type){
		List result = null;	
		DBConn conn = null;
		Session session = null;
		
		if(templateId == null || templateId.equals("")
				|| versionId == null || versionId.equals("")
				/*|| childOrgIds == null || childOrgIds.equals("")*/)
			return result;
		
		try{
			conn=new DBConn();
			session=conn.openSession();
			
			String sql = "";

			//总行汇虚拟机构
			if (type.equals(Integer.valueOf(1)) || type.equals(Integer.valueOf(4))){
				sql = "select org from AfOrg org where org.orgId in " +
				"(select distinct M.comp_id.orgId from AfViewReport M where " 
				//	+ "M.comp_id.orgId in (" + childOrgIds + ")  and " 
					+ "M.comp_id.templateId='" + templateId + "' "			
					+ "  and  M.comp_id.versionId='" + versionId + "'";
				//	+ ") and org.isCollect=1";
				if(type.equals(Integer.valueOf(1)))
					sql += ") and org.isCollect=1 and org.orgType='"+com.fitech.gznx.common.Config.COLLECT_ORG_PARENT_ID+"'";
				if(type.equals(Integer.valueOf(4)))
					sql += ") and org.isCollect=1 and org.orgType='"+com.fitech.gznx.common.Config.COLLECT_ORG_PARENT_QT_ID+"'";
			
			}
			//虚拟机构汇分行
			else if(type.equals(Integer.valueOf(2)) || type.equals(Integer.valueOf(5))){
				sql = "select org from AfOrg org where org.orgId in " +
				"(select distinct M.id.orgId from AfViewReport M,AfCollectRelation afc" +
				" where afc.id.orgId=M.id.orgId " +
				//" and M.id.orgId in (" + childOrgIds + ")" +
				" and M.id.templateId='" + templateId + 
				"' and M.id.versionId='" + versionId + "'" +
				" and afc.id.collectId='"+ orgId +"')";
			
			}
			//分行汇支行
			else{
				sql = "select org from AfOrg org where org.orgId in " +
				"(select distinct M.id.orgId from AfViewReport M where M.id.orgId in " +
				//"(" + childOrgIds + ")
				"(select aor.orgId from AfOrg aor where aor.preOrgId='"+ orgId + "')" +
				//"' and aor.orgId in (" + childOrgIds + ")) " +
				"and M.id.templateId='" + templateId + "' "	+
				"and M.id.versionId='" + versionId + "')";
			
			}
			StringBuffer sb = new StringBuffer(sql);
			sb.append(" and (org.orgId in (select r.id.orgId from AfTemplateCollRep r where r.id.templateId='" + templateId + "' and r.id.versionId='" + versionId + "'))");
			List list=session.createQuery(sb.toString()).list();
			
			if(list!=null && list.size()>0){
				result = new ArrayList();
				for(int j=0;j<list.size();j++){
					AfOrg org = (AfOrg)list.get(j);
					result.add(org);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(conn!=null)			
				conn.closeSession();
		}
		return result;
	}
	/**
	 * jdbc技术 需要修改 卞以刚 2011-12-21
	 * 修改所有表的机构信息
	 * @param orgInfoForm
	 * @return
	 */
	public static boolean updateAllOrgInfo(OrgInfoForm orgInfoForm) {
		StringBuffer addsql= null;
		DBConn conn=null;
    	Session session=null;
    	Connection connection=null;
    	Statement stmt = null;
    	boolean result=false;
    	try {
			conn = new DBConn();
	    	session=conn.beginTransaction();
	    	connection=session.connection();
	    	connection.setAutoCommit(false);	    	
	    	stmt = connection.createStatement();
	    	addsql = new StringBuffer();

	    	addsql.append("update AF_ORG set PRE_ORG_ID=").append(orgInfoForm.getNewOrgId()).
			append(" where PRE_ORG_ID=").append(orgInfoForm.getNewOrgId());
	    	stmt.addBatch(addsql.toString().toLowerCase());

	    	addsql = new StringBuffer();
			addsql.append("update ORG set PRE_ORG_ID=").append(orgInfoForm.getNewOrgId()).
			append(" where PRE_ORG_ID=").append(orgInfoForm.getNewOrgId());
			stmt.addBatch(addsql.toString().toLowerCase());			

			addsql = new StringBuffer();
			addsql.append("update AF_REPORT set ORG_ID=").append(orgInfoForm.getNewOrgId()).
			append(" where ORG_ID=").append(orgInfoForm.getOrgId());
			stmt.addBatch(addsql.toString().toLowerCase());
			
			addsql = new StringBuffer();
			addsql.append("update REPORT_IN set ORG_ID=").append(orgInfoForm.getNewOrgId()).
			append(" where ORG_ID=").append(orgInfoForm.getOrgId());
			stmt.addBatch(addsql.toString().toLowerCase());
			
			addsql = new StringBuffer();
			addsql.append("update DEPARTMENT set ORG_ID=").append(orgInfoForm.getNewOrgId()).
			append(" where ORG_ID=").append(orgInfoForm.getOrgId());
			stmt.addBatch(addsql.toString().toLowerCase());
			
			addsql = new StringBuffer();
			addsql.append("update M_PUR_ORG set ORG_ID=").append(orgInfoForm.getNewOrgId()).
			append(" where ORG_ID=").append(orgInfoForm.getOrgId());
			stmt.addBatch(addsql.toString().toLowerCase());
			
			addsql = new StringBuffer();
			addsql.append("update m_rep_range set ORG_ID=").append(orgInfoForm.getNewOrgId()).
			append(" where ORG_ID=").append(orgInfoForm.getOrgId());
			stmt.addBatch(addsql.toString().toLowerCase());
			
			addsql = new StringBuffer();
			addsql.append("update m_user_grp set set_org_id=").append(orgInfoForm.getNewOrgId()).
			append(" where set_org_id=").append(orgInfoForm.getOrgId());
			stmt.addBatch(addsql.toString().toLowerCase());
			
			addsql = new StringBuffer();
			addsql.append("update OPERATOR set ORG_ID=").append(orgInfoForm.getNewOrgId()).
			append(" where ORG_ID=").append(orgInfoForm.getOrgId());
			stmt.addBatch(addsql.toString().toLowerCase());
	    	
			
			addsql = new StringBuffer();
			addsql.append("update report_in set ORG_ID=").append(orgInfoForm.getNewOrgId()).
			append(" where ORG_ID=").append(orgInfoForm.getOrgId());
			stmt.addBatch(addsql.toString().toLowerCase());
			
			addsql = new StringBuffer();
			addsql.append("update AF_ORG set ORG_NAME='").append(orgInfoForm.getOrgName()).append("',REGION_ID='").
			append(orgInfoForm.getOrgRegion()).append("',ORG_ID=").append(orgInfoForm.getNewOrgId()).
			append(" where ORG_ID=").append(orgInfoForm.getOrgId());
			stmt.addBatch(addsql.toString().toLowerCase());
			
			addsql = new StringBuffer();
			addsql.append("update ORG set ORG_NAME='").append(orgInfoForm.getOrgName()).append("',REGION_ID='").
			append(orgInfoForm.getOrgRegion()).append("',ORG_ID=").append(orgInfoForm.getNewOrgId()).
			append(" where ORG_ID=").append(orgInfoForm.getOrgId());
			stmt.addBatch(addsql.toString().toLowerCase());
			
			addsql = new StringBuffer();
			addsql.append("update AF_COLLECT_RELATION set ORG_ID=").append(orgInfoForm.getNewOrgId()).
			append(" where ORG_ID=").append(orgInfoForm.getOrgId());
			stmt.addBatch(addsql.toString().toLowerCase());
			
			addsql = new StringBuffer();
			addsql.append("update AF_TEMPLATE_ORG_RELATION set ORG_ID=").append(orgInfoForm.getNewOrgId()).
			append(" where ORG_ID=").append(orgInfoForm.getOrgId());
			stmt.addBatch(addsql.toString().toLowerCase());
	    	
			stmt.executeBatch();
			result = true;
		}  catch (Throwable e1) {
			e1.printStackTrace();
			result= false;
		}finally{
			try {
				if(result == true) {
					connection.commit();
				}else{
					connection.rollback();
				}
				connection.setAutoCommit(result);
				if(stmt != null) 
					stmt.close();
				if(connection != null)
					connection.close();
				if(session != null) 
					session.close();
				if(conn != null) 
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
	 * @return
	 */
	public static String findTopOrg(){
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
		return (String)resultList.get(0);
	}
	/**
	 * 查询下级行是否存在虚拟机构
	 * @param orgId
	 * @return
	 */
	public static boolean isExistAttrOrgId(String orgId) {
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
			String hsql = "from AfCollectRelation af where af.id.collectId='" + orgId + "' and af.id.orgId in(select org.orgId from AfOrg org where org.isCollect=1)";
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
}
