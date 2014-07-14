/*
 * Created on 2005-12-13
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.cbrc.smis.system.cb;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import com.cbrc.smis.common.ConfigOncb;
import com.cbrc.smis.form.ReportInForm;

/**
 * @author cb
 * 
 * �������ڴ����嵥ʽ�ı���
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class InputDataByListing {

    /**
     * �÷��������嵥�౨������,ͨ�����벻ͬ���ӱ�������ѡ��ͬ��ʵ����
     * 
     * @param file
     * @param tableName
     * @param repInId
     * @throws Exception
     */
    public void controler(File file, String tableName, Integer repInId,
            String childRepId, String zipFileName, String xmlFileName)
            throws Exception {

        String className = this.getClassName(childRepId);

        ConductListing conductListing = (ConductListing) Class.forName(className).newInstance();

        conductListing.conductData(file, tableName, repInId, zipFileName,xmlFileName);

        if(ConfigOncb.FLOGSTOREPROCESS)        	
        	InputDataByStoreProcess.conductDetail(repInId);
    }

    /**
     * ����������ӱ�����ȥ��ȡ�����ļ��еĶ�Ӧֵ
     * 
     * @param childRepId
     * @return
     */
    public String getClassName(String childRepId) throws Exception {

        String className = null;

        File file = new File(ConfigOncb.CONFIGBYIMPLPATH);

        Properties props = new Properties();

        InputStream input = new FileInputStream(file);

        props.load(input);

        className = props.getProperty(childRepId);

        return className;
    }
    
    public void updateControler(File file, String tableName, Integer repInId,
            String childRepId, String zipFileName, String xmlFileName)
            throws Exception {

        String className = this.getClassName(childRepId);

        ConductListing conductListing = (ConductListing) Class.forName(className).newInstance();

        conductListing.updataReportData(file, tableName, repInId, zipFileName,xmlFileName);

//        if(ConfigOncb.FLOGSTOREPROCESS)        	
//        	InputDataByStoreProcess.conductDetail(repInId);
    }
    
    public ReportInForm getChecker(File file, String tableName, Integer repInId,
            String childRepId, String zipFileName, String xmlFileName)
            throws Exception {

        String className = this.getClassName(childRepId);

        ConductListing conductListing = (ConductListing) Class.forName(className).newInstance();

        return conductListing.getChecker(file, tableName, repInId, zipFileName,xmlFileName);

    }

}