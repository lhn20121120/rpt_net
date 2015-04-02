package com.fitech.gznx.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fitech.gznx.common.Config;
import com.fitech.gznx.security.OperatorLead;
import com.fitech.gznx.util.DateUtil;
import com.fitech.gznx.util.FitechUtil;

/**
 * 该类用于获取图形数据
 * 
 * @author jack
 * 
 */
public class GraphDataSource
{
	public String naviGraph ="";
	public String naviGraphName="";
	/**
	 * 时间列表
	 */
	public List repDateList = new ArrayList();

	public String orgId = "";

	public String orgName = "";
	
	public String orgList="";

	public String orgPurview = "";

	public String freq = String.valueOf(Config.FREQ_DAY.intValue());

	public String orgLevel = "";

	public String ccyId = "999";

	public String reptDate = "2006-12-31";

	public List dataList = new ArrayList();
	
	public String contextP = null ;

	public GraphDataSource(OperatorLead operator,String naviGraph)
	{
		this.naviGraph = naviGraph;
		this.orgId = operator.getOrgID();
		this.orgName = operator.getOrgName();
		this.contextP = operator.getContextP();
	}

	/**
	 * 方法说明:该方法用于处理饼图查询
	 * 
	 */
	public static void setPieData(GraphDataSource gs)
	{

		List list = gs.getDataList();

		for (int i = 0; i < list.size(); i++)
		{

			String[] params = (String[]) list.get(i);

			String sql = getSql(params[2], gs);

			List tmp = FitechUtil.jdbcQueryUtil(sql);

			if (tmp != null && tmp.size() != 0)
			{
				params[1] = ((Double) tmp.get(0)).toString(); // 更新数
			}
			else
				params[1] = "0"; // 如果查不到数或系统

		}

	}

	/**
	 * 方法说明:该方法用于折线图的查询
	 * 
	 */
	
	public static void setBreakLineData(GraphDataSource gs)
		{
	
			List list = gs.getDataList();
	
			String tempdate = gs.getReptDate();
	
//			conductRepDate(GraphFactory.BREAKLINE, gs);
	
			for (int i = 0; i < list.size(); i++)
			{
	
				BreakLineItem item = (BreakLineItem) list.get(i);
	
				gs.setReptDate(getRepDates(gs));
	
				String sql = getSql(item.getBaseSql(), gs);
	
				item.setMap(FitechUtil.jdbcQueryUtilOfMap(sql));
	
			}
	
			gs.setReptDate(tempdate);
		}
	

	/**
	 * 方法说明:该方法用于柱状图的查询
	 * 
	 */
	
	
	public static void setColumnData(GraphDataSource gs)
	{

		List datas = gs.getDataList();

		if (datas != null && datas.size() != 0)
		{

			String curOrgId = gs.getOrgId();
			System.out.println(datas.size());
			for (int i = 0; i < datas.size(); i++)
			{
				
				SerialByColumn sbc = (SerialByColumn) datas.get(i);

				Map map = sbc.getMap();

				gs.setOrgId(sbc.getValue());
System.out.println("GraphDataSource.java =="+gs.getOrgId());
				Iterator iterator = map.keySet().iterator();

				Map hm = new HashMap();

				while (iterator.hasNext())
				{

					String label = (String) iterator.next();

					String[] strs = (String[]) map.get(label);

					String sql = getSql(strs[0], gs);

					List tmp = FitechUtil.jdbcQueryUtil(sql,i);

					if (tmp != null && tmp.size() != 0)
					{
						
						strs[1] = ((Double) tmp.get(i)).toString(); // 更新数

					} 
  
					else

						strs[1] = "0"; // 如果查不到数或系统

					hm.put(label, strs); // 插入新map

				}

				sbc.setMap(hm); // 将新map插入

			}

			gs.setOrgId(curOrgId);

		}

	}
	

	/**
	 * 方法说明：给sql语句赋参数
	 * 
	 * @param datasetBaseSql
	 * @param reptDate
	 * @return
	 */
	public static String getSql(String datasetBaseSql, GraphDataSource gs)
	{

		String sql = datasetBaseSql;

		sql = sql.replaceAll("@OrgID@", "'" + gs.getOrgId() + "'");

//		sql = sql.replaceAll("@ccyId@", "'" + ccyId + "'");

		sql = sql.replaceAll("@ReptDate@", "'" +  gs.getReptDate().replaceAll("-", "") + "'");

		return sql.toString();
	}

	/**
	 * 方法说明:该方法用于根据不同的图形类型处理时间并将其赋值给repDateList
	 * 
	 * @param graphType
	 */
	

	public static void conductRepDate(String graphType, GraphDataSource gs)
	{

		gs.setRepDateList(new ArrayList());

		if (graphType.equals(GraphFactory.PIE) || graphType.equals(GraphFactory.COLUMN)) // 如果是饼状图则直接将时间放入列表中的第一个位置

			gs.getRepDateList().add(gs.getReptDate());
		
		if (graphType.equals(GraphFactory.BREAKLINE))
		{// 如果时折线图将经过处理生成一个时间列表

			String startDate = "";

			String endDate = "";
			String naviGraph = gs.getNaviGraph();
			String tempdate = gs.getReptDate();
			if(naviGraph.equals("liudongxingqingkuang"))
			{	
				startDate = DateUtil.dateAdd(tempdate,-29);
				endDate = tempdate;
				gs.setRepDateList(DateUtil.getDateList(startDate, endDate));
			}
			else
			{
				if (DateUtil.isMonthDay(tempdate))
				{ // 如果输入的时间时月末时间
					if(naviGraph.equals("yingliqingkuang"))
					{
						startDate = DateUtil.getLastYear(tempdate);
					}
						
					else
						startDate = DateUtil.getLYSameDay(tempdate);
					endDate = tempdate;

					gs.setRepDateList(DateUtil.getBetweenDate(startDate, endDate));
					gs.getRepDateList().add(endDate);

				}
				else
				{

					endDate = DateUtil.getLastMonth(tempdate);

					startDate = DateUtil.getLYSameDay(endDate);

					gs.setRepDateList(DateUtil.getBetweenDate(startDate, endDate));

					gs.getRepDateList().add(endDate);
				}
			}
			
		}
	}
	
	

	/**
	 * 方法说明:该方法用于将repDateList中的时间转换成一个用逗号分割的字符串
	 * 
	 * @return
	 */
	public static String getRepDates(GraphDataSource gs)
	{

		StringBuffer sb = new StringBuffer("");

		String result = null;

		List tempDateList = gs.getRepDateList();

		if (tempDateList != null && tempDateList.size() != 0)
		{

			sb.append((String) tempDateList.get(0) + "'");

			for (int i = 1; i < tempDateList.size(); i++)
			{

				if (i != tempDateList.size() - 1)

					sb.append("," + "'" + tempDateList.get(i) + "'");

				else

					sb.append("," + "'" + tempDateList.get(i));
			}

			result = sb.toString();

		}
		else

			result = gs.getReptDate();

		return result;

	}

	public String getCcyId()
	{
		return ccyId;
	}

	public void setCcyId(String ccyId)
	{
		this.ccyId = ccyId;
	}

	public List getDataList()
	{
		return dataList;
	}

	public void setDataList(List dataList)
	{
		this.dataList = dataList;
	}

	

	public String getFreq()
	{
		return freq;
	}

	public void setFreq(String freq)
	{
		this.freq = freq;
	}

	public String getOrgId()
	{
		return orgId;
	}

	public void setOrgId(String orgId)
	{
		this.orgId = orgId;
	}

	public String getOrgLevel()
	{
		return orgLevel;
	}

	public void setOrgLevel(String orgLevel)
	{
		this.orgLevel = orgLevel;
	}

	public String getOrgName()
	{
		return orgName;
	}

	public void setOrgName(String orgName)
	{
		this.orgName = orgName;
	}

	public String getOrgPurview()
	{
		return orgPurview;
	}

	public void setOrgPurview(String orgPurview)
	{
		this.orgPurview = orgPurview;
	}

	public List getRepDateList()
	{
		return repDateList;
	}

	public void setRepDateList(List repDateList)
	{
		this.repDateList = repDateList;
	}

	public String getReptDate()
	{
		return reptDate;
	}

	public void setReptDate(String reptDate)
	{
		this.reptDate = reptDate;
	}

	public String getNaviGraph()
	{
		return naviGraph;
	}

	public void setNaviGraph(String naviGraph)
	{
		this.naviGraph = naviGraph;
	}

	public String getNaviGraphName()
	{
		return naviGraphName;
	}

	public void setNaviGraphName(String naviGraphName)
	{
		this.naviGraphName = naviGraphName;
	}

	public String getContextP() {
		return contextP;
	}

	public void setContextP(String contextP) {
		this.contextP = contextP;
	}

	public String getOrgList() {
		return orgList;
	}

	public void setOrgList(String orgList) {
		this.orgList = orgList;
	}

	
}
