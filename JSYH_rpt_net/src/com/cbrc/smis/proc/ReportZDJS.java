package com.cbrc.smis.proc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import com.cbrc.smis.proc.impl.Expression;
import com.cbrc.smis.proc.impl.LogInImpl;
import com.cbrc.smis.proc.impl.Report;
import com.cbrc.smis.proc.impl.ReportImpl;
import com.cbrc.smis.proc.po.MCellFormu;
import com.cbrc.smis.proc.po.ReportIn;

/**
 * 计算报表中的自动填报域的值
 * 
 * @author rds
 */
public class ReportZDJS implements Report{
	/**
	 * 表内自动填报域的计算
	 */
	public static void calculateBN(int repInId) throws SQLException,Exception{
//		Connection conn=DriverManager.getConnection("jdbc:default:connection");   //获取数据库连接
		Connection conn=new com.cbrc.smis.proc.jdbc.FitechConnection().getConnect();
		
		ReportIn reportIn=ReportImpl.getReportIn(conn,repInId);
		
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
		
		if(reportIn==null) return;
		
		/**报表的类别**/
		int reportStyle=ReportImpl.getReportStyle(conn,reportIn.getChildRepId(),reportIn.getVersionId());
		reportIn.setReportStyle(reportStyle);
		
		/**点对点式报表表内自动填报域的计算**/
		if(reportStyle==REPORT_STYLE_DD){
			calculateCellValue(conn,reportIn,repInId,1);
		}
		
		/**清单式报表自动填报域[合计栏]的计算**/
		if(reportStyle==REPORT_STYLE_QD){
				
		}
		
		conn.close();
	}
	
	/**
	 * 表间自动填报域的计算
	 */
	public static void calculateBJ(int repInId) throws SQLException,Exception{
		Connection conn=DriverManager.getConnection("jdbc:default:connection");   //获取数据库连接
		
		ReportIn reportIn=ReportImpl.getReportIn(conn,repInId);
				
		if(reportIn==null) return;
		
		/**报表的类别**/
		int reportStyle=ReportImpl.getReportStyle(conn,reportIn.getChildRepId(),reportIn.getVersionId());
		reportIn.setReportStyle(reportStyle);
		
		/**点对点式报表表间自动填报域的计算**/
		if(reportStyle==REPORT_STYLE_DD){
			calculateCellValue(conn,reportIn,repInId,2);
		}
	}
	
	/**
	 * 根据计算表达式计算单元格的值
	 */
	public static void calculateCellValue(Connection conn,ReportIn reportIn,int repInId,int flag) throws Exception{
		List cellFormuList=ReportImpl.getCellFormus(conn,reportIn.getChildRepId(),reportIn.getVersionId(),FORMU_TYPE_BN_JS);
		if(cellFormuList!=null && cellFormuList.size()>0){			
			MCellFormu mCellFormu=null;
			// System.out.println("cellFormuList.size():" + cellFormuList.size());
			for(int i=0;i<cellFormuList.size();i++){
				mCellFormu=(MCellFormu)cellFormuList.get(i);
				String arr[]=mCellFormu.getCellFormu().split(Expression.SPLIT_SYMBOL_EQUAL);
				if(arr.length<=0) break;				
				String _valueFormu=formatFormu(conn,reportIn,arr[1]);				
				String cellName=Expression.getCellName(arr[0]);
				cellName=cellName.trim();
				int cellId=ReportImpl.getCellId(conn,reportIn.getChildRepId(),reportIn.getVersionId(),cellName);
							
				/*// System.out.println("arr[i]:" + arr[1]);
				// System.out.println("_valueFormu:" + _valueFormu);
				// System.out.println("cellId:" + cellId);*/
								
				if(ReportImpl.updateCellValue(conn,repInId,cellId,
						ReportImpl.getCalculatorFormu(conn,_valueFormu))==false){
					if(flag==1)
						LogInImpl.writeBNLog(conn,"计算单元格[" + cellName + "]失败!");
					else
						LogInImpl.writeBJLog(conn,"计算单元格[" + cellName + "]失败!");
				}
			}
		}
	}
	
	/**
	 * 计算表达式
	 * 
	 * @param conn Connection 数据库连接
	 * @param reportIn ReportIn 实际数据报表对象
	 * @param cellFormu String 表达式
	 * @return String
	 * @exception Exception
	 */
	private static String formatFormu(Connection conn,ReportIn reportIn,String cellFormu)
		throws Exception{
		String formu="";

		/*if(conn==null || reportIn==null) // System.out.println("reporin is null");
		if(reportIn.getRepInId()==null) // System.out.println("repInId is null");
		if(reportIn.getChildRepId()==null) // System.out.println("childRepId is null");
		if(reportIn.getVersionId()==null) // System.out.println("versionId is null"); 
		if(reportIn.getDataRangeId()==null) // System.out.println("dataRangeId is null");
		if(reportIn.getOrgId()==null) // System.out.println("orgId is null");
		if(reportIn.getYear()==null) // System.out.println("year is null");
		if(reportIn.getTerm()==null) // System.out.println("term is null");
		if(reportIn.getReportStyle()==null) // System.out.println("reportStyle is null"); 
		if(cellFormu==null) // System.out.println("cellFormu is null");*/
		
		if(conn==null || reportIn==null || reportIn.getRepInId()==null || reportIn.getChildRepId()==null ||
				reportIn.getVersionId()==null || reportIn.getDataRangeId()==null || reportIn.getOrgId()==null ||
				reportIn.getYear()==null || reportIn.getTerm()==null || reportIn.getReportStyle()==null 
				|| cellFormu==null) 
			return formu;

		int posS=0,posE=0;
		
		cellFormu=cellFormu.trim();
		
		posS=cellFormu.indexOf(Expression.SPLIT_SYMBOL_LEFT_MID_KUOHU);
		posE=cellFormu.indexOf(Expression.SPLIT_SYMBOL_RIGHT_MID_HUOHU);
		
		String cellName=null;
		String cellValue="0";
		while(posS>=0 && posE>=0){
			cellName=cellFormu.substring(posS,posE+1);

			if(cellName!=null && cellName.length()>2){
				if(cellName.indexOf("_")>0){
					String arr[]=cellName.substring(1,cellName.length()-1).split("_");
					if(arr.length>0){
						cellValue=ReportImpl.getCellValue(conn,
								reportIn.getRepInId().intValue(),
								arr[0],
								arr[1],
								reportIn.getDataRangeId().intValue(),
								reportIn.getOrgId(),
								reportIn.getCurId().intValue(),
								reportIn.getYear().intValue(),
								reportIn.getTerm().intValue(),
								arr[2],
								Report.FORMU_TYPE_BJ);
					}
				}else{
					cellValue=ReportImpl.getCellValue(conn,reportIn.getRepInId().intValue(),
							reportIn.getChildRepId(),
							reportIn.getVersionId(),
							cellName.substring(1,cellName.length()-1));
				}
				//cellFormu=cellFormu.substring(0,posS) + cellValue + cellFormu.substring(posE+1);
				cellFormu=cellFormu.substring(0,posS) + 
					(cellValue.indexOf("-")==0?"(" + String.valueOf(cellValue) + ")":String.valueOf(cellValue)) +  
					cellFormu.substring(posE+1);
				posS=cellFormu.indexOf(Expression.SPLIT_SYMBOL_LEFT_MID_KUOHU);
				posE=cellFormu.indexOf(Expression.SPLIT_SYMBOL_RIGHT_MID_HUOHU);
			}
		}
		
		formu=cellFormu;
		
		return formu;
	}
}
