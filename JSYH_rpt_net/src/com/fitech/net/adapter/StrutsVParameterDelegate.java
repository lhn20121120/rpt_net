package com.fitech.net.adapter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.dao.FitechConnection;
import com.cbrc.smis.util.FitechException;
import com.fitech.net.form.VParameterForm;
import com.fitech.net.hibernate.VParameter;

/**
 * @StrutsVParamDelegate �������Delegate����
 * 
 * @author James
 */
public class StrutsVParameterDelegate {
    //Catch��������׳��������쳣
    private static FitechException log = new FitechException(StrutsVParameterDelegate.class);

    /**
     * У���������
     * 
     * @param mRepFreqForm VParameterForm
     * @return boolean result,�����ɹ�����true,���򷵻�false
     * @exception Exception����׽�쳣����
     */
    public static boolean create(VParameterForm vParamForm) throws Exception {

        boolean result = false; //��result���
        VParameter vParamPersistence = new VParameter();

        if (vParamForm == null) {
            return result;
        }
        DBConn conn = null;        
        Session session = null;

        try {
            //��ʾ�㵽�־ò��CopyTo����(McurUnitPresistence�־ò�����ʵ��,vParamForm��ʾ�����)            
            TranslatorUtil.copyVoToPersistence(vParamPersistence, vParamForm);
            
            //ʵ�������Ӷ���
            conn = new DBConn();
            session = conn.beginTransaction();
           
            session.save(vParamPersistence);            
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

    /**
     * ȡ�ð�������ѯ���ļ�¼����
     * 
     * @return int ��ѯ���ļ�¼����
     * @param vParamForm
     *            ������ѯ��������Ϣ������ID���������ƣ�
     */

    public static int getRecordCount(VParameterForm vParamForm) {
        int count = 0;

        //���Ӷ���ͻỰ�����ʼ��
        DBConn conn = null;
        Session session = null;

        //	 ��ѯ����HQL������
        StringBuffer hql = new StringBuffer("select count(*) from VParameter vparam");
        StringBuffer where = new StringBuffer("");

        if (vParamForm != null) {
            // �����������ж�,�������Ʋ���Ϊ��
            if (vParamForm.getVpNote() != null
                    && !vParamForm.getVpNote().equals(""))
                where.append((where.toString().equals("") ? "" : " and ")
                        + "vparam.vpNote like '%" + vParamForm.getVpNote() + "%'");
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
     * @param vParamForm
     *            VParameterForm ��ѯ������
     * @return List ������ҵ���¼������List��¼�������򣬷���null
     */
    public static List select(VParameterForm vParamForm, int offset, int limit) {

        //	List���ϵĶ���
        List refVals = null;

        //���Ӷ���ͻỰ�����ʼ��
        DBConn conn = null;
        Session session = null;

        //	��ѯ����HQL������
        StringBuffer hql = new StringBuffer("from VParameter vparam");
        StringBuffer where = new StringBuffer("");
        if (vParamForm != null) {
            // �����������ж�,�������Ʋ���Ϊ��
            if (vParamForm.getVpNote() != null
                    && !vParamForm.getVpNote().equals(""))
                where.append((where.toString().equals("") ? "" : " and ")
                        + "vparam.vpNote like '%" + vParamForm.getVpNote() + "%'");
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
                    VParameterForm vParamFormTemp = new VParameterForm();
                    VParameter vParamPersistence = (VParameter)it.next();
                    TranslatorUtil.copyPersistenceToVo(vParamPersistence,
                    		vParamFormTemp);
                    refVals.add(vParamFormTemp);
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
     * @param vParamForm
     *            VParameterForm ������ݵĶ���
     * @exception Exception
     *                ���VParameterForm����ʧ��,��׽�׳��쳣
     */
    public static boolean update(VParameterForm vParamForm) throws Exception {
        boolean result = false;
        DBConn conn = null;
        Session session = null;

        VParameter vParamPersistence = new VParameter();

        if (vParamForm == null) {
            return result;
        }
        try {
            if (vParamForm.getVpNote() == null
                    || vParamForm.getVpNote().equals("")) {
                return result;
            }
            conn = new DBConn();
            session = conn.beginTransaction();

            TranslatorUtil.copyVoToPersistence(vParamPersistence, vParamForm);
            session.update(vParamPersistence);

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
     * @param vParamForm
     *            The VParameterForm �������ݵĴ���
     * @exception Exception
     *                ��� VParameterForm ����ʧ���׳��쳣.
     */
    public static boolean edit(VParameterForm vParamForm) throws Exception {
        boolean result = false;
        DBConn conn = null;
        Session session = null;

        VParameter mParamPersistence = new VParameter();
        vParamForm = new VParameterForm();
        vParamForm.getVpNote();

        if (vParamForm == null) {
            return result;
        }

        try {
            if (vParamForm.getVpNote() == null
                    && vParamForm.getVpNote().equals("")) {
                return result;
            }
            conn = new DBConn();
            session = conn.beginTransaction();

            mParamPersistence = (VParameter) session.load(VParameter.class, vParamForm.getVpNote());

            TranslatorUtil.copyVoToPersistence(mParamPersistence, vParamForm);

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
     * @param vParamForm
     *            VParameterForm ��ѯ���Ķ���
     * @return boolean ���ɾ���ɹ��򷵻�true,����false
     */
    public static boolean remove(VParameterForm vParamForm) throws Exception {
        //�ñ�־result
        boolean result = false;
        //���ӺͻỰ����ĳ�ʼ��
        DBConn conn = null;
        Session session = null;

        //mCurr�Ƿ�Ϊ��,����result
        if (vParamForm == null && vParamForm.getVpId() == null)
            return result;

        try {
            //	���Ӷ���ͻỰ�����ʼ��
            conn = new DBConn();
            session = conn.beginTransaction();
            //��vParamForm�Ļ��ҵ�λ�Ļ�����������HQL�в�ѯ
            VParameter mCurr = (VParameter) session.load(VParameter.class, vParamForm
                    .getVpId());
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
     * @param vParamForm
     *            VParameterFormʵ��������
     * @return VParameterForm ����һ����¼
     */

    public static VParameterForm selectOne(VParameterForm vParamForm) {

        //���Ӷ���ͻỰ�����ʼ��
        DBConn conn = null;
        Session session = null;

        if (vParamForm != null)
         
        try {
            if (vParamForm.getVpId() != null
                    && !vParamForm.getVpId().equals(""))
                //conn�����ʵ����
                conn = new DBConn();
            //�����ӿ�ʼ�Ự
            session = conn.openSession();

            VParameter mCurrPersistence = (VParameter) session.load(VParameter.class,
                    vParamForm.getVpId());

            TranslatorUtil.copyPersistenceToVo(mCurrPersistence, vParamForm);

        } catch (HibernateException he) {
            log.printStackTrace(he);
        } catch (Exception e) {
            log.printStackTrace(e);
        } finally {
            //������Ӵ��ڣ���Ͽ��������Ự������
            if (conn != null)
                conn.closeSession();
        }
        return vParamForm;
    }
    
    /**
     * ����ĳ�ű��ֶ��Ƿ��Ѿ�������������ʵ���ά�ȱ�
     * @param vParamForm
     * @return
     */
    public static boolean isExist(VParameterForm vParamForm) {

    	boolean bool = false;
        //���Ӷ���ͻỰ�����ʼ��
        DBConn conn = null;
        Session session = null;

        if (vParamForm == null) return bool;         
        try {
        	
        	String hql = "from VParameter vparam where vparam.vpTableid='"+ vParamForm.getVpTableid() 
        				+"' and vparam.vpColumnid='"+ vParamForm.getVpColumnid() +"' and vparam.vttId='"+ vParamForm.getVttId()+"'";
        	
        	conn = new DBConn();
            session = conn.openSession();

            List list = session.find(hql.toString());
            if (list != null && list.size() > 0) {
                bool = true;
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
     * Retrieve all existing VParameterForm objects.
     * 
     * @exception Exception
     *                If the VParameterForm objects cannot be retrieved.
     */
    public static List findAll() throws Exception {
        List retVals = new ArrayList();
        DBConn conn=new DBConn();
        Session session = null;
        session=conn.openSession();
        net.sf.hibernate.Transaction tx = session.beginTransaction();
        retVals.addAll(session.find("from VParameter vparam"));
        tx.commit();
        session.close();
        ArrayList mParam = new ArrayList();
        for (Iterator it = retVals.iterator(); it.hasNext();) {
            VParameterForm vParamFormTemp = new VParameterForm();
            VParameter vParamPersistence = (VParameter) it.next();
            TranslatorUtil.copyPersistenceToVo(vParamPersistence, vParamFormTemp);
            mParam.add(vParamFormTemp);
        }
        retVals = mParam;
        return retVals;
    }
  
    public static VParameter getVParameter(String curName) throws Exception {

        VParameter mCurr = null;

        DBConn dBConn = null;

        Session session = null;
        
        Query  query = null;
        
        List l = null;
        
        String hsql = "from VParameter mc where mc.curName=:curName";

        try {

            dBConn = new DBConn();

            session = dBConn.openSession();

            query = session.createQuery(hsql);
            
            query.setString("curName",curName);
            
            l = query.list();
            
            int  size = l.size();
            
            if(size!=0)
                
                mCurr = (VParameter)l.get(0);

        } catch (HibernateException e) {

            log.printStackTrace(e);

        }
        finally{
            
            if(session!=null)
                
                dBConn.closeSession();
        }

        return   mCurr;
    }
    
    /**
	 * �������ĵı������ֶ���,ȡ�����Ӧ�����ݿ�������ֶ���(Ӣ��)
	 * 
	 * @param tableName_CN String ���ı���
	 * @param colName_CN String �����ֶ���
	 * @return String[3] {Ӣ�ı���,Ӣ���ֶ���,��������}
	 */
	public static VParameterForm getENNameByCNName(String tableName_CN, String colName_CN) throws Exception
	{
		VParameterForm result = null;

		Connection conn = (new FitechConnection()).getConnect();
		if (conn == null)
		{
			return null;
		}
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try
		{
			if (tableName_CN != null && !tableName_CN.equals("") && colName_CN != null
				&& !colName_CN.equals(""))
			{
			
				String sql = "select * from V_PARAMETER where VP_TABLEDESC=? and VP_COLUMNDESC=?";

				stmt = conn.prepareStatement(sql);
				stmt.setString(1,tableName_CN.trim());
				stmt.setString(2,colName_CN.trim());
				rs = stmt.executeQuery();
				if(rs.next())
				{
					result = new VParameterForm();
					result.setVpId(new Integer(rs.getInt("VP_ID")));
					result.setVpTabledesc(rs.getString("VP_TABLEDESC"));;
					result.setVpColumndesc(rs.getString("VP_COLUMNDESC"));
					result.setVpTableid(rs.getString("VP_TABLEID"));
					result.setVpColumnid(rs.getString("VP_COLUMNID"));
					result.setVpColtype(new Integer(rs.getInt("VP_COLTYPE")));
				}

			}
		}
		catch (Exception e)
		{
			result = null;
			e.printStackTrace();
		}
		finally
		{
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
		}
		return result;
		/*try
		{
			if (tableName_CN != null && !tableName_CN.equals("") && colName_CN != null
					&& !colName_CN.equals(""))
			{
				conn = new DBConn();
				session = conn.openSession();
				StringBuffer hql = new StringBuffer("from VParameter vp where vp.vpTabledesc=? and vp.vpColumndesc=?");

				Query query = session.createQuery(hql.toString());
				query.setString(0, tableName_CN);
				query.setString(1, colName_CN);
				List list = query.list();

				if (list != null && list.size() != 0)
				{
					result = new VParameterForm();
					VParameter vParamPersistence = (VParameter)list.get(0);
					TranslatorUtil.copyPersistenceToVo(vParamPersistence, result);
				}
			}

		}
		catch (Exception e)
		{
			log.printStackTrace(e);
		}
		finally
		{
			//������Ӵ��ڣ���Ͽ��������Ự������
			if (conn != null)
				conn.closeSession();
		}*/
		//return result;
	}
}