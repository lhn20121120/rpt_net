package com.cbrc.smis.proc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.proc.impl.Expression;
import com.cbrc.smis.proc.impl.LogInImpl;
import com.cbrc.smis.proc.impl.Report;
import com.cbrc.smis.proc.impl.ReportDDImpl;
import com.cbrc.smis.proc.impl.ReportImpl;
import com.cbrc.smis.proc.po.DataValidateInfo;
import com.cbrc.smis.proc.po.MCellFormu;
import com.cbrc.smis.proc.po.ReportIn;
/**
*
* <p>Title: ReportValid </p>
*
* <p>Description �Ա������ݣ�������ݽ��кϷ���У��ķ���������</p>
*
* <p>Copyright: Copyright (c) 2007</p>
*
* <p>Company: </p>
*
* @author   ����
* @date     2009-09-30
* @version 1.0
*/
public class ReportValid
{

    public ReportValid()
    {
    }

    /**ʹ��jdbc ���ܲ���Ҫ�޸�  ���Ը� 2011-12-21**/
    /**
     * 
     * ����У��
     * @param repInId       Integer     ����ʵ���ӱ���Id
     * @param formuType     int         ��������
     * @return ��boolean     
     * @throws Exception
     *  ��֤ͨ������true   ʧ�ܷ���false
     */
    public static boolean processValid(Integer repInId, int formuType,String userName)
            throws Exception
    {
    	/**ʹ��jdbc ���ܲ���Ҫ�޸�  ���Ը� 2011-12-21**/
        return repInId == null ? false : valid(repInId.intValue(), formuType,userName);
    }

    /**
     * ʹ��jdbc ���ܲ���Ҫ�޸�  ���Ը� 2011-12-21
     * �ϱ����ݱ�䣬����У���Ƿ�ͨ��(�ο�ԭ���ߴ��������ָĶ�)
     * 
     * @author gongming
     * @date 2009-09-24
     * @param   repInId         int         ����ʵ���ӱ���ID
     * @param   formuType       int         У������
     * @return boolean
     * @throws SQLException
     * @throws Exception
     * ��֤ͨ������true   ʧ�ܷ���false
     */
    private static boolean valid(int repInId, int formuType,String userName)
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
            /**ʹ��jdbc ���ܲ���Ҫ�޸�  ���Ը� 2011-12-21**/
            ReportIn reportIn = ReportImpl.getReportIn(orclCon, repInId);
            if(orclCon.getAutoCommit())
                orclCon.setAutoCommit(false);
            
            // �ж�������Ƿ����
            if (reportIn == null)
            {
                LogInImpl.writeBNLog(orclCon, "����ʵ�����ݱ���ID[" + repInId
                        + "]��ȡ������Ϣʧ��!",userName);
                close(orclCon);
                return false;
            }
            if(formuType==2){
            	if( (reportIn.getChildRepId().trim().equals("G3301") || reportIn.getChildRepId().trim().equals("G3302")))	{
            		if(!reportIn.getCurId().equals(new Integer(1))){
            			/**ʹ��jdbc ���ܲ���Ҫ�޸�  ���Ը� 2011-12-21**/
            			ReportImpl.updateFlag(
			                        orclCon, repInId, flagName, validFlag.intValue());
            			return true;
            		}
            	}
            }
            
            /**ʹ��jdbc ���ܲ���Ҫ�޸�  ���Ը� 2011-12-21**/
            orclCon.createStatement().executeUpdate("delete from DATA_VALIDATE_INFO where REP_IN_ID="+repInId+" and VALIDATE_TYPE_ID="+formuType);
            
            // ������ʽ
            int reportStyle = reportIn.getReportStyle().intValue();
            // ���ֱ�仹�Ǳ��ڻ�ȡ�����ĵ�Ԫ��У����ʽ����
            /**ʹ��jdbc ���ܲ���Ҫ�޸�  ���Ը� 2011-12-21**/
            List cellFormuList = ReportImpl.getCellFormus(orclCon, reportIn, formuType);
            
            // �ϱ����ݵĵ�Ԫ�����ƣ��ֵ��ֵ�Լ���
            /**ʹ��jdbc ���ܲ���Ҫ�޸�  ���Ը� 2011-12-21**/
            Map cellMap = ReportDDImpl.parseCell(orclCon, reportIn);

           
            if (cellFormuList != null && !cellFormuList.isEmpty())
            {
                dataValidateInfoList = new ArrayList();
                // ��Ԫ��У����ʽ
                Iterator itr = cellFormuList.iterator();
                if (reportStyle == Report.REPORT_STYLE_DD)
                {
                    Map validMap = null;    
                    //�����ĵ�Ԫ�񼯺�û������                    
                    if(cellMap == null || cellMap.isEmpty())
                    {
                        validFlag = Report.REPORT_CANCLE;
                    }   
                    else
                    {
                        //��ȡ��֤���ʽMap����
                        validMap = Expression.valid(orclCon, reportIn,
                                cellFormuList, cellMap);
                    }
                    
                    /**(
                     * �˴�����֤���
                     */
                    if (validMap != null && !validMap.isEmpty())
                    {
                        // ����ж�Ӧ�ı�䱨��δ������Ϣ
                        Iterator itor = validMap.keySet().iterator();
                        // ����������֤���
                        while (itor.hasNext())
                        {
                            MCellFormu mCellFormu = (MCellFormu) itor.next();
                            Boolean pass = (Boolean) validMap.get(mCellFormu);
//                            System.out.println(mCellFormu.getTarget()+":"+mCellFormu.getSource());
                            // ���û��ͨ����֤
                            if (pass != null && !pass.booleanValue())
                            {
                                DataValidateInfo dvi = createDataValidateInfo(
                                        reportIn, mCellFormu, true, formuType);
                                // ����Ӧ�ı��������ϱ�
                                if (Report.VALIDATE_TYPE_BJ == formuType
                                        && mCellFormu.getCellFormu().indexOf(
                                                "null") != -1) 
                                    validFlag = Report.REPORT_CANCLE;
                                validFlag = Report.RESULT_NO;
                                dataValidateInfoList.add(dvi);
                            }
                        }
                        validMap.clear();
                        cellFormuList.clear();
                    }
                }
                else
                {
                    while (itr.hasNext())
                    {
                        MCellFormu mCellFormu = (MCellFormu) itr.next();
                        if (!Expression.bnValid(orclCon, reportIn, mCellFormu,
                                cellMap))
                        {
                            DataValidateInfo dvi = createDataValidateInfo(
                                    reportIn, mCellFormu, false, formuType);
                            validFlag = Report.RESULT_NO;
                            dvi.setCause(Expression.getCause());
                            if (!Expression.getErrMsg().equals(""))
                            { 
                            	// ���У�鲻ͨ������Ϊϵͳ��������д����־
                                LogInImpl.writeBJLog(orclCon, Expression.getErrMsg(),userName);
                            }
                            dataValidateInfoList.add(dvi);
                        }
                    }
                }

                /** ���У�������Ϣ��Ϊ�գ�����У������������* */
                if (dataValidateInfoList != null
                        && dataValidateInfoList.size() > 0)
                {
                    String operation = "���ݱ���[���:" + reportIn.getChildRepId()
                            + "," + "�汾��:" + reportIn.getVersionId() + ","
                            + "���ݷ�Χ:" + reportIn.getDataRangeId() + ","
                            + "����ID:" + reportIn.getOrgId() + "," + "���ҵ�λ:"
                            + reportIn.getCurId() + "," + "���:"
                            + reportIn.getYear() + "," + "����:"
                            + reportIn.getTerm() + "]";
                    //����У����Ϣ�Ĵ���
                    if (Report.VALIDATE_TYPE_BN == formuType)
                    {
                        if (ReportImpl.writeDataValidateInfo(orclCon,repInId,
                                dataValidateInfoList, reportStyle))
                        {
                            // д��־
                            LogInImpl.writeBNLog(orclCon, operation
                                    + "����У��"+ (validFlag.equals(Report.RESULT_NO) ? "δ"
                                            : "") + "ͨ��!",userName);
                        }
                        else
                        {
                            LogInImpl.writeBNLog(orclCon, operation + "����У��"
                                    + "����ʧ��!",userName);
                        }
                    }
                   
                    //���У����Ϣ�Ĵ���
                    if (Report.VALIDATE_TYPE_BJ == formuType)
                    {
                        String msg = "У��δͨ��";
                        if (ReportImpl.writeBJDataValidateInfo(orclCon,
                                dataValidateInfoList, reportStyle))
                        {
                            msg = "У��ͨ��";
                        }
                        LogInImpl.writeBJLog(orclCon, operation + msg, userName);
                    }
                }
               //����report_in���еı������У���־
               ReportImpl.updateFlag(orclCon, repInId, flagName, validFlag.intValue());
            }
            else
            {
                // ��ȡУ���ϵ���ʽʧ��
                if (Report.FORMU_TYPE_BN == formuType) {
                	LogInImpl.writeBNLog(               
                        orclCon, "����[���:" + reportIn.getChildRepId() + ",�汾��:"
                                + reportIn.getVersionId() + "]���ޱ���У���ϵ���ʽ!",userName);
                	ReportImpl.updateFlag(
                            orclCon, repInId, flagName, validFlag.intValue());
                }
                // ���ڱ��У�����У�鹫ʽҲ�����У���־��Ϊͨ��
                if (Report.FORMU_TYPE_BJ == formuType) ReportImpl.updateFlag(
                        orclCon, repInId, flagName, validFlag.intValue());
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
     * û��sql�ַ�����ƴװ sql���������������ܲ���Ҫƴװ  ���Ը� 2011-12-21
     * ����һ��DataValidateInfo
     * 
     * @author gongming
     * @date 2007-09-25
     * @param reportIn              ReportIn        ��������
     * @param mCellFormu            MCellFormu      ��Ԫ����ʽ
     * @param style_dd              boolean         �Ƿ��Ե�ʽ����
     * @param formuType             int             У������
     * @return DataValidateInfo
     */
    private static DataValidateInfo createDataValidateInfo(ReportIn reportIn,
            MCellFormu mCellFormu, boolean style_dd, int formuType)
    {
        DataValidateInfo dvi = new DataValidateInfo();
        dvi.setRepInId(reportIn.getRepInId());
        dvi.setCellFormuId(mCellFormu.getCellFormuId());
        dvi.setValidateTypeId(mCellFormu.getCellType());
        dvi.setValidateTypeId(formuType);
        if (style_dd){
        	if(Config.DB_SERVER_TYPE.equals("oracle")){
				dvi.setSeqNo("  Seq_Data_Validate_Info.nextval");
    		}
    		if(Config.DB_SERVER_TYPE.equals("db2")){
    			dvi.setSeqNo("nextval for Seq_Data_Validate_Info");
    		} 
    		if(Config.DB_SERVER_TYPE.equals("sqlserver")){
    			dvi.setSeqNo("(select isnull(max(SEQ_NO),0)+1 from DATA_VALIDATE_INFO)");
    		}
        }
        //��ӳ������ڵ�Ԫ���ֵ
        dvi.setSourceValue(mCellFormu.getSource());
        dvi.setTargetValue(mCellFormu.getTarget());
        dvi.setResult(Report.RESULT_NO);
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
