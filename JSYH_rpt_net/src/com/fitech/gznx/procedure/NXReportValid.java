package com.fitech.gznx.procedure;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.hibernate.Session;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.proc.impl.Expression;
import com.cbrc.smis.proc.impl.LogInImpl;
import com.cbrc.smis.proc.impl.Report;
import com.cbrc.smis.proc.impl.ReportDDImpl;
import com.cbrc.smis.proc.impl.ReportImpl;
import com.cbrc.smis.proc.po.DataValidateInfo;
import com.cbrc.smis.proc.po.MCellFormu;
import com.cbrc.smis.proc.po.ReportIn;
import com.fitech.gznx.po.AfDatavalidateinfo;
import com.fitech.gznx.po.AfDatavalidateinfoId;
import com.fitech.gznx.po.AfReport;
import com.fitech.gznx.po.AfValidateformula;
import com.fitech.gznx.service.AFDataValidateInfoDelegate;
import com.fitech.gznx.service.AFReportDelegate;
import com.fitech.gznx.service.AFValidateFormulaDelegate;

/**
 * ũ�Ų���У��
 * 
 * @author Dennis Yee
 *
 */
public class NXReportValid {
	
    /**
     * ��oracle�﷨(nextval) ��Ҫ�޸�
     * ���Ը� 2011-12-26
     * ���������ݿ��ж�
     * ���޸� ���sqlserver���ݿ�sql��� ���Ը� ������ 2011-12-26
     * ����У��
     * @param repInId       Integer     ����ʵ���ӱ���Id
     * @param formuType     int         ��������
     * @return ��boolean     
     * @throws Exception
     *  ��֤ͨ������true   ʧ�ܷ���false
     */
    public static boolean processValid(Integer repInId, int formuType, String userName, Integer templateType)
            throws Exception {
    	/***
    	 * ��oracle�﷨(nextval) ��Ҫ�޸�
    	 * ���Ը� 2011-12-26
    	 * ���������ݿ��ж�
    	 * ���޸� ���sqlserver���ݿ�sql��� ���Ը� ������ 2011-12-26
    	 */
        return repInId == null ? false : valid(repInId.intValue(), formuType, userName, templateType);
    }

    /**
     * ��oracle�﷨(nextval) ��Ҫ�޸�
     * ���Ը� 2011-12-26
     * ���������ݿ��ж�
     * ���޸� ���sqlserver���ݿ�sql��� ���Ը� ������ 2011-12-26
     * �ϱ����ݱ�䣬����У���Ƿ�ͨ��
     * @param   repInId         int         ����ʵ���ӱ���ID
     * @param   formuType       int         У������
     * @return boolean
     * @throws SQLException
     * @throws Exception
     * ��֤ͨ������true   ʧ�ܷ���false
     */
    private static boolean valid(Integer repInId, int formuType, String userName, Integer templateType)
            throws SQLException, Exception
    {
        // У������б�
        List dataValidateInfoList = null;

        Connection orclCon = null;
        // д�������У���ʶ
        Integer validFlag = Report.RESULT_OK;
        String flagName = setFlagName(formuType);

        try
        {
        	orclCon = (new com.cbrc.smis.proc.jdbc.FitechConnection()).getConnect();
            // ��ȡ�������
        	/**��ʹ��hibernate ���Ը� 2011-12-21
        	 * Ӱ�����AfReport**/
            AfReport reportIn = AFReportDelegate.getReportIn(repInId.longValue());
            
            if(orclCon.getAutoCommit())
                orclCon.setAutoCommit(false);
            
            // �ж�������Ƿ����
            if (reportIn == null)
            {
            	/****
            	 *jdbc���� oracle�﷨(nextval) ��Ҫ�޸�
            	 * ���Ը� 2011-12-26
            	 * ���������ݿ��ж�
            	 * ���޸� ���sqlserver���ݿ�sql��� ���Ը� ������ 2011-12-26
            	 * Ӱ���log_in
            	 */
                LogInImpl.writeBNLog(orclCon, "����ʵ�����ݱ���ID[" + repInId
                        + "]��ȡ������Ϣʧ��!",userName);
                close(orclCon);
                return false;
            }

            /**jdbc���� �������﷨ ����Ҫ�޸�
             * Ӱ���AF_DATAVALIDATEINFO**/
            orclCon.createStatement().executeUpdate("delete from AF_DATAVALIDATEINFO where REP_ID="+repInId+" and VALIDATE_DESC="+formuType);
            
            // ������ʽ
            Integer reportStyle = reportIn.getTemplateStyle();
            
            // ���ֱ�仹�Ǳ��ڻ�ȡ�����ĵ�Ԫ��У����ʽ����
            /****
             * jdbc���� �������﷨ ����Ҫ�޸�
     	 	 * ���Ը� 2011-12-26
     	 	 * Ӱ���af_validateformula
             */
            List cellFormuList = AFValidateFormulaDelegate.getCellFormus(orclCon, reportIn, formuType);
            
            // �ϱ����ݵĵ�Ԫ�����ƣ��ֵ��ֵ�Լ���
            /****
             * jdbc���� �������﷨ ����Ҫ�޸�
             * ���Ը� 2011-12-26
             * Ӱ���af_pbocreportdata || af_otherreportdata
             */
            Map cellMap = ValidateP2PReport.parseCell(orclCon, reportIn, templateType);
            
            if (cellFormuList != null && !cellFormuList.isEmpty()) {
            	
                dataValidateInfoList = new ArrayList();
                
                // ��Ԫ��У����ʽ
              //  Iterator itr = cellFormuList.iterator();
                // ��Ե�ʽУ��
                if (reportStyle == Report.REPORT_STYLE_DD) {
                	
                    Map validMap = null;    
                    //�����ĵ�Ԫ�񼯺�û������
                    if(cellMap == null || cellMap.isEmpty()) {
                        validFlag = Report.REPORT_CANCLE;
                    } else {
                        //��ȡ��֤���ʽMap����
                    	/***
                    	 * jdbc����
                    	 * ������oracle�﷨ ����Ҫ�޸� 
                    	 * ���Ը� 2011-12-26
                    	 */
                        validMap = ExpressionHandle.valid(orclCon, reportIn, cellFormuList, cellMap, templateType);
                    }
                    
                    if (validMap != null && !validMap.isEmpty()) {
                    	
                        // ����ж�Ӧ�ı�䱨��δ������Ϣ
                        Iterator itor = validMap.keySet().iterator();
                        // ����������֤���
                        while (itor.hasNext()) {
                            AfValidateformula mCellFormu = (AfValidateformula) itor.next();
                            Boolean pass = (Boolean) validMap.get(mCellFormu);
                            // ���û��ͨ����֤
                            if (pass != null && !pass.booleanValue()) {
                            	
                                AfDatavalidateinfo dvi = createDataValidateInfo(
                                        reportIn, mCellFormu, true, formuType);
                                
                                // ����Ӧ�ı��������ϱ�
                                if (Report.VALIDATE_TYPE_BJ == formuType
                                        && mCellFormu.getFormulaValue().indexOf("null") != -1) 
                                    validFlag = Report.REPORT_CANCLE;
                                
                                validFlag = Report.RESULT_NO;
                                
                                dataValidateInfoList.add(dvi);
                            }
                        }
                        validMap.clear();
                        cellFormuList.clear();
                    }
                }


                /** ���У�������Ϣ��Ϊ�գ�����У������������* */
                if (dataValidateInfoList != null
                        && dataValidateInfoList.size() > 0) {
                	
                    String operation = "���ݱ���[���:" + reportIn.getTemplateId()
                            + "," + "�汾��:" + reportIn.getVersionId() + ","
//                            + "���ݷ�Χ:" + reportIn.getDataRangeId() + ","
                            + "����ID:" + reportIn.getOrgId() + "," + "���ҵ�λ:"
                            + reportIn.getCurId() + "," + "����::"
                            + reportIn.getYear() + "-" 
                            + reportIn.getTerm() + "-" 
                            + reportIn.getDay() + "]";
                    
                    //����У����Ϣ�Ĵ���
                    if (Report.VALIDATE_TYPE_BN == formuType) {
                    	/***
                    	 * jdbc���� ������oracle�﷨
                    	 * ����Ҫ�޸� ���Ը� 2011-12-26
                    	 */
                        if (AFDataValidateInfoDelegate.writeDataValidateInfo(orclCon,repInId,
                                dataValidateInfoList,formuType, reportStyle)) {
                            // д��־
                        	/***
                        	 * jdbc���� oracle�﷨(nextval) ��Ҫ�޸�
                        	 * ���Ը� 2011-12-26
                        	 */
                            LogInImpl.writeBNLog(orclCon, operation
                                    + "����У��"+ (validFlag.equals(Report.RESULT_NO) ? "δ"
                                            : "") + "ͨ��!",userName);
                        } else {
                        	/***
                        	 * jdbc���� oracle�﷨(nextval) ��Ҫ�޸�
                        	 * ���Ը� 2011-12-26
                        	 */
                            LogInImpl.writeBNLog(orclCon, operation + "����У��"
                                    + "����ʧ��!",userName);
                        }
                    }
                   
                    //���У����Ϣ�Ĵ���
                    if (Report.VALIDATE_TYPE_BJ == formuType) {
//                        String msg = "У��δͨ��";
//                        if (AFDataValidateInfoDelegate.writeBJDataValidateInfo(orclCon,
//                                dataValidateInfoList,formuType, reportStyle)) {
//                            msg = "У��ͨ��";
//                        }
                        String msg = "";
                        /***
                         * jdbc���� oracle�﷨(nextval) ��Ҫ�޸�
                         * ���Ը� 2011-12-26
                         */
                        if (AFDataValidateInfoDelegate.writeDataValidateInfo(orclCon,repInId,
                                dataValidateInfoList,formuType, reportStyle)) {
                            msg = "У��δͨ��";
                        } else {
                        	msg = "У��ʧ��";
                        }
                        /***
                         * jdbc���� oracle�﷨(nextval) ��Ҫ�޸�
                         * ���Ը� 2011-12-26
                         */
                        LogInImpl.writeBJLog(orclCon, operation + msg, userName);
                    }
                }
               //����af_report���еı������У���־
                /***
                 * jdbc���� ������oracle�﷨ ����Ҫ�޸�
                 * ���Ը� 2011-12-26
                 */
               AFReportDelegate.updateFlag(orclCon, repInId, flagName, validFlag.intValue());
               
            } else {
            	
                // ��ȡУ���ϵ���ʽʧ��
                if (Report.FORMU_TYPE_BN == formuType) {
                	/***
                	 * jdbc���� oracle�﷨(nextval) ��Ҫ�޸�
                	 * ���Ը� 2011-12-26
                	 */
                	LogInImpl.writeBNLog(
                        orclCon, "����[���:" + reportIn.getTemplateId() 
                        + ",�汾��:" + reportIn.getVersionId() 
                        + "]���ޱ���У���ϵ���ʽ!",userName);
                	/**
                	 * jdbc���� ������oracle ����Ҫ�޸� 
                	 * 2011-12-26 ���Ը�
                	 * **/
                	AFReportDelegate.updateFlag(orclCon, repInId, flagName, validFlag.intValue());
                }
                // ���ڱ��У�����У�鹫ʽҲ�����У���־��Ϊͨ��
                if (Report.FORMU_TYPE_BJ == formuType) {
                	/***
                	 * jdbc���� oracle�﷨(nextval) ��Ҫ�޸�
                	 * ���Ը� 2011-12-26
                	 */
                	LogInImpl.writeBNLog(
                            orclCon, "����[���:" + reportIn.getTemplateId() 
                            + ",�汾��:" + reportIn.getVersionId() 
                            + "]���ޱ��У���ϵ���ʽ!",userName);
                	/***
                	 * jdbc���� ������oracle����Ҫ�޸�
                	 * ���Ը� 2011-12-26
                	 */
                	AFReportDelegate.updateFlag(orclCon, repInId, flagName, validFlag.intValue());
                }
            }
        }
        catch (SQLException sqle)
        {
       	    orclCon.rollback();
            sqle.printStackTrace();
            throw new Exception(sqle.getMessage());
        }
        catch (Exception e)
        {
            orclCon.rollback();
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
        finally
        {

            close(orclCon);
        }
        
        return validFlag == Report.RESULT_OK ? true : false;
    }

    /**
     * ����һ��DataValidateInfo
     * 
     * @param reportIn              ReportIn        ��������
     * @param mCellFormu            MCellFormu      ��Ԫ����ʽ
     * @param style_dd              boolean         �Ƿ��Ե�ʽ����
     * @param formuType             int             У������
     * @return DataValidateInfo
     */
    private static AfDatavalidateinfo createDataValidateInfo(AfReport reportIn,
            AfValidateformula mCellFormu, boolean style_dd, int formuType) {
    	
    	AfDatavalidateinfo dvi = new AfDatavalidateinfo();
    	AfDatavalidateinfoId dviId = new AfDatavalidateinfoId();
    	
    	dviId.setRepId(reportIn.getRepId());
    	dviId.setFormulaId(mCellFormu.getFormulaId());
    	
    	dvi.setId(dviId);

        dvi.setValidateFlg(Report.RESULT_NO.longValue());
        dvi.setValidateDesc(mCellFormu.getValidateTypeId().toString());
        dvi.setSourceValue(mCellFormu.getSource());
        dvi.setTargetValue(mCellFormu.getTarget());
        
        return dvi;
    }

    /**
     * ����У�����µ��ֶ���
     * 
     * @param formuType     String      У������
     * @return  String
     */
    private static String setFlagName(int formuType)
    {
        if (Report.VALIDATE_TYPE_BN == formuType) return "TBL_INNER_VALIDATE_FLAG";
        if (Report.VALIDATE_TYPE_BJ == formuType) return "TBL_OUTER_VALIDATE_FLAG";
        return null;
    }

    /**
     * �ر����ݿ�����
     * 
     * @param conn Connection
     * @return void
     * @exception Exception
     */
    private static void close(Connection conn) throws Exception
    {
        if (conn != null)
        {
            try
            {
                // 2007-09-29�����޸�
                if (!conn.getAutoCommit()) conn.commit();
                if (conn != null)
                // ///////////////
                conn.close();
            }
            catch (SQLException sqle)
            {
                throw new Exception(sqle.getMessage());
            }
        }
    }
}
