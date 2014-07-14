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
 * ����ı���У��洢������
 * 
 * @author rds
 * @serialData 2005-12-22 0:53
 */
public class ReportBNValidate implements Report{	
	/**
	 * ����ı���ϵУ��
	 * 
	 * @param repInId ʵ�����ݱ���ID
	 * @return void
	 */
	public static void validate(int repInId) throws SQLException,Exception{
		List dataValidateInfoList=null;      //У������б�
		
		Connection conn=null;
		
		try{
//			conn=DriverManager.getConnection("jdbc:default:connection");   //��ȡ���ݿ�����
			conn=(new com.cbrc.smis.proc.jdbc.FitechConnection()).getConnect();
			
			ReportIn reportIn=ReportImpl.getReportIn(conn,repInId);

			if(reportIn==null){
				LogInImpl.writeBNLog(conn,"����ʵ�����ݱ���ID[" + repInId + "]��ȡ������Ϣʧ��!");
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
							if(!Expression.getErrMsg().equals("")){  //���У�鲻ͨ������Ϊϵͳ��������д����־
								LogInImpl.writeBJLog(conn,Expression.getErrMsg());
							}
							dataValidateInfoList.add(dvi);
						}
					}
					//dataValidateInfoList.add(dvi);
				}
				
				/**���У�������Ϣ��Ϊ�գ�����У������������**/
				if(dataValidateInfoList!=null && dataValidateInfoList.size()>0){
					String operation="���ݱ���[���:" + reportIn.getChildRepId() + "," + 
						"�汾��:" + reportIn.getVersionId() + "," +
						"���ݷ�Χ:" + reportIn.getDataRangeId() + "," +
						"����ID:" + reportIn.getOrgId() + "," + 
						"���ҵ�λ:" + reportIn.getCurId() + "," + 
						"���:" + reportIn.getYear() + "," +
						"����:" + reportIn.getTerm() + "]";
					if(ReportImpl.writeDataValidateInfo(conn,repInId,dataValidateInfoList,reportStyle)==true){
						//������У����ϸ��Ϣд��
						ReportImpl.updateFlag(conn,repInId,"TBL_INNER_VALIDATE_FLAG",tblInnerValidateFlag.intValue());
						//���У�鲻ͨ���������ر���¼
						if(tblInnerValidateFlag.equals(RESULT_NO)) ReportImpl.insertReportAgainSet(conn,reportIn.getRepInId());
						//д��־	
						LogInImpl.writeBNLog(conn,operation + "����У��" + 
								(tblInnerValidateFlag.equals(RESULT_NO)?"δ":"") +  
								"ͨ��!");
					}else{
						LogInImpl.writeBNLog(conn,operation + "����У�����ʧ��!");
					}
				}else{
					ReportImpl.updateFlag(conn,repInId,"TBL_INNER_VALIDATE_FLAG",tblInnerValidateFlag.intValue());
				}
			}else{  //��ȡУ���ϵ���ʽʧ��
				LogInImpl.writeBNLog(conn,"����[���:" + reportIn.getChildRepId() + 
						",�汾��:" + reportIn.getVersionId() + "]���ޱ���У���ϵ���ʽ!");
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
     * �ϱ�����У���Ƿ�ͨ��(�ο�ԭ���ߴ��������ָĶ�)
     * @author  gongming    
     * @date    2009-09-24
     * @param repInId       int         ����ʵ���ӱ���ID
     * @return void
     * @throws SQLException
     * @throws Exception
     */
     public static boolean valid(int repInId) throws SQLException,Exception{
         //У������б�
        List dataValidateInfoList=null;          

        Connection orclCon=null;   
        Integer tblInnerValidateFlag=RESULT_OK;
        try{
            orclCon=(new com.cbrc.smis.proc.jdbc.FitechConnection()).getConnect();  
            //��ȡ�������
            ReportIn reportIn=ReportImpl.getReportIn(orclCon,repInId);
            //�ж�������Ƿ����
            if(reportIn==null){
                LogInImpl.writeBNLog(orclCon,"����ʵ�����ݱ���ID[" + repInId + "]��ȡ������Ϣʧ��!");
                close(orclCon);
                return false;
            }     
//// System.out.println(" ��֤��ʼ..");
//// System.out.println("����Id: " + reportIn.getChildRepId());
//// System.out.println("�汾��: " + reportIn.getVersionId());
           //������ʽ
            int reportStyle = reportIn.getReportStyle().intValue();  
            //��ȡ�����ĵ�Ԫ��У����ʽ����
            List cellFormuList=ReportImpl.getCellFormus(orclCon,reportIn,VALIDATE_TYPE_BN);
            //���У���ʶ
             
            //�ϱ����ݵĵ�Ԫ�����ƣ��ֵ��ֵ�Լ���
            Map cellMap = ReportDDImpl.parseCell(orclCon,reportIn);
            
            if(cellFormuList!=null && !cellFormuList.isEmpty()){
                dataValidateInfoList=new ArrayList();
                //��Ԫ��У����ʽ  
                Iterator itr = cellFormuList.iterator();
                if (reportStyle == Report.REPORT_STYLE_DD) {
                   
                    //��ȡ��֤���ʽMap����
                    Map validMap = null;    
                    //�����ĵ�Ԫ�񼯺�û������                    
                    if(cellMap == null || cellMap.isEmpty())
                    {
                        tblInnerValidateFlag = RESULT_NO;
                    }   
                    else
                    {
                        //��ȡ��֤���ʽMap����
                        validMap = Expression.valid(orclCon, reportIn,
                                cellFormuList, cellMap);
                    }
//// System.out.println("ԭ���ʽ����--->" + cellFormuList.size());
//// System.out.println("��֤����------->" + validMap.size());
                    if(validMap != null && !validMap.isEmpty()){
                        Iterator itor = validMap.keySet().iterator();
                        //����������֤���
                        while(itor.hasNext()){
                            MCellFormu mCellFormu = (MCellFormu)itor.next();
//// System.out.print("���ʽ: " + mCellFormu.getCellFormu());                            
                            Boolean pass = (Boolean)validMap.get(mCellFormu);
//// System.out.println("    ��֤:"+((pass.booleanValue())?"��ȷ":"����ȷ"));                            
                            //���û��ͨ����֤
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
                            if (!Expression.getErrMsg().equals("")) { // ���У�鲻ͨ������Ϊϵͳ��������д����־
                                LogInImpl.writeBJLog(orclCon, Expression
                                        .getErrMsg());
                            }
                            dataValidateInfoList.add(dvi);
                        }
                    }
                }
              
                
                /**���У�������Ϣ��Ϊ�գ�����У������������**/
                if(dataValidateInfoList!=null && dataValidateInfoList.size()>0){
                    String operation="���ݱ���[���:" + reportIn.getChildRepId() + "," + 
                        "�汾��:" + reportIn.getVersionId() + "," +
                        "���ݷ�Χ:" + reportIn.getDataRangeId() + "," +
                        "����ID:" + reportIn.getOrgId() + "," + 
                        "���ҵ�λ:" + reportIn.getCurId() + "," + 
                        "���:" + reportIn.getYear() + "," +
                        "����:" + reportIn.getTerm() + "]";
                    if(ReportImpl.writeDataValidateInfo(orclCon,repInId,dataValidateInfoList,reportStyle)){
                        //������У����ϸ��Ϣд��
                        ReportImpl.updateFlag(orclCon,repInId,"TBL_INNER_VALIDATE_FLAG",tblInnerValidateFlag.intValue());
                        //���У�鲻ͨ���������ر���¼
                        if(tblInnerValidateFlag.equals(RESULT_NO)) ReportImpl.insertReportAgainSet(orclCon,reportIn.getRepInId());
                        //д��־   
                        LogInImpl.writeBNLog(orclCon,operation + "����У��" + 
                                (tblInnerValidateFlag.equals(RESULT_NO)?"δ":"") +  
                                "ͨ��!");
                    }else{
                        LogInImpl.writeBNLog(orclCon,operation + "����У�����ʧ��!");
                    }
                }else{
                    ReportImpl.updateFlag(orclCon,repInId,"TBL_INNER_VALIDATE_FLAG",tblInnerValidateFlag.intValue());
                }
            }else{  //��ȡУ���ϵ���ʽʧ��
                LogInImpl.writeBNLog(orclCon,"����[���:" + reportIn.getChildRepId() + 
                        ",�汾��:" + reportIn.getVersionId() + "]���ޱ���У���ϵ���ʽ!");
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
      * ����һ��DataValidateInfo
      * @author gongming
      * @date   2007-09-25
      * @param reportIn         ReportIn        ��������
      * @param mCellFormu       MCellFormu      ��Ԫ����ʽ
      * @param style_dd         boolean         �Ƿ��Ե�ʽ����
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
	 * �ر����ݿ�����
	 * 
	 * @param conn Connection
	 * @return void
	 * @exception Exception
	 */
	private static void close(Connection conn) throws Exception{
		if(conn!=null){
			try{
                //2007-09-29�����޸�
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