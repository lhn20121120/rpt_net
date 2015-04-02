package com.fitech.net.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.Criteria;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.util.FitechException;
import com.fitech.net.form.TargetBusinessForm;
import com.fitech.net.form.TargetNormalForm;
import com.fitech.net.hibernate.MBusiness;
import com.fitech.net.hibernate.MNormal;

/**
 * ����ָ�����
 * @author masclnj
 *
 */
public class StrutsTargetDelegate {
	//Catch��������׳��������쳣
	private static FitechException log=new FitechException(StrutsTargetDelegate.class);
	
	/**
	 * �ж�ָ��ҵ�����������Ƿ��Ѿ�����
	 * 
	 * @param normalName ָ��ҵ����������
	 * @return boolean true-���� false-������
	 */
	public static boolean isMNormalExist(String normalName){
		if(normalName == null || normalName.equals("")) return false;
		boolean bool = false;
		DBConn conn = null;
		Session session = null;
		
		String hql = "from MNormal mn where mn.normalName='"+ normalName +"'";
		try {
            conn = new DBConn();
            session = conn.openSession();
            List list = session.find(hql.toString());
            if (list != null && list.size() >0 ) 
            	bool = true;
        } catch (HibernateException he) {
        	bool = false;
            log.printStackTrace(he);
        } catch (Exception e) {
        	bool = false;
            log.printStackTrace(e);
        } finally {
            //������Ӵ��ڣ���Ͽ��������Ự������
            if (conn != null)
                conn.closeSession();
        }
		return bool;
	}
	
	/**
     * ȡ�ð�������ѯ���ļ�¼����
     * 
     * @return int ��ѯ���ļ�¼����
     * @param TargetNormalForm ������ѯ��������Ϣ��ָ��ҵ�����ƣ�
     *            
     */
    public static int getRecordCount(TargetNormalForm targetNormal) {
        int count = 0;

        //���Ӷ���ͻỰ�����ʼ��
        DBConn conn = null;
        Session session = null;

        //	 ��ѯ����HQL������
        StringBuffer hql = new StringBuffer("select count(*) from MNormal mn");
        StringBuffer where = new StringBuffer("");

        if (targetNormal != null) {
            // �����������ж�,�������Ʋ���Ϊ��
            if (targetNormal.getNormalName() != null
                    && !targetNormal.getNormalName().equals(""))
                where.append((where.toString().equals("") ? "" : " and ")
                        + "mn.normalName like '%" + targetNormal.getNormalName() + "%'");
        }

        try { //List���ϵĲ���
            //��ʼ��
            hql.append((where.toString().equals("") ? "" : " where ")
                    + where.toString());
            //conn�����ʵ����
            conn = new DBConn();
            //�����ӿ�ʼ�Ự
            session = conn.openSession();
            List list = session.find(hql.toString());
            if (list != null && list.size() == 1) {
                count = ((Integer) list.get(0)).intValue();
            }

        } catch (HibernateException he) {
            log.printStackTrace(he);
        } catch (Exception e) {
            log.printStackTrace(e);
        } finally {
            //������Ӵ��ڣ���Ͽ��������Ự������
            if (conn != null)
                conn.closeSession();
        }
        return count;
    }	
    
    /**
     * ȡ�ð�������ѯ���ļ�¼����
     * 
     * @return int ��ѯ���ļ�¼����
     * @param TargetNormalForm ������ѯ��������Ϣ��ָ��ҵ�����ƣ�
     *            
     */

    public static int getRecordCount(TargetBusinessForm targetBusiness) {
        int count = 0;

        //���Ӷ���ͻỰ�����ʼ��
        DBConn conn = null;
        Session session = null;

        //	 ��ѯ����HQL������
        StringBuffer hql = new StringBuffer("select count(*) from MBusiness mb");
        StringBuffer where = new StringBuffer("");

        if (targetBusiness != null) {
            // �����������ж�,�������Ʋ���Ϊ��
            if (targetBusiness.getBusinessName() != null
                    && !targetBusiness.getBusinessName().equals(""))
                where.append((where.toString().equals("") ? "" : " and ")
                        + "mb.businessName like '%" + targetBusiness.getBusinessName() + "%'");
        }

        try { //List���ϵĲ���
            //��ʼ��
            hql.append((where.toString().equals("") ? "" : " where ")
                    + where.toString());
            //conn�����ʵ����
            conn = new DBConn();
            //�����ӿ�ʼ�Ự
            session = conn.openSession();
            List list = session.find(hql.toString());
            if (list != null && list.size() == 1) {
                count = ((Integer) list.get(0)).intValue();
            }

        } catch (HibernateException he) {
            log.printStackTrace(he);
        } catch (Exception e) {
            log.printStackTrace(e);
        } finally {
            //������Ӵ��ڣ���Ͽ��������Ự������
            if (conn != null)
                conn.closeSession();
        }
        return count;
    }
    
    /**
     * ָ��ҵ��Ĳ�ѯ
     * 
     * @param TargetNormalForm ��ѯ������
     *            
     * @return List ������ҵ���¼������List��¼�������򣬷���null
     */
    public static List select(TargetNormalForm targetNormal, int offset, int limit) {

        //		 List���ϵĶ���
        List refVals = null;

        //���Ӷ���ͻỰ�����ʼ��
        DBConn conn = null;
        Session session = null;

        //	 ��ѯ����HQL������
        StringBuffer hql = new StringBuffer("from MNormal mn");
        StringBuffer where = new StringBuffer("");
        if (targetNormal != null) {
            // �����������ж�,�������Ʋ���Ϊ��
            if (targetNormal.getNormalName() != null
                    && !targetNormal.getNormalName().equals(""))
                where.append((where.toString().equals("") ? "" : " and ")
                        + "mn.normalName like '%" + targetNormal.getNormalName() + "%'");
        }

        try { //List���ϵĲ���
            //��ʼ��
            
            hql.append((where.toString().equals("") ? "" : " where ")
                    + where.toString());
          
            //conn�����ʵ����
            conn = new DBConn();
            //�����ӿ�ʼ�Ự
            session = conn.openSession();
            //��Ӽ�����Session
            //List list=session.find(hql.toString());
            Query query = session.createQuery(hql.toString());
            query.setFirstResult(offset).setMaxResults(limit);
            List list = query.list();

            if (list != null) {
                refVals = new ArrayList();
                //ѭ����ȡ���ݿ����������¼
                for (Iterator it = list.iterator(); it.hasNext();) {
                	TargetNormalForm targetNormalTemp = new TargetNormalForm();
                    MNormal targetNormalPersistence = (MNormal) it.next();
                    TranslatorUtil.copyPersistenceToVo(targetNormalPersistence,
                    		targetNormalTemp);
                    refVals.add(targetNormalTemp);
                }
            }
        } catch (HibernateException he) {
            refVals = null;
            log.printStackTrace(he);
        } catch (Exception e) {
            refVals = null;
            log.printStackTrace(e);
        } finally {
            //������Ӵ��ڣ���Ͽ��������Ự������
            if (conn != null)
                conn.closeSession();
        }
        return refVals;
    }
    
    /**
     * ָ��ҵ��Ĳ�ѯ
     * 
     * @param TargetNormalForm ��ѯ������
     *            
     * @return List ������ҵ���¼������List��¼�������򣬷���null
     */
    public static List select(TargetBusinessForm targetBusiness, int offset, int limit) {

        //		 List���ϵĶ���
        List refVals = null;

        //���Ӷ���ͻỰ�����ʼ��
        DBConn conn = null;
        Session session = null;

        //	 ��ѯ����HQL������
        StringBuffer hql = new StringBuffer("from MBusiness mb");
        StringBuffer where = new StringBuffer("");
        if (targetBusiness != null) {
            // �����������ж�,�������Ʋ���Ϊ��
            if (targetBusiness.getBusinessName() != null
                    && !targetBusiness.getBusinessName().equals(""))
                where.append((where.toString().equals("") ? "" : " and ")
                        + "mb.businessName like '%" + targetBusiness.getBusinessName() + "%'");
        }

        try { //List���ϵĲ���
            //��ʼ��
            
            hql.append((where.toString().equals("") ? "" : " where ")
                    + where.toString());
          
            //conn�����ʵ����
            conn = new DBConn();
            //�����ӿ�ʼ�Ự
            session = conn.openSession();
            //��Ӽ�����Session
            //List list=session.find(hql.toString());
            Query query = session.createQuery(hql.toString());
            query.setFirstResult(offset).setMaxResults(limit);
            List list = query.list();

            if (list != null) {
                refVals = new ArrayList();
                //ѭ����ȡ���ݿ����������¼
                for (Iterator it = list.iterator(); it.hasNext();) {
                	TargetBusinessForm targetBusinessTemp = new TargetBusinessForm();
                    MBusiness targetBusinessPersistence = (MBusiness) it.next();
                    TranslatorUtil.copyPersistenceToVo(targetBusinessPersistence,
                    		targetBusinessTemp);
                    refVals.add(targetBusinessTemp);
                }
            }
        } catch (HibernateException he) {
            refVals = null;
            log.printStackTrace(he);
        } catch (Exception e) {
            refVals = null;
            log.printStackTrace(e);
        } finally {
            //������Ӵ��ڣ���Ͽ��������Ự������
            if (conn != null)
                conn.closeSession();
        }
        return refVals;
    }

    /**
     * ָ��ҵ������
     * 
     * @param TargetNormalForm           
     * @return boolean result,�����ɹ�����true,���򷵻�false
     * @exception Exception����׽�쳣����
     */
    public static boolean create(TargetNormalForm targetNormalForm) throws Exception {

        boolean result = false; //��result���
        MNormal targetNormalPersistence = new MNormal();

        if (targetNormalForm == null || targetNormalForm.getNormalName().equals("")) {
            return result;
        }
        //���Ӷ���ĳ�ʼ��
        DBConn conn = null;
        //�Ự����ĳ�ʼ��
        Session session = null;

        try {
            //��ʾ�㵽�־ò��CopyTo����(McurUnitPresistence�־ò�����ʵ��,mCurrForm��ʾ�����)
            if (targetNormalForm.getNormalName() == null
                    || targetNormalForm.getNormalName().equals("")) {
                return result;
            }

            TranslatorUtil.copyVoToPersistence(targetNormalPersistence, targetNormalForm);
            //ʵ�������Ӷ���
            conn = new DBConn();
            //�Ự����Ϊ���Ӷ������������
            session = conn.beginTransaction();
            //mCurrForm�����ʵ����
            targetNormalForm = new TargetNormalForm();
            targetNormalForm.getNormalName();

            //�Ự���󱣴�־ò����
            session.save(targetNormalPersistence);
            //��־Ϊtrue
            result = true;
        } catch (HibernateException e) {
            //�־ò���쳣����׽
            log.printStackTrace(e);
        } finally {
            //�������״̬��,��Ͽ�,��������,����
            if (conn != null)
                conn.endTransaction(result);
        }
        return result;

    }
    
    /**
     * ָ��ҵ������
     * 
     * @param TargetNormalForm           
     * @return boolean result,�����ɹ�����true,���򷵻�false
     * @exception Exception����׽�쳣����
     */
    public static boolean create(TargetBusinessForm targetBusinessForm) throws Exception {

        boolean result = false; //��result���
        MBusiness targetBusinessPersistence = new MBusiness();

        if (targetBusinessForm == null || targetBusinessForm.getBusinessName().equals("")) {
            return result;
        }
        //���Ӷ���ĳ�ʼ��
        DBConn conn = null;
        //�Ự����ĳ�ʼ��
        Session session = null;

        try {
            //��ʾ�㵽�־ò��CopyTo����(McurUnitPresistence�־ò�����ʵ��,mCurrForm��ʾ�����)
            if (targetBusinessForm.getBusinessName() == null
                    || targetBusinessForm.getBusinessName().equals("")) {
                return result;
            }

            TranslatorUtil.copyVoToPersistence(targetBusinessPersistence, targetBusinessForm);
            //ʵ�������Ӷ���
            conn = new DBConn();
            //�Ự����Ϊ���Ӷ������������
            session = conn.beginTransaction();
            //mCurrForm�����ʵ����
            targetBusinessForm = new TargetBusinessForm();
            targetBusinessForm.getBusinessName();

            //�Ự���󱣴�־ò����
            session.save(targetBusinessPersistence);
            //��־Ϊtrue
            result = true;
        } catch (HibernateException e) {
            //�־ò���쳣����׽
            log.printStackTrace(e);
        } finally {
            //�������״̬��,��Ͽ�,��������,����
            if (conn != null)
                conn.endTransaction(result);
        }
        return result;

    }
    /**
     * ָ��ҵ��ɾ��
     * 
     * @param TargetNormalForm           
     * @return boolean result,ɾ���ɹ�����true,���򷵻�false
     * @exception Exception����׽�쳣����
     */
    public static boolean delete(TargetBusinessForm targetBusinessForm) throws Exception {

        boolean result = false; //��result���
        MBusiness targetBusinessPersistence = new MBusiness();

        if (targetBusinessForm == null || targetBusinessForm.getBusinessId().equals("")) {
            return result;
        }
        //���Ӷ���ĳ�ʼ��
        DBConn conn = null;
        //�Ự����ĳ�ʼ��
        Session session = null;

        try {
            //��ʾ�㵽�־ò��CopyTo����(McurUnitPresistence�־ò�����ʵ��,mCurrForm��ʾ�����)
           
        	if (targetBusinessForm.getBusinessId() == null
                   ) {
                return result;
            }

            TranslatorUtil.copyVoToPersistence(targetBusinessPersistence, targetBusinessForm);
            //ʵ�������Ӷ���
            conn = new DBConn();
            //�Ự����Ϊ���Ӷ������������
            session = conn.beginTransaction();
            //mCurrForm�����ʵ����
            targetBusinessForm = new TargetBusinessForm();
            targetBusinessForm.getBusinessName();

            //�Ự���󱣴�־ò����
            session.delete(targetBusinessPersistence);
            //��־Ϊtrue
            result = true;
        } catch (HibernateException e) {
            //�־ò���쳣����׽
            log.printStackTrace(e);
        } finally {
            //�������״̬��,��Ͽ�,��������,����
            if (conn != null)
                conn.endTransaction(result);
        }
        return result;

    }
    
    
    
    
    /**
     * ��ѯһ����¼,���ص�EditAction��
     * 
     * @param targetNormal
     *            
     * @return targetNormal ����һ����¼
     */

    public static TargetNormalForm selectOne(TargetNormalForm targetNormal) {
    	
        //���Ӷ���ͻỰ�����ʼ��
        DBConn conn = null;
        Session session = null;

        if (targetNormal != null)
         
        try {
            if (targetNormal.getNormalId() != null
                    && !targetNormal.getNormalId().equals(""))
                //conn�����ʵ����
                conn = new DBConn();
            //�����ӿ�ʼ�Ự
            session = conn.openSession();

            MNormal targetNormalPersistence = (MNormal) session.load(MNormal.class,
            		targetNormal.getNormalId());

            TranslatorUtil.copyPersistenceToVo(targetNormalPersistence, targetNormal);

        } catch (HibernateException he) {
            log.printStackTrace(he);
        } catch (Exception e) {
            log.printStackTrace(e);
        } finally {
            //������Ӵ��ڣ���Ͽ��������Ự������
            if (conn != null)
                conn.closeSession();
        }
        return targetNormal;
    }
    
    /**
     * ��ѯһ����¼,���ص�EditAction��
     * 
     * @param targetNormal
     *            
     * @return targetNormal ����һ����¼
     */

    public static TargetBusinessForm selectOne(TargetBusinessForm targetBusiness) {
    	
        //���Ӷ���ͻỰ�����ʼ��
        DBConn conn = null;
        Session session = null;

        if (targetBusiness != null)
         
        try {
            if (targetBusiness.getBusinessId() != null
                    && !targetBusiness.getBusinessId().equals(""))
                //conn�����ʵ����
                conn = new DBConn();
            //�����ӿ�ʼ�Ự
            session = conn.openSession();

            MBusiness targetBusinessPersistence = (MBusiness) session.load(MBusiness.class,
            		targetBusiness.getBusinessId());

            TranslatorUtil.copyPersistenceToVo(targetBusinessPersistence, targetBusiness);

        } catch (HibernateException he) {
            log.printStackTrace(he);
        } catch (Exception e) {
            log.printStackTrace(e);
        } finally {
            //������Ӵ��ڣ���Ͽ��������Ự������
            if (conn != null)
                conn.closeSession();
        }
        return targetBusiness;
    }

    /**
     * ����ָ������
     * 
     * @param mCurrForm
     *            MCurrForm ������ݵĶ���
     * @exception Exception
     *                ���MCurrForm����ʧ��,��׽�׳��쳣
     */
    public static boolean update(TargetNormalForm targetNormal) throws Exception {
        boolean result = false;
        DBConn conn = null;
        Session session = null;

        MNormal targetNormalPersistence = new MNormal();

        if (targetNormal == null) {
            return result;
        }
        try {
            if (targetNormal.getNormalName() == null
                    || targetNormal.getNormalName().equals("")) {
                return result;
            }
            conn = new DBConn();
            session = conn.beginTransaction();

            TranslatorUtil.copyVoToPersistence(targetNormalPersistence, targetNormal);            
           
            session.update(targetNormalPersistence);            

            result = true;
        } catch (HibernateException he) {
            log.printStackTrace(he);
        } finally {
            if (conn != null)
                conn.endTransaction(result);
        }
        return result;
    }
    
    /**
     * ����ָ������
     * 
     * @param mCurrForm
     *            MCurrForm ������ݵĶ���
     * @exception Exception
     *                ���MCurrForm����ʧ��,��׽�׳��쳣
     */
    public static boolean update(TargetBusinessForm targetBusiness) throws Exception {
        boolean result = false;
        DBConn conn = null;
        Session session = null;

        MBusiness targetBusinessPersistence = new MBusiness();

        if (targetBusiness == null) {
            return result;
        }
        try {
            if (targetBusiness.getBusinessName() == null
                    || targetBusiness.getBusinessName().equals("")) {
                return result;
            }
            conn = new DBConn();
            session = conn.beginTransaction();

            TranslatorUtil.copyVoToPersistence(targetBusinessPersistence, targetBusiness);            
           
            session.update(targetBusinessPersistence);            

            result = true;
        } catch (HibernateException he) {
            log.printStackTrace(he);
        } finally {
            if (conn != null)
                conn.endTransaction(result);
        }
        return result;
    }

    /**
     * ָ��ҵ��ɾ������
     * 
     * @param TargetNormalForm
     *            TargetNormalForm ��ѯ���Ķ���
     * @return boolean ���ɾ���ɹ��򷵻�true,����false
     */
    public static boolean remove(TargetNormalForm targetNormal) throws Exception {
        //�ñ�־result
        boolean result = false;
        //���ӺͻỰ����ĳ�ʼ��
        DBConn conn = null;
        Session session = null;

        //mCurr�Ƿ�Ϊ��,����result
        if (targetNormal == null && targetNormal.getNormalId() == null)
            return result;

        try {
            //	���Ӷ���ͻỰ�����ʼ��
            conn = new DBConn();
            session = conn.beginTransaction();
            //��mCurrForm�Ļ��ҵ�λ�Ļ�����������HQL�в�ѯ
            MNormal targetNormals = (MNormal) session.load(MNormal.class, targetNormal
                    .getNormalId());
            //�Ự����ɾ���־ò����
            session.delete(targetNormals);
            session.flush();

            //ɾ���ɹ�����Ϊtrue
            result = true;
        } catch (HibernateException he) {
            //��׽������쳣,�׳�
            log.printStackTrace(he);
        } finally {
            //�����������Ͽ����ӣ������Ự������
            if (conn != null)
                conn.endTransaction(result);
        }
        return result;
    }
    
    /**
     * ָ��ҵ��ɾ������
     * 
     * @param TargetNormalForm
     *            TargetNormalForm ��ѯ���Ķ���
     * @return boolean ���ɾ���ɹ��򷵻�true,����false
     */
    public static boolean remove(TargetBusinessForm targetBusiness) throws Exception {
        //�ñ�־result
        boolean result = false;
        //���ӺͻỰ����ĳ�ʼ��
        DBConn conn = null;
        Session session = null;

        //mCurr�Ƿ�Ϊ��,����result
        if (targetBusiness == null && targetBusiness.getBusinessId() == null)
            return result;

        try {
            //	���Ӷ���ͻỰ�����ʼ��
            conn = new DBConn();
            session = conn.beginTransaction();
            //��mCurrForm�Ļ��ҵ�λ�Ļ�����������HQL�в�ѯ
            MBusiness targetBusiness2 = (MBusiness) session.load(MBusiness.class, targetBusiness
                    .getBusinessId());
            //�Ự����ɾ���־ò����
            session.delete(targetBusiness2);
            session.flush();

            //ɾ���ɹ�����Ϊtrue
            result = true;
        } catch (HibernateException he) {
            //��׽������쳣,�׳�
            log.printStackTrace(he);
        } finally {
            //�����������Ͽ����ӣ������Ự������
            if (conn != null)
                conn.endTransaction(result);
        }
        return result;
    }

    /**
     * ��ѯ���е�ָ������
     * @author  gongming
     * @date    2008-06-19
     * @return List
     */
    public static List findAll(){
        List tarLst = null;
        DBConn dbCon = null;
        Session s = null;
        try{
            dbCon = new DBConn();
            s = dbCon.openSession();
            Criteria c = s.createCriteria(MBusiness.class);
            tarLst = c.list();      
        }catch (Exception e){
            log.printStackTrace(e);
        }finally{
            if(dbCon != null)
                dbCon.closeSession();
        }
        return tarLst;
    }
    /**
     *  
     */
    public static List selectInfo(String id){
    	List result=null;
    	DBConn dbCon = null;
        Session s = null;
    	try{
    		dbCon = new DBConn();
    		s=dbCon.openSession();
    	
    		System.out.println(id);
    		result=s.createQuery("from TargetDefine a where a.mbusiness.businessId="+id).list();
    	//	String hql="from MBusiness b join TargetDefine d on b.mbusiness=d.mbusiness where b.NM_BUSINESSSORTID="+id;
    	//	result=s.getSessionFactory().openSession().;
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	
    	return result;
    }
    /**
     *  
     * @param id
     * @return
     */
    public static List selectNormalInfo(String id){
    	List result=null;
    	DBConn dbCon = null;
        Session s = null;
    	try{
    		dbCon = new DBConn();
    		s=dbCon.openSession();
    	
    		System.out.println(id);
    		result=s.createQuery("from TargetDefine a where a.mnormal.normalId="+id).list();
    	//	String hql="from MBusiness b join TargetDefine d on b.mbusiness=d.mbusiness where b.NM_BUSINESSSORTID="+id;
    	//	result=s.getSessionFactory().openSession().;
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	
    	return result;
    }
}