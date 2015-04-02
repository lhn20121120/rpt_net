package com.cbrc.smis.adapter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import com.cbrc.org.adapter.StrutsMOrgDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.common.ConfigOncb;
import com.cbrc.smis.common.DateUtil;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.form.ReportAgainSetForm;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.hibernate.MActuRep;
import com.cbrc.smis.hibernate.MChildReport;
import com.cbrc.smis.hibernate.MCurr;
import com.cbrc.smis.hibernate.MDataRgType;
import com.cbrc.smis.hibernate.MRepRange;
import com.cbrc.smis.hibernate.ReportAgainSet;
import com.cbrc.smis.hibernate.ReportIn;
import com.cbrc.smis.other.Aditing;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechLog;
import com.fitech.gznx.common.StringUtil;
import com.fitech.net.adapter.StrutsOrgNetDelegate;
import com.fitech.net.hibernate.OrgNet;
/**
 * This is a delegate class to handle interaction with the backend persistence
 * layer of hibernate. It has a set of methods to handle persistence for
 * ReportIn data (i.e. com.cbrc.smis.form.ReportInForm objects).
 * 
 * @author����
 */
public class StrutsReportInAgainDelegate {
    private static FitechException log = new FitechException(
            StrutsReportInDelegate.class);
   
    /**
     * ��ȡʵ�����ݱ�����Ϣ
     * 
     * @author rds 
     * @serialData 2005-12-18
     * 
     * @param repInId Integer ʵ�����ݱ���ID
     * @return ReportInForm
     */
    public static com.cbrc.smis.form.ReportInForm getReportIn(Integer repInId){
    	ReportInForm reportInForm=null;
    	
    	if(repInId==null) return null;
    	
    	DBConn conn=null;
    	
    	try{
    		conn=new DBConn();
    		ReportIn reportIn=(ReportIn)conn.openSession().get(ReportIn.class,repInId);
    		if(reportIn!=null){
    			reportInForm=new ReportInForm();
    			TranslatorUtil.copyPersistenceToVo(reportIn,reportInForm);
    		}
    	}catch(HibernateException he){
    		log.printStackTrace(he);
    		reportInForm=null;
    	}catch(Exception e){
    		log.printStackTrace(e);
    		reportInForm=null;
    	}finally{
    		if(conn!=null) conn.closeSession();
    	}
    	
    	return reportInForm;
    }
    
    /**
     * Create a new com.cbrc.smis.form.ReportInForm object and persist (i.e.
     * insert) it.
     * 
     * @param reportInForm
     *            The object containing the data for the new
     *            com.cbrc.smis.form.ReportInForm object
     * @exception Exception
     *                If the new com.cbrc.smis.form.ReportInForm object cannot
     *                be created or persisted.
     */
    public static com.cbrc.smis.form.ReportInForm create(
            com.cbrc.smis.form.ReportInForm reportInForm) throws Exception {
        com.cbrc.smis.hibernate.ReportIn reportInPersistence = new com.cbrc.smis.hibernate.ReportIn();
        TranslatorUtil.copyVoToPersistence(reportInPersistence, reportInForm);
        javax.naming.InitialContext ctx = new javax.naming.InitialContext();
        // TODO: Make adapter get SessionFactory jndi name by ant task argument?
        net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory) ctx
                .lookup("java:AirlineHibernateFactory");
        net.sf.hibernate.Session session = factory.openSession();
        net.sf.hibernate.Transaction tx = session.beginTransaction();
        // TODO error?: reportInPersistence =
        // (com.cbrc.smis.hibernate.ReportIn)session.save(reportInPersistence);
        session.save(reportInPersistence);
        tx.commit();
        session.close();
        TranslatorUtil.copyPersistenceToVo(reportInPersistence, reportInForm);
        return reportInForm;
    }

    /**
     * ����ǰ̨ҳ��Ĳ�ѯ������û�ѡ�и��ĵļ�¼д��˱�־Ϊ1
     * @author ����
     * @param orgArray �������Ļ���id��string���������
     * @param checkSign ���������û�ѡ�����˱�־
     * @param reportInPersistence �־û�����ReportIn
     * @param he HibernateException ���쳣��׽�׳�
     * @param e Exception  ���쳣��׽�׳�              
     * @param result boolean�ͱ���,���³ɹ�����true,���򷵻�false  
     */
    public static boolean update(com.cbrc.smis.form.ReportInForm reportInForm) throws Exception {
    	//���±�־
        boolean result=true;
        //��������
        DBConn conn=null;
        Session session=null;
        //reportform���Ƿ��в���
        if (reportInForm==null){
        	return false;
        }
        
        try{
        	conn=new DBConn();
        	session=conn.beginTransaction();

        	//ѭ�����־û����󴫵�form����Ļ���id����Ĳ���
        	if(reportInForm.getRepInIdArray()!=null && !reportInForm.getRepInIdArray().equals("")){
        		Integer repOutIds[]=new Integer[reportInForm.getRepInIdArray().length];
        		for(int i=0;i<reportInForm.getRepInIdArray().length;i++){
        			ReportIn reportInPersistence=(ReportIn)session.load(ReportIn.class, reportInForm.getRepInIdArray()[i]);
        			//reportInPersistence.setCheckFlag(reportInForm.getCheckSignOption()[i]);
        			reportInPersistence.setCheckFlag(reportInForm.getCheckFlag());
        			
        			if(reportInForm.getForseReportAgainFlag()!=null) 
        			{
        				
        				reportInPersistence.setForseReportAgainFlag(reportInForm.getForseReportAgainFlag());
        			}
        			session.update(reportInPersistence);
        			session.flush();
        			repOutIds[i]=reportInPersistence.getRepOutId();
        		}
        		reportInForm.setRepOutIds(repOutIds);
        	}else{
        		result=false;
        	}
        	
        	//�쳣�Ĳ�׽����
        }catch(HibernateException he){
        	result=false;
        	log.printStackTrace(he);
        }catch(Exception e){
        	result=false;
        	log.printStackTrace(e);
        }
        finally{
        	//���conn������Ȼ����,�������ǰ���񲢶Ͽ�����
        	if (conn!=null)	conn.endTransaction(result);
        }
        //���³ɹ����ذ�!!!
        return result;
    }

    /**
     * Retrieve an existing com.cbrc.smis.form.ReportInForm object for editing.
     * 
     * @param reportInForm
     *            The com.cbrc.smis.form.ReportInForm object containing the data
     *            used to retrieve the object (i.e. the primary key info).
     * @exception Exception
     *                If the com.cbrc.smis.form.ReportInForm object cannot be
     *                retrieved.
     */
    public static com.cbrc.smis.form.ReportInForm edit(
            com.cbrc.smis.form.ReportInForm reportInForm) throws Exception {
        com.cbrc.smis.hibernate.ReportIn reportInPersistence = new com.cbrc.smis.hibernate.ReportIn();
        TranslatorUtil.copyVoToPersistence(reportInPersistence, reportInForm);
        javax.naming.InitialContext ctx = new javax.naming.InitialContext();
        // TODO: Make adapter get SessionFactory jndi name by ant task argument?
//        net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory) ctx
//                .lookup("java:AirlineHibernateFactory");
//        net.sf.hibernate.Session session = factory.openSession();
        DBConn conn=new DBConn();
        Session session=conn.openSession();
        net.sf.hibernate.Transaction tx = session.beginTransaction();
        reportInPersistence = (com.cbrc.smis.hibernate.ReportIn) session.load(
                com.cbrc.smis.hibernate.ReportIn.class, reportInPersistence
                        .getRepInId());
        tx.commit();
        session.close();
        TranslatorUtil.copyPersistenceToVo(reportInPersistence, reportInForm);
        return reportInForm;
    }

    /**
     * Remove (delete) an existing com.cbrc.smis.form.ReportInForm object.
     * 
     * @param reportInForm
     *            The com.cbrc.smis.form.ReportInForm object containing the data
     *            to be deleted.
     * @exception Exception
     *                If the com.cbrc.smis.form.ReportInForm object cannot be
     *                removed.
     */
    public static void remove(com.cbrc.smis.form.ReportInForm reportInForm)
            throws Exception {
        com.cbrc.smis.hibernate.ReportIn reportInPersistence = new com.cbrc.smis.hibernate.ReportIn();
        TranslatorUtil.copyVoToPersistence(reportInPersistence, reportInForm);
        javax.naming.InitialContext ctx = new javax.naming.InitialContext();
        // TODO: Make adapter get SessionFactory jndi name by ant task argument?
        net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory) ctx
                .lookup("java:AirlineHibernateFactory");
        net.sf.hibernate.Session session = factory.openSession();
        net.sf.hibernate.Transaction tx = session.beginTransaction();
        // TODO: is this really needed?
        reportInPersistence = (com.cbrc.smis.hibernate.ReportIn) session.load(
                com.cbrc.smis.hibernate.ReportIn.class, reportInPersistence
                        .getRepInId());
        session.delete(reportInPersistence);
        tx.commit();
        session.close();
    }

    /**
     * Retrieve all existing com.cbrc.smis.form.ReportInForm objects.
     * 
     * @exception Exception
     *                If the com.cbrc.smis.form.ReportInForm objects cannot be
     *                retrieved.
     */
    public static List findAll() throws Exception {
        List retVals = new ArrayList();
        javax.naming.InitialContext ctx = new javax.naming.InitialContext();
        // TODO: Make adapter get SessionFactory jndi name by ant task argument?
        net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory) ctx
                .lookup("java:AirlineHibernateFactory");
        net.sf.hibernate.Session session = factory.openSession();
        net.sf.hibernate.Transaction tx = session.beginTransaction();
        retVals.addAll(session.find("from com.cbrc.smis.hibernate.ReportIn"));
        tx.commit();
        session.close();
        ArrayList reportIn_vos = new ArrayList();
        for (Iterator it = retVals.iterator(); it.hasNext();) {
            com.cbrc.smis.form.ReportInForm reportInFormTemp = new com.cbrc.smis.form.ReportInForm();
            com.cbrc.smis.hibernate.ReportIn reportInPersistence = (com.cbrc.smis.hibernate.ReportIn) it
                    .next();
            TranslatorUtil.copyPersistenceToVo(reportInPersistence,
                    reportInFormTemp);
            reportIn_vos.add(reportInFormTemp);
        }
        retVals = reportIn_vos;
        return retVals;
    }

   

    /**
     * This method will return all objects referenced by MDataRgType
     */
    public static List getMDataRgType(
            com.cbrc.smis.form.ReportInForm reportInForm) throws Exception {
        List retVals = new ArrayList();
        com.cbrc.smis.hibernate.ReportIn reportInPersistence = null;
        javax.naming.InitialContext ctx = new javax.naming.InitialContext();
        // TODO: Make adapter get SessionFactory jndi name by ant task argument?
        net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory) ctx
                .lookup("java:AirlineHibernateFactory");
        net.sf.hibernate.Session session = factory.openSession();
        net.sf.hibernate.Transaction tx = session.beginTransaction();
        reportInPersistence = (com.cbrc.smis.hibernate.ReportIn) session.load(
                com.cbrc.smis.hibernate.ReportIn.class, reportInPersistence
                        .getRepInId());
        tx.commit();
        session.close();
        retVals.add(reportInPersistence.getMDataRgType());
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
     * This method will return all objects referenced by MCurr
     */
    public static List getMCurr(com.cbrc.smis.form.ReportInForm reportInForm)
            throws Exception {
        List retVals = new ArrayList();
        com.cbrc.smis.hibernate.ReportIn reportInPersistence = null;
        javax.naming.InitialContext ctx = new javax.naming.InitialContext();
        // TODO: Make adapter get SessionFactory jndi name by ant task argument?
        net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory) ctx
                .lookup("java:AirlineHibernateFactory");
        net.sf.hibernate.Session session = factory.openSession();
        net.sf.hibernate.Transaction tx = session.beginTransaction();
        reportInPersistence = (com.cbrc.smis.hibernate.ReportIn) session.load(
                com.cbrc.smis.hibernate.ReportIn.class, reportInPersistence
                        .getRepInId());
        tx.commit();
        session.close();
        retVals.add(reportInPersistence.getMCurr());
        ArrayList CUR_ID_vos = new ArrayList();
        for (Iterator it = retVals.iterator(); it.hasNext();) {
            com.cbrc.smis.form.MCurrForm CUR_ID_Temp = new com.cbrc.smis.form.MCurrForm();
            com.cbrc.smis.hibernate.MCurr CUR_ID_PO = (com.cbrc.smis.hibernate.MCurr) it
                    .next();
            TranslatorUtil.copyPersistenceToVo(CUR_ID_PO, CUR_ID_Temp);
            CUR_ID_vos.add(CUR_ID_Temp);
        }
        retVals = CUR_ID_vos;
        return retVals;
    }

    /**
     * This method will return all objects referenced by MRepRange
     */
    public static List getMRepRange(com.cbrc.smis.form.ReportInForm reportInForm)
            throws Exception {
        List retVals = new ArrayList();
        com.cbrc.smis.hibernate.ReportIn reportInPersistence = null;
        javax.naming.InitialContext ctx = new javax.naming.InitialContext();
        // TODO: Make adapter get SessionFactory jndi name by ant task argument?
        net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory) ctx
                .lookup("java:AirlineHibernateFactory");
        net.sf.hibernate.Session session = factory.openSession();
        net.sf.hibernate.Transaction tx = session.beginTransaction();
        reportInPersistence = (com.cbrc.smis.hibernate.ReportIn) session.load(
                com.cbrc.smis.hibernate.ReportIn.class, reportInPersistence
                        .getRepInId());
        tx.commit();
        session.close();
        retVals.add(reportInPersistence.getMRepRange());
        ArrayList ORG_ID_vos = new ArrayList();
        for (Iterator it = retVals.iterator(); it.hasNext();) {
            com.cbrc.smis.form.MRepRangeForm ORG_ID_Temp = new com.cbrc.smis.form.MRepRangeForm();
            com.cbrc.smis.hibernate.MRepRange ORG_ID_PO = (com.cbrc.smis.hibernate.MRepRange) it
                    .next();
            TranslatorUtil.copyPersistenceToVo(ORG_ID_PO, ORG_ID_Temp);
            ORG_ID_vos.add(ORG_ID_Temp);
        }
        retVals = ORG_ID_vos;
        return retVals;
    }

    /**
     * This method will return all objects referenced by MChildReport
     */
    public static List getMChildReport(
            com.cbrc.smis.form.ReportInForm reportInForm) throws Exception {
        List retVals = new ArrayList();
        com.cbrc.smis.hibernate.ReportIn reportInPersistence = null;
        javax.naming.InitialContext ctx = new javax.naming.InitialContext();
        // TODO: Make adapter get SessionFactory jndi name by ant task argument?
        net.sf.hibernate.SessionFactory factory = (net.sf.hibernate.SessionFactory) ctx
                .lookup("java:AirlineHibernateFactory");
        net.sf.hibernate.Session session = factory.openSession();
        net.sf.hibernate.Transaction tx = session.beginTransaction();
        reportInPersistence = (com.cbrc.smis.hibernate.ReportIn) session.load(
                com.cbrc.smis.hibernate.ReportIn.class, reportInPersistence
                        .getRepInId());
        tx.commit();
        session.close();
        retVals.add(reportInPersistence.getMChildReport());
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

    /**
     * �÷������ڽ�һ��ʵ���ӱ������־û������ݿ���ȥ
     * @param mcr
     * @param mCurr
     * @param mdrt
     * @param mrr
     * @param year
     * @param term
     * @param writer
     * @param checker
     * @param principal
     * @param times
     * @param repName
     * @param date
     * @param zipFileName
     * @param xmlFileName
     * @param repOutId
     * @return
     */
    public static ReportIn insertReportIn(MChildReport mcr, MCurr mCurr,
            MDataRgType mdrt, MRepRange mrr, Integer year, Integer term,
            String writer, String checker,String principal,Integer times, String repName, Date date,
            String zipFileName, String xmlFileName,Integer repOutId) {

        ReportIn reportIn = new ReportIn();

        reportIn.setMChildReport(mcr); //���ӱ���SET��ʵ�ʱ��������

        reportIn.setMCurr(mCurr); //������SET��ʵ�ʱ��������

        reportIn.setMRepRange(mrr); //���������÷�ΧSET��ʵ�ʱ��������

        reportIn.setMDataRgType(mdrt); //�����ݷ�ΧSET��ʵ�ʱ��������

        reportIn.setOrgId(mrr.getComp_id().getOrgId());
        
        reportIn.setYear(year);

        reportIn.setTerm(term);

        reportIn.setWriter(writer);
        reportIn.setChecker(checker);
        reportIn.setPrincipal(principal);
        reportIn.setTimes(times);

        reportIn.setRepName(repName);

        reportIn.setReportDate(date);
        
        reportIn.setCheckFlag(new Short((short)0));
        
        reportIn.setRepOutId(repOutId);   //�������ݼ�¼ID
        
        reportIn.setReportDataWarehouseFlag(new Short("0"));  //��ʼ�����ݲֿ�ı�־
        
        reportIn.setForseReportAgainFlag(new Short("0"));   //��ʼ�������ر����־
        
        /*reportIn.setReportDate(reportDate);*/
                
        DBConn dBConn = null;

        Session session = null;

        try {

            dBConn = new DBConn();

            session = dBConn.beginTransaction();

            session.save(reportIn);

            dBConn.endTransaction(true);

        } catch (HibernateException e) {

            dBConn.endTransaction(false);

            String errorMessage = "�������ڴ洢" + zipFileName + "���ļ��е�";

            errorMessage = errorMessage + xmlFileName + "�ļ�";

            FitechLog.writeLog(Config.LOG_SYSTEM_SAVEDATA, ConfigOncb.HANDLER,errorMessage, "OVER");

            reportIn = null;

        } finally {

            if (session != null)

                dBConn.closeSession();
        }

        return reportIn;

    }

    /**
     * @author cb ��ʵ�ʱ�����޸�
     * 
     * @param reportIn
     * @param repName
     * @param date
     * @throws Exception
     */
    public static void updateReportIn(ReportIn reportIn, String repName,
            String dateString) throws Exception {

        DBConn dBConn = null;

        Session session = null;

        Date date = null;

        if (dateString.equals(""))

            dateString = null;

        if (dateString != null) {

            try {

                date = DateUtil.getDateByString(dateString, DateUtil.NORMALDATE);

            } catch (Exception e) {

                date = null;
            }

        }

        try {

            dBConn = new DBConn();

            session = dBConn.beginTransaction();

            reportIn.setRepName(repName);

            reportIn.setReportDate(date);

            session.update(reportIn);

            dBConn.endTransaction(true);

        } catch (Exception e) {

            dBConn.endTransaction(false);
        } finally {

            if (session != null) {

                dBConn.closeSession();
            }
        }
    }

    /**
     * ��������Ĳ������ж��ж�һ��ʵ�ʱ��Ƿ��Ѿ�¼�����
     * 
     * @param childRepId
     *            �ӱ���ID
     * @param versionId
     *            �汾��
     * @param orgId
     *            ����ID
     * @param mCurr
     *            ������
     * @param dataRangeId
     *            ���ݷ�Χ
     * @param year
     *            ���
     * @param term
     *            ����
     * @param times
     *            ����
     * @return boolean
     * @throws Exception
     */
    public static boolean isRepeat(String childRepId, String versionId,
            String orgId, String mCurrName, Integer dataRangeId, Integer year,
            Integer term, Integer times) throws Exception {

        DBConn dBConn = null;

        Session session = null;

        List l = null;

        Query query = null;

        boolean isre = false; //Ĭ�ϲ��ظ�

        int size;

        String hsql = "from ReportIn ri where  ";

        hsql = hsql + "ri.MChildReport.comp_id.versionId=:versionId and ";

        hsql = hsql + " ri.MChildReport.comp_id.childRepId=:childRepId and ";

        hsql = hsql + "ri.orgId=:orgId and ";

        hsql = hsql + "ri.MCurr.curName=:mCurrName and ";

        hsql = hsql + "ri.MDataRgType.dataRangeId=:dataRangeId and ";

        hsql = hsql + "ri.year=:year and ";

        hsql = hsql + "ri.term=:term  and ";

        hsql = hsql + "ri.times=:times ";

        try {

            dBConn = new DBConn();

            session = dBConn.openSession();

            query = session.createQuery(hsql);

            query.setString("childRepId", childRepId);

            query.setString("versionId", versionId);

            query.setString("orgId", orgId);

            if (mCurrName == null || mCurrName.equals(""))

                query.setString("mCurrName", ConfigOncb.COMMONCURRNAME);

            else

                query.setString("mCurrName", mCurrName);

            query.setInteger("dataRangeId", dataRangeId.intValue());

            query.setInteger("year", year.intValue());

            query.setInteger("term", term.intValue());

            query.setInteger("times", times.intValue());

            l = query.list();

            size = l.size();

            if (size != 0)

                isre = true;

            else

                 System.out.println("������");

        } catch (Exception e) {

            log.printStackTrace(e);

            isre = true;
        }

        finally {

            if (session != null)

                dBConn.closeSession();
        }
        return isre;
    }

    /*
     * public ReportIn getReportInOncb(Integer id)throws Exception{
     * 
     * 
     * DBConn dBConn = null;
     * 
     * Session session = null;
     * 
     * ReportIn = null;
     * 
     * try{
     * 
     * dBConn = new DBConn(); } }
     */
    /**
     * ȡ����Ҫת��xml�ı�����Ϣ
     * @author Ҧ��
     * @return List ������Ҫת��xml�ı�����Ϣ
     */
    public static List getNeedToXmlReps()
    {
        List result = new ArrayList();
        DBConn conn = null;
        Session session = null;
        
        String hql = "from ReportIn ri where ri.checkFlag=1 and ri.reportDataWarehouseFlag=0 and " +
        	"ri.times=(select max(r.times) from ReportIn r where " + 
        	"ri.MChildReport.comp_id.childRepId=r.MChildReport.comp_id.childRepId and " +
        	"ri.MChildReport.comp_id.versionId=r.MChildReport.comp_id.versionId and " + 
        	"ri.MDataRgType.dataRangeId=r.MDataRgType.dataRangeId and " +
        	"ri.orgId=r.orgId and ri.MCurr.curId=r.MCurr.curId and " +
        	"ri.year=r.year and ri.term=r.term and r.checkFlag=1 and r.reportDataWarehouseFlag=0)";
               
        try 
        {
        	conn = new DBConn();
            session = conn.openSession();
            result = session.createQuery(hql).list(); 
        } 
        catch (Exception e) {
           log.printStackTrace(e);
        }
        finally{
            if(conn!=null) conn.closeSession();
        }
        return result;      
    }
    /**
     * ���������ݲֿ��־�ı�
     * @author Ҧ��
     * @param ʵ���ӱ���id
     * @return �Ƿ�ɹ�
     */
    public static boolean setReportDataWarehouseFlag(Integer repInId)
    {
        boolean result = false;
        DBConn conn = null;
        Session session = null;
        
        conn = new DBConn();
        
        try 
        {
            session = conn.beginTransaction();
            ReportIn repIn = (ReportIn)session.load(ReportIn.class, repInId);
            if(repIn!=null)
            {
                repIn.setReportDataWarehouseFlag(Short.valueOf("1"));
                session.update(repIn);
                session.flush();
                result = true;
            }
        } 
        catch (Exception e) {
            result = false;
           log.printStackTrace(e);
        }
        finally{
            if(conn!=null)
                conn.endTransaction(result);
        }
        return result;      
    }
    /**
     * �ر������ѯ
     * @author�ܷ���
     * @return ���ؼ�¼����
     */
    public static int getCount(ReportInForm form,Operator operator) {
		int result = 0;
		if (null == form)
			return result;
		StringBuffer hql = new StringBuffer("select count(*) from ReportAgainSet a");
		
		StringBuffer where = new StringBuffer(" where a.reportIn.forseReportAgainFlag="	+ 
				Config.FORSE_REPORT_AGAIN_FLAG_1);
		
		String[] repname = null;
		if (!form.getRepName().equals("0"))  repname = form.getRepName().split(",");
		
		if (form.getOrgName() != null && !form.getOrgName().equals("")) {
			String orgids = com.cbrc.org.adapter.StrutsMOrgDelegate.getOrgid(form.getOrgName());
			if(!orgids.equals(""))
				where.append((where.toString().equals("") ? "" : " and ")
					+ " a.reportIn.orgId in(" +orgids+ ")");
		}
		
		if (null != repname&&!(repname.length<2)) {
			where.append((where.toString().equals("") ? "" : " and ")
					+ " a.reportIn.MChildReport.comp_id.childRepId ='"
					+ repname[0] + "'");
			where.append((where.toString().equals("") ? "" : " and ")
					+ " a.reportIn.MChildReport.comp_id.versionId ='"
					+ repname[1] + "'");
		}
		
	/*	if (null != form.getChildRepId() && !form.getChildRepId().equals("0") && !form.getChildRepId().equals("")){
			where.append((where.toString().equals("") ? "" : " and ")
					+ " a.reportIn.orgId in("
					+ com.cbrc.org.adapter.StrutsMOrgDelegate.getOrgids(form.getChildRepId()) + ")");
		}*/
		if (null != form.getStartDate() && !form.getStartDate().equals("")){
			where.append((where.toString().equals("") ? "" : " and ")
					+ " day(a.setDate) >=day('" + form.getStartDate()+ "')");
		}
		if (null != form.getEndDate() && !form.getEndDate().equals("")){
			where.append((where.toString().equals("") ? "" : " and ")
					+ "day(a.setDate) <=day('" + form.getEndDate()+ "')");
		}
        
        /**
         * ���ϻ����ͱ���Ȩ����Ϣ��û��Ȩ����ʲôҲ��ѯ����
         * @author Ҧ��
         * */
        if(operator ==null) return result;
        /**����Ȩ��*/
        if(operator.getOrgPopedomSQL()!=null && !operator.getOrgPopedomSQL().equals("")){
        	String sql="select distinct pv.comp_id.orgId from MPurView pv where pv.comp_id.userGrpId in("+operator.getUserGrpIds() +")";
            where.append((where.toString().equals("") ? "" : " and ")
                    + " a.reportIn.orgId in("+sql+")");
        }else{
            return result;
        }
        /**����Ȩ��*/
        if(operator.getChildRepPodedomSQL()!=null && !operator.getChildRepPodedomSQL().equals("")){
            where.append((where.toString().equals("") ? "" : " and ")
                    + " a.reportIn.MChildReport.comp_id.childRepId in("+operator.getChildRepPodedomSQL()+")");
        }else{
             return result;
        }
        
		if (!where.toString().equals("")) hql.append(where.toString());
		
		DBConn conn = null;
		Session session = null;
		
		try {
			conn = new DBConn();
			session = conn.openSession();
			Query query = session.createQuery(hql.toString());
			// System.out.println("###"+hql);
			List list = query.list();

			if (list != null) {
				result = ((Integer) list.get(0)).intValue();
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
     * �ر������ѯ
     * @author�ܷ���
     * @return ���ؼ�¼
     */
    public static List getRecord(ReportInForm form,int offset,int limit,Operator operator){
    	ArrayList result=new ArrayList();
 	   if(null==form)return result;
 	   StringBuffer hql = new StringBuffer("select a.rasId,a.reportIn.repInId,a.cause,a.setDate,a.reportIn.repName ,a.reportIn.orgId from ReportAgainSet a ");
 	  StringBuffer where = new StringBuffer(
				" where a.reportIn.forseReportAgainFlag="
						+ Config.FORSE_REPORT_AGAIN_FLAG_1);
		String[] repname = null;
		if (!form.getRepName().equals("0"))
			repname = form.getRepName().split(",");
		if (form.getOrgName() != null && !form.getOrgName().equals("")) {
			String orgids = com.cbrc.org.adapter.StrutsMOrgDelegate.getOrgid(form.getOrgName());
			if(!orgids.endsWith(""))
			where.append((where.toString().equals("") ? "" : " and ")
					+ " a.reportIn.orgId in(" +orgids+ ")");
		}
		if (null != repname&&!(repname.length<2)) {
			where.append((where.toString().equals("") ? "" : " and ")
					+ " a.reportIn.MChildReport.comp_id.childRepId ='"
					+ repname[0] + "'");
			where.append((where.toString().equals("") ? "" : " and ")
					+ " a.reportIn.MChildReport.comp_id.versionId ='"
					+ repname[1] + "'");
		}
		if (null != form.getOrgClsId() && !form.getOrgClsId().equals("0") && !form.getOrgClsId().equals("")){
			// System.out.println("form.getChildRepId():" + form.getChildRepId());
			// System.out.println("OrgIds:" + com.cbrc.org.adapter.StrutsMOrgDelegate.getOrgids(form.getChildRepId()));
			where.append((where.toString().equals("") ? "" : " and ")
					+ " a.reportIn.orgId in("
					+ com.cbrc.org.adapter.StrutsMOrgDelegate.getOrgids(form.getChildRepId()) + ")");
		}
		if (null != form.getStartDate() && !form.getStartDate().equals(""))
			where.append((where.toString().equals("") ? "" : " and ")
					+ "day( a.setDate) >=day('" + form.getStartDate()+ "')");
		if (null != form.getEndDate() && !form.getEndDate().equals(""))
			where.append((where.toString().equals("") ? "" : " and ")
					+ "day( a.setDate )<=day('" + form.getEndDate()+"')");
        /**
         * ���ϻ����ͱ���Ȩ����Ϣ
         * @author Ҧ��
         * */
        if(operator==null)
            return result;
        /**����Ȩ��*/
        if(operator.getOrgPopedomSQL()!=null && !operator.getOrgPopedomSQL().equals(""))
        {
        	String sql="select distinct pv.comp_id.orgId from MPurView pv where pv.comp_id.userGrpId in("+operator.getUserGrpIds() +")";
            where.append((where.toString().equals("") ? "" : " and ")
                    + " a.reportIn.orgId in("+sql+")");
        }
            else
            return result;
        /**����Ȩ��*/
        if(operator.getChildRepPodedomSQL()!=null && !operator.getChildRepPodedomSQL().equals(""))
            where.append((where.toString().equals("") ? "" : " and ")
                    + " a.reportIn.MChildReport.comp_id.childRepId in("+operator.getChildRepPodedomSQL()+")");
        else
            return result;
        
		where.append(" order by a.setDate desc ");
		if (!where.toString().equals(""))
			hql.append(where.toString());
 	   DBConn conn=null;                
        Session session=null;
 	   try{
 		   conn=new DBConn();
            session=conn.openSession();
            Query query=session.createQuery(hql.toString());  
            query.setFirstResult(offset).setMaxResults(limit);
            List list=query.list();

            if (list!=null)for(int i=0;i<list.size();i++){
            	Object[] items=(Object[])list.get(i);
            	ReportAgainSetForm setform=new ReportAgainSetForm();
            	setform.setRasId((Integer)items[0]);
            	setform.setRepInId(items[1] == null ? new Integer(0):(Integer) items[1]);
            	setform.setCause(items[2] == null ? "":(String)items[2]);
            	setform.setSetDate(items[3]!=null?(Timestamp)items[3]:null);
            	setform.setRepName(items[4] == null ? "":(String)items[4]);
            	setform.setOrgName(com.cbrc.org.adapter.StrutsMOrgDelegate.selectOne(items[5].toString()).getOrg_name ()); 
            	result.add(setform);
            }   
 	   }catch(Exception e){
 		   e.printStackTrace();

        }finally{
            if(conn!=null) conn.closeSession();
        }   
 	   return result;
    }
    /**
     * ��֤�½�ǿ���ر��ύ�ı�
     * @author �ܷ���
     * @return true ��Ϣ��д��ȷ��false ��Ϣ��д����
     */
    public static boolean validate(ReportInForm reportInForm) {
		boolean result = false;
		DBConn conn = null;
		Session session = null;
		try {
			if (reportInForm == null)
				return result;
			String repName = reportInForm.getRepName();
			String versionId = reportInForm.getVersionId();
			String orgName = reportInForm.getOrgName();
		//	String dataRangeId = reportInForm.getDataRangeId().toString();
			String curId = reportInForm.getCurId().toString();
			String term = reportInForm.getTerm().toString();
			String year = reportInForm.getYear().toString();
			String cause = reportInForm.getCause();

			if (repName == null || versionId == null || orgName == null
					 || curId == null || term == null
					|| year == null || cause == null)
				return result;
			if (repName.equals("") || versionId.equals("")
					|| orgName.equals("") 
					|| curId.equals("") || term.equals("") || year.equals("")
					|| cause.equals(""))
				return result;
		
			String orgids = com.cbrc.org.adapter.StrutsMOrgDelegate
					.getOrgNetid(reportInForm.getOrgName());
			
			if (orgids.equals(""))
				return false;
			StringBuffer hql = new StringBuffer(
					"select count(*) from ReportIn a");
			StringBuffer where = new StringBuffer(" where a.repName='"
					+ repName + "' and a.MRepRange.comp_id.versionId='"
					+ versionId + "' and a.orgId in(" + orgids
					+ ") " 
					+ " and a.MCurr.curId=" + curId 
					+ " and a.term=" + term
					+ " and a.year=" + year);
			hql.append(where);
// System.out.println(hql.toString() );

			conn = new DBConn();
			session = conn.openSession();
			Query query = session.createQuery(hql.toString());
			List list = query.list();

			if (list != null) {
				int count = ((Integer) list.get(0)).intValue();
				if (count == 1)
					result = true;
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
     * �½�ǿ���ر�
     * @author �ܷ���
     * @return true �½��ɹ���false�½�ʧ�ܣ�
     * @throws HibernateException 
     */
    public static boolean newForseReportIn (ReportInForm reportInForm) throws HibernateException{
    	
    	boolean result=false;
    	DBConn conn = null;
		Session session = null;
		Transaction tx=null;
		try {
			conn = new DBConn();
			session = conn.openSession();
			tx=session.beginTransaction();
			String repName=reportInForm.getRepName();
	    	String versionId=reportInForm.getVersionId();
	    	String dataRangeId=reportInForm.getDataRangeId().toString();
	    	String curId=reportInForm.getCurId().toString();
	    	String term =reportInForm.getTerm().toString();
	    	String year=reportInForm.getYear().toString();
	    	String orgids = com.cbrc.org.adapter.StrutsMOrgDelegate.getOrgid(reportInForm.getOrgName());
	    	StringBuffer hql = new StringBuffer("from ReportIn a");
	   	    StringBuffer where = new StringBuffer(" where a.repName='" + repName + 
	   	    		"' and a.MRepRange.comp_id.versionId='" + versionId + 
	   	    		"'and a.orgId in(" + orgids + ")and a.MDataRgType.dataRangeId=" + dataRangeId + 
	   	    		" and a.MCurr.curId=" + curId + " and a.term=" + term + 
	   	    		" and a.year=" + year + 
	   	    		" and a.checkFlag!=" + Config.CHECK_FLAG_NO + 
	   	    		" and a.reportDataWarehouseFlag!=" + Config.Reported_Data_Warehouse + 
	   	    		" order by a.times desc");
            hql.append(where);
	   	    Query query = session.createQuery(hql.toString());
			List list = query.list();
			ReportIn reportIn=(ReportIn)list.get(0);
			reportIn.setForseReportAgainFlag(Config.FORSE_REPORT_AGAIN_FLAG_1);
			ReportAgainSet reportAgainSet=new ReportAgainSet();
			reportAgainSet.setCause(reportInForm.getCause());
			reportAgainSet.setSetDate(reportInForm.getSetDateasDate());
			reportAgainSet.setRepInId(reportIn.getRepInId());
			session.save(reportIn);
			session.save(reportAgainSet);
			tx.commit();
			TranslatorUtil.copyPersistenceToVo(reportIn, reportInForm);
			session.close();
			result=true;
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			result=false;

		} finally {
			if (conn != null)
				conn.closeSession();
		}
    	return result;
    }


    /**
     * ����������ѯ��������
     * @param reportInForm
     * @return
     */
    public static int getRecordCount(ReportInForm reportInForm,Operator operator) {
        int count = 0;

        //���Ӷ���ͻỰ�����ʼ��
        DBConn conn = null;
        Session session = null;

        //	 ��ѯ����HQL������
        StringBuffer hql = new StringBuffer("select count(*) from ReportIn ri");
        StringBuffer where = new StringBuffer("");

        if (reportInForm != null) {
            // �����������ж�,�������Ʋ���Ϊ��
            if (reportInForm.getOrgId() != null
                    && !reportInForm.getOrgId().equals(""))
                where.append((where.toString().equals("") ? "" : " or ")
                        + "mc.orgId like '%" + reportInForm.getOrgId() + "%'");
        }

        try { //List���ϵĲ���
            //��ʼ��
            hql.append((where.toString().equals("") ? "" : " where ")
                    + where.toString());
            /**
             * ���ϻ����ͱ���Ȩ����Ϣ
             * @author Ҧ��
             * */
            if(operator.getOrgPopedomSQL()!=null && !operator.getOrgPopedomSQL().equals(""))
                where.append((where.toString().equals("") ? "" : " and ")
                        + "ri.orgId in("+operator.getOrgPopedomSQL()+")");
            if(operator.getChildRepPodedomSQL()!=null && !operator.getChildRepPodedomSQL().equals(""))
                where.append((where.toString().equals("") ? "" : " and ")
                        + "ri.MChildReport.comp_id.childRepId in("+operator.getChildRepPodedomSQL()+")");
            
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
     * ����oracle�﷨(upper) ���Ը� 2011-12-22
     * ��ʹ��hibernate 
     * ��ѯ�����趨ǿ���ر��ı����¼�������ͨ���ı���
     * 
     * @param reportInForm ����FormBean
     * @param operator ��ǰ��¼�û�
     * @return int �����¼��
     */
    public static int getRecordCountOfmanual(ReportInForm reportInForm,Operator operator) {    	
        int count = 0;
        DBConn conn = null;
        Session session = null;

        if(reportInForm == null || operator == null)
        	return count;
        
        try {         	
        	//��ѯ����HQL������
            StringBuffer hql = new StringBuffer("select count(*) from ReportIn ri WHERE " +
            		"ri.times=(select max(r.times) from ReportIn r where r.MChildReport.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and " + 
            		"r.MChildReport.comp_id.versionId=ri.MChildReport.comp_id.versionId and r.MDataRgType.dataRangeId=ri.MDataRgType.dataRangeId and " +
            		"r.orgId=ri.orgId and r.MCurr.curId=ri.MCurr.curId and r.year=ri.year and r.term=ri.term and r.checkFlag=ri.checkFlag)");
            
            /**��ѯ����״̬Ϊ���ͨ������*/
			StringBuffer where = new StringBuffer(" and ri.checkFlag=" + com.fitech.net.config.Config.CHECK_FLAG_PASS);
			
			/**��ӱ����Ų�ѯ���������Դ�Сдģ����ѯ��*/
			if(reportInForm.getChildRepId() != null && !reportInForm.getChildRepId().equals("")){				
				where.append(" and upper(ri.MChildReport.comp_id.childRepId) like upper('%" 
						+ reportInForm.getChildRepId().trim() + "%')");
			}
			/**��ӱ������Ʋ�ѯ������ģ����ѯ��*/
			if(reportInForm.getRepName() != null && !reportInForm.getRepName().equals("")){
				where.append(" and ri.repName like '%" + reportInForm.getRepName().trim() + "%'");
			}
			/**���ģ�����ͣ�ȫ��/����/��֧����ѯ����*/
			if(reportInForm.getFrOrFzType() != null && !reportInForm.getFrOrFzType().equals(Config.DEFAULT_VALUE)){
				where.append(" and ri.MChildReport.frOrFzType='" + reportInForm.getFrOrFzType() + "'");
			}
			/**��ӱ���Ƶ�ȣ���/��/����/�꣩��ѯ����*/
			if(reportInForm.getRepFreqId() != null && !String.valueOf(reportInForm.getRepFreqId()).equals(Config.DEFAULT_VALUE)){
				where.append(" and ri.MDataRgType.dataRangeId in (select M.comp_id.dataRangeId from MActuRep M " 
						+ "where M.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and M.comp_id.versionId=ri.MChildReport.comp_id.versionId " 
						+ "and M.comp_id.repFreqId=" + reportInForm.getRepFreqId() + ")");
			}
			/**������ڣ���ݣ���ѯ����*/
			if(reportInForm.getYear() != null){
				where.append(" and ri.year=" + reportInForm.getYear());
			}
			/**������ڣ��·ݣ���ѯ����*/
			if(reportInForm.getTerm() != null){
				where.append(" and ri.term=" + reportInForm.getTerm());
			}
			/**��ӻ�����ѯ����*/
			if(!StringUtil.isEmpty(reportInForm.getOrgId()) && !reportInForm.getOrgId().equals(Config.DEFAULT_VALUE)){
				where.append(" and ri.orgId='" + reportInForm.getOrgId().trim() + "'");
			}
			
			/**��ӱ������Ȩ�ޣ�ǿ���ر�Ȩ�������Ȩ��һ�£�
			 * ���������ݿ��ж�*/
			if (operator.isSuperManager() == false){
				if (operator.getChildRepCheckPodedom() == null || operator.getChildRepCheckPodedom().equals(""))
					return count;
				if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
					where.append(" and ltrim(rtrim(ri.orgId)) || ri.MChildReport.comp_id.childRepId in("
							+ operator.getChildRepCheckPodedom() + ")");
				if(Config.DB_SERVER_TYPE.equals("sqlserver"))
					where.append(" and ltrim(rtrim(ri.orgId)) + ri.MChildReport.comp_id.childRepId in("
							+ operator.getChildRepCheckPodedom() + ")");
			}
            hql.append(where.toString());
            
            conn = new DBConn();
            session = conn.openSession();
            List list = session.find(hql.toString());
            if (list != null && list.size() >0)
            	count = ((Integer) list.get(0)).intValue();
        } catch (HibernateException he) {
        	count=0;
            log.printStackTrace(he);
        } catch (Exception e) {
        	count=0;
            log.printStackTrace(e);
        } finally {
        	//�ر����ݿ�����
            if (conn != null) 
            	conn.closeSession();
        }        
        return count;
    }
    
    /**
     * ����oracle�﷨(upper) ���Ը� 2011-12-22
     * ��ʹ��hibernate 
     * ��ѯ�����趨ǿ���ر��ı����¼�����ͨ���ı���
     * 
     * @param reportInForm ����FormBean
     * @param offset ƫ����
     * @param limit ÿҳ��ʾ��¼��
     * @param operator ��ǰ��¼�û�
     * @return List �����趨ǿ���ر��ı����¼�����
     */
    public static List selectOfManual(ReportInForm reportInForm, int offset, int limit,Operator operator){
        List resList = null;
        DBConn conn = null;
        Session session = null;
        
        if(reportInForm == null | operator == null)
        	return resList;

		try {
			//��ѯ����HQL������
            StringBuffer hql = new StringBuffer("from ReportIn ri WHERE " +
            		"ri.times=(select max(r.times) from ReportIn r where r.MChildReport.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and " + 
            		"r.MChildReport.comp_id.versionId=ri.MChildReport.comp_id.versionId and r.MDataRgType.dataRangeId=ri.MDataRgType.dataRangeId and " +
            		"r.orgId=ri.orgId and r.MCurr.curId=ri.MCurr.curId and r.year=ri.year and r.term=ri.term and r.checkFlag=ri.checkFlag)");
            
            /**��ѯ����״̬Ϊ���ͨ������*/
			StringBuffer where = new StringBuffer(" and ri.checkFlag=" + com.fitech.net.config.Config.CHECK_FLAG_PASS);
			
			/**��ӱ����Ų�ѯ���������Դ�Сдģ����ѯ��*/
			if(reportInForm.getChildRepId() != null && !reportInForm.getChildRepId().equals("")){				
				where.append(" and upper(ri.MChildReport.comp_id.childRepId) like upper('%" 
						+ reportInForm.getChildRepId().trim() + "%')");
			}
			/**��ӱ������Ʋ�ѯ������ģ����ѯ��*/
			if(reportInForm.getRepName() != null && !reportInForm.getRepName().equals("")){
				where.append(" and ri.repName like '%" + reportInForm.getRepName().trim() + "%'");
			}
			/**���ģ�����ͣ�ȫ��/����/��֧����ѯ����*/
			if(reportInForm.getFrOrFzType() != null && !reportInForm.getFrOrFzType().equals(Config.DEFAULT_VALUE)){
				where.append(" and ri.MChildReport.frOrFzType='" + reportInForm.getFrOrFzType() + "'");
			}
			/**��ӱ���Ƶ�ȣ���/��/����/�꣩��ѯ����*/
			if(reportInForm.getRepFreqId() != null && !String.valueOf(reportInForm.getRepFreqId()).equals(Config.DEFAULT_VALUE)){
				where.append(" and ri.MDataRgType.dataRangeId in (select M.comp_id.dataRangeId from MActuRep M " 
						+ "where M.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and M.comp_id.versionId=ri.MChildReport.comp_id.versionId " 
						+ "and M.comp_id.repFreqId=" + reportInForm.getRepFreqId() + ")");
			}
			/**������ڣ���ݣ���ѯ����*/
			if(reportInForm.getYear() != null){
				where.append(" and ri.year=" + reportInForm.getYear());
			}
			/**������ڣ��·ݣ���ѯ����*/
			if(reportInForm.getTerm() != null){
				where.append(" and ri.term=" + reportInForm.getTerm());
			}
			/**��ӻ�����ѯ����*/
			if(!StringUtil.isEmpty(reportInForm.getOrgId())  && !reportInForm.getOrgId().equals(Config.DEFAULT_VALUE)){
				where.append(" and ri.orgId='" + reportInForm.getOrgId().trim() + "'");
			}
			
			/**��ӱ������Ȩ�ޣ�ǿ���ر�Ȩ�������Ȩ��һ�£�
			 * ���������ݿ��ж�*/
			if (operator.isSuperManager() == false){
				if (operator.getChildRepCheckPodedom() == null || operator.getChildRepCheckPodedom().equals(""))
					return resList;
				if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
					where.append(" and ltrim(rtrim(ri.orgId)) || ri.MChildReport.comp_id.childRepId in("
							+ operator.getChildRepCheckPodedom() + ")");
				if(Config.DB_SERVER_TYPE.equals("sqlserver"))
					where.append(" and ltrim(rtrim(ri.orgId)) + ri.MChildReport.comp_id.childRepId in("
							+ operator.getChildRepCheckPodedom() + ")");
			}						
        	hql.append(where.toString() + " order by ri.year,ri.term,ri.orgId");
        	
            conn = new DBConn();
            session = conn.openSession();
            Query query = session.createQuery(hql.toString());
            query.setFirstResult(offset).setMaxResults(limit);
            List list = query.list();
     
            if (list != null){            	
                resList = new ArrayList();
                Aditing aditing = null;
                OrgNet orgNet = null;
                ReportIn reportInRecord = null;
                
                for (Iterator it = list.iterator(); it.hasNext();){                
                    aditing = new Aditing();
                    reportInRecord = (ReportIn) it.next();
                    //���ñ������״̬
                    aditing.setCheckFlag(reportInRecord.getCheckFlag());
                    //���ñ��ͻ�������                    
                    orgNet = StrutsOrgNetDelegate.selectOne(reportInRecord.getOrgId());
                    if(orgNet != null) aditing.setOrgName(orgNet.getOrgName());                   
                    //���ñ���ID��ʶ��
                    aditing.setRepInId(reportInRecord.getRepInId());
                    //���ñ������
                    aditing.setYear(reportInRecord.getYear());
                    //���ñ�������
                    aditing.setTerm(reportInRecord.getTerm());
                    //���ñ�������
                    aditing.setRepName(reportInRecord.getRepName());
                    //���ñ���������
                    aditing.setReportDate(reportInRecord.getReportDate());
                    //���ñ�����
                    aditing.setChildRepId(reportInRecord.getMChildReport().getComp_id().getChildRepId());
                    //���ñ���汾��
                    aditing.setVersionId(reportInRecord.getMChildReport().getComp_id().getVersionId());
                    //���ñ����������
                    aditing.setCurrName(reportInRecord.getMCurr().getCurName());
                    //�����쳣�仯��־
                    aditing.setAbmormityChangeFlag(reportInRecord.getAbmormityChangeFlag());             
                    MActuRep mActuRep = GetFreR(reportInRecord);
					if (mActuRep != null){
						//���ñ��Ϳھ�
						aditing.setDataRgTypeName(mActuRep.getMDataRgType().getDataRgDesc());
						//���ñ���Ƶ��
						aditing.setActuFreqName(mActuRep.getMRepFreq().getRepFreqName());
					}                               
                    resList.add(aditing);
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
     * �ر��������ͨ���ı���
     * 
     * @param reportInForm
     * @param operator
     * @return
     * @throws Exception
     */
    public static List selectOfManuals(ReportInForm reportInForm, Operator operator)throws Exception {
        List retvals = null;
        DBConn conn = null;
        Session session = null;
        String childRepCheckPodedom = null;
        
        StringBuffer hql = new StringBuffer("from ReportIn ri where " +
        		"ri.times=(select max(r.times) from ReportIn r where r.MChildReport.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and " + 
        		"r.MChildReport.comp_id.versionId=ri.MChildReport.comp_id.versionId and r.MDataRgType.dataRangeId=ri.MDataRgType.dataRangeId and r.orgId=ri.orgId and " +
        		"r.MCurr.curId=ri.MCurr.curId and r.year=ri.year and r.term=ri.term and r.checkFlag not in ("+ com.fitech.net.config.Config.CHECK_FLAG_AFTERJY +","+ com.fitech.net.config.Config.CHECK_FLAG_UNREPORT+"))");
        
		StringBuffer where = new StringBuffer(" and ri.checkFlag=" + com.fitech.net.config.Config.CHECK_FLAG_PASS);
		if (reportInForm == null) return retvals;

		try {
			//�����������ж�
			if (reportInForm.getOrgName() != null && !reportInForm.getOrgName().equals("")) {				
				String orgIds = StrutsOrgNetDelegate.selectOrgIdToString(reportInForm.getOrgName());				
				if(orgIds != null && !orgIds.equals("")) 
					where.append(" and ri.orgId in (" +orgIds + ")");
			}
			if(reportInForm.getYear()!=null && reportInForm.getYear().intValue()>0){
        		where.append(" and ri.year=" + reportInForm.getYear());
        	}
        	if(reportInForm.getTerm()!=null && reportInForm.getTerm().intValue()>0){
        		where.append(" and ri.term=" + reportInForm.getTerm());
        	}
			//��ʼʱ��������ж�
			if (reportInForm.getStartDate() != null && !reportInForm.getStartDate().equals("")) {
				where.append(" and ri.reportDate>='"+ reportInForm.getStartDate()+"'");
			}			
			//����ʱ����ж�
			if (reportInForm.getEndDate() != null && !reportInForm.getEndDate().equals("")) {
				where.append(" and ri.reportDate<='" + reportInForm.getEndDate()+"'");
			}
        	//�������Ƶ��ж�
			if(reportInForm.getRepName()!=null&& !reportInForm.getRepName().equals("")){
				where.append(" and ri.repName like '%"+reportInForm.getRepName()+"%'");				
			}
        	
			/**���ϱ������Ȩ�ޣ�ǿ���ر�Ȩ�������Ȩ��һ�£�*/
			if(operator == null) return retvals;
			childRepCheckPodedom = operator.getChildRepCheckPodedom();
			if(operator.isSuperManager() == false){
				if(childRepCheckPodedom == null || childRepCheckPodedom.equals(""))
					return retvals;
			}
						
        	hql.append(where.toString() + " order by ri.year,ri.term");
            conn = new DBConn();
            session = conn.openSession();
            Query query = session.createQuery(hql.toString());
            List list = query.list();
     
            if (list != null){                            
                if(operator.isSuperManager() == false){
                	retvals = new ArrayList();
                	String childRepId = null;
                	for (Iterator it = list.iterator(); it.hasNext();){
                        ReportIn reportInPersistence = (ReportIn) it.next();
                        childRepId = reportInPersistence.getMChildReport().getComp_id().getChildRepId();
						if(childRepCheckPodedom.indexOf(reportInPersistence.getOrgId()+childRepId) <= -1)
							continue;
                        
                        Aditing aditing=new Aditing();
                        aditing.setCheckFlag(reportInPersistence.getCheckFlag());
                        OrgNet org = StrutsOrgNetDelegate.selectOne(reportInPersistence.getOrgId());
                        if(org != null && org.getOrgName() != null){                	   
                        	aditing.setOrgName(org.getOrgName());                   
                        }
                        aditing.setRepInId(reportInPersistence.getRepInId());
                        aditing.setYear(reportInPersistence.getYear());
                        aditing.setTerm(reportInPersistence.getTerm());
                        if(!reportInPersistence.getRepName().equals(reportInPersistence.getMChildReport().getReportName()))
                        	aditing.setRepName(reportInPersistence.getRepName()+"-"+reportInPersistence.getMChildReport().getReportName());
                        else 
                        	aditing.setRepName(reportInPersistence.getRepName());
                        aditing.setReportDate(reportInPersistence.getReportDate());
                        aditing.setChildRepId(reportInPersistence.getMChildReport().getComp_id().getChildRepId());
                        aditing.setVersionId(reportInPersistence.getMChildReport().getComp_id().getVersionId());
                        aditing.setCurrName(reportInPersistence.getMCurr().getCurName());                   
                        aditing.setAbmormityChangeFlag(reportInPersistence.getAbmormityChangeFlag());                    
                       
                        MActuRep  mActuRep= GetFreR(reportInPersistence);                   
                        if (mActuRep!=null){                  
                        	aditing.setDataRgTypeName(mActuRep.getMDataRgType().getDataRgDesc());                   
                        	aditing.setActuFreqName(mActuRep.getMRepFreq().getRepFreqName());                   
                        }
                        retvals.add(aditing);
                    }
                }else{
                	retvals = new ArrayList();
                	for (Iterator it = list.iterator(); it.hasNext();){
                        Aditing aditing=new Aditing();
                        ReportIn reportInPersistence = (ReportIn) it.next();
                        aditing.setCheckFlag(reportInPersistence.getCheckFlag());
                        OrgNet org = StrutsOrgNetDelegate.selectOne(reportInPersistence.getOrgId());
                        if(org != null && org.getOrgName() != null){
                        	aditing.setOrgName(org.getOrgName());                
                        }
                        aditing.setRepInId(reportInPersistence.getRepInId());
                        aditing.setYear(reportInPersistence.getYear());
                        aditing.setTerm(reportInPersistence.getTerm());
                        if(!reportInPersistence.getRepName().equals(reportInPersistence.getMChildReport().getReportName()))
                        	aditing.setRepName(reportInPersistence.getRepName()+"-"+reportInPersistence.getMChildReport().getReportName());
                        else 
                        	aditing.setRepName(reportInPersistence.getRepName());
                        aditing.setReportDate(reportInPersistence.getReportDate());
                        aditing.setChildRepId(reportInPersistence.getMChildReport().getComp_id().getChildRepId());
                        aditing.setVersionId(reportInPersistence.getMChildReport().getComp_id().getVersionId());
                        aditing.setCurrName(reportInPersistence.getMCurr().getCurName());                   
                        aditing.setAbmormityChangeFlag(reportInPersistence.getAbmormityChangeFlag());                    
                       
                        MActuRep  mActuRep= GetFreR(reportInPersistence);                   
                        if (mActuRep!=null){                  
                        	aditing.setDataRgTypeName(mActuRep.getMDataRgType().getDataRgDesc());                   
                        	aditing.setActuFreqName(mActuRep.getMRepFreq().getRepFreqName());                   
                        }
                        retvals.add(aditing);
                    }
                }
            }
        } catch (HibernateException he) {
            retvals = null;
            log.printStackTrace(he);
        } catch (Exception e) {
            retvals = null;
            log.printStackTrace(e);
        } finally {
            //������Ӵ��ڣ���Ͽ��������Ự������
            if (conn != null)
                conn.closeSession();
        }
        
        return retvals;
    }
    
    /**
     * ���ݱ���ס����Ϣ���ظñ����Ƶ����Ϣ
     * @param in
     * @return
     */
    public static MActuRep  GetFreR(ReportIn in){	  
    	DBConn conn=null;	  
    	Session session = null;

    	StringBuffer sql=new StringBuffer("from MActuRep M where 1=1 ");	  
    	String version=in.getMChildReport().getComp_id().getVersionId();	  
    	String RepId=in.getMChildReport().getComp_id().getChildRepId();
    	String dataRangeId=in.getMDataRgType().getDataRangeId() != null ? 
    			in.getMDataRgType().getDataRangeId().toString() : null;
    	
    	if(RepId!=null && !RepId.equals(""))		
    		sql.append(" and M.MChildReport.comp_id.childRepId='"+RepId+"'");	  
    	if(version!=null && !version.equals(""))		
    		sql.append(" and M.MChildReport.comp_id.versionId='"+version+"'");
    	if(dataRangeId!=null && !dataRangeId.equals(""))
    		sql.append(" and M.MDataRgType.dataRangeId="+dataRangeId);

    	try{		  
    		conn=new DBConn();		  
    		session = conn.openSession();
    		
    		Query query = session.createQuery(sql.toString());          
    		List list = query.list();
    		if (list != null) {        	
    			for (Iterator it = list.iterator(); it.hasNext();) {
    				MActuRep reportInPersistence = (MActuRep) it.next();
    				return reportInPersistence;              
    			}      
    		}	  
    	}catch(Exception ex){		  
    		ex.printStackTrace();	  
    	}finally{		
    		if(conn!=null)conn.closeSession();	  
    	}	 
    	return null;  
    }
    
    /***
     * ��ѯ������Ϣ
     * @param reportInForm
     * @param offset
     * @param limit
     * @return
     * @throws Exception
     */    
    public static List select(ReportInForm reportInForm, int offset, int limit,Operator operator)
    throws Exception {
        List retvals = null;
        DBConn conn = null;
        Session session = null;

        StringBuffer hql = new StringBuffer("from ReportIn ri");
        StringBuffer where = new StringBuffer("");

        if (reportInForm != null) 
        {
            // �����������ж�,�������Ʋ���Ϊ��
            if (reportInForm.getOrgId() != null
                    && !reportInForm.getOrgId().equals(""))
                where.append((where.toString().equals("") ? "" : " and ")
                        + "ri.orgId like like '%" + reportInForm.getOrgId()
                        + "%'");
            /**
             * ���ϻ����ͱ���Ȩ����Ϣ
             * @author Ҧ��
             * */
            if(operator.getOrgPopedomSQL()!=null && !operator.getOrgPopedomSQL().equals(""))
                where.append((where.toString().equals("") ? "" : " and ")
                        + "ri.orgId in("+operator.getOrgPopedomSQL()+")");
            if(operator.getChildRepPodedomSQL()!=null && !operator.getChildRepPodedomSQL().equals(""))
                where.append((where.toString().equals("") ? "" : " and ")
                        + "ri.MChildReport.comp_id.childRepId in("+operator.getChildRepPodedomSQL()+")");
        }

        try { // List���ϵĲ���
            // ��ʼ��
            hql.append((where.toString().equals("") ? "" : " where ")
                    + where.toString());

            // conn�����ʵ����
            conn = new DBConn();
            // �����ӿ�ʼ�Ự
            session = conn.openSession();
            // ��Ӽ�����Session
            // List list=session.find(hql.toString());
            Query query = session.createQuery(hql.toString());
            query.setFirstResult(offset).setMaxResults(limit);
            List list = query.list();

            if (list != null) {
                retvals = new ArrayList();
                // ѭ����ȡ���ݿ����������¼
                for (Iterator it = list.iterator(); it.hasNext();) {
                    ReportInForm reportInFormTemp = new ReportInForm();
                    ReportIn reportInPersistence = (ReportIn) it.next();
                    TranslatorUtil.copyPersistenceToVo(reportInPersistence,
                            reportInFormTemp);
                    retvals.add(reportInFormTemp);
                }
            }
        } catch (HibernateException he) {
            retvals = null;
            log.printStackTrace(he);
        } catch (Exception e) {
            retvals = null;
            log.printStackTrace(e);
        } finally {
            //������Ӵ��ڣ���Ͽ��������Ự������
            if (conn != null)
                conn.closeSession();
        }
        return retvals;
    }
    /**
     * ͨ������id��������һЩ������Ϣ
     * ****δ���****************************************8
     * @param reportInId
     * @return
     */
    public String getReportInfo(Integer reportInId)
    {
        
        String result = "";
        DBConn conn = null;
        Session session = null;
        
        conn = new DBConn();
        
        try 
        {
            session = conn.beginTransaction();
            ReportIn repIn = (ReportIn)session.load(ReportIn.class, reportInId);
            if(repIn!=null)
            {
                result += repIn.getOrgId();
                session.update(repIn);
                session.flush();
                result = "";
            }
        } 
        catch (Exception e) {
            result = "";
           log.printStackTrace(e);
        }
        finally{
            if(conn!=null)
                conn.closeSession();
        }
        
        return result;
    }
    /**
     * �ر������趨��ѯ
     * @author�ܷ���
     * @return ���ؼ�¼����
     */
    public static int getReportAgainSettingCount(ReportInForm form,Operator operator) {
		int result = 0;
		
		if (form==null) return result;
		
		StringBuffer hql = new StringBuffer("select count(*) from ReportIn a");
		StringBuffer where = new StringBuffer(
				" where a.checkFlag="
				+ Config.CHECK_FLAG_NO +" and a.forseReportAgainFlag!="
				+ Config.FORSE_REPORT_AGAIN_FLAG_1);
		
		String[] repname = null;
		if (!form.getRepName().equals("0"))
			repname = form.getRepName().split(",");
		if (form.getOrgName() != null && !form.getOrgName().equals("")) {
			String orgids = com.cbrc.org.adapter.StrutsMOrgDelegate.getOrgid(form.getOrgName());
			if(!orgids.equals(""))
				where.append((where.toString().equals("") ? "" : " and ")
					+ " a.orgId in(" +orgids+ ")");
		}
		if (repname!=null && !(repname.length<2)) {
			where.append((where.toString().equals("") ? "" : " and ")
					+ " a.MChildReport.comp_id.childRepId ='"
					+ repname[0] + "'");
			where.append((where.toString().equals("") ? "" : " and ")
					+ " a.MChildReport.comp_id.versionId ='"
					+ repname[1] + "'");
		}
		if (null != form.getChildRepId() && !form.getChildRepId().equals("0")
				&& !form.getChildRepId().equals(""))
			where.append((where.toString().equals("") ? "" : " and ")
					+ " a.orgId in("
					+ com.cbrc.org.adapter.StrutsMOrgDelegate.getOrgids(form
							.getChildRepId()) + ")");
		if (null != form.getStartDate() && !form.getStartDate().equals(""))
			where.append((where.toString().equals("") ? "" : " and ")
					+ " day(a.reportDate) >=day('" + form.getStartDate()+ "')");
		if (null != form.getEndDate() && !form.getEndDate().equals(""))
			where.append((where.toString().equals("") ? "" : " and ")
					+ "day(a.reportDate) <=day('" + form.getEndDate()+ "')");

        /**
         * ���ϻ����ͱ���Ȩ����Ϣ
         * @author Ҧ��
         * */
        if(operator == null) return 0;

        /**����Ȩ��*/
        if(operator.getOrgPopedomSQL()!=null && !operator.getOrgPopedomSQL().equals("")){
            where.append((where.toString().equals("") ? "" : " and ")
                    + " a.orgId in("+operator.getOrgPopedomSQL()+")");
        }else{
            return 0;
        }
        /**����Ȩ��*/
        if(operator.getChildRepPodedomSQL()!=null && !operator.getChildRepPodedomSQL().equals("")){
            where.append((where.toString().equals("") ? "" : " and ")
                    + " a.MChildReport.comp_id.childRepId in("+operator.getChildRepPodedomSQL()+")");
        }else{
            return 0;
        }
        
		if (!where.toString().equals("")) hql.append(where.toString());
		
		DBConn conn = null;
		Session session = null;
		
		try {
			conn = new DBConn();
			session = conn.openSession();
			Query query = session.createQuery(hql.toString());
			List list = query.list();

			if (list != null) {
				result = ((Integer) list.get(0)).intValue();
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
     * �����ر����ò�ѯ
     * @author�ܷ���
     * @return ���ؼ�¼
     */
    public static List getReportAgainSettingRecord(ReportInForm form,int offset,int limit,Operator operator){
    	ArrayList result=new ArrayList();
 	   if(null==form)return result;
 	   StringBuffer hql = new StringBuffer("from ReportIn a ");
 	  StringBuffer where = new StringBuffer(
				" where a.checkFlag="
						+ Config.CHECK_FLAG_NO +" and a.forseReportAgainFlag!="
							+ Config.FORSE_REPORT_AGAIN_FLAG_1);
		String[] repname = null;
		if (!form.getRepName().equals("0"))
			repname = form.getRepName().split(",");
		if (form.getOrgName() != null && !form.getOrgName().equals("")) {
			String orgids = com.cbrc.org.adapter.StrutsMOrgDelegate.getOrgid(form.getOrgName());
			where.append((where.toString().equals("") ? "" : " and ")
					+ " a.orgId in(" +orgids+ ")");
		}
		if (null != repname&&!(repname.length<2)) {
			where.append((where.toString().equals("") ? "" : " and ")
					+ " a.MChildReport.comp_id.childRepId ='"
					+ repname[0] + "'");
			where.append((where.toString().equals("") ? "" : " and ")
					+ " a.MChildReport.comp_id.versionId ='"
					+ repname[1] + "'");
		}
		if (null != form.getChildRepId() && !form.getChildRepId().equals("0")
				&& !form.getChildRepId().equals(""))
			where.append((where.toString().equals("") ? "" : " and ")
					+ " a.orgId in("
					+ com.cbrc.org.adapter.StrutsMOrgDelegate.getOrgids(form
							.getChildRepId()) + ")");
		if (null != form.getStartDate() && !form.getStartDate().equals(""))
			where.append((where.toString().equals("") ? "" : " and ")
					+ "day( a.reportDate) >=day('" + form.getStartDate()+ "')");
		if (null != form.getEndDate() && !form.getEndDate().equals(""))
			where.append((where.toString().equals("") ? "" : " and ")
					+ "day( a.reportDate )<=day('" + form.getEndDate()+"')");
		
        
        /**
         * ���ϻ����ͱ���Ȩ����Ϣ
         * @author Ҧ��
         * */
        if(operator == null)
            return result;
        /**����Ȩ��*/
        if(operator.getOrgPopedomSQL()!=null && !operator.getOrgPopedomSQL().equals(""))
            where.append((where.toString().equals("") ? "" : " and ")
                    + " a.orgId in("+operator.getOrgPopedomSQL()+")");
        else
            return result;
        /**����Ȩ��*/
        if(operator.getChildRepPodedomSQL()!=null && !operator.getChildRepPodedomSQL().equals(""))
            where.append((where.toString().equals("") ? "" : " and ")
                    + " a.MChildReport.comp_id.childRepId in("+operator.getChildRepPodedomSQL()+")");
        else
            return result;
        
        where.append(" order by a.reportDate desc ");
        
		if (!where.toString().equals(""))
			hql.append(where.toString());
 	      
        DBConn conn=null;                
        Session session=null;
 	    try{
 		   conn=new DBConn();
            session=conn.openSession();
            Query query=session.createQuery(hql.toString());  
            query.setFirstResult(offset).setMaxResults(limit);
            List list=query.list();

            if (list!=null)for(int i=0;i<list.size();i++){
            	ReportIn reportin=(ReportIn)list.get(i);
            	ReportInForm reportInForm=new ReportInForm();
            	TranslatorUtil.copyPersistenceToVo(reportin, reportInForm);
            	if(StrutsMOrgDelegate.selectOne(reportInForm.getOrgId())!=null)
    			    reportInForm.setOrgName(StrutsMOrgDelegate.selectOne(reportInForm.getOrgId()).getOrg_name ());
            	result.add(reportInForm);
            }   
 	   }catch(Exception e){
 		   e.printStackTrace();

        }finally{
            if(conn!=null) conn.closeSession();
        }   
 	   return result;
     }
    /**
     * �½��ر������趨
     * @author �ܷ���
     * @return true �½��ɹ���false�½�ʧ�ܣ�
     * @throws HibernateException 
     */
    public static boolean newForseReportAgainSetting (ReportInForm reportInForm) throws HibernateException{
    	boolean result=false;
    	if(reportInForm==null)return result;
    	DBConn conn = null;
		Session session = null;
		Transaction tx=null;
		try {
			conn = new DBConn();
			session = conn.openSession();
			tx=session.beginTransaction();
			ReportIn reportIn=(ReportIn)session.load(ReportIn.class,reportInForm.getRepInId());
			if(reportIn==null)return result;
			reportIn.setForseReportAgainFlag(Config.FORSE_REPORT_AGAIN_FLAG_1);
			ReportAgainSet reportAgainSet=new ReportAgainSet();
			reportAgainSet.setCause(reportInForm.getCause());
			reportAgainSet.setSetDate(new Date());
			reportAgainSet.setRepInId(reportIn.getRepInId());
			session.save(reportIn);
			session.save(reportAgainSet);
			tx.commit();
			TranslatorUtil.copyPersistenceToVo(reportIn, reportInForm);
			session.close();
			result=true;
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			result=false;

		} finally {
			if (conn != null)
				conn.closeSession();
		}
    	return result;
    }
    
    /**
     * ����repInId����laterReportDay�ļ�¼
     * ����
     */
    public static int selectLaterReportDay(Integer repInId)
    {
    	int laterRepDay=0;
    	DBConn conn=null;
    	Session session=null;
    	List resList=null;
    	
    	if(repInId!=null){
    		try{
    			conn=new DBConn();
    			session=conn.openSession();
    			
    			String hql="from ReportIn ri where 1=1";
    			hql+=" and ri.laterReportDay="+repInId;
    			
    			Query query=session.createQuery(hql.toString());
    			resList=query.list();
    			
    			if(resList!=null && resList.size()>0){
    				laterRepDay=((ReportIn)resList.get(0)).getLaterReportDay().intValue();
    			}
    		}catch(HibernateException he){
    			log.printStackTrace(he);
    		}catch(Exception e){
    			log.printStackTrace(e);
    		}finally{
    			if(conn!=null)conn.closeSession();
    		}
    	}
    	return laterRepDay; 
    }
    
    
    /**insert report_in��
     * 
     * @param repIn
     * @return
     */
    public static ReportIn insertReportIn(ReportIn repIn)
    {
    	DBConn conn=new DBConn();
    	Session session=conn.beginTransaction();
    	try {
			session.save(repIn);
			conn.endTransaction(true);
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			conn.endTransaction(false);
			e.printStackTrace();
		}finally{
			if(conn!=null){
				conn.closeSession();
			}
		}
    	return repIn;
    }
    
}


    





