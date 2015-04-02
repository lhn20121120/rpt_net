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
 * @author唐磊
 */
public class StrutsReportInAgainDelegate {
    private static FitechException log = new FitechException(
            StrutsReportInDelegate.class);
   
    /**
     * 获取实际数据报表信息
     * 
     * @author rds 
     * @serialData 2005-12-18
     * 
     * @param repInId Integer 实际数据报表ID
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
     * 根据前台页面的查询结果将用户选中更改的记录写审核标志为1
     * @author 唐磊
     * @param orgArray 传过来的机构id的string型数组变量
     * @param checkSign 传过来的用户选择的审核标志
     * @param reportInPersistence 持久化对象ReportIn
     * @param he HibernateException 有异常则捕捉抛出
     * @param e Exception  有异常则捕捉抛出              
     * @param result boolean型变量,更新成功返回true,否则返回false  
     */
    public static boolean update(com.cbrc.smis.form.ReportInForm reportInForm) throws Exception {
    	//更新标志
        boolean result=true;
        //创建连接
        DBConn conn=null;
        Session session=null;
        //reportform中是否有参数
        if (reportInForm==null){
        	return false;
        }
        
        try{
        	conn=new DBConn();
        	session=conn.beginTransaction();

        	//循环给持久化对象传递form对象的机构id数组的参数
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
        	
        	//异常的捕捉机制
        }catch(HibernateException he){
        	result=false;
        	log.printStackTrace(he);
        }catch(Exception e){
        	result=false;
        	log.printStackTrace(e);
        }
        finally{
        	//如果conn对象依然存在,则结束当前事务并断开连接
        	if (conn!=null)	conn.endTransaction(result);
        }
        //更新成功返回吧!!!
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
     * 该方法用于将一个实际子报表对象持久化到数据库中去
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

        reportIn.setMChildReport(mcr); //将子报表SET进实际报表对象中

        reportIn.setMCurr(mCurr); //将货币SET进实际报表对象中

        reportIn.setMRepRange(mrr); //将机构适用范围SET进实际报表对象中

        reportIn.setMDataRgType(mdrt); //将数据范围SET进实际报表对象中

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
        
        reportIn.setRepOutId(repOutId);   //外网数据记录ID
        
        reportIn.setReportDataWarehouseFlag(new Short("0"));  //初始化数据仓库的标志
        
        reportIn.setForseReportAgainFlag(new Short("0"));   //初始化数据重报填报标志
        
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

            String errorMessage = "错误发生在存储" + zipFileName + "包文件中的";

            errorMessage = errorMessage + xmlFileName + "文件";

            FitechLog.writeLog(Config.LOG_SYSTEM_SAVEDATA, ConfigOncb.HANDLER,errorMessage, "OVER");

            reportIn = null;

        } finally {

            if (session != null)

                dBConn.closeSession();
        }

        return reportIn;

    }

    /**
     * @author cb 对实际报表表单修改
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
     * 根据输入的参数来判断判断一个实际表单是否已经录入过了
     * 
     * @param childRepId
     *            子报表ID
     * @param versionId
     *            版本号
     * @param orgId
     *            机构ID
     * @param mCurr
     *            币种名
     * @param dataRangeId
     *            数据范围
     * @param year
     *            年份
     * @param term
     *            期数
     * @param times
     *            次数
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

        boolean isre = false; //默认不重复

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

                 System.out.println("不存在");

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
     * 取得需要转成xml的报表信息
     * @author 姚捷
     * @return List 包含需要转成xml的报表信息
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
     * 将报送数据仓库标志改变
     * @author 姚捷
     * @param 实际子报表id
     * @return 是否成功
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
     * 重报管理查询
     * @author曹发根
     * @return 返回记录数量
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
         * 加上机构和报表权限信息，没有权限则什么也查询不到
         * @author 姚捷
         * */
        if(operator ==null) return result;
        /**机构权限*/
        if(operator.getOrgPopedomSQL()!=null && !operator.getOrgPopedomSQL().equals("")){
        	String sql="select distinct pv.comp_id.orgId from MPurView pv where pv.comp_id.userGrpId in("+operator.getUserGrpIds() +")";
            where.append((where.toString().equals("") ? "" : " and ")
                    + " a.reportIn.orgId in("+sql+")");
        }else{
            return result;
        }
        /**报表权限*/
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
     * 重报管理查询
     * @author曹发根
     * @return 返回记录
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
         * 加上机构和报表权限信息
         * @author 姚捷
         * */
        if(operator==null)
            return result;
        /**机构权限*/
        if(operator.getOrgPopedomSQL()!=null && !operator.getOrgPopedomSQL().equals(""))
        {
        	String sql="select distinct pv.comp_id.orgId from MPurView pv where pv.comp_id.userGrpId in("+operator.getUserGrpIds() +")";
            where.append((where.toString().equals("") ? "" : " and ")
                    + " a.reportIn.orgId in("+sql+")");
        }
            else
            return result;
        /**报表权限*/
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
     * 验证新建强制重报提交的表单
     * @author 曹发根
     * @return true 信息填写正确；false 信息填写错误。
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
     * 新建强制重报
     * @author 曹发根
     * @return true 新建成功；false新建失败！
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
     * 根据条件查询报表数量
     * @param reportInForm
     * @return
     */
    public static int getRecordCount(ReportInForm reportInForm,Operator operator) {
        int count = 0;

        //连接对象和会话对象初始化
        DBConn conn = null;
        Session session = null;

        //	 查询条件HQL的生成
        StringBuffer hql = new StringBuffer("select count(*) from ReportIn ri");
        StringBuffer where = new StringBuffer("");

        if (reportInForm != null) {
            // 查找条件的判断,查找名称不可为空
            if (reportInForm.getOrgId() != null
                    && !reportInForm.getOrgId().equals(""))
                where.append((where.toString().equals("") ? "" : " or ")
                        + "mc.orgId like '%" + reportInForm.getOrgId() + "%'");
        }

        try { //List集合的操作
            //初始化
            hql.append((where.toString().equals("") ? "" : " where ")
                    + where.toString());
            /**
             * 加上机构和报表权限信息
             * @author 姚捷
             * */
            if(operator.getOrgPopedomSQL()!=null && !operator.getOrgPopedomSQL().equals(""))
                where.append((where.toString().equals("") ? "" : " and ")
                        + "ri.orgId in("+operator.getOrgPopedomSQL()+")");
            if(operator.getChildRepPodedomSQL()!=null && !operator.getChildRepPodedomSQL().equals(""))
                where.append((where.toString().equals("") ? "" : " and ")
                        + "ri.MChildReport.comp_id.childRepId in("+operator.getChildRepPodedomSQL()+")");
            
            //conn对象的实例化
            conn = new DBConn();
            //打开连接开始会话
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
            //如果连接存在，则断开，结束会话，返回
            if (conn != null)
                conn.closeSession();
        }
        return count;
    }
    
    /**
     * 疑是oracle语法(upper) 卞以刚 2011-12-22
     * 已使用hibernate 
     * 查询可以设定强制重报的报表记录数（审核通过的报表）
     * 
     * @param reportInForm 报表FormBean
     * @param operator 当前登录用户
     * @return int 报表记录数
     */
    public static int getRecordCountOfmanual(ReportInForm reportInForm,Operator operator) {    	
        int count = 0;
        DBConn conn = null;
        Session session = null;

        if(reportInForm == null || operator == null)
        	return count;
        
        try {         	
        	//查询条件HQL的生成
            StringBuffer hql = new StringBuffer("select count(*) from ReportIn ri WHERE " +
            		"ri.times=(select max(r.times) from ReportIn r where r.MChildReport.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and " + 
            		"r.MChildReport.comp_id.versionId=ri.MChildReport.comp_id.versionId and r.MDataRgType.dataRangeId=ri.MDataRgType.dataRangeId and " +
            		"r.orgId=ri.orgId and r.MCurr.curId=ri.MCurr.curId and r.year=ri.year and r.term=ri.term and r.checkFlag=ri.checkFlag)");
            
            /**查询报表状态为审核通过报表*/
			StringBuffer where = new StringBuffer(" and ri.checkFlag=" + com.fitech.net.config.Config.CHECK_FLAG_PASS);
			
			/**添加报表编号查询条件（忽略大小写模糊查询）*/
			if(reportInForm.getChildRepId() != null && !reportInForm.getChildRepId().equals("")){				
				where.append(" and upper(ri.MChildReport.comp_id.childRepId) like upper('%" 
						+ reportInForm.getChildRepId().trim() + "%')");
			}
			/**添加报表名称查询条件（模糊查询）*/
			if(reportInForm.getRepName() != null && !reportInForm.getRepName().equals("")){
				where.append(" and ri.repName like '%" + reportInForm.getRepName().trim() + "%'");
			}
			/**添加模板类型（全部/法人/分支）查询条件*/
			if(reportInForm.getFrOrFzType() != null && !reportInForm.getFrOrFzType().equals(Config.DEFAULT_VALUE)){
				where.append(" and ri.MChildReport.frOrFzType='" + reportInForm.getFrOrFzType() + "'");
			}
			/**添加报送频度（月/季/半年/年）查询条件*/
			if(reportInForm.getRepFreqId() != null && !String.valueOf(reportInForm.getRepFreqId()).equals(Config.DEFAULT_VALUE)){
				where.append(" and ri.MDataRgType.dataRangeId in (select M.comp_id.dataRangeId from MActuRep M " 
						+ "where M.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and M.comp_id.versionId=ri.MChildReport.comp_id.versionId " 
						+ "and M.comp_id.repFreqId=" + reportInForm.getRepFreqId() + ")");
			}
			/**添加日期（年份）查询条件*/
			if(reportInForm.getYear() != null){
				where.append(" and ri.year=" + reportInForm.getYear());
			}
			/**添加日期（月份）查询条件*/
			if(reportInForm.getTerm() != null){
				where.append(" and ri.term=" + reportInForm.getTerm());
			}
			/**添加机构查询条件*/
			if(!StringUtil.isEmpty(reportInForm.getOrgId()) && !reportInForm.getOrgId().equals(Config.DEFAULT_VALUE)){
				where.append(" and ri.orgId='" + reportInForm.getOrgId().trim() + "'");
			}
			
			/**添加报表审核权限（强制重报权限与审核权限一致）
			 * 已增加数据库判断*/
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
        	//关闭数据库连接
            if (conn != null) 
            	conn.closeSession();
        }        
        return count;
    }
    
    /**
     * 疑是oracle语法(upper) 卞以刚 2011-12-22
     * 已使用hibernate 
     * 查询可以设定强制重报的报表记录（审核通过的报表）
     * 
     * @param reportInForm 报表FormBean
     * @param offset 偏移量
     * @param limit 每页显示记录数
     * @param operator 当前登录用户
     * @return List 允许设定强制重报的报表记录结果集
     */
    public static List selectOfManual(ReportInForm reportInForm, int offset, int limit,Operator operator){
        List resList = null;
        DBConn conn = null;
        Session session = null;
        
        if(reportInForm == null | operator == null)
        	return resList;

		try {
			//查询条件HQL的生成
            StringBuffer hql = new StringBuffer("from ReportIn ri WHERE " +
            		"ri.times=(select max(r.times) from ReportIn r where r.MChildReport.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and " + 
            		"r.MChildReport.comp_id.versionId=ri.MChildReport.comp_id.versionId and r.MDataRgType.dataRangeId=ri.MDataRgType.dataRangeId and " +
            		"r.orgId=ri.orgId and r.MCurr.curId=ri.MCurr.curId and r.year=ri.year and r.term=ri.term and r.checkFlag=ri.checkFlag)");
            
            /**查询报表状态为审核通过报表*/
			StringBuffer where = new StringBuffer(" and ri.checkFlag=" + com.fitech.net.config.Config.CHECK_FLAG_PASS);
			
			/**添加报表编号查询条件（忽略大小写模糊查询）*/
			if(reportInForm.getChildRepId() != null && !reportInForm.getChildRepId().equals("")){				
				where.append(" and upper(ri.MChildReport.comp_id.childRepId) like upper('%" 
						+ reportInForm.getChildRepId().trim() + "%')");
			}
			/**添加报表名称查询条件（模糊查询）*/
			if(reportInForm.getRepName() != null && !reportInForm.getRepName().equals("")){
				where.append(" and ri.repName like '%" + reportInForm.getRepName().trim() + "%'");
			}
			/**添加模板类型（全部/法人/分支）查询条件*/
			if(reportInForm.getFrOrFzType() != null && !reportInForm.getFrOrFzType().equals(Config.DEFAULT_VALUE)){
				where.append(" and ri.MChildReport.frOrFzType='" + reportInForm.getFrOrFzType() + "'");
			}
			/**添加报送频度（月/季/半年/年）查询条件*/
			if(reportInForm.getRepFreqId() != null && !String.valueOf(reportInForm.getRepFreqId()).equals(Config.DEFAULT_VALUE)){
				where.append(" and ri.MDataRgType.dataRangeId in (select M.comp_id.dataRangeId from MActuRep M " 
						+ "where M.comp_id.childRepId=ri.MChildReport.comp_id.childRepId and M.comp_id.versionId=ri.MChildReport.comp_id.versionId " 
						+ "and M.comp_id.repFreqId=" + reportInForm.getRepFreqId() + ")");
			}
			/**添加日期（年份）查询条件*/
			if(reportInForm.getYear() != null){
				where.append(" and ri.year=" + reportInForm.getYear());
			}
			/**添加日期（月份）查询条件*/
			if(reportInForm.getTerm() != null){
				where.append(" and ri.term=" + reportInForm.getTerm());
			}
			/**添加机构查询条件*/
			if(!StringUtil.isEmpty(reportInForm.getOrgId())  && !reportInForm.getOrgId().equals(Config.DEFAULT_VALUE)){
				where.append(" and ri.orgId='" + reportInForm.getOrgId().trim() + "'");
			}
			
			/**添加报表审核权限（强制重报权限与审核权限一致）
			 * 已增加数据库判断*/
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
                    //设置报表审核状态
                    aditing.setCheckFlag(reportInRecord.getCheckFlag());
                    //设置报送机构名称                    
                    orgNet = StrutsOrgNetDelegate.selectOne(reportInRecord.getOrgId());
                    if(orgNet != null) aditing.setOrgName(orgNet.getOrgName());                   
                    //设置报表ID标识符
                    aditing.setRepInId(reportInRecord.getRepInId());
                    //设置报表年份
                    aditing.setYear(reportInRecord.getYear());
                    //设置报表期数
                    aditing.setTerm(reportInRecord.getTerm());
                    //设置报表名称
                    aditing.setRepName(reportInRecord.getRepName());
                    //设置报表报送日期
                    aditing.setReportDate(reportInRecord.getReportDate());
                    //设置报表编号
                    aditing.setChildRepId(reportInRecord.getMChildReport().getComp_id().getChildRepId());
                    //设置报表版本号
                    aditing.setVersionId(reportInRecord.getMChildReport().getComp_id().getVersionId());
                    //设置报表币种名称
                    aditing.setCurrName(reportInRecord.getMCurr().getCurName());
                    //设置异常变化标志
                    aditing.setAbmormityChangeFlag(reportInRecord.getAbmormityChangeFlag());             
                    MActuRep mActuRep = GetFreR(reportInRecord);
					if (mActuRep != null){
						//设置报送口径
						aditing.setDataRgTypeName(mActuRep.getMDataRgType().getDataRgDesc());
						//设置报送频度
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
            //如果连接存在，则断开，结束会话，返回
            if (conn != null)
                conn.closeSession();
        }
        return resList;
    }
    
    /**
     * 重报查找审核通过的报表
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
			//机构的条件判断
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
			//开始时间的条件判断
			if (reportInForm.getStartDate() != null && !reportInForm.getStartDate().equals("")) {
				where.append(" and ri.reportDate>='"+ reportInForm.getStartDate()+"'");
			}			
			//结束时间的判断
			if (reportInForm.getEndDate() != null && !reportInForm.getEndDate().equals("")) {
				where.append(" and ri.reportDate<='" + reportInForm.getEndDate()+"'");
			}
        	//报表名称的判断
			if(reportInForm.getRepName()!=null&& !reportInForm.getRepName().equals("")){
				where.append(" and ri.repName like '%"+reportInForm.getRepName()+"%'");				
			}
        	
			/**加上报表审核权限（强制重报权限与审核权限一致）*/
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
            //如果连接存在，则断开，结束会话，返回
            if (conn != null)
                conn.closeSession();
        }
        
        return retvals;
    }
    
    /**
     * 根据报表住处信息返回该报表的频度信息
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
     * 查询报表信息
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
            // 查找条件的判断,查找名称不可为空
            if (reportInForm.getOrgId() != null
                    && !reportInForm.getOrgId().equals(""))
                where.append((where.toString().equals("") ? "" : " and ")
                        + "ri.orgId like like '%" + reportInForm.getOrgId()
                        + "%'");
            /**
             * 加上机构和报表权限信息
             * @author 姚捷
             * */
            if(operator.getOrgPopedomSQL()!=null && !operator.getOrgPopedomSQL().equals(""))
                where.append((where.toString().equals("") ? "" : " and ")
                        + "ri.orgId in("+operator.getOrgPopedomSQL()+")");
            if(operator.getChildRepPodedomSQL()!=null && !operator.getChildRepPodedomSQL().equals(""))
                where.append((where.toString().equals("") ? "" : " and ")
                        + "ri.MChildReport.comp_id.childRepId in("+operator.getChildRepPodedomSQL()+")");
        }

        try { // List集合的操作
            // 初始化
            hql.append((where.toString().equals("") ? "" : " where ")
                    + where.toString());

            // conn对象的实例化
            conn = new DBConn();
            // 打开连接开始会话
            session = conn.openSession();
            // 添加集合至Session
            // List list=session.find(hql.toString());
            Query query = session.createQuery(hql.toString());
            query.setFirstResult(offset).setMaxResults(limit);
            List list = query.list();

            if (list != null) {
                retvals = new ArrayList();
                // 循环读取数据库符合条件记录
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
            //如果连接存在，则断开，结束会话，返回
            if (conn != null)
                conn.closeSession();
        }
        return retvals;
    }
    /**
     * 通过报表id查找它的一些基本信息
     * ****未完成****************************************8
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
     * 重报管理设定查询
     * @author曹发根
     * @return 返回记录数量
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
         * 加上机构和报表权限信息
         * @author 姚捷
         * */
        if(operator == null) return 0;

        /**机构权限*/
        if(operator.getOrgPopedomSQL()!=null && !operator.getOrgPopedomSQL().equals("")){
            where.append((where.toString().equals("") ? "" : " and ")
                    + " a.orgId in("+operator.getOrgPopedomSQL()+")");
        }else{
            return 0;
        }
        /**报表权限*/
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
     * 报表重报设置查询
     * @author曹发根
     * @return 返回记录
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
         * 加上机构和报表权限信息
         * @author 姚捷
         * */
        if(operator == null)
            return result;
        /**机构权限*/
        if(operator.getOrgPopedomSQL()!=null && !operator.getOrgPopedomSQL().equals(""))
            where.append((where.toString().equals("") ? "" : " and ")
                    + " a.orgId in("+operator.getOrgPopedomSQL()+")");
        else
            return result;
        /**报表权限*/
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
     * 新建重报报表设定
     * @author 曹发根
     * @return true 新建成功；false新建失败！
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
     * 根据repInId查找laterReportDay的记录
     * 唐磊
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
    
    
    /**insert report_in表
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


    





