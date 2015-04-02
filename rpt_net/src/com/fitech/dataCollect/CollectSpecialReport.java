package com.fitech.dataCollect;

import java.util.ArrayList;
import java.util.List;

import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.util.FitechException;
/***
 * 特殊报表处理类
 * @author Yao
 *
 */
public class CollectSpecialReport {
	private static FitechException log = new FitechException();
	private String[] specialReport = {"G1301","G1302","G1303","G1304","G1401","G1402","G1500","G2300","G2400","S4400"};
	
	/**处理特殊报表*/
	public List getCollect_specialReport(String childRepId,String repInIds)
	{
		// System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
		// System.out.println("子报表Id＝＝＝"+childRepId);
		List list = null;
		/**根据不同的报表名处理不同不报表*/
		if(childRepId.equalsIgnoreCase("G1301")||childRepId.equalsIgnoreCase("G1302")
				||childRepId.equalsIgnoreCase("G1303")||childRepId.equalsIgnoreCase("G1304"))
		{
			list = this.G13_Collect(repInIds);
		}
		
		if(childRepId.equalsIgnoreCase("G1401") || childRepId.equalsIgnoreCase("G1402"))
			list = this.G14_Collect(repInIds);			
		
		if(childRepId.equalsIgnoreCase("G1500"))
			list = this.G1500_Collect(repInIds);
					
		if(childRepId.equalsIgnoreCase("G2300"))
			list = this.G2300_Collect(repInIds);
					
		if(childRepId.equalsIgnoreCase("G2400"))
			list =this.G2400_Collect(repInIds);
					
		if(childRepId.equalsIgnoreCase("S4400"))
			list =this.S4400_Collect(repInIds);
					
		return list;
		
	}
	
	/**处理G13系列报表汇总,最大十家*/
	private List G13_Collect(String repInIds)
	{
		/**该表应该从第几行到第几行取哪一列的前多少位的值*/
		/**pdf中有效的那个起始行是6*/
		int START_ROW = 6;	
		/**第10位所在的行是15*/
		int END_ROW = 15;
		/**取最大十家*/
		int TOP = 10;
		/**参考值是第D列的值*/
		String COL_ID ="D";
		/**合计行位置*/
		int SUM_ROW_ID =16;
		/**需要合计的列*/
		String[] NEED_SUM_COLS = {"D","E","F","H","I","J","K","L"};
		List reportDataList = new ArrayList();
		
		DBConn conn = null;
		Session session = null;
				
		String hql = "select r.comp_id.repInId,r.MCell.rowId "
					+"from ReportInInfo r where r.comp_id.repInId in("+repInIds+") "
					+"and r.MCell.rowId between "+START_ROW+" and "+END_ROW
					+" and r.MCell.colId='"+COL_ID+"' order by r.reportValue desc "
					+"FETCH FIRST "+TOP+" ROWS ONLY";
		try
		{
			conn = new DBConn();
			session = conn.openSession();
			/***搜索出前10位的数据所对应哪张报表的哪一行*/
			Query query = session.createQuery(hql);
			//query.setFetchSize(TOP);
			List list = query.list();
			
			if(list==null || list.size()==0)
				return null;
			
			/**起始行*/
			int row = START_ROW;
			
			for(int i=0; i<list.size();i++)
			{
				Object[] item = (Object[])list.get(i);
				Integer repInId = (Integer)item[0];
				Integer rowId = (Integer)item[1];
				/**取出这几张报表的应该取的行**/
				hql = "select r.MCell.colId,r.reportValue,r.MCell.dataType from ReportInInfo r where r.comp_id.repInId="+repInId
						+" and r.MCell.rowId="+rowId
						+" order by r.MCell.colId";
				List valueList = session.createQuery(hql).list();
				
				if(valueList==null||valueList.size()==0)
					continue;
				
				
				/**为这些值重新设定行号*/
				for(int j=0 ; j<valueList.size();j++)
				{
					Object[] values = (Object[])valueList.get(j);
					String colId = (String)values[0];
					String cellValue = (String)values[1];
					Integer dataType = (Integer)values[2];
					
					P2PRep_Data data = new P2PRep_Data();
					data.setRowId(Integer.valueOf((String.valueOf(row))));
					data.setColId(colId);
					data.setDataType(String.valueOf(dataType));
					data.setValue(cellValue);  
					reportDataList.add(data);
				}
				row++;
			}
			
			/**
			 * 处理合计项
			 * */
			
			/**合计项数组*/
			P2PRep_Data[] sumDatas = new P2PRep_Data[NEED_SUM_COLS.length];
			/**初始化该合计项数组*/
			for(int i=0; i<sumDatas.length; i++)
			{
				sumDatas[i] = new P2PRep_Data();
				sumDatas[i].setRowId(Integer.valueOf(String.valueOf(SUM_ROW_ID)));
				sumDatas[i].setColId(NEED_SUM_COLS[i]);
				sumDatas[i].setValue("0");
			}
			
			/**计算各个合计项的值**/
			for(int i=0 ;i<reportDataList.size() ; i++)
			{
				
				P2PRep_Data data  = (P2PRep_Data)reportDataList.get(i);
				
				String colId = data.getColId();
												
				/**查看是否是需要合计的列*/
				for(int j=0 ; j<sumDatas.length ;j++)
				{
					if(sumDatas[j].getColId().equalsIgnoreCase(colId))
					{
						if(Util.isDouble(data.getValue()))
						{
							/**需要加合计上数值*/
							double value = Double.parseDouble(data.getValue()); 
							/**合计项中的数值*/
							double oldValue = Double.parseDouble(sumDatas[j].getValue());
							/**累加该数值*/
							double newValue = oldValue + value;
							sumDatas[j].setValue(String.valueOf(newValue));
							break;
						}
					}			
				}
			}
			for(int i=0;i<sumDatas.length ;i++)
				reportDataList.add(sumDatas[i]);
		} 
		catch (Exception e) 
		{
			log.printStackTrace(e);
			reportDataList =null;
		} 
		finally {
			if (conn != null)
				conn.closeSession();
		}
		
		return reportDataList;
		
	}
	/**处理G14报表汇总*/
	private List G14_Collect(String repInIds)
	{		
		/**该表应该从第几行到第几行取哪一列的前多少位的值*/
		/**pdf中有效的那个起始行是6*/
		int START_ROW = 8;	
		/**第10位所在的行是15*/
		int END_ROW = 17;
		/**取最大十家*/
		int TOP = 10;
		/**参考值是第D列的值*/
		String COL_ID ="E";
		/**合计行位置*/
		int SUM_ROW_ID =18;
		/**需要合计的列*/
		String[] NEED_SUM_COLS = {"F","G","H","I","J","K","L","M","N","O","P","Q","R"};
		/**需要算比例的列*/
		String[] NEED_Persent_COLS={"G","Q"};
		/**资本净额行号*/
		int EQUITY_ROWID=12;
		/**资产净额列号*/
		String EQUITY_COLID="D";
		
		List reportDataList = new ArrayList();
		
		DBConn conn = null;
		Session session = null;
				
		String hql = "select r.comp_id.repInId,r.MCell.rowId "
					+"from ReportInInfo r where r.comp_id.repInId in("+repInIds+") "
					+"and r.MCell.rowId between "+START_ROW+" and "+END_ROW
					+" and r.MCell.colId='"+COL_ID+"' order by r.reportValue desc "
					+"FETCH FIRST "+TOP+" ROWS ONLY";
		
		try
		{
			conn = new DBConn();
			session = conn.openSession();
			/***搜索出前10位的数据所对应哪张报表的哪一行*/
			Query query = session.createQuery(hql);
			//query.setFetchSize(TOP);
			List list = query.list();
			
			if(list==null || list.size()==0)
				return null;
			
			/**起始行*/
			int row = START_ROW;
			
			for(int i=0; i<list.size();i++)
			{
				Object[] item = (Object[])list.get(i);
				Integer repInId = (Integer)item[0];
				Integer rowId = (Integer)item[1];
				/**取出这几张报表的应该取的行**/
				hql = "select r.MCell.colId,r.reportValue,r.MCell.dataType from ReportInInfo r where r.comp_id.repInId="+repInId
						+" and r.MCell.rowId="+rowId
						+" order by r.MCell.colId";
				List valueList = session.createQuery(hql).list();
				
				if(valueList==null||valueList.size()==0)
					continue;
				
				
				/**为这些值重新设定行号*/
				for(int j=0 ; j<valueList.size();j++)
				{
					Object[] values = (Object[])valueList.get(j);
					String colId = (String)values[0];
					String cellValue = (String)values[1];
					Integer dataType = (Integer)values[2];
					
					P2PRep_Data data = new P2PRep_Data();
					data.setRowId(Integer.valueOf((String.valueOf(row))));
					data.setColId(colId);
					data.setDataType(String.valueOf(dataType));
					data.setValue(cellValue);  
					reportDataList.add(data);
				}
				row++;
			}
			
			/**
			 * 处理合计项
			 * */
			
			/**合计项数组*/
			P2PRep_Data[] sumDatas = new P2PRep_Data[NEED_SUM_COLS.length];
			/**初始化该合计项数组*/
			for(int i=0; i<sumDatas.length; i++)
			{
				sumDatas[i] = new P2PRep_Data();
				sumDatas[i].setRowId(Integer.valueOf(String.valueOf(SUM_ROW_ID)));
				sumDatas[i].setColId(NEED_SUM_COLS[i]);
				sumDatas[i].setValue("0");
			}
			
			/**计算各个合计项的值**/
			for(int i=0 ;i<reportDataList.size() ; i++)
			{
				
				P2PRep_Data data  = (P2PRep_Data)reportDataList.get(i);
				
				String colId = data.getColId();
												
				/**查看是否是需要合计的列*/
				for(int j=0 ; j<sumDatas.length ;j++)
				{
					if(sumDatas[j].getColId().equalsIgnoreCase(colId))
					{
						if(Util.isDouble(data.getValue()))
						{
							/**需要加合计上数值*/
							double value = Double.parseDouble(data.getValue()); 
							/**合计项中的数值*/
							double oldValue = Double.parseDouble(sumDatas[j].getValue());
							/**累加该数值*/
							double newValue = oldValue + value;
							sumDatas[j].setValue(String.valueOf(newValue));
							break;
						}
					}			
				}
			}
			for(int i=0;i<sumDatas.length ;i++)
				reportDataList.add(sumDatas[i]);
		} 
		catch (Exception e) 
		{
			log.printStackTrace(e);
			reportDataList =null;
		} 
		finally {
			if (conn != null)
				conn.closeSession();
		}
		return reportDataList;
		
	}
	/**处理G1500报表汇总*/
	private List G1500_Collect(String repInIds)
	{
		List reportData = null;
		return reportData;
		
	}
	/**处理G2300报表汇总*/
	private List G2300_Collect(String repInIds)
	{
		List reportData = null;
		return reportData;
		
	}
	/**处理G2400报表汇总*/
	private List G2400_Collect(String repInIds)
	{
		List reportData = null;
		return reportData;
		
	}
	/**处理S4400报表汇总*/
	private List S4400_Collect(String repInIds)
	{
		List reportData = null;
		return reportData;
		
	}
}
