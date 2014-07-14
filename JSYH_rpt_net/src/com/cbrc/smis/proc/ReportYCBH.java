package com.cbrc.smis.proc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.cbrc.smis.proc.impl.Expression;
import com.cbrc.smis.proc.impl.LogInImpl;
import com.cbrc.smis.proc.impl.Report;
import com.cbrc.smis.proc.impl.ReportDDImpl;
import com.cbrc.smis.proc.impl.ReportImpl;
import com.cbrc.smis.proc.po.AbnormityChange;
import com.cbrc.smis.proc.po.ReportIn;

/**
 * �����쳣�仯�ļ���
 * 
 * @author rds
 * @date 2005-12-26 12:23
 */
public class ReportYCBH implements Report{
	private final static int SCALE=4;
	
	/**
	 * ���㱨����쳣
	 * 
	 * @param repInId ʵ�����ݱ���ID
	 * @return void
	 */
	public static void calculateYCBH(int repInId) throws SQLException,Exception{
//		Connection conn=DriverManager.getConnection("jdbc:default:connection");   //��ȡ���ݿ�����
		Connection conn=new com.cbrc.smis.proc.jdbc.FitechConnection().getConnect();
		
		ReportIn reportIn=ReportImpl.getReportIn(conn,repInId);
		
		if(reportIn==null) return;
		
		/**��������**/
		int reportStyle=ReportImpl.getReportStyle(conn,reportIn.getChildRepId(),reportIn.getVersionId());
			
		/**��Ե�ʽ������쳣�仯�ļ���**/
		if(reportStyle==REPORT_STYLE_DD){
			List acList=ReportImpl.getAbnormityChangeList(conn,
					reportIn.getChildRepId(),
					reportIn.getVersionId(),
					reportIn.getOrgId(),
					Report.REPORT_STYLE_DD);
			
			if(acList!=null && acList.size()>0){
				AbnormityChange ac=null;
				double cellValue=0,prevCellValue=0,sameCellValue=0;
				float prevPercent=0,samePercent=0;
				float prevRiseStandard,prevFallStandard,sameRiseStandard,sameFallStandard;
				int abnormityChangeFlag=ABNORMITY_CHANGE_FLAG_OK;
				String repFreqNm="";   //�����Ƶ��
				repFreqNm=ReportImpl.getRepActuName(conn,repInId);
				
				for(int i=0;i<acList.size();i++){
					ac=(AbnormityChange)acList.get(i);
					cellValue=ReportDDImpl.getCellValue(conn,repInId,ac.getCellId());
					Double pvalue=ReportDDImpl.getPrevTermCellValue(conn,reportIn,ac.getCellId(),repFreqNm);
					if(pvalue!=null){
						prevCellValue=pvalue.doubleValue();

					}
					Double sValue =ReportDDImpl.getLastYearSameTermCellValue(conn,reportIn,ac.getCellId(),repFreqNm);
					if(sValue != null){
						sameCellValue=sValue.doubleValue();
					}
					//// System.out.println(cellValue + "\t" + prevCellValue + "\t" + sameCellValue);
					/**���������ݵıȽ�**/
					if(cellValue!=prevCellValue){
						prevPercent=prevCellValue==0?200:Expression.round(cellValue,prevCellValue,SCALE)*100;
						if(prevPercent>100){  //����
							if((prevPercent-100)>ac.getPrevRiseStandard()){
								prevRiseStandard=-(prevPercent-100);
								abnormityChangeFlag=ABNORMITY_CHANGE_FLAG_NO;
							}else{
								prevRiseStandard=prevPercent-100;
							}
							prevFallStandard=0;
						}else{  //�½�
							prevRiseStandard=0;							
							if((prevPercent-100)<ac.getPrevFallStandard()){
								prevFallStandard=-(prevPercent-100);
								abnormityChangeFlag=ABNORMITY_CHANGE_FLAG_NO;
							}else{
								prevFallStandard=prevPercent-100;;
							}
						}
					}else{  //û���쳣�仯
						prevRiseStandard=0;
						prevFallStandard=0;
					}
					/**������ͬ�ڵıȽ�**/
					if(cellValue!=sameCellValue){
						samePercent=sameCellValue==0?200:Expression.round(cellValue,sameCellValue,SCALE)*100;						
						if(samePercent>100){  //����
							if(samePercent-100>ac.getSameRiseStandard()){
								sameRiseStandard=-(samePercent-100);
								abnormityChangeFlag=ABNORMITY_CHANGE_FLAG_NO;
							}else{
								sameRiseStandard=samePercent-100;
							}
							sameFallStandard=0;
						}else{ //�½�
							sameRiseStandard=0;
							if(samePercent-100<ac.getSameFallStandard()){
								sameFallStandard=-(samePercent-100);
								abnormityChangeFlag=ABNORMITY_CHANGE_FLAG_NO;
							}else{
								sameFallStandard=samePercent-100;
							}
						}
					}else{ //û���쳣�仯
						sameRiseStandard=0;
						sameFallStandard=0;
					}
					/**���µ�Ԫ����쳣��Ϣ**/
					ReportDDImpl.update(conn,repInId,ac.getCellId(),prevRiseStandard,prevFallStandard,sameRiseStandard,sameFallStandard);
				}
				/**����ʵ�����ݱ���쳣�仯״̬��ʶ**/
				if(ReportImpl.updateFlag(conn,repInId,"Abmormity_Change_Flag",abnormityChangeFlag)==true){
					//LogInImpl.writeLog(conn,"���㱨��[Rep_In_Id:" + repInId + "]�쳣�仯�ɹ�!");
				}else{
					LogInImpl.writeLog(conn,"���㱨��[Rep_In_Id:" + repInId + "]�쳣�仯ʧ��!");
				}
			}
		}
		
		/**�嵥ʽ�����쳣�仯�ļ���**/
		if(reportStyle==REPORT_STYLE_QD){
			/*List acList=ReportImpl.getAbnormityChangeList(conn,
					reportIn.getChildRepId(),
					reportIn.getVersionId(),
					reportIn.getVersionId(),
					Report.REPORT_STYLE_QD);
			if(acList!=null && acList.size()>0){
				AbnormityChange ac=null;
				float cellValue=0,prevCellValue=0,sameCellValue=0;
				float prevPercent=0,samePercent=0;
				float prevRiseStandard,prevFallStandard,sameRiseStandard,sameFallStandard;
				int abnormityChangeFlag=ABNORMITY_CHANGE_FLAG_OK;
				for(int i=0;i<acList.size();i++){
					
				}
			}*/
		}
		
		conn.close();
	}
}
