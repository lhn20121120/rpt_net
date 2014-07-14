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
 * 报表的表内校验存储过程类
 * 
 * @author rds
 * @serialData 2005-12-22 0:53
 */
public class ReportBNValidate implements Report{	
	/**
	 * 报表的表间关系校验
	 * 
	 * @param repInId 实际数据报表ID
	 * @return void
	 */
	public static void validate(int repInId) throws SQLException,Exception{
		List dataValidateInfoList=null;      //校验情况列表
		
		Connection conn=null;
		
		try{
//			conn=DriverManager.getConnection("jdbc:default:connection");   //获取数据库连接
			conn=(new com.cbrc.smis.proc.jdbc.FitechConnection()).getConnect();
			
			ReportIn reportIn=ReportImpl.getReportIn(conn,repInId);

			if(reportIn==null){
				LogInImpl.writeBNLog(conn,"根据实际数据报表ID[" + repInId + "]读取报表信息失败!");
				close(conn);
				return;
			}
           
			
			int reportStyle=ReportImpl.getReportStyle(conn,reportIn.getChildRepId(),reportIn.getVersionId());
			reportIn.setReportStyle(new Integer(reportStyle));			
			/*
			// System.out.println("reportIn.childRepId:" + reportIn.getChildRepId());
			// System.out.println("reportIn.versionId:" + reportIn.getVersionId());
			// System.out.println("reportIn.OrgId:" + reportIn.getOrgId());
			// System.out.println("reportIn.CurId:" + reportIn.getCurId());
			// System.out.println("reportIn.DataRangeId:" + reportIn.getDataRangeId());
			// System.out.println("reportIn.Term:" + reportIn.getTerm());
			// System.out.println("reportIn.Year:" + reportIn.getYear());
			// System.out.println("reportStyle:" + reportStyle);			
			*/
			
			List cellFormuList=ReportImpl.getCellFormus(conn,reportIn.getChildRepId(),reportIn.getVersionId(),FORMU_TYPE_BN);
			/*// System.out.println("cellFormuList.size:" + cellFormuList.size());*/
			Integer tblInnerValidateFlag=RESULT_OK;
			
			if(cellFormuList!=null && cellFormuList.size()>0){
				dataValidateInfoList=new ArrayList();
				MCellFormu mCellFormu=null;
				for(int i=0;i<cellFormuList.size();i++){
					mCellFormu=(MCellFormu)cellFormuList.get(i);
                   
					/*
					DataValidateInfo dvi=new DataValidateInfo();
					dvi.setRepInId(new Integer(repInId));
					dvi.setCellFormuId(mCellFormu.getCellFormuId());
					dvi.setValidateTypeId(mCellFormu.getCellType());
					dvi.setValidateTypeId(Report.VALIDATE_TYPE_BN);
					*/
					if(reportStyle==Report.REPORT_STYLE_DD){
						//dvi.setSeqNo("nextval for Seq_Data_Validate_Info");
						if(Expression.bnValidate(conn,reportIn,mCellFormu.getCellFormu(),mCellFormu.getPointNumber())){
							//dvi.setResult(RESULT_OK);
						}else{
							DataValidateInfo dvi=new DataValidateInfo();
							dvi.setRepInId(new Integer(repInId));
							dvi.setCellFormuId(mCellFormu.getCellFormuId());
							dvi.setValidateTypeId(mCellFormu.getCellType());
							dvi.setValidateTypeId(Report.VALIDATE_TYPE_BN);
							if(Config.DB_SERVER_TYPE.equals("oracle")){
								dvi.setSeqNo("  Seq_Data_Validate_Info.nextval");
		            		}
		            		if(Config.DB_SERVER_TYPE.equals("db2")){
		            			dvi.setSeqNo("nextval for Seq_Data_Validate_Info");
		            		}  
							dvi.setResult(RESULT_NO);
							tblInnerValidateFlag=RESULT_NO;
							dataValidateInfoList.add(dvi);
						}
					}else{
						if(Expression.bnValidate(conn,reportIn,mCellFormu.getCellFormu(),mCellFormu.getPointNumber())){
							//dvi.setResult(RESULT_OK);
//							// System.out.println("OK");
						}else{
//							// System.out.println("ERROR");
							DataValidateInfo dvi=new DataValidateInfo();
							dvi.setRepInId(new Integer(repInId));
							dvi.setCellFormuId(mCellFormu.getCellFormuId());
							dvi.setValidateTypeId(mCellFormu.getCellType());
							dvi.setValidateTypeId(Report.VALIDATE_TYPE_BN);
							dvi.setResult(RESULT_NO);
							tblInnerValidateFlag=RESULT_NO;
							dvi.setCause(Expression.getCause());
							if(!Expression.getErrMsg().equals("")){  //如果校验不通过是因为系统错误，则将其写入日志
								LogInImpl.writeBJLog(conn,Expression.getErrMsg());
							}
							dataValidateInfoList.add(dvi);
						}
					}
					//dataValidateInfoList.add(dvi);
				}
				
				/**如果校验情况信息不为空，将其校验情况批量入库**/
				if(dataValidateInfoList!=null && dataValidateInfoList.size()>0){
					String operation="数据报表[编号:" + reportIn.getChildRepId() + "," + 
						"版本号:" + reportIn.getVersionId() + "," +
						"数据范围:" + reportIn.getDataRangeId() + "," +
						"机构ID:" + reportIn.getOrgId() + "," + 
						"货币单位:" + reportIn.getCurId() + "," + 
						"年份:" + reportIn.getYear() + "," +
						"期数:" + reportIn.getTerm() + "]";
					if(ReportImpl.writeDataValidateInfo(conn,repInId,dataValidateInfoList,reportStyle)==true){
						//将表内校验详细信息写表
						ReportImpl.updateFlag(conn,repInId,"TBL_INNER_VALIDATE_FLAG",tblInnerValidateFlag.intValue());
						//如果校验不通过，新增重报记录
						if(tblInnerValidateFlag.equals(RESULT_NO)) ReportImpl.insertReportAgainSet(conn,reportIn.getRepInId());
						//写日志	
						LogInImpl.writeBNLog(conn,operation + "表内校验" + 
								(tblInnerValidateFlag.equals(RESULT_NO)?"未":"") +  
								"通过!");
					}else{
						LogInImpl.writeBNLog(conn,operation + "表内校验操作失败!");
					}
				}else{
					ReportImpl.updateFlag(conn,repInId,"TBL_INNER_VALIDATE_FLAG",tblInnerValidateFlag.intValue());
				}
			}else{  //获取校验关系表达式失败
				LogInImpl.writeBNLog(conn,"报表[编号:" + reportIn.getChildRepId() + 
						",版本号:" + reportIn.getVersionId() + "]暂无表内校验关系表达式!");
			}
		}catch(SQLException sqle){
			sqle.printStackTrace();
			throw new Exception(sqle.getMessage());
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}finally{
			close(conn);
		}		
	}
    
    /**
     * 上报数据校验是否通过(参考原作者代码作部分改动)
     * @author  gongming    
     * @date    2009-09-24
     * @param repInId       int         表单表实际子报表ID
     * @return void
     * @throws SQLException
     * @throws Exception
     */
     public static boolean valid(int repInId) throws SQLException,Exception{
         //校验情况列表
        List dataValidateInfoList=null;          

        Connection orclCon=null;   
        Integer tblInnerValidateFlag=RESULT_OK;
        try{
            orclCon=(new com.cbrc.smis.proc.jdbc.FitechConnection()).getConnect();  
            //获取填报的数据
            ReportIn reportIn=ReportImpl.getReportIn(orclCon,repInId);
            //判断填报数据是否存在
            if(reportIn==null){
                LogInImpl.writeBNLog(orclCon,"根据实际数据报表ID[" + repInId + "]读取报表信息失败!");
                close(orclCon);
                return false;
            }     
//// System.out.println(" 验证开始..");
//// System.out.println("报表Id: " + reportIn.getChildRepId());
//// System.out.println("版本号: " + reportIn.getVersionId());
           //报表样式
            int reportStyle = reportIn.getReportStyle().intValue();  
            //获取填报报表的单元格校验表达式集合
            List cellFormuList=ReportImpl.getCellFormus(orclCon,reportIn,VALIDATE_TYPE_BN);
            //表间校验标识
             
            //上报数据的单元格名称，填报值键值对集合
            Map cellMap = ReportDDImpl.parseCell(orclCon,reportIn);
            
            if(cellFormuList!=null && !cellFormuList.isEmpty()){
                dataValidateInfoList=new ArrayList();
                //单元格校验表达式  
                Iterator itr = cellFormuList.iterator();
                if (reportStyle == Report.REPORT_STYLE_DD) {
                   
                    //获取验证表达式Map集合
                    Map validMap = null;    
                    //如果填报的单元格集合没有数据                    
                    if(cellMap == null || cellMap.isEmpty())
                    {
                        tblInnerValidateFlag = RESULT_NO;
                    }   
                    else
                    {
                        //获取验证表达式Map集合
                        validMap = Expression.valid(orclCon, reportIn,
                                cellFormuList, cellMap);
                    }
//// System.out.println("原表达式个数--->" + cellFormuList.size());
//// System.out.println("验证个数------->" + validMap.size());
                    if(validMap != null && !validMap.isEmpty()){
                        Iterator itor = validMap.keySet().iterator();
                        //迭代表达的验证情况
                        while(itor.hasNext()){
                            MCellFormu mCellFormu = (MCellFormu)itor.next();
//// System.out.print("表达式: " + mCellFormu.getCellFormu());                            
                            Boolean pass = (Boolean)validMap.get(mCellFormu);
//// System.out.println("    验证:"+((pass.booleanValue())?"正确":"不正确"));                            
                            //如果没有通过验证
                            if(pass != null && !pass.booleanValue())
                            {
                                DataValidateInfo dvi = createDataValidateInfo(
                                        reportIn, mCellFormu, true);
                                tblInnerValidateFlag = RESULT_NO;
                                dataValidateInfoList.add(dvi);
                            }
                        }  
                        validMap.clear();
                        cellFormuList.clear();
                    }
                } else {
                    while(itr.hasNext()){
                        MCellFormu mCellFormu = (MCellFormu)itr.next();
                        if (!Expression.bnValid(orclCon, reportIn, mCellFormu, cellMap)) {
                            DataValidateInfo dvi = createDataValidateInfo(reportIn,
                                    mCellFormu, false);
                            tblInnerValidateFlag = RESULT_NO;
                            dvi.setCause(Expression.getCause());
                            if (!Expression.getErrMsg().equals("")) { // 如果校验不通过是因为系统错误，则将其写入日志
                                LogInImpl.writeBJLog(orclCon, Expression
                                        .getErrMsg());
                            }
                            dataValidateInfoList.add(dvi);
                        }
                    }
                }
              
                
                /**如果校验情况信息不为空，将其校验情况批量入库**/
                if(dataValidateInfoList!=null && dataValidateInfoList.size()>0){
                    String operation="数据报表[编号:" + reportIn.getChildRepId() + "," + 
                        "版本号:" + reportIn.getVersionId() + "," +
                        "数据范围:" + reportIn.getDataRangeId() + "," +
                        "机构ID:" + reportIn.getOrgId() + "," + 
                        "货币单位:" + reportIn.getCurId() + "," + 
                        "年份:" + reportIn.getYear() + "," +
                        "期数:" + reportIn.getTerm() + "]";
                    if(ReportImpl.writeDataValidateInfo(orclCon,repInId,dataValidateInfoList,reportStyle)){
                        //将表内校验详细信息写表
                        ReportImpl.updateFlag(orclCon,repInId,"TBL_INNER_VALIDATE_FLAG",tblInnerValidateFlag.intValue());
                        //如果校验不通过，新增重报记录
                        if(tblInnerValidateFlag.equals(RESULT_NO)) ReportImpl.insertReportAgainSet(orclCon,reportIn.getRepInId());
                        //写日志   
                        LogInImpl.writeBNLog(orclCon,operation + "表内校验" + 
                                (tblInnerValidateFlag.equals(RESULT_NO)?"未":"") +  
                                "通过!");
                    }else{
                        LogInImpl.writeBNLog(orclCon,operation + "表内校验操作失败!");
                    }
                }else{
                    ReportImpl.updateFlag(orclCon,repInId,"TBL_INNER_VALIDATE_FLAG",tblInnerValidateFlag.intValue());
                }
            }else{  //获取校验关系表达式失败
                LogInImpl.writeBNLog(orclCon,"报表[编号:" + reportIn.getChildRepId() + 
                        ",版本号:" + reportIn.getVersionId() + "]暂无表内校验关系表达式!");
            }
        }catch(SQLException sqle){
            sqle.printStackTrace();
            throw new Exception(sqle.getMessage());
        }catch(Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }finally{
            close(orclCon);
        }       
        return tblInnerValidateFlag==RESULT_OK ? true:false;
    }

     /**
      * 构建一个DataValidateInfo
      * @author gongming
      * @date   2007-09-25
      * @param reportIn         ReportIn        内网表单表
      * @param mCellFormu       MCellFormu      单元格表达式
      * @param style_dd         boolean         是否点对点式报表
      * @return DataValidateInfo
      */
    public static DataValidateInfo createDataValidateInfo(ReportIn reportIn,
                                                          MCellFormu mCellFormu,
                                                          boolean style_dd){
        DataValidateInfo dvi=new DataValidateInfo();
        dvi.setRepInId(reportIn.getRepInId());
        dvi.setCellFormuId(mCellFormu.getCellFormuId());
        dvi.setValidateTypeId(mCellFormu.getCellType());
        if(style_dd){
        	if(Config.DB_SERVER_TYPE.equals("oracle")){
				dvi.setSeqNo("  Seq_Data_Validate_Info.nextval");
    		}
    		if(Config.DB_SERVER_TYPE.equals("db2")){
    			dvi.setSeqNo("nextval for Seq_Data_Validate_Info");
    		} 
        }
        dvi.setResult(RESULT_NO);
        return dvi;
    }
	/**
	 * 关闭数据库连接
	 * 
	 * @param conn Connection
	 * @return void
	 * @exception Exception
	 */
	private static void close(Connection conn) throws Exception{
		if(conn!=null){
			try{
                //2007-09-29龚明修改
                if(!conn.getAutoCommit())
                    conn.commit();
                if(conn != null)
                /////////////////
				conn.close();                
			}catch(SQLException sqle){
				throw new Exception(sqle.getMessage());
			}
		}
	}
}