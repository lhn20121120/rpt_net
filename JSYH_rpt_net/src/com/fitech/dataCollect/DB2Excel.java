package com.fitech.dataCollect;

import java.util.List;

import com.cbrc.smis.security.Operator;

public class DB2Excel
{
	private DB2ExcelHandler handler = new DB2ExcelHandler();
	/**
	 * 取得该月份应该有那些报表上报
	 * @param int 月份
	 * @return List 该月份应该报的报表集合 List中每个元素是MActuRepForm对象(包含子报表id，版本号，数据范围id，频度id)
	 * */
	public List getThisMonthReport(int month)
	{
		List list = null;
		/**判断该月份是否有效*/
		if(month>12 && month<1){			
			return null;		
		}
		
		/**该月应该有哪几种频度报表报送*/
		String rep_freq = "";
		if(month == 12){			
			rep_freq = "('月','季','半年','年')";
		}else if(month == 6){
			rep_freq = "('月','季','半年')";			
		}else if(month == 3 || month == 9){			
			rep_freq = "('月','季')";
		}else {
			rep_freq = "('月')";
		}
			
		
		/**取得该月频度所对应的频度id串*/
		String repFreqIds = handler.getRepFreqID_from_freqName(rep_freq);
		if(repFreqIds==null || repFreqIds.equals("")){			
			return null;
		}
		// System.out.println("repFreqIds is "+repFreqIds);
		/**根据该月所对应的频度，取得该月份所要报送的报表*/
		list = handler.getReport_from_repFreq(repFreqIds);
		
		return list;
	}
	
	public List getThisMonthReport(int month,int year,Operator operator)
	{
		List list = null;
		/**判断该月份是否有效*/
		if(month>12 && month<1){			
			return null;		
		}
		
		/**该月应该有哪几种频度报表报送*/
		String rep_freq = "";
		if(month == 12){			
			rep_freq = "('月','季','半年','年')";
		}else if(month == 6){
			rep_freq = "('月','季','半年')";			
		}else if(month == 3 || month == 9){			
			rep_freq = "('月','季')";
		}else {
			rep_freq = "('月')";
		}
			
		
		/**取得该月频度所对应的频度id串*/
		String repFreqIds = handler.getRepFreqID_from_freqName(rep_freq);
		if(repFreqIds==null || repFreqIds.equals("")){			
			return null;
		}
		// System.out.println("repFreqIds is "+repFreqIds);
		/**根据该月所对应的频度，取得该月份所要报送的报表*/
		list = handler.getReport_from_repFreq(repFreqIds,year,month,operator);
		
		return list;
	}
//	/***
//	 * 强行生成汇总文件，单个报表
//	 * @param childRepId 子报表id
//	 * @param versionId 版本号
//	 * @param childRepName 报表名称
//	 * @param dataRangeId 数据范围id
//	 * @param dataRangeDesc 数据范围描述
//	 * @param year 年份
//	 * @param month 月份
//	 * @param term 期数
//	 */
//	public boolean execute_DB2Excel_force(String childRepId,String versionId,String reportName,Integer dataRangeId,String dataRangeDesc,int year,int month)
//	{
//		MActuRepForm rep = new MActuRepForm();
//		rep.setChildRepId(childRepId);
//		rep.setVersionId(versionId);
//		rep.setReportName(reportName);
//		rep.setDataRangeId(dataRangeId);
//		rep.setDataRgDesc(dataRangeDesc);
//		
//		/**报表信息，写日志用*/
//		String reportInfo = "报表名: "+reportName+" 版本号："+versionId+
//				" 数据范围："+dataRangeDesc+" "+year+"年"+month+"月";
//		/**汇总该报表*/
//		Collect_Report collect_report = handler.collectReport(rep,year,month);
//		if(collect_report!=null)
//		{
//			try 
//			{
//				/**写汇总文件*/
//				boolean result = Collect_Tools.collecttoExcel(collect_report,String.valueOf(year),String.valueOf(month));
//				/**汇总失败*/
//				if(result==false)
//				{
//					/**写日志*/
//					log = reportInfo+"，生成汇总报表失败！";
//					FitechLog.writeLog(Config.LOG_SYSTEM_CREATESTORAGEXML,"系统",log);
//					return false;
//				}
//				/**汇总成功*/
//				else
//				{
//					/**写日志*/
//					log = reportInfo+"，生成汇总报表成功！";
//					FitechLog.writeLog(Config.LOG_SYSTEM_CREATESTORAGEXML,"系统",log);
//					return true;
//				}
//			} 
//			catch (Exception e) {
//				e.printStackTrace();
//				/**写日志*/
//				log = reportInfo+"，生成汇总报表失败！";
//				FitechLog.writeLog(Config.LOG_SYSTEM_CREATESTORAGEXML,"系统",log);
//				return false;
//			}
//		
//		}
//		else
//		{
//			/**写日志*/
//			log = reportInfo+"，无上报数据，不需要汇总！";
//			FitechLog.writeLog(Config.LOG_SYSTEM_CREATESTORAGEXML,"系统",log);
//			return false;
//		}
//		
//	}
}
