package com.fitech.dataCollect;

import java.util.ArrayList;
import java.util.List;

import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.util.FitechException;
/***
 * ���ⱨ������
 * @author Yao
 *
 */
public class CollectSpecialReport {
	private static FitechException log = new FitechException();
	private String[] specialReport = {"G1301","G1302","G1303","G1304","G1401","G1402","G1500","G2300","G2400","S4400"};
	
	/**�������ⱨ��*/
	public List getCollect_specialReport(String childRepId,String repInIds)
	{
		// System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
		// System.out.println("�ӱ���Id������"+childRepId);
		List list = null;
		/**���ݲ�ͬ�ı���������ͬ������*/
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
	
	/**����G13ϵ�б������,���ʮ��*/
	private List G13_Collect(String repInIds)
	{
		/**�ñ�Ӧ�ôӵڼ��е��ڼ���ȡ��һ�е�ǰ����λ��ֵ*/
		/**pdf����Ч���Ǹ���ʼ����6*/
		int START_ROW = 6;	
		/**��10λ���ڵ�����15*/
		int END_ROW = 15;
		/**ȡ���ʮ��*/
		int TOP = 10;
		/**�ο�ֵ�ǵ�D�е�ֵ*/
		String COL_ID ="D";
		/**�ϼ���λ��*/
		int SUM_ROW_ID =16;
		/**��Ҫ�ϼƵ���*/
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
			/***������ǰ10λ����������Ӧ���ű������һ��*/
			Query query = session.createQuery(hql);
			//query.setFetchSize(TOP);
			List list = query.list();
			
			if(list==null || list.size()==0)
				return null;
			
			/**��ʼ��*/
			int row = START_ROW;
			
			for(int i=0; i<list.size();i++)
			{
				Object[] item = (Object[])list.get(i);
				Integer repInId = (Integer)item[0];
				Integer rowId = (Integer)item[1];
				/**ȡ���⼸�ű����Ӧ��ȡ����**/
				hql = "select r.MCell.colId,r.reportValue,r.MCell.dataType from ReportInInfo r where r.comp_id.repInId="+repInId
						+" and r.MCell.rowId="+rowId
						+" order by r.MCell.colId";
				List valueList = session.createQuery(hql).list();
				
				if(valueList==null||valueList.size()==0)
					continue;
				
				
				/**Ϊ��Щֵ�����趨�к�*/
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
			 * ����ϼ���
			 * */
			
			/**�ϼ�������*/
			P2PRep_Data[] sumDatas = new P2PRep_Data[NEED_SUM_COLS.length];
			/**��ʼ���úϼ�������*/
			for(int i=0; i<sumDatas.length; i++)
			{
				sumDatas[i] = new P2PRep_Data();
				sumDatas[i].setRowId(Integer.valueOf(String.valueOf(SUM_ROW_ID)));
				sumDatas[i].setColId(NEED_SUM_COLS[i]);
				sumDatas[i].setValue("0");
			}
			
			/**��������ϼ����ֵ**/
			for(int i=0 ;i<reportDataList.size() ; i++)
			{
				
				P2PRep_Data data  = (P2PRep_Data)reportDataList.get(i);
				
				String colId = data.getColId();
												
				/**�鿴�Ƿ�����Ҫ�ϼƵ���*/
				for(int j=0 ; j<sumDatas.length ;j++)
				{
					if(sumDatas[j].getColId().equalsIgnoreCase(colId))
					{
						if(Util.isDouble(data.getValue()))
						{
							/**��Ҫ�Ӻϼ�����ֵ*/
							double value = Double.parseDouble(data.getValue()); 
							/**�ϼ����е���ֵ*/
							double oldValue = Double.parseDouble(sumDatas[j].getValue());
							/**�ۼӸ���ֵ*/
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
	/**����G14�������*/
	private List G14_Collect(String repInIds)
	{		
		/**�ñ�Ӧ�ôӵڼ��е��ڼ���ȡ��һ�е�ǰ����λ��ֵ*/
		/**pdf����Ч���Ǹ���ʼ����6*/
		int START_ROW = 8;	
		/**��10λ���ڵ�����15*/
		int END_ROW = 17;
		/**ȡ���ʮ��*/
		int TOP = 10;
		/**�ο�ֵ�ǵ�D�е�ֵ*/
		String COL_ID ="E";
		/**�ϼ���λ��*/
		int SUM_ROW_ID =18;
		/**��Ҫ�ϼƵ���*/
		String[] NEED_SUM_COLS = {"F","G","H","I","J","K","L","M","N","O","P","Q","R"};
		/**��Ҫ���������*/
		String[] NEED_Persent_COLS={"G","Q"};
		/**�ʱ������к�*/
		int EQUITY_ROWID=12;
		/**�ʲ������к�*/
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
			/***������ǰ10λ����������Ӧ���ű������һ��*/
			Query query = session.createQuery(hql);
			//query.setFetchSize(TOP);
			List list = query.list();
			
			if(list==null || list.size()==0)
				return null;
			
			/**��ʼ��*/
			int row = START_ROW;
			
			for(int i=0; i<list.size();i++)
			{
				Object[] item = (Object[])list.get(i);
				Integer repInId = (Integer)item[0];
				Integer rowId = (Integer)item[1];
				/**ȡ���⼸�ű����Ӧ��ȡ����**/
				hql = "select r.MCell.colId,r.reportValue,r.MCell.dataType from ReportInInfo r where r.comp_id.repInId="+repInId
						+" and r.MCell.rowId="+rowId
						+" order by r.MCell.colId";
				List valueList = session.createQuery(hql).list();
				
				if(valueList==null||valueList.size()==0)
					continue;
				
				
				/**Ϊ��Щֵ�����趨�к�*/
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
			 * ����ϼ���
			 * */
			
			/**�ϼ�������*/
			P2PRep_Data[] sumDatas = new P2PRep_Data[NEED_SUM_COLS.length];
			/**��ʼ���úϼ�������*/
			for(int i=0; i<sumDatas.length; i++)
			{
				sumDatas[i] = new P2PRep_Data();
				sumDatas[i].setRowId(Integer.valueOf(String.valueOf(SUM_ROW_ID)));
				sumDatas[i].setColId(NEED_SUM_COLS[i]);
				sumDatas[i].setValue("0");
			}
			
			/**��������ϼ����ֵ**/
			for(int i=0 ;i<reportDataList.size() ; i++)
			{
				
				P2PRep_Data data  = (P2PRep_Data)reportDataList.get(i);
				
				String colId = data.getColId();
												
				/**�鿴�Ƿ�����Ҫ�ϼƵ���*/
				for(int j=0 ; j<sumDatas.length ;j++)
				{
					if(sumDatas[j].getColId().equalsIgnoreCase(colId))
					{
						if(Util.isDouble(data.getValue()))
						{
							/**��Ҫ�Ӻϼ�����ֵ*/
							double value = Double.parseDouble(data.getValue()); 
							/**�ϼ����е���ֵ*/
							double oldValue = Double.parseDouble(sumDatas[j].getValue());
							/**�ۼӸ���ֵ*/
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
	/**����G1500�������*/
	private List G1500_Collect(String repInIds)
	{
		List reportData = null;
		return reportData;
		
	}
	/**����G2300�������*/
	private List G2300_Collect(String repInIds)
	{
		List reportData = null;
		return reportData;
		
	}
	/**����G2400�������*/
	private List G2400_Collect(String repInIds)
	{
		List reportData = null;
		return reportData;
		
	}
	/**����S4400�������*/
	private List S4400_Collect(String repInIds)
	{
		List reportData = null;
		return reportData;
		
	}
}
