package com.fitech.dataCollect;

import java.util.List;

import com.cbrc.smis.security.Operator;

public class DB2Excel
{
	private DB2ExcelHandler handler = new DB2ExcelHandler();
	/**
	 * ȡ�ø��·�Ӧ������Щ�����ϱ�
	 * @param int �·�
	 * @return List ���·�Ӧ�ñ��ı����� List��ÿ��Ԫ����MActuRepForm����(�����ӱ���id���汾�ţ����ݷ�Χid��Ƶ��id)
	 * */
	public List getThisMonthReport(int month)
	{
		List list = null;
		/**�жϸ��·��Ƿ���Ч*/
		if(month>12 && month<1){			
			return null;		
		}
		
		/**����Ӧ�����ļ���Ƶ�ȱ�����*/
		String rep_freq = "";
		if(month == 12){			
			rep_freq = "('��','��','����','��')";
		}else if(month == 6){
			rep_freq = "('��','��','����')";			
		}else if(month == 3 || month == 9){			
			rep_freq = "('��','��')";
		}else {
			rep_freq = "('��')";
		}
			
		
		/**ȡ�ø���Ƶ������Ӧ��Ƶ��id��*/
		String repFreqIds = handler.getRepFreqID_from_freqName(rep_freq);
		if(repFreqIds==null || repFreqIds.equals("")){			
			return null;
		}
		// System.out.println("repFreqIds is "+repFreqIds);
		/**���ݸ�������Ӧ��Ƶ�ȣ�ȡ�ø��·���Ҫ���͵ı���*/
		list = handler.getReport_from_repFreq(repFreqIds);
		
		return list;
	}
	
	public List getThisMonthReport(int month,int year,Operator operator)
	{
		List list = null;
		/**�жϸ��·��Ƿ���Ч*/
		if(month>12 && month<1){			
			return null;		
		}
		
		/**����Ӧ�����ļ���Ƶ�ȱ�����*/
		String rep_freq = "";
		if(month == 12){			
			rep_freq = "('��','��','����','��')";
		}else if(month == 6){
			rep_freq = "('��','��','����')";			
		}else if(month == 3 || month == 9){			
			rep_freq = "('��','��')";
		}else {
			rep_freq = "('��')";
		}
			
		
		/**ȡ�ø���Ƶ������Ӧ��Ƶ��id��*/
		String repFreqIds = handler.getRepFreqID_from_freqName(rep_freq);
		if(repFreqIds==null || repFreqIds.equals("")){			
			return null;
		}
		// System.out.println("repFreqIds is "+repFreqIds);
		/**���ݸ�������Ӧ��Ƶ�ȣ�ȡ�ø��·���Ҫ���͵ı���*/
		list = handler.getReport_from_repFreq(repFreqIds,year,month,operator);
		
		return list;
	}
//	/***
//	 * ǿ�����ɻ����ļ�����������
//	 * @param childRepId �ӱ���id
//	 * @param versionId �汾��
//	 * @param childRepName ��������
//	 * @param dataRangeId ���ݷ�Χid
//	 * @param dataRangeDesc ���ݷ�Χ����
//	 * @param year ���
//	 * @param month �·�
//	 * @param term ����
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
//		/**������Ϣ��д��־��*/
//		String reportInfo = "������: "+reportName+" �汾�ţ�"+versionId+
//				" ���ݷ�Χ��"+dataRangeDesc+" "+year+"��"+month+"��";
//		/**���ܸñ���*/
//		Collect_Report collect_report = handler.collectReport(rep,year,month);
//		if(collect_report!=null)
//		{
//			try 
//			{
//				/**д�����ļ�*/
//				boolean result = Collect_Tools.collecttoExcel(collect_report,String.valueOf(year),String.valueOf(month));
//				/**����ʧ��*/
//				if(result==false)
//				{
//					/**д��־*/
//					log = reportInfo+"�����ɻ��ܱ���ʧ�ܣ�";
//					FitechLog.writeLog(Config.LOG_SYSTEM_CREATESTORAGEXML,"ϵͳ",log);
//					return false;
//				}
//				/**���ܳɹ�*/
//				else
//				{
//					/**д��־*/
//					log = reportInfo+"�����ɻ��ܱ���ɹ���";
//					FitechLog.writeLog(Config.LOG_SYSTEM_CREATESTORAGEXML,"ϵͳ",log);
//					return true;
//				}
//			} 
//			catch (Exception e) {
//				e.printStackTrace();
//				/**д��־*/
//				log = reportInfo+"�����ɻ��ܱ���ʧ�ܣ�";
//				FitechLog.writeLog(Config.LOG_SYSTEM_CREATESTORAGEXML,"ϵͳ",log);
//				return false;
//			}
//		
//		}
//		else
//		{
//			/**д��־*/
//			log = reportInfo+"�����ϱ����ݣ�����Ҫ���ܣ�";
//			FitechLog.writeLog(Config.LOG_SYSTEM_CREATESTORAGEXML,"ϵͳ",log);
//			return false;
//		}
//		
//	}
}
