package com.fitech.gznx.entity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;


import com.fitech.gznx.entity.SerialByColumn;
import com.fitech.gznx.util.FitechUtil;
import com.fitech.gznx.util.DateUtil;

/**
 * 
 * 类说明:该类定义一个用于图形展示用的Bean,对应一张图片
 * 
 * @author jack
 * @date 2009-12-11
 */
public class GraphBean {

	/**
	 * 表
	 */
	public static String TABLEPATH = "0";
	/**
	 * 柱状图
	 */
	public static String COLUMNGRAPH = "1";

	/**
	 * 饼状图
	 */
	public static String PIEGRAPH = "2";

	/**
	 * 折线图
	 */
	public static String BREAKLINE = "3";

	/**
	 * 指标表名
	 */
	public static String MEASURE_ALERT = "measure_alert";
 
	/**
	 * 科目表名
	 */
	public static String SUBJECT_TOTAL = "subject_total";

	/**
	 * 图片信息在XML文件中的配置ID
	 */
	private String graphId;

	/**
	 * 图片生成文件的名称
	 */
	private String graphStoreName;

	/**
	 * 图片所属类型
	 */
	private String graphType;

	/**
	 * 图形大标题
	 */
	private String graphTitle;

	/**
	 * 图片的宽度
	 */
	private int graphWidth;

	/**
	 * 图片的高度
	 */
	private int graphHeigth;

	/**
	 * 是否是3D
	 */
	private String graphSolid;

	/**
	 * 横坐标名称
	 */
	private String alabelTitle;

	/**
	 * 横坐标单位
	 */
	private String alableUnit;

	/**
	 * 纵坐标名称
	 */
	private String vlabelTitle;

	/**
	 * 纵坐标单位
	 */
	private String vlabelUnit;

	/**
	 * 业务分类列表(内部装载的是业务分类数组)
	 */
	private List itemList;

	/**
	 * 时间列表
	 */
	private List repDateList;

	/**
	 * 参数列表,里面存放的对象因不同的图形而不同 饼状图:里面存放的是一个长度为3的字符串数组(分类标签,分类值,baseSql)
	 * 折线图:里面存放的是一个BreakLineItem列表 柱状图:里面存放的时SerialByColumn列表
	 * 
	 */
	private List paramList;

	/**
	 * 当按机构分序列时是否包含本行机构
	 */
	private String isContailMe;

	/**
	 * 分类数量
	 */
	private int typeCount = 0;
	
	/**
	 * 表的参数
	 */
	public  static String[] argumentNames = null;
	/**
	 * 表的参数的值
	 */
	public  static String[] argumentValues = null;

	public int getGraphHeigth() {
		return graphHeigth;
	}

	public void setGraphHeigth(int graphHeigth) {
		this.graphHeigth = graphHeigth;
	}

	public int getGraphWidth() {
		return graphWidth;
	}

	public void setGraphWidth(int graphWidth) {
		this.graphWidth = graphWidth;
	}

	/**
	 * 
	 * 方法说明:该方法用于处理时间
	 * 
	 * @author jack
	 * @date 2009-12-10
	 * @param repDate
	 */
	public void conductRepDate(String repDate) {

		this.repDateList = null;

		this.repDateList = new ArrayList();

		if (this.graphType.equals(GraphBean.PIEGRAPH)
				|| this.graphType.equals(GraphBean.COLUMNGRAPH)) // 如果是饼状图则直接将时间放入列表中的第一个位置

			this.repDateList.add(repDate);

		if (this.graphType.equals(GraphBean.BREAKLINE)) {// 如果时折线图将经过处理生成一个时间列表

			String startDate = "";

			String endDate = "";

			if (DateUtil.isMonthDay(repDate)) { // 如果输入的时间时月末时间

				startDate = DateUtil.getLYSameDay(repDate);

				endDate = repDate;

				this.repDateList = DateUtil.getBetweenDate(startDate, endDate);

				this.repDateList.add(endDate);

			} else {

				endDate = DateUtil.getLastMonth(repDate);

				startDate = DateUtil.getLYSameDay(endDate);

				this.repDateList = DateUtil.getBetweenDate(startDate, endDate);

				this.repDateList.add(endDate);
			}

		}

	}

	/**
	 * 
	 * 方法说明:该方法用于将时间列表转换成一个用都后分割的字符串
	 * 
	 * @author jack
	 * @date 2009-12-10
	 * @return
	 */
	public String getRepDates() {

		StringBuffer sb = new StringBuffer("");

		String result = null;

		if (this.repDateList != null && this.repDateList.size() != 0) {

			sb.append((String) this.repDateList.get(0) + "'");

			for (int i = 1; i < this.repDateList.size(); i++) {

				if (i != this.repDateList.size() - 1)

					sb.append("," + "'" + this.repDateList.get(i) + "'");

				else

					sb.append("," + "'" + this.repDateList.get(i));
			}

			result = sb.toString();

		} else

			result = "'1906-12-31'";

		return result;

	}

	/**
	 * 构造方法
	 * 
	 * @param element
	 * @param orgId
	 * @param ccyId
	 * @param repDate
	 */
	public GraphBean(Element element, String orgId, String orgName,
			String ccyId, String repDate) {

		this.paramList = new ArrayList(); // 初始化参数列表

		this.graphId = element.attributeValue("id");

		this.graphType = element.attributeValue("type");

		this.graphSolid = element.attributeValue("solid");

		this.graphTitle = element.attributeValue("title");

		try {

			this.graphWidth = Integer.parseInt(element.attributeValue("width")); // 设置宽度

		} catch (Exception e) {

			this.graphWidth = 350;
		}
		try {

			this.graphHeigth = Integer.parseInt(element
					.attributeValue("height"));

		} catch (Exception e) {

			this.graphHeigth = 300;
		}

		this.conductRepDate(repDate); // 生成时间列表
		
		if(this.graphType.equals(GraphBean.TABLEPATH)) //如果是表
			
			this.initTable(element);

		if (this.graphType.equals(GraphBean.PIEGRAPH)) // 如果是饼状图

			this.initPie(element, orgId, ccyId, repDate); // 解析XML信息

		if (this.graphType.equals(GraphBean.BREAKLINE)) // 如果是折线图

			this.initBeakLine(element, orgId, ccyId, repDate); // 解析折线图

		if (this.graphType.equals(GraphBean.COLUMNGRAPH))

			this.initColumn(element, orgId, orgName, ccyId, repDate); // 解析柱状图

	}

	/**
	 * 
	 * 方法说明:该方法用于对饼状图进行解析初始化
	 * 
	 * @author jack
	 * @date 2009-12-10
	 * @param Element
	 */
	public void initPie(Element element, String orgId, String ccyId,
			String repDate) {

		this.paramList = null;

		this.paramList = new ArrayList();

		List list = element.selectNodes("dataset/item");

		if (list != null && list.size() != 0) {

			for (int i = 0; i < list.size(); i++) {

				Element e = (Element) list.get(i);

				String datasetBaseSql = e.attributeValue("sql"); // 得到XML文件中的查询SQL

				String[] params = new String[3];

				params[0] = e.attributeValue("label");

				params[1] = "0";

				params[2] = datasetBaseSql;

				this.paramList.add(params);

			}
			this.conductPie(orgId, ccyId); // 查询取数
		}

	}

	/**
	 * 
	 * 方法说明:该方法用于对折线图进行解析初始化
	 * 
	 * @author jack
	 * @date 2009-12-10
	 * @param element
	 * @param orgId
	 * @param ccyId
	 * @param repDate
	 */
	public void initBeakLine(Element element, String orgId, String ccyId,
			String repDate) {

		List list = element.selectNodes("alabel");

		String str = ((Element) list.get(0)).attributeValue("title");

		this.setAlabelTitle(str);

		list = element.selectNodes("vlabel");

		str = ((Element) list.get(0)).attributeValue("title");

		this.setVlabelTitle(str);

		this.paramList = null;

		this.paramList = new ArrayList();

		list = element.selectNodes("dataset/item");

		if (list != null && list.size() != 0) {

			for (int i = 0; i < list.size(); i++) {

				Element e = (Element) list.get(i);

				String datasetBaseSql = e.attributeValue("sql"); // 得到XML文件中的查询SQL

				BreakLineItem breakLineItem = new BreakLineItem();

				breakLineItem.setBaseSql(datasetBaseSql);

				breakLineItem.setLabel(e.attributeValue("label"));

				this.paramList.add(breakLineItem);

			}
			this.conductBreakLine(orgId, ccyId); // 查询取数
		}
	}

	/**
	 * 
	 * 方法说明:该方法用于柱状图的初始化
	 * 
	 * @author jack
	 * @date 2009-12-13
	 * @param element
	 * @param orgId
	 * @param ccyId
	 * @param repDate
	 */
	public void initColumn(Element element, String orgId, String orgName,
			String ccyId, String repDate) {

		List list = element.selectNodes("vlabel");

		String str = ((Element) list.get(0)).attributeValue("title");

		this.setVlabelTitle(str);

		list = element.selectNodes("dataset");

		str = ((Element) list.get(0)).attributeValue("isContailMe");

		this.setIsContailMe(str);

		this.paramList = null;

		this.paramList = new ArrayList();

		if (this.isContailMe.equals("y")) {

			SerialByColumn sbc = new SerialByColumn();

			sbc.setLabel(orgName);

			sbc.setValue(orgId);

			this.paramList.add(sbc);

		}

		list = element.selectNodes("dataset/serial");

		if (list != null && list.size() != 0) {

			for (int i = 0; i < list.size(); i++) {

				Element e = (Element) list.get(i);

				SerialByColumn sbc = new SerialByColumn();

				sbc.setLabel(e.attributeValue("label"));

				sbc.setLabel(e.attributeValue("value"));

				this.paramList.add(sbc);
			}
		}

		list = element.selectNodes("dataset/item");

		this.typeCount = list.size(); // 初始化分类

		if (list != null && list.size() != 0) {

			for (int i = 0; i < this.paramList.size(); i++) {

				Map map = new HashMap();

				List lis = new ArrayList();

				for (int j = 0; j < list.size(); j++) {

					Element e = (Element) list.get(j);

					String[] strs = new String[2];

					strs[0] = e.attributeValue("sql");

					strs[1] = e.attributeValue("0");

					map.put(e.attributeValue("label").toString(), strs);

					lis.add(e.attributeValue("label").toString());

					System.out.println((String) lis.get(j));
				}

				SerialByColumn sbc = (SerialByColumn) this.paramList.get(i);

				sbc.setMap(map);

				sbc.setList(lis);

			}

			this.conductColumn(orgId, ccyId); // 查询取数
		}

	}

	/**
	 * 
	 * 方法说明:该方法用于处理饼图查询
	 * 
	 * @author jack
	 * @date 2009-12-7
	 * @param orgId
	 * @param ccyId
	 */
	public void conductPie(String orgId, String ccyId) {

		for (int i = 0; i < this.paramList.size(); i++) {

			String[] params = (String[]) this.paramList.get(i);

			String sql = this.getSql(params[2], orgId, ccyId,
					(String) this.repDateList.get(0));

			List tmp = FitechUtil.jdbcQueryUtil(sql);

			if (tmp != null && tmp.size() != 0) {

				params[1] = ((Double) tmp.get(0)).toString(); // 更新数

			}

			else

				params[1] = "0"; // 如果查不到数或系统

		}

	}

	/**
	 * 
	 * 方法说明:该方法用于折线图的查询
	 * 
	 * @author jack
	 * @date 2009-12-13
	 * @param orgId
	 * @param ccyId
	 */
	public void conductBreakLine(String orgId, String ccyId) {

		for (int i = 0; i < this.paramList.size(); i++) {

			BreakLineItem item = (BreakLineItem) paramList.get(i);

			String sql = this.getSql(item.getBaseSql(), orgId, ccyId, this
					.getRepDates());

			// System.out.println(sql);

			item.setMap(FitechUtil.jdbcQueryUtilOfMap(sql));

		}
	}

	/**
	 * 
	 * 方法说明:该方法用于
	 * 
	 * @author jack
	 * @date 2009-12-14
	 * @param orgId
	 * @param ccyId
	 */
	public void conductColumn(String orgId, String ccyId) {

		for (int i = 0; i < this.paramList.size(); i++) {

			SerialByColumn sbc = (SerialByColumn) this.paramList.get(i);

			Map map = sbc.getMap();

			Iterator iterator = map.keySet().iterator();

			Map hm = new HashMap();

			while (iterator.hasNext()) {

				String label = (String) iterator.next();

				String[] strs = (String[]) map.get(label);

				String sql = this.getSql(strs[0], orgId, ccyId,
						(String) this.repDateList.get(0));

				List tmp = FitechUtil.jdbcQueryUtil(sql);

				if (tmp != null && tmp.size() != 0) {

					strs[1] = ((Double) tmp.get(0)).toString(); // 更新数

				}

				else

					strs[1] = "0"; // 如果查不到数或系统

				hm.put(label, strs); // 插入新map

			}
			sbc.setMap(hm); // 将新map插入
		}
	}
	
	/**
	 * 
	 * @param element
	 * @param orgId
	 * @param orgName
	 * @param ccyId
	 * @param repDate
	 */
	public void initTable(Element element) {

		List list = element.selectNodes("dataset");

		Element items = (Element) list.get(0);

		list = items.selectNodes("item");
		
		argumentNames = new String[list.size()];
		
		argumentValues = new String[list.size()];

		if (list != null && list.size() != 0) {

			for (int i = 0; i < list.size(); i++) {

				Element e = (Element) list.get(i);

				String argumentName = e.attributeValue("label");
				
				argumentNames[i] = argumentName; 

				String argumentValue = e.attributeValue("value");
				
				argumentValues[i] = argumentValue;

			}
			
			
		}


	}


	/**
	 * 
	 * 方法说明:该方法用于统一处理SQL的组装(降低复杂度),此方法待用
	 * 
	 * @author jack
	 * @date 2009-12-7
	 * @param datasetBaseSql
	 * @param orgId
	 * @param ccyId
	 * @param repDateList
	 * @return
	 */
	public String getSql(String datasetBaseSql, String orgId, String ccyId,
			String repDate) {

		String sql = datasetBaseSql;

		sql = sql.replaceAll("@OrgID@", "'" + orgId + "'");

//		sql = sql.replaceAll("@ccyId@", "'" + ccyId + "'");

		sql = sql.replaceAll("@ReptDate@", "'" + repDate.replaceAll("-", "") + "'");

		return sql.toString();
	}

	/**
	 * 
	 * 方法说明:该方法用于对各种图形进行调度处理
	 * 
	 * @author jack
	 * @date 2009-12-13
	 * @param orgId
	 * @param ccyId
	 */
	public void conductGraphBean(String orgId, String ccyId, String repDate) {

		this.conductRepDate(repDate);

		if (this.graphType.equals(GraphBean.PIEGRAPH)) // 如果是饼状图

			this.conductPie(orgId, ccyId); // 装配SQL语并查询数据库得到结果

		if (this.graphType.equals(GraphBean.BREAKLINE))

			this.conductBreakLine(orgId, ccyId);
		
		if(this.graphType.equals(GraphBean.COLUMNGRAPH))
			
			this.conductColumn(orgId,ccyId);

	}

	public static String getBREAKLINE() {
		return BREAKLINE;
	}

	public static void setBREAKLINE(String breakline) {
		BREAKLINE = breakline;
	}

	public static String getCOLUMNGRAPH() {
		return COLUMNGRAPH;
	}

	public static void setCOLUMNGRAPH(String columngraph) {
		COLUMNGRAPH = columngraph;
	}

	public static String getPIEGRAPH() {
		return PIEGRAPH;
	}

	public static void setPIEGRAPH(String piegraph) {
		PIEGRAPH = piegraph;
	}

	public String getAlabelTitle() {
		return alabelTitle;
	}

	public void setAlabelTitle(String alabelTitle) {
		this.alabelTitle = alabelTitle;
	}

	public String getAlableUnit() {
		return alableUnit;
	}

	public void setAlableUnit(String alableUnit) {
		this.alableUnit = alableUnit;
	}

	public String getGraphId() {
		return graphId;
	}

	public void setGraphId(String graphId) {
		this.graphId = graphId;
	}

	public String getGraphSolid() {
		return graphSolid;
	}

	public void setGraphSolid(String graphSolid) {
		this.graphSolid = graphSolid;
	}

	public String getGraphTitle() {
		return graphTitle;
	}

	public void setGraphTitle(String graphTitle) {
		this.graphTitle = graphTitle;
	}

	public String getGraphType() {
		return graphType;
	}

	public void setGraphType(String graphType) {
		this.graphType = graphType;
	}

	public String getVlabelTitle() {
		return vlabelTitle;
	}

	public void setVlabelTitle(String vlabelTitle) {
		this.vlabelTitle = vlabelTitle;
	}

	public String getVlabelUnit() {
		return vlabelUnit;
	}

	public void setVlabelUnit(String vlabelUnit) {
		this.vlabelUnit = vlabelUnit;
	}

	public String getGraphStoreName() {
		return graphStoreName;
	}

	public void setGraphStoreName(String graphStoreName) {
		this.graphStoreName = graphStoreName;
	}

	public List getItemList() {
		return itemList;
	}

	public void setItemList(List itemList) {
		this.itemList = itemList;
	}

	public List getRepDateList() {
		return repDateList;
	}

	public void setRepDateList(List repDateList) {
		this.repDateList = repDateList;
	}

	public List getParamList() {
		return paramList;
	}

	public void setParamList(List paramList) {
		this.paramList = paramList;
	}

	public String getIsContailMe() {
		return isContailMe;
	}

	public void setIsContailMe(String isContailMe) {
		this.isContailMe = isContailMe;
	}

	public int getTypeCount() {
		return typeCount;
	}

	public void setTypeCount(int typeCount) {
		this.typeCount = typeCount;
	}

	public String[] getArgumentNames() {
		return argumentNames;
	}

	public void setArgumentNames(String[] argumentNames) {
		this.argumentNames = argumentNames;
	}

}
