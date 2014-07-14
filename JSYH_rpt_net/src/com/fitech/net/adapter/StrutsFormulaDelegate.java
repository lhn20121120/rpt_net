package com.fitech.net.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.util.FitechException;
import com.fitech.net.form.EtlIndexForm;
import com.fitech.net.hibernate.EtlIndex;

/**
 * �Թ�ʽ���в���
 * @author wh
 *
 */
public class StrutsFormulaDelegate {
//	Catch��������׳��������쳣
    private static FitechException log = new FitechException(StrutsVParameterDelegate.class);
	/**
	 * ��ѯ����ָ�깫ʽ����Ϣ,,
	 * @return  List
	 */
	public static List selectTargetFormula(){
		 //	List���ϵĶ���
        List resList = null;

        //���Ӷ���ͻỰ�����ʼ��
        DBConn conn = null;
        Session session = null;
        try {
            //conn�����ʵ����
            conn = new DBConn();
            //�����ӿ�ʼ�Ự
            session = conn.openSession();
            //��Ӽ�����Session
            //List list=session.find(hql.toString());
           String sql="from EtlIndex etl";

            List list = conn.openSession().find(sql);

            if (list != null) {
            	resList = new ArrayList();
                //ѭ����ȡ���ݿ����������¼
                for (Iterator it = list.iterator(); it.hasNext();) {
                	EtlIndexForm etlIndexForm = new EtlIndexForm();
                	EtlIndex etlIndexPersistence = (EtlIndex)it.next();
                   TranslatorUtil.copyPersistenceToVo(etlIndexPersistence,
                    		etlIndexForm);
                    resList.add(etlIndexForm);
                }
            }
        } catch (HibernateException he) {
        	resList = null;
            log.printStackTrace(he);
        } catch (Exception e) {
        	resList = null;
            log.printStackTrace(e);
        } finally {
            //������Ӵ��ڣ���Ͽ��������Ự������
            if (conn != null)
                conn.closeSession();
        }
        return resList;
	}
	 /**
     * ȡ�ð�������ѯ���ļ�¼����
     * 
     * @return int ��ѯ���ļ�¼����
     * @param EtlIndexForm
     *            ������ѯ��������Ϣ��ָ�����ƣ�
     */

    public static int getEtlIndexRecordCount(EtlIndexForm etlIndexForm) {
        int count = 0;

        //���Ӷ���ͻỰ�����ʼ��
        DBConn conn = null;
        Session session = null;

        //	 ��ѯ����HQL������
        StringBuffer hql = new StringBuffer("select count(*) from EtlIndex etlIndex");
        StringBuffer where = new StringBuffer("");

        if (etlIndexForm != null) {
            // �����������ж�,�������Ʋ���Ϊ��
            if (etlIndexForm.getIndexName() != null
                    && !etlIndexForm.getIndexName().equals(""))
                where.append((where.toString().equals("") ? "" : " and ")
                        + "etlIndex.indexName like '%" + etlIndexForm.getIndexName() + "%'");
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
     * ȡ����ʽָ��Ĳ�ѯ
     * 
     * @param  etlIndexForm
     *            etlIndexForm ��ѯ������
     * @return List ������ҵ���¼������List��¼�������򣬷���null
     */
    public static List selectEtlIndex(EtlIndexForm etlIndexForm, int offset, int limit) {

        //	List���ϵĶ���
        List refVals = null;

        //���Ӷ���ͻỰ�����ʼ��
        DBConn conn = null;
        Session session = null;

        //	��ѯ����HQL������
        StringBuffer hql = new StringBuffer("from EtlIndex etlIndex");
        StringBuffer where = new StringBuffer("");
        if (etlIndexForm != null) {
            // �����������ж�,�������Ʋ���Ϊ��
            if (etlIndexForm.getIndexName() != null
                    && !etlIndexForm.getIndexName().equals(""))
                where.append((where.toString().equals("") ? "" : " and ")
                        + "etlIndex.indexName like '%" + etlIndexForm.getIndexName() + "%'");
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
                	EtlIndexForm etlForm = new EtlIndexForm();
                	EtlIndex etlIndexPersistence = (EtlIndex)it.next();
                    TranslatorUtil.copyPersistenceToVo(etlIndexPersistence,
                    		etlForm);
                    refVals.add(etlForm);
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
     * ɾ������
     * 
     * @param EtlIndexForm
     *            EtlIndexForm ��ѯ���Ķ���
     * @return boolean ���ɾ���ɹ��򷵻�true,����false
     */
    public static boolean removeEtlIndex(EtlIndexForm etlIndexForm) throws Exception {
        //�ñ�־result
        boolean result = false;
        //���ӺͻỰ����ĳ�ʼ��
        DBConn conn = null;
        Session session = null;

        //mCurr�Ƿ�Ϊ��,����result
        if (etlIndexForm == null && etlIndexForm.getIndexName() == null)
            return result;

        try {
            //	���Ӷ���ͻỰ�����ʼ��
            conn = new DBConn();
            session = conn.beginTransaction();
            //��vParamForm�Ļ��ҵ�λ�Ļ�����������HQL�в�ѯ
            EtlIndex etlIndex = (EtlIndex) session.load(EtlIndex.class, etlIndexForm.getIndexName());
            //�Ự����ɾ���־ò����
            session.delete(etlIndex);
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
     * @param etlIndexForm
     *            etlIndexFormʵ��������
     * @return etlIndexForm ����һ����¼
     */

    public static EtlIndexForm selectOneEtlIndex(EtlIndexForm etlIndexForm) {

        //���Ӷ���ͻỰ�����ʼ��
        DBConn conn = null;
        Session session = null;

        if (etlIndexForm != null)
         
        try {
            if (etlIndexForm.getIndexName() != null
                    && !etlIndexForm.getIndexName().equals(""))
                //conn�����ʵ����
                conn = new DBConn();
            //�����ӿ�ʼ�Ự
            session = conn.openSession();

            EtlIndex mCurrPersistence = (EtlIndex) session.load(EtlIndex.class,
            		etlIndexForm.getIndexName());

            TranslatorUtil.copyPersistenceToVo(mCurrPersistence, etlIndexForm);

        } catch (HibernateException he) {
            log.printStackTrace(he);
        } catch (Exception e) {
            log.printStackTrace(e);
        } finally {
            //������Ӵ��ڣ���Ͽ��������Ự������
            if (conn != null)
                conn.closeSession();
        }
        return etlIndexForm;
    }
    /**
     * ����ָ�깫ʽ
     * 
     * @param EtlIndexForm
     *            EtlIndexForm ������ݵĶ���
     * @exception Exception
     *                ���VParameterForm����ʧ��,��׽�׳��쳣
     */
    public static boolean updateEtlIndex(EtlIndexForm etlIndexForm) throws Exception {
        boolean result = false;
        DBConn conn = null;
        Session session = null;

        EtlIndex etlIndexPersistence = new EtlIndex();

        if (etlIndexForm == null) {
            return result;
        }
        try {
            if (etlIndexForm.getIndexName() == null
                    || etlIndexForm.getIndexName().equals("")) {
                return result;
            }
            conn = new DBConn();
            session = conn.beginTransaction();

            TranslatorUtil.copyVoToPersistence(etlIndexPersistence, etlIndexForm);
            
            session.update(etlIndexPersistence);

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
    * ���Ҹ�ָ�����Ƿ��Ѿ�����
    * @param etlIndexForm
    * @return
    */
   public static boolean isExist(EtlIndexForm etlIndexForm) {

   	boolean bool = false;
       //���Ӷ���ͻỰ�����ʼ��
       DBConn conn = null;
       Session session = null;

       if (etlIndexForm == null) return bool;         
       try {
       	
       	String hql = "select count(*) from EtlIndex etl where etl.indexName='"+ etlIndexForm.getIndexName()+"'";
       	
       	conn = new DBConn();
           session = conn.openSession();

           List list = session.find(hql.toString());
           if (list != null && list.size() > 0) {
        	   int size=((Integer)list.get(0)).intValue();
        	   if(size>0){
        		   bool = true;
        	   }
               
           }


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
    *  ����ָ��
    * 
    * @param mRepFreqForm etlIndexForm
    * @return boolean result,�����ɹ�����true,���򷵻�false
    * @exception Exception����׽�쳣����
    */
   public static boolean createEtlIndex(EtlIndexForm etlIndexForm) throws Exception {

       boolean result = false; //��result���
       EtlIndex etlIndexPersistence = new EtlIndex();

       if (etlIndexForm == null) {
           return result;
       }
       DBConn conn = null;        
       Session session = null;

       try {
           //��ʾ�㵽�־ò��CopyTo����(McurUnitPresistence�־ò�����ʵ��,vParamForm��ʾ�����)            
           TranslatorUtil.copyVoToPersistence(etlIndexPersistence, etlIndexForm);
           //ʵ�������Ӷ���
           conn = new DBConn();
           session = conn.beginTransaction();
          
           session.save(etlIndexPersistence);            
           session.flush();
           
           result = true;
       } catch (HibernateException e) {
       	result = false;
           log.printStackTrace(e);
       } finally {
           //�������״̬��,��Ͽ�,��������,����
           if (conn != null)
               conn.endTransaction(result);
       }
       return result;
   }

}
