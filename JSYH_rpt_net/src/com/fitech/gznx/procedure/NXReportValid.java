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
 * 农信部分校验
 * 
 * @author Dennis Yee
 *
 */
public class NXReportValid {
	
    /**
     * 有oracle语法(nextval) 需要修改
     * 卞以刚 2011-12-26
     * 已增加数据库判断
     * 已修改 添加sqlserver数据库sql语句 卞以刚 待测试 2011-12-26
     * 处理校验
     * @param repInId       Integer     表单表实际子报表Id
     * @param formuType     int         检验类型
     * @return 　boolean     
     * @throws Exception
     *  验证通过返回true   失败返回false
     */
    public static boolean processValid(Integer repInId, int formuType, String userName, Integer templateType)
            throws Exception {
    	/***
    	 * 有oracle语法(nextval) 需要修改
    	 * 卞以刚 2011-12-26
    	 * 已增加数据库判断
    	 * 已修改 添加sqlserver数据库sql语句 卞以刚 待测试 2011-12-26
    	 */
        return repInId == null ? false : valid(repInId.intValue(), formuType, userName, templateType);
    }

    /**
     * 有oracle语法(nextval) 需要修改
     * 卞以刚 2011-12-26
     * 已增加数据库判断
     * 已修改 添加sqlserver数据库sql语句 卞以刚 待测试 2011-12-26
     * 上报数据表间，表内校验是否通过
     * @param   repInId         int         表单表实际子报表ID
     * @param   formuType       int         校验类型
     * @return boolean
     * @throws SQLException
     * @throws Exception
     * 验证通过返回true   失败返回false
     */
    private static boolean valid(Integer repInId, int formuType, String userName, Integer templateType)
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
        	/**已使用hibernate 卞以刚 2011-12-21
        	 * 影响对象：AfReport**/
            AfReport reportIn = AFReportDelegate.getReportIn(repInId.longValue());
            
            if(orclCon.getAutoCommit())
                orclCon.setAutoCommit(false);
            
            // 判断填报数据是否存在
            if (reportIn == null)
            {
            	/****
            	 *jdbc技术 oracle语法(nextval) 需要修改
            	 * 卞以刚 2011-12-26
            	 * 已增加数据库判断
            	 * 已修改 添加sqlserver数据库sql语句 卞以刚 待测试 2011-12-26
            	 * 影响表：log_in
            	 */
                LogInImpl.writeBNLog(orclCon, "根据实际数据报表ID[" + repInId
                        + "]读取报表信息失败!",userName);
                close(orclCon);
                return false;
            }

            /**jdbc技术 无特殊语法 不需要修改
             * 影响表：AF_DATAVALIDATEINFO**/
            orclCon.createStatement().executeUpdate("delete from AF_DATAVALIDATEINFO where REP_ID="+repInId+" and VALIDATE_DESC="+formuType);
            
            // 报表样式
            Integer reportStyle = reportIn.getTemplateStyle();
            
            // 区分表间还是表内获取填报报表的单元格校验表达式集合
            /****
             * jdbc技术 无特殊语法 不需要修改
     	 	 * 卞以刚 2011-12-26
     	 	 * 影响表：af_validateformula
             */
            List cellFormuList = AFValidateFormulaDelegate.getCellFormus(orclCon, reportIn, formuType);
            
            // 上报数据的单元格名称，填报值键值对集合
            /****
             * jdbc技术 无特殊语法 不需要修改
             * 卞以刚 2011-12-26
             * 影响表：af_pbocreportdata || af_otherreportdata
             */
            Map cellMap = ValidateP2PReport.parseCell(orclCon, reportIn, templateType);
            
            if (cellFormuList != null && !cellFormuList.isEmpty()) {
            	
                dataValidateInfoList = new ArrayList();
                
                // 单元格校验表达式
              //  Iterator itr = cellFormuList.iterator();
                // 点对点式校验
                if (reportStyle == Report.REPORT_STYLE_DD) {
                	
                    Map validMap = null;    
                    //如果填报的单元格集合没有数据
                    if(cellMap == null || cellMap.isEmpty()) {
                        validFlag = Report.REPORT_CANCLE;
                    } else {
                        //获取验证表达式Map集合
                    	/***
                    	 * jdbc技术
                    	 * 无特殊oracle语法 不需要修改 
                    	 * 卞以刚 2011-12-26
                    	 */
                        validMap = ExpressionHandle.valid(orclCon, reportIn, cellFormuList, cellMap, templateType);
                    }
                    
                    if (validMap != null && !validMap.isEmpty()) {
                    	
                        // 如果有对应的表间报表未报的信息
                        Iterator itor = validMap.keySet().iterator();
                        // 迭代表达的验证情况
                        while (itor.hasNext()) {
                            AfValidateformula mCellFormu = (AfValidateformula) itor.next();
                            Boolean pass = (Boolean) validMap.get(mCellFormu);
                            // 如果没有通过验证
                            if (pass != null && !pass.booleanValue()) {
                            	
                                AfDatavalidateinfo dvi = createDataValidateInfo(
                                        reportIn, mCellFormu, true, formuType);
                                
                                // 检查对应的报表有无上报
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


                /** 如果校验情况信息不为空，将其校验情况批量入库* */
                if (dataValidateInfoList != null
                        && dataValidateInfoList.size() > 0) {
                	
                    String operation = "数据报表[编号:" + reportIn.getTemplateId()
                            + "," + "版本号:" + reportIn.getVersionId() + ","
//                            + "数据范围:" + reportIn.getDataRangeId() + ","
                            + "机构ID:" + reportIn.getOrgId() + "," + "货币单位:"
                            + reportIn.getCurId() + "," + "期数::"
                            + reportIn.getYear() + "-" 
                            + reportIn.getTerm() + "-" 
                            + reportIn.getDay() + "]";
                    
                    //表内校验信息的处理
                    if (Report.VALIDATE_TYPE_BN == formuType) {
                    	/***
                    	 * jdbc技术 无特殊oracle语法
                    	 * 不需要修改 卞以刚 2011-12-26
                    	 */
                        if (AFDataValidateInfoDelegate.writeDataValidateInfo(orclCon,repInId,
                                dataValidateInfoList,formuType, reportStyle)) {
                            // 写日志
                        	/***
                        	 * jdbc技术 oracle语法(nextval) 需要修改
                        	 * 卞以刚 2011-12-26
                        	 */
                            LogInImpl.writeBNLog(orclCon, operation
                                    + "表内校验"+ (validFlag.equals(Report.RESULT_NO) ? "未"
                                            : "") + "通过!",userName);
                        } else {
                        	/***
                        	 * jdbc技术 oracle语法(nextval) 需要修改
                        	 * 卞以刚 2011-12-26
                        	 */
                            LogInImpl.writeBNLog(orclCon, operation + "表内校验"
                                    + "操作失败!",userName);
                        }
                    }
                   
                    //表间校验信息的处理
                    if (Report.VALIDATE_TYPE_BJ == formuType) {
//                        String msg = "校验未通过";
//                        if (AFDataValidateInfoDelegate.writeBJDataValidateInfo(orclCon,
//                                dataValidateInfoList,formuType, reportStyle)) {
//                            msg = "校验通过";
//                        }
                        String msg = "";
                        /***
                         * jdbc技术 oracle语法(nextval) 需要修改
                         * 卞以刚 2011-12-26
                         */
                        if (AFDataValidateInfoDelegate.writeDataValidateInfo(orclCon,repInId,
                                dataValidateInfoList,formuType, reportStyle)) {
                            msg = "校验未通过";
                        } else {
                        	msg = "校验失败";
                        }
                        /***
                         * jdbc技术 oracle语法(nextval) 需要修改
                         * 卞以刚 2011-12-26
                         */
                        LogInImpl.writeBJLog(orclCon, operation + msg, userName);
                    }
                }
               //更新af_report表中的表间或表内校验标志
                /***
                 * jdbc技术 无特殊oracle语法 不需要修改
                 * 卞以刚 2011-12-26
                 */
               AFReportDelegate.updateFlag(orclCon, repInId, flagName, validFlag.intValue());
               
            } else {
            	
                // 获取校验关系表达式失败
                if (Report.FORMU_TYPE_BN == formuType) {
                	/***
                	 * jdbc技术 oracle语法(nextval) 需要修改
                	 * 卞以刚 2011-12-26
                	 */
                	LogInImpl.writeBNLog(
                        orclCon, "报表[编号:" + reportIn.getTemplateId() 
                        + ",版本号:" + reportIn.getVersionId() 
                        + "]暂无表内校验关系表达式!",userName);
                	/**
                	 * jdbc技术 无特殊oracle 不需要修改 
                	 * 2011-12-26 卞以刚
                	 * **/
                	AFReportDelegate.updateFlag(orclCon, repInId, flagName, validFlag.intValue());
                }
                // 属于表间校验的无校验公式也将表间校验标志置为通过
                if (Report.FORMU_TYPE_BJ == formuType) {
                	/***
                	 * jdbc技术 oracle语法(nextval) 需要修改
                	 * 卞以刚 2011-12-26
                	 */
                	LogInImpl.writeBNLog(
                            orclCon, "报表[编号:" + reportIn.getTemplateId() 
                            + ",版本号:" + reportIn.getVersionId() 
                            + "]暂无表间校验关系表达式!",userName);
                	/***
                	 * jdbc技术 无特殊oracle不需要修改
                	 * 卞以刚 2011-12-26
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
     * 构建一个DataValidateInfo
     * 
     * @param reportIn              ReportIn        内网表单表
     * @param mCellFormu            MCellFormu      单元格表达式
     * @param style_dd              boolean         是否点对点式报表
     * @param formuType             int             校验类型
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
