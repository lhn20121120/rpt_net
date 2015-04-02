package com.cbrc.smis.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.form.MCurrForm;
import com.cbrc.smis.hibernate.MCurr;
import com.cbrc.smis.util.FitechException;

/**
 * @StrutsMCurrDelegate ���ֱ�Delegate����
 * 
 * @author ����
 */
public class StrutsMCurrDelegate {
    //Catch��������׳��������쳣
    private static FitechException log = new FitechException(
            StrutsMCurrDelegate.class);

    /**
     * У���������
     * 
     * @param mRepFreqForm
     *            MCurrForm
     * @return boolean result,�����ɹ�����true,���򷵻�false
     * @exception Exception����׽�쳣����
     */
    public static boolean create(MCurrForm mCurrForm) throws Exception {

        boolean result = false; //��result���
        MCurr mCurrPersistence = new MCurr();

        if (mCurrForm == null || mCurrForm.getCurName().equals("")) {
            return result;
        }
        //���Ӷ���ĳ�ʼ��
        DBConn conn = null;
        //�Ự����ĳ�ʼ��
        Session session = null;

        try {
            //��ʾ�㵽�־ò��CopyTo����(McurUnitPresistence�־ò�����ʵ��,mCurrForm��ʾ�����)
            if (mCurrForm.getCurName() == null
                    || mCurrForm.getCurName().equals("")) {
                return result;
            }

            TranslatorUtil.copyVoToPersistence(mCurrPersistence, mCurrForm);
            //ʵ�������Ӷ���
            conn = new DBConn();
            //�Ự����Ϊ���Ӷ������������
            session = conn.beginTransaction();
            //mCurrForm�����ʵ����
            mCurrForm = new MCurrForm();
            mCurrForm.getCurName();

            //�Ự���󱣴�־ò����
            session.save(mCurrPersistence);
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
     * ȡ�ð�������ѯ���ļ�¼����
     * 
     * @return int ��ѯ���ļ�¼����
     * @param mCurrForm
     *            ������ѯ��������Ϣ������ID���������ƣ�
     */

    public static int getRecordCount(MCurrForm mCurrForm) {
        int count = 0;

        //���Ӷ���ͻỰ�����ʼ��
        DBConn conn = null;
        Session session = null;

        //	 ��ѯ����HQL������
        StringBuffer hql = new StringBuffer("select count(*) from MCurr mc");
        StringBuffer where = new StringBuffer("");

        if (mCurrForm != null) {
            // �����������ж�,�������Ʋ���Ϊ��
            if (mCurrForm.getCurName() != null
                    && !mCurrForm.getCurName().equals(""))
                where.append((where.toString().equals("") ? "" : " and ")
                        + "mc.curName like '%" + mCurrForm.getCurName() + "%'");
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
     * ���ֵĲ�ѯ
     * 
     * @param mCurrForm
     *            MCurrForm ��ѯ������
     * @return List ������ҵ���¼������List��¼�������򣬷���null
     */
    public static List select(MCurrForm mCurrForm, int offset, int limit) {

        //		 List���ϵĶ���
        List refVals = null;

        //���Ӷ���ͻỰ�����ʼ��
        DBConn conn = null;
        Session session = null;

        //	 ��ѯ����HQL������
        StringBuffer hql = new StringBuffer("from MCurr mc");
        StringBuffer where = new StringBuffer("");
        if (mCurrForm != null) {
            // �����������ж�,�������Ʋ���Ϊ��
            if (mCurrForm.getCurName() != null
                    && !mCurrForm.getCurName().equals(""))
                where.append((where.toString().equals("") ? "" : " and ")
                        + "mc.curName like '%" + mCurrForm.getCurName() + "%'");
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
                    MCurrForm mCurrFormTemp = new MCurrForm();
                    MCurr mCurrPersistence = (MCurr) it.next();
                    TranslatorUtil.copyPersistenceToVo(mCurrPersistence,
                            mCurrFormTemp);
                    refVals.add(mCurrFormTemp);
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
     * ���±���
     * 
     * @param mCurrForm
     *            MCurrForm ������ݵĶ���
     * @exception Exception
     *                ���MCurrForm����ʧ��,��׽�׳��쳣
     */
    public static boolean update(MCurrForm mCurrForm) throws Exception {
        boolean result = false;
        DBConn conn = null;
        Session session = null;

        MCurr mCurrPersistence = new MCurr();

        if (mCurrForm == null) {
            return result;
        }
        try {
            if (mCurrForm.getCurName() == null
                    || mCurrForm.getCurName().equals("")) {
                return result;
            }
            conn = new DBConn();
            session = conn.beginTransaction();

            TranslatorUtil.copyVoToPersistence(mCurrPersistence, mCurrForm);
            session.update(mCurrPersistence);

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
     * �༭����
     * 
     * @param mCurrForm
     *            The MCurrForm �������ݵĴ���
     * @exception Exception
     *                ��� MCurrForm ����ʧ���׳��쳣.
     */
    public static boolean edit(MCurrForm mCurrForm) throws Exception {
        boolean result = false;
        DBConn conn = null;
        Session session = null;

        MCurr mCurrPersistence = new MCurr();
        mCurrForm = new MCurrForm();
        mCurrForm.getCurName();

        if (mCurrForm == null) {
            return result;
        }

        try {
            if (mCurrForm.getCurName() == null
                    && mCurrForm.getCurName().equals("")) {
                return result;
            }
            conn = new DBConn();
            session = conn.beginTransaction();

            mCurrPersistence = (MCurr) session.load(MCurr.class, mCurrForm
                    .getCurName());

            TranslatorUtil.copyVoToPersistence(mCurrPersistence, mCurrForm);

        } catch (HibernateException he) {
            log.printStackTrace(he);
        } finally {
            if (conn != null)
                conn.endTransaction(result);
        }
        return result;
    }

    /**
     * ɾ������
     * 
     * @param mCurrForm
     *            MCurrForm ��ѯ���Ķ���
     * @return boolean ���ɾ���ɹ��򷵻�true,����false
     */
    public static boolean remove(MCurrForm mCurrForm) throws Exception {
        //�ñ�־result
        boolean result = false;
        //���ӺͻỰ����ĳ�ʼ��
        DBConn conn = null;
        Session session = null;

        //mCurr�Ƿ�Ϊ��,����result
        if (mCurrForm == null && mCurrForm.getCurId() == null)
            return result;

        try {
            //	���Ӷ���ͻỰ�����ʼ��
            conn = new DBConn();
            session = conn.beginTransaction();
            //��mCurrForm�Ļ��ҵ�λ�Ļ�����������HQL�в�ѯ
            MCurr mCurr = (MCurr) session.load(MCurr.class, mCurrForm
                    .getCurId());
            //�Ự����ɾ���־ò����
            session.delete(mCurr);
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
     * ��ѯһ����¼,���ص�EditAction��
     * 
     * @param mCurrForm
     *            MCurrFormʵ��������
     * @return MCurrForm ����һ����¼
     */

    public static MCurrForm selectOne(MCurrForm mCurrForm) {

        //���Ӷ���ͻỰ�����ʼ��
        DBConn conn = null;
        Session session = null;

        if (mCurrForm != null)
         
        try {
            if (mCurrForm.getCurId() != null
                    && !mCurrForm.getCurId().equals(""))
                //conn�����ʵ����
                conn = new DBConn();
            //�����ӿ�ʼ�Ự
            session = conn.openSession();

            MCurr mCurrPersistence = (MCurr) session.load(MCurr.class,
                    mCurrForm.getCurId());

            TranslatorUtil.copyPersistenceToVo(mCurrPersistence, mCurrForm);

        } catch (HibernateException he) {
            log.printStackTrace(he);
        } catch (Exception e) {
            log.printStackTrace(e);
        } finally {
            //������Ӵ��ڣ���Ͽ��������Ự������
            if (conn != null)
                conn.closeSession();
        }
        return mCurrForm;
    }

    /**
     * Retrieve all existing MCurrForm objects.
     * 
     * @exception Exception
     *                If the MCurrForm objects cannot be retrieved.
     */
    public static List findAll() throws Exception {
        List retVals = new ArrayList();
        DBConn conn=new DBConn();
        Session session = null;
        session=conn.openSession();
        net.sf.hibernate.Transaction tx = session.beginTransaction();
        retVals.addAll(session.find("from com.cbrc.smis.hibernate.MCurr m order by m.curId"));
        tx.commit();
        session.close();
        ArrayList mCurr_vos = new ArrayList();
        for (Iterator it = retVals.iterator(); it.hasNext();) {
            MCurrForm mCurrFormTemp = new MCurrForm();
            com.cbrc.smis.hibernate.MCurr mCurrPersistence = (com.cbrc.smis.hibernate.MCurr) it
                    .next();
            TranslatorUtil.copyPersistenceToVo(mCurrPersistence, mCurrFormTemp);
            mCurr_vos.add(mCurrFormTemp);
        }
        retVals = mCurr_vos;
        return retVals;
    }
    
    /***
     * ��ʹ��hibernate ���Ը� 2011-12-21
     * @param curName
     * @return
     * @throws Exception
     */
    public static MCurr getMCurr(String curName) throws Exception {

        MCurr mCurr = null;

        DBConn dBConn = null;

        Session session = null;
        
        Query  query = null;
        
        List l = null;
        
        String hsql = "from MCurr mc where mc.curName=:curName";

        try {

            dBConn = new DBConn();

            session = dBConn.openSession();

            query = session.createQuery(hsql);
            
            query.setString("curName",curName);
            
            l = query.list();
            
            int  size = l.size();
            
            if(size!=0)
                
                mCurr = (MCurr)l.get(0);

        } catch (HibernateException e) {

            log.printStackTrace(e);

        }
        finally{
            
            if(session!=null)
                
                dBConn.closeSession();
        }

        return   mCurr;
    }
    /***
     * ��ʹ��hibernate ���Ը� 2011-12-21
     * Ӱ�����MCurr
     * @param curId
     * @return
     * @throws Exception
     */
    public static MCurr getISMCurr(String curId) throws Exception {

        MCurr mCurr = null;
        DBConn dBConn = null;
        try {
            dBConn = new DBConn();
            String hsql = "from MCurr mc where mc.curId="+curId;
            List list = dBConn.openSession().find(hsql);
            if(list!=null && list.size()!=0)
                
                mCurr = (MCurr)list.get(0);

        } catch (HibernateException e) {

            log.printStackTrace(e);

        }finally {
                //������Ӵ��ڣ���Ͽ��������Ự������
                if (dBConn != null)
                	dBConn.closeSession();
        }

        return   mCurr;
    }
}