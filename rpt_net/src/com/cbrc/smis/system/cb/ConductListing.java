/*
 * Created on 2005-12-15
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.cbrc.smis.system.cb;

import java.io.File;

import com.cbrc.smis.form.ReportInForm;

/**
 * @author cb
 * 
 * �ýӿ����ڶ����е��嵥ʽ������д��� 
 * 
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author cb
 * �÷���������InputDataByListing�е�control������
 * 
 */
public interface ConductListing {
    
    public void conductData(File file, String tableName, Integer repInId,String zipFileName,String xmlFileName) throws Exception;
    
    public void updataReportData (File file, String tableName, Integer repInId,String zipFileName,String xmlFileName) throws Exception;
    
    public ReportInForm getChecker (File file, String tableName, Integer repInId,String zipFileName,String xmlFileName) throws Exception;

}
