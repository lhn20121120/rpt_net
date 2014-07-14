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
* <p>Description 对表内数据，表间数据进行合法性校验的方法操作类</p>
*
* <p>Copyright: Copyright (c) 2007</p>
*
* <p>Company: </p>
*
* @author   龚明
* @date     2009-09-30
* @version 1.0
*/
public class ReportValid
{

    public ReportValid()
    {
    }

    /**使用jdbc 可能不需要修改  卞以刚 2011-12-21**/
    /**
     * 
     * 处理校验
     * @param repInId       Integer     表单表实际子报表Id
     * @param formuType     int         检验类型
     * @return 　boolean     
     * @throws Exception
     *  验证通过返回true   失败返回false
     */
    public static boolean processValid(Integer repInId, int formuType,String userName)
            throws Exception
    {
    	/**使用jdbc 可能不需要修改  卞以刚 2011-12-21**/
        return repInId == null ? false : valid(repInId.intValue(), formuType,userName);
    }

    /**
     * 使用jdbc 可能不需要修改  卞以刚 2011-12-21
     * 上报数据表间，表内校验是否通过(参考原作者代码作部分改动)
     * 
     * @author gongming
     * @date 2009-09-24
     * @param   repInId         int         表单表实际子报表ID
     * @param   formuType       int         校验类型
     * @return boolean
     * @throws SQLException
     * @throws Exception
     * 验证通过返回true   失败返回false
     */
    private static boolean valid(int repInId, int formuType,String userName)
            throws SQLException, Exception
    {
        // 校验情况列表
        List dataValidateInfoList = null;

        Connection orclCon = null;
        // 写表间或表内校验标识
        Integer validFlag = Report.RESULT_OK;
        String flagName = setFlagName(formuType);

        try
        {
            orclCon = (new com.cbrc.smis.proc.jdbc.FitechConnection()).getConnect();
            // 获取填报的数据
            /**使用jdbc 可能不需要修改  卞以刚 2011-12-21**/
            ReportIn reportIn = ReportImpl.getReportIn(orclCon, repInId);
            if(orclCon.getAutoCommit())
                orclCon.setAutoCommit(false);
            
            // 判断填报数据是否存在
            if (reportIn == null)
            {
                LogInImpl.writeBNLog(orclCon, "根据实际数据报表ID[" + repInId
                        + "]读取报表信息失败!",userName);
                close(orclCon);
                return false;
            }
            if(formuType==2){
            	if( (reportIn.getChildRepId().trim().equals("G3301") || reportIn.getChildRepId().trim().equals("G3302")))	{
            		if(!reportIn.getCurId().equals(new Integer(1))){
            			/**使用jdbc 可能不需要修改  卞以刚 2011-12-21**/
            			ReportImpl.updateFlag(
			                        orclCon, repInId, flagName, validFlag.intValue());
            			return true;
            		}
            	}
            }
            
            /**使用jdbc 可能不需要修改  卞以刚 2011-12-21**/
            orclCon.createStatement().executeUpdate("delete from DATA_VALIDATE_INFO where REP_IN_ID="+repInId+" and VALIDATE_TYPE_ID="+formuType);
            
            // 报表样式
            int reportStyle = reportIn.getReportStyle().intValue();
            // 区分表间还是表内获取填报报表的单元格校验表达式集合
            /**使用jdbc 可能不需要修改  卞以刚 2011-12-21**/
            List cellFormuList = ReportImpl.getCellFormus(orclCon, reportIn, formuType);
            
            // 上报数据的单元格名称，填报值键值对集合
            /**使用jdbc 可能不需要修改  卞以刚 2011-12-21**/
            Map cellMap = ReportDDImpl.parseCell(orclCon, reportIn);

           
            if (cellFormuList != null && !cellFormuList.isEmpty())
            {
                dataValidateInfoList = new ArrayList();
                // 单元格校验表达式
                Iterator itr = cellFormuList.iterator();
                if (reportStyle == Report.REPORT_STYLE_DD)
                {
                    Map validMap = null;    
                    //如果填报的单元格集合没有数据                    
                    if(cellMap == null || cellMap.isEmpty())
                    {
                        validFlag = Report.REPORT_CANCLE;
                    }   
                    else
                    {
                        //获取验证表达式Map集合
                        validMap = Expression.valid(orclCon, reportIn,
                                cellFormuList, cellMap);
                    }
                    
                    /**(
                     * 此处已验证完成
                     */
                    if (validMap != null && !validMap.isEmpty())
                    {
                        // 如果有对应的表间报表未报的信息
                        Iterator itor = validMap.keySet().iterator();
                        // 迭代表达的验证情况
                        while (itor.hasNext())
                        {
                            MCellFormu mCellFormu = (MCellFormu) itor.next();
                            Boolean pass = (Boolean) validMap.get(mCellFormu);
//                            System.out.println(mCellFormu.getTarget()+":"+mCellFormu.getSource());
                            // 如果没有通过验证
                            if (pass != null && !pass.booleanValue())
                            {
                                DataValidateInfo dvi = createDataValidateInfo(
                                        reportIn, mCellFormu, true, formuType);
                                // 检查对应的报表有无上报
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
                            	// 如果校验不通过是因为系统错误，则将其写入日志
                                LogInImpl.writeBJLog(orclCon, Expression.getErrMsg(),userName);
                            }
                            dataValidateInfoList.add(dvi);
                        }
                    }
                }

                /** 如果校验情况信息不为空，将其校验情况批量入库* */
                if (dataValidateInfoList != null
                        && dataValidateInfoList.size() > 0)
                {
                    String operation = "数据报表[编号:" + reportIn.getChildRepId()
                            + "," + "版本号:" + reportIn.getVersionId() + ","
                            + "数据范围:" + reportIn.getDataRangeId() + ","
                            + "机构ID:" + reportIn.getOrgId() + "," + "货币单位:"
                            + reportIn.getCurId() + "," + "年份:"
                            + reportIn.getYear() + "," + "期数:"
                            + reportIn.getTerm() + "]";
                    //表内校验信息的处理
                    if (Report.VALIDATE_TYPE_BN == formuType)
                    {
                        if (ReportImpl.writeDataValidateInfo(orclCon,repInId,
                                dataValidateInfoList, reportStyle))
                        {
                            // 写日志
                            LogInImpl.writeBNLog(orclCon, operation
                                    + "表内校验"+ (validFlag.equals(Report.RESULT_NO) ? "未"
                                            : "") + "通过!",userName);
                        }
                        else
                        {
                            LogInImpl.writeBNLog(orclCon, operation + "表内校验"
                                    + "操作失败!",userName);
                        }
                    }
                   
                    //表间校验信息的处理
                    if (Report.VALIDATE_TYPE_BJ == formuType)
                    {
                        String msg = "校验未通过";
                        if (ReportImpl.writeBJDataValidateInfo(orclCon,
                                dataValidateInfoList, reportStyle))
                        {
                            msg = "校验通过";
                        }
                        LogInImpl.writeBJLog(orclCon, operation + msg, userName);
                    }
                }
               //更新report_in表中的表间或表内校验标志
               ReportImpl.updateFlag(orclCon, repInId, flagName, validFlag.intValue());
            }
            else
            {
                // 获取校验关系表达式失败
                if (Report.FORMU_TYPE_BN == formuType) {
                	LogInImpl.writeBNLog(               
                        orclCon, "报表[编号:" + reportIn.getChildRepId() + ",版本号:"
                                + reportIn.getVersionId() + "]暂无表内校验关系表达式!",userName);
                	ReportImpl.updateFlag(
                            orclCon, repInId, flagName, validFlag.intValue());
                }
                // 属于表间校验的无校验公式也将表间校验标志置为通过
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
     * 没有sql字符串的拼装 sql的主键自增，可能不需要拼装  卞以刚 2011-12-21
     * 构建一个DataValidateInfo
     * 
     * @author gongming
     * @date 2007-09-25
     * @param reportIn              ReportIn        内网表单表
     * @param mCellFormu            MCellFormu      单元格表达式
     * @param style_dd              boolean         是否点对点式报表
     * @param formuType             int             校验类型
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
        //添加出错所在单元格的值
        dvi.setSourceValue(mCellFormu.getSource());
        dvi.setTargetValue(mCellFormu.getTarget());
        dvi.setResult(Report.RESULT_NO);
        return dvi;
    }

    /**
     * 设置校验后更新的字段名
     * 
     * @param formuType     String      校验类型
     * @return  String
     */
    private static String setFlagName(int formuType)
    {
        if (Report.VALIDATE_TYPE_BN == formuType) return "TBL_INNER_VALIDATE_FLAG";
        if (Report.VALIDATE_TYPE_BJ == formuType) return "TBL_OUTER_VALIDATE_FLAG";
        return null;
    }

    /**
     * 关闭数据库连接
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
                // 2007-09-29龚明修改
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
