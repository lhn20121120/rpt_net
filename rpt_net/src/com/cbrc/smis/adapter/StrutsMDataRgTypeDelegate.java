package com.cbrc.smis.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.form.MDataRgTypeForm;
import com.cbrc.smis.hibernate.MDataRgType;
import com.cbrc.smis.hibernate.MRepFreq;
import com.cbrc.smis.util.FitechException;

/**
 * @StrutsMDataRgTypeDelegate ���ݷ�Χ��Delegate
 * 
 * @author ����
 */
public class StrutsMDataRgTypeDelegate {
    //Catch��������׳��������쳣
    private static FitechException log = new FitechException(
            StrutsMDataRgTypeDelegate.class);
    
    /**
     * 
     * 
     */
    public static String getdatename(Integer dataid){
    	if(dataid==null)return null;
    	 String dataname=null; 
    	DBConn conn=null;
    	Session session=null;
    //	// System.out.println("dataid is "+dataid);
    	try{
    		
    		String hql="from MDataRgType mdrt where mdrt.dataRangeId="+dataid;
    		conn=new DBConn();
    		List list=conn.openSession().find(hql);
    		if(list!= null &&list.size()>0)
    		{ 
    			MDataRgTypeForm mDataRgTypeForm =new MDataRgTypeForm();
    			MDataRgType mDataRgTypePersistence=(MDataRgType)list.get(0);
    		    TranslatorUtil.copyPersistenceToVo(mDataRgTypePersistence,mDataRgTypeForm);
    		    dataname=mDataRgTypeForm.getDataRgDesc();
    		}
    	}catch(Exception e)
    		{
    			dataname=null;
    			log.printStackTrace(e);
    		}finally{
    			if(conn!=null)
    				conn.closeSession();
    		}
    	return dataname;
    }

    /**
     * ���ݷ�Χ����
     * 
     * @param mDataRgTypeForm
     *            MDataRgTypeForm
     * @return boolean result,�����ɹ�����true,���򷵻�false
     * @exception Exception����׽�쳣����
     */
    public static boolean create(MDataRgTypeForm mDataRgTypeForm)
            throws Exception {

        boolean result = false; //��result���
        MDataRgType mDataRgTypePersistence = new MDataRgType();

        if (mDataRgTypeForm == null
                || mDataRgTypeForm.getDataRgDesc().equals("")) {
            return result;
        }
        //���Ӷ���ĳ�ʼ��
        DBConn conn = null;
        //�Ự����ĳ�ʼ��
        Session session = null;

        try {
            //��ʾ�㵽�־ò��CopyTo����(McurUnitPresistence�־ò�����ʵ��,mDataRgTypeForm��ʾ�����)
            if (mDataRgTypeForm.getDataRgDesc() == null
                    || mDataRgTypeForm.getDataRgDesc().equals("")) {
                return result;
            }

            TranslatorUtil.copyVoToPersistence(mDataRgTypePersistence,mDataRgTypeForm);
            //ʵ�������Ӷ���
            conn = new DBConn();
            //�Ự����Ϊ���Ӷ������������
            session = conn.beginTransaction();
            //mDataRgTypeForm�����ʵ����
           
            //�Ự���󱣴�־ò����
            session.save(mDataRgTypePersistence);
            session.flush();
            TranslatorUtil.copyPersistenceToVo(mDataRgTypePersistence,mDataRgTypeForm);
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
     * @param mDataRgTypeForm
     *            ������ѯ��������Ϣ�����ݷ�ΧID�����ݷ�Χ��
     */

    public static int getRecordCount(MDataRgTypeForm mDataRgTypeForm) {
        int count = 0;

        //���Ӷ���ͻỰ�����ʼ��
        DBConn conn = null;
        Session session = null;

        //	 ��ѯ����HQL������
        StringBuffer hql = new StringBuffer(
                "select count(*) from MDataRgType mdrt");
        StringBuffer where = new StringBuffer("");

        if (mDataRgTypeForm != null) {
            // �����������ж�,�������Ʋ���Ϊ��
            if (mDataRgTypeForm.getDataRgDesc() != null
                    && !mDataRgTypeForm.getDataRgDesc().equals(""))
                where.append((where.toString().equals("") ? "" : " and ")
                        + "mdrt.dataRgDesc like '%"
                        + mDataRgTypeForm.getDataRgDesc() + "%'");
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
     * ���ݷ�Χ�Ĳ�ѯ
     * 
     * @param mDataRgTypeForm
     *            MDataRgTypeForm ��ѯ������
     * @return List ������ҵ���¼������List��¼�������򣬷���null
     */
    public static List select(MDataRgTypeForm mDataRgTypeForm, int offset,
            int limit) {

        //	 List���ϵĶ���
        List refVals = null;

        //���Ӷ���ͻỰ�����ʼ��
        DBConn conn = null;
        Session session = null;

        //	 ��ѯ����HQL������
        StringBuffer hql = new StringBuffer("from MDataRgType mdrt");
        StringBuffer where = new StringBuffer("");
      
        if (mDataRgTypeForm != null) {
            // �����������ж�,�������Ʋ���Ϊ��
            if (mDataRgTypeForm.getDataRgDesc() != null
                    && !mDataRgTypeForm.getDataRgDesc().equals(""))
                where.append((where.toString().equals("") ? "" : " and ")
                        + "mdrt.dataRgDesc like '%"
                        + mDataRgTypeForm.getDataRgDesc() + "%'");
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
                    MDataRgTypeForm mDataRgTypeFormTemp = new MDataRgTypeForm();
                    MDataRgType mDataRgTypePersistence = (MDataRgType) it
                            .next();
                    TranslatorUtil.copyPersistenceToVo(mDataRgTypePersistence,
                            mDataRgTypeFormTemp);
                    refVals.add(mDataRgTypeFormTemp);
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
     * ����MDataRgTypeForm����
     * 
     * @param mDataRgTypeForm
     *            MDataRgTypeForm ������ݵĶ���
     * @exception Exception
     *                ���MDataRgTypeForm����ʧ��,��׽�׳��쳣
     */
    public static boolean update(MDataRgTypeForm mDataRgTypeForm)
            throws Exception {
        boolean result = false;
        DBConn conn = null;
        Session session = null;

        MDataRgType mDataRgTypePersistence = new MDataRgType();

        if (mDataRgTypeForm == null) {
            return result;
        }
        try {
            if (mDataRgTypeForm.getDataRgDesc() == null
                    || mDataRgTypeForm.getDataRgDesc().equals("")) {
                return result;
            }
            conn = new DBConn();
            session = conn.beginTransaction();

            TranslatorUtil.copyVoToPersistence(mDataRgTypePersistence,
                    mDataRgTypeForm);
            session.update(mDataRgTypePersistence);

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
     * @param mDataRgTypeForm
     *            The MDataRgTypeForm �������ݵĴ���
     * @exception Exception
     *                ��� MDataRgTypeForm ����ʧ���׳��쳣.
     */
    public static boolean edit(MDataRgTypeForm mDataRgTypeForm)
            throws Exception {
        boolean result = false;
        DBConn conn = null;
        Session session = null;

        MDataRgType mDataRgTypePersistence = new MDataRgType();
        mDataRgTypeForm = new MDataRgTypeForm();
        mDataRgTypeForm.getDataRgDesc();

        if (mDataRgTypeForm == null) {
            return result;
        }

        try {
            if (mDataRgTypeForm.getDataRgDesc() == null
                    && mDataRgTypeForm.getDataRgDesc().equals("")) {
                return result;
            }
            conn = new DBConn();
            session = conn.beginTransaction();

            mDataRgTypePersistence = (MDataRgType) session.load(
                    MDataRgType.class, mDataRgTypeForm.getDataRgDesc());

            TranslatorUtil.copyVoToPersistence(mDataRgTypePersistence,
                    mDataRgTypeForm);

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
     * @param mDataRgTypeForm
     *            MDataRgTypeForm ��ѯ���Ķ���
     * @return boolean ���ɾ���ɹ��򷵻�true,����false
     */
    public static boolean remove(MDataRgTypeForm mDataRgTypeForm)
            throws Exception {
        //�ñ�־result
        boolean result = false;
        //���ӺͻỰ����ĳ�ʼ��
        DBConn conn = null;
        Session session = null;

        //mDataRgType�Ƿ�Ϊ��,����result
        if (mDataRgTypeForm == null && mDataRgTypeForm.getDataRangeId() == null)
            return result;

        try {
            //	���Ӷ���ͻỰ�����ʼ��
            conn = new DBConn();
            session = conn.beginTransaction();
            //��mDataRgTypeForm�Ļ��ҵ�λ�Ļ�����������HQL�в�ѯ
            MDataRgType mDataRgType = (MDataRgType) session.load(
                    MDataRgType.class, mDataRgTypeForm.getDataRangeId());
            //�Ự����ɾ���־ò����
            TranslatorUtil.copyPersistenceToVo(mDataRgType,mDataRgTypeForm);
            session.delete(mDataRgType);
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
     * @param mDataRgTypeForm
     *            MDataRgTypeFormʵ��������
     * @return MDataRgTypeForm ����һ����¼
     */

    public static MDataRgTypeForm selectOne(MDataRgTypeForm mDataRgTypeForm) {

        //���Ӷ���ͻỰ�����ʼ��
        DBConn conn = null;
        Session session = null;

        if (mDataRgTypeForm != null)
           
        try {
            if (mDataRgTypeForm.getDataRangeId() != null
                    && !mDataRgTypeForm.getDataRangeId().equals(""))
                //conn�����ʵ����
                conn = new DBConn();
            //�����ӿ�ʼ�Ự
            session = conn.openSession();

            MDataRgType mDataRgTypePersistence = (MDataRgType) session.load(
                    MDataRgType.class, mDataRgTypeForm.getDataRangeId());

            TranslatorUtil.copyPersistenceToVo(mDataRgTypePersistence,
                    mDataRgTypeForm);

        } catch (HibernateException he) {
            log.printStackTrace(he);
        } catch (Exception e) {
            log.printStackTrace(e);
        } finally {
            //������Ӵ��ڣ���Ͽ��������Ự������
            if (conn != null)
                conn.closeSession();
        }
        return mDataRgTypeForm;
    }

    /**
     * ��ѯ���м�¼
     * 
     * @return List
     * @exception Exception
     *                If the MDataRgTypeForm objects cannot be retrieved.
     */
    public static List findAll() throws Exception {

        //	 List���ϵĶ���
        List refVals = null;

        //���Ӷ���ͻỰ�����ʼ��
        DBConn conn = null;
        Session session = null;

        //	 ��ѯ����HQL������
        StringBuffer hql = new StringBuffer("from MDataRgType mrt order by mrt.dataRangeId");
        try {
            //conn�����ʵ����
            conn = new DBConn();
            //�����ӿ�ʼ�Ự
            session = conn.openSession();
            //��Ӽ�����Session
            //List list=session.find(hql.toString());
            Query query = session.createQuery(hql.toString());
            List list = query.list();
            if (list != null) {
                refVals = new ArrayList();
                //ѭ����ȡ���ݿ����������¼
                for (Iterator it = list.iterator(); it.hasNext();) {
                    MDataRgTypeForm mDataRgTypeFormTemp = new MDataRgTypeForm();
                    MDataRgType mDataRgTypePersistence = (MDataRgType) it
                            .next();
                    TranslatorUtil.copyPersistenceToVo(mDataRgTypePersistence,
                            mDataRgTypeFormTemp);
                    refVals.add(mDataRgTypeFormTemp);
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
     * @author  cb
     * 
     * ���������ID���ҵ���Ӧ�����ݷ�Χ����
     * 
     * @param dataRgId  ���ݷ�ΧID��
     * @return
     * @throws Exception
     */
    public static MDataRgType getMDataRgTypeOncb(Integer dataRgId)
            throws Exception {

        DBConn dBConn = null;

        Session session = null;

        MDataRgType mrt = null;

        try {

            dBConn = new DBConn();

            session = dBConn.openSession();

            mrt = (MDataRgType) session.get(MDataRgType.class, dataRgId);

        } catch (Exception e) {

        	mrt = null;
        	
         // e.printStackTrace();
        }
        finally{
            
            if(session!=null)
                
                dBConn.closeSession();
        }

        return  mrt;
    }
    /**
     * ��ʹ��hibernate ���Ը� 2011-12-21
     * Ӱ�����MRepFreq
     * �õ�����Ƶ��
     * @param repFreqId
     * @return
     * @throws Exception
     */
    public  MRepFreq getActuRepFlag(Integer repFreqId) throws Exception {

    	MRepFreq mRepFreq = null;
    	
        //���Ӷ���ͻỰ�����ʼ��
        DBConn conn = null;
        Session session = null;

        //	 ��ѯ����HQL������
        StringBuffer hql = new StringBuffer("from MRepFreq mrt where mrt.repFreqId="+ repFreqId );
        try {
            //conn�����ʵ����
            conn = new DBConn();
            //�����ӿ�ʼ�Ự
            session = conn.openSession();
            //��Ӽ�����Session
            //List list=session.find(hql.toString());
            Query query = session.createQuery(hql.toString());
            List list = query.list();
            if (list != null && list.size() > 0) {
            	mRepFreq = (MRepFreq)list.get(0);
            }
        } catch (HibernateException he) {
        	mRepFreq = null;
            log.printStackTrace(he);
        } catch (Exception e) {
        	mRepFreq = null;
            log.printStackTrace(e);
        } finally {
            //������Ӵ��ڣ���Ͽ��������Ự������
            if (conn != null)
                conn.closeSession();
        }
        return mRepFreq;
    }
    
    /***
     * ��ʹ��hibernate ���Ը� 2011-12-21
     * @param dataRangeId
     * @return
     * @throws Exception
     */
    public static MDataRgType selectOneByName(String dataRangeId) throws Exception {

    	MDataRgType mDataRgType = null;
    	
        //���Ӷ���ͻỰ�����ʼ��
        DBConn conn = null;
        Session session = null;

        //	 ��ѯ����HQL������
        StringBuffer hql = new StringBuffer("from MDataRgType mrt where mrt.dataRangeId="+ dataRangeId );
        try {
            //conn�����ʵ����
            conn = new DBConn();
            //�����ӿ�ʼ�Ự
            session = conn.openSession();
            //��Ӽ�����Session
            //List list=session.find(hql.toString());
            Query query = session.createQuery(hql.toString());
            List list = query.list();
            if (list != null && list.size() > 0) {
                mDataRgType = (MDataRgType)list.get(0);
            }
        } catch (HibernateException he) {
        	mDataRgType = null;
            log.printStackTrace(he);
        } catch (Exception e) {
        	mDataRgType = null;
            log.printStackTrace(e);
        } finally {
            //������Ӵ��ڣ���Ͽ��������Ự������
            if (conn != null)
                conn.closeSession();
        }
        return mDataRgType;
    }
    
    /**
     * ���ݱ��Ϳھ����Ʋ�ѯ���Ϳھ�����
     * 
     * @param dataRgDesc ���Ϳھ�����
     * @return MDataRgType ���Ϳھ�����
     * @throws Exception
     */
    public static MDataRgType selectDataRange(String dataRgDesc) throws Exception {
    	MDataRgType mDataRgType = null;
        DBConn conn = null;
        Session session = null;

        StringBuffer hql = new StringBuffer("from MDataRgType mrt where mrt.dataRgDesc='"+ dataRgDesc +"'");
        try {
            conn = new DBConn();
            session = conn.openSession();
            Query query = session.createQuery(hql.toString());
            List list = query.list();
            if (list != null && list.size() > 0) {
                mDataRgType = (MDataRgType)list.get(0);
            }
        } catch (HibernateException he) {
        	mDataRgType = null;
            log.printStackTrace(he);
        } catch (Exception e) {
        	mDataRgType = null;
            log.printStackTrace(e);
        } finally {
            //������Ӵ��ڣ���Ͽ��������Ự������
            if (conn != null)
                conn.closeSession();
        }
        return mDataRgType;
    }
}