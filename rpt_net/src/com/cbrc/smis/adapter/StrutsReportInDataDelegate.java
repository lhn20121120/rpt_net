/*
 * Created on 2005-12-17
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.cbrc.smis.adapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.util.List;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.hibernate.ReportInData;
import com.cbrc.smis.util.FitechException;

/**
 * @author cb
 * 
 * 该类封装了一些对持久化对象ReportInData操作
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */

public class StrutsReportInDataDelegate {
	private static FitechException log=new FitechException(StrutsReportInDataDelegate.class);

    /**
     * 该方法用于将一个XML文件存入数据库
     * 
     * @param file   XML文件名
     * @param repInId  实际表单对象的主键
     * @throws Exception
     */
    public static void insertReortInData(File file, Integer repInId) throws Exception {

        ReportInData reportInData = new ReportInData();

        InputStream input = new FileInputStream(file);

        int size = input.available();

        /*byte[] buffer = new byte[size];*/

       /* Blob xmlBlob = Hibernate.createBlob(buffer);*/
        Blob xmlBlob = Hibernate.createBlob(input);
        reportInData.setRepInId(repInId);
        reportInData.setXml(xmlBlob);
        reportInData.setXmlSize(new Integer(size));

        DBConn dBConn = null;

        Session session = null;

        try {

            dBConn = new DBConn();

            session = dBConn.beginTransaction();

            session.save(reportInData);

            session.flush();

          /*  reportInData.setXml(xmlBlob);

            session.update(reportInData);//考虑到BLOB的特殊性,所以是先存后更新
*/
            dBConn.endTransaction(true);

        } catch (Exception e) {
        	log.printStackTrace(e);
            dBConn.endTransaction(false);
        }
        finally{
        	if(input !=null)
        		input.close();
            if(session!=null)
                
                session.close();
        }

    }
    
    /**
     * 根据实际数据报表ID获取报表的XML数据信息
     * 
     * @author rds
     * @serialData 2005-12-18 11:36
     * 
     * @param repInId Integer 实际数据报表ID
     * @param ReportInData 
     */
    public static ReportInData getReportInData(Integer repInId){
    	ReportInData reportInData=null;
    	
    	if(repInId==null) return reportInData;
    	
    	DBConn conn=null;
    	
    	try{
    		conn=new DBConn();
    		reportInData=(ReportInData)conn.openSession().get(ReportInData.class,repInId);
    		
    	}catch(HibernateException he){
    		log.printStackTrace(he);
    		reportInData=null;
    	}catch(Exception e){
    		log.printStackTrace(e);
    		reportInData=null;
    	}finally{
    		if(conn!=null) conn.closeSession();
    	}
    	
    	return reportInData;
    }
    
    public static List getBlobsByIds(String ids)
    {
    	DBConn conn=new DBConn();
    	Session session=conn.openSession();
    	List blobList=null;
    	try {
			blobList=session.find("select rid.xml from ReportInData rid where rid.repInId in("+ids+")");
			conn.closeSession();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			if(conn != null) conn.closeSession();
		}
    	
    	return blobList;
    }
    
    public static void insertReportInData(ReportInData rid)
    {
    	DBConn conn=new DBConn();
    	Session session=conn.beginTransaction();
    	try {
			session.save(rid);
			conn.endTransaction(true);
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			conn.endTransaction(false);
			e.printStackTrace();
		}
    }
    
    /**
     * 编辑报表，保存XML文件
     * @param file
     * @param repInId
     * @throws Exception
     */
    public static void updateReortInData(File file, Integer repInId) throws Exception {

        ReportInData reportInData = new ReportInData();
        InputStream input = new FileInputStream(file);
        int size = input.available();

        Blob xmlBlob = Hibernate.createBlob(input);
        reportInData.setRepInId(repInId);
        reportInData.setXml(xmlBlob);
        reportInData.setXmlSize(new Integer(size));

        DBConn dBConn = null;
        Session session = null;
        try {
            dBConn = new DBConn();
            session = dBConn.beginTransaction();
            session.update(reportInData);
            session.flush();

            dBConn.endTransaction(true);

        } catch (Exception e) {
        	log.printStackTrace(e);
            dBConn.endTransaction(false);
        }finally{
        	if(input!=null)
        		input.close();
            if(session!=null)         
                session.close();
        }

    }
}