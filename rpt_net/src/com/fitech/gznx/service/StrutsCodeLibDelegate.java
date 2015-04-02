package com.fitech.gznx.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import org.apache.struts.util.LabelValueBean;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.util.FitechException;
import com.fitech.gznx.common.PageListInfo;
import com.fitech.gznx.form.AFTemplateForm;
import com.fitech.gznx.form.CodeLibForm;
import com.fitech.gznx.po.AfCodelib;
import com.fitech.gznx.po.AfCodelibId;
import com.fitech.gznx.po.AfTemplate;


public class StrutsCodeLibDelegate {
	private static FitechException log = new FitechException(
			StrutsCodeLibDelegate.class);

	/**
	 * 
	 * ��ѯ�����ֵ���õ����������ļ�¼���б�
	 * 
	 * @param hql
	 *            String ��ѯ����
	 * @author Nick
	 * @return List ���з��������ļ�¼�б�
	 */
	public static List getCodeLibList(String codeId) {
		return getCodeLibList(codeId, false);
	}

	/**
	 * 
	 * ��ѯ�����ֵ���õ����������ļ�¼���б�
	 * 
	 * @param hql
	 *            String ��ѯ����
	 * @param boolean
	 *            �Ƿ���Ҫ��������
	 * @author Nick
	 * @return List ���з��������ļ�¼�б�
	 */
	public static List getCodeLibList(String codeId, boolean isOrderBy) {
		StringBuffer hql = new StringBuffer();
		hql.append("from AfCodelib c where c.id.codeType=?");

		if (isOrderBy) {
			hql.append(" order by c.id.codeId desc");
		} else {
			hql.append(" order by c.id.codeId");
		}

		return getCodeLibByHql(hql.toString(), codeId);
	}
 
	/**
	 * 
	 * ��ѯ�����ֵ���õ����������ļ�¼���б�
	 * 
	 * @param hql
	 *            String ��ѯ����
	 * @author Nick
	 * @return List ���з��������ļ�¼�б�
	 */
	private static List getCodeLibByHql(String hql, String codeId) {
		// List���ϵĶ���
		List result = new ArrayList();
		// ���Ӷ���ͻỰ�����ʼ��
		DBConn conn = null;
		Session session = null;

		// ��ѯ����HQL������
		try {
			// conn�����ʵ����
			conn = new DBConn();
			// �����ӿ�ʼ�Ự
			session = conn.openSession();
			Query query = session.createQuery(hql);
			query.setString(0, codeId);
			result = query.list();

		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			// ������Ӵ��ڣ���Ͽ��������Ự������
			if (conn != null)
				conn.closeSession();
		}
		return result;
	}

	/**
	 * ��ʹ��Hibernate ���Ը� 2011-12-27
	 * Ӱ�����AfCodelib 
	 * ��ѯ�����ֵ���õ����������ļ�¼
	 * 
	 * @param codeTypeId
	 *            String �ֵ�����ID
	 * 
	 * @param codeId
	 *            String �ֵ�ID
	 * 
	 * @author Nick
	 * @return List ���з��������ļ�¼�б�
	 */
	public static AfCodelib getCodeLib(String codeTypeId, String codeId) {
		AfCodelib result = null;
		// ���Ӷ���ͻỰ�����ʼ��
		DBConn conn = null;
		Session session = null;

		// ��ѯ����HQL������
		try {
			// conn�����ʵ����
			conn = new DBConn();
			// �����ӿ�ʼ�Ự
			session = conn.openSession();

			String hql = "from AfCodelib c where c.id.codeType=? and c.id.codeId=?";
			Query query = session.createQuery(hql);
			query.setString(0, codeTypeId);
			query.setString(1, codeId);

			List lst = query.list();
			if (lst != null && lst.size() > 0) {
				result = (AfCodelib) lst.get(0);
			} else {
				result = new AfCodelib();
			}

		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			// ������Ӵ��ڣ���Ͽ��������Ự������
			if (conn != null)
				conn.closeSession();
		}
		return result;
	}
    /**
     * 
     * title:�÷������ڸ����ֵ����ͷ����ֵ�ֵ
     * author:chenbing
     * date:2008-3-1
     * @param codeTypeId
     * @return
     */
	public static List getIntervalList(String codeTypeId) {
		List result = new ArrayList();
		// ���Ӷ���ͻỰ�����ʼ��
		DBConn conn = null;
		Session session = null;

		// ��ѯ����HQL������
		try {
			// conn�����ʵ����
			conn = new DBConn();
			// �����ӿ�ʼ�Ự
			session = conn.openSession();
			String type =(String)Config.TAKEDATA_TIME_INTERVAL.get(codeTypeId);
			String hql = "select c.codeName from AfCodelib c where c.id.codeType='"
					+ type + "'";
			List list = session.createQuery(hql).list();
			for(int i=0;i<list.size();i++){
				String times = ((String)list.get(i)).trim();
				String[] strs = times.split("/");
				result.add(strs);
			}
			result = result.size() == 0 ? null : result;
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			// ������Ӵ��ڣ���Ͽ��������Ự������
			if (conn != null)
				conn.closeSession();
		}
		return result;

	}
	
/*
	 * ��ѯ����ϵͳ�ֵ���¼
	 * 
	 * XY
	 * */
	/*
	public static List select()
			throws Exception {
		List result = null;
		DBConn conn = null;
		Session session = null;
		Query query = null;

		try {
			conn = new DBConn();
			session = conn.openSession();

			String hql = "select C.typeName from AfCodelib C group by C.typeName ";
			System.out.println(hql);
			query = session.createQuery(hql);
			
			List list = query.list();
			if (list != null && list.size() != 0) {
				result = new ArrayList();
				for (int i = 0; i < list.size(); i++) {
					CodeLibForm codeLibFormTemp = new CodeLibForm();
					Object[] os = (Object[]) list.get(i);
					System.out.println(String.valueOf(os[0]));
					codeLibFormTemp.setCodeId((String.valueOf(os[0])) );
					result.add(codeLibFormTemp);
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
	*/
	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-28
	 * Ӱ�����AfCodelib
	 * �жϲ����Ƿ����
	 * @return boolean
	 * @exception Exception
	 */
	public static boolean isExist(CodeLibForm codeLibForm) throws Exception {
		boolean result = false;
		DBConn conn = null;
		Session session = null;
		String codeTypeId = codeLibForm.getCodeTypeId();
		String codeId = codeLibForm.getCodeId();
		String effectiveDate = codeLibForm.getEffectiveDate();
		
		try {
			if (codeTypeId != null&&codeId != null&&effectiveDate != null) {
				conn = new DBConn();
				session = conn.openSession();
				String hql = "from AfCodelib C where C.id.codeType='"+codeTypeId+"' and C.id.codeId='"+codeId+"'";
				System.out.println(hql);
				Query query = session.createQuery(hql);
				List list = query.list();
				
				if (list.size()!=0)
					result = true;
				else
					result = false;
			}
		} catch (Exception e) {
			result = false;
			log.printStackTrace(e);
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		
		return result;
	}

	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-28
	 * Ӱ�����AfCodelib AfCodelibId
	 * ���Ӳ���
	 * 
	 * return boolean ���ӳɹ����
	 */
	public static boolean addCodeLib(CodeLibForm codeLibForm) {
		boolean result = false;
		DBConn conn = null;
		Session session = null;
		try {
			conn = new DBConn();
			session = conn.beginTransaction();
			
			AfCodelib codeLib = new AfCodelib();
			AfCodelibId codeLibId = new AfCodelibId();
			codeLibId.setCodeId(new Long(codeLibForm.getCodeId()));			
			codeLibId.setCodeType(new Long(codeLibForm.getCodeTypeId()));
			codeLib.setId(codeLibId);
			codeLib.setTypeName(codeLibForm.getCodeTypeValue());
			
			codeLib.setEditFlg(new Long(codeLibForm.getIsModi()));
			codeLib.setCodeName(codeLibForm.getCodeValue());
			codeLib.setCodeBak(codeLibForm.getCodeDesc());
			
			

			
			session.save(codeLib);
			session.flush();
			result = true;
		} catch (Exception e) {
			log.printStackTrace(e);
			result = false;
		} finally {
			if (conn != null) {
				conn.endTransaction(result);
			}
		}
		return result;
	}
	
	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-28
	 * Ӱ�����AfCodelib AfCodelibId
	 * ɾ������
	 * 
	 * return boolean ɾ���ɹ����
	 */
	public static boolean deleteCodeLib(CodeLibForm codeLibForm) {
		boolean result = false;
		DBConn conn = null;
		Session session = null;
		try {
			conn = new DBConn();
			session = conn.beginTransaction();
			
			AfCodelib codeLib = new AfCodelib();
			AfCodelibId codeLibId = new AfCodelibId();
			codeLibId.setCodeId(new Long(codeLibForm.getCodeId()));			
			codeLibId.setCodeType(new Long(codeLibForm.getCodeTypeId()));
			codeLib.setId(codeLibId);
			codeLib.setTypeName(codeLibForm.getCodeTypeValue());
			//codeLib.setEditFlg(new Long(codeLibForm.getIsModi()));
			codeLib.setCodeName(codeLibForm.getCodeValue());
			codeLib.setCodeBak(codeLibForm.getCodeDesc());

			
			session.delete(codeLib);
			session.flush();
			result = true;
		} catch (Exception e) {
			log.printStackTrace(e);
			result = false;
		} finally {
			if (conn != null) {
				conn.endTransaction(result);
			}
		}
		return result;
	}
	
	/**
	 * ��ȡ�޸Ĳ�����Ϣ
	 * 
	 * @return
	 */

	public static void EditCodeLib(CodeLibForm codeLibForm) {
		
		DBConn conn = null;
		Session session = null;
		Query query = null;
		
		try {
			conn = new DBConn();
			session = conn.openSession();
			
			String codeTypeId = codeLibForm.getCodeTypeId();
			String codeId = codeLibForm.getCodeId();
			String effectiveDate = codeLibForm.getEffectiveDate();
			
			String hql = "select C.id.codeType,C.typeName,C.id.codeId,C.codeName,C.editFlg from AfCodelib C where C.id.codeType="
					+ codeTypeId
					+ " and C.id.codeId="
					+ codeId
					+ "";

			query = session.createQuery(hql);

			List list = query.list();
			
			if (list != null && list.size() != 0) {
				Object[] os = (Object[]) list.get(0);
				codeLibForm.setCodeTypeId((String.valueOf(os[0])) );
				codeLibForm.setCodeTypeValue((String.valueOf(os[1])) );
				codeLibForm.setCodeId((String.valueOf(os[2])) );
				codeLibForm.setCodeValue((String.valueOf( os[3])));
				
				codeLibForm.setIsModi((Integer.valueOf(String.valueOf( os[4]))));
			}
			
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			if (conn != null)
				conn.closeSession();
		}
	}

	/**
	 * �޸Ĳ�����Ϣ
	 * xy
	 * @param
	 * @return �޸ĳɹ����
	 */
	public static boolean updateCodeLib(CodeLibForm codeLibForm) {
		boolean result = false;
		DBConn conn = null;
		Session session = null;
		try {
			conn = new DBConn();
			session = conn.beginTransaction();

			if (codeLibForm != null && !codeLibForm.equals("")) {
				/**ɾ���޸�ǰ��¼*/
				AfCodelib codeLib = new AfCodelib();
				//codeLib.setCodeId(new Long(codeLibForm.getCodeId_upd()));
				AfCodelibId codeLibId = new AfCodelibId();
				codeLibId.setCodeId(new Long(codeLibForm.getCodeId_upd()));			
				codeLibId.setCodeType(new Long(codeLibForm.getCodeTypeId_upd()));
				codeLib.setId(codeLibId);
				
				session.delete(codeLib);
				session.flush();
				
				/**�����޸ĺ��¼*/
				if (codeLib != null && codeLibForm != null) {
					codeLibId.setCodeId(new Long(codeLibForm.getCodeId()));
					codeLibId.setCodeType(new Long(codeLibForm.getCodeTypeId()));
					
					codeLib.setId(codeLibId);
					codeLib.setTypeName(codeLibForm.getCodeTypeValue());
					codeLib.setEditFlg(new Long(codeLibForm.getIsModi()));
					codeLib.setCodeName(codeLibForm.getCodeValue());
					codeLib.setCodeBak(codeLibForm.getCodeDesc());
				}
				session.save(codeLib);
				session.flush();
				result = true;
			}
		} catch (Exception e) {
			log.printStackTrace(e);
			result = false;
		} finally {
			if (conn != null) {
				conn.endTransaction(result);
			}
		}

		return result;
	}
	/**
	 * 
	 * title:�÷������ڸ���������ֵ����ͷ�����Ӧ���ֵ��б�
	 * author:chenbing
	 * date:2008-4-5
	 * @param codeTypeId
	 * @return
	 */
	public static List getCodeLabelValue(String codeTypeId){
		List result = new ArrayList();
		// ���Ӷ���ͻỰ�����ʼ��
		DBConn conn = null;
		Session session = null;

		// ��ѯ����HQL������
		try {
			// conn�����ʵ����
			conn = new DBConn();
			// �����ӿ�ʼ�Ự
			session = conn.openSession();

			String hql = "select c.codeName,c.id.codeId from AfCodelib c where c.id.codeType='" + codeTypeId + "'";
			List list = session.createQuery(hql).list();
			for(int i=0;i<list.size();i++){
				Object[] os = (Object[])list.get(i);
				result.add(new LabelValueBean((String)os[0],(String)os[1]));
			}
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			// ������Ӵ��ڣ���Ͽ��������Ự������
			if (conn != null)
				conn.closeSession();
		}
		return result;
		
	}
	/**
	 * 
	 * title:�÷������ڸ����ֵ����ͺ�
	 * author:chenbing
	 * date:2008-5-5
	 * @param codeTypeId
	 * @param param
	 * @return
	 */
	public static List getCodeLabelValueByPurview(String codeTypeId,List codeIdList){
		List result = new ArrayList();
		// ���Ӷ���ͻỰ�����ʼ��
		DBConn conn = null;
		Session session = null;

		// ��ѯ����HQL������
		try {
			String codes = getStrsBySplitStr(codeIdList,",","'");
			// conn�����ʵ����
			conn = new DBConn();
			// �����ӿ�ʼ�Ự
			session = conn.openSession();
			String hql = "select c.codeName,c.id.codeId from AfCodelib c where c.id.codeType='" + codeTypeId + "'";
			hql = hql + " and c.id.codeId in(" + codes + ")";
			List list = session.createQuery(hql).list();
			for(int i=0;i<list.size();i++){
				Object[] os = (Object[])list.get(i);
				result.add(new LabelValueBean((String)os[0],(String)os[1]));
			}
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			// ������Ӵ��ڣ���Ͽ��������Ự������
			if (conn != null)
				conn.closeSession();
		}
		return result;
		
	}
	/**
	 * 
	 * title:�÷������ڽ�ָ���Ķ������ͽ�����ָ���Ľ��޷��ͷָ����������е��ַ����ӹ���ָ�����ַ��� author:chenbing
	 * author:chenbing date:2008-2-26
	 * 
	 * @param obj
	 *            ָ���Ķ���
	 * @param splitStr
	 *            �ָ���
	 * @param bracketStr
	 *            ���޷� ���Ϊ����û�н��޷�
	 * @return
	 */
	public static String getStrsBySplitStr(Object obj, String splitStr,
			String bracketStr) {

		if (obj == null)
			return null;

		bracketStr = bracketStr == null ? "" : bracketStr;

		String result = null;

		StringBuffer sb = new StringBuffer("");

		if (obj instanceof ArrayList) { // �ַ����б�

			List list = (List) obj;

			for (int i = 0; i < list.size(); i++)

				sb.append(splitStr + bracketStr + ((String) list.get(i)).trim()
						+ bracketStr);

		} else if (obj instanceof String) {// �ַ���

			String str = (String) obj;

			String[] strs = str.split(",");

			for (int i = 0; i < strs.length; i++)

				sb.append(splitStr + bracketStr + strs[i].trim() + bracketStr);

		} else if (obj instanceof HashSet) {// Set

			Set set = (HashSet) obj;

			List list = new ArrayList();

			list.addAll(set);

			for (int i = 0; i < list.size(); i++)

				sb.append(splitStr + bracketStr + ((String) list.get(i)).trim()
						+ bracketStr);
		}
		if (sb.length() > 1)

			result = sb.substring(1);

		return result;
	}

	public static List getRepTypes(String type) {
		List result = new ArrayList();
		// ���Ӷ���ͻỰ�����ʼ��
		DBConn conn = null;
		Session session = null;

		// ��ѯ����HQL������
		try {
			
			// conn�����ʵ����
			conn = new DBConn();
			// �����ӿ�ʼ�Ự
			session = conn.openSession();
			String hql = "select c.codeName,c.id.codeId from AfCodelib c where c.id.codeType=" + type ;
			
			List list = session.createQuery(hql).list();
			result.add(new LabelValueBean("",""));
			for(int i=0;i<list.size();i++){
				Object[] os = (Object[])list.get(i);
				result.add(new LabelValueBean((String)os[0],(String.valueOf(os[1]))));
			}
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			// ������Ӵ��ڣ���Ͽ��������Ự������
			if (conn != null)
				conn.closeSession();
		}
		return result;
		
	}
	public static List getRepcodebak(String type) {
		List result = new ArrayList();
		// ���Ӷ���ͻỰ�����ʼ��
		DBConn conn = null;
		Session session = null;

		// ��ѯ����HQL������
		try {
			
			// conn�����ʵ����
			conn = new DBConn();
			// �����ӿ�ʼ�Ự
			session = conn.openSession();
			String hql = "select c.codeName,c.codeBak from AfCodelib c where c.id.codeType=" + type ;
			
			List list = session.createQuery(hql).list();
			result.add(new LabelValueBean("",""));
			for(int i=0;i<list.size();i++){
				Object[] os = (Object[])list.get(i);
				result.add(new LabelValueBean((String)os[0],(String.valueOf(os[1]))));
			}
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			// ������Ӵ��ڣ���Ͽ��������Ự������
			if (conn != null)
				conn.closeSession();
		}
		return result;
		
	}
	
	/**
	 * 
	 * ��ѯ�����ֵ���õ����������ļ�¼
	 * 
	 * @param codeTypeId
	 *            String �ֵ�����ID
	 * 
	 * @param codeId
	 *            String �ֵ�ID
	 * 
	 * @author Nick
	 * @return List ���з��������ļ�¼�б�
	 */
	public static AfCodelib getCodeLibbybak(String codeTypeId, String codebak) {
		AfCodelib result = new AfCodelib();
		// ���Ӷ���ͻỰ�����ʼ��
		DBConn conn = null;
		Session session = null;

		// ��ѯ����HQL������
		try {
			// conn�����ʵ����
			conn = new DBConn();
			// �����ӿ�ʼ�Ự
			session = conn.openSession();

			String hql = "from AfCodelib c where c.id.codeType=? and c.codeBak=?";
			Query query = session.createQuery(hql);
			query.setString(0, codeTypeId);
			query.setString(1, codebak);

			List lst = query.list();
			if (lst != null && lst.size() > 0) {
				result = (AfCodelib) lst.get(0);
			}

		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			// ������Ӵ��ڣ���Ͽ��������Ự������
			if (conn != null)
				conn.closeSession();
		}
		return result;
	}
	
	
	public static AfCodelib getRepTypes(String type,String codeId) {
		AfCodelib codeLib = null;
		// ���Ӷ���ͻỰ�����ʼ��
		DBConn conn = null;
		Session session = null;

		// ��ѯ����HQL������
		try {
			
			// conn�����ʵ����
			conn = new DBConn();
			// �����ӿ�ʼ�Ự
			session = conn.openSession();
			String hql = "from AfCodelib c where c.id.codeType='" + type + "'" +
					"and c.id.codeId=" + codeId;
			
			List list = session.createQuery(hql).list();
			
			if(list!=null && list.size()>0){
				codeLib = (AfCodelib) list.get(0);
			}
			
		} catch (Exception e) {
			codeLib = null;
			log.printStackTrace(e);
		} finally {
			// ������Ӵ��ڣ���Ͽ��������Ự������
			if (conn != null)
				conn.closeSession();
		}
		return codeLib;
		
	}

	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-28
	 * Ӱ�����AfCodelib
	 * ��ѯ�����ֵ��õ���Ҫ���
	 * 
	 * @param codeLibForm
	 * @param curPage
	 * @return 
	 * 
	 */
	public static PageListInfo select(CodeLibForm codeLibForm, int curPage) {
		// TODO Auto-generated method stub

		List resList = new ArrayList();
		
    	DBConn conn = null;
    	Session session = null;
    	PageListInfo pageListInfo=null;
    	try{    		
			conn = new DBConn();
			session = conn.openSession();    		
			String hql = "select C.id.codeType,C.typeName,C.id.codeId,C.codeName,C.editFlg from AfCodelib C  ";			
			String codeId = codeLibForm.getCodeTypeId();
			hql += " where 1=1 ";
			if (codeId != null && !codeId.equals("")){
					hql+=" and C.id.codeType=" + codeId.trim();
			}
			if(codeLibForm.getCodeTypeValue()!=null && !codeLibForm.getCodeTypeValue().equals(""))
				hql += " and C.typeName = '"+codeLibForm.getCodeTypeValue()+"'";
			hql+=" order by C.id.codeType";
			pageListInfo = findByPageWithSQL(session,hql,Config.PER_PAGE_ROWS,curPage);
			//recordCount = (int) pageListInfo.getRowCount();
			List list=pageListInfo.getList();
			if (list != null && list.size() != 0) {
				
				for (int i = 0; i < list.size(); i++) {
					CodeLibForm codeLibFormTemp = new CodeLibForm();
					Object[] os = (Object[]) list.get(i);
					codeLibFormTemp.setCodeTypeId((String.valueOf(os[0])) );
					codeLibFormTemp.setCodeTypeValue((String) os[1]);
					codeLibFormTemp.setCodeId((String.valueOf(os[2])) );
					codeLibFormTemp.setCodeValue((String) os[3]);					
					codeLibFormTemp.setIsModi(((Long) os[4]).intValue());
					resList.add(codeLibFormTemp);
				}
			}
			pageListInfo.setList(resList);
    	}catch(Exception he){
    		he.printStackTrace();
    	}finally{
    		
    		if(conn != null) conn.closeSession();
    	}
		return pageListInfo;
	}
	  public static PageListInfo findByPageWithSQL(Session sessionsq,String queryStr,  int pageSize , int curPage){
	        PageListInfo pageListInfo = new PageListInfo(pageSize,curPage);
	        try {
	        	
	            Query  query;
	            query =  sessionsq.createQuery(queryStr);
	            Integer i=query.list().size();
	            pageListInfo.setRowCount(i);
	            int firstPage = (pageListInfo.getCurPage()-1)*pageSize;
	            query.setFirstResult(firstPage);
	            query.setMaxResults(pageSize);
	            pageListInfo.setList(query.list());
	            int count = curPage * pageSize;
	           
	            if(pageListInfo.getRowCount()<pageSize || curPage == pageListInfo.getPageCount())
	            	count = (int)pageListInfo.getRowCount();
	            pageListInfo.setCurPageRowCount(count);
	           
	            
	            return pageListInfo;
	        }catch (RuntimeException re) {
	            
	            throw re;
	        }catch (HibernateException e) {
				
				e.printStackTrace();
			}
			return pageListInfo;
	    }

}
